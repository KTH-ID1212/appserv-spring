package se.kth.id1212.appserv.bank.domain;

import se.kth.id1212.appserv.bank.util.Util;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "ACCOUNT")
public class Account implements AccountDTO {
    private static final String SEQUENCE_NAME_KEY = "SEQ_NAME";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME_KEY)
    @SequenceGenerator(name = SEQUENCE_NAME_KEY, sequenceName = "BANK_SEQUENCE")
    @Column(name = "ACCT_ID")
    private long id;

    @PositiveOrZero(message = "{acct.balance.negative}")
    @Column(name = "ACCT_BALANCE")
    private int balance;

    @Column(name = "ACCT_NO")
    private long acctNo;

    @Version
    @Column(name = "ACCT_OPTLOCK_VERSION")
    private int optLockVersion;

    @NotNull(message = "{acct.holder.missing}")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
                          CascadeType.REFRESH,
                          CascadeType.DETACH}, optional = false)
    @JoinColumn(name = "FK_ACCOUNT_HOLDER")
    private Holder holder;

    /**
     * Behaves like {@link #Account(Holder, int)}, except that the balance is
     * set to zero.
     *
     * @param holder The account holder.
     */
    public Account(Holder holder) {
        this(holder, 0);
    }

    /**
     * <p>Creates a new instance with the specified holder and balance. Note
     * that an account always has exactly one holder. The newly created account
     * will <em>not</em> be passed to the specified holder, that must be done
     * after the constructor returns and the new instance is completely
     * created.</p>
     *
     * <p>A unique account number will be set on the newly created
     * instance.</p>
     *
     * @param holder  The account holder.
     * @param balance The initial balance.
     */
    public Account(Holder holder, int balance) {
        this.holder = holder;
        this.balance = balance;
        acctNo =
            BeanFactory.getBean(BusinessIdGenerator.class).generateAcctNo();
    }

    /**
     * Required by JPA, should not be used.
     */
    protected Account() {
    }

    @Override
    public int getbalance() {
        return balance;
    }

    @Override
    public long getAcctNo() {
        return acctNo;
    }

    @Override
    public Holder getHolder() {
        return holder;
    }

    /**
     * Withdraws the specified amount.
     *
     * @param amount The amount to withdraw.
     * @throws IllegalBankTransactionException When attempting to withdraw a
     *                                         negative or zero amount, or if
     *                                         withdrawal would result in a
     *                                         negative balance.
     */
    public void withdraw(int amount) throws IllegalBankTransactionException {
        if (amount <= 0) {
            throw new IllegalBankTransactionException(
                "Attempt to withdraw non-positive amount: " + amount);
        }
        if (amount > balance) {
            throw new IllegalBankTransactionException(
                "Overdraft attempt, balance: " + balance + ", amount: " +
                amount);
        }
        balance = balance - amount;
    }

    /**
     * Deposits the specified amount.
     *
     * @param amount The amount to deposit.
     * @throws IllegalBankTransactionException When attempting to deposit a
     *                                         negative or zero amount.
     */
    public void deposit(int amount) throws IllegalBankTransactionException {
        if (amount <= 0) {
            throw new IllegalBankTransactionException(
                "Attempt to deposit non-positive amount: " + amount);
        }
        balance = balance + amount;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(acctNo).hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account)object;
        return this.acctNo == other.acctNo;
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }
}
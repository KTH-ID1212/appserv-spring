package se.kth.id1212.appserv.bank.domain;

import se.kth.id1212.appserv.bank.util.Util;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "HOLDER")
public class Holder implements HolderDTO {
    private static final String SEQUENCE_NAME_KEY = "SEQ_NAME";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME_KEY)
    @SequenceGenerator(name = SEQUENCE_NAME_KEY, sequenceName = "BANK_SEQUENCE")
    @Column(name = "HLD_ID")
    private long id;

    @OneToMany(mappedBy = "holder", cascade = CascadeType.ALL)
    private Set<Account> accounts = new HashSet<>();

    @NotNull(message = "{holder.name.missing}")
    // The regex below should permit only characters, but asterisk is
    // unfortunately also valid.
    @Pattern(regexp = "^[\\p{L}\\p{M}*]*$", message = "{holder.name.invalid-char}")
    @Size(min = 2, max = 30, message = "{holder.name.length}")
    @Column(name = "HLD_NAME")
    private String name;

    @Column(name = "HLD_NO")
    private long holderNo;

    @Version
    @Column(name = "HLD_OPTLOCK_VERSION")
    private int optLockVersion;

    /**
     * Creates a new instance with the specified name. A unique holder number
     * will be set on the newly created instance.
     *
     * @param name The holder's name.
     */
    public Holder(String name) {
        this.name = name;
        holderNo =
            BeanFactory.getBean(BusinessIdGenerator.class).generateHolderNo();
    }

    /**
     * Required by JPA, should not be used.
     */
    protected Holder() {
    }

    /**
     * Returns a set containing all accounts owned by this account holder.
     */
    @Override
    public Set<AccountDTO> getAccounts() {
        Set<AccountDTO> copyOfAccts = new HashSet<>();
        copyOfAccts.addAll(accounts);
        return copyOfAccts;
    }

    /**
     * Adds the specified account to the set of accounts owned by this holder.
     * There is no limit on the number of accounts that can be owned by the same
     * holder.
     *
     * @param acct The account to add to this holder's accounts.
     */
    public void addAccount(Account acct) {
        accounts.add(acct);
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the account holder's name
     */
    public void setName(String hldName) {
        this.name = hldName;
    }

    @Override
    public long getHolderNo() {
        return holderNo;
    }

    @Override
    public int hashCode() {
        return Long.valueOf(holderNo).hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Holder)) {
            return false;
        }
        Holder other = (Holder)object;
        return this.holderNo == other.holderNo;
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }
}
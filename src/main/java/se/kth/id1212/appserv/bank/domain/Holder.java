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
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "HOLDER")
public class Holder {
    private static final String SEQUENCE_NAME_KEY = "SEQ_NAME";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME_KEY)
    @SequenceGenerator(name = SEQUENCE_NAME_KEY, sequenceName = "BANK_SEQUENCE")
    @Column(name = "HLD_ID")
    private long id;

    @OneToMany(mappedBy = "holder", cascade = CascadeType.ALL)
    private Set<Account> accounts = new HashSet<>();

    @Column(name = "HLD_NAME")
    private String name;

    @Column(name = "HLD_NO")
    private long holderNo;

    @Version
    @Column(name = "HLD_OPTLOCK_VERSION")
    private int optlockVersion;

    /**
     * Returns all accounts owned by this account holder.
     */
    public Set<Account> getAccounts() {
        return accounts;
    }

    /**
     * Returns the account holder's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the account holder's name
     */
    public void setName(String hldName) {
        this.name = hldName;
    }

    /**
     * Returns the holder's number. This number is unique for each holder in the
     * bank.
     */
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
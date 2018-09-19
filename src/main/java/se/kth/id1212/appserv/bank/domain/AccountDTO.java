package se.kth.id1212.appserv.bank.domain;

/**
 * Defines all operation that can be performed on an {@link Account} outside
 * the application and domain layers.
 */
public interface AccountDTO {
    /**
     * Returns the balance of the account.
     */
    int getbalance();

    /**
     * Returns the account number. This number is unique for each account in the
     * bank.
     */
    long getAcctNo();

    /**
     * Returns the account holder. An account always has exactly one holder.
     */
    HolderDTO getHolder();
}

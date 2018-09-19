package se.kth.id1212.appserv.bank.domain;

import java.util.Set;

/**
 * Defines all operation that can be performed on a {@link Holder} outside
 * the application and domain layers.
 */
public interface HolderDTO {
    /**
     * Returns the account holder's name
     */
    String getName();

    /**
     * Returns the holder's number. This number is unique for each holder in the
     * bank.
     */
    long getHolderNo();

    /**
     * Returns a set containing all accounts owned by this account holder.
     */
    Set<AccountDTO> getAccounts();
}

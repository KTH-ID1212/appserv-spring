package se.kth.id1212.appserv.bank.repository;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import se.kth.id1212.appserv.bank.domain.Account;

/**
 * Contains all database access concerning accounts.
 */
@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, Long> {
    /**
     * Returns the account with the specified account number, or null if there
     * is no such account.
     *
     * @param acctNo The number of the account to search for.
     * @return The account with the specified account number, or null if there
     * is no such account.
     * @throws IncorrectResultSizeDataAccessException If more than one account
     *                                                with the specified number
     *                                                was found.
     */
    Account findAccountByAcctNo(long acctNo);
}

package se.kth.id1212.appserv.bank.repository;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import se.kth.id1212.appserv.bank.domain.Holder;

/**
 * Contains all database access concerning holders.
 */
@Repository
@Transactional
public interface HolderRepository extends JpaRepository<Holder, Long> {
    // /**
    //  * Searches for all holders with the specified name.
    //  *
    //  * @param name The name of the holder's to search for.
    //  * @return A list containing all holders with the specified name. The list
    //  * is empty if there are no such holders.
    //  */
    // public List<Holder> findHoldersByName(String name);
    //
    /**
     * Returns the holder with the specified holder number, or null if there
     * is no such holder.
     *
     * @param holderNo The number of the holder to search for.
     * @return The account with the specified holder number, or null if there
     * is no such holder.
     * @throws IncorrectResultSizeDataAccessException If more than one holder
     *                                                with the specified number
     *                                                was found.
     */
    Holder findHolderByHolderNo(long holderNo);
}

package se.kth.id1212.appserv.bank.domain;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.security.SecureRandom;

/**
 * Generates business ids for entities, for example account numbers for bank
 * accounts. Take care never to confuse a business id is with the primary key of
 * an entity. A business id follows business rules, and is therefore allowed to
 * change if business rules change, while a primary key should remain unchanged
 * during the entire lifespan of an entity instance.
 */
@ApplicationScope
@Service
class BusinessIdGenerator {
    private SecureRandom randomNoGenerator = new SecureRandom();

    /**
     * Generates a unique account number.
     *
     * @return The newly generated account number.
     */
    long generateAcctNo() {
        return randomNoGenerator.nextLong();
    }

    /**
     * Generates a unique holder number.
     *
     * @return The newly generated account number.
     */
    long generateHolderNo() {
        return randomNoGenerator.nextLong();
    }
}

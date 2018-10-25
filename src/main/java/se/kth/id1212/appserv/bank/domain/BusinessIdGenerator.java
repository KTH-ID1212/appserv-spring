/*
 * The MIT License
 *
 * Copyright 2018 Leif Lindbäck <leifl@kth.se>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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

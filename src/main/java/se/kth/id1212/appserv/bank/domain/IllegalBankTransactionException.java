package se.kth.id1212.appserv.bank.domain;

/**
 * Thrown whenever an attempt is made to perform a transaction that is not
 * allowed by the bank's business rules.
 */
public class IllegalBankTransactionException extends Exception {
    /**
     * Creates a new instance with the specified message.
     *
     * @param msg A message explaining why the exception is thrown.
     */
    public IllegalBankTransactionException(String msg) {
        super(msg);
    }
}

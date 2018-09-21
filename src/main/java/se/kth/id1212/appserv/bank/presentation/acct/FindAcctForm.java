package se.kth.id1212.appserv.bank.presentation.acct;

import se.kth.id1212.appserv.bank.util.Util;

import javax.validation.constraints.NotNull;

/**
 * A form bean for the search account form.
 */
class FindAcctForm {
    @NotNull(message = "{find-acct.number.missing}")
    private Long number;

    /**
     * @return The number of the searched account.
     */
    public Long getNumber() {
        return number;
    }

    /**
     * @param number The number of the searched account.
     */
    public void setNumber(Long number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }
}

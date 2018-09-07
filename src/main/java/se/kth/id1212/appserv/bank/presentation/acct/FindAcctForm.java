package se.kth.id1212.appserv.bank.presentation.acct;

import se.kth.id1212.appserv.bank.util.Util;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * A form bean for the search account form.
 */
class FindAcctForm {
    @NotNull(message = "{find-acct.number.missing}")
    @Positive(message = "{find-acct.number.not-pos}")
    private Integer number;

    /**
     * @return The number of the searched account.
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @param number The number of the searched account.
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }
}

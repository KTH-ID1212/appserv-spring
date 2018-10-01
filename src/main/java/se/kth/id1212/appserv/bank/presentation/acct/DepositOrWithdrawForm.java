package se.kth.id1212.appserv.bank.presentation.acct;

import se.kth.id1212.appserv.bank.util.Util;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * A form bean for the deposit form.
 */
class DepositOrWithdrawForm {
    @NotNull(message = "Please specify amount")
    @Positive(message = "Amount must be greater than zero")
    private Integer amount;

    /**
     * @return The amount of the searched account.
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * @param amount The amount of the searched account.
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }
}

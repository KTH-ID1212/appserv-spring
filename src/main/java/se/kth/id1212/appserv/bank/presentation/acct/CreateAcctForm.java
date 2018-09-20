package se.kth.id1212.appserv.bank.presentation.acct;

import se.kth.id1212.appserv.bank.util.Util;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

/**
 * A form bean for the account creation form.
 */
class CreateAcctForm {
    @NotBlank(message = "{create-acct.holder-name.missing}")
    // The regex below should permit only characters, but asterisk is
    // unfortunately also valid.
    @Pattern(regexp = "^[\\p{L}\\p{M}*]*$", message = "{create-acct" +
                                                      ".holder-name" +
                                                      ".invalid-char}")
    @Size(min = 2, max = 30, message = "{create-acct.holder-name.length}")
    private String holderName;

    @NotNull(message = "{create-acct.balance.missing}")
    @PositiveOrZero(message = "{create-acct.balance.negative}")
    private Integer balance;

    /**
     * @return The initial balance of the account that will be created.
     */
    public Integer getBalance() {
        return balance;
    }

    /**
     * @param balance The initial balance of the account that will be created.
     */
    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    /**
     * @return The name of the holderName of the account that will be created.
     */
    public String getHolderName() {
        return holderName;
    }

    /**
     * @param holderName The name of the holderName of the account that will be
     *               created.
     */
    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    @Override
    public String toString() {
        return Util.toString(this);
    }
}

package se.kth.id1212.appserv.bank.presentation.acct;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import se.kth.id1212.appserv.bank.repository.DbUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

@SpringJUnitWebConfig(initializers = ConfigFileApplicationContextInitializer.class)
@EnableAutoConfiguration
    //@SpringBootTest can be used instead of @SpringJUnitWebConfig and
    // @EnableAutoConfiguration, but are we using JUnit5 in that case?
class DepositOrWithdrawFormTest {
    @Autowired
    private Validator validator;

    @BeforeAll
    static void enableCreatingEMFWhichIsNeededForTheApplicationContext()
        throws SQLException, IOException, ClassNotFoundException {
        DbUtil.emptyDb();
    }

    @Test
    void testNegAmt() {
        testInvalidAmt(-1, "{amt.not-pos}");
    }

    @Test
    void testNullAmt() {
        testInvalidAmt(null, "{amt.missing}");
    }

    @Test
    void testZeroAmt() {
        testInvalidAmt(0, "{amt.not-pos}");
    }

    @Test
    void testCorrectAmt() {
        DepositOrWithdrawForm sut = new DepositOrWithdrawForm();
        sut.setAmount(1);
        Set<ConstraintViolation<DepositOrWithdrawForm>> result =
            validator.validate(sut);
        assertThat(result, is(empty()));
    }

    private void testInvalidAmt(Integer invalidAmt, String expectedMsg) {
        DepositOrWithdrawForm sut = new DepositOrWithdrawForm();
        sut.setAmount(invalidAmt);
        Set<ConstraintViolation<DepositOrWithdrawForm>> result =
            validator.validate(sut);
        assertThat(result.size(), is(1));
        assertThat(result, hasItem(hasProperty("messageTemplate", equalTo(
            expectedMsg))));
    }
}

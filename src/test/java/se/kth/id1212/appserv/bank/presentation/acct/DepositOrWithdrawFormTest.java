package se.kth.id1212.appserv.bank.presentation.acct;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
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
@ComponentScan(basePackages = {"se.kth.id1212.appserv.bank"})
//@SpringBootTest can be used instead of @SpringJUnitWebConfig,
// @EnableAutoConfiguration and @ComponentScan, but are we using
// JUnit5 in that case?
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, DepositOrWithdrawFormTest.class})
class DepositOrWithdrawFormTest implements TestExecutionListener {
    @Autowired
    private DbUtil dbUtil;
    @Autowired
    private Validator validator;

    @Override
    public void beforeTestClass(TestContext testContext) throws SQLException, IOException, ClassNotFoundException {
        dbUtil = testContext.getApplicationContext().getBean(DbUtil.class);
        enableCreatingEMFWhichIsNeededForTheApplicationContext();
    }

    private void enableCreatingEMFWhichIsNeededForTheApplicationContext()
        throws SQLException, IOException, ClassNotFoundException {
        dbUtil.emptyDb();
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

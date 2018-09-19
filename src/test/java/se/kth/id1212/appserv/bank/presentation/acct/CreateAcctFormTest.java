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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;

@SpringJUnitWebConfig(initializers = ConfigFileApplicationContextInitializer.class)
@EnableAutoConfiguration
    //@SpringBootTest can be used instead of @SpringJUnitWebConfig and
    // @EnableAutoConfiguration, but are we using JUnit5 in that case?
class CreateAcctFormTest {
    @Autowired
    private Validator validator;

    @BeforeAll
    static void enableCreatingEMFWhichIsNeededForTheApplicationContext()
        throws SQLException, IOException, ClassNotFoundException {
        DbUtil.emptyDb();
    }

    @Test
    void testNegBalance() {
        testInvalidBalance(-1, "{create-acct.balance.negative}");
    }

    @Test
    void testNullBalance() {
        testInvalidBalance(null, "{create-acct.balance.missing}");
    }

    @Test
    void testCorrectPosBalance() {
        testvalidBalance(1);
    }

    @Test
    void testCorrectZeroBalance() {
        testvalidBalance(0);
    }

    @Test
    void testNullHolder() {
        testInvalidHolder(null, "{create-acct.holder.missing}");
    }

    @Test
    void testEmptyHolder() {
        testInvalidHolder("", "{create-acct.holder.length}",
                              "{create-acct.holder.missing}");
    }

    @Test
    void testTooShortHolder() {
        testInvalidHolder("a", "{create-acct.holder.length}");
    }

    @Test
    void testTooLongHolder() {
        testInvalidHolder("abcdeabcdeabcdeabcdeabcdeabcdep", "{create-acct" +
                                                             ".holder.length}");
    }

    @Test
    void testHolderWithInvalidChar() {
        testInvalidHolder("a.", "{create-acct.holder.invalid-char}");
    }

    @Test
    void testCorrectHolder() {
        testValidHolder("abc");
    }

    @Test
    void testCorrectHolderWithNonLatinChars() {
        testValidHolder("åäöÅÄÖéèëê");
    }

    private void testvalidBalance(Integer validBalance) {
        String validHolder = "abc";
        CreateAcctForm sut = new CreateAcctForm();
        sut.setHolder(validHolder);
        sut.setBalance(validBalance);
        Set<ConstraintViolation<CreateAcctForm>> result =
            validator.validate(sut);
        assertThat(result, is(empty()));
    }

    private void testValidHolder(String validHolder) {
        int validBalance = 1;
        CreateAcctForm sut = new CreateAcctForm();
        sut.setHolder(validHolder);
        sut.setBalance(validBalance);
        Set<ConstraintViolation<CreateAcctForm>> result =
            validator.validate(sut);
        assertThat(result, is(empty()));
    }

    private void testInvalidBalance(Integer invalidBalance, String expectedMsg) {
        String validHolder = "abc";
        CreateAcctForm sut = new CreateAcctForm();
        sut.setHolder(validHolder);
        sut.setBalance(invalidBalance);
        Set<ConstraintViolation<CreateAcctForm>> result =
            validator.validate(sut);
        assertThat(result.size(), is(1));
        assertThat(result, hasItem(hasProperty("messageTemplate", equalTo(
            expectedMsg))));
    }

    private void testInvalidHolder(String invalidHolder,
                                   String... expectedMsgs) {
        int validBalance = 1;
        CreateAcctForm sut = new CreateAcctForm();
        sut.setBalance(validBalance);
        sut.setHolder(invalidHolder);
        Set<ConstraintViolation<CreateAcctForm>> result =
            validator.validate(sut);
        assertThat(result.size(), is(expectedMsgs.length));
        for (String expectedMsg : expectedMsgs) {
            assertThat(result, hasItem(hasProperty("messageTemplate",
                                                   equalTo(expectedMsg))));
        }
    }
}

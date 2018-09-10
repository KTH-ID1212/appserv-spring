package se.kth.id1212.appserv.bank.presentation.acct;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
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
class FindAcctFormTest {

    @Autowired
    private Validator validator;

    @Test
    void testNegNo() {
        testInvalidNo(-1, "{find-acct.number.not-pos}");
    }

    @Test
    void testNullNo() {
        testInvalidNo(null, "{find-acct.number.missing}");
    }

    @Test
    void testZeroNo() {
        testInvalidNo(0, "{find-acct.number.not-pos}");
    }

    @Test
    void testCorrectNo() {
        FindAcctForm sut = new FindAcctForm();
        sut.setNumber(1);
        Set<ConstraintViolation<FindAcctForm>> result =
            validator.validate(sut);
        assertThat(result, is(empty()));
    }

    private void testInvalidNo(Integer invalidNo, String expectedMsg) {
        FindAcctForm sut = new FindAcctForm();
        sut.setNumber(invalidNo);
        Set<ConstraintViolation<FindAcctForm>> result =
            validator.validate(sut);
        assertThat(result.size(), is(1));
        assertThat(result, hasItem(hasProperty("messageTemplate", equalTo(
            expectedMsg))));
    }
}

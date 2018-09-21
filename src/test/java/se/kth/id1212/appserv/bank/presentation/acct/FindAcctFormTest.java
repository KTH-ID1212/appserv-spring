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
class FindAcctFormTest {
    @Autowired
    private Validator validator;

    @BeforeAll
    static void enableCreatingEMFWhichIsNeededForTheApplicationContext()
        throws SQLException, IOException, ClassNotFoundException {
        DbUtil.emptyDb();
    }

    @Test
    void testNullNo() {
        testInvalidNo(null, "{find-acct.number.missing}");
    }

    @Test
    void testCorrectNo() {
        FindAcctForm sut = new FindAcctForm();
        sut.setNumber(1L);
        Set<ConstraintViolation<FindAcctForm>> result =
            validator.validate(sut);
        assertThat(result, is(empty()));
    }

    private void testInvalidNo(Long invalidNo, String expectedMsg) {
        FindAcctForm sut = new FindAcctForm();
        sut.setNumber(invalidNo);
        Set<ConstraintViolation<FindAcctForm>> result =
            validator.validate(sut);
        assertThat(result.size(), is(1));
        assertThat(result, hasItem(hasProperty("messageTemplate", equalTo(
            expectedMsg))));
    }
}

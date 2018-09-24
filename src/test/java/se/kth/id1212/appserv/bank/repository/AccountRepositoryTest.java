package se.kth.id1212.appserv.bank.repository;

import net.jcip.annotations.NotThreadSafe;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import se.kth.id1212.appserv.bank.domain.Account;
import se.kth.id1212.appserv.bank.domain.Holder;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

@SpringJUnitWebConfig(initializers = ConfigFileApplicationContextInitializer.class)
@EnableAutoConfiguration
@ComponentScan(basePackages = {"se.kth.id1212.appserv.bank"})
    //@SpringBootTest can be used instead of @SpringJUnitWebConfig,
    // @EnableAutoConfiguration and @ComponentScan, but are we using
    // JUnit5 in that case?
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, AccountRepositoryTest.class})
@NotThreadSafe
class AccountRepositoryTest implements TestExecutionListener {
    @Autowired
    private DbUtil dbUtil;
    @Autowired
    private AccountRepository instance;
    private Account acct;
    private Holder holder;

    @Override
    public void beforeTestClass(TestContext testContext) throws SQLException, IOException, ClassNotFoundException {
        dbUtil = testContext.getApplicationContext().getBean(DbUtil.class);
        enableCreatingEMFWhichIsNeededForTheApplicationContext();
    }

    @Override
    public void afterTestClass(TestContext testContext) throws SQLException, IOException, ClassNotFoundException {
        enableCreatingEMFWhichIsNeededForTheApplicationContext();
    }

    private void enableCreatingEMFWhichIsNeededForTheApplicationContext()
        throws SQLException, IOException, ClassNotFoundException {
        dbUtil.emptyDb();
    }

    @BeforeEach
    void setup() throws SQLException, IOException, ClassNotFoundException {
        holder = new Holder("holderName");
        acct = new Account(holder, 10);
        dbUtil.emptyDb();
    }

    @Test
    void testCreateAcct() {
        instance.save(acct);
        List<Account> acctsInDb = instance.findAll();
        assertThat(acctsInDb, containsInAnyOrder(acct));
        assertThat(acctsInDb.get(0).getHolder(), is(holder));
    }

    @Test
    void testFindExistingAcctByAcctNo() {
        instance.save(acct);
        Account acctInDb =
            instance.findAccountByAcctNo(acct.getAcctNo());
        assertThat(acctInDb.getAcctNo(), is(acct.getAcctNo()));
    }

    @Test
    void testFindNonExistingAcctByAcctNo() {
        instance.save(acct);
        Account acctInDb =
            instance.findAccountByAcctNo(0);
        assertThat(acctInDb == null, is(true));
    }
}

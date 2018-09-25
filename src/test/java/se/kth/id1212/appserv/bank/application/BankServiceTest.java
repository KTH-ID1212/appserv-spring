package se.kth.id1212.appserv.bank.application;

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
import se.kth.id1212.appserv.bank.domain.AccountDTO;
import se.kth.id1212.appserv.bank.domain.Holder;
import se.kth.id1212.appserv.bank.domain.IllegalBankTransactionException;
import se.kth.id1212.appserv.bank.repository.AccountRepository;
import se.kth.id1212.appserv.bank.repository.DbUtil;
import se.kth.id1212.appserv.bank.repository.HolderRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitWebConfig(initializers = ConfigFileApplicationContextInitializer.class)
@EnableAutoConfiguration
@ComponentScan(basePackages = {"se.kth.id1212.appserv.bank"})
//@SpringBootTest can be used instead of @SpringJUnitWebConfig,
// @EnableAutoConfiguration and @ComponentScan, but are we using
// JUnit5 in that case?
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class, BankServiceTest.class})
@NotThreadSafe
public class BankServiceTest implements TestExecutionListener {
    @Autowired
    private DbUtil dbUtil;
    @Autowired
    private BankService instance;
    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private HolderRepository holderRepo;
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
    void testCreateAcctAndHolder() throws IllegalBankTransactionException {
        instance.createAccountAndHolder(holder.getName(), acct.getBalance());
        List<Account> acctsInDb = accountRepo.findAll();
        assertThat(acctsInDb.size(), is(1));
        assertThat(acctsInDb, hasItem(hasProperty("balance", equalTo(acct.getBalance()))));
        assertThat(acctsInDb.get(0).getHolder().getName(), is(holder.getName()));
    }

    @Test
    void testCreateAcctForNonExistingHolder() {
        Exception exception = assertThrows(IllegalBankTransactionException.class, () -> {
            instance.createAccount(holder, acct.getBalance());
        });
        assertThat(exception.getMessage(), containsString("does not exist"));
        assertThat(exception.getMessage(), containsString(holder.toString()));
    }

    @Test
    void testCreateAcctForExistingHolder() throws IllegalBankTransactionException {
        holderRepo.save(holder);
        instance.createAccount(holder, acct.getBalance());
        List<Account> acctsInDb = accountRepo.findAll();
        assertThat(acctsInDb.size(), is(1));
        assertThat(acctsInDb, hasItem(hasProperty("balance", equalTo(acct.getBalance()))));
        assertThat(acctsInDb.get(0).getHolder().getName(), is(holder.getName()));
    }

    @Test
    void testFindExistingAcctByAcctNo() {
        accountRepo.save(acct);
        AccountDTO acctInDb = instance.findAccount(acct.getAcctNo());
        assertThat(acctInDb.getAcctNo(), is(acct.getAcctNo()));
    }

    @Test
    void testFindNonExistingAcctByAcctNo() {
        accountRepo.save(acct);
        AccountDTO acctInDb = instance.findAccount(0);
        assertThat(acctInDb == null, is(true));
    }

    @Test
    void testDepositLegalAmt() throws IllegalBankTransactionException {
        int amtToDeposit = 5;
        accountRepo.save(acct);
        instance.deposit(acct, amtToDeposit);
        Account acctInDb = accountRepo.findAccountByAcctNo(acct.getAcctNo());
        int expectedBalance = acct.getBalance() + amtToDeposit;
        assertThat(acctInDb.getBalance(), is(expectedBalance));
    }

    @Test
    void testDepositNegAmt() {
        int amtToDeposit = -5;
        accountRepo.save(acct);
        Exception exception = assertThrows(IllegalBankTransactionException.class, () -> {
            instance.deposit(acct, amtToDeposit);
        });
        assertThat(exception.getMessage(), containsString("non-positive"));
        assertThat(exception.getMessage(), containsString(Integer.toString(amtToDeposit)));
    }

    @Test
    void testDepositZero() {
        int amtToDeposit = 0;
        accountRepo.save(acct);
        Exception exception = assertThrows(IllegalBankTransactionException.class, () -> {
            instance.deposit(acct, amtToDeposit);
        });
        assertThat(exception.getMessage(), containsString("non-positive"));
        assertThat(exception.getMessage(), containsString(Integer.toString(amtToDeposit)));
    }

    @Test
    void testDepositToNonExistingAcct() {
        int amtToDeposit = 5;
        Exception exception = assertThrows(IllegalBankTransactionException.class, () -> {
            instance.deposit(acct, amtToDeposit);
        });
        assertThat(exception.getMessage(), containsString("non-existing"));
        assertThat(exception.getMessage(), containsString(acct.toString()));
    }

    @Test
    void testDepositToNullAcct() {
        int amtToDeposit = 5;
        Exception exception = assertThrows(IllegalBankTransactionException.class, () -> {
            instance.deposit(null, amtToDeposit);
        });
        assertThat(exception.getMessage(), containsString("null"));
    }

    @Test
    void testWithdrawLegalAmt() throws IllegalBankTransactionException {
        int amtToWithdraw = 5;
        accountRepo.save(acct);
        instance.withdraw(acct, amtToWithdraw);
        Account acctInDb = accountRepo.findAccountByAcctNo(acct.getAcctNo());
        int expectedBalance = acct.getBalance() - amtToWithdraw;
        assertThat(acctInDb.getBalance(), is(expectedBalance));
    }

    @Test
    void testWithdrawNegAmt() {
        int amtToWithdraw = -5;
        accountRepo.save(acct);
        Exception exception = assertThrows(IllegalBankTransactionException.class, () -> {
            instance.withdraw(acct, amtToWithdraw);
        });
        assertThat(exception.getMessage(), containsString("non-positive"));
        assertThat(exception.getMessage(), containsString(Integer.toString(amtToWithdraw)));
    }

    @Test
    void testWithdrawZero() {
        int amtToWithdraw = 0;
        accountRepo.save(acct);
        Exception exception = assertThrows(IllegalBankTransactionException.class, () -> {
            instance.withdraw(acct, amtToWithdraw);
        });
        assertThat(exception.getMessage(), containsString("non-positive"));
        assertThat(exception.getMessage(), containsString(Integer.toString(amtToWithdraw)));
    }

    @Test
    void testOverdraft() {
        int amtToWithdraw = acct.getBalance() + 1;
        accountRepo.save(acct);
        Exception exception = assertThrows(IllegalBankTransactionException.class, () -> {
            instance.withdraw(acct, amtToWithdraw);
        });
        assertThat(exception.getMessage(), containsString("greater than balance"));
        assertThat(exception.getMessage(), containsString(Integer.toString(amtToWithdraw)));
    }

    @Test
    void testWithdrawFromNonExistingAcct() {
        int amtToWithdraw = 5;
        Exception exception = assertThrows(IllegalBankTransactionException.class, () -> {
            instance.withdraw(acct, amtToWithdraw);
        });
        assertThat(exception.getMessage(), containsString("non-existing"));
        assertThat(exception.getMessage(), containsString(acct.toString()));
    }

    @Test
    void testCreateHolder() {
        instance.createHolder(holder.getName());
        List<Holder> holdersInDb = holderRepo.findAll();
        assertThat(holdersInDb.size(), is(1));
        assertThat(holdersInDb, hasItem(hasProperty("name", equalTo(holder.getName()))));
    }
}

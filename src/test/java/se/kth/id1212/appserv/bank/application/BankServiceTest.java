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
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
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
@NotThreadSafe
public class BankServiceTest {
    @Autowired
    private BankService instance;
    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private HolderRepository holderRepo;
    private Account acct;
    private Holder holder;

    @AfterAll
    static void cleanup()
        throws SQLException, IOException, ClassNotFoundException {
        DbUtil.emptyDb();
    }

    @BeforeAll
    static void enableCreatingEMFWhichIsNeededForTheApplicationContext()
        throws SQLException, IOException, ClassNotFoundException {
        DbUtil.emptyDb();
    }

    @BeforeEach
    void setup() throws SQLException, IOException, ClassNotFoundException {
        holder = new Holder("holderName");
        acct = new Account(holder, 10);
        DbUtil.emptyDb();
    }

    @Test
    void testCreateAcctForNonExistingHolder() {
        Exception exception =
            assertThrows(IllegalBankTransactionException.class, () -> {
                instance.createAccount(holder, acct.getbalance());
            });
        assertThat(exception.getMessage(), containsString("does not exist"));
        assertThat(exception.getMessage(), containsString(holder.toString()));
    }

    @Test
    void testCreateAcctForExistingHolder()
        throws IllegalBankTransactionException {
        holderRepo.save(holder);
        instance.createAccount(holder, acct.getbalance());
        List<Account> acctsInDb = accountRepo.findAll();
        assertThat(acctsInDb.size(), is(1));
        assertThat(acctsInDb,
                   hasItem(hasProperty("balance", equalTo(acct.getbalance()))));
        assertThat(acctsInDb.get(0).getHolder().getName(),
                   is(holder.getName()));
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
        int expectedBalance = acct.getbalance() + amtToDeposit;
        assertThat(acctInDb.getbalance(), is(expectedBalance));
    }

    @Test
    void testDepositNegAmt() {
        int amtToDeposit = -5;
        accountRepo.save(acct);
        Exception exception =
            assertThrows(IllegalBankTransactionException.class, () -> {
                instance.deposit(acct, amtToDeposit);
            });
        assertThat(exception.getMessage(), containsString("non-positive"));
        assertThat(exception.getMessage(),
                   containsString(Integer.toString(amtToDeposit)));
    }

    @Test
    void testDepositZero() {
        int amtToDeposit = 0;
        accountRepo.save(acct);
        Exception exception =
            assertThrows(IllegalBankTransactionException.class, () -> {
                instance.deposit(acct, amtToDeposit);
            });
        assertThat(exception.getMessage(), containsString("non-positive"));
        assertThat(exception.getMessage(),
                   containsString(Integer.toString(amtToDeposit)));
    }

    @Test
    void testDepositToNonExistingAcct() {
        int amtToDeposit = 5;
        Exception exception =
            assertThrows(IllegalBankTransactionException.class, () -> {
                instance.deposit(acct, amtToDeposit);
            });
        assertThat(exception.getMessage(), containsString("does not exist"));
        assertThat(exception.getMessage(), containsString(acct.toString()));
    }

    @Test
    void testWithdrawLegalAmt() throws IllegalBankTransactionException {
        int amtToWithdraw = 5;
        accountRepo.save(acct);
        instance.withdraw(acct, amtToWithdraw);
        Account acctInDb = accountRepo.findAccountByAcctNo(acct.getAcctNo());
        int expectedBalance = acct.getbalance() - amtToWithdraw;
        assertThat(acctInDb.getbalance(), is(expectedBalance));
    }

    @Test
    void testWithdrawNegAmt() {
        int amtToWithdraw = -5;
        accountRepo.save(acct);
        Exception exception =
            assertThrows(IllegalBankTransactionException.class, () -> {
                instance.withdraw(acct, amtToWithdraw);
            });
        assertThat(exception.getMessage(), containsString("non-positive"));
        assertThat(exception.getMessage(),
                   containsString(Integer.toString(amtToWithdraw)));
    }

    @Test
    void testWithdrawZero() {
        int amtToWithdraw = 0;
        accountRepo.save(acct);
        Exception exception =
            assertThrows(IllegalBankTransactionException.class, () -> {
                instance.withdraw(acct, amtToWithdraw);
            });
        assertThat(exception.getMessage(), containsString("non-positive"));
        assertThat(exception.getMessage(),
                   containsString(Integer.toString(amtToWithdraw)));
    }

    @Test
    void testOverdraft() {
        int amtToWithdraw = acct.getbalance() + 1;
        accountRepo.save(acct);
        Exception exception =
            assertThrows(IllegalBankTransactionException.class, () -> {
                instance.withdraw(acct, amtToWithdraw);
            });
        assertThat(exception.getMessage(), containsString("Overdraft attempt"));
        assertThat(exception.getMessage(),
                   containsString(Integer.toString(amtToWithdraw)));
    }

    @Test
    void testWithdrawFromNonExistingAcct() {
        int amtToWithdraw = 5;
        Exception exception =
            assertThrows(IllegalBankTransactionException.class, () -> {
                instance.withdraw(acct, amtToWithdraw);
            });
        assertThat(exception.getMessage(), containsString("does not exist"));
        assertThat(exception.getMessage(), containsString(acct.toString()));
    }

    @Test
    void testCreateHolder() {
        instance.createHolder(holder.getName());
        List<Holder> holdersInDb = holderRepo.findAll();
        assertThat(holdersInDb.size(), is(1));
        assertThat(holdersInDb,
                   hasItem(hasProperty("name", equalTo(holder.getName()))));
    }
}

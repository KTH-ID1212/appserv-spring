package se.kth.id1212.appserv.bank.presentation.acct;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import se.kth.id1212.appserv.bank.repository.DbUtil;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.kth.id1212.appserv.bank.presentation.PresentationTestHelper.addParam;
import static se.kth.id1212.appserv.bank.presentation.PresentationTestHelper.containsElements;
import static se.kth.id1212.appserv.bank.presentation.PresentationTestHelper.doesNotContainElements;
import static se.kth.id1212.appserv.bank.presentation.PresentationTestHelper.sendGetRequest;
import static se.kth.id1212.appserv.bank.presentation.PresentationTestHelper.sendPostRequest;

@SpringJUnitWebConfig(initializers = ConfigFileApplicationContextInitializer.class)
@EnableAutoConfiguration
@ComponentScan(basePackages = {"se.kth.id1212.appserv.bank"})
    //@SpringBootTest can be used instead of @SpringJUnitWebConfig,
    // @EnableAutoConfiguration and @ComponentScan, but are we using
    // JUnit5 in that case?
class AcctControllerTest {
    @Autowired
    private WebApplicationContext webappContext;
    private MockMvc mockMvc;

    @BeforeAll
    static void enableCreatingEMFWhichIsNeededForTheApplicationContext()
        throws SQLException, IOException, ClassNotFoundException {
        DbUtil.emptyDb();
    }

    @BeforeEach
    void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webappContext).build();
    }

    @Test
    void testCorrectViewForDeafultUrl() throws Exception {
        sendGetRequest(mockMvc, "").andExpect(status().is3xxRedirection())
                                   .andExpect(header().exists("Location"));
    }

    @Test
    void testCorrectViewForSelectAcctUrl() throws Exception {
        sendGetRequest(mockMvc, AcctController.SELECT_ACCT_PAGE_URL)
            .andExpect(status().isOk()).andExpect(isSelectAcctPage());
    }

    @Test
    void testCreateCorrectParams() throws Exception {
        sendPostRequest(mockMvc, AcctController.CREATE_ACCT_URL,
                        addParam(addParam("balance", "1"), "holderName", "ab"))
            .andExpect(status().isOk()).andExpect(isAcctPage())
            .andExpect(doesNotContainElements("span.error"));
    }

    @Test
    void testCreateAndChangeLang() throws Exception {
        sendPostRequest(mockMvc, AcctController.CREATE_ACCT_URL,
                        addParam(addParam("balance", "-1"), "holderName", "ab"))
            .andExpect(status().isOk()).andExpect(isSelectAcctPage());
        sendGetRequest(mockMvc, AcctController.CREATE_ACCT_URL + "?lang=en")
            .andExpect(status().isOk()).andExpect(isSelectAcctPage());
    }

    @Test
    void testCreateAcctTooShortHolder() throws Exception {
        sendPostRequest(mockMvc, AcctController.CREATE_ACCT_URL,
                        addParam(addParam("balance", "1"), "holderName", "1"))
            .andExpect(status().isOk()).andExpect(isSelectAcctPage())
            .andExpect(containsErrorMsg("create-acct-holder", "2"));
    }

    @Test
    void testCreateAcctMissingHolder() throws Exception {
        sendPostRequest(mockMvc, AcctController.CREATE_ACCT_URL,
                        addParam(addParam("balance", "1"), "holderName", ""))
            .andExpect(status().isOk()).andExpect(isSelectAcctPage()).andExpect(
            containsErrorMsg("create-acct-holder",
                             "specify " + "account " + "holder"));
    }

    @Test
    void testCreateAcctWrongCharInHolder() throws Exception {
        sendPostRequest(mockMvc, AcctController.CREATE_ACCT_URL,
                        addParam(addParam("balance", "1"), "holderName", "123"))
            .andExpect(status().isOk()).andExpect(isSelectAcctPage())
            .andExpect(containsErrorMsg("create-acct-holder", "only letters"));
    }

    @Test
    void testCreateAcctTooLongHolder() throws Exception {
        sendPostRequest(mockMvc, AcctController.CREATE_ACCT_URL,
                        addParam(addParam("balance", "1"), "holderName",
                                 "1234567890123456789012345678901"))
            .andExpect(status().isOk()).andExpect(isSelectAcctPage())
            .andExpect(containsErrorMsg("create-acct-holder", "30"));
    }

    @Test
    void testCreateAcctMissingBalance() throws Exception {
        sendPostRequest(mockMvc, AcctController.CREATE_ACCT_URL,
                        addParam("holderName", "12")).andExpect(status().isOk())
                                                     .andExpect(
                                                         isSelectAcctPage())
                                                     .andExpect(
                                                         containsErrorMsg(
                                                             "create-balance",
                                                             "specify balance"));
    }

    @Test
    void testCreateAcctWrongCharInBalance() throws Exception {
        sendPostRequest(mockMvc, AcctController.CREATE_ACCT_URL,
                        addParam(addParam("balance", "a"), "holderName", "abc"))
            .andExpect(status().isOk()).andExpect(isSelectAcctPage())
            .andExpect(containsErrorMsg("create-balance", "only numbers"));
    }

    @Test
    void testCreateAcctNegativeBalance() throws Exception {
        sendPostRequest(mockMvc, AcctController.CREATE_ACCT_URL,
                        addParam(addParam("balance", "-1"), "holderName", "12"))
            .andExpect(status().isOk()).andExpect(isSelectAcctPage())
            .andExpect(containsErrorMsg("create-balance", "zero or greater"));
    }

    @Test
    void testFindCorrectParams() throws Exception {
        sendPostRequest(mockMvc, AcctController.FIND_ACCT_URL,
                        addParam("number", "1")).andExpect(status().isOk())
                                                .andExpect(isAcctPage())
                                                .andExpect(
                                                    doesNotContainElements(
                                                        "span.error"));
    }

    @Test
    void testFindAndChangeLang() throws Exception {
        sendPostRequest(mockMvc, AcctController.FIND_ACCT_URL,
                        addParam("number", "a")).andExpect(status().isOk())
                                                .andExpect(isSelectAcctPage());
        sendGetRequest(mockMvc, AcctController.FIND_ACCT_URL + "?lang=en")
            .andExpect(status().isOk()).andExpect(isSelectAcctPage());
    }

    @Test
    void testFindAcctMissingAcctNo() throws Exception {
        sendPostRequest(mockMvc, AcctController.FIND_ACCT_URL)
            .andExpect(status().isOk()).andExpect(isSelectAcctPage()).andExpect(
            containsErrorMsg("search-acct-number",
                             "specify " + "account " + "number"));
    }

    @Test
    void testFindAcctWrongCharInAcctNo() throws Exception {
        sendPostRequest(mockMvc, AcctController.FIND_ACCT_URL,
                        addParam("number", "a")).andExpect(status().isOk())
                                                .andExpect(isSelectAcctPage())
                                                .andExpect(containsErrorMsg(
                                                    "search-acct-number",
                                                    "only numbers"));
    }

    @Test
    void testFindAcctNegativeNumber() throws Exception {
        sendPostRequest(mockMvc, AcctController.FIND_ACCT_URL,
                        addParam("number", "-1")).andExpect(status().isOk())
                                                 .andExpect(isSelectAcctPage())
                                                 .andExpect(containsErrorMsg(
                                                     "search-acct-number",
                                                     "greater than " + "zero"));
    }

    @Test
    void testDepositCorrectParams() throws Exception {
        HttpSession session = createSessionWithContrThatHasAcct();
        sendPostRequest(mockMvc, AcctController.DEPOSIT_URL, session,
                        addParam("amount", "1")).andExpect(status().isOk())
                                                .andExpect(isAcctPage())
                                                .andExpect(
                                                    doesNotContainElements(
                                                        "span.error"));
    }

    @Test
    void testDepositAndChangeLang() throws Exception {
        HttpSession session = createSessionWithContrThatHasAcct();
        sendPostRequest(mockMvc, AcctController.DEPOSIT_URL, session,
                        addParam("amount", "a")).andExpect(status().isOk())
                                                .andExpect(isAcctPage());
        sendGetRequest(mockMvc, AcctController.DEPOSIT_URL + "?lang=en",
                       session).andExpect(status().isOk())
                               .andExpect(isAcctPage());
    }

    @Test
    void testDepositMissingAmt() throws Exception {
        HttpSession session = createSessionWithContrThatHasAcct();
        sendPostRequest(mockMvc, AcctController.DEPOSIT_URL, session)
            .andExpect(status().isOk()).andExpect(isAcctPage())
            .andExpect(containsErrorMsg("deposit-amt", "specify amount"));
    }

    @Test
    void testDepositWrongCharInAmt() throws Exception {
        HttpSession session = createSessionWithContrThatHasAcct();
        sendPostRequest(mockMvc, AcctController.DEPOSIT_URL, session,
                        addParam("amount", "a")).andExpect(status().isOk())
                                                .andExpect(isAcctPage())
                                                .andExpect(containsErrorMsg(
                                                    "deposit-amt",
                                                    "only numbers"));
    }

    @Test
    void testDepositNegativeNumber() throws Exception {
        HttpSession session = createSessionWithContrThatHasAcct();
        sendPostRequest(mockMvc, AcctController.DEPOSIT_URL, session,
                        addParam("amount", "-1")).andExpect(status().isOk())
                                                 .andExpect(isAcctPage())
                                                 .andExpect(containsErrorMsg(
                                                     "deposit-amt",
                                                     "greater than zero"));
    }

    @Test
    void testWithdrawCorrectParams() throws Exception {
        HttpSession session = createSessionWithContrThatHasAcct();
        sendPostRequest(mockMvc, AcctController.WITHDRAW_URL, session,
                        addParam("amount", "1")).andExpect(status().isOk())
                                                .andExpect(isAcctPage())
                                                .andExpect(
                                                    doesNotContainElements(
                                                        "span.error"));
    }

    @Test
    void testWithdrawAndChangeLang() throws Exception {
        HttpSession session = createSessionWithContrThatHasAcct();
        sendPostRequest(mockMvc, AcctController.WITHDRAW_URL, session,
                        addParam("amount", "a")).andExpect(status().isOk())
                                                .andExpect(isAcctPage());
        sendGetRequest(mockMvc, AcctController.WITHDRAW_URL + "?lang=en",
                       session).andExpect(status().isOk())
                               .andExpect(isAcctPage());
    }

    @Test
    void testWithdrawMissingAmt() throws Exception {
        HttpSession session = createSessionWithContrThatHasAcct();
        sendPostRequest(mockMvc, AcctController.WITHDRAW_URL, session)
            .andExpect(status().isOk()).andExpect(isAcctPage())
            .andExpect(containsErrorMsg("withdraw-amt", "specify amount"));
    }

    @Test
    void testWithdrawWrongCharInAmt() throws Exception {
        HttpSession session = createSessionWithContrThatHasAcct();
        sendPostRequest(mockMvc, AcctController.WITHDRAW_URL, session,
                        addParam("amount", "a")).andExpect(status().isOk())
                                                .andExpect(isAcctPage())
                                                .andExpect(containsErrorMsg(
                                                    "withdraw-amt",
                                                    "only numbers"));
    }

    @Test
    void testWithdrawNegativeNumber() throws Exception {
        HttpSession session = createSessionWithContrThatHasAcct();
        sendPostRequest(mockMvc, AcctController.WITHDRAW_URL, session,
                        addParam("amount", "-1")).andExpect(status().isOk())
                                                 .andExpect(isAcctPage())
                                                 .andExpect(containsErrorMsg(
                                                     "withdraw-amt",
                                                     "greater than zero"));
    }

    @Test
    void testCorrectViewForAcctUrl() throws Exception {
        sendGetRequest(mockMvc, AcctController.ACCT_PAGE_URL)
            .andExpect(status().isOk()).andExpect(isAcctPage());
    }

    private HttpSession createSessionWithContrThatHasAcct() throws Exception {
        return sendPostRequest(mockMvc, AcctController.CREATE_ACCT_URL,
                               addParam(addParam("balance", "1"), "holderName",
                                        "ab")).andReturn().getRequest()
                                              .getSession();
    }

    private ResultMatcher containsErrorMsg(String idForErrorElem,
                                           String errorMsg) {
        return containsElements(
            "input[id=" + idForErrorElem + "]" + "~span.error:contains(" +
            errorMsg + ")");
    }

    private ResultMatcher isAcctPage() {
        return containsElements("main h1:contains(Deposit)",
                                "main h1:contains(Withdraw)");
    }

    private ResultMatcher isSelectAcctPage() {
        return containsElements("main h1:contains(Search Account)",
                                "main h1:contains(Create Account)");
    }
}

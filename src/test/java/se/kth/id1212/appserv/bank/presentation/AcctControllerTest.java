package se.kth.id1212.appserv.bank.presentation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.kth.id1212.appserv.bank.presentation.Util.containsElements;
import static se.kth.id1212.appserv.bank.presentation.Util.doesNotContainElements;

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

    @BeforeEach
    void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webappContext).build();
    }

    @Test
    void testCorrectViewForDeafultUrl() throws Exception {
        sendGetRequest("")
           .andExpect(status().is3xxRedirection())
           .andExpect(header().exists("Location"));
    }

    @Test
    void testCorrectViewForSelectAcctUrl() throws Exception {
        sendGetRequest(AcctController.SELECT_ACCT_PAGE_URL)
            .andExpect(status().isOk())
            .andExpect(isSelectAcctPage());
    }

    @Test
    void testCorrectViewForCreateCorrectParams() throws Exception {
        sendPostRequest(AcctController.CREATE_ACCT_URL,addParam(addParam(
            "balance", "1"), "holder", "ab"))
            .andExpect(status().isOk())
            .andExpect(isAcctPage())
            .andExpect(doesNotContainElements("span.error"));
    }

    @Test
    void testCreateAndChangeLang() throws Exception {
        sendPostRequest(AcctController.CREATE_ACCT_URL,addParam(addParam(
            "balance", "-1"), "holder", "ab"))
            .andExpect(status().isOk())
            .andExpect(isSelectAcctPage());
        sendGetRequest(AcctController.CREATE_ACCT_URL + "?lang=en")
            .andExpect(status().isOk())
            .andExpect(isSelectAcctPage());
    }

    @Test
    void testCorrectViewForCreateAcctTooShortHolder() throws Exception {
        sendPostRequest(AcctController.CREATE_ACCT_URL,addParam(addParam(
            "balance", "1"), "holder", "1"))
            .andExpect(status().isOk())
            .andExpect(isSelectAcctPage())
            .andExpect(containsErrorMsg("create-acct-holder", "2"));
    }

    @Test
    void testCorrectViewForCreateAcctMissingHolder() throws Exception {
        sendPostRequest(AcctController.CREATE_ACCT_URL,addParam(addParam(
            "balance", "1"), "holder", ""))
            .andExpect(status().isOk())
            .andExpect(isSelectAcctPage())
            .andExpect(containsErrorMsg("create-acct-holder", "specify " +
                                                              "account " +
                                                              "holder"));
    }

    @Test
    void testCorrectViewForCreateAcctWrongCharInHolder() throws Exception {
        sendPostRequest(AcctController.CREATE_ACCT_URL,addParam(addParam(
            "balance", "1"), "holder", "123"))
            .andExpect(status().isOk())
            .andExpect(isSelectAcctPage())
            .andExpect(containsErrorMsg("create-acct-holder", "only letters"));
    }

    @Test
    void testCorrectViewForCreateAcctTooLongHolder() throws Exception {
        sendPostRequest(AcctController.CREATE_ACCT_URL,addParam(addParam(
                "balance", "1"), "holder", "1234567890123456789012345678901"))
               .andExpect(status().isOk())
               .andExpect(isSelectAcctPage())
               .andExpect(containsErrorMsg("create-acct-holder", "30"));
    }

    @Test
    void testCorrectViewForCreateAcctMissingBalance() throws Exception {
        sendPostRequest(AcctController.CREATE_ACCT_URL,addParam("name", "12"))
            .andExpect(status().isOk())
            .andExpect(isSelectAcctPage())
            .andExpect(containsErrorMsg("create-balance", "specify balance"));
    }

    @Test
    void testCorrectViewForCreateAcctWrongCharInBalance() throws Exception {
        sendPostRequest(AcctController.CREATE_ACCT_URL,addParam(addParam(
            "balance", "a"), "holder", "abc"))
            .andExpect(status().isOk())
            .andExpect(isSelectAcctPage())
            .andExpect(containsErrorMsg("create-balance", "only numbers"));
    }

    @Test
    void testCorrectViewForCreateAcctNegativeBalance() throws Exception {
        sendPostRequest(AcctController.CREATE_ACCT_URL,addParam(addParam(
            "balance", "-1"), "name", "12"))
            .andExpect(status().isOk())
            .andExpect(isSelectAcctPage())
            .andExpect(containsErrorMsg("create-balance", "zero or greater"));
    }

    @Test
    void testCorrectViewForFindCorrectParams() throws Exception {
        sendPostRequest(AcctController.FIND_ACCT_URL,addParam(
            "number", "1"))
            .andExpect(status().isOk())
            .andExpect(isAcctPage())
            .andExpect(doesNotContainElements("span.error"));
    }

    @Test
    void testFindAndChangeLang() throws Exception {
        sendPostRequest(AcctController.FIND_ACCT_URL,addParam("number", "a"))
            .andExpect(status().isOk())
            .andExpect(isSelectAcctPage());
        sendGetRequest(AcctController.FIND_ACCT_URL + "?lang=en")
            .andExpect(status().isOk())
            .andExpect(isSelectAcctPage());
    }

    @Test
    void testCorrectViewForFindAcctMissingAcctNo() throws Exception {
        sendPostRequest(AcctController.FIND_ACCT_URL)
            .andExpect(status().isOk())
            .andExpect(isSelectAcctPage())
            .andExpect(containsErrorMsg("search-acct-number", "specify " +
                                                              "account " +
                                                              "number"));
    }

    @Test
    void testCorrectViewForFindAcctWrongCharInAcctNo() throws Exception {
        sendPostRequest(AcctController.FIND_ACCT_URL,addParam("number", "a"))
            .andExpect(status().isOk())
            .andExpect(isSelectAcctPage())
            .andExpect(containsErrorMsg("search-acct-number", "only numbers"));
    }

    @Test
    void testCorrectViewForFindAcctNegativeNumber() throws Exception {
        sendPostRequest(AcctController.FIND_ACCT_URL,addParam("number", "-1"))
            .andExpect(status().isOk())
            .andExpect(isSelectAcctPage())
            .andExpect(containsErrorMsg("search-acct-number", "greater than " +
                                                           "zero"));
    }

    @Test
    void testCorrectViewForDepositCorrectParams() throws Exception {
        sendPostRequest(AcctController.DEPOSIT_URL,addParam("amount", "1"))
            .andExpect(status().isOk())
            .andExpect(isAcctPage())
            .andExpect(doesNotContainElements("span.error"));
    }

    @Test
    void testDepositAndChangeLang() throws Exception {
        sendPostRequest(AcctController.DEPOSIT_URL,addParam("amount", "a"))
            .andExpect(status().isOk())
            .andExpect(isAcctPage());
        sendGetRequest(AcctController.DEPOSIT_URL + "?lang=en")
            .andExpect(status().isOk())
            .andExpect(isAcctPage());
    }

    @Test
    void testCorrectViewForDepositMissingAmt() throws Exception {
        sendPostRequest(AcctController.DEPOSIT_URL)
            .andExpect(status().isOk())
            .andExpect(isAcctPage())
            .andExpect(containsErrorMsg("deposit-amt", "specify amount"));
    }

    @Test
    void testCorrectViewForDepositWrongCharInAmt() throws Exception {
        sendPostRequest(AcctController.DEPOSIT_URL,addParam("amount", "a"))
            .andExpect(status().isOk())
            .andExpect(isAcctPage())
            .andExpect(containsErrorMsg("deposit-amt", "only numbers"));
    }

    @Test
    void testCorrectViewForDepositNegativeNumber() throws Exception {
        sendPostRequest(AcctController.DEPOSIT_URL,addParam("amount", "-1"))
            .andExpect(status().isOk())
            .andExpect(isAcctPage())
            .andExpect(containsErrorMsg("deposit-amt", "greater than zero"));
    }

    @Test
    void testCorrectViewForWithdrawCorrectParams() throws Exception {
        sendPostRequest(AcctController.WITHDRAW_URL,addParam("amount", "1"))
            .andExpect(status().isOk())
            .andExpect(isAcctPage())
            .andExpect(doesNotContainElements("span.error"));
    }

    @Test
    void testWithdrawAndChangeLang() throws Exception {
        sendPostRequest(AcctController.WITHDRAW_URL,addParam("amount", "a"))
            .andExpect(status().isOk())
            .andExpect(isAcctPage());
        sendGetRequest(AcctController.WITHDRAW_URL + "?lang=en")
            .andExpect(status().isOk())
            .andExpect(isAcctPage());
    }

    @Test
    void testCorrectViewForWithdrawMissingAmt() throws Exception {
        sendPostRequest(AcctController.WITHDRAW_URL)
            .andExpect(status().isOk())
            .andExpect(isAcctPage())
            .andExpect(containsErrorMsg("withdraw-amt", "specify amount"));
    }

    @Test
    void testCorrectViewForWithdrawWrongCharInAmt() throws Exception {
        sendPostRequest(AcctController.WITHDRAW_URL,addParam("amount", "a"))
            .andExpect(status().isOk())
            .andExpect(isAcctPage())
            .andExpect(containsErrorMsg("withdraw-amt", "only numbers"));
    }

    @Test
    void testCorrectViewForWithdrawNegativeNumber() throws Exception {
        sendPostRequest(AcctController.WITHDRAW_URL,addParam("amount", "-1"))
            .andExpect(status().isOk())
            .andExpect(isAcctPage())
            .andExpect(containsErrorMsg("withdraw-amt", "greater than zero"));
    }

    @Test
    void testCorrectViewForAcctUrl() throws Exception {
        sendGetRequest(AcctController.ACCT_PAGE_URL)
               .andExpect(status().isOk())
               .andExpect(isAcctPage());
    }

    private ResultMatcher containsErrorMsg(String idForErrorElem,
                                           String errorMsg) {
        return containsElements("input[id=" + idForErrorElem + "]" +
                                "~span.error:contains(" +
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

    private ResultActions sendGetRequest(String Url) throws Exception {
        return mockMvc.perform(get("/" + Url)); //no context path in Url
        // since we are not using any server.
    }

    private ResultActions sendPostRequest(String Url,
                                          MultiValueMap<String, String> params)
        throws Exception {
        return mockMvc.perform(post("/" + Url).params(params)); //no context
        // path in Url since we are not using any server.
    }

    private ResultActions sendPostRequest(String Url)
        throws Exception {
        return mockMvc.perform(post("/" + Url)); //no context
        // path in Url since we are not using any server.
    }

    private MultiValueMap<String, String> addParam(MultiValueMap<String,
            String> params, String name, String value) {
            params.add(name, value);
        return params;
    }

    private MultiValueMap<String, String> addParam(String name, String value) {
        LinkedMultiValueMap<String, String> params =
            new LinkedMultiValueMap<>();
            params.add(name, value);
        return params;
    }

}

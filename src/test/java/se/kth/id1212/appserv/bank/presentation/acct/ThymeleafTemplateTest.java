package se.kth.id1212.appserv.bank.presentation.acct;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import se.kth.id1212.appserv.bank.presentation.acct.AcctController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.kth.id1212.appserv.bank.presentation.Util.containsElements;


@SpringJUnitWebConfig(initializers = ConfigFileApplicationContextInitializer.class)
@EnableAutoConfiguration
@ComponentScan(basePackages = {"se.kth.id1212.appserv.bank"})
    //@SpringBootTest can be used instead of @SpringJUnitWebConfig,
    // @EnableAutoConfiguration and @ComponentScan, but are we using
    // JUnit5 in that case?
class ThymeleafTemplateTest {
    @Autowired
    private WebApplicationContext webappContext;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webappContext).build();
    }

    @Test
    void testHeadingIsIncluded() throws Exception {
        sendGetRequest(AcctController.ACCT_PAGE_URL)
            .andExpect(status().isOk())
            .andExpect(containsElements("head link[href$=bank.css]"));
    }

    @Test
    void testHeaderIsIncluded() throws Exception {
        sendGetRequest(AcctController.ACCT_PAGE_URL)
            .andExpect(status().isOk())
            .andExpect(containsElements("header img[src$=/logo.png]"));
    }

    @Test
    void testNavigationIsIncluded() throws Exception {
        sendGetRequest(AcctController.ACCT_PAGE_URL)
            .andExpect(status().isOk())
            .andExpect(containsElements("nav>ul>li>a"));
    }

    @Test
    void testFooterIsIncluded() throws Exception {
        sendGetRequest(AcctController.ACCT_PAGE_URL)
            .andExpect(status().isOk())
            .andExpect(containsElements("footer"));
    }

    @Test
    void testContentIsIncluded() throws Exception {
        sendGetRequest(AcctController.ACCT_PAGE_URL)
            .andExpect(status().isOk())
            .andExpect(containsElements("main>section>h1:contains(Account)"));
    }

    @Test
    void testSelectAccountPageHasAllFragments() throws Exception {
        sendGetRequest(AcctController.SELECT_ACCT_PAGE_URL)
            .andExpect(status().isOk())
            .andExpect(containsElements("head", "header", "nav", "main",
                                        "footer"));
    }

    @Test
    void testAccountPageHasAllFragments() throws Exception {
        sendGetRequest(AcctController.ACCT_PAGE_URL)
            .andExpect(status().isOk())
            .andExpect(containsElements("head", "header", "nav", "main",
                                        "footer"));
    }

    @Test
    void testCorrectLanguageIsUsed() throws Exception {
        sendGetRequest(AcctController.ACCT_PAGE_URL)
            .andExpect(status().isOk())
            .andExpect(containsElements("footer>span:contains(Phone)"));
        sendGetRequest(AcctController.ACCT_PAGE_URL + "?lang=sv")
            .andExpect(status().isOk())
            .andExpect(containsElements("footer>span:contains(Telefon)"));
    }

    private ResultActions sendGetRequest(String Url) throws Exception {
        return mockMvc.perform(get("/" + Url)); //no context path in Url
        // since we are not using any server.
    }
}

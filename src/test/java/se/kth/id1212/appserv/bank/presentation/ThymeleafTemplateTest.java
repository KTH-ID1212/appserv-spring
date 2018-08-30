package se.kth.id1212.appserv.bank.presentation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.kth.id1212.appserv.bank.presentation.HtmlMatcher.containsElement;
import static org.hamcrest.Matchers.allOf;

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
        mockMvc.perform(get("/account")) //no context root since we are
               // not using any server.
               .andExpect(status().isOk())
               .andExpect(content().string(containsElement(
                       "head link[href$=bank.css]")));
    }

    @Test
    void testHeaderIsIncluded() throws Exception {
        mockMvc.perform(get("/account")) //no context root since we are
               // not using any server.
               .andExpect(status().isOk())
               .andExpect(content().string(containsElement(
                       "header img[src$=/logo.png]")));
    }

    @Test
    void testNavigationIsIncluded() throws Exception {
        mockMvc.perform(get("/account")) //no context root since we are
               // not using any server.
               .andExpect(status().isOk())
               .andExpect(content().string(containsElement(
                       "nav>ul>li>a")));
    }

    @Test
    void testFooterIsIncluded() throws Exception {
        mockMvc.perform(get("/account")) //no context root since we are
               // not using any server.
               .andExpect(status().isOk())
               .andExpect(content().string(containsElement(
                       "footer")));
    }

    @Test
    void testContentIsIncluded() throws Exception {
        mockMvc.perform(get("/account")) //no context root since we are
               // not using any server.
               .andExpect(status().isOk())
               .andExpect(content().string(containsElement(
                       "main>section>h1:contains(Account)")));
    }

    @Test
    void testSelectAccountPageHasAllFragments() throws Exception {
        mockMvc.perform(get("/select-account")) //no context root since we are
               // not using any server.
               .andExpect(status().isOk())
               .andExpect(content().string(allOf(containsElement("head"),
                                                 containsElement("header"),
                                                 containsElement("nav"),
                                                 containsElement("main"),
                                                 containsElement("footer"))));
    }

    @Test
    void testAccountPageHasAllFragments() throws Exception {
        mockMvc.perform(get("/account")) //no context root since we are
               // not using any server.
               .andExpect(status().isOk())
               .andExpect(content().string(allOf(containsElement("head"),
                                                 containsElement("header"),
                                                 containsElement("nav"),
                                                 containsElement("main"),
                                                 containsElement("footer"))));
    }

    @Test
    void testCorrectLanguageIsUsed() throws Exception {
        mockMvc.perform(get("/account")) //no context root since we are
               // not using any server.
               .andExpect(status().isOk())
               .andExpect(content().string(containsElement("footer>span" +
                                                           ":contains(Phone)")));
        mockMvc.perform(get("/account?lang=sv")) //no context root since we are
               // not using any server.
               .andExpect(status().isOk())
               .andExpect(content().string(containsElement("footer>span" +
                                                           ":contains" +
                                                           "(Telefon)")));
    }
}

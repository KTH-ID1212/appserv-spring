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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(initializers = ConfigFileApplicationContextInitializer.class)
@EnableAutoConfiguration
@ComponentScan(basePackages = {"se.kth.id1212.appserv.bank"})
        //@SpringBootTest can be used instead of @SpringJUnitWebConfig,
        // @EnableAutoConfiguration and @ComponentScan, but are we using
        // JUnit5 in that case?
class AccountControllerTest {
    @Autowired
    private WebApplicationContext webappContext;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webappContext).build();
    }

    @Test
    void testCorrectViewForDeafultUrl() throws Exception {
        mockMvc.perform(get("/")) //no context root since we are not using any
               // server.
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("Search Account")))
               .andExpect(content().string(containsString("Create Account")));
    }

    @Test
    void testCorrectViewForSelectAcctUrl() throws Exception {
        mockMvc.perform(get("/select-account")) //no context root since we are
               // not using any server.
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("Search Account")))
               .andExpect(content().string(containsString("Create Account")));
    }

    @Test
    void testCorrectViewForAcctUrl() throws Exception {
        mockMvc.perform(get("/account")) //no context root since we are
               // not using any server.
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("Deposit")))
               .andExpect(content().string(containsString("Withdraw")));
    }
}

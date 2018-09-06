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
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static se.kth.id1212.appserv.bank.presentation.Util.containsElements;

@SpringJUnitWebConfig(initializers = ConfigFileApplicationContextInitializer.class)
@EnableAutoConfiguration
@ComponentScan(basePackages = {"se.kth.id1212.appserv.bank"})
    //@SpringBootTest can be used instead of @SpringJUnitWebConfig,
    // @EnableAutoConfiguration and @ComponentScan, but are we using
    // JUnit5 in that case?
class ExceptionHandlersTest {
    @Autowired
    private WebApplicationContext webappContext;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webappContext).build();
    }

    @Test
    void testExceptionIsThrown() throws Exception {
        sendGetRequest(ExceptionThrowingController.URL_THAT_THROWS_EXCEPTION)
            .andExpect(status().isInternalServerError())
            .andExpect(isGenericErrorPage());
    }

    private ResultActions sendGetRequest(String Url) throws Exception {
        return mockMvc.perform(get("/" + Url)); //no context path in Url
        // since we are not using any server.
    }

    private ResultMatcher isGenericErrorPage() {
        return containsElements("main h1:contains(Operation Failed)");
    }
}



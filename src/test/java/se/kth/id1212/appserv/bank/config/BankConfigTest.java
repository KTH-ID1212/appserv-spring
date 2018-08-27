package se.kth.id1212.appserv.bank.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(initializers = ConfigFileApplicationContextInitializer.class)
@EnableAutoConfiguration
@ComponentScan(basePackages = {"se.kth.id1212.appserv.bank"})
        //@SpringBootTest can be used instead of @SpringJUnitWebConfig,
        // @EnableAutoConfiguration and @ComponentScan, but are we using
        // JUnit5 in that case?
class BankConfigTest {
    @Autowired
    private BankConfig conf;
    @Autowired
    private ThymeleafViewResolver viewResolver;
    @Autowired
    @Qualifier("bankTemplateEngine")
    private SpringTemplateEngine templateEngine;
    @Autowired
    private SpringResourceTemplateResolver templateResolver;
    @Autowired
    private WebApplicationContext webappContext;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webappContext).build();
    }

    @Test
    void testCorrectServerPropsCreated() {
        assertEquals("/bank", conf.serverProperties().getContextRoot(),
                     "Wrong ServerProperties instance.");
    }

    @Test
    void testStaticResourceCanBeRead() throws Exception {
        mockMvc.perform(get("/logo.png")) //no context root since we are not
               // using any server.
               .andExpect(status().isOk()).andReturn().getResponse()
               .getContentType().equalsIgnoreCase("image/png");
    }

    @Test
    void correctTemplEngineIsUsed() {
        assertEquals(templateEngine, viewResolver.getTemplateEngine(),
                     "Wrong template engine.");
    }

    @Test
    void correctTemplResolverIsUsed() {
        assertTrue(((SpringTemplateEngine)viewResolver.getTemplateEngine())
                           .getTemplateResolvers().contains(templateResolver),
                   "Wrong template resolver.");
    }

}

package se.kth.id1212.appserv.bank.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
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
    private ThymeleafViewResolver viewResolver;
    @Autowired
    @Qualifier("bankTemplateEngine")
    private SpringTemplateEngine templateEngine;
    @Autowired
    private SpringResourceTemplateResolver templateResolver;
    @Autowired
    private WebApplicationContext webappContext;
    @Autowired
    private LocaleChangeInterceptor i18nBean;
    @Autowired
    private ReloadableResourceBundleMessageSource messageSource;
    private MockMvc mockMvc;

    @BeforeEach
    void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webappContext).build();
    }

    @Test
    void testCorrectServerPropsCreated(@Autowired BankConfig conf) {
        assertEquals("/bank", conf.serverProperties().getContextRoot(),
                     "Wrong ServerProperties instance.");
    }

    @Test
    void testStaticResourceCanBeRead() throws Exception {
        mockMvc.perform(get("/resources/fragments/header-imgs/logo.png")) //no
               // context root since we are not using any server.
               .andExpect(status().isOk()).andReturn().getResponse()
               .getContentType().equalsIgnoreCase("image/png");
    }

    @Test
    void testCorrectTemplEngineIsUsed() {
        assertEquals(templateEngine, viewResolver.getTemplateEngine(),
                     "Wrong template engine.");
    }

    @Test
    void testCorrectTemplResolverIsUsed() {
        assertTrue(((SpringTemplateEngine)viewResolver.getTemplateEngine())
                           .getTemplateResolvers().contains(templateResolver),
                   "Wrong template resolver.");
    }

    @Test
    void testCorrectI18nBeanCreated() {
        assertThat("Wrong properties in i18n bean.", i18nBean, allOf(
                hasProperty("paramName", equalTo("lang")),
                hasProperty("httpMethods", arrayContainingInAnyOrder(
                        "GET", "POST")),
                hasProperty("ignoreInvalidLocale", equalTo(true))));
    }

    @Test
    void testCorrectMsgSourceBeanCreated() {
        assertThat("Wrong properties in message source bean.", messageSource,
            hasProperty("basenameSet",
                containsInAnyOrder("classpath:/i18n/Messages",
                                   "classpath:/i18n/ValidationMessages")));
    }
}

package se.kth.id1212.appserv.bank;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context
        .ConfigFileApplicationContextInitializer;
import org.springframework.boot.web.embedded.tomcat
        .TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server
        .ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringJUnitWebConfig(initializers = ConfigFileApplicationContextInitializer
        .class)
@EnableAutoConfiguration
@ComponentScan(basePackages = {"se.kth.id1212.appserv.bank"})
        //@SpringBootTest can be used instead of @SpringJUnitWebConfig,
        // @EnableAutoConfiguration and @ComponentScan, but are we using
        // JUnit5 in that case?
class MainTest {
    @Autowired
    @Qualifier("getWebServerFactoryCustomizer")
    WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
            factoryCustomizer;

    @Test
    void testWebServerFactoryCustomizerCreation() {
        TomcatServletWebServerFactory factory =
                new TomcatServletWebServerFactory();
        factoryCustomizer.customize(factory);
        assertEquals("/bank", factory.getContextPath(),
                     "Wrong context root in server factory");
    }
}

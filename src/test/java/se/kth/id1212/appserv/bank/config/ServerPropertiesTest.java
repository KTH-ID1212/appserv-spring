package se.kth.id1212.appserv.bank.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context
        .ConfigFileApplicationContextInitializer;
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
class ServerPropertiesTest {
    @Autowired
    ServerProperties props;

    @Test
    void testContextRootIsReadFromAppConfig() {
        assertEquals("/bank", props.getContextRoot(), "Wrong context root");
    }
}

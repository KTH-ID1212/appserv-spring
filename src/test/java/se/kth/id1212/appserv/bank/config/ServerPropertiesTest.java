package se.kth.id1212.appserv.bank.config;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context
        .ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import se.kth.id1212.appserv.bank.repository.DbUtil;

import java.io.IOException;
import java.sql.SQLException;

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
    private ServerProperties props;

    @BeforeAll
    static void enableCreatingEMFWhichIsNeededForTheApplicationContext()
        throws SQLException, IOException, ClassNotFoundException {
        DbUtil.emptyDb();
    }

    @Test
    void testContextRootIsReadFromAppConfig() {
        assertEquals("/bank", props.getContextRoot(), "Wrong context root");
    }
}

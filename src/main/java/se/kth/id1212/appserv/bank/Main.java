package se.kth.id1212.appserv.bank;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server
        .ConfigurableServletWebServerFactory;
import se.kth.id1212.appserv.bank.config.ServerProperties;

/**
 * Starts the bank application.
 */
@SpringBootApplication
public class Main {
    @Autowired
    private ServerProperties serverProps;
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * Starts the bank application.
     *
     * @param args There are no command line parameters.
     */
    public static void main(String[] args) {
        LOGGER.debug("debug enabled: " + LOGGER.isDebugEnabled());
        LOGGER.debug("trace enabled: " + LOGGER.isTraceEnabled());
        LOGGER.debug("logger name: " + LOGGER.getName());
        ((ch.qos.logback.classic.Logger)LOGGER).setLevel(Level.TRACE);
        LOGGER.debug("debug enabled: " + LOGGER.isDebugEnabled());
        LOGGER.debug("trace enabled: " + LOGGER.isTraceEnabled());
        LOGGER.debug("logger name: " + LOGGER.getName());
        SpringApplication app = new SpringApplication(Main.class);
        app.setBanner((environment, sourceClass, out) -> {
            out.println("\n>>>>>>>>>>>>>> RUN IN TERMINAL TO SEE FRAMEWORK" +
                        "VERSIONS. USE 'mvn spring-boot:run' TO SHOW VERSIONS" +
                        " AND START. USE FOR EXAMPLE 'mvn dependency:resolve" +
                        " -Dsort' TO ONLY SEE DEPENDENCIES. " +
                        "<<<<<<<<<<<<<<<<<\n");
        });
        app.run(args);
    }

    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
    getWebServerFactoryCustomizer() {
        LOGGER.error("Setting WebServerFactory.");
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        return serverFactory -> {
            LOGGER.trace("Setting context root.");
            serverFactory.setContextPath(serverProps.getContextRoot());
        };
    }
}

package se.kth.id1212.appserv.bank;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server
        .ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import se.kth.id1212.appserv.bank.config.ServerProperties;

/**
 * Starts the bank application.
 */
@SpringBootApplication
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    @Autowired
    private ServerProperties serverProps;

    /**
     * Starts the bank application.
     *
     * @param args There are no command line parameters.
     */
    public static void main(String[] args) {
        LOGGER.debug("log level is set after app.run(args), the default " +
                     "level (debug) is used here: " + LOGGER.isTraceEnabled());
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

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
    getWebServerFactoryCustomizer() {
        LOGGER.trace("Setting WebServerFactory.");
        return serverFactory -> {
            LOGGER.trace("Setting context root.");
            serverFactory.setContextPath(serverProps.getContextRoot());
        };
    }
}

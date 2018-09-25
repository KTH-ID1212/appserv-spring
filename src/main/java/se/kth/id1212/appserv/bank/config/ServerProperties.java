package se.kth.id1212.appserv.bank.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Contains properties from application.properities, starting with 'se.kth
 * .id1212.server'.
 */
@ConfigurationProperties(prefix = "se.kth.id1212.bank.server")
public class ServerProperties {
    private static final Logger LOGGER = LoggerFactory.getLogger(
            ServerProperties.class);
    private String contextRoot;

    /**
     * @return The context root of the web site.
     */
    public String getContextRoot() {
        LOGGER.trace("Reading context root {}.", contextRoot);
        return contextRoot;
    }

    /**
     * @param contextRoot The new context root of the web site.
     */
    public void setContextRoot(String contextRoot) {
        LOGGER.trace("Setting context root {}.", contextRoot);
        this.contextRoot = contextRoot;
    }
}

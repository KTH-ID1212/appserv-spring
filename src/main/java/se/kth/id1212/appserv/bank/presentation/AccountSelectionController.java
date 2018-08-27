package se.kth.id1212.appserv.bank.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Handles all HTTP requests to context root.
 */
@Controller
public class AccountSelectionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(
            AccountSelectionController.class);

    @GetMapping("/")
    public String defaultView() {
        LOGGER.trace("Call to context root.");
        return "bank";
    }
}

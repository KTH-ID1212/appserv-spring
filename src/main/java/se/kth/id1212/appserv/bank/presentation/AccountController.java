package se.kth.id1212.appserv.bank.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Handles all HTTP requests to context root.
 */
@Controller
public class AccountController {
    private static final Logger LOGGER = LoggerFactory.getLogger(
            AccountController.class);

    @GetMapping("/")
    public String defaultView() {
        LOGGER.trace("Call to context root.");
        return "select-account";
    }

    @GetMapping("/select-account")
    public String accountSelectionView() {
        LOGGER.trace("Call to account selection view.");
        return "select-account";
    }

    @GetMapping("/account")
    public String accountView() {
        LOGGER.trace("Call to account view.");
        return "account";
    }
}

package se.kth.id1212.appserv.bank.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles all HTTP requests to context root.
 */
@Controller
@ResponseBody
public class ContextRootController {
    private static final Logger LOGGER = LoggerFactory.getLogger(
            ContextRootController.class);

    @GetMapping("/")
    public String defaultView() {
        LOGGER.trace("Call to context root.");
        return "The bank app is up!!";
    }
}

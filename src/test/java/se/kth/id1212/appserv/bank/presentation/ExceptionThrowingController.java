package se.kth.id1212.appserv.bank.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Throws exceptions in order to facilitate testing exception handling.
 */
@Controller
public class ExceptionThrowingController {
    static final String URL_THAT_THROWS_EXCEPTION = "throw-exception";

    /**
     * Used for exception handling tests.
     *
     * @throws Exception
     */
    @GetMapping("/" + URL_THAT_THROWS_EXCEPTION)
    public void throwException() throws Exception {
        throw new Exception("This is the exception message");
    }
}

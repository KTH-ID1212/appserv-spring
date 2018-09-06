package se.kth.id1212.appserv.bank.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Contains all exception handling methods.
 */
@ControllerAdvice
public class ExceptionHandlers {
    static final String GENERIC_ERROR_PAGE_URL = "failure";
    private static final Logger LOGGER =
        LoggerFactory.getLogger(ExceptionHandlers.class);

    /**
     * The most generic exception handler, will be used if there is not a more
     * specific handler for the exception that shall be handled.
     *
     * @return The generic error page.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception exception) {
        logException(exception);
        return GENERIC_ERROR_PAGE_URL;
    }

    private void logException(Exception exception) {
        LOGGER.error("Exception handler got {}: {}",
                     exception.getClass().getName(), exception.getMessage(),
                     exception);
    }
}

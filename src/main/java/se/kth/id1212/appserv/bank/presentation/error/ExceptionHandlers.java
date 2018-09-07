package se.kth.id1212.appserv.bank.presentation.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Contains all exception handling methods.
 */
@Controller
@ControllerAdvice
public class ExceptionHandlers implements ErrorController {
    static final String ERROR_PATH = "failure";
    private static final String ERROR_PAGE_URL = "error";
    private static final String ERROR_TYPE_KEY = "errorType";
    private static final String GENERIC_ERROR_TYPE = "generic";
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
    public String handleException(Exception exception, Model model) {
        logException(exception);
        model.addAttribute(ERROR_TYPE_KEY, GENERIC_ERROR_TYPE);
        return ERROR_PAGE_URL;
    }

    private void logException(Exception exception) {
        LOGGER.error("Exception handler got {}: {}",
                     exception.getClass().getName(), exception.getMessage(),
                     exception);
    }

    @GetMapping("/" + ERROR_PATH)
    public String handleHttpError(HttpServletRequest request,
                                  HttpServletResponse response, Model model) {
        LOGGER.debug("Http error handler got Http status: {}",
                     request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
        String statusCode = extractHttpStatusCode(request);
        model.addAttribute(ERROR_TYPE_KEY, statusCode);
        response.setStatus(Integer.parseInt(statusCode));
        return ERROR_PAGE_URL;
    }

    private String extractHttpStatusCode(HttpServletRequest request) {
        return request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)
                      .toString();
    }

    // This method is never called. Could that be a bug in spring? I can not
    // find any call to an appropriate method named getErrorPath anywhere in
    // spring's source code. The path is instead set in application.properties,
    // in the property server.error.path. For this to work, there
    // must also be the property setting server.error.whitelabel.enabled=false.
    @Override
    public String getErrorPath() {
        return "/" + ERROR_PATH;
    }
}

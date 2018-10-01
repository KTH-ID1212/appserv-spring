package se.kth.id1212.appserv.bank.presentation.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import se.kth.id1212.appserv.bank.domain.IllegalBankTransactionException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Contains all exception handling methods.
 */
@Controller
@ControllerAdvice
public class ExceptionHandlers implements ErrorController {
    public static final String ERROR_PAGE_URL = "error";
    public static final String ERROR_TYPE_KEY = "errorType";
    public static final String ERROR_INFO_KEY = "errorInfo";
    public static final String GENERIC_ERROR = "Operation Failed";
    public static final String GENERIC_ERROR_INFO = "Sorry, it didn't work. Please try again.";
    public static final String ACCT_NOT_FOUND = "No Account Found";
    public static final String ACCT_NOT_FOUND_INFO = "There is no account with the specified number.";
    public static final String DEPOSIT_FAILED = "Deposit Failed";
    public static final String DEPOSIT_FAILED_INFO = "It was not possible to deposit the specified amount to the specified account.";
    public static final String WITHDRAWAL_FAILED = "Withdrawal Failed";
    public static final String WITHDRAWAL_FAILED_INFO = "It was not possible to withdraw the specified amount from the specified account.";
    public static final String HTTP_404 = "The page could not be found";
    public static final String HTTP_404_INFO = "Sorry, but there is no such page. We would like to fix this error, please tell us what you where trying to do.";
    static final String ERROR_PATH = "failure";

    /**
     * Exception handler for broken business rules.
     *
     * @return An appropriate error page.
     */
    @ExceptionHandler(IllegalBankTransactionException.class)
    @ResponseStatus(HttpStatus.OK)
    public String handleException(IllegalBankTransactionException exception, Model model) {
        if (exception.getMessage().toUpperCase().contains("DEPOSIT")) {
            model.addAttribute(ERROR_TYPE_KEY, DEPOSIT_FAILED);
            model.addAttribute(ERROR_INFO_KEY, DEPOSIT_FAILED_INFO);
        } else if (exception.getMessage().toUpperCase().contains("WITHDRAW")) {
            model.addAttribute(ERROR_TYPE_KEY, WITHDRAWAL_FAILED);
            model.addAttribute(ERROR_INFO_KEY, WITHDRAWAL_FAILED_INFO);
        } else {
            model.addAttribute(ERROR_TYPE_KEY, GENERIC_ERROR);
            model.addAttribute(ERROR_INFO_KEY, GENERIC_ERROR_INFO);
        }
        return ERROR_PAGE_URL;
    }

    /**
     * The most generic exception handler, will be used if there is not a more specific handler for the exception that
     * shall be handled.
     *
     * @return The generic error page.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception exception, Model model) {
        model.addAttribute(ERROR_TYPE_KEY, GENERIC_ERROR);
        return ERROR_PAGE_URL;
    }

    @GetMapping("/" + ERROR_PATH)
    public String handleHttpError(HttpServletRequest request, HttpServletResponse response, Model model) {
        int statusCode = Integer.parseInt(extractHttpStatusCode(request));
        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            model.addAttribute(ERROR_TYPE_KEY, HTTP_404);
            model.addAttribute(ERROR_INFO_KEY, HTTP_404_INFO);
            response.setStatus(statusCode);
        } else {
            model.addAttribute(ERROR_TYPE_KEY, GENERIC_ERROR);
            model.addAttribute(ERROR_INFO_KEY, GENERIC_ERROR_INFO);
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ERROR_PAGE_URL;
    }

    private String extractHttpStatusCode(HttpServletRequest request) {
        return request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString();
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

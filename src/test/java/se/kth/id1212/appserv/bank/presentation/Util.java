package se.kth.id1212.appserv.bank.presentation;

import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * Functional library for testing the presentation layer.
 */
public class Util {
    private Util() {}

    /**
     * Creates a <code>org.springframework.test.web.servlet.ResultMatcher</code>
     * which verifies that the specified elements exist in the page content.
     *
     * @param cssSelectors CSS selectors identifying the HTML elements that
     *                     shall exist.
     * @return The desired matcher.
     */
    public static ResultMatcher containsElements(String... cssSelectors) {
        List<Matcher<? super String>> matchers = new ArrayList<>();
        for (String selector : cssSelectors) {
            matchers.add(HtmlMatchers.containsElement(selector));
        }
        return content().string(AllOf.allOf(matchers));
    }

    /**
     * Creates a <code>org.springframework.test.web.servlet.ResultMatcher</code>
     * which verifies that the specified elements exist in the page content.
     *
     * @param cssSelectors CSS selectors identifying the HTML elements that
     *                     shall exist.
     * @return The desired matcher.
     */
    public static ResultMatcher doesNotContainElements(String... cssSelectors) {
        List<Matcher<? super String>> matchers = new ArrayList<>();
        for (String selector : cssSelectors) {
            matchers.add(HtmlMatchers.doesNotContainElement(selector));
        }
        return content().string(AllOf.allOf(matchers));
    }
}

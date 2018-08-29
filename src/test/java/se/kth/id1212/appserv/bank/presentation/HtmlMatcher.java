package se.kth.id1212.appserv.bank.presentation;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Creates hamcrest matchers for an html document.
 */
public class HtmlMatcher extends TypeSafeMatcher<String> {
    private static final String HTML_START = "<html";
    private static final String HTML_END = "</html>";
    private final String cssSelector;

    HtmlMatcher(String cssSelector) {
        this.cssSelector = cssSelector;
    }

    /**
     * Creates a matcher that matches when the examined html document contains
     * one or more elements with the specified css selector.
     *
     * @param cssSelector What to search for in the examined html document.
     */
    static HtmlMatcher containsElement(String cssSelector) {
        return new HtmlMatcher(cssSelector);
    }

    @Override
    protected boolean matchesSafely(String httpResponse) {
        String html = extractHtmlDoc(httpResponse);
        Document htmlDoc = Jsoup.parse(html);
        Elements elements = htmlDoc.select(cssSelector);
        return !elements.isEmpty();
    }

    private String extractHtmlDoc(String httpResponse) {
        int htmlStartPos = httpResponse.indexOf(HTML_START);
        int htmlEndPos = httpResponse.indexOf(HTML_END) + HTML_END.length();
        return httpResponse.substring(htmlStartPos, htmlEndPos);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(
                "document contains element matching \"" + cssSelector + "\"");
    }
}

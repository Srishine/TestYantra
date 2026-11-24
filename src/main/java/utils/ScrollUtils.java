
package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ScrollUtils {

    private WebDriver driver;
    private JavascriptExecutor js;

    public ScrollUtils(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
    }

    // Scroll to a specific element
    public void scrollToElement(WebElement element) {
        js.executeScript(
            "arguments[0].scrollIntoView({block:'center'});", element
        );
    }

    // Scroll down
    public void scrollDown() {
        js.executeScript("window.scrollBy(0, 500);");
    }

    // Scroll up
    public void scrollUp() {
        js.executeScript("window.scrollBy(0, -500);");
    }

    // Scroll to page bottom
    public void scrollToBottom() {
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "window.scrollTo(0, document.body.scrollHeight);"
        );
    }

    // Scroll to page top
    public void scrollToTop() {
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
    }

    // Scroll by custom pixels
    public void scrollByPixels(int x, int y) {
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "window.scrollBy(arguments[0], arguments[1]);", x, y
        );
    }
}

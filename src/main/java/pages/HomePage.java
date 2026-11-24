package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {
    private static final By PROFILE_ICON = By.cssSelector(
            "button[aria-label*='profile' i], a[aria-label*='profile' i], "
                    + "[role='button'][aria-label*='profile' i], "
                    + "button[title*='profile' i], a[title*='profile' i]");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void clickProfileIcon() {
        wait.until(ExpectedConditions.elementToBeClickable(PROFILE_ICON)).click();
    }
}

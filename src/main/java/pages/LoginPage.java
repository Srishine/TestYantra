package pages;

import java.time.Duration;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {
    private static final Logger log = Logger.getLogger(LoginPage.class.getName());
    private static final By LOGIN_BUTTON =
            By.cssSelector("div > div:nth-child(2) > a.c-btn.c-btn--secondary.c-btn--header");
    private static final By LOGIN_USERNAME_FIELD = By.name("username");
    private static final By PASSWORD_FIELD = By.xpath("//input[@name='password']");
    private static final By SUBMIT_BUTTON =
            By.cssSelector("form.o-wrapper--huge button[type='submit']");
    private static final By LOGIN_FORM = By.cssSelector("form.o-wrapper--huge");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoginButtonPresent() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON));
            return true;
        } catch (TimeoutException e) {
            log.info("Login button was not available within 5 seconds; skipping login.");
            return false;
        }
    }

    public void login(String nickname, String password) {
        wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_USERNAME_FIELD))
                .sendKeys(nickname);
        wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD_FIELD))
                .sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(SUBMIT_BUTTON)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOGIN_FORM));
        log.info("Logged in to the application");
    }
}

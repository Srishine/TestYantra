package pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import java.util.logging.Logger;

public class RegistrationPage extends BasePage {
    private static final Logger log = Logger.getLogger(RegistrationPage.class.getName());
	
    private By RegistrationButton = By.xpath("//a[text()='Register']");
    private By CookieBanner = By.id("onetrust-banner-sdk");
    private By AcceptAllCookiesButton = By.id("onetrust-accept-btn-handler");
    private By EmailField = By.xpath("//input[@name='email']");
    private By NicknameField = By.xpath("//input[@name='nickname']");
    private By PasswordField = By.xpath("//input[@name='password']");
    private By SecurityQuesModelPassword = By.xpath("//input[@name='new-password']");
    private By DayField = By.xpath("//*[@name='day']");
    private By MonthField = By.xpath("//*[@name='month']");
    private By YearField = By.xpath("//*[@name='year']");
    private By CaptchaFrame = By.cssSelector(
            "iframe[title='reCAPTCHA'], iframe[title='reCAPTCHA checkbox'], iframe[src*='recaptcha']");
    private By CaptchaCheckbox = By.cssSelector("#recaptcha-anchor, .recaptcha-checkbox-border");
    private By TermsCheckbox = By.cssSelector("#termsAccept");   
    private By BeginAdventureButton = By.cssSelector("div > div > form > div:nth-child(2) > div > button");
    private By ConfirmationPrompt = By.cssSelector("body > div.c-dialogs > div > div > div > div > div");
    private By CloseEmailConfirmationPopup = By.cssSelector(
            "body > div.c-dialogs > div > div > div > header > div > "
                    + "div.o-box__item.o-box__item--large.c-btn--close > a > i");
    private By EmailVerifiedConfirmation = By.xpath(
            "//*[contains(., 'Your e-mail address has been confirmed')]");
    

    public RegistrationPage(WebDriver driver) {
        super(driver);
    }
    public void acceptAllCookiesIfPresent() {
        List<WebElement> banners = driver.findElements(CookieBanner);
        if (banners.isEmpty() || !banners.get(0).isDisplayed()) {
            return;
        }

        WebElement acceptButton =
                wait.until(ExpectedConditions.elementToBeClickable(AcceptAllCookiesButton));
        try {
            acceptButton.click();
        } catch (ElementClickInterceptedException exception) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", acceptButton);
        }
        wait.until(ExpectedConditions.invisibilityOfElementLocated(CookieBanner));
        log.info("Accepted all cookies");
    }
     
     public void ClickOnRegisterButton() {
        wait.until(ExpectedConditions.elementToBeClickable(RegistrationButton)).click();
        log.info("Clicked on register button");
     }
      
     public boolean isRegistrationFormDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(EmailField)).isDisplayed();
     }

     public void enterEmail(String email) {
        driver.findElement(EmailField).sendKeys(email);
        log.info("Entered email: " + email);
     }

     public void EnterNickname(String nickname) {
        driver.findElement(NicknameField).sendKeys(nickname);
        log.info("Entered nickname: " + nickname);
     }

     public void EnterPassword(String password) {
        driver.findElement(PasswordField).sendKeys(password);
        log.info("Entered password: " + password);
     }

     public void EnterPasswordSecurityModel(String passwordSecurityModel) {
        driver.findElement(SecurityQuesModelPassword).sendKeys(passwordSecurityModel);
        log.info("Entered security model password");
     }

     public void EnterDateOfBirth(String day, String month, String year) {
        EnterDay(day);
        EnterMonth(month);
        EnterYear(year);
        log.info("Entered date of birth: {}/{}".format(day, month, year));
     }

     public void EnterDay(String day) {
        driver.findElement(DayField).sendKeys(day);
        log.info("Entered day: " + day);
     }

     public void EnterMonth(String month) {
        driver.findElement(MonthField).sendKeys(month);
        log.info("Entered month: " + month);
     }

     public void EnterYear(String year) {
        driver.findElement(YearField).sendKeys(year);
        log.info("Entered year: " + year);
     }

     public void ClickOnCaptcha() {
        List<WebElement> captchaFrames = driver.findElements(CaptchaFrame);
        if (captchaFrames.isEmpty()) {
            return;
        }

        try {
            final int CAPTCHA_TIMEOUT = 120;
            for (WebElement captchaFrame : captchaFrames) {
                if (!captchaFrame.isDisplayed()) {
                    continue;
                }

                driver.switchTo().frame(captchaFrame);
                WebElement checkbox = wait.until(
                        ExpectedConditions.visibilityOfElementLocated(CaptchaCheckbox));
                if (checkbox.isDisplayed()) {
                    try {
                        ((JavascriptExecutor) driver).executeScript(
                                "arguments[0].scrollIntoView({block:'center'});", checkbox);
                        checkbox.click();
                    } catch (ElementClickInterceptedException exception) {
                        ((JavascriptExecutor) driver).executeScript(
                                "arguments[0].click();", checkbox);
                    }
                    return;
                }
                driver.switchTo().defaultContent();
            }
        } finally {
            driver.switchTo().defaultContent();
        }
     }

     public void ClickOnTermsCheckbox() {    
         driver.switchTo().defaultContent();
         WebElement termsCheckbox =
                 wait.until(ExpectedConditions.presenceOfElementLocated(TermsCheckbox));
         if (!termsCheckbox.isSelected()) {
             ((JavascriptExecutor) driver).executeScript(
                     "arguments[0].click();", termsCheckbox);
                      log.info("Clicked on terms checkbox");
         }
	}

    public void ClickOnBeginAdventureButton() {
        acceptAllCookiesIfPresent();
        WebElement beginAdventureButton =
                wait.until(ExpectedConditions.elementToBeClickable(BeginAdventureButton));
        try {
            beginAdventureButton.click();
        } catch (ElementClickInterceptedException exception) {
            acceptAllCookiesIfPresent();
            wait.until(ExpectedConditions.elementToBeClickable(BeginAdventureButton)).click();
        }
        log.info("Clicked on begin adventure button");
    }

   public boolean isConfirmationPromptIsDisplayed(){
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(3)).until(
                    ExpectedConditions.visibilityOfElementLocated(ConfirmationPrompt)
            ).isDisplayed();
        } catch (TimeoutException exception) {
            return false;
        }
    }

    public void closeEmailConfirmationPopup() {
        wait.until(ExpectedConditions.elementToBeClickable(CloseEmailConfirmationPopup)).click();
        log.info("Closed email confirmation popup");
    }

    public boolean isEmailVerificationConfirmed() {
        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(EmailVerifiedConfirmation))
                    .isDisplayed();
        } catch (TimeoutException exception) {
            return false;
        }
    }
    
}
	


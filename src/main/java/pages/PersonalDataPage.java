package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;
import java.time.Duration;
import java.util.Random;
import java.util.logging.Logger;
import utils.ScrollUtils;

public class PersonalDataPage extends BasePage {
    private static final Logger log = Logger.getLogger(PersonalDataPage.class.getName());
    private static final Duration OPTIONAL_CLICK_WAIT = Duration.ofSeconds(5);
    private final ScrollUtils scroll;

    private By ProfileIcon = By.cssSelector(".c-header__grid .c-bar-status__username");   
    private By PersonalDataLink = By.cssSelector("div > div > div:nth-child(4) > a > i");
    private By closeWheelPopup = By.cssSelector("div.c-dialogs > div > div > div > div > div.c-wheel > a");
    private By Collectcoins = By.cssSelector("div.c-dialogs > div > div > div > div > a");
    private By NotNowRadioButton = By.cssSelector("input[type='radio'][value='not_now']");
    private By OptOutBenefitsRadioButton = By.cssSelector("input[type='radio'][value='opt_out']");
    private By ConfirmButton = By.xpath("button[type='submit']");
    private By ChangeSecurityQuestionButton = By.cssSelector("form > div > div:nth-child(2) > div > div:nth-child(2) > button");
    private By SecurityQuestionDropdown = By.cssSelector("body > div.c-dialogs > div > div > div > form > div > div:nth-child(2) > div > div > select");
    private By SecurityQuestionAnswerField = By.xpath("//input[@name='security-answer']");
    private By SaveChangesButton = By.cssSelector("button[type='submit']");
    private By CloseSecurityPopUp = By.cssSelector("div.o-box__item.o-box__item--large.c-btn--close > a > i");
    private By SecurityQuestionConfirmationMessage = By.xpath("//div[contains(text(),'The security question and answer have been changed.')]");
    private By NewsLetterChangeButton = By.cssSelector("tr.even > td:nth-child(4) > button");
    private By NewsLetterModelPopUp = By.cssSelector("body > div.c-dialogs > div > div > div > header");
    private By NewsLetterModelPopUpGreatButton = By.cssSelector("div.o-grid.c-registration-offer__action-wrapper > div:nth-child(2)");
    private By NewsLetterConsentChangeAcceptText = By.cssSelector("#table-gdpr-status > tbody > tr.even > td:nth-child(2)");
    private By LogoutButton = By.cssSelector("div.c-sidemenu__menu > div > div > div:nth-child(8) > a");
    
// Constructor
     public PersonalDataPage(WebDriver driver){
        super(driver);
        this.scroll = new ScrollUtils(driver);
    }   
    
    public void clickProfileIconIfPresent() {
        Duration waitDuration = Duration.ofSeconds(3);
        try {
            new WebDriverWait(driver, waitDuration)
                    .until(ExpectedConditions.elementToBeClickable(ProfileIcon))
                    .click();
            log.info("Clicked on profile icon");
        } catch (TimeoutException | NoSuchElementException e) {
            log.info("Profile icon was not present or clickable within 3 seconds. Skipping.");
        }
    }

    public void isPersonalDataFormDisplayed(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(PersonalDataLink)).isDisplayed();
        log.info("Personal data form is displayed");
    }

    public void clickOnPersonalDataLinkIfPresent() {
        clickIfPresent(PersonalDataLink, "personal data link");
    }

    private void clickIfPresent(By locator, String elementName) {
        try {
            WebElement element = new WebDriverWait(driver, OPTIONAL_CLICK_WAIT)
                    .until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
            log.info("Clicked on " + elementName);
        } catch (TimeoutException | NoSuchElementException e) {
            log.info(elementName + " was not present or clickable within "
                    + OPTIONAL_CLICK_WAIT.getSeconds() + " seconds. Skipping.");
        }
    }
   
    public void closeWheelPopupIfPresent() {
    try {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement popup = shortWait.until(
                ExpectedConditions.visibilityOfElementLocated(closeWheelPopup)        
        );

        WebElement closepopup = shortWait.until(
                ExpectedConditions.visibilityOfElementLocated(closeWheelPopup)
        );

        WebElement collectCoins = shortWait.until(
                ExpectedConditions.visibilityOfElementLocated(Collectcoins)
        );

        if (popup.isDisplayed()) {
            shortWait.until(ExpectedConditions.elementToBeClickable(closeWheelPopup)).click();
            log.info("Wheel of Fortune popup displayed");

        if (closepopup.isDisplayed()) {
             shortWait.until(ExpectedConditions.elementToBeClickable(closepopup)).click();
             log.info("Wheel of Fortune popup closed");

        if (collectCoins.isDisplayed()) {
             shortWait.until(ExpectedConditions.elementToBeClickable(collectCoins)).click();
             log.info("Wheel of Fortune popup closed");    
           }
         } 
       }  
    }         
     catch (TimeoutException e) {
       log.info("Wheel of Fortune popup not displayed or could not be closed. Continuing test.");

     } catch (org.openqa.selenium.NoSuchElementException e) {
        log.info("Wheel of Fortune popup elements not found. Continuing test.");
       }
    }

    public void clickChangeSecurityQuestionButton(){
       try {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement popup = shortWait.until(
                ExpectedConditions.visibilityOfElementLocated(ChangeSecurityQuestionButton)        
        );    
        wait.until(ExpectedConditions.visibilityOfElementLocated(ChangeSecurityQuestionButton)).click();
        log.info("Security question button is displayed");

        wait.until(ExpectedConditions.elementToBeClickable(ChangeSecurityQuestionButton));
        log.info("Security question button is displayed");
    }

    catch (TimeoutException e) {
       log.info("Security question button is displayed. Continuing test.");

     } catch (org.openqa.selenium.NoSuchElementException e) {
        log.info("WSecurity question button is displayed. Continuing test.");
       }
    }

    public void clickSecurityQuestionDropdown() {
        WebDriverWait dropdownWait = new WebDriverWait(driver, Duration.ofSeconds(8));
        try {
            dropdownWait.until(ExpectedConditions.elementToBeClickable(SecurityQuestionDropdown)).click();
            log.info("Security question dropdown is displayed");

            dropdownWait.until(ExpectedConditions.visibilityOfElementLocated(SecurityQuestionDropdown));
            dropdownWait.until(currentDriver ->
                    new Select(currentDriver.findElement(SecurityQuestionDropdown))
                            .getOptions().size() > 1);

            Select select = new Select(
                    dropdownWait.until(ExpectedConditions.elementToBeClickable(SecurityQuestionDropdown)));
            int totalOptions = select.getOptions().size();
            int randomIndex = 1 + new Random().nextInt(totalOptions - 1);
            select.selectByIndex(randomIndex);
            log.info("Selected Security Question: " + select.getFirstSelectedOption().getText());
        } catch (TimeoutException | NoSuchElementException e) {
            log.severe("Unable to select a security question within 8 seconds: " + e.getMessage());
            throw e;
        }
    }

    public void selectNotNowRadioButtonIfPresent() {
        clickIfPresent(NotNowRadioButton, "Not Now / Remind Me Later radio button");
    }

    public void enterSecurityAnswer(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(SecurityQuestionAnswerField)).isDisplayed();
        log.info("Security question answer field is displayed");
        wait.until(ExpectedConditions.elementToBeClickable(SecurityQuestionAnswerField)).sendKeys("Test");
        log.info("Security question answer entered");
    }
        
      public void clickSaveChangesButton(){
        wait.until(ExpectedConditions.elementToBeClickable(SaveChangesButton)).click();
        log.info("Clicked on Save Changes button");

      }

      public void closeWSecurityQuestionPopUp(){
        wait.until(ExpectedConditions.elementToBeClickable(CloseSecurityPopUp)).click();
        log.info("Clicked on Close icon Security Question PopUp ");
      }

     public boolean isSecurityQuestionUpdated(){
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(SecurityQuestionConfirmationMessage)).isDisplayed();
            log.info("Security question and answer have been changed.");
            return true;
        } catch (TimeoutException e) {
            log.info("Security question and answer update confirmation message not displayed.");
            return false;
        }

     }

     public void selectOptOutBenefitsRadioButtonIfPresent() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.elementToBeClickable(OptOutBenefitsRadioButton))
                    .click();
            log.info("Selected Opt Out Benefits radio button");
        } catch (TimeoutException | NoSuchElementException e) {
            log.info("Opt Out Benefits radio button was not present or clickable within "
                    + "3 seconds. Skipping.");
        }
     }

     public void clickConfirmButton(){
       WebElement confirmButton =
               wait.until(ExpectedConditions.visibilityOfElementLocated(ConfirmButton));
       scroll.scrollToElement(confirmButton);
       try {
           wait.until(ExpectedConditions.elementToBeClickable(confirmButton)).click();
       } catch (ElementClickInterceptedException exception) {
           ((JavascriptExecutor) driver).executeScript(
                   "arguments[0].click();", confirmButton);
       }
        log.info("Clicked on Confirm button");
     }

     public void clickCollectButtonIfPresent(){
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(15));
            shortWait.until(ExpectedConditions.elementToBeClickable(Collectcoins)).click();
            log.info("Clicked on Collect Coins button");
            
        } catch (TimeoutException | NoSuchElementException e) {
            log.info("Collect Coins button was not present. Continuing test.");
        }
     }

     public void clickNewsletterConsent(){

        scroll.scrollToElement(wait.until(ExpectedConditions.visibilityOfElementLocated(NewsLetterChangeButton)));

        wait.until(ExpectedConditions.elementToBeClickable(NewsLetterChangeButton)).click();
        log.info("Clicked on Newsletter Consent Change button");

        wait.until(ExpectedConditions.visibilityOfElementLocated(NewsLetterModelPopUp)).isDisplayed();
        log.info("Newsletter Consent Model PopUp is displayed");

        wait.until(ExpectedConditions.elementToBeClickable(NewsLetterModelPopUpGreatButton)).click();
        log.info("Clicked on Newsletter Consent Model PopUp Great button");

        wait.until(ExpectedConditions.visibilityOfElementLocated(NewsLetterConsentChangeAcceptText)).isDisplayed();
        log.info("Newsletter Consent Change Accept Text is displayed");
     }

     public boolean isNewsletterConsentAccepted(){
        WebDriverWait newsletterWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        String consentText = newsletterWait.until(
                ExpectedConditions.visibilityOfElementLocated(NewsLetterConsentChangeAcceptText)
        ).getText().trim();
        log.info("Newsletter consent status: " + consentText);
        return "Accepted".equalsIgnoreCase(consentText);
     }

        public void clickLogoutButton(){
            wait.until(ExpectedConditions.elementToBeClickable(LogoutButton)).click();
            log.info("Clicked on Logout button");
        }
     }
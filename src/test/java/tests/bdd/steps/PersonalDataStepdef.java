package tests.bdd.steps;

import io.cucumber.java.en.*;
import java.util.logging.Logger;
import base.DriverFactory;
import org.junit.Assert;
import org.openqa.selenium.TimeoutException;
import pages.PersonalDataPage;
import pages.RegistrationPage;

public class PersonalDataStepdef {

    private static final Logger log = Logger.getLogger(PersonalDataStepdef.class.getName());
    private PersonalDataPage personalDataPage;
    private RegistrationPage registrationPage;

    public PersonalDataStepdef() {
        this.personalDataPage = new PersonalDataPage(DriverFactory.getDriver());
        this.registrationPage = new RegistrationPage(DriverFactory.getDriver());
    }

     @And("i click on profile icon")
    public void iClickOnProfileIcon() {
        personalDataPage.clickProfileIconIfPresent();
    }

    @And("i click on personal data link")
    public void clickOnPersonalDataLink() {
        log.info("Clicking on Personal Data link");
        personalDataPage.clickOnPersonalDataLinkIfPresent();
    }

    @And("i close wheel of fortune popup")
    public void closeWheelOfFortunePopup() {
        log.info("Closing Wheel of Fortune popup");
        personalDataPage.closeWheelPopupIfPresent();
    }

    @Then("i close Security Question popUp")
    public void closeSecurityQuestionPopUp(){
        personalDataPage.closeWSecurityQuestionPopUp();
    }

    @And("i click on not now remind me later radio button")
    public void clickNotNowRadioButton() {
        log.info("Selecting Not Now / Remind Me Later option");
        personalDataPage.selectNotNowRadioButtonIfPresent();
    }

    @And("i click on i opt out benifits radio button")
    public void clickOptOutBenefitsRadioButton() {
        log.info("Selecting Opt Out Benefits option");
        personalDataPage.selectOptOutBenefitsRadioButtonIfPresent();
    }

    @Then("i click on confirm button")
    public void clickOnConfirmButton(){        
        log.info("Clicking Confirm button");
        personalDataPage.clickConfirmButton();
    }

    @Then("i click on collect button")
    public void clickCollectButton() {
        log.info("Clicking Collect Coins button");
        personalDataPage.clickCollectButtonIfPresent();
    }

    @Then("i click on change security question button")
    public void clickChangeSecurityQuestionButton() {
        log.info("Clicking Change Security Question button");
        personalDataPage.clickChangeSecurityQuestionButton();
    }

    @And("i select a security question from the drop down")
    public void selectSecurityQuestion() {
        log.info("Selecting security question");
        personalDataPage.clickSecurityQuestionDropdown();
    }

    @And("i enter an answer for the security question")
    public void enterSecurityAnswer() {
        log.info("Entering security question answer");
        personalDataPage.enterSecurityAnswer();
    }

    @And("I enter password {string}")
    public void enterPassword(String password) {
        log.info("Entering account password");
        registrationPage.EnterPassword(password);
    }

    @Then("i click on Save Changes button")
    public void clickSaveChangesButton() {
        log.info("Clicking Save Changes button");
        personalDataPage.clickSaveChangesButton();
    }

    @And("verify that The security question and answer have been changed")
    public void verifySecurityQuestionAndAnswerChanged() {
        log.info("Verifying security question and answer update");
        Assert.assertTrue(
            "Security question and answer were not updated successfully",
            personalDataPage.isSecurityQuestionUpdated()
        );
    }

    /*@Then("close popup")
    public void closePopup() {
        log.info("Closing confirmation popup");
        personalDataPage.closeConfirmationPopup();
    }
*/
    @And("i click on news letter consent")
    public void clickNewsletterConsent() {
        log.info("Clicking Newsletter Consent");
        personalDataPage.clickNewsletterConsent();
    }

    @Then("i verify Accept text for news letter consent")
    public void verifyAcceptTextForNewsletterConsent() {
        try {
            Assert.assertTrue(
                    "Newsletter consent status was not Accept",
                    personalDataPage.isNewsletterConsentAccepted());
        } catch (TimeoutException | AssertionError e) {
            log.severe("Newsletter consent verification failed: " + e.getMessage());
            throw e;
        }
    }

    @Then("i click on logout button")
    public void clickLogoutButton() {
        log.info("Clicking Logout button");
        personalDataPage.clickLogoutButton();
    }
}

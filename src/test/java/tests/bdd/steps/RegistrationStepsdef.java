package tests.bdd.steps;

import base.DriverFactory;
import config.ConfigReader;
import constants.FrameworkConstants;
import data.RegistrationTestData;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.logging.Logger;
import org.junit.Assert;
import pages.MailTmPage;
import api.MailTmClient;
import api.Mailbox;
import pages.HomePage;
import pages.RegistrationPage;
import utils.JsonUtils;
import java.util.UUID;

public class RegistrationStepsdef {
    private static final Logger log = Logger.getLogger(RegistrationStepsdef.class.getName());
    private HomePage homePage;
    private RegistrationPage registrationPage;
    private RegistrationTestData registrationData;
    private Mailbox mailbox;

    @Given("I generate registration test data")
    public void generateRegistrationTestData() throws Exception {
        MailTmClient client = new MailTmClient();
        mailbox = client.createMailbox();

        registrationData = new RegistrationTestData();
        registrationData.setEmail(mailbox.getEmail());
        registrationData.setNickname(
                "player" + UUID.randomUUID().toString().substring(0, 3));
        registrationData.setPassword("GameTwist@123");
        registrationData.setDay("12");
        registrationData.setMonth("June");
        registrationData.setYear("1991");

        JsonUtils.writeJson(
                registrationData,
                FrameworkConstants.GENERATED_REGISTRATION_DATA_PATH);

        log.info("Registration data generated and saved");
        log.info("Email: " + registrationData.getEmail());
        log.info("Nickname: " + registrationData.getNickname());
    }

    @Given("i am on the GameTwist home page")
    public void iAmOnTheGameTwistHomePage() {
        Assert.assertTrue(
                "The browser did not open the configured home page",
                DriverFactory.getDriver().getCurrentUrl()
                        .startsWith(ConfigReader.getProperty("base.url")));
        homePage = new HomePage(DriverFactory.getDriver());
        registrationPage = new RegistrationPage(DriverFactory.getDriver());
    }

    @When("i accept all cookies")
    public void iAcceptAllCookies() {
        registrationPage.acceptAllCookiesIfPresent();
    }

    @And("i dismiss browser notification alert")
    public void dismissBrowserNotificationAlert() {
        registrationPage.dismissBrowserNotificationPrompt();
        log.info("Blocked browser notifications for the current site");
    }

    @When("i choose to register")
    public void iChooseToRegister() {
        registrationPage.ClickOnRegisterButton();
    }

    @Then("the registration form is displayed")
    public void theRegistrationFormIsDisplayed() {
        Assert.assertTrue(
                "The registration email field was not displayed",
                registrationPage.isRegistrationFormDisplayed());
    }

    @When("I enter registration details from JSON")
    public void enterRegistrationDetailsFromJSON() {
        registrationData = JsonUtils.readJson(
                FrameworkConstants.GENERATED_REGISTRATION_DATA_PATH,
                RegistrationTestData.class);
        registrationPage.enterEmail(registrationData.getEmail());
        registrationPage.EnterNickname(registrationData.getNickname());
        registrationPage.EnterPassword(registrationData.getPassword());
        registrationPage.EnterDay(registrationData.getDay());
        registrationPage.EnterMonth(registrationData.getMonth());
        registrationPage.EnterYear(registrationData.getYear());
        log.info("Registration details entered from JSON");
    }


    @When("i enter email {string}")
    public void iEnterEmail(String email) {
        registrationPage.enterEmail(email);
    }

    @And("i enter nickname {string}")
    public void iEnterNickname(String nickname) {
        registrationPage.EnterNickname(nickname);
    }

    @And("i enter password {string}")
    public void iEnterPassword(String password) {
        registrationPage.EnterPassword(password);
    }
    
    @And("i enter Enter Password Security Model")
    public void iEnterPasswordSecurityModel() {
        if (registrationData == null) {
            registrationData = JsonUtils.readJson(
                    FrameworkConstants.GENERATED_REGISTRATION_DATA_PATH,
                    RegistrationTestData.class);
        }
        if (registrationData.getPassword() == null) {
            throw new IllegalStateException(
                    "Registration data does not contain a security model password");
        }
        registrationPage.EnterPasswordSecurityModel(registrationData.getPassword());
    }

    @And("i enter day {string}")
    public void iEnterDay(String day) {
        registrationPage.EnterDay(day);
    }

    @And("i enter month {string}")
    public void iEnterMonth(String month) {
        registrationPage.EnterMonth(month);
    }

    @And("i enter year {string}")
    public void iEnterYear(String year) {
        registrationPage.EnterYear(year);
    }

    @And("i click captcha")
    public void iClickCaptcha() {
    registrationPage.ClickOnCaptcha();
 }

    @And("i click terms checkbox")
    public void iClickTermsCheckbox() {
    registrationPage.ClickOnTermsCheckbox();
}

    @And("i click begin adventure button")
    public void iClickBeginAdventureButton() {
    registrationPage.ClickOnBeginAdventureButton(); 
  }

    @Then("Verify email from API")
    public void verifyEmailFromApi() throws Exception {
        Assert.assertNotNull("Registration mailbox was not created", mailbox);
        MailTmPage mailTmPage = new MailTmPage(new MailTmClient(), mailbox);
        MailTmClient.MailMessage message = mailTmPage.waitForEmail();
        String verificationUrl = mailTmPage.getEmailVerificationUrl(message);
        DriverFactory.getDriver().get(verificationUrl);
        Assert.assertTrue(
                "The registration email verification was not confirmed",
                registrationPage.isEmailVerificationConfirmed());
        log.info("Registration email verified successfully");
    }

     @Then("Verify email confirmation text")
    public void theConfirmationPromptIsDisplayed() {
        Assert.assertTrue(
                "Your e-mail address has been confirmed. You can log in now.",
                registrationPage.isConfirmationPromptIsDisplayed());
    }
    
    @And("i close email confirmation popup")
    public void closeEmailConfirmationPopUp(){
        registrationPage.closeEmailConfirmationPopup();
    }
}
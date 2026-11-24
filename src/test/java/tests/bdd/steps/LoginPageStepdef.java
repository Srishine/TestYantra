package tests.bdd.steps;

import constants.FrameworkConstants;
import data.RegistrationTestData;
import io.cucumber.java.en.Then;
import pages.LoginPage;
import base.DriverFactory;
import utils.JsonUtils;

public class LoginPageStepdef {
    private final LoginPage loginPage;

    public LoginPageStepdef() {
        loginPage = new LoginPage(DriverFactory.getDriver());
    }

    @Then("i login to application")
    public void iLoginToApplication() {
        if (!loginPage.isLoginButtonPresent()) {
            return;
        }

        RegistrationTestData registrationData = JsonUtils.readJson(
                FrameworkConstants.GENERATED_REGISTRATION_DATA_PATH,
                RegistrationTestData.class);
        loginPage.login(registrationData.getNickname(), registrationData.getPassword());
    }
}

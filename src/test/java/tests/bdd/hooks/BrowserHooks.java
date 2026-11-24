package tests.bdd.hooks;

import base.DriverFactory;
import config.ConfigReader;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class BrowserHooks {
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(DriverFactory::quitAllDrivers));
    }

    @Before
    public void startBrowser() {
        WebDriver driver = DriverFactory.getDriverIfPresent();
        if (driver == null) {
            driver = DriverFactory.createDriver(ConfigReader.getProperty("browser"));
            driver.manage().window().maximize();
            driver.get(ConfigReader.getProperty("base.url"));
        }
    }

   /*@Before
p   public void setupMailbox() throws Exception {
    MailTmClient client = new MailTmClient();
    Mailbox mailbox = client.createMailbox();
    scenarioEmail = mailbox.getEmail();
    }
*/

    @After
    public void stopBrowser(Scenario scenario) {
        WebDriver driver = DriverFactory.getDriverIfPresent();
        if (scenario.isFailed() && driver instanceof TakesScreenshot screenshotDriver) {
            scenario.attach(
                    screenshotDriver.getScreenshotAs(OutputType.BYTES),
                    "image/png",
                    "failure");
        }
    }

    @AfterAll
    public static void quitBrowser() {
        DriverFactory.quitAllDrivers();
    }
}

package base;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class DriverFactory {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final Set<WebDriver> ACTIVE_DRIVERS = ConcurrentHashMap.newKeySet();

    private DriverFactory() {
    }

    public static WebDriver createDriver(String browser) {
        if (DRIVER.get() != null) {
            throw new IllegalStateException("A WebDriver is already active on this thread.");
        }

        WebDriver driver;
        switch (browser.trim().toLowerCase(Locale.ROOT)) {
            case "chrome":
                driver = new ChromeDriver(new ChromeOptions());
                break;
            case "firefox":
                driver = new FirefoxDriver(new FirefoxOptions());
                break;
            case "edge":
                driver = new EdgeDriver(new EdgeOptions());
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
        DRIVER.set(driver);
        ACTIVE_DRIVERS.add(driver);
        return driver;
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver has not been created for this thread.");
        }
        return driver;
    }

    public static WebDriver getDriverIfPresent() {
        return DRIVER.get();
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        try {
            if (driver != null) {
                quitAndForget(driver);
            }
        } finally {
            DRIVER.remove();
        }
    }

    public static void quitAllDrivers() {
        for (WebDriver driver : ACTIVE_DRIVERS) {
            quitAndForget(driver);
        }
        DRIVER.remove();
    }

    private static void quitAndForget(WebDriver driver) {
        try {
            driver.quit();
        } catch (RuntimeException exception) {
            System.err.println("Unable to quit WebDriver cleanly: " + exception.getMessage());
        } finally {
            ACTIVE_DRIVERS.remove(driver);
        }
    }
}

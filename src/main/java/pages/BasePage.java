package pages;

import java.time.Duration;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chromium.HasCdp;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitUtils;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = Objects.requireNonNull(driver, "driver must not be null");
        this.wait = WaitUtils.explicitWait(
                driver,
                Duration.ofSeconds(Integer.parseInt(
                        config.ConfigReader.getProperty("explicit.wait.seconds"))));
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void dismissBrowserNotificationPrompt() {
        if (!(driver instanceof HasCdp cdpDriver)) {
            throw new UnsupportedOperationException(
                    "Dismissing the browser notification prompt requires a Chromium-based browser.");
        }

        URI currentUri = URI.create(driver.getCurrentUrl());
        String scheme = currentUri.getScheme();
        String host = currentUri.getHost();
        if (host == null || !("http".equalsIgnoreCase(scheme)
                || "https".equalsIgnoreCase(scheme))) {
            throw new IllegalStateException(
                    "Cannot set notification permission for the current page: "
                            + driver.getCurrentUrl());
        }

        String origin = scheme + "://" + host
                + (currentUri.getPort() == -1 ? "" : ":" + currentUri.getPort());
        cdpDriver.executeCdpCommand(
                "Browser.setPermission",
                Map.of(
                        "origin", origin,
                        "permission", Map.of("name", "notifications"),
                        "setting", "denied"));
    }
}

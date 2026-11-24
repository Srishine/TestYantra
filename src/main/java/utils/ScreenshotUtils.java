package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public final class ScreenshotUtils {
    private static final DateTimeFormatter FILE_TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    private ScreenshotUtils() {
    }

    public static Path capture(WebDriver driver, String name, Path outputDirectory)
            throws IOException {
        Objects.requireNonNull(driver, "driver must not be null");
        Objects.requireNonNull(outputDirectory, "outputDirectory must not be null");
        if (!(driver instanceof TakesScreenshot)) {
            throw new IllegalArgumentException("The current WebDriver does not support screenshots.");
        }
        String safeName = name.replaceAll("[^A-Za-z0-9._-]", "_");
        if (safeName.isEmpty()) {
            throw new IllegalArgumentException("Screenshot name must not be blank.");
        }
        Files.createDirectories(outputDirectory);
        Path screenshot = outputDirectory.resolve(
                safeName + "-" + LocalDateTime.now().format(FILE_TIMESTAMP) + ".png");
        byte[] image = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        return Files.write(screenshot, image);
    }

    public static Path capture(WebDriver driver, String name) throws IOException {
        return capture(driver, name, Paths.get("test-output", "screenshots"));
    }
}

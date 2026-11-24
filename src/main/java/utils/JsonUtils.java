
package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    // Write Java object into JSON
    public static void writeJson(Object data, String filePath) {

        try {
            File file = new File(filePath);

            file.getParentFile().mkdirs();

            mapper.writerWithDefaultPrettyPrinter()
                  .writeValue(file, data);

            System.out.println("JSON data saved successfully");

        } catch (IOException e) {
            throw new RuntimeException("Unable to write JSON data", e);
        }
    }

    // Read JSON into Java object
    public static <T> T readJson(String filePath, Class<T> clazz) {

        try {
            return mapper.readValue(new File(filePath), clazz);

        } catch (IOException e) {
            throw new RuntimeException("Unable to read JSON data", e);
        }
    }

    public static <T> T readJsonResource(String resourcePath, Class<T> clazz) {
        try (InputStream input = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IllegalArgumentException(
                        "JSON resource was not found on the classpath: " + resourcePath);
            }
            return mapper.readValue(input, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read JSON resource: " + resourcePath, e);
        }
    }
}

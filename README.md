# GameTwistAutomationFramework

Java 17 test automation framework using Maven, Selenium, Cucumber BDD, JUnit,
and the Page Object Model. Jackson is available for JSON test data.

## Requirements

- JDK 17 or newer
- Maven 3.8 or newer
- Chrome, Firefox, or Edge for browser tests

Selenium Manager resolves the browser driver automatically.

## Configuration and execution

Edit `src/test/resources/config.properties` for the target URL and browser, then
run the suite from this directory:

```sh
mvn test
```

Cucumber scenarios are in `src/test/resources/features/`, step definitions are
under `src/test/java/tests/bdd/steps/`, and browser setup/teardown is managed by
Cucumber hooks. The current scenario opens the registration form and verifies
that its email field is visible; it does not submit a registration.

Run the Cucumber suite with `mvn test`. The HTML and JSON reports are written to
`target/cucumber.html` and `target/cucumber.json`.

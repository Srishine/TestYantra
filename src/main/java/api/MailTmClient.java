
package api;

import io.restassured.response.Response;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import java.util.UUID;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static io.restassured.RestAssured.given;

/*
 * This class communicates with the Mail.tm API to create an email account and access its inbox.
 * Responsible for API communication.
 * - Fetches available email domains.
 * - Generates email and password.
 * - Creates a mailbox through API.
 * - Generates an authentication token.
- Fetches incoming emails.
*/
public class MailTmClient {

    private static final String BASE_URL = "https://api.mail.tm";
    private static final int MAX_RATE_LIMIT_RETRIES = 2;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public record MailMessage(String id, String subject, String intro) {
    }

    public record MailMessageContent(String text, List<String> html) {
    }

    private static final class RateLimitedException extends RuntimeException {
        private final Duration retryAfter;

        private RateLimitedException(Duration retryAfter) {
            super("Mail.tm rate limited the inbox request");
            this.retryAfter = retryAfter;
        }
    }

    public Mailbox createMailbox() throws Exception {

        // Get available domain
        Response domainResponse = requestWithRateLimitRetry(
                () -> given()
                        .baseUri(BASE_URL)
                        .when()
                        .get("/domains"),
                "/domains");

        domainResponse.then().statusCode(200);

        JsonNode root = MAPPER.readTree(domainResponse.asString());
        JsonNode members = root.path("hydra:member");
        if (!members.isArray() || members.isEmpty()) {
            throw new IllegalStateException("No active Mail.tm domain found");
        }

        String domain = members.get(0).path("domain").asText();
        if (domain == null || domain.isBlank()) {
            throw new IllegalStateException("No domain available");
        }

        // Generate unique email
        String username = "qa" + UUID.randomUUID()
                .toString().replace("-", "").substring(0, 12);

        String email = username + "@" + domain;
        String password = "Qa@" + UUID.randomUUID();

        // Create account
        Map<String, String> credentials = Map.of(
                "address", email,
                "password", password);
        Response accountResponse = requestWithRateLimitRetry(
                () -> given()
                        .baseUri(BASE_URL)
                        .contentType("application/json")
                        .body(credentials)
                        .when()
                        .post("/accounts"),
                "/accounts");

        accountResponse.then().statusCode(201);

        String accountId = accountResponse.jsonPath()
                .getString("id");

        // Generate token
        Response tokenResponse = requestWithRateLimitRetry(
                () -> given()
                        .baseUri(BASE_URL)
                        .contentType("application/json")
                        .body(credentials)
                        .when()
                        .post("/token"),
                "/token");

        tokenResponse.then().statusCode(200);

        String token = tokenResponse.jsonPath()
                .getString("token");

        return new Mailbox(email, password, token, accountId);
    }

    private static Response requestWithRateLimitRetry(
            Supplier<Response> request, String endpoint) throws InterruptedException {
        Response response = request.get();
        for (int retry = 0; response.statusCode() == 429 && retry < MAX_RATE_LIMIT_RETRIES; retry++) {
            Duration delay = retryDelay(response);
            Thread.sleep(delay.toMillis());
            response = request.get();
        }
        if (response.statusCode() == 429) {
            throw new IllegalStateException(
                    "Mail.tm rate limited " + endpoint + " after "
                            + (MAX_RATE_LIMIT_RETRIES + 1) + " attempts"
                            + retryAfterDescription(response));
        }
        return response;
    }

    private static String retryAfterDescription(Response response) {
        String retryAfter = response.getHeader("Retry-After");
        return retryAfter == null || retryAfter.isBlank()
                ? ""
                : "; Retry-After: " + retryAfter;
    }

    public List<MailMessage> getInboxMessages(Mailbox mailbox) throws Exception {
        Objects.requireNonNull(mailbox, "mailbox must not be null");
        if (mailbox.getToken() == null || mailbox.getToken().isBlank()) {
            throw new IllegalArgumentException("Mailbox token must not be blank");
        }

        Response response = given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + mailbox.getToken())
                .when()
                .get("/messages");
        if (response.statusCode() == 429) {
            throw new RateLimitedException(retryDelay(response));
        }
        response.then().statusCode(200);

        JsonNode members = MAPPER.readTree(response.asString()).path("hydra:member");
        if (!members.isArray()) {
            throw new IllegalStateException("Mail.tm inbox response did not contain a message list");
        }

        List<MailMessage> messages = new ArrayList<>();
        for (JsonNode message : members) {
            messages.add(new MailMessage(
                    message.path("id").asText(),
                    message.path("subject").asText(),
                    message.path("intro").asText()));
        }
        return messages;
    }

    public MailMessageContent getMessageContent(Mailbox mailbox, String messageId)
            throws Exception {
        Objects.requireNonNull(mailbox, "mailbox must not be null");
        if (mailbox.getToken() == null || mailbox.getToken().isBlank()) {
            throw new IllegalArgumentException("Mailbox token must not be blank");
        }
        if (messageId == null || messageId.isBlank()) {
            throw new IllegalArgumentException("Message id must not be blank");
        }

        Response response = given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + mailbox.getToken())
                .when()
                .get("/messages/{id}", messageId);
        response.then().statusCode(200);

        JsonNode message = MAPPER.readTree(response.asString());
        JsonNode htmlNode = message.path("html");
        List<String> html = new ArrayList<>();
        if (htmlNode.isArray()) {
            for (JsonNode part : htmlNode) {
                if (part.isTextual() && !part.asText().isBlank()) {
                    html.add(part.asText());
                }
            }
        } else if (htmlNode.isTextual() && !htmlNode.asText().isBlank()) {
            html.add(htmlNode.asText());
        }

        return new MailMessageContent(message.path("text").asText(""), List.copyOf(html));
    }

    public MailMessage waitForFirstMessage(
            Mailbox mailbox, Duration timeout, Duration pollInterval) throws Exception {
        Objects.requireNonNull(timeout, "timeout must not be null");
        Objects.requireNonNull(pollInterval, "pollInterval must not be null");
        if (timeout.isZero() || timeout.isNegative()) {
            throw new IllegalArgumentException("timeout must be positive");
        }
        if (pollInterval.isZero() || pollInterval.isNegative()) {
            throw new IllegalArgumentException("pollInterval must be positive");
        }

        long deadline = System.nanoTime() + timeout.toNanos();
        RateLimitedException lastRateLimit = null;
        while (System.nanoTime() < deadline) {
            List<MailMessage> messages = null;
            try {
                messages = getInboxMessages(mailbox);
                lastRateLimit = null;
            } catch (RateLimitedException exception) {
                lastRateLimit = exception;
            }
            if (messages != null && !messages.isEmpty()) {
                return messages.get(0);
            }

            long remainingNanos = deadline - System.nanoTime();
            if (remainingNanos <= 0) {
                break;
            }
            long requestedDelay = lastRateLimit == null
                    ? pollInterval.toNanos()
                    : Math.max(pollInterval.toNanos(), lastRateLimit.retryAfter.toNanos());
            long sleepNanos = Math.min(requestedDelay, remainingNanos);
            Thread.sleep(
                    sleepNanos / 1_000_000,
                    (int) (sleepNanos % 1_000_000));
        }

        if (lastRateLimit != null) {
            throw new IllegalStateException(
                    "Mail.tm continued to rate-limit inbox requests for " + timeout,
                    lastRateLimit);
        }

        throw new TimeoutException(
                "No email arrived in the Mail.tm inbox within " + timeout);
    }

    private static Duration retryDelay(Response response) {
        String retryAfter = response.getHeader("Retry-After");
        if (retryAfter != null && !retryAfter.isBlank()) {
            try {
                long seconds = Long.parseLong(retryAfter);
                if (seconds > 0) {
                    return Duration.ofSeconds(seconds);
                }
            } catch (NumberFormatException ignored) {
                // Retry-After may contain an HTTP date instead of seconds.
            }

            try {
                Instant retryAt = ZonedDateTime.parse(
                        retryAfter, DateTimeFormatter.RFC_1123_DATE_TIME).toInstant();
                Duration delay = Duration.between(Instant.now(), retryAt);
                if (!delay.isNegative() && !delay.isZero()) {
                    return delay;
                }
            } catch (DateTimeParseException ignored) {
                // Use the bounded fallback below for an invalid header.
            }
        }
        return Duration.ofSeconds(30);
    }
}

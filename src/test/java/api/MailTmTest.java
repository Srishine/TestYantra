
package api;

import constants.FrameworkConstants;
import utils.JsonUtils;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import org.junit.Assume;
import org.junit.Test;

/*
Creates a temporary email account.
Generates a unique email address for every registration test.
Retrieves an authentication token from Mail.tm.
Fetches incoming emails.
Finds the registration verification email.
Extracts the verification link or token.
Passes the link/token to the verification process.
*/

public class MailTmTest {
    private static final Logger log = Logger.getLogger(MailTmTest.class.getName());

    private record MailTmTestData(
            String email,
            String mailboxPassword,
            String accountId,
            String nickname,
            String registrationPassword) {
    }

    @Test
    public void createTemporaryEmail() throws Exception {
        Assume.assumeTrue(
                "Run with -Dmailtm.integration.enabled=true to call the live Mail.tm API",
                Boolean.getBoolean("mailtm.integration.enabled"));

        MailTmClient client = new MailTmClient();
        Mailbox mailbox = client.createMailbox();
        String registrationPassword = "TestPass123";
        String nickname = "Nick"
                + ThreadLocalRandom.current().nextInt(100, 1_000);

        JsonUtils.writeJson(
                new MailTmTestData(
                        mailbox.getEmail(),
                        mailbox.getPassword(),
                        mailbox.getAccountId(),
                        nickname,
                        registrationPassword),
                FrameworkConstants.MAIL_TM_TEST_DATA_PATH);

        log.info("Generated Mail.tm test data saved to "
                + FrameworkConstants.MAIL_TM_TEST_DATA_PATH);
        log.info("Temporary email: " + mailbox.getEmail());
        log.info("Account ID: " + mailbox.getAccountId());
        log.info("Nickname: " + nickname);
    }
}

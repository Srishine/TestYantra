package pages;

import api.MailTmClient;
import api.Mailbox;
import config.ConfigReader;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MailTmPage {
    private static final Duration EMAIL_TIMEOUT = Duration.ofMinutes(2);
    private static final Duration POLL_INTERVAL = Duration.ofSeconds(30);
    private static final Pattern URL_PATTERN = Pattern.compile("https?://[^\\s<>\"']+");

    private final MailTmClient mailTmClient;
    private final Mailbox mailbox;

    public MailTmPage(MailTmClient mailTmClient, Mailbox mailbox) {
        this.mailTmClient = Objects.requireNonNull(mailTmClient, "mailTmClient must not be null");
        this.mailbox = Objects.requireNonNull(mailbox, "mailbox must not be null");
    }

    public MailTmClient.MailMessage waitForEmail() throws Exception {
        return mailTmClient.waitForFirstMessage(mailbox, EMAIL_TIMEOUT, POLL_INTERVAL);
    }

    public String getEmailVerificationUrl(MailTmClient.MailMessage message) throws Exception {
        Objects.requireNonNull(message, "message must not be null");
        MailTmClient.MailMessageContent content =
                mailTmClient.getMessageContent(mailbox, message.id());
        URI baseUri = URI.create(ConfigReader.getProperty("base.url"));
        String baseHost = baseUri.getHost();
        String trustedHost = baseHost != null && baseHost.startsWith("www.")
                ? baseHost.substring(4)
                : baseHost;
        List<String> trustedLinks = new ArrayList<>();

        for (String html : content.html()) {
            Document document = Jsoup.parse(html);
            for (Element link : document.select("a[href]")) {
                String url = trustedVerificationUrl(
                        baseUri, trustedHost, link.attr("href"));
                if (url != null) {
                    String surroundingText = link.parent() == null
                            ? link.text()
                            : link.parent().text();
                    if (isVerificationLink(url, link.text())
                            || isVerificationLink(url, surroundingText)) {
                        return url;
                    }
                    trustedLinks.add(url);
                }
            }
        }

        Matcher matcher = URL_PATTERN.matcher(content.text());
        while (matcher.find()) {
            String candidate = matcher.group().replaceAll("[),.;]+$", "");
            String url = trustedVerificationUrl(baseUri, trustedHost, candidate);
            if (url != null) {
                if (isVerificationLink(url, candidate)) {
                    return url;
                }
                trustedLinks.add(url);
            }
        }

        if (trustedLinks.size() == 1) {
            return trustedLinks.get(0);
        }
        throw new IllegalStateException(
                "Could not identify a GameTwist email-verification link in the message");
    }

    private static String trustedVerificationUrl(
            URI baseUri, String trustedHost, String href) {
        if (trustedHost == null || href == null || href.isBlank()) {
            return null;
        }
        try {
            URI uri = baseUri.resolve(href.trim());
            String host = uri.getHost();
            if (!"https".equalsIgnoreCase(uri.getScheme())
                    || host == null
                    || !(host.equalsIgnoreCase(trustedHost)
                            || host.toLowerCase(Locale.ROOT)
                                    .endsWith("." + trustedHost.toLowerCase(Locale.ROOT)))) {
                return null;
            }
            return uri.toString();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static boolean isVerificationLink(String url, String linkText) {
        String candidate = (url + " " + linkText).toLowerCase(Locale.ROOT);
        return candidate.contains("verify")
                || candidate.contains("confirm")
                || candidate.contains("activate")
                || candidate.matches(".*(?:token|code|key)=[^&\\s]+.*");
    }
}

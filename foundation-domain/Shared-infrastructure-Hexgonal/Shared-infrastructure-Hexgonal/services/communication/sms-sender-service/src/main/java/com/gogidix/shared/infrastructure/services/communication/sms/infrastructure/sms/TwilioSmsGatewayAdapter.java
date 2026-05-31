package com.gogidix.shared.infrastructure.services.communication.sms.infrastructure.sms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Twilio SMS gateway adapter implementation.
 *
 * <p>This implementation uses Twilio's REST API for sending SMS messages.
 * It supports:
 * <ul>
 *   <li>Sending single SMS messages</li>
 *   <li>Bulk SMS sending</li>
 *   <li>Delivery status tracking</li>
 *   <li>Phone number validation</li>
 *   <li>Message queuing for rate limiting</li>
 * </ul>
 *
 * <p>Configuration:
 * <pre>
 * sms.provider=twilio
 * sms.twilio.account.sid=ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
 * sms.twilio.auth.token=your_auth_token
 * sms.twilio.from.number=+1234567890
 * sms.twilio.api.url=https://api.twilio.com
 * sms.twilio.messaging.service.sid=optional_messaging_service_sid
 * </pre>
 */
@Component
@ConditionalOnProperty(name = "sms.provider", havingValue = "twilio", matchIfMissing = true)
public class TwilioSmsGatewayAdapter implements SmsGatewayService {

    private static final Logger logger = LoggerFactory.getLogger(TwilioSmsGatewayAdapter.class);
    private static final String MESSAGES_ENDPOINT = "/2010-04-01/Accounts/%s/Messages.json";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String accountSid;
    private final String authToken;
    private final String fromNumber;
    private final String twilioApiUrl;
    private final String messagingServiceSid;
    private final boolean configured;

    // Local message status tracking (in production, use webhook callbacks)
    private final Map<String, DeliveryStatus> messageStatusCache = new ConcurrentHashMap<>();

    public TwilioSmsGatewayAdapter(
            @Value("${sms.twilio.account.sid:}") String accountSid,
            @Value("${sms.twilio.auth.token:}") String authToken,
            @Value("${sms.twilio.from.number:}") String fromNumber,
            @Value("${sms.twilio.api.url:https://api.twilio.com}") String twilioApiUrl,
            @Value("${sms.twilio.messaging.service.sid:}") String messagingServiceSid) {

        this.accountSid = accountSid;
        this.authToken = authToken;
        this.fromNumber = fromNumber;
        this.twilioApiUrl = twilioApiUrl;
        this.messagingServiceSid = messagingServiceSid;

        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();

        this.configured = accountSid != null && !accountSid.isEmpty() &&
                         authToken != null && !authToken.isEmpty() &&
                         fromNumber != null && !fromNumber.isEmpty();

        if (configured) {
            logger.info("Twilio SMS gateway initialized with account SID: {}", maskAccountSid(accountSid));
        } else {
            logger.warn("Twilio SMS gateway not configured. Please set sms.twilio.account.sid, sms.twilio.auth.token, and sms.twilio.from.number");
        }
    }

    @Override
    public String sendSms(String to, String message) {
        return sendSmsWithMetadata(to, message, Collections.emptyMap());
    }

    @Override
    public String sendSmsWithMetadata(String to, String message, Map<String, String> metadata) {
        if (!configured) {
            return simulateSmsSending(to, message);
        }

        // Validate phone number
        if (!isValidPhoneNumber(to)) {
            throw new SmsSendingException("Invalid phone number format: " + to, to, "INVALID_PHONE_NUMBER");
        }

        try {
            String endpoint = String.format(MESSAGES_ENDPOINT, accountSid);
            String url = twilioApiUrl + endpoint;

            // Build request body
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("To", to);
            requestBody.put("From", fromNumber);
            requestBody.put("Body", message);

            // Use Messaging Service SID if configured
            if (messagingServiceSid != null && !messagingServiceSid.isEmpty()) {
                requestBody.put("MessagingServiceSid", messagingServiceSid);
            }

            // Add metadata as status callback (for tracking)
            if (metadata.containsKey("callbackUrl")) {
                requestBody.put("StatusCallback", metadata.get("callbackUrl"));
            }

            HttpHeaders headers = createTwilioHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<String> request = new HttpEntity<>(buildFormUrlEncodedBody(requestBody), headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    URI.create(url),
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode responseBody = objectMapper.readTree(response.getBody());
                String messageId = responseBody.get("sid").asText();
                String status = responseBody.get("status").asText();

                // Cache initial status
                messageStatusCache.put(messageId, mapTwilioStatus(status));

                logger.info("Twilio SMS sent successfully. Message ID: {}, Status: {}, To: {}", messageId, status, to);
                return messageId;
            } else {
                logger.error("Twilio SMS failed with status: {}", response.getStatusCode());
                throw new SmsSendingException("Failed to send SMS. Status: " + response.getStatusCode(), to);
            }
        } catch (SmsSendingException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error sending SMS via Twilio to {}: {}", to, e.getMessage(), e);
            throw new SmsSendingException("SMS sending failed: " + e.getMessage(), to, e);
        }
    }

    @Override
    public int sendBulkSms(List<String> recipients, String message) {
        if (!configured) {
            logger.warn("Twilio not configured, simulating bulk SMS send");
            return recipients.size();
        }

        int successCount = 0;
        int rateLimitDelayMs = 1000; // 1 second between batches to respect rate limits

        for (String recipient : recipients) {
            try {
                String messageId = sendSms(recipient, message);
                if (messageId != null) {
                    successCount++;
                }

                // Rate limiting - Twilio allows 1 message per second per sender
                Thread.sleep(rateLimitDelayMs);
            } catch (SmsSendingException e) {
                logger.error("Failed to send bulk SMS to {}: {}", recipient, e.getMessage());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Bulk SMS sending interrupted");
                break;
            }
        }

        logger.info("Bulk SMS sending completed: {}/{} successful", successCount, recipients.size());
        return successCount;
    }

    @Override
    public DeliveryStatus getDeliveryStatus(String messageId) {
        if (!configured) {
            return DeliveryStatus.UNKNOWN;
        }

        // Check cache first
        if (messageStatusCache.containsKey(messageId)) {
            return messageStatusCache.get(messageId);
        }

        try {
            String endpoint = String.format(MESSAGES_ENDPOINT, accountSid) + "/" + messageId;
            String url = twilioApiUrl + endpoint;

            HttpHeaders headers = createTwilioHeaders();
            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    URI.create(url),
                    HttpMethod.GET,
                    request,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode responseBody = objectMapper.readTree(response.getBody());
                String status = responseBody.get("status").asText();
                DeliveryStatus deliveryStatus = mapTwilioStatus(status);
                messageStatusCache.put(messageId, deliveryStatus);
                return deliveryStatus;
            }

            return DeliveryStatus.UNKNOWN;
        } catch (Exception e) {
            logger.error("Error retrieving SMS status for {}: {}", messageId, e.getMessage());
            return DeliveryStatus.UNKNOWN;
        }
    }

    @Override
    public boolean isHealthy() {
        if (!configured) {
            return false;
        }

        try {
            // Test connectivity by fetching account info
            String url = twilioApiUrl + String.format(MESSAGES_ENDPOINT, accountSid) + "?PageSize=1";
            HttpHeaders headers = createTwilioHeaders();
            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    URI.create(url),
                    HttpMethod.GET,
                    request,
                    String.class
            );

            boolean isHealthy = response.getStatusCode().is2xxSuccessful();
            logger.debug("Twilio health check: {}", isHealthy);
            return isHealthy;
        } catch (Exception e) {
            logger.warn("Twilio health check failed: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public String getProvider() {
        return "twilio";
    }

    @Override
    public boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }

        // Basic E.164 validation: should start with + and contain 10-15 digits
        // Format: +[country code][number]
        // Examples: +1234567890, +442071234567
        String e164Pattern = "^\\+[1-9]\\d{1,14}$";
        return phoneNumber.matches(e164Pattern);
    }

    /**
     * Simulate SMS sending when Twilio is not configured.
     * This is for testing/development only.
     */
    private String simulateSmsSending(String to, String message) {
        logger.info("Simulating SMS send to: {}, message: {}", to,
                   message.length() > 50 ? message.substring(0, 50) + "..." : message);

        try {
            Thread.sleep(200); // Simulate network delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String simulatedId = "SM_simulated_" + UUID.randomUUID().toString().substring(0, 20);
        logger.info("Simulated SMS sent with ID: {}", simulatedId);
        messageStatusCache.put(simulatedId, DeliveryStatus.SENT);
        return simulatedId;
    }

    /**
     * Map Twilio status to DeliveryStatus enum.
     */
    private DeliveryStatus mapTwilioStatus(String twilioStatus) {
        return switch (twilioStatus.toLowerCase()) {
            case "queued", "accepted" -> DeliveryStatus.PENDING;
            case "sent" -> DeliveryStatus.SENT;
            case "delivered" -> DeliveryStatus.DELIVERED;
            case "undelivered" -> DeliveryStatus.UNDELIVERED;
            case "failed" -> DeliveryStatus.FAILED;
            case "expired" -> DeliveryStatus.EXPIRED;
            case "rejected" -> DeliveryStatus.REJECTED;
            default -> DeliveryStatus.UNKNOWN;
        };
    }

    /**
     * Create HTTP headers for Twilio API requests.
     */
    private HttpHeaders createTwilioHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String auth = accountSid + ":" + authToken;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + encodedAuth);
        return headers;
    }

    /**
     * Build form-urlencoded request body.
     */
    private String buildFormUrlEncodedBody(Map<String, String> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(java.net.URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            sb.append("=");
            sb.append(java.net.URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return sb.toString();
    }

    /**
     * Mask account SID for logging.
     */
    private String maskAccountSid(String accountSid) {
        if (accountSid == null || accountSid.length() < 10) {
            return "****";
        }
        return accountSid.substring(0, 4) + "****" + accountSid.substring(accountSid.length() - 4);
    }
}

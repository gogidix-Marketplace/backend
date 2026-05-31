package com.gogidix.shared.infrastructure.services.communication.sms.infrastructure.sms;

import java.util.Map;

/**
 * SMS gateway service interface for sending SMS messages.
 *
 * <p>This abstraction supports multiple SMS service providers:
 * <ul>
 *   <li>Twilio</li>
 *   <li>AWS SNS</li>
 *   <li>MessageBird</li>
 *   <li>Nexmo (Vonage)</li>
 *   <li>Firebase Cloud Messaging</li>
 * </ul>
 *
 * <p>Implementation is selected via configuration:
 * <pre>
 * sms.provider=twilio|aws-sns|messagebird|nexmo
 * sms.twilio.account.sid=your_account_sid
 * sms.twilio.auth.token=your_auth_token
 * sms.twilio.from.number=+1234567890
 * sms.aws.access.key=your_access_key
 * sms.aws.secret.key=your_secret_key
 * sms.aws.region=us-east-1
 * </pre>
 */
public interface SmsGatewayService {

    /**
     * Send a plain text SMS message.
     *
     * @param to the recipient phone number (E.164 format)
     * @param message the message content
     * @return the message ID from the gateway, or null if failed
     * @throws SmsSendingException if sending fails
     */
    String sendSms(String to, String message);

    /**
     * Send an SMS with metadata.
     *
     * @param to the recipient phone number (E.164 format)
     * @param message the message content
     * @param metadata additional metadata for tracking
     * @return the message ID from the gateway
     * @throws SmsSendingException if sending fails
     */
    String sendSmsWithMetadata(String to, String message, Map<String, String> metadata);

    /**
     * Send a bulk SMS to multiple recipients.
     *
     * @param recipients list of recipient phone numbers
     * @param message the message content
     * @return number of successfully sent messages
     */
    int sendBulkSms(java.util.List<String> recipients, String message);

    /**
     * Get the delivery status of a sent SMS.
     *
     * @param messageId the message ID from the gateway
     * @return the delivery status
     */
    DeliveryStatus getDeliveryStatus(String messageId);

    /**
     * Check if the SMS service is healthy/configured.
     *
     * @return true if service is operational
     */
    boolean isHealthy();

    /**
     * Get the provider name.
     *
     * @return the SMS provider (e.g., "twilio", "aws-sns")
     */
    String getProvider();

    /**
     * Validate a phone number format.
     *
     * @param phoneNumber the phone number to validate
     * @return true if valid E.164 format
     */
    boolean isValidPhoneNumber(String phoneNumber);

    /**
     * Delivery status enum matching gateway statuses.
     */
    enum DeliveryStatus {
        PENDING,
        SENT,
        DELIVERED,
        FAILED,
        UNDELIVERED,
        EXPIRED,
        REJECTED,
        UNKNOWN
    }

    /**
     * Exception thrown when SMS sending fails.
     */
    class SmsSendingException extends RuntimeException {
        private final String recipient;
        private final String errorCode;

        public SmsSendingException(String message, String recipient) {
            super(message);
            this.recipient = recipient;
            this.errorCode = null;
        }

        public SmsSendingException(String message, String recipient, String errorCode) {
            super(message);
            this.recipient = recipient;
            this.errorCode = errorCode;
        }

        public SmsSendingException(String message, String recipient, Throwable cause) {
            super(message, cause);
            this.recipient = recipient;
            this.errorCode = null;
        }

        public String getRecipient() {
            return recipient;
        }

        public String getErrorCode() {
            return errorCode;
        }
    }
}

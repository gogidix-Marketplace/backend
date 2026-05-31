package com.gogidix.sales.communication.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Recipient Address Value Object
 * Represents a recipient's contact information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipientAddress {

    private String recipientId;
    private String recipientName;
    private String recipientType; // USER, GROUP, EXTERNAL
    private String emailAddress;
    private String phoneNumber;
    private String countryCode; // For WhatsApp/SMS
    private String userId; // Internal user ID if applicable

    /**
     * Validates if the recipient address is valid for the given channel
     */
    public boolean isValidForChannel(String channelType) {
        return switch (channelType.toUpperCase()) {
            case "EMAIL" -> emailAddress != null && !emailAddress.isBlank();
            case "SMS", "WHATSAPP" -> phoneNumber != null && !phoneNumber.isBlank();
            case "IN_APP", "PUSH_NOTIFICATION" -> userId != null && !userId.isBlank();
            default -> false;
        };
    }

    /**
     * Gets the address for the given channel
     */
    public String getAddressForChannel(String channelType) {
        return switch (channelType.toUpperCase()) {
            case "EMAIL" -> emailAddress;
            case "SMS", "WHATSAPP" -> phoneNumber;
            case "IN_APP", "PUSH_NOTIFICATION" -> userId;
            default -> null;
        };
    }
}

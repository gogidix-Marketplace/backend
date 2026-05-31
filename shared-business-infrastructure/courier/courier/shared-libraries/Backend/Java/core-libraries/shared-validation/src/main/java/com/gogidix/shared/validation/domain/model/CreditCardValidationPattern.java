package com.gogidix.shared.validation.domain.model;

import java.util.regex.Pattern;

/**
 * Domain service for credit card validation using Luhn algorithm and format patterns.
 */
public class CreditCardValidationPattern {
    
    // Credit card type patterns
    private static final Pattern VISA_PATTERN = Pattern.compile("^4[0-9]{12}(?:[0-9]{3})?$");
    private static final Pattern MASTERCARD_PATTERN = Pattern.compile("^5[1-5][0-9]{14}$");
    private static final Pattern AMEX_PATTERN = Pattern.compile("^3[47][0-9]{13}$");
    private static final Pattern DISCOVER_PATTERN = Pattern.compile("^6(?:011|5[0-9]{2})[0-9]{12}$");
    private static final Pattern DINERS_PATTERN = Pattern.compile("^3[0689][0-9]{11}$");
    private static final Pattern JCB_PATTERN = Pattern.compile("^(?:2131|1800|35\\d{3})\\d{11}$");
    
    /**
     * Validates credit card number using Luhn algorithm.
     */
    public static boolean isValid(String creditCardNumber) {
        if (creditCardNumber == null || creditCardNumber.trim().isEmpty()) {
            return false;
        }
        
        String cleaned = cleanCreditCardNumber(creditCardNumber);
        
        // Check if it contains only digits
        if (!cleaned.matches("^[0-9]+$")) {
            return false;
        }
        
        // Check length (credit cards are typically 13-19 digits)
        if (cleaned.length() < 13 || cleaned.length() > 19) {
            return false;
        }
        
        // Validate using Luhn algorithm
        return isValidLuhn(cleaned);
    }
    
    /**
     * Validates credit card number and detects card type.
     */
    public static CreditCardValidationInfo getValidationInfo(String creditCardNumber) {
        if (creditCardNumber == null) {
            return new CreditCardValidationInfo(false, "Credit card number is null");
        }
        
        String trimmed = creditCardNumber.trim();
        if (trimmed.isEmpty()) {
            return new CreditCardValidationInfo(false, "Credit card number is empty");
        }
        
        String cleaned = cleanCreditCardNumber(trimmed);
        
        if (!cleaned.matches("^[0-9]+$")) {
            return new CreditCardValidationInfo(false, "Credit card number contains non-digit characters");
        }
        
        if (cleaned.length() < 13) {
            return new CreditCardValidationInfo(false, "Credit card number too short (minimum 13 digits)");
        }
        
        if (cleaned.length() > 19) {
            return new CreditCardValidationInfo(false, "Credit card number too long (maximum 19 digits)");
        }
        
        // Detect card type
        CreditCardType cardType = detectCardType(cleaned);
        
        // Validate using Luhn algorithm
        boolean luhnValid = isValidLuhn(cleaned);
        
        if (luhnValid && cardType != CreditCardType.UNKNOWN) {
            return new CreditCardValidationInfo(true, "Valid credit card number", cardType);
        } else if (luhnValid) {
            return new CreditCardValidationInfo(true, "Valid credit card number (unknown type)", cardType);
        } else {
            return new CreditCardValidationInfo(false, "Invalid credit card number (failed Luhn check)", cardType);
        }
    }
    
    /**
     * Cleans credit card number by removing non-digit characters.
     */
    private static String cleanCreditCardNumber(String creditCardNumber) {
        if (creditCardNumber == null) {
            return "";
        }
        
        return creditCardNumber.replaceAll("[^0-9]", "");
    }
    
    /**
     * Validates credit card number using Luhn algorithm.
     */
    private static boolean isValidLuhn(String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        
        // Process digits from right to left
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }
            
            sum += digit;
            alternate = !alternate;
        }
        
        return (sum % 10 == 0);
    }
    
    /**
     * Detects the type of credit card based on number patterns.
     */
    public static CreditCardType detectCardType(String creditCardNumber) {
        String cleaned = cleanCreditCardNumber(creditCardNumber);
        
        if (cleaned.isEmpty()) {
            return CreditCardType.UNKNOWN;
        }
        
        if (VISA_PATTERN.matcher(cleaned).matches()) {
            return CreditCardType.VISA;
        } else if (MASTERCARD_PATTERN.matcher(cleaned).matches()) {
            return CreditCardType.MASTERCARD;
        } else if (AMEX_PATTERN.matcher(cleaned).matches()) {
            return CreditCardType.AMERICAN_EXPRESS;
        } else if (DISCOVER_PATTERN.matcher(cleaned).matches()) {
            return CreditCardType.DISCOVER;
        } else if (DINERS_PATTERN.matcher(cleaned).matches()) {
            return CreditCardType.DINERS_CLUB;
        } else if (JCB_PATTERN.matcher(cleaned).matches()) {
            return CreditCardType.JCB;
        } else {
            return CreditCardType.UNKNOWN;
        }
    }
    
    /**
     * Masks credit card number for display (shows only last 4 digits).
     */
    public static String maskCreditCardNumber(String creditCardNumber) {
        String cleaned = cleanCreditCardNumber(creditCardNumber);
        
        if (cleaned.length() < 4) {
            return "*".repeat(cleaned.length());
        }
        
        String lastFour = cleaned.substring(cleaned.length() - 4);
        String masked = "*".repeat(cleaned.length() - 4);
        
        return masked + lastFour;
    }
    
    /**
     * Formats credit card number for display with spaces.
     */
    public static String formatCreditCardNumber(String creditCardNumber, CreditCardType cardType) {
        String cleaned = cleanCreditCardNumber(creditCardNumber);
        
        if (cleaned.isEmpty()) {
            return creditCardNumber;
        }
        
        return switch (cardType) {
            case AMERICAN_EXPRESS -> formatAmexNumber(cleaned);
            case DINERS_CLUB -> formatDinersNumber(cleaned);
            default -> formatStandardNumber(cleaned);
        };
    }
    
    /**
     * Formats standard credit card number (4-4-4-4 pattern).
     */
    private static String formatStandardNumber(String cardNumber) {
        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < cardNumber.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                formatted.append(" ");
            }
            formatted.append(cardNumber.charAt(i));
        }
        return formatted.toString();
    }
    
    /**
     * Formats American Express number (4-6-5 pattern).
     */
    private static String formatAmexNumber(String cardNumber) {
        if (cardNumber.length() != 15) {
            return cardNumber;
        }
        
        return cardNumber.substring(0, 4) + " " + 
               cardNumber.substring(4, 10) + " " + 
               cardNumber.substring(10);
    }
    
    /**
     * Formats Diners Club number (4-6-4 pattern).
     */
    private static String formatDinersNumber(String cardNumber) {
        if (cardNumber.length() != 14) {
            return cardNumber;
        }
        
        return cardNumber.substring(0, 4) + " " + 
               cardNumber.substring(4, 10) + " " + 
               cardNumber.substring(10);
    }
    
    /**
     * Credit card types.
     */
    public enum CreditCardType {
        VISA("Visa", "4xxx-xxxx-xxxx-xxxx", 16),
        MASTERCARD("MasterCard", "5xxx-xxxx-xxxx-xxxx", 16),
        AMERICAN_EXPRESS("American Express", "3xxx-xxxxxx-xxxxx", 15),
        DISCOVER("Discover", "6xxx-xxxx-xxxx-xxxx", 16),
        DINERS_CLUB("Diners Club", "3xxx-xxxxxx-xxxx", 14),
        JCB("JCB", "35xx-xxxx-xxxx-xxxx", 16),
        UNKNOWN("Unknown", "Unknown format", 0);
        
        private final String displayName;
        private final String format;
        private final int standardLength;
        
        CreditCardType(String displayName, String format, int standardLength) {
            this.displayName = displayName;
            this.format = format;
            this.standardLength = standardLength;
        }
        
        public String getDisplayName() { return displayName; }
        public String getFormat() { return format; }
        public int getStandardLength() { return standardLength; }
    }
    
    /**
     * Credit card validation information.
     */
    public static class CreditCardValidationInfo {
        private final boolean valid;
        private final String message;
        private final CreditCardType cardType;
        
        public CreditCardValidationInfo(boolean valid, String message) {
            this(valid, message, CreditCardType.UNKNOWN);
        }
        
        public CreditCardValidationInfo(boolean valid, String message, CreditCardType cardType) {
            this.valid = valid;
            this.message = message;
            this.cardType = cardType;
        }
        
        public boolean isValid() { return valid; }
        public String getMessage() { return message; }
        public CreditCardType getCardType() { return cardType; }
    }
}
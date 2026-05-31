package com.gogidix.shared.validation.domain.model;

import java.util.regex.Pattern;

/**
 * Domain service for Social Security Number (SSN) validation.
 */
public class SSNValidationPattern {
    
    // SSN pattern (XXX-XX-XXXX format)
    private static final Pattern SSN_FORMATTED_PATTERN = Pattern.compile("^\\d{3}-\\d{2}-\\d{4}$");
    
    // SSN pattern (9 digits without formatting)
    private static final Pattern SSN_DIGITS_PATTERN = Pattern.compile("^\\d{9}$");
    
    // Invalid SSN patterns that should be rejected
    private static final Pattern[] INVALID_PATTERNS = {
        Pattern.compile("^000-\\d{2}-\\d{4}$"),    // Area number 000
        Pattern.compile("^\\d{3}-00-\\d{4}$"),    // Group number 00
        Pattern.compile("^\\d{3}-\\d{2}-0000$"),  // Serial number 0000
        Pattern.compile("^666-\\d{2}-\\d{4}$"),   // Area number 666
        Pattern.compile("^9\\d{2}-\\d{2}-\\d{4}$") // Area numbers 900-999
    };
    
    /**
     * Validates Social Security Number.
     */
    public static boolean isValid(String ssn) {
        if (ssn == null || ssn.trim().isEmpty()) {
            return false;
        }
        
        String cleaned = cleanSSN(ssn);
        
        // Check if it's 9 digits
        if (!SSN_DIGITS_PATTERN.matcher(cleaned).matches()) {
            return false;
        }
        
        // Format for pattern checking
        String formatted = formatSSN(cleaned);
        
        // Check against invalid patterns
        for (Pattern invalidPattern : INVALID_PATTERNS) {
            if (invalidPattern.matcher(formatted).matches()) {
                return false;
            }
        }
        
        // Additional business rule checks
        return isValidByBusinessRules(cleaned);
    }
    
    /**
     * Gets detailed validation information for an SSN.
     */
    public static SSNValidationInfo getValidationInfo(String ssn) {
        if (ssn == null) {
            return new SSNValidationInfo(false, "SSN is null");
        }
        
        String trimmed = ssn.trim();
        if (trimmed.isEmpty()) {
            return new SSNValidationInfo(false, "SSN is empty");
        }
        
        String cleaned = cleanSSN(trimmed);
        
        if (!SSN_DIGITS_PATTERN.matcher(cleaned).matches()) {
            return new SSNValidationInfo(false, "SSN must be exactly 9 digits");
        }
        
        String formatted = formatSSN(cleaned);
        
        // Check specific invalid patterns
        if (cleaned.startsWith("000")) {
            return new SSNValidationInfo(false, "SSN cannot start with 000");
        }
        
        if (cleaned.startsWith("666")) {
            return new SSNValidationInfo(false, "SSN cannot start with 666");
        }
        
        if (cleaned.charAt(0) == '9') {
            return new SSNValidationInfo(false, "SSN cannot start with 9xx");
        }
        
        if (cleaned.substring(3, 5).equals("00")) {
            return new SSNValidationInfo(false, "SSN group number cannot be 00");
        }
        
        if (cleaned.substring(5).equals("0000")) {
            return new SSNValidationInfo(false, "SSN serial number cannot be 0000");
        }
        
        // Check for sequential or repeated patterns
        if (isSequentialPattern(cleaned)) {
            return new SSNValidationInfo(false, "SSN cannot be a sequential pattern", 
                SSNValidationLevel.STRICT);
        }
        
        if (isRepeatedPattern(cleaned)) {
            return new SSNValidationInfo(false, "SSN cannot be a repeated pattern", 
                SSNValidationLevel.STRICT);
        }
        
        // Check against known test/dummy SSNs
        if (isTestSSN(cleaned)) {
            return new SSNValidationInfo(false, "SSN appears to be a test/dummy number", 
                SSNValidationLevel.BUSINESS);
        }
        
        return new SSNValidationInfo(true, "Valid SSN", SSNValidationLevel.STANDARD);
    }
    
    /**
     * Cleans SSN by removing non-digit characters.
     */
    private static String cleanSSN(String ssn) {
        if (ssn == null) {
            return "";
        }
        
        return ssn.replaceAll("[^0-9]", "");
    }
    
    /**
     * Formats SSN with dashes (XXX-XX-XXXX).
     */
    public static String formatSSN(String ssn) {
        String cleaned = cleanSSN(ssn);
        
        if (cleaned.length() != 9) {
            return ssn; // Return original if not 9 digits
        }
        
        return cleaned.substring(0, 3) + "-" + 
               cleaned.substring(3, 5) + "-" + 
               cleaned.substring(5);
    }
    
    /**
     * Masks SSN for display (shows only last 4 digits).
     */
    public static String maskSSN(String ssn) {
        String cleaned = cleanSSN(ssn);
        
        if (cleaned.length() != 9) {
            return "***-**-****"; // Generic mask if invalid
        }
        
        return "***-**-" + cleaned.substring(5);
    }
    
    /**
     * Validates SSN against business rules.
     */
    private static boolean isValidByBusinessRules(String ssn) {
        // Check for obviously invalid patterns
        if (isSequentialPattern(ssn) || isRepeatedPattern(ssn)) {
            return false;
        }
        
        // Check against known test SSNs
        if (isTestSSN(ssn)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Checks if SSN is a sequential pattern (e.g., 123456789).
     */
    private static boolean isSequentialPattern(String ssn) {
        if (ssn.length() != 9) {
            return false;
        }
        
        // Check ascending sequence
        boolean ascending = true;
        for (int i = 1; i < ssn.length(); i++) {
            int current = Character.getNumericValue(ssn.charAt(i));
            int previous = Character.getNumericValue(ssn.charAt(i - 1));
            
            if (current != (previous + 1) % 10) {
                ascending = false;
                break;
            }
        }
        
        // Check descending sequence
        boolean descending = true;
        for (int i = 1; i < ssn.length(); i++) {
            int current = Character.getNumericValue(ssn.charAt(i));
            int previous = Character.getNumericValue(ssn.charAt(i - 1));
            
            if (current != (previous - 1 + 10) % 10) {
                descending = false;
                break;
            }
        }
        
        return ascending || descending;
    }
    
    /**
     * Checks if SSN is a repeated pattern (e.g., 111111111).
     */
    private static boolean isRepeatedPattern(String ssn) {
        if (ssn.length() != 9) {
            return false;
        }
        
        char firstChar = ssn.charAt(0);
        for (int i = 1; i < ssn.length(); i++) {
            if (ssn.charAt(i) != firstChar) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Checks if SSN is a known test/dummy number.
     */
    private static boolean isTestSSN(String ssn) {
        // Common test SSNs used in documentation and examples
        String[] testSSNs = {
            "123456789", "111111111", "222222222", "333333333", "444444444",
            "555555555", "777777777", "888888888", "999999999",
            "123123123", "987654321", "111223333", "987987987"
        };
        
        for (String testSSN : testSSNs) {
            if (ssn.equals(testSSN)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * SSN validation levels.
     */
    public enum SSNValidationLevel {
        BASIC("Basic", "Format validation only"),
        STANDARD("Standard", "Format and basic business rules"),
        STRICT("Strict", "Comprehensive validation including patterns"),
        BUSINESS("Business", "Business-specific validation rules");
        
        private final String displayName;
        private final String description;
        
        SSNValidationLevel(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    /**
     * SSN validation information.
     */
    public static class SSNValidationInfo {
        private final boolean valid;
        private final String message;
        private final SSNValidationLevel level;
        
        public SSNValidationInfo(boolean valid, String message) {
            this(valid, message, SSNValidationLevel.STANDARD);
        }
        
        public SSNValidationInfo(boolean valid, String message, SSNValidationLevel level) {
            this.valid = valid;
            this.message = message;
            this.level = level;
        }
        
        public boolean isValid() { return valid; }
        public String getMessage() { return message; }
        public SSNValidationLevel getLevel() { return level; }
    }
}
package com.gogidix.shared.validation.domain.model;

import java.util.regex.Pattern;

/**
 * Domain service for email validation patterns and logic.
 */
public class EmailValidationPattern {
    
    // RFC 5322 compliant email regex (simplified version)
    private static final Pattern BASIC_EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    // More strict email pattern
    private static final Pattern STRICT_EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9][a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]*[a-zA-Z0-9]@[a-zA-Z0-9][a-zA-Z0-9.-]{0,61}[a-zA-Z0-9]\\.[a-zA-Z]{2,}$"
    );
    
    // Lenient email pattern (allows more characters)
    private static final Pattern LENIENT_EMAIL_PATTERN = Pattern.compile(
        "^.+@.+\\..+$"
    );
    
    /**
     * Validates email using basic pattern.
     */
    public static boolean isValid(String email) {
        return isValid(email, EmailValidationLevel.BASIC);
    }
    
    /**
     * Validates email using specified validation level.
     */
    public static boolean isValid(String email, EmailValidationLevel level) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        String trimmedEmail = email.trim();
        
        // Check basic constraints first
        if (trimmedEmail.length() > 254) { // RFC 5321 limit
            return false;
        }
        
        if (trimmedEmail.startsWith(".") || trimmedEmail.endsWith(".")) {
            return false;
        }
        
        if (trimmedEmail.contains("..")) {
            return false;
        }
        
        // Check for at symbol
        long atCount = trimmedEmail.chars().filter(ch -> ch == '@').count();
        if (atCount != 1) {
            return false;
        }
        
        // Split and validate local and domain parts
        String[] parts = trimmedEmail.split("@");
        if (parts.length != 2) {
            return false;
        }
        
        String localPart = parts[0];
        String domainPart = parts[1];
        
        if (!isValidLocalPart(localPart) || !isValidDomainPart(domainPart)) {
            return false;
        }
        
        // Apply pattern validation based on level
        Pattern pattern = switch (level) {
            case LENIENT -> LENIENT_EMAIL_PATTERN;
            case BASIC -> BASIC_EMAIL_PATTERN;
            case STRICT -> STRICT_EMAIL_PATTERN;
        };
        
        return pattern.matcher(trimmedEmail).matches();
    }
    
    /**
     * Validates the local part of an email address (before @).
     */
    private static boolean isValidLocalPart(String localPart) {
        if (localPart == null || localPart.isEmpty()) {
            return false;
        }
        
        if (localPart.length() > 64) { // RFC 5321 limit
            return false;
        }
        
        if (localPart.startsWith(".") || localPart.endsWith(".")) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Validates the domain part of an email address (after @).
     */
    private static boolean isValidDomainPart(String domainPart) {
        if (domainPart == null || domainPart.isEmpty()) {
            return false;
        }
        
        if (domainPart.length() > 253) { // RFC 5321 limit
            return false;
        }
        
        if (domainPart.startsWith(".") || domainPart.endsWith(".")) {
            return false;
        }
        
        if (domainPart.startsWith("-") || domainPart.endsWith("-")) {
            return false;
        }
        
        // Check if domain contains at least one dot
        if (!domainPart.contains(".")) {
            return false;
        }
        
        // Validate each label in the domain
        String[] labels = domainPart.split("\\.");
        for (String label : labels) {
            if (!isValidDomainLabel(label)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Validates a single domain label.
     */
    private static boolean isValidDomainLabel(String label) {
        if (label == null || label.isEmpty()) {
            return false;
        }
        
        if (label.length() > 63) { // RFC 1035 limit
            return false;
        }
        
        if (label.startsWith("-") || label.endsWith("-")) {
            return false;
        }
        
        // Check if label contains only allowed characters
        return label.matches("^[a-zA-Z0-9-]+$");
    }
    
    /**
     * Gets detailed validation information for an email.
     */
    public static EmailValidationInfo getValidationInfo(String email) {
        if (email == null) {
            return new EmailValidationInfo(false, "Email is null");
        }
        
        String trimmedEmail = email.trim();
        
        if (trimmedEmail.isEmpty()) {
            return new EmailValidationInfo(false, "Email is empty");
        }
        
        if (trimmedEmail.length() > 254) {
            return new EmailValidationInfo(false, "Email too long (max 254 characters)");
        }
        
        long atCount = trimmedEmail.chars().filter(ch -> ch == '@').count();
        if (atCount == 0) {
            return new EmailValidationInfo(false, "Missing @ symbol");
        }

        if (atCount > 1) {
            return new EmailValidationInfo(false, "Multiple @ symbols found");
        }

        String[] parts = trimmedEmail.split("@");
        if (parts.length != 2) {
            // When atCount == 1 but split gives != 2 parts, @ must be at start or end
            if (trimmedEmail.endsWith("@")) {
                return new EmailValidationInfo(false, "Missing domain part (after @)");
            }
            if (trimmedEmail.startsWith("@")) {
                return new EmailValidationInfo(false, "Missing local part (before @)");
            }
            return new EmailValidationInfo(false, "Invalid email format");
        }

        String localPart = parts[0];
        String domainPart = parts[1];

        if (localPart.isEmpty()) {
            return new EmailValidationInfo(false, "Missing local part (before @)");
        }

        if (domainPart.isEmpty()) {
            return new EmailValidationInfo(false, "Missing domain part (after @)");
        }
        
        if (localPart.length() > 64) {
            return new EmailValidationInfo(false, "Local part too long (max 64 characters)");
        }
        
        if (domainPart.length() > 253) {
            return new EmailValidationInfo(false, "Domain part too long (max 253 characters)");
        }
        
        if (!domainPart.contains(".")) {
            return new EmailValidationInfo(false, "Domain must contain at least one dot");
        }
        
        if (STRICT_EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            return new EmailValidationInfo(true, "Valid email address", EmailValidationLevel.STRICT);
        } else if (BASIC_EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            return new EmailValidationInfo(true, "Valid email address", EmailValidationLevel.BASIC);
        } else if (LENIENT_EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            return new EmailValidationInfo(true, "Valid email address", EmailValidationLevel.LENIENT);
        } else {
            return new EmailValidationInfo(false, "Invalid email format");
        }
    }
    
    /**
     * Email validation levels.
     */
    public enum EmailValidationLevel {
        LENIENT("Lenient", "Basic format check"),
        BASIC("Basic", "Standard email format validation"),
        STRICT("Strict", "Strict RFC-compliant validation");
        
        private final String displayName;
        private final String description;
        
        EmailValidationLevel(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    /**
     * Email validation information.
     */
    public static class EmailValidationInfo {
        private final boolean valid;
        private final String message;
        private final EmailValidationLevel level;
        
        public EmailValidationInfo(boolean valid, String message) {
            this(valid, message, null);
        }
        
        public EmailValidationInfo(boolean valid, String message, EmailValidationLevel level) {
            this.valid = valid;
            this.message = message;
            this.level = level;
        }
        
        public boolean isValid() { return valid; }
        public String getMessage() { return message; }
        public EmailValidationLevel getLevel() { return level; }
    }
}
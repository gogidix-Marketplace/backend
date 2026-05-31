package com.gogidix.shared.validation.domain.model;

import java.util.regex.Pattern;
import java.util.Map;
import java.util.HashMap;

/**
 * Domain service for phone number validation patterns and logic.
 */
public class PhoneValidationPattern {
    
    // International phone number pattern (E.164 format)
    private static final Pattern INTERNATIONAL_PATTERN = Pattern.compile(
        "^\\+[1-9]\\d{1,14}$"
    );
    
    // Basic phone number pattern (allows various formats)
    private static final Pattern BASIC_PATTERN = Pattern.compile(
        "^\\+?[1-9]\\d{6,14}$"
    );
    
    // Lenient pattern (allows spaces, hyphens, parentheses)
    private static final Pattern LENIENT_PATTERN = Pattern.compile(
        "^[+]?[\\s\\-\\(\\)0-9]{7,20}$"
    );
    
    // Country-specific patterns
    private static final Map<String, Pattern> COUNTRY_PATTERNS = new HashMap<>();
    
    static {
        // US phone number patterns
        COUNTRY_PATTERNS.put("US", Pattern.compile("^\\+1[2-9]\\d{2}[2-9]\\d{2}\\d{4}$"));
        COUNTRY_PATTERNS.put("US_DOMESTIC", Pattern.compile("^[2-9]\\d{2}[2-9]\\d{2}\\d{4}$"));
        
        // UK phone number patterns
        COUNTRY_PATTERNS.put("UK", Pattern.compile("^\\+44[1-9]\\d{8,9}$"));
        
        // Germany phone number patterns
        COUNTRY_PATTERNS.put("DE", Pattern.compile("^\\+49[1-9]\\d{10,11}$"));
        
        // France phone number patterns
        COUNTRY_PATTERNS.put("FR", Pattern.compile("^\\+33[1-9]\\d{8}$"));
        
        // China phone number patterns
        COUNTRY_PATTERNS.put("CN", Pattern.compile("^\\+86[1]\\d{10}$"));
        
        // Japan phone number patterns
        COUNTRY_PATTERNS.put("JP", Pattern.compile("^\\+81[1-9]\\d{8,9}$"));
        
        // India phone number patterns
        COUNTRY_PATTERNS.put("IN", Pattern.compile("^\\+91[6-9]\\d{9}$"));
        
        // Brazil phone number patterns
        COUNTRY_PATTERNS.put("BR", Pattern.compile("^\\+55[1-9]{2}9?\\d{8}$"));
        
        // Canada phone number patterns (same as US)
        COUNTRY_PATTERNS.put("CA", Pattern.compile("^\\+1[2-9]\\d{2}[2-9]\\d{2}\\d{4}$"));
    }
    
    /**
     * Validates phone number using basic pattern.
     */
    public static boolean isValid(String phoneNumber) {
        return isValid(phoneNumber, PhoneValidationLevel.BASIC);
    }
    
    /**
     * Validates phone number using specified validation level.
     */
    public static boolean isValid(String phoneNumber, PhoneValidationLevel level) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        
        String cleanedPhone = cleanPhoneNumber(phoneNumber);
        
        if (cleanedPhone.isEmpty()) {
            return false;
        }
        
        // Check length constraints
        if (cleanedPhone.length() < 7 || cleanedPhone.length() > 15) {
            return false;
        }
        
        // Apply pattern validation based on level
        Pattern pattern = switch (level) {
            case LENIENT -> LENIENT_PATTERN;
            case BASIC -> BASIC_PATTERN;
            case INTERNATIONAL -> INTERNATIONAL_PATTERN;
        };

        return pattern.matcher(cleanedPhone).matches();
    }
    
    /**
     * Validates phone number for a specific country.
     */
    public static boolean isValidForCountry(String phoneNumber, String countryCode) {
        if (phoneNumber == null || countryCode == null) {
            return false;
        }
        
        Pattern countryPattern = COUNTRY_PATTERNS.get(countryCode.toUpperCase());
        if (countryPattern == null) {
            // Fall back to basic validation if country not supported
            return isValid(phoneNumber, PhoneValidationLevel.BASIC);
        }
        
        String cleanedPhone = cleanPhoneNumber(phoneNumber);
        return countryPattern.matcher(cleanedPhone).matches();
    }
    
    /**
     * Cleans phone number by removing non-digit characters except +.
     */
    private static String cleanPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return "";
        }
        
        // Remove all characters except digits and +
        String cleaned = phoneNumber.replaceAll("[^+\\d]", "");
        
        // Ensure + is only at the beginning
        if (cleaned.contains("+")) {
            if (!cleaned.startsWith("+")) {
                cleaned = cleaned.replace("+", "");
            } else {
                // Remove any + that's not at the beginning
                cleaned = "+" + cleaned.substring(1).replace("+", "");
            }
        }
        
        return cleaned;
    }
    
    /**
     * Formats phone number according to international E.164 standard.
     */
    public static String formatInternational(String phoneNumber) {
        String cleaned = cleanPhoneNumber(phoneNumber);
        
        if (cleaned.isEmpty()) {
            return phoneNumber;
        }
        
        // Add + if not present
        if (!cleaned.startsWith("+")) {
            cleaned = "+" + cleaned;
        }
        
        return cleaned;
    }
    
    /**
     * Formats phone number for display (with spacing).
     */
    public static String formatForDisplay(String phoneNumber, String countryCode) {
        String cleaned = cleanPhoneNumber(phoneNumber);
        
        if (cleaned.isEmpty()) {
            return phoneNumber;
        }
        
        // Format based on country code
        return switch (countryCode.toUpperCase()) {
            case "US", "CA" -> formatUSPhone(cleaned);
            case "UK" -> formatUKPhone(cleaned);
            case "DE" -> formatGermanyPhone(cleaned);
            case "FR" -> formatFrancePhone(cleaned);
            default -> cleaned; // Return cleaned version if no specific formatter
        };
    }
    
    /**
     * Formats US/Canada phone number for display.
     */
    private static String formatUSPhone(String phoneNumber) {
        String cleaned = phoneNumber.replaceAll("[^\\d]", "");
        
        if (cleaned.length() == 10) {
            return String.format("(%s) %s-%s", 
                cleaned.substring(0, 3),
                cleaned.substring(3, 6),
                cleaned.substring(6));
        } else if (cleaned.length() == 11 && cleaned.startsWith("1")) {
            return String.format("+1 (%s) %s-%s", 
                cleaned.substring(1, 4),
                cleaned.substring(4, 7),
                cleaned.substring(7));
        }
        
        return phoneNumber;
    }
    
    /**
     * Formats UK phone number for display.
     */
    private static String formatUKPhone(String phoneNumber) {
        if (phoneNumber.startsWith("+44")) {
            String digits = phoneNumber.substring(3);
            if (digits.length() >= 10) {
                return String.format("+44 %s %s %s", 
                    digits.substring(0, 2),
                    digits.substring(2, 6),
                    digits.substring(6));
            }
        }
        return phoneNumber;
    }
    
    /**
     * Formats German phone number for display.
     */
    private static String formatGermanyPhone(String phoneNumber) {
        if (phoneNumber.startsWith("+49")) {
            String digits = phoneNumber.substring(3);
            if (digits.length() >= 10) {
                return String.format("+49 %s %s", 
                    digits.substring(0, 3),
                    digits.substring(3));
            }
        }
        return phoneNumber;
    }
    
    /**
     * Formats French phone number for display.
     */
    private static String formatFrancePhone(String phoneNumber) {
        if (phoneNumber.startsWith("+33")) {
            String digits = phoneNumber.substring(3);
            if (digits.length() == 9) {
                return String.format("+33 %s %s %s %s %s", 
                    digits.substring(0, 1),
                    digits.substring(1, 3),
                    digits.substring(3, 5),
                    digits.substring(5, 7),
                    digits.substring(7));
            }
        }
        return phoneNumber;
    }
    
    /**
     * Gets detailed validation information for a phone number.
     */
    public static PhoneValidationInfo getValidationInfo(String phoneNumber) {
        if (phoneNumber == null) {
            return new PhoneValidationInfo(false, "Phone number is null");
        }
        
        String trimmed = phoneNumber.trim();
        if (trimmed.isEmpty()) {
            return new PhoneValidationInfo(false, "Phone number is empty");
        }
        
        String cleaned = cleanPhoneNumber(trimmed);
        if (cleaned.isEmpty()) {
            return new PhoneValidationInfo(false, "Phone number contains no digits");
        }
        
        if (cleaned.length() < 7) {
            return new PhoneValidationInfo(false, "Phone number too short (minimum 7 digits)");
        }
        
        if (cleaned.length() > 15) {
            return new PhoneValidationInfo(false, "Phone number too long (maximum 15 digits)");
        }
        
        // Check against different validation levels
        if (INTERNATIONAL_PATTERN.matcher(cleaned).matches()) {
            String countryCode = detectCountryCode(cleaned);
            return new PhoneValidationInfo(true, "Valid international phone number", 
                PhoneValidationLevel.INTERNATIONAL, countryCode);
        } else if (BASIC_PATTERN.matcher(cleaned).matches()) {
            return new PhoneValidationInfo(true, "Valid phone number", PhoneValidationLevel.BASIC);
        } else if (LENIENT_PATTERN.matcher(trimmed).matches()) {
            return new PhoneValidationInfo(true, "Valid phone number", PhoneValidationLevel.LENIENT);
        } else {
            return new PhoneValidationInfo(false, "Invalid phone number format");
        }
    }
    
    /**
     * Attempts to detect country code from international phone number.
     */
    private static String detectCountryCode(String phoneNumber) {
        if (!phoneNumber.startsWith("+")) {
            return null;
        }
        
        // Simple country code detection based on common patterns
        if (phoneNumber.startsWith("+1")) return "US/CA";
        if (phoneNumber.startsWith("+44")) return "UK";
        if (phoneNumber.startsWith("+49")) return "DE";
        if (phoneNumber.startsWith("+33")) return "FR";
        if (phoneNumber.startsWith("+86")) return "CN";
        if (phoneNumber.startsWith("+81")) return "JP";
        if (phoneNumber.startsWith("+91")) return "IN";
        if (phoneNumber.startsWith("+55")) return "BR";
        
        return "UNKNOWN";
    }
    
    /**
     * Phone validation levels.
     */
    public enum PhoneValidationLevel {
        LENIENT("Lenient", "Basic format check with flexible characters"),
        BASIC("Basic", "Standard phone number validation"),
        INTERNATIONAL("International", "Strict E.164 international format");
        
        private final String displayName;
        private final String description;
        
        PhoneValidationLevel(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    /**
     * Phone validation information.
     */
    public static class PhoneValidationInfo {
        private final boolean valid;
        private final String message;
        private final PhoneValidationLevel level;
        private final String countryCode;
        
        public PhoneValidationInfo(boolean valid, String message) {
            this(valid, message, null, null);
        }
        
        public PhoneValidationInfo(boolean valid, String message, PhoneValidationLevel level) {
            this(valid, message, level, null);
        }
        
        public PhoneValidationInfo(boolean valid, String message, PhoneValidationLevel level, String countryCode) {
            this.valid = valid;
            this.message = message;
            this.level = level;
            this.countryCode = countryCode;
        }
        
        public boolean isValid() { return valid; }
        public String getMessage() { return message; }
        public PhoneValidationLevel getLevel() { return level; }
        public String getCountryCode() { return countryCode; }
    }
}
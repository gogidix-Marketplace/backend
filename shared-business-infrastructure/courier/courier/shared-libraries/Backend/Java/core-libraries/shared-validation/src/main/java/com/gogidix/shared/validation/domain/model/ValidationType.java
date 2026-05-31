package com.gogidix.shared.validation.domain.model;

/**
 * Enumeration of supported validation types.
 */
public enum ValidationType {
    
    // Basic validations
    REQUIRED("Required", "Field must not be null or empty"),
    NOT_NULL("Not Null", "Field must not be null"),
    NOT_EMPTY("Not Empty", "Field must not be empty"),
    NOT_BLANK("Not Blank", "Field must not be blank"),
    
    // Format validations
    EMAIL("Email", "Valid email address format"),
    URL("URL", "Valid URL format"),
    PHONE("Phone Number", "Valid phone number format"),
    CREDIT_CARD("Credit Card", "Valid credit card number"),
    SSN("Social Security Number", "Valid social security number"),
    REGEX("Regular Expression", "Matches specified pattern"),
    
    // Range validations
    NUMERIC_RANGE("Numeric Range", "Number within specified range"),
    LENGTH_RANGE("Length Range", "Text length within specified range"),
    DATE_RANGE("Date Range", "Date within specified range"),
    
    // Collection validations
    ENUM_VALUES("Allowed Values", "Value from predefined set"),
    DATE_FORMAT("Date Format", "Date in specified format"),
    
    // Complex validations
    CUSTOM("Custom Validation", "Custom validation logic"),
    BUSINESS_LOGIC("Business Logic", "Business rule validation"),
    CROSS_FIELD("Cross-Field", "Validation across multiple fields"),
    
    // Conditional validations
    CONDITIONAL("Conditional", "Validation based on conditions"),
    DEPENDENT("Dependent", "Validation dependent on other fields");
    
    private final String displayName;
    private final String description;
    
    ValidationType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    /**
     * Gets the display name of the validation type.
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets the description of the validation type.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Checks if this validation type requires a value to be present.
     */
    public boolean requiresValue() {
        return this != NOT_NULL && this != REQUIRED && this != NOT_EMPTY && this != NOT_BLANK;
    }
    
    /**
     * Checks if this validation type supports parameters.
     */
    public boolean supportsParameters() {
        return switch (this) {
            case REGEX, NUMERIC_RANGE, LENGTH_RANGE, DATE_RANGE, ENUM_VALUES, 
                 DATE_FORMAT, CUSTOM, BUSINESS_LOGIC, CROSS_FIELD, 
                 CONDITIONAL, DEPENDENT -> true;
            default -> false;
        };
    }
    
    /**
     * Gets validation types suitable for text fields.
     */
    public static ValidationType[] getTextValidations() {
        return new ValidationType[]{
            REQUIRED, NOT_NULL, NOT_EMPTY, NOT_BLANK,
            EMAIL, URL, PHONE, REGEX, LENGTH_RANGE, 
            ENUM_VALUES, CUSTOM
        };
    }
    
    /**
     * Gets validation types suitable for numeric fields.
     */
    public static ValidationType[] getNumericValidations() {
        return new ValidationType[]{
            REQUIRED, NOT_NULL, NUMERIC_RANGE, CUSTOM
        };
    }
    
    /**
     * Gets validation types suitable for date fields.
     */
    public static ValidationType[] getDateValidations() {
        return new ValidationType[]{
            REQUIRED, NOT_NULL, DATE_RANGE, DATE_FORMAT, CUSTOM
        };
    }
    
    /**
     * Gets validation types suitable for collection fields.
     */
    public static ValidationType[] getCollectionValidations() {
        return new ValidationType[]{
            REQUIRED, NOT_NULL, NOT_EMPTY, LENGTH_RANGE, CUSTOM
        };
    }
}
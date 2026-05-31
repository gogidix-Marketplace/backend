package com.gogidix.ecosystem.shared.validation;

import com.gogidix.shared.validation.domain.model.PhoneValidationPattern;

/**
 * Legacy phone validator - delegates to new hexagonal architecture implementation.
 * @deprecated Use {@link PhoneValidationPattern} directly for new code.
 */
@Deprecated(since = "2.0.0", forRemoval = true)
public class PhoneValidator {
    
    /**
     * Validates phone number using basic pattern.
     * @param phone Phone number to validate
     * @return true if phone number is valid, false otherwise
     * @deprecated Use {@link PhoneValidationPattern#isValid(String)} instead
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    public static boolean isValid(String phone) {
        return PhoneValidationPattern.isValid(phone);
    }
    
    /**
     * Validates phone number and throws exception if invalid.
     * @param phone Phone number to validate
     * @throws IllegalArgumentException if phone number is invalid
     * @deprecated Use {@link PhoneValidationPattern#getValidationInfo(String)} for detailed validation
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    public static void validate(String phone) {
        if (!isValid(phone)) {
            throw new IllegalArgumentException("Invalid phone format: " + phone);
        }
    }
}
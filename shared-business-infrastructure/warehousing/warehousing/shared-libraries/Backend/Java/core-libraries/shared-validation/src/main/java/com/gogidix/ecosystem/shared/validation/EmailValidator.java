package com.gogidix.ecosystem.shared.validation;

import com.gogidix.shared.validation.domain.model.EmailValidationPattern;

/**
 * Legacy email validator - delegates to new hexagonal architecture implementation.
 * @deprecated Use {@link EmailValidationPattern} directly for new code.
 */
@Deprecated(since = "2.0.0", forRemoval = true)
public class EmailValidator {
    
    /**
     * Validates email using basic pattern.
     * @param email Email address to validate
     * @return true if email is valid, false otherwise
     * @deprecated Use {@link EmailValidationPattern#isValid(String)} instead
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    public static boolean isValid(String email) {
        return EmailValidationPattern.isValid(email);
    }
    
    /**
     * Validates email and throws exception if invalid.
     * @param email Email address to validate
     * @throws IllegalArgumentException if email is invalid
     * @deprecated Use {@link EmailValidationPattern#getValidationInfo(String)} for detailed validation
     */
    @Deprecated(since = "2.0.0", forRemoval = true)
    public static void validate(String email) {
        if (!isValid(email)) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
    }
}
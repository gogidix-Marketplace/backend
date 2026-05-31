package com.gogidix.aiservices.aiauthenticationservice.domain.policy;

import com.gogidix.aiservices.aiauthenticationservice.domain.model.PasswordStrength;

import java.util.List;
import java.util.Set;

public class PasswordPolicy {
    private static final Set<String> COMMON_PASSWORDS = Set.of(
            "Password123!", "Welcome123!", "Admin123!", "Password1!",
            "Welcome1!", "Admin1!", "Password@123", "Welcome@123"
    );

    private static final int MIN_LENGTH = 8;
    private static final double MIN_ADMIN_STRENGTH_SCORE = 0.8;

    public ValidationResult validate(String password) {
        ValidationResult result = new ValidationResult();

        if (password == null || password.isEmpty()) {
            result.addError("Password cannot be empty");
            return result;
        }

        if (password.length() < MIN_LENGTH) {
            result.addError("Password must be at least " + MIN_LENGTH + " characters long");
        }

        if (!password.matches(".*[A-Z].*")) {
            result.addError("Password must contain at least one uppercase letter");
        }

        if (!password.matches(".*[a-z].*")) {
            result.addError("Password must contain at least one lowercase letter");
        }

        if (!password.matches(".*\\d.*")) {
            result.addError("Password must contain at least one digit");
        }

        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            result.addError("Password must contain at least one special character");
        }

        if (COMMON_PASSWORDS.contains(password)) {
            result.addError("Password is too common");
        }

        return result;
    }

    public ValidationResult validateForAdmin(String password) {
        ValidationResult result = validate(password);
        if (result.isValid()) {
            PasswordStrength strength = calculateStrength(password);
            if (strength == PasswordStrength.WEAK || strength == PasswordStrength.MEDIUM) {
                result.addError("Admin passwords must be strong");
            }
        }
        return result;
    }

    public PasswordStrength calculateStrength(String password) {
        int score = 0;

        // Length
        if (password.length() >= 8) score += 1;
        if (password.length() >= 12) score += 1;
        if (password.length() >= 16) score += 1;

        // Character types
        if (password.matches(".*[a-z].*")) score += 1;
        if (password.matches(".*[A-Z].*")) score += 1;
        if (password.matches(".*\\d.*")) score += 1;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) score += 1;

        // Complexity
        if (password.matches(".*[a-z].*") && password.matches(".*[A-Z].*") &&
            password.matches(".*\\d.**") && password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            score += 2;
        }

        if (score <= 3) return PasswordStrength.WEAK;
        if (score <= 5) return PasswordStrength.MEDIUM;
        return PasswordStrength.STRONG;
    }

    public static class ValidationResult {
        private final List<String> errors = new java.util.ArrayList<>();

        public void addError(String error) {
            errors.add(error);
        }

        public boolean isValid() {
            return errors.isEmpty();
        }

        public List<String> getErrors() {
            return errors;
        }
    }
}

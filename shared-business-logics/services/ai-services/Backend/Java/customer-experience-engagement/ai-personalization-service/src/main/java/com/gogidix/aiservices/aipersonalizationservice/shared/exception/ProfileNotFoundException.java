package com.gogidix.aiservices.aipersonalizationservice.shared.exception;

public class ProfileNotFoundException extends PersonalizationException {
    public ProfileNotFoundException(String message) {
        super(message);
    }

    public static ProfileNotFoundException forUser(String userId) {
        return new ProfileNotFoundException("Profile not found for user: " + userId);
    }
}

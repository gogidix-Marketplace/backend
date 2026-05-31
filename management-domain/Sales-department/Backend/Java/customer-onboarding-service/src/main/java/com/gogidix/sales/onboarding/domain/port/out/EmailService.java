package com.gogidix.sales.onboarding.domain.port.out;

import com.gogidix.sales.onboarding.domain.model.Onboarding;

import java.util.Map;

/**
 * Email Service Interface (Port)
 * Defines the contract for sending emails
 */
public interface EmailService {

    /**
     * Sends welcome email
     */
    void sendWelcomeEmail(Onboarding onboarding, Map<String, Object> context);

    /**
     * Sends step completion email
     */
    void sendStepCompletionEmail(Onboarding onboarding, String stepName, Map<String, Object> context);

    /**
     * Sends onboarding completion email
     */
    void sendOnboardingCompletionEmail(Onboarding onboarding, Map<String, Object> context);

    /**
     * Sends assignment email
     */
    void sendAssignmentEmail(Onboarding onboarding, String assignedTo, Map<String, Object> context);

    /**
     * Checks if the email service is ready
     */
    boolean isReady();
}

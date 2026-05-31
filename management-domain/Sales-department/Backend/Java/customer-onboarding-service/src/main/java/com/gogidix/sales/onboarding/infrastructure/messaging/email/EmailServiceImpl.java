package com.gogidix.sales.onboarding.infrastructure.messaging.email;

import com.gogidix.sales.onboarding.domain.model.Onboarding;
import com.gogidix.sales.onboarding.domain.port.out.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.from:noreply@gogidix.com}")
    private String fromEmail;

    @Value("${spring.mail.properties.mail.from-name:Gogidix}")
    private String fromName;

    @Value("${customer-onboarding-service.onboarding.email.enabled:true}")
    private boolean emailEnabled;

    @Override
    public void sendWelcomeEmail(Onboarding onboarding, Map<String, Object> context) {
        if (!emailEnabled) {
            log.debug("Email sending is disabled. Skipping welcome email.");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(onboarding.getCustomerEmail());
            helper.setSubject("Welcome to Gogidix - Let's Get Started!");

            String emailBody = buildWelcomeEmailBody(onboarding, context);
            helper.setText(emailBody, true);

            mailSender.send(message);
            log.info("Sent welcome email to: {} for onboarding: {}",
                    onboarding.getCustomerEmail(), onboarding.getOnboardingId());

        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send welcome email for onboarding: {}",
                    onboarding.getOnboardingId(), e);
        }
    }

    @Override
    public void sendStepCompletionEmail(Onboarding onboarding, String stepName, Map<String, Object> context) {
        if (!emailEnabled) {
            log.debug("Email sending is disabled. Skipping step completion email.");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(onboarding.getCustomerEmail());
            helper.setSubject("Onboarding Update: " + stepName + " Completed");

            String emailBody = buildStepCompletionEmailBody(onboarding, stepName, context);
            helper.setText(emailBody, true);

            mailSender.send(message);
            log.info("Sent step completion email to: {} for step: {}",
                    onboarding.getCustomerEmail(), stepName);

        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send step completion email for onboarding: {}",
                    onboarding.getOnboardingId(), e);
        }
    }

    @Override
    public void sendOnboardingCompletionEmail(Onboarding onboarding, Map<String, Object> context) {
        if (!emailEnabled) {
            log.debug("Email sending is disabled. Skipping onboarding completion email.");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(onboarding.getCustomerEmail());
            helper.setSubject("Congratulations! Your Onboarding is Complete");

            String emailBody = buildCompletionEmailBody(onboarding, context);
            helper.setText(emailBody, true);

            mailSender.send(message);
            log.info("Sent onboarding completion email to: {}",
                    onboarding.getCustomerEmail());

        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send onboarding completion email for onboarding: {}",
                    onboarding.getOnboardingId(), e);
        }
    }

    @Override
    public void sendAssignmentEmail(Onboarding onboarding, String assignedTo, Map<String, Object> context) {
        log.info("Onboarding: {} assigned to: {}. Email would be sent to: {}",
                onboarding.getOnboardingId(), assignedTo, assignedTo);
    }

    @Override
    public boolean isReady() {
        return mailSender != null && emailEnabled;
    }

    private String buildWelcomeEmailBody(Onboarding onboarding, Map<String, Object> context) {
        return "<html><body>" +
                "<h2>Welcome to Gogidix, " + onboarding.getCustomerName() + "!</h2>" +
                "<p>We're excited to have you on board. Your onboarding journey has begun.</p>" +
                "<p><strong>Onboarding ID:</strong> " + onboarding.getOnboardingId() + "</p>" +
                "<p><strong>Customer Type:</strong> " + onboarding.getCustomerType().getValue() + "</p>" +
                "<p>Your onboarding process consists of several steps designed to get you up and running quickly.</p>" +
                "<p>You can track your progress in your dashboard.</p>" +
                "<p>If you have any questions, don't hesitate to reach out to our support team.</p>" +
                "<p>Best regards,<br>The Gogidix Team</p>" +
                "</body></html>";
    }

    private String buildStepCompletionEmailBody(Onboarding onboarding, String stepName, Map<String, Object> context) {
        return "<html><body>" +
                "<h2>Onboarding Progress Update</h2>" +
                "<p>Hi " + onboarding.getCustomerName() + ",</p>" +
                "<p>Great news! You've completed the step: <strong>" + stepName + "</strong></p>" +
                "<p><strong>Overall Progress:</strong> " + onboarding.getProgressPercentage() + "%</p>" +
                "<p>You're making great progress! Keep up the good work.</p>" +
                "<p>Best regards,<br>The Gogidix Team</p>" +
                "</body></html>";
    }

    private String buildCompletionEmailBody(Onboarding onboarding, Map<String, Object> context) {
        return "<html><body>" +
                "<h2>Congratulations, " + onboarding.getCustomerName() + "!</h2>" +
                "<p>Your onboarding is now complete!</p>" +
                "<p><strong>Onboarding ID:</strong> " + onboarding.getOnboardingId() + "</p>" +
                "<p><strong>Duration:</strong> " + (onboarding.getActualDurationMinutes() != null ?
                onboarding.getActualDurationMinutes() / 60 : "N/A") + " hours</p>" +
                "<p>You're all set to start using our platform. If you need any assistance, our support team is here to help.</p>" +
                "<p>Welcome to the Gogidix family!</p>" +
                "<p>Best regards,<br>The Gogidix Team</p>" +
                "</body></html>";
    }
}

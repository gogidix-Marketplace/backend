package com.gogidix.shared.infrastructure.services.communication.email.infrastructure.email;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EmailSenderGateway interface contract.
 */
@DisplayName("Email Sender Gateway Contract Tests")
class EmailSenderGatewayTest {

    @Test
    @DisplayName("Should verify EmailAttachment record")
    void shouldVerifyEmailAttachmentRecord() {
        byte[] content = "test content".getBytes();

        EmailSenderGateway.EmailAttachment attachment1 = new EmailSenderGateway.EmailAttachment(
                "test.txt", content, "text/plain"
        );

        assertEquals("test.txt", attachment1.filename());
        assertEquals(content, attachment1.content());
        assertEquals("text/plain", attachment1.contentType());
        assertEquals("attachment", attachment1.disposition());

        EmailSenderGateway.EmailAttachment attachment2 = new EmailSenderGateway.EmailAttachment(
                "test.txt", content, "text/plain"
        );

        assertEquals("attachment", attachment2.disposition());
    }

    @Test
    @DisplayName("Should verify EmailSendingException")
    void shouldVerifyEmailSendingException() {
        EmailSenderGateway.EmailSendingException exception1 =
                new EmailSenderGateway.EmailSendingException("Failed to send", "test@example.com");

        assertEquals("Failed to send", exception1.getMessage());
        assertEquals("test@example.com", exception1.getRecipient());
        assertNull(exception1.getErrorCode());

        EmailSenderGateway.EmailSendingException exception2 =
                new EmailSenderGateway.EmailSendingException("Failed to send", "test@example.com", "ERR_001");

        assertEquals("ERR_001", exception2.getErrorCode());

        EmailSenderGateway.EmailSendingException exception3 =
                new EmailSenderGateway.EmailSendingException("Failed to send", "test@example.com", new RuntimeException("Cause"));

        assertNotNull(exception3.getCause());
    }

    @Test
    @DisplayName("Should verify EmailAttachment equals")
    void shouldVerifyEmailAttachmentEquals() {
        byte[] content = "test".getBytes();
        EmailSenderGateway.EmailAttachment attachment1 = new EmailSenderGateway.EmailAttachment(
                "file.txt", content, "text/plain"
        );
        EmailSenderGateway.EmailAttachment attachment2 = new EmailSenderGateway.EmailAttachment(
                "file.txt", content, "text/plain"
        );

        assertEquals(attachment1.filename(), attachment2.filename());
        assertEquals(attachment1.contentType(), attachment2.contentType());
    }
}

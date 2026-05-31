package com.gogidix.shared.infrastructure.services.communication.email.application.service;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.services.communication.email.application.dto.request.CreateEmailMessageRequestDto;
import com.gogidix.shared.infrastructure.services.communication.email.application.dto.response.EmailMessageResponseDto;
import com.gogidix.shared.infrastructure.services.communication.email.application.mapper.EmailMessageMapper;
import com.gogidix.shared.infrastructure.services.communication.email.domain.model.EmailMessage;
import com.gogidix.shared.infrastructure.services.communication.email.domain.port.in.IEmailMessageUseCase;
import com.gogidix.shared.infrastructure.services.communication.email.domain.port.out.IEmailMessageRepository;
import com.gogidix.shared.infrastructure.services.communication.email.infrastructure.email.EmailSenderGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for EmailMessageService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Email Message Service Tests")
class EmailMessageServiceTest {

    @Mock
    private EmailMessageMapper mapper;

    @Mock
    private IEmailMessageRepository repository;

    @Mock
    private TenantContextHolder tenantContextHolder;

    @Mock
    private EmailSenderGateway emailSenderGateway;

    @InjectMocks
    private EmailMessageService emailMessageService;

    private static final String TENANT_ID = "tenant123";

    @BeforeEach
    void setUp() {
        lenient().when(tenantContextHolder.getRequiredTenantId()).thenReturn(TENANT_ID);
    }

    @Test
    @DisplayName("Should create email message")
    void shouldCreateEmailMessage() {
        CreateEmailMessageRequestDto dto = new CreateEmailMessageRequestDto(
                "test@example.com", "cc@example.com", "bcc@example.com",
                "Test Subject", "Test Body", "template", null, null
        );

        EmailMessage entity = new EmailMessage();
        entity.setId("email123");
        entity.setTo("test@example.com");

        when(mapper.toEntity(dto, TENANT_ID)).thenReturn(entity);
        when(repository.save(any(EmailMessage.class))).thenReturn(entity);
        when(mapper.toResponseDto(any())).thenReturn(new EmailMessageResponseDto(
                "email123", TENANT_ID, "test@example.com", "cc@example.com",
                "bcc@example.com", "Test Subject", "Test Body", "template",
                "PENDING", 0, 3, null, "SMTP", null,
                LocalDateTime.now(), null
        ));

        EmailMessageResponseDto response = emailMessageService.create(dto);

        assertNotNull(response);
        assertEquals("email123", response.id());
        verify(mapper).toEntity(dto, TENANT_ID);
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Should find email by ID")
    void shouldFindEmailById() {
        EmailMessage entity = new EmailMessage();
        entity.setId("email123");
        entity.setTo("test@example.com");

        when(repository.findByIdAndTenantId("email123", TENANT_ID)).thenReturn(entity);
        when(mapper.toResponseDto(any())).thenReturn(new EmailMessageResponseDto(
                "email123", TENANT_ID, "test@example.com", null,
                null, "Subject", "Body", null,
                "PENDING", 0, 3, null, "SMTP", null,
                LocalDateTime.now(), null
        ));

        EmailMessageResponseDto response = emailMessageService.findById("email123");

        assertNotNull(response);
        assertEquals("email123", response.id());
        verify(repository).findByIdAndTenantId("email123", TENANT_ID);
    }

    @Test
    @DisplayName("Should throw exception when email not found by ID")
    void shouldThrowExceptionWhenEmailNotFoundById() {
        when(repository.findByIdAndTenantId("email123", TENANT_ID)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> emailMessageService.findById("email123"));
    }

    @Test
    @DisplayName("Should find all emails")
    void shouldFindAllEmails() {
        EmailMessage entity1 = new EmailMessage();
        entity1.setId("email1");
        EmailMessage entity2 = new EmailMessage();
        entity2.setId("email2");

        when(repository.findAllByTenantId(TENANT_ID)).thenReturn(List.of(entity1, entity2));
        when(mapper.toResponseDto(any())).thenReturn(new EmailMessageResponseDto(
                "email1", TENANT_ID, "test@example.com", null,
                null, "Subject", "Body", null,
                "PENDING", 0, 3, null, "SMTP", null,
                LocalDateTime.now(), null
        ));

        List<EmailMessageResponseDto> response = emailMessageService.findAll();

        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test
    @DisplayName("Should find emails by recipient")
    void shouldFindEmailsByRecipient() {
        EmailMessage entity = new EmailMessage();
        entity.setId("email123");

        when(repository.findByToAndTenantId("test@example.com", TENANT_ID)).thenReturn(List.of(entity));
        when(mapper.toResponseDto(any())).thenReturn(new EmailMessageResponseDto(
                "email123", TENANT_ID, "test@example.com", null,
                null, "Subject", "Body", null,
                "PENDING", 0, 3, null, "SMTP", null,
                LocalDateTime.now(), null
        ));

        List<EmailMessageResponseDto> response = emailMessageService.findByTo("test@example.com");

        assertNotNull(response);
        assertEquals(1, response.size());
        verify(repository).findByToAndTenantId("test@example.com", TENANT_ID);
    }

    @Test
    @DisplayName("Should find emails by status")
    void shouldFindEmailsByStatus() {
        EmailMessage entity = new EmailMessage();
        entity.setId("email123");

        when(repository.findByStatusAndTenantId("PENDING", TENANT_ID)).thenReturn(List.of(entity));
        when(mapper.toResponseDto(any())).thenReturn(new EmailMessageResponseDto(
                "email123", TENANT_ID, "test@example.com", null,
                null, "Subject", "Body", null,
                "PENDING", 0, 3, null, "SMTP", null,
                LocalDateTime.now(), null
        ));

        List<EmailMessageResponseDto> response = emailMessageService.findByStatus("PENDING");

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    @DisplayName("Should find emails by campaign ID")
    void shouldFindEmailsByCampaignId() {
        EmailMessage entity = new EmailMessage();
        entity.setId("email123");

        when(repository.findByCampaignIdAndTenantId("campaign123", TENANT_ID)).thenReturn(List.of(entity));
        when(mapper.toResponseDto(any())).thenReturn(new EmailMessageResponseDto(
                "email123", TENANT_ID, "test@example.com", null,
                null, "Subject", "Body", null,
                "PENDING", 0, 3, null, "SMTP", null,
                LocalDateTime.now(), null
        ));

        List<EmailMessageResponseDto> response = emailMessageService.findByCampaignId("campaign123");

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    @DisplayName("Should delete email by ID")
    void shouldDeleteEmailById() {
        EmailMessage entity = new EmailMessage();
        entity.setId("email123");

        when(repository.findByIdAndTenantId("email123", TENANT_ID)).thenReturn(entity);
        doNothing().when(repository).deleteByIdAndTenantId("email123", TENANT_ID);

        emailMessageService.delete("email123");

        verify(repository).deleteByIdAndTenantId("email123", TENANT_ID);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent email")
    void shouldThrowExceptionWhenDeletingNonExistentEmail() {
        when(repository.findByIdAndTenantId("email123", TENANT_ID)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> emailMessageService.delete("email123"));
    }

    @Test
    @DisplayName("Should cleanup old messages")
    void shouldCleanupOldMessages() {
        doNothing().when(repository).deleteOldByTenantIdAndCreatedAt(eq(TENANT_ID), any(LocalDateTime.class));

        emailMessageService.cleanupOldMessages(30);

        verify(repository).deleteOldByTenantIdAndCreatedAt(eq(TENANT_ID), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Should get email statistics")
    void shouldGetEmailStatistics() {
        when(repository.countByTenantId(TENANT_ID)).thenReturn(100L);
        when(repository.countByStatusAndTenantId("PENDING", TENANT_ID)).thenReturn(10L);
        when(repository.countByStatusAndTenantId("SENT", TENANT_ID)).thenReturn(80L);
        when(repository.countByStatusAndTenantId("FAILED", TENANT_ID)).thenReturn(5L);
        when(repository.countByStatusAndTenantId("RETRYING", TENANT_ID)).thenReturn(5L);

        IEmailMessageUseCase.EmailMessageStatsDto stats = emailMessageService.getStats();

        assertNotNull(stats);
        assertEquals(100, stats.totalCount());
        assertEquals(10, stats.pendingCount());
        assertEquals(80, stats.sentCount());
        assertEquals(5, stats.failedCount());
        assertEquals(5, stats.retryingCount());
    }

    @Test
    @DisplayName("Should send plain text email")
    void shouldSendPlainTextEmail() {
        EmailMessage entity = new EmailMessage();
        entity.setId("email123");
        entity.setTo("test@example.com");
        entity.setSubject("Test Subject");
        entity.setBody("Test Body");
        entity.setStatus("PENDING");

        when(repository.findByIdAndTenantId("email123", TENANT_ID)).thenReturn(entity);
        when(emailSenderGateway.sendEmail("test@example.com", "Test Subject", "Test Body")).thenReturn(true);
        when(repository.save(any(EmailMessage.class))).thenReturn(entity);

        boolean result = emailMessageService.sendEmail("email123");

        assertTrue(result);
        assertEquals("SENT", entity.getStatus());
        assertNotNull(entity.getSentAt());
    }

    @Test
    @DisplayName("Should send HTML email")
    void shouldSendHtmlEmail() {
        EmailMessage entity = new EmailMessage();
        entity.setId("email123");
        entity.setTo("test@example.com");
        entity.setSubject("Test Subject");
        entity.setBody("<html><body>Test Body</body></html>");
        entity.setStatus("PENDING");

        when(repository.findByIdAndTenantId("email123", TENANT_ID)).thenReturn(entity);
        when(emailSenderGateway.sendHtmlEmail("test@example.com", "Test Subject", entity.getBody())).thenReturn(true);
        when(repository.save(any(EmailMessage.class))).thenReturn(entity);

        boolean result = emailMessageService.sendEmail("email123");

        assertTrue(result);
        assertEquals("SENT", entity.getStatus());
        verify(emailSenderGateway).sendHtmlEmail(eq("test@example.com"), eq("Test Subject"), anyString());
    }

    @Test
    @DisplayName("Should handle already sent email")
    void shouldHandleAlreadySentEmail() {
        EmailMessage entity = new EmailMessage();
        entity.setId("email123");
        entity.setStatus("SENT");

        when(repository.findByIdAndTenantId("email123", TENANT_ID)).thenReturn(entity);

        boolean result = emailMessageService.sendEmail("email123");

        assertTrue(result);
        verify(emailSenderGateway, never()).sendEmail(any(), any(), any());
    }

    @Test
    @DisplayName("Should handle email sending failure")
    void shouldHandleEmailSendingFailure() {
        EmailMessage entity = new EmailMessage();
        entity.setId("email123");
        entity.setTo("test@example.com");
        entity.setSubject("Test Subject");
        entity.setBody("Test Body");
        entity.setStatus("PENDING");

        when(repository.findByIdAndTenantId("email123", TENANT_ID)).thenReturn(entity);
        when(emailSenderGateway.sendEmail("test@example.com", "Test Subject", "Test Body")).thenReturn(false);
        when(repository.save(any(EmailMessage.class))).thenReturn(entity);

        boolean result = emailMessageService.sendEmail("email123");

        assertFalse(result);
        assertEquals("FAILED", entity.getStatus());
        assertNotNull(entity.getErrorMessage());
    }

    @Test
    @DisplayName("Should create and send email in one operation")
    void shouldCreateAndSendEmail() {
        CreateEmailMessageRequestDto dto = new CreateEmailMessageRequestDto(
                "test@example.com", null, null,
                "Subject", "Body", null, null, null
        );

        EmailMessage entity = new EmailMessage();
        entity.setId("email123");
        entity.setTo("test@example.com");
        entity.setSubject("Subject");
        entity.setBody("Body");
        entity.setStatus("PENDING");

        EmailMessageResponseDto responseDto = new EmailMessageResponseDto(
                "email123", TENANT_ID, "test@example.com", null,
                null, "Subject", "Body", null,
                "PENDING", 0, 3, null, "SMTP", null,
                LocalDateTime.now(), null
        );

        when(mapper.toEntity(dto, TENANT_ID)).thenReturn(entity);
        when(repository.save(any(EmailMessage.class))).thenReturn(entity);
        when(repository.findByIdAndTenantId("email123", TENANT_ID)).thenReturn(entity);
        when(mapper.toResponseDto(any())).thenReturn(responseDto);
        when(emailSenderGateway.sendEmail(any(), any(), any())).thenReturn(true);

        EmailMessageResponseDto response = emailMessageService.createAndSend(dto);

        assertNotNull(response);
        assertEquals("email123", response.id());
        verify(emailSenderGateway).sendEmail("test@example.com", "Subject", "Body");
    }

    @Test
    @DisplayName("Should check gateway health")
    void shouldCheckGatewayHealth() {
        when(emailSenderGateway.isHealthy()).thenReturn(true);

        boolean healthy = emailMessageService.isGatewayHealthy();

        assertTrue(healthy);
        verify(emailSenderGateway).isHealthy();
    }

    @Test
    @DisplayName("Should get gateway provider")
    void shouldGetGatewayProvider() {
        when(emailSenderGateway.getProvider()).thenReturn("smtp");

        String provider = emailMessageService.getGatewayProvider();

        assertEquals("smtp", provider);
        verify(emailSenderGateway).getProvider();
    }

    @Test
    @DisplayName("Should handle gateway exception")
    void shouldHandleGatewayException() {
        EmailMessage entity = new EmailMessage();
        entity.setId("email123");
        entity.setTo("test@example.com");
        entity.setSubject("Test Subject");
        entity.setBody("Test Body");
        entity.setStatus("PENDING");

        when(repository.findByIdAndTenantId("email123", TENANT_ID)).thenReturn(entity);
        when(emailSenderGateway.sendEmail("test@example.com", "Test Subject", "Test Body"))
                .thenThrow(new EmailSenderGateway.EmailSendingException("Connection failed", "test@example.com"));
        when(repository.save(any(EmailMessage.class))).thenReturn(entity);

        boolean result = emailMessageService.sendEmail("email123");

        assertFalse(result);
        assertEquals("FAILED", entity.getStatus());
        assertNotNull(entity.getErrorMessage());
    }
}

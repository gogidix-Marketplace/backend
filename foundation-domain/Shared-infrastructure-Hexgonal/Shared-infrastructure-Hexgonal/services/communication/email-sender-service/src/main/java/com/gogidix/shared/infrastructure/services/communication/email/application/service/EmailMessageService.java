package com.gogidix.shared.infrastructure.services.communication.email.application.service;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.services.communication.email.application.dto.request.CreateEmailMessageRequestDto;
import com.gogidix.shared.infrastructure.services.communication.email.application.dto.response.EmailMessageResponseDto;
import com.gogidix.shared.infrastructure.services.communication.email.application.mapper.EmailMessageMapper;
import com.gogidix.shared.infrastructure.services.communication.email.domain.model.EmailMessage;
import com.gogidix.shared.infrastructure.services.communication.email.domain.port.in.IEmailMessageUseCase;
import com.gogidix.shared.infrastructure.services.communication.email.domain.port.out.IEmailMessageRepository;
import com.gogidix.shared.infrastructure.services.communication.email.infrastructure.email.EmailSenderGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Application service for EmailMessage management.
 * Integrates with SMTP gateway for actual email sending.
 */
@Service
@Transactional
public class EmailMessageService implements IEmailMessageUseCase {

    private static final Logger logger = LoggerFactory.getLogger(EmailMessageService.class);

    private final EmailMessageMapper mapper;
    private final IEmailMessageRepository repository;
    private final TenantContextHolder tenantContextHolder;
    private final EmailSenderGateway emailSenderGateway;

    public EmailMessageService(EmailMessageMapper mapper,
                               IEmailMessageRepository repository,
                               TenantContextHolder tenantContextHolder,
                               EmailSenderGateway emailSenderGateway) {
        this.mapper = mapper;
        this.repository = repository;
        this.tenantContextHolder = tenantContextHolder;
        this.emailSenderGateway = emailSenderGateway;
    }

    @Override
    public EmailMessageResponseDto create(CreateEmailMessageRequestDto dto) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        EmailMessage entity = mapper.toEntity(dto, tenantId);
        EmailMessage saved = repository.save(entity);
        return mapper.toResponseDto(saved);
    }

    @Override
    public EmailMessageResponseDto findById(String id) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        EmailMessage entity = repository.findByIdAndTenantId(id, tenantId);
        if (entity == null) {
            throw new IllegalArgumentException("EmailMessage not found with id: " + id);
        }
        return mapper.toResponseDto(entity);
    }

    @Override
    public List<EmailMessageResponseDto> findAll() {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findAllByTenantId(tenantId).stream()
            .map(mapper::toResponseDto)
            .toList();
    }

    @Override
    public List<EmailMessageResponseDto> findByTo(String to) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findByToAndTenantId(to, tenantId).stream()
            .map(mapper::toResponseDto)
            .toList();
    }

    @Override
    public List<EmailMessageResponseDto> findByStatus(String status) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findByStatusAndTenantId(status, tenantId).stream()
            .map(mapper::toResponseDto)
            .toList();
    }

    @Override
    public List<EmailMessageResponseDto> findByCampaignId(String campaignId) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findByCampaignIdAndTenantId(campaignId, tenantId).stream()
            .map(mapper::toResponseDto)
            .toList();
    }

    @Override
    public void delete(String id) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        EmailMessage entity = repository.findByIdAndTenantId(id, tenantId);
        if (entity == null) {
            throw new IllegalArgumentException("EmailMessage not found with id: " + id);
        }
        repository.deleteByIdAndTenantId(id, tenantId);
    }

    @Override
    public void cleanupOldMessages(int daysToKeep) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        LocalDateTime cutoff = LocalDateTime.now().minusDays(daysToKeep);
        repository.deleteOldByTenantIdAndCreatedAt(tenantId, cutoff);
    }

    @Override
    public EmailMessageStatsDto getStats() {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        long totalCount = repository.countByTenantId(tenantId);
        long pendingCount = repository.countByStatusAndTenantId("PENDING", tenantId);
        long sentCount = repository.countByStatusAndTenantId("SENT", tenantId);
        long failedCount = repository.countByStatusAndTenantId("FAILED", tenantId);
        long retryingCount = repository.countByStatusAndTenantId("RETRYING", tenantId);
        return new EmailMessageStatsDto(totalCount, pendingCount, sentCount, failedCount, retryingCount);
    }

    /**
     * Send an email message immediately via SMTP gateway.
     *
     * @param emailId the email message ID to send
     * @return true if sent successfully
     */
    @Transactional
    public boolean sendEmail(String emailId) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        EmailMessage entity = repository.findByIdAndTenantId(emailId, tenantId);
        if (entity == null) {
            throw new IllegalArgumentException("EmailMessage not found with id: " + emailId);
        }

        // Check if already sent
        if ("SENT".equals(entity.getStatus())) {
            logger.info("Email {} already sent, skipping", emailId);
            return true;
        }

        try {
            // Send via SMTP gateway
            boolean sent;
            if (entity.getBody() != null && entity.getBody().contains("<html")) {
                sent = emailSenderGateway.sendHtmlEmail(entity.getTo(), entity.getSubject(), entity.getBody());
            } else {
                sent = emailSenderGateway.sendEmail(entity.getTo(), entity.getSubject(), entity.getBody());
            }

            if (sent) {
                entity.setStatus("SENT");
                entity.setSentAt(LocalDateTime.now());
                repository.save(entity);
                logger.info("Email {} sent successfully to: {}", emailId, entity.getTo());
                return true;
            } else {
                entity.setStatus("FAILED");
                entity.setErrorMessage("Failed to send via gateway");
                repository.save(entity);
                logger.warn("Email {} failed to send to: {}", emailId, entity.getTo());
                return false;
            }
        } catch (EmailSenderGateway.EmailSendingException e) {
            entity.setStatus("FAILED");
            entity.setErrorMessage(e.getMessage());
            repository.save(entity);
            logger.error("Error sending email {}: {}", emailId, e.getMessage());
            return false;
        }
    }

    /**
     * Create and send an email in one operation.
     *
     * @param dto the email message request
     * @return the sent email message response
     */
    @Transactional
    public EmailMessageResponseDto createAndSend(CreateEmailMessageRequestDto dto) {
        EmailMessageResponseDto response = create(dto);
        sendEmail(response.id());
        return response;
    }

    /**
     * Check if the email gateway is healthy and configured.
     *
     * @return true if gateway is operational
     */
    public boolean isGatewayHealthy() {
        return emailSenderGateway.isHealthy();
    }

    /**
     * Get the email gateway provider name.
     *
     * @return the provider name (e.g., "smtp", "sendgrid")
     */
    public String getGatewayProvider() {
        return emailSenderGateway.getProvider();
    }
}

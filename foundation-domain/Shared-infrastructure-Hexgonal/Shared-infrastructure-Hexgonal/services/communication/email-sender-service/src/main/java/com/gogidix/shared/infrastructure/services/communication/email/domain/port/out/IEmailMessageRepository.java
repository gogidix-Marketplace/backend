package com.gogidix.shared.infrastructure.services.communication.email.domain.port.out;

import com.gogidix.shared.infrastructure.services.communication.email.domain.model.EmailMessage;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Output port for EmailMessage repository
 */
public interface IEmailMessageRepository {

    EmailMessage save(EmailMessage entity);
    EmailMessage findById(String id);
    List<EmailMessage> findAllByTenantId(String tenantId);
    EmailMessage findByIdAndTenantId(String id, String tenantId);
    List<EmailMessage> findByToAndTenantId(String to, String tenantId);
    List<EmailMessage> findByStatusAndTenantId(String status, String tenantId);
    List<EmailMessage> findByCampaignIdAndTenantId(String campaignId, String tenantId);
    long countByTenantId(String tenantId);
    long countByStatusAndTenantId(String status, String tenantId);
    void deleteByIdAndTenantId(String id, String tenantId);
    void deleteOldByTenantIdAndCreatedAt(String tenantId, LocalDateTime timestamp);
}

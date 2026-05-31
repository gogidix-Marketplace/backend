package com.gogidix.shared.infrastructure.services.communication.email.infrastructure.persistence;

import com.gogidix.shared.infrastructure.services.communication.email.domain.model.EmailMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailMessageRepository extends MongoRepository<EmailMessage, String> {

    List<EmailMessage> findByTenantId_ValueOrderByCreatedAtDesc(String tenantId);
    List<EmailMessage> findByTenantId_ValueAndTo(String tenantId, String to);
    List<EmailMessage> findByTenantId_ValueAndStatus(String tenantId, String status);
    List<EmailMessage> findByTenantId_ValueAndCampaignId(String tenantId, String campaignId);
    void deleteByTenantId_ValueAndCreatedAtBefore(String tenantId, LocalDateTime timestamp);
    long countByTenantId_Value(String tenantId);
    long countByTenantId_ValueAndStatus(String tenantId, String status);
}

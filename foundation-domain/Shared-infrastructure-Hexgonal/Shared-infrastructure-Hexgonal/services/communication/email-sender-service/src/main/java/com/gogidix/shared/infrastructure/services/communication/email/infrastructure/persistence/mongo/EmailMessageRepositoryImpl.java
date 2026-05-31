package com.gogidix.shared.infrastructure.services.communication.email.infrastructure.persistence.mongo;

import com.gogidix.shared.infrastructure.services.communication.email.domain.model.EmailMessage;
import com.gogidix.shared.infrastructure.services.communication.email.domain.port.out.IEmailMessageRepository;
import com.gogidix.shared.infrastructure.services.communication.email.infrastructure.persistence.EmailMessageRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * MongoDB implementation of EmailMessage repository
 */
@Repository
public class EmailMessageRepositoryImpl implements IEmailMessageRepository {

    private final EmailMessageRepository mongoRepository;

    public EmailMessageRepositoryImpl(EmailMessageRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public EmailMessage save(EmailMessage entity) {
        return mongoRepository.save(entity);
    }

    @Override
    public EmailMessage findById(String id) {
        return mongoRepository.findById(id).orElse(null);
    }

    @Override
    public List<EmailMessage> findAllByTenantId(String tenantId) {
        return mongoRepository.findByTenantId_ValueOrderByCreatedAtDesc(tenantId);
    }

    @Override
    public EmailMessage findByIdAndTenantId(String id, String tenantId) {
        return mongoRepository.findById(id)
                .filter(e -> e.getTenantId().getValue().equals(tenantId))
                .orElse(null);
    }

    @Override
    public List<EmailMessage> findByToAndTenantId(String to, String tenantId) {
        return mongoRepository.findByTenantId_ValueAndTo(tenantId, to);
    }

    @Override
    public List<EmailMessage> findByStatusAndTenantId(String status, String tenantId) {
        return mongoRepository.findByTenantId_ValueAndStatus(tenantId, status);
    }

    @Override
    public List<EmailMessage> findByCampaignIdAndTenantId(String campaignId, String tenantId) {
        return mongoRepository.findByTenantId_ValueAndCampaignId(tenantId, campaignId);
    }

    @Override
    public long countByTenantId(String tenantId) {
        return mongoRepository.countByTenantId_Value(tenantId);
    }

    @Override
    public long countByStatusAndTenantId(String status, String tenantId) {
        return mongoRepository.countByTenantId_ValueAndStatus(tenantId, status);
    }

    @Override
    public void deleteByIdAndTenantId(String id, String tenantId) {
        mongoRepository.findById(id)
            .filter(e -> e.getTenantId().getValue().equals(tenantId))
            .ifPresent(mongoRepository::delete);
    }

    @Override
    public void deleteOldByTenantIdAndCreatedAt(String tenantId, LocalDateTime timestamp) {
        mongoRepository.deleteByTenantId_ValueAndCreatedAtBefore(tenantId, timestamp);
    }
}

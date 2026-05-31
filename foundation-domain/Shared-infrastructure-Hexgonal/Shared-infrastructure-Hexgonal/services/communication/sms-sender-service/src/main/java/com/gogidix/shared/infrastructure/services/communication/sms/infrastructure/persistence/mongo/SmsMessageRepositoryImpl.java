package com.gogidix.shared.infrastructure.services.communication.sms.infrastructure.persistence.mongo;

import com.gogidix.shared.infrastructure.services.communication.sms.domain.model.SmsMessage;
import com.gogidix.shared.infrastructure.services.communication.sms.domain.port.out.ISmsMessageRepository;
import com.gogidix.shared.infrastructure.services.communication.sms.infrastructure.persistence.SmsMessageRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * MongoDB implementation of SmsMessage repository
 */
@Repository
public class SmsMessageRepositoryImpl implements ISmsMessageRepository {

    private final SmsMessageRepository mongoRepository;

    public SmsMessageRepositoryImpl(SmsMessageRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public SmsMessage save(SmsMessage entity) {
        return mongoRepository.save(entity);
    }

    @Override
    public SmsMessage findById(String id) {
        return mongoRepository.findById(id).orElse(null);
    }

    @Override
    public List<SmsMessage> findAllByTenantId(String tenantId) {
        return mongoRepository.findByTenantId_ValueOrderByCreatedAtDesc(tenantId);
    }

    @Override
    public SmsMessage findByIdAndTenantId(String id, String tenantId) {
        return mongoRepository.findById(id)
                .filter(e -> e.getTenantId().getValue().equals(tenantId))
                .orElse(null);
    }

    @Override
    public List<SmsMessage> findByPhoneNumberAndTenantId(String phoneNumber, String tenantId) {
        return mongoRepository.findByTenantId_ValueAndPhoneNumber(tenantId, phoneNumber);
    }

    @Override
    public List<SmsMessage> findByStatusAndTenantId(String status, String tenantId) {
        return mongoRepository.findByTenantId_ValueAndStatus(tenantId, status);
    }

    @Override
    public List<SmsMessage> findByCampaignIdAndTenantId(String campaignId, String tenantId) {
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

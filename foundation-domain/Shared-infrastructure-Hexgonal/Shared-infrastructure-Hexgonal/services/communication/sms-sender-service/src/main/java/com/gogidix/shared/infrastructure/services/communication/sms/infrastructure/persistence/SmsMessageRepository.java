package com.gogidix.shared.infrastructure.services.communication.sms.infrastructure.persistence;

import com.gogidix.shared.infrastructure.services.communication.sms.domain.model.SmsMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SmsMessageRepository extends MongoRepository<SmsMessage, String> {

    List<SmsMessage> findByTenantId_ValueOrderByCreatedAtDesc(String tenantId);
    List<SmsMessage> findByTenantId_ValueAndPhoneNumber(String tenantId, String phoneNumber);
    List<SmsMessage> findByTenantId_ValueAndStatus(String tenantId, String status);
    List<SmsMessage> findByTenantId_ValueAndCampaignId(String tenantId, String campaignId);
    void deleteByTenantId_ValueAndCreatedAtBefore(String tenantId, LocalDateTime timestamp);
    long countByTenantId_Value(String tenantId);
    long countByTenantId_ValueAndStatus(String tenantId, String status);
}

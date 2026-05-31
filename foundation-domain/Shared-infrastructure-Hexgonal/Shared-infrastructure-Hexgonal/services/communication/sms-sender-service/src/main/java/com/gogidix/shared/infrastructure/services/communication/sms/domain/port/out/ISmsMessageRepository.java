package com.gogidix.shared.infrastructure.services.communication.sms.domain.port.out;

import com.gogidix.shared.infrastructure.services.communication.sms.domain.model.SmsMessage;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Output port for SmsMessage repository
 */
public interface ISmsMessageRepository {

    SmsMessage save(SmsMessage entity);
    SmsMessage findById(String id);
    List<SmsMessage> findAllByTenantId(String tenantId);
    SmsMessage findByIdAndTenantId(String id, String tenantId);
    List<SmsMessage> findByPhoneNumberAndTenantId(String phoneNumber, String tenantId);
    List<SmsMessage> findByStatusAndTenantId(String status, String tenantId);
    List<SmsMessage> findByCampaignIdAndTenantId(String campaignId, String tenantId);
    long countByTenantId(String tenantId);
    long countByStatusAndTenantId(String status, String tenantId);
    void deleteByIdAndTenantId(String id, String tenantId);
    void deleteOldByTenantIdAndCreatedAt(String tenantId, LocalDateTime timestamp);
}

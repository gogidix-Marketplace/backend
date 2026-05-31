package com.gogidix.shared.infrastructure.services.communication.sms.domain.port.in;

import com.gogidix.shared.infrastructure.services.communication.sms.application.dto.request.CreateSmsMessageRequestDto;
import com.gogidix.shared.infrastructure.services.communication.sms.application.dto.response.SmsMessageResponseDto;

import java.util.List;

/**
 * Input port for SmsMessage use cases
 */
public interface ISmsMessageUseCase {

    SmsMessageResponseDto create(CreateSmsMessageRequestDto dto);
    SmsMessageResponseDto findById(String id);
    List<SmsMessageResponseDto> findAll();
    List<SmsMessageResponseDto> findByPhoneNumber(String phoneNumber);
    List<SmsMessageResponseDto> findByStatus(String status);
    List<SmsMessageResponseDto> findByCampaignId(String campaignId);
    void delete(String id);
    void cleanupOldMessages(int daysToKeep);
    SmsMessageStatsDto getStats();

    record SmsMessageStatsDto(long totalCount, long pendingCount, long sentCount,
                              long deliveredCount, long failedCount) {}
}

package com.gogidix.shared.infrastructure.services.communication.email.domain.port.in;

import com.gogidix.shared.infrastructure.services.communication.email.application.dto.request.CreateEmailMessageRequestDto;
import com.gogidix.shared.infrastructure.services.communication.email.application.dto.response.EmailMessageResponseDto;

import java.util.List;

/**
 * Input port for EmailMessage use cases
 */
public interface IEmailMessageUseCase {

    EmailMessageResponseDto create(CreateEmailMessageRequestDto dto);
    EmailMessageResponseDto findById(String id);
    List<EmailMessageResponseDto> findAll();
    List<EmailMessageResponseDto> findByTo(String to);
    List<EmailMessageResponseDto> findByStatus(String status);
    List<EmailMessageResponseDto> findByCampaignId(String campaignId);
    void delete(String id);
    void cleanupOldMessages(int daysToKeep);
    EmailMessageStatsDto getStats();

    record EmailMessageStatsDto(long totalCount, long pendingCount, long sentCount,
                                long failedCount, long retryingCount) {}
}

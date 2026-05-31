package com.gogidix.shared.infrastructure.services.communication.sms.application.service;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.services.communication.sms.application.dto.request.CreateSmsMessageRequestDto;
import com.gogidix.shared.infrastructure.services.communication.sms.application.dto.response.SmsMessageResponseDto;
import com.gogidix.shared.infrastructure.services.communication.sms.application.mapper.SmsMessageMapper;
import com.gogidix.shared.infrastructure.services.communication.sms.domain.model.SmsMessage;
import com.gogidix.shared.infrastructure.services.communication.sms.domain.port.in.ISmsMessageUseCase;
import com.gogidix.shared.infrastructure.services.communication.sms.domain.port.out.ISmsMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Application service for SmsMessage management
 */
@Service
@Transactional
public class SmsMessageService implements ISmsMessageUseCase {

    private final SmsMessageMapper mapper;
    private final ISmsMessageRepository repository;
    private final TenantContextHolder tenantContextHolder;

    public SmsMessageService(SmsMessageMapper mapper,
                             ISmsMessageRepository repository,
                             TenantContextHolder tenantContextHolder) {
        this.mapper = mapper;
        this.repository = repository;
        this.tenantContextHolder = tenantContextHolder;
    }

    @Override
    public SmsMessageResponseDto create(CreateSmsMessageRequestDto dto) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        SmsMessage entity = mapper.toEntity(dto, tenantId);
        SmsMessage saved = repository.save(entity);
        return mapper.toResponseDto(saved);
    }

    @Override
    public SmsMessageResponseDto findById(String id) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        SmsMessage entity = repository.findByIdAndTenantId(id, tenantId);
        if (entity == null) {
            throw new IllegalArgumentException("SmsMessage not found with id: " + id);
        }
        return mapper.toResponseDto(entity);
    }

    @Override
    public List<SmsMessageResponseDto> findAll() {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findAllByTenantId(tenantId).stream()
            .map(mapper::toResponseDto)
            .toList();
    }

    @Override
    public List<SmsMessageResponseDto> findByPhoneNumber(String phoneNumber) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findByPhoneNumberAndTenantId(phoneNumber, tenantId).stream()
            .map(mapper::toResponseDto)
            .toList();
    }

    @Override
    public List<SmsMessageResponseDto> findByStatus(String status) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findByStatusAndTenantId(status, tenantId).stream()
            .map(mapper::toResponseDto)
            .toList();
    }

    @Override
    public List<SmsMessageResponseDto> findByCampaignId(String campaignId) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findByCampaignIdAndTenantId(campaignId, tenantId).stream()
            .map(mapper::toResponseDto)
            .toList();
    }

    @Override
    public void delete(String id) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        SmsMessage entity = repository.findByIdAndTenantId(id, tenantId);
        if (entity == null) {
            throw new IllegalArgumentException("SmsMessage not found with id: " + id);
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
    public ISmsMessageUseCase.SmsMessageStatsDto getStats() {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        long totalCount = repository.countByTenantId(tenantId);
        long pendingCount = repository.countByStatusAndTenantId("PENDING", tenantId);
        long sentCount = repository.countByStatusAndTenantId("SENT", tenantId);
        long deliveredCount = repository.countByStatusAndTenantId("DELIVERED", tenantId);
        long failedCount = repository.countByStatusAndTenantId("FAILED", tenantId);
        return new ISmsMessageUseCase.SmsMessageStatsDto(totalCount, pendingCount, sentCount, deliveredCount, failedCount);
    }
}

package com.gogidix.shared.infrastructure.services.security.analytics.application.service;
import com.gogidix.shared.infrastructure.services.security.analytics.application.dto.request.CreateSecurityEventRequestDto;
import com.gogidix.shared.infrastructure.services.security.analytics.application.dto.response.SecurityEventResponseDto;
import com.gogidix.shared.infrastructure.services.security.analytics.application.mapper.SecurityEventMapper;
import com.gogidix.shared.infrastructure.services.security.analytics.domain.exception.SecurityEventNotFoundException;
import com.gogidix.shared.infrastructure.services.security.analytics.domain.exception.SecurityEventValidationException;
import com.gogidix.shared.infrastructure.services.security.analytics.domain.model.SecurityEvent;
import com.gogidix.shared.infrastructure.services.security.analytics.domain.port.in.SecurityEventPort;
import com.gogidix.shared.infrastructure.services.security.analytics.domain.port.out.SecurityEventRepositoryPort;
import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Application service for SecurityEvent management.
 */
@Service
@Transactional
public class SecurityEventService implements SecurityEventPort {
    private final SecurityEventMapper mapper;
    private final SecurityEventRepositoryPort repository;
    private final TenantContextHolder tenantContextHolder;
    public SecurityEventService(SecurityEventMapper mapper,
                                SecurityEventRepositoryPort repository,
                                TenantContextHolder tenantContextHolder) {
        this.mapper = mapper;
        this.repository = repository;
        this.tenantContextHolder = tenantContextHolder;
    }
    private String getTenantId() {
        return tenantContextHolder.getRequiredTenantId();
    }
    @Override
    public SecurityEventResponseDto create(CreateSecurityEventRequestDto dto) {
        String tenantId = getTenantId();
        SecurityEvent entity = mapper.toEntity(dto, tenantId);
        SecurityEvent saved = repository.save(entity);
        return mapper.toResponseDto(saved);
    }
    @Override
    public SecurityEventResponseDto findById(String id) {
        String tenantId = getTenantId();
        SecurityEvent entity = repository.findByTenantIdAndId(tenantId, id)
            .orElseThrow(() -> new SecurityEventNotFoundException(id));
        return mapper.toResponseDto(entity);
    }
    @Override
    public List<SecurityEventResponseDto> findAll() {
        String tenantId = getTenantId();
        return repository.findAllByTenantId(tenantId).stream()
            .map(mapper::toResponseDto)
            .toList();
    }
    @Override
    public List<SecurityEventResponseDto> findBySeverity(String severity) {
        String tenantId = getTenantId();
        return repository.findByTenantIdAndSeverity(tenantId, severity).stream()
            .map(mapper::toResponseDto)
            .toList();
    }
    @Override
    public List<SecurityEventResponseDto> findByEventType(String eventType) {
        String tenantId = getTenantId();
        return repository.findAllByTenantId(tenantId).stream()
            .filter(event -> event.getEventType() != null && event.getEventType().equalsIgnoreCase(eventType))
            .map(mapper::toResponseDto)
            .toList();
    }
    @Override
    public List<SecurityEventResponseDto> findRecent(String hours) {
        String tenantId = getTenantId();
        try {
            int hoursInt = Integer.parseInt(hours);
            LocalDateTime cutoff = LocalDateTime.now().minusHours(hoursInt);
            return repository.findByTenantIdAndTimestampAfter(tenantId, cutoff).stream()
                .map(mapper::toResponseDto)
                .toList();
        } catch (NumberFormatException e) {
            throw new SecurityEventValidationException("Invalid hours parameter: " + hours);
        }
    }
    @Override
    public void delete(String id) {
        String tenantId = getTenantId();
        if (!repository.findByTenantIdAndId(tenantId, id).isPresent()) {
            throw new SecurityEventNotFoundException(id);
        }
        repository.deleteByTenantIdAndId(tenantId, id);
    }
}

package com.gogidix.shared.infrastructure.services.security.threat.application.service;
import com.gogidix.shared.infrastructure.services.security.threat.application.dto.request.CreateThreatIndicatorRequestDto;
import com.gogidix.shared.infrastructure.services.security.threat.application.dto.request.UpdateThreatIndicatorRequestDto;
import com.gogidix.shared.infrastructure.services.security.threat.application.dto.response.ThreatIndicatorResponseDto;
import com.gogidix.shared.infrastructure.services.security.threat.application.mapper.ThreatIndicatorMapper;
import com.gogidix.shared.infrastructure.services.security.threat.domain.exception.ThreatIndicatorNotFoundException;
import com.gogidix.shared.infrastructure.services.security.threat.domain.exception.ThreatIndicatorValidationException;
import com.gogidix.shared.infrastructure.services.security.threat.domain.model.ThreatIndicator;
import com.gogidix.shared.infrastructure.services.security.threat.domain.port.in.ThreatIndicatorPort;
import com.gogidix.shared.infrastructure.services.security.threat.domain.port.out.ThreatIndicatorRepositoryPort;
import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * Application service for ThreatIndicator management.
 */
@Service
@Transactional
public class ThreatIndicatorService implements ThreatIndicatorPort {
    private final ThreatIndicatorMapper mapper;
    private final ThreatIndicatorRepositoryPort repository;
    private final TenantContextHolder tenantContextHolder;
    public ThreatIndicatorService(ThreatIndicatorMapper mapper,
                                  ThreatIndicatorRepositoryPort repository,
                                  TenantContextHolder tenantContextHolder) {
        this.mapper = mapper;
        this.repository = repository;
        this.tenantContextHolder = tenantContextHolder;
    }
    private String getTenantId() {
        return tenantContextHolder.getRequiredTenantId();
    }
    @Override
    public ThreatIndicatorResponseDto create(CreateThreatIndicatorRequestDto dto) {
        String tenantId = getTenantId();
        ThreatIndicator entity = mapper.toEntity(dto, tenantId);
        ThreatIndicator saved = repository.save(entity);
        return mapper.toResponseDto(saved);
    }
    @Override
    public ThreatIndicatorResponseDto findById(String id) {
        String tenantId = getTenantId();
        ThreatIndicator entity = repository.findByTenantIdAndId(tenantId, id)
            .orElseThrow(() -> new ThreatIndicatorNotFoundException(id));
        return mapper.toResponseDto(entity);
    }
    @Override
    public List<ThreatIndicatorResponseDto> findAll() {
        String tenantId = getTenantId();
        return repository.findAllByTenantId(tenantId).stream()
            .map(mapper::toResponseDto)
            .toList();
    }
    @Override
    public List<ThreatIndicatorResponseDto> findActive() {
        String tenantId = getTenantId();
        return repository.findByTenantIdAndActiveTrue(tenantId).stream()
            .map(mapper::toResponseDto)
            .toList();
    }
    @Override
    public List<ThreatIndicatorResponseDto> findByType(String indicatorType) {
        String tenantId = getTenantId();
        return repository.findByTenantIdAndIndicatorType(tenantId, indicatorType).stream()
            .map(mapper::toResponseDto)
            .toList();
    }
    @Override
    public List<ThreatIndicatorResponseDto> findBySeverity(String severity) {
        String tenantId = getTenantId();
        return repository.findAllByTenantId(tenantId).stream()
            .filter(indicator -> indicator.getSeverity() != null && indicator.getSeverity().name().equalsIgnoreCase(severity))
            .map(mapper::toResponseDto)
            .toList();
    }
    @Override
    public ThreatIndicatorResponseDto update(String id, UpdateThreatIndicatorRequestDto dto) {
        String tenantId = getTenantId();
        ThreatIndicator entity = repository.findByTenantIdAndId(tenantId, id)
            .orElseThrow(() -> new ThreatIndicatorNotFoundException(id));
        mapper.updateEntity(dto, entity);
        ThreatIndicator updated = repository.save(entity);
        return mapper.toResponseDto(updated);
    }
    @Override
    public void delete(String id) {
        String tenantId = getTenantId();
        if (!repository.findByTenantIdAndId(tenantId, id).isPresent()) {
            throw new ThreatIndicatorNotFoundException(id);
        }
        repository.deleteByTenantIdAndId(tenantId, id);
    }
    @Override
    public ThreatIndicatorResponseDto deactivate(String id) {
        String tenantId = getTenantId();
        ThreatIndicator entity = repository.findByTenantIdAndId(tenantId, id)
            .orElseThrow(() -> new ThreatIndicatorNotFoundException(id));
        entity.setActive(false);
        ThreatIndicator updated = repository.save(entity);
        return mapper.toResponseDto(updated);
    }
}

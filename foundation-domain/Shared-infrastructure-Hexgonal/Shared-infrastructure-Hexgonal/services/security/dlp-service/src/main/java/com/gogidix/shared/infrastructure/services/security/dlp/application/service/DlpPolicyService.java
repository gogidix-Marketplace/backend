package com.gogidix.shared.infrastructure.services.security.dlp.application.service;

import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import com.gogidix.shared.infrastructure.services.security.dlp.application.dto.request.CreateDlpPolicyRequestDto;
import com.gogidix.shared.infrastructure.services.security.dlp.application.dto.request.UpdateDlpPolicyRequestDto;
import com.gogidix.shared.infrastructure.services.security.dlp.application.dto.response.DlpPolicyResponseDto;
import com.gogidix.shared.infrastructure.services.security.dlp.application.mapper.DlpPolicyMapper;
import com.gogidix.shared.infrastructure.services.security.dlp.domain.exception.DlpPolicyNotFoundException;
import com.gogidix.shared.infrastructure.services.security.dlp.domain.exception.DlpPolicyValidationException;
import com.gogidix.shared.infrastructure.services.security.dlp.domain.model.DlpPolicy;
import com.gogidix.shared.infrastructure.services.security.dlp.domain.port.in.DlpPolicyPort;
import com.gogidix.shared.infrastructure.services.security.dlp.domain.port.out.DlpPolicyRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Application service for DlpPolicy management.
 */
@Service
@Transactional
public class DlpPolicyService implements DlpPolicyPort {

    private final DlpPolicyMapper mapper;
    private final DlpPolicyRepositoryPort repository;
    private final TenantContextHolder tenantContextHolder;

    public DlpPolicyService(DlpPolicyMapper mapper,
                           DlpPolicyRepositoryPort repository,
                           TenantContextHolder tenantContextHolder) {
        this.mapper = mapper;
        this.repository = repository;
        this.tenantContextHolder = tenantContextHolder;
    }

    private String getTenantId() {
        return tenantContextHolder.getRequiredTenantId();
    }

    @Override
    public DlpPolicyResponseDto create(CreateDlpPolicyRequestDto dto) {
        String tenantId = getTenantId();

        if (repository.existsByTenantIdAndPolicyName(tenantId, dto.policyName())) {
            throw new DlpPolicyValidationException("DLP policy already exists with name: " + dto.policyName());
        }

        DlpPolicy entity = mapper.toEntity(dto, tenantId);
        DlpPolicy saved = repository.save(entity);

        return mapper.toResponseDto(saved);
    }

    @Override
    public DlpPolicyResponseDto findById(String id) {
        String tenantId = getTenantId();

        DlpPolicy entity = repository.findByTenantIdAndId(tenantId, id)
            .orElseThrow(() -> new DlpPolicyNotFoundException(id));

        return mapper.toResponseDto(entity);
    }

    @Override
    public List<DlpPolicyResponseDto> findAll() {
        String tenantId = getTenantId();

        return repository.findAllByTenantId(tenantId).stream()
            .map(mapper::toResponseDto)
            .toList();
    }

    @Override
    public List<DlpPolicyResponseDto> findByStatus(String status) {
        String tenantId = getTenantId();

        try {
            DlpPolicy.PolicyStatus policyStatus = DlpPolicy.PolicyStatus.valueOf(status.toUpperCase());
            return repository.findByTenantIdAndStatus(tenantId, policyStatus).stream()
                .map(mapper::toResponseDto)
                .toList();
        } catch (IllegalArgumentException e) {
            throw new DlpPolicyValidationException("Invalid status: " + status);
        }
    }

    @Override
    public DlpPolicyResponseDto update(String id, UpdateDlpPolicyRequestDto dto) {
        String tenantId = getTenantId();

        DlpPolicy entity = repository.findByTenantIdAndId(tenantId, id)
            .orElseThrow(() -> new DlpPolicyNotFoundException(id));

        mapper.updateEntity(dto, entity);

        DlpPolicy updated = repository.save(entity);

        return mapper.toResponseDto(updated);
    }

    @Override
    public void delete(String id) {
        String tenantId = getTenantId();

        if (!repository.findByTenantIdAndId(tenantId, id).isPresent()) {
            throw new DlpPolicyNotFoundException(id);
        }

        repository.deleteByTenantIdAndId(tenantId, id);
    }

    @Override
    public DlpPolicyResponseDto activate(String id) {
        String tenantId = getTenantId();

        DlpPolicy entity = repository.findByTenantIdAndId(tenantId, id)
            .orElseThrow(() -> new DlpPolicyNotFoundException(id));

        entity.setStatus(DlpPolicy.PolicyStatus.ACTIVE);

        DlpPolicy updated = repository.save(entity);

        return mapper.toResponseDto(updated);
    }

    @Override
    public DlpPolicyResponseDto deactivate(String id) {
        String tenantId = getTenantId();

        DlpPolicy entity = repository.findByTenantIdAndId(tenantId, id)
            .orElseThrow(() -> new DlpPolicyNotFoundException(id));

        entity.setStatus(DlpPolicy.PolicyStatus.INACTIVE);

        DlpPolicy updated = repository.save(entity);

        return mapper.toResponseDto(updated);
    }
}

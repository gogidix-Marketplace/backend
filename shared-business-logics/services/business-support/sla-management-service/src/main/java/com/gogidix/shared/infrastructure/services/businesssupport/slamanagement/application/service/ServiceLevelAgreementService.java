package com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.service;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.request.CreateServiceLevelAgreementRequestDto;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.request.UpdateServiceLevelAgreementRequestDto;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.dto.response.ServiceLevelAgreementResponseDto;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.application.mapper.ServiceLevelAgreementMapper;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.exception.ServiceLevelAgreementNotFoundException;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.model.ServiceLevelAgreement;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.port.in.IServiceLevelAgreementUseCase;
import com.gogidix.shared.infrastructure.services.businesssupport.slamanagement.domain.port.out.IServiceLevelAgreementRepository;
import com.gogidix.shared.multitenancy.context.TenantContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Application service for ServiceLevelAgreement management
 */
@Service
@Transactional
public class ServiceLevelAgreementService implements IServiceLevelAgreementUseCase {
    private final ServiceLevelAgreementMapper mapper;
    private final IServiceLevelAgreementRepository repository;
    private final TenantContextHolder tenantContextHolder;
    public ServiceLevelAgreementService(ServiceLevelAgreementMapper mapper,
                                        IServiceLevelAgreementRepository repository,
                                        TenantContextHolder tenantContextHolder) {
        this.mapper = mapper;
        this.repository = repository;
        this.tenantContextHolder = tenantContextHolder;
    }
    @Override
    public ServiceLevelAgreementResponseDto create(CreateServiceLevelAgreementRequestDto dto) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        // Validate unique name within tenant
        if (repository.existsByNameAndTenantId(dto.name(), tenantId)) {
            throw new IllegalArgumentException("SLA with name '" + dto.name() + "' already exists for this tenant");
        }
        ServiceLevelAgreement entity = mapper.toEntity(dto, tenantId);
        // Business validation
        validateSla(entity);
        ServiceLevelAgreement saved = repository.save(entity);
        return mapper.toResponseDto(saved);
    }
    @Override
    public ServiceLevelAgreementResponseDto findById(String id) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        ServiceLevelAgreement entity = repository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new ServiceLevelAgreementNotFoundException(id));
        return mapper.toResponseDto(entity);
    }
    @Override
    public List<ServiceLevelAgreementResponseDto> findAll() {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findAllByTenantId(tenantId).stream()
            .map(mapper::toResponseDto)
            .collect(Collectors.toList());
    }
    @Override
    public List<ServiceLevelAgreementResponseDto> findByStatus(String status) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findByTenantIdAndStatus(tenantId, status).stream()
            .map(mapper::toResponseDto)
            .collect(Collectors.toList());
    }
    @Override
    public List<ServiceLevelAgreementResponseDto> findByServiceType(String serviceType) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findByTenantIdAndServiceType(tenantId, serviceType).stream()
            .map(mapper::toResponseDto)
            .collect(Collectors.toList());
    }
    @Override
    public List<ServiceLevelAgreementResponseDto> findActive() {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findActiveByTenantAndDate(tenantId, LocalDateTime.now()).stream()
            .map(mapper::toResponseDto)
            .collect(Collectors.toList());
    }
    @Override
    public ServiceLevelAgreementResponseDto update(String id, UpdateServiceLevelAgreementRequestDto dto) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        ServiceLevelAgreement entity = repository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new ServiceLevelAgreementNotFoundException(id));
        // Check if name is being changed and if new name already exists
        if (!entity.getName().equals(dto.name()) &&
            repository.existsByNameAndTenantId(dto.name(), tenantId)) {
            throw new IllegalArgumentException("SLA with name '" + dto.name() + "' already exists for this tenant");
        }
        mapper.updateEntity(entity, dto);
        // Business validation
        validateSla(entity);
        ServiceLevelAgreement updated = repository.save(entity);
        return mapper.toResponseDto(updated);
    }
    @Override
    public void delete(String id) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        ServiceLevelAgreement entity = repository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new ServiceLevelAgreementNotFoundException(id));
        repository.deleteByTenantIdAndId(tenantId, id);
    }
    @Override
    public void deactivate(String id) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        ServiceLevelAgreement entity = repository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new ServiceLevelAgreementNotFoundException(id));
        entity.setStatus("INACTIVE");
        repository.save(entity);
    }
    /**
     * Validate SLA business rules
     */
    private void validateSla(ServiceLevelAgreement entity) {
        if (entity.getUptimePercentage() < 0 || entity.getUptimePercentage() > 100) {
            throw new IllegalArgumentException("Uptime percentage must be between 0 and 100");
        }
        if (entity.getResponseTimeThreshold() < 0) {
            throw new IllegalArgumentException("Response time threshold must be positive");
        }
        if (entity.getPenaltyPercentage() < 0 || entity.getPenaltyPercentage() > 100) {
            throw new IllegalArgumentException("Penalty percentage must be between 0 and 100");
        }
        if (entity.getValidFrom() != null && entity.getValidUntil() != null &&
            entity.getValidFrom().isAfter(entity.getValidUntil())) {
            throw new IllegalArgumentException("Valid from date must be before valid until date");
        }
    }
}

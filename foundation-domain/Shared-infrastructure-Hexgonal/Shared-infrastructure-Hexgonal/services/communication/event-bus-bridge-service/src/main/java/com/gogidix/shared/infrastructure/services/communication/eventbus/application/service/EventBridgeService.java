package com.gogidix.shared.infrastructure.services.communication.eventbus.application.service;
import com.gogidix.shared.infrastructure.services.communication.eventbus.application.dto.request.CreateEventBridgeRequestDto;
import com.gogidix.shared.infrastructure.services.communication.eventbus.application.dto.response.EventBridgeResponseDto;
import com.gogidix.shared.infrastructure.services.communication.eventbus.application.mapper.EventBridgeMapper;
import com.gogidix.shared.infrastructure.services.communication.eventbus.domain.model.EventBridge;
import com.gogidix.shared.infrastructure.services.communication.eventbus.domain.port.in.IEventBridgeUseCase;
import com.gogidix.shared.infrastructure.services.communication.eventbus.domain.port.out.IEventBridgeRepository;
import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
 * Application service for EventBridge management
 */
@Service
@Transactional
public class EventBridgeService implements IEventBridgeUseCase {
    private final EventBridgeMapper mapper;
    private final IEventBridgeRepository repository;
    private final TenantContextHolder tenantContextHolder;
    public EventBridgeService(EventBridgeMapper mapper,
                              IEventBridgeRepository repository,
                              TenantContextHolder tenantContextHolder) {
        this.mapper = mapper;
        this.repository = repository;
        this.tenantContextHolder = tenantContextHolder;
    }
    @Override
    public EventBridgeResponseDto create(CreateEventBridgeRequestDto dto) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        // Check if bridge with same name exists
        if (repository.existsByNameAndTenantId(dto.name(), tenantId)) {
            throw new IllegalArgumentException("EventBridge with name '" + dto.name() + "' already exists");
        }
        EventBridge entity = mapper.toEntity(dto, tenantId);
        EventBridge saved = repository.save(entity);
        return mapper.toResponseDto(saved);
    }
    @Override
    public EventBridgeResponseDto findById(String id) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        EventBridge entity = repository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("EventBridge not found with id: " + id));
        return mapper.toResponseDto(entity);
    }
    @Override
    public List<EventBridgeResponseDto> findAll() {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findAllByTenantId(tenantId).stream()
            .map(mapper::toResponseDto)
            .toList();
    }
    @Override
    public List<EventBridgeResponseDto> findByStatus(String status) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findByStatusAndTenantId(status, tenantId).stream()
            .map(mapper::toResponseDto)
            .toList();
    }
    @Override
    public List<EventBridgeResponseDto> findBySourceType(String sourceType) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findBySourceTypeAndTenantId(sourceType, tenantId).stream()
            .map(mapper::toResponseDto)
            .toList();
    }
    @Override
    public List<EventBridgeResponseDto> findByTargetType(String targetType) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findByTargetTypeAndTenantId(targetType, tenantId).stream()
            .map(mapper::toResponseDto)
            .toList();
    }
    @Override
    public List<EventBridgeResponseDto> findEnabled() {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findEnabledByTenantId(tenantId).stream()
            .map(mapper::toResponseDto)
            .toList();
    }
    @Override
    public EventBridgeResponseDto update(String id, CreateEventBridgeRequestDto dto) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        EventBridge entity = repository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("EventBridge not found with id: " + id));
        mapper.updateEntity(entity, dto);
        EventBridge updated = repository.save(entity);
        return mapper.toResponseDto(updated);
    }
    @Override
    public void delete(String id) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        if (!repository.findByIdAndTenantId(id, tenantId).isPresent()) {
            throw new IllegalArgumentException("EventBridge not found with id: " + id);
        }
        repository.deleteByIdAndTenantId(id, tenantId);
    }
    @Override
    public IEventBridgeUseCase.EventBridgeStatsDto getStats() {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        long totalCount = repository.countByTenantId(tenantId);
        long enabledCount = repository.findEnabledByTenantId(tenantId).size();
        long activeCount = repository.findByStatusAndTenantId("ACTIVE", tenantId).size();
        return new IEventBridgeUseCase.EventBridgeStatsDto(totalCount, enabledCount, activeCount);
    }
}

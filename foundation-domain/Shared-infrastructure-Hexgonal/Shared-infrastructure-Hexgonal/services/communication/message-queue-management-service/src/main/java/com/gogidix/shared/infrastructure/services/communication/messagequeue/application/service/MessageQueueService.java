package com.gogidix.shared.infrastructure.services.communication.messagequeue.application.service;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.application.dto.request.CreateMessageQueueRequestDto;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.application.dto.response.MessageQueueResponseDto;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.domain.exception.MessageQueueNotFoundException;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.domain.model.MessageQueue;
import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.domain.port.in.IMessageQueueUseCase;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.domain.port.out.IMessageQueueRepository;
import com.gogidix.shared.infrastructure.core.tenancy.context.TenantContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional
public class MessageQueueService implements IMessageQueueUseCase {
    private final IMessageQueueRepository repository;
    private final TenantContextHolder tenantContextHolder;
    public MessageQueueService(IMessageQueueRepository repository, TenantContextHolder tenantContextHolder) {
        this.repository = repository;
        this.tenantContextHolder = tenantContextHolder;
    }
    @Override
    public MessageQueueResponseDto create(CreateMessageQueueRequestDto dto) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        if (repository.existsByNameAndTenantId(dto.name(), tenantId)) {
            throw new IllegalArgumentException("Queue with name '" + dto.name() + "' already exists");
        }
        MessageQueue entity = new MessageQueue(new TenantId(tenantId), dto.name(), dto.type());
        entity.setDescription(dto.description());
        entity.setRegion(dto.region());
        if (dto.maxSize() != null) entity.setMaxSize(dto.maxSize());
        if (dto.messageRetentionPeriod() != null) entity.setMessageRetentionPeriod(dto.messageRetentionPeriod());
        if (dto.maxReceiveCount() != null) entity.setMaxReceiveCount(dto.maxReceiveCount());
        if (dto.visibilityTimeout() != null) entity.setVisibilityTimeout(dto.visibilityTimeout());
        if (dto.deliveryDelay() != null) entity.setDeliveryDelay(dto.deliveryDelay());
        MessageQueue saved = repository.save(entity);
        return toResponseDto(saved);
    }
    @Override
    public MessageQueueResponseDto findById(String id) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        MessageQueue entity = repository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new MessageQueueNotFoundException(id));
        return toResponseDto(entity);
    }
    @Override
    public List<MessageQueueResponseDto> findAll() {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findAllByTenantId(tenantId).stream()
            .map(this::toResponseDto).collect(Collectors.toList());
    }
    @Override
    public List<MessageQueueResponseDto> findByStatus(String status) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findByTenantIdAndStatus(tenantId, status).stream()
            .map(this::toResponseDto).collect(Collectors.toList());
    }
    @Override
    public List<MessageQueueResponseDto> findByType(String type) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        return repository.findByTenantIdAndType(tenantId, type).stream()
            .map(this::toResponseDto).collect(Collectors.toList());
    }
    @Override
    public MessageQueueResponseDto update(String id, CreateMessageQueueRequestDto dto) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        MessageQueue entity = repository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new MessageQueueNotFoundException(id));
        entity.setDescription(dto.description());
        if (dto.maxSize() != null) entity.setMaxSize(dto.maxSize());
        if (dto.maxReceiveCount() != null) entity.setMaxReceiveCount(dto.maxReceiveCount());
        MessageQueue updated = repository.save(entity);
        return toResponseDto(updated);
    }
    @Override
    public void delete(String id) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        repository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new MessageQueueNotFoundException(id));
        repository.deleteByTenantIdAndId(tenantId, id);
    }
    @Override
    public void activate(String id) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        MessageQueue entity = repository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new MessageQueueNotFoundException(id));
        entity.setStatus("ACTIVE");
        repository.save(entity);
    }
    @Override
    public void deactivate(String id) {
        String tenantId = tenantContextHolder.getRequiredTenantId();
        MessageQueue entity = repository.findByIdAndTenantId(id, tenantId)
            .orElseThrow(() -> new MessageQueueNotFoundException(id));
        entity.setStatus("INACTIVE");
        repository.save(entity);
    }
    private MessageQueueResponseDto toResponseDto(MessageQueue entity) {
        return new MessageQueueResponseDto(
            entity.getId(), entity.getTenantId() != null ? entity.getTenantId().getValue() : null,
            entity.getName(), entity.getDescription(), entity.getType(), entity.getStatus(),
            entity.getRegion(), entity.getMaxSize(), entity.getMessageRetentionPeriod(),
            entity.getMaxReceiveCount(), entity.getVisibilityTimeout(),
            entity.getDeliveryDelay(), entity.getCreatedAt(), entity.getUpdatedAt()
        );
    }
}

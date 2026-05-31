package com.gogidix.shared.infrastructure.services.communication.messagequeue.infrastructure.persistence.impl;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.domain.model.MessageQueue;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.domain.port.out.IMessageQueueRepository;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.infrastructure.persistence.MessageQueueRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public class MessageQueueRepositoryImpl implements IMessageQueueRepository {
    private final MessageQueueRepository mongoRepository;
    public MessageQueueRepositoryImpl(MessageQueueRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }
    @Override
    public MessageQueue save(MessageQueue entity) {
        return mongoRepository.save(entity);
    }
    @Override
    public Optional<MessageQueue> findById(String id) {
        return mongoRepository.findById(id);
    }
    @Override
    public List<MessageQueue> findAllByTenantId(String tenantId) {
        return mongoRepository.findByTenantId_Value(tenantId);
    }
    @Override
    public Optional<MessageQueue> findByIdAndTenantId(String id, String tenantId) {
        return mongoRepository.findByTenantId_ValueAndId(tenantId, id);
    }
    @Override
    public Optional<MessageQueue> findByNameAndTenantId(String name, String tenantId) {
        return mongoRepository.findByTenantId_ValueAndName(tenantId, name);
    }
    @Override
    public List<MessageQueue> findByTenantIdAndStatus(String tenantId, String status) {
        return mongoRepository.findByTenantId_ValueAndStatus(tenantId, status);
    }
    @Override
    public List<MessageQueue> findByTenantIdAndType(String tenantId, String type) {
        return mongoRepository.findByTenantId_ValueAndType(tenantId, type);
    }
    @Override
    public long countByTenantId(String tenantId) {
        return mongoRepository.countByTenantId_Value(tenantId);
    }
    @Override
    public void deleteByTenantIdAndId(String tenantId, String id) {
        mongoRepository.deleteByTenantId_ValueAndId(tenantId, id);
    }
    @Override
    public boolean existsByNameAndTenantId(String name, String tenantId) {
        return mongoRepository.existsByTenantId_ValueAndName(tenantId, name);
    }
}

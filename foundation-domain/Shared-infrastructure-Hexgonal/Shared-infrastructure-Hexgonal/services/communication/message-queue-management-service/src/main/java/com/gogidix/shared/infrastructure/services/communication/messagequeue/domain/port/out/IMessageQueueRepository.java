package com.gogidix.shared.infrastructure.services.communication.messagequeue.domain.port.out;
import com.gogidix.shared.infrastructure.services.communication.messagequeue.domain.model.MessageQueue;
import java.util.List;
import java.util.Optional;
public interface IMessageQueueRepository {
    MessageQueue save(MessageQueue entity);
    Optional<MessageQueue> findById(String id);
    List<MessageQueue> findAllByTenantId(String tenantId);
    Optional<MessageQueue> findByIdAndTenantId(String id, String tenantId);
    Optional<MessageQueue> findByNameAndTenantId(String name, String tenantId);
    List<MessageQueue> findByTenantIdAndStatus(String tenantId, String status);
    List<MessageQueue> findByTenantIdAndType(String tenantId, String type);
    long countByTenantId(String tenantId);
    void deleteByTenantIdAndId(String tenantId, String id);
    boolean existsByNameAndTenantId(String name, String tenantId);
}

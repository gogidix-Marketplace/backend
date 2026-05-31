package com.gogidix.shared.infrastructure.services.communication.eventbus.infrastructure.persistence.mongo;
import com.gogidix.shared.infrastructure.services.communication.eventbus.domain.model.EventBridge;
import com.gogidix.shared.infrastructure.services.communication.eventbus.domain.port.out.IEventBridgeRepository;
import com.gogidix.shared.infrastructure.services.communication.eventbus.infrastructure.persistence.EventBridgeRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
/**
 * MongoDB implementation of EventBridge repository
 */
@Repository
public class EventBridgeRepositoryImpl implements IEventBridgeRepository {
    private final EventBridgeRepository mongoRepository;
    public EventBridgeRepositoryImpl(EventBridgeRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }
    @Override
    public EventBridge save(EventBridge entity) {
        return mongoRepository.save(entity);
    }
    @Override
    public Optional<EventBridge> findById(String id) {
        return mongoRepository.findById(id);
    }
    @Override
    public List<EventBridge> findAllByTenantId(String tenantId) {
        return mongoRepository.findByTenantId_Value(tenantId);
    }
    @Override
    public Optional<EventBridge> findByIdAndTenantId(String id, String tenantId) {
        return mongoRepository.findByTenantId_ValueAndId(tenantId, id);
    }
    @Override
    public Optional<EventBridge> findByNameAndTenantId(String name, String tenantId) {
        return mongoRepository.findByTenantId_ValueAndName(tenantId, name);
    }
    @Override
    public List<EventBridge> findByStatusAndTenantId(String status, String tenantId) {
        return mongoRepository.findByTenantId_ValueAndStatus(tenantId, status);
    }
    @Override
    public List<EventBridge> findBySourceTypeAndTenantId(String sourceType, String tenantId) {
        return mongoRepository.findByTenantId_ValueAndSourceType(tenantId, sourceType);
    }
    @Override
    public List<EventBridge> findByTargetTypeAndTenantId(String targetType, String tenantId) {
        return mongoRepository.findByTenantId_ValueAndTargetType(tenantId, targetType);
    }
    @Override
    public List<EventBridge> findEnabledByTenantId(String tenantId) {
        return mongoRepository.findByTenantId_ValueAndEnabledTrue(tenantId);
    }
    @Override
    public long countByTenantId(String tenantId) {
        return mongoRepository.countByTenantId_Value(tenantId);
    }
    @Override
    public void deleteByIdAndTenantId(String id, String tenantId) {
        mongoRepository.deleteByTenantId_ValueAndId(tenantId, id);
    }
    @Override
    public boolean existsByNameAndTenantId(String name, String tenantId) {
        return mongoRepository.existsByTenantId_ValueAndName(tenantId, name);
    }
}

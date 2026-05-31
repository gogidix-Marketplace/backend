package com.gogidix.shared.infrastructure.services.communication.statusbroadcast.domain.port.out;

import com.gogidix.shared.infrastructure.services.communication.statusbroadcast.domain.model.StatusBroadcast;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Output port for Status Broadcast repository
 */
public interface IStatusBroadcastRepository {
    StatusBroadcast save(StatusBroadcast entity);
    Optional<StatusBroadcast> findById(String id);
    List<StatusBroadcast> findAllByTenantId();
    Optional<StatusBroadcast> findByIdAndTenantId(String id);
    List<StatusBroadcast> findByStatusAndTenantId(String status);
    List<StatusBroadcast> findByTypeAndTenantId(String broadcastType);
    List<StatusBroadcast> findActiveBroadcasts(LocalDateTime now);
    void deleteByIdAndTenantId(String id);
    long countByTenantId();
}

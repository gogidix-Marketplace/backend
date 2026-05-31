package com.gogidix.dashboard.gateway.websocket.domain.repository;

import com.gogidix.dashboard.gateway.websocket.domain.model.ConnectionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for ConnectionInfo entities.
 */
@Repository
public interface ConnectionInfoRepository extends JpaRepository<ConnectionInfo, Long> {

    /**
     * Find connection by session ID
     */
    Optional<ConnectionInfo> findBySessionId(String sessionId);

    /**
     * Find all connections by tenant ID
     */
    List<ConnectionInfo> findByTenantIdAndStatus(String tenantId, ConnectionInfo.ConnectionStatus status);

    /**
     * Find stale connections (no heartbeat for specified minutes)
     */
    List<ConnectionInfo> findByStatusAndLastHeartbeatBefore(ConnectionInfo.ConnectionStatus status, LocalDateTime threshold);

    /**
     * Count active connections by tenant
     */
    long countByTenantIdAndStatus(String tenantId, ConnectionInfo.ConnectionStatus status);
}

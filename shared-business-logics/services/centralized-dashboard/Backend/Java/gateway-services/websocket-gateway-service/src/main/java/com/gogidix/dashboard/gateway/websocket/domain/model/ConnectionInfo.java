package com.gogidix.dashboard.gateway.websocket.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Types;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing a WebSocket connection.
 */
@Entity
@Table(name = "connection_info", indexes = {
    @Index(name = "idx_session_id", columnList = "session_id"),
    @Index(name = "idx_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_status", columnList = "status")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ConnectionInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false, unique = true)
    private String sessionId;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "connected_at", nullable = false)
    private LocalDateTime connectedAt;

    @Column(name = "disconnected_at")
    private LocalDateTime disconnectedAt;

    @Column(name = "last_heartbeat")
    private LocalDateTime lastHeartbeat;

    @Column(name = "status", nullable = false)
    @Builder.Default
    private ConnectionStatus status = ConnectionStatus.CONNECTED;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "connection_subscriptions", joinColumns = @JoinColumn(name = "connection_id"))
    @Column(name = "topic")
    @JdbcTypeCode(Types.VARCHAR)
    @Builder.Default
    private Set<String> subscriptions = new HashSet<>();

    @Column(name = "remote_address")
    private String remoteAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void addSubscription(String topic) {
        if (this.subscriptions == null) {
            this.subscriptions = new HashSet<>();
        }
        this.subscriptions.add(topic);
    }

    public void removeSubscription(String topic) {
        if (this.subscriptions != null) {
            this.subscriptions.remove(topic);
        }
    }

    public enum ConnectionStatus {
        CONNECTED, DISCONNECTED, ERROR
    }
}

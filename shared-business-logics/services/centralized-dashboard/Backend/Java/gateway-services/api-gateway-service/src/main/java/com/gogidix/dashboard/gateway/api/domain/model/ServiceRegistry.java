package com.gogidix.dashboard.gateway.api.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entity representing a registered downstream service.
 */
@Entity
@Table(name = "service_registry", indexes = {
    @Index(name = "idx_service_name", columnList = "service_name"),
    @Index(name = "idx_tenant_id", columnList = "tenant_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ServiceRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_name", nullable = false, unique = true)
    private String serviceName;

    @Column(name = "base_url", nullable = false)
    private String baseUrl;

    @Column(name = "health_check_url")
    private String healthCheckUrl;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "enabled")
    @Builder.Default
    private Boolean enabled = true;

    @Column(name = "timeout_ms")
    @Builder.Default
    private Integer timeoutMs = 5000;

    @Column(name = "retry_count")
    @Builder.Default
    private Integer retryCount = 3;

    @Column(name = "description")
    private String description;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

package com.gogidix.centralconfiguration.environmentservice.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Environment Variable entity.
 */
@Entity
@Table(name = "environment_variables", indexes = {
    @Index(name = "idx_env_var_env_id", columnList = "environment_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnvironmentVariable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "environment_id", nullable = false)
    private Long environmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "environment_id", insertable = false, updatable = false)
    private Environment environment;

    @Column(name = "variable_key", nullable = false, length = 255)
    private String variableKey;

    @Column(name = "variable_value", columnDefinition = "TEXT")
    private String variableValue;

    @Column(name = "is_encrypted", nullable = false)
    @Builder.Default
    private Boolean isEncrypted = false;

    @Column(name = "is_sensitive", nullable = false)
    @Builder.Default
    private Boolean isSensitive = false;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

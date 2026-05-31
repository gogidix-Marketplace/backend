package com.gogidix.foundation.devtools.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a deployment job configuration.
 */
@Entity
@Table(name = "deployment_jobs", indexes = {
    @Index(name = "idx_deploy_name", columnList = "name"),
    @Index(name = "idx_deploy_project", columnList = "projectId"),
    @Index(name = "idx_deploy_created", columnList = "createdAt")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DeploymentJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String projectId;

    @Column(nullable = false, length = 20)
    private String type;

    @Column(nullable = false)
    private String targetEnvironment;

    @Lob
    private String deploymentScript;

    @Lob
    private String preDeploymentScript;

    @Lob
    private String postDeploymentScript;

    @Lob
    private String rollbackScript;

    @Lob
    private String configuration;

    @Column
    private Integer timeout;

    @Column
    private Integer retryCount;

    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    @Column(length = 1000)
    private String tags;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(length = 100, updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(length = 100)
    private String updatedBy;

    @Version
    private Long version;
}

package com.gogidix.foundation.devtools.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a deployment execution.
 */
@Entity
@Table(name = "deployment_executions", indexes = {
    @Index(name = "idx_deploy_exec_job", columnList = "jobId"),
    @Index(name = "idx_deploy_exec_status", columnList = "status"),
    @Index(name = "idx_deploy_exec_executed", columnList = "executedAt")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DeploymentExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(nullable = false)
    private Long jobId;

    @Column(nullable = false, length = 20)
    private String status;

    @Column
    private String deploymentVersion;

    @Column
    private String commitSha;

    @Lob
    private String outputLog;

    @Lob
    private String errorLog;

    @Column
    private Long startTime;

    @Column
    private Long endTime;

    @Column
    private Long duration;

    @Column(length = 100)
    private String executedBy;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime executedAt;

    @Lob
    private String metadata;

    @Version
    private Long entityVersion;
}

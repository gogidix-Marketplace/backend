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
 * Entity representing an execution of an API test case.
 */
@Entity
@Table(name = "api_test_executions", indexes = {
    @Index(name = "idx_api_exec_test_case", columnList = "testCaseId"),
    @Index(name = "idx_api_exec_status", columnList = "status"),
    @Index(name = "idx_api_exec_executed", columnList = "executedAt")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ApiTestExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(nullable = false)
    private Long testCaseId;

    @Column(nullable = false, length = 20)
    private String status;

    @Column
    private Integer actualStatusCode;

    @Lob
    private String responseBody;

    @Lob
    private String errorMessage;

    @Column
    private Long responseTime;

    @Column
    private Long executionTime;

    @Column(length = 100)
    private String executedBy;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime executedAt;

    @Column
    private String environment;

    @Lob
    private String metadata;

    @Version
    private Long version;
}

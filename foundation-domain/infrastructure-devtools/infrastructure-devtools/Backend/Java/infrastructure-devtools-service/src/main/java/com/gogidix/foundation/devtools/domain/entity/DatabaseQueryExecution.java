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
 * Entity representing a database query execution.
 */
@Entity
@Table(name = "database_query_executions", indexes = {
    @Index(name = "idx_db_exec_query", columnList = "queryId"),
    @Index(name = "idx_db_exec_status", columnList = "status"),
    @Index(name = "idx_db_exec_executed", columnList = "executedAt")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DatabaseQueryExecution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(nullable = false)
    private Long queryId;

    @Column(nullable = false, length = 20)
    private String status;

    @Column
    private Integer rowsAffected;

    @Column
    private Integer rowsReturned;

    @Lob
    private String resultData;

    @Lob
    private String errorMessage;

    @Column
    private Long executionTime;

    @Column(length = 100)
    private String executedBy;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime executedAt;

    @Lob
    private String metadata;

    @Version
    private Long version;
}

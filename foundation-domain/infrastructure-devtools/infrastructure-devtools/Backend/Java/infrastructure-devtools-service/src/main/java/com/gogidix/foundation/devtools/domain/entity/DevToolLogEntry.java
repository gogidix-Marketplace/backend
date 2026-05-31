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
 * Entity representing a developer tool log entry for debugging.
 */
@Entity
@Table(name = "dev_tool_logs", indexes = {
    @Index(name = "idx_dev_log_level", columnList = "level"),
    @Index(name = "idx_dev_log_source", columnList = "source"),
    @Index(name = "idx_dev_log_created", columnList = "createdAt")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DevToolLogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(nullable = false, length = 20)
    private String level;

    @Column(nullable = false, length = 100)
    private String source;

    @Column(length = 100)
    private String category;

    @Lob
    private String message;

    @Lob
    private String stackTrace;

    @Lob
    private String context;

    @Column(length = 100)
    private String userId;

    @Column(length = 100)
    private String sessionId;

    @Column(length = 100)
    private String requestId;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Version
    private Long version;
}

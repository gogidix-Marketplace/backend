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
 * Entity representing a documentation generation execution.
 */
@Entity
@Table(name = "documentation_generations", indexes = {
    @Index(name = "idx_doc_gen_project", columnList = "projectId"),
    @Index(name = "idx_doc_gen_status", columnList = "status"),
    @Index(name = "idx_doc_gen_created", columnList = "createdAt")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DocumentationGeneration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID uuid;

    @Column(nullable = false)
    private Long projectId;

    @Column(nullable = false, length = 20)
    private String status;

    @Lob
    private String outputPath;

    @Lob
    private String outputContent;

    @Column
    private Integer pageCount;

    @Lob
    private String errorMessage;

    @Column
    private Long generationTime;

    @Column(length = 100)
    private String generatedBy;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Lob
    private String metadata;

    @Version
    private Long version;
}

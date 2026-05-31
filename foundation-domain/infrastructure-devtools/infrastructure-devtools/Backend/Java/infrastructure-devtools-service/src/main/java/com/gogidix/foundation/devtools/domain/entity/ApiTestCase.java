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
import java.util.Map;
import java.util.UUID;

/**
 * Entity representing an API test case for testing REST endpoints.
 */
@Entity
@Table(name = "api_test_cases", indexes = {
    @Index(name = "idx_api_test_project", columnList = "projectId"),
    @Index(name = "idx_api_test_name", columnList = "name"),
    @Index(name = "idx_api_test_created", columnList = "createdAt")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ApiTestCase {

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

    @Column(nullable = false, length = 10)
    private String method;

    @Column(nullable = false, length = 2000)
    private String url;

    @Lob
    private String headers;

    @Lob
    private String requestBody;

    @Column(nullable = false)
    private Integer expectedStatusCode;

    @Lob
    private String expectedResponseBody;

    @Lob
    private String validationScript;

    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    @Column(length = 20)
    private String environment;

    @Column(length = 1000)
    private String tags;

    @Column(nullable = false)
    @Builder.Default
    private Integer timeout = 30000;

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

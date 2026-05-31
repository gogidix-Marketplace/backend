package com.gogidix.transaction.audit.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.transaction.audit.application.dto.request.CreateAuditLogRequestDto;
import com.gogidix.transaction.audit.application.dto.response.AuditLogResponseDto;
import com.gogidix.transaction.audit.application.service.AuditLogCommandService;
import com.gogidix.transaction.audit.application.service.AuditLogQueryService;
import com.gogidix.transaction.audit.domain.model.AuditLog;
import com.gogidix.transaction.audit.domain.repository.AuditLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Disabled("Requires Docker/Testcontainers")
@DisplayName("Audit Trail Service Integration Tests")
class AuditTrailServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine"))
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("kafka.enabled", () -> "false");
    }

    @Autowired
    private AuditLogCommandService commandService;

    @Autowired
    private AuditLogQueryService queryService;

    @Autowired
    private AuditLogRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateAuditLogRequestDto createRequest;

    @BeforeEach
    void setUp() {
        // TODO: Clean up database before each test once AuditLogRepository supports findAll()
        // repository.findAll().forEach(auditLog -> repository.delete(auditLog));

        createRequest = CreateAuditLogRequestDto.builder()
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-12345")
                .action("CREATE")
                .actorId("user-001")
                .actorType("USER")
                .severity("INFO")
                .status("SUCCESS")
                .description("Transaction created")
                .correlationId("corr-001")
                .build();
    }

    @Test
    @DisplayName("Should create and retrieve audit log")
    void shouldCreateAndRetrieveAuditLog() {
        // When
        AuditLogResponseDto created = commandService.createAuditLog(createRequest);

        // Then
        assertNotNull(created.getId());
        assertEquals("tenant-001", created.getTenantId());
        assertEquals("Transaction", created.getEntityType());
        assertEquals("txn-12345", created.getEntityId());
        assertEquals("CREATE", created.getAction());

        // When - retrieve
        AuditLog retrieved = repository.findById(created.getId()).orElse(null);

        // Then
        assertNotNull(retrieved);
        assertEquals(created.getId(), retrieved.getId());
        assertEquals("tenant-001", retrieved.getTenantId());
        assertEquals("Transaction", retrieved.getEntityType());
    }

    @Test
    @DisplayName("Should store all audit log fields")
    void shouldStoreAllAuditLogFields() {
        // Given
        CreateAuditLogRequestDto fullRequest = CreateAuditLogRequestDto.builder()
                .tenantId("tenant-001")
                .entityType("Payment")
                .entityId("pay-001")
                .action("PROCESS")
                .actorId("system")
                .actorType("SERVICE")
                .ipAddress("10.0.0.1")
                .userAgent("PaymentService/1.0")
                .correlationId("corr-001")
                .oldState("{\"status\":\"PENDING\"}")
                .newState("{\"status\":\"COMPLETED\"}")
                .changedFields("[\"status\"]")
                .businessContext("{\"source\":\"API\"}")
                .severity("INFO")
                .category("BUSINESS")
                .description("Payment processed successfully")
                .status("SUCCESS")
                .sessionId("sess-001")
                .requestId("req-001")
                .build();

        // When
        AuditLogResponseDto created = commandService.createAuditLog(fullRequest);
        AuditLog retrieved = repository.findById(created.getId()).orElseThrow();

        // Then
        assertNotNull(retrieved);
        assertEquals("Payment", retrieved.getEntityType());
        assertEquals("pay-001", retrieved.getEntityId());
        assertEquals("PROCESS", retrieved.getAction());
        assertEquals("system", retrieved.getActorId());
        assertEquals("SERVICE", retrieved.getActorType());
        assertEquals("10.0.0.1", retrieved.getIpAddress());
        assertEquals("PaymentService/1.0", retrieved.getUserAgent());
        assertEquals("corr-001", retrieved.getCorrelationId());
        assertEquals("{\"status\":\"PENDING\"}", retrieved.getOldState());
        assertEquals("{\"status\":\"COMPLETED\"}", retrieved.getNewState());
        assertEquals("[\"status\"]", retrieved.getChangedFields());
        assertEquals("{\"source\":\"API\"}", retrieved.getBusinessContext());
        assertEquals("INFO", retrieved.getSeverity());
        assertEquals("BUSINESS", retrieved.getCategory());
        assertEquals("Payment processed successfully", retrieved.getDescription());
        assertEquals("SUCCESS", retrieved.getStatus());
        assertEquals("sess-001", retrieved.getSessionId());
        assertEquals("req-001", retrieved.getRequestId());
    }

    @Test
    @DisplayName("Should find audit logs by entity")
    void shouldFindAuditLogsByEntity() {
        // Given - create multiple audit logs for same entity
        commandService.createAuditLog(createRequest);

        CreateAuditLogRequestDto updateRequest = CreateAuditLogRequestDto.builder()
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-12345")
                .action("UPDATE")
                .actorId("user-001")
                .actorType("USER")
                .severity("INFO")
                .status("SUCCESS")
                .description("Transaction updated")
                .build();

        commandService.createAuditLog(updateRequest);

        // When
        List<AuditLogResponseDto> results = queryService.getAuditLogsByEntity("Transaction", "txn-12345");

        // Then
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(a -> "CREATE".equals(a.getAction())));
        assertTrue(results.stream().anyMatch(a -> "UPDATE".equals(a.getAction())));
    }

    @Test
    @DisplayName("Should find audit logs by tenant and entity")
    void shouldFindAuditLogsByTenantAndEntity() {
        // Given
        commandService.createAuditLog(createRequest);

        // When
        List<AuditLogResponseDto> results = queryService.getAuditLogsByTenantAndEntity(
                "tenant-001", "Transaction", "txn-12345");

        // Then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("tenant-001", results.get(0).getTenantId());
        assertEquals("Transaction", results.get(0).getEntityType());
        assertEquals("txn-12345", results.get(0).getEntityId());
    }

    @Test
    @DisplayName("Should find audit logs by correlation ID")
    void shouldFindAuditLogsByCorrelationId() {
        // Given - create multiple audit logs with same correlation ID
        commandService.createAuditLog(createRequest);

        CreateAuditLogRequestDto secondRequest = CreateAuditLogRequestDto.builder()
                .tenantId("tenant-001")
                .entityType("Payment")
                .entityId("pay-001")
                .action("CREATE")
                .actorId("system")
                .actorType("SERVICE")
                .correlationId("corr-001") // Same correlation ID
                .build();

        commandService.createAuditLog(secondRequest);

        // When
        List<AuditLogResponseDto> results = queryService.getAuditLogsByCorrelationId("corr-001");

        // Then
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(a -> "corr-001".equals(a.getCorrelationId())));
    }

    @Test
    @DisplayName("Should find audit logs by actor")
    void shouldFindAuditLogsByActor() {
        // Given
        commandService.createAuditLog(createRequest);

        CreateAuditLogRequestDto actorRequest = CreateAuditLogRequestDto.builder()
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-67890")
                .action("CREATE")
                .actorId("user-001") // Same actor
                .actorType("USER")
                .build();

        commandService.createAuditLog(actorRequest);

        // When
        List<AuditLogResponseDto> results = queryService.getAuditLogsByActor("user-001");

        // Then
        assertNotNull(results);
        assertTrue(results.size() >= 2);
        assertTrue(results.stream().allMatch(a -> "user-001".equals(a.getActorId())));
    }

    @Test
    @DisplayName("Should count audit logs by tenant")
    void shouldCountAuditLogsByTenant() {
        // Given
        commandService.createAuditLog(createRequest);

        CreateAuditLogRequestDto secondRequest = CreateAuditLogRequestDto.builder()
                .tenantId("tenant-001")
                .entityType("Payment")
                .entityId("pay-001")
                .action("CREATE")
                .build();

        commandService.createAuditLog(secondRequest);

        // When
        Long count = queryService.countByTenant("tenant-001");

        // Then
        assertNotNull(count);
        assertEquals(2L, count);
    }

    @Test
    @DisplayName("Should delete audit log")
    void shouldDeleteAuditLog() {
        // Given
        AuditLogResponseDto created = commandService.createAuditLog(createRequest);
        UUID auditLogId = created.getId();

        // When
        commandService.deleteAuditLog(auditLogId);

        // Then
        assertFalse(repository.existsById(auditLogId));
    }

    @Test
    @DisplayName("Should set default values")
    void shouldSetDefaultValues() {
        // Given - request without severity, status, timestamp
        CreateAuditLogRequestDto minimalRequest = CreateAuditLogRequestDto.builder()
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-12345")
                .action("CREATE")
                .build();

        // When
        AuditLogResponseDto created = commandService.createAuditLog(minimalRequest);
        AuditLog retrieved = repository.findById(created.getId()).orElseThrow();

        // Then
        assertEquals("INFO", retrieved.getSeverity());
        assertEquals("SUCCESS", retrieved.getStatus());
        assertNotNull(retrieved.getTimestamp());
        assertNotNull(retrieved.getCreatedAt());
        assertNotNull(retrieved.getUpdatedAt());
    }

    @Test
    @DisplayName("Should handle audit log with error status")
    void shouldHandleAuditLogWithErrorStatus() {
        // Given
        CreateAuditLogRequestDto errorRequest = CreateAuditLogRequestDto.builder()
                .tenantId("tenant-001")
                .entityType("Payment")
                .entityId("pay-001")
                .action("PROCESS")
                .severity("ERROR")
                .status("FAILURE")
                .errorMessage("Payment gateway timeout")
                .description("Payment processing failed")
                .build();

        // When
        AuditLogResponseDto created = commandService.createAuditLog(errorRequest);
        AuditLog retrieved = repository.findById(created.getId()).orElseThrow();

        // Then
        assertEquals("ERROR", retrieved.getSeverity());
        assertEquals("FAILURE", retrieved.getStatus());
        assertEquals("Payment gateway timeout", retrieved.getErrorMessage());
        assertTrue(retrieved.isCritical());
        assertFalse(retrieved.isSuccess());
    }

    @Test
    @DisplayName("Should identify system vs user actors")
    void shouldIdentifySystemVsUserActors() {
        // Given - system actor
        CreateAuditLogRequestDto systemRequest = CreateAuditLogRequestDto.builder()
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-12345")
                .action("CREATE")
                .actorId("scheduler-service")
                .actorType("SYSTEM")
                .build();

        // When
        AuditLogResponseDto systemCreated = commandService.createAuditLog(systemRequest);
        AuditLog systemRetrieved = repository.findById(systemCreated.getId()).orElseThrow();

        // Then
        assertTrue(systemRetrieved.isSystemActor());
        assertFalse(systemRetrieved.isUserActor());

        // Given - user actor
        CreateAuditLogRequestDto userRequest = CreateAuditLogRequestDto.builder()
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-67890")
                .action("CREATE")
                .actorId("user-001")
                .actorType("USER")
                .build();

        // When
        AuditLogResponseDto userCreated = commandService.createAuditLog(userRequest);
        AuditLog userRetrieved = repository.findById(userCreated.getId()).orElseThrow();

        // Then
        assertFalse(userRetrieved.isSystemActor());
        assertTrue(userRetrieved.isUserActor());
    }

    @Test
    @DisplayName("Should generate audit log summary")
    void shouldGenerateAuditLogSummary() {
        // Given
        CreateAuditLogRequestDto request = CreateAuditLogRequestDto.builder()
                .tenantId("tenant-001")
                .entityType("Payment")
                .entityId("pay-001")
                .action("PROCESS")
                .actorId("user-001")
                .actorType("USER")
                .severity("INFO")
                .description("Payment processed")
                .build();

        // When
        AuditLogResponseDto created = commandService.createAuditLog(request);
        AuditLog retrieved = repository.findById(created.getId()).orElseThrow();
        String summary = retrieved.getSummary();

        // Then
        assertNotNull(summary);
        assertTrue(summary.contains("[INFO]"));
        assertTrue(summary.contains("Payment"));
        assertTrue(summary.contains("PROCESS"));
        assertTrue(summary.contains("pay-001"));
    }
}

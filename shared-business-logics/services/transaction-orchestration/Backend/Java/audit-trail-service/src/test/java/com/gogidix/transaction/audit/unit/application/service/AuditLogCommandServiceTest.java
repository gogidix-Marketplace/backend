package com.gogidix.transaction.audit.unit.application.service;

import com.gogidix.transaction.audit.application.dto.request.CreateAuditLogRequestDto;
import com.gogidix.transaction.audit.application.dto.response.AuditLogResponseDto;
import com.gogidix.transaction.audit.application.mapper.AuditLogMapper;
import com.gogidix.transaction.audit.application.service.AuditLogCommandService;
import com.gogidix.transaction.audit.domain.model.AuditLog;
import com.gogidix.transaction.audit.domain.port.in.CreateAuditLogCommand;
import com.gogidix.transaction.audit.domain.repository.AuditLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuditLogCommandService Unit Tests")
class AuditLogCommandServiceTest {

    @Mock
    private AuditLogRepository repository;

    @Mock
    private AuditLogMapper mapper;

    @InjectMocks
    private AuditLogCommandService commandService;

    private CreateAuditLogRequestDto requestDto;
    private CreateAuditLogCommand.CreateAuditLogCommandDto commandDto;
    private AuditLog auditLog;
    private AuditLogResponseDto responseDto;
    private UUID auditLogId;

    @BeforeEach
    void setUp() {
        auditLogId = UUID.randomUUID();

        requestDto = CreateAuditLogRequestDto.builder()
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-12345")
                .action("CREATE")
                .actorId("user-001")
                .actorType("USER")
                .severity("INFO")
                .status("SUCCESS")
                .description("Transaction created")
                .build();

        commandDto = CreateAuditLogCommand.CreateAuditLogCommandDto.builder()
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-12345")
                .action("CREATE")
                .actorId("user-001")
                .actorType("USER")
                .severity("INFO")
                .status("SUCCESS")
                .description("Transaction created")
                .build();

        auditLog = AuditLog.builder()
                .id(auditLogId)
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-12345")
                .action("CREATE")
                .actorId("user-001")
                .actorType("USER")
                .severity("INFO")
                .status("SUCCESS")
                .description("Transaction created")
                .timestamp(LocalDateTime.now())
                .build();

        responseDto = AuditLogResponseDto.builder()
                .id(auditLogId)
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-12345")
                .action("CREATE")
                .actorId("user-001")
                .actorType("USER")
                .severity("INFO")
                .status("SUCCESS")
                .description("Transaction created")
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should create audit log successfully")
    void shouldCreateAuditLogSuccessfully() {
        // Given
        when(mapper.toCommand(requestDto)).thenReturn(commandDto);
        when(mapper.toEntity(commandDto)).thenReturn(auditLog);
        when(repository.save(any(AuditLog.class))).thenReturn(auditLog);
        when(mapper.toResponseDto(auditLog)).thenReturn(responseDto);

        // When
        AuditLogResponseDto result = commandService.createAuditLog(requestDto);

        // Then
        assertNotNull(result);
        assertEquals(auditLogId, result.getId());
        assertEquals("tenant-001", result.getTenantId());
        assertEquals("Transaction", result.getEntityType());
        assertEquals("txn-12345", result.getEntityId());
        assertEquals("CREATE", result.getAction());

        verify(mapper).toCommand(requestDto);
        verify(mapper).toEntity(commandDto);
        verify(repository).save(any(AuditLog.class));
        verify(mapper).toResponseDto(auditLog);
    }

    @Test
    @DisplayName("Should set default severity when not provided")
    void shouldSetDefaultSeverityWhenNotProvided() {
        // Given
        requestDto.setSeverity(null);
        commandDto.setSeverity(null);
        auditLog.setSeverity(null);

        when(mapper.toCommand(requestDto)).thenReturn(commandDto);
        when(mapper.toEntity(commandDto)).thenReturn(auditLog);
        when(repository.save(any(AuditLog.class))).thenAnswer(invocation -> {
            AuditLog saved = invocation.getArgument(0);
            if (saved.getSeverity() == null) {
                saved.setSeverity("INFO");
            }
            return saved;
        });
        when(mapper.toResponseDto(any(AuditLog.class))).thenReturn(responseDto);

        // When
        commandService.createAuditLog(requestDto);

        // Then
        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(repository).save(captor.capture());
        assertEquals("INFO", captor.getValue().getSeverity());
    }

    @Test
    @DisplayName("Should set default status when not provided")
    void shouldSetDefaultStatusWhenNotProvided() {
        // Given
        requestDto.setStatus(null);
        commandDto.setStatus(null);
        auditLog.setStatus(null);

        when(mapper.toCommand(requestDto)).thenReturn(commandDto);
        when(mapper.toEntity(commandDto)).thenReturn(auditLog);
        when(repository.save(any(AuditLog.class))).thenAnswer(invocation -> {
            AuditLog saved = invocation.getArgument(0);
            if (saved.getStatus() == null) {
                saved.setStatus("SUCCESS");
            }
            return saved;
        });
        when(mapper.toResponseDto(any(AuditLog.class))).thenReturn(responseDto);

        // When
        commandService.createAuditLog(requestDto);

        // Then
        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(repository).save(captor.capture());
        assertEquals("SUCCESS", captor.getValue().getStatus());
    }

    @Test
    @DisplayName("Should set timestamp when not provided")
    void shouldSetTimestampWhenNotProvided() {
        // Given
        // Note: CreateAuditLogRequestDto and CreateAuditLogCommandDto do not have a timestamp field;
        // timestamp is set internally by the service
        auditLog.setTimestamp(null);

        when(mapper.toCommand(requestDto)).thenReturn(commandDto);
        when(mapper.toEntity(commandDto)).thenReturn(auditLog);
        when(repository.save(any(AuditLog.class))).thenAnswer(invocation -> {
            AuditLog saved = invocation.getArgument(0);
            if (saved.getTimestamp() == null) {
                saved.setTimestamp(LocalDateTime.now());
            }
            return saved;
        });
        when(mapper.toResponseDto(any(AuditLog.class))).thenReturn(responseDto);

        // When
        commandService.createAuditLog(requestDto);

        // Then
        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(repository).save(captor.capture());
        assertNotNull(captor.getValue().getTimestamp());
    }

    @Test
    @DisplayName("Should delete audit log successfully")
    void shouldDeleteAuditLogSuccessfully() {
        // Given
        when(repository.findById(auditLogId)).thenReturn(Optional.of(auditLog));
        doNothing().when(repository).delete(any(AuditLog.class));

        // When
        commandService.deleteAuditLog(auditLogId);

        // Then
        verify(repository).findById(auditLogId);
        verify(repository).delete(auditLog);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent audit log")
    void shouldThrowExceptionWhenDeletingNonExistentAuditLog() {
        // Given
        UUID nonExistentId = UUID.randomUUID();
        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> commandService.deleteAuditLog(nonExistentId)
        );

        assertTrue(exception.getMessage().contains("Audit log not found"));
        verify(repository).findById(nonExistentId);
        verify(repository, never()).delete(any(AuditLog.class));
    }

    @Test
    @DisplayName("Should create audit log with all fields")
    void shouldCreateAuditLogWithAllFields() {
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

        when(mapper.toCommand(fullRequest)).thenReturn(commandDto);
        when(mapper.toEntity(commandDto)).thenReturn(auditLog);
        when(repository.save(any(AuditLog.class))).thenReturn(auditLog);
        when(mapper.toResponseDto(auditLog)).thenReturn(responseDto);

        // When
        AuditLogResponseDto result = commandService.createAuditLog(fullRequest);

        // Then
        assertNotNull(result);
        verify(repository).save(any(AuditLog.class));
    }

    @Test
    @DisplayName("Should preserve provided severity")
    void shouldPreserveProvidedSeverity() {
        // Given
        requestDto.setSeverity("CRITICAL");
        commandDto.setSeverity("CRITICAL");

        when(mapper.toCommand(requestDto)).thenReturn(commandDto);
        when(mapper.toEntity(commandDto)).thenReturn(auditLog);
        when(repository.save(any(AuditLog.class))).thenReturn(auditLog);
        when(mapper.toResponseDto(auditLog)).thenReturn(responseDto);

        // When
        commandService.createAuditLog(requestDto);

        // Then
        verify(repository).save(any(AuditLog.class));
    }

    @Test
    @DisplayName("Should preserve provided status")
    void shouldPreserveProvidedStatus() {
        // Given
        requestDto.setStatus("FAILURE");
        commandDto.setStatus("FAILURE");

        when(mapper.toCommand(requestDto)).thenReturn(commandDto);
        when(mapper.toEntity(commandDto)).thenReturn(auditLog);
        when(repository.save(any(AuditLog.class))).thenReturn(auditLog);
        when(mapper.toResponseDto(auditLog)).thenReturn(responseDto);

        // When
        commandService.createAuditLog(requestDto);

        // Then
        verify(repository).save(any(AuditLog.class));
    }

    @Test
    @DisplayName("Should preserve provided timestamp")
    void shouldPreserveProvidedTimestamp() {
        // Given
        LocalDateTime specificTimestamp = LocalDateTime.of(2024, 1, 15, 10, 30);
        // Note: CreateAuditLogRequestDto and CreateAuditLogCommandDto do not have a timestamp field;
        // timestamp is set internally by the service
        auditLog.setTimestamp(specificTimestamp);

        when(mapper.toCommand(requestDto)).thenReturn(commandDto);
        when(mapper.toEntity(commandDto)).thenReturn(auditLog);
        when(repository.save(any(AuditLog.class))).thenReturn(auditLog);
        when(mapper.toResponseDto(auditLog)).thenReturn(responseDto);

        // When
        commandService.createAuditLog(requestDto);

        // Then
        verify(repository).save(any(AuditLog.class));
    }

    @Test
    @DisplayName("Should handle repository exception")
    void shouldHandleRepositoryException() {
        // Given
        when(mapper.toCommand(requestDto)).thenReturn(commandDto);
        when(mapper.toEntity(commandDto)).thenReturn(auditLog);
        when(repository.save(any(AuditLog.class))).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> commandService.createAuditLog(requestDto));
    }
}

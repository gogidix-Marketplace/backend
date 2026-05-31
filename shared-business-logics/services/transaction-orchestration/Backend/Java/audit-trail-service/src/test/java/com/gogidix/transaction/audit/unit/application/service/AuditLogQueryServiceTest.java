package com.gogidix.transaction.audit.unit.application.service;

import com.gogidix.transaction.audit.application.dto.response.AuditLogResponseDto;
import com.gogidix.transaction.audit.application.dto.response.PagedAuditLogsResponseDto;
import com.gogidix.transaction.audit.application.mapper.AuditLogMapper;
import com.gogidix.transaction.audit.application.service.AuditLogQueryService;
import com.gogidix.transaction.audit.domain.model.AuditLog;
import com.gogidix.transaction.audit.domain.port.in.GetAuditLogQuery;
import com.gogidix.transaction.audit.domain.port.in.SearchAuditLogsQuery;
import com.gogidix.transaction.audit.domain.port.out.AuditLogRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuditLogQueryService Unit Tests")
class AuditLogQueryServiceTest {

    @Mock
    private AuditLogRepositoryPort repository;

    @Mock
    private AuditLogMapper mapper;

    @InjectMocks
    private AuditLogQueryService queryService;

    private AuditLog auditLog1;
    private AuditLog auditLog2;
    private AuditLogResponseDto responseDto1;
    private AuditLogResponseDto responseDto2;
    private UUID auditLogId;

    @BeforeEach
    void setUp() {
        auditLogId = UUID.randomUUID();

        auditLog1 = AuditLog.builder()
                .id(auditLogId)
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-12345")
                .action("CREATE")
                .actorId("user-001")
                .actorType("USER")
                .severity("INFO")
                .status("SUCCESS")
                .timestamp(LocalDateTime.now())
                .description("Transaction created")
                .build();

        auditLog2 = AuditLog.builder()
                .id(UUID.randomUUID())
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-12345")
                .action("UPDATE")
                .actorId("user-001")
                .actorType("USER")
                .severity("INFO")
                .status("SUCCESS")
                .timestamp(LocalDateTime.now().plusMinutes(5))
                .description("Transaction updated")
                .build();

        responseDto1 = AuditLogResponseDto.builder()
                .id(auditLogId)
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-12345")
                .action("CREATE")
                .build();

        responseDto2 = AuditLogResponseDto.builder()
                .id(auditLog2.getId())
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-12345")
                .action("UPDATE")
                .build();
    }

    @Test
    @DisplayName("Should get audit log by ID")
    void shouldGetAuditLogById() {
        // Given
        GetAuditLogQuery.GetAuditLogQueryDto query = GetAuditLogQuery.GetAuditLogQueryDto.builder()
                .auditLogId(auditLogId.toString())
                .build();

        when(repository.findById(auditLogId.toString())).thenReturn(Optional.of(auditLog1));
        when(mapper.toResponseDto(auditLog1)).thenReturn(responseDto1);

        // When
        AuditLogResponseDto result = queryService.getAuditLog(query);

        // Then
        assertNotNull(result);
        assertEquals(auditLogId, result.getId());
        assertEquals("tenant-001", result.getTenantId());
        assertEquals("Transaction", result.getEntityType());

        verify(repository).findById(auditLogId.toString());
        verify(mapper).toResponseDto(auditLog1);
    }

    @Test
    @DisplayName("Should throw exception when audit log not found")
    void shouldThrowExceptionWhenAuditLogNotFound() {
        // Given
        UUID nonExistentId = UUID.randomUUID();
        GetAuditLogQuery.GetAuditLogQueryDto query = GetAuditLogQuery.GetAuditLogQueryDto.builder()
                .auditLogId(nonExistentId.toString())
                .build();

        when(repository.findById(nonExistentId.toString())).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> queryService.getAuditLog(query)
        );

        assertTrue(exception.getMessage().contains("Audit log not found"));
        verify(repository).findById(nonExistentId.toString());
        verify(mapper, never()).toResponseDto(any(AuditLog.class));
    }

    @Test
    @DisplayName("Should search audit logs with pagination")
    void shouldSearchAuditLogsWithPagination() {
        // Given
        List<AuditLog> auditLogs = Arrays.asList(auditLog1, auditLog2);
        List<AuditLogResponseDto> responseDtos = Arrays.asList(responseDto1, responseDto2);

        SearchAuditLogsQuery.SearchAuditLogsQueryDto query = SearchAuditLogsQuery.SearchAuditLogsQueryDto.builder()
                .tenantId("tenant-001")
                .entityType("Transaction")
                .page(0)
                .size(20)
                .build();

        when(repository.searchAuditLogs(
                eq("tenant-001"), eq("Transaction"), isNull(),
                isNull(), isNull(), isNull(), isNull(), isNull(),
                isNull(), isNull()
        )).thenReturn(auditLogs);
        when(mapper.toResponseDtoList(anyList())).thenReturn(responseDtos);

        // When
        PagedAuditLogsResponseDto result = queryService.searchAuditLogs(query);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getAuditLogs().size());
        assertEquals(0, result.getPage());
        assertEquals(20, result.getSize());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertFalse(result.getHasNext());
        assertFalse(result.getHasPrevious());

        verify(repository).searchAuditLogs(any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        verify(mapper).toResponseDtoList(anyList());
    }

    @Test
    @DisplayName("Should handle empty search results")
    void shouldHandleEmptySearchResults() {
        // Given
        SearchAuditLogsQuery.SearchAuditLogsQueryDto query = SearchAuditLogsQuery.SearchAuditLogsQueryDto.builder()
                .tenantId("non-existent-tenant")
                .page(0)
                .size(20)
                .build();

        when(repository.searchAuditLogs(any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(List.of());
        when(mapper.toResponseDtoList(anyList())).thenReturn(List.of());

        // When
        PagedAuditLogsResponseDto result = queryService.searchAuditLogs(query);

        // Then
        assertNotNull(result);
        assertTrue(result.getAuditLogs().isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
        assertFalse(result.getHasNext());
        assertFalse(result.getHasPrevious());
    }

    @Test
    @DisplayName("Should get audit logs by entity")
    void shouldGetAuditLogsByEntity() {
        // Given
        List<AuditLog> auditLogs = Arrays.asList(auditLog1, auditLog2);
        List<AuditLogResponseDto> responseDtos = Arrays.asList(responseDto1, responseDto2);

        when(repository.findByEntityTypeAndEntityId("Transaction", "txn-12345"))
                .thenReturn(auditLogs);
        when(mapper.toResponseDtoList(auditLogs)).thenReturn(responseDtos);

        // When
        List<AuditLogResponseDto> result = queryService.getAuditLogsByEntity("Transaction", "txn-12345");

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findByEntityTypeAndEntityId("Transaction", "txn-12345");
        verify(mapper).toResponseDtoList(auditLogs);
    }

    @Test
    @DisplayName("Should get audit logs by tenant and entity")
    void shouldGetAuditLogsByTenantAndEntity() {
        // Given
        List<AuditLog> auditLogs = Arrays.asList(auditLog1);
        List<AuditLogResponseDto> responseDtos = Arrays.asList(responseDto1);

        when(repository.findByTenantIdAndEntityTypeAndEntityId("tenant-001", "Transaction", "txn-12345"))
                .thenReturn(auditLogs);
        when(mapper.toResponseDtoList(auditLogs)).thenReturn(responseDtos);

        // When
        List<AuditLogResponseDto> result = queryService.getAuditLogsByTenantAndEntity(
                "tenant-001", "Transaction", "txn-12345");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repository).findByTenantIdAndEntityTypeAndEntityId("tenant-001", "Transaction", "txn-12345");
        verify(mapper).toResponseDtoList(auditLogs);
    }

    @Test
    @DisplayName("Should get audit logs by correlation ID")
    void shouldGetAuditLogsByCorrelationId() {
        // Given
        String correlationId = "corr-001";
        List<AuditLog> auditLogs = Arrays.asList(auditLog1, auditLog2);
        List<AuditLogResponseDto> responseDtos = Arrays.asList(responseDto1, responseDto2);

        when(repository.findByCorrelationId(correlationId)).thenReturn(auditLogs);
        when(mapper.toResponseDtoList(auditLogs)).thenReturn(responseDtos);

        // When
        List<AuditLogResponseDto> result = queryService.getAuditLogsByCorrelationId(correlationId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findByCorrelationId(correlationId);
        verify(mapper).toResponseDtoList(auditLogs);
    }

    @Test
    @DisplayName("Should get audit logs by actor")
    void shouldGetAuditLogsByActor() {
        // Given
        String actorId = "user-001";
        List<AuditLog> auditLogs = Arrays.asList(auditLog1, auditLog2);
        List<AuditLogResponseDto> responseDtos = Arrays.asList(responseDto1, responseDto2);

        when(repository.findByActorId(actorId)).thenReturn(auditLogs);
        when(mapper.toResponseDtoList(auditLogs)).thenReturn(responseDtos);

        // When
        List<AuditLogResponseDto> result = queryService.getAuditLogsByActor(actorId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findByActorId(actorId);
        verify(mapper).toResponseDtoList(auditLogs);
    }

    @Test
    @DisplayName("Should count audit logs by tenant")
    void shouldCountAuditLogsByTenant() {
        // Given
        String tenantId = "tenant-001";
        Long expectedCount = 42L;

        when(repository.countByTenantId(tenantId)).thenReturn(expectedCount);

        // When
        Long result = queryService.countByTenant(tenantId);

        // Then
        assertNotNull(result);
        assertEquals(expectedCount, result);
        verify(repository).countByTenantId(tenantId);
    }

    @Test
    @DisplayName("Should handle pagination correctly")
    void shouldHandlePaginationCorrectly() {
        // Given
        List<AuditLog> auditLogs = Arrays.asList(
                auditLog1, auditLog2,
                AuditLog.builder().id(UUID.randomUUID()).tenantId("tenant-001").build(),
                AuditLog.builder().id(UUID.randomUUID()).tenantId("tenant-001").build(),
                AuditLog.builder().id(UUID.randomUUID()).tenantId("tenant-001").build()
        );

        when(repository.searchAuditLogs(any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(auditLogs);
        when(mapper.toResponseDtoList(anyList())).thenAnswer(invocation -> {
            List<?> input = invocation.getArgument(0);
            if (input.size() <= 2) return Arrays.asList(responseDto1, responseDto2).subList(0, input.size());
            return Arrays.asList(responseDto1, responseDto2,
                    AuditLogResponseDto.builder().build(),
                    AuditLogResponseDto.builder().build(),
                    AuditLogResponseDto.builder().build()
            );
        });

        SearchAuditLogsQuery.SearchAuditLogsQueryDto query = SearchAuditLogsQuery.SearchAuditLogsQueryDto.builder()
                .tenantId("tenant-001")
                .page(0)
                .size(2)
                .build();

        // When
        PagedAuditLogsResponseDto result = queryService.searchAuditLogs(query);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getAuditLogs().size()); // Page size
        assertEquals(5, result.getTotalElements());    // Total from repository
        assertEquals(3, result.getTotalPages());       // Ceiling(5/2)
        assertTrue(result.getHasNext());
        assertFalse(result.getHasPrevious());
    }

    @Test
    @DisplayName("Should indicate no next page on last page")
    void shouldIndicateNoNextPageOnLastPage() {
        // Given
        List<AuditLog> auditLogs = Arrays.asList(auditLog1, auditLog2);

        when(repository.searchAuditLogs(any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(auditLogs);
        when(mapper.toResponseDtoList(anyList())).thenReturn(Arrays.asList(responseDto1, responseDto2));

        SearchAuditLogsQuery.SearchAuditLogsQueryDto query = SearchAuditLogsQuery.SearchAuditLogsQueryDto.builder()
                .tenantId("tenant-001")
                .page(1)
                .size(2)
                .build();

        // When
        PagedAuditLogsResponseDto result = queryService.searchAuditLogs(query);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getAuditLogs().size());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertFalse(result.getHasNext());
        assertTrue(result.getHasPrevious());
    }

    @Test
    @DisplayName("Should handle start beyond list size")
    void shouldHandleStartBeyondListSize() {
        // Given
        List<AuditLog> auditLogs = Arrays.asList(auditLog1);

        when(repository.searchAuditLogs(any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(auditLogs);
        when(mapper.toResponseDtoList(anyList())).thenAnswer(invocation -> {
            List<?> input = invocation.getArgument(0);
            return input.isEmpty() ? List.of() : List.of(responseDto1);
        });

        SearchAuditLogsQuery.SearchAuditLogsQueryDto query = SearchAuditLogsQuery.SearchAuditLogsQueryDto.builder()
                .tenantId("tenant-001")
                .page(5)
                .size(2)
                .build();

        // When
        PagedAuditLogsResponseDto result = queryService.searchAuditLogs(query);

        // Then
        assertNotNull(result);
        assertTrue(result.getAuditLogs().isEmpty());
        assertEquals(1, result.getTotalElements());
        assertTrue(result.getHasPrevious());
    }

    @Test
    @DisplayName("Should search with date range filters")
    void shouldSearchWithDateRangeFilters() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 31, 23, 59);

        SearchAuditLogsQuery.SearchAuditLogsQueryDto query = SearchAuditLogsQuery.SearchAuditLogsQueryDto.builder()
                .tenantId("tenant-001")
                .startDate(startDate)
                .endDate(endDate)
                .page(0)
                .size(20)
                .build();

        when(repository.searchAuditLogs(
                eq("tenant-001"), isNull(), isNull(),
                isNull(), isNull(), isNull(), isNull(), isNull(),
                eq(startDate), eq(endDate)
        )).thenReturn(List.of(auditLog1));
        when(mapper.toResponseDtoList(anyList())).thenReturn(List.of(responseDto1));

        // When
        PagedAuditLogsResponseDto result = queryService.searchAuditLogs(query);

        // Then
        assertNotNull(result);
        verify(repository).searchAuditLogs(
                eq("tenant-001"), isNull(), isNull(),
                isNull(), isNull(), isNull(), isNull(), isNull(),
                eq(startDate), eq(endDate)
        );
    }

    @Test
    @DisplayName("Should search with all filters")
    void shouldSearchWithAllFilters() {
        // Given
        SearchAuditLogsQuery.SearchAuditLogsQueryDto query = SearchAuditLogsQuery.SearchAuditLogsQueryDto.builder()
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-12345")
                .action("CREATE")
                .actorId("user-001")
                .severity("INFO")
                .category("BUSINESS")
                .status("SUCCESS")
                .page(0)
                .size(20)
                .build();

        when(repository.searchAuditLogs(
                any(), any(), any(), any(), any(), any(), any(), any(), any(), any()
        )).thenReturn(List.of(auditLog1));
        when(mapper.toResponseDtoList(anyList())).thenReturn(List.of(responseDto1));

        // When
        PagedAuditLogsResponseDto result = queryService.searchAuditLogs(query);

        // Then
        assertNotNull(result);
        verify(repository).searchAuditLogs(
                eq("tenant-001"),
                eq("Transaction"),
                eq("txn-12345"),
                eq("CREATE"),
                eq("user-001"),
                eq("INFO"),
                eq("BUSINESS"),
                eq("SUCCESS"),
                isNull(),
                isNull()
        );
    }
}

package com.gogidix.analytics.data.application.service;

import com.gogidix.analytics.data.domain.model.DataExport;
import com.gogidix.analytics.data.domain.port.in.CreateExportCommand;
import com.gogidix.analytics.data.domain.port.out.DataStorageGateway;
import com.gogidix.analytics.data.domain.repository.DataExportRepository;
import com.gogidix.shared.audit.service.AuditService;
import com.gogidix.shared.exceptions.NotFoundException;
import com.gogidix.shared.exceptions.ValidationException;
import com.gogidix.shared.security.context.RequestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DataExportCommandService Tests")
class DataExportCommandServiceTest {

    @Mock
    private DataExportRepository exportRepository;

    @Mock
    private DataStorageGateway storageGateway;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private DataExportCommandService service;

    private MockedStatic<RequestContext> requestContextMock;

    @BeforeEach
    void setUp() {
        requestContextMock = mockStatic(RequestContext.class);
        requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn("tenant-1");
    }

    @AfterEach
    void tearDown() {
        requestContextMock.close();
    }

    private DataExport buildTestDataExport() {
        return DataExport.builder()
            .id("export-1")
            .exportName("Test Export")
            .exportFormat(DataExport.ExportFormat.CSV)
            .tenantId("tenant-1")
            .requestedBy("user-1")
            .status(DataExport.ExportStatus.PENDING)
            .build();
    }

    private CreateExportCommand buildTestCommand() {
        return CreateExportCommand.builder()
            .exportName("Test Export")
            .exportFormat(DataExport.ExportFormat.CSV)
            .compressionType(DataExport.CompressionType.NONE)
            .includeHeaders(true)
            .requestedBy("user-1")
            .build();
    }

    @Nested
    @DisplayName("createExport Tests")
    class CreateExportTests {

        @Test
        @DisplayName("Should create export successfully")
        void shouldCreateExport() {
            CreateExportCommand cmd = buildTestCommand();
            DataExport saved = buildTestDataExport();
            when(exportRepository.save(any(DataExport.class))).thenReturn(saved);

            DataExport result = service.createExport(cmd);

            assertNotNull(result);
            verify(exportRepository).save(any(DataExport.class));
            verify(auditService).logEvent(eq("DATA_EXPORT_CREATED"), eq("DataExport"), anyString(), anyString());
        }

        @Test
        @DisplayName("Should create export with custom expiration")
        void shouldCreateExportWithCustomExpiration() {
            CreateExportCommand cmd = buildTestCommand();
            cmd.setExpiresInHours(48);
            DataExport saved = buildTestDataExport();
            when(exportRepository.save(any(DataExport.class))).thenReturn(saved);

            DataExport result = service.createExport(cmd);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should create export with filters")
        void shouldCreateExportWithFilters() {
            CreateExportCommand cmd = buildTestCommand();
            cmd.setFilters(Map.of("status", "active"));
            DataExport saved = buildTestDataExport();
            when(exportRepository.save(any(DataExport.class))).thenReturn(saved);

            DataExport result = service.createExport(cmd);

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should throw ValidationException when no tenant")
        void shouldThrowWhenNoTenant() {
            requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.createExport(buildTestCommand()));
        }
    }

    @Nested
    @DisplayName("cancelExport Tests")
    class CancelExportTests {

        @Test
        @DisplayName("Should cancel pending export")
        void shouldCancelPendingExport() {
            DataExport export = buildTestDataExport();
            export.setStatus(DataExport.ExportStatus.PENDING);
            when(exportRepository.findById("export-1")).thenReturn(Optional.of(export));
            when(exportRepository.save(any(DataExport.class))).thenReturn(export);

            service.cancelExport("export-1");

            assertEquals(DataExport.ExportStatus.CANCELLED, export.getStatus());
            verify(auditService).logEvent(eq("DATA_EXPORT_CANCELLED"), eq("DataExport"), anyString(), anyString());
        }

        @Test
        @DisplayName("Should cancel processing export")
        void shouldCancelProcessingExport() {
            DataExport export = buildTestDataExport();
            export.setStatus(DataExport.ExportStatus.PROCESSING);
            when(exportRepository.findById("export-1")).thenReturn(Optional.of(export));
            when(exportRepository.save(any(DataExport.class))).thenReturn(export);

            service.cancelExport("export-1");

            assertEquals(DataExport.ExportStatus.CANCELLED, export.getStatus());
        }

        @Test
        @DisplayName("Should not cancel completed export")
        void shouldNotCancelCompletedExport() {
            DataExport export = buildTestDataExport();
            export.setStatus(DataExport.ExportStatus.COMPLETED);
            when(exportRepository.findById("export-1")).thenReturn(Optional.of(export));

            service.cancelExport("export-1");

            assertEquals(DataExport.ExportStatus.COMPLETED, export.getStatus());
            verify(exportRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw NotFoundException for missing export")
        void shouldThrowNotFound() {
            when(exportRepository.findById("missing")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.cancelExport("missing"));
        }

        @Test
        @DisplayName("Should throw ValidationException for different tenant")
        void shouldThrowForDifferentTenant() {
            DataExport export = buildTestDataExport();
            export.setTenantId("other-tenant");
            when(exportRepository.findById("export-1")).thenReturn(Optional.of(export));

            assertThrows(ValidationException.class, () -> service.cancelExport("export-1"));
        }

        @Test
        @DisplayName("Should throw ValidationException when no tenant")
        void shouldThrowWhenNoTenant() {
            requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.cancelExport("e1"));
        }
    }

    @Nested
    @DisplayName("deleteExport Tests")
    class DeleteExportTests {

        @Test
        @DisplayName("Should delete export with file")
        void shouldDeleteExportWithFile() {
            DataExport export = buildTestDataExport();
            export.setFilePath("/exports/file.csv");
            when(exportRepository.findById("export-1")).thenReturn(Optional.of(export));

            service.deleteExport("export-1");

            verify(storageGateway).deleteExportFile("/exports/file.csv");
            verify(exportRepository).delete(export);
            verify(auditService).logEvent(eq("DATA_EXPORT_DELETED"), eq("DataExport"), anyString(), anyString());
        }

        @Test
        @DisplayName("Should delete export without file")
        void shouldDeleteExportWithoutFile() {
            DataExport export = buildTestDataExport();
            export.setFilePath(null);
            when(exportRepository.findById("export-1")).thenReturn(Optional.of(export));

            service.deleteExport("export-1");

            verify(storageGateway, never()).deleteExportFile(anyString());
            verify(exportRepository).delete(export);
        }

        @Test
        @DisplayName("Should handle file deletion failure gracefully")
        void shouldHandleFileDeletionFailure() {
            DataExport export = buildTestDataExport();
            export.setFilePath("/exports/file.csv");
            when(exportRepository.findById("export-1")).thenReturn(Optional.of(export));
            doThrow(new RuntimeException("Storage error")).when(storageGateway).deleteExportFile(anyString());

            service.deleteExport("export-1");

            verify(exportRepository).delete(export);
        }

        @Test
        @DisplayName("Should throw NotFoundException for missing export")
        void shouldThrowNotFound() {
            when(exportRepository.findById("missing")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.deleteExport("missing"));
        }

        @Test
        @DisplayName("Should throw ValidationException for different tenant")
        void shouldThrowForDifferentTenant() {
            DataExport export = buildTestDataExport();
            export.setTenantId("other-tenant");
            when(exportRepository.findById("export-1")).thenReturn(Optional.of(export));

            assertThrows(ValidationException.class, () -> service.deleteExport("export-1"));
        }

        @Test
        @DisplayName("Should throw ValidationException when no tenant")
        void shouldThrowWhenNoTenant() {
            requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.deleteExport("e1"));
        }
    }

    @Nested
    @DisplayName("createExport filter handling Tests")
    class CreateExportFilterTests {

        @Test
        @DisplayName("Should create export with empty filters")
        void shouldCreateExportWithEmptyFilters() {
            CreateExportCommand cmd = buildTestCommand();
            cmd.setFilters(Map.of());
            DataExport saved = buildTestDataExport();
            when(exportRepository.save(any(DataExport.class))).thenReturn(saved);

            DataExport result = service.createExport(cmd);

            assertNotNull(result);
            verify(exportRepository).save(argThat(export -> export.getFilters() == null));
        }

        @Test
        @DisplayName("Should create export with null expiresInHours")
        void shouldCreateExportWithNullExpiresInHours() {
            CreateExportCommand cmd = buildTestCommand();
            cmd.setExpiresInHours(null);
            DataExport saved = buildTestDataExport();
            when(exportRepository.save(any(DataExport.class))).thenReturn(saved);

            DataExport result = service.createExport(cmd);

            assertNotNull(result);
            verify(exportRepository).save(argThat(export -> export.getExpiresAt() == null));
        }

        @Test
        @DisplayName("Should not cancel CANCELLED export")
        void shouldNotCancelCancelledExport() {
            DataExport export = buildTestDataExport();
            export.setStatus(DataExport.ExportStatus.CANCELLED);
            when(exportRepository.findById("export-1")).thenReturn(Optional.of(export));

            service.cancelExport("export-1");

            assertEquals(DataExport.ExportStatus.CANCELLED, export.getStatus());
            verify(exportRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should not cancel FAILED export")
        void shouldNotCancelFailedExport() {
            DataExport export = buildTestDataExport();
            export.setStatus(DataExport.ExportStatus.FAILED);
            when(exportRepository.findById("export-1")).thenReturn(Optional.of(export));

            service.cancelExport("export-1");

            assertEquals(DataExport.ExportStatus.FAILED, export.getStatus());
            verify(exportRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("processExportAsync Tests")
    class ProcessExportAsyncTests {

        @Test
        @DisplayName("Should return early when export not found")
        void shouldReturnEarlyWhenExportNotFound() throws Exception {
            when(exportRepository.findById("missing")).thenReturn(Optional.empty());
            CreateExportCommand cmd = buildTestCommand();

            java.lang.reflect.Method method = DataExportCommandService.class
                .getDeclaredMethod("processExportAsync", String.class, CreateExportCommand.class);
            method.setAccessible(true);
            method.invoke(service, "missing", cmd);

            verify(exportRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should process export and mark completed")
        void shouldProcessExportAndMarkCompleted() throws Exception {
            DataExport export = buildTestDataExport();
            export.setId("export-process");
            when(exportRepository.findById("export-process")).thenReturn(Optional.of(export));
            when(exportRepository.save(any(DataExport.class))).thenReturn(export);
            when(storageGateway.getDownloadUrl(anyString(), anyInt())).thenReturn("http://url");

            CreateExportCommand cmd = buildTestCommand();

            java.lang.reflect.Method method = DataExportCommandService.class
                .getDeclaredMethod("processExportAsync", String.class, CreateExportCommand.class);
            method.setAccessible(true);
            method.invoke(service, "export-process", cmd);

            verify(exportRepository, atLeast(2)).save(any(DataExport.class));
            assertEquals(DataExport.ExportStatus.COMPLETED, export.getStatus());
        }

        @Test
        @DisplayName("Should mark as failed on exception")
        void shouldMarkAsFailedOnException() throws Exception {
            DataExport export = buildTestDataExport();
            export.setId("export-fail");
            when(exportRepository.findById("export-fail"))
                .thenThrow(new RuntimeException("DB error"))
                .thenReturn(Optional.of(export));
            when(exportRepository.save(any(DataExport.class))).thenReturn(export);

            CreateExportCommand cmd = buildTestCommand();

            java.lang.reflect.Method method = DataExportCommandService.class
                .getDeclaredMethod("processExportAsync", String.class, CreateExportCommand.class);
            method.setAccessible(true);
            method.invoke(service, "export-fail", cmd);

            verify(exportRepository).save(argThat(e ->
                e.getStatus() == DataExport.ExportStatus.FAILED));
        }

        @Test
        @DisplayName("Should handle exception when export not found in catch")
        void shouldHandleExceptionWhenExportNotFoundInCatch() throws Exception {
            when(exportRepository.findById("export-gone"))
                .thenThrow(new RuntimeException("DB error"))
                .thenReturn(Optional.empty());

            CreateExportCommand cmd = buildTestCommand();

            java.lang.reflect.Method method = DataExportCommandService.class
                .getDeclaredMethod("processExportAsync", String.class, CreateExportCommand.class);
            method.setAccessible(true);
            method.invoke(service, "export-gone", cmd);

            verify(exportRepository, never()).save(any());
        }
    }
}

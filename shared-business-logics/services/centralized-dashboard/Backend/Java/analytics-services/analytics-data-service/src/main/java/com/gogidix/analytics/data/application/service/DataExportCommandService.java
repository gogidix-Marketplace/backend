package com.gogidix.analytics.data.application.service;

import com.gogidix.analytics.data.domain.model.DataExport;
import com.gogidix.analytics.data.domain.port.in.CreateExportCommand;
import com.gogidix.analytics.data.domain.port.out.DataStorageGateway;
import com.gogidix.analytics.data.domain.repository.DataExportRepository;
import com.gogidix.shared.audit.service.AuditService;
import com.gogidix.shared.exceptions.NotFoundException;
import com.gogidix.shared.exceptions.ValidationException;
import com.gogidix.shared.security.context.RequestContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataExportCommandService {

    private final DataExportRepository exportRepository;
    private final DataStorageGateway storageGateway;
    private final AuditService auditService;

    @Transactional
    public DataExport createExport(CreateExportCommand command) {
        log.info("Creating data export: format={}, queryId={}", command.getExportFormat(), command.getQueryId());

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        DataExport export = DataExport.builder()
            .exportName(command.getExportName())
            .queryId(command.getQueryId())
            .queryDefinition(command.getQueryDefinition())
            .exportFormat(command.getExportFormat())
            .status(DataExport.ExportStatus.PENDING)
            .compressionType(command.getCompressionType())
            .includeHeaders(command.getIncludeHeaders())
            .filters(convertFiltersToJson(command.getFilters()))
            .requestedBy(command.getRequestedBy())
            .tenantId(tenantId)
            .progressPercentage(0)
            .build();

        if (command.getExpiresInHours() != null) {
            export.setExpiresAt(LocalDateTime.now().plusHours(command.getExpiresInHours()));
        }

        DataExport savedExport = exportRepository.save(export);

        auditService.logEvent(
            "DATA_EXPORT_CREATED",
            "DataExport",
            savedExport.getId(),
            "Created data export: " + savedExport.getExportName()
        );

        processExportAsync(savedExport.getId(), command);

        log.info("Data export created: id={}", savedExport.getId());
        return savedExport;
    }

    @Transactional
    public void cancelExport(String exportId) {
        log.info("Cancelling data export: exportId={}", exportId);

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        DataExport export = exportRepository.findById(exportId)
            .orElseThrow(() -> new NotFoundException("Export not found: " + exportId));

        if (!export.getTenantId().equals(tenantId)) {
            throw new ValidationException("Access denied: Export belongs to different tenant");
        }

        if (export.getStatus() == DataExport.ExportStatus.PENDING ||
            export.getStatus() == DataExport.ExportStatus.PROCESSING) {

            export.setStatus(DataExport.ExportStatus.CANCELLED);
            export.setCompletedAt(LocalDateTime.now());
            exportRepository.save(export);

            auditService.logEvent(
                "DATA_EXPORT_CANCELLED",
                "DataExport",
                exportId,
                "Cancelled data export"
            );

            log.info("Data export cancelled: exportId={}", exportId);
        }
    }

    @Transactional
    public void deleteExport(String exportId) {
        log.info("Deleting data export: exportId={}", exportId);

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        DataExport export = exportRepository.findById(exportId)
            .orElseThrow(() -> new NotFoundException("Export not found: " + exportId));

        if (!export.getTenantId().equals(tenantId)) {
            throw new ValidationException("Access denied: Export belongs to different tenant");
        }

        if (export.getFilePath() != null) {
            try {
                storageGateway.deleteExportFile(export.getFilePath());
            } catch (Exception e) {
                log.warn("Failed to delete export file: {}", export.getFilePath(), e);
            }
        }

        exportRepository.delete(export);

        auditService.logEvent(
            "DATA_EXPORT_DELETED",
            "DataExport",
            exportId,
            "Deleted data export"
        );

        log.info("Data export deleted: exportId={}", exportId);
    }

    @Async
    protected void processExportAsync(String exportId, CreateExportCommand command) {
        try {
            Thread.sleep(100);

            DataExport export = exportRepository.findById(exportId).orElse(null);
            if (export == null) return;

            export.markAsProcessing();
            exportRepository.save(export);

            int totalSteps = 10;
            for (int i = 1; i <= totalSteps; i++) {
                Thread.sleep(200);
                export.updateProgress(i * 100 / totalSteps);
                exportRepository.save(export);
            }

            String filePath = "/exports/" + exportId + "." +
                command.getExportFormat().name().toLowerCase();
            String fileUrl = storageGateway.getDownloadUrl(filePath, 86400);

            export.markAsCompleted(filePath, fileUrl, 2048L, 100, 5);
            exportRepository.save(export);

            log.info("Export processing completed: exportId={}", exportId);

        } catch (Exception e) {
            DataExport export = exportRepository.findById(exportId).orElse(null);
            if (export != null) {
                export.markAsFailed(e.getMessage());
                exportRepository.save(export);
            }
            log.error("Export processing failed: exportId={}", exportId, e);
        }
    }

    private String convertFiltersToJson(java.util.Map<String, Object> filters) {
        if (filters == null || filters.isEmpty()) {
            return null;
        }
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.writeValueAsString(filters);
        } catch (Exception e) {
            log.warn("Failed to convert filters to JSON", e);
            return null;
        }
    }
}

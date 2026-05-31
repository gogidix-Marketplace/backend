package com.gogidix.analytics.data.infrastructure.persistence;

import com.gogidix.analytics.data.domain.model.DataExport;
import com.gogidix.analytics.data.domain.port.out.DataStorageGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * File system implementation of DataStorageGateway.
 * In production, this would use cloud storage (S3, Azure Blob, etc.).
 */
@Slf4j
@Component
public class DataStorageGatewayImpl implements DataStorageGateway {

    private static final String BASE_EXPORT_PATH = "/tmp/exports";

    @Override
    public String storeExportFile(DataExport export, byte[] data) {
        try {
            String fileName = export.getId() + "." + export.getExportFormat().name().toLowerCase();
            Path directory = Paths.get(BASE_EXPORT_PATH, export.getTenantId());
            Files.createDirectories(directory);

            Path filePath = directory.resolve(fileName);
            Files.write(filePath, data);

            log.info("Export file stored: path={}", filePath);
            return filePath.toString();
        } catch (IOException e) {
            log.error("Failed to store export file", e);
            throw new RuntimeException("Failed to store export file", e);
        }
    }

    @Override
    public byte[] retrieveExportFile(String filePath) {
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            log.error("Failed to retrieve export file: path={}", filePath, e);
            throw new RuntimeException("Failed to retrieve export file", e);
        }
    }

    @Override
    public String getDownloadUrl(String filePath, int expiresInSeconds) {
        // In production, this would generate a pre-signed URL for cloud storage
        return "http://localhost:8903/api/v1/data/download?file=" +
            filePath.replace("/", "_");
    }

    @Override
    public void deleteExportFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
            log.info("Export file deleted: path={}", filePath);
        } catch (IOException e) {
            log.warn("Failed to delete export file: path={}", filePath, e);
        }
    }

    @Override
    public Map<String, Object> getExportMetadata(String filePath) {
        File file = new File(filePath);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("filePath", filePath);
        metadata.put("exists", file.exists());
        metadata.put("size", file.exists() ? file.length() : 0);
        metadata.put("lastModified", file.exists() ? file.lastModified() : null);
        return metadata;
    }
}

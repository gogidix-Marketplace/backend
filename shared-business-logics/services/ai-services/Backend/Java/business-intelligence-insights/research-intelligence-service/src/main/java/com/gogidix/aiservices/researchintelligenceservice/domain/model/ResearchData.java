package com.gogidix.aiservices.researchintelligenceservice.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class ResearchData {
    private final String datasetId;
    private final String projectId;
    private final String name;
    private final String description;
    private final DataType dataType;
    private final long sizeInBytes;
    private final String storageLocation;
    private final String uploadedBy;
    private final LocalDateTime uploadedAt;
    private final DataStatus status;
    private final int recordCount;
    private final String schemaVersion;

    public ResearchData(String datasetId, String projectId, String name, String description,
                       DataType dataType, String storageLocation, String uploadedBy) {
        this.datasetId = Objects.requireNonNull(datasetId, "datasetId cannot be null");
        this.projectId = Objects.requireNonNull(projectId, "projectId cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.description = description;
        this.dataType = Objects.requireNonNull(dataType, "dataType cannot be null");
        this.sizeInBytes = 0;
        this.storageLocation = storageLocation;
        this.uploadedBy = uploadedBy;
        this.uploadedAt = LocalDateTime.now();
        this.status = DataStatus.ACTIVE;
        this.recordCount = 0;
        this.schemaVersion = "1.0";
    }

    public String getDatasetId() {
        return datasetId;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public DataType getDataType() {
        return dataType;
    }

    public long getSizeInBytes() {
        return sizeInBytes;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public DataStatus getStatus() {
        return status;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResearchData that = (ResearchData) o;
        return Objects.equals(datasetId, that.datasetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(datasetId);
    }

    @Override
    public String toString() {
        return "ResearchData{" +
                "datasetId='" + datasetId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", name='" + name + '\'' +
                ", dataType=" + dataType +
                ", sizeInBytes=" + sizeInBytes +
                ", status=" + status +
                ", recordCount=" + recordCount +
                '}';
    }

    public enum DataType {
        STRUCTURED,
        UNSTRUCTURED,
        SEMI_STRUCTURED,
        TIME_SERIES,
        IMAGE,
        VIDEO,
        AUDIO,
        TEXT,
        NUMERICAL,
        CATEGORICAL
    }

    public enum DataStatus {
        ACTIVE,
        ARCHIVED,
        DEPRECATED,
        PROCESSING,
        ERROR
    }
}

package com.gogidix.aiservices.aiprediction.domain;

import lombok.Builder;
import lombok.NonNull;
import lombok.Getter;
import lombok.With;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * Value object representing model metadata.
 */
@Getter
@Builder
@With
public class ModelMetadata {

    @NonNull
    private final String modelId;

    @NonNull
    private final String modelName;

    @NonNull
    private final ModelType type;

    @NonNull
    private final String version;

    @NonNull
    private final LocalDateTime createdAt;

    @Builder.Default
    private final boolean deprecated = false;

    @Builder.Default
    private final String description = "";

    private ModelMetadata(String modelId, String modelName, ModelType type,
                       String version, LocalDateTime createdAt, boolean deprecated, String description) {
        if (modelId == null || modelId.trim().isEmpty()) {
            throw new IllegalArgumentException("modelId cannot be null or empty");
        }
        if (modelName == null || modelName.trim().isEmpty()) {
            throw new IllegalArgumentException("modelName cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null");
        }
        if (version == null || !isValidVersion(version)) {
            throw new IllegalArgumentException("version must be in semantic version format (e.g., 1.0.0)");
        }
        if (createdAt == null) {
            throw new IllegalArgumentException("createdAt cannot be null");
        }

        this.modelId = modelId;
        this.modelName = modelName;
        this.type = type;
        this.version = version;
        this.createdAt = createdAt;
        this.deprecated = deprecated;
        this.description = description;
    }

    // Alternate constructor for backward compatibility
    public ModelMetadata(String modelId, String modelName, ModelType type,
                       String version, LocalDateTime createdAt) {
        this(modelId, modelName, type, version, createdAt, false, "");
    }

    private static boolean isValidVersion(String version) {
        return Pattern.matches("^\\d+\\.\\d+\\.\\d+(-[a-zA-Z0-9]+)?$", version);
    }

    public boolean isNewerThan(ModelMetadata other) {
        if (other == null) {
            return true;
        }
        return compareVersions(this.version, other.version) > 0;
    }

    public boolean isDeprecated() {
        // Consider deprecated if created more than 2 years ago
        return deprecated || createdAt.isBefore(LocalDateTime.now().minusYears(2));
    }

    private int compareVersions(String v1, String v2) {
        String[] parts1 = v1.replaceFirst("-.*", "").split("\\.");
        String[] parts2 = v2.replaceFirst("-.*", "").split("\\.");

        for (int i = 0; i < 3; i++) {
            int num1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int num2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;
            if (num1 != num2) {
                return Integer.compare(num1, num2);
            }
        }
        return 0;
    }

    public enum ModelType {
        CLASSIFICATION,
        REGRESSION,
        CLUSTERING,
        TIME_SERIES,
        ANOMALY_DETECTION
    }
}

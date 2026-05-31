package com.gogidix.aiservices.aidataprocessing.domain.aggregate;

import com.gogidix.aiservices.aidataprocessing.domain.model.*;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class DataBatch {
    private static final int MAX_TRANSFORMATIONS = 15;
    private static final int MAX_RECORDS_PER_BATCH = 10_000_000;
    private static final long MAX_BATCH_SIZE_BYTES = 1_073_741_824L; // 1GB

    private final String batchId;
    private final String source;
    private final DataFormat format;
    private ProcessingStatus status;
    private final List<DataTransformation> transformations;
    private final List<ValidationRule> validationRules;
    private final List<Map<String, Object>> records;
    private final Instant createdAt;
    private Instant startedAt;
    private Instant completedAt;
    private int recordsProcessed;
    private int progressTotal;
    private int progressCurrent;
    private String error;
    private long estimatedSizeBytes;

    private DataBatch(String source, DataFormat format) {
        if (source == null || source.trim().isEmpty()) {
            throw new IllegalArgumentException("Source cannot be null or empty");
        }
        if (format == null) {
            throw new IllegalArgumentException("Format cannot be null");
        }

        this.batchId = UUID.randomUUID().toString();
        this.source = source;
        this.format = format;
        this.status = ProcessingStatus.PENDING;
        this.transformations = new ArrayList<>();
        this.validationRules = new ArrayList<>();
        this.records = new ArrayList<>();
        this.createdAt = Instant.now();
        this.recordsProcessed = 0;
        this.progressTotal = 0;
        this.progressCurrent = 0;
        this.estimatedSizeBytes = 0;
    }

    public static DataBatch create(String source, DataFormat format) {
        return new DataBatch(source, format);
    }

    // Getters
    public String getBatchId() { return batchId; }
    public String getSource() { return source; }
    public DataFormat getFormat() { return format; }
    public ProcessingStatus getStatus() { return status; }
    public List<DataTransformation> getTransformations() { return Collections.unmodifiableList(transformations); }
    public List<ValidationRule> getValidationRules() { return Collections.unmodifiableList(validationRules); }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public int getRecordsProcessed() { return recordsProcessed; }
    public int getProgress() { return progressTotal > 0 ? (progressCurrent * 100) / progressTotal : 0; }
    public String getError() { return error; }
    public long getBatchSizeBytes() { return estimatedSizeBytes; }
    public int getRecordCount() { return records.size(); }
    public Long getProcessingDurationMs() {
        if (startedAt == null) return 0L;
        Instant end = completedAt != null ? completedAt : Instant.now();
        return Duration.between(startedAt, end).toMillis();
    }

    // Business methods
    public void startProcessing() {
        if (status != ProcessingStatus.PENDING) {
            throw new IllegalStateException("Can only start processing from PENDING state");
        }
        this.status = ProcessingStatus.PROCESSING;
        this.startedAt = Instant.now();
    }

    public void completeProcessing(int recordsProcessed) {
        if (status != ProcessingStatus.PROCESSING) {
            throw new IllegalStateException("Can only complete from PROCESSING state");
        }
        this.status = ProcessingStatus.COMPLETED;
        this.completedAt = Instant.now();
        this.recordsProcessed = recordsProcessed;
        this.progressCurrent = this.progressTotal;
    }

    public void failProcessing(String error) {
        if (status != ProcessingStatus.PROCESSING) {
            throw new IllegalStateException("Can only fail from PROCESSING state");
        }
        this.status = ProcessingStatus.FAILED;
        this.completedAt = Instant.now();
        this.error = error;
    }

    public void cancel() {
        if (status == ProcessingStatus.COMPLETED || status == ProcessingStatus.FAILED) {
            throw new IllegalStateException("Cannot cancel completed or failed batch");
        }
        this.status = ProcessingStatus.CANCELLED;
        this.completedAt = Instant.now();
    }

    public void addTransformation(DataTransformation transformation) {
        if (transformation == null) {
            throw new IllegalArgumentException("Transformation cannot be null");
        }
        if (transformations.size() >= MAX_TRANSFORMATIONS) {
            throw new IllegalStateException("Maximum transformations (" + MAX_TRANSFORMATIONS + ") exceeded");
        }
        transformations.add(transformation);
    }

    public void addValidationRule(ValidationRule rule) {
        if (rule == null) {
            throw new IllegalArgumentException("Validation rule cannot be null");
        }
        validationRules.add(rule);
    }

    public void addRecord(Map<String, Object> record) {
        if (status != ProcessingStatus.PROCESSING) {
            throw new IllegalStateException("Can only add records during processing");
        }
        if (records.size() >= MAX_RECORDS_PER_BATCH) {
            return; // Silently drop excess records
        }
        records.add(record != null ? new HashMap<>(record) : new HashMap<>());
        updateSizeEstimate();
    }

    public void addSampleData(Map<String, Object> sample) {
        records.add(sample != null ? new HashMap<>(sample) : new HashMap<>());
        updateSizeEstimate();
    }

    public void updateProgress(int current, int total) {
        this.progressCurrent = current;
        this.progressTotal = total;
    }

    public ValidationResult validate() {
        List<ValidationResult.ValidationError> errors = new ArrayList<>();
        List<ValidationResult.ValidationWarning> warnings = new ArrayList<>();

        for (ValidationRule rule : validationRules) {
            for (Map<String, Object> record : records) {
                Object value = record.get(rule.getField());
                ValidationResult result = validateField(value, rule);

                if (!result.isValid()) {
                    errors.addAll(result.getErrors());
                }
                warnings.addAll(result.getWarnings());
            }
        }

        return errors.isEmpty()
                ? ValidationResult.withWarnings(warnings)
                : ValidationResult.invalid(errors);
    }

    private ValidationResult validateField(Object value, ValidationRule rule) {
        switch (rule.getType()) {
            case REQUIRED:
                if (value == null) {
                    return ValidationResult.invalid(List.of(
                            new ValidationResult.ValidationError(rule.getField(), "Field is required", null)
                    ));
                }
                break;

            case TYPE:
                if (value != null && rule.getConfig().containsKey("expectedType")) {
                    String expectedType = (String) rule.getConfig().get("expectedType");
                    if (!isTypeMatch(value, expectedType)) {
                        return ValidationResult.invalid(List.of(
                                new ValidationResult.ValidationError(rule.getField(),
                                        "Expected type: " + expectedType + ", got: " + value.getClass().getSimpleName(), value)
                        ));
                    }
                }
                break;

            case RANGE:
                if (value instanceof Number) {
                    double numValue = ((Number) value).doubleValue();
                    double min = rule.getConfig().containsKey("min")
                            ? ((Number) rule.getConfig().get("min")).doubleValue() : Double.MIN_VALUE;
                    double max = rule.getConfig().containsKey("max")
                            ? ((Number) rule.getConfig().get("max")).doubleValue() : Double.MAX_VALUE;

                    if (numValue < min || numValue > max) {
                        return ValidationResult.invalid(List.of(
                                new ValidationResult.ValidationError(rule.getField(),
                                        "Value out of range [" + min + ", " + max + "]", value)
                        ));
                    }
                }
                break;

            case EMAIL:
                if (value != null && value instanceof String) {
                    String email = (String) value;
                    if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                        return ValidationResult.invalid(List.of(
                                new ValidationResult.ValidationError(rule.getField(), "Invalid email format", value)
                        ));
                    }
                }
                break;
        }

        return ValidationResult.valid();
    }

    private boolean isTypeMatch(Object value, String expectedType) {
        return switch (expectedType.toLowerCase()) {
            case "string" -> value instanceof String;
            case "integer", "int" -> value instanceof Integer;
            case "long" -> value instanceof Long;
            case "double", "decimal" -> value instanceof Double || value instanceof Float;
            case "boolean" -> value instanceof Boolean;
            default -> true;
        };
    }

    private void updateSizeEstimate() {
        estimatedSizeBytes = records.stream()
                .mapToLong(r -> estimateRecordSize(r))
                .sum();
    }

    private long estimateRecordSize(Map<String, Object> record) {
        return record.entrySet().stream()
                .mapToLong(e -> {
                    long keySize = e.getKey().getBytes().length;
                    long valueSize = e.getValue() != null ? e.getValue().toString().getBytes().length : 0;
                    return keySize + valueSize + 16; // overhead
                })
                .sum();
    }
}

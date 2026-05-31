package com.gogidix.aiservices.aidocumentprocessingservice.domain.aggregate;

import com.gogidix.aiservices.aidocumentprocessingservice.domain.event.*;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.*;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DocumentProcessingJob {
    private static final long MAX_PROCESSING_TIMEOUT_MS = Duration.ofMinutes(5).toMillis();
    private static final double MIN_CONFIDENCE_THRESHOLD = 0.7;

    private final UUID jobId;
    private final String documentUrl;
    private final DocumentType documentType;
    private final String userId;
    private final List<ExtractedField> extractedFields;
    private final Instant createdAt;

    private ProcessingStatus status;
    private Instant startedAt;
    private Instant completedAt;
    private Instant failedAt;
    private Instant updatedAt;
    private int progress;
    private int pagesProcessed;
    private double confidence;
    private String errorMessage;
    private ExtractionConfig extractionConfig;
    private final Map<String, Object> metadata;

    private DocumentProcessingJob(String documentUrl, DocumentType documentType, String userId) {
        if (documentUrl == null || documentUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("Document URL cannot be null or empty");
        }
        if (documentType == null) {
            throw new IllegalArgumentException("Document type cannot be null");
        }

        this.jobId = UUID.randomUUID();
        this.documentUrl = documentUrl;
        this.documentType = documentType;
        this.userId = userId;
        this.status = ProcessingStatus.PENDING;
        this.extractedFields = new ArrayList<>();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.progress = 0;
        this.pagesProcessed = 0;
        this.confidence = 0.0;
        this.metadata = new ConcurrentHashMap<>();
    }

    public static DocumentProcessingJob create(String documentUrl, DocumentType documentType, String userId) {
        return new DocumentProcessingJob(documentUrl, documentType, userId);
    }

    public void startProcessing() {
        if (this.status != ProcessingStatus.PENDING) {
            throw new IllegalStateException("Job is already being processed or completed");
        }
        this.status = ProcessingStatus.PROCESSING;
        this.startedAt = Instant.now();
        touch();
    }

    public void completeProcessing(List<ExtractedField> fields, int pagesProcessed, double confidence) {
        if (this.status != ProcessingStatus.PROCESSING) {
            throw new IllegalStateException("Job is not in processing state");
        }

        this.extractedFields.clear();
        if (fields != null) {
            this.extractedFields.addAll(fields);
        }

        this.pagesProcessed = pagesProcessed;
        this.confidence = confidence;
        this.status = ProcessingStatus.COMPLETED;
        this.completedAt = Instant.now();
        this.progress = 100;
        touch();
    }

    public void failProcessing(String errorMessage) {
        if (this.status == ProcessingStatus.COMPLETED || this.status == ProcessingStatus.FAILED) {
            throw new IllegalStateException("Cannot fail a completed or already failed job");
        }

        this.status = ProcessingStatus.FAILED;
        this.errorMessage = errorMessage;
        this.failedAt = Instant.now();
        touch();
    }

    public void addExtractedField(ExtractedField field) {
        if (field != null) {
            this.extractedFields.add(field);
            touch();
        }
    }

    public Optional<ExtractedField> getField(String name) {
        return extractedFields.stream()
                .filter(f -> f.getName().equals(name))
                .findFirst();
    }

    public String getFieldValue(String name) {
        return getField(name)
                .map(ExtractedField::getValue)
                .orElse(null);
    }

    public void updateProgress(int progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("Progress must be between 0 and 100");
        }
        this.progress = progress;
        touch();
    }

    public void setExtractionConfig(ExtractionConfig config) {
        this.extractionConfig = config;
        touch();
    }

    public ValidationResult validate() {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        if (confidence < MIN_CONFIDENCE_THRESHOLD) {
            warnings.add("Overall confidence below threshold: " + confidence);
        }

        for (ExtractedField field : extractedFields) {
            if (field.getConfidence() < MIN_CONFIDENCE_THRESHOLD) {
                warnings.add("Field '" + field.getName() + "' has low confidence: " + field.getConfidence());
            }
        }

        if (!hasRequiredFields()) {
            errors.add("Missing required fields for document type: " + documentType);
        }

        return ValidationResult.builder()
                .valid(errors.isEmpty())
                .errors(errors)
                .warnings(warnings)
                .build();
    }

    private boolean hasRequiredFields() {
        if (documentType == DocumentType.INVOICE) {
            return extractedFields.stream().anyMatch(f -> f.getName().equals("invoice_number")) &&
                    extractedFields.stream().anyMatch(f -> f.getName().equals("amount"));
        }
        return true;
    }

    public Instant getEstimatedCompletionTime() {
        if (startedAt == null || progress <= 0) {
            return null;
        }

        long elapsed = Duration.between(startedAt, Instant.now()).toMillis();
        long estimatedTotal = (elapsed * 100) / progress;
        return startedAt.plusMillis(estimatedTotal);
    }

    public ProcessingStartedEvent createStartedEvent() {
        return new ProcessingStartedEvent(jobId, documentUrl, documentType, userId, startedAt);
    }

    public ProcessingCompletedEvent createCompletedEvent() {
        return new ProcessingCompletedEvent(jobId, documentUrl, extractedFields.size(),
                pagesProcessed, confidence, userId, completedAt);
    }

    public ProcessingFailedEvent createFailedEvent() {
        return new ProcessingFailedEvent(jobId, documentUrl, errorMessage, userId, failedAt);
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    // Getters
    public UUID getJobId() { return jobId; }
    public String getDocumentUrl() { return documentUrl; }
    public DocumentType getDocumentType() { return documentType; }
    public String getUserId() { return userId; }
    public ProcessingStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public Instant getFailedAt() { return failedAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public int getProgress() { return progress; }
    public int getPagesProcessed() { return pagesProcessed; }
    public double getConfidence() { return confidence; }
    public String getErrorMessage() { return errorMessage; }
    public ExtractionConfig getExtractionConfig() { return extractionConfig; }
    public List<ExtractedField> getExtractedFields() { return Collections.unmodifiableList(extractedFields); }
    public Map<String, Object> getMetadata() { return Collections.unmodifiableMap(metadata); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentProcessingJob that = (DocumentProcessingJob) o;
        return Objects.equals(jobId, that.jobId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId);
    }
}

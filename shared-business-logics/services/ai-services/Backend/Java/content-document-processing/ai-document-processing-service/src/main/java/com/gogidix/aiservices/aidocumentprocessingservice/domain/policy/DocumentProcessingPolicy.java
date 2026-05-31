package com.gogidix.aiservices.aidocumentprocessingservice.domain.policy;

import com.gogidix.aiservices.aidocumentprocessingservice.domain.aggregate.DocumentProcessingJob;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class DocumentProcessingPolicy {
    private static final Set<String> SUPPORTED_FORMATS = Set.of("pdf", "docx", "txt", "png", "jpg", "jpeg");
    private static final long MAX_FILE_SIZE_BYTES = 50L * 1024 * 1024; // 50MB
    private static final long MAX_PROCESSING_TIMEOUT_MS = Duration.ofMinutes(5).toMillis();
    private static final double MIN_CONFIDENCE_THRESHOLD = 0.7;

    public boolean isSupportedFormat(String format) {
        if (format == null) {
            return false;
        }
        return SUPPORTED_FORMATS.contains(format.toLowerCase());
    }

    public boolean isValidSize(long fileSizeBytes) {
        return fileSizeBytes > 0 && fileSizeBytes <= MAX_FILE_SIZE_BYTES;
    }

    public boolean isValidUrl(String urlString) {
        if (urlString == null || urlString.trim().isEmpty()) {
            return false;
        }
        try {
            URL url = new URL(urlString);
            String protocol = url.getProtocol();
            return Set.of("http", "https", "s3", "gs").contains(protocol);
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public boolean isAccessibleUrl(String urlString) {
        return isValidUrl(urlString);
    }

    public boolean meetsMinimumThreshold(double confidence) {
        return confidence >= MIN_CONFIDENCE_THRESHOLD;
    }

    public double getMinimumConfidenceThreshold() {
        return MIN_CONFIDENCE_THRESHOLD;
    }

    public boolean isJobQualityAcceptable(DocumentProcessingJob job) {
        return job.getConfidence() >= MIN_CONFIDENCE_THRESHOLD;
    }

    public boolean hasRequiredFields(DocumentType type, List<ExtractedField> fields) {
        if (type == null || fields == null) {
            return false;
        }

        return switch (type) {
            case INVOICE -> hasField(fields, "invoice_number") &&
                    hasField(fields, "amount") &&
                    hasField(fields, "date") &&
                    hasField(fields, "vendor");
            case CONTRACT -> hasField(fields, "contract_number") &&
                    hasField(fields, "party_a") &&
                    hasField(fields, "party_b") &&
                    hasField(fields, "start_date") &&
                    hasField(fields, "end_date");
            case RECEIPT -> hasField(fields, "amount") &&
                    hasField(fields, "date") &&
                    hasField(fields, "merchant");
            default -> true;
        };
    }

    private boolean hasField(List<ExtractedField> fields, String fieldName) {
        return fields.stream()
                .anyMatch(f -> f.getName().equals(fieldName) && f.getValue() != null && !f.getValue().isEmpty());
    }

    public boolean allFieldsMeetThreshold(List<ExtractedField> fields, double threshold) {
        return fields.stream().allMatch(f -> f.getConfidence() >= threshold);
    }

    public boolean isProcessingTimeoutExceeded(long elapsedMs) {
        return elapsedMs > MAX_PROCESSING_TIMEOUT_MS;
    }

    public long getMaxProcessingTimeoutMs() {
        return MAX_PROCESSING_TIMEOUT_MS;
    }

    public long getRemainingProcessingTime(long elapsedMs) {
        long remaining = MAX_PROCESSING_TIMEOUT_MS - elapsedMs;
        return Math.max(0, remaining);
    }

    public DocumentType detectTypeFromFilename(String filename) {
        return DocumentType.fromFilename(filename);
    }

    public boolean isValidExtractionConfig(ExtractionConfig config) {
        if (config == null) {
            return false;
        }

        int extractionCount = 0;
        if (config.isExtractTables()) extractionCount++;
        if (config.isExtractImages()) extractionCount++;

        int fieldCount = config.getFields() != null ? config.getFields().size() : 0;

        return fieldCount + extractionCount <= 5;
    }

    public ExtractionConfig getDefaultExtractionConfig(DocumentType type) {
        List<String> defaultFields = switch (type) {
            case INVOICE -> Arrays.asList("invoice_number", "amount", "date", "vendor", "due_date");
            case CONTRACT -> Arrays.asList("contract_number", "party_a", "party_b", "start_date", "end_date");
            case RECEIPT -> Arrays.asList("amount", "date", "merchant", "category");
            case FORM -> Arrays.asList("form_number", "applicant_name", "date");
            default -> List.of();
        };

        return ExtractionConfig.builder()
                .fields(defaultFields)
                .extractTables(type == DocumentType.INVOICE || type == DocumentType.RECEIPT)
                .extractImages(false)
                .build();
    }
}

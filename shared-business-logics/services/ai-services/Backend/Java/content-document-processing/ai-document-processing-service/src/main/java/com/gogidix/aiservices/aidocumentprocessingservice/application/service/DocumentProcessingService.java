package com.gogidix.aiservices.aidocumentprocessingservice.application.service;

import com.gogidix.aiservices.aidocumentprocessingservice.application.dto.request.ProcessDocumentRequest;
import com.gogidix.aiservices.aidocumentprocessingservice.application.dto.response.DocumentProcessingResponse;
import com.gogidix.aiservices.aidocumentprocessingservice.application.dto.response.ProcessingStatusResponse;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.aggregate.DocumentProcessingJob;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.event.*;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.*;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.port.out.DocumentProcessingRepository;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.port.out.OcrEnginePort;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.port.out.EventPublisherPort;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.policy.DocumentProcessingPolicy;
import com.gogidix.aiservices.aidocumentprocessingservice.shared.exception.DocumentNotFoundException;
import com.gogidix.aiservices.aidocumentprocessingservice.shared.exception.DocumentProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentProcessingService {
    private final DocumentProcessingRepository repository;
    private final OcrEnginePort ocrEngine;
    private final EventPublisherPort eventPublisher;
    private final DocumentProcessingPolicy policy;

    private static final int MAX_BATCH_SIZE = 10;
    private static final int MAX_FILE_SIZE_BYTES = 50 * 1024 * 1024;

    public DocumentProcessingResponse processDocument(ProcessDocumentRequest request, String userId) {
        validateRequest(request);

        DocumentProcessingJob job = DocumentProcessingJob.create(
                request.documentUrl(),
                request.documentType(),
                userId
        );

        if (request.extractionConfig() != null) {
            job.setExtractionConfig(request.extractionConfig());
        }

        job.startProcessing();
        job = repository.save(job);

        eventPublisher.publish("document.processing.started", Map.of(
                "jobId", job.getJobId().toString(),
                "documentUrl", request.documentUrl(),
                "documentType", request.documentType().name(),
                "userId", userId
        ));

        try {
            ExtractionConfig config = request.extractionConfig() != null
                    ? request.extractionConfig()
                    : policy.getDefaultExtractionConfig(request.documentType());

            List<ExtractedField> fields = ocrEngine.processDocument(request.documentUrl(), config);

            double avgConfidence = fields.stream()
                    .mapToDouble(ExtractedField::getConfidence)
                    .average()
                    .orElse(0.0);

            job.completeProcessing(fields, 1, avgConfidence);
            job = repository.save(job);

            eventPublisher.publish("document.processing.completed", Map.of(
                    "jobId", job.getJobId().toString(),
                    "fieldsCount", fields.size(),
                    "confidence", avgConfidence,
                    "userId", userId
            ));

            return toResponse(job);
        } catch (Exception e) {
            job.failProcessing(e.getMessage());
            repository.save(job);

            eventPublisher.publish("document.processing.failed", Map.of(
                    "jobId", job.getJobId().toString(),
                    "error", e.getMessage(),
                    "userId", userId
            ));

            throw new DocumentProcessingException("Document processing failed: " + e.getMessage(), e);
        }
    }

    public ProcessingStatusResponse getProcessingStatus(String jobId) {
        DocumentProcessingJob job = repository.findById(jobId)
                .orElseThrow(() -> new DocumentNotFoundException("Processing job not found: " + jobId));

        return toStatusResponse(job);
    }

    public void cancelProcessing(String jobId) {
        DocumentProcessingJob job = repository.findById(jobId)
                .orElseThrow(() -> new DocumentNotFoundException("Processing job not found: " + jobId));

        if (job.getStatus() == ProcessingStatus.COMPLETED || job.getStatus() == ProcessingStatus.FAILED) {
            throw new IllegalStateException("Cannot cancel a job that is already " + job.getStatus());
        }

        job.failProcessing("Processing cancelled by user");
        repository.save(job);

        eventPublisher.publish("document.processing.cancelled", Map.of(
                "jobId", jobId,
                "userId", job.getUserId()
        ));
    }

    public void retryProcessing(String jobId) {
        DocumentProcessingJob job = repository.findById(jobId)
                .orElseThrow(() -> new DocumentNotFoundException("Processing job not found: " + jobId));

        if (job.getStatus() != ProcessingStatus.FAILED) {
            throw new IllegalStateException("Can only retry failed jobs");
        }

        ExtractionConfig config = job.getExtractionConfig() != null
                ? job.getExtractionConfig()
                : ExtractionConfig.builder().build();

        List<ExtractedField> fields = ocrEngine.processDocument(job.getDocumentUrl(), config);

        double avgConfidence = fields.stream()
                .mapToDouble(ExtractedField::getConfidence)
                .average()
                .orElse(0.0);

        job.startProcessing();
        job.completeProcessing(fields, 1, avgConfidence);
        repository.save(job);

        eventPublisher.publish("document.processing.retried", Map.of(
                "jobId", jobId,
                "userId", job.getUserId()
        ));
    }

    public List<DocumentProcessingResponse> processBatch(List<ProcessDocumentRequest> requests, String userId) {
        if (requests.size() > MAX_BATCH_SIZE) {
            throw new IllegalArgumentException("Batch size exceeds maximum of " + MAX_BATCH_SIZE);
        }

        return requests.stream()
                .map(request -> processDocument(request, userId))
                .collect(Collectors.toList());
    }

    public ValidationResult validateJob(DocumentProcessingJob job) {
        if (!policy.hasRequiredFields(job.getDocumentType(), job.getExtractedFields())) {
            return ValidationResult.failure("Missing required fields for " + job.getDocumentType());
        }

        if (!policy.meetsMinimumThreshold(job.getConfidence())) {
            return ValidationResult.failure("Confidence below threshold: " + job.getConfidence());
        }

        return ValidationResult.success();
    }

    private void validateRequest(ProcessDocumentRequest request) {
        String url = request.documentUrl().toLowerCase();
        String format = url.substring(url.lastIndexOf('.') + 1);

        if (!policy.isSupportedFormat(format)) {
            throw new DocumentProcessingException("Unsupported document format: " + format);
        }

        if (!policy.isValidUrl(request.documentUrl())) {
            throw new DocumentProcessingException("Invalid document URL");
        }

        if (!policy.isValidSize(MAX_FILE_SIZE_BYTES)) {
            throw new DocumentProcessingException("Document size exceeds maximum limit of 50MB");
        }
    }

    private DocumentProcessingResponse toResponse(DocumentProcessingJob job) {
        Map<String, Object> extractedData = job.getExtractedFields().stream()
                .collect(Collectors.toMap(
                        ExtractedField::getName,
                        ExtractedField::getValue,
                        (a, b) -> a
                ));

        return DocumentProcessingResponse.builder()
                .processingId(job.getJobId().toString())
                .status(job.getStatus())
                .extractedData(extractedData)
                .confidence(job.getConfidence())
                .pagesProcessed(job.getPagesProcessed())
                .build();
    }

    private ProcessingStatusResponse toStatusResponse(DocumentProcessingJob job) {
        Map<String, Object> extractedData = job.getExtractedFields().stream()
                .collect(Collectors.toMap(
                        ExtractedField::getName,
                        ExtractedField::getValue,
                        (a, b) -> a
                ));

        return ProcessingStatusResponse.builder()
                .processingId(job.getJobId().toString())
                .status(job.getStatus())
                .progress(job.getProgress())
                .pagesProcessed(job.getPagesProcessed())
                .confidence(job.getConfidence())
                .extractedData(extractedData)
                .errorMessage(job.getErrorMessage())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }
}

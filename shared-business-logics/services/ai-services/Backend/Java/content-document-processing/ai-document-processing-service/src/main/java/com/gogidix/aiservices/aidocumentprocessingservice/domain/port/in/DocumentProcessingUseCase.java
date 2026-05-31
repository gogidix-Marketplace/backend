package com.gogidix.aiservices.aidocumentprocessingservice.domain.port.in;

import com.gogidix.aiservices.aidocumentprocessingservice.application.dto.request.ProcessDocumentRequest;
import com.gogidix.aiservices.aidocumentprocessingservice.application.dto.response.DocumentProcessingResponse;
import com.gogidix.aiservices.aidocumentprocessingservice.application.dto.response.ProcessingStatusResponse;

import java.util.List;

/**
 * Input port for document processing use cases.
 * Defines the contract for document processing operations.
 */
public interface DocumentProcessingUseCase {

    /**
     * Process a document with the specified configuration.
     *
     * @param request the document processing request
     * @param userId the user ID requesting processing
     * @return the processing response
     */
    DocumentProcessingResponse processDocument(ProcessDocumentRequest request, String userId);

    /**
     * Get the status of a processing job.
     *
     * @param jobId the job ID
     * @return the status response
     */
    ProcessingStatusResponse getProcessingStatus(String jobId);

    /**
     * Cancel a processing job.
     *
     * @param jobId the job ID to cancel
     */
    void cancelProcessing(String jobId);

    /**
     * Retry a failed processing job.
     *
     * @param jobId the job ID to retry
     */
    void retryProcessing(String jobId);

    /**
     * Process multiple documents in batch.
     *
     * @param requests the list of document processing requests
     * @param userId the user ID requesting processing
     * @return the list of processing responses
     */
    List<DocumentProcessingResponse> processBatch(List<ProcessDocumentRequest> requests, String userId);
}

package com.gogidix.aiservices.aidocumentprocessingservice.domain.port.out;

import com.gogidix.aiservices.aidocumentprocessingservice.domain.aggregate.DocumentProcessingJob;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.ProcessingStatus;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.DocumentType;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DocumentProcessingRepository {
    DocumentProcessingJob save(DocumentProcessingJob job);
    Optional<DocumentProcessingJob> findById(String id);
    List<DocumentProcessingJob> findByUserId(String userId);
    List<DocumentProcessingJob> findByStatus(ProcessingStatus status);
    List<DocumentProcessingJob> findByDocumentType(DocumentType type);
    List<DocumentProcessingJob> findByCreatedAtAfter(Instant timestamp);
    void deleteById(String id);
    void deleteByUserId(String userId);
    List<DocumentProcessingJob> findAll();
    List<DocumentProcessingJob> saveAll(List<DocumentProcessingJob> jobs);
    long countByStatus(ProcessingStatus status);
    Map<String, Long> getUserStatistics(String userId);
}

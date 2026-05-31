package com.gogidix.aiservices.aidocumentprocessingservice.infrastructure.persistence;

import com.gogidix.aiservices.aidocumentprocessingservice.domain.aggregate.DocumentProcessingJob;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.DocumentType;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.ProcessingStatus;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.port.out.DocumentProcessingRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryDocumentJobRepository implements DocumentProcessingRepository {
    private final Map<String, DocumentProcessingJob> storage = new ConcurrentHashMap<>();

    @Override
    public DocumentProcessingJob save(DocumentProcessingJob job) {
        storage.put(job.getJobId().toString(), job);
        return job;
    }

    @Override
    public Optional<DocumentProcessingJob> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<DocumentProcessingJob> findByUserId(String userId) {
        return storage.values().stream()
                .filter(job -> job.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentProcessingJob> findByStatus(ProcessingStatus status) {
        return storage.values().stream()
                .filter(job -> job.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentProcessingJob> findByDocumentType(DocumentType type) {
        return storage.values().stream()
                .filter(job -> job.getDocumentType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentProcessingJob> findByCreatedAtAfter(Instant timestamp) {
        return storage.values().stream()
                .filter(job -> job.getCreatedAt().isAfter(timestamp))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        storage.remove(id);
    }

    @Override
    public void deleteByUserId(String userId) {
        storage.entrySet().removeIf(entry -> entry.getValue().getUserId().equals(userId));
    }

    @Override
    public List<DocumentProcessingJob> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<DocumentProcessingJob> saveAll(List<DocumentProcessingJob> jobs) {
        jobs.forEach(this::save);
        return jobs;
    }

    @Override
    public long countByStatus(ProcessingStatus status) {
        return storage.values().stream()
                .filter(job -> job.getStatus() == status)
                .count();
    }

    @Override
    public Map<String, Long> getUserStatistics(String userId) {
        List<DocumentProcessingJob> userJobs = findByUserId(userId);

        Map<String, Long> stats = new HashMap<>();
        stats.put("total", (long) userJobs.size());
        stats.put("completed", userJobs.stream().filter(j -> j.getStatus() == ProcessingStatus.COMPLETED).count());
        stats.put("failed", userJobs.stream().filter(j -> j.getStatus() == ProcessingStatus.FAILED).count());
        stats.put("processing", userJobs.stream().filter(j -> j.getStatus() == ProcessingStatus.PROCESSING).count());
        stats.put("pending", userJobs.stream().filter(j -> j.getStatus() == ProcessingStatus.PENDING).count());

        return stats;
    }
}

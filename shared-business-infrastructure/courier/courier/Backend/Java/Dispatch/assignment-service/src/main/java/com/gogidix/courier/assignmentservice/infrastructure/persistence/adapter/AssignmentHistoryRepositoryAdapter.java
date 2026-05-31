package com.gogidix.courier.assignmentservice.infrastructure.persistence.adapter;

import com.gogidix.courier.assignmentservice.domain.entity.AssignmentHistory;
import com.gogidix.courier.assignmentservice.domain.repository.AssignmentHistoryRepository;
import com.gogidix.courier.assignmentservice.infrastructure.persistence.repository.MongoAssignmentHistoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

/**
 * MongoDB implementation of AssignmentHistoryRepository.
 */
@Component
public class AssignmentHistoryRepositoryAdapter implements AssignmentHistoryRepository {

    private final MongoAssignmentHistoryRepository mongoRepository;

    public AssignmentHistoryRepositoryAdapter(MongoAssignmentHistoryRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public AssignmentHistory save(AssignmentHistory history) {
        return mongoRepository.save(history);
    }

    @Override
    public List<AssignmentHistory> saveAll(List<AssignmentHistory> historyList) {
        return mongoRepository.saveAll(historyList);
    }

    @Override
    public List<AssignmentHistory> findByAssignmentId(String assignmentId) {
        return mongoRepository.findByAssignmentId(assignmentId);
    }

    @Override
    public List<AssignmentHistory> findByAssignmentIdAndTenantId(String assignmentId, String tenantId) {
        return mongoRepository.findByAssignmentIdAndTenantId(assignmentId, tenantId);
    }

    @Override
    public List<AssignmentHistory> findByDriverIdAndTenantId(String driverId, String tenantId, int limit) {
        List<AssignmentHistory> history = mongoRepository.findByDriverIdAndTenantId(driverId, tenantId);
        return history.stream()
                .sorted((h1, h2) -> h2.getTimestamp().compareTo(h1.getTimestamp()))
                .limit(limit)
                .toList();
    }

    @Override
    public List<AssignmentHistory> findByDispatchIdAndTenantId(String dispatchId, String tenantId) {
        return mongoRepository.findByDispatchIdAndTenantId(dispatchId, tenantId);
    }

    @Override
    public List<AssignmentHistory> findByEventTypeAndTenantId(
            AssignmentHistory.HistoryEventType eventType, String tenantId, int limit) {
        List<AssignmentHistory> history = mongoRepository.findByEventTypeAndTenantId(eventType, tenantId);
        return history.stream()
                .sorted((h1, h2) -> h2.getTimestamp().compareTo(h1.getTimestamp()))
                .limit(limit)
                .toList();
    }

    @Override
    public List<AssignmentHistory> findByTenantIdAndTimestampBetween(
            String tenantId, Instant fromDate, Instant toDate) {
        return mongoRepository.findByTenantIdAndTimestampBetween(tenantId, fromDate, toDate);
    }

    @Override
    public long countByAssignmentId(String assignmentId) {
        return mongoRepository.countByAssignmentId(assignmentId);
    }

    @Override
    public void deleteByAssignmentId(String assignmentId) {
        mongoRepository.deleteByAssignmentId(assignmentId);
    }

    @Override
    public List<AssignmentHistory> findRecentByDriverIdAndTenantId(
            String driverId, String tenantId, int days) {
        Instant since = Instant.now().minusSeconds(days * 24L * 60 * 60);
        return mongoRepository.findRecentByDriverIdAndTenantId(driverId, tenantId, since);
    }
}

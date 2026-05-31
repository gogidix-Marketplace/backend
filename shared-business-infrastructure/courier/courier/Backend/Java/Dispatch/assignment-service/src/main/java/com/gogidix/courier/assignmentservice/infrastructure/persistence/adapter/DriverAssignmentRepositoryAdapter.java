package com.gogidix.courier.assignmentservice.infrastructure.persistence.adapter;

import com.gogidix.courier.assignmentservice.domain.entity.DriverAssignment;
import com.gogidix.courier.assignmentservice.domain.repository.DriverAssignmentRepository;
import com.gogidix.courier.assignmentservice.infrastructure.persistence.repository.MongoDriverAssignmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB implementation of DriverAssignmentRepository.
 */
@Component
public class DriverAssignmentRepositoryAdapter implements DriverAssignmentRepository {

    private final MongoDriverAssignmentRepository mongoRepository;

    public DriverAssignmentRepositoryAdapter(MongoDriverAssignmentRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public DriverAssignment save(DriverAssignment assignment) {
        return mongoRepository.save(assignment);
    }

    @Override
    public List<DriverAssignment> saveAll(List<DriverAssignment> assignments) {
        return mongoRepository.saveAll(assignments);
    }

    @Override
    public Optional<DriverAssignment> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Optional<DriverAssignment> findByIdAndTenantId(String id, String tenantId) {
        return mongoRepository.findById(id)
                .filter(a -> a.getTenantId().equals(tenantId));
    }

    @Override
    public List<DriverAssignment> findByDispatchId(String dispatchId) {
        return mongoRepository.findByDispatchId(dispatchId);
    }

    @Override
    public List<DriverAssignment> findByDispatchIdAndTenantId(String dispatchId, String tenantId) {
        return mongoRepository.findByDispatchIdAndTenantId(dispatchId, tenantId);
    }

    @Override
    public Optional<DriverAssignment> findActiveByDispatchIdAndTenantId(String dispatchId, String tenantId) {
        List<DriverAssignment> activeAssignments = mongoRepository.findActiveByDispatchIdAndTenantId(dispatchId, tenantId);
        return activeAssignments.isEmpty() ? Optional.empty() : Optional.of(activeAssignments.get(0));
    }

    @Override
    public List<DriverAssignment> findByDriverId(String driverId) {
        return mongoRepository.findByDriverIdAndTenantId(driverId, "");
    }

    @Override
    public List<DriverAssignment> findByDriverIdAndTenantId(String driverId, String tenantId) {
        return mongoRepository.findByDriverIdAndTenantId(driverId, tenantId);
    }

    @Override
    public List<DriverAssignment> findActiveByDriverIdAndTenantId(String driverId, String tenantId) {
        return mongoRepository.findActiveByDriverIdAndTenantId(driverId, tenantId);
    }

    @Override
    public List<DriverAssignment> findByStatusAndTenantId(DriverAssignment.AssignmentStatus status, String tenantId) {
        return mongoRepository.findByStatusAndTenantId(status, tenantId);
    }

    @Override
    public List<DriverAssignment> findByStatusAndCreatedAtBeforeAndTenantId(
            DriverAssignment.AssignmentStatus status, Instant timestamp, String tenantId) {
        return mongoRepository.findByStatusAndCreatedAtBeforeAndTenantId(status, timestamp, tenantId);
    }

    @Override
    public List<DriverAssignment> findByTenantId(String tenantId) {
        return mongoRepository.findByTenantId(tenantId);
    }

    @Override
    public List<DriverAssignment> findByTenantIdPaginated(String tenantId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<DriverAssignment> result = mongoRepository.findByTenantId(tenantId, pageable);
        return result.getContent();
    }

    @Override
    public long countByTenantId(String tenantId) {
        return mongoRepository.countByTenantId(tenantId);
    }

    @Override
    public long countByStatusAndTenantId(DriverAssignment.AssignmentStatus status, String tenantId) {
        return mongoRepository.countByStatusAndTenantId(status, tenantId);
    }

    @Override
    public long countActiveByDriverIdAndTenantId(String driverId, String tenantId) {
        return mongoRepository.countActiveByDriverIdAndTenantId(driverId, tenantId);
    }

    @Override
    public boolean existsActiveByDispatchIdAndTenantId(String dispatchId, String tenantId) {
        return mongoRepository.existsActiveByDispatchIdAndTenantId(dispatchId, tenantId);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public List<DriverAssignment> findPendingAssignmentsOlderThan(Instant beforeTimestamp, String tenantId) {
        return mongoRepository.findPendingAssignmentsOlderThan(beforeTimestamp, tenantId);
    }
}

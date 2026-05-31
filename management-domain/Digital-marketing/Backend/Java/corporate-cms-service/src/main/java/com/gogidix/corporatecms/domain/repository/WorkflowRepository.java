package com.gogidix.corporatecms.domain.repository;

import com.gogidix.corporatecms.domain.enums.WorkflowStatus;
import com.gogidix.corporatecms.domain.model.Workflow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Workflow entity.
 */
@Repository
public interface WorkflowRepository extends MongoRepository<Workflow, String> {

    Optional<Workflow> findByContentId(String contentId);

    List<Workflow> findByStatus(WorkflowStatus status);

    Page<Workflow> findByStatus(WorkflowStatus status, Pageable pageable);

    List<Workflow> findByRequestedBy(String requestedBy);

    @Query("{'steps.approverId': ?0}")
    List<Workflow> findPendingApprovalsForUser(String userId);

    @Query("{'steps.approverId': ?0, 'status': ?1}")
    List<Workflow> findPendingApprovalsForUserByStatus(String userId, WorkflowStatus status);

    @Query("{'currentApproverId': ?0, 'status': ?1}")
    List<Workflow> findByCurrentApproverIdAndStatus(String approverId, WorkflowStatus status);

    @Query("{'dueDate': {$lt: ?0, $ne: null}, 'status': {$nin: ['APPROVED', 'REJECTED', 'CANCELLED']}}")
    List<Workflow> findOverdueWorkflows(LocalDateTime now);

    @Query("{'dueDate': {$gte: ?0, $lt: ?1}, 'status': {$nin: ['APPROVED', 'REJECTED', 'CANCELLED']}}")
    List<Workflow> findWorkflowsDueBetween(LocalDateTime start, LocalDateTime end);

    Long countByStatus(WorkflowStatus status);

    @Query("{'requestedBy': ?0, 'status': ?1}")
    Long countByRequestedByAndStatus(String requestedBy, WorkflowStatus status);
}

package com.gogidix.hr.employeeselfservice.domain.repository;

import com.gogidix.hr.employeeselfservice.domain.model.SelfServiceRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Self Service Request Repository Interface (Port)
 * Defines the contract for self-service request persistence operations
 */
public interface SelfServiceRequestRepository {

    SelfServiceRequest save(SelfServiceRequest request);

    List<SelfServiceRequest> saveAll(List<SelfServiceRequest> requests);

    Optional<SelfServiceRequest> findById(String id);

    Optional<SelfServiceRequest> findByRequestNumberAndTenantId(String requestNumber, String tenantId);

    List<SelfServiceRequest> findByTenantId(String tenantId);

    List<SelfServiceRequest> findByEmployeeIdAndTenantId(String employeeId, String tenantId);

    List<SelfServiceRequest> findByTenantIdAndStatus(String tenantId, SelfServiceRequest.RequestStatus status);

    List<SelfServiceRequest> findByEmployeeIdAndTenantIdAndStatus(String employeeId, String tenantId,
                                                                    SelfServiceRequest.RequestStatus status);

    List<SelfServiceRequest> findByTenantIdAndType(String tenantId, SelfServiceRequest.RequestType type);

    List<SelfServiceRequest> findByTenantIdAndCategory(String tenantId, String category);

    List<SelfServiceRequest> findByTenantIdAndAssignedTo(String tenantId, String assignedTo);

    List<SelfServiceRequest> findByTenantIdAndReviewedBy(String tenantId, String reviewedBy);

    List<SelfServiceRequest> findByTenantIdAndPriorityGreaterThanEqual(String tenantId, Integer priority);

    List<SelfServiceRequest> findByTenantIdAndDueDateBefore(String tenantId, LocalDate date);

    List<SelfServiceRequest> findByTenantIdAndSubmittedDateBetween(String tenantId, LocalDate startDate,
                                                                     LocalDate endDate);

    List<SelfServiceRequest> findByTenantIdAndStatusIn(String tenantId, List<SelfServiceRequest.RequestStatus> statuses);

    List<SelfServiceRequest> searchByDescription(String tenantId, String searchTerm);

    List<SelfServiceRequest> searchByEmployeeName(String tenantId, String searchTerm);

    List<SelfServiceRequest> findByTenantIdAndSupportingDocumentsContaining(String tenantId, String documentUrl);

    boolean existsByRequestNumberAndTenantId(String requestNumber, String tenantId);

    void deleteById(String id);

    void deleteByRequestNumberAndTenantId(String requestNumber, String tenantId);

    void deleteAllByTenantId(String tenantId);

    void deleteByEmployeeIdAndTenantId(String employeeId, String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, SelfServiceRequest.RequestStatus status);

    long countByEmployeeIdAndTenantId(String employeeId, String tenantId);

    long countByTenantIdAndType(String tenantId, SelfServiceRequest.RequestType type);

    List<SelfServiceRequest> findPendingRequestsByReviewer(String tenantId, String reviewerId);
}

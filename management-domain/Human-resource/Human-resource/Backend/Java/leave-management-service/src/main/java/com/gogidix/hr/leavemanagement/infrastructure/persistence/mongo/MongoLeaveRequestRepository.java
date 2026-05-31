package com.gogidix.hr.leavemanagement.infrastructure.persistence.mongo;

import com.gogidix.hr.leavemanagement.domain.enums.LeaveStatus;
import com.gogidix.hr.leavemanagement.domain.enums.LeaveType;
import com.gogidix.hr.leavemanagement.domain.model.LeaveRequest;
import com.gogidix.hr.leavemanagement.domain.repository.LeaveRequestRepository;
import com.gogidix.hr.leavemanagement.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - LeaveRequest
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoLeaveRequestRepository implements LeaveRequestRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public LeaveRequest save(LeaveRequest request) {
        return mongoTemplate.save(request);
    }

    @Override
    public List<LeaveRequest> saveAll(List<LeaveRequest> requests) {
        return requests.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<LeaveRequest> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(Criteria.where("id").is(id).and("tenantId").is(tenantId));
        return Optional.ofNullable(mongoTemplate.findOne(query, LeaveRequest.class));
    }

    @Override
    public Optional<LeaveRequest> findByRequestIdAndTenantId(String requestId, String tenantId) {
        Query query = Query.query(Criteria.where("requestId").is(requestId).and("tenantId").is(tenantId));
        return Optional.ofNullable(mongoTemplate.findOne(query, LeaveRequest.class));
    }

    @Override
    public List<LeaveRequest> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, LeaveRequest.class);
    }

    @Override
    public Page<LeaveRequest> findByTenantId(String tenantId, Pageable pageable) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId)).with(pageable);
        List<LeaveRequest> requests = mongoTemplate.find(query, LeaveRequest.class);
        long count = mongoTemplate.count(Query.query(Criteria.where("tenantId").is(tenantId)), LeaveRequest.class);
        return new PageImpl<>(requests, pageable, count);
    }

    @Override
    public List<LeaveRequest> findByTenantIdAndEmployeeId(String tenantId, String employeeId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("employeeId").is(employeeId));
        return mongoTemplate.find(query, LeaveRequest.class);
    }

    @Override
    public List<LeaveRequest> findByTenantIdAndEmployeeIdOrderBySubmissionDateDesc(String tenantId, String employeeId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("employeeId").is(employeeId))
                .with(org.springframework.data.domain.Sort.by(
                        org.springframework.data.domain.Sort.Direction.DESC, "submissionDate"));
        return mongoTemplate.find(query, LeaveRequest.class);
    }

    @Override
    public List<LeaveRequest> findByTenantIdAndStatus(String tenantId, LeaveStatus status) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("status").is(status));
        return mongoTemplate.find(query, LeaveRequest.class);
    }

    @Override
    public Page<LeaveRequest> findByTenantIdAndStatus(String tenantId, LeaveStatus status, Pageable pageable) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("status").is(status))
                .with(pageable);
        List<LeaveRequest> requests = mongoTemplate.find(query, LeaveRequest.class);
        long count = mongoTemplate.count(
                Query.query(Criteria.where("tenantId").is(tenantId).and("status").is(status)),
                LeaveRequest.class);
        return new PageImpl<>(requests, pageable, count);
    }

    @Override
    public List<LeaveRequest> findByTenantIdAndLeaveType(String tenantId, LeaveType leaveType) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("leaveType").is(leaveType));
        return mongoTemplate.find(query, LeaveRequest.class);
    }

    @Override
    public List<LeaveRequest> findByTenantIdAndEmployeeIdAndStatus(String tenantId, String employeeId, LeaveStatus status) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId)
                .and("employeeId").is(employeeId)
                .and("status").is(status));
        return mongoTemplate.find(query, LeaveRequest.class);
    }

    @Override
    public List<LeaveRequest> findByTenantIdAndManagerId(String tenantId, String managerId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("managerId").is(managerId));
        return mongoTemplate.find(query, LeaveRequest.class);
    }

    @Override
    public List<LeaveRequest> findPendingApprovalForManager(String tenantId, String managerId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId)
                .and("managerId").is(managerId)
                .and("status").is(LeaveStatus.PENDING));
        return mongoTemplate.find(query, LeaveRequest.class);
    }

    @Override
    public List<LeaveRequest> findByTenantIdAndStartDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId)
                .and("startDate").gte(startDate).lte(endDate));
        return mongoTemplate.find(query, LeaveRequest.class);
    }

    @Override
    public List<LeaveRequest> findByTenantIdAndEndDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId)
                .and("endDate").gte(startDate).lte(endDate));
        return mongoTemplate.find(query, LeaveRequest.class);
    }

    @Override
    public List<LeaveRequest> findByTenantIdAndEmployeeIdAndLeaveTypeAndYear(String tenantId, String employeeId, LeaveType leaveType, String year) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId)
                .and("employeeId").is(employeeId)
                .and("leaveType").is(leaveType)
                .and("year").is(year));
        return mongoTemplate.find(query, LeaveRequest.class);
    }

    @Override
    public List<LeaveRequest> findOverlappingRequests(String tenantId, String employeeId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId)
                .and("employeeId").is(employeeId)
                .and("status").in(List.of(LeaveStatus.PENDING, LeaveStatus.APPROVED))
                .orOperator(
                        Criteria.where("startDate").lte(endDate).and("endDate").gte(startDate),
                        Criteria.where("startDate").gte(startDate).and("startDate").lte(endDate),
                        Criteria.where("endDate").gte(startDate).and("endDate").lte(endDate)
                ));
        return mongoTemplate.find(query, LeaveRequest.class);
    }

    @Override
    public List<LeaveRequest> findPendingCancellationRequests(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId)
                .and("status").is(LeaveStatus.APPROVED)
                .and("cancellationReason").exists(true));
        return mongoTemplate.find(query, LeaveRequest.class);
    }

    @Override
    public Page<LeaveRequest> searchRequests(String tenantId, String searchTerm, Pageable pageable) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId)
                .orOperator(
                        Criteria.where("employeeName").regex(searchTerm, "i"),
                        Criteria.where("requestId").regex(searchTerm, "i"),
                        Criteria.where("reason").regex(searchTerm, "i")
                )).with(pageable);
        List<LeaveRequest> requests = mongoTemplate.find(query, LeaveRequest.class);
        return new PageImpl<>(requests, pageable, requests.size());
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, LeaveRequest.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, LeaveStatus status) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("status").is(status));
        return mongoTemplate.count(query, LeaveRequest.class);
    }

    @Override
    public long countByTenantIdAndEmployeeIdAndStatus(String tenantId, String employeeId, LeaveStatus status) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId)
                .and("employeeId").is(employeeId)
                .and("status").is(status));
        return mongoTemplate.count(query, LeaveRequest.class);
    }

    @Override
    public boolean existsOverlappingRequest(String tenantId, String employeeId, LocalDate startDate, LocalDate endDate, String excludeRequestId) {
        Criteria baseCriteria = Criteria.where("tenantId").is(tenantId)
                .and("employeeId").is(employeeId)
                .and("status").in(List.of(LeaveStatus.PENDING, LeaveStatus.APPROVED));

        if (excludeRequestId != null) {
            baseCriteria.and("requestId").ne(excludeRequestId);
        }

        Query query = Query.query(baseCriteria.orOperator(
                Criteria.where("startDate").lte(endDate).and("endDate").gte(startDate),
                Criteria.where("startDate").gte(startDate).and("startDate").lte(endDate),
                Criteria.where("endDate").gte(startDate).and("endDate").lte(endDate)
        ));

        return mongoTemplate.exists(query, LeaveRequest.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), LeaveRequest.class);
    }

    @Override
    public void deleteByRequestIdAndTenantId(String requestId, String tenantId) {
        Query query = Query.query(Criteria.where("requestId").is(requestId).and("tenantId").is(tenantId));
        mongoTemplate.remove(query, LeaveRequest.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, LeaveRequest.class);
    }

    @Override
    public List<LeaveRequest> findActiveLeaveRequestsForEmployee(String tenantId, String employeeId, LocalDate date) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId)
                .and("employeeId").is(employeeId)
                .and("status").is(LeaveStatus.APPROVED)
                .and("startDate").lte(date)
                .and("endDate").gte(date));
        return mongoTemplate.find(query, LeaveRequest.class);
    }

    @Override
    public List<LeaveRequest> findByTenantIdAndDepartment(String tenantId, String department) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId).and("department").is(department));
        return mongoTemplate.find(query, LeaveRequest.class);
    }
}

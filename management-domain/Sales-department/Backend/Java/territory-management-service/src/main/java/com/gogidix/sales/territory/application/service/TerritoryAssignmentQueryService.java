package com.gogidix.sales.territory.application.service;

import com.gogidix.sales.territory.domain.model.TerritoryAssignment;
import com.gogidix.sales.territory.domain.repository.TerritoryAssignmentRepository;
import com.gogidix.sales.territory.shared.exception.NotFoundException;
import com.gogidix.sales.territory.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Territory Assignment Query Service
 * Handles all read operations for territory assignments
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TerritoryAssignmentQueryService {

    private final TerritoryAssignmentRepository assignmentRepository;
    private final MongoTemplate mongoTemplate;

    public TerritoryAssignment getById(String assignmentId) {
        String tenantId = RequestContextHolder.getTenantId();
        return assignmentRepository.findByAssignmentIdAndTenantId(assignmentId, tenantId)
            .orElseThrow(() -> new NotFoundException("Assignment", assignmentId));
    }

    public List<TerritoryAssignment> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return assignmentRepository.findByTenantId(tenantId);
    }

    public List<TerritoryAssignment> getByTerritoryId(String territoryId) {
        String tenantId = RequestContextHolder.getTenantId();
        return assignmentRepository.findByTenantIdAndTerritoryId(tenantId, territoryId);
    }

    public List<TerritoryAssignment> getActiveByTerritoryId(String territoryId) {
        String tenantId = RequestContextHolder.getTenantId();
        return assignmentRepository.findActiveByTenantIdAndTerritoryId(tenantId, territoryId);
    }

    public List<TerritoryAssignment> getBySalesRepresentativeId(String salesRepresentativeId) {
        String tenantId = RequestContextHolder.getTenantId();
        return assignmentRepository.findByTenantIdAndSalesRepresentativeId(tenantId, salesRepresentativeId);
    }

    public List<TerritoryAssignment> getActiveBySalesRepresentativeId(String salesRepresentativeId) {
        String tenantId = RequestContextHolder.getTenantId();
        return assignmentRepository.findActiveByTenantIdAndSalesRepresentativeId(tenantId, salesRepresentativeId);
    }

    public TerritoryAssignment getPrimaryBySalesRepresentativeId(String salesRepresentativeId) {
        String tenantId = RequestContextHolder.getTenantId();
        return assignmentRepository.findPrimaryByTenantIdAndSalesRepresentativeId(tenantId, salesRepresentativeId)
            .orElseThrow(() -> new NotFoundException("Primary Assignment", salesRepresentativeId));
    }

    public List<TerritoryAssignment> getByStatus(TerritoryAssignment.AssignmentStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        return assignmentRepository.findByTenantIdAndStatus(tenantId, status);
    }

    public List<TerritoryAssignment> getByEffectiveDateRange(LocalDate startDate, LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();
        return assignmentRepository.findByTenantIdAndEffectiveDateBetween(tenantId, startDate, endDate);
    }

    public List<TerritoryAssignment> getPrimaryAssignmentsByTerritoryId(String territoryId) {
        String tenantId = RequestContextHolder.getTenantId();
        return assignmentRepository.findPrimaryAssignmentsByTenantIdAndTerritoryId(tenantId, territoryId);
    }

    public AssignmentSummary getSummary() {
        String tenantId = RequestContextHolder.getTenantId();

        long totalAssignments = assignmentRepository.countByTenantId(tenantId);
        List<TerritoryAssignment> allAssignments = assignmentRepository.findByTenantId(tenantId);

        long activeAssignments = allAssignments.stream()
            .filter(a -> a.getStatus() == TerritoryAssignment.AssignmentStatus.ACTIVE)
            .count();

        Map<TerritoryAssignment.AssignmentType, Long> countByType = allAssignments.stream()
            .collect(Collectors.groupingBy(TerritoryAssignment::getType, Collectors.counting()));

        Map<TerritoryAssignment.AssignmentStatus, Long> countByStatus = allAssignments.stream()
            .collect(Collectors.groupingBy(TerritoryAssignment::getStatus, Collectors.counting()));

        return AssignmentSummary.builder()
            .totalAssignments(totalAssignments)
            .activeAssignments(activeAssignments)
            .countByType(countByType)
            .countByStatus(countByStatus)
            .build();
    }

    public Page<TerritoryAssignment> searchAssignments(String territoryId, String salesRepresentativeId,
                                                         TerritoryAssignment.AssignmentStatus status,
                                                         Pageable pageable) {
        String tenantId = RequestContextHolder.getTenantId();

        Criteria criteria = Criteria.where("tenantId").is(tenantId);

        if (territoryId != null && !territoryId.isEmpty()) {
            criteria = criteria.and("territoryId").is(territoryId);
        }

        if (salesRepresentativeId != null && !salesRepresentativeId.isEmpty()) {
            criteria = criteria.and("salesRepresentativeId").is(salesRepresentativeId);
        }

        if (status != null) {
            criteria = criteria.and("status").is(status);
        }

        Query query = Query.query(criteria).with(pageable);
        List<TerritoryAssignment> results = mongoTemplate.find(query, TerritoryAssignment.class);

        long count = mongoTemplate.count(Query.query(criteria), TerritoryAssignment.class);

        return new PageImpl<>(results, pageable, count);
    }

    /**
     * Assignment Summary DTO
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class AssignmentSummary {
        private Long totalAssignments;
        private Long activeAssignments;
        private Map<TerritoryAssignment.AssignmentType, Long> countByType;
        private Map<TerritoryAssignment.AssignmentStatus, Long> countByStatus;
    }
}

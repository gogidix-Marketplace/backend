package com.gogidix.courier.assignmentservice.application.service;

import com.gogidix.courier.assignmentservice.application.command.AcceptAssignmentCommand;
import com.gogidix.courier.assignmentservice.application.command.AssignDriverCommand;
import com.gogidix.courier.assignmentservice.application.command.CancelAssignmentCommand;
import com.gogidix.courier.assignmentservice.application.command.CompleteAssignmentCommand;
import com.gogidix.courier.assignmentservice.application.command.CreateAssignmentCommand;
import com.gogidix.courier.assignmentservice.application.command.StartAssignmentCommand;
import com.gogidix.courier.assignmentservice.application.dto.*;
import com.gogidix.courier.assignmentservice.application.mapper.AssignmentMapper;
import com.gogidix.courier.assignmentservice.application.query.AssignmentQuery;
import com.gogidix.courier.assignmentservice.domain.entity.AssignmentHistory;
import com.gogidix.courier.assignmentservice.domain.entity.DriverAssignment;
import com.gogidix.courier.assignmentservice.domain.event.AssignmentCancelledEvent;
import com.gogidix.courier.assignmentservice.domain.event.AssignmentCompletedEvent;
import com.gogidix.courier.assignmentservice.domain.event.AssignmentCreatedEvent;
import com.gogidix.courier.assignmentservice.domain.event.DriverAssignedEvent;
import com.gogidix.courier.assignmentservice.domain.repository.AssignmentHistoryRepository;
import com.gogidix.courier.assignmentservice.domain.repository.DriverAssignmentRepository;
import com.gogidix.courier.assignmentservice.shared.exception.NotFoundException;
import com.gogidix.courier.assignmentservice.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Application service for driver assignment operations.
 */
@Service
@Transactional(readOnly = true)
public class AssignmentApplicationService {

    private static final Logger log = LoggerFactory.getLogger(AssignmentApplicationService.class);
    private static final String CACHE_NAME = "assignments";

    private final DriverAssignmentRepository repository;
    private final AssignmentHistoryRepository historyRepository;
    private final AssignmentMapper mapper;
    private final AssignmentEventPublisher eventPublisher;

    public AssignmentApplicationService(
            DriverAssignmentRepository repository,
            AssignmentHistoryRepository historyRepository,
            AssignmentMapper mapper,
            AssignmentEventPublisher eventPublisher) {
        this.repository = repository;
        this.historyRepository = historyRepository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Create a new driver assignment.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public AssignmentResponse createAssignment(AssignmentRequest request, String userId) {
        log.info("Creating assignment for dispatch: {} to driver: {}", request.dispatchId(), request.driverId());

        // Check if active assignment already exists for this dispatch
        if (repository.existsActiveByDispatchIdAndTenantId(request.dispatchId(), request.tenantId())) {
            throw new ValidationException("Active assignment already exists for dispatch: " + request.dispatchId());
        }

        DriverAssignment assignment = mapper.toEntity(request);
        assignment.validate();
        assignment.setAssignedAt(Instant.now());

        DriverAssignment saved = repository.save(assignment);

        // Create history record
        AssignmentHistory history = AssignmentHistory.forAssignmentCreated(
                saved.getTenantId(), saved.getId(), saved.getDriverId(),
                saved.getDispatchId(), userId
        );
        historyRepository.save(history);

        // Publish domain events
        eventPublisher.publish(new AssignmentCreatedEvent(
                saved.getId(),
                saved.getTenantId(),
                saved.getDispatchId(),
                saved.getDriverId(),
                saved.getStatus(),
                saved.getPriority()
        ));
        eventPublisher.publish(new DriverAssignedEvent(
                saved.getId(),
                saved.getTenantId(),
                saved.getDispatchId(),
                saved.getDriverId(),
                saved.getAssignmentReason(),
                saved.getAssignmentScore()
        ));

        log.info("Assignment created with ID: {}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Get an assignment by ID.
     */
    @Cacheable(value = CACHE_NAME, key = "#id")
    public AssignmentResponse getAssignmentById(String id) {
        log.debug("Fetching assignment: {}", id);

        DriverAssignment assignment = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Assignment", id));

        return mapper.toResponseDto(assignment);
    }

    /**
     * Get an assignment by ID and tenant ID.
     */
    @Cacheable(value = CACHE_NAME, key = "'tenant:' + #tenantId + ':' + #id")
    public AssignmentResponse getAssignmentByIdAndTenantId(String id, String tenantId) {
        log.debug("Fetching assignment: {} for tenant: {}", id, tenantId);

        DriverAssignment assignment = repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new NotFoundException("Assignment", id));

        return mapper.toResponseDto(assignment);
    }

    /**
     * Get assignments by dispatch ID.
     */
    public List<AssignmentResponse> getAssignmentsByDispatchId(String dispatchId, String tenantId) {
        log.debug("Fetching assignments for dispatch: {}", dispatchId);

        List<DriverAssignment> assignments = repository.findByDispatchIdAndTenantId(dispatchId, tenantId);
        return mapper.toResponseDtoList(assignments);
    }

    /**
     * Get active assignment by dispatch ID.
     */
    public AssignmentResponse getActiveAssignmentByDispatchId(String dispatchId, String tenantId) {
        log.debug("Fetching active assignment for dispatch: {}", dispatchId);

        DriverAssignment assignment = repository.findActiveByDispatchIdAndTenantId(dispatchId, tenantId)
                .orElseThrow(() -> new NotFoundException("Active Assignment", dispatchId));

        return mapper.toResponseDto(assignment);
    }

    /**
     * Get assignments by driver ID.
     */
    public List<AssignmentResponse> getAssignmentsByDriverId(String driverId, String tenantId) {
        log.debug("Fetching assignments for driver: {}", driverId);

        List<DriverAssignment> assignments = repository.findByDriverIdAndTenantId(driverId, tenantId);
        return mapper.toResponseDtoList(assignments);
    }

    /**
     * Get active assignments by driver ID.
     */
    public List<AssignmentResponse> getActiveAssignmentsByDriverId(String driverId, String tenantId) {
        log.debug("Fetching active assignments for driver: {}", driverId);

        List<DriverAssignment> assignments = repository.findActiveByDriverIdAndTenantId(driverId, tenantId);
        return mapper.toResponseDtoList(assignments);
    }

    /**
     * List assignments with pagination and filtering.
     */
    public PagedResponseDto<AssignmentResponse> listAssignments(AssignmentQuery query) {
        log.debug("Listing assignments - page: {}, size: {}", query.page(), query.size());

        List<DriverAssignment> allAssignments = repository.findByTenantId(query.dispatchId() != null ? "" : "default");

        // Apply filters
        List<DriverAssignment> filtered = allAssignments.stream()
                .filter(a -> query.dispatchId() == null || a.getDispatchId().equals(query.dispatchId()))
                .filter(a -> query.driverId() == null || a.getDriverId().equals(query.driverId()))
                .filter(a -> query.status() == null || a.getStatus() == query.status())
                .filter(a -> query.priority() == null || a.getPriority() == query.priority())
                .filter(a -> !query.activeOnly() || !a.isTerminal())
                .filter(a -> query.createdAfter() == null || !a.getCreatedAt().isBefore(query.createdAfter()))
                .filter(a -> query.createdBefore() == null || !a.getCreatedAt().isAfter(query.createdBefore()))
                .collect(Collectors.toList());

        // Apply sorting
        Sort sort = Sort.by(query.sortDirection().equalsIgnoreCase("ASC") ?
                Sort.Direction.ASC : Sort.Direction.DESC, query.sortBy());
        int start = query.page() * query.size();
        int end = Math.min(start + query.size(), filtered.size());

        List<DriverAssignment> pagedAssignments = new ArrayList<>();
        if (start < filtered.size()) {
            pagedAssignments = filtered.subList(start, end);
        }

        List<AssignmentResponse> responses = mapper.toResponseDtoList(pagedAssignments);

        return PagedResponseDto.of(responses, query.page(), query.size(), filtered.size());
    }

    /**
     * Assign a driver to an assignment (reassignment).
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#command.assignmentId")
    public AssignmentResponse assignDriver(AssignDriverCommand command, String userId) {
        log.info("Assigning driver {} to assignment: {}", command.driverId(), command.assignmentId());

        DriverAssignment assignment = repository.findById(command.assignmentId())
                .orElseThrow(() -> new NotFoundException("Assignment", command.assignmentId()));

        String previousDriverId = assignment.getDriverId();
        assignment.reassign(command.driverId(), command.reason());

        if (command.assignmentScore() != null) {
            assignment.updateScore(command.assignmentScore());
        }

        DriverAssignment saved = repository.save(assignment);

        // Create history record
        AssignmentHistory history = AssignmentHistory.forReassignment(
                saved.getTenantId(), saved.getId(), previousDriverId,
                saved.getDriverId(), saved.getDispatchId(), command.reason()
        );
        historyRepository.save(history);

        // Publish domain event
        eventPublisher.publish(new DriverAssignedEvent(
                saved.getId(),
                saved.getTenantId(),
                saved.getDispatchId(),
                saved.getDriverId(),
                command.reason(),
                command.assignmentScore()
        ));

        log.info("Driver reassigned for assignment: {}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Accept an assignment.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#command.assignmentId")
    public AssignmentResponse acceptAssignment(AcceptAssignmentCommand command) {
        log.info("Accepting assignment: {}", command.assignmentId());

        DriverAssignment assignment = repository.findById(command.assignmentId())
                .orElseThrow(() -> new NotFoundException("Assignment", command.assignmentId()));

        assignment.accept();
        DriverAssignment saved = repository.save(assignment);

        // Create history record
        AssignmentHistory history = AssignmentHistory.forStatusChange(
                saved.getTenantId(), saved.getId(), saved.getDriverId(),
                saved.getDispatchId(), DriverAssignment.AssignmentStatus.PENDING,
                DriverAssignment.AssignmentStatus.ACCEPTED, command.acceptedBy()
        );
        historyRepository.save(history);

        log.info("Assignment accepted: {}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Start an assignment.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#command.assignmentId")
    public AssignmentResponse startAssignment(StartAssignmentCommand command) {
        log.info("Starting assignment: {}", command.assignmentId());

        DriverAssignment assignment = repository.findById(command.assignmentId())
                .orElseThrow(() -> new NotFoundException("Assignment", command.assignmentId()));

        assignment.start();
        DriverAssignment saved = repository.save(assignment);

        // Create history record
        AssignmentHistory history = AssignmentHistory.forStatusChange(
                saved.getTenantId(), saved.getId(), saved.getDriverId(),
                saved.getDispatchId(), DriverAssignment.AssignmentStatus.ACCEPTED,
                DriverAssignment.AssignmentStatus.IN_PROGRESS, command.startedBy()
        );
        historyRepository.save(history);

        log.info("Assignment started: {}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Complete an assignment.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#command.assignmentId")
    public AssignmentResponse completeAssignment(CompleteAssignmentCommand command) {
        log.info("Completing assignment: {}", command.assignmentId());

        DriverAssignment assignment = repository.findById(command.assignmentId())
                .orElseThrow(() -> new NotFoundException("Assignment", command.assignmentId()));

        assignment.complete(command.actualDistanceKm(), command.actualDurationMinutes());
        DriverAssignment saved = repository.save(assignment);

        // Create history record
        AssignmentHistory history = AssignmentHistory.forCompletion(
                saved.getTenantId(), saved.getId(), saved.getDriverId(),
                saved.getDispatchId(), command.actualDistanceKm(), command.actualDurationMinutes()
        );
        historyRepository.save(history);

        // Publish domain event
        eventPublisher.publish(new AssignmentCompletedEvent(
                saved.getId(),
                saved.getTenantId(),
                saved.getDispatchId(),
                saved.getDriverId(),
                command.actualDistanceKm(),
                command.actualDurationMinutes()
        ));

        log.info("Assignment completed: {}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Cancel an assignment.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#command.assignmentId")
    public AssignmentResponse cancelAssignment(CancelAssignmentCommand command) {
        log.info("Cancelling assignment: {}", command.assignmentId());

        DriverAssignment assignment = repository.findById(command.assignmentId())
                .orElseThrow(() -> new NotFoundException("Assignment", command.assignmentId()));

        assignment.cancel(command.cancellationReason());
        DriverAssignment saved = repository.save(assignment);

        // Create history record
        AssignmentHistory history = AssignmentHistory.forCancellation(
                saved.getTenantId(), saved.getId(), saved.getDriverId(),
                saved.getDispatchId(), command.cancellationReason(), command.cancelledBy()
        );
        historyRepository.save(history);

        // Publish domain event
        eventPublisher.publish(new AssignmentCancelledEvent(
                saved.getId(),
                saved.getTenantId(),
                saved.getDispatchId(),
                saved.getDriverId(),
                command.cancellationReason(),
                command.cancelledBy()
        ));

        log.info("Assignment cancelled: {}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    /**
     * Get assignment statistics.
     */
    public AssignmentStats getAssignmentStats(String tenantId) {
        log.debug("Fetching assignment stats for tenant: {}", tenantId);

        List<DriverAssignment> allAssignments = repository.findByTenantId(tenantId);

        long total = allAssignments.size();
        long active = allAssignments.stream().filter(a -> !a.isTerminal()).count();
        long completed = allAssignments.stream()
                .filter(a -> a.getStatus() == DriverAssignment.AssignmentStatus.COMPLETED).count();
        long cancelled = allAssignments.stream()
                .filter(a -> a.getStatus() == DriverAssignment.AssignmentStatus.CANCELLED).count();
        long failed = allAssignments.stream()
                .filter(a -> a.getStatus() == DriverAssignment.AssignmentStatus.FAILED).count();
        long pending = allAssignments.stream()
                .filter(a -> a.getStatus() == DriverAssignment.AssignmentStatus.PENDING).count();

        DoubleSummaryStatistics scoreStats = allAssignments.stream()
                .filter(a -> a.getAssignmentScore() != null)
                .mapToDouble(DriverAssignment::getAssignmentScore)
                .summaryStatistics();

        DoubleSummaryStatistics timeStats = allAssignments.stream()
                .filter(a -> a.getActualDurationMinutes() != null)
                .mapToInt(DriverAssignment::getActualDurationMinutes)
                .asDoubleStream()
                .summaryStatistics();

        DoubleSummaryStatistics distanceStats = allAssignments.stream()
                .filter(a -> a.getActualDistanceKm() != null)
                .mapToDouble(DriverAssignment::getActualDistanceKm)
                .summaryStatistics();

        // Group by status
        Map<String, Long> byStatus = allAssignments.stream()
                .collect(Collectors.groupingBy(a -> a.getStatus().name(), Collectors.counting()));

        // Group by priority
        Map<String, Long> byPriority = allAssignments.stream()
                .collect(Collectors.groupingBy(a -> a.getPriority().name(), Collectors.counting()));

        // Driver stats
        Map<String, AssignmentStats.DriverStatEntry> driverStats = allAssignments.stream()
                .collect(Collectors.groupingBy(
                        DriverAssignment::getDriverId,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> {
                                    long driverTotal = list.size();
                                    long driverActive = list.stream().filter(a -> !a.isTerminal()).count();
                                    long driverCompleted = list.stream()
                                            .filter(a -> a.getStatus() == DriverAssignment.AssignmentStatus.COMPLETED).count();
                                    long driverCancelled = list.stream()
                                            .filter(a -> a.getStatus() == DriverAssignment.AssignmentStatus.CANCELLED).count();

                                    DoubleSummaryStatistics driverScoreStats = list.stream()
                                            .filter(a -> a.getAssignmentScore() != null)
                                            .mapToDouble(DriverAssignment::getAssignmentScore)
                                            .summaryStatistics();

                                    DoubleSummaryStatistics driverTimeStats = list.stream()
                                            .filter(a -> a.getActualDurationMinutes() != null)
                                            .mapToInt(DriverAssignment::getActualDurationMinutes)
                                            .asDoubleStream()
                                            .summaryStatistics();

                                    return new AssignmentStats.DriverStatEntry(
                                            driverTotal,
                                            driverActive,
                                            driverCompleted,
                                            driverCancelled,
                                            driverScoreStats.getCount() > 0 ? driverScoreStats.getAverage() : null,
                                            driverTimeStats.getCount() > 0 ? driverTimeStats.getAverage() : null
                                    );
                                }
                        )
                ));

        return new AssignmentStats(
                total,
                active,
                completed,
                cancelled,
                failed,
                pending,
                scoreStats.getCount() > 0 ? scoreStats.getAverage() : null,
                timeStats.getCount() > 0 ? timeStats.getAverage() : null,
                distanceStats.getCount() > 0 ? distanceStats.getAverage() : null,
                byStatus,
                byPriority,
                driverStats,
                Instant.now()
        );
    }

    /**
     * Delete an assignment by ID.
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#id")
    public void deleteAssignment(String id) {
        log.info("Deleting assignment: {}", id);

        DriverAssignment assignment = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Assignment", id));

        // Only allow deletion of terminal assignments
        if (!assignment.isTerminal()) {
            throw new ValidationException("Cannot delete active assignment. Cancel it first.");
        }

        repository.deleteById(id);
        historyRepository.deleteByAssignmentId(id);

        log.info("Assignment deleted: {}", id);
    }

    /**
     * Count all assignments for a tenant.
     */
    public long countAssignments(String tenantId) {
        return repository.countByTenantId(tenantId);
    }

    /**
     * Count assignments by status for a tenant.
     */
    public long countAssignmentsByStatus(DriverAssignment.AssignmentStatus status, String tenantId) {
        return repository.countByStatusAndTenantId(status, tenantId);
    }
}

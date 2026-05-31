package com.gogidix.sales.territory.application.service;

import com.gogidix.sales.territory.domain.model.Territory;
import com.gogidix.sales.territory.domain.model.TerritoryAssignment;
import com.gogidix.sales.territory.domain.port.in.TerritoryAssignmentCommand;
import com.gogidix.sales.territory.domain.port.out.EventPublisher;
import com.gogidix.sales.territory.domain.repository.TerritoryAssignmentRepository;
import com.gogidix.sales.territory.domain.repository.TerritoryRepository;
import com.gogidix.sales.territory.shared.exception.ConflictException;
import com.gogidix.sales.territory.shared.exception.NotFoundException;
import com.gogidix.sales.territory.shared.exception.ValidationException;
import com.gogidix.sales.territory.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Territory Assignment Command Service
 * Handles all write operations for territory assignments
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TerritoryAssignmentCommandService {

    private final TerritoryAssignmentRepository assignmentRepository;
    private final TerritoryRepository territoryRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public TerritoryAssignment create(TerritoryAssignmentCommand.CreateAssignmentCommand command) {
        log.info("Creating assignment for territory: {} to representative: {} for tenant: {}",
            command.getTerritoryId(), command.getSalesRepresentativeId(), command.getTenantId());

        // Verify territory exists
        Territory territory = territoryRepository.findByTerritoryIdAndTenantId(
            command.getTerritoryId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Territory", command.getTerritoryId()));

        // Check for existing active assignment
        if (assignmentRepository.existsByTerritoryIdAndSalesRepresentativeIdAndStatus(
            command.getTerritoryId(), command.getSalesRepresentativeId(),
            TerritoryAssignment.AssignmentStatus.ACTIVE)) {
            throw new ConflictException("Assignment", "Active assignment already exists for this territory and representative");
        }

        TerritoryAssignment assignment = TerritoryAssignment.create(
            command.getTenantId(),
            command.getTerritoryId(),
            command.getSalesRepresentativeId(),
            command.getSalesRepresentativeName(),
            RequestContextHolder.getUserId()
        );

        // Set optional fields
        assignment.setType(command.getType());
        if (command.getEffectiveDate() != null) {
            assignment.setEffectiveDate(command.getEffectiveDate());
        }
        if (command.getEndDate() != null) {
            assignment.setEndDate(command.getEndDate());
        }
        if (command.getPrimaryAssignment() != null && command.getPrimaryAssignment()) {
            assignment.setAsPrimary();
        }
        if (command.getPriority() != null) {
            assignment.setPriority(command.getPriority());
        }
        assignment.setNotes(command.getNotes());

        TerritoryAssignment savedAssignment = assignmentRepository.save(assignment);
        publishEvents(savedAssignment);

        log.info("Created assignment: {} for tenant: {}", savedAssignment.getAssignmentId(), command.getTenantId());
        return savedAssignment;
    }

    @Transactional
    public void activate(TerritoryAssignmentCommand.ActivateAssignmentCommand command) {
        log.info("Activating assignment: {} for tenant: {}", command.getAssignmentId(), command.getTenantId());

        TerritoryAssignment assignment = assignmentRepository.findByAssignmentIdAndTenantId(
            command.getAssignmentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Assignment", command.getAssignmentId()));

        assignment.activate(command.getActivatedBy());
        assignmentRepository.save(assignment);
        publishEvents(assignment);

        log.info("Activated assignment: {}", command.getAssignmentId());
    }

    @Transactional
    public void deactivate(TerritoryAssignmentCommand.DeactivateAssignmentCommand command) {
        log.info("Deactivating assignment: {} for tenant: {}", command.getAssignmentId(), command.getTenantId());

        TerritoryAssignment assignment = assignmentRepository.findByAssignmentIdAndTenantId(
            command.getAssignmentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Assignment", command.getAssignmentId()));

        assignment.deactivate();
        assignmentRepository.save(assignment);
        publishEvents(assignment);

        log.info("Deactivated assignment: {}", command.getAssignmentId());
    }

    @Transactional
    public void revoke(TerritoryAssignmentCommand.RevokeAssignmentCommand command) {
        log.info("Revoking assignment: {} for tenant: {}", command.getAssignmentId(), command.getTenantId());

        TerritoryAssignment assignment = assignmentRepository.findByAssignmentIdAndTenantId(
            command.getAssignmentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Assignment", command.getAssignmentId()));

        assignment.revoke();
        assignment.setNotes(command.getReason());
        assignmentRepository.save(assignment);
        publishEvents(assignment);

        log.info("Revoked assignment: {}", command.getAssignmentId());
    }

    @Transactional
    public TerritoryAssignment update(TerritoryAssignmentCommand.UpdateAssignmentCommand command) {
        log.info("Updating assignment: {} for tenant: {}", command.getAssignmentId(), command.getTenantId());

        TerritoryAssignment assignment = assignmentRepository.findByAssignmentIdAndTenantId(
            command.getAssignmentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Assignment", command.getAssignmentId()));

        if (command.getEndDate() != null) {
            assignment.setEndDate(command.getEndDate());
        }
        if (command.getPrimaryAssignment() != null) {
            assignment.setPrimaryAssignment(command.getPrimaryAssignment());
            if (command.getPrimaryAssignment()) {
                assignment.setAsPrimary();
            }
        }
        if (command.getPriority() != null) {
            assignment.setPriority(command.getPriority());
        }
        if (command.getNotes() != null) {
            assignment.setNotes(command.getNotes());
        }

        TerritoryAssignment savedAssignment = assignmentRepository.save(assignment);
        publishEvents(savedAssignment);

        return savedAssignment;
    }

    @Transactional
    public void delete(TerritoryAssignmentCommand.DeleteAssignmentCommand command) {
        log.info("Deleting assignment: {} for tenant: {}", command.getAssignmentId(), command.getTenantId());

        TerritoryAssignment assignment = assignmentRepository.findByAssignmentIdAndTenantId(
            command.getAssignmentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Assignment", command.getAssignmentId()));

        if (assignment.isCurrentlyActive()) {
            throw new ValidationException("Cannot delete active assignment. Deactivate or revoke it first.");
        }

        assignmentRepository.deleteByAssignmentIdAndTenantId(command.getAssignmentId(), command.getTenantId());

        log.info("Deleted assignment: {}", command.getAssignmentId());
    }

    @Transactional
    public void updatePerformance(TerritoryAssignmentCommand.UpdateAssignmentPerformanceCommand command) {
        log.info("Updating performance for assignment: {} for tenant: {}",
            command.getAssignmentId(), command.getTenantId());

        TerritoryAssignment assignment = assignmentRepository.findByAssignmentIdAndTenantId(
            command.getAssignmentId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("Assignment", command.getAssignmentId()));

        assignment.updatePerformance(
            command.getSalesGenerated(),
            command.getAccountsManaged(),
            command.getDealsClosed()
        );

        assignmentRepository.save(assignment);
        publishEvents(assignment);

        log.info("Updated performance for assignment: {}", command.getAssignmentId());
    }

    private void publishEvents(TerritoryAssignment assignment) {
        if (!assignment.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(assignment.getDomainEvents());
            assignment.clearDomainEvents();
        }
    }
}

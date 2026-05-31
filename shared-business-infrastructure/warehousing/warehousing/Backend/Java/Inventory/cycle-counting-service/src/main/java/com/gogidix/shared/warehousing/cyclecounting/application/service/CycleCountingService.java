package com.gogidix.shared.warehousing.cyclecounting.application.service;

import com.gogidix.shared.warehousing.cyclecounting.application.dto.*;
import com.gogidix.shared.warehousing.cyclecounting.domain.entity.*;
import com.gogidix.shared.warehousing.cyclecounting.domain.exception.CycleCountNotFoundException;
import com.gogidix.shared.warehousing.cyclecounting.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Cycle Counting Service
 *
 * Handles inventory cycle counting operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CycleCountingService {

    private final CycleCountRepository cycleCountRepository;
    private final CountSessionRepository countSessionRepository;
    private final CountDiscrepancyRepository discrepancyRepository;

    /**
     * Create cycle count
     */
    public CycleCountDTO createCycleCount(CreateCycleCountCommand command) {
        log.info("Creating cycle count for warehouse: {}", command.getWarehouseId());

        String tenantId = "current-tenant";

        CycleCount cycleCount = CycleCount.builder()
            .tenantId(tenantId)
            .warehouseId(command.getWarehouseId())
            .zoneId(command.getZoneId())
            .countNumber(generateCountNumber())
            .countType(command.getCountType())
            .priority(command.getPriority())
            .status(CycleCount.CountStatus.SCHEDULED)
            .scheduledDate(command.getScheduledDate())
            .dueDate(command.getDueDate())
            .createdBy(command.getCreatedBy())
            .assignedTo(command.getAssignedTo())
            .totalItems(command.getTotalItems())
            .itemsCounted(0)
            .itemsDiscrepant(0)
            .completionPercentage(0.0)
            .notes(command.getNotes())
            .reconciled(false)
            .build();

        CycleCount saved = cycleCountRepository.save(cycleCount);
        log.info("Cycle count created with ID: {}", saved.getCountNumber());

        return toDTO(saved);
    }

    /**
     * Start count session
     */
    public CountSessionDTO startSession(String cycleCountId, String counterId) {
        log.info("Starting count session for cycle count: {}", cycleCountId);

        String tenantId = "current-tenant";

        CycleCount cycleCount = cycleCountRepository.findById(cycleCountId)
            .orElseThrow(() -> new CycleCountNotFoundException("Cycle count not found: " + cycleCountId));

        if (cycleCount.getStatus() == CycleCount.CountStatus.SCHEDULED) {
            cycleCount.start();
            cycleCountRepository.save(cycleCount);
        }

        CountSession session = CountSession.builder()
            .tenantId(tenantId)
            .cycleCountId(cycleCountId)
            .sessionNumber(generateSessionNumber())
            .counterId(counterId)
            .status(CountSession.SessionStatus.ACTIVE)
            .itemsCounted(0)
            .itemsDiscrepant(0)
            .zoneId(cycleCount.getZoneId())
            .build();

        session.start();
        CountSession saved = countSessionRepository.save(session);
        log.info("Count session started: {}", saved.getSessionNumber());

        return toDTO(saved);
    }

    /**
     * Record count
     */
    public CountDiscrepancyDTO recordCount(RecordCountCommand command) {
        log.info("Recording count for SKU: {}", command.getSku());

        String tenantId = "current-tenant";

        // Check for discrepancy
        boolean hasDiscrepancy = !command.getSystemQuantity().equals(command.getCountedQuantity());

        CountDiscrepancy discrepancy = null;
        if (hasDiscrepancy) {
            CountDiscrepancy.DiscrepancyType type = determineDiscrepancyType(
                command.getSystemQuantity(), command.getCountedQuantity());

            discrepancy = CountDiscrepancy.builder()
                .tenantId(tenantId)
                .cycleCountId(command.getCycleCountId())
                .sku(command.getSku())
                .itemName(command.getItemName())
                .location(command.getLocation())
                .discrepancyType(type)
                .systemQuantity(command.getSystemQuantity())
                .countedQuantity(command.getCountedQuantity())
                .unitValue(command.getUnitValue())
                .status(CountDiscrepancy.DiscrepancyStatus.PENDING_REVIEW)
                .resolved(false)
                .build();

            discrepancy.calculateVariance();
            discrepancy.calculateTotalValue();

            discrepancy = discrepancyRepository.save(discrepancy);
        }

        // Update cycle count progress
        cycleCountRepository.findById(command.getCycleCountId()).ifPresent(count -> {
            count.updateProgress(count.getItemsCounted() + 1,
                count.getItemsDiscrepant() + (hasDiscrepancy ? 1 : 0));
            cycleCountRepository.save(count);
        });

        // Update session
        if (command.getCounterId() != null) {
            List<CountSession> sessions = countSessionRepository
                .findByTenantIdAndCycleCountIdOrderByStartedAtDesc(tenantId, command.getCycleCountId());

            sessions.stream()
                .filter(s -> CountSession.SessionStatus.ACTIVE.equals(s.getStatus()))
                .findFirst()
                .ifPresent(session -> {
                    session.setItemsCounted(session.getItemsCounted() + 1);
                    if (hasDiscrepancy) {
                        session.setItemsDiscrepant(session.getItemsDiscrepant() + 1);
                    }
                    countSessionRepository.save(session);
                });
        }

        log.info("Count recorded for SKU: {}, discrepancy: {}", command.getSku(), hasDiscrepancy);

        return discrepancy != null ? toDTO(discrepancy) : null;
    }

    /**
     * Reconcile discrepancy
     */
    public CountDiscrepancyDTO reconcileDiscrepancy(String discrepancyId, ReconcileDiscrepancyCommand command) {
        log.info("Reconciling discrepancy: {}", discrepancyId);

        CountDiscrepancy discrepancy = discrepancyRepository.findById(discrepancyId)
            .orElseThrow(() -> new CycleCountNotFoundException("Discrepancy not found: " + discrepancyId));

        discrepancy.resolve(command.getResolvedBy(), command.getAdjustmentAction(), command.getNotes());
        CountDiscrepancy saved = discrepancyRepository.save(discrepancy);

        return toDTO(saved);
    }

    /**
     * Complete cycle count
     */
    public CycleCountDTO completeCycleCount(String cycleCountId) {
        log.info("Completing cycle count: {}", cycleCountId);

        CycleCount cycleCount = cycleCountRepository.findById(cycleCountId)
            .orElseThrow(() -> new CycleCountNotFoundException("Cycle count not found: " + cycleCountId));

        // Complete active sessions
        List<CountSession> activeSessions = countSessionRepository
            .findByTenantIdAndCycleCountIdOrderByStartedAtDesc(
                cycleCount.getTenantId(), cycleCountId);

        activeSessions.stream()
            .filter(s -> CountSession.SessionStatus.ACTIVE.equals(s.getStatus()))
            .forEach(session -> {
                session.complete();
                countSessionRepository.save(session);
            });

        cycleCount.complete();
        CycleCount saved = cycleCountRepository.save(cycleCount);

        log.info("Cycle count completed: {}", cycleCountId);

        return toDTO(saved);
    }

    /**
     * Get cycle count by ID
     */
    @Transactional(readOnly = true)
    public CycleCountDTO getCycleCount(String cycleCountId) {
        CycleCount cycleCount = cycleCountRepository.findById(cycleCountId)
            .orElseThrow(() -> new CycleCountNotFoundException("Cycle count not found: " + cycleCountId));
        return toDTO(cycleCount);
    }

    /**
     * Get cycle counts for warehouse
     */
    @Transactional(readOnly = true)
    public List<CycleCountDTO> getCycleCounts(String warehouseId) {
        String tenantId = "current-tenant";
        return cycleCountRepository.findByTenantIdAndWarehouseIdOrderByScheduledDateDesc(tenantId, warehouseId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get discrepancies for cycle count
     */
    @Transactional(readOnly = true)
    public List<CountDiscrepancyDTO> getDiscrepancies(String cycleCountId) {
        String tenantId = "current-tenant";
        return discrepancyRepository.findByTenantIdAndCycleCountIdOrderByCreatedAtDesc(tenantId, cycleCountId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get unresolved discrepancies
     */
    @Transactional(readOnly = true)
    public List<CountDiscrepancyDTO> getUnresolvedDiscrepancies() {
        String tenantId = "current-tenant";
        return discrepancyRepository.findByTenantIdAndResolvedFalse(tenantId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    private CountDiscrepancy.DiscrepancyType determineDiscrepancyType(Integer systemQty, Integer countedQty) {
        if (countedQty < systemQty) {
            return CountDiscrepancy.DiscrepancyType.SHORTAGE;
        } else if (countedQty > systemQty) {
            return CountDiscrepancy.DiscrepancyType.OVERAGE;
        }
        return CountDiscrepancy.DiscrepancyType.MISMATCH;
    }

    private String generateCountNumber() {
        return "CC-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateSessionNumber() {
        return "CS-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private CycleCountDTO toDTO(CycleCount count) {
        return CycleCountDTO.builder()
            .id(count.getId())
            .tenantId(count.getTenantId())
            .warehouseId(count.getWarehouseId())
            .zoneId(count.getZoneId())
            .countNumber(count.getCountNumber())
            .countType(count.getCountType())
            .priority(count.getPriority())
            .status(count.getStatus())
            .scheduledDate(count.getScheduledDate())
            .startedAt(count.getStartedAt())
            .completedAt(count.getCompletedAt())
            .createdBy(count.getCreatedBy())
            .assignedTo(count.getAssignedTo())
            .totalItems(count.getTotalItems())
            .itemsCounted(count.getItemsCounted())
            .itemsDiscrepant(count.getItemsDiscrepant())
            .completionPercentage(count.getCompletionPercentage())
            .notes(count.getNotes())
            .dueDate(count.getDueDate())
            .reconciled(count.getReconciled())
            .reconciledBy(count.getReconciledBy())
            .reconciledAt(count.getReconciledAt())
            .createdAt(count.getCreatedAt())
            .updatedAt(count.getUpdatedAt())
            .build();
    }

    private CountSessionDTO toDTO(CountSession session) {
        return CountSessionDTO.builder()
            .id(session.getId())
            .tenantId(session.getTenantId())
            .cycleCountId(session.getCycleCountId())
            .sessionNumber(session.getSessionNumber())
            .counterId(session.getCounterId())
            .counterName(session.getCounterName())
            .status(session.getStatus())
            .startedAt(session.getStartedAt())
            .endedAt(session.getEndedAt())
            .itemsCounted(session.getItemsCounted())
            .itemsDiscrepant(session.getItemsDiscrepant())
            .durationSeconds(session.getDurationSeconds())
            .zoneId(session.getZoneId())
            .location(session.getLocation())
            .notes(session.getNotes())
            .createdAt(session.getCreatedAt())
            .build();
    }

    private CountDiscrepancyDTO toDTO(CountDiscrepancy discrepancy) {
        return CountDiscrepancyDTO.builder()
            .id(discrepancy.getId())
            .tenantId(discrepancy.getTenantId())
            .cycleCountId(discrepancy.getCycleCountId())
            .countSessionId(discrepancy.getCountSessionId())
            .sku(discrepancy.getSku())
            .itemName(discrepancy.getItemName())
            .location(discrepancy.getLocation())
            .discrepancyType(discrepancy.getDiscrepancyType())
            .systemQuantity(discrepancy.getSystemQuantity())
            .countedQuantity(discrepancy.getCountedQuantity())
            .variance(discrepancy.getVariance())
            .unitValue(discrepancy.getUnitValue())
            .totalValue(discrepancy.getTotalValue())
            .status(discrepancy.getStatus())
            .resolved(discrepancy.getResolved())
            .resolvedBy(discrepancy.getResolvedBy())
            .resolvedAt(discrepancy.getResolvedAt())
            .resolutionNotes(discrepancy.getResolutionNotes())
            .adjustmentAction(discrepancy.getAdjustmentAction())
            .referenceType(discrepancy.getReferenceType())
            .referenceId(discrepancy.getReferenceId())
            .createdAt(discrepancy.getCreatedAt())
            .build();
    }

    /**
     * Command to reconcile discrepancy
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class ReconcileDiscrepancyCommand {
        private String resolvedBy;
        private CountDiscrepancy.AdjustmentAction adjustmentAction;
        private String notes;
    }
}

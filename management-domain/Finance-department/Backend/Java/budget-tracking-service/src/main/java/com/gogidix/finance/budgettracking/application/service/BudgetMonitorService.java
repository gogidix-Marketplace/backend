package com.gogidix.finance.budgettracking.application.service;

import com.gogidix.finance.budgettracking.domain.model.BudgetMonitor;
import com.gogidix.finance.budgettracking.domain.port.in.BudgetMonitorCommand;
import com.gogidix.finance.budgettracking.domain.port.out.EventPublisher;
import com.gogidix.finance.budgettracking.domain.repository.BudgetMonitorRepository;
import com.gogidix.finance.budgettracking.shared.exception.ConflictException;
import com.gogidix.finance.budgettracking.shared.exception.NotFoundException;
import com.gogidix.finance.budgettracking.shared.exception.ValidationException;
import com.gogidix.finance.budgettracking.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Budget Monitor Service
 * Handles all command operations for budget monitoring
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BudgetMonitorService {

    private final BudgetMonitorRepository monitorRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public BudgetMonitor create(BudgetMonitorCommand.CreateMonitorCommand command) {
        log.info("Creating budget monitor for tenant: {}, budget: {}",
            command.getTenantId(), command.getBudgetCode());

        validateAllocationAmount(command.getAllocatedAmount());

        if (monitorRepository.existsByTenantIdAndBudgetIdAndPeriod(
            command.getTenantId(), command.getBudgetId(), command.getPeriod())) {
            throw new ConflictException("BudgetMonitor",
                command.getBudgetId() + "-" + command.getPeriod());
        }

        BudgetMonitor monitor = BudgetMonitor.create(
            command.getTenantId(),
            command.getBudgetId(),
            command.getBudgetCode(),
            command.getBudgetName(),
            command.getBudgetPeriod(),
            command.getPeriod(),
            command.getAllocatedAmount(),
            command.getCurrency(),
            command.getCreatedBy()
        );

        monitor.setMonitorId(generateMonitorId());
        monitor.setCategory(command.getCategory());
        monitor.setDepartment(command.getDepartment());
        monitor.setCostCenter(command.getCostCenter());
        monitor.setFiscalYear(command.getFiscalYear());

        if (command.getAlertRecipients() != null) {
            command.getAlertRecipients().forEach(monitor::addAlertRecipient);
        }

        BudgetMonitor savedMonitor = monitorRepository.save(monitor);

        // Check initial thresholds if provided
        if (command.getThresholds() != null) {
            for (BudgetMonitorCommand.ThresholdConfig threshold : command.getThresholds()) {
                checkThreshold(savedMonitor.getMonitorId(), command.getTenantId(),
                    threshold.getThresholdType(), threshold.getThresholdValue(), threshold.getLevel());
            }
        }

        log.info("Created budget monitor: {} for tenant: {}",
            savedMonitor.getMonitorId(), command.getTenantId());
        return savedMonitor;
    }

    @Transactional
    public BudgetMonitor recordExpenditure(BudgetMonitorCommand.RecordExpenditureCommand command) {
        log.info("Recording expenditure for monitor: {} amount: {}",
            command.getMonitorId(), command.getAmount());

        BudgetMonitor monitor = monitorRepository.findByMonitorIdAndTenantId(
            command.getMonitorId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetMonitor", command.getMonitorId()));

        if (!monitor.isAvailable(command.getAmount())) {
            throw new ValidationException("Insufficient budget balance. Available: " +
                monitor.getAvailableBalance() + ", Required: " + command.getAmount());
        }

        monitor.recordExpenditure(command.getAmount());
        BudgetMonitor savedMonitor = monitorRepository.save(monitor);
        publishEvents(savedMonitor);

        log.info("Recorded expenditure for monitor: {}", command.getMonitorId());
        return savedMonitor;
    }

    @Transactional
    public BudgetMonitor recordCommitment(BudgetMonitorCommand.RecordCommitmentCommand command) {
        log.info("Recording commitment for monitor: {} amount: {}",
            command.getMonitorId(), command.getAmount());

        BudgetMonitor monitor = monitorRepository.findByMonitorIdAndTenantId(
            command.getMonitorId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetMonitor", command.getMonitorId()));

        monitor.recordCommitment(command.getAmount());
        BudgetMonitor savedMonitor = monitorRepository.save(monitor);
        publishEvents(savedMonitor);

        log.info("Recorded commitment for monitor: {}", command.getMonitorId());
        return savedMonitor;
    }

    @Transactional
    public BudgetMonitor releaseCommitment(BudgetMonitorCommand.ReleaseCommitmentCommand command) {
        log.info("Releasing commitment for monitor: {} amount: {}",
            command.getMonitorId(), command.getAmount());

        BudgetMonitor monitor = monitorRepository.findByMonitorIdAndTenantId(
            command.getMonitorId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetMonitor", command.getMonitorId()));

        monitor.releaseCommitment(command.getAmount());
        BudgetMonitor savedMonitor = monitorRepository.save(monitor);
        publishEvents(savedMonitor);

        log.info("Released commitment for monitor: {}", command.getMonitorId());
        return savedMonitor;
    }

    @Transactional
    public BudgetMonitor adjustAllocation(BudgetMonitorCommand.AdjustAllocationCommand command) {
        log.info("Adjusting allocation for monitor: {} to: {}",
            command.getMonitorId(), command.getNewAllocation());

        BudgetMonitor monitor = monitorRepository.findByMonitorIdAndTenantId(
            command.getMonitorId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetMonitor", command.getMonitorId()));

        validateAllocationAmount(command.getNewAllocation());

        monitor.adjustAllocation(command.getNewAllocation(), command.getUpdatedBy());
        BudgetMonitor savedMonitor = monitorRepository.save(monitor);
        publishEvents(savedMonitor);

        log.info("Adjusted allocation for monitor: {}", command.getMonitorId());
        return savedMonitor;
    }

    @Transactional
    public BudgetMonitor reverseExpenditure(BudgetMonitorCommand.ReverseExpenditureCommand command) {
        log.info("Reversing expenditure for monitor: {} amount: {}",
            command.getMonitorId(), command.getAmount());

        BudgetMonitor monitor = monitorRepository.findByMonitorIdAndTenantId(
            command.getMonitorId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetMonitor", command.getMonitorId()));

        monitor.reverseExpenditure(command.getAmount());
        BudgetMonitor savedMonitor = monitorRepository.save(monitor);
        publishEvents(savedMonitor);

        log.info("Reversed expenditure for monitor: {}", command.getMonitorId());
        return savedMonitor;
    }

    @Transactional
    public BudgetMonitor.ThresholdStatus checkThreshold(String monitorId, String tenantId,
                                                         String thresholdType, BigDecimal thresholdValue,
                                                         BudgetMonitor.ThresholdLevel level) {
        log.info("Checking threshold {} for monitor: {}", thresholdType, monitorId);

        BudgetMonitor monitor = monitorRepository.findByMonitorIdAndTenantId(monitorId, tenantId)
            .orElseThrow(() -> new NotFoundException("BudgetMonitor", monitorId));

        BudgetMonitor.ThresholdStatus status = monitor.checkThreshold(thresholdType, thresholdValue, level);
        monitorRepository.save(monitor);
        publishEvents(monitor);

        return status;
    }

    @Transactional
    public void acknowledgeThreshold(BudgetMonitorCommand.AcknowledgeThresholdCommand command) {
        log.info("Acknowledging threshold {} for monitor: {}",
            command.getThresholdType(), command.getMonitorId());

        BudgetMonitor monitor = monitorRepository.findByMonitorIdAndTenantId(
            command.getMonitorId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetMonitor", command.getMonitorId()));

        monitor.acknowledgeThreshold(command.getThresholdType(), command.getAcknowledgedBy());
        monitorRepository.save(monitor);

        log.info("Acknowledged threshold for monitor: {}", command.getMonitorId());
    }

    @Transactional
    public void addAlertRecipient(BudgetMonitorCommand.AddAlertRecipientCommand command) {
        log.info("Adding alert recipient to monitor: {}", command.getMonitorId());

        BudgetMonitor monitor = monitorRepository.findByMonitorIdAndTenantId(
            command.getMonitorId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetMonitor", command.getMonitorId()));

        monitor.addAlertRecipient(command.getRecipient());
        monitorRepository.save(monitor);

        log.info("Added alert recipient to monitor: {}", command.getMonitorId());
    }

    @Transactional
    public void removeAlertRecipient(BudgetMonitorCommand.RemoveAlertRecipientCommand command) {
        log.info("Removing alert recipient from monitor: {}", command.getMonitorId());

        BudgetMonitor monitor = monitorRepository.findByMonitorIdAndTenantId(
            command.getMonitorId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetMonitor", command.getMonitorId()));

        monitor.removeAlertRecipient(command.getRecipient());
        monitorRepository.save(monitor);

        log.info("Removed alert recipient from monitor: {}", command.getMonitorId());
    }

    @Transactional
    public BudgetMonitor update(BudgetMonitorCommand.UpdateMonitorCommand command) {
        log.info("Updating budget monitor: {} for tenant: {}",
            command.getMonitorId(), command.getTenantId());

        BudgetMonitor monitor = monitorRepository.findByMonitorIdAndTenantId(
            command.getMonitorId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetMonitor", command.getMonitorId()));

        if (command.getBudgetName() != null) {
            monitor.setBudgetName(command.getBudgetName());
        }
        if (command.getAllocatedAmount() != null) {
            validateAllocationAmount(command.getAllocatedAmount());
            monitor.adjustAllocation(command.getAllocatedAmount(), com.gogidix.finance.budgettracking.shared.requestcontext.RequestContextHolder.getUserId().orElse("system"));
        }
        if (command.getCategory() != null) {
            monitor.setCategory(command.getCategory());
        }
        if (command.getDepartment() != null) {
            monitor.setDepartment(command.getDepartment());
        }
        if (command.getCostCenter() != null) {
            monitor.setCostCenter(command.getCostCenter());
        }

        return monitorRepository.save(monitor);
    }

    public List<BudgetMonitor> getCriticalBudgets(String tenantId) {
        return monitorRepository.findCriticalBudgets(tenantId);
    }

    public List<BudgetMonitor> getOverBudgetMonitors(String tenantId) {
        return monitorRepository.findOverBudgetMonitors(tenantId);
    }

    private void validateAllocationAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("allocatedAmount", "Allocation amount must be positive");
        }
    }

    private void publishEvents(BudgetMonitor monitor) {
        if (!monitor.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(new java.util.ArrayList<>(monitor.getDomainEvents()));
            monitor.clearDomainEvents();
        }
    }

    private String generateMonitorId() {
        return "BMON-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public BudgetMonitor createMonitor(com.gogidix.finance.budgettracking.domain.port.in.BudgetMonitorCommand.CreateMonitorCommand command) {
        return create(command);
    }

    public BudgetMonitor recordExpenditure(String monitorId, BigDecimal amount) {
        BudgetMonitor monitor = monitorRepository.findByMonitorIdAndTenantId(monitorId, RequestContextHolder.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetMonitor", monitorId));
        monitor.recordExpenditure(amount);
        return monitorRepository.save(monitor);
    }

    public BudgetMonitor recordCommitment(String monitorId, BigDecimal amount) {
        BudgetMonitor monitor = monitorRepository.findByMonitorIdAndTenantId(monitorId, RequestContextHolder.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetMonitor", monitorId));
        monitor.recordCommitment(amount);
        return monitorRepository.save(monitor);
    }

    public BudgetMonitor releaseCommitment(String monitorId, BigDecimal amount) {
        BudgetMonitor monitor = monitorRepository.findByMonitorIdAndTenantId(monitorId, RequestContextHolder.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetMonitor", monitorId));
        monitor.releaseCommitment(amount);
        return monitorRepository.save(monitor);
    }

    public BudgetMonitor adjustAllocation(String monitorId, BigDecimal newAllocation) {
        BudgetMonitor monitor = monitorRepository.findByMonitorIdAndTenantId(monitorId, RequestContextHolder.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetMonitor", monitorId));
        monitor.adjustAllocation(newAllocation, RequestContextHolder.getUserId().orElse("system"));
        return monitorRepository.save(monitor);
    }

    public BudgetMonitor checkThreshold(String monitorId, String thresholdType, BigDecimal value, BudgetMonitor.ThresholdLevel level) {
        BudgetMonitor monitor = monitorRepository.findByMonitorIdAndTenantId(monitorId, RequestContextHolder.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetMonitor", monitorId));
        monitor.checkThreshold(thresholdType, value, level);
        return monitorRepository.save(monitor);
    }

    public void acknowledgeThreshold(String monitorId, String thresholdType, java.util.Optional<String> userId) {
        BudgetMonitor monitor = monitorRepository.findByMonitorIdAndTenantId(monitorId, RequestContextHolder.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetMonitor", monitorId));
        monitor.acknowledgeThreshold("all", userId.orElse("system"));
        monitorRepository.save(monitor);
    }

    public BudgetMonitor getMonitorById(String monitorId) {
        return monitorRepository.findByMonitorIdAndTenantId(monitorId, RequestContextHolder.getTenantId())
            .orElseThrow(() -> new NotFoundException("BudgetMonitor", monitorId));
    }

    public BudgetMonitor getMonitorByBudget(String budgetId) {
        List<BudgetMonitor> monitors = monitorRepository.findByTenantIdAndBudgetId(RequestContextHolder.getTenantId(), budgetId);
        return monitors.isEmpty() ? null : monitors.get(0);
    }

    public List<BudgetMonitor> getAllMonitors() {
        return monitorRepository.findByTenantId(RequestContextHolder.getTenantId());
    }

    public List<BudgetMonitor> getMonitorsByPeriod(java.time.YearMonth period) {
        return monitorRepository.findByTenantIdAndPeriod(RequestContextHolder.getTenantId(), period);
    }

    public List<BudgetMonitor> getMonitorsByStatus(BudgetMonitor.MonitorStatus status) {
        return monitorRepository.findByTenantIdAndStatus(RequestContextHolder.getTenantId(), status);
    }

    public List<BudgetMonitor> getCriticalBudgets() {
        return getCriticalBudgets(RequestContextHolder.getTenantId());
    }
}

package com.gogidix.hr.payroll.application.service;

import com.gogidix.hr.payroll.domain.event.PayrollApprovedEvent;
import com.gogidix.hr.payroll.domain.event.PayrollCreatedEvent;
import com.gogidix.hr.payroll.domain.event.PayrollPaidEvent;
import com.gogidix.hr.payroll.domain.event.PayrollProcessedEvent;
import com.gogidix.hr.payroll.domain.model.Payroll;
import com.gogidix.hr.payroll.domain.model.PayrollEntry;
import com.gogidix.hr.payroll.domain.enums.SalaryFrequency;
import com.gogidix.hr.payroll.domain.port.out.EventPublisher;
import com.gogidix.hr.payroll.domain.port.in.PayrollCommand;
import com.gogidix.hr.payroll.domain.repository.PayrollEntryRepository;
import com.gogidix.hr.payroll.domain.repository.PayrollRepository;
import com.gogidix.hr.payroll.domain.repository.PayslipRepository;
import com.gogidix.hr.payroll.shared.exception.PayrollNotFoundException;
import com.gogidix.hr.payroll.shared.exception.PayrollValidationException;
import com.gogidix.hr.payroll.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

/**
 * Payroll Command Service
 * Handles all write operations for payroll
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PayrollCommandService {

    private final PayrollRepository payrollRepository;
    private final PayrollEntryRepository payrollEntryRepository;
    private final PayslipRepository payslipRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public Payroll create(PayrollCommand.CreatePayrollCommand command) {
        log.info("Creating payroll: {} for tenant: {}", command.getPayrollName(), command.getTenantId());

        // Check if payroll already exists for this period
        if (payrollRepository.existsByPayrollPeriodAndTenantId(command.getPayrollPeriod(), command.getTenantId())) {
            throw new PayrollValidationException("Payroll already exists for period: " + command.getPayrollPeriod());
        }

        String payrollId = generatePayrollId();

        Payroll payroll = new Payroll();
        payroll.setId(UUID.randomUUID().toString());
        payroll.setTenantId(command.getTenantId());
        payroll.setCountryCode(command.getCountryCode());
        payroll.setPayrollId(payrollId);
        payroll.setPayrollName(command.getPayrollName());
        payroll.setPayrollPeriod(command.getPayrollPeriod());
        payroll.setStartDate(command.getStartDate());
        payroll.setEndDate(command.getEndDate());
        payroll.setPaymentDate(command.getPaymentDate());
        payroll.setStatus(com.gogidix.hr.payroll.domain.enums.PayrollStatus.DRAFT);
        payroll.setFrequency(command.getFrequency() != null ? SalaryFrequency.valueOf(command.getFrequency().toUpperCase()) : null);
        payroll.setCurrency(command.getCurrency());
        payroll.setBatchId(UUID.randomUUID().toString());
        payroll.setRunType(command.getRunType());
        payroll.setNotes(command.getNotes());
        payroll.setCreatedBy(command.getCreatedBy());

        Payroll savedPayroll = payrollRepository.save(payroll);

        // Add employees if provided
        if (command.getEmployeeIds() != null && !command.getEmployeeIds().isEmpty()) {
            for (String employeeId : command.getEmployeeIds()) {
                addEmployeeToPayroll(savedPayroll, employeeId, command.getCreatedBy());
            }
            savedPayroll = payrollRepository.save(savedPayroll);
        }

        // Publish event
        if (eventPublisher.isReady()) {
            eventPublisher.publishPayrollCreated(PayrollCreatedEvent.builder()
                    .eventId(java.util.UUID.randomUUID().toString())
                    .payrollId(savedPayroll.getId())
                    .tenantId(savedPayroll.getTenantId())
                    .countryCode(savedPayroll.getCountryCode())
                    .payrollName(savedPayroll.getPayrollName())
                    .payrollPeriod(savedPayroll.getPayrollPeriod())
                    .totalAmount(savedPayroll.getTotalGrossPay())
                    .employeeCount(savedPayroll.getEmployeeCount())
                    .createdBy(command.getCreatedBy())
                    .occurredAt(java.time.Instant.now())
                    .correlationId(command.getTenantId())
                    .build());
        }

        log.info("Created payroll: {}", savedPayroll.getId());
        return savedPayroll;
    }

    @Transactional
    public Payroll update(PayrollCommand.UpdatePayrollCommand command) {
        log.info("Updating payroll: {} for tenant: {}", command.getPayrollId(), command.getTenantId());

        Payroll payroll = payrollRepository.findByPayrollIdAndTenantId(command.getPayrollId(), command.getTenantId())
                .orElseThrow(() -> new PayrollNotFoundException("Payroll", command.getPayrollId()));

        if (!payroll.canModify()) {
            throw new PayrollValidationException("Payroll cannot be modified in current status");
        }

        if (command.getPayrollName() != null) {
            payroll.setPayrollName(command.getPayrollName());
        }
        if (command.getPaymentDate() != null) {
            payroll.setPaymentDate(command.getPaymentDate());
        }
        if (command.getNotes() != null) {
            payroll.setNotes(command.getNotes());
        }
        payroll.setUpdatedBy(command.getUpdatedBy());

        Payroll updatedPayroll = payrollRepository.save(payroll);
        log.info("Updated payroll: {}", command.getPayrollId());
        return updatedPayroll;
    }

    @Transactional
    public void submit(PayrollCommand.SubmitPayrollCommand command) {
        log.info("Submitting payroll: {} for tenant: {}", command.getPayrollId(), command.getTenantId());

        Payroll payroll = payrollRepository.findByPayrollIdAndTenantId(command.getPayrollId(), command.getTenantId())
                .orElseThrow(() -> new PayrollNotFoundException("Payroll", command.getPayrollId()));

        // Validate payroll has entries
        List<PayrollEntry> entries = payrollEntryRepository.findByPayrollId(command.getPayrollId());
        if (entries.isEmpty()) {
            throw new PayrollValidationException("Cannot submit payroll without any employee entries");
        }

        payroll.submitForApproval(command.getSubmittedBy());
        payrollRepository.save(payroll);

        log.info("Submitted payroll: {}", command.getPayrollId());
    }

    @Transactional
    public void approve(PayrollCommand.ApprovePayrollCommand command) {
        log.info("Approving payroll: {} for tenant: {}", command.getPayrollId(), command.getTenantId());

        Payroll payroll = payrollRepository.findByPayrollIdAndTenantId(command.getPayrollId(), command.getTenantId())
                .orElseThrow(() -> new PayrollNotFoundException("Payroll", command.getPayrollId()));

        payroll.approve(command.getApprovedBy());
        Payroll savedPayroll = payrollRepository.save(payroll);

        // Publish event
        if (eventPublisher.isReady()) {
            eventPublisher.publishPayrollApproved(PayrollApprovedEvent.builder()
                    .eventId(java.util.UUID.randomUUID().toString())
                    .payrollId(savedPayroll.getId())
                    .tenantId(savedPayroll.getTenantId())
                    .countryCode(savedPayroll.getCountryCode())
                    .payrollPeriod(savedPayroll.getPayrollPeriod())
                    .approvedBy(command.getApprovedBy())
                    .occurredAt(java.time.Instant.now())
                    .correlationId(command.getTenantId())
                    .build());
        }

        log.info("Approved payroll: {}", command.getPayrollId());
    }

    @Transactional
    public void reject(PayrollCommand.RejectPayrollCommand command) {
        log.info("Rejecting payroll: {} for tenant: {}", command.getPayrollId(), command.getTenantId());

        Payroll payroll = payrollRepository.findByPayrollIdAndTenantId(command.getPayrollId(), command.getTenantId())
                .orElseThrow(() -> new PayrollNotFoundException("Payroll", command.getPayrollId()));

        payroll.reject(command.getRejectedBy());
        payroll.setNotes(command.getReason());
        payrollRepository.save(payroll);

        log.info("Rejected payroll: {}", command.getPayrollId());
    }

    @Transactional
    public void process(PayrollCommand.ProcessPayrollCommand command) {
        log.info("Processing payroll: {} for tenant: {}", command.getPayrollId(), command.getTenantId());

        Payroll payroll = payrollRepository.findByPayrollIdAndTenantId(command.getPayrollId(), command.getTenantId())
                .orElseThrow(() -> new PayrollNotFoundException("Payroll", command.getPayrollId()));

        // Get all entries and calculate totals
        List<PayrollEntry> entries = payrollEntryRepository.findByPayrollId(command.getPayrollId());
        entries.forEach(PayrollEntry::calculateNetPay);
        payrollEntryRepository.saveAll(entries);

        payroll.calculateTotals(entries);
        payroll.process(command.getProcessedBy());
        Payroll savedPayroll = payrollRepository.save(payroll);

        // Publish event
        if (eventPublisher.isReady()) {
            eventPublisher.publishPayrollProcessed(PayrollProcessedEvent.builder()
                    .eventId(java.util.UUID.randomUUID().toString())
                    .payrollId(savedPayroll.getId())
                    .tenantId(savedPayroll.getTenantId())
                    .countryCode(savedPayroll.getCountryCode())
                    .payrollPeriod(savedPayroll.getPayrollPeriod())
                    .totalGrossPay(savedPayroll.getTotalGrossPay())
                    .totalNetPay(savedPayroll.getTotalNetPay())
                    .totalTaxes(savedPayroll.getTotalTaxes())
                    .totalDeductions(savedPayroll.getTotalDeductions())
                    .employeeCount(savedPayroll.getEmployeeCount())
                    .processedBy(command.getProcessedBy())
                    .occurredAt(java.time.Instant.now())
                    .correlationId(command.getTenantId())
                    .build());
        }

        log.info("Processed payroll: {}", command.getPayrollId());
    }

    @Transactional
    public void markAsPaid(PayrollCommand.MarkAsPaidCommand command) {
        log.info("Marking payroll as paid: {} for tenant: {}", command.getPayrollId(), command.getTenantId());

        Payroll payroll = payrollRepository.findByPayrollIdAndTenantId(command.getPayrollId(), command.getTenantId())
                .orElseThrow(() -> new PayrollNotFoundException("Payroll", command.getPayrollId()));

        payroll.markAsPaid(command.getProcessedBy());
        Payroll savedPayroll = payrollRepository.save(payroll);

        // Mark all entries as paid
        List<PayrollEntry> entries = payrollEntryRepository.findByPayrollId(command.getPayrollId());
        entries.forEach(entry -> entry.markAsPaid(savedPayroll.getPaymentDate()));
        payrollEntryRepository.saveAll(entries);

        // Publish event
        if (eventPublisher.isReady()) {
            eventPublisher.publishPayrollPaid(PayrollPaidEvent.builder()
                    .eventId(java.util.UUID.randomUUID().toString())
                    .payrollId(savedPayroll.getId())
                    .tenantId(savedPayroll.getTenantId())
                    .countryCode(savedPayroll.getCountryCode())
                    .payrollPeriod(savedPayroll.getPayrollPeriod())
                    .totalAmount(savedPayroll.getTotalNetPay())
                    .employeeCount(savedPayroll.getEmployeeCount())
                    .paidBy(command.getProcessedBy())
                    .occurredAt(java.time.Instant.now())
                    .correlationId(command.getTenantId())
                    .build());
        }

        log.info("Marked payroll as paid: {}", command.getPayrollId());
    }

    @Transactional
    public void lock(PayrollCommand.LockPayrollCommand command) {
        log.info("Locking payroll: {} for tenant: {}", command.getPayrollId(), command.getTenantId());

        Payroll payroll = payrollRepository.findByPayrollIdAndTenantId(command.getPayrollId(), command.getTenantId())
                .orElseThrow(() -> new PayrollNotFoundException("Payroll", command.getPayrollId()));

        payroll.lock(command.getLockedBy());
        payrollRepository.save(payroll);

        log.info("Locked payroll: {}", command.getPayrollId());
    }

    @Transactional
    public void unlock(PayrollCommand.UnlockPayrollCommand command) {
        log.info("Unlocking payroll: {} for tenant: {}", command.getPayrollId(), command.getTenantId());

        Payroll payroll = payrollRepository.findByPayrollIdAndTenantId(command.getPayrollId(), command.getTenantId())
                .orElseThrow(() -> new PayrollNotFoundException("Payroll", command.getPayrollId()));

        payroll.unlock(command.getUnlockedBy());
        payrollRepository.save(payroll);

        log.info("Unlocked payroll: {}", command.getPayrollId());
    }

    @Transactional
    public void cancel(PayrollCommand.CancelPayrollCommand command) {
        log.info("Cancelling payroll: {} for tenant: {}", command.getPayrollId(), command.getTenantId());

        Payroll payroll = payrollRepository.findByPayrollIdAndTenantId(command.getPayrollId(), command.getTenantId())
                .orElseThrow(() -> new PayrollNotFoundException("Payroll", command.getPayrollId()));

        payroll.setStatus(com.gogidix.hr.payroll.domain.enums.PayrollStatus.CANCELLED);
        payroll.setNotes(command.getReason());
        payroll.setUpdatedBy(command.getCancelledBy());
        payrollRepository.save(payroll);

        log.info("Cancelled payroll: {}", command.getPayrollId());
    }

    @Transactional
    public void addEmployee(PayrollCommand.AddEmployeeCommand command) {
        log.info("Adding employee {} to payroll: {}", command.getEmployeeId(), command.getPayrollId());

        Payroll payroll = payrollRepository.findByPayrollIdAndTenantId(command.getPayrollId(), command.getTenantId())
                .orElseThrow(() -> new PayrollNotFoundException("Payroll", command.getPayrollId()));

        if (!payroll.canModify()) {
            throw new PayrollValidationException("Cannot add employee to payroll in current status");
        }

        addEmployeeToPayroll(payroll, command.getEmployeeId(), command.getAddedBy());
        payrollRepository.save(payroll);

        log.info("Added employee to payroll: {}", command.getPayrollId());
    }

    @Transactional
    public void removeEmployee(PayrollCommand.RemoveEmployeeCommand command) {
        log.info("Removing employee {} from payroll: {}", command.getEmployeeId(), command.getPayrollId());

        Payroll payroll = payrollRepository.findByPayrollIdAndTenantId(command.getPayrollId(), command.getTenantId())
                .orElseThrow(() -> new PayrollNotFoundException("Payroll", command.getPayrollId()));

        if (!payroll.canModify()) {
            throw new PayrollValidationException("Cannot remove employee from payroll in current status");
        }

        // Remove entry if exists
        List<PayrollEntry> entries = payrollEntryRepository.findByPayrollIdAndEmployeeId(command.getPayrollId(), command.getEmployeeId());
        entries.forEach(entry -> payrollEntryRepository.deleteById(entry.getId()));

        payroll.setUpdatedBy(command.getRemovedBy());
        payrollRepository.save(payroll);

        log.info("Removed employee from payroll: {}", command.getPayrollId());
    }

    @Transactional
    public void delete(PayrollCommand.DeletePayrollCommand command) {
        log.info("Deleting payroll: {} for tenant: {}", command.getPayrollId(), command.getTenantId());

        Payroll payroll = payrollRepository.findByPayrollIdAndTenantId(command.getPayrollId(), command.getTenantId())
                .orElseThrow(() -> new PayrollNotFoundException("Payroll", command.getPayrollId()));

        if (!payroll.canModify()) {
            throw new PayrollValidationException("Cannot delete payroll in current status");
        }

        // Delete all entries
        payrollEntryRepository.deleteByPayrollId(command.getPayrollId());
        payslipRepository.deleteByPayrollId(command.getPayrollId());

        // Delete payroll
        payrollRepository.deleteByPayrollIdAndTenantId(command.getPayrollId(), command.getTenantId());

        log.info("Deleted payroll: {}", command.getPayrollId());
    }

    private void addEmployeeToPayroll(Payroll payroll, String employeeId, String userId) {
        if (!payroll.getPayrollEntryIds().contains(employeeId)) {
            payroll.getPayrollEntryIds().add(employeeId);
            payroll.setUpdatedBy(userId);
        }
    }

    private String generatePayrollId() {
        return "PR-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

package com.gogidix.hr.payroll.application.service;

import com.gogidix.hr.payroll.domain.model.Payroll;
import com.gogidix.hr.payroll.domain.model.PayrollEntry;
import com.gogidix.hr.payroll.domain.port.in.PayrollEntryCommand;
import com.gogidix.hr.payroll.domain.repository.PayrollEntryRepository;
import com.gogidix.hr.payroll.domain.repository.PayrollRepository;
import com.gogidix.hr.payroll.shared.exception.PayrollNotFoundException;
import com.gogidix.hr.payroll.shared.exception.PayrollValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Payroll Entry Command Service
 * Handles all write operations for payroll entries
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PayrollEntryCommandService {

    private final PayrollEntryRepository payrollEntryRepository;
    private final PayrollRepository payrollRepository;

    @Transactional
    public PayrollEntry create(PayrollEntryCommand.CreateEntryCommand command) {
        log.info("Creating payroll entry for employee: {} in payroll: {}", command.getEmployeeId(), command.getPayrollId());

        // Verify payroll exists and is modifiable
        Payroll payroll = payrollRepository.findByPayrollIdAndTenantId(command.getPayrollId(), command.getTenantId())
                .orElseThrow(() -> new PayrollNotFoundException("Payroll", command.getPayrollId()));

        if (!payroll.canModify()) {
            throw new PayrollValidationException("Cannot add entry to payroll in current status");
        }

        // Check if entry already exists
        List<PayrollEntry> existing = payrollEntryRepository.findByPayrollIdAndEmployeeId(command.getPayrollId(), command.getEmployeeId());
        if (!existing.isEmpty()) {
            throw new PayrollValidationException("Payroll entry already exists for employee: " + command.getEmployeeId());
        }

        PayrollEntry entry = new PayrollEntry();
        entry.setId(UUID.randomUUID().toString());
        entry.setTenantId(command.getTenantId());
        entry.setCountryCode(command.getCountryCode());
        entry.setPayrollId(command.getPayrollId());
        entry.setEmployeeId(command.getEmployeeId());
        entry.setEmployeeName(command.getEmployeeName());
        entry.setEmployeeCode(command.getEmployeeCode());
        entry.setDepartment(command.getDepartment());
        entry.setPosition(command.getPosition());
        entry.setPayPeriodStart(payroll.getStartDate());
        entry.setPayPeriodEnd(payroll.getEndDate());
        entry.setBasicSalary(command.getBasicSalary());
        entry.setOvertimeHours(command.getOvertimeHours());
        entry.setOvertimeRate(command.getOvertimeRate());
        entry.setBonus(command.getBonus());
        entry.setCommission(command.getCommission());
        entry.setAllowances(command.getAllowances());
        entry.setHealthInsurance(command.getHealthInsurance());
        entry.setDentalInsurance(command.getDentalInsurance());
        entry.setRetirement401k(command.getRetirement401k());
        entry.setPaymentMethod(command.getPaymentMethod());
        entry.setBankAccountNumber(command.getBankAccountNumber());
        entry.setBankRoutingNumber(command.getBankRoutingNumber());
        entry.setCurrency(payroll.getCurrency());
        entry.setTaxCode(command.getTaxCode());
        entry.setTaxExemptions(command.getTaxExemptions());
        entry.setPaid(false);
        entry.setIsHold(false);
        entry.setUpdatedBy(command.getCreatedBy());

        // Calculate amounts
        entry.calculateOvertimePay();
        entry.calculateNetPay();

        PayrollEntry savedEntry = payrollEntryRepository.save(entry);

        // Add entry ID to payroll
        payroll.getPayrollEntryIds().add(entry.getId());
        payrollRepository.save(payroll);

        log.info("Created payroll entry: {}", entry.getId());
        return savedEntry;
    }

    @Transactional
    public PayrollEntry update(PayrollEntryCommand.UpdateEntryCommand command) {
        log.info("Updating payroll entry: {}", command.getEntryId());

        PayrollEntry entry = payrollEntryRepository.findByEntryIdAndTenantId(command.getEntryId(), command.getTenantId())
                .orElseThrow(() -> new PayrollNotFoundException("PayrollEntry", command.getEntryId()));

        // Verify payroll is modifiable
        Payroll payroll = payrollRepository.findByPayrollIdAndTenantId(entry.getPayrollId(), command.getTenantId())
                .orElseThrow(() -> new PayrollNotFoundException("Payroll", entry.getPayrollId()));

        if (!payroll.canModify()) {
            throw new PayrollValidationException("Cannot update entry in payroll in current status");
        }

        // Update fields
        if (command.getBasicSalary() != null) {
            entry.setBasicSalary(command.getBasicSalary());
        }
        if (command.getOvertimeHours() != null) {
            entry.setOvertimeHours(command.getOvertimeHours());
        }
        if (command.getOvertimeRate() != null) {
            entry.setOvertimeRate(command.getOvertimeRate());
        }
        if (command.getBonus() != null) {
            entry.setBonus(command.getBonus());
        }
        if (command.getCommission() != null) {
            entry.setCommission(command.getCommission());
        }
        if (command.getAllowances() != null) {
            entry.setAllowances(command.getAllowances());
        }
        if (command.getHealthInsurance() != null) {
            entry.setHealthInsurance(command.getHealthInsurance());
        }
        if (command.getDentalInsurance() != null) {
            entry.setDentalInsurance(command.getDentalInsurance());
        }
        if (command.getRetirement401k() != null) {
            entry.setRetirement401k(command.getRetirement401k());
        }
        if (command.getPaymentMethod() != null) {
            entry.setPaymentMethod(command.getPaymentMethod());
        }
        if (command.getBankAccountNumber() != null) {
            entry.setBankAccountNumber(command.getBankAccountNumber());
        }
        if (command.getBankRoutingNumber() != null) {
            entry.setBankRoutingNumber(command.getBankRoutingNumber());
        }
        if (command.getNotes() != null) {
            entry.setNotes(command.getNotes());
        }

        entry.setUpdatedBy(command.getUpdatedBy());

        // Recalculate amounts
        entry.calculateOvertimePay();
        entry.calculateNetPay();

        PayrollEntry updatedEntry = payrollEntryRepository.save(entry);
        log.info("Updated payroll entry: {}", command.getEntryId());
        return updatedEntry;
    }

    @Transactional
    public void updateTax(PayrollEntryCommand.UpdateTaxCommand command) {
        log.info("Updating tax for payroll entry: {}", command.getEntryId());

        PayrollEntry entry = payrollEntryRepository.findByEntryIdAndTenantId(command.getEntryId(), command.getTenantId())
                .orElseThrow(() -> new PayrollNotFoundException("PayrollEntry", command.getEntryId()));

        // Verify payroll is modifiable
        Payroll payroll = payrollRepository.findByPayrollIdAndTenantId(entry.getPayrollId(), command.getTenantId())
                .orElseThrow(() -> new PayrollNotFoundException("Payroll", entry.getPayrollId()));

        if (!payroll.canModify()) {
            throw new PayrollValidationException("Cannot update tax in payroll in current status");
        }

        entry.setFederalTax(command.getFederalTax());
        entry.setStateTax(command.getStateTax());
        entry.setLocalTax(command.getLocalTax());
        entry.setSocialSecurityTax(command.getSocialSecurityTax());
        entry.setMedicareTax(command.getMedicareTax());
        entry.setOtherTaxes(command.getOtherTaxes());
        entry.setUpdatedBy(command.getUpdatedBy());

        entry.calculateTotalTax();
        entry.calculateNetPay();

        payrollEntryRepository.save(entry);
        log.info("Updated tax for payroll entry: {}", command.getEntryId());
    }

    @Transactional
    public void holdPayment(PayrollEntryCommand.HoldPaymentCommand command) {
        log.info("Holding payment for entry: {}", command.getEntryId());

        PayrollEntry entry = payrollEntryRepository.findByEntryIdAndTenantId(command.getEntryId(), command.getTenantId())
                .orElseThrow(() -> new PayrollNotFoundException("PayrollEntry", command.getEntryId()));

        entry.holdPayment(command.getReason());
        entry.setUpdatedBy(command.getUpdatedBy());

        payrollEntryRepository.save(entry);
        log.info("Held payment for entry: {}", command.getEntryId());
    }

    @Transactional
    public void releaseHold(PayrollEntryCommand.ReleaseHoldCommand command) {
        log.info("Releasing hold for entry: {}", command.getEntryId());

        PayrollEntry entry = payrollEntryRepository.findByEntryIdAndTenantId(command.getEntryId(), command.getTenantId())
                .orElseThrow(() -> new PayrollNotFoundException("PayrollEntry", command.getEntryId()));

        entry.releaseHold();
        entry.setUpdatedBy(command.getUpdatedBy());

        payrollEntryRepository.save(entry);
        log.info("Released hold for entry: {}", command.getEntryId());
    }

    @Transactional
    public void delete(PayrollEntryCommand.DeleteEntryCommand command) {
        log.info("Deleting payroll entry: {}", command.getEntryId());

        PayrollEntry entry = payrollEntryRepository.findByEntryIdAndTenantId(command.getEntryId(), command.getTenantId())
                .orElseThrow(() -> new PayrollNotFoundException("PayrollEntry", command.getEntryId()));

        // Verify payroll is modifiable
        Payroll payroll = payrollRepository.findByPayrollIdAndTenantId(entry.getPayrollId(), command.getTenantId())
                .orElseThrow(() -> new PayrollNotFoundException("Payroll", entry.getPayrollId()));

        if (!payroll.canModify()) {
            throw new PayrollValidationException("Cannot delete entry from payroll in current status");
        }

        // Remove entry ID from payroll
        payroll.getPayrollEntryIds().remove(entry.getId());
        payrollRepository.save(payroll);

        // Delete entry
        payrollEntryRepository.deleteByEntryIdAndTenantId(command.getEntryId(), command.getTenantId());

        log.info("Deleted payroll entry: {}", command.getEntryId());
    }

    @Transactional
    public void batchCreate(List<PayrollEntryCommand.CreateEntryCommand> commands) {
        log.info("Batch creating {} payroll entries", commands.size());

        for (PayrollEntryCommand.CreateEntryCommand command : commands) {
            try {
                create(command);
            } catch (Exception e) {
                log.error("Error creating entry for employee: {}", command.getEmployeeId(), e);
            }
        }

        log.info("Completed batch creation of payroll entries");
    }

    private String generateEntryId() {
        return "PE-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

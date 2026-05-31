package com.gogidix.hr.payroll.application.service;

import com.gogidix.hr.payroll.domain.event.PayslipGeneratedEvent;
import com.gogidix.hr.payroll.domain.model.Payslip;
import com.gogidix.hr.payroll.domain.model.Payroll;
import com.gogidix.hr.payroll.domain.model.PayrollEntry;
import com.gogidix.hr.payroll.domain.port.out.EventPublisher;
import com.gogidix.hr.payroll.domain.repository.PayslipRepository;
import com.gogidix.hr.payroll.domain.repository.PayrollEntryRepository;
import com.gogidix.hr.payroll.domain.repository.PayrollRepository;
import com.gogidix.hr.payroll.shared.exception.PayrollNotFoundException;
import com.gogidix.hr.payroll.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Payslip Service
 * Handles payslip generation and management
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PayslipService {

    private final PayslipRepository payslipRepository;
    private final PayrollRepository payrollRepository;
    private final PayrollEntryRepository payrollEntryRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public List<Payslip> generatePayslips(String payrollId) {
        log.info("Generating payslips for payroll: {}", payrollId);

        String tenantId = RequestContextHolder.getTenantId();

        // Verify payroll exists
        Payroll payroll = payrollRepository.findByPayrollIdAndTenantId(payrollId, tenantId)
                .orElseThrow(() -> new PayrollNotFoundException("Payroll", payrollId));

        // Get all entries
        List<PayrollEntry> entries = payrollEntryRepository.findByPayrollId(payrollId);
        List<Payslip> payslips = new ArrayList<>();

        for (PayrollEntry entry : entries) {
            Payslip payslip = generatePayslipForEntry(payroll, entry);
            payslips.add(payslip);
        }

        List<Payslip> savedPayslips = payslipRepository.saveAll(payslips);

        // Publish events
        if (eventPublisher.isReady()) {
            for (Payslip payslip : savedPayslips) {
                eventPublisher.publishPayslipGenerated(PayslipGeneratedEvent.builder()
                        .eventId(java.util.UUID.randomUUID().toString())
                        .payslipId(payslip.getPayslipId())
                        .payrollId(payrollId)
                        .employeeId(payslip.getEmployeeId())
                        .tenantId(payslip.getTenantId())
                        .netPay(payslip.getNetPay())
                        .currency(payslip.getCurrency())
                        .occurredAt(java.time.Instant.now())
                        .correlationId(payslip.getTenantId())
                        .build());
            }
        }

        log.info("Generated {} payslips for payroll: {}", savedPayslips.size(), payrollId);
        return savedPayslips;
    }

    @Transactional
    public Payslip generatePayslipForEntry(String payrollId, String employeeId) {
        log.info("Generating payslip for employee: {} in payroll: {}", employeeId, payrollId);

        String tenantId = RequestContextHolder.getTenantId();

        // Verify payroll exists
        Payroll payroll = payrollRepository.findByPayrollIdAndTenantId(payrollId, tenantId)
                .orElseThrow(() -> new PayrollNotFoundException("Payroll", payrollId));

        // Get entry
        List<PayrollEntry> entries = payrollEntryRepository.findByPayrollIdAndEmployeeId(payrollId, employeeId);
        if (entries.isEmpty()) {
            throw new PayrollNotFoundException("PayrollEntry", employeeId);
        }

        PayrollEntry entry = entries.get(0);
        Payslip payslip = generatePayslipForEntry(payroll, entry);
        Payslip savedPayslip = payslipRepository.save(payslip);

        log.info("Generated payslip: {} for employee: {}", savedPayslip.getPayslipId(), employeeId);
        return savedPayslip;
    }

    public Payslip getById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        return payslipRepository.findById(id)
                .filter(p -> p.getTenantId().equals(tenantId))
                .orElseThrow(() -> new PayrollNotFoundException("Payslip", id));
    }

    public Payslip getByPayslipId(String payslipId) {
        String tenantId = RequestContextHolder.getTenantId();
        return payslipRepository.findByPayslipIdAndTenantId(payslipId, tenantId)
                .orElseThrow(() -> new PayrollNotFoundException("Payslip", payslipId));
    }

    public List<Payslip> getByPayrollId(String payrollId) {
        String tenantId = RequestContextHolder.getTenantId();
        return payslipRepository.findByPayrollIdAndTenantId(payrollId, tenantId);
    }

    public List<Payslip> getByEmployeeId(String employeeId) {
        String tenantId = RequestContextHolder.getTenantId();
        return payslipRepository.findByTenantIdAndEmployeeId(tenantId, employeeId);
    }

    public List<Payslip> getByEmployeeIdAndPeriod(String employeeId, YearMonth period) {
        String tenantId = RequestContextHolder.getTenantId();
        return payslipRepository.findByTenantIdAndEmployeeIdAndPeriod(tenantId, employeeId, period);
    }

    private Payslip generatePayslipForEntry(Payroll payroll, PayrollEntry entry) {
        String payslipId = generatePayslipId();

        Payslip payslip = new Payslip();
        payslip.setId(UUID.randomUUID().toString());
        payslip.setTenantId(payroll.getTenantId());
        payslip.setCountryCode(payroll.getCountryCode());
        payslip.setPayslipId(payslipId);
        payslip.setPayrollId(payroll.getPayrollId());
        payslip.setEmployeeId(entry.getEmployeeId());
        payslip.setEmployeeName(entry.getEmployeeName());
        payslip.setEmployeeCode(entry.getEmployeeCode());
        payslip.setDepartment(entry.getDepartment());
        payslip.setPosition(entry.getPosition());
        payslip.setPayPeriod(payroll.getPayrollPeriod());
        payslip.setPayDate(payroll.getPaymentDate());
        payslip.setCurrency(payroll.getCurrency());
        payslip.setGrossPay(entry.getGrossPay());
        payslip.setNetPay(entry.getNetPay());
        payslip.setTotalTax(entry.getTotalTax());
        payslip.setTotalDeductions(entry.getTotalDeductions());
        payslip.setPaymentMethod(entry.getPaymentMethod() != null ? entry.getPaymentMethod().name() : null);
        payslip.setBankAccount(entry.getBankAccountNumber());
        payslip.setPayFrequency(1);
        payslip.setPayFrequencyText(payroll.getFrequency().name());
        payslip.setTaxCode(entry.getTaxCode());
        payslip.setTaxExemptions(entry.getTaxExemptions());
        payslip.setStatus("GENERATED");
        payslip.setIsFinal(false);

        // Build earning items
        addEarningItems(payslip, entry);

        // Build tax items
        addTaxItems(payslip, entry);

        // Build deduction items
        addDeductionItems(payslip, entry);

        // Calculate totals
        payslip.calculateTotals();

        return payslip;
    }

    private void addEarningItems(Payslip payslip, PayrollEntry entry) {
        if (entry.getBasicSalary() != null && entry.getBasicSalary().compareTo(BigDecimal.ZERO) > 0) {
            payslip.addEarning(new Payslip.EarningItem(
                    "BASIC", "Basic Salary", "Basic Salary", entry.getBasicSalary(),
                    null, null, null, true, "REGULAR"
            ));
        }
        if (entry.getOvertimePay() != null && entry.getOvertimePay().compareTo(BigDecimal.ZERO) > 0) {
            payslip.addEarning(new Payslip.EarningItem(
                    "OT", "Overtime", "Overtime Pay", entry.getOvertimePay(),
                    entry.getOvertimeHours() != null ? entry.getOvertimeHours().intValue() : null,
                    entry.getOvertimeRate(), null, true, "OVERTIME"
            ));
        }
        if (entry.getBonus() != null && entry.getBonus().compareTo(BigDecimal.ZERO) > 0) {
            payslip.addEarning(new Payslip.EarningItem(
                    "BONUS", "Bonus", "Bonus", entry.getBonus(),
                    null, null, null, true, "BONUS"
            ));
        }
        if (entry.getCommission() != null && entry.getCommission().compareTo(BigDecimal.ZERO) > 0) {
            payslip.addEarning(new Payslip.EarningItem(
                    "COMM", "Commission", "Commission", entry.getCommission(),
                    null, null, null, true, "COMMISSION"
            ));
        }
        if (entry.getAllowances() != null && entry.getAllowances().compareTo(BigDecimal.ZERO) > 0) {
            payslip.addEarning(new Payslip.EarningItem(
                    "ALLOW", "Allowances", "Allowances", entry.getAllowances(),
                    null, null, null, true, "ALLOWANCE"
            ));
        }

        // Add custom earning details
        if (entry.getEarningDetails() != null) {
            for (PayrollEntry.EarningDetail detail : entry.getEarningDetails()) {
                payslip.addEarning(new Payslip.EarningItem(
                        detail.getEarningType(), detail.getEarningType(),
                        detail.getDescription(), detail.getAmount(),
                        detail.getHours(), detail.getRate(),
                        detail.getQuantity(), true, detail.getCategory()
                ));
            }
        }
    }

    private void addTaxItems(Payslip payslip, PayrollEntry entry) {
        if (entry.getFederalTax() != null && entry.getFederalTax().compareTo(BigDecimal.ZERO) > 0) {
            payslip.addTax(new Payslip.TaxItem(
                    "FED", "Federal Tax", "Federal Income Tax",
                    entry.getFederalTax(), null, entry.getGrossPay(), "FEDERAL"
            ));
        }
        if (entry.getStateTax() != null && entry.getStateTax().compareTo(BigDecimal.ZERO) > 0) {
            payslip.addTax(new Payslip.TaxItem(
                    "STATE", "State Tax", "State Income Tax",
                    entry.getStateTax(), null, entry.getGrossPay(), "STATE"
            ));
        }
        if (entry.getLocalTax() != null && entry.getLocalTax().compareTo(BigDecimal.ZERO) > 0) {
            payslip.addTax(new Payslip.TaxItem(
                    "LOCAL", "Local Tax", "Local Income Tax",
                    entry.getLocalTax(), null, entry.getGrossPay(), "LOCAL"
            ));
        }
        if (entry.getSocialSecurityTax() != null && entry.getSocialSecurityTax().compareTo(BigDecimal.ZERO) > 0) {
            payslip.addTax(new Payslip.TaxItem(
                    "SS", "Social Security", "Social Security Tax",
                    entry.getSocialSecurityTax(), null, entry.getGrossPay(), "FICA"
            ));
        }
        if (entry.getMedicareTax() != null && entry.getMedicareTax().compareTo(BigDecimal.ZERO) > 0) {
            payslip.addTax(new Payslip.TaxItem(
                    "MEDICARE", "Medicare", "Medicare Tax",
                    entry.getMedicareTax(), null, entry.getGrossPay(), "FICA"
            ));
        }
    }

    private void addDeductionItems(Payslip payslip, PayrollEntry entry) {
        if (entry.getHealthInsurance() != null && entry.getHealthInsurance().compareTo(BigDecimal.ZERO) > 0) {
            payslip.addDeduction(new Payslip.DeductionItem(
                    "HEALTH", "Health Insurance", "Health Insurance Deduction",
                    entry.getHealthInsurance(), null, false, true, "HEALTH"
            ));
        }
        if (entry.getDentalInsurance() != null && entry.getDentalInsurance().compareTo(BigDecimal.ZERO) > 0) {
            payslip.addDeduction(new Payslip.DeductionItem(
                    "DENTAL", "Dental Insurance", "Dental Insurance Deduction",
                    entry.getDentalInsurance(), null, false, true, "DENTAL"
            ));
        }
        if (entry.getRetirement401k() != null && entry.getRetirement401k().compareTo(BigDecimal.ZERO) > 0) {
            payslip.addDeduction(new Payslip.DeductionItem(
                    "401K", "401(k)", "Retirement 401(k) Contribution",
                    entry.getRetirement401k(), null, true, true, "RETIREMENT"
            ));
        }

        // Add custom deduction details
        if (entry.getDeductionDetails() != null) {
            for (PayrollEntry.DeductionDetail detail : entry.getDeductionDetails()) {
                payslip.addDeduction(new Payslip.DeductionItem(
                        detail.getDeductionType(), detail.getDeductionType(),
                        detail.getDescription(), detail.getAmount(),
                        detail.getPretax() ? null : detail.getAmount(),
                        detail.getPretax(), true, detail.getReferenceId()
                ));
            }
        }
    }

    private String generatePayslipId() {
        return "PS-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

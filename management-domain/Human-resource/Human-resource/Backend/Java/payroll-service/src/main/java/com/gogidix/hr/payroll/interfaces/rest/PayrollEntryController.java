package com.gogidix.hr.payroll.interfaces.rest;

import com.gogidix.hr.payroll.application.service.PayrollEntryCommandService;
import com.gogidix.hr.payroll.application.service.PayrollEntryQueryService;
import com.gogidix.hr.payroll.domain.enums.PaymentMethod;
import com.gogidix.hr.payroll.domain.model.PayrollEntry;
import com.gogidix.hr.payroll.domain.port.in.PayrollEntryCommand;
import com.gogidix.hr.payroll.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Payroll Entry REST Controller
 * Handles HTTP requests for payroll entry operations
 */
@RestController
@RequestMapping("/payroll-entries")
@RequiredArgsConstructor
@Tag(name = "Payroll Entries", description = "Payroll entry management endpoints")
public class PayrollEntryController {

    private final PayrollEntryCommandService payrollEntryCommandService;
    private final PayrollEntryQueryService payrollEntryQueryService;

    @PostMapping
    @Operation(summary = "Create a new payroll entry")
    public ResponseEntity<PayrollEntry> createEntry(
            @Valid @RequestBody CreateEntryRequestDto request) {
        PayrollEntryCommand.CreateEntryCommand command = PayrollEntryCommand.CreateEntryCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .countryCode(request.getCountryCode())
                .payrollId(request.getPayrollId())
                .employeeId(request.getEmployeeId())
                .employeeName(request.getEmployeeName())
                .employeeCode(request.getEmployeeCode())
                .department(request.getDepartment())
                .position(request.getPosition())
                .basicSalary(request.getBasicSalary())
                .overtimeHours(request.getOvertimeHours())
                .overtimeRate(request.getOvertimeRate())
                .bonus(request.getBonus())
                .commission(request.getCommission())
                .allowances(request.getAllowances())
                .healthInsurance(request.getHealthInsurance())
                .dentalInsurance(request.getDentalInsurance())
                .retirement401k(request.getRetirement401k())
                .paymentMethod(request.getPaymentMethod())
                .bankAccountNumber(request.getBankAccountNumber())
                .bankRoutingNumber(request.getBankRoutingNumber())
                .taxCode(request.getTaxCode())
                .taxExemptions(request.getTaxExemptions())
                .createdBy(RequestContextHolder.getUserId().orElse(null))
                .build();

        PayrollEntry entry = payrollEntryCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(entry);
    }

    @GetMapping("/{entryId}")
    @Operation(summary = "Get payroll entry by ID")
    public ResponseEntity<PayrollEntry> getEntry(
            @Parameter(description = "Entry ID") @PathVariable String entryId) {
        PayrollEntry entry = payrollEntryQueryService.getByEntryId(entryId);
        return ResponseEntity.ok(entry);
    }

    @GetMapping("/payroll/{payrollId}")
    @Operation(summary = "Get entries by payroll ID")
    public ResponseEntity<Page<PayrollEntry>> getEntriesByPayroll(
            @Parameter(description = "Payroll ID") @PathVariable String payrollId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<PayrollEntry> entries = payrollEntryQueryService.getByPayrollId(payrollId, page, size);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get entries by employee ID")
    public ResponseEntity<Page<PayrollEntry>> getEntriesByEmployee(
            @Parameter(description = "Employee ID") @PathVariable String employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<PayrollEntry> entries = payrollEntryQueryService.getByEmployeeId(employeeId, page, size);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/payroll/{payrollId}/employee/{employeeId}")
    @Operation(summary = "Get entry by payroll and employee")
    public ResponseEntity<List<PayrollEntry>> getEntryByPayrollAndEmployee(
            @Parameter(description = "Payroll ID") @PathVariable String payrollId,
            @Parameter(description = "Employee ID") @PathVariable String employeeId) {
        List<PayrollEntry> entries = payrollEntryQueryService.getByPayrollIdAndEmployeeId(payrollId, employeeId);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/department/{department}")
    @Operation(summary = "Get entries by department")
    public ResponseEntity<List<PayrollEntry>> getEntriesByDepartment(
            @Parameter(description = "Department") @PathVariable String department) {
        List<PayrollEntry> entries = payrollEntryQueryService.getByDepartment(department);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/payroll/{payrollId}/unpaid")
    @Operation(summary = "Get unpaid entries")
    public ResponseEntity<List<PayrollEntry>> getUnpaidEntries(
            @Parameter(description = "Payroll ID") @PathVariable String payrollId) {
        List<PayrollEntry> entries = payrollEntryQueryService.getUnpaidEntries(payrollId);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/payroll/{payrollId}/held")
    @Operation(summary = "Get held entries")
    public ResponseEntity<List<PayrollEntry>> getHeldEntries(
            @Parameter(description = "Payroll ID") @PathVariable String payrollId) {
        List<PayrollEntry> entries = payrollEntryQueryService.getHeldEntries(payrollId);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/payroll/{payrollId}/issues")
    @Operation(summary = "Get entries with issues")
    public ResponseEntity<List<PayrollEntry>> getEntriesWithIssues(
            @Parameter(description = "Payroll ID") @PathVariable String payrollId) {
        List<PayrollEntry> entries = payrollEntryQueryService.getEntriesWithIssues(payrollId);
        return ResponseEntity.ok(entries);
    }

    @PutMapping("/{entryId}")
    @Operation(summary = "Update payroll entry")
    public ResponseEntity<PayrollEntry> updateEntry(
            @Parameter(description = "Entry ID") @PathVariable String entryId,
            @Valid @RequestBody UpdateEntryRequestDto request) {
        PayrollEntryCommand.UpdateEntryCommand command = PayrollEntryCommand.UpdateEntryCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .entryId(entryId)
                .basicSalary(request.getBasicSalary())
                .overtimeHours(request.getOvertimeHours())
                .overtimeRate(request.getOvertimeRate())
                .bonus(request.getBonus())
                .commission(request.getCommission())
                .allowances(request.getAllowances())
                .healthInsurance(request.getHealthInsurance())
                .dentalInsurance(request.getDentalInsurance())
                .retirement401k(request.getRetirement401k())
                .paymentMethod(request.getPaymentMethod())
                .bankAccountNumber(request.getBankAccountNumber())
                .bankRoutingNumber(request.getBankRoutingNumber())
                .notes(request.getNotes())
                .updatedBy(RequestContextHolder.getUserId().orElse(null))
                .build();

        PayrollEntry entry = payrollEntryCommandService.update(command);
        return ResponseEntity.ok(entry);
    }

    @PutMapping("/{entryId}/tax")
    @Operation(summary = "Update entry tax")
    public ResponseEntity<Void> updateTax(
            @Parameter(description = "Entry ID") @PathVariable String entryId,
            @Valid @RequestBody UpdateTaxRequestDto request) {
        PayrollEntryCommand.UpdateTaxCommand command = PayrollEntryCommand.UpdateTaxCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .entryId(entryId)
                .federalTax(request.getFederalTax())
                .stateTax(request.getStateTax())
                .localTax(request.getLocalTax())
                .socialSecurityTax(request.getSocialSecurityTax())
                .medicareTax(request.getMedicareTax())
                .otherTaxes(request.getOtherTaxes())
                .updatedBy(RequestContextHolder.getUserId().orElse(null))
                .build();
        payrollEntryCommandService.updateTax(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{entryId}/hold")
    @Operation(summary = "Hold payment for entry")
    public ResponseEntity<Void> holdPayment(
            @Parameter(description = "Entry ID") @PathVariable String entryId,
            @RequestBody HoldPaymentRequestDto request) {
        PayrollEntryCommand.HoldPaymentCommand command = PayrollEntryCommand.HoldPaymentCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .entryId(entryId)
                .reason(request.getReason())
                .updatedBy(RequestContextHolder.getUserId().orElse(null))
                .build();
        payrollEntryCommandService.holdPayment(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{entryId}/release-hold")
    @Operation(summary = "Release hold on entry")
    public ResponseEntity<Void> releaseHold(
            @Parameter(description = "Entry ID") @PathVariable String entryId) {
        PayrollEntryCommand.ReleaseHoldCommand command = PayrollEntryCommand.ReleaseHoldCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .entryId(entryId)
                .updatedBy(RequestContextHolder.getUserId().orElse(null))
                .build();
        payrollEntryCommandService.releaseHold(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{entryId}")
    @Operation(summary = "Delete payroll entry")
    public ResponseEntity<Void> deleteEntry(
            @Parameter(description = "Entry ID") @PathVariable String entryId) {
        PayrollEntryCommand.DeleteEntryCommand command = PayrollEntryCommand.DeleteEntryCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .entryId(entryId)
                .deletedBy(RequestContextHolder.getUserId().orElse(null))
                .build();
        payrollEntryCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/batch")
    @Operation(summary = "Batch create payroll entries")
    public ResponseEntity<List<PayrollEntry>> batchCreate(
            @Valid @RequestBody BatchCreateRequestDto request) {
        List<PayrollEntryCommand.CreateEntryCommand> commands = request.getEntries().stream()
                .map(dto -> PayrollEntryCommand.CreateEntryCommand.builder()
                        .tenantId(RequestContextHolder.getTenantId())
                        .countryCode(dto.getCountryCode())
                        .payrollId(dto.getPayrollId())
                        .employeeId(dto.getEmployeeId())
                        .employeeName(dto.getEmployeeName())
                        .employeeCode(dto.getEmployeeCode())
                        .department(dto.getDepartment())
                        .position(dto.getPosition())
                        .basicSalary(dto.getBasicSalary())
                        .overtimeHours(dto.getOvertimeHours())
                        .overtimeRate(dto.getOvertimeRate())
                        .bonus(dto.getBonus())
                        .commission(dto.getCommission())
                        .allowances(dto.getAllowances())
                        .healthInsurance(dto.getHealthInsurance())
                        .dentalInsurance(dto.getDentalInsurance())
                        .retirement401k(dto.getRetirement401k())
                        .paymentMethod(dto.getPaymentMethod())
                        .bankAccountNumber(dto.getBankAccountNumber())
                        .bankRoutingNumber(dto.getBankRoutingNumber())
                        .taxCode(dto.getTaxCode())
                        .taxExemptions(dto.getTaxExemptions())
                        .createdBy(RequestContextHolder.getUserId().orElse(null))
                        .build())
                .toList();

        payrollEntryCommandService.batchCreate(commands);
        List<PayrollEntry> entries = payrollEntryQueryService.getByPayrollId(
                request.getEntries().get(0).getPayrollId());
        return ResponseEntity.status(HttpStatus.CREATED).body(entries);
    }

    // Request DTOs
    @Data
    public static class CreateEntryRequestDto {
        public String countryCode;
        public String payrollId;
        public String employeeId;
        public String employeeName;
        public String employeeCode;
        public String department;
        public String position;
        public BigDecimal basicSalary;
        public BigDecimal overtimeHours;
        public BigDecimal overtimeRate;
        public BigDecimal bonus;
        public BigDecimal commission;
        public BigDecimal allowances;
        public BigDecimal healthInsurance;
        public BigDecimal dentalInsurance;
        public BigDecimal retirement401k;
        public PaymentMethod paymentMethod;
        public String bankAccountNumber;
        public String bankRoutingNumber;
        public String taxCode;
        public Integer taxExemptions;
    }

    @Data
    public static class UpdateEntryRequestDto {
        public BigDecimal basicSalary;
        public BigDecimal overtimeHours;
        public BigDecimal overtimeRate;
        public BigDecimal bonus;
        public BigDecimal commission;
        public BigDecimal allowances;
        public BigDecimal healthInsurance;
        public BigDecimal dentalInsurance;
        public BigDecimal retirement401k;
        public PaymentMethod paymentMethod;
        public String bankAccountNumber;
        public String bankRoutingNumber;
        public String notes;
    }

    @Data
    public static class UpdateTaxRequestDto {
        public BigDecimal federalTax;
        public BigDecimal stateTax;
        public BigDecimal localTax;
        public BigDecimal socialSecurityTax;
        public BigDecimal medicareTax;
        public BigDecimal otherTaxes;
    }

    @Data
    public static class HoldPaymentRequestDto {
        public String reason;
    }

    @Data
    public static class BatchCreateRequestDto {
        public List<CreateEntryRequestDto> entries;
    }
}

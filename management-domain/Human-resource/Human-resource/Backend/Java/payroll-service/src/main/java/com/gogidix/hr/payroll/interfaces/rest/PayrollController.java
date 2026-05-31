package com.gogidix.hr.payroll.interfaces.rest;

import com.gogidix.hr.payroll.application.service.PayrollCommandService;
import com.gogidix.hr.payroll.application.service.PayrollQueryService;
import com.gogidix.hr.payroll.domain.model.Payroll;
import com.gogidix.hr.payroll.domain.port.in.PayrollCommand;
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

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Payroll REST Controller
 * Handles HTTP requests for payroll operations
 */
@RestController
@RequestMapping("/payrolls")
@RequiredArgsConstructor
@Tag(name = "Payrolls", description = "Payroll management endpoints")
public class PayrollController {

    private final PayrollCommandService payrollCommandService;
    private final PayrollQueryService payrollQueryService;

    @PostMapping
    @Operation(summary = "Create a new payroll")
    public ResponseEntity<Payroll> createPayroll(
            @Valid @RequestBody CreatePayrollRequestDto request) {
        PayrollCommand.CreatePayrollCommand command = PayrollCommand.CreatePayrollCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .countryCode(request.getCountryCode())
                .payrollName(request.getPayrollName())
                .payrollPeriod(request.getPayrollPeriod())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .paymentDate(request.getPaymentDate())
                .frequency(request.getFrequency())
                .currency(request.getCurrency())
                .runType(request.getRunType())
                .employeeIds(request.getEmployeeIds())
                .notes(request.getNotes())
                .createdBy(RequestContextHolder.getUserId().orElse(null))
                .build();

        Payroll payroll = payrollCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(payroll);
    }

    @GetMapping("/{payrollId}")
    @Operation(summary = "Get payroll by ID")
    public ResponseEntity<Payroll> getPayroll(
            @Parameter(description = "Payroll ID") @PathVariable String payrollId) {
        Payroll payroll = payrollQueryService.getByPayrollId(payrollId);
        return ResponseEntity.ok(payroll);
    }

    @GetMapping
    @Operation(summary = "Get all payrolls for tenant")
    public ResponseEntity<Page<Payroll>> getAllPayrolls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Payroll> payrolls = payrollQueryService.getAllForTenant(page, size);
        return ResponseEntity.ok(payrolls);
    }

    @GetMapping("/country/{countryCode}")
    @Operation(summary = "Get payrolls by country")
    public ResponseEntity<Page<Payroll>> getPayrollsByCountry(
            @Parameter(description = "Country Code") @PathVariable String countryCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Payroll> payrolls = payrollQueryService.getByCountryCode(countryCode, page, size);
        return ResponseEntity.ok(payrolls);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get payrolls by status")
    public ResponseEntity<Page<Payroll>> getPayrollsByStatus(
            @Parameter(description = "Payroll Status") @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Payroll> payrolls = payrollQueryService.getByStatus(status, page, size);
        return ResponseEntity.ok(payrolls);
    }

    @GetMapping("/period/{period}")
    @Operation(summary = "Get payroll by period")
    public ResponseEntity<List<Payroll>> getPayrollByPeriod(
            @Parameter(description = "Pay Period (YYYY-MM)") @PathVariable String period) {
        YearMonth yearMonth = YearMonth.parse(period);
        List<Payroll> payrolls = payrollQueryService.getByPeriod(yearMonth);
        return ResponseEntity.ok(payrolls);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get payrolls by date range")
    public ResponseEntity<List<Payroll>> getPayrollsByDateRange(
            @Parameter(description = "Start Date") @RequestParam LocalDate startDate,
            @Parameter(description = "End Date") @RequestParam LocalDate endDate) {
        List<Payroll> payrolls = payrollQueryService.getByDateRange(startDate, endDate);
        return ResponseEntity.ok(payrolls);
    }

    @GetMapping("/pending-approval")
    @Operation(summary = "Get payrolls pending approval")
    public ResponseEntity<Page<Payroll>> getPendingApproval(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Payroll> payrolls = payrollQueryService.getPendingApproval(page, size);
        return ResponseEntity.ok(payrolls);
    }

    @GetMapping("/processed")
    @Operation(summary = "Get processed payrolls")
    public ResponseEntity<Page<Payroll>> getProcessedPayrolls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Payroll> payrolls = payrollQueryService.getProcessedPayrolls(page, size);
        return ResponseEntity.ok(payrolls);
    }

    @GetMapping("/paid")
    @Operation(summary = "Get paid payrolls")
    public ResponseEntity<Page<Payroll>> getPaidPayrolls(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Payroll> payrolls = payrollQueryService.getPaidPayrolls(page, size);
        return ResponseEntity.ok(payrolls);
    }

    @PutMapping("/{payrollId}")
    @Operation(summary = "Update payroll")
    public ResponseEntity<Payroll> updatePayroll(
            @Parameter(description = "Payroll ID") @PathVariable String payrollId,
            @Valid @RequestBody UpdatePayrollRequestDto request) {
        PayrollCommand.UpdatePayrollCommand command = PayrollCommand.UpdatePayrollCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .payrollId(payrollId)
                .payrollName(request.getPayrollName())
                .paymentDate(request.getPaymentDate())
                .notes(request.getNotes())
                .updatedBy(RequestContextHolder.getUserId().orElse(null))
                .build();

        Payroll payroll = payrollCommandService.update(command);
        return ResponseEntity.ok(payroll);
    }

    @PostMapping("/{payrollId}/submit")
    @Operation(summary = "Submit payroll for approval")
    public ResponseEntity<Void> submitPayroll(
            @Parameter(description = "Payroll ID") @PathVariable String payrollId) {
        PayrollCommand.SubmitPayrollCommand command = PayrollCommand.SubmitPayrollCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .payrollId(payrollId)
                .submittedBy(RequestContextHolder.getUserId().orElse(null))
                .build();
        payrollCommandService.submit(command);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{payrollId}/approve")
    @Operation(summary = "Approve payroll")
    public ResponseEntity<Void> approvePayroll(
            @Parameter(description = "Payroll ID") @PathVariable String payrollId) {
        PayrollCommand.ApprovePayrollCommand command = PayrollCommand.ApprovePayrollCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .payrollId(payrollId)
                .approvedBy(RequestContextHolder.getUserId().orElse(null))
                .build();
        payrollCommandService.approve(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{payrollId}/reject")
    @Operation(summary = "Reject payroll")
    public ResponseEntity<Void> rejectPayroll(
            @Parameter(description = "Payroll ID") @PathVariable String payrollId,
            @RequestBody RejectPayrollRequestDto request) {
        PayrollCommand.RejectPayrollCommand command = PayrollCommand.RejectPayrollCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .payrollId(payrollId)
                .reason(request.getReason())
                .rejectedBy(RequestContextHolder.getUserId().orElse(null))
                .build();
        payrollCommandService.reject(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{payrollId}/process")
    @Operation(summary = "Process payroll")
    public ResponseEntity<Void> processPayroll(
            @Parameter(description = "Payroll ID") @PathVariable String payrollId) {
        PayrollCommand.ProcessPayrollCommand command = PayrollCommand.ProcessPayrollCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .payrollId(payrollId)
                .processedBy(RequestContextHolder.getUserId().orElse(null))
                .build();
        payrollCommandService.process(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{payrollId}/pay")
    @Operation(summary = "Mark payroll as paid")
    public ResponseEntity<Void> markAsPaid(
            @Parameter(description = "Payroll ID") @PathVariable String payrollId,
            @RequestBody PaymentRequestDto request) {
        PayrollCommand.MarkAsPaidCommand command = PayrollCommand.MarkAsPaidCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .payrollId(payrollId)
                .paymentReference(request.getPaymentReference())
                .processedBy(RequestContextHolder.getUserId().orElse(null))
                .build();
        payrollCommandService.markAsPaid(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{payrollId}/lock")
    @Operation(summary = "Lock payroll")
    public ResponseEntity<Void> lockPayroll(
            @Parameter(description = "Payroll ID") @PathVariable String payrollId) {
        PayrollCommand.LockPayrollCommand command = PayrollCommand.LockPayrollCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .payrollId(payrollId)
                .lockedBy(RequestContextHolder.getUserId().orElse(null))
                .build();
        payrollCommandService.lock(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{payrollId}/unlock")
    @Operation(summary = "Unlock payroll")
    public ResponseEntity<Void> unlockPayroll(
            @Parameter(description = "Payroll ID") @PathVariable String payrollId) {
        PayrollCommand.UnlockPayrollCommand command = PayrollCommand.UnlockPayrollCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .payrollId(payrollId)
                .unlockedBy(RequestContextHolder.getUserId().orElse(null))
                .build();
        payrollCommandService.unlock(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{payrollId}/cancel")
    @Operation(summary = "Cancel payroll")
    public ResponseEntity<Void> cancelPayroll(
            @Parameter(description = "Payroll ID") @PathVariable String payrollId,
            @RequestBody CancelPayrollRequestDto request) {
        PayrollCommand.CancelPayrollCommand command = PayrollCommand.CancelPayrollCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .payrollId(payrollId)
                .reason(request.getReason())
                .cancelledBy(RequestContextHolder.getUserId().orElse(null))
                .build();
        payrollCommandService.cancel(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{payrollId}/employees/{employeeId}")
    @Operation(summary = "Add employee to payroll")
    public ResponseEntity<Void> addEmployee(
            @Parameter(description = "Payroll ID") @PathVariable String payrollId,
            @Parameter(description = "Employee ID") @PathVariable String employeeId) {
        PayrollCommand.AddEmployeeCommand command = PayrollCommand.AddEmployeeCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .payrollId(payrollId)
                .employeeId(employeeId)
                .addedBy(RequestContextHolder.getUserId().orElse(null))
                .build();
        payrollCommandService.addEmployee(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{payrollId}/employees/{employeeId}")
    @Operation(summary = "Remove employee from payroll")
    public ResponseEntity<Void> removeEmployee(
            @Parameter(description = "Payroll ID") @PathVariable String payrollId,
            @Parameter(description = "Employee ID") @PathVariable String employeeId,
            @RequestBody RemoveEmployeeRequestDto request) {
        PayrollCommand.RemoveEmployeeCommand command = PayrollCommand.RemoveEmployeeCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .payrollId(payrollId)
                .employeeId(employeeId)
                .reason(request.getReason())
                .removedBy(RequestContextHolder.getUserId().orElse(null))
                .build();
        payrollCommandService.removeEmployee(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{payrollId}")
    @Operation(summary = "Delete payroll")
    public ResponseEntity<Void> deletePayroll(
            @Parameter(description = "Payroll ID") @PathVariable String payrollId) {
        PayrollCommand.DeletePayrollCommand command = PayrollCommand.DeletePayrollCommand.builder()
                .tenantId(RequestContextHolder.getTenantId())
                .payrollId(payrollId)
                .deletedBy(RequestContextHolder.getUserId().orElse(null))
                .build();
        payrollCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    // Request DTOs
    @Data
    public static class CreatePayrollRequestDto {
        public String countryCode;
        public String payrollName;
        public YearMonth payrollPeriod;
        public LocalDate startDate;
        public LocalDate endDate;
        public LocalDate paymentDate;
        public String frequency;
        public String currency;
        public String runType;
        public List<String> employeeIds;
        public String notes;
    }

    @Data
    public static class UpdatePayrollRequestDto {
        public String payrollName;
        public LocalDate paymentDate;
        public String notes;
    }

    @Data
    public static class RejectPayrollRequestDto {
        public String reason;
    }

    @Data
    public static class PaymentRequestDto {
        public String paymentReference;
    }

    @Data
    public static class CancelPayrollRequestDto {
        public String reason;
    }

    @Data
    public static class RemoveEmployeeRequestDto {
        public String reason;
    }
}

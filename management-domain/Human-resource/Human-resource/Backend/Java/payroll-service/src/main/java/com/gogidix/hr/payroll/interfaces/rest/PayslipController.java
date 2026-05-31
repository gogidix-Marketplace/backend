package com.gogidix.hr.payroll.interfaces.rest;

import com.gogidix.hr.payroll.application.service.PayslipService;
import com.gogidix.hr.payroll.domain.model.Payslip;
import com.gogidix.hr.payroll.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

/**
 * Payslip REST Controller
 * Handles HTTP requests for payslip operations
 */
@RestController
@RequestMapping("/payslips")
@RequiredArgsConstructor
@Tag(name = "Payslips", description = "Payslip management endpoints")
public class PayslipController {

    private final PayslipService payslipService;

    @GetMapping("/{payslipId}")
    @Operation(summary = "Get payslip by ID")
    public ResponseEntity<Payslip> getPayslip(
            @Parameter(description = "Payslip ID") @PathVariable String payslipId) {
        Payslip payslip = payslipService.getByPayslipId(payslipId);
        return ResponseEntity.ok(payslip);
    }

    @GetMapping("/payroll/{payrollId}")
    @Operation(summary = "Get payslips by payroll ID")
    public ResponseEntity<List<Payslip>> getPayslipsByPayroll(
            @Parameter(description = "Payroll ID") @PathVariable String payrollId) {
        List<Payslip> payslips = payslipService.getByPayrollId(payrollId);
        return ResponseEntity.ok(payslips);
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get payslips by employee ID")
    public ResponseEntity<List<Payslip>> getPayslipsByEmployee(
            @Parameter(description = "Employee ID") @PathVariable String employeeId) {
        List<Payslip> payslips = payslipService.getByEmployeeId(employeeId);
        return ResponseEntity.ok(payslips);
    }

    @GetMapping("/employee/{employeeId}/period/{period}")
    @Operation(summary = "Get payslips by employee and period")
    public ResponseEntity<List<Payslip>> getPayslipsByEmployeeAndPeriod(
            @Parameter(description = "Employee ID") @PathVariable String employeeId,
            @Parameter(description = "Pay Period (YYYY-MM)") @PathVariable String period) {
        YearMonth yearMonth = YearMonth.parse(period);
        List<Payslip> payslips = payslipService.getByEmployeeIdAndPeriod(employeeId, yearMonth);
        return ResponseEntity.ok(payslips);
    }

    @PostMapping("/payroll/{payrollId}/generate")
    @Operation(summary = "Generate payslips for payroll")
    public ResponseEntity<List<Payslip>> generatePayslips(
            @Parameter(description = "Payroll ID") @PathVariable String payrollId) {
        List<Payslip> payslips = payslipService.generatePayslips(payrollId);
        return ResponseEntity.status(HttpStatus.CREATED).body(payslips);
    }

    @PostMapping("/payroll/{payrollId}/employee/{employeeId}/generate")
    @Operation(summary = "Generate payslip for specific employee")
    public ResponseEntity<Payslip> generatePayslipForEmployee(
            @Parameter(description = "Payroll ID") @PathVariable String payrollId,
            @Parameter(description = "Employee ID") @PathVariable String employeeId) {
        Payslip payslip = payslipService.generatePayslipForEntry(payrollId, employeeId);
        return ResponseEntity.status(HttpStatus.CREATED).body(payslip);
    }

    @GetMapping("/employee/{employeeId}/current")
    @Operation(summary = "Get current period payslip for employee")
    public ResponseEntity<Payslip> getCurrentPayslip(
            @Parameter(description = "Employee ID") @PathVariable String employeeId) {
        YearMonth currentPeriod = YearMonth.now();
        List<Payslip> payslips = payslipService.getByEmployeeIdAndPeriod(employeeId, currentPeriod);
        return ResponseEntity.ok(payslips.isEmpty() ? null : payslips.get(0));
    }
}

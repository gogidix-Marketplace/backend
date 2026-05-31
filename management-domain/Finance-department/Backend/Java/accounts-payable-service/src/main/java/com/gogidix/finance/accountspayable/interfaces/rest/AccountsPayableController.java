package com.gogidix.finance.accountspayable.interfaces.rest;

import com.gogidix.finance.accountspayable.application.dto.response.APSummaryDto;
import com.gogidix.finance.accountspayable.application.service.AccountsPayableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * Accounts Payable REST Controller
 * Handles HTTP requests for AP summary and operations
 */
@RestController
@RequestMapping("/ap")
@RequiredArgsConstructor
@Tag(name = "Accounts Payable", description = "Accounts payable summary endpoints")
public class AccountsPayableController {

    private final AccountsPayableService accountsPayableService;

    @GetMapping("/summary")
    @Operation(summary = "Get AP summary for tenant")
    public ResponseEntity<APSummaryDto> getSummary() {
        APSummaryDto summary = accountsPayableService.getSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/summary/vendor/{vendorId}")
    @Operation(summary = "Get AP summary for specific vendor")
    public ResponseEntity<APSummaryDto> getVendorSummary(
            @PathVariable String vendorId) {
        APSummaryDto summary = accountsPayableService.getVendorSummary(vendorId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/cash-requirements")
    @Operation(summary = "Get cash requirements for period")
    public ResponseEntity<CashRequirementsResponseDto> getCashRequirements(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate) {
        BigDecimal cashRequired = accountsPayableService.getCashRequirements(fromDate, toDate);
        return ResponseEntity.ok(new CashRequirementsResponseDto(fromDate, toDate, cashRequired));
    }

    @GetMapping("/aging-report")
    @Operation(summary = "Get aging report")
    public ResponseEntity<AgingReportResponseDto> getAgingReport() {
        AccountsPayableService.AgingReport agingReport = accountsPayableService.getAgingReport();
        return ResponseEntity.ok(new AgingReportResponseDto(agingReport));
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get AP statistics")
    public ResponseEntity<APStatisticsDto> getStatistics() {
        APSummaryDto summary = accountsPayableService.getSummary();
        return ResponseEntity.ok(APStatisticsDto.fromSummary(summary));
    }

    // Response DTOs
    public record CashRequirementsResponseDto(
        LocalDate fromDate,
        LocalDate toDate,
        BigDecimal cashRequired
    ) {}

    public record AgingReportResponseDto(
        BigDecimal current,
        BigDecimal days1to30,
        BigDecimal days31to60,
        BigDecimal days61to90,
        BigDecimal over90,
        BigDecimal total
    ) {
        public AgingReportResponseDto(AccountsPayableService.AgingReport report) {
            this(
                report.current(),
                report.days1to30(),
                report.days31to60(),
                report.days61to90(),
                report.over90(),
                report.current().add(report.days1to30())
                    .add(report.days31to60())
                    .add(report.days61to90())
                    .add(report.over90())
            );
        }
    }

    public record APStatisticsDto(
        // Invoice statistics
        Long totalInvoices,
        Long pendingInvoices,
        Long approvedInvoices,
        Long overdueInvoices,
        BigDecimal pendingAmount,
        BigDecimal approvedAmount,
        BigDecimal overdueAmount,
        BigDecimal totalOutstanding,
        // Payment statistics
        Long totalPayments,
        Long completedPayments,
        BigDecimal totalPaymentAmount,
        // Vendor statistics
        Long totalVendors,
        Long activeVendors
    ) {
        public static APStatisticsDto fromSummary(APSummaryDto summary) {
            return new APStatisticsDto(
                summary.getTotalInvoices(),
                summary.getPendingInvoices(),
                summary.getApprovedInvoices(),
                summary.getOverdueInvoices(),
                summary.getPendingAmount(),
                summary.getApprovedAmount(),
                summary.getOverdueAmount(),
                summary.getTotalOutstanding(),
                summary.getTotalPayments(),
                summary.getCompletedPayments(),
                summary.getTotalPaymentAmount(),
                summary.getTotalVendors(),
                summary.getActiveVendors()
            );
        }
    }
}

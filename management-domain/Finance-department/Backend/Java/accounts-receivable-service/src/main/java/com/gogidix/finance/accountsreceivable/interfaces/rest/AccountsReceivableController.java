package com.gogidix.finance.accountsreceivable.interfaces.rest;

import com.gogidix.finance.accountsreceivable.application.service.AccountsReceivableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Accounts Receivable REST Controller
 * Handles HTTP requests for AR operations
 */
@RestController
@RequestMapping("/ar")
@RequiredArgsConstructor
@Tag(name = "Accounts Receivable", description = "AR management endpoints")
public class AccountsReceivableController {

    private final AccountsReceivableService accountsReceivableService;

    @PostMapping("/record-payment")
    @Operation(summary = "Record payment and apply to invoice")
    public ResponseEntity<Void> recordPaymentAndApply(
            @RequestParam String customerId,
            @RequestParam String invoiceId,
            @RequestParam java.math.BigDecimal amount,
            @RequestParam String paymentMethod,
            @RequestParam LocalDate paymentDate,
            @RequestParam(required = false) String referenceNumber) {

        accountsReceivableService.recordPaymentAndApply(
            customerId, invoiceId, amount, paymentMethod, paymentDate, referenceNumber);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/process-invoice")
    @Operation(summary = "Process invoice and update customer metrics")
    public ResponseEntity<Void> processInvoice(
            @RequestParam String customerId,
            @RequestParam String invoiceId) {

        accountsReceivableService.processInvoice(customerId, invoiceId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/aging-report")
    @Operation(summary = "Get AR aging report")
    public ResponseEntity<AccountsReceivableService.AgingReport> getAgingReport() {
        AccountsReceivableService.AgingReport report = accountsReceivableService.getAgingReport();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/customer/{customerId}/summary")
    @Operation(summary = "Get customer AR summary")
    public ResponseEntity<AccountsReceivableService.CustomerARSummary> getCustomerARSummary(
            @PathVariable String customerId) {

        AccountsReceivableService.CustomerARSummary summary =
            accountsReceivableService.getCustomerARSummary(customerId);

        return ResponseEntity.ok(summary);
    }

    @PostMapping("/update-collection-stages")
    @Operation(summary = "Update collection stages for all customers")
    public ResponseEntity<Void> updateCollectionStages() {
        accountsReceivableService.updateCollectionStages();
        return ResponseEntity.ok().build();
    }
}

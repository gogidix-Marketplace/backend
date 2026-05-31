package com.gogidix.finance.budgettracking.interfaces.rest;

import com.gogidix.finance.budgettracking.application.dto.response.BudgetTransactionResponseDto;
import com.gogidix.finance.budgettracking.application.service.BudgetTransactionService;
import com.gogidix.finance.budgettracking.domain.model.BudgetTransaction;
import com.gogidix.finance.budgettracking.domain.port.in.BudgetTransactionCommand;
import com.gogidix.finance.budgettracking.domain.port.in.BudgetTrackingQuery;
import com.gogidix.finance.budgettracking.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Budget Transaction REST Controller
 * Handles HTTP requests for budget transaction operations
 */
@RestController
@RequestMapping("/budget-transactions")
@RequiredArgsConstructor
@Tag(name = "Budget Transactions", description = "Budget transaction management endpoints")
public class BudgetTransactionController {

    private final BudgetTransactionService budgetTransactionService;

    @PostMapping
    @Operation(summary = "Create a new budget transaction")
    public ResponseEntity<BudgetTransactionResponseDto> createTransaction(
            @Valid @RequestBody CreateTransactionRequestDto request) {
        BudgetTransactionCommand.CreateTransactionCommand command =
            new BudgetTransactionCommand.CreateTransactionCommand();
        command.setTenantId(RequestContextHolder.getTenantId());
        command.setBudgetId(request.getBudgetId());
        command.setBudgetCode(request.getBudgetCode());
        command.setTransactionType(request.getTransactionType());
        command.setAmount(request.getAmount());
        command.setCurrency(request.getCurrency());
        command.setDescription(request.getDescription());
        command.setReferenceType(request.getReferenceType());
        command.setReferenceId(request.getReferenceId());
        command.setCategory(request.getCategory());
        command.setDepartment(request.getDepartment());
        command.setCostCenter(request.getCostCenter());
        command.setProjectId(request.getProjectId());
        command.setTags(request.getTags());
        command.setNotes(request.getNotes());
        command.setCorrelationId(request.getCorrelationId());

        BudgetTransaction transaction = budgetTransactionService.createTransaction(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(transaction));
    }

    @PostMapping("/{transactionId}/record")
    @Operation(summary = "Record a budget transaction")
    public ResponseEntity<BudgetTransactionResponseDto> recordTransaction(
            @Parameter(description = "Transaction ID") @PathVariable String transactionId) {

        BudgetTransaction transaction = budgetTransactionService.recordTransaction(transactionId);
        return ResponseEntity.ok(toDto(transaction));
    }

    @PostMapping("/{transactionId}/approve")
    @Operation(summary = "Approve a budget transaction")
    public ResponseEntity<Void> approveTransaction(
            @Parameter(description = "Transaction ID") @PathVariable String transactionId) {

        budgetTransactionService.approveTransaction(transactionId, RequestContextHolder.getUserId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{transactionId}/reject")
    @Operation(summary = "Reject a budget transaction")
    public ResponseEntity<Void> rejectTransaction(
            @Parameter(description = "Transaction ID") @PathVariable String transactionId,
            @RequestBody RejectionRequestDto request) {

        budgetTransactionService.rejectTransaction(transactionId,
            RequestContextHolder.getUserId(), request.getReason());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{transactionId}/reverse")
    @Operation(summary = "Reverse a budget transaction")
    public ResponseEntity<Void> reverseTransaction(
            @Parameter(description = "Transaction ID") @PathVariable String transactionId,
            @RequestBody ReversalRequestDto request) {

        budgetTransactionService.reverseTransaction(transactionId, request.getReason());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{transactionId}")
    @Operation(summary = "Get budget transaction by ID")
    public ResponseEntity<BudgetTransactionResponseDto> getTransaction(
            @Parameter(description = "Transaction ID") @PathVariable String transactionId) {

        BudgetTransaction transaction = budgetTransactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(toDto(transaction));
    }

    @GetMapping("/budget/{budgetId}")
    @Operation(summary = "Get transactions by budget ID")
    public ResponseEntity<List<BudgetTransactionResponseDto>> getTransactionsByBudget(
            @Parameter(description = "Budget ID") @PathVariable String budgetId) {

        List<BudgetTransaction> transactions =
            budgetTransactionService.getTransactionsByBudget(budgetId);
        return ResponseEntity.ok(transactions.stream()
            .map(this::toDto)
            .collect(Collectors.toList()));
    }

    @GetMapping
    @Operation(summary = "Get all transactions for tenant")
    public ResponseEntity<List<BudgetTransactionResponseDto>> getAllTransactions() {
        List<BudgetTransaction> transactions = budgetTransactionService.getAllTransactions();
        return ResponseEntity.ok(transactions.stream()
            .map(this::toDto)
            .collect(Collectors.toList()));
    }

    @GetMapping("/search")
    @Operation(summary = "Search transactions")
    public ResponseEntity<List<BudgetTransactionResponseDto>> searchTransactions(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) BudgetTransaction.TransactionType transactionType,
            @RequestParam(required = false) BudgetTransaction.TransactionStatus status,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        BudgetTrackingQuery.SearchTransactionsQuery query =
            new BudgetTrackingQuery.SearchTransactionsQuery();
        query.setTenantId(RequestContextHolder.getTenantId());
        query.setCategory(category);
        query.setDepartment(department);
        query.setTransactionType(transactionType != null ? transactionType.name() : null);
        query.setStatus(status != null ? status.name() : null);
        query.setStartDate(startDate);
        query.setEndDate(endDate);

        List<BudgetTransaction> transactions = budgetTransactionService.searchTransactions(query);
        return ResponseEntity.ok(transactions.stream()
            .map(this::toDto)
            .collect(Collectors.toList()));
    }

    @GetMapping("/reference/{referenceType}/{referenceId}")
    @Operation(summary = "Get transactions by reference")
    public ResponseEntity<List<BudgetTransactionResponseDto>> getTransactionsByReference(
            @Parameter(description = "Reference Type") @PathVariable String referenceType,
            @Parameter(description = "Reference ID") @PathVariable String referenceId) {

        List<BudgetTransaction> transactions =
            budgetTransactionService.getTransactionsByReference(referenceType, referenceId);
        return ResponseEntity.ok(transactions.stream()
            .map(this::toDto)
            .collect(Collectors.toList()));
    }

    private BudgetTransactionResponseDto toDto(BudgetTransaction transaction) {
        return BudgetTransactionResponseDto.builder()
            .id(transaction.getId())
            .transactionId(transaction.getTransactionId())
            .tenantId(transaction.getTenantId())
            .budgetId(transaction.getBudgetId())
            .budgetCode(transaction.getBudgetCode())
            .referenceType(transaction.getReferenceType())
            .referenceId(transaction.getReferenceId())
            .transactionType(mapTransactionType(transaction.getTransactionType()))
            .amount(transaction.getAmount())
            .currency(transaction.getCurrency())
            .description(transaction.getDescription())
            .status(mapTransactionStatus(transaction.getStatus()))
            .transactionDate(transaction.getTransactionDate())
            .category(transaction.getCategory())
            .department(transaction.getDepartment())
            .costCenter(transaction.getCostCenter())
            .projectId(transaction.getProjectId())
            .recordedBy(transaction.getRecordedBy())
            .approvedBy(transaction.getApprovedBy())
            .approvedAt(transaction.getApprovedAt())
            .rejectionReason(transaction.getRejectionReason())
            .relatedBudgetPeriod(transaction.getRelatedBudgetPeriod())
            .balanceBefore(transaction.getBalanceBefore())
            .balanceAfter(transaction.getBalanceAfter())
            .tags(transaction.getTags())
            .notes(transaction.getNotes())
            .correlationId(transaction.getCorrelationId())
            .createdAt(transaction.getCreatedAt())
            .updatedAt(transaction.getUpdatedAt())
            .build();
    }

    private BudgetTransactionResponseDto.TransactionTypeDto mapTransactionType(
            BudgetTransaction.TransactionType type) {
        return type != null ? BudgetTransactionResponseDto.TransactionTypeDto.valueOf(type.name()) : null;
    }

    private BudgetTransactionResponseDto.TransactionStatusDto mapTransactionStatus(
            BudgetTransaction.TransactionStatus status) {
        return status != null ? BudgetTransactionResponseDto.TransactionStatusDto.valueOf(status.name()) : null;
    }

    // Request DTOs
    @lombok.Data
    @lombok.NoArgsConstructor
    public static class CreateTransactionRequestDto {
        private String budgetId;
        private String budgetCode;
        private String referenceType;
        private String referenceId;
        private BudgetTransaction.TransactionType transactionType;
        private BigDecimal amount;
        private String currency;
        private String description;
        private String category;
        private String department;
        private String costCenter;
        private String projectId;
        private List<String> tags;
        private String notes;
        private String correlationId;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    public static class RejectionRequestDto {
        private String reason;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    public static class ReversalRequestDto {
        private String reason;
    }
}

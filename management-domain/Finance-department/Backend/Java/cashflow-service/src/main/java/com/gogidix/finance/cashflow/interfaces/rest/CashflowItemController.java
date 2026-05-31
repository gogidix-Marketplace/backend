package com.gogidix.finance.cashflow.interfaces.rest;

import com.gogidix.finance.cashflow.application.dto.response.CashflowItemResponseDto;
import com.gogidix.finance.cashflow.application.service.CashflowItemService;
import com.gogidix.finance.cashflow.application.service.CashflowQueryService;
import com.gogidix.finance.cashflow.domain.model.CashflowItem;
import com.gogidix.finance.cashflow.domain.port.in.CashflowItemCommand;
import com.gogidix.finance.cashflow.shared.requestcontext.RequestContextHolder;
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
import java.time.LocalDate;
import java.util.List;

/**
 * Cashflow Item REST Controller
 * Handles HTTP requests for cashflow item operations
 */
@RestController
@RequestMapping("/cashflow-items")
@RequiredArgsConstructor
@Tag(name = "Cashflow Items", description = "Cashflow item management endpoints")
public class CashflowItemController {

    private final CashflowItemService cashflowItemService;
    private final CashflowQueryService cashflowQueryService;

    @PostMapping
    @Operation(summary = "Create a new cashflow item")
    public ResponseEntity<CashflowItemResponseDto> createCashflowItem(
            @Valid @RequestBody CreateCashflowItemRequestDto request) {
        CashflowItemCommand.CreateCashflowItemCommand command = new CashflowItemCommand.CreateCashflowItemCommand(
                RequestContextHolder.getTenantId(),
                RequestContextHolder.getUserIdOrDefault(null),
                request.getReference(),
                request.getType(),
                request.getCategory(),
                request.getAmount(),
                request.getCurrency(),
                request.getTransactionDate(),
                request.getExpectedDate(),
                request.getDescription(),
                request.getCounterparty(),
                request.getAccount(),
                request.getCostCenter(),
                request.getProjectId(),
                request.getRecurring(),
                request.getRecurringFrequency(),
                request.getParentRecurringItemId(),
                request.getPaymentMethod(),
                request.getTaxAmount(),
                request.getTags(),
                request.getNotes(),
                request.getLinkedExpenseId(),
                request.getLinkedRevenueId()
        );

        CashflowItem item = cashflowItemService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(item));
    }

    @PostMapping("/bulk")
    @Operation(summary = "Bulk create cashflow items")
    public ResponseEntity<List<CashflowItemResponseDto>> bulkCreateCashflowItems(
            @Valid @RequestBody BulkCreateRequestDto request) {
        List<CashflowItemCommand.CreateCashflowItemCommand> commandItems = request.getItems().stream()
                .map(dto -> new CashflowItemCommand.CreateCashflowItemCommand(
                        RequestContextHolder.getTenantId(),
                        RequestContextHolder.getUserIdOrDefault(null),
                        dto.getReference(),
                        dto.getType(),
                        dto.getCategory(),
                        dto.getAmount(),
                        dto.getCurrency(),
                        dto.getTransactionDate(),
                        dto.getExpectedDate(),
                        dto.getDescription(),
                        dto.getCounterparty(),
                        dto.getAccount(),
                        dto.getCostCenter(),
                        dto.getProjectId(),
                        dto.getRecurring(),
                        dto.getRecurringFrequency(),
                        dto.getParentRecurringItemId(),
                        dto.getPaymentMethod(),
                        dto.getTaxAmount(),
                        dto.getTags(),
                        dto.getNotes(),
                        dto.getLinkedExpenseId(),
                        dto.getLinkedRevenueId()
                ))
                .toList();

        CashflowItemCommand.BulkCreateCommand command = new CashflowItemCommand.BulkCreateCommand(
                RequestContextHolder.getTenantId(),
                RequestContextHolder.getUserIdOrDefault(null),
                commandItems
        );

        List<CashflowItem> items = cashflowItemService.bulkCreate(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(items.stream().map(this::toDto).toList());
    }

    @GetMapping("/{cashflowItemId}")
    @Operation(summary = "Get cashflow item by ID")
    public ResponseEntity<CashflowItemResponseDto> getCashflowItem(
            @Parameter(description = "Cashflow Item ID") @PathVariable String cashflowItemId) {
        CashflowItem item = cashflowQueryService.getCashflowItemById(cashflowItemId);
        return ResponseEntity.ok(toDto(item));
    }

    @GetMapping
    @Operation(summary = "Get all cashflow items for tenant")
    public ResponseEntity<List<CashflowItemResponseDto>> getAllCashflowItems() {
        List<CashflowItem> items = cashflowQueryService.getAllCashflowItemsForTenant();
        return ResponseEntity.ok(items.stream().map(this::toDto).toList());
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get cashflow items by date range")
    public ResponseEntity<Page<CashflowItemResponseDto>> getCashflowItemsByDateRange(
            @Parameter(description = "Start Date") @RequestParam LocalDate startDate,
            @Parameter(description = "End Date") @RequestParam LocalDate endDate,
            @RequestParam(required = false) CashflowItem.CashflowType type,
            @RequestParam(required = false) List<CashflowItem.ItemStatus> statuses,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<CashflowItem> items = cashflowQueryService.getCashflowItemsByDateRange(
                startDate, endDate, type, statuses, page, size);

        return ResponseEntity.ok(items.map(this::toDto));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get cashflow items by type")
    public ResponseEntity<Page<CashflowItemResponseDto>> getCashflowItemsByType(
            @Parameter(description = "Cashflow Type") @PathVariable CashflowItem.CashflowType type,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<CashflowItem> items = cashflowQueryService.getCashflowItemsByType(
                type, startDate, endDate, page, size);

        return ResponseEntity.ok(items.map(this::toDto));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get cashflow items by category")
    public ResponseEntity<Page<CashflowItemResponseDto>> getCashflowItemsByCategory(
            @Parameter(description = "Cashflow Category") @PathVariable CashflowItem.CashflowCategory category,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<CashflowItem> items = cashflowQueryService.getCashflowItemsByCategory(
                category, startDate, endDate, page, size);

        return ResponseEntity.ok(items.map(this::toDto));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get cashflow items by status")
    public ResponseEntity<Page<CashflowItemResponseDto>> getCashflowItemsByStatus(
            @Parameter(description = "Item Status") @PathVariable CashflowItem.ItemStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<CashflowItem> items = cashflowQueryService.getCashflowItemsByStatus(status, page, size);

        return ResponseEntity.ok(items.map(this::toDto));
    }

    @PutMapping("/{cashflowItemId}")
    @Operation(summary = "Update cashflow item")
    public ResponseEntity<CashflowItemResponseDto> updateCashflowItem(
            @Parameter(description = "Cashflow Item ID") @PathVariable String cashflowItemId,
            @Valid @RequestBody UpdateCashflowItemRequestDto request) {

        CashflowItemCommand.UpdateCashflowItemCommand command = new CashflowItemCommand.UpdateCashflowItemCommand(
                RequestContextHolder.getTenantId(),
                cashflowItemId,
                request.getDescription(),
                request.getAmount(),
                request.getExpectedDate(),
                request.getTransactionDate(),
                request.getCounterparty(),
                request.getAccount(),
                request.getCostCenter(),
                request.getTags(),
                request.getNotes(),
                request.getTaxAmount()
        );

        CashflowItem item = cashflowItemService.update(command);
        return ResponseEntity.ok(toDto(item));
    }

    @PostMapping("/{cashflowItemId}/mark-expected")
    @Operation(summary = "Mark cashflow item as expected")
    public ResponseEntity<Void> markAsExpected(
            @Parameter(description = "Cashflow Item ID") @PathVariable String cashflowItemId) {

        CashflowItemCommand.MarkAsExpectedCommand command = new CashflowItemCommand.MarkAsExpectedCommand(
                RequestContextHolder.getTenantId(), cashflowItemId);

        cashflowItemService.markAsExpected(command);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{cashflowItemId}/commit")
    @Operation(summary = "Commit cashflow item")
    public ResponseEntity<Void> commitCashflowItem(
            @Parameter(description = "Cashflow Item ID") @PathVariable String cashflowItemId) {

        CashflowItemCommand.CommitCashflowItemCommand command = new CashflowItemCommand.CommitCashflowItemCommand(
                RequestContextHolder.getTenantId(), cashflowItemId);

        cashflowItemService.commit(command);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{cashflowItemId}/settle")
    @Operation(summary = "Settle cashflow item")
    public ResponseEntity<Void> settleCashflowItem(
            @Parameter(description = "Cashflow Item ID") @PathVariable String cashflowItemId,
            @RequestBody SettleRequestDto request) {

        CashflowItemCommand.SettleCashflowItemCommand command = new CashflowItemCommand.SettleCashflowItemCommand(
                RequestContextHolder.getTenantId(),
                cashflowItemId,
                request.getBankReference()
        );

        cashflowItemService.settle(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{cashflowItemId}/cancel")
    @Operation(summary = "Cancel cashflow item")
    public ResponseEntity<Void> cancelCashflowItem(
            @Parameter(description = "Cashflow Item ID") @PathVariable String cashflowItemId,
            @RequestBody CancelRequestDto request) {

        CashflowItemCommand.CancelCashflowItemCommand command = new CashflowItemCommand.CancelCashflowItemCommand(
                RequestContextHolder.getTenantId(),
                cashflowItemId,
                request.getReason()
        );

        cashflowItemService.cancel(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{cashflowItemId}/mark-failed")
    @Operation(summary = "Mark cashflow item as failed")
    public ResponseEntity<Void> markAsFailed(
            @Parameter(description = "Cashflow Item ID") @PathVariable String cashflowItemId,
            @RequestBody MarkFailedRequestDto request) {

        CashflowItemCommand.MarkAsFailedCommand command = new CashflowItemCommand.MarkAsFailedCommand(
                RequestContextHolder.getTenantId(),
                cashflowItemId,
                request.getReason()
        );

        cashflowItemService.markAsFailed(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{cashflowItemId}/setup-recurring")
    @Operation(summary = "Setup recurring for cashflow item")
    public ResponseEntity<Void> setupRecurring(
            @Parameter(description = "Cashflow Item ID") @PathVariable String cashflowItemId,
            @RequestBody RecurringSetupRequestDto request) {

        CashflowItemCommand.SetupRecurringCommand command = new CashflowItemCommand.SetupRecurringCommand(
                RequestContextHolder.getTenantId(),
                cashflowItemId,
                request.getFrequency()
        );

        cashflowItemService.setupRecurring(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{cashflowItemId}")
    @Operation(summary = "Delete cashflow item")
    public ResponseEntity<Void> deleteCashflowItem(
            @Parameter(description = "Cashflow Item ID") @PathVariable String cashflowItemId) {

        CashflowItemCommand.DeleteCashflowItemCommand command = new CashflowItemCommand.DeleteCashflowItemCommand(
                RequestContextHolder.getTenantId(), cashflowItemId);

        cashflowItemService.delete(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending cashflow items")
    public ResponseEntity<List<CashflowItemResponseDto>> getPendingCashflowItems(
            @RequestParam(required = false) LocalDate dueDate) {

        List<CashflowItem> items = cashflowQueryService.getPendingCashflowItems(dueDate);
        return ResponseEntity.ok(items.stream().map(this::toDto).toList());
    }

    private CashflowItemResponseDto toDto(CashflowItem item) {
        return CashflowItemResponseDto.builder()
                .id(item.getId())
                .cashflowItemId(item.getCashflowItemId())
                .tenantId(item.getTenantId())
                .recordedBy(item.getRecordedBy())
                .reference(item.getReference())
                .type(mapType(item.getType()))
                .category(mapCategory(item.getCategory()))
                .amount(item.getAmount())
                .currency(item.getCurrency())
                .transactionDate(item.getTransactionDate())
                .expectedDate(item.getExpectedDate())
                .settledDate(item.getSettledDate())
                .description(item.getDescription())
                .counterparty(item.getCounterparty())
                .account(item.getAccount())
                .costCenter(item.getCostCenter())
                .projectId(item.getProjectId())
                .status(mapStatus(item.getStatus()))
                .recurring(item.getRecurring())
                .recurringFrequency(mapRecurringFrequency(item.getRecurringFrequency()))
                .parentRecurringItemId(item.getParentRecurringItemId())
                .expectedAt(item.getExpectedAt())
                .settledAt(item.getSettledAt())
                .paymentMethod(item.getPaymentMethod())
                .bankReference(item.getBankReference())
                .invoiceReference(item.getInvoiceReference())
                .taxAmount(item.getTaxAmount())
                .netAmount(item.getNetAmount())
                .tags(item.getTags())
                .notes(item.getNotes())
                .linkedExpenseId(item.getLinkedExpenseId())
                .linkedRevenueId(item.getLinkedRevenueId())
                .allocationPercentage(item.getAllocationPercentage())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }

    private CashflowItemResponseDto.CashflowTypeDto mapType(CashflowItem.CashflowType type) {
        return type != null ? CashflowItemResponseDto.CashflowTypeDto.valueOf(type.name()) : null;
    }

    private CashflowItemResponseDto.CashflowCategoryDto mapCategory(CashflowItem.CashflowCategory category) {
        return category != null ? CashflowItemResponseDto.CashflowCategoryDto.valueOf(category.name()) : null;
    }

    private CashflowItemResponseDto.ItemStatusDto mapStatus(CashflowItem.ItemStatus status) {
        return status != null ? CashflowItemResponseDto.ItemStatusDto.valueOf(status.name()) : null;
    }

    private CashflowItemResponseDto.RecurringFrequencyDto mapRecurringFrequency(CashflowItem.RecurringFrequency frequency) {
        return frequency != null ? CashflowItemResponseDto.RecurringFrequencyDto.valueOf(frequency.name()) : null;
    }

    // Request DTOs
    @Data
    public static class CreateCashflowItemRequestDto {
        public String reference;
        public CashflowItem.CashflowType type;
        public CashflowItem.CashflowCategory category;
        public BigDecimal amount;
        public String currency;
        public LocalDate transactionDate;
        public LocalDate expectedDate;
        public String description;
        public String counterparty;
        public String account;
        public String costCenter;
        public String projectId;
        public Boolean recurring;
        public CashflowItem.RecurringFrequency recurringFrequency;
        public String parentRecurringItemId;
        public String paymentMethod;
        public BigDecimal taxAmount;
        public List<String> tags;
        public String notes;
        public String linkedExpenseId;
        public String linkedRevenueId;
    }

    @Data
    public static class BulkCreateRequestDto {
        public List<CreateCashflowItemRequestDto> items;
    }

    @Data
    public static class UpdateCashflowItemRequestDto {
        public String description;
        public BigDecimal amount;
        public LocalDate expectedDate;
        public LocalDate transactionDate;
        public String counterparty;
        public String account;
        public String costCenter;
        public List<String> tags;
        public String notes;
        public BigDecimal taxAmount;
    }

    @Data
    public static class SettleRequestDto {
        public String bankReference;
    }

    @Data
    public static class CancelRequestDto {
        public String reason;
    }

    @Data
    public static class MarkFailedRequestDto {
        public String reason;
    }

    @Data
    public static class RecurringSetupRequestDto {
        public CashflowItem.RecurringFrequency frequency;
    }
}

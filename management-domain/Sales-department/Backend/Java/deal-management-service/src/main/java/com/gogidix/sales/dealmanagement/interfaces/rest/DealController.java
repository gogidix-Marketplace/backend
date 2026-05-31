package com.gogidix.sales.dealmanagement.interfaces.rest;

import com.gogidix.sales.dealmanagement.application.dto.response.DealResponseDto;
import com.gogidix.sales.dealmanagement.application.dto.response.ErrorResponseDto;
import com.gogidix.sales.dealmanagement.application.service.DealCommandService;
import com.gogidix.sales.dealmanagement.application.service.DealQueryService;
import com.gogidix.sales.dealmanagement.domain.model.Deal;
import com.gogidix.sales.dealmanagement.domain.model.DealActivity;
import com.gogidix.sales.dealmanagement.domain.model.DealProduct;
import com.gogidix.sales.dealmanagement.domain.model.Competitor;
import com.gogidix.sales.dealmanagement.domain.port.in.DealCommand;
import com.gogidix.sales.dealmanagement.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Deal REST Controller
 * Handles HTTP requests for deal operations
 */
@RestController
@RequestMapping("/deals")
@RequiredArgsConstructor
@Tag(name = "Deals", description = "Deal management endpoints")
public class DealController {

    private final DealCommandService dealCommandService;
    private final DealQueryService dealQueryService;

    @PostMapping
    @Operation(summary = "Create a new deal")
    public ResponseEntity<DealResponseDto> createDeal(@Valid @RequestBody CreateDealRequestDto request) {
        DealCommand.CreateDealCommand command = new DealCommand.CreateDealCommand(
                RequestContextHolder.getTenantId(),
                request.getDealName(),
                request.getAccountId(),
                request.getAccountName(),
                request.getContactId(),
                request.getContactName(),
                request.getAmount(),
                request.getCurrency(),
                request.getStage(),
                request.getOwnerId(),
                request.getOwnerName(),
                request.getExpectedCloseDate(),
                request.getPriority(),
                request.getSource(),
                request.getCampaign(),
                request.getLeadSource(),
                request.getDescription(),
                request.getNextSteps(),
                request.getRegion(),
                request.getIndustry(),
                request.getSegment(),
                request.getTerritory(),
                request.getContractType(),
                request.getContractLengthMonths(),
                request.getRenewal(),
                request.getRenewalDealId(),
                request.getTeamMemberIds(),
                request.getTags(),
                request.getProducts()
        );

        Deal deal = dealCommandService.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(dealQueryService.toResponseDto(deal));
    }

    @GetMapping("/{dealId}")
    @Operation(summary = "Get deal by ID")
    public ResponseEntity<DealResponseDto> getDeal(
            @Parameter(description = "Deal ID") @PathVariable String dealId) {
        Deal deal = dealQueryService.getById(dealId);
        return ResponseEntity.ok(dealQueryService.toResponseDto(deal));
    }

    @GetMapping
    @Operation(summary = "Get all deals for tenant")
    public ResponseEntity<List<DealResponseDto>> getAllDeals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Deal> deals = dealQueryService.getDeals(pageable);
        return ResponseEntity.ok(deals.map(deal -> dealQueryService.toResponseDto(deal)).getContent());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get deals by status")
    public ResponseEntity<List<DealResponseDto>> getDealsByStatus(
            @Parameter(description = "Deal status") @PathVariable Deal.DealStatus status) {
        List<Deal> deals = dealQueryService.getDealsByStatus(status);
        return ResponseEntity.ok(deals.stream().map(dealQueryService::toResponseDto).toList());
    }

    @GetMapping("/stage/{stage}")
    @Operation(summary = "Get deals by stage")
    public ResponseEntity<List<DealResponseDto>> getDealsByStage(
            @Parameter(description = "Deal stage") @PathVariable Deal.DealStage stage) {
        List<Deal> deals = dealQueryService.getDealsByStage(stage);
        return ResponseEntity.ok(deals.stream().map(dealQueryService::toResponseDto).toList());
    }

    @GetMapping("/owner/{ownerId}")
    @Operation(summary = "Get deals by owner")
    public ResponseEntity<List<DealResponseDto>> getDealsByOwner(
            @Parameter(description = "Owner ID") @PathVariable String ownerId) {
        List<Deal> deals = dealQueryService.getDealsByOwner(ownerId);
        return ResponseEntity.ok(deals.stream().map(dealQueryService::toResponseDto).toList());
    }

    @GetMapping("/account/{accountId}")
    @Operation(summary = "Get deals by account")
    public ResponseEntity<List<DealResponseDto>> getDealsByAccount(
            @Parameter(description = "Account ID") @PathVariable String accountId) {
        List<Deal> deals = dealQueryService.getDealsByAccount(accountId);
        return ResponseEntity.ok(deals.stream().map(dealQueryService::toResponseDto).toList());
    }

    @GetMapping("/pipeline")
    @Operation(summary = "Get pipeline view")
    public ResponseEntity<Map<Deal.DealStage, DealResponseDto.PipelineViewDto>> getPipelineView() {
        return ResponseEntity.ok(dealQueryService.getPipelineView());
    }

    @GetMapping("/forecast")
    @Operation(summary = "Get forecast summary")
    public ResponseEntity<DealQueryService.ForecastSummary> getForecast(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        DealQueryService.ForecastSummary summary = dealQueryService.getForecastSummary(startDate, endDate);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/summary")
    @Operation(summary = "Get deal summary")
    public ResponseEntity<DealQueryService.DealSummary> getSummary() {
        DealQueryService.DealSummary summary = dealQueryService.getDealSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/search")
    @Operation(summary = "Search deals")
    public ResponseEntity<List<DealResponseDto>> searchDeals(
            @Parameter(description = "Search term") @RequestParam String q) {
        List<Deal> deals = dealQueryService.searchDeals(q);
        return ResponseEntity.ok(deals.stream().map(dealQueryService::toResponseDto).toList());
    }

    @PutMapping("/{dealId}")
    @Operation(summary = "Update deal")
    public ResponseEntity<DealResponseDto> updateDeal(
            @Parameter(description = "Deal ID") @PathVariable String dealId,
            @Valid @RequestBody UpdateDealRequestDto request) {

        DealCommand.UpdateDealCommand command = new DealCommand.UpdateDealCommand(
                RequestContextHolder.getTenantId(),
                dealId,
                request.getDealName(),
                request.getAmount(),
                request.getExpectedCloseDate(),
                request.getPriority(),
                request.getDescription(),
                request.getNextSteps(),
                request.getProbability(),
                request.getTags()
        );

        Deal deal = dealCommandService.update(command);
        return ResponseEntity.ok(dealQueryService.toResponseDto(deal));
    }

    @PostMapping("/{dealId}/advance")
    @Operation(summary = "Advance deal to next stage")
    public ResponseEntity<Void> advanceStage(
            @Parameter(description = "Deal ID") @PathVariable String dealId,
            @RequestBody AdvanceStageRequestDto request) {

        DealCommand.AdvanceStageCommand command = new DealCommand.AdvanceStageCommand(
                RequestContextHolder.getTenantId(),
                dealId,
                request.getNotes()
        );

        dealCommandService.advanceStage(command);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{dealId}/regress")
    @Operation(summary = "Regress deal to previous stage")
    public ResponseEntity<Void> regressStage(
            @Parameter(description = "Deal ID") @PathVariable String dealId,
            @RequestBody RegressStageRequestDto request) {

        DealCommand.RegressStageCommand command = new DealCommand.RegressStageCommand(
                RequestContextHolder.getTenantId(),
                dealId,
                request.getTargetStage(),
                request.getReason()
        );

        dealCommandService.regressStage(command);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{dealId}/win")
    @Operation(summary = "Mark deal as won")
    public ResponseEntity<Void> markAsWon(
            @Parameter(description = "Deal ID") @PathVariable String dealId,
            @RequestBody MarkAsWonRequestDto request) {

        DealCommand.MarkAsWonCommand command = new DealCommand.MarkAsWonCommand(
                RequestContextHolder.getTenantId(),
                dealId,
                request.getFinalAmount(),
                request.getNotes()
        );

        dealCommandService.markAsWon(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{dealId}/lose")
    @Operation(summary = "Mark deal as lost")
    public ResponseEntity<Void> markAsLost(
            @Parameter(description = "Deal ID") @PathVariable String dealId,
            @RequestBody MarkAsLostRequestDto request) {

        DealCommand.MarkAsLostCommand command = new DealCommand.MarkAsLostCommand(
                RequestContextHolder.getTenantId(),
                dealId,
                request.getLossReason(),
                request.getLossDetails()
        );

        dealCommandService.markAsLost(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{dealId}/products")
    @Operation(summary = "Add product to deal")
    public ResponseEntity<Void> addProduct(
            @Parameter(description = "Deal ID") @PathVariable String dealId,
            @Valid @RequestBody AddProductRequestDto request) {

        DealCommand.AddProductCommand command = new DealCommand.AddProductCommand(
                RequestContextHolder.getTenantId(),
                dealId,
                request.getProductName(),
                request.getProductCode(),
                request.getProductDescription(),
                request.getProductCategory(),
                request.getQuantity(),
                request.getUnitPrice(),
                request.getCurrency(),
                request.getServiceType(),
                request.getStartDate(),
                request.getEndDate(),
                request.getIsRecurring(),
                request.getBillingCycle()
        );

        dealCommandService.addProduct(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{dealId}/products/{productId}")
    @Operation(summary = "Remove product from deal")
    public ResponseEntity<Void> removeProduct(
            @Parameter(description = "Deal ID") @PathVariable String dealId,
            @Parameter(description = "Product ID") @PathVariable String productId) {

        dealCommandService.removeProduct(dealId, productId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{dealId}/activities")
    @Operation(summary = "Add activity to deal")
    public ResponseEntity<Void> addActivity(
            @Parameter(description = "Deal ID") @PathVariable String dealId,
            @Valid @RequestBody AddActivityRequestDto request) {

        DealCommand.AddActivityCommand command = new DealCommand.AddActivityCommand(
                RequestContextHolder.getTenantId(),
                dealId,
                request.getActivityType(),
                request.getSubject(),
                request.getDescription(),
                request.getDueDate(),
                request.getPriority()
        );

        dealCommandService.addActivity(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{dealId}/competitors")
    @Operation(summary = "Add competitor to deal")
    public ResponseEntity<Void> addCompetitor(
            @Parameter(description = "Deal ID") @PathVariable String dealId,
            @Valid @RequestBody AddCompetitorRequestDto request) {

        DealCommand.AddCompetitorCommand command = new DealCommand.AddCompetitorCommand(
                RequestContextHolder.getTenantId(),
                dealId,
                request.getCompetitorName(),
                request.getStrength(),
                request.getThreat(),
                request.getEstimatedDealValue(),
                request.getCompetingProduct(),
                request.getCompetitorStrengths(),
                request.getCompetitorWeaknesses(),
                request.getOurAdvantage(),
                request.getProbabilityOfWin()
        );

        dealCommandService.addCompetitor(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{dealId}/team")
    @Operation(summary = "Add team member to deal")
    public ResponseEntity<Void> addTeamMember(
            @Parameter(description = "Deal ID") @PathVariable String dealId,
            @RequestBody AddTeamMemberRequestDto request) {

        DealCommand.AddTeamMemberCommand command = new DealCommand.AddTeamMemberCommand(
                RequestContextHolder.getTenantId(),
                dealId,
                request.getUserId()
        );

        dealCommandService.addTeamMember(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{dealId}/team/{userId}")
    @Operation(summary = "Remove team member from deal")
    public ResponseEntity<Void> removeTeamMember(
            @Parameter(description = "Deal ID") @PathVariable String dealId,
            @Parameter(description = "User ID") @PathVariable String userId) {

        DealCommand.RemoveTeamMemberCommand command = new DealCommand.RemoveTeamMemberCommand(
                RequestContextHolder.getTenantId(),
                dealId,
                userId
        );

        dealCommandService.removeTeamMember(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{dealId}/approval/request")
    @Operation(summary = "Request approval for deal")
    public ResponseEntity<Void> requestApproval(
            @Parameter(description = "Deal ID") @PathVariable String dealId) {

        DealCommand.RequestApprovalCommand command = new DealCommand.RequestApprovalCommand(
                RequestContextHolder.getTenantId(),
                dealId
        );

        dealCommandService.requestApproval(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{dealId}/approval/approve")
    @Operation(summary = "Approve deal")
    public ResponseEntity<Void> approve(
            @Parameter(description = "Deal ID") @PathVariable String dealId) {

        DealCommand.ApproveDealCommand command = new DealCommand.ApproveDealCommand(
                RequestContextHolder.getTenantId(),
                dealId,
                RequestContextHolder.getUserId()
        );

        dealCommandService.approve(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{dealId}/approval/reject")
    @Operation(summary = "Reject deal approval")
    public ResponseEntity<Void> rejectApproval(
            @Parameter(description = "Deal ID") @PathVariable String dealId,
            @RequestBody RejectApprovalRequestDto request) {

        DealCommand.RejectApprovalCommand command = new DealCommand.RejectApprovalCommand(
                RequestContextHolder.getTenantId(),
                dealId,
                RequestContextHolder.getUserId(),
                request.getReason()
        );

        dealCommandService.rejectApproval(command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{dealId}")
    @Operation(summary = "Delete deal")
    public ResponseEntity<Void> deleteDeal(
            @Parameter(description = "Deal ID") @PathVariable String dealId) {

        DealCommand.DeleteDealCommand command = new DealCommand.DeleteDealCommand(
                RequestContextHolder.getTenantId(),
                dealId
        );

        dealCommandService.delete(command);
        return ResponseEntity.noContent().build();
    }

    @Data
    public static class CreateDealRequestDto {
        public String dealName;
        public String accountId;
        public String accountName;
        public String contactId;
        public String contactName;
        public BigDecimal amount;
        public String currency;
        public Deal.DealStage stage;
        public String ownerId;
        public String ownerName;
        public LocalDate expectedCloseDate;
        public Deal.DealPriority priority;
        public String source;
        public String campaign;
        public String leadSource;
        public String description;
        public String nextSteps;
        public String region;
        public String industry;
        public String segment;
        public String territory;
        public String contractType;
        public Integer contractLengthMonths;
        public Boolean renewal;
        public String renewalDealId;
        public List<String> teamMemberIds;
        public List<String> tags;
        public List<DealCommand.CreateProductCommand> products;
    }

    @Data
    public static class UpdateDealRequestDto {
        public String dealName;
        public BigDecimal amount;
        public LocalDate expectedCloseDate;
        public Deal.DealPriority priority;
        public String description;
        public String nextSteps;
        public Integer probability;
        public List<String> tags;
    }

    @Data
    public static class AdvanceStageRequestDto {
        public String notes;
    }

    @Data
    public static class RegressStageRequestDto {
        public Deal.DealStage targetStage;
        public String reason;
    }

    @Data
    public static class MarkAsWonRequestDto {
        public BigDecimal finalAmount;
        public String notes;
    }

    @Data
    public static class MarkAsLostRequestDto {
        public String lossReason;
        public String lossDetails;
    }

    @Data
    public static class AddProductRequestDto {
        public String productName;
        public String productCode;
        public String productDescription;
        public String productCategory;
        public Integer quantity;
        public BigDecimal unitPrice;
        public String currency;
        public String serviceType;
        public LocalDate startDate;
        public LocalDate endDate;
        public Boolean isRecurring;
        public DealProduct.BillingCycle billingCycle;
    }

    @Data
    public static class AddActivityRequestDto {
        public DealActivity.ActivityType activityType;
        public String subject;
        public String description;
        public java.time.Instant dueDate;
        public DealActivity.Priority priority;
    }

    @Data
    public static class AddCompetitorRequestDto {
        public String competitorName;
        public Competitor.StrengthLevel strength;
        public Competitor.ThreatLevel threat;
        public BigDecimal estimatedDealValue;
        public String competingProduct;
        public String competitorStrengths;
        public String competitorWeaknesses;
        public String ourAdvantage;
        public Integer probabilityOfWin;
    }

    @Data
    public static class AddTeamMemberRequestDto {
        public String userId;
    }

    @Data
    public static class RejectApprovalRequestDto {
        public String reason;
    }
}

package com.gogidix.marketing.campaign.interfaces.rest;

import com.gogidix.marketing.campaign.application.command.CampaignCommandService;
import com.gogidix.marketing.campaign.application.dto.*;
import com.gogidix.marketing.campaign.application.query.CampaignQueryService;
import com.gogidix.marketing.campaign.domain.model.Campaign;
import com.gogidix.marketing.campaign.domain.model.CampaignChannel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Campaign REST Controller - Campaign management endpoints
 */
@RestController
@RequestMapping("/campaigns")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Campaign", description = "Marketing Campaign management APIs")
public class CampaignController {

    private final CampaignCommandService campaignCommandService;
    private final CampaignQueryService campaignQueryService;

    // ========== CRUD Operations ==========

    @PostMapping
    @Operation(summary = "Create a new campaign", description = "Creates a new marketing campaign")
    public ResponseEntity<Campaign> createCampaign(@Valid @RequestBody CreateCampaignRequest request) {
        log.info("REST: Creating campaign: {}", request.getName());
        Campaign created = campaignCommandService.createCampaign(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get campaign by ID", description = "Retrieves a campaign by its ID")
    public ResponseEntity<CampaignResponseDTO> getCampaign(
        @Parameter(description = "Campaign ID") @PathVariable String id
    ) {
        log.debug("REST: Getting campaign: {}", id);
        CampaignResponseDTO campaign = campaignQueryService.getCampaignDetailById(id);
        return ResponseEntity.ok(campaign);
    }

    @GetMapping
    @Operation(summary = "Get all campaigns", description = "Retrieves all campaigns for the current tenant")
    public ResponseEntity<List<Campaign>> getAllCampaigns() {
        log.debug("REST: Getting all campaigns");
        List<Campaign> campaigns = campaignQueryService.getAllCampaigns();
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get paginated campaigns", description = "Retrieves campaigns with pagination")
    public ResponseEntity<Page<Campaign>> getCampaignsPaginated(
        @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size
    ) {
        log.debug("REST: Getting paginated campaigns: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Campaign> campaigns = campaignQueryService.getCampaignsPaginated(pageable);
        return ResponseEntity.ok(campaigns);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update campaign", description = "Updates an existing campaign")
    public ResponseEntity<Campaign> updateCampaign(
        @Parameter(description = "Campaign ID") @PathVariable String id,
        @Valid @RequestBody UpdateCampaignRequest request
    ) {
        log.info("REST: Updating campaign: {}", id);
        Campaign updated = campaignCommandService.updateCampaign(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete campaign", description = "Deletes a campaign by ID")
    public ResponseEntity<Void> deleteCampaign(
        @Parameter(description = "Campaign ID") @PathVariable String id
    ) {
        log.info("REST: Deleting campaign: {}", id);
        campaignCommandService.deleteCampaign(id);
        return ResponseEntity.noContent().build();
    }

    // ========== Status Operations ==========

    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate campaign", description = "Activates a scheduled or paused campaign")
    public ResponseEntity<Campaign> activateCampaign(
        @Parameter(description = "Campaign ID") @PathVariable String id
    ) {
        log.info("REST: Activating campaign: {}", id);
        Campaign campaign = campaignCommandService.activateCampaign(id);
        return ResponseEntity.ok(campaign);
    }

    @PostMapping("/{id}/pause")
    @Operation(summary = "Pause campaign", description = "Pauses an active campaign")
    public ResponseEntity<Campaign> pauseCampaign(
        @Parameter(description = "Campaign ID") @PathVariable String id
    ) {
        log.info("REST: Pausing campaign: {}", id);
        Campaign campaign = campaignCommandService.pauseCampaign(id);
        return ResponseEntity.ok(campaign);
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel campaign", description = "Cancels a campaign")
    public ResponseEntity<Campaign> cancelCampaign(
        @Parameter(description = "Campaign ID") @PathVariable String id
    ) {
        log.info("REST: Cancelling campaign: {}", id);
        Campaign campaign = campaignCommandService.cancelCampaign(id);
        return ResponseEntity.ok(campaign);
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete campaign", description = "Marks an active campaign as completed")
    public ResponseEntity<Campaign> completeCampaign(
        @Parameter(description = "Campaign ID") @PathVariable String id
    ) {
        log.info("REST: Completing campaign: {}", id);
        Campaign campaign = campaignCommandService.completeCampaign(id);
        return ResponseEntity.ok(campaign);
    }

    // ========== Approval Operations ==========

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve campaign", description = "Approves a pending campaign")
    public ResponseEntity<Campaign> approveCampaign(
        @Parameter(description = "Campaign ID") @PathVariable String id,
        @Parameter(description = "User ID of approver") @RequestParam String approvedBy
    ) {
        log.info("REST: Approving campaign: {} by: {}", id, approvedBy);
        Campaign campaign = campaignCommandService.approveCampaign(id, approvedBy);
        return ResponseEntity.ok(campaign);
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject campaign", description = "Rejects a pending campaign")
    public ResponseEntity<Campaign> rejectCampaign(
        @Parameter(description = "Campaign ID") @PathVariable String id,
        @Parameter(description = "User ID of rejecter") @RequestParam String rejectedBy
    ) {
        log.info("REST: Rejecting campaign: {} by: {}", id, rejectedBy);
        Campaign campaign = campaignCommandService.rejectCampaign(id, rejectedBy);
        return ResponseEntity.ok(campaign);
    }

    // ========== Budget Operations ==========

    @PostMapping("/{id}/spending")
    @Operation(summary = "Record spending", description = "Records spending against campaign budget")
    public ResponseEntity<Campaign> recordSpending(
        @Parameter(description = "Campaign ID") @PathVariable String id,
        @Parameter(description = "Amount spent") @RequestParam BigDecimal amount
    ) {
        log.info("REST: Recording spending for campaign: {}, amount: {}", id, amount);
        Campaign campaign = campaignCommandService.recordSpending(id, amount);
        return ResponseEntity.ok(campaign);
    }

    // ========== Channel Operations ==========

    @PostMapping("/{id}/channels")
    @Operation(summary = "Add channel to campaign", description = "Adds a channel to the campaign")
    public ResponseEntity<Campaign> addChannel(
        @Parameter(description = "Campaign ID") @PathVariable String id,
        @Parameter(description = "Channel name") @RequestParam String channel
    ) {
        log.info("REST: Adding channel {} to campaign: {}", channel, id);
        Campaign campaign = campaignCommandService.addChannel(id, channel);
        return ResponseEntity.ok(campaign);
    }

    @DeleteMapping("/{id}/channels/{channel}")
    @Operation(summary = "Remove channel from campaign", description = "Removes a channel from the campaign")
    public ResponseEntity<Campaign> removeChannel(
        @Parameter(description = "Campaign ID") @PathVariable String id,
        @Parameter(description = "Channel name") @PathVariable String channel
    ) {
        log.info("REST: Removing channel {} from campaign: {}", channel, id);
        Campaign campaign = campaignCommandService.removeChannel(id, channel);
        return ResponseEntity.ok(campaign);
    }

    @PostMapping("/channels")
    @Operation(summary = "Create campaign channel", description = "Creates a new campaign channel")
    public ResponseEntity<CampaignChannel> createCampaignChannel(
        @Valid @RequestBody CreateCampaignChannelRequest request
    ) {
        log.info("REST: Creating campaign channel for campaign: {}", request.getCampaignId());
        CampaignChannel channel = campaignCommandService.createCampaignChannel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(channel);
    }

    @GetMapping("/{id}/channels")
    @Operation(summary = "Get campaign channels", description = "Retrieves all channels for a campaign")
    public ResponseEntity<List<CampaignChannel>> getCampaignChannels(
        @Parameter(description = "Campaign ID") @PathVariable String id
    ) {
        log.debug("REST: Getting channels for campaign: {}", id);
        List<CampaignChannel> channels = campaignQueryService.getCampaignChannels(id);
        return ResponseEntity.ok(channels);
    }

    // ========== Query Operations ==========

    @GetMapping("/by-status/{status}")
    @Operation(summary = "Get campaigns by status", description = "Retrieves campaigns filtered by status")
    public ResponseEntity<List<Campaign>> getCampaignsByStatus(
        @Parameter(description = "Campaign status") @PathVariable String status
    ) {
        log.debug("REST: Getting campaigns by status: {}", status);
        List<Campaign> campaigns = campaignQueryService.getCampaignsByStatus(status);
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/by-type/{type}")
    @Operation(summary = "Get campaigns by type", description = "Retrieves campaigns filtered by type")
    public ResponseEntity<List<Campaign>> getCampaignsByType(
        @Parameter(description = "Campaign type") @PathVariable String type
    ) {
        log.debug("REST: Getting campaigns by type: {}", type);
        List<Campaign> campaigns = campaignQueryService.getCampaignsByType(type);
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/by-scope/{scope}")
    @Operation(summary = "Get campaigns by scope", description = "Retrieves campaigns filtered by scope")
    public ResponseEntity<List<Campaign>> getCampaignsByScope(
        @Parameter(description = "Campaign scope") @PathVariable String scope
    ) {
        log.debug("REST: Getting campaigns by scope: {}", scope);
        List<Campaign> campaigns = campaignQueryService.getCampaignsByScope(scope);
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/active")
    @Operation(summary = "Get active campaigns", description = "Retrieves all active campaigns")
    public ResponseEntity<List<Campaign>> getActiveCampaigns() {
        log.debug("REST: Getting active campaigns");
        List<Campaign> campaigns = campaignQueryService.getActiveCampaigns();
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/draft")
    @Operation(summary = "Get draft campaigns", description = "Retrieves all draft campaigns")
    public ResponseEntity<List<Campaign>> getDraftCampaigns() {
        log.debug("REST: Getting draft campaigns");
        List<Campaign> campaigns = campaignQueryService.getDraftCampaigns();
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/scheduled")
    @Operation(summary = "Get scheduled campaigns", description = "Retrieves all scheduled campaigns")
    public ResponseEntity<List<Campaign>> getScheduledCampaigns() {
        log.debug("REST: Getting scheduled campaigns");
        List<Campaign> campaigns = campaignQueryService.getScheduledCampaigns();
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/by-country/{country}")
    @Operation(summary = "Get campaigns by country", description = "Retrieves campaigns targeting a country")
    public ResponseEntity<List<Campaign>> getCampaignsByCountry(
        @Parameter(description = "Country code") @PathVariable String country
    ) {
        log.debug("REST: Getting campaigns by country: {}", country);
        List<Campaign> campaigns = campaignQueryService.getCampaignsByCountry(country);
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/by-region/{region}")
    @Operation(summary = "Get campaigns by region", description = "Retrieves campaigns targeting a region")
    public ResponseEntity<List<Campaign>> getCampaignsByRegion(
        @Parameter(description = "Region name") @PathVariable String region
    ) {
        log.debug("REST: Getting campaigns by region: {}", region);
        List<Campaign> campaigns = campaignQueryService.getCampaignsByRegion(region);
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/by-owner/{owner}")
    @Operation(summary = "Get campaigns by owner", description = "Retrieves campaigns by owner")
    public ResponseEntity<List<Campaign>> getCampaignsByOwner(
        @Parameter(description = "Owner user ID") @PathVariable String owner
    ) {
        log.debug("REST: Getting campaigns by owner: {}", owner);
        List<Campaign> campaigns = campaignQueryService.getCampaignsByOwner(owner);
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/sub-campaigns/{parentId}")
    @Operation(summary = "Get sub-campaigns", description = "Retrieves sub-campaigns of a parent campaign")
    public ResponseEntity<List<Campaign>> getSubCampaigns(
        @Parameter(description = "Parent campaign ID") @PathVariable String parentId
    ) {
        log.debug("REST: Getting sub-campaigns of: {}", parentId);
        List<Campaign> campaigns = campaignQueryService.getSubCampaigns(parentId);
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/templates")
    @Operation(summary = "Get template campaigns", description = "Retrieves all template campaigns")
    public ResponseEntity<List<Campaign>> getTemplateCampaigns() {
        log.debug("REST: Getting template campaigns");
        List<Campaign> campaigns = campaignQueryService.getTemplateCampaigns();
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/search")
    @Operation(summary = "Search campaigns", description = "Searches campaigns by name or description")
    public ResponseEntity<List<Campaign>> searchCampaigns(
        @Parameter(description = "Search term") @RequestParam String term
    ) {
        log.debug("REST: Searching campaigns with term: {}", term);
        List<Campaign> campaigns = campaignQueryService.searchCampaigns(term);
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/pending-approval")
    @Operation(summary = "Get campaigns pending approval", description = "Retrieves campaigns awaiting approval")
    public ResponseEntity<List<Campaign>> getCampaignsPendingApproval() {
        log.debug("REST: Getting campaigns pending approval");
        List<Campaign> campaigns = campaignQueryService.getCampaignsPendingApproval();
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/by-approval-status/{status}")
    @Operation(summary = "Get campaigns by approval status", description = "Retrieves campaigns by approval status")
    public ResponseEntity<List<Campaign>> getCampaignsByApprovalStatus(
        @Parameter(description = "Approval status") @PathVariable String status
    ) {
        log.debug("REST: Getting campaigns by approval status: {}", status);
        List<Campaign> campaigns = campaignQueryService.getCampaignsByApprovalStatus(status);
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/running-in-range")
    @Operation(summary = "Get campaigns in date range", description = "Retrieves active campaigns in a date range")
    public ResponseEntity<List<Campaign>> getCampaignsRunningInDateRange(
        @Parameter(description = "Start date") @RequestParam String startDate,
        @Parameter(description = "End date") @RequestParam String endDate
    ) {
        log.debug("REST: Getting campaigns in date range: {} to {}", startDate, endDate);
        Instant start = Instant.parse(startDate);
        Instant end = Instant.parse(endDate);
        List<Campaign> campaigns = campaignQueryService.getCampaignsRunningInDateRange(start, end);
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/budget-alerts")
    @Operation(summary = "Get campaigns with budget alerts", description = "Retrieves campaigns over budget threshold")
    public ResponseEntity<List<Campaign>> getCampaignsWithBudgetAlerts(
        @Parameter(description = "Threshold percentage") @RequestParam(defaultValue = "75") BigDecimal threshold
    ) {
        log.debug("REST: Getting campaigns with budget alerts over {}%", threshold);
        List<Campaign> campaigns = campaignQueryService.getCampaignsWithBudgetAlerts(threshold);
        return ResponseEntity.ok(campaigns);
    }

    // ========== Dashboard Operations ==========

    @GetMapping("/dashboard")
    @Operation(summary = "Get campaign dashboard", description = "Retrieves aggregated dashboard data")
    public ResponseEntity<CampaignDashboardDTO> getCampaignDashboard() {
        log.debug("REST: Getting campaign dashboard");
        CampaignDashboardDTO dashboard = campaignQueryService.getCampaignDashboard();
        return ResponseEntity.ok(dashboard);
    }
}

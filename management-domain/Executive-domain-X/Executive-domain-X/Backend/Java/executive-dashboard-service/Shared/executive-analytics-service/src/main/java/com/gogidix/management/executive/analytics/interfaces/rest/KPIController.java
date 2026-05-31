package com.gogidix.management.executive.analytics.interfaces.rest;

import com.gogidix.management.executive.analytics.application.command.KPICommandService;
import com.gogidix.management.executive.analytics.application.dto.CreateKPIRequest;
import com.gogidix.management.executive.analytics.application.dto.KPIDashboardDTO;
import com.gogidix.management.executive.analytics.application.dto.KPIDetailDTO;
import com.gogidix.management.executive.analytics.application.dto.UpdateKPIRequest;
import com.gogidix.management.executive.analytics.application.query.KPIQueryService;
import com.gogidix.management.executive.analytics.domain.model.KPI;
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
import java.util.List;

@RestController
@RequestMapping("/kpi")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "KPI", description = "Key Performance Indicator management APIs")
public class KPIController {
    private final KPICommandService kpiCommandService;
    private final KPIQueryService kpiQueryService;

    @PostMapping
    @Operation(summary = "Create a new KPI", description = "Creates a new KPI for the current tenant")
    public ResponseEntity<KPI> createKPI(@Valid @RequestBody CreateKPIRequest request) {
        log.info("REST: Creating KPI: {}", request.getName());
        KPI created = kpiCommandService.createKPI(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/bulk")
    @Operation(summary = "Bulk create KPIs", description = "Creates multiple KPIs in a single request")
    public ResponseEntity<List<KPI>> bulkCreateKPIs(@Valid @RequestBody List<CreateKPIRequest> requests) {
        log.info("REST: Bulk creating {} KPIs", requests.size());
        List<KPI> created = kpiCommandService.bulkCreateKPIs(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get KPI by ID", description = "Retrieves a KPI by its ID")
    public ResponseEntity<KPIDetailDTO> getKPI(
        @Parameter(description = "KPI ID") @PathVariable String id
    ) {
        log.debug("REST: Getting KPI: {}", id);
        KPIDetailDTO kpi = kpiQueryService.getKPIDetailById(id);
        return ResponseEntity.ok(kpi);
    }

    @GetMapping
    @Operation(summary = "Get all KPIs", description = "Retrieves all KPIs for the current tenant")
    public ResponseEntity<List<KPI>> getAllKPIs() {
        log.debug("REST: Getting all KPIs");
        List<KPI> kpis = kpiQueryService.getAllKPIs();
        return ResponseEntity.ok(kpis);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Get paginated KPIs", description = "Retrieves KPIs with pagination")
    public ResponseEntity<Page<KPI>> getKPIsPaginated(
        @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
        @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
        @Parameter(description = "Category filter (optional)") @RequestParam(required = false) String category
    ) {
        log.debug("REST: Getting paginated KPIs: page={}, size={}, category={}", page, size, category);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<KPI> kpis = kpiQueryService.getKPIsPaginated(category, pageable);
        return ResponseEntity.ok(kpis);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update KPI", description = "Updates an existing KPI")
    public ResponseEntity<KPI> updateKPI(
        @Parameter(description = "KPI ID") @PathVariable String id,
        @Valid @RequestBody UpdateKPIRequest request
    ) {
        log.info("REST: Updating KPI: {}", id);
        KPI updated = kpiCommandService.updateKPI(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete KPI", description = "Deletes a KPI by ID")
    public ResponseEntity<Void> deleteKPI(@Parameter(description = "KPI ID") @PathVariable String id) {
        log.info("REST: Deleting KPI: {}", id);
        kpiCommandService.deleteKPI(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-category/{category}")
    @Operation(summary = "Get KPIs by category", description = "Retrieves KPIs filtered by category")
    public ResponseEntity<List<KPI>> getKPIsByCategory(
        @Parameter(description = "Category (FINANCIAL, OPERATIONAL, etc.)") @PathVariable String category
    ) {
        log.debug("REST: Getting KPIs by category: {}", category);
        List<KPI> kpis = kpiQueryService.getKPIsByCategory(category);
        return ResponseEntity.ok(kpis);
    }

    @GetMapping("/by-executive/{level}")
    @Operation(summary = "Get KPIs by executive level", description = "Retrieves KPIs for specific executive level")
    public ResponseEntity<List<KPI>> getKPIsByExecutiveLevel(
        @Parameter(description = "Executive level (CEO, COO, CFO, CTO, ALL)") @PathVariable String level
    ) {
        log.debug("REST: Getting KPIs for executive: {}", level);
        List<KPI> kpis = kpiQueryService.getKPIsByExecutiveLevel(level);
        return ResponseEntity.ok(kpis);
    }

    @GetMapping("/dashboard/{executiveLevel}")
    @Operation(summary = "Get KPI dashboard", description = "Retrieves dashboard data for an executive level")
    public ResponseEntity<KPIDashboardDTO> getKPIDashboard(
        @Parameter(description = "Executive level") @PathVariable String executiveLevel
    ) {
        log.debug("REST: Getting dashboard for executive: {}", executiveLevel);
        KPIDashboardDTO dashboard = kpiQueryService.getKPIDashboard(executiveLevel);
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/by-period/{period}")
    @Operation(summary = "Get KPIs by period", description = "Retrieves KPIs for a specific period")
    public ResponseEntity<List<KPI>> getKPIsByPeriod(
        @Parameter(description = "Period (e.g., 2024-01, Q1-2024)") @PathVariable String period
    ) {
        log.debug("REST: Getting KPIs for period: {}", period);
        List<KPI> kpis = kpiQueryService.getKPIsByPeriod(period);
        return ResponseEntity.ok(kpis);
    }

    @GetMapping("/needs-attention")
    @Operation(summary = "Get KPIs needing attention", description = "Retrieves KPIs with status AT_RISK or BEHIND")
    public ResponseEntity<List<KPI>> getKPIsNeedingAttention() {
        log.debug("REST: Getting KPIs needing attention");
        List<KPI> kpis = kpiQueryService.getKPIsNeedingAttention();
        return ResponseEntity.ok(kpis);
    }

    @GetMapping("/by-status/{status}")
    @Operation(summary = "Get KPIs by status", description = "Retrieves KPIs filtered by status")
    public ResponseEntity<List<KPI>> getKPIsByStatus(
        @Parameter(description = "Status (ON_TRACK, AT_RISK, BEHIND, AHEAD)") @PathVariable String status
    ) {
        log.debug("REST: Getting KPIs by status: {}", status);
        List<KPI> kpis = kpiQueryService.getKPIsByStatus(status);
        return ResponseEntity.ok(kpis);
    }

    @GetMapping("/search")
    @Operation(summary = "Search KPIs", description = "Searches KPIs by name or category")
    public ResponseEntity<List<KPI>> searchKPIs(
        @Parameter(description = "Search term") @RequestParam String term
    ) {
        log.debug("REST: Searching KPIs with term: {}", term);
        List<KPI> kpis = kpiQueryService.searchKPIs(term);
        return ResponseEntity.ok(kpis);
    }

    @GetMapping("/significant-change")
    @Operation(summary = "Get KPIs with significant change", description = "Retrieves KPIs with percent change above threshold")
    public ResponseEntity<List<KPI>> getKPIsWithSignificantChange(
        @Parameter(description = "Threshold percentage") @RequestParam(defaultValue = "10") BigDecimal threshold
    ) {
        log.debug("REST: Getting KPIs with significant change > {}%", threshold);
        List<KPI> kpis = kpiQueryService.getKPIsWithSignificantChange(threshold);
        return ResponseEntity.ok(kpis);
    }

    @GetMapping("/metadata/categories")
    @Operation(summary = "Get unique categories", description = "Retrieves list of unique KPI categories")
    public ResponseEntity<List<String>> getUniqueCategories() {
        log.debug("REST: Getting unique categories");
        List<String> categories = kpiQueryService.getUniqueCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/metadata/executive-levels")
    @Operation(summary = "Get unique executive levels", description = "Retrieves list of unique executive levels")
    public ResponseEntity<List<String>> getUniqueExecutiveLevels() {
        log.debug("REST: Getting unique executive levels");
        List<String> levels = kpiQueryService.getUniqueExecutiveLevels();
        return ResponseEntity.ok(levels);
    }

    @GetMapping("/metadata/periods")
    @Operation(summary = "Get unique periods", description = "Retrieves list of unique periods")
    public ResponseEntity<List<String>> getUniquePeriods() {
        log.debug("REST: Getting unique periods");
        List<String> periods = kpiQueryService.getUniquePeriods();
        return ResponseEntity.ok(periods);
    }

    @PostMapping("/{id}/manual-value")
    @Operation(summary = "Set manual KPI value", description = "Manually sets a KPI value")
    public ResponseEntity<KPI> setManualValue(
        @Parameter(description = "KPI ID") @PathVariable String id,
        @Parameter(description = "New value") @RequestParam BigDecimal value,
        @Parameter(description = "Updated by user") @RequestParam String updatedBy
    ) {
        log.info("REST: Setting manual value for KPI: {} = {}", id, value);
        KPI kpi = kpiCommandService.setManualValue(id, value, updatedBy);
        return ResponseEntity.ok(kpi);
    }

    @PutMapping("/{id}/visibility")
    @Operation(summary = "Set KPI visibility", description = "Updates KPI visibility on dashboard")
    public ResponseEntity<Void> setVisibility(
        @Parameter(description = "KPI ID") @PathVariable String id,
        @Parameter(description = "Visibility flag") @RequestParam boolean visible
    ) {
        log.info("REST: Setting KPI visibility: {} = {}", id, visible);
        kpiCommandService.setVisibility(id, visible);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/recalculate")
    @Operation(summary = "Mark KPI for recalculation", description = "Marks a KPI to be recalculated")
    public ResponseEntity<Void> markForRecalculation(@Parameter(description = "KPI ID") @PathVariable String id) {
        log.info("REST: Marking KPI for recalculation: {}", id);
        kpiCommandService.markForRecalculation(id);
        return ResponseEntity.accepted().build();
    }
}

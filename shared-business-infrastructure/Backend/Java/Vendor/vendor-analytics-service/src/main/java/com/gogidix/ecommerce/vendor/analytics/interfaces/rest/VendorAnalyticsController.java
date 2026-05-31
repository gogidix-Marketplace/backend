package com.gogidix.ecommerce.vendor.analytics.interfaces.rest;

import com.gogidix.ecommerce.vendor.analytics.domain.model.VendorAnalytics;
import com.gogidix.ecommerce.vendor.analytics.domain.service.VendorAnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vendor-analytics")
@RequiredArgsConstructor
@Tag(name = "Vendor analytics Management", description = "APIs for managing vendor analyticss")
public class VendorAnalyticsController {

    private final VendorAnalyticsService vendor_analyticsService;

    @GetMapping
    @Operation(summary = "List all")
    public ResponseEntity<List<VendorAnalytics>> listAll() { return ResponseEntity.ok(vendor_analyticsService.findAll()); }

    @GetMapping("/{id}")
    @Operation(summary = "Get by ID")
    public ResponseEntity<VendorAnalytics> getById(@PathVariable String id) {
        VendorAnalytics result = vendor_analyticsService.findById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create")
    public ResponseEntity<VendorAnalytics> create(@RequestBody VendorAnalytics entity) { return ResponseEntity.ok(vendor_analyticsService.create(entity)); }

    @PutMapping("/{id}")
    @Operation(summary = "Update")
    public ResponseEntity<VendorAnalytics> update(@PathVariable String id, @RequestBody VendorAnalytics entity) {
        VendorAnalytics result = vendor_analyticsService.update(id, entity);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete")
    public ResponseEntity<Void> delete(@PathVariable String id) { vendor_analyticsService.delete(id); return ResponseEntity.noContent().build(); }
}
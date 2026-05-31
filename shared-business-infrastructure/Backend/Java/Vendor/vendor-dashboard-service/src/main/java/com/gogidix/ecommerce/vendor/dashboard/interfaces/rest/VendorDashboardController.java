package com.gogidix.ecommerce.vendor.dashboard.interfaces.rest;

import com.gogidix.ecommerce.vendor.dashboard.domain.model.VendorDashboard;
import com.gogidix.ecommerce.vendor.dashboard.domain.service.VendorDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vendor-dashboard")
@RequiredArgsConstructor
@Tag(name = "Vendor dashboard Management", description = "APIs for managing vendor dashboards")
public class VendorDashboardController {

    private final VendorDashboardService vendor_dashboardService;

    @GetMapping
    @Operation(summary = "List all")
    public ResponseEntity<List<VendorDashboard>> listAll() { return ResponseEntity.ok(vendor_dashboardService.findAll()); }

    @GetMapping("/{id}")
    @Operation(summary = "Get by ID")
    public ResponseEntity<VendorDashboard> getById(@PathVariable String id) {
        VendorDashboard result = vendor_dashboardService.findById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create")
    public ResponseEntity<VendorDashboard> create(@RequestBody VendorDashboard entity) { return ResponseEntity.ok(vendor_dashboardService.create(entity)); }

    @PutMapping("/{id}")
    @Operation(summary = "Update")
    public ResponseEntity<VendorDashboard> update(@PathVariable String id, @RequestBody VendorDashboard entity) {
        VendorDashboard result = vendor_dashboardService.update(id, entity);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete")
    public ResponseEntity<Void> delete(@PathVariable String id) { vendor_dashboardService.delete(id); return ResponseEntity.noContent().build(); }
}
package com.gogidix.shared.warehousing.ecommerce.interfaces.rest;

import com.gogidix.shared.warehousing.ecommerce.application.service.EcommerceStockService;
import com.gogidix.shared.warehousing.ecommerce.interfaces.rest.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/ecommerce/warehouse")
@RequiredArgsConstructor
@Tag(name = "E-Commerce Warehouse", description = "E-commerce warehouse stock and availability APIs")
public class EcommerceWarehouseController {

    private final EcommerceStockService stockService;

    @PostMapping("/stock/register")
    @Operation(summary = "Register vendor stock at warehouse")
    public ResponseEntity<StockRegistrationResponse> registerStock(
            @Valid @RequestBody StockRegistrationRequest request) {
        log.info("REST request to register stock for vendor {} at warehouse {}",
                request.getVendorId(), request.getWarehouseId());
        StockRegistrationResponse response = stockService.registerStock(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/availability")
    @Operation(summary = "Query warehouse availability by zone and SKU")
    public ResponseEntity<WarehouseAvailabilityResponse> getAvailability(
            @Parameter(description = "Zone ID") @RequestParam(required = false) String zoneId,
            @Parameter(description = "SKU") @RequestParam(required = false) String sku) {
        log.info("REST request to query availability: zoneId={}, sku={}", zoneId, sku);
        WarehouseAvailabilityResponse response = stockService.getWarehouseAvailability(zoneId, sku);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vendor/{vendorId}/stock")
    @Operation(summary = "Get vendor multi-zone stock overview")
    public ResponseEntity<VendorStockOverviewResponse> getVendorStock(
            @Parameter(description = "Vendor ID") @PathVariable String vendorId) {
        log.info("REST request to get stock overview for vendor {}", vendorId);
        VendorStockOverviewResponse response = stockService.getVendorStockOverview(vendorId);
        return ResponseEntity.ok(response);
    }
}

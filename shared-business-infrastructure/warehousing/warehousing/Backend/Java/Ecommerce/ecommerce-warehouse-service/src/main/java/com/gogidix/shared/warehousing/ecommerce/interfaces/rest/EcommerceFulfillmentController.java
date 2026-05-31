package com.gogidix.shared.warehousing.ecommerce.interfaces.rest;

import com.gogidix.shared.warehousing.ecommerce.application.service.EcommerceFulfillmentService;
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
@Tag(name = "E-Commerce Fulfillment", description = "E-commerce order fulfillment APIs")
public class EcommerceFulfillmentController {

    private final EcommerceFulfillmentService fulfillmentService;

    @PostMapping("/fulfill")
    @Operation(summary = "Create e-commerce order fulfillment")
    public ResponseEntity<EcommerceFulfillmentResponse> createFulfillment(
            @Valid @RequestBody EcommerceFulfillmentRequest request) {
        log.info("REST request to fulfill order {} at warehouse {}",
                request.getOrderId(), request.getWarehouseId());
        EcommerceFulfillmentResponse response = fulfillmentService.createFulfillment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/fulfillment/{fulfillmentId}")
    @Operation(summary = "Get fulfillment status")
    public ResponseEntity<FulfillmentStatusResponse> getFulfillmentStatus(
            @Parameter(description = "Fulfillment ID") @PathVariable String fulfillmentId) {
        log.info("REST request to get fulfillment status for {}", fulfillmentId);
        FulfillmentStatusResponse response = fulfillmentService.getFulfillmentStatus(fulfillmentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/fulfillment/{fulfillmentId}/start-picking")
    @Operation(summary = "Start picking for a fulfillment order")
    public ResponseEntity<FulfillmentStatusResponse> startPicking(
            @PathVariable String fulfillmentId,
            @RequestParam String staffId,
            @RequestParam String staffName) {
        log.info("REST request to start picking for fulfillment {}", fulfillmentId);
        FulfillmentStatusResponse response = fulfillmentService.startPicking(fulfillmentId, staffId, staffName);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/fulfillment/{fulfillmentId}/complete-picking")
    @Operation(summary = "Complete picking for a fulfillment order")
    public ResponseEntity<FulfillmentStatusResponse> completePicking(
            @PathVariable String fulfillmentId) {
        log.info("REST request to complete picking for fulfillment {}", fulfillmentId);
        FulfillmentStatusResponse response = fulfillmentService.completePicking(fulfillmentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/fulfillment/{fulfillmentId}/complete-packing")
    @Operation(summary = "Complete packing for a fulfillment order")
    public ResponseEntity<FulfillmentStatusResponse> completePacking(
            @PathVariable String fulfillmentId) {
        log.info("REST request to complete packing for fulfillment {}", fulfillmentId);
        FulfillmentStatusResponse response = fulfillmentService.completePacking(fulfillmentId);
        return ResponseEntity.ok(response);
    }
}

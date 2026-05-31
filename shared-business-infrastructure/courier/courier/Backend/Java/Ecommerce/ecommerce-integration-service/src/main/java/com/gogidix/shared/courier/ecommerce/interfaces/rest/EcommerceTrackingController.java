package com.gogidix.shared.courier.ecommerce.interfaces.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gogidix.shared.courier.ecommerce.application.service.EcommerceTrackingService;
import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceDeliveryTracking;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.TrackingResponse;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.mappers.EcommerceTrackingMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/ecommerce/tracking")
@Tag(name = "E-Commerce Tracking", description = "E-commerce delivery tracking with hub-stage visibility")
public class EcommerceTrackingController {

    private static final Logger log = LoggerFactory.getLogger(EcommerceTrackingController.class);

    private final EcommerceTrackingService trackingService;
    private final EcommerceTrackingMapper mapper;

    public EcommerceTrackingController(EcommerceTrackingService trackingService,
                                       EcommerceTrackingMapper mapper) {
        this.trackingService = trackingService;
        this.mapper = mapper;
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get delivery tracking for an e-commerce order")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tracking retrieved"),
        @ApiResponse(responseCode = "404", description = "Tracking not found")
    })
    public ResponseEntity<TrackingResponse> getTracking(@PathVariable String orderId) {
        log.debug("Get tracking for order {}", orderId);

        EcommerceDeliveryTracking tracking = trackingService.getTrackingByOrderId(orderId);
        TrackingResponse response = mapper.toResponse(tracking);

        return ResponseEntity.ok(response);
    }
}

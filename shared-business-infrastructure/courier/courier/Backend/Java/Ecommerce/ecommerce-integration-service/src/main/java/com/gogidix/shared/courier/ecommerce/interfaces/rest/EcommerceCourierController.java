package com.gogidix.shared.courier.ecommerce.interfaces.rest;

import java.time.Instant;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gogidix.shared.courier.ecommerce.application.command.AssignCourierCommand;
import com.gogidix.shared.courier.ecommerce.application.command.AssignHubLegCommand;
import com.gogidix.shared.courier.ecommerce.application.service.EcommerceCourierAssignmentService;
import com.gogidix.shared.courier.ecommerce.application.service.ZoneCourierPoolService;
import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceCourierAssignment;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.AssignmentPriority;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryLeg;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryType;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.VehicleType;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.AssignCourierRequest;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.AssignCourierResponse;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.AssignHubLegRequest;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.CourierAvailabilityResponse;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.mappers.EcommerceAssignmentMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/ecommerce/courier")
@Tag(name = "E-Commerce Courier Assignment", description = "Zone-based courier assignment for e-commerce orders")
public class EcommerceCourierController {

    private static final Logger log = LoggerFactory.getLogger(EcommerceCourierController.class);

    private final EcommerceCourierAssignmentService assignmentService;
    private final ZoneCourierPoolService zoneCourierPoolService;
    private final EcommerceAssignmentMapper mapper;

    public EcommerceCourierController(EcommerceCourierAssignmentService assignmentService,
                                      ZoneCourierPoolService zoneCourierPoolService,
                                      EcommerceAssignmentMapper mapper) {
        this.assignmentService = assignmentService;
        this.zoneCourierPoolService = zoneCourierPoolService;
        this.mapper = mapper;
    }

    @PostMapping("/assign")
    @Operation(summary = "Assign a courier for an e-commerce order")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Courier assigned successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "404", description = "No courier available")
    })
    public ResponseEntity<AssignCourierResponse> assignCourier(
            @Valid @RequestBody AssignCourierRequest request) {
        log.info("Assign courier request for order {}", request.orderId());

        AssignCourierCommand command = AssignCourierCommand.builder()
            .orderId(request.orderId())
            .subOrderId(request.subOrderId())
            .vendorId(request.vendorId())
            .deliveryType(DeliveryType.valueOf(request.deliveryType()))
            .pickupZone(request.pickupZone())
            .deliveryZone(request.deliveryZone())
            .packages(request.packages())
            .priority(request.priority() != null ? AssignmentPriority.valueOf(request.priority()) : AssignmentPriority.NORMAL)
            .scheduledFor(request.scheduledFor())
            .requiresHubProcessing(request.requiresHubProcessing())
            .hubDetails(request.hubDetails())
            .build();

        EcommerceCourierAssignment assignment = assignmentService.assignCourier(command);
        AssignCourierResponse response = mapper.toResponse(assignment);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/assign-hub-leg")
    @Operation(summary = "Assign a courier for an inter-state hub leg")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Hub leg courier assigned"),
        @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<AssignCourierResponse> assignHubLeg(
            @Valid @RequestBody AssignHubLegRequest request) {
        log.info("Assign hub leg request for order {}, leg {}", request.orderId(), request.leg());

        AssignHubLegCommand command = AssignHubLegCommand.builder()
            .orderId(request.orderId())
            .subOrderId(request.subOrderId())
            .leg(DeliveryLeg.valueOf(request.leg()))
            .originHub(request.originHub())
            .destinationHub(request.destinationHub())
            .customerDelivery(request.customerDelivery())
            .packages(request.packages())
            .scheduledFor(request.scheduledFor())
            .build();

        EcommerceCourierAssignment assignment = assignmentService.assignHubLeg(command);
        AssignCourierResponse response = mapper.toResponse(assignment);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/availability")
    @Operation(summary = "Check courier availability in a zone")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Availability retrieved")
    })
    public ResponseEntity<CourierAvailabilityResponse> checkAvailability(
            @RequestParam String zoneId,
            @RequestParam(required = false) String vehicleType) {
        log.debug("Checking courier availability for zone {}", zoneId);

        CourierAvailabilityResponse response = new CourierAvailabilityResponse(
            zoneId,
            zoneCourierPoolService.getAvailableCourierCount(zoneId),
            zoneCourierPoolService.getAvailableVehicleTypes(zoneId),
            zoneCourierPoolService.getAverageETA(zoneId),
            zoneCourierPoolService.isSurgePricing(zoneId)
        );

        return ResponseEntity.ok(response);
    }
}

package com.gogidix.shared.courier.ecommerce.application.service;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gogidix.shared.courier.ecommerce.application.command.AssignCourierCommand;
import com.gogidix.shared.courier.ecommerce.application.command.AssignHubLegCommand;
import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceCourierAssignment;
import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceDeliveryTracking;
import com.gogidix.shared.courier.ecommerce.domain.events.CourierAssignedEvent;
import com.gogidix.shared.courier.ecommerce.domain.events.CourierStatusChangedEvent;
import com.gogidix.shared.courier.ecommerce.domain.repository.EcommerceAssignmentRepository;
import com.gogidix.shared.courier.ecommerce.domain.repository.EcommerceTrackingRepository;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.AssignmentPriority;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryLeg;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryType;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.VehicleType;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.mappers.EcommerceAssignmentMapper;

@Service
@Transactional
public class EcommerceCourierAssignmentService {

    private static final Logger log = LoggerFactory.getLogger(EcommerceCourierAssignmentService.class);

    private final EcommerceAssignmentRepository assignmentRepository;
    private final EcommerceTrackingRepository trackingRepository;
    private final ZoneCourierPoolService zoneCourierPoolService;
    private final EcommerceAssignmentMapper mapper;

    public EcommerceCourierAssignmentService(EcommerceAssignmentRepository assignmentRepository,
                                             EcommerceTrackingRepository trackingRepository,
                                             ZoneCourierPoolService zoneCourierPoolService,
                                             EcommerceAssignmentMapper mapper) {
        this.assignmentRepository = assignmentRepository;
        this.trackingRepository = trackingRepository;
        this.zoneCourierPoolService = zoneCourierPoolService;
        this.mapper = mapper;
    }

    public EcommerceCourierAssignment assignCourier(AssignCourierCommand command) {
        log.info("Assigning courier for order {}, sub-order {}", command.getOrderId(), command.getSubOrderId());

        EcommerceCourierAssignment assignment = new EcommerceCourierAssignment(
            command.getOrderId(),
            command.getSubOrderId(),
            command.getVendorId(),
            command.getDeliveryType(),
            command.getPickupZone().zoneId()
        );

        assignment.setDeliveryZoneId(command.getDeliveryZone().zoneId());
        assignment.setPickupAddress(command.getPickupZone().address());
        assignment.setPickupLatitude(command.getPickupZone().latitude());
        assignment.setPickupLongitude(command.getPickupZone().longitude());
        assignment.setPickupContactName(command.getPickupZone().contactName());
        assignment.setPickupContactPhone(command.getPickupZone().contactPhone());
        assignment.setDeliveryAddress(command.getDeliveryZone().address());
        assignment.setDeliveryLatitude(command.getDeliveryZone().latitude());
        assignment.setDeliveryLongitude(command.getDeliveryZone().longitude());
        assignment.setDeliveryContactName(command.getDeliveryZone().contactName());
        assignment.setDeliveryContactPhone(command.getDeliveryZone().contactPhone());
        assignment.setPackages(mapper.toPackageInfoList(command.getPackages()));
        assignment.setPriority(command.getPriority() != null ? command.getPriority() : AssignmentPriority.NORMAL);
        assignment.setScheduledFor(command.getScheduledFor());
        assignment.setRequiresHubProcessing(command.isRequiresHubProcessing());

        if (command.getHubDetails() != null) {
            assignment.setOriginHubId(command.getHubDetails().originHubId());
            assignment.setDestinationHubId(command.getHubDetails().destinationHubId());
        }

        DeliveryLeg leg = determineDeliveryLeg(command.getDeliveryType());
        assignment.setDeliveryLeg(leg);

        ZoneCourierPoolService.CourierInfo courier = zoneCourierPoolService.findAvailableCourier(
            command.getPickupZone().zoneId(),
            determineVehicleType(command.getPackages())
        );

        String trackingId = "TRK-COURIER-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Instant now = Instant.now();
        Instant estimatedPickup = now.plusSeconds(900);
        Instant estimatedDelivery = calculateEstimatedDelivery(command.getDeliveryType(), now);

        assignment.assignCourier(
            courier.courierId(),
            courier.courierName(),
            courier.courierPhone(),
            courier.vehicleType(),
            trackingId,
            estimatedPickup,
            estimatedDelivery
        );

        EcommerceCourierAssignment saved = assignmentRepository.save(assignment);

        createInitialTracking(saved);

        log.info("Courier {} assigned for order {}, tracking ID: {}",
            courier.courierId(), command.getOrderId(), trackingId);

        return saved;
    }

    public EcommerceCourierAssignment assignHubLeg(AssignHubLegCommand command) {
        log.info("Assigning hub leg {} for order {}", command.getLeg(), command.getOrderId());

        String zoneId = command.getLeg() == DeliveryLeg.ORIGIN_PICKUP
            ? command.getOriginHub().zoneId()
            : command.getDestinationHub().zoneId();

        EcommerceCourierAssignment assignment = new EcommerceCourierAssignment(
            command.getOrderId(),
            command.getSubOrderId(),
            null,
            DeliveryType.TYPE_B,
            zoneId
        );

        assignment.setDeliveryLeg(command.getLeg());
        assignment.setRequiresHubProcessing(true);
        assignment.setOriginHubId(command.getOriginHub().hubId());
        assignment.setDestinationHubId(command.getDestinationHub().hubId());

        if (command.getLeg() == DeliveryLeg.ORIGIN_PICKUP) {
            assignment.setPickupAddress(command.getOriginHub().address());
            assignment.setPickupLatitude(command.getOriginHub().latitude());
            assignment.setPickupLongitude(command.getOriginHub().longitude());
            assignment.setDeliveryAddress(command.getDestinationHub().address());
            assignment.setDeliveryLatitude(command.getDestinationHub().latitude());
            assignment.setDeliveryLongitude(command.getDestinationHub().longitude());
        } else {
            assignment.setPickupAddress(command.getDestinationHub().address());
            assignment.setPickupLatitude(command.getDestinationHub().latitude());
            assignment.setPickupLongitude(command.getDestinationHub().longitude());
            assignment.setDeliveryAddress(command.getCustomerDelivery().address());
            assignment.setDeliveryLatitude(command.getCustomerDelivery().latitude());
            assignment.setDeliveryLongitude(command.getCustomerDelivery().longitude());
            assignment.setDeliveryContactName(command.getCustomerDelivery().contactName());
            assignment.setDeliveryContactPhone(command.getCustomerDelivery().contactPhone());
        }

        assignment.setPackages(mapper.toPackageInfoList(command.getPackages()));
        assignment.setScheduledFor(command.getScheduledFor());

        ZoneCourierPoolService.CourierInfo courier = zoneCourierPoolService.findAvailableCourier(
            zoneId, VehicleType.MOTORCYCLE
        );

        String trackingId = "TRK-HUB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Instant now = Instant.now();
        assignment.assignCourier(
            courier.courierId(),
            courier.courierName(),
            courier.courierPhone(),
            courier.vehicleType(),
            trackingId,
            now.plusSeconds(600),
            now.plusSeconds(3600)
        );

        return assignmentRepository.save(assignment);
    }

    @Transactional(readOnly = true)
    public EcommerceCourierAssignment findByOrderIdAndSubOrderId(String orderId, String subOrderId) {
        return assignmentRepository.findByOrderIdAndSubOrderId(orderId, subOrderId)
            .orElseThrow(() -> new IllegalArgumentException("Assignment not found for order " + orderId));
    }

    public void cancelAssignment(String orderId, String subOrderId, String reason) {
        EcommerceCourierAssignment assignment = findByOrderIdAndSubOrderId(orderId, subOrderId);
        String previousStatus = assignment.getStatus().name();
        assignment.cancel(reason);
        assignmentRepository.save(assignment);
        log.info("Assignment cancelled for order {}, reason: {}", orderId, reason);
    }

    public void rescheduleAssignment(String orderId, String subOrderId, Instant newScheduledFor) {
        EcommerceCourierAssignment assignment = findByOrderIdAndSubOrderId(orderId, subOrderId);
        assignment.updateSchedule(newScheduledFor);
        assignmentRepository.save(assignment);
        log.info("Assignment rescheduled for order {} to {}", orderId, newScheduledFor);
    }

    private DeliveryLeg determineDeliveryLeg(DeliveryType deliveryType) {
        return switch (deliveryType) {
            case TYPE_A -> DeliveryLeg.FULL;
            case TYPE_B, TYPE_C -> DeliveryLeg.ORIGIN_PICKUP;
        };
    }

    private Instant calculateEstimatedDelivery(DeliveryType deliveryType, Instant from) {
        return switch (deliveryType) {
            case TYPE_A -> from.plusSeconds(5400);
            case TYPE_B -> from.plusSeconds(86400);
            case TYPE_C -> from.plusSeconds(129600);
        };
    }

    private VehicleType determineVehicleType(java.util.List<com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.PackageDto> packages) {
        if (packages == null || packages.isEmpty()) return VehicleType.MOTORCYCLE;
        double totalWeight = packages.stream().mapToDouble(p -> p.weight() * p.quantity()).sum();
        if (totalWeight > 100) return VehicleType.TRUCK;
        if (totalWeight > 20) return VehicleType.VAN;
        return VehicleType.MOTORCYCLE;
    }

    private void createInitialTracking(EcommerceCourierAssignment assignment) {
        EcommerceDeliveryTracking tracking = new EcommerceDeliveryTracking(
            assignment.getOrderId(),
            assignment.getSubOrderId(),
            assignment.getDeliveryType()
        );
        tracking.setTrackingId(assignment.getTrackingId());
        tracking.updateCourierInfo(assignment.getCourierName(), assignment.getCourierPhone());
        trackingRepository.save(tracking);
    }
}

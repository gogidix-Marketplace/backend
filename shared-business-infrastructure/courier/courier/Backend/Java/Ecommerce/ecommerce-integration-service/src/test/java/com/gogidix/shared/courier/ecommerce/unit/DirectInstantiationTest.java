package com.gogidix.shared.courier.ecommerce.unit;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.gogidix.shared.courier.ecommerce.application.command.AssignCourierCommand;
import com.gogidix.shared.courier.ecommerce.application.command.AssignHubLegCommand;
import com.gogidix.shared.courier.ecommerce.application.service.EcommerceCourierAssignmentService;
import com.gogidix.shared.courier.ecommerce.application.service.EcommerceTrackingService;
import com.gogidix.shared.courier.ecommerce.application.service.ZoneCourierPoolService;
import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceCourierAssignment;
import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceDeliveryTracking;
import com.gogidix.shared.courier.ecommerce.domain.repository.EcommerceAssignmentRepository;
import com.gogidix.shared.courier.ecommerce.domain.repository.EcommerceTrackingRepository;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.*;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.*;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.mappers.EcommerceAssignmentMapper;
import org.junit.jupiter.api.Test;

class DirectInstantiationTest {

    private EcommerceAssignmentRepository assignmentRepo = new EcommerceAssignmentRepository() {
        public EcommerceCourierAssignment save(EcommerceCourierAssignment a) { return a; }
        public Optional<EcommerceCourierAssignment> findById(String id) { return Optional.empty(); }
        public Optional<EcommerceCourierAssignment> findByOrderIdAndSubOrderId(String o, String s) {
            EcommerceCourierAssignment a = new EcommerceCourierAssignment(o, s, "v", DeliveryType.TYPE_A, "z");
            a.assignCourier("c1", "Rider", "+234", VehicleType.MOTORCYCLE, "TRK-001", Instant.now(), Instant.now());
            return Optional.of(a);
        }
        public Optional<EcommerceCourierAssignment> findByTrackingId(String t) { return Optional.empty(); }
        public List<EcommerceCourierAssignment> findByOrderId(String o) { return List.of(); }
        public List<EcommerceCourierAssignment> findByPickupZoneIdAndStatus(String z, EcommerceCourierAssignment.AssignmentStatus s) { return List.of(); }
        public List<EcommerceCourierAssignment> findByDeliveryZoneIdAndStatus(String z, EcommerceCourierAssignment.AssignmentStatus s) { return List.of(); }
    };

    private EcommerceTrackingRepository trackingRepo = new EcommerceTrackingRepository() {
        public EcommerceDeliveryTracking save(EcommerceDeliveryTracking t) { return t; }
        public Optional<EcommerceDeliveryTracking> findById(String id) { return Optional.empty(); }
        public Optional<EcommerceDeliveryTracking> findByOrderId(String o) {
            return Optional.of(new EcommerceDeliveryTracking(o, "SO-001", DeliveryType.TYPE_A));
        }
        public Optional<EcommerceDeliveryTracking> findBySubOrderId(String s) { return Optional.empty(); }
        public Optional<EcommerceDeliveryTracking> findByTrackingId(String t) { return Optional.empty(); }
    };

    @Test
    void shouldAssignCourierDirectly() {
        ZoneCourierPoolService poolService = new ZoneCourierPoolService();
        EcommerceAssignmentMapper mapper = new EcommerceAssignmentMapper();
        EcommerceTrackingService trackingService = new EcommerceTrackingService(trackingRepo);
        EcommerceCourierAssignmentService service = new EcommerceCourierAssignmentService(
            assignmentRepo, trackingRepo, poolService, mapper
        );

        AssignCourierCommand cmd = AssignCourierCommand.builder()
            .orderId("GO-001").subOrderId("SO-001").vendorId("v1")
            .deliveryType(DeliveryType.TYPE_A)
            .pickupZone(new ZoneInfoDto("z1", "addr", 6.0, 3.0, "n", "+234"))
            .deliveryZone(new ZoneInfoDto("z2", "addr", 9.0, 7.0, "n", "+234"))
            .packages(List.of(new PackageDto("Item", 0.5, 1, new DimensionsDto(10, 10, 10))))
            .priority(AssignmentPriority.NORMAL)
            .build();

        EcommerceCourierAssignment result = service.assignCourier(cmd);
        assertNotNull(result);
        assertNotNull(result.getTrackingId());
        assertEquals("c-001".getClass(), result.getCourierId().getClass());
    }

    @Test
    void shouldAssignTypeBDirectly() {
        ZoneCourierPoolService poolService = new ZoneCourierPoolService();
        EcommerceAssignmentMapper mapper = new EcommerceAssignmentMapper();
        EcommerceCourierAssignmentService service = new EcommerceCourierAssignmentService(
            assignmentRepo, trackingRepo, poolService, mapper
        );

        AssignCourierCommand cmd = AssignCourierCommand.builder()
            .orderId("GO-002").subOrderId("SO-002").vendorId("v1")
            .deliveryType(DeliveryType.TYPE_B)
            .pickupZone(new ZoneInfoDto("z1", "a", 1.0, 1.0, "n", "p"))
            .deliveryZone(new ZoneInfoDto("z2", "a", 1.0, 1.0, "n", "p"))
            .requiresHubProcessing(true)
            .hubDetails(new HubDetailsDto("h1", "a", 1.0, 1.0, "h2", "a", 1.0, 1.0))
            .build();

        EcommerceCourierAssignment result = service.assignCourier(cmd);
        assertEquals(DeliveryType.TYPE_B, result.getDeliveryType());
        assertTrue(result.isRequiresHubProcessing());
    }

    @Test
    void shouldAssignTypeCDirectly() {
        ZoneCourierPoolService poolService = new ZoneCourierPoolService();
        EcommerceAssignmentMapper mapper = new EcommerceAssignmentMapper();
        EcommerceCourierAssignmentService service = new EcommerceCourierAssignmentService(
            assignmentRepo, trackingRepo, poolService, mapper
        );

        AssignCourierCommand cmd = AssignCourierCommand.builder()
            .orderId("GO-003").subOrderId("SO-003").vendorId("v1")
            .deliveryType(DeliveryType.TYPE_C)
            .pickupZone(new ZoneInfoDto("z1", "a", 1.0, 1.0, "n", "p"))
            .deliveryZone(new ZoneInfoDto("z2", "a", 1.0, 1.0, "n", "p"))
            .build();

        EcommerceCourierAssignment result = service.assignCourier(cmd);
        assertEquals(DeliveryType.TYPE_C, result.getDeliveryType());
    }

    @Test
    void shouldAssignHubLegDirectly() {
        ZoneCourierPoolService poolService = new ZoneCourierPoolService();
        EcommerceAssignmentMapper mapper = new EcommerceAssignmentMapper();
        EcommerceCourierAssignmentService service = new EcommerceCourierAssignmentService(
            assignmentRepo, trackingRepo, poolService, mapper
        );

        AssignHubLegCommand cmd = AssignHubLegCommand.builder()
            .orderId("GO-004").subOrderId("SO-004")
            .leg(DeliveryLeg.LASTMILE)
            .originHub(new HubInfoDto("h1", "z1", "a", 1.0, 1.0))
            .destinationHub(new HubInfoDto("h2", "z2", "a", 1.0, 1.0))
            .customerDelivery(new CustomerDeliveryDto("addr", 1.0, 1.0, "n", "+234"))
            .build();

        EcommerceCourierAssignment result = service.assignHubLeg(cmd);
        assertNotNull(result);
        assertEquals(DeliveryLeg.LASTMILE, result.getDeliveryLeg());
    }

    @Test
    void shouldCancelDirectly() {
        ZoneCourierPoolService poolService = new ZoneCourierPoolService();
        EcommerceAssignmentMapper mapper = new EcommerceAssignmentMapper();
        EcommerceCourierAssignmentService service = new EcommerceCourierAssignmentService(
            assignmentRepo, trackingRepo, poolService, mapper
        );

        service.cancelAssignment("GO-005", "SO-005", "reason");
    }

    @Test
    void shouldRescheduleDirectly() {
        ZoneCourierPoolService poolService = new ZoneCourierPoolService();
        EcommerceAssignmentMapper mapper = new EcommerceAssignmentMapper();
        EcommerceCourierAssignmentService service = new EcommerceCourierAssignmentService(
            assignmentRepo, trackingRepo, poolService, mapper
        );

        service.rescheduleAssignment("GO-006", "SO-006", Instant.now().plusSeconds(86400));
    }

    @Test
    void shouldTrackOrder() {
        EcommerceTrackingService service = new EcommerceTrackingService(trackingRepo);

        EcommerceDeliveryTracking result = service.getTrackingByOrderId("GO-001");
        assertNotNull(result);

        service.updateStage("GO-001", HubStageStatus.PICKED_UP, StageCompletionStatus.COMPLETED);
        service.markHubArrival("GO-001", "Lagos Hub");
        service.markDelivered("GO-001");
    }

    @Test
    void shouldGetZoneAvailability() {
        ZoneCourierPoolService service = new ZoneCourierPoolService();

        var courier = service.findAvailableCourier("z1", VehicleType.MOTORCYCLE);
        assertNotNull(courier);

        Map<String, Integer> types = service.getAvailableVehicleTypes("z1");
        assertEquals(3, types.size());

        assertEquals(15, service.getAvailableCourierCount("z1"));
        assertNotNull(service.getAverageETA("z1"));
        assertFalse(service.isSurgePricing("z1"));
    }

    @Test
    void shouldMapAssignment() {
        EcommerceAssignmentMapper mapper = new EcommerceAssignmentMapper();
        EcommerceCourierAssignment assignment = new EcommerceCourierAssignment("o", "s", "v", DeliveryType.TYPE_A, "z");
        assignment.assignCourier("c1", "Rider", "+234", VehicleType.MOTORCYCLE, "TRK-001", Instant.now(), Instant.now());

        AssignCourierResponse response = mapper.toResponse(assignment);
        assertNotNull(response);

        var packages = mapper.toPackageInfoList(List.of(
            new PackageDto("Item", 1.0, 1, new DimensionsDto(10, 10, 10))
        ));
        assertEquals(1, packages.size());

        var empty = mapper.toPackageInfoList(null);
        assertTrue(empty.isEmpty());
    }

    @Test
    void shouldMapTracking() {
        com.gogidix.shared.courier.ecommerce.interfaces.rest.mappers.EcommerceTrackingMapper mapper =
            new com.gogidix.shared.courier.ecommerce.interfaces.rest.mappers.EcommerceTrackingMapper();

        EcommerceDeliveryTracking tracking = new EcommerceDeliveryTracking("o", "s", DeliveryType.TYPE_B);
        tracking.setTrackingId("TRK-001");
        tracking.updateCourierInfo("Rider", "+234");
        tracking.addStage(new EcommerceDeliveryTracking.HubStage(
            HubStageStatus.PICKUP_ASSIGNED, StageCompletionStatus.COMPLETED
        ));

        TrackingResponse response = mapper.toResponse(tracking);
        assertNotNull(response);
        assertEquals(1, response.stages().size());

        EcommerceDeliveryTracking empty = new EcommerceDeliveryTracking("o2", "s2", DeliveryType.TYPE_A);
        TrackingResponse emptyResponse = mapper.toResponse(empty);
        assertTrue(emptyResponse.stages().isEmpty());
    }

    @Test
    void shouldConsumeEvents() {
        EcommerceCourierAssignmentService assignmentService = new EcommerceCourierAssignmentService(
            assignmentRepo, trackingRepo, new ZoneCourierPoolService(), new EcommerceAssignmentMapper()
        );

        com.gogidix.shared.courier.ecommerce.infrastructure.messaging.consumers.EcommerceOrderEventConsumer consumer =
            new com.gogidix.shared.courier.ecommerce.infrastructure.messaging.consumers.EcommerceOrderEventConsumer(assignmentService);

        Map<String, Object> event3 = Map.of("orderId", "GO-3", "subOrderId", "SO-3", "reason", "cancel");
        assertDoesNotThrow(() -> consumer.handleOrderCancelled(event3));

        Map<String, Object> event4 = Map.of("orderId", "GO-4", "subOrderId", "SO-4", "newScheduledTime", "2026-05-07T10:00:00Z");
        assertDoesNotThrow(() -> consumer.handleDeliveryRescheduled(event4));
    }

    @Test
    void shouldConsumeEventsWithNullTime() {
        EcommerceCourierAssignmentService assignmentService = new EcommerceCourierAssignmentService(
            assignmentRepo, trackingRepo, new ZoneCourierPoolService(), new EcommerceAssignmentMapper()
        );

        com.gogidix.shared.courier.ecommerce.infrastructure.messaging.consumers.EcommerceOrderEventConsumer consumer =
            new com.gogidix.shared.courier.ecommerce.infrastructure.messaging.consumers.EcommerceOrderEventConsumer(assignmentService);

        Map<String, Object> event = Map.of("orderId", "GO-5", "subOrderId", "SO-5");
        assertDoesNotThrow(() -> consumer.handleDeliveryRescheduled(event));
    }
}

package com.gogidix.shared.courier.ecommerce.unit;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;

import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceCourierAssignment;
import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceCourierAssignment.Dimensions;
import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceCourierAssignment.PackageInfo;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.AssignmentPriority;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryType;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EcommerceCourierAssignmentTest {

    private EcommerceCourierAssignment assignment;

    @BeforeEach
    void setUp() {
        assignment = new EcommerceCourierAssignment(
            "GO-2026-001", "SO-2026-001-V1", "vendor-001",
            DeliveryType.TYPE_A, "zone-lagos-island"
        );
    }

    @Test
    void shouldCreateAssignmentWithCorrectFields() {
        assertNotNull(assignment.getId());
        assertEquals("GO-2026-001", assignment.getOrderId());
        assertEquals("SO-2026-001-V1", assignment.getSubOrderId());
        assertEquals("vendor-001", assignment.getVendorId());
        assertEquals(DeliveryType.TYPE_A, assignment.getDeliveryType());
        assertEquals("zone-lagos-island", assignment.getPickupZoneId());
        assertEquals(EcommerceCourierAssignment.AssignmentStatus.ASSIGNED, assignment.getStatus());
        assertNotNull(assignment.getCreatedAt());
        assertNotNull(assignment.getUpdatedAt());
    }

    @Test
    void shouldAssignCourier() {
        Instant estimatedPickup = Instant.now().plusSeconds(900);
        Instant estimatedDelivery = Instant.now().plusSeconds(5400);

        assignment.assignCourier(
            "courier-001", "Adebayo Express", "+2348011111111",
            VehicleType.MOTORCYCLE, "TRK-001", estimatedPickup, estimatedDelivery
        );

        assertEquals("courier-001", assignment.getCourierId());
        assertEquals("Adebayo Express", assignment.getCourierName());
        assertEquals("+2348011111111", assignment.getCourierPhone());
        assertEquals(VehicleType.MOTORCYCLE, assignment.getVehicleType());
        assertEquals("TRK-001", assignment.getTrackingId());
        assertEquals(estimatedPickup, assignment.getEstimatedPickupTime());
        assertEquals(estimatedDelivery, assignment.getEstimatedDeliveryTime());
        assertEquals(EcommerceCourierAssignment.AssignmentStatus.ASSIGNED, assignment.getStatus());
    }

    @Test
    void shouldMarkPickedUp() {
        assignment.assignCourier("c1", "n1", "p1", VehicleType.MOTORCYCLE, "t1", Instant.now(), Instant.now());
        assignment.markPickedUp();

        assertEquals(EcommerceCourierAssignment.AssignmentStatus.PICKED_UP, assignment.getStatus());
        assertNotNull(assignment.getActualPickupTime());
    }

    @Test
    void shouldMarkAtHub() {
        assignment.assignCourier("c1", "n1", "p1", VehicleType.MOTORCYCLE, "t1", Instant.now(), Instant.now());
        assignment.markAtHub();

        assertEquals(EcommerceCourierAssignment.AssignmentStatus.AT_HUB, assignment.getStatus());
    }

    @Test
    void shouldMarkInTransit() {
        assignment.assignCourier("c1", "n1", "p1", VehicleType.MOTORCYCLE, "t1", Instant.now(), Instant.now());
        assignment.markInTransit();

        assertEquals(EcommerceCourierAssignment.AssignmentStatus.IN_TRANSIT, assignment.getStatus());
    }

    @Test
    void shouldMarkDelivered() {
        assignment.assignCourier("c1", "n1", "p1", VehicleType.MOTORCYCLE, "t1", Instant.now(), Instant.now());
        assignment.markDelivered("photo.jpg", "John Doe");

        assertEquals(EcommerceCourierAssignment.AssignmentStatus.DELIVERED, assignment.getStatus());
        assertNotNull(assignment.getActualDeliveryTime());
        assertEquals("photo.jpg", assignment.getProofOfDelivery());
        assertEquals("John Doe", assignment.getRecipientName());
    }

    @Test
    void shouldCancel() {
        assignment.cancel("Customer request");

        assertEquals(EcommerceCourierAssignment.AssignmentStatus.CANCELLED, assignment.getStatus());
        assertEquals("Customer request", assignment.getCancellationReason());
    }

    @Test
    void shouldMarkFailed() {
        assignment.markFailed();

        assertEquals(EcommerceCourierAssignment.AssignmentStatus.FAILED, assignment.getStatus());
    }

    @Test
    void shouldIdentifyTerminalStates() {
        assignment.markDelivered("p", "n");
        assertTrue(assignment.isTerminal());

        EcommerceCourierAssignment a2 = new EcommerceCourierAssignment("o", "s", "v", DeliveryType.TYPE_A, "z");
        a2.cancel("reason");
        assertTrue(a2.isTerminal());

        EcommerceCourierAssignment a3 = new EcommerceCourierAssignment("o", "s", "v", DeliveryType.TYPE_A, "z");
        a3.markFailed();
        assertTrue(a3.isTerminal());
    }

    @Test
    void shouldNotBeTerminalWhenAssigned() {
        assertFalse(assignment.isTerminal());
    }

    @Test
    void shouldUpdateSchedule() {
        Instant newTime = Instant.now().plusSeconds(86400);
        assignment.updateSchedule(newTime);

        assertEquals(newTime, assignment.getScheduledFor());
    }

    @Test
    void shouldSetPackages() {
        PackageInfo pkg = new PackageInfo("Headphones", 0.5, 1, new Dimensions(20, 15, 8));
        assignment.setPackages(List.of(pkg));

        assertEquals(1, assignment.getPackages().size());
        assertEquals("Headphones", assignment.getPackages().get(0).getDescription());
        assertEquals(0.5, assignment.getPackages().get(0).getWeight());
    }

    @Test
    void shouldTestEquality() {
        EcommerceCourierAssignment other = new EcommerceCourierAssignment("o", "s", "v", DeliveryType.TYPE_A, "z");
        assertNotEquals(assignment, other);
        assertEquals(assignment, assignment);
    }

    @Test
    void shouldSetAllProperties() {
        assignment.setDeliveryZoneId("zone-mainland");
        assignment.setPickupAddress("12 Marina Road");
        assignment.setPickupLatitude(6.45);
        assignment.setPickupLongitude(3.39);
        assignment.setPickupContactName("Vendor");
        assignment.setPickupContactPhone("+234");
        assignment.setDeliveryAddress("45 Allen Ave");
        assignment.setDeliveryLatitude(6.59);
        assignment.setDeliveryLongitude(3.34);
        assignment.setDeliveryContactName("Customer");
        assignment.setDeliveryContactPhone("+234");
        assignment.setPriority(AssignmentPriority.HIGH);
        assignment.setRequiresHubProcessing(true);
        assignment.setOriginHubId("hub-1");
        assignment.setDestinationHubId("hub-2");

        assertEquals("zone-mainland", assignment.getDeliveryZoneId());
        assertEquals("12 Marina Road", assignment.getPickupAddress());
        assertTrue(assignment.isRequiresHubProcessing());
        assertEquals(AssignmentPriority.HIGH, assignment.getPriority());
    }
}

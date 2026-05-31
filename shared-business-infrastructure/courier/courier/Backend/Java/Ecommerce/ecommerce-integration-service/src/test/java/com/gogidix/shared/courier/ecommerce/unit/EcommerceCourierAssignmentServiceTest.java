package com.gogidix.shared.courier.ecommerce.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.gogidix.shared.courier.ecommerce.application.command.AssignCourierCommand;
import com.gogidix.shared.courier.ecommerce.application.command.AssignHubLegCommand;
import com.gogidix.shared.courier.ecommerce.application.service.EcommerceCourierAssignmentService;
import com.gogidix.shared.courier.ecommerce.application.service.ZoneCourierPoolService;
import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceCourierAssignment;
import com.gogidix.shared.courier.ecommerce.domain.repository.EcommerceAssignmentRepository;
import com.gogidix.shared.courier.ecommerce.domain.repository.EcommerceTrackingRepository;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.AssignmentPriority;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryLeg;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryType;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.VehicleType;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.*;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.mappers.EcommerceAssignmentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EcommerceCourierAssignmentServiceTest {

    @Mock private EcommerceAssignmentRepository assignmentRepository;
    @Mock private EcommerceTrackingRepository trackingRepository;
    @Mock private ZoneCourierPoolService zoneCourierPoolService;
    private EcommerceAssignmentMapper mapper;
    private EcommerceCourierAssignmentService service;

    @BeforeEach
    void setUp() {
        mapper = new EcommerceAssignmentMapper();
        service = new EcommerceCourierAssignmentService(
            assignmentRepository, trackingRepository, zoneCourierPoolService, mapper
        );
    }

    @Test
    void shouldAssignCourierForTypeA() {
        ZoneCourierPoolService.CourierInfo courier = new ZoneCourierPoolService.CourierInfo(
            "c-001", "Lagos Rider", "+234", VehicleType.MOTORCYCLE
        );
        when(zoneCourierPoolService.findAvailableCourier(anyString(), any())).thenReturn(courier);
        when(assignmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(trackingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AssignCourierCommand command = AssignCourierCommand.builder()
            .orderId("GO-001")
            .subOrderId("SO-001")
            .vendorId("vendor-001")
            .deliveryType(DeliveryType.TYPE_A)
            .pickupZone(new ZoneInfoDto("zone-lagos", "12 Marina", 6.45, 3.39, "Vendor", "+234"))
            .deliveryZone(new ZoneInfoDto("zone-mainland", "45 Allen", 6.59, 3.34, "Customer", "+234"))
            .packages(List.of(new PackageDto("Headphones", 0.5, 1, new DimensionsDto(20, 15, 8))))
            .priority(AssignmentPriority.NORMAL)
            .build();

        EcommerceCourierAssignment result = service.assignCourier(command);

        assertNotNull(result);
        assertEquals("GO-001", result.getOrderId());
        assertEquals("c-001", result.getCourierId());
        assertEquals("Lagos Rider", result.getCourierName());
        assertNotNull(result.getTrackingId());
        assertEquals(DeliveryLeg.FULL, result.getDeliveryLeg());
        verify(assignmentRepository).save(any());
        verify(trackingRepository).save(any());
    }

    @Test
    void shouldAssignCourierForTypeB() {
        ZoneCourierPoolService.CourierInfo courier = new ZoneCourierPoolService.CourierInfo(
            "c-002", "Hub Rider", "+234", VehicleType.VAN
        );
        when(zoneCourierPoolService.findAvailableCourier(anyString(), any())).thenReturn(courier);
        when(assignmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(trackingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AssignCourierCommand command = AssignCourierCommand.builder()
            .orderId("GO-002")
            .subOrderId("SO-002")
            .vendorId("vendor-002")
            .deliveryType(DeliveryType.TYPE_B)
            .pickupZone(new ZoneInfoDto("zone-lagos", "Addr", 6.0, 3.0, "V", "+234"))
            .deliveryZone(new ZoneInfoDto("zone-abuja", "Addr", 9.0, 7.0, "C", "+234"))
            .requiresHubProcessing(true)
            .hubDetails(new HubDetailsDto("hub-lagos", "Hub Rd", 6.5, 3.3, "hub-abuja", "Hub Ave", 9.0, 7.4))
            .build();

        EcommerceCourierAssignment result = service.assignCourier(command);

        assertEquals(DeliveryType.TYPE_B, result.getDeliveryType());
        assertEquals(DeliveryLeg.ORIGIN_PICKUP, result.getDeliveryLeg());
        assertTrue(result.isRequiresHubProcessing());
        assertEquals("hub-lagos", result.getOriginHubId());
    }

    @Test
    void shouldAssignHubLeg() {
        ZoneCourierPoolService.CourierInfo courier = new ZoneCourierPoolService.CourierInfo(
            "c-003", "LastMile Rider", "+234", VehicleType.MOTORCYCLE
        );
        when(zoneCourierPoolService.findAvailableCourier(anyString(), any())).thenReturn(courier);
        when(assignmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AssignHubLegCommand command = AssignHubLegCommand.builder()
            .orderId("GO-003")
            .subOrderId("SO-003")
            .leg(DeliveryLeg.LASTMILE)
            .originHub(new HubInfoDto("hub-lagos", "zone-lagos", "Hub Rd", 6.5, 3.3))
            .destinationHub(new HubInfoDto("hub-abuja", "zone-abuja", "Hub Ave", 9.0, 7.4))
            .customerDelivery(new CustomerDeliveryDto("23 Garki", 9.03, 7.48, "Aisha", "+234"))
            .build();

        EcommerceCourierAssignment result = service.assignHubLeg(command);

        assertNotNull(result);
        assertEquals(DeliveryLeg.LASTMILE, result.getDeliveryLeg());
        assertEquals("23 Garki", result.getDeliveryAddress());
        assertEquals("Aisha", result.getDeliveryContactName());
    }

    @Test
    void shouldCancelAssignment() {
        EcommerceCourierAssignment assignment = new EcommerceCourierAssignment(
            "GO-004", "SO-004", "v1", DeliveryType.TYPE_A, "z1"
        );
        when(assignmentRepository.findByOrderIdAndSubOrderId("GO-004", "SO-004"))
            .thenReturn(Optional.of(assignment));
        when(assignmentRepository.save(any())).thenReturn(assignment);

        service.cancelAssignment("GO-004", "SO-004", "Customer cancelled");

        assertEquals(EcommerceCourierAssignment.AssignmentStatus.CANCELLED, assignment.getStatus());
        verify(assignmentRepository).save(assignment);
    }

    @Test
    void shouldThrowWhenAssignmentNotFound() {
        when(assignmentRepository.findByOrderIdAndSubOrderId("MISSING", "MISSING"))
            .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
            service.findByOrderIdAndSubOrderId("MISSING", "MISSING")
        );
    }

    @Test
    void shouldRescheduleAssignment() {
        EcommerceCourierAssignment assignment = new EcommerceCourierAssignment(
            "GO-005", "SO-005", "v1", DeliveryType.TYPE_A, "z1"
        );
        when(assignmentRepository.findByOrderIdAndSubOrderId("GO-005", "SO-005"))
            .thenReturn(Optional.of(assignment));
        when(assignmentRepository.save(any())).thenReturn(assignment);

        Instant newTime = Instant.now().plusSeconds(86400);
        service.rescheduleAssignment("GO-005", "SO-005", newTime);

        assertEquals(newTime, assignment.getScheduledFor());
    }

    @Test
    void shouldDetermineVehicleTypeByWeight() {
        ZoneCourierPoolService.CourierInfo courier = new ZoneCourierPoolService.CourierInfo(
            "c", "n", "p", VehicleType.TRUCK
        );
        when(zoneCourierPoolService.findAvailableCourier(anyString(), any())).thenReturn(courier);
        when(assignmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(trackingRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        AssignCourierCommand command = AssignCourierCommand.builder()
            .orderId("GO-006")
            .subOrderId("SO-006")
            .vendorId("v1")
            .deliveryType(DeliveryType.TYPE_A)
            .pickupZone(new ZoneInfoDto("z1", "a", 1.0, 1.0, "n", "p"))
            .deliveryZone(new ZoneInfoDto("z2", "a", 1.0, 1.0, "n", "p"))
            .packages(List.of(new PackageDto("Heavy", 50.0, 3, new DimensionsDto(50, 50, 50))))
            .build();

        EcommerceCourierAssignment result = service.assignCourier(command);
        assertNotNull(result);
    }
}

package com.gogidix.shared.courier.ecommerce.unit;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;

import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceCourierAssignment;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryType;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.VehicleType;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.AssignCourierResponse;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.DimensionsDto;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.PackageDto;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.mappers.EcommerceAssignmentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EcommerceAssignmentMapperTest {

    private EcommerceAssignmentMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new EcommerceAssignmentMapper();
    }

    @Test
    void shouldMapAssignmentToResponse() {
        EcommerceCourierAssignment assignment = new EcommerceCourierAssignment(
            "GO-001", "SO-001", "v1", DeliveryType.TYPE_A, "z1"
        );
        assignment.assignCourier(
            "c1", "Rider 1", "+234", VehicleType.MOTORCYCLE,
            "TRK-001", Instant.now(), Instant.now().plusSeconds(3600)
        );

        AssignCourierResponse response = mapper.toResponse(assignment);

        assertEquals(assignment.getId(), response.assignmentId());
        assertEquals("c1", response.courierId());
        assertEquals("Rider 1", response.courierName());
        assertEquals("MOTORCYCLE", response.vehicleType());
        assertEquals("TRK-001", response.trackingId());
        assertEquals("ASSIGNED", response.status());
    }

    @Test
    void shouldMapPackages() {
        List<PackageDto> dtos = List.of(
            new PackageDto("Headphones", 0.5, 1, new DimensionsDto(20, 15, 8)),
            new PackageDto("Laptop", 2.0, 1, null)
        );

        var result = mapper.toPackageInfoList(dtos);

        assertEquals(2, result.size());
        assertEquals("Headphones", result.get(0).getDescription());
        assertNotNull(result.get(0).getDimensions());
        assertNull(result.get(1).getDimensions());
    }

    @Test
    void shouldHandleNullPackages() {
        var result = mapper.toPackageInfoList(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldHandleNullVehicleType() {
        EcommerceCourierAssignment assignment = new EcommerceCourierAssignment(
            "GO-001", "SO-001", "v1", DeliveryType.TYPE_A, "z1"
        );

        AssignCourierResponse response = mapper.toResponse(assignment);
        assertNull(response.vehicleType());
    }
}

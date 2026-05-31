package com.gogidix.shared.courier.ecommerce.unit;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gogidix.shared.courier.ecommerce.application.command.AssignCourierCommand;
import com.gogidix.shared.courier.ecommerce.application.command.AssignHubLegCommand;
import com.gogidix.shared.courier.ecommerce.application.service.EcommerceCourierAssignmentService;
import com.gogidix.shared.courier.ecommerce.application.service.EcommerceTrackingService;
import com.gogidix.shared.courier.ecommerce.application.service.ZoneCourierPoolService;
import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceCourierAssignment;
import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceDeliveryTracking;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryType;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.VehicleType;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.*;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.*;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.mappers.EcommerceAssignmentMapper;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.mappers.EcommerceTrackingMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class ControllerUnitTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Mock private EcommerceCourierAssignmentService assignmentService;
    @Mock private EcommerceTrackingService trackingService;
    @Mock private ZoneCourierPoolService zoneCourierPoolService;
    @Mock private EcommerceAssignmentMapper assignmentMapper;
    @Mock private EcommerceTrackingMapper trackingMapper;

    @InjectMocks private EcommerceCourierController courierController;
    @InjectMocks private EcommerceTrackingController trackingController;
    @InjectMocks private HealthController healthController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(courierController, trackingController, healthController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
            .build();
    }

    @Test
    void healthEndpoint() throws Exception {
        mockMvc.perform(get("/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"))
            .andExpect(jsonPath("$.service").value("ecommerce-integration-service"));
    }

    @Test
    void readyEndpoint() throws Exception {
        mockMvc.perform(get("/health/ready"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void liveEndpoint() throws Exception {
        mockMvc.perform(get("/health/live"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void assignCourierEndpoint() throws Exception {
        AssignCourierRequest request = new AssignCourierRequest(
            "GO-001", "SO-001", "v1", "TYPE_A",
            new ZoneInfoDto("z1", "addr", 6.0, 3.0, "name", "+234"),
            new ZoneInfoDto("z2", "addr", 9.0, 7.0, "name", "+234"),
            null, "NORMAL", null, false, null
        );

        EcommerceCourierAssignment assignment = new EcommerceCourierAssignment("GO-001", "SO-001", "v1", DeliveryType.TYPE_A, "z1");
        assignment.assignCourier("c1", "Rider", "+234", VehicleType.MOTORCYCLE, "TRK-001", Instant.now(), Instant.now());

        when(assignmentService.assignCourier(any(AssignCourierCommand.class))).thenReturn(assignment);
        when(assignmentMapper.toResponse(any())).thenReturn(
            new AssignCourierResponse("id", "c1", "Rider", "+234", "MOTORCYCLE", Instant.now(), Instant.now(), "TRK-001", "ASSIGNED"));

        mockMvc.perform(post("/ecommerce/courier/assign")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.courierId").value("c1"));
    }

    @Test
    void assignCourierEndpointNullPriority() throws Exception {
        AssignCourierRequest request = new AssignCourierRequest(
            "GO-002", "SO-002", "v1", "TYPE_B",
            new ZoneInfoDto("z1", "addr", 6.0, 3.0, "n", "+234"),
            new ZoneInfoDto("z2", "addr", 9.0, 7.0, "n", "+234"),
            null, null, null, false, null
        );

        EcommerceCourierAssignment assignment = new EcommerceCourierAssignment("GO-002", "SO-002", "v1", DeliveryType.TYPE_B, "z1");
        when(assignmentService.assignCourier(any())).thenReturn(assignment);
        when(assignmentMapper.toResponse(any())).thenReturn(
            new AssignCourierResponse("id", "c1", "n", "+234", "MOTORCYCLE", Instant.now(), Instant.now(), "TRK-001", "ASSIGNED"));

        mockMvc.perform(post("/ecommerce/courier/assign")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }

    @Test
    void assignHubLegEndpoint() throws Exception {
        AssignHubLegRequest request = new AssignHubLegRequest(
            "GO-003", "SO-003", "LASTMILE",
            new HubInfoDto("h1", "z1", "addr", 6.0, 3.0),
            new HubInfoDto("h2", "z2", "addr", 9.0, 7.0),
            new CustomerDeliveryDto("addr", 9.0, 7.0, "n", "+234"),
            null, null
        );

        EcommerceCourierAssignment assignment = new EcommerceCourierAssignment("GO-003", "SO-003", null, DeliveryType.TYPE_B, "z1");
        when(assignmentService.assignHubLeg(any(AssignHubLegCommand.class))).thenReturn(assignment);
        when(assignmentMapper.toResponse(any())).thenReturn(
            new AssignCourierResponse("id", "c1", "Rider", "+234", "MOTORCYCLE", Instant.now(), Instant.now(), "TRK-001", "ASSIGNED"));

        mockMvc.perform(post("/ecommerce/courier/assign-hub-leg")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }

    @Test
    void checkAvailabilityEndpoint() throws Exception {
        when(zoneCourierPoolService.getAvailableCourierCount("zone-1")).thenReturn(10);
        when(zoneCourierPoolService.getAvailableVehicleTypes("zone-1")).thenReturn(Map.of("MOTORCYCLE", 5));
        when(zoneCourierPoolService.getAverageETA("zone-1")).thenReturn("10 min");
        when(zoneCourierPoolService.isSurgePricing("zone-1")).thenReturn(true);

        mockMvc.perform(get("/ecommerce/courier/availability")
                .param("zoneId", "zone-1")
                .param("vehicleType", "MOTORCYCLE"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.availableCouriers").value(10))
            .andExpect(jsonPath("$.surgePricing").value(true));
    }

    @Test
    void getTrackingEndpoint() throws Exception {
        EcommerceDeliveryTracking tracking = new EcommerceDeliveryTracking("GO-001", "SO-001", DeliveryType.TYPE_B);
        when(trackingService.getTrackingByOrderId("GO-001")).thenReturn(tracking);
        when(trackingMapper.toResponse(any())).thenReturn(
            new TrackingResponse("GO-001", "SO-001", "TYPE_B", "PICKUP_ASSIGNED", null, null));

        mockMvc.perform(get("/ecommerce/tracking/GO-001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.orderId").value("GO-001"));
    }

    @Test
    void getTrackingNotFoundEndpoint() throws Exception {
        when(trackingService.getTrackingByOrderId("MISSING"))
            .thenThrow(new IllegalArgumentException("Not found"));

        mockMvc.perform(get("/ecommerce/tracking/MISSING"))
            .andExpect(status().isNotFound());
    }
}

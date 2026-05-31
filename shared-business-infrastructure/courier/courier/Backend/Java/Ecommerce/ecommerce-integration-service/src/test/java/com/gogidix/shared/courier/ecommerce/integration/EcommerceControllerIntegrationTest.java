package com.gogidix.shared.courier.ecommerce.integration;

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
class EcommerceControllerIntegrationTest {

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
    void shouldReturnHealth() throws Exception {
        mockMvc.perform(get("/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"))
            .andExpect(jsonPath("$.service").value("ecommerce-integration-service"));
    }

    @Test
    void shouldReturnReady() throws Exception {
        mockMvc.perform(get("/health/ready"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void shouldReturnLive() throws Exception {
        mockMvc.perform(get("/health/live"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void shouldCheckCourierAvailability() throws Exception {
        when(zoneCourierPoolService.getAvailableCourierCount("zone-lagos")).thenReturn(15);
        when(zoneCourierPoolService.getAvailableVehicleTypes("zone-lagos"))
            .thenReturn(Map.of("MOTORCYCLE", 10, "VAN", 3, "TRUCK", 2));
        when(zoneCourierPoolService.getAverageETA("zone-lagos")).thenReturn("15 minutes");
        when(zoneCourierPoolService.isSurgePricing("zone-lagos")).thenReturn(false);

        mockMvc.perform(get("/ecommerce/courier/availability")
                .param("zoneId", "zone-lagos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.zoneId").value("zone-lagos"))
            .andExpect(jsonPath("$.availableCouriers").value(15))
            .andExpect(jsonPath("$.surgePricing").value(false));
    }

    @Test
    void shouldAssignCourier() throws Exception {
        AssignCourierRequest request = new AssignCourierRequest(
            "GO-001", "SO-001", "vendor-001", "TYPE_A",
            new ZoneInfoDto("zone-lagos", "12 Marina", 6.45, 3.39, "Vendor", "+234"),
            new ZoneInfoDto("zone-mainland", "45 Allen", 6.59, 3.34, "Customer", "+234"),
            null, "NORMAL", null, false, null
        );

        EcommerceCourierAssignment mockAssignment = new EcommerceCourierAssignment(
            "GO-001", "SO-001", "v1", DeliveryType.TYPE_A, "zone-lagos"
        );
        mockAssignment.assignCourier("c1", "Rider", "+234", VehicleType.MOTORCYCLE,
            "TRK-001", Instant.now(), Instant.now());

        when(assignmentService.assignCourier(any(AssignCourierCommand.class))).thenReturn(mockAssignment);
        when(assignmentMapper.toResponse(any())).thenReturn(
            new AssignCourierResponse("id", "c1", "Rider", "+234", "MOTORCYCLE",
                Instant.now(), Instant.now(), "TRK-001", "ASSIGNED")
        );

        mockMvc.perform(post("/ecommerce/courier/assign")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.assignmentId").exists())
            .andExpect(jsonPath("$.courierId").value("c1"))
            .andExpect(jsonPath("$.trackingId").value("TRK-001"));
    }

    @Test
    void shouldAssignHubLeg() throws Exception {
        AssignHubLegRequest request = new AssignHubLegRequest(
            "GO-002", "SO-002", "ORIGIN_PICKUP",
            new HubInfoDto("hub-lagos", "zone-lagos", "Hub Rd", 6.5, 3.3),
            new HubInfoDto("hub-abuja", "zone-abuja", "Hub Ave", 9.0, 7.4),
            new CustomerDeliveryDto("23 Garki", 9.03, 7.48, "Aisha", "+234"),
            null, null
        );

        EcommerceCourierAssignment mockAssignment = new EcommerceCourierAssignment(
            "GO-002", "SO-002", null, DeliveryType.TYPE_B, "zone-lagos"
        );

        when(assignmentService.assignHubLeg(any(AssignHubLegCommand.class))).thenReturn(mockAssignment);
        when(assignmentMapper.toResponse(any())).thenReturn(
            new AssignCourierResponse("id", "c1", "Rider", "+234", "MOTORCYCLE",
                Instant.now(), Instant.now(), "TRK-HUB-001", "ASSIGNED")
        );

        mockMvc.perform(post("/ecommerce/courier/assign-hub-leg")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.trackingId").value("TRK-HUB-001"));
    }

    @Test
    void shouldGetTracking() throws Exception {
        EcommerceDeliveryTracking mockTracking = new EcommerceDeliveryTracking(
            "GO-001", "SO-001", DeliveryType.TYPE_B
        );
        when(trackingService.getTrackingByOrderId("GO-001")).thenReturn(mockTracking);
        when(trackingMapper.toResponse(any())).thenReturn(
            new TrackingResponse("GO-001", "SO-001", "TYPE_B", "PICKUP_ASSIGNED", null, null)
        );

        mockMvc.perform(get("/ecommerce/tracking/GO-001"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.orderId").value("GO-001"))
            .andExpect(jsonPath("$.deliveryType").value("TYPE_B"));
    }

    @Test
    void shouldReturn404WhenTrackingNotFound() throws Exception {
        when(trackingService.getTrackingByOrderId("MISSING"))
            .thenThrow(new IllegalArgumentException("Tracking not found"));

        mockMvc.perform(get("/ecommerce/tracking/MISSING"))
            .andExpect(status().isNotFound());
    }
}

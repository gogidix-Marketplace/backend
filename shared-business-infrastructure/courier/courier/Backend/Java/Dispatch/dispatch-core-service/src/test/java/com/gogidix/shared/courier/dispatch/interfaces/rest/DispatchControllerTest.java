package com.gogidix.shared.courier.dispatch.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.shared.courier.dispatch.application.dto.DispatchOrderDTO;
import com.gogidix.shared.courier.dispatch.application.service.DispatchApplicationService;
import com.gogidix.shared.courier.dispatch.interfaces.rest.dto.AssignmentRequest;
import com.gogidix.shared.courier.dispatch.interfaces.rest.dto.CancellationRequest;
import com.gogidix.shared.courier.dispatch.interfaces.rest.dto.CompletionRequest;
import com.gogidix.shared.courier.dispatch.interfaces.rest.dto.DispatchRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for DispatchController
 * Uses standalone MockMvc - no Spring context required
 */
@ExtendWith(MockitoExtension.class)
class DispatchControllerTest {

    @Mock
    private DispatchApplicationService dispatchApplicationService;

    @InjectMocks
    private DispatchController dispatchController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dispatchController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldCreateDispatch() throws Exception {
        DispatchRequest request = DispatchRequest.builder()
                .orderId("order-001")
                .customerId("customer-001")
                .pickupLocation(DispatchRequest.LocationDto.builder()
                        .latitude(40.7484)
                        .longitude(-73.9857)
                        .build())
                .deliveryLocation(DispatchRequest.LocationDto.builder()
                        .latitude(40.7128)
                        .longitude(-74.0060)
                        .build())
                .pickupAddress("123 Pickup St")
                .deliveryAddress("456 Delivery Ave")
                .priority("MEDIUM")
                .build();

        DispatchOrderDTO dispatchDTO = DispatchOrderDTO.builder()
                .id("dispatch-doc-001")
                .dispatchId("dispatch-001")
                .orderId("order-001")
                .customerId("customer-001")
                .pickupLocation(DispatchOrderDTO.LocationDto.builder()
                        .latitude(40.7484)
                        .longitude(-73.9857)
                        .build())
                .deliveryLocation(DispatchOrderDTO.LocationDto.builder()
                        .latitude(40.7128)
                        .longitude(-74.0060)
                        .build())
                .pickupAddress("123 Pickup St")
                .deliveryAddress("456 Delivery Ave")
                .status("PENDING")
                .priority(2)
                .build();

        when(dispatchApplicationService.createDispatch(eq("tenant-001"), any()))
                .thenReturn(dispatchDTO);

        mockMvc.perform(post("/api/v1/dispatch/orders")
                        .header("X-Tenant-ID", "tenant-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dispatchId").value("dispatch-001"))
                .andExpect(jsonPath("$.orderId").value("order-001"));
    }

    @Test
    void shouldGetDispatch() throws Exception {
        DispatchOrderDTO dispatchDTO = DispatchOrderDTO.builder()
                .id("dispatch-doc-001")
                .dispatchId("dispatch-001")
                .orderId("order-001")
                .customerId("customer-001")
                .status("PENDING")
                .build();

        when(dispatchApplicationService.getDispatch("tenant-001", "dispatch-001"))
                .thenReturn(dispatchDTO);

        mockMvc.perform(get("/api/v1/dispatch/orders/dispatch-001")
                        .header("X-Tenant-ID", "tenant-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dispatchId").value("dispatch-001"));
    }

    @Test
    void shouldListDispatches() throws Exception {
        DispatchOrderDTO dispatchDTO = DispatchOrderDTO.builder()
                .id("dispatch-doc-001")
                .dispatchId("dispatch-001")
                .orderId("order-001")
                .status("PENDING")
                .build();

        when(dispatchApplicationService.queryDispatches(eq("tenant-001"), any()))
                .thenReturn(List.of(dispatchDTO));

        mockMvc.perform(get("/api/v1/dispatch/orders")
                        .header("X-Tenant-ID", "tenant-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldAssignDriver() throws Exception {
        AssignmentRequest request = AssignmentRequest.builder()
                .driverId("driver-001")
                .vehicleId("vehicle-001")
                .build();

        DispatchOrderDTO dispatchDTO = DispatchOrderDTO.builder()
                .id("dispatch-doc-001")
                .dispatchId("dispatch-001")
                .status("ASSIGNED")
                .assignedDriverId("driver-001")
                .assignedVehicleId("vehicle-001")
                .build();

        when(dispatchApplicationService.assignDriver(eq("tenant-001"), eq("dispatch-001"), any()))
                .thenReturn(dispatchDTO);

        mockMvc.perform(put("/api/v1/dispatch/orders/dispatch-001/assign")
                        .header("X-Tenant-ID", "tenant-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assignedDriverId").value("driver-001"));
    }

    @Test
    void shouldCancelDispatch() throws Exception {
        CancellationRequest request = CancellationRequest.builder()
                .cancelledBy("admin")
                .cancellationReason("Customer request")
                .build();

        DispatchOrderDTO dispatchDTO = DispatchOrderDTO.builder()
                .id("dispatch-doc-001")
                .dispatchId("dispatch-001")
                .status("CANCELLED")
                .build();

        when(dispatchApplicationService.cancelDispatch(eq("tenant-001"), eq("dispatch-001"), any()))
                .thenReturn(dispatchDTO);

        mockMvc.perform(put("/api/v1/dispatch/orders/dispatch-001/cancel")
                        .header("X-Tenant-ID", "tenant-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void shouldCompleteDispatch() throws Exception {
        CompletionRequest request = CompletionRequest.builder()
                .actualDistanceMeters(5000.0)
                .actualDurationMinutes(30L)
                .build();

        DispatchOrderDTO dispatchDTO = DispatchOrderDTO.builder()
                .id("dispatch-doc-001")
                .dispatchId("dispatch-001")
                .status("DELIVERED")
                .distanceMeters(5000.0)
                .build();

        when(dispatchApplicationService.completeDispatch(eq("tenant-001"), eq("dispatch-001"), any()))
                .thenReturn(dispatchDTO);

        mockMvc.perform(put("/api/v1/dispatch/orders/dispatch-001/complete")
                        .header("X-Tenant-ID", "tenant-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DELIVERED"));
    }

    @Test
    void shouldGetDashboard() throws Exception {
        when(dispatchApplicationService.getDashboard("tenant-001"))
                .thenReturn(Map.of("totalDispatches", 10L, "pendingDispatches", 2L));

        mockMvc.perform(get("/api/v1/dispatch/dashboard")
                        .header("X-Tenant-ID", "tenant-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalDispatches").value(10))
                .andExpect(jsonPath("$.pendingDispatches").value(2));
    }

    @Test
    void shouldFindNearbyPickups() throws Exception {
        DispatchOrderDTO dispatchDTO = DispatchOrderDTO.builder()
                .id("dispatch-doc-001")
                .dispatchId("dispatch-001")
                .status("PENDING")
                .build();

        when(dispatchApplicationService.findNearbyPickups(eq("tenant-001"), eq(40.7484), eq(-73.9857), eq(5.0)))
                .thenReturn(List.of(dispatchDTO));

        mockMvc.perform(get("/api/v1/dispatch/nearby-pickups")
                        .header("X-Tenant-ID", "tenant-001")
                        .param("latitude", "40.7484")
                        .param("longitude", "-73.9857")
                        .param("radiusKm", "5.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}

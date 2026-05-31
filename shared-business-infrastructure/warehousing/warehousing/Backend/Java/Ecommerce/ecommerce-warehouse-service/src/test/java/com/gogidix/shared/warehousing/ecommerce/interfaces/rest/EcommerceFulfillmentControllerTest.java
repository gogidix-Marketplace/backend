package com.gogidix.shared.warehousing.ecommerce.interfaces.rest;

import com.gogidix.shared.warehousing.ecommerce.application.service.EcommerceFulfillmentService;
import com.gogidix.shared.warehousing.ecommerce.interfaces.rest.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EcommerceFulfillmentControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private EcommerceFulfillmentService fulfillmentService;

    @InjectMocks
    private EcommerceFulfillmentController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createFulfillment_returns201() throws Exception {
        EcommerceFulfillmentRequest request = EcommerceFulfillmentRequest.builder()
                .orderId("GO-2026-04723")
                .subOrderId("SO-2026-04723-V1")
                .vendorId("vendor-001")
                .warehouseId("wh-lagos-01")
                .items(List.of(
                        EcommerceFulfillmentRequest.OrderItem.builder()
                                .sku("TECH-001-BT").productName("Headphones").quantity(1).build()
                ))
                .priority("HIGH")
                .deliveryType("TYPE_A")
                .customerAddress(EcommerceFulfillmentRequest.CustomerAddress.builder()
                        .address("45 Allen Avenue").latitude(6.5963).longitude(3.3420).build())
                .build();

        EcommerceFulfillmentResponse response = EcommerceFulfillmentResponse.builder()
                .fulfillmentId("ful-001")
                .orderId("GO-2026-04723")
                .subOrderId("SO-2026-04723-V1")
                .warehouseId("wh-lagos-01")
                .status("RECEIVED")
                .estimatedCompletion(LocalDateTime.now().plusHours(2))
                .stages(List.of(
                        EcommerceFulfillmentResponse.StageInfo.builder()
                                .stage("RECEIVED").status("COMPLETED").build()
                ))
                .build();

        when(fulfillmentService.createFulfillment(any(EcommerceFulfillmentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/ecommerce/warehouse/fulfill")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fulfillmentId").value("ful-001"))
                .andExpect(jsonPath("$.orderId").value("GO-2026-04723"))
                .andExpect(jsonPath("$.status").value("RECEIVED"));
    }

    @Test
    void createFulfillment_missingOrderId_returns400() throws Exception {
        EcommerceFulfillmentRequest request = EcommerceFulfillmentRequest.builder()
                .subOrderId("SO-001")
                .vendorId("vendor-001")
                .warehouseId("wh-01")
                .items(List.of(
                        EcommerceFulfillmentRequest.OrderItem.builder()
                                .sku("SKU-1").productName("Product").quantity(1).build()
                ))
                .build();

        mockMvc.perform(post("/api/v1/ecommerce/warehouse/fulfill")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFulfillmentStatus_returns200() throws Exception {
        FulfillmentStatusResponse response = FulfillmentStatusResponse.builder()
                .fulfillmentId("ful-001")
                .orderId("GO-2026-04723")
                .status("PICKING")
                .currentStage("PICKING")
                .assignedStaff(FulfillmentStatusResponse.AssignedStaffInfo.builder()
                        .name("Emmanuel Adebayo")
                        .role("Picker/Packer")
                        .startedAt(LocalDateTime.now())
                        .build())
                .progress(FulfillmentStatusResponse.ProgressInfo.builder()
                        .itemsPicked(1).totalItems(3).percentageComplete(33).build())
                .stages(List.of(
                        FulfillmentStatusResponse.StageDetail.builder()
                                .stage("RECEIVED").status("COMPLETED").build(),
                        FulfillmentStatusResponse.StageDetail.builder()
                                .stage("PICKING").status("IN_PROGRESS").build()
                ))
                .build();

        when(fulfillmentService.getFulfillmentStatus("ful-001")).thenReturn(response);

        mockMvc.perform(get("/api/v1/ecommerce/warehouse/fulfillment/ful-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fulfillmentId").value("ful-001"))
                .andExpect(jsonPath("$.status").value("PICKING"))
                .andExpect(jsonPath("$.assignedStaff.name").value("Emmanuel Adebayo"))
                .andExpect(jsonPath("$.progress.percentageComplete").value(33));
    }

    @Test
    void startPicking_returns200() throws Exception {
        FulfillmentStatusResponse response = FulfillmentStatusResponse.builder()
                .fulfillmentId("ful-001")
                .status("PICKING")
                .build();
        when(fulfillmentService.startPicking(eq("ful-001"), eq("staff-1"), eq("John"))).thenReturn(response);

        mockMvc.perform(post("/api/v1/ecommerce/warehouse/fulfillment/ful-001/start-picking")
                        .param("staffId", "staff-1")
                        .param("staffName", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PICKING"));
    }

    @Test
    void completePicking_returns200() throws Exception {
        FulfillmentStatusResponse response = FulfillmentStatusResponse.builder()
                .fulfillmentId("ful-001")
                .status("PACKING")
                .build();
        when(fulfillmentService.completePicking("ful-001")).thenReturn(response);

        mockMvc.perform(post("/api/v1/ecommerce/warehouse/fulfillment/ful-001/complete-picking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PACKING"));
    }

    @Test
    void completePacking_returns200() throws Exception {
        FulfillmentStatusResponse response = FulfillmentStatusResponse.builder()
                .fulfillmentId("ful-001")
                .status("READY_FOR_PICKUP")
                .build();
        when(fulfillmentService.completePacking("ful-001")).thenReturn(response);

        mockMvc.perform(post("/api/v1/ecommerce/warehouse/fulfillment/ful-001/complete-packing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("READY_FOR_PICKUP"));
    }
}

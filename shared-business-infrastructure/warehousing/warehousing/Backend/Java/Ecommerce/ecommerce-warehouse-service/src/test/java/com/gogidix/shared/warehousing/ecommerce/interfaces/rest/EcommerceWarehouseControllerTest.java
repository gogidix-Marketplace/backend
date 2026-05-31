package com.gogidix.shared.warehousing.ecommerce.interfaces.rest;

import com.gogidix.shared.warehousing.ecommerce.application.service.EcommerceStockService;
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
class EcommerceWarehouseControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private EcommerceStockService stockService;

    @InjectMocks
    private EcommerceWarehouseController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void registerStock_returns201() throws Exception {
        StockRegistrationRequest request = StockRegistrationRequest.builder()
                .vendorId("vendor-001")
                .warehouseId("wh-lagos-01")
                .zoneId("zone-lagos")
                .items(List.of(
                        StockRegistrationRequest.StockItem.builder()
                                .sku("TECH-001-BT")
                                .productName("Wireless Bluetooth Headphones")
                                .quantity(100)
                                .build()
                ))
                .build();

        StockRegistrationResponse response = StockRegistrationResponse.builder()
                .registrationId("reg-001")
                .vendorId("vendor-001")
                .warehouseId("wh-lagos-01")
                .zoneId("zone-lagos")
                .items(List.of(
                        StockRegistrationResponse.RegisteredItem.builder()
                                .sku("TECH-001-BT")
                                .allocatedLocation("ZONE-A-AISLE-03-SHELF-02-BIN-15")
                                .quantity(100)
                                .status("REGISTERED")
                                .build()
                ))
                .estimatedReceivingDate(LocalDateTime.now().plusDays(1))
                .build();

        when(stockService.registerStock(any(StockRegistrationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/ecommerce/warehouse/stock/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.registrationId").value("reg-001"))
                .andExpect(jsonPath("$.vendorId").value("vendor-001"))
                .andExpect(jsonPath("$.items[0].sku").value("TECH-001-BT"));
    }

    @Test
    void registerStock_missingVendorId_returns400() throws Exception {
        StockRegistrationRequest request = StockRegistrationRequest.builder()
                .warehouseId("wh-lagos-01")
                .zoneId("zone-lagos")
                .items(List.of(
                        StockRegistrationRequest.StockItem.builder()
                                .sku("SKU-1").productName("Product").quantity(10).build()
                ))
                .build();

        mockMvc.perform(post("/api/v1/ecommerce/warehouse/stock/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAvailability_returns200() throws Exception {
        WarehouseAvailabilityResponse response = WarehouseAvailabilityResponse.builder()
                .sku("TECH-001-BT")
                .totalAvailable(85)
                .warehouses(List.of(
                        WarehouseAvailabilityResponse.WarehouseStockInfo.builder()
                                .warehouseId("wh-lagos-01")
                                .warehouseName("Lagos Fulfillment Center")
                                .zoneId("zone-lagos")
                                .available(50)
                                .reserved(15)
                                .incoming(30)
                                .estimatedPickTime("2 hours")
                                .build()
                ))
                .build();

        when(stockService.getWarehouseAvailability("zone-lagos", "TECH-001-BT")).thenReturn(response);

        mockMvc.perform(get("/api/v1/ecommerce/warehouse/availability")
                        .param("zoneId", "zone-lagos")
                        .param("sku", "TECH-001-BT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("TECH-001-BT"))
                .andExpect(jsonPath("$.totalAvailable").value(85))
                .andExpect(jsonPath("$.warehouses[0].warehouseId").value("wh-lagos-01"));
    }

    @Test
    void getVendorStock_returns200() throws Exception {
        VendorStockOverviewResponse response = VendorStockOverviewResponse.builder()
                .vendorId("vendor-001")
                .sellingRadius("NATIONWIDE")
                .totalSKUs(45)
                .warehouses(List.of(
                        VendorStockOverviewResponse.WarehouseStockSummary.builder()
                                .warehouseId("wh-lagos-01")
                                .zoneId("zone-lagos")
                                .warehouseName("Lagos Fulfillment Center")
                                .skuCount(40)
                                .totalUnits(2500)
                                .build()
                ))
                .build();

        when(stockService.getVendorStockOverview("vendor-001")).thenReturn(response);

        mockMvc.perform(get("/api/v1/ecommerce/warehouse/vendor/vendor-001/stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendorId").value("vendor-001"))
                .andExpect(jsonPath("$.totalSKUs").value(45));
    }
}

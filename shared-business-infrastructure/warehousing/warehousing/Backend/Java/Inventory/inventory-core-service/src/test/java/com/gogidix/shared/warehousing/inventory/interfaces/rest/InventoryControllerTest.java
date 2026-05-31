package com.gogidix.shared.warehousing.inventory.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.shared.warehousing.inventory.application.command.CreateInventoryCommand;
import com.gogidix.shared.warehousing.inventory.application.dto.InventoryDTO;
import com.gogidix.shared.warehousing.inventory.application.service.InventoryService;
import com.gogidix.shared.warehousing.inventory.domain.entity.Inventory;
import com.gogidix.shared.warehousing.inventory.infrastructure.security.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * REST Controller tests for Inventory
 * Tests HTTP endpoints, request/response handling, and status codes
 */
@WebMvcTest(
    controllers = InventoryController.class,
    excludeAutoConfiguration = {
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class,
        MongoRepositoriesAutoConfiguration.class,
        SecurityAutoConfiguration.class
    }
)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InventoryService inventoryService;

    private static final String TEST_TENANT_ID = "test-tenant-123";

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenantId(TEST_TENANT_ID);
    }

    @Test
    void createInventory_ValidRequest_ReturnsCreated() throws Exception {
        // Given
        CreateInventoryCommand command = CreateInventoryCommand.builder()
            .sku("SKU-001")
            .quantity(100)
            .locationId("LOC-001")
            .locationType(Inventory.LocationType.WAREHOUSE)
            .tenantType(Inventory.TenantType.ECOMMERCE_VENDOR)
            .build();

        InventoryDTO responseDTO = InventoryDTO.builder()
            .id("inv-001")
            .sku("SKU-001")
            .quantity(100)
            .locationId("LOC-001")
            .build();

        when(inventoryService.createInventory(any(CreateInventoryCommand.class)))
            .thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/inventory")
                .header("X-Tenant-ID", TEST_TENANT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("inv-001"))
                .andExpect(jsonPath("$.sku").value("SKU-001"))
                .andExpect(jsonPath("$.quantity").value(100))
                .andExpect(jsonPath("$.locationId").value("LOC-001"));
    }

    @Test
    void createInventory_MissingSKU_ReturnsBadRequest() throws Exception {
        // Given
        CreateInventoryCommand command = CreateInventoryCommand.builder()
            .quantity(100)
            .locationId("LOC-001")
            .build();

        // When & Then
        mockMvc.perform(post("/inventory")
                .header("X-Tenant-ID", TEST_TENANT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getInventory_ExistingId_ReturnsInventory() throws Exception {
        // Given
        String inventoryId = "inv-001";
        InventoryDTO responseDTO = InventoryDTO.builder()
            .id(inventoryId)
            .sku("SKU-001")
            .quantity(100)
            .locationId("LOC-001")
            .build();

        when(inventoryService.getInventory(inventoryId)).thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(get("/inventory/{id}", inventoryId)
                .header("X-Tenant-ID", TEST_TENANT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(inventoryId))
                .andExpect(jsonPath("$.sku").value("SKU-001"))
                .andExpect(jsonPath("$.quantity").value(100));
    }

    @Test
    void getInventory_NotFound_ThrowsException() throws Exception {
        // Given
        String inventoryId = "non-existent";
        when(inventoryService.getInventory(inventoryId))
            .thenThrow(new IllegalArgumentException("Inventory not found"));

        // When & Then
        mockMvc.perform(get("/inventory/{id}", inventoryId)
                .header("X-Tenant-ID", TEST_TENANT_ID))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllInventory_ReturnsListOfInventories() throws Exception {
        // Given
        List<InventoryDTO> inventories = Arrays.asList(
            InventoryDTO.builder().id("inv-001").sku("SKU-001").quantity(100).build(),
            InventoryDTO.builder().id("inv-002").sku("SKU-002").quantity(200).build()
        );

        when(inventoryService.getAllInventory()).thenReturn(inventories);

        // When & Then
        mockMvc.perform(get("/inventory")
                .header("X-Tenant-ID", TEST_TENANT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("inv-001"))
                .andExpect(jsonPath("$[0].sku").value("SKU-001"))
                .andExpect(jsonPath("$[1].id").value("inv-002"))
                .andExpect(jsonPath("$[1].sku").value("SKU-002"));
    }

    @Test
    void getAllInventory_EmptyList_ReturnsEmptyArray() throws Exception {
        // Given
        when(inventoryService.getAllInventory()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/inventory")
                .header("X-Tenant-ID", TEST_TENANT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getInventoryBySku_ValidSku_ReturnsMatchingInventories() throws Exception {
        // Given
        String sku = "SKU-001";
        List<InventoryDTO> inventories = Arrays.asList(
            InventoryDTO.builder().id("inv-001").sku(sku).quantity(100).build(),
            InventoryDTO.builder().id("inv-002").sku(sku).quantity(50).build()
        );

        when(inventoryService.getInventoryBySku(sku)).thenReturn(inventories);

        // When & Then
        mockMvc.perform(get("/inventory/sku/{sku}", sku)
                .header("X-Tenant-ID", TEST_TENANT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value(sku))
                .andExpect(jsonPath("$[1].sku").value(sku));
    }

    @Test
    void getAvailableInventory_ReturnsOnlyInStockItems() throws Exception {
        // Given
        List<InventoryDTO> inventories = Arrays.asList(
            InventoryDTO.builder().id("inv-001").sku("SKU-001").quantity(100).build(),
            InventoryDTO.builder().id("inv-002").sku("SKU-002").quantity(50).build()
        );

        when(inventoryService.getAvailableInventory()).thenReturn(inventories);

        // When & Then
        mockMvc.perform(get("/inventory/available")
                .header("X-Tenant-ID", TEST_TENANT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quantity").value(100))
                .andExpect(jsonPath("$[1].quantity").value(50));
    }

    @Test
    void adjustQuantity_ValidAdjustment_ReturnsUpdatedInventory() throws Exception {
        // Given
        String inventoryId = "inv-001";
        int adjustment = 50;
        InventoryDTO responseDTO = InventoryDTO.builder()
            .id(inventoryId)
            .sku("SKU-001")
            .quantity(150)  // 100 + 50
            .build();

        when(inventoryService.adjustQuantity(eq(inventoryId), eq(adjustment)))
            .thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(patch("/inventory/{id}/quantity", inventoryId)
                .header("X-Tenant-ID", TEST_TENANT_ID)
                .param("adjustment", String.valueOf(adjustment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(inventoryId))
                .andExpect(jsonPath("$.quantity").value(150));
    }

    @Test
    void deleteInventory_ExistingInventory_ReturnsNoContent() throws Exception {
        // Given
        String inventoryId = "inv-001";

        // When & Then
        mockMvc.perform(delete("/inventory/{id}", inventoryId)
                .header("X-Tenant-ID", TEST_TENANT_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void checkAvailability_SufficientStock_ReturnsTrue() throws Exception {
        // Given
        String sku = "SKU-001";
        int quantity = 50;

        when(inventoryService.checkAvailability(sku, quantity)).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/inventory/check")
                .header("X-Tenant-ID", TEST_TENANT_ID)
                .param("sku", sku)
                .param("quantity", String.valueOf(quantity)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void checkAvailability_InsufficientStock_ReturnsFalse() throws Exception {
        // Given
        String sku = "SKU-001";
        int quantity = 500;

        when(inventoryService.checkAvailability(sku, quantity)).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/inventory/check")
                .header("X-Tenant-ID", TEST_TENANT_ID)
                .param("sku", sku)
                .param("quantity", String.valueOf(quantity)))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}

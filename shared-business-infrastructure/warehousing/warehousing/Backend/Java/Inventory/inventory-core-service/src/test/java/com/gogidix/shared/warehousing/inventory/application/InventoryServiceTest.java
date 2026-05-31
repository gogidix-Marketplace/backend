package com.gogidix.shared.warehousing.inventory.application;

import com.gogidix.shared.warehousing.inventory.application.command.CreateInventoryCommand;
import com.gogidix.shared.warehousing.inventory.application.command.UpdateInventoryCommand;
import com.gogidix.shared.warehousing.inventory.application.dto.InventoryDTO;
import com.gogidix.shared.warehousing.inventory.application.mapper.InventoryMapper;
import com.gogidix.shared.warehousing.inventory.application.service.InventoryService;
import com.gogidix.shared.warehousing.inventory.domain.entity.Inventory;
import com.gogidix.shared.warehousing.inventory.domain.repository.InventoryRepository;
import com.gogidix.shared.warehousing.inventory.infrastructure.messaging.InventoryEventPublisher;
import com.gogidix.shared.warehousing.inventory.infrastructure.security.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private InventoryMapper inventoryMapper;

    @Mock
    private InventoryEventPublisher eventPublisher;

    @InjectMocks
    private InventoryService inventoryService;

    private static final String TEST_TENANT_ID = "test-tenant-123";

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenantId(TEST_TENANT_ID);
    }

    @Test
    void createInventory_Success() {
        // Given
        CreateInventoryCommand command = CreateInventoryCommand.builder()
            .sku("SKU-001")
            .quantity(100)
            .locationId("LOC-001")
            .locationType(Inventory.LocationType.WAREHOUSE)
            .tenantType(Inventory.TenantType.ECOMMERCE_VENDOR)
            .build();

        Inventory inventory = Inventory.builder()
            .id(UUID.randomUUID().toString())
            .sku("SKU-001")
            .quantity(100)
            .tenantId(TEST_TENANT_ID)
            .build();

        InventoryDTO expectedDTO = InventoryDTO.builder()
            .id(inventory.getId())
            .sku("SKU-001")
            .quantity(100)
            .build();

        when(inventoryMapper.toEntity(command)).thenReturn(inventory);
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);
        when(inventoryMapper.toDTO(inventory)).thenReturn(expectedDTO);

        // When
        InventoryDTO result = inventoryService.createInventory(command);

        // Then
        assertNotNull(result);
        assertEquals("SKU-001", result.getSku());
        assertEquals(100, result.getQuantity());
        verify(eventPublisher).publishInventoryCreated(any());
    }

    @Test
    void getInventory_Success() {
        // Given
        String inventoryId = UUID.randomUUID().toString();
        Inventory inventory = Inventory.builder()
            .id(inventoryId)
            .sku("SKU-001")
            .quantity(100)
            .tenantId(TEST_TENANT_ID)
            .build();

        InventoryDTO expectedDTO = InventoryDTO.builder()
            .id(inventoryId)
            .sku("SKU-001")
            .quantity(100)
            .build();

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(inventory));
        when(inventoryMapper.toDTO(inventory)).thenReturn(expectedDTO);

        // When
        InventoryDTO result = inventoryService.getInventory(inventoryId);

        // Then
        assertNotNull(result);
        assertEquals(inventoryId, result.getId());
    }

    @Test
    void getInventory_NotFound() {
        // Given
        String inventoryId = UUID.randomUUID().toString();
        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> inventoryService.getInventory(inventoryId));
    }

    @Test
    void getAllInventory_Success() {
        // Given
        Inventory inventory1 = Inventory.builder().id("1").sku("SKU-001").tenantId(TEST_TENANT_ID).build();
        Inventory inventory2 = Inventory.builder().id("2").sku("SKU-002").tenantId(TEST_TENANT_ID).build();
        List<Inventory> inventories = Arrays.asList(inventory1, inventory2);

        when(inventoryRepository.findByTenantId(TEST_TENANT_ID)).thenReturn(inventories);
        when(inventoryMapper.toDTOList(inventories)).thenReturn(Arrays.asList(
            InventoryDTO.builder().id("1").sku("SKU-001").build(),
            InventoryDTO.builder().id("2").sku("SKU-002").build()
        ));

        // When
        List<InventoryDTO> result = inventoryService.getAllInventory();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void adjustQuantity_Success() {
        // Given
        String inventoryId = UUID.randomUUID().toString();
        Inventory inventory = Inventory.builder()
            .id(inventoryId)
            .sku("SKU-001")
            .quantity(100)
            .tenantId(TEST_TENANT_ID)
            .build();

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);
        when(inventoryMapper.toDTO(any(Inventory.class))).thenReturn(
            InventoryDTO.builder().id(inventoryId).sku("SKU-001").quantity(150).build()
        );

        // When
        InventoryDTO result = inventoryService.adjustQuantity(inventoryId, 50);

        // Then
        assertNotNull(result);
        assertEquals(150, result.getQuantity());
        verify(eventPublisher).publishInventoryUpdated(any());
    }

    @Test
    void adjustQuantity_NegativeResult_ThrowsException() {
        // Given
        String inventoryId = UUID.randomUUID().toString();
        Inventory inventory = Inventory.builder()
            .id(inventoryId)
            .sku("SKU-001")
            .quantity(10)
            .tenantId(TEST_TENANT_ID)
            .build();

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(inventory));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> inventoryService.adjustQuantity(inventoryId, -20));
    }

    @Test
    void deleteInventory_Success() {
        // Given
        String inventoryId = UUID.randomUUID().toString();
        when(inventoryRepository.existsById(inventoryId)).thenReturn(true);

        // When
        inventoryService.deleteInventory(inventoryId);

        // Then
        verify(inventoryRepository).deleteById(inventoryId);
    }

    @Test
    void checkAvailability_SufficientQuantity_ReturnsTrue() {
        // Given
        String sku = "SKU-001";
        Inventory inventory = Inventory.builder()
            .sku(sku)
            .quantity(100)
            .tenantId(TEST_TENANT_ID)
            .build();

        when(inventoryRepository.findByTenantIdAndSku(TEST_TENANT_ID, sku)).thenReturn(Arrays.asList(inventory));

        // When
        boolean result = inventoryService.checkAvailability(sku, 50);

        // Then
        assertTrue(result);
    }

    @Test
    void checkAvailability_InsufficientQuantity_ReturnsFalse() {
        // Given
        String sku = "SKU-001";
        Inventory inventory = Inventory.builder()
            .sku(sku)
            .quantity(10)
            .tenantId(TEST_TENANT_ID)
            .build();

        when(inventoryRepository.findByTenantIdAndSku(TEST_TENANT_ID, sku)).thenReturn(Arrays.asList(inventory));

        // When
        boolean result = inventoryService.checkAvailability(sku, 50);

        // Then
        assertFalse(result);
    }
}


package com.gogidix.shared.warehousing.stock.application.service;

import com.gogidix.shared.warehousing.stock.application.command.CreateStockCommand;
import com.gogidix.shared.warehousing.stock.application.dto.StockLevelDTO;
import com.gogidix.shared.warehousing.stock.application.mapper.StockMapper;
import com.gogidix.shared.warehousing.stock.domain.entity.StockLevel;
import com.gogidix.shared.warehousing.stock.domain.repository.StockLevelRepository;
import com.gogidix.shared.warehousing.stock.domain.repository.StockMovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @Mock
    private StockLevelRepository stockLevelRepository;

    @Mock
    private StockMovementRepository stockMovementRepository;

    @Mock
    private StockMapper stockMapper;

    @InjectMocks
    private StockService stockService;

    private static final String TENANT_ID = "tenant-1";
    private static final String SKU = "SKU-001";
    private static final String LOCATION_ID = "LOC-001";

    @BeforeEach
    void setUp() {
    }

    @Test
    void createStock_Success() {
        // Given
        CreateStockCommand command = CreateStockCommand.builder()
            .sku(SKU)
            .locationId(LOCATION_ID)
            .quantity(100)
            .reorderPoint(20)
            .reorderQuantity(50)
            .build();

        StockLevel stockLevel = StockLevel.builder()
            .id("stock-1")
            .sku(SKU)
            .locationId(LOCATION_ID)
            .availableQuantity(100)
            .reservedQuantity(0)
            .allocatedQuantity(0)
            .build();

        StockLevelDTO stockLevelDTO = StockLevelDTO.builder()
            .id("stock-1")
            .sku(SKU)
            .locationId(LOCATION_ID)
            .availableQuantity(100)
            .build();

        when(stockLevelRepository.findBySkuAndLocationId(SKU, LOCATION_ID))
            .thenReturn(Optional.empty());
        when(stockMapper.toEntity(command)).thenReturn(stockLevel);
        when(stockLevelRepository.save(any(StockLevel.class))).thenReturn(stockLevel);
        when(stockMapper.toDTO(stockLevel)).thenReturn(stockLevelDTO);

        // When
        StockLevelDTO result = stockService.createStock(command, TENANT_ID);

        // Then
        assertNotNull(result);
        assertEquals(SKU, result.getSku());
        assertEquals(LOCATION_ID, result.getLocationId());
        verify(stockLevelRepository).save(any(StockLevel.class));
        verify(stockMovementRepository).save(any());
    }

    @Test
    void createStock_AlreadyExists_ThrowsException() {
        // Given
        CreateStockCommand command = CreateStockCommand.builder()
            .sku(SKU)
            .locationId(LOCATION_ID)
            .quantity(100)
            .build();

        when(stockLevelRepository.findBySkuAndLocationId(SKU, LOCATION_ID))
            .thenReturn(Optional.of(new StockLevel()));

        // When & Then
        assertThrows(IllegalStateException.class, () -> {
            stockService.createStock(command, TENANT_ID);
        });

        verify(stockLevelRepository, never()).save(any());
    }

    @Test
    void getStock_Success() {
        // Given
        StockLevel stockLevel = StockLevel.builder()
            .id("stock-1")
            .sku(SKU)
            .locationId(LOCATION_ID)
            .availableQuantity(100)
            .build();

        StockLevelDTO stockLevelDTO = StockLevelDTO.builder()
            .id("stock-1")
            .sku(SKU)
            .locationId(LOCATION_ID)
            .availableQuantity(100)
            .build();

        when(stockLevelRepository.findBySkuAndLocationId(SKU, LOCATION_ID))
            .thenReturn(Optional.of(stockLevel));
        when(stockMapper.toDTO(stockLevel)).thenReturn(stockLevelDTO);

        // When
        Optional<StockLevelDTO> result = stockService.getStock(SKU, LOCATION_ID);

        // Then
        assertTrue(result.isPresent());
        assertEquals(SKU, result.get().getSku());
    }

    @Test
    void getStock_NotFound() {
        // Given
        when(stockLevelRepository.findBySkuAndLocationId(SKU, LOCATION_ID))
            .thenReturn(Optional.empty());

        // When
        Optional<StockLevelDTO> result = stockService.getStock(SKU, LOCATION_ID);

        // Then
        assertTrue(result.isEmpty());
    }
}
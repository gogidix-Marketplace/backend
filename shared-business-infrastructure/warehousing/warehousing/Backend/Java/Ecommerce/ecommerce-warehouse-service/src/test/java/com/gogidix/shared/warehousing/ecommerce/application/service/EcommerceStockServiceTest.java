package com.gogidix.shared.warehousing.ecommerce.application.service;

import com.gogidix.shared.warehousing.ecommerce.domain.entity.VendorStockRegistration;
import com.gogidix.shared.warehousing.ecommerce.domain.entity.WarehouseStockLevel;
import com.gogidix.shared.warehousing.ecommerce.domain.repository.VendorStockRegistrationRepository;
import com.gogidix.shared.warehousing.ecommerce.domain.repository.WarehouseStockLevelRepository;
import com.gogidix.shared.warehousing.ecommerce.infrastructure.messaging.producers.EcommerceWarehouseEventProducer;
import com.gogidix.shared.warehousing.ecommerce.interfaces.rest.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EcommerceStockServiceTest {

    @Mock
    private VendorStockRegistrationRepository registrationRepository;

    @Mock
    private WarehouseStockLevelRepository stockLevelRepository;

    @Mock
    private EcommerceWarehouseEventProducer eventProducer;

    @InjectMocks
    private EcommerceStockService stockService;

    private StockRegistrationRequest registrationRequest;

    @BeforeEach
    void setUp() {
        registrationRequest = StockRegistrationRequest.builder()
                .vendorId("vendor-001")
                .warehouseId("wh-lagos-01")
                .zoneId("zone-lagos")
                .sellingRadius("NATIONWIDE")
                .items(List.of(
                        StockRegistrationRequest.StockItem.builder()
                                .sku("TECH-001-BT")
                                .productName("Wireless Bluetooth Headphones")
                                .quantity(100)
                                .unitOfMeasure("EA")
                                .category("ELECTRONICS")
                                .weight(0.5)
                                .weightUnit("KG")
                                .dimensions(StockRegistrationRequest.Dimensions.builder()
                                        .length(20.0).width(15.0).height(8.0).build())
                                .attributes(Map.of("brand", "TechZone"))
                                .build()
                ))
                .build();
    }

    @Test
    void registerStock_shouldCreateRegistrationAndStockLevels() {
        when(registrationRepository.save(any(VendorStockRegistration.class)))
                .thenAnswer(invocation -> {
                    VendorStockRegistration reg = invocation.getArgument(0);
                    reg.setId("reg-uuid-001");
                    return reg;
                });
        when(stockLevelRepository.findBySkuAndWarehouseId(anyString(), anyString()))
                .thenReturn(Optional.empty());
        when(stockLevelRepository.save(any(WarehouseStockLevel.class)))
                .thenAnswer(invocation -> {
                    WarehouseStockLevel level = invocation.getArgument(0);
                    level.setId("stock-uuid-001");
                    return level;
                });

        StockRegistrationResponse response = stockService.registerStock(registrationRequest);

        assertNotNull(response);
        assertEquals("reg-uuid-001", response.getRegistrationId());
        assertEquals("vendor-001", response.getVendorId());
        assertEquals("wh-lagos-01", response.getWarehouseId());
        assertEquals(1, response.getItems().size());
        assertEquals("TECH-001-BT", response.getItems().get(0).getSku());
        assertEquals("REGISTERED", response.getItems().get(0).getStatus());
        assertNotNull(response.getItems().get(0).getAllocatedLocation());

        verify(registrationRepository).save(any(VendorStockRegistration.class));
        verify(stockLevelRepository).save(any(WarehouseStockLevel.class));
        verify(eventProducer).publishStockRegistered(eq("vendor-001"), eq("wh-lagos-01"),
                eq("zone-lagos"), anyList());
    }

    @Test
    void registerStock_shouldAddToExistingStockLevel() {
        WarehouseStockLevel existing = WarehouseStockLevel.builder()
                .id("stock-001")
                .sku("TECH-001-BT")
                .warehouseId("wh-lagos-01")
                .quantity(50)
                .reserved(10)
                .reorderThreshold(10)
                .build();

        when(stockLevelRepository.findBySkuAndWarehouseId("TECH-001-BT", "wh-lagos-01"))
                .thenReturn(Optional.of(existing));
        when(registrationRepository.save(any())).thenAnswer(invocation -> {
            VendorStockRegistration reg = invocation.getArgument(0);
            reg.setId("reg-002");
            return reg;
        });

        StockRegistrationResponse response = stockService.registerStock(registrationRequest);

        assertNotNull(response);
        verify(stockLevelRepository).save(argThat(level -> level.getQuantity() == 150));
    }

    @Test
    void getWarehouseAvailability_shouldReturnAggregatedStock() {
        WarehouseStockLevel level1 = WarehouseStockLevel.builder()
                .sku("TECH-001-BT").warehouseId("wh-lagos-01").zoneId("zone-lagos")
                .quantity(50).reserved(15).incoming(30).estimatedPickTime("2 hours").build();
        WarehouseStockLevel level2 = WarehouseStockLevel.builder()
                .sku("TECH-001-BT").warehouseId("wh-abuja-01").zoneId("zone-abuja")
                .quantity(35).reserved(5).incoming(0).estimatedPickTime("3 hours").build();

        when(stockLevelRepository.findBySkuAndZoneId("TECH-001-BT", "zone-lagos"))
                .thenReturn(List.of(level1));

        WarehouseAvailabilityResponse response = stockService.getWarehouseAvailability("zone-lagos", "TECH-001-BT");

        assertNotNull(response);
        assertEquals("TECH-001-BT", response.getSku());
        assertEquals(35, response.getTotalAvailable());
        assertEquals(1, response.getWarehouses().size());
        assertEquals("wh-lagos-01", response.getWarehouses().get(0).getWarehouseId());
        assertEquals(35, response.getWarehouses().get(0).getAvailable());
    }

    @Test
    void getWarehouseAvailability_withNoFilters_returnsAll() {
        WarehouseStockLevel level = WarehouseStockLevel.builder()
                .sku("SKU-1").warehouseId("wh-01").zoneId("zone-1")
                .quantity(100).reserved(20).build();
        when(stockLevelRepository.findAll()).thenReturn(List.of(level));

        WarehouseAvailabilityResponse response = stockService.getWarehouseAvailability(null, null);

        assertEquals(80, response.getTotalAvailable());
    }

    @Test
    void getVendorStockOverview_shouldAggregatePerWarehouse() {
        WarehouseStockLevel l1 = WarehouseStockLevel.builder()
                .sku("SKU-1").warehouseId("wh-lagos-01").zoneId("zone-lagos")
                .quantity(100).vendorId("vendor-001").sellingRadius("NATIONWIDE").build();
        WarehouseStockLevel l2 = WarehouseStockLevel.builder()
                .sku("SKU-2").warehouseId("wh-lagos-01").zoneId("zone-lagos")
                .quantity(50).vendorId("vendor-001").sellingRadius("NATIONWIDE").build();
        WarehouseStockLevel l3 = WarehouseStockLevel.builder()
                .sku("SKU-3").warehouseId("wh-abuja-01").zoneId("zone-abuja")
                .quantity(30).vendorId("vendor-001").sellingRadius("NATIONWIDE").build();

        when(stockLevelRepository.findByVendorId("vendor-001")).thenReturn(List.of(l1, l2, l3));

        VendorStockOverviewResponse response = stockService.getVendorStockOverview("vendor-001");

        assertNotNull(response);
        assertEquals("vendor-001", response.getVendorId());
        assertEquals("NATIONWIDE", response.getSellingRadius());
        assertEquals(3, response.getTotalSKUs());
        assertEquals(2, response.getWarehouses().size());
    }

    @Test
    void updateStockLevel_existingLevel_updatesQuantity() {
        WarehouseStockLevel existing = WarehouseStockLevel.builder()
                .id("sl-1").sku("SKU-1").warehouseId("wh-01")
                .quantity(100).reserved(0).reorderThreshold(10).build();
        when(stockLevelRepository.findBySkuAndWarehouseId("SKU-1", "wh-01"))
                .thenReturn(Optional.of(existing));

        stockService.updateStockLevel("vendor-1", "wh-01", "SKU-1", 200);

        verify(stockLevelRepository).save(argThat(level -> level.getQuantity() == 200));
    }

    @Test
    void updateStockLevel_notFound_doesNothing() {
        when(stockLevelRepository.findBySkuAndWarehouseId("SKU-X", "wh-01"))
                .thenReturn(Optional.empty());

        stockService.updateStockLevel("vendor-1", "wh-01", "SKU-X", 50);

        verify(stockLevelRepository, never()).save(any());
    }

    @Test
    void reserveStock_sufficientStock_reserves() {
        WarehouseStockLevel level = WarehouseStockLevel.builder()
                .quantity(100).reserved(10).reorderThreshold(10).build();
        when(stockLevelRepository.findBySkuAndWarehouseId("SKU-1", "wh-01"))
                .thenReturn(Optional.of(level));

        boolean result = stockService.reserveStock("wh-01", "SKU-1", 20);

        assertTrue(result);
        verify(stockLevelRepository).save(argThat(l -> l.getReserved() == 30));
    }

    @Test
    void reserveStock_insufficientStock_fails() {
        WarehouseStockLevel level = WarehouseStockLevel.builder()
                .quantity(10).reserved(10).reorderThreshold(10).build();
        when(stockLevelRepository.findBySkuAndWarehouseId("SKU-1", "wh-01"))
                .thenReturn(Optional.of(level));

        boolean result = stockService.reserveStock("wh-01", "SKU-1", 5);

        assertFalse(result);
        verify(stockLevelRepository, never()).save(any());
    }

    @Test
    void reserveStock_notFound_fails() {
        when(stockLevelRepository.findBySkuAndWarehouseId("SKU-1", "wh-01"))
                .thenReturn(Optional.empty());

        boolean result = stockService.reserveStock("wh-01", "SKU-1", 5);

        assertFalse(result);
    }

    @Test
    void releaseStock_reducesReserved() {
        WarehouseStockLevel level = WarehouseStockLevel.builder()
                .quantity(100).reserved(20).reorderThreshold(10).build();
        when(stockLevelRepository.findBySkuAndWarehouseId("SKU-1", "wh-01"))
                .thenReturn(Optional.of(level));

        stockService.releaseStock("wh-01", "SKU-1", 15);

        verify(stockLevelRepository).save(argThat(l -> l.getReserved() == 5));
    }

    @Test
    void releaseStock_cannotGoBelowZero() {
        WarehouseStockLevel level = WarehouseStockLevel.builder()
                .quantity(100).reserved(5).reorderThreshold(10).build();
        when(stockLevelRepository.findBySkuAndWarehouseId("SKU-1", "wh-01"))
                .thenReturn(Optional.of(level));

        stockService.releaseStock("wh-01", "SKU-1", 20);

        verify(stockLevelRepository).save(argThat(l -> l.getReserved() == 0));
    }

    @Test
    void stockLevelDepleted_publishesDepletedEvent() {
        WarehouseStockLevel level = WarehouseStockLevel.builder()
                .id("sl-1").sku("SKU-1").warehouseId("wh-01").vendorId("v-1")
                .quantity(5).reserved(5).reorderThreshold(10).build();
        when(stockLevelRepository.findBySkuAndWarehouseId("SKU-1", "wh-01"))
                .thenReturn(Optional.of(level));

        stockService.updateStockLevel("v-1", "wh-01", "SKU-1", 5);

        verify(eventProducer).publishStockDepleted("v-1", "wh-01", "SKU-1");
    }

    @Test
    void stockLevelLow_publishesLowEvent() {
        WarehouseStockLevel level = WarehouseStockLevel.builder()
                .id("sl-1").sku("SKU-1").warehouseId("wh-01").vendorId("v-1")
                .quantity(15).reserved(5).reorderThreshold(10).build();
        when(stockLevelRepository.findBySkuAndWarehouseId("SKU-1", "wh-01"))
                .thenReturn(Optional.of(level));

        stockService.updateStockLevel("v-1", "wh-01", "SKU-1", 15);

        verify(eventProducer).publishStockLow(eq("v-1"), eq("wh-01"), eq("SKU-1"), eq(10), eq(10));
    }
}

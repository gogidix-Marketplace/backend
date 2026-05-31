package com.gogidix.sales.revenue.application.dto;

import com.gogidix.sales.revenue.application.dto.RevenueResponseDto;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RevenueResponseDtoTest {

        @Test
    void testBuilder() {
        RevenueResponseDto dto = RevenueResponseDto.builder()
                        .id("test-id")
            .revenueId("test-revenueId")
            .tenantId("test-tenantId")
            .contractId("test-contractId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .productId("test-productId")
            .productName("test-productName")
            .territory("test-territory")
            .region("test-region")
            .revenueType("test-revenueType")
            .totalAmount(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-revenueId", dto.getRevenueId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-contractId", dto.getContractId());
        assertEquals("test-customerId", dto.getCustomerId());
        assertEquals("test-customerName", dto.getCustomerName());
        assertEquals("test-productId", dto.getProductId());
        assertEquals("test-productName", dto.getProductName());
        assertEquals("test-territory", dto.getTerritory());
        assertEquals("test-region", dto.getRegion());
        assertEquals("test-revenueType", dto.getRevenueType());
        assertEquals(BigDecimal.TEN, dto.getTotalAmount());
    }

    @Test
    void testSettersAndGetters() {
        RevenueResponseDto dto = new RevenueResponseDto();
        dto.setId("val-id");
        dto.setRevenueId("val-revenueId");
        dto.setTenantId("val-tenantId");
        dto.setContractId("val-contractId");
        dto.setCustomerId("val-customerId");
        dto.setCustomerName("val-customerName");
        dto.setProductId("val-productId");
        dto.setProductName("val-productName");
        dto.setTerritory("val-territory");
        dto.setRegion("val-region");
        dto.setRevenueType("val-revenueType");
        dto.setTotalAmount(BigDecimal.ONE);
        assertEquals("val-id", dto.getId());
        assertEquals("val-revenueId", dto.getRevenueId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-contractId", dto.getContractId());
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals("val-productId", dto.getProductId());
        assertEquals("val-productName", dto.getProductName());
        assertEquals("val-territory", dto.getTerritory());
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-revenueType", dto.getRevenueType());
        assertEquals(BigDecimal.ONE, dto.getTotalAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueResponseDto dto1 = RevenueResponseDto.builder()
                        .id("test-id")
            .revenueId("test-revenueId")
            .tenantId("test-tenantId")
            .contractId("test-contractId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .productId("test-productId")
            .productName("test-productName")
            .territory("test-territory")
            .region("test-region")
            .revenueType("test-revenueType")
            .totalAmount(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        RevenueResponseDto dto2 = RevenueResponseDto.builder()
                        .id("test-id")
            .revenueId("test-revenueId")
            .tenantId("test-tenantId")
            .contractId("test-contractId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .productId("test-productId")
            .productName("test-productName")
            .territory("test-territory")
            .region("test-region")
            .revenueType("test-revenueType")
            .totalAmount(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RevenueResponseDto dto = RevenueResponseDto.builder()
                        .id("test-id")
            .revenueId("test-revenueId")
            .tenantId("test-tenantId")
            .contractId("test-contractId")
            .customerId("test-customerId")
            .customerName("test-customerName")
            .productId("test-productId")
            .productName("test-productName")
            .territory("test-territory")
            .region("test-region")
            .revenueType("test-revenueType")
            .totalAmount(BigDecimal.TEN)
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
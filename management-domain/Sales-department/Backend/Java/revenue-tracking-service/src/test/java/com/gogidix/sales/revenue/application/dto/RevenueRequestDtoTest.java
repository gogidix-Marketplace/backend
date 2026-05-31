package com.gogidix.sales.revenue.application.dto;

import com.gogidix.sales.revenue.application.dto.RevenueRequestDto;
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
class RevenueRequestDtoTest {

        @Test
    void testBuilder() {
        RevenueRequestDto dto = RevenueRequestDto.builder()
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
            .build();
        assertNotNull(dto);
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
        RevenueRequestDto dto = new RevenueRequestDto();
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
        RevenueRequestDto dto1 = RevenueRequestDto.builder()
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
            .build();
        RevenueRequestDto dto2 = RevenueRequestDto.builder()
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
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RevenueRequestDto dto = RevenueRequestDto.builder()
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
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
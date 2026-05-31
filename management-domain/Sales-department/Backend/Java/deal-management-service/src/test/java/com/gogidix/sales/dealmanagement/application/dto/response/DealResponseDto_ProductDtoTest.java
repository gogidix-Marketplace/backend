package com.gogidix.sales.dealmanagement.application.dto.response;

import com.gogidix.sales.dealmanagement.application.dto.response.DealResponseDto;
import com.gogidix.sales.dealmanagement.domain.model.DealProduct;
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
class DealResponseDto_ProductDtoTest {

        @Test
    void testBuilder() {
        DealResponseDto.ProductDto dto = DealResponseDto.ProductDto.builder()
                        .productId("test-productId")
            .productName("test-productName")
            .productCode("test-productCode")
            .productCategory("test-productCategory")
            .quantity(42)
            .unitPrice(BigDecimal.TEN)
            .totalPrice(BigDecimal.TEN)
            .currency("test-currency")
            .isRecurring(true)
            .billingCycle(DealProduct.BillingCycle.MONTHLY)
            .build();
        assertNotNull(dto);
        assertEquals("test-productId", dto.getProductId());
        assertEquals("test-productName", dto.getProductName());
        assertEquals("test-productCode", dto.getProductCode());
        assertEquals("test-productCategory", dto.getProductCategory());
        assertEquals(42, dto.getQuantity());
        assertEquals(BigDecimal.TEN, dto.getUnitPrice());
        assertEquals(BigDecimal.TEN, dto.getTotalPrice());
        assertEquals("test-currency", dto.getCurrency());
        assertTrue(dto.getIsRecurring());
    }

    @Test
    void testSettersAndGetters() {
        DealResponseDto.ProductDto dto = new DealResponseDto.ProductDto();
        dto.setProductId("val-productId");
        dto.setProductName("val-productName");
        dto.setProductCode("val-productCode");
        dto.setProductCategory("val-productCategory");
        dto.setQuantity(99);
        dto.setUnitPrice(BigDecimal.ONE);
        dto.setTotalPrice(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setIsRecurring(true);
        assertEquals("val-productId", dto.getProductId());
        assertEquals("val-productName", dto.getProductName());
        assertEquals("val-productCode", dto.getProductCode());
        assertEquals("val-productCategory", dto.getProductCategory());
        assertEquals(99, dto.getQuantity());
        assertEquals(BigDecimal.ONE, dto.getUnitPrice());
        assertEquals(BigDecimal.ONE, dto.getTotalPrice());
        assertEquals("val-currency", dto.getCurrency());
        assertTrue(dto.getIsRecurring());
    }

    @Test
    void testEqualsAndHashCode() {
        DealResponseDto.ProductDto dto1 = DealResponseDto.ProductDto.builder()
                        .productId("test-productId")
            .productName("test-productName")
            .productCode("test-productCode")
            .productCategory("test-productCategory")
            .quantity(42)
            .unitPrice(BigDecimal.TEN)
            .totalPrice(BigDecimal.TEN)
            .currency("test-currency")
            .isRecurring(true)
            .billingCycle(DealProduct.BillingCycle.MONTHLY)
            .build();
        DealResponseDto.ProductDto dto2 = DealResponseDto.ProductDto.builder()
                        .productId("test-productId")
            .productName("test-productName")
            .productCode("test-productCode")
            .productCategory("test-productCategory")
            .quantity(42)
            .unitPrice(BigDecimal.TEN)
            .totalPrice(BigDecimal.TEN)
            .currency("test-currency")
            .isRecurring(true)
            .billingCycle(DealProduct.BillingCycle.MONTHLY)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DealResponseDto.ProductDto dto = DealResponseDto.ProductDto.builder()
                        .productId("test-productId")
            .productName("test-productName")
            .productCode("test-productCode")
            .productCategory("test-productCategory")
            .quantity(42)
            .unitPrice(BigDecimal.TEN)
            .totalPrice(BigDecimal.TEN)
            .currency("test-currency")
            .isRecurring(true)
            .billingCycle(DealProduct.BillingCycle.MONTHLY)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
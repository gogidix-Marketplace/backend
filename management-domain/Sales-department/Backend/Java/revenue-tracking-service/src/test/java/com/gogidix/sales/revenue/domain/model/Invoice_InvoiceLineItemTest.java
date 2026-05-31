package com.gogidix.sales.revenue.domain.model;

import com.gogidix.sales.revenue.domain.model.Invoice;
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
class Invoice_InvoiceLineItemTest {

        @Test
    void testBuilder() {
        Invoice.InvoiceLineItem dto = Invoice.InvoiceLineItem.builder()
                        .lineItemId("test-lineItemId")
            .productId("test-productId")
            .productName("test-productName")
            .description("test-description")
            .quantity(42)
            .unitPrice(BigDecimal.TEN)
            .discount(BigDecimal.TEN)
            .taxRate(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .lineTotal(BigDecimal.TEN)
            .revenueId("test-revenueId")
            .contractId("test-contractId")
            .build();
        assertNotNull(dto);
        assertEquals("test-lineItemId", dto.getLineItemId());
        assertEquals("test-productId", dto.getProductId());
        assertEquals("test-productName", dto.getProductName());
        assertEquals("test-description", dto.getDescription());
        assertEquals(42, dto.getQuantity());
        assertEquals(BigDecimal.TEN, dto.getUnitPrice());
        assertEquals(BigDecimal.TEN, dto.getDiscount());
        assertEquals(BigDecimal.TEN, dto.getTaxRate());
        assertEquals(BigDecimal.TEN, dto.getTaxAmount());
        assertEquals(BigDecimal.TEN, dto.getLineTotal());
        assertEquals("test-revenueId", dto.getRevenueId());
        assertEquals("test-contractId", dto.getContractId());
    }

    @Test
    void testSettersAndGetters() {
        Invoice.InvoiceLineItem dto = new Invoice.InvoiceLineItem();
        dto.setLineItemId("val-lineItemId");
        dto.setProductId("val-productId");
        dto.setProductName("val-productName");
        dto.setDescription("val-description");
        dto.setQuantity(99);
        dto.setUnitPrice(BigDecimal.ONE);
        dto.setDiscount(BigDecimal.ONE);
        dto.setTaxRate(BigDecimal.ONE);
        dto.setTaxAmount(BigDecimal.ONE);
        dto.setLineTotal(BigDecimal.ONE);
        dto.setRevenueId("val-revenueId");
        dto.setContractId("val-contractId");
        assertEquals("val-lineItemId", dto.getLineItemId());
        assertEquals("val-productId", dto.getProductId());
        assertEquals("val-productName", dto.getProductName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(99, dto.getQuantity());
        assertEquals(BigDecimal.ONE, dto.getUnitPrice());
        assertEquals(BigDecimal.ONE, dto.getDiscount());
        assertEquals(BigDecimal.ONE, dto.getTaxRate());
        assertEquals(BigDecimal.ONE, dto.getTaxAmount());
        assertEquals(BigDecimal.ONE, dto.getLineTotal());
        assertEquals("val-revenueId", dto.getRevenueId());
        assertEquals("val-contractId", dto.getContractId());
    }

    @Test
    void testEqualsAndHashCode() {
        Invoice.InvoiceLineItem dto1 = Invoice.InvoiceLineItem.builder()
                        .lineItemId("test-lineItemId")
            .productId("test-productId")
            .productName("test-productName")
            .description("test-description")
            .quantity(42)
            .unitPrice(BigDecimal.TEN)
            .discount(BigDecimal.TEN)
            .taxRate(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .lineTotal(BigDecimal.TEN)
            .revenueId("test-revenueId")
            .contractId("test-contractId")
            .build();
        Invoice.InvoiceLineItem dto2 = Invoice.InvoiceLineItem.builder()
                        .lineItemId("test-lineItemId")
            .productId("test-productId")
            .productName("test-productName")
            .description("test-description")
            .quantity(42)
            .unitPrice(BigDecimal.TEN)
            .discount(BigDecimal.TEN)
            .taxRate(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .lineTotal(BigDecimal.TEN)
            .revenueId("test-revenueId")
            .contractId("test-contractId")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Invoice.InvoiceLineItem dto = Invoice.InvoiceLineItem.builder()
                        .lineItemId("test-lineItemId")
            .productId("test-productId")
            .productName("test-productName")
            .description("test-description")
            .quantity(42)
            .unitPrice(BigDecimal.TEN)
            .discount(BigDecimal.TEN)
            .taxRate(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .lineTotal(BigDecimal.TEN)
            .revenueId("test-revenueId")
            .contractId("test-contractId")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
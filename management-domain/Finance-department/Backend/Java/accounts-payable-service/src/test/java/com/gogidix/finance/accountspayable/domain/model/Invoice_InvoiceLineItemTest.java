package com.gogidix.finance.accountspayable.domain.model;

import com.gogidix.finance.accountspayable.domain.model.Invoice;
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
            .description("test-description")
            .quantity(BigDecimal.TEN)
            .unitPrice(BigDecimal.TEN)
            .amount(BigDecimal.TEN)
            .accountCode("test-accountCode")
            .taxCode("test-taxCode")
            .build();
        assertNotNull(dto);
        assertEquals("test-lineItemId", dto.getLineItemId());
        assertEquals("test-description", dto.getDescription());
        assertEquals(BigDecimal.TEN, dto.getQuantity());
        assertEquals(BigDecimal.TEN, dto.getUnitPrice());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-accountCode", dto.getAccountCode());
        assertEquals("test-taxCode", dto.getTaxCode());
    }

    @Test
    void testSettersAndGetters() {
        Invoice.InvoiceLineItem dto = new Invoice.InvoiceLineItem();
        dto.setLineItemId("val-lineItemId");
        dto.setDescription("val-description");
        dto.setQuantity(BigDecimal.ONE);
        dto.setUnitPrice(BigDecimal.ONE);
        dto.setAmount(BigDecimal.ONE);
        dto.setAccountCode("val-accountCode");
        dto.setTaxCode("val-taxCode");
        assertEquals("val-lineItemId", dto.getLineItemId());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getQuantity());
        assertEquals(BigDecimal.ONE, dto.getUnitPrice());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-accountCode", dto.getAccountCode());
        assertEquals("val-taxCode", dto.getTaxCode());
    }

    @Test
    void testEqualsAndHashCode() {
        Invoice.InvoiceLineItem dto1 = Invoice.InvoiceLineItem.builder()
                        .lineItemId("test-lineItemId")
            .description("test-description")
            .quantity(BigDecimal.TEN)
            .unitPrice(BigDecimal.TEN)
            .amount(BigDecimal.TEN)
            .accountCode("test-accountCode")
            .taxCode("test-taxCode")
            .build();
        Invoice.InvoiceLineItem dto2 = Invoice.InvoiceLineItem.builder()
                        .lineItemId("test-lineItemId")
            .description("test-description")
            .quantity(BigDecimal.TEN)
            .unitPrice(BigDecimal.TEN)
            .amount(BigDecimal.TEN)
            .accountCode("test-accountCode")
            .taxCode("test-taxCode")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Invoice.InvoiceLineItem dto = Invoice.InvoiceLineItem.builder()
                        .lineItemId("test-lineItemId")
            .description("test-description")
            .quantity(BigDecimal.TEN)
            .unitPrice(BigDecimal.TEN)
            .amount(BigDecimal.TEN)
            .accountCode("test-accountCode")
            .taxCode("test-taxCode")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
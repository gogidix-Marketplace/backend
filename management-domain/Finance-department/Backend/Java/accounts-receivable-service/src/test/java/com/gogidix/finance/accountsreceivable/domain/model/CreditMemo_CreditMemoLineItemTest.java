package com.gogidix.finance.accountsreceivable.domain.model;

import com.gogidix.finance.accountsreceivable.domain.model.CreditMemo;
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
class CreditMemo_CreditMemoLineItemTest {

        @Test
    void testBuilder() {
        CreditMemo.CreditMemoLineItem dto = CreditMemo.CreditMemoLineItem.builder()
                        .lineItemId("test-lineItemId")
            .itemId("test-itemId")
            .itemCode("test-itemCode")
            .description("test-description")
            .quantity(42)
            .unitPrice(BigDecimal.TEN)
            .discountAmount(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .lineTotal(BigDecimal.TEN)
            .accountCode("test-accountCode")
            .taxCode("test-taxCode")
            .itemType("test-itemType")
            .reason("test-reason")
            .build();
        assertNotNull(dto);
        assertEquals("test-lineItemId", dto.getLineItemId());
        assertEquals("test-itemId", dto.getItemId());
        assertEquals("test-itemCode", dto.getItemCode());
        assertEquals("test-description", dto.getDescription());
        assertEquals(42, dto.getQuantity());
        assertEquals(BigDecimal.TEN, dto.getUnitPrice());
        assertEquals(BigDecimal.TEN, dto.getDiscountAmount());
        assertEquals(BigDecimal.TEN, dto.getTaxAmount());
        assertEquals(BigDecimal.TEN, dto.getLineTotal());
        assertEquals("test-accountCode", dto.getAccountCode());
        assertEquals("test-taxCode", dto.getTaxCode());
        assertEquals("test-itemType", dto.getItemType());
        assertEquals("test-reason", dto.getReason());
    }

    @Test
    void testSettersAndGetters() {
        CreditMemo.CreditMemoLineItem dto = new CreditMemo.CreditMemoLineItem();
        dto.setLineItemId("val-lineItemId");
        dto.setItemId("val-itemId");
        dto.setItemCode("val-itemCode");
        dto.setDescription("val-description");
        dto.setQuantity(99);
        dto.setUnitPrice(BigDecimal.ONE);
        dto.setDiscountAmount(BigDecimal.ONE);
        dto.setTaxAmount(BigDecimal.ONE);
        dto.setLineTotal(BigDecimal.ONE);
        dto.setAccountCode("val-accountCode");
        dto.setTaxCode("val-taxCode");
        dto.setItemType("val-itemType");
        dto.setReason("val-reason");
        assertEquals("val-lineItemId", dto.getLineItemId());
        assertEquals("val-itemId", dto.getItemId());
        assertEquals("val-itemCode", dto.getItemCode());
        assertEquals("val-description", dto.getDescription());
        assertEquals(99, dto.getQuantity());
        assertEquals(BigDecimal.ONE, dto.getUnitPrice());
        assertEquals(BigDecimal.ONE, dto.getDiscountAmount());
        assertEquals(BigDecimal.ONE, dto.getTaxAmount());
        assertEquals(BigDecimal.ONE, dto.getLineTotal());
        assertEquals("val-accountCode", dto.getAccountCode());
        assertEquals("val-taxCode", dto.getTaxCode());
        assertEquals("val-itemType", dto.getItemType());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        CreditMemo.CreditMemoLineItem dto1 = CreditMemo.CreditMemoLineItem.builder()
                        .lineItemId("test-lineItemId")
            .itemId("test-itemId")
            .itemCode("test-itemCode")
            .description("test-description")
            .quantity(42)
            .unitPrice(BigDecimal.TEN)
            .discountAmount(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .lineTotal(BigDecimal.TEN)
            .accountCode("test-accountCode")
            .taxCode("test-taxCode")
            .itemType("test-itemType")
            .reason("test-reason")
            .build();
        CreditMemo.CreditMemoLineItem dto2 = CreditMemo.CreditMemoLineItem.builder()
                        .lineItemId("test-lineItemId")
            .itemId("test-itemId")
            .itemCode("test-itemCode")
            .description("test-description")
            .quantity(42)
            .unitPrice(BigDecimal.TEN)
            .discountAmount(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .lineTotal(BigDecimal.TEN)
            .accountCode("test-accountCode")
            .taxCode("test-taxCode")
            .itemType("test-itemType")
            .reason("test-reason")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CreditMemo.CreditMemoLineItem dto = CreditMemo.CreditMemoLineItem.builder()
                        .lineItemId("test-lineItemId")
            .itemId("test-itemId")
            .itemCode("test-itemCode")
            .description("test-description")
            .quantity(42)
            .unitPrice(BigDecimal.TEN)
            .discountAmount(BigDecimal.TEN)
            .taxAmount(BigDecimal.TEN)
            .lineTotal(BigDecimal.TEN)
            .accountCode("test-accountCode")
            .taxCode("test-taxCode")
            .itemType("test-itemType")
            .reason("test-reason")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
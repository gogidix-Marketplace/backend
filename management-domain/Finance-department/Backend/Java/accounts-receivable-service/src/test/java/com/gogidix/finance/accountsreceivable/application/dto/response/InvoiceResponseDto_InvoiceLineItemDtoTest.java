package com.gogidix.finance.accountsreceivable.application.dto.response;

import com.gogidix.finance.accountsreceivable.application.dto.response.InvoiceResponseDto;
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
class InvoiceResponseDto_InvoiceLineItemDtoTest {

        @Test
    void testBuilder() {
        InvoiceResponseDto.InvoiceLineItemDto dto = InvoiceResponseDto.InvoiceLineItemDto.builder()
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
            .serviceStartDate("test-serviceStartDate")
            .serviceEndDate("test-serviceEndDate")
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
        assertEquals("test-serviceStartDate", dto.getServiceStartDate());
        assertEquals("test-serviceEndDate", dto.getServiceEndDate());
    }

    @Test
    void testSettersAndGetters() {
        InvoiceResponseDto.InvoiceLineItemDto dto = new InvoiceResponseDto.InvoiceLineItemDto();
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
        dto.setServiceStartDate("val-serviceStartDate");
        dto.setServiceEndDate("val-serviceEndDate");
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
        assertEquals("val-serviceStartDate", dto.getServiceStartDate());
        assertEquals("val-serviceEndDate", dto.getServiceEndDate());
    }

    @Test
    void testEqualsAndHashCode() {
        InvoiceResponseDto.InvoiceLineItemDto dto1 = InvoiceResponseDto.InvoiceLineItemDto.builder()
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
            .serviceStartDate("test-serviceStartDate")
            .serviceEndDate("test-serviceEndDate")
            .build();
        InvoiceResponseDto.InvoiceLineItemDto dto2 = InvoiceResponseDto.InvoiceLineItemDto.builder()
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
            .serviceStartDate("test-serviceStartDate")
            .serviceEndDate("test-serviceEndDate")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        InvoiceResponseDto.InvoiceLineItemDto dto = InvoiceResponseDto.InvoiceLineItemDto.builder()
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
            .serviceStartDate("test-serviceStartDate")
            .serviceEndDate("test-serviceEndDate")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
package com.gogidix.finance.cashflow.application.dto.response;

import com.gogidix.finance.cashflow.application.dto.response.CashflowStatementResponseDto;
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
class CashflowStatementResponseDto_StatementItemDtoTest {

        @Test
    void testBuilder() {
        CashflowStatementResponseDto.StatementItemDto dto = CashflowStatementResponseDto.StatementItemDto.builder()
                        .itemId("test-itemId")
            .code("test-code")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .type(null)
            .category("test-category")
            .sequence(42)
            .isSubtotal(true)
            .parentItemId("test-parentItemId")
            .build();
        assertNotNull(dto);
        assertEquals("test-itemId", dto.getItemId());
        assertEquals("test-code", dto.getCode());
        assertEquals("test-description", dto.getDescription());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals("test-category", dto.getCategory());
        assertEquals(42, dto.getSequence());
        assertTrue(dto.getIsSubtotal());
        assertEquals("test-parentItemId", dto.getParentItemId());
    }

    @Test
    void testSettersAndGetters() {
        CashflowStatementResponseDto.StatementItemDto dto = new CashflowStatementResponseDto.StatementItemDto();
        dto.setItemId("val-itemId");
        dto.setCode("val-code");
        dto.setDescription("val-description");
        dto.setAmount(BigDecimal.ONE);
        dto.setCategory("val-category");
        dto.setSequence(99);
        dto.setIsSubtotal(true);
        dto.setParentItemId("val-parentItemId");
        assertEquals("val-itemId", dto.getItemId());
        assertEquals("val-code", dto.getCode());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals("val-category", dto.getCategory());
        assertEquals(99, dto.getSequence());
        assertTrue(dto.getIsSubtotal());
        assertEquals("val-parentItemId", dto.getParentItemId());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowStatementResponseDto.StatementItemDto dto1 = CashflowStatementResponseDto.StatementItemDto.builder()
                        .itemId("test-itemId")
            .code("test-code")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .type(null)
            .category("test-category")
            .sequence(42)
            .isSubtotal(true)
            .parentItemId("test-parentItemId")
            .build();
        CashflowStatementResponseDto.StatementItemDto dto2 = CashflowStatementResponseDto.StatementItemDto.builder()
                        .itemId("test-itemId")
            .code("test-code")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .type(null)
            .category("test-category")
            .sequence(42)
            .isSubtotal(true)
            .parentItemId("test-parentItemId")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CashflowStatementResponseDto.StatementItemDto dto = CashflowStatementResponseDto.StatementItemDto.builder()
                        .itemId("test-itemId")
            .code("test-code")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .type(null)
            .category("test-category")
            .sequence(42)
            .isSubtotal(true)
            .parentItemId("test-parentItemId")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
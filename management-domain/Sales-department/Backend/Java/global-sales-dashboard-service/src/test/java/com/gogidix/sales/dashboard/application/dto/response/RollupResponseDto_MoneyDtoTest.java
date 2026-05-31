package com.gogidix.sales.dashboard.application.dto.response;

import com.gogidix.sales.dashboard.application.dto.response.RollupResponseDto;
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
class RollupResponseDto_MoneyDtoTest {

        @Test
    void testBuilder() {
        RollupResponseDto.MoneyDto dto = RollupResponseDto.MoneyDto.builder()
                        .amount(null)
            .currency("test-currency")
            .formatted("test-formatted")
            .build();
        assertNotNull(dto);
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-formatted", dto.getFormatted());
    }

    @Test
    void testSettersAndGetters() {
        RollupResponseDto.MoneyDto dto = new RollupResponseDto.MoneyDto();
        dto.setCurrency("val-currency");
        dto.setFormatted("val-formatted");
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-formatted", dto.getFormatted());
    }

    @Test
    void testEqualsAndHashCode() {
        RollupResponseDto.MoneyDto dto1 = RollupResponseDto.MoneyDto.builder()
                        .amount(null)
            .currency("test-currency")
            .formatted("test-formatted")
            .build();
        RollupResponseDto.MoneyDto dto2 = RollupResponseDto.MoneyDto.builder()
                        .amount(null)
            .currency("test-currency")
            .formatted("test-formatted")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RollupResponseDto.MoneyDto dto = RollupResponseDto.MoneyDto.builder()
                        .amount(null)
            .currency("test-currency")
            .formatted("test-formatted")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
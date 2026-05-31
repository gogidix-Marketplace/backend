package com.gogidix.finance.generalledger.application.dto.response;

import com.gogidix.finance.generalledger.application.dto.response.LedgerAccountResponseDto;
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
class LedgerAccountResponseDto_AccountTagDtoTest {

        @Test
    void testBuilder() {
        LedgerAccountResponseDto.AccountTagDto dto = LedgerAccountResponseDto.AccountTagDto.builder()
                        .key("test-key")
            .value("test-value")
            .build();
        assertNotNull(dto);
        assertEquals("test-key", dto.getKey());
        assertEquals("test-value", dto.getValue());
    }

    @Test
    void testSettersAndGetters() {
        LedgerAccountResponseDto.AccountTagDto dto = new LedgerAccountResponseDto.AccountTagDto();
        dto.setKey("val-key");
        dto.setValue("val-value");
        assertEquals("val-key", dto.getKey());
        assertEquals("val-value", dto.getValue());
    }

    @Test
    void testEqualsAndHashCode() {
        LedgerAccountResponseDto.AccountTagDto dto1 = LedgerAccountResponseDto.AccountTagDto.builder()
                        .key("test-key")
            .value("test-value")
            .build();
        LedgerAccountResponseDto.AccountTagDto dto2 = LedgerAccountResponseDto.AccountTagDto.builder()
                        .key("test-key")
            .value("test-value")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LedgerAccountResponseDto.AccountTagDto dto = LedgerAccountResponseDto.AccountTagDto.builder()
                        .key("test-key")
            .value("test-value")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
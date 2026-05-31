package com.gogidix.customersupport.slamanagement.application.dto;

import com.gogidix.customersupport.slamanagement.application.dto.SLAPolicyResponseDto;
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
class SLAPolicyResponseDto_PenaltyConfigDtoTest {

        @Test
    void testBuilder() {
        SLAPolicyResponseDto.PenaltyConfigDto dto = SLAPolicyResponseDto.PenaltyConfigDto.builder()
                        .enabled(true)
            .penaltyPerBreach(null)
            .maxPenaltyPerTicket(null)
            .build();
        assertNotNull(dto);
        assertTrue(dto.getEnabled());
    }

    @Test
    void testSettersAndGetters() {
        SLAPolicyResponseDto.PenaltyConfigDto dto = new SLAPolicyResponseDto.PenaltyConfigDto();
        dto.setEnabled(true);
        assertTrue(dto.getEnabled());
    }

    @Test
    void testEqualsAndHashCode() {
        SLAPolicyResponseDto.PenaltyConfigDto dto1 = SLAPolicyResponseDto.PenaltyConfigDto.builder()
                        .enabled(true)
            .penaltyPerBreach(null)
            .maxPenaltyPerTicket(null)
            .build();
        SLAPolicyResponseDto.PenaltyConfigDto dto2 = SLAPolicyResponseDto.PenaltyConfigDto.builder()
                        .enabled(true)
            .penaltyPerBreach(null)
            .maxPenaltyPerTicket(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        SLAPolicyResponseDto.PenaltyConfigDto dto = SLAPolicyResponseDto.PenaltyConfigDto.builder()
                        .enabled(true)
            .penaltyPerBreach(null)
            .maxPenaltyPerTicket(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
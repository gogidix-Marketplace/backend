package com.gogidix.sales.countrydashboard.application.dto.response;

import com.gogidix.sales.countrydashboard.application.dto.response.ComparisonDataResponseDto;
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
class ComparisonDataResponseDtoTest {

        @Test
    void testBuilder() {
        ComparisonDataResponseDto dto = ComparisonDataResponseDto.builder()
                        .yearOverYear(null)
            .monthOverMonth(null)
            .quarterOverQuarter(null)
            .regionalComparison(null)
            .build();
        assertNotNull(dto);

    }

    @Test
    void testSettersAndGetters() {
        ComparisonDataResponseDto dto = new ComparisonDataResponseDto();


    }

    @Test
    void testEqualsAndHashCode() {
        ComparisonDataResponseDto dto1 = ComparisonDataResponseDto.builder()
                        .yearOverYear(null)
            .monthOverMonth(null)
            .quarterOverQuarter(null)
            .regionalComparison(null)
            .build();
        ComparisonDataResponseDto dto2 = ComparisonDataResponseDto.builder()
                        .yearOverYear(null)
            .monthOverMonth(null)
            .quarterOverQuarter(null)
            .regionalComparison(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ComparisonDataResponseDto dto = ComparisonDataResponseDto.builder()
                        .yearOverYear(null)
            .monthOverMonth(null)
            .quarterOverQuarter(null)
            .regionalComparison(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
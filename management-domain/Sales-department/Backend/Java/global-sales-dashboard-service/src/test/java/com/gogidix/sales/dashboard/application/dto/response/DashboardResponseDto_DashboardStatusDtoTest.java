package com.gogidix.sales.dashboard.application.dto.response;

import com.gogidix.sales.dashboard.application.dto.response.DashboardResponseDto;
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
class DashboardResponseDto_DashboardStatusDtoTest {

        @Test
    void testBuilder() {
        DashboardResponseDto.DashboardStatusDto dto = DashboardResponseDto.DashboardStatusDto.builder()
                        .name("test-name")
            .value("test-value")
            .build();
        assertNotNull(dto);
        assertEquals("test-name", dto.getName());
        assertEquals("test-value", dto.getValue());
    }

    @Test
    void testSettersAndGetters() {
        DashboardResponseDto.DashboardStatusDto dto = new DashboardResponseDto.DashboardStatusDto();
        dto.setName("val-name");
        dto.setValue("val-value");
        assertEquals("val-name", dto.getName());
        assertEquals("val-value", dto.getValue());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardResponseDto.DashboardStatusDto dto1 = DashboardResponseDto.DashboardStatusDto.builder()
                        .name("test-name")
            .value("test-value")
            .build();
        DashboardResponseDto.DashboardStatusDto dto2 = DashboardResponseDto.DashboardStatusDto.builder()
                        .name("test-name")
            .value("test-value")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardResponseDto.DashboardStatusDto dto = DashboardResponseDto.DashboardStatusDto.builder()
                        .name("test-name")
            .value("test-value")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
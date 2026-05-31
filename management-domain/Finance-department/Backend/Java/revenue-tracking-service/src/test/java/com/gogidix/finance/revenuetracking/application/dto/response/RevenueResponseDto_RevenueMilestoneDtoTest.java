package com.gogidix.finance.revenuetracking.application.dto.response;

import com.gogidix.finance.revenuetracking.application.dto.response.RevenueResponseDto;
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
class RevenueResponseDto_RevenueMilestoneDtoTest {

        @Test
    void testBuilder() {
        RevenueResponseDto.RevenueMilestoneDto dto = RevenueResponseDto.RevenueMilestoneDto.builder()
                        .milestoneId("test-milestoneId")
            .name("test-name")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .targetDate(LocalDate.of(2025,1,15))
            .completedDate(LocalDate.of(2025,1,15))
            .status(RevenueResponseDto.RevenueMilestoneDto.MilestoneStatusDto.PENDING)
            .build();
        assertNotNull(dto);
        assertEquals("test-milestoneId", dto.getMilestoneId());
        assertEquals("test-name", dto.getName());
        assertEquals("test-description", dto.getDescription());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals(LocalDate.of(2025,1,15), dto.getTargetDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getCompletedDate());
        assertEquals(RevenueResponseDto.RevenueMilestoneDto.MilestoneStatusDto.PENDING, dto.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        RevenueResponseDto.RevenueMilestoneDto dto = new RevenueResponseDto.RevenueMilestoneDto();
        dto.setMilestoneId("val-milestoneId");
        dto.setName("val-name");
        dto.setDescription("val-description");
        dto.setAmount(BigDecimal.ONE);
        dto.setTargetDate(LocalDate.of(2025,6,1));
        dto.setCompletedDate(LocalDate.of(2025,6,1));
        dto.setStatus(RevenueResponseDto.RevenueMilestoneDto.MilestoneStatusDto.PENDING);
        assertEquals("val-milestoneId", dto.getMilestoneId());
        assertEquals("val-name", dto.getName());
        assertEquals("val-description", dto.getDescription());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(LocalDate.of(2025,6,1), dto.getTargetDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getCompletedDate());
        assertEquals(RevenueResponseDto.RevenueMilestoneDto.MilestoneStatusDto.PENDING, dto.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        RevenueResponseDto.RevenueMilestoneDto dto1 = RevenueResponseDto.RevenueMilestoneDto.builder()
                        .milestoneId("test-milestoneId")
            .name("test-name")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .targetDate(LocalDate.of(2025,1,15))
            .completedDate(LocalDate.of(2025,1,15))
            .status(RevenueResponseDto.RevenueMilestoneDto.MilestoneStatusDto.PENDING)
            .build();
        RevenueResponseDto.RevenueMilestoneDto dto2 = RevenueResponseDto.RevenueMilestoneDto.builder()
                        .milestoneId("test-milestoneId")
            .name("test-name")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .targetDate(LocalDate.of(2025,1,15))
            .completedDate(LocalDate.of(2025,1,15))
            .status(RevenueResponseDto.RevenueMilestoneDto.MilestoneStatusDto.PENDING)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RevenueResponseDto.RevenueMilestoneDto dto = RevenueResponseDto.RevenueMilestoneDto.builder()
                        .milestoneId("test-milestoneId")
            .name("test-name")
            .description("test-description")
            .amount(BigDecimal.TEN)
            .targetDate(LocalDate.of(2025,1,15))
            .completedDate(LocalDate.of(2025,1,15))
            .status(RevenueResponseDto.RevenueMilestoneDto.MilestoneStatusDto.PENDING)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
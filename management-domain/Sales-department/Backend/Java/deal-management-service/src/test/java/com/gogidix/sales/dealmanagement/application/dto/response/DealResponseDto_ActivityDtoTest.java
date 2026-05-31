package com.gogidix.sales.dealmanagement.application.dto.response;

import com.gogidix.sales.dealmanagement.application.dto.response.DealResponseDto;
import com.gogidix.sales.dealmanagement.domain.model.DealActivity;
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
class DealResponseDto_ActivityDtoTest {

        @Test
    void testBuilder() {
        DealResponseDto.ActivityDto dto = DealResponseDto.ActivityDto.builder()
                        .activityId("test-activityId")
            .activityType(DealActivity.ActivityType.CALL)
            .subject("test-subject")
            .description("test-description")
            .userName("test-userName")
            .activityDate(Instant.parse("2025-01-15T10:00:00Z"))
            .isCompleted(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-activityId", dto.getActivityId());
        assertEquals("test-subject", dto.getSubject());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-userName", dto.getUserName());
        assertTrue(dto.getIsCompleted());
    }

    @Test
    void testSettersAndGetters() {
        DealResponseDto.ActivityDto dto = new DealResponseDto.ActivityDto();
        dto.setActivityId("val-activityId");
        dto.setSubject("val-subject");
        dto.setDescription("val-description");
        dto.setUserName("val-userName");
        dto.setIsCompleted(true);
        assertEquals("val-activityId", dto.getActivityId());
        assertEquals("val-subject", dto.getSubject());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-userName", dto.getUserName());
        assertTrue(dto.getIsCompleted());
    }

    @Test
    void testEqualsAndHashCode() {
        DealResponseDto.ActivityDto dto1 = DealResponseDto.ActivityDto.builder()
                        .activityId("test-activityId")
            .activityType(DealActivity.ActivityType.CALL)
            .subject("test-subject")
            .description("test-description")
            .userName("test-userName")
            .activityDate(Instant.parse("2025-01-15T10:00:00Z"))
            .isCompleted(true)
            .build();
        DealResponseDto.ActivityDto dto2 = DealResponseDto.ActivityDto.builder()
                        .activityId("test-activityId")
            .activityType(DealActivity.ActivityType.CALL)
            .subject("test-subject")
            .description("test-description")
            .userName("test-userName")
            .activityDate(Instant.parse("2025-01-15T10:00:00Z"))
            .isCompleted(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DealResponseDto.ActivityDto dto = DealResponseDto.ActivityDto.builder()
                        .activityId("test-activityId")
            .activityType(DealActivity.ActivityType.CALL)
            .subject("test-subject")
            .description("test-description")
            .userName("test-userName")
            .activityDate(Instant.parse("2025-01-15T10:00:00Z"))
            .isCompleted(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
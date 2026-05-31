package com.gogidix.customersupport.qualitymanagement.application.dto;

import com.gogidix.customersupport.qualitymanagement.application.dto.AgentQualityProfileDto;
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
class AgentQualityProfileDto_ScoreSnapshotDtoTest {

        @Test
    void testBuilder() {
        AgentQualityProfileDto.ScoreSnapshotDto dto = AgentQualityProfileDto.ScoreSnapshotDto.builder()
                        .reviewId("test-reviewId")
            .score(null)
            .passed(true)
            .reviewType("test-reviewType")
            .reviewDate(Instant.parse("2025-01-15T10:00:00Z"))
            .channel("test-channel")
            .build();
        assertNotNull(dto);
        assertEquals("test-reviewId", dto.getReviewId());
        assertTrue(dto.getPassed());
        assertEquals("test-reviewType", dto.getReviewType());
        assertEquals("test-channel", dto.getChannel());
    }

    @Test
    void testSettersAndGetters() {
        AgentQualityProfileDto.ScoreSnapshotDto dto = new AgentQualityProfileDto.ScoreSnapshotDto();
        dto.setReviewId("val-reviewId");
        dto.setPassed(true);
        dto.setReviewType("val-reviewType");
        dto.setChannel("val-channel");
        assertEquals("val-reviewId", dto.getReviewId());
        assertTrue(dto.getPassed());
        assertEquals("val-reviewType", dto.getReviewType());
        assertEquals("val-channel", dto.getChannel());
    }

    @Test
    void testEqualsAndHashCode() {
        AgentQualityProfileDto.ScoreSnapshotDto dto1 = AgentQualityProfileDto.ScoreSnapshotDto.builder()
                        .reviewId("test-reviewId")
            .score(null)
            .passed(true)
            .reviewType("test-reviewType")
            .reviewDate(Instant.parse("2025-01-15T10:00:00Z"))
            .channel("test-channel")
            .build();
        AgentQualityProfileDto.ScoreSnapshotDto dto2 = AgentQualityProfileDto.ScoreSnapshotDto.builder()
                        .reviewId("test-reviewId")
            .score(null)
            .passed(true)
            .reviewType("test-reviewType")
            .reviewDate(Instant.parse("2025-01-15T10:00:00Z"))
            .channel("test-channel")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AgentQualityProfileDto.ScoreSnapshotDto dto = AgentQualityProfileDto.ScoreSnapshotDto.builder()
                        .reviewId("test-reviewId")
            .score(null)
            .passed(true)
            .reviewType("test-reviewType")
            .reviewDate(Instant.parse("2025-01-15T10:00:00Z"))
            .channel("test-channel")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
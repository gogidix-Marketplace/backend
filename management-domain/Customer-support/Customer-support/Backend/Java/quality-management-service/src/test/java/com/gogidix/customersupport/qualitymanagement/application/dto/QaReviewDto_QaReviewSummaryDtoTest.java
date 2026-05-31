package com.gogidix.customersupport.qualitymanagement.application.dto;

import com.gogidix.customersupport.qualitymanagement.application.dto.QaReviewDto;
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
class QaReviewDto_QaReviewSummaryDtoTest {

        @Test
    void testBuilder() {
        QaReviewDto.QaReviewSummaryDto dto = QaReviewDto.QaReviewSummaryDto.builder()
                        .reviewId("test-reviewId")
            .ticketId("test-ticketId")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .reviewerName("test-reviewerName")
            .reviewType("test-reviewType")
            .reviewStatus("test-reviewStatus")
            .percentageScore(null)
            .passed(true)
            .channelType("test-channelType")
            .reviewDate(Instant.parse("2025-01-15T10:00:00Z"))
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .isOverdue(true)
            .isCalibrated(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-reviewId", dto.getReviewId());
        assertEquals("test-ticketId", dto.getTicketId());
        assertEquals("test-agentId", dto.getAgentId());
        assertEquals("test-agentName", dto.getAgentName());
        assertEquals("test-reviewerName", dto.getReviewerName());
        assertEquals("test-reviewType", dto.getReviewType());
        assertEquals("test-reviewStatus", dto.getReviewStatus());
        assertTrue(dto.getPassed());
        assertEquals("test-channelType", dto.getChannelType());
        assertTrue(dto.getIsOverdue());
        assertTrue(dto.getIsCalibrated());
    }

    @Test
    void testSettersAndGetters() {
        QaReviewDto.QaReviewSummaryDto dto = new QaReviewDto.QaReviewSummaryDto();
        dto.setReviewId("val-reviewId");
        dto.setTicketId("val-ticketId");
        dto.setAgentId("val-agentId");
        dto.setAgentName("val-agentName");
        dto.setReviewerName("val-reviewerName");
        dto.setReviewType("val-reviewType");
        dto.setReviewStatus("val-reviewStatus");
        dto.setPassed(true);
        dto.setChannelType("val-channelType");
        dto.setIsOverdue(true);
        dto.setIsCalibrated(true);
        assertEquals("val-reviewId", dto.getReviewId());
        assertEquals("val-ticketId", dto.getTicketId());
        assertEquals("val-agentId", dto.getAgentId());
        assertEquals("val-agentName", dto.getAgentName());
        assertEquals("val-reviewerName", dto.getReviewerName());
        assertEquals("val-reviewType", dto.getReviewType());
        assertEquals("val-reviewStatus", dto.getReviewStatus());
        assertTrue(dto.getPassed());
        assertEquals("val-channelType", dto.getChannelType());
        assertTrue(dto.getIsOverdue());
        assertTrue(dto.getIsCalibrated());
    }

    @Test
    void testEqualsAndHashCode() {
        QaReviewDto.QaReviewSummaryDto dto1 = QaReviewDto.QaReviewSummaryDto.builder()
                        .reviewId("test-reviewId")
            .ticketId("test-ticketId")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .reviewerName("test-reviewerName")
            .reviewType("test-reviewType")
            .reviewStatus("test-reviewStatus")
            .percentageScore(null)
            .passed(true)
            .channelType("test-channelType")
            .reviewDate(Instant.parse("2025-01-15T10:00:00Z"))
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .isOverdue(true)
            .isCalibrated(true)
            .build();
        QaReviewDto.QaReviewSummaryDto dto2 = QaReviewDto.QaReviewSummaryDto.builder()
                        .reviewId("test-reviewId")
            .ticketId("test-ticketId")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .reviewerName("test-reviewerName")
            .reviewType("test-reviewType")
            .reviewStatus("test-reviewStatus")
            .percentageScore(null)
            .passed(true)
            .channelType("test-channelType")
            .reviewDate(Instant.parse("2025-01-15T10:00:00Z"))
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .isOverdue(true)
            .isCalibrated(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        QaReviewDto.QaReviewSummaryDto dto = QaReviewDto.QaReviewSummaryDto.builder()
                        .reviewId("test-reviewId")
            .ticketId("test-ticketId")
            .agentId("test-agentId")
            .agentName("test-agentName")
            .reviewerName("test-reviewerName")
            .reviewType("test-reviewType")
            .reviewStatus("test-reviewStatus")
            .percentageScore(null)
            .passed(true)
            .channelType("test-channelType")
            .reviewDate(Instant.parse("2025-01-15T10:00:00Z"))
            .dueDate(Instant.parse("2025-01-15T10:00:00Z"))
            .isOverdue(true)
            .isCalibrated(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
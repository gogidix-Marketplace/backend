package com.gogidix.customersupport.qualitymanagement.application.service;

import com.gogidix.customersupport.qualitymanagement.application.service.QaReviewService;
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
class QaReviewService_QaReviewStatisticsDtoTest {

        @Test
    void testBuilder() {
        QaReviewService.QaReviewStatisticsDto dto = QaReviewService.QaReviewStatisticsDto.builder()
                        .totalReviews(42L)
            .completedReviews(42L)
            .pendingReviews(42L)
            .passedReviews(42L)
            .failedReviews(42L)
            .averageScore(null)
            .passRate(null)
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotalReviews());
        assertEquals(42L, dto.getCompletedReviews());
        assertEquals(42L, dto.getPendingReviews());
        assertEquals(42L, dto.getPassedReviews());
        assertEquals(42L, dto.getFailedReviews());
    }

    @Test
    void testSettersAndGetters() {
        QaReviewService.QaReviewStatisticsDto dto = new QaReviewService.QaReviewStatisticsDto();


    }

    @Test
    void testEqualsAndHashCode() {
        QaReviewService.QaReviewStatisticsDto dto1 = QaReviewService.QaReviewStatisticsDto.builder()
                        .totalReviews(42L)
            .completedReviews(42L)
            .pendingReviews(42L)
            .passedReviews(42L)
            .failedReviews(42L)
            .averageScore(null)
            .passRate(null)
            .build();
        QaReviewService.QaReviewStatisticsDto dto2 = QaReviewService.QaReviewStatisticsDto.builder()
                        .totalReviews(42L)
            .completedReviews(42L)
            .pendingReviews(42L)
            .passedReviews(42L)
            .failedReviews(42L)
            .averageScore(null)
            .passRate(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        QaReviewService.QaReviewStatisticsDto dto = QaReviewService.QaReviewStatisticsDto.builder()
                        .totalReviews(42L)
            .completedReviews(42L)
            .pendingReviews(42L)
            .passedReviews(42L)
            .failedReviews(42L)
            .averageScore(null)
            .passRate(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
package com.gogidix.aiservices.airecommendationservice.application.service;

import com.gogidix.aiservices.airecommendationservice.domain.model.*;
import com.gogidix.aiservices.airecommendationservice.domain.port.out.RecommendationEnginePort;
import com.gogidix.aiservices.airecommendationservice.domain.policy.RecommendationPolicy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private RecommendationEnginePort recommendationEngine;

    @Mock
    private RecommendationPolicy policy;

    @InjectMocks
    private RecommendationService recommendationService;

    @Test
    void shouldGetRecommendations() {
        RecommendationResult mockResult = RecommendationResult.builder()
                .requestId("req-1")
                .userId("user-1")
                .type(RecommendationType.PERSONALIZED)
                .items(List.of(
                        RecommendationItem.builder()
                                .itemId("item-1")
                                .score(0.9)
                                .reason(RecommendationReason.COLLABORATIVE)
                                .build()
                ))
                .generatedAt(Instant.now())
                .build();

        when(recommendationEngine.generateRecommendations(anyString(), any(), anyString(), anyInt()))
                .thenReturn(mockResult);
        when(policy.getMaxRecommendations()).thenReturn(100);

        RecommendationResult result = recommendationService.getRecommendations(
                "user-1", RecommendationType.PERSONALIZED, "homepage", 10
        );

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo("user-1");
    }

    @Test
    void shouldGetSimilarItems() {
        List<RecommendationItem> items = List.of(
                RecommendationItem.builder()
                        .itemId("item-2")
                        .score(0.85)
                        .reason(RecommendationReason.CONTENT_BASED)
                        .build()
        );

        when(recommendationEngine.getSimilarItems(anyString(), anyInt())).thenReturn(items);

        List<RecommendationItem> result = recommendationService.getSimilarItems("item-1", 10);

        assertThat(result).hasSize(1);
    }
}

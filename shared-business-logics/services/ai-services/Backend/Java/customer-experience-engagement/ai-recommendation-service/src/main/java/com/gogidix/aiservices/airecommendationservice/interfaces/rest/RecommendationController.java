package com.gogidix.aiservices.airecommendationservice.interfaces.rest;

import com.gogidix.aiservices.airecommendationservice.application.service.RecommendationService;
import com.gogidix.aiservices.airecommendationservice.domain.model.RecommendationItem;
import com.gogidix.aiservices.airecommendationservice.domain.model.RecommendationResult;
import com.gogidix.aiservices.airecommendationservice.domain.model.RecommendationType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/{userId}")
    public ResponseEntity<RecommendationResult> getRecommendations(
            @PathVariable String userId,
            @RequestParam(defaultValue = "PERSONALIZED") RecommendationType type,
            @RequestParam(defaultValue = "homepage") String context,
            @RequestParam(defaultValue = "10") Integer limit) {

        RecommendationResult result = recommendationService.getRecommendations(userId, type, context, limit);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/similar/{itemId}")
    public ResponseEntity<List<RecommendationItem>> getSimilarItems(
            @PathVariable String itemId,
            @RequestParam(defaultValue = "10") Integer limit) {

        List<RecommendationItem> items = recommendationService.getSimilarItems(itemId, limit);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/trending")
    public ResponseEntity<List<RecommendationItem>> getTrendingItems(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "10") Integer limit) {

        List<RecommendationItem> items = recommendationService.getTrendingItems(category, limit);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/interactions")
    public ResponseEntity<Void> trackInteraction(
            @RequestParam String userId,
            @RequestParam String itemId,
            @RequestParam String interactionType) {

        recommendationService.trackInteraction(userId, itemId, interactionType);
        return ResponseEntity.noContent().build();
    }
}

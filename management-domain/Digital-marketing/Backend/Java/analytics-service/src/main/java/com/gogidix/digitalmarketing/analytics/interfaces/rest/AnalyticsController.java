package com.gogidix.digitalmarketing.analytics.interfaces.rest;

import com.gogidix.digitalmarketing.analytics.application.service.AnalyticsService;
import com.gogidix.digitalmarketing.analytics.domain.model.CampaignAnalytics;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService service;

    @PostMapping
    public ResponseEntity<CampaignAnalytics> create(@RequestBody CampaignAnalytics entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping
    public ResponseEntity<List<CampaignAnalytics>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignAnalytics> getById(@PathVariable String id) {
        CampaignAnalytics result = service.getById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

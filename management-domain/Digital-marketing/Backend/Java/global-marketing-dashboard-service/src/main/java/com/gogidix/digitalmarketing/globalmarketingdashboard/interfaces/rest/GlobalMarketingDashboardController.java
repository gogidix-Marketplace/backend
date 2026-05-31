package com.gogidix.digitalmarketing.globalmarketingdashboard.interfaces.rest;

import com.gogidix.digitalmarketing.globalmarketingdashboard.application.service.MarketingDashboardService;
import com.gogidix.digitalmarketing.globalmarketingdashboard.domain.model.MarketingDashboard;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/global-marketing/dashboard")
@RequiredArgsConstructor
public class GlobalMarketingDashboardController {
    private final MarketingDashboardService service;

    @PostMapping
    public ResponseEntity<MarketingDashboard> create(@RequestBody MarketingDashboard entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping
    public ResponseEntity<List<MarketingDashboard>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarketingDashboard> getById(@PathVariable String id) {
        MarketingDashboard result = service.getById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

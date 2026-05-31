package com.gogidix.globalbusinessmanagement.businessintelligence.interfaces.rest;

import com.gogidix.globalbusinessmanagement.businessintelligence.application.service.BusinessIntelligenceService;
import com.gogidix.globalbusinessmanagement.businessintelligence.domain.model.BIReport;
import com.gogidix.globalbusinessmanagement.businessintelligence.domain.model.Insight;
import com.gogidix.globalbusinessmanagement.businessintelligence.domain.model.Forecast;
import com.gogidix.globalbusinessmanagement.businessintelligence.domain.model.TrendAnalysis;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/business-intelligence")
@RequiredArgsConstructor
public class BusinessIntelligenceController {

    private final BusinessIntelligenceService service;

    @PostMapping("/reports")
    public ResponseEntity<BIReport> createReport(@RequestBody BIReport report) {
        return ResponseEntity.ok(service.createReport(report));
    }

    @GetMapping("/reports/{id}")
    public ResponseEntity<BIReport> getReport(@PathVariable String id) {
        BIReport result = service.getReport(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @GetMapping("/reports/tenant/{tenantId}")
    public ResponseEntity<List<BIReport>> getReportsByTenant(@PathVariable String tenantId) {
        return ResponseEntity.ok(service.getReportsByTenant(tenantId));
    }

    @DeleteMapping("/reports/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable String id) {
        service.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/insights")
    public ResponseEntity<Insight> createInsight(@RequestBody Insight insight) {
        return ResponseEntity.ok(service.createInsight(insight));
    }

    @GetMapping("/insights/{id}")
    public ResponseEntity<Insight> getInsight(@PathVariable String id) {
        Insight result = service.getInsight(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @GetMapping("/insights/tenant/{tenantId}")
    public ResponseEntity<List<Insight>> getInsightsByTenant(@PathVariable String tenantId) {
        return ResponseEntity.ok(service.getInsightsByTenant(tenantId));
    }

    @PostMapping("/forecasts")
    public ResponseEntity<Forecast> createForecast(@RequestBody Forecast forecast) {
        return ResponseEntity.ok(service.createForecast(forecast));
    }

    @GetMapping("/forecasts/{id}")
    public ResponseEntity<Forecast> getForecast(@PathVariable String id) {
        Forecast result = service.getForecast(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @GetMapping("/forecasts/tenant/{tenantId}")
    public ResponseEntity<List<Forecast>> getForecastsByTenant(@PathVariable String tenantId) {
        return ResponseEntity.ok(service.getForecastsByTenant(tenantId));
    }

    @PostMapping("/trend-analyses")
    public ResponseEntity<TrendAnalysis> createTrendAnalysis(@RequestBody TrendAnalysis analysis) {
        return ResponseEntity.ok(service.createTrendAnalysis(analysis));
    }

    @GetMapping("/trend-analyses/{id}")
    public ResponseEntity<TrendAnalysis> getTrendAnalysis(@PathVariable String id) {
        TrendAnalysis result = service.getTrendAnalysis(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @GetMapping("/trend-analyses/tenant/{tenantId}")
    public ResponseEntity<List<TrendAnalysis>> getTrendAnalysesByTenant(@PathVariable String tenantId) {
        return ResponseEntity.ok(service.getTrendAnalysesByTenant(tenantId));
    }

    @GetMapping("/trend-analyses/metric/{metric}")
    public ResponseEntity<List<TrendAnalysis>> getTrendAnalysesByMetric(@PathVariable String metric) {
        return ResponseEntity.ok(service.getTrendAnalysesByMetric(metric));
    }
}

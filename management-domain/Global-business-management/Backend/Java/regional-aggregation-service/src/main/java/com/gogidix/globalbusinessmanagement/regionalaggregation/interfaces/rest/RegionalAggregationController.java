package com.gogidix.globalbusinessmanagement.regionalaggregation.interfaces.rest;

import com.gogidix.globalbusinessmanagement.regionalaggregation.application.service.RegionalAggregationService;
import com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model.AggregatedMetrics;
import com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model.CountryContribution;
import com.gogidix.globalbusinessmanagement.regionalaggregation.domain.model.RegionalData;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/regional-aggregation")
@RequiredArgsConstructor
public class RegionalAggregationController {

    private final RegionalAggregationService regionalAggregationService;

    @PostMapping("/aggregate")
    public ResponseEntity<RegionalData> aggregateRegionalData(
            @RequestParam String regionCode,
            @RequestParam String periodId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(regionalAggregationService.aggregateRegionalData(regionCode, periodId, startDate, endDate));
    }

    @GetMapping("/metrics")
    public ResponseEntity<AggregatedMetrics> getAggregatedMetrics(
            @RequestParam String regionCode,
            @RequestParam String periodId) {
        return ResponseEntity.ok(regionalAggregationService.getAggregatedMetrics(regionCode, periodId));
    }

    @GetMapping("/top-countries")
    public ResponseEntity<List<CountryContribution>> getTopContributingCountries(
            @RequestParam String regionCode,
            @RequestParam String periodId,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(regionalAggregationService.getTopContributingCountries(regionCode, periodId, limit));
    }
}

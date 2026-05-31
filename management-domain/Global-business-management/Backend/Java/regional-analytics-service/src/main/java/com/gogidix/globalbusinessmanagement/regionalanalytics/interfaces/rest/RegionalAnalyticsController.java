package com.gogidix.globalbusinessmanagement.regionalanalytics.interfaces.rest;
import com.gogidix.globalbusinessmanagement.regionalanalytics.application.dto.RegionalAnalyticsRequestDto;
import com.gogidix.globalbusinessmanagement.regionalanalytics.application.dto.RegionalAnalyticsResponseDto;
import com.gogidix.globalbusinessmanagement.regionalanalytics.application.service.RegionalAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/regional-analytics")
@RequiredArgsConstructor
public class RegionalAnalyticsController {
    private final RegionalAnalyticsService service;
    @PostMapping
    public ResponseEntity<RegionalAnalyticsResponseDto> create(@RequestBody RegionalAnalyticsRequestDto dto) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto)); }
    @GetMapping("/{id}")
    public ResponseEntity<RegionalAnalyticsResponseDto> getById(@PathVariable String id) { return ResponseEntity.ok(service.getById(id)); }
    @GetMapping
    public ResponseEntity<List<RegionalAnalyticsResponseDto>> getAll() { return ResponseEntity.ok(service.getAll()); }
    @PutMapping("/{id}")
    public ResponseEntity<RegionalAnalyticsResponseDto> update(@PathVariable String id, @RequestBody RegionalAnalyticsRequestDto dto) { return ResponseEntity.ok(service.update(id, dto)); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}

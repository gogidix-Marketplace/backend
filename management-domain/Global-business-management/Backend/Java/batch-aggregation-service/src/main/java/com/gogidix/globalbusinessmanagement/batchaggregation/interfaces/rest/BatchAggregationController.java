package com.gogidix.globalbusinessmanagement.batchaggregation.interfaces.rest;

import com.gogidix.globalbusinessmanagement.batchaggregation.application.dto.BatchAggregationRequestDto;
import com.gogidix.globalbusinessmanagement.batchaggregation.application.dto.BatchAggregationResponseDto;
import com.gogidix.globalbusinessmanagement.batchaggregation.application.service.BatchAggregationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/batch-aggregations")
@RequiredArgsConstructor
public class BatchAggregationController {

    private final BatchAggregationService service;

    @PostMapping
    public ResponseEntity<BatchAggregationResponseDto> create(@RequestBody BatchAggregationRequestDto dto) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto)); }
    @GetMapping("/{id}")
    public ResponseEntity<BatchAggregationResponseDto> getById(@PathVariable String id) { return ResponseEntity.ok(service.getById(id)); }
    @GetMapping
    public ResponseEntity<List<BatchAggregationResponseDto>> getAll() { return ResponseEntity.ok(service.getAll()); }
    @PutMapping("/{id}")
    public ResponseEntity<BatchAggregationResponseDto> update(@PathVariable String id, @RequestBody BatchAggregationRequestDto dto) { return ResponseEntity.ok(service.update(id, dto)); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}

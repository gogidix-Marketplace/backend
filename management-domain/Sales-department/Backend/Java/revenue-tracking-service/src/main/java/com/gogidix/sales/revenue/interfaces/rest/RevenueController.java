package com.gogidix.sales.revenue.interfaces.rest;

import com.gogidix.sales.revenue.application.dto.RevenueRequestDto;
import com.gogidix.sales.revenue.application.dto.RevenueResponseDto;
import com.gogidix.sales.revenue.application.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/revenues")
@RequiredArgsConstructor
public class RevenueController {

    private final RevenueService service;

    @PostMapping
    public ResponseEntity<RevenueResponseDto> create(@RequestBody RevenueRequestDto dto) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto)); }
    @GetMapping("/{id}")
    public ResponseEntity<RevenueResponseDto> getById(@PathVariable String id) { return ResponseEntity.ok(service.getById(id)); }
    @GetMapping
    public ResponseEntity<List<RevenueResponseDto>> getAll() { return ResponseEntity.ok(service.getAll()); }
    @PutMapping("/{id}")
    public ResponseEntity<RevenueResponseDto> update(@PathVariable String id, @RequestBody RevenueRequestDto dto) { return ResponseEntity.ok(service.update(id, dto)); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
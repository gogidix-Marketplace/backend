package com.gogidix.globalbusinessmanagement.kafkaingestion.interfaces.rest;
import com.gogidix.globalbusinessmanagement.kafkaingestion.application.dto.IngestionJobRequestDto;
import com.gogidix.globalbusinessmanagement.kafkaingestion.application.dto.IngestionJobResponseDto;
import com.gogidix.globalbusinessmanagement.kafkaingestion.application.service.IngestionJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/ingestion-jobs")
@RequiredArgsConstructor
public class IngestionJobController {
    private final IngestionJobService service;
    @PostMapping
    public ResponseEntity<IngestionJobResponseDto> create(@RequestBody IngestionJobRequestDto dto) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto)); }
    @GetMapping("/{id}")
    public ResponseEntity<IngestionJobResponseDto> getById(@PathVariable String id) { return ResponseEntity.ok(service.getById(id)); }
    @GetMapping
    public ResponseEntity<List<IngestionJobResponseDto>> getAll() { return ResponseEntity.ok(service.getAll()); }
    @PutMapping("/{id}")
    public ResponseEntity<IngestionJobResponseDto> update(@PathVariable String id, @RequestBody IngestionJobRequestDto dto) { return ResponseEntity.ok(service.update(id, dto)); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}

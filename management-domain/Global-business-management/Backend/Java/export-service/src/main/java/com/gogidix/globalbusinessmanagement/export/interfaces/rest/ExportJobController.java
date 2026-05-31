package com.gogidix.globalbusinessmanagement.export.interfaces.rest;
import com.gogidix.globalbusinessmanagement.export.application.dto.ExportJobRequestDto;
import com.gogidix.globalbusinessmanagement.export.application.dto.ExportJobResponseDto;
import com.gogidix.globalbusinessmanagement.export.application.service.ExportJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/export-jobs")
@RequiredArgsConstructor
public class ExportJobController {
    private final ExportJobService service;
    @PostMapping
    public ResponseEntity<ExportJobResponseDto> create(@RequestBody ExportJobRequestDto dto) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto)); }
    @GetMapping("/{id}")
    public ResponseEntity<ExportJobResponseDto> getById(@PathVariable String id) { return ResponseEntity.ok(service.getById(id)); }
    @GetMapping
    public ResponseEntity<List<ExportJobResponseDto>> getAll() { return ResponseEntity.ok(service.getAll()); }
    @PutMapping("/{id}")
    public ResponseEntity<ExportJobResponseDto> update(@PathVariable String id, @RequestBody ExportJobRequestDto dto) { return ResponseEntity.ok(service.update(id, dto)); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}

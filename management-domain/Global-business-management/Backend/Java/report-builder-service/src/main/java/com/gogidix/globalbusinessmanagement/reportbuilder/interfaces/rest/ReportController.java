package com.gogidix.globalbusinessmanagement.reportbuilder.interfaces.rest;
import com.gogidix.globalbusinessmanagement.reportbuilder.application.dto.ReportRequestDto;
import com.gogidix.globalbusinessmanagement.reportbuilder.application.dto.ReportResponseDto;
import com.gogidix.globalbusinessmanagement.reportbuilder.application.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService service;
    @PostMapping
    public ResponseEntity<ReportResponseDto> create(@RequestBody ReportRequestDto dto) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto)); }
    @GetMapping("/{id}")
    public ResponseEntity<ReportResponseDto> getById(@PathVariable String id) { return ResponseEntity.ok(service.getById(id)); }
    @GetMapping
    public ResponseEntity<List<ReportResponseDto>> getAll() { return ResponseEntity.ok(service.getAll()); }
    @PutMapping("/{id}")
    public ResponseEntity<ReportResponseDto> update(@PathVariable String id, @RequestBody ReportRequestDto dto) { return ResponseEntity.ok(service.update(id, dto)); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}

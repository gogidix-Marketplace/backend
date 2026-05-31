package com.gogidix.globalbusinessmanagement.scheduledreport.interfaces.rest;
import com.gogidix.globalbusinessmanagement.scheduledreport.application.dto.ScheduledReportRequestDto;
import com.gogidix.globalbusinessmanagement.scheduledreport.application.dto.ScheduledReportResponseDto;
import com.gogidix.globalbusinessmanagement.scheduledreport.application.service.ScheduledReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/scheduled-reports")
@RequiredArgsConstructor
public class ScheduledReportController {
    private final ScheduledReportService service;
    @PostMapping
    public ResponseEntity<ScheduledReportResponseDto> create(@RequestBody ScheduledReportRequestDto dto) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto)); }
    @GetMapping("/{id}")
    public ResponseEntity<ScheduledReportResponseDto> getById(@PathVariable String id) { return ResponseEntity.ok(service.getById(id)); }
    @GetMapping
    public ResponseEntity<List<ScheduledReportResponseDto>> getAll() { return ResponseEntity.ok(service.getAll()); }
    @PutMapping("/{id}")
    public ResponseEntity<ScheduledReportResponseDto> update(@PathVariable String id, @RequestBody ScheduledReportRequestDto dto) { return ResponseEntity.ok(service.update(id, dto)); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}

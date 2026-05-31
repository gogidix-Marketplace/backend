package com.gogidix.sysadmin.compliance.interfaces.rest;
import com.gogidix.sysadmin.compliance.application.service.ComplianceReportService;
import com.gogidix.sysadmin.compliance.domain.model.ComplianceReport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/compliance")
@RequiredArgsConstructor
public class ComplianceReportController {
    private final ComplianceReportService service;
    @PostMapping
    public ResponseEntity<ComplianceReport> create(@RequestBody ComplianceReport entity) { return ResponseEntity.ok(service.create(entity)); }
    @GetMapping
    public ResponseEntity<List<ComplianceReport>> getAll() { return ResponseEntity.ok(service.getAll()); }
    @GetMapping(("/{id}"))
    public ResponseEntity<ComplianceReport> getById(@PathVariable String id) { ComplianceReport result = service.getById(id); return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build(); }
    @DeleteMapping(("/{id}"))
    public ResponseEntity<Void> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}

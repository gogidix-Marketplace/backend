package com.gogidix.digitalmarketing.leadgeneration.interfaces.rest;

import com.gogidix.digitalmarketing.leadgeneration.application.service.LeadGenerationService;
import com.gogidix.digitalmarketing.leadgeneration.domain.model.Lead;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leads")
@RequiredArgsConstructor
public class LeadGenerationController {
    private final LeadGenerationService service;

    @PostMapping
    public ResponseEntity<Lead> create(@RequestBody Lead lead) {
        return ResponseEntity.ok(service.create(lead));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lead> getById(@PathVariable String id) {
        Lead result = service.getById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @GetMapping("/source")
    public ResponseEntity<List<Lead>> getBySource(@RequestParam String tenantId, @RequestParam String source) {
        return ResponseEntity.ok(service.getBySource(tenantId, source));
    }
}

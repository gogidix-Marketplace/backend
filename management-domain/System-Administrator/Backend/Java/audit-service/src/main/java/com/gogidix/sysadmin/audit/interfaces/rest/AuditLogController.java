package com.gogidix.sysadmin.audit.interfaces.rest;

import com.gogidix.sysadmin.audit.application.service.AuditLogService;
import com.gogidix.sysadmin.audit.domain.model.AuditLog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
public class AuditLogController {
    private final AuditLogService service;

    @PostMapping
    public ResponseEntity<AuditLog> create(@RequestBody AuditLog entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLog> getById(@PathVariable String id) {
        AuditLog result = service.getById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

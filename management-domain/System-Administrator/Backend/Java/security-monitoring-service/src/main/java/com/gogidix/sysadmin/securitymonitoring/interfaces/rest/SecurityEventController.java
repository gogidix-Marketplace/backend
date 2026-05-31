package com.gogidix.sysadmin.securitymonitoring.interfaces.rest;
import com.gogidix.sysadmin.securitymonitoring.application.service.SecurityEventService;
import com.gogidix.sysadmin.securitymonitoring.domain.model.SecurityEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/security-monitoring")
@RequiredArgsConstructor
public class SecurityEventController {
    private final SecurityEventService service;
    @PostMapping
    public ResponseEntity<SecurityEvent> create(@RequestBody SecurityEvent entity) { return ResponseEntity.ok(service.create(entity)); }
    @GetMapping
    public ResponseEntity<List<SecurityEvent>> getAll() { return ResponseEntity.ok(service.getAll()); }
    @GetMapping(("/{id}"))
    public ResponseEntity<SecurityEvent> getById(@PathVariable String id) { SecurityEvent result = service.getById(id); return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build(); }
    @DeleteMapping(("/{id}"))
    public ResponseEntity<Void> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}

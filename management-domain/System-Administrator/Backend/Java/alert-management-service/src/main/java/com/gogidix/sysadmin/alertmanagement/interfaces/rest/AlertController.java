package com.gogidix.sysadmin.alertmanagement.interfaces.rest;
import com.gogidix.sysadmin.alertmanagement.application.service.AlertService;
import com.gogidix.sysadmin.alertmanagement.domain.model.Alert;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/alert-management")
@RequiredArgsConstructor
public class AlertController {
    private final AlertService service;
    @PostMapping
    public ResponseEntity<Alert> create(@RequestBody Alert entity) { return ResponseEntity.ok(service.create(entity)); }
    @GetMapping
    public ResponseEntity<List<Alert>> getAll() { return ResponseEntity.ok(service.getAll()); }
    @GetMapping(("/{id}"))
    public ResponseEntity<Alert> getById(@PathVariable String id) { Alert result = service.getById(id); return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build(); }
    @DeleteMapping(("/{id}"))
    public ResponseEntity<Void> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}

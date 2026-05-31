package com.gogidix.sysadmin.incidentmanagement.interfaces.rest;
import com.gogidix.sysadmin.incidentmanagement.application.service.IncidentService;
import com.gogidix.sysadmin.incidentmanagement.domain.model.Incident;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/incident-management")
@RequiredArgsConstructor
public class IncidentController {
    private final IncidentService service;
    @PostMapping
    public ResponseEntity<Incident> create(@RequestBody Incident entity) { return ResponseEntity.ok(service.create(entity)); }
    @GetMapping
    public ResponseEntity<List<Incident>> getAll() { return ResponseEntity.ok(service.getAll()); }
    @GetMapping(("/{id}"))
    public ResponseEntity<Incident> getById(@PathVariable String id) { Incident result = service.getById(id); return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build(); }
    @DeleteMapping(("/{id}"))
    public ResponseEntity<Void> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}

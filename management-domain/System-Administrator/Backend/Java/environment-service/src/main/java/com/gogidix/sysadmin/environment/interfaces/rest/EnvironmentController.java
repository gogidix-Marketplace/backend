package com.gogidix.sysadmin.environment.interfaces.rest;
import com.gogidix.sysadmin.environment.application.service.EnvironmentService;
import com.gogidix.sysadmin.environment.domain.model.Environment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/environment")
@RequiredArgsConstructor
public class EnvironmentController {
    private final EnvironmentService service;
    @PostMapping
    public ResponseEntity<Environment> create(@RequestBody Environment entity) { return ResponseEntity.ok(service.create(entity)); }
    @GetMapping
    public ResponseEntity<List<Environment>> getAll() { return ResponseEntity.ok(service.getAll()); }
    @GetMapping(("/{id}"))
    public ResponseEntity<Environment> getById(@PathVariable String id) { Environment result = service.getById(id); return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build(); }
    @DeleteMapping(("/{id}"))
    public ResponseEntity<Void> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}

package com.gogidix.sysadmin.deployment.interfaces.rest;
import com.gogidix.sysadmin.deployment.application.service.DeploymentService;
import com.gogidix.sysadmin.deployment.domain.model.Deployment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/deployment")
@RequiredArgsConstructor
public class DeploymentController {
    private final DeploymentService service;
    @PostMapping
    public ResponseEntity<Deployment> create(@RequestBody Deployment entity) { return ResponseEntity.ok(service.create(entity)); }
    @GetMapping
    public ResponseEntity<List<Deployment>> getAll() { return ResponseEntity.ok(service.getAll()); }
    @GetMapping(("/{id}"))
    public ResponseEntity<Deployment> getById(@PathVariable String id) { Deployment result = service.getById(id); return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build(); }
    @DeleteMapping(("/{id}"))
    public ResponseEntity<Void> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}

package com.gogidix.sysadmin.configuration.interfaces.rest;
import com.gogidix.sysadmin.configuration.application.service.ConfigurationService;
import com.gogidix.sysadmin.configuration.domain.model.Configuration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/configuration")
@RequiredArgsConstructor
public class ConfigurationController {
    private final ConfigurationService service;
    @PostMapping
    public ResponseEntity<Configuration> create(@RequestBody Configuration entity) { return ResponseEntity.ok(service.create(entity)); }
    @GetMapping
    public ResponseEntity<List<Configuration>> getAll() { return ResponseEntity.ok(service.getAll()); }
    @GetMapping(("/{id}"))
    public ResponseEntity<Configuration> getById(@PathVariable String id) { Configuration result = service.getById(id); return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build(); }
    @DeleteMapping(("/{id}"))
    public ResponseEntity<Void> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}

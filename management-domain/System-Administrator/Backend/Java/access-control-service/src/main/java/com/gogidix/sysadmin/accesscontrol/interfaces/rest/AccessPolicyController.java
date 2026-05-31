package com.gogidix.sysadmin.accesscontrol.interfaces.rest;
import com.gogidix.sysadmin.accesscontrol.application.service.AccessPolicyService;
import com.gogidix.sysadmin.accesscontrol.domain.model.AccessPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/access-control")
@RequiredArgsConstructor
public class AccessPolicyController {
    private final AccessPolicyService service;
    @PostMapping
    public ResponseEntity<AccessPolicy> create(@RequestBody AccessPolicy entity) { return ResponseEntity.ok(service.create(entity)); }
    @GetMapping
    public ResponseEntity<List<AccessPolicy>> getAll() { return ResponseEntity.ok(service.getAll()); }
    @GetMapping(("/{id}"))
    public ResponseEntity<AccessPolicy> getById(@PathVariable String id) { AccessPolicy result = service.getById(id); return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build(); }
    @DeleteMapping(("/{id}"))
    public ResponseEntity<Void> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}

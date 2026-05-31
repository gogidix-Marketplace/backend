package com.gogidix.hr.globalpolicymanagement.interfaces.rest;

import com.gogidix.hr.globalpolicymanagement.application.service.HRPolicyService;
import com.gogidix.hr.globalpolicymanagement.domain.model.HRPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/policies")
@RequiredArgsConstructor
public class HRPolicyController {
    private final HRPolicyService service;

    @PostMapping
    public ResponseEntity<HRPolicy> create(@RequestBody HRPolicy entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping
    public ResponseEntity<List<HRPolicy>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HRPolicy> getById(@PathVariable String id) {
        HRPolicy result = service.getById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<HRPolicy> update(@PathVariable String id, @RequestBody HRPolicy entity) {
        return ResponseEntity.ok(service.update(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

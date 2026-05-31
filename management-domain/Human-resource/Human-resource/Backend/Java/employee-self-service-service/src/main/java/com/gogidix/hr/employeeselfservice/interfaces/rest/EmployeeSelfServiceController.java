package com.gogidix.hr.employeeselfservice.interfaces.rest;

import com.gogidix.hr.employeeselfservice.application.service.EmployeeSelfService;
import com.gogidix.hr.employeeselfservice.domain.model.EmployeeProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee-self-service/profiles")
@RequiredArgsConstructor
public class EmployeeSelfServiceController {
    private final EmployeeSelfService service;

    @PostMapping
    public ResponseEntity<EmployeeProfile> create(@RequestBody EmployeeProfile entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeProfile>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeProfile> getById(@PathVariable String id) {
        EmployeeProfile result = service.getById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeProfile> update(@PathVariable String id, @RequestBody EmployeeProfile entity) {
        return ResponseEntity.ok(service.update(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

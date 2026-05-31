package com.gogidix.hr.documentmanagement.interfaces.rest;

import com.gogidix.hr.documentmanagement.application.service.DocumentService;
import com.gogidix.hr.documentmanagement.domain.model.HRDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService service;

    @PostMapping
    public ResponseEntity<HRDocument> create(@RequestBody HRDocument entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping
    public ResponseEntity<List<HRDocument>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HRDocument> getById(@PathVariable String id) {
        HRDocument result = service.getById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<HRDocument> update(@PathVariable String id, @RequestBody HRDocument entity) {
        return ResponseEntity.ok(service.update(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

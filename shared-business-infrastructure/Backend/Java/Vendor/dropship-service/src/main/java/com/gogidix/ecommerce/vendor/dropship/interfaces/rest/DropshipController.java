package com.gogidix.ecommerce.vendor.dropship.interfaces.rest;

import com.gogidix.ecommerce.vendor.dropship.domain.model.Dropship;
import com.gogidix.ecommerce.vendor.dropship.domain.service.DropshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dropship")
@RequiredArgsConstructor
@Tag(name = "Dropship order Management", description = "APIs for managing dropship orders")
public class DropshipController {

    private final DropshipService dropshipService;

    @GetMapping
    @Operation(summary = "List all")
    public ResponseEntity<List<Dropship>> listAll() { return ResponseEntity.ok(dropshipService.findAll()); }

    @GetMapping("/{id}")
    @Operation(summary = "Get by ID")
    public ResponseEntity<Dropship> getById(@PathVariable String id) {
        Dropship result = dropshipService.findById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create")
    public ResponseEntity<Dropship> create(@RequestBody Dropship entity) { return ResponseEntity.ok(dropshipService.create(entity)); }

    @PutMapping("/{id}")
    @Operation(summary = "Update")
    public ResponseEntity<Dropship> update(@PathVariable String id, @RequestBody Dropship entity) {
        Dropship result = dropshipService.update(id, entity);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete")
    public ResponseEntity<Void> delete(@PathVariable String id) { dropshipService.delete(id); return ResponseEntity.noContent().build(); }
}
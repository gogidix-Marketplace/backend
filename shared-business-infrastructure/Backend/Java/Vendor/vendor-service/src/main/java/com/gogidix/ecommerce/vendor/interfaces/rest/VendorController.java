package com.gogidix.ecommerce.vendor.interfaces.rest;

import com.gogidix.ecommerce.vendor.domain.model.Vendor;
import com.gogidix.ecommerce.vendor.domain.service.VendorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vendor")
@RequiredArgsConstructor
@Tag(name = "Vendor Management", description = "APIs for managing vendors")
public class VendorController {

    private final VendorService vendorService;

    @GetMapping
    @Operation(summary = "List all")
    public ResponseEntity<List<Vendor>> listAll() { return ResponseEntity.ok(vendorService.findAll()); }

    @GetMapping("/{id}")
    @Operation(summary = "Get by ID")
    public ResponseEntity<Vendor> getById(@PathVariable String id) {
        Vendor result = vendorService.findById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create")
    public ResponseEntity<Vendor> create(@RequestBody Vendor entity) { return ResponseEntity.ok(vendorService.create(entity)); }

    @PutMapping("/{id}")
    @Operation(summary = "Update")
    public ResponseEntity<Vendor> update(@PathVariable String id, @RequestBody Vendor entity) {
        Vendor result = vendorService.update(id, entity);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete")
    public ResponseEntity<Void> delete(@PathVariable String id) { vendorService.delete(id); return ResponseEntity.noContent().build(); }
}
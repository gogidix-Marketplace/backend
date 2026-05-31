package com.gogidix.ecommerce.search.interfaces.rest;

import com.gogidix.ecommerce.search.application.dto.*;
import com.gogidix.ecommerce.search.application.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/searchs")
@Tag(name = "Search Service", description = "APIs for managing searchs")
public class SearchController {

    private final SearchService service;

    public SearchController(SearchService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active searchs")
    public ResponseEntity<List<SearchResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get search by ID")
    public ResponseEntity<SearchResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create search")
    public ResponseEntity<SearchResponse> create(@Valid @RequestBody CreateSearchRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update search")
    public ResponseEntity<SearchResponse> update(@PathVariable String id, @Valid @RequestBody UpdateSearchRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete search")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}

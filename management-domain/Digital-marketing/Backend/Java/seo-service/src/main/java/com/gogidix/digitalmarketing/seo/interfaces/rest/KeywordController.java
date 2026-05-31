package com.gogidix.digitalmarketing.seo.interfaces.rest;

import com.gogidix.digitalmarketing.seo.application.dto.KeywordRequestDto;
import com.gogidix.digitalmarketing.seo.application.dto.KeywordResponseDto;
import com.gogidix.digitalmarketing.seo.application.service.KeywordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seo-keywords")
@RequiredArgsConstructor
@Tag(name = "SEO Management", description = "SEO Keyword Management")
public class KeywordController {

    private final KeywordService service;

    @PostMapping
    @Operation(summary = "Create a new Keyword")
    public ResponseEntity<KeywordResponseDto> create(@RequestBody KeywordRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Keyword by ID")
    public ResponseEntity<KeywordResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all Keywords")
    public ResponseEntity<List<KeywordResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Keyword")
    public ResponseEntity<KeywordResponseDto> update(@PathVariable String id, @RequestBody KeywordRequestDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Keyword")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
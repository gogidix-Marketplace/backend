package com.gogidix.digitalmarketing.brandmanagement.interfaces.rest;

import com.gogidix.digitalmarketing.brandmanagement.application.dto.BrandAssetRequestDto;
import com.gogidix.digitalmarketing.brandmanagement.application.dto.BrandAssetResponseDto;
import com.gogidix.digitalmarketing.brandmanagement.application.service.BrandAssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brand-assets")
@RequiredArgsConstructor
@Tag(name = "Brand Asset Management", description = "Brand Asset Management")
public class BrandAssetController {

    private final BrandAssetService service;

    @PostMapping
    @Operation(summary = "Create a new BrandAsset")
    public ResponseEntity<BrandAssetResponseDto> create(@RequestBody BrandAssetRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get BrandAsset by ID")
    public ResponseEntity<BrandAssetResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all BrandAssets")
    public ResponseEntity<List<BrandAssetResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update BrandAsset")
    public ResponseEntity<BrandAssetResponseDto> update(@PathVariable String id, @RequestBody BrandAssetRequestDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete BrandAsset")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
package com.gogidix.digitalmarketing.marketingautomation.interfaces.rest;

import com.gogidix.digitalmarketing.marketingautomation.application.dto.CampaignRequestDto;
import com.gogidix.digitalmarketing.marketingautomation.application.dto.CampaignResponseDto;
import com.gogidix.digitalmarketing.marketingautomation.application.service.CampaignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/automation-campaigns")
@RequiredArgsConstructor
@Tag(name = "Marketing Automation", description = "Marketing Automation")
public class CampaignController {

    private final CampaignService service;

    @PostMapping
    @Operation(summary = "Create a new Campaign")
    public ResponseEntity<CampaignResponseDto> create(@RequestBody CampaignRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Campaign by ID")
    public ResponseEntity<CampaignResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all Campaigns")
    public ResponseEntity<List<CampaignResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Campaign")
    public ResponseEntity<CampaignResponseDto> update(@PathVariable String id, @RequestBody CampaignRequestDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Campaign")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
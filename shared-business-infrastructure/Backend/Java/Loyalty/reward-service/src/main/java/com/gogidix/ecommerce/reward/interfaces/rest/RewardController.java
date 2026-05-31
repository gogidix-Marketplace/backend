package com.gogidix.ecommerce.reward.interfaces.rest;

import com.gogidix.ecommerce.reward.application.dto.*;
import com.gogidix.ecommerce.reward.application.service.RewardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rewards")
@Tag(name = "Reward Service", description = "APIs for managing rewards")
public class RewardController {

    private final RewardService service;

    public RewardController(RewardService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active rewards")
    public ResponseEntity<List<RewardResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reward by ID")
    public ResponseEntity<RewardResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create reward")
    public ResponseEntity<RewardResponse> create(@Valid @RequestBody CreateRewardRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update reward")
    public ResponseEntity<RewardResponse> update(@PathVariable String id, @Valid @RequestBody UpdateRewardRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete reward")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}

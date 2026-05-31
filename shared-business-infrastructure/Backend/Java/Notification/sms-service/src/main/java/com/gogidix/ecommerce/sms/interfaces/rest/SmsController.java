package com.gogidix.ecommerce.sms.interfaces.rest;

import com.gogidix.ecommerce.sms.application.dto.*;
import com.gogidix.ecommerce.sms.application.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/smss")
@Tag(name = "Sms Service", description = "APIs for managing smss")
public class SmsController {

    private final SmsService service;

    public SmsController(SmsService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active smss")
    public ResponseEntity<List<SmsResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get sms by ID")
    public ResponseEntity<SmsResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create sms")
    public ResponseEntity<SmsResponse> create(@Valid @RequestBody CreateSmsRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update sms")
    public ResponseEntity<SmsResponse> update(@PathVariable String id, @Valid @RequestBody UpdateSmsRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete sms")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}

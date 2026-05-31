package com.gogidix.ecommerce.email.interfaces.rest;

import com.gogidix.ecommerce.email.application.dto.*;
import com.gogidix.ecommerce.email.application.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/emails")
@Tag(name = "Email Service", description = "APIs for managing emails")
public class EmailController {

    private final EmailService service;

    public EmailController(EmailService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active emails")
    public ResponseEntity<List<EmailResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get email by ID")
    public ResponseEntity<EmailResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create email")
    public ResponseEntity<EmailResponse> create(@Valid @RequestBody CreateEmailRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update email")
    public ResponseEntity<EmailResponse> update(@PathVariable String id, @Valid @RequestBody UpdateEmailRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete email")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}

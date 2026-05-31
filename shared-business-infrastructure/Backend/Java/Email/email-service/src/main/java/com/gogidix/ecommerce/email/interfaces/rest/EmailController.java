package com.gogidix.ecommerce.email.interfaces.rest;

import com.gogidix.ecommerce.email.application.dto.CreateEmailRequest;
import com.gogidix.ecommerce.email.application.dto.UpdateEmailRequest;
import com.gogidix.ecommerce.email.application.dto.EmailResponse;
import com.gogidix.ecommerce.email.application.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/emails")
public class EmailController {

    private final EmailService service;

    public EmailController(EmailService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EmailResponse> create(@Valid @RequestBody CreateEmailRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmailResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<EmailResponse>> getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(service.getList(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmailResponse> update(@PathVariable String id, @RequestBody UpdateEmailRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
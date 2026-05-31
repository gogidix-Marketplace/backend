package com.gogidix.ecommerce.payment.interfaces.rest;

import com.gogidix.ecommerce.payment.application.dto.*;
import com.gogidix.ecommerce.payment.application.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payment Service", description = "APIs for managing payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active payments")
    public ResponseEntity<List<PaymentResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<PaymentResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create payment")
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody CreatePaymentRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update payment")
    public ResponseEntity<PaymentResponse> update(@PathVariable String id, @Valid @RequestBody UpdatePaymentRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete payment")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}

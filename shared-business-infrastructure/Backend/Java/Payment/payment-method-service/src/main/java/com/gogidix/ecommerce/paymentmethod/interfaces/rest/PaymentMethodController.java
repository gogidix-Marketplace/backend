package com.gogidix.ecommerce.paymentmethod.interfaces.rest;

import com.gogidix.ecommerce.paymentmethod.application.dto.*;
import com.gogidix.ecommerce.paymentmethod.application.service.PaymentMethodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payment-methods")
@Tag(name = "PaymentMethod Service", description = "APIs for managing payment-methods")
public class PaymentMethodController {

    private final PaymentMethodService service;

    public PaymentMethodController(PaymentMethodService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active payment-methods")
    public ResponseEntity<List<PaymentMethodResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment-method by ID")
    public ResponseEntity<PaymentMethodResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create payment-method")
    public ResponseEntity<PaymentMethodResponse> create(@Valid @RequestBody CreatePaymentMethodRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update payment-method")
    public ResponseEntity<PaymentMethodResponse> update(@PathVariable String id, @Valid @RequestBody UpdatePaymentMethodRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete payment-method")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}

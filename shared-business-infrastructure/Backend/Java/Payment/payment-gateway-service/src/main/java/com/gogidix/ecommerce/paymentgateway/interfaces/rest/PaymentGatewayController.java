package com.gogidix.ecommerce.paymentgateway.interfaces.rest;

import com.gogidix.ecommerce.paymentgateway.application.dto.*;
import com.gogidix.ecommerce.paymentgateway.application.service.PaymentGatewayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payment-gateways")
@Tag(name = "PaymentGateway Service", description = "APIs for managing payment-gateways")
public class PaymentGatewayController {

    private final PaymentGatewayService service;

    public PaymentGatewayController(PaymentGatewayService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active payment-gateways")
    public ResponseEntity<List<PaymentGatewayResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment-gateway by ID")
    public ResponseEntity<PaymentGatewayResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create payment-gateway")
    public ResponseEntity<PaymentGatewayResponse> create(@Valid @RequestBody CreatePaymentGatewayRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update payment-gateway")
    public ResponseEntity<PaymentGatewayResponse> update(@PathVariable String id, @Valid @RequestBody UpdatePaymentGatewayRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete payment-gateway")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}

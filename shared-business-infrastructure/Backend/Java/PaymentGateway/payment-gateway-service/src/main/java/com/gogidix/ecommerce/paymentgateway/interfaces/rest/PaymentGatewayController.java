package com.gogidix.ecommerce.paymentgateway.interfaces.rest;

import com.gogidix.ecommerce.paymentgateway.application.dto.CreatePaymentGatewayRequest;
import com.gogidix.ecommerce.paymentgateway.application.dto.UpdatePaymentGatewayRequest;
import com.gogidix.ecommerce.paymentgateway.application.dto.PaymentGatewayResponse;
import com.gogidix.ecommerce.paymentgateway.application.service.PaymentGatewayService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment-gateways")
public class PaymentGatewayController {

    private final PaymentGatewayService service;

    public PaymentGatewayController(PaymentGatewayService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PaymentGatewayResponse> create(@Valid @RequestBody CreatePaymentGatewayRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentGatewayResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<PaymentGatewayResponse>> getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(service.getList(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentGatewayResponse> update(@PathVariable String id, @RequestBody UpdatePaymentGatewayRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
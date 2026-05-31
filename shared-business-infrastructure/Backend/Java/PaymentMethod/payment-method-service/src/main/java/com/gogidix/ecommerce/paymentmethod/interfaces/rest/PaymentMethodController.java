package com.gogidix.ecommerce.paymentmethod.interfaces.rest;

import com.gogidix.ecommerce.paymentmethod.application.dto.CreatePaymentMethodRequest;
import com.gogidix.ecommerce.paymentmethod.application.dto.UpdatePaymentMethodRequest;
import com.gogidix.ecommerce.paymentmethod.application.dto.PaymentMethodResponse;
import com.gogidix.ecommerce.paymentmethod.application.service.PaymentMethodService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment-methods")
public class PaymentMethodController {

    private final PaymentMethodService service;

    public PaymentMethodController(PaymentMethodService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PaymentMethodResponse> create(@Valid @RequestBody CreatePaymentMethodRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<PaymentMethodResponse>> getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(service.getList(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethodResponse> update(@PathVariable String id, @RequestBody UpdatePaymentMethodRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
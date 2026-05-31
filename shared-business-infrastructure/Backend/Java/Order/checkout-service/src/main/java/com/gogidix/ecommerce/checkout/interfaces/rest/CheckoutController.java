package com.gogidix.ecommerce.checkout.interfaces.rest;

import com.gogidix.ecommerce.checkout.domain.model.Checkout;
import com.gogidix.ecommerce.checkout.domain.service.CheckoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
@Tag(name = "Checkout Management", description = "APIs for managing checkout process")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping
    @Operation(summary = "Initiate checkout", description = "Start the checkout process for a customer's cart")
    public ResponseEntity<Checkout> initiateCheckout(
            @Parameter(description = "Customer ID") @RequestParam String customerId,
            @Parameter(description = "Cart ID") @RequestParam String cartId) {
        return ResponseEntity.ok(checkoutService.initiateCheckout(customerId, cartId));
    }

    @GetMapping("/{checkoutId}")
    @Operation(summary = "Get checkout by ID", description = "Retrieve a specific checkout session by its ID")
    public ResponseEntity<Checkout> getCheckout(
            @Parameter(description = "Checkout ID") @PathVariable String checkoutId) {
        Checkout checkout = checkoutService.getCheckout(checkoutId);
        return checkout != null ? ResponseEntity.ok(checkout) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{checkoutId}/shipping")
    @Operation(summary = "Update shipping address", description = "Update the shipping address for a checkout session")
    public ResponseEntity<Checkout> updateShipping(
            @Parameter(description = "Checkout ID") @PathVariable String checkoutId,
            @RequestBody Checkout.ShippingAddress address) {
        Checkout checkout = checkoutService.updateShipping(checkoutId, address);
        return checkout != null ? ResponseEntity.ok(checkout) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{checkoutId}/payment")
    @Operation(summary = "Update payment method", description = "Update the payment method for a checkout session")
    public ResponseEntity<Checkout> updatePayment(
            @Parameter(description = "Checkout ID") @PathVariable String checkoutId,
            @Parameter(description = "Payment method") @RequestParam String paymentMethod) {
        Checkout checkout = checkoutService.updatePayment(checkoutId, paymentMethod);
        return checkout != null ? ResponseEntity.ok(checkout) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{checkoutId}/complete")
    @Operation(summary = "Complete checkout", description = "Complete the checkout process and create the order")
    public ResponseEntity<Checkout> completeCheckout(
            @Parameter(description = "Checkout ID") @PathVariable String checkoutId,
            @Parameter(description = "Order ID") @RequestParam String orderId) {
        Checkout checkout = checkoutService.completeCheckout(checkoutId, orderId);
        return checkout != null ? ResponseEntity.ok(checkout) : ResponseEntity.notFound().build();
    }
}

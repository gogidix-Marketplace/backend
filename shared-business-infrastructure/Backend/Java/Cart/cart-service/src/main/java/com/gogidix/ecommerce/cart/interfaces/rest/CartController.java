package com.gogidix.ecommerce.cart.interfaces.rest;

import com.gogidix.ecommerce.cart.application.dto.AddItemRequest;
import com.gogidix.ecommerce.cart.application.dto.CartResponse;
import com.gogidix.ecommerce.cart.application.dto.CreateCartRequest;
import com.gogidix.ecommerce.cart.application.dto.UpdateItemQuantityRequest;
import com.gogidix.ecommerce.cart.application.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
@Tag(name = "Cart Management", description = "APIs for managing shopping carts and cart items")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{cartId}")
    @Operation(summary = "Get cart by ID", description = "Retrieve a specific cart by its ID")
    public ResponseEntity<CartResponse> getCart(
            @Parameter(description = "Cart ID") @PathVariable String cartId) {
        return ResponseEntity.ok(cartService.getCart(cartId));
    }

    @GetMapping("/customer/{customerId}/active")
    @Operation(summary = "Get active cart for customer", description = "Retrieve the active cart for a specific customer")
    public ResponseEntity<CartResponse> getActiveCart(
            @Parameter(description = "Customer ID") @PathVariable String customerId) {
        return ResponseEntity.ok(cartService.getActiveCart(customerId));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get all carts for customer", description = "Retrieve all carts associated with a specific customer")
    public ResponseEntity<List<CartResponse>> getCustomerCarts(
            @Parameter(description = "Customer ID") @PathVariable String customerId) {
        return ResponseEntity.ok(cartService.getCustomerCarts(customerId));
    }

    @PostMapping
    @Operation(summary = "Create a new cart", description = "Create a new shopping cart for a customer")
    public ResponseEntity<CartResponse> createCart(
            @Valid @RequestBody CreateCartRequest request) {
        return ResponseEntity.ok(cartService.createCart(request));
    }

    @PostMapping("/{cartId}/items")
    @Operation(summary = "Add item to cart", description = "Add a new item to an existing cart")
    public ResponseEntity<CartResponse> addItem(
            @Parameter(description = "Cart ID") @PathVariable String cartId,
            @Valid @RequestBody AddItemRequest request) {
        return ResponseEntity.ok(cartService.addItem(cartId, request));
    }

    @DeleteMapping("/{cartId}/items/{sku}")
    @Operation(summary = "Remove item from cart", description = "Remove a specific item from the cart by SKU")
    public ResponseEntity<CartResponse> removeItem(
            @Parameter(description = "Cart ID") @PathVariable String cartId,
            @Parameter(description = "Item SKU") @PathVariable String sku) {
        return ResponseEntity.ok(cartService.removeItem(cartId, sku));
    }

    @PutMapping("/{cartId}/items/{sku}/quantity")
    @Operation(summary = "Update item quantity", description = "Update the quantity of a specific item in the cart")
    public ResponseEntity<CartResponse> updateQuantity(
            @Parameter(description = "Cart ID") @PathVariable String cartId,
            @Parameter(description = "Item SKU") @PathVariable String sku,
            @Valid @RequestBody UpdateItemQuantityRequest request) {
        return ResponseEntity.ok(cartService.updateItemQuantity(cartId, sku, request));
    }

    @DeleteMapping("/{cartId}/items")
    @Operation(summary = "Clear cart items", description = "Remove all items from the cart")
    public ResponseEntity<CartResponse> clearCart(
            @Parameter(description = "Cart ID") @PathVariable String cartId) {
        return ResponseEntity.ok(cartService.clearCart(cartId));
    }

    @PostMapping("/{cartId}/lock")
    @Operation(summary = "Lock cart", description = "Lock a cart to prevent further modifications")
    public ResponseEntity<CartResponse> lockCart(
            @Parameter(description = "Cart ID") @PathVariable String cartId) {
        return ResponseEntity.ok(cartService.lockCart(cartId));
    }

    @PostMapping("/{cartId}/convert")
    @Operation(summary = "Convert cart", description = "Convert a cart to an order")
    public ResponseEntity<CartResponse> convertCart(
            @Parameter(description = "Cart ID") @PathVariable String cartId) {
        return ResponseEntity.ok(cartService.convertCart(cartId));
    }
}

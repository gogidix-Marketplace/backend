package com.gogidix.ecommerce.cart.interfaces.rest;

import com.gogidix.ecommerce.cart.application.dto.*;
import com.gogidix.ecommerce.cart.application.mapper.CartMapper;
import com.gogidix.ecommerce.cart.domain.model.Cart;
import com.gogidix.ecommerce.cart.domain.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
@Tag(name = "Cart Management", description = "APIs for managing shopping carts")
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;

    @GetMapping("/{cartId}")
    @Operation(summary = "Get cart by ID", description = "Retrieve a specific cart by its ID")
    public ResponseEntity<CartResponse> getCart(
            @Parameter(description = "Cart ID") @PathVariable String cartId) {
        Cart cart = cartService.getCart(cartId);
        return cart != null ? ResponseEntity.ok(cartMapper.toResponse(cartMapper.toDto(cart))) : ResponseEntity.notFound().build();
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get active cart for customer", description = "Retrieve the active cart for a specific customer")
    public ResponseEntity<CartResponse> getActiveCart(
            @Parameter(description = "Customer ID") @PathVariable String customerId) {
        Cart cart = cartService.getActiveCart(customerId);
        return ResponseEntity.ok(cartMapper.toResponse(cartMapper.toDto(cart)));
    }

    @PostMapping
    @Operation(summary = "Create new cart", description = "Create a new shopping cart")
    public ResponseEntity<CartResponse> createCart(
            @Parameter(description = "Customer ID") @RequestParam String customerId) {
        Cart cart = cartService.createCart(customerId);
        return ResponseEntity.ok(cartMapper.toResponse(cartMapper.toDto(cart)));
    }

    @PostMapping("/{cartId}/items")
    @Operation(summary = "Add item to cart", description = "Add a new item to the specified cart")
    public ResponseEntity<CartResponse> addItem(
            @Parameter(description = "Cart ID") @PathVariable String cartId,
            @RequestBody AddCartItemRequest request) {
        Cart.CartItem item = cartMapper.toCartItem(request);
        Cart cart = cartService.addItem(cartId, item);
        return cart != null ? ResponseEntity.ok(cartMapper.toResponse(cartMapper.toDto(cart))) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{cartId}/items/{itemId}")
    @Operation(summary = "Update cart item quantity", description = "Update the quantity of an item in the cart")
    public ResponseEntity<CartResponse> updateItem(
            @Parameter(description = "Cart ID") @PathVariable String cartId,
            @Parameter(description = "Item ID") @PathVariable String itemId,
            @RequestBody UpdateCartItemRequest request) {
        Cart cart = cartService.updateItem(cartId, itemId, request.quantity());
        return cart != null ? ResponseEntity.ok(cartMapper.toResponse(cartMapper.toDto(cart))) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    @Operation(summary = "Remove item from cart", description = "Remove an item from the specified cart")
    public ResponseEntity<CartResponse> removeItem(
            @Parameter(description = "Cart ID") @PathVariable String cartId,
            @Parameter(description = "Item ID") @PathVariable String itemId) {
        Cart cart = cartService.removeItem(cartId, itemId);
        return cart != null ? ResponseEntity.ok(cartMapper.toResponse(cartMapper.toDto(cart))) : ResponseEntity.notFound().build();
    }
}

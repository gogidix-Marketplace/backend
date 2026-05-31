package com.gogidix.ecommerce.wishlist.interfaces.rest;

import com.gogidix.ecommerce.wishlist.application.dto.*;
import com.gogidix.ecommerce.wishlist.application.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlists")
@Tag(name = "Wishlist Service", description = "APIs for managing wishlists")
public class WishlistController {

    private final WishlistService service;

    public WishlistController(WishlistService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active wishlists")
    public ResponseEntity<List<WishlistResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get wishlist by ID")
    public ResponseEntity<WishlistResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create wishlist")
    public ResponseEntity<WishlistResponse> create(@Valid @RequestBody CreateWishlistRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update wishlist")
    public ResponseEntity<WishlistResponse> update(@PathVariable String id, @Valid @RequestBody UpdateWishlistRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete wishlist")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}

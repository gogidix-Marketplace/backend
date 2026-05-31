package com.gogidix.ecommerce.giftcard.interfaces.rest;

import com.gogidix.ecommerce.giftcard.application.dto.*;
import com.gogidix.ecommerce.giftcard.application.service.GiftCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/gift-cards")
@Tag(name = "GiftCard Service", description = "APIs for managing gift-cards")
public class GiftCardController {

    private final GiftCardService service;

    public GiftCardController(GiftCardService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active gift-cards")
    public ResponseEntity<List<GiftCardResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get gift-card by ID")
    public ResponseEntity<GiftCardResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create gift-card")
    public ResponseEntity<GiftCardResponse> create(@Valid @RequestBody CreateGiftCardRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update gift-card")
    public ResponseEntity<GiftCardResponse> update(@PathVariable String id, @Valid @RequestBody UpdateGiftCardRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete gift-card")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}

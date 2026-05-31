package com.gogidix.ecommerce.coupon.interfaces.rest;

import com.gogidix.ecommerce.coupon.application.dto.*;
import com.gogidix.ecommerce.coupon.application.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/coupons")
@Tag(name = "Coupon Service", description = "APIs for managing coupons")
public class CouponController {

    private final CouponService service;

    public CouponController(CouponService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active coupons")
    public ResponseEntity<List<CouponResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get coupon by ID")
    public ResponseEntity<CouponResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create coupon")
    public ResponseEntity<CouponResponse> create(@Valid @RequestBody CreateCouponRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update coupon")
    public ResponseEntity<CouponResponse> update(@PathVariable String id, @Valid @RequestBody UpdateCouponRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete coupon")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}

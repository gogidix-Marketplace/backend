package com.gogidix.ecommerce.coupon.shared.exception;

public class CouponNotFoundException extends RuntimeException {
    private final String id;
    public CouponNotFoundException(String id) { super("Coupon not found: " + id); this.id = id; }
    public String getId() { return id; }
}

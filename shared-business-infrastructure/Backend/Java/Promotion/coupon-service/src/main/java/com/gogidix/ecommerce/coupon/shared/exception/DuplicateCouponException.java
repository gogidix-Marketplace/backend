package com.gogidix.ecommerce.coupon.shared.exception;

public class DuplicateCouponException extends RuntimeException {
    private final String name;
    public DuplicateCouponException(String name) { super("Duplicate Coupon: " + name); this.name = name; }
    public String getName() { return name; }
}

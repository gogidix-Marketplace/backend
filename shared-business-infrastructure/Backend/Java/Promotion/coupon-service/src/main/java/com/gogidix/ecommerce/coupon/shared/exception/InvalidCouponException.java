package com.gogidix.ecommerce.coupon.shared.exception;

public class InvalidCouponException extends RuntimeException {
    public InvalidCouponException(String message) { super(message); }
    public InvalidCouponException(String message, Throwable cause) { super(message, cause); }
}

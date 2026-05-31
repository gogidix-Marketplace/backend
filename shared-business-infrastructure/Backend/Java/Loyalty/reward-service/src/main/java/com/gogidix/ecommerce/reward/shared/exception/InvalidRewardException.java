package com.gogidix.ecommerce.reward.shared.exception;

public class InvalidRewardException extends RuntimeException {
    public InvalidRewardException(String message) { super(message); }
    public InvalidRewardException(String message, Throwable cause) { super(message, cause); }
}

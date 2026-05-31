package com.gogidix.ecommerce.reward.shared.exception;

public class DuplicateRewardException extends RuntimeException {
    private final String name;
    public DuplicateRewardException(String name) { super("Duplicate Reward: " + name); this.name = name; }
    public String getName() { return name; }
}

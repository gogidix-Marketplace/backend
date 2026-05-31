package com.gogidix.ecommerce.reward.shared.exception;

public class RewardNotFoundException extends RuntimeException {
    private final String id;
    public RewardNotFoundException(String id) { super("Reward not found: " + id); this.id = id; }
    public String getId() { return id; }
}

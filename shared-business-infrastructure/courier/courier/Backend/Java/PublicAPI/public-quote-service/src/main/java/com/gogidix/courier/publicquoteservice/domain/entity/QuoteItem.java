package com.gogidix.courier.publicquoteservice.domain.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

public class QuoteItem {

    @Field("description")
    private String description;

    @Field("quantity")
    private Integer quantity;

    @Field("weight")
    private BigDecimal weight;

    @Field("amount")
    private BigDecimal amount;

    public QuoteItem() {
    }

    public QuoteItem(String description, Integer quantity, BigDecimal weight, BigDecimal amount) {
        this.description = description;
        this.quantity = quantity;
        this.weight = weight;
        this.amount = amount;
    }

    // Getters
    public String getDescription() { return description; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getWeight() { return weight; }
    public BigDecimal getAmount() { return amount; }

    // Setters
    public void setDescription(String description) { this.description = description; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}

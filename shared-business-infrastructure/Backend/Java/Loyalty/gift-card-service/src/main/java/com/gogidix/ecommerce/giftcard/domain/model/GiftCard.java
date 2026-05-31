package com.gogidix.ecommerce.giftcard.domain.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "gift_cards")
public class GiftCard extends BaseEntity {

    private String name;
    private String description;
    private String type;

    @Indexed
    @Field("is_active")
    private Boolean isActive;

    private Integer priority;

    private String cardNumber;
    private java.math.BigDecimal balance;
    private java.math.BigDecimal initialAmount;

    public GiftCard() {
        this.isActive = true;
        this.priority = 0;
    }

    public GiftCard(String tenantId) {
        super(tenantId);
        this.isActive = true;
        this.priority = 0;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public java.math.BigDecimal getBalance() { return balance; }
    public void setBalance(java.math.BigDecimal balance) { this.balance = balance; }
    public java.math.BigDecimal getInitialAmount() { return initialAmount; }
    public void setInitialAmount(java.math.BigDecimal initialAmount) { this.initialAmount = initialAmount; }
}

package com.gogidix.ecommerce.storecredit.domain.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.math.BigDecimal;

@Document(collection = "store_credits")
public class StoreCredit extends BaseEntity {

    private String name;
    private String description;
    private String type;

    @Indexed
    @Field("is_active")
    private Boolean isActive;

    private Integer priority;

    private java.math.BigDecimal creditAmount;
    private java.math.BigDecimal remainingBalance;
    private String reason;

    public StoreCredit() {
        this.isActive = true;
        this.priority = 0;
    }

    public StoreCredit(String tenantId) {
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
    public java.math.BigDecimal getCreditAmount() { return creditAmount; }
    public void setCreditAmount(java.math.BigDecimal creditAmount) { this.creditAmount = creditAmount; }
    public java.math.BigDecimal getRemainingBalance() { return remainingBalance; }
    public void setRemainingBalance(java.math.BigDecimal remainingBalance) { this.remainingBalance = remainingBalance; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}

package com.gogidix.digitalmarketing.budgetmanagement.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "budget_transactions")
public class BudgetTransaction {

    @Id
    private String id;
    private String tenantId;
    private String budgetId;
    private String type;
    private BigDecimal amount;
    private String description;
    private String status;
    private String category;
    private String approvedBy;
    private Instant transactionDate;
    private String referenceNumber;
    private String campaignId;
    private String channelId;
    private String initiatedBy;
    private String currency;
    private BigDecimal exchangeRate;
    private String vendor;
    private String invoiceNumber;
    private String createdBy;
    private Instant createdAt;
    private Map<String, Object> metadata;

    public BudgetTransaction(String tenantId, String budgetId, String type, BigDecimal amount) {
        this.id = UUID.randomUUID().toString();
        this.tenantId = tenantId;
        this.budgetId = budgetId;
        this.type = type;
        this.amount = amount;
        this.status = "PENDING";
        this.transactionDate = Instant.now();
        this.createdAt = Instant.now();
        this.metadata = new HashMap<>();
    }

    public void approve(String userId) {
        this.status = "APPROVED";
        this.approvedBy = userId;
    }

    public void reject() {
        this.status = "REJECTED";
    }

    public boolean isPending() { return "PENDING".equals(status); }
    public boolean isApproved() { return "APPROVED".equals(status); }
    public boolean isRejected() { return "REJECTED".equals(status); }

    public Map<String, Object> getMetadata() {
        if (this.metadata == null) this.metadata = new HashMap<>();
        return this.metadata;
    }
}

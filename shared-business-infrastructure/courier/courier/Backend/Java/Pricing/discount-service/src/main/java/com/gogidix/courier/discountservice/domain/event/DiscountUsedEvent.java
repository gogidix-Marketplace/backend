package com.gogidix.courier.discountservice.domain.event;

import java.math.BigDecimal;

public class DiscountUsedEvent extends DomainEvent {

    private final String discountCodeId;
    private final String code;
    private final String tenantId;
    private final String userId;
    private final String orderId;
    private final BigDecimal discountAmount;

    public DiscountUsedEvent(String discountCodeId, String code, String tenantId,
                            String userId, String orderId, BigDecimal discountAmount) {
        super("DiscountUsed");
        this.discountCodeId = discountCodeId;
        this.code = code;
        this.tenantId = tenantId;
        this.userId = userId;
        this.orderId = orderId;
        this.discountAmount = discountAmount;
    }

    public String getDiscountCodeId() {
        return discountCodeId;
    }

    public String getCode() {
        return code;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getUserId() {
        return userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
}

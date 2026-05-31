package com.gogidix.courier.discountservice.domain.event;

public class DiscountCreatedEvent extends DomainEvent {

    private final String discountCodeId;
    private final String code;
    private final String tenantId;

    public DiscountCreatedEvent(String discountCodeId, String code, String tenantId) {
        super("DiscountCreated");
        this.discountCodeId = discountCodeId;
        this.code = code;
        this.tenantId = tenantId;
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
}

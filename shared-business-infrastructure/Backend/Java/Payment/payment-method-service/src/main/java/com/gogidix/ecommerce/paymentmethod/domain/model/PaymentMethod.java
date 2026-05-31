package com.gogidix.ecommerce.paymentmethod.domain.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "payment_methods")
public class PaymentMethod extends BaseEntity {

    private String name;
    private String description;
    private String type;

    @Indexed
    @Field("is_active")
    private Boolean isActive;

    private Integer priority;

    private String methodName;
    private String methodCode;
    private Boolean enabled;

    public PaymentMethod() {
        this.isActive = true;
        this.priority = 0;
    }

    public PaymentMethod(String tenantId) {
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
    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) { this.methodName = methodName; }
    public String getMethodCode() { return methodCode; }
    public void setMethodCode(String methodCode) { this.methodCode = methodCode; }
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
}

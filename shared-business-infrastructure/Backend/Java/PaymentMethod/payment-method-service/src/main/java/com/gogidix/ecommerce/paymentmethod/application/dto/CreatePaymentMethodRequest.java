package com.gogidix.ecommerce.paymentmethod.application.dto;

import jakarta.validation.constraints.NotBlank;

public class CreatePaymentMethodRequest {

    @NotBlank
    private String name;

    private String description;

    private String type;

    private String methodName;
    private String methodCode;
    private boolean isActive;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getMethodName() { return methodName; }
    public void setMethodName(String methodName) { this.methodName = methodName; }
    public String getMethodCode() { return methodCode; }
    public void setMethodCode(String methodCode) { this.methodCode = methodCode; }
    public boolean getIsActive() { return isActive; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }

}
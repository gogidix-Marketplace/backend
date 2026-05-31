package com.gogidix.ecommerce.paymentgateway.application.dto;

import jakarta.validation.constraints.NotBlank;

public class CreatePaymentGatewayRequest {

    @NotBlank
    private String name;

    private String description;

    private String type;

    private String gatewayName;
    private String gatewayCode;
    private String supportedCurrencies;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getGatewayName() { return gatewayName; }
    public void setGatewayName(String gatewayName) { this.gatewayName = gatewayName; }
    public String getGatewayCode() { return gatewayCode; }
    public void setGatewayCode(String gatewayCode) { this.gatewayCode = gatewayCode; }
    public String getSupportedCurrencies() { return supportedCurrencies; }
    public void setSupportedCurrencies(String supportedCurrencies) { this.supportedCurrencies = supportedCurrencies; }

}
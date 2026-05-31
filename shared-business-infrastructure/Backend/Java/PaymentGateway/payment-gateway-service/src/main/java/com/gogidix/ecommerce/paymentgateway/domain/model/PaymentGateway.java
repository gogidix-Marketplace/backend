package com.gogidix.ecommerce.paymentgateway.domain.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "paymentgateway")
public class PaymentGateway extends BaseEntity {

    private String name;
    private String description;
    private String type;
    private boolean active;
    private int priority;

    private String gatewayName;
    private String gatewayCode;
    private String supportedCurrencies;

    public enum Status {
        ACTIVE,
        INACTIVE
    }
}
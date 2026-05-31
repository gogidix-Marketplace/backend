package com.gogidix.ecommerce.paymentmethod.domain.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "paymentmethod")
public class PaymentMethod extends BaseEntity {

    private String name;
    private String description;
    private String type;
    private boolean active;
    private int priority;

    private String methodName;
    private String methodCode;
    private boolean isActive;

    public enum Status {
        ACTIVE,
        INACTIVE
    }
}
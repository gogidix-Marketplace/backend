package com.gogidix.ecommerce.sms.domain.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "sms")
public class Sms extends BaseEntity {

    private String name;
    private String description;
    private String type;
    private boolean active;
    private int priority;

    private String phoneNumber;
    private String message;
    private String deliveryStatus;

    public enum Status {
        ACTIVE,
        INACTIVE
    }
}
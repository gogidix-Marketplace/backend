package com.gogidix.ecommerce.email.domain.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "email")
public class Email extends BaseEntity {

    private String name;
    private String description;
    private String type;
    private boolean active;
    private int priority;

    private String recipient;
    private String subject;
    private String bodyTemplate;
    private String status;

    public enum Status {
        ACTIVE,
        INACTIVE
    }
}
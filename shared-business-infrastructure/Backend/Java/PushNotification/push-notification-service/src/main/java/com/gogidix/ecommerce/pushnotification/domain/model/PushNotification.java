package com.gogidix.ecommerce.pushnotification.domain.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "pushnotification")
public class PushNotification extends BaseEntity {

    private String name;
    private String description;
    private String type;
    private boolean active;
    private int priority;

    private String deviceToken;
    private String platform;
    private String title;
    private String payload;

    public enum Status {
        ACTIVE,
        INACTIVE
    }
}
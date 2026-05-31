package com.gogidix.aiservices.aipersonalizationservice.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.Objects;

@Value
@Builder
public class Demographics {
    Integer age;
    String gender;
    String location;

    public static Demographics of(Integer age, String gender, String location) {
        if (age != null && (age < 0 || age > 120)) {
            throw new IllegalArgumentException("Age must be between 0 and 120");
        }
        return new Demographics(age, gender, location);
    }
}

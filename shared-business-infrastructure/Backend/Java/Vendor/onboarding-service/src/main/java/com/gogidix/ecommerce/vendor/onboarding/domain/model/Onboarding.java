package com.gogidix.ecommerce.vendor.onboarding.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "onboarding_sessions")
public class Onboarding {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    public enum OnboardingStatus { DRAFT, SUBMITTED, UNDER_REVIEW, APPROVED, REJECTED, SUSPENDED }
    private String vendorId;
    private String name;
    private OnboardingStatus status;
    private java.time.Instant createdAt;
    private java.time.Instant updatedAt;
}
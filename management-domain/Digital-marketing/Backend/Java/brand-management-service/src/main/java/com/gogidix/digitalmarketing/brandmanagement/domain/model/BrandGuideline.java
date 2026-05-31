package com.gogidix.digitalmarketing.brandmanagement.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "brand_guidelines")
public class BrandGuideline {

    @Id
    private String id;
    private String tenantId;
    private String name;
    private String category;
    private String description;
    private String status;
    private String version;
    private String effectiveFrom;
    private String effectiveTo;
    private String approvedBy;
    private Instant approvedAt;
    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;
    private List<GuidelineRule> rules;
    private List<GuidelineExample> examples;
    private List<String> tags;
    private Map<String, Object> metadata;

    public BrandGuideline(String tenantId, String name, String category) {
        this.id = UUID.randomUUID().toString();
        this.tenantId = tenantId;
        this.name = name;
        this.category = category;
        this.status = "DRAFT";
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.rules = new ArrayList<>();
        this.examples = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.metadata = new HashMap<>();
    }

    public void approve(String userId) {
        this.status = "APPROVED";
        this.approvedBy = userId;
        this.approvedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void addRule(String title, String description) {
        if (this.rules == null) {
            this.rules = new ArrayList<>();
        }
        this.rules.add(new GuidelineRule(title, description));
        this.updatedAt = Instant.now();
    }

    public void addExample(String title, String imageUrl, boolean isCorrect) {
        if (this.examples == null) {
            this.examples = new ArrayList<>();
        }
        this.examples.add(new GuidelineExample(title, imageUrl, isCorrect));
        this.updatedAt = Instant.now();
    }

    public List<GuidelineRule> getRules() {
        if (this.rules == null) {
            this.rules = new ArrayList<>();
        }
        return this.rules;
    }

    public List<GuidelineExample> getExamples() {
        if (this.examples == null) {
            this.examples = new ArrayList<>();
        }
        return this.examples;
    }

    public Map<String, Object> getMetadata() {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        return this.metadata;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GuidelineRule {
        private String title;
        private String description;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GuidelineExample {
        private String title;
        private String imageUrl;
        private boolean isCorrect;
    }
}

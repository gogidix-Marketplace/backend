package com.gogidix.aiservices.aiuserprofilingservice.infrastructure.persistence.mongo.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "user_profiles")
public class UserProfileDocument {
    @Id
    private String id;
    private String userId;
    private String tenantId;
    private String email;
    private String displayName;
    private String userType;
    private String status;
    private List<String> interests;
    private List<String> preferences;
    private List<BehavioralPatternDocument> behavioralPatterns;
    private String country;
    private String language;
    private String timezone;
    private Integer age;
    private String gender;
    private String occupation;
    private String industry;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastActiveAt;

    public UserProfileDocument() {
    }

    public UserProfileDocument(String userId, String tenantId, String email, String displayName,
                               String userType, String status, List<String> interests,
                               List<String> preferences, List<BehavioralPatternDocument> behavioralPatterns) {
        this.userId = userId;
        this.tenantId = tenantId;
        this.email = email;
        this.displayName = displayName;
        this.userType = userType;
        this.status = status;
        this.interests = interests;
        this.preferences = preferences;
        this.behavioralPatterns = behavioralPatterns;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.lastActiveAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public List<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
    }

    public List<BehavioralPatternDocument> getBehavioralPatterns() {
        return behavioralPatterns;
    }

    public void setBehavioralPatterns(List<BehavioralPatternDocument> behavioralPatterns) {
        this.behavioralPatterns = behavioralPatterns;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(LocalDateTime lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    public record BehavioralPatternDocument(
            String patternId,
            String patternType,
            String description,
            double confidenceScore,
            int frequency,
            String firstObservedAt,
            String lastObservedAt
    ) {}
}

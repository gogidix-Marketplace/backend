package com.gogidix.courier.partnerportalservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "partners")
public class Partner {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("partner_code")
    private String partnerCode;

    @Field("business_name")
    private String businessName;

    @Field("contact_name")
    private String contactName;

    @Field("email")
    private String email;

    @Field("phone")
    private String phone;

    @Field("address")
    private Address address;

    @Field("status")
    private PartnerStatus status;

    @Field("verification_status")
    private VerificationStatus verificationStatus;

    @Field("service_areas")
    private List<String> serviceAreas;

    @Field("capabilities")
    private List<String> capabilities;

    @Field("rating")
    private Double rating;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    protected Partner() {
    }

    public Partner(String tenantId, String partnerCode, String businessName, String email) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId);
        this.partnerCode = Objects.requireNonNull(partnerCode);
        this.businessName = businessName;
        this.email = email;
        this.status = PartnerStatus.PENDING;
        this.verificationStatus = VerificationStatus.PENDING;
        this.serviceAreas = new ArrayList<>();
        this.capabilities = new ArrayList<>();
        this.rating = 0.0;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void activate() {
        this.status = PartnerStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.status = PartnerStatus.INACTIVE;
        this.updatedAt = Instant.now();
    }

    public void verify() {
        this.verificationStatus = VerificationStatus.VERIFIED;
        this.updatedAt = Instant.now();
    }

    public void rejectVerification(String reason) {
        this.verificationStatus = VerificationStatus.REJECTED;
        this.updatedAt = Instant.now();
    }

    public void updateRating(Double newRating) {
        this.rating = newRating;
        this.updatedAt = Instant.now();
    }

    public enum PartnerStatus {
        PENDING,
        ACTIVE,
        INACTIVE,
        SUSPENDED
    }

    public enum VerificationStatus {
        PENDING,
        VERIFIED,
        REJECTED,
        IN_REVIEW
    }

    public static class Address {
        @Field("street")
        private String street;

        @Field("city")
        private String city;

        @Field("state")
        private String state;

        @Field("postal_code")
        private String postalCode;

        @Field("country")
        private String country;

        @Field("latitude")
        private Double latitude;

        @Field("longitude")
        private Double longitude;

        // Getters and Setters
        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public String getPostalCode() { return postalCode; }
        public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
        public Double getLatitude() { return latitude; }
        public void setLatitude(Double latitude) { this.latitude = latitude; }
        public Double getLongitude() { return longitude; }
        public void setLongitude(Double longitude) { this.longitude = longitude; }
    }

    // Getters
    public String getId() { return id; }
    public String getTenantId() { return tenantId; }
    public String getPartnerCode() { return partnerCode; }
    public String getBusinessName() { return businessName; }
    public String getContactName() { return contactName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Address getAddress() { return address; }
    public PartnerStatus getStatus() { return status; }
    public VerificationStatus getVerificationStatus() { return verificationStatus; }
    public List<String> getServiceAreas() { return serviceAreas; }
    public List<String> getCapabilities() { return capabilities; }
    public Double getRating() { return rating; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    // Setters
    public void setContactName(String contactName) { this.contactName = contactName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(Address address) { this.address = address; }
    public void setServiceAreas(List<String> serviceAreas) { this.serviceAreas = serviceAreas; }
    public void setCapabilities(List<String> capabilities) { this.capabilities = capabilities; }

    protected void setId(String id) { this.id = id; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setPartnerCode(String partnerCode) { this.partnerCode = partnerCode; }
    protected void setBusinessName(String businessName) { this.businessName = businessName; }
    protected void setEmail(String email) { this.email = email; }
    protected void setStatus(PartnerStatus status) { this.status = status; }
    protected void setVerificationStatus(VerificationStatus verificationStatus) { this.verificationStatus = verificationStatus; }
    protected void setRating(Double rating) { this.rating = rating; }
    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    protected void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}

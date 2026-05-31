package com.gogidix.courier.publicbookingservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "bookings")
public class Booking {

    @Id
    private String id;

    @Indexed
    @Field("booking_number")
    private String bookingNumber;

    @Indexed
    @Field("customer_id")
    private String customerId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("items")
    private List<BookingItem> items;

    @Field("pickup_address")
    private Address pickupAddress;

    @Field("delivery_address")
    private Address deliveryAddress;

    @Field("scheduled_pickup_time")
    private Instant scheduledPickupTime;

    @Field("scheduled_delivery_time")
    private Instant scheduledDeliveryTime;

    @Field("actual_pickup_time")
    private Instant actualPickupTime;

    @Field("actual_delivery_time")
    private Instant actualDeliveryTime;

    @Field("status")
    private BookingStatus status;

    @Field("total_amount")
    private BigDecimal totalAmount;

    @Field("currency")
    private String currency;

    @Field("notes")
    private String notes;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    protected Booking() {
    }

    public Booking(String customerId, String tenantId, String bookingNumber) {
        this.id = java.util.UUID.randomUUID().toString();
        this.bookingNumber = bookingNumber;
        this.customerId = Objects.requireNonNull(customerId);
        this.tenantId = Objects.requireNonNull(tenantId);
        this.status = BookingStatus.PENDING;
        this.items = new ArrayList<>();
        this.currency = "USD";
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void confirm() {
        this.status = BookingStatus.CONFIRMED;
        this.updatedAt = Instant.now();
    }

    public void startPickup() {
        this.status = BookingStatus.PICKUP_IN_PROGRESS;
        this.actualPickupTime = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void completePickup() {
        this.status = BookingStatus.IN_TRANSIT;
        this.updatedAt = Instant.now();
    }

    public void startDelivery() {
        this.status = BookingStatus.OUT_FOR_DELIVERY;
        this.updatedAt = Instant.now();
    }

    public void completeDelivery() {
        this.status = BookingStatus.DELIVERED;
        this.actualDeliveryTime = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void cancel(String reason) {
        this.status = BookingStatus.CANCELLED;
        this.notes = reason;
        this.updatedAt = Instant.now();
    }

    public enum BookingStatus {
        PENDING,
        CONFIRMED,
        PICKUP_IN_PROGRESS,
        IN_TRANSIT,
        OUT_FOR_DELIVERY,
        DELIVERED,
        CANCELLED,
        FAILED
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

        @Field("contact_name")
        private String contactName;

        @Field("contact_phone")
        private String contactPhone;

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
        public String getContactName() { return contactName; }
        public void setContactName(String contactName) { this.contactName = contactName; }
        public String getContactPhone() { return contactPhone; }
        public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    }

    // Getters
    public String getId() { return id; }
    public String getBookingNumber() { return bookingNumber; }
    public String getCustomerId() { return customerId; }
    public String getTenantId() { return tenantId; }
    public List<BookingItem> getItems() { return items; }
    public Address getPickupAddress() { return pickupAddress; }
    public Address getDeliveryAddress() { return deliveryAddress; }
    public Instant getScheduledPickupTime() { return scheduledPickupTime; }
    public Instant getScheduledDeliveryTime() { return scheduledDeliveryTime; }
    public Instant getActualPickupTime() { return actualPickupTime; }
    public Instant getActualDeliveryTime() { return actualDeliveryTime; }
    public BookingStatus getStatus() { return status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getCurrency() { return currency; }
    public String getNotes() { return notes; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    // Setters
    public void setItems(List<BookingItem> items) { this.items = items; }
    public void setPickupAddress(Address pickupAddress) { this.pickupAddress = pickupAddress; }
    public void setDeliveryAddress(Address deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public void setScheduledPickupTime(Instant scheduledPickupTime) { this.scheduledPickupTime = scheduledPickupTime; }
    public void setScheduledDeliveryTime(Instant scheduledDeliveryTime) { this.scheduledDeliveryTime = scheduledDeliveryTime; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public void setNotes(String notes) { this.notes = notes; }

    protected void setId(String id) { this.id = id; }
    protected void setBookingNumber(String bookingNumber) { this.bookingNumber = bookingNumber; }
    protected void setCustomerId(String customerId) { this.customerId = customerId; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setStatus(BookingStatus status) { this.status = status; }
    protected void setCurrency(String currency) { this.currency = currency; }
    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    protected void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    protected void setActualPickupTime(Instant actualPickupTime) { this.actualPickupTime = actualPickupTime; }
    protected void setActualDeliveryTime(Instant actualDeliveryTime) { this.actualDeliveryTime = actualDeliveryTime; }
}

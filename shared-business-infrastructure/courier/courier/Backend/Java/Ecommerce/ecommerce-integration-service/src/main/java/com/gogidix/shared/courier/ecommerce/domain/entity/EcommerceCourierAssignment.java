package com.gogidix.shared.courier.ecommerce.domain.entity;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.gogidix.shared.courier.ecommerce.domain.valueobject.AssignmentPriority;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryLeg;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryType;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.VehicleType;

@Document(collection = "ecommerce_courier_assignments")
@CompoundIndex(name = "order_suborder_idx", def = "{'orderId': 1, 'subOrderId': 1}")
@CompoundIndex(name = "zone_status_idx", def = "{'pickupZoneId': 1, 'status': 1}")
public class EcommerceCourierAssignment {

    public enum AssignmentStatus {
        ASSIGNED,
        PICKED_UP,
        AT_HUB,
        IN_TRANSIT,
        DELIVERED,
        CANCELLED,
        FAILED
    }

    @Id
    private String id;

    @Indexed
    @Field("order_id")
    private String orderId;

    @Indexed
    @Field("sub_order_id")
    private String subOrderId;

    @Field("vendor_id")
    private String vendorId;

    @Field("delivery_type")
    private DeliveryType deliveryType;

    @Field("delivery_leg")
    private DeliveryLeg deliveryLeg;

    @Field("pickup_zone_id")
    private String pickupZoneId;

    @Field("pickup_address")
    private String pickupAddress;

    @Field("pickup_latitude")
    private double pickupLatitude;

    @Field("pickup_longitude")
    private double pickupLongitude;

    @Field("pickup_contact_name")
    private String pickupContactName;

    @Field("pickup_contact_phone")
    private String pickupContactPhone;

    @Field("delivery_zone_id")
    private String deliveryZoneId;

    @Field("delivery_address")
    private String deliveryAddress;

    @Field("delivery_latitude")
    private double deliveryLatitude;

    @Field("delivery_longitude")
    private double deliveryLongitude;

    @Field("delivery_contact_name")
    private String deliveryContactName;

    @Field("delivery_contact_phone")
    private String deliveryContactPhone;

    @Field("packages")
    private List<PackageInfo> packages;

    @Field("priority")
    private AssignmentPriority priority;

    @Field("scheduled_for")
    private Instant scheduledFor;

    @Field("requires_hub_processing")
    private boolean requiresHubProcessing;

    @Field("origin_hub_id")
    private String originHubId;

    @Field("destination_hub_id")
    private String destinationHubId;

    @Indexed
    @Field("courier_id")
    private String courierId;

    @Field("courier_name")
    private String courierName;

    @Field("courier_phone")
    private String courierPhone;

    @Field("vehicle_type")
    private VehicleType vehicleType;

    @Indexed
    @Field("tracking_id")
    private String trackingId;

    @Indexed
    @Field("status")
    private AssignmentStatus status;

    @Field("estimated_pickup_time")
    private Instant estimatedPickupTime;

    @Field("estimated_delivery_time")
    private Instant estimatedDeliveryTime;

    @Field("actual_pickup_time")
    private Instant actualPickupTime;

    @Field("actual_delivery_time")
    private Instant actualDeliveryTime;

    @Field("proof_of_delivery")
    private String proofOfDelivery;

    @Field("recipient_name")
    private String recipientName;

    @Field("cancellation_reason")
    private String cancellationReason;

    @Field("metadata")
    private Map<String, String> metadata;

    @CreatedDate
    @Field("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private Instant updatedAt;

    protected EcommerceCourierAssignment() {}

    public EcommerceCourierAssignment(String orderId, String subOrderId, String vendorId,
                                      DeliveryType deliveryType, String pickupZoneId) {
        this.id = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.subOrderId = subOrderId;
        this.vendorId = vendorId;
        this.deliveryType = deliveryType;
        this.pickupZoneId = pickupZoneId;
        this.status = AssignmentStatus.ASSIGNED;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void assignCourier(String courierId, String courierName, String courierPhone,
                              VehicleType vehicleType, String trackingId,
                              Instant estimatedPickupTime, Instant estimatedDeliveryTime) {
        this.courierId = courierId;
        this.courierName = courierName;
        this.courierPhone = courierPhone;
        this.vehicleType = vehicleType;
        this.trackingId = trackingId;
        this.estimatedPickupTime = estimatedPickupTime;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.status = AssignmentStatus.ASSIGNED;
        this.updatedAt = Instant.now();
    }

    public void markPickedUp() {
        this.status = AssignmentStatus.PICKED_UP;
        this.actualPickupTime = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void markAtHub() {
        this.status = AssignmentStatus.AT_HUB;
        this.updatedAt = Instant.now();
    }

    public void markInTransit() {
        this.status = AssignmentStatus.IN_TRANSIT;
        this.updatedAt = Instant.now();
    }

    public void markDelivered(String proofOfDelivery, String recipientName) {
        this.status = AssignmentStatus.DELIVERED;
        this.actualDeliveryTime = Instant.now();
        this.proofOfDelivery = proofOfDelivery;
        this.recipientName = recipientName;
        this.updatedAt = Instant.now();
    }

    public void cancel(String reason) {
        this.status = AssignmentStatus.CANCELLED;
        this.cancellationReason = reason;
        this.updatedAt = Instant.now();
    }

    public void markFailed() {
        this.status = AssignmentStatus.FAILED;
        this.updatedAt = Instant.now();
    }

    public void updateSchedule(Instant newScheduledFor) {
        this.scheduledFor = newScheduledFor;
        this.updatedAt = Instant.now();
    }

    public boolean isTerminal() {
        return status == AssignmentStatus.DELIVERED
                || status == AssignmentStatus.CANCELLED
                || status == AssignmentStatus.FAILED;
    }

    public String getId() { return id; }
    public String getOrderId() { return orderId; }
    public String getSubOrderId() { return subOrderId; }
    public String getVendorId() { return vendorId; }
    public DeliveryType getDeliveryType() { return deliveryType; }
    public DeliveryLeg getDeliveryLeg() { return deliveryLeg; }
    public String getPickupZoneId() { return pickupZoneId; }
    public String getPickupAddress() { return pickupAddress; }
    public double getPickupLatitude() { return pickupLatitude; }
    public double getPickupLongitude() { return pickupLongitude; }
    public String getPickupContactName() { return pickupContactName; }
    public String getPickupContactPhone() { return pickupContactPhone; }
    public String getDeliveryZoneId() { return deliveryZoneId; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public double getDeliveryLatitude() { return deliveryLatitude; }
    public double getDeliveryLongitude() { return deliveryLongitude; }
    public String getDeliveryContactName() { return deliveryContactName; }
    public String getDeliveryContactPhone() { return deliveryContactPhone; }
    public List<PackageInfo> getPackages() { return packages; }
    public AssignmentPriority getPriority() { return priority; }
    public Instant getScheduledFor() { return scheduledFor; }
    public boolean isRequiresHubProcessing() { return requiresHubProcessing; }
    public String getOriginHubId() { return originHubId; }
    public String getDestinationHubId() { return destinationHubId; }
    public String getCourierId() { return courierId; }
    public String getCourierName() { return courierName; }
    public String getCourierPhone() { return courierPhone; }
    public VehicleType getVehicleType() { return vehicleType; }
    public String getTrackingId() { return trackingId; }
    public AssignmentStatus getStatus() { return status; }
    public Instant getEstimatedPickupTime() { return estimatedPickupTime; }
    public Instant getEstimatedDeliveryTime() { return estimatedDeliveryTime; }
    public Instant getActualPickupTime() { return actualPickupTime; }
    public Instant getActualDeliveryTime() { return actualDeliveryTime; }
    public String getProofOfDelivery() { return proofOfDelivery; }
    public String getRecipientName() { return recipientName; }
    public String getCancellationReason() { return cancellationReason; }
    public Map<String, String> getMetadata() { return metadata; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setDeliveryLeg(DeliveryLeg deliveryLeg) { this.deliveryLeg = deliveryLeg; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }
    public void setPickupLatitude(double pickupLatitude) { this.pickupLatitude = pickupLatitude; }
    public void setPickupLongitude(double pickupLongitude) { this.pickupLongitude = pickupLongitude; }
    public void setPickupContactName(String pickupContactName) { this.pickupContactName = pickupContactName; }
    public void setPickupContactPhone(String pickupContactPhone) { this.pickupContactPhone = pickupContactPhone; }
    public void setDeliveryZoneId(String deliveryZoneId) { this.deliveryZoneId = deliveryZoneId; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public void setDeliveryLatitude(double deliveryLatitude) { this.deliveryLatitude = deliveryLatitude; }
    public void setDeliveryLongitude(double deliveryLongitude) { this.deliveryLongitude = deliveryLongitude; }
    public void setDeliveryContactName(String deliveryContactName) { this.deliveryContactName = deliveryContactName; }
    public void setDeliveryContactPhone(String deliveryContactPhone) { this.deliveryContactPhone = deliveryContactPhone; }
    public void setPackages(List<PackageInfo> packages) { this.packages = packages; }
    public void setPriority(AssignmentPriority priority) { this.priority = priority; }
    public void setScheduledFor(Instant scheduledFor) { this.scheduledFor = scheduledFor; }
    public void setRequiresHubProcessing(boolean requiresHubProcessing) { this.requiresHubProcessing = requiresHubProcessing; }
    public void setOriginHubId(String originHubId) { this.originHubId = originHubId; }
    public void setDestinationHubId(String destinationHubId) { this.destinationHubId = destinationHubId; }
    public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EcommerceCourierAssignment that = (EcommerceCourierAssignment) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public static class PackageInfo {
        private String description;
        private double weight;
        private int quantity;
        private Dimensions dimensions;

        protected PackageInfo() {}

        public PackageInfo(String description, double weight, int quantity, Dimensions dimensions) {
            this.description = description;
            this.weight = weight;
            this.quantity = quantity;
            this.dimensions = dimensions;
        }

        public String getDescription() { return description; }
        public double getWeight() { return weight; }
        public int getQuantity() { return quantity; }
        public Dimensions getDimensions() { return dimensions; }
    }

    public static class Dimensions {
        private double length;
        private double width;
        private double height;

        protected Dimensions() {}

        public Dimensions(double length, double width, double height) {
            this.length = length;
            this.width = width;
            this.height = height;
        }

        public double getLength() { return length; }
        public double getWidth() { return width; }
        public double getHeight() { return height; }
    }
}

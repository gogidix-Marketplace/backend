package com.gogidix.shared.courier.ecommerce.domain.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryType;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.HubStageStatus;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.StageCompletionStatus;

@Document(collection = "ecommerce_delivery_tracking")
public class EcommerceDeliveryTracking {

    @Id
    private String id;

    @Indexed
    @Field("order_id")
    private String orderId;

    @Indexed
    @Field("sub_order_id")
    private String subOrderId;

    @Field("delivery_type")
    private DeliveryType deliveryType;

    @Field("current_stage")
    private HubStageStatus currentStage;

    @Field("tracking_id")
    private String trackingId;

    @Field("stages")
    private List<HubStage> stages;

    @Field("eta")
    private Instant eta;

    @Field("courier_name")
    private String courierName;

    @Field("courier_phone")
    private String courierPhone;

    @CreatedDate
    @Field("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private Instant updatedAt;

    protected EcommerceDeliveryTracking() {}

    public EcommerceDeliveryTracking(String orderId, String subOrderId, DeliveryType deliveryType) {
        this.id = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.subOrderId = subOrderId;
        this.deliveryType = deliveryType;
        this.stages = new ArrayList<>();
        this.currentStage = HubStageStatus.PICKUP_ASSIGNED;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void addStage(HubStage stage) {
        this.stages.add(stage);
        this.updatedAt = Instant.now();
    }

    public void updateCurrentStage(HubStageStatus stage) {
        this.currentStage = stage;
        this.updatedAt = Instant.now();
    }

    public void updateEta(Instant eta) {
        this.eta = eta;
        this.updatedAt = Instant.now();
    }

    public void updateCourierInfo(String courierName, String courierPhone) {
        this.courierName = courierName;
        this.courierPhone = courierPhone;
        this.updatedAt = Instant.now();
    }

    public String getId() { return id; }
    public String getOrderId() { return orderId; }
    public String getSubOrderId() { return subOrderId; }
    public DeliveryType getDeliveryType() { return deliveryType; }
    public HubStageStatus getCurrentStage() { return currentStage; }
    public String getTrackingId() { return trackingId; }
    public List<HubStage> getStages() { return stages; }
    public Instant getEta() { return eta; }
    public String getCourierName() { return courierName; }
    public String getCourierPhone() { return courierPhone; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setTrackingId(String trackingId) { this.trackingId = trackingId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EcommerceDeliveryTracking that = (EcommerceDeliveryTracking) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public static class HubStage {
        private HubStageStatus stage;
        private StageCompletionStatus status;
        private String courierName;
        private String courierPhone;
        private String hubName;
        private Instant completedAt;
        private Instant arrivedAt;

        protected HubStage() {}

        public HubStage(HubStageStatus stage, StageCompletionStatus status) {
            this.stage = stage;
            this.status = status;
        }

        public HubStageStatus getStage() { return stage; }
        public StageCompletionStatus getStatus() { return status; }
        public String getCourierName() { return courierName; }
        public String getCourierPhone() { return courierPhone; }
        public String getHubName() { return hubName; }
        public Instant getCompletedAt() { return completedAt; }
        public Instant getArrivedAt() { return arrivedAt; }

        public void setStatus(StageCompletionStatus status) { this.status = status; }
        public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
        public void setArrivedAt(Instant arrivedAt) { this.arrivedAt = arrivedAt; }
        public void setCourierName(String courierName) { this.courierName = courierName; }
        public void setCourierPhone(String courierPhone) { this.courierPhone = courierPhone; }
        public void setHubName(String hubName) { this.hubName = hubName; }
    }
}

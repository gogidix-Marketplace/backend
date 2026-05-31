package com.gogidix.ecommerce.oceanshipping.domain.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "ocean_shipments")
public class OceanShipping extends BaseEntity {

    private String name;
    private String description;
    private String type;

    @Indexed
    @Field("is_active")
    private Boolean isActive;

    private Integer priority;

    private String carrierName;
    private String shippingRoute;
    private String estimatedTransitTime;

    public OceanShipping() {
        this.isActive = true;
        this.priority = 0;
    }

    public OceanShipping(String tenantId) {
        super(tenantId);
        this.isActive = true;
        this.priority = 0;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public String getCarrierName() { return carrierName; }
    public void setCarrierName(String carrierName) { this.carrierName = carrierName; }
    public String getShippingRoute() { return shippingRoute; }
    public void setShippingRoute(String shippingRoute) { this.shippingRoute = shippingRoute; }
    public String getEstimatedTransitTime() { return estimatedTransitTime; }
    public void setEstimatedTransitTime(String estimatedTransitTime) { this.estimatedTransitTime = estimatedTransitTime; }
}

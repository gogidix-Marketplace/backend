package com.gogidix.courier.publicbookingservice.domain.entity;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Objects;

@Document(collection = "booking_items")
public class BookingItem {

    @Field("item_id")
    private String itemId;

    @Field("description")
    private String description;

    @Field("quantity")
    private Integer quantity;

    @Field("weight")
    private BigDecimal weight;

    @Field("weight_unit")
    private String weightUnit;

    @Field("length")
    private BigDecimal length;

    @Field("width")
    private BigDecimal width;

    @Field("height")
    private BigDecimal height;

    @Field("dimension_unit")
    private String dimensionUnit;

    @Field("declared_value")
    private BigDecimal declaredValue;

    @Field("item_type")
    private ItemType itemType;

    @Field("fragile")
    private Boolean fragile;

    @Field("special_instructions")
    private String specialInstructions;

    public BookingItem() {
    }

    public BookingItem(String description, Integer quantity, BigDecimal weight) {
        this.itemId = java.util.UUID.randomUUID().toString();
        this.description = Objects.requireNonNull(description);
        this.quantity = Objects.requireNonNull(quantity);
        this.weight = weight;
        this.weightUnit = "KG";
        this.dimensionUnit = "CM";
        this.fragile = false;
    }

    public enum ItemType {
        DOCUMENT,
        PACKAGE,
        FRAGILE,
        PERISHABLE,
        ELECTRONICS,
        FURNITURE,
        OTHER
    }

    // Getters
    public String getItemId() { return itemId; }
    public String getDescription() { return description; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getWeight() { return weight; }
    public String getWeightUnit() { return weightUnit; }
    public BigDecimal getLength() { return length; }
    public BigDecimal getWidth() { return width; }
    public BigDecimal getHeight() { return height; }
    public String getDimensionUnit() { return dimensionUnit; }
    public BigDecimal getDeclaredValue() { return declaredValue; }
    public ItemType getItemType() { return itemType; }
    public Boolean getFragile() { return fragile; }
    public String getSpecialInstructions() { return specialInstructions; }

    // Setters
    public void setItemId(String itemId) { this.itemId = itemId; }
    public void setDescription(String description) { this.description = description; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }
    public void setWeightUnit(String weightUnit) { this.weightUnit = weightUnit; }
    public void setLength(BigDecimal length) { this.length = length; }
    public void setWidth(BigDecimal width) { this.width = width; }
    public void setHeight(BigDecimal height) { this.height = height; }
    public void setDimensionUnit(String dimensionUnit) { this.dimensionUnit = dimensionUnit; }
    public void setDeclaredValue(BigDecimal declaredValue) { this.declaredValue = declaredValue; }
    public void setItemType(ItemType itemType) { this.itemType = itemType; }
    public void setFragile(Boolean fragile) { this.fragile = fragile; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
}

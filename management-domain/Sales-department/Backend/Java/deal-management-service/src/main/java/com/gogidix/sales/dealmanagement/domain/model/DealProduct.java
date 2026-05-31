package com.gogidix.sales.dealmanagement.domain.model;

import com.gogidix.sales.dealmanagement.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Deal Product Domain Entity
 * Represents products/services included in a deal
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "deal_products")
public class DealProduct extends BaseEntity {

    @Indexed
    private String productId;

    @Indexed
    private String tenantId;

    @Indexed
    private String dealId;

    private String productCode;

    private String productName;

    private String productDescription;

    private String productCategory;

    private String productFamily;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal discountAmount;

    private BigDecimal discountPercentage;

    private BigDecimal totalPrice;

    private String currency;

    private String serviceType;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer durationMonths;

    private Boolean isRecurring;

    private BillingCycle billingCycle;

    private String sku;

    private BigDecimal taxRate;

    private BigDecimal taxAmount;

    private BigDecimal margin;

    private BigDecimal marginPercentage;

    private String costCenter;

    private String notes;

    private Integer lineItemOrder;

    public enum BillingCycle {
        MONTHLY,
        QUARTERLY,
        ANNUALLY,
        ONE_TIME,
        CUSTOM
    }

    /**
     * Creates a new deal product
     */
    public static DealProduct create(String tenantId, String dealId, String productName,
                                      Integer quantity, BigDecimal unitPrice, String currency) {
        DealProduct product = DealProduct.builder()
                .tenantId(tenantId)
                .dealId(dealId)
                .productName(productName)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .currency(currency)
                .build();

        product.calculateTotalPrice();

        return product;
    }

    /**
     * Calculates the total price for this product line item
     */
    public void calculateTotalPrice() {
        if (this.quantity == null || this.unitPrice == null) {
            this.totalPrice = BigDecimal.ZERO;
            return;
        }

        BigDecimal subtotal = this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));

        if (this.discountAmount != null && this.discountAmount.compareTo(BigDecimal.ZERO) > 0) {
            subtotal = subtotal.subtract(this.discountAmount);
        } else if (this.discountPercentage != null && this.discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discount = subtotal.multiply(this.discountPercentage)
                    .divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
            subtotal = subtotal.subtract(discount);
        }

        this.totalPrice = subtotal;
    }

    /**
     * Applies a discount to this product
     */
    public void applyDiscount(BigDecimal discountAmount, BigDecimal discountPercentage) {
        this.discountAmount = discountAmount;
        this.discountPercentage = discountPercentage;
        calculateTotalPrice();
    }

    /**
     * Updates the quantity and recalculates
     */
    public void updateQuantity(Integer newQuantity) {
        if (newQuantity == null || newQuantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.quantity = newQuantity;
        calculateTotalPrice();
    }

    /**
     * Updates the unit price and recalculates
     */
    public void updateUnitPrice(BigDecimal newUnitPrice) {
        if (newUnitPrice == null || newUnitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Unit price must be positive");
        }
        this.unitPrice = newUnitPrice;
        calculateTotalPrice();
    }

    /**
     * Calculates margin
     */
    public void calculateMargin(BigDecimal cost) {
        if (this.totalPrice == null || cost == null) {
            return;
        }

        this.margin = this.totalPrice.subtract(cost);

        if (this.totalPrice.compareTo(BigDecimal.ZERO) > 0) {
            this.marginPercentage = this.margin
                    .multiply(BigDecimal.valueOf(100))
                    .divide(this.totalPrice, 2, java.math.RoundingMode.HALF_UP);
        }
    }

    /**
     * Gets the annual recurring revenue value for this product
     */
    public BigDecimal getARR() {
        if (!Boolean.TRUE.equals(this.isRecurring)) {
            return BigDecimal.ZERO;
        }

        if (this.billingCycle == BillingCycle.MONTHLY) {
            return this.totalPrice.multiply(BigDecimal.valueOf(12));
        } else if (this.billingCycle == BillingCycle.QUARTERLY) {
            return this.totalPrice.multiply(BigDecimal.valueOf(4));
        } else if (this.billingCycle == BillingCycle.ANNUALLY) {
            return this.totalPrice;
        }

        return BigDecimal.ZERO;
    }

    /**
     * Gets the monthly recurring revenue value for this product
     */
    public BigDecimal getMRR() {
        if (!Boolean.TRUE.equals(this.isRecurring)) {
            return BigDecimal.ZERO;
        }

        if (this.billingCycle == BillingCycle.MONTHLY) {
            return this.totalPrice;
        } else if (this.billingCycle == BillingCycle.QUARTERLY) {
            return this.totalPrice.divide(BigDecimal.valueOf(3), 2, java.math.RoundingMode.HALF_UP);
        } else if (this.billingCycle == BillingCycle.ANNUALLY) {
            return this.totalPrice.divide(BigDecimal.valueOf(12), 2, java.math.RoundingMode.HALF_UP);
        }

        return BigDecimal.ZERO;
    }
}

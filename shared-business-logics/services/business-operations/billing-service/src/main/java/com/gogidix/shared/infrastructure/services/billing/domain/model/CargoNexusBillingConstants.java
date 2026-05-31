package com.gogidix.shared.infrastructure.services.billing.domain.model;

import java.math.BigDecimal;

public final class CargoNexusBillingConstants {

    private CargoNexusBillingConstants() {}

    public static final String PRODUCT_PMS = "PMS";
    public static final String PRODUCT_AGO = "AGO";
    public static final String PRODUCT_DPK = "DPK";
    public static final String PRODUCT_LPG = "LPG";
    public static final String PRODUCT_AVIATION_FUEL = "AVIATION_FUEL";

    public static final String BILLING_TYPE_PER_LITRE = "PER_LITRE";
    public static final String BILLING_TYPE_FLAT_RATE = "FLAT_RATE";
    public static final String BILLING_TYPE_VOLUME_TIERED = "VOLUME_TIERED";

    public static final String LINE_ITEM_LOADING_FEE = "LOADING_FEE";
    public static final String LINE_ITEM_DELIVERY_FEE = "DELIVERY_FEE";
    public static final String LINE_ITEM_INSURANCE_PREMIUM = "INSURANCE_PREMIUM";
    public static final String LINE_ITEM_BANK_PROCESSING = "BANK_PROCESSING_FEE";
    public static final String LINE_ITEM_DEPOT_SERVICE = "DEPOT_SERVICE_FEE";
    public static final String LINE_ITEM_CUSTODY_TRACKING = "CUSTODY_TRACKING_FEE";
    public static final String LINE_ITEM_IOT_MONITORING = "IOT_MONITORING_FEE";

    public static BigDecimal calculatePerLitrePrice(BigDecimal volumeLitres, BigDecimal pricePerLitre) {
        return volumeLitres.multiply(pricePerLitre);
    }

    public static BigDecimal calculateLoadingFee(BigDecimal volumeLitres, BigDecimal feePerLitre) {
        return volumeLitres.multiply(feePerLitre);
    }

    public static BigDecimal calculateInsurancePremium(BigDecimal cargoValue, BigDecimal premiumRate) {
        return cargoValue.multiply(premiumRate);
    }
}

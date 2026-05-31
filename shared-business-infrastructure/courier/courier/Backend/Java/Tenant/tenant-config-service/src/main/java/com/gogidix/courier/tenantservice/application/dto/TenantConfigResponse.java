package com.gogidix.courier.tenantservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

/**
 * Response DTO for tenant configuration.
 */
@Schema(description="Response DTO for tenant configuration")
public record TenantConfigResponse(

        @JsonProperty("max_drivers")
        @Schema(description="Maximum number of drivers", example = "100")
        Integer maxDrivers,

        @JsonProperty("max_daily_orders")
        @Schema(description="Maximum daily orders", example = "1000")
        Integer maxDailyOrders,

        @JsonProperty("max_daily_deliveries")
        @Schema(description="Maximum daily deliveries", example = "1000")
        Integer maxDailyDeliveries,

        @JsonProperty("service_radius_km")
        @Schema(description="Service radius in kilometers", example = "50.0")
        Double serviceRadiusKm,

        @JsonProperty("default_currency")
        @Schema(description="Default currency code", example = "USD")
        String defaultCurrency,

        @JsonProperty("timezone")
        @Schema(description="Tenant timezone", example = "UTC")
        String timezone,

        @JsonProperty("delivery_fee_enabled")
        @Schema(description="Enable delivery fee", example = "true")
        Boolean deliveryFeeEnabled,

        @JsonProperty("delivery_fee_amount")
        @Schema(description="Delivery fee amount", example = "5.0")
        Double deliveryFeeAmount,

        @JsonProperty("tax_rate")
        @Schema(description="Tax rate (0-1)", example = "0.1")
        Double taxRate,

        @JsonProperty("auto_accept_orders")
        @Schema(description="Auto accept orders", example = "false")
        Boolean autoAcceptOrders,

        @JsonProperty("require_driver_verification")
        @Schema(description="Require driver verification", example = "true")
        Boolean requireDriverVerification,

        @JsonProperty("custom_settings")
        @Schema(description="Custom settings")
        Map<String, Object> customSettings
) {
}

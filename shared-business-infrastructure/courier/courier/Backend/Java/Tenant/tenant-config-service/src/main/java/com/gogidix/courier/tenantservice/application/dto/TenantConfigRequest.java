package com.gogidix.courier.tenantservice.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.Map;

/**
 * Request DTO for updating tenant configuration.
 */
@Schema(description="Request DTO for tenant configuration update")
public record TenantConfigRequest(

        @JsonProperty("max_drivers")
        @Schema(description="Maximum number of drivers", example = "100")
        @Min(value = 1, message = "maxDrivers must be at least 1")
        Integer maxDrivers,

        @JsonProperty("max_daily_orders")
        @Schema(description="Maximum daily orders", example = "1000")
        @Min(value = 1, message = "maxDailyOrders must be at least 1")
        Integer maxDailyOrders,

        @JsonProperty("max_daily_deliveries")
        @Schema(description="Maximum daily deliveries", example = "1000")
        @Min(value = 1, message = "maxDailyDeliveries must be at least 1")
        Integer maxDailyDeliveries,

        @JsonProperty("service_radius_km")
        @Schema(description="Service radius in kilometers", example = "50.0")
        @Min(value = 1, message = "serviceRadiusKm must be at least 1")
        Double serviceRadiusKm,

        @JsonProperty("default_currency")
        @Schema(description="Default currency code", example = "USD")
        @NotBlank(message = "defaultCurrency is required")
        String defaultCurrency,

        @JsonProperty("timezone")
        @Schema(description="Tenant timezone", example = "UTC")
        String timezone,

        @JsonProperty("delivery_fee_enabled")
        @Schema(description="Enable delivery fee", example = "true")
        Boolean deliveryFeeEnabled,

        @JsonProperty("delivery_fee_amount")
        @Schema(description="Delivery fee amount", example = "5.0")
        @Min(value = 0, message = "deliveryFeeAmount must be non-negative")
        Double deliveryFeeAmount,

        @JsonProperty("tax_rate")
        @Schema(description="Tax rate (0-1)", example = "0.1")
        @Min(value = 0, message = "taxRate must be non-negative")
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

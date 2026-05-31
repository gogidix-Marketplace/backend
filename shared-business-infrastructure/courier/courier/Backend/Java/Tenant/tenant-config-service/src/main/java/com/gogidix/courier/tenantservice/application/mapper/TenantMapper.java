package com.gogidix.courier.tenantservice.application.mapper;

import com.gogidix.courier.tenantservice.application.dto.TenantConfigRequest;
import com.gogidix.courier.tenantservice.application.dto.TenantConfigResponse;
import com.gogidix.courier.tenantservice.application.dto.TenantRequest;
import com.gogidix.courier.tenantservice.application.dto.TenantResponse;
import com.gogidix.courier.tenantservice.domain.entity.Tenant;
import com.gogidix.courier.tenantservice.domain.entity.TenantConfig;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between domain entities and DTOs.
 */
@Component
public class TenantMapper {

    /**
     * Convert TenantRequest to Tenant domain entity.
     */
    public Tenant toEntity(TenantRequest request) {
        if (request == null) {
            return null;
        }
        return new Tenant(
                request.tenantId(),
                request.name(),
                request.description()
        );
    }

    /**
     * Convert Tenant domain entity to TenantResponse DTO.
     */
    public TenantResponse toResponseDto(Tenant tenant) {
        if (tenant == null) {
            return null;
        }
        return new TenantResponse(
                tenant.getId(),
                tenant.getTenantId(),
                tenant.getName(),
                tenant.getDescription(),
                tenant.getStatus(),
                toConfigResponseDto(tenant.getConfig()),
                tenant.getCreatedAt(),
                tenant.getUpdatedAt(),
                tenant.getActivatedAt(),
                tenant.getDeactivatedAt()
        );
    }

    /**
     * Convert TenantConfigRequest to TenantConfig value object.
     */
    public TenantConfig toConfigEntity(TenantConfigRequest request) {
        if (request == null) {
            return new TenantConfig();
        }

        TenantConfig config = new TenantConfig();

        if (request.maxDrivers() != null) {
            config.setMaxDrivers(request.maxDrivers());
        }
        if (request.maxDailyOrders() != null) {
            config.setMaxDailyOrders(request.maxDailyOrders());
        }
        if (request.maxDailyDeliveries() != null) {
            config.setMaxDailyDeliveries(request.maxDailyDeliveries());
        }
        if (request.serviceRadiusKm() != null) {
            config.setServiceRadiusKm(request.serviceRadiusKm());
        }
        if (request.defaultCurrency() != null) {
            config.setDefaultCurrency(request.defaultCurrency());
        }
        if (request.timezone() != null) {
            config.setTimezone(request.timezone());
        }
        if (request.deliveryFeeEnabled() != null) {
            config.setDeliveryFeeEnabled(request.deliveryFeeEnabled());
        }
        if (request.deliveryFeeAmount() != null) {
            config.setDeliveryFeeAmount(request.deliveryFeeAmount());
        }
        if (request.taxRate() != null) {
            config.setTaxRate(request.taxRate());
        }
        if (request.autoAcceptOrders() != null) {
            config.setAutoAcceptOrders(request.autoAcceptOrders());
        }
        if (request.requireDriverVerification() != null) {
            config.setRequireDriverVerification(request.requireDriverVerification());
        }
        if (request.customSettings() != null) {
            config.setCustomSettings(request.customSettings());
        }

        return config;
    }

    /**
     * Convert TenantConfig value object to TenantConfigResponse DTO.
     */
    public TenantConfigResponse toConfigResponseDto(TenantConfig config) {
        if (config == null) {
            return null;
        }
        return new TenantConfigResponse(
                config.getMaxDrivers(),
                config.getMaxDailyOrders(),
                config.getMaxDailyDeliveries(),
                config.getServiceRadiusKm(),
                config.getDefaultCurrency(),
                config.getTimezone(),
                config.getDeliveryFeeEnabled(),
                config.getDeliveryFeeAmount(),
                config.getTaxRate(),
                config.getAutoAcceptOrders(),
                config.getRequireDriverVerification(),
                config.getCustomSettings()
        );
    }

    /**
     * Update tenant entity from request DTO.
     */
    public void updateEntityFromRequest(Tenant tenant, TenantRequest request) {
        if (tenant == null || request == null) {
            return;
        }
        tenant.updateDetails(request.name(), request.description());
    }

    /**
     * Update tenant config from request DTO.
     */
    public void updateConfigFromRequest(TenantConfig config, TenantConfigRequest request) {
        if (config == null || request == null) {
            return;
        }

        if (request.maxDrivers() != null) {
            config.setMaxDrivers(request.maxDrivers());
        }
        if (request.maxDailyOrders() != null) {
            config.setMaxDailyOrders(request.maxDailyOrders());
        }
        if (request.maxDailyDeliveries() != null) {
            config.setMaxDailyDeliveries(request.maxDailyDeliveries());
        }
        if (request.serviceRadiusKm() != null) {
            config.setServiceRadiusKm(request.serviceRadiusKm());
        }
        if (request.defaultCurrency() != null) {
            config.setDefaultCurrency(request.defaultCurrency());
        }
        if (request.timezone() != null) {
            config.setTimezone(request.timezone());
        }
        if (request.deliveryFeeEnabled() != null) {
            config.setDeliveryFeeEnabled(request.deliveryFeeEnabled());
        }
        if (request.deliveryFeeAmount() != null) {
            config.setDeliveryFeeAmount(request.deliveryFeeAmount());
        }
        if (request.taxRate() != null) {
            config.setTaxRate(request.taxRate());
        }
        if (request.autoAcceptOrders() != null) {
            config.setAutoAcceptOrders(request.autoAcceptOrders());
        }
        if (request.requireDriverVerification() != null) {
            config.setRequireDriverVerification(request.requireDriverVerification());
        }
        if (request.customSettings() != null) {
            config.setCustomSettings(request.customSettings());
        }
    }
}

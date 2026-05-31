package com.gogidix.shared.warehousing.tenant.application.mapper;

import com.gogidix.shared.warehousing.tenant.application.command.CreateTenantCommand;
import com.gogidix.shared.warehousing.tenant.application.command.UpdateTenantCommand;
import com.gogidix.shared.warehousing.tenant.domain.entity.Tenant;
import com.gogidix.shared.warehousing.tenant.interfaces.rest.dto.TenantRequest;
import com.gogidix.shared.warehousing.tenant.interfaces.rest.dto.TenantResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Tenant DTO Mapper
 *
 * Maps between entities, commands, and DTOs
 */
@Component
public class TenantDtoMapper {

    public Tenant toEntity(CreateTenantCommand command) {
        return Tenant.builder()
                .tenantId(command.getTenantId())
                .tenantName(command.getTenantName())
                .tenantType(command.getTenantType())
                .storageModel(command.getStorageModel())
                .businessRules(command.getBusinessRules())
                .pricingModel(command.getPricingModel())
                .integrationEndpoints(command.getIntegrationEndpoints())
                .complianceRequirements(command.getComplianceRequirements())
                .sla(command.getSla())
                .status(command.getStatus())
                .build();
    }

    public Tenant toEntity(TenantRequest request) {
        return Tenant.builder()
                .tenantId(request.getTenantId())
                .tenantName(request.getTenantName())
                .tenantType(request.getTenantType())
                .storageModel(request.getStorageModel())
                .businessRules(request.getBusinessRules())
                .pricingModel(request.getPricingModel())
                .integrationEndpoints(request.getIntegrationEndpoints())
                .complianceRequirements(request.getComplianceRequirements())
                .sla(request.getSla())
                .status(request.getStatus())
                .build();
    }

    public void updateEntity(Tenant tenant, UpdateTenantCommand command) {
        if (command.getTenantName() != null) {
            tenant.setTenantName(command.getTenantName());
        }
        if (command.getTenantType() != null) {
            tenant.setTenantType(command.getTenantType());
        }
        if (command.getStorageModel() != null) {
            tenant.setStorageModel(command.getStorageModel());
        }
        if (command.getBusinessRules() != null) {
            tenant.setBusinessRules(command.getBusinessRules());
        }
        if (command.getPricingModel() != null) {
            tenant.setPricingModel(command.getPricingModel());
        }
        if (command.getIntegrationEndpoints() != null) {
            tenant.setIntegrationEndpoints(command.getIntegrationEndpoints());
        }
        if (command.getComplianceRequirements() != null) {
            tenant.setComplianceRequirements(command.getComplianceRequirements());
        }
        if (command.getSla() != null) {
            tenant.setSla(command.getSla());
        }
        if (command.getStatus() != null) {
            tenant.setStatus(command.getStatus());
        }
    }

    public TenantResponse toDTO(Tenant tenant) {
        return TenantResponse.builder()
                .id(tenant.getId())
                .tenantId(tenant.getTenantId())
                .tenantName(tenant.getTenantName())
                .tenantType(tenant.getTenantType())
                .storageModel(tenant.getStorageModel())
                .businessRules(tenant.getBusinessRules())
                .pricingModel(tenant.getPricingModel())
                .integrationEndpoints(tenant.getIntegrationEndpoints())
                .complianceRequirements(tenant.getComplianceRequirements())
                .sla(tenant.getSla())
                .status(tenant.getStatus())
                .createdAt(tenant.getCreatedAt())
                .updatedAt(tenant.getUpdatedAt())
                .build();
    }

    public List<TenantResponse> toDTOList(List<Tenant> tenants) {
        return tenants.stream()
                .map(this::toDTO)
                .toList();
    }
}

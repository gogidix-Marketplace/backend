package com.gogidix.courier.discountservice.application.mapper;

import com.gogidix.courier.discountservice.application.dto.CreateDiscountRequest;
import com.gogidix.courier.discountservice.application.dto.DiscountResponse;
import com.gogidix.courier.discountservice.domain.entity.DiscountCode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DiscountMapper {

    public DiscountCode toEntity(String tenantId, CreateDiscountRequest request) {
        DiscountCode code = new DiscountCode(
                tenantId,
                request.code(),
                convertDiscountType(request.discountType()),
                request.discountValue()
        );

        code.setDescription(request.description());
        code.setMaxDiscountAmount(request.maxDiscountAmount());
        code.setMinOrderAmount(request.minOrderAmount());
        code.setStartDate(request.startDate());
        code.setEndDate(request.endDate());
        code.setMaxUses(request.maxUses());
        code.setMaxUsesPerUser(request.maxUsesPerUser());

        if (request.applicableZones() != null && !request.applicableZones().isEmpty()) {
            code.setApplicableZones(String.join(",", request.applicableZones()));
        }

        if (request.applicableServices() != null && !request.applicableServices().isEmpty()) {
            code.setApplicableServices(String.join(",", request.applicableServices()));
        }

        return code;
    }

    public DiscountResponse toResponseDto(DiscountCode entity) {
        return new DiscountResponse(
                entity.getId(),
                entity.getTenantId(),
                entity.getCode(),
                entity.getDescription(),
                entity.getDiscountType(),
                entity.getDiscountValue(),
                entity.getMaxDiscountAmount(),
                entity.getMinOrderAmount(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getMaxUses(),
                entity.getCurrentUses(),
                entity.getMaxUsesPerUser(),
                entity.getStatus(),
                entity.getApplicableZones(),
                entity.getApplicableServices(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public List<DiscountResponse> toResponseDtoList(List<DiscountCode> entities) {
        return entities.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private DiscountCode.DiscountType convertDiscountType(CreateDiscountRequest.DiscountTypeDto dto) {
        return DiscountCode.DiscountType.valueOf(dto.name());
    }
}

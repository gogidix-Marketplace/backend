package com.gogidix.ecommerce.vendor.onboarding.application.mapper;

import com.gogidix.ecommerce.vendor.onboarding.application.dto.CreateOnboardingRequest;
import com.gogidix.ecommerce.vendor.onboarding.application.dto.OnboardingResponse;
import com.gogidix.ecommerce.vendor.onboarding.domain.model.Onboarding;
import com.gogidix.ecommerce.vendor.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Component;

@Component
public class OnboardingMapper {

    public OnboardingResponse toResponse(Onboarding entity) {
        return new OnboardingResponse(entity.getId(), entity.getTenantId(), null, entity.getCreatedAt(), entity.getUpdatedAt());
    }

    public Onboarding toEntity(CreateOnboardingRequest request) {
        Onboarding entity = new Onboarding();
        entity.setTenantId(RequestContextHolder.getTenantId());
        entity.setName(request.name());
        return entity;
    }
}
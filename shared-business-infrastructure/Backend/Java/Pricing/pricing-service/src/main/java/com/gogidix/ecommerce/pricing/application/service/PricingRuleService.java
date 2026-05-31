package com.gogidix.ecommerce.pricing.application.service;

import com.gogidix.ecommerce.pricing.application.dto.*;
import com.gogidix.ecommerce.pricing.application.mapper.PricingRuleMapper;
import com.gogidix.ecommerce.pricing.domain.model.PricingRule;
import com.gogidix.ecommerce.pricing.domain.repository.PricingRuleRepository;
import com.gogidix.ecommerce.pricing.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PricingRuleService {

    private final PricingRuleRepository pricingRuleRepository;
    private final PricingRuleMapper pricingRuleMapper;

    public List<PricingRuleResponse> getAllActivePricingRules() {
        String tenantId = RequestContextHolder.getTenantId();
        return pricingRuleRepository.findByTenantIdAndIsActiveOrderByPriority(tenantId, true)
                .stream()
                .map(pricingRuleMapper::toPricingRuleResponse)
                .toList();
    }

    public PricingRuleResponse getPricingRuleById(String id) {
        PricingRule rule = pricingRuleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pricing rule not found"));
        return pricingRuleMapper.toPricingRuleResponse(rule);
    }

    public PricingRuleResponse getPricingRuleByCode(String ruleCode) {
        String tenantId = RequestContextHolder.getTenantId();
        PricingRule rule = pricingRuleRepository.findByTenantIdAndRuleCode(tenantId, ruleCode);
        if (rule == null) {
            throw new IllegalArgumentException("Pricing rule not found with code: " + ruleCode);
        }
        return pricingRuleMapper.toPricingRuleResponse(rule);
    }

    public List<PricingRuleResponse> getPricingRulesByProduct(String productId) {
        String tenantId = RequestContextHolder.getTenantId();
        return pricingRuleRepository.findByTenantIdAndProductIdAndIsActive(tenantId, productId, true)
                .stream()
                .map(pricingRuleMapper::toPricingRuleResponse)
                .toList();
    }

    public List<PricingRuleResponse> getPricingRulesByCategory(String categoryId) {
        String tenantId = RequestContextHolder.getTenantId();
        return pricingRuleRepository.findByTenantIdAndCategoryIdAndIsActive(tenantId, categoryId, true)
                .stream()
                .map(pricingRuleMapper::toPricingRuleResponse)
                .toList();
    }

    public List<PricingRuleResponse> getPricingRulesByType(PricingRule.PricingType type) {
        String tenantId = RequestContextHolder.getTenantId();
        return pricingRuleRepository.findByTenantIdAndTypeAndIsActive(tenantId, type, true)
                .stream()
                .map(pricingRuleMapper::toPricingRuleResponse)
                .toList();
    }

    @Transactional
    public PricingRuleResponse createPricingRule(CreatePricingRuleRequest request) {
        String tenantId = RequestContextHolder.getTenantId();

        if (pricingRuleRepository.existsByTenantIdAndRuleCode(tenantId, request.ruleCode())) {
            throw new IllegalArgumentException("Pricing rule with code already exists: " + request.ruleCode());
        }

        PricingRule rule = pricingRuleMapper.toPricingRule(request);
        rule.setTenantId(tenantId);

        PricingRule saved = pricingRuleRepository.save(rule);
        return pricingRuleMapper.toPricingRuleResponse(saved);
    }

    @Transactional
    public PricingRuleResponse updatePricingRule(String id, UpdatePricingRuleRequest request) {
        PricingRule existing = pricingRuleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pricing rule not found"));

        pricingRuleMapper.updatePricingRuleFromRequest(existing, request);
        existing.updateTimestamp();

        PricingRule saved = pricingRuleRepository.save(existing);
        return pricingRuleMapper.toPricingRuleResponse(saved);
    }

    @Transactional
    public void deletePricingRule(String id) {
        PricingRule rule = pricingRuleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pricing rule not found"));
        pricingRuleRepository.deleteById(id);
    }
}

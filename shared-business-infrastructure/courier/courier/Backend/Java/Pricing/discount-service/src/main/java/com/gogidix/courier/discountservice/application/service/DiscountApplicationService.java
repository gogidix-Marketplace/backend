package com.gogidix.courier.discountservice.application.service;

import com.gogidix.courier.discountservice.application.dto.*;
import com.gogidix.courier.discountservice.application.mapper.DiscountMapper;
import com.gogidix.courier.discountservice.domain.entity.DiscountCode;
import com.gogidix.courier.discountservice.domain.entity.DiscountUsage;
import com.gogidix.courier.discountservice.domain.entity.DiscountValidation;
import com.gogidix.courier.discountservice.domain.event.DiscountCreatedEvent;
import com.gogidix.courier.discountservice.domain.event.DiscountUsedEvent;
import com.gogidix.courier.discountservice.domain.repository.*;
import com.gogidix.courier.discountservice.shared.exception.NotFoundException;
import com.gogidix.courier.discountservice.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DiscountApplicationService {

    private static final Logger log = LoggerFactory.getLogger(DiscountApplicationService.class);

    private final DiscountCodeRepository codeRepository;
    private final DiscountUsageRepository usageRepository;
    private final DiscountValidationRepository validationRepository;
    private final DiscountMapper mapper;
    private final DiscountEventPublisher eventPublisher;

    public DiscountApplicationService(
            DiscountCodeRepository codeRepository,
            DiscountUsageRepository usageRepository,
            DiscountValidationRepository validationRepository,
            DiscountMapper mapper,
            DiscountEventPublisher eventPublisher) {
        this.codeRepository = codeRepository;
        this.usageRepository = usageRepository;
        this.validationRepository = validationRepository;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @CacheEvict(value = "discountCodes", allEntries = true)
    public DiscountResponse createDiscount(String tenantId, CreateDiscountRequest request) {
        log.info("Creating discount code: {} for tenant: {}", request.code(), tenantId);

        if (codeRepository.existsByTenantIdAndCode(tenantId, request.code().toUpperCase())) {
            throw new ValidationException("Discount code already exists");
        }

        DiscountCode discountCode = mapper.toEntity(tenantId, request);
        DiscountCode saved = codeRepository.save(discountCode);

        eventPublisher.publish(new DiscountCreatedEvent(
                saved.getId(),
                saved.getCode(),
                saved.getTenantId()
        ));

        log.info("Discount code created: {}", saved.getId());
        return mapper.toResponseDto(saved);
    }

    @Cacheable(value = "discountCodes", key = "#id")
    public DiscountResponse getDiscount(String id) {
        DiscountCode discountCode = codeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("DiscountCode", id));
        return mapper.toResponseDto(discountCode);
    }

    public List<DiscountResponse> listDiscounts(String tenantId) {
        List<DiscountCode> codes = codeRepository.findByTenantId(tenantId);
        return mapper.toResponseDtoList(codes);
    }

    public List<DiscountResponse> listActiveDiscounts(String tenantId) {
        List<DiscountCode> codes = codeRepository.findByTenantIdAndStatus(
                tenantId, DiscountCode.DiscountStatus.ACTIVE);
        return mapper.toResponseDtoList(codes);
    }

    public ValidationResponse validateCode(String tenantId, ValidateCodeRequest request) {
        log.debug("Validating discount code: {} for user: {}", request.discountCode(), request.userId());

        // Check cache first
        validationRepository.findByTenantIdAndDiscountCodeAndUserId(tenantId, request.discountCode(), request.userId())
                .filter(validation -> !validation.isExpired())
                .ifPresent(cached -> {
                    log.debug("Using cached validation result");
                });

        DiscountCode discountCode = codeRepository.findByTenantIdAndCode(tenantId, request.discountCode().toUpperCase())
                .orElse(null);

        if (discountCode == null) {
            return ValidationResponse.invalid("Discount code not found", request.discountCode());
        }

        // Validate
        if (!discountCode.isValid()) {
            if (discountCode.getStatus() == DiscountCode.DiscountStatus.INACTIVE) {
                return ValidationResponse.invalid("Discount code is inactive", request.discountCode());
            }
            if (discountCode.isExpired()) {
                return ValidationResponse.invalid("Discount code has expired", request.discountCode());
            }
            if (discountCode.isMaxUsageReached()) {
                return ValidationResponse.invalid("Discount code has reached maximum usage", request.discountCode());
            }
            if (discountCode.isNotYetStarted()) {
                return ValidationResponse.invalid("Discount code is not yet active", request.discountCode());
            }
        }

        // Check user usage limit
        long userUsageCount = usageRepository.countByTenantIdAndDiscountCodeAndUserId(
                tenantId, discountCode.getCode(), request.userId());
        if (!discountCode.canBeUsedByUser(request.userId(), (int) userUsageCount)) {
            return ValidationResponse.invalid("You have reached the maximum usage limit for this code", request.discountCode());
        }

        // Check minimum order amount
        if (!discountCode.meetsMinimumOrder(request.orderAmount())) {
            return ValidationResponse.invalid(
                    "Minimum order amount of " + discountCode.getMinOrderAmount() + " is required",
                    request.discountCode()
            );
        }

        // Check zone applicability
        if (request.zoneId() != null && !discountCode.isApplicableToZone(request.zoneId())) {
            return ValidationResponse.invalid("Discount code is not applicable for this zone", request.discountCode());
        }

        // Check service applicability
        if (request.serviceType() != null && !discountCode.isApplicableToService(request.serviceType())) {
            return ValidationResponse.invalid("Discount code is not applicable for this service", request.discountCode());
        }

        BigDecimal discountAmount = discountCode.calculateDiscount(request.orderAmount());
        BigDecimal finalAmount = request.orderAmount().subtract(discountAmount);

        // Cache validation result
        DiscountValidation validation = new DiscountValidation(
                tenantId, request.discountCode(), request.userId(),
                request.orderAmount(), request.zoneId(), request.serviceType()
        );
        validation.setIsValid(true);
        validation.setDiscountAmount(discountAmount);
        validationRepository.save(validation);

        return ValidationResponse.valid(discountAmount, finalAmount, discountCode.getCode());
    }

    @Transactional
    @CacheEvict(value = "discountCodes", allEntries = true)
    public ApplyDiscountResponse applyDiscount(String tenantId, ApplyDiscountRequest request) {
        log.info("Applying discount code: {} for order: {}", request.discountCode(), request.orderId());

        // First validate
        ValidateCodeRequest validateRequest = new ValidateCodeRequest(
                request.discountCode(),
                request.userId(),
                request.orderAmount(),
                request.zoneId(),
                request.serviceType()
        );
        ValidationResponse validation = validateCode(tenantId, validateRequest);

        if (!validation.valid()) {
            throw new ValidationException(validation.rejectionReason());
        }

        DiscountCode discountCode = codeRepository.findByTenantIdAndCode(tenantId, request.discountCode().toUpperCase())
                .orElseThrow(() -> new NotFoundException("DiscountCode", request.discountCode()));

        BigDecimal discountAmount = discountCode.calculateDiscount(request.orderAmount());
        BigDecimal finalAmount = request.orderAmount().subtract(discountAmount);

        // Record usage
        DiscountUsage usage = new DiscountUsage(
                tenantId,
                discountCode.getCode(),
                request.userId(),
                request.orderId(),
                request.orderAmount(),
                discountAmount,
                finalAmount
        );
        usage.setZoneId(request.zoneId());
        usage.setServiceType(request.serviceType());
        usageRepository.save(usage);

        // Update discount code usage
        discountCode.recordUsage();
        codeRepository.save(discountCode);

        // Publish event
        eventPublisher.publish(new DiscountUsedEvent(
                discountCode.getId(),
                discountCode.getCode(),
                tenantId,
                request.userId(),
                request.orderId(),
                discountAmount
        ));

        log.info("Discount applied: {} amount: {}", discountCode.getCode(), discountAmount);

        return new ApplyDiscountResponse(
                usage.getId(),
                discountCode.getCode(),
                request.orderAmount(),
                discountAmount,
                finalAmount,
                "Discount applied successfully"
        );
    }

    @Transactional
    @CacheEvict(value = "discountCodes", allEntries = true)
    public void deleteDiscount(String id) {
        log.info("Deleting discount: {}", id);
        if (!codeRepository.findById(id).isPresent()) {
            throw new NotFoundException("DiscountCode", id);
        }
        codeRepository.deleteById(id);
        log.info("Discount deleted: {}", id);
    }

    @Transactional
    @CacheEvict(value = "discountCodes", allEntries = true)
    public DiscountResponse activateDiscount(String id) {
        DiscountCode discountCode = codeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("DiscountCode", id));
        discountCode.activate();
        return mapper.toResponseDto(codeRepository.save(discountCode));
    }

    @Transactional
    @CacheEvict(value = "discountCodes", allEntries = true)
    public DiscountResponse deactivateDiscount(String id) {
        DiscountCode discountCode = codeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("DiscountCode", id));
        discountCode.deactivate();
        return mapper.toResponseDto(codeRepository.save(discountCode));
    }
}

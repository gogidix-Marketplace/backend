package com.gogidix.sales.dealmanagement.application.service;

import com.gogidix.sales.dealmanagement.application.dto.response.DealResponseDto;
import com.gogidix.sales.dealmanagement.domain.model.Competitor;
import com.gogidix.sales.dealmanagement.domain.model.Deal;
import com.gogidix.sales.dealmanagement.domain.model.DealActivity;
import com.gogidix.sales.dealmanagement.domain.model.DealProduct;
import com.gogidix.sales.dealmanagement.domain.repository.*;
import com.gogidix.sales.dealmanagement.shared.exception.NotFoundException;
import com.gogidix.sales.dealmanagement.shared.requestcontext.RequestContextHolder;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Deal Query Service
 * Handles all read operations for deals
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DealQueryService {

    private final DealRepository dealRepository;
    private final DealProductRepository productRepository;
    private final DealActivityRepository activityRepository;
    private final CompetitorRepository competitorRepository;

    public Deal getById(String dealId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching deal: {} for tenant: {}", dealId, tenantId);

        return dealRepository.findByDealIdAndTenantId(dealId, tenantId)
                .orElseThrow(() -> new NotFoundException("Deal", dealId));
    }

    public List<Deal> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all deals for tenant: {}", tenantId);

        return dealRepository.findAllByTenantId(tenantId);
    }

    public Page<Deal> getDeals(Pageable pageable) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching deals for tenant: {} with pagination", tenantId);

        List<Deal> deals = dealRepository.findAllByTenantId(tenantId);

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), deals.size());

        List<Deal> pagedDeals = deals.subList(start, end);

        return new PageImpl<>(pagedDeals, pageable, deals.size());
    }

    public List<Deal> getDealsByStatus(Deal.DealStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching deals for tenant: {} with status: {}", tenantId, status);

        return dealRepository.findByTenantIdAndStatus(tenantId, status);
    }

    public List<Deal> getDealsByStage(Deal.DealStage stage) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching deals for tenant: {} with stage: {}", tenantId, stage);

        return dealRepository.findByTenantIdAndStage(tenantId, stage);
    }

    public List<Deal> getDealsByOwner(String ownerId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching deals for tenant: {} owned by: {}", tenantId, ownerId);

        return dealRepository.findByOwnerIdAndTenantId(ownerId, tenantId);
    }

    public List<Deal> getDealsByAccount(String accountId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching deals for tenant: {} for account: {}", tenantId, accountId);

        return dealRepository.findByAccountIdAndTenantId(accountId, tenantId);
    }

    public List<Deal> getDealsClosingBetween(LocalDate startDate, LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching deals for tenant: {} closing between: {} and {}", tenantId, startDate, endDate);

        return dealRepository.findByTenantIdAndExpectedCloseDateBetween(tenantId, startDate, endDate);
    }

    public List<Deal> getPipelineDeals() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching pipeline deals for tenant: {}", tenantId);

        List<Deal.DealStage> pipelineStages = Arrays.asList(
                Deal.DealStage.LEAD,
                Deal.DealStage.QUALIFIED,
                Deal.DealStage.PROPOSAL,
                Deal.DealStage.NEGOTIATION,
                Deal.DealStage.VERBAL_COMMIT
        );

        return dealRepository.findByTenantIdAndStageIn(tenantId, pipelineStages);
    }

    public List<Deal> getForecastDeals(LocalDate asOfDate) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching forecast deals for tenant: {} as of: {}", tenantId, asOfDate);

        return dealRepository.findForecastDeals(tenantId, asOfDate != null ? asOfDate : LocalDate.now());
    }

    public List<Deal> searchDeals(String searchTerm) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Searching deals for tenant: {} with term: {}", tenantId, searchTerm);

        return dealRepository.searchDeals(tenantId, searchTerm);
    }

    /**
     * Get pipeline view organized by stage
     */
    public Map<Deal.DealStage, DealResponseDto.PipelineViewDto> getPipelineView() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching pipeline view for tenant: {}", tenantId);

        List<Deal> pipelineDeals = getPipelineDeals();

        Map<Deal.DealStage, List<Deal>> dealsByStage = pipelineDeals.stream()
                .collect(Collectors.groupingBy(Deal::getStage));

        Map<Deal.DealStage, DealResponseDto.PipelineViewDto> pipelineView = new LinkedHashMap<>();

        for (Deal.DealStage stage : Deal.DealStage.values()) {
            if (stage == Deal.DealStage.CLOSED_WON || stage == Deal.DealStage.CLOSED_LOST) {
                continue;
            }

            List<Deal> stageDeals = dealsByStage.getOrDefault(stage, Collections.emptyList());

            BigDecimal totalAmount = stageDeals.stream()
                    .map(Deal::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal weightedAmount = stageDeals.stream()
                    .map(Deal::getWeightedAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            List<DealResponseDto.DealSummaryDto> dealSummaries = stageDeals.stream()
                    .map(this::toSummaryDto)
                    .collect(Collectors.toList());

            pipelineView.put(stage, DealResponseDto.PipelineViewDto.builder()
                    .stage(mapStage(stage))
                    .order(stage.getOrder())
                    .probability(stage.getDefaultProbability())
                    .totalAmount(totalAmount)
                    .weightedAmount(weightedAmount)
                    .dealCount(stageDeals.size())
                    .deals(dealSummaries)
                    .build());
        }

        return pipelineView;
    }

    /**
     * Get forecast summary
     */
    public ForecastSummary getForecastSummary(LocalDate startDate, LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching forecast summary for tenant: {} from: {} to: {}", tenantId, startDate, endDate);

        List<Deal> openDeals = dealRepository.findByTenantIdAndStatus(tenantId, Deal.DealStatus.OPEN);

        BigDecimal totalPipeline = openDeals.stream()
                .map(Deal::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal weightedForecast = openDeals.stream()
                .map(Deal::getWeightedAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Deal> wonDeals = dealRepository.findByTenantIdAndStatus(tenantId, Deal.DealStatus.WON);

        BigDecimal wonAmount = wonDeals.stream()
                .filter(d -> d.getActualCloseDate() != null &&
                        (startDate == null || !d.getActualCloseDate().isBefore(startDate)) &&
                        (endDate == null || !d.getActualCloseDate().isAfter(endDate)))
                .map(Deal::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer wonDealsCount = (int) wonDeals.stream()
                .filter(d -> d.getActualCloseDate() != null &&
                        (startDate == null || !d.getActualCloseDate().isBefore(startDate)) &&
                        (endDate == null || !d.getActualCloseDate().isAfter(endDate)))
                .count();

        double winRate = 0.0;
        long totalClosed = wonDeals.stream()
                .filter(d -> d.getActualCloseDate() != null &&
                        (startDate == null || !d.getActualCloseDate().isBefore(startDate)) &&
                        (endDate == null || !d.getActualCloseDate().isAfter(endDate)))
                .count() +
                dealRepository.findByTenantIdAndStatus(tenantId, Deal.DealStatus.LOST).stream()
                        .filter(d -> d.getActualCloseDate() != null &&
                                (startDate == null || !d.getActualCloseDate().isBefore(startDate)) &&
                                (endDate == null || !d.getActualCloseDate().isAfter(endDate)))
                        .count();

        if (totalClosed > 0) {
            winRate = (double) wonDealsCount / totalClosed * 100;
        }

        return ForecastSummary.builder()
                .totalPipeline(totalPipeline)
                .weightedForecast(weightedForecast)
                .bestCase(totalPipeline.multiply(BigDecimal.valueOf(0.75)))
                .worstCase(weightedForecast.multiply(BigDecimal.valueOf(0.5)))
                .openDeals(openDeals.size())
                .wonDeals(wonDealsCount)
                .wonAmount(wonAmount)
                .winRate(winRate)
                .build();
    }

    /**
     * Get deal summary statistics
     */
    public DealSummary getDealSummary() {
        String tenantId = RequestContextHolder.getTenantId();

        long openDeals = dealRepository.countByTenantIdAndStatus(tenantId, Deal.DealStatus.OPEN);
        long wonDeals = dealRepository.countByTenantIdAndStatus(tenantId, Deal.DealStatus.WON);
        long lostDeals = dealRepository.countByTenantIdAndStatus(tenantId, Deal.DealStatus.LOST);

        return DealSummary.builder()
                .openDeals((int) openDeals)
                .wonDeals((int) wonDeals)
                .lostDeals((int) lostDeals)
                .totalDeals((int) (openDeals + wonDeals + lostDeals))
                .build();
    }

    public DealResponseDto toResponseDto(Deal deal) {
        // Load related entities
        List<DealProduct> products = productRepository.findByDealIdAndTenantId(deal.getDealId(), deal.getTenantId());
        List<DealActivity> activities = activityRepository.findByDealIdAndTenantIdOrderByActivityDateDesc(deal.getDealId(), deal.getTenantId());
        List<Competitor> competitors = competitorRepository.findByDealIdAndTenantId(deal.getDealId(), deal.getTenantId());

        return DealResponseDto.builder()
                .id(deal.getId())
                .dealId(deal.getDealId())
                .tenantId(deal.getTenantId())
                .dealName(deal.getDealName())
                .dealCode(deal.getDealCode())
                .stage(mapStage(deal.getStage()))
                .stageOrder(deal.getStageOrder())
                .probability(deal.getProbability())
                .amount(deal.getAmount())
                .weightedAmount(deal.getWeightedAmount())
                .currency(deal.getCurrency())
                .accountId(deal.getAccountId())
                .accountName(deal.getAccountName())
                .contactId(deal.getContactId())
                .contactName(deal.getContactName())
                .ownerId(deal.getOwnerId())
                .ownerName(deal.getOwnerName())
                .teamMemberIds(deal.getTeamMemberIds())
                .priority(mapPriority(deal.getPriority()))
                .status(mapStatus(deal.getStatus()))
                .approvalStatus(mapApprovalStatus(deal.getApprovalStatus()))
                .approvalRequired(deal.getApprovalRequired())
                .approvedBy(deal.getApprovedBy())
                .approvedAt(deal.getApprovedAt())
                .expectedCloseDate(deal.getExpectedCloseDate())
                .actualCloseDate(deal.getActualCloseDate())
                .createdDate(deal.getCreatedDate())
                .dealDurationDays(deal.getDealDurationDays())
                .source(deal.getSource())
                .campaign(deal.getCampaign())
                .leadSource(deal.getLeadSource())
                .description(deal.getDescription())
                .nextSteps(deal.getNextSteps())
                .lossReason(deal.getLossReason())
                .lossReasonDetails(deal.getLossReasonDetails())
                .products(products.stream().map(this::toProductDto).collect(Collectors.toList()))
                .activities(activities.stream().map(this::toActivityDto).collect(Collectors.toList()))
                .competitors(competitors.stream().map(this::toCompetitorDto).collect(Collectors.toList()))
                .tags(deal.getTagList())
                .region(deal.getRegion())
                .industry(deal.getIndustry())
                .segment(deal.getSegment())
                .territory(deal.getTerritory())
                .contractType(deal.getContractType())
                .contractLengthMonths(deal.getContractLengthMonths())
                .renewal(deal.getRenewal())
                .renewalDealId(deal.getRenewalDealId())
                .discountAmount(deal.getDiscountAmount())
                .discountPercentage(deal.getDiscountPercentage())
                .createdAt(deal.getCreatedAt())
                .updatedAt(deal.getUpdatedAt())
                .build();
    }

    private DealResponseDto.DealSummaryDto toSummaryDto(Deal deal) {
        int daysInStage = 0;
        if (deal.getCreatedAt() != null) {
            daysInStage = (int) ChronoUnit.DAYS.between(deal.getCreatedAt().atZone(java.time.ZoneId.systemDefault()).toLocalDate(), LocalDate.now());
        }

        return DealResponseDto.DealSummaryDto.builder()
                .dealId(deal.getDealId())
                .dealName(deal.getDealName())
                .dealCode(deal.getDealCode())
                .amount(deal.getAmount())
                .weightedAmount(deal.getWeightedAmount())
                .currency(deal.getCurrency())
                .stage(mapStage(deal.getStage()))
                .probability(deal.getProbability())
                .priority(mapPriority(deal.getPriority()))
                .ownerName(deal.getOwnerName())
                .accountName(deal.getAccountName())
                .expectedCloseDate(deal.getExpectedCloseDate())
                .daysInStage(daysInStage)
                .build();
    }

    private DealResponseDto.ProductDto toProductDto(DealProduct product) {
        return DealResponseDto.ProductDto.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productCode(product.getProductCode())
                .productCategory(product.getProductCategory())
                .quantity(product.getQuantity())
                .unitPrice(product.getUnitPrice())
                .totalPrice(product.getTotalPrice())
                .currency(product.getCurrency())
                .isRecurring(product.getIsRecurring())
                .billingCycle(product.getBillingCycle())
                .build();
    }

    private DealResponseDto.ActivityDto toActivityDto(DealActivity activity) {
        return DealResponseDto.ActivityDto.builder()
                .activityId(activity.getActivityId())
                .activityType(activity.getActivityType())
                .subject(activity.getSubject())
                .description(activity.getDescription())
                .userName(activity.getUserName())
                .activityDate(activity.getActivityDate())
                .isCompleted(activity.getIsCompleted())
                .build();
    }

    private DealResponseDto.CompetitorDto toCompetitorDto(Competitor competitor) {
        return DealResponseDto.CompetitorDto.builder()
                .competitorId(competitor.getCompetitorId())
                .competitorName(competitor.getCompetitorName())
                .strength(competitor.getStrength())
                .threat(competitor.getThreat())
                .probabilityOfWin(competitor.getProbabilityOfWin())
                .competingProduct(competitor.getCompetingProduct())
                .build();
    }

    private DealResponseDto.DealStageDto mapStage(Deal.DealStage stage) {
        return stage != null ? DealResponseDto.DealStageDto.valueOf(stage.name()) : null;
    }

    private DealResponseDto.DealPriorityDto mapPriority(Deal.DealPriority priority) {
        return priority != null ? DealResponseDto.DealPriorityDto.valueOf(priority.name()) : null;
    }

    private DealResponseDto.DealStatusDto mapStatus(Deal.DealStatus status) {
        return status != null ? DealResponseDto.DealStatusDto.valueOf(status.name()) : null;
    }

    private DealResponseDto.ApprovalStatusDto mapApprovalStatus(Deal.ApprovalStatus status) {
        return status != null ? DealResponseDto.ApprovalStatusDto.valueOf(status.name()) : null;
    }

    @lombok.Data
    @Builder
    public static class ForecastSummary {
        private BigDecimal totalPipeline;
        private BigDecimal weightedForecast;
        private BigDecimal bestCase;
        private BigDecimal worstCase;
        private Integer openDeals;
        private Integer wonDeals;
        private BigDecimal wonAmount;
        private Double winRate;
    }

    @lombok.Data
    @Builder
    public static class DealSummary {
        private Integer totalDeals;
        private Integer openDeals;
        private Integer wonDeals;
        private Integer lostDeals;
    }
}

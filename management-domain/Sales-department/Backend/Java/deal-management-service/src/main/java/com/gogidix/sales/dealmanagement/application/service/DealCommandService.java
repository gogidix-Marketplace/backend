package com.gogidix.sales.dealmanagement.application.service;

import com.gogidix.sales.dealmanagement.domain.model.*;
import com.gogidix.sales.dealmanagement.domain.port.in.DealCommand;
import com.gogidix.sales.dealmanagement.domain.port.out.EventPublisher;
import com.gogidix.sales.dealmanagement.domain.repository.*;
import com.gogidix.sales.dealmanagement.shared.exception.ConflictException;
import com.gogidix.sales.dealmanagement.shared.exception.NotFoundException;
import com.gogidix.sales.dealmanagement.shared.exception.ValidationException;
import com.gogidix.sales.dealmanagement.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Deal Command Service
 * Handles all write operations for deals
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DealCommandService {

    private final DealRepository dealRepository;
    private final DealProductRepository productRepository;
    private final DealActivityRepository activityRepository;
    private final CompetitorRepository competitorRepository;
    private final EventPublisher eventPublisher;

    @Value("${deal-management-service.approval.thresholds.manager-approval-above:50000}")
    private BigDecimal managerApprovalThreshold;

    @Value("${deal-management-service.approval.thresholds.executive-approval-above:250000}")
    private BigDecimal executiveApprovalThreshold;

    @Transactional
    public Deal create(DealCommand.CreateDealCommand command) {
        log.info("Creating deal for tenant: {}, name: {}", command.getTenantId(), command.getDealName());

        validateDealAmount(command.getAmount());

        Deal.DealStage initialStage = command.getStage() != null ? command.getStage() : Deal.DealStage.LEAD;

        Deal deal = Deal.create(
                command.getTenantId(),
                command.getDealName(),
                command.getAccountId(),
                command.getAmount(),
                command.getCurrency(),
                initialStage,
                command.getOwnerId(),
                command.getExpectedCloseDate()
        );

        // Set additional fields
        deal.setAccountName(command.getAccountName());
        deal.setContactId(command.getContactId());
        deal.setContactName(command.getContactName());
        deal.setOwnerName(command.getOwnerName());
        deal.setPriority(command.getPriority() != null ? command.getPriority() : Deal.DealPriority.MEDIUM);
        deal.setSource(command.getSource());
        deal.setCampaign(command.getCampaign());
        deal.setLeadSource(command.getLeadSource());
        deal.setDescription(command.getDescription());
        deal.setNextSteps(command.getNextSteps());
        deal.setRegion(command.getRegion());
        deal.setIndustry(command.getIndustry());
        deal.setSegment(command.getSegment());
        deal.setTerritory(command.getTerritory());
        deal.setContractType(command.getContractType());
        deal.setContractLengthMonths(command.getContractLengthMonths());
        deal.setRenewal(command.getRenewal());
        deal.setRenewalDealId(command.getRenewalDealId());

        // Set team members
        if (command.getTeamMemberIds() != null && !command.getTeamMemberIds().isEmpty()) {
            command.getTeamMemberIds().forEach(deal::addTeamMember);
        }

        // Set tags
        if (command.getTags() != null && !command.getTags().isEmpty()) {
            command.getTags().forEach(deal::addTag);
        }

        // Check if approval is required
        deal.checkApprovalRequired(managerApprovalThreshold, executiveApprovalThreshold);

        // Add products if provided
        if (command.getProducts() != null && !command.getProducts().isEmpty()) {
            for (DealCommand.CreateProductCommand productCmd : command.getProducts()) {
                DealProduct product = DealProduct.create(
                        command.getTenantId(),
                        deal.getDealId(),
                        productCmd.getProductName(),
                        productCmd.getQuantity(),
                        productCmd.getUnitPrice(),
                        command.getCurrency()
                );

                product.setProductCode(productCmd.getProductCode());
                product.setProductDescription(productCmd.getProductDescription());
                product.setProductCategory(productCmd.getProductCategory());
                product.setServiceType(productCmd.getServiceType());
                product.setStartDate(productCmd.getStartDate());
                product.setEndDate(productCmd.getEndDate());
                product.setIsRecurring(productCmd.getIsRecurring());
                product.setBillingCycle(productCmd.getBillingCycle());

                if (productCmd.getDiscountAmount() != null || productCmd.getDiscountPercentage() != null) {
                    product.applyDiscount(productCmd.getDiscountAmount(), productCmd.getDiscountPercentage());
                }

                DealProduct savedProduct = productRepository.save(product);
                deal.addProduct(savedProduct);
            }
        }

        // Add creation activity
        deal.addActivity(command.getOwnerId(), DealActivity.ActivityType.NOTE,
                "Deal created", "Deal " + deal.getDealName() + " created");

        Deal savedDeal = dealRepository.save(deal);
        publishEvents(savedDeal);

        log.info("Created deal: {} for tenant: {}", savedDeal.getDealId(), command.getTenantId());
        return savedDeal;
    }

    @Transactional
    public Deal update(DealCommand.UpdateDealCommand command) {
        log.info("Updating deal: {} for tenant: {}", command.getDealId(), command.getTenantId());

        Deal deal = dealRepository.findByDealIdAndTenantId(command.getDealId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Deal", command.getDealId()));

        if (deal.getStatus() == Deal.DealStatus.WON || deal.getStatus() == Deal.DealStatus.LOST) {
            throw new ValidationException("Cannot update closed deals");
        }

        if (command.getDealName() != null) {
            deal.setDealName(command.getDealName());
        }
        if (command.getAmount() != null) {
            validateDealAmount(command.getAmount());
            deal.updateAmount(command.getAmount());
            deal.checkApprovalRequired(managerApprovalThreshold, executiveApprovalThreshold);
        }
        if (command.getExpectedCloseDate() != null) {
            deal.setExpectedCloseDate(command.getExpectedCloseDate());
        }
        if (command.getPriority() != null) {
            deal.setPriority(command.getPriority());
        }
        if (command.getDescription() != null) {
            deal.setDescription(command.getDescription());
        }
        if (command.getNextSteps() != null) {
            deal.setNextSteps(command.getNextSteps());
        }
        if (command.getProbability() != null) {
            deal.updateProbability(command.getProbability());
        }

        // Update tags
        if (command.getTags() != null) {
            deal.getTagList().clear();
            command.getTags().forEach(deal::addTag);
        }

        Deal savedDeal = dealRepository.save(deal);

        log.info("Updated deal: {}", command.getDealId());
        return savedDeal;
    }

    @Transactional
    public void advanceStage(DealCommand.AdvanceStageCommand command) {
        log.info("Advancing stage for deal: {} for tenant: {}", command.getDealId(), command.getTenantId());

        Deal deal = dealRepository.findByDealIdAndTenantId(command.getDealId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Deal", command.getDealId()));

        String userId = RequestContextHolder.getUserId();

        deal.advanceStage(userId, command.getNotes());
        dealRepository.save(deal);
        publishEvents(deal);

        log.info("Advanced deal: {} to stage: {}", command.getDealId(), deal.getStage());
    }

    @Transactional
    public void regressStage(DealCommand.RegressStageCommand command) {
        log.info("Regressing stage for deal: {} for tenant: {}", command.getDealId(), command.getTenantId());

        Deal deal = dealRepository.findByDealIdAndTenantId(command.getDealId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Deal", command.getDealId()));

        String userId = RequestContextHolder.getUserId();

        deal.regressStage(userId, command.getTargetStage(), command.getReason());
        dealRepository.save(deal);
        publishEvents(deal);

        log.info("Regressed deal: {} to stage: {}", command.getDealId(), command.getTargetStage());
    }

    @Transactional
    public void markAsWon(DealCommand.MarkAsWonCommand command) {
        log.info("Marking deal as won: {} for tenant: {}", command.getDealId(), command.getTenantId());

        Deal deal = dealRepository.findByDealIdAndTenantId(command.getDealId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Deal", command.getDealId()));

        String userId = RequestContextHolder.getUserId();

        deal.markAsWon(userId, command.getFinalAmount(), command.getNotes());
        dealRepository.save(deal);
        publishEvents(deal);

        log.info("Marked deal as won: {}", command.getDealId());
    }

    @Transactional
    public void markAsLost(DealCommand.MarkAsLostCommand command) {
        log.info("Marking deal as lost: {} for tenant: {}", command.getDealId(), command.getTenantId());

        Deal deal = dealRepository.findByDealIdAndTenantId(command.getDealId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Deal", command.getDealId()));

        String userId = RequestContextHolder.getUserId();

        deal.markAsLost(userId, command.getLossReason(), command.getLossDetails());
        dealRepository.save(deal);
        publishEvents(deal);

        log.info("Marked deal as lost: {}", command.getDealId());
    }

    @Transactional
    public DealProduct addProduct(DealCommand.AddProductCommand command) {
        log.info("Adding product to deal: {} for tenant: {}", command.getDealId(), command.getTenantId());

        Deal deal = dealRepository.findByDealIdAndTenantId(command.getDealId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Deal", command.getDealId()));

        DealProduct product = DealProduct.create(
                command.getTenantId(),
                command.getDealId(),
                command.getProductName(),
                command.getQuantity(),
                command.getUnitPrice(),
                command.getCurrency() != null ? command.getCurrency() : deal.getCurrency()
        );

        product.setProductCode(command.getProductCode());
        product.setProductDescription(command.getProductDescription());
        product.setProductCategory(command.getProductCategory());
        product.setServiceType(command.getServiceType());
        product.setStartDate(command.getStartDate());
        product.setEndDate(command.getEndDate());
        product.setIsRecurring(command.getIsRecurring());
        product.setBillingCycle(command.getBillingCycle());

        DealProduct savedProduct = productRepository.save(product);
        deal.addProduct(savedProduct);
        dealRepository.save(deal);

        log.info("Added product: {} to deal: {}", savedProduct.getProductId(), command.getDealId());
        return savedProduct;
    }

    @Transactional
    public void removeProduct(String dealId, String productId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.info("Removing product: {} from deal: {}", productId, dealId);

        Deal deal = dealRepository.findByDealIdAndTenantId(dealId, tenantId)
                .orElseThrow(() -> new NotFoundException("Deal", dealId));

        deal.removeProduct(productId);
        dealRepository.save(deal);

        productRepository.deleteByProductIdAndTenantId(productId, tenantId);

        log.info("Removed product: {} from deal: {}", productId, dealId);
    }

    @Transactional
    public DealActivity addActivity(DealCommand.AddActivityCommand command) {
        log.info("Adding activity to deal: {} for tenant: {}", command.getDealId(), command.getTenantId());

        Deal deal = dealRepository.findByDealIdAndTenantId(command.getDealId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Deal", command.getDealId()));

        DealActivity activity;
        String userId = RequestContextHolder.getUserId();

        if (command.getDueDate() != null) {
            activity = DealActivity.schedule(
                    command.getDealId(),
                    command.getTenantId(),
                    userId,
                    command.getActivityType(),
                    command.getSubject(),
                    command.getDueDate(),
                    command.getPriority() != null ? command.getPriority() : DealActivity.Priority.MEDIUM
            );
            activity.setDescription(command.getDescription());
        } else {
            activity = DealActivity.create(
                    command.getDealId(),
                    command.getTenantId(),
                    userId,
                    command.getActivityType(),
                    command.getSubject(),
                    command.getDescription()
            );
        }

        DealActivity savedActivity = activityRepository.save(activity);
        deal.addActivity(userId, command.getActivityType(), command.getSubject(), command.getDescription());
        dealRepository.save(deal);

        log.info("Added activity: {} to deal: {}", savedActivity.getActivityId(), command.getDealId());
        return savedActivity;
    }

    @Transactional
    public Competitor addCompetitor(DealCommand.AddCompetitorCommand command) {
        log.info("Adding competitor to deal: {} for tenant: {}", command.getDealId(), command.getTenantId());

        Deal deal = dealRepository.findByDealIdAndTenantId(command.getDealId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Deal", command.getDealId()));

        Competitor competitor = Competitor.create(
                command.getTenantId(),
                command.getDealId(),
                command.getCompetitorName()
        );

        competitor.setStrength(command.getStrength() != null ? command.getStrength() : Competitor.StrengthLevel.MODERATE);
        competitor.setThreat(command.getThreat() != null ? command.getThreat() : Competitor.ThreatLevel.MEDIUM);
        competitor.setEstimatedDealValue(command.getEstimatedDealValue());
        competitor.setCompetingProduct(command.getCompetingProduct());
        competitor.setCompetitorStrengths(command.getCompetitorStrengths());
        competitor.setCompetitorWeaknesses(command.getCompetitorWeaknesses());
        competitor.setOurAdvantage(command.getOurAdvantage());

        if (command.getProbabilityOfWin() != null) {
            competitor.updateWinProbability(command.getProbabilityOfWin());
        }

        Competitor savedCompetitor = competitorRepository.save(competitor);
        deal.addCompetitor(savedCompetitor);
        dealRepository.save(deal);

        log.info("Added competitor: {} to deal: {}", savedCompetitor.getCompetitorId(), command.getDealId());
        return savedCompetitor;
    }

    @Transactional
    public void addTeamMember(DealCommand.AddTeamMemberCommand command) {
        log.info("Adding team member: {} to deal: {}", command.getUserId(), command.getDealId());

        Deal deal = dealRepository.findByDealIdAndTenantId(command.getDealId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Deal", command.getDealId()));

        if (deal.getTeamMemberIds().size() >= 10) {
            throw new ValidationException("Maximum team members limit reached");
        }

        deal.addTeamMember(command.getUserId());
        dealRepository.save(deal);

        log.info("Added team member: {} to deal: {}", command.getUserId(), command.getDealId());
    }

    @Transactional
    public void removeTeamMember(DealCommand.RemoveTeamMemberCommand command) {
        log.info("Removing team member: {} from deal: {}", command.getUserId(), command.getDealId());

        Deal deal = dealRepository.findByDealIdAndTenantId(command.getDealId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Deal", command.getDealId()));

        if (deal.getOwnerId().equals(command.getUserId())) {
            throw new ValidationException("Cannot remove deal owner");
        }

        deal.removeTeamMember(command.getUserId());
        dealRepository.save(deal);

        log.info("Removed team member: {} from deal: {}", command.getUserId(), command.getDealId());
    }

    @Transactional
    public void requestApproval(DealCommand.RequestApprovalCommand command) {
        log.info("Requesting approval for deal: {}", command.getDealId());

        Deal deal = dealRepository.findByDealIdAndTenantId(command.getDealId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Deal", command.getDealId()));

        deal.requestApproval();
        dealRepository.save(deal);

        log.info("Requested approval for deal: {}", command.getDealId());
    }

    @Transactional
    public void approve(DealCommand.ApproveDealCommand command) {
        log.info("Approving deal: {} by: {}", command.getDealId(), command.getApprover());

        Deal deal = dealRepository.findByDealIdAndTenantId(command.getDealId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Deal", command.getDealId()));

        deal.approve(command.getApprover());
        dealRepository.save(deal);

        log.info("Approved deal: {}", command.getDealId());
    }

    @Transactional
    public void rejectApproval(DealCommand.RejectApprovalCommand command) {
        log.info("Rejecting approval for deal: {} by: {}", command.getDealId(), command.getRejecter());

        Deal deal = dealRepository.findByDealIdAndTenantId(command.getDealId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Deal", command.getDealId()));

        deal.rejectApproval(command.getRejecter(), command.getReason());
        dealRepository.save(deal);

        log.info("Rejected approval for deal: {}", command.getDealId());
    }

    @Transactional
    public void delete(DealCommand.DeleteDealCommand command) {
        log.info("Deleting deal: {} for tenant: {}", command.getDealId(), command.getTenantId());

        Deal deal = dealRepository.findByDealIdAndTenantId(command.getDealId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("Deal", command.getDealId()));

        if (deal.getStatus() == Deal.DealStatus.WON || deal.getStatus() == Deal.DealStatus.LOST) {
            throw new ValidationException("Cannot delete closed deals");
        }

        // Delete related entities
        productRepository.deleteByDealIdAndTenantId(command.getDealId(), command.getTenantId());
        activityRepository.deleteByDealIdAndTenantId(command.getDealId(), command.getTenantId());
        competitorRepository.deleteByDealIdAndTenantId(command.getDealId(), command.getTenantId());

        dealRepository.deleteByDealIdAndTenantId(command.getDealId(), command.getTenantId());

        log.info("Deleted deal: {}", command.getDealId());
    }

    private void validateDealAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("amount", "Amount must be positive");
        }
        if (amount.compareTo(new BigDecimal("10000000")) > 0) {
            throw new ValidationException("amount", "Amount exceeds maximum limit");
        }
    }

    private void publishEvents(Deal deal) {
        if (!deal.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(deal.getDomainEvents());
            deal.clearDomainEvents();
        }
    }
}

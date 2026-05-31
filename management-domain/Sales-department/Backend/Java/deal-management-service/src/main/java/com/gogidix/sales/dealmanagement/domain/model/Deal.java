package com.gogidix.sales.dealmanagement.domain.model;

import com.gogidix.sales.dealmanagement.domain.event.DealCreatedEvent;
import com.gogidix.sales.dealmanagement.domain.event.DealLostEvent;
import com.gogidix.sales.dealmanagement.domain.event.DealStageChangedEvent;
import com.gogidix.sales.dealmanagement.domain.event.DealWonEvent;
import com.gogidix.sales.dealmanagement.shared.base.BaseEntity;
import com.gogidix.sales.dealmanagement.shared.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Deal Domain Entity
 * Multi-tenant deal management with pipeline stages and forecasting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "deals")
public class Deal extends BaseEntity {

    @Indexed
    private String dealId;

    @Indexed
    private String tenantId;

    private String dealName;

    private String dealCode;

    private DealStage stage;

    private Integer stageOrder;

    private Integer probability;

    private BigDecimal amount;

    private BigDecimal weightedAmount;

    private String currency;

    private String accountId;

    private String accountName;

    private String contactId;

    private String contactName;

    private String ownerId;

    private String ownerName;

    @Builder.Default
    private List<String> teamMemberIds = new ArrayList<>();

    private DealPriority priority;

    private DealStatus status;

    private LocalDate expectedCloseDate;

    private LocalDate actualCloseDate;

    private LocalDate createdDate;

    private Integer dealDurationDays;

    private String source;

    private String campaign;

    private String leadSource;

    private String description;

    private String nextSteps;

    private Boolean approvalRequired;

    private ApprovalStatus approvalStatus;

    private String approvedBy;

    private Instant approvedAt;

    private String lossReason;

    private String lossReasonDetails;

    @DBRef(lazy = true)
    @Builder.Default
    private List<DealProduct> products = new ArrayList<>();

    @DBRef(lazy = true)
    @Builder.Default
    private List<DealActivity> activities = new ArrayList<>();

    @DBRef(lazy = true)
    @Builder.Default
    private List<Competitor> competitors = new ArrayList<>();

    private String tags;

    @Builder.Default
    private List<String> tagList = new ArrayList<>();

    private String region;

    private String industry;

    private String segment;

    private String territory;

    private String contractType;

    private Integer contractLengthMonths;

    private Boolean renewal;

    private String renewalDealId;

    private BigDecimal discountAmount;

    private BigDecimal discountPercentage;

    private BigDecimal commissionAmount;

    private BigDecimal commissionPercentage;

    @Transient
    @Builder.Default
    private List<Object> domainEvents = new ArrayList<>();

    public enum DealStage {
        LEAD(10, 1),
        QUALIFIED(20, 2),
        PROPOSAL(40, 3),
        NEGOTIATION(60, 4),
        VERBAL_COMMIT(80, 5),
        CLOSED_WON(100, 6),
        CLOSED_LOST(0, 7);

        private final int defaultProbability;
        private final int order;

        DealStage(int defaultProbability, int order) {
            this.defaultProbability = defaultProbability;
            this.order = order;
        }

        public int getDefaultProbability() {
            return defaultProbability;
        }

        public int getOrder() {
            return order;
        }

        public static DealStage fromOrder(int order) {
            for (DealStage stage : values()) {
                if (stage.order == order) {
                    return stage;
                }
            }
            return null;
        }
    }

    public enum DealPriority {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }

    public enum DealStatus {
        OPEN,
        WON,
        LOST,
        ABANDONED,
        ON_HOLD
    }

    public enum ApprovalStatus {
        NOT_REQUIRED,
        PENDING,
        APPROVED,
        REJECTED
    }

    /**
     * Creates a new deal
     */
    public static Deal create(String tenantId, String dealName, String accountId,
                              BigDecimal amount, String currency, DealStage stage,
                              String ownerId, LocalDate expectedCloseDate) {
        Deal deal = Deal.builder()
                .tenantId(tenantId)
                .dealName(dealName)
                .accountId(accountId)
                .amount(amount)
                .currency(currency)
                .stage(stage)
                .stageOrder(stage.getOrder())
                .probability(stage.getDefaultProbability())
                .ownerId(ownerId)
                .expectedCloseDate(expectedCloseDate)
                .createdDate(LocalDate.now())
                .status(DealStatus.OPEN)
                .approvalStatus(ApprovalStatus.NOT_REQUIRED)
                .products(new ArrayList<>())
                .activities(new ArrayList<>())
                .competitors(new ArrayList<>())
                .teamMemberIds(new ArrayList<>())
                .tagList(new ArrayList<>())
                .build();

        deal.generateDealCode();
        deal.calculateWeightedAmount();

        deal.addDomainEvent(DealCreatedEvent.builder()
                .dealId(deal.getDealId())
                .tenantId(tenantId)
                .dealName(dealName)
                .amount(amount)
                .currency(currency)
                .stage(stage.name())
                .ownerId(ownerId)
                .timestamp(Instant.now())
                .eventType("DEAL_CREATED")
                .build());

        return deal;
    }

    /**
     * Advances the deal to the next stage
     */
    public void advanceStage(String userId, String notes) {
        if (this.status == DealStatus.WON || this.status == DealStatus.LOST) {
            throw new ValidationException("Cannot advance stage of closed deal");
        }

        DealStage currentStage = this.stage;
        DealStage nextStage = DealStage.fromOrder(this.stageOrder + 1);

        if (nextStage == null) {
            throw new ValidationException("Already at final stage");
        }

        if (nextStage == DealStage.CLOSED_LOST) {
            throw new ValidationException("Use loseDeal method to mark deal as lost");
        }

        DealStage previousStage = this.stage;
        this.stage = nextStage;
        this.stageOrder = nextStage.getOrder();
        this.probability = nextStage.getDefaultProbability();

        if (nextStage == DealStage.CLOSED_WON) {
            this.status = DealStatus.WON;
            this.actualCloseDate = LocalDate.now();
            calculateDealDuration();
        } else {
            this.status = DealStatus.OPEN;
        }

        this.calculateWeightedAmount();
        addActivity(userId, DealActivity.ActivityType.STAGE_CHANGE,
                "Stage changed from " + previousStage + " to " + nextStage, notes);

        addDomainEvent(DealStageChangedEvent.builder()
                .dealId(this.dealId)
                .tenantId(this.tenantId)
                .previousStage(previousStage.name())
                .newStage(nextStage.name())
                .probability(nextStage.getDefaultProbability())
                .changedBy(userId)
                .timestamp(Instant.now())
                .eventType("DEAL_STAGE_CHANGED")
                .build());

        if (nextStage == DealStage.CLOSED_WON) {
            addDomainEvent(DealWonEvent.builder()
                    .dealId(this.dealId)
                    .tenantId(this.tenantId)
                    .dealName(this.dealName)
                    .amount(this.amount)
                    .currency(this.currency)
                    .closedBy(userId)
                    .closeDate(this.actualCloseDate)
                    .timestamp(Instant.now())
                    .eventType("DEAL_WON")
                    .build());
        }
    }

    /**
     * Moves the deal back to a previous stage
     */
    public void regressStage(String userId, DealStage targetStage, String reason) {
        if (this.status == DealStatus.WON || this.status == DealStatus.LOST) {
            throw new ValidationException("Cannot regress stage of closed deal");
        }

        if (targetStage.getOrder() >= this.stageOrder) {
            throw new ValidationException("Target stage must be before current stage");
        }

        DealStage previousStage = this.stage;
        this.stage = targetStage;
        this.stageOrder = targetStage.getOrder();
        this.probability = targetStage.getDefaultProbability();
        this.status = DealStatus.OPEN;

        this.calculateWeightedAmount();
        addActivity(userId, DealActivity.ActivityType.STAGE_CHANGE,
                "Stage regressed from " + previousStage + " to " + targetStage, reason);

        addDomainEvent(DealStageChangedEvent.builder()
                .dealId(this.dealId)
                .tenantId(this.tenantId)
                .previousStage(previousStage.name())
                .newStage(targetStage.name())
                .probability(targetStage.getDefaultProbability())
                .changedBy(userId)
                .timestamp(Instant.now())
                .eventType("DEAL_STAGE_REGRESSED")
                .build());
    }

    /**
     * Marks the deal as won
     */
    public void markAsWon(String userId, BigDecimal finalAmount, String notes) {
        if (this.status != DealStatus.OPEN) {
            throw new ValidationException("Can only mark open deals as won");
        }

        DealStage previousStage = this.stage;
        this.stage = DealStage.CLOSED_WON;
        this.stageOrder = DealStage.CLOSED_WON.getOrder();
        this.probability = 100;
        this.status = DealStatus.WON;
        this.actualCloseDate = LocalDate.now();

        if (finalAmount != null && finalAmount.compareTo(BigDecimal.ZERO) > 0) {
            this.amount = finalAmount;
        }

        this.calculateWeightedAmount();
        this.calculateDealDuration();

        addActivity(userId, DealActivity.ActivityType.DEAL_WON,
                "Deal closed as won at $" + this.amount, notes);

        addDomainEvent(DealStageChangedEvent.builder()
                .dealId(this.dealId)
                .tenantId(this.tenantId)
                .previousStage(previousStage.name())
                .newStage(DealStage.CLOSED_WON.name())
                .probability(100)
                .changedBy(userId)
                .timestamp(Instant.now())
                .eventType("DEAL_STAGE_CHANGED")
                .build());

        addDomainEvent(DealWonEvent.builder()
                .dealId(this.dealId)
                .tenantId(this.tenantId)
                .dealName(this.dealName)
                .amount(this.amount)
                .currency(this.currency)
                .closedBy(userId)
                .closeDate(this.actualCloseDate)
                .timestamp(Instant.now())
                .eventType("DEAL_WON")
                .build());
    }

    /**
     * Marks the deal as lost
     */
    public void markAsLost(String userId, String lossReason, String lossDetails) {
        if (this.status != DealStatus.OPEN) {
            throw new ValidationException("Can only mark open deals as lost");
        }

        DealStage previousStage = this.stage;
        this.stage = DealStage.CLOSED_LOST;
        this.stageOrder = DealStage.CLOSED_LOST.getOrder();
        this.probability = 0;
        this.status = DealStatus.LOST;
        this.actualCloseDate = LocalDate.now();
        this.lossReason = lossReason;
        this.lossReasonDetails = lossDetails;

        this.weightedAmount = BigDecimal.ZERO;
        this.calculateDealDuration();

        addActivity(userId, DealActivity.ActivityType.DEAL_LOST,
                "Deal closed as lost. Reason: " + lossReason, lossDetails);

        addDomainEvent(DealStageChangedEvent.builder()
                .dealId(this.dealId)
                .tenantId(this.tenantId)
                .previousStage(previousStage.name())
                .newStage(DealStage.CLOSED_LOST.name())
                .probability(0)
                .changedBy(userId)
                .timestamp(Instant.now())
                .eventType("DEAL_STAGE_CHANGED")
                .build());

        addDomainEvent(DealLostEvent.builder()
                .dealId(this.dealId)
                .tenantId(this.tenantId)
                .dealName(this.dealName)
                .amount(this.amount)
                .currency(this.currency)
                .lostBy(userId)
                .lostDate(this.actualCloseDate)
                .lossReason(lossReason)
                .timestamp(Instant.now())
                .eventType("DEAL_LOST")
                .build());
    }

    /**
     * Adds a product to the deal
     */
    public void addProduct(DealProduct product) {
        if (this.products == null) {
            this.products = new ArrayList<>();
        }
        this.products.add(product);
        recalculateAmount();
    }

    /**
     * Removes a product from the deal
     */
    public void removeProduct(String productId) {
        if (this.products != null) {
            this.products.removeIf(p -> p.getProductId().equals(productId));
            recalculateAmount();
        }
    }

    /**
     * Adds an activity to the deal
     */
    public void addActivity(String userId, DealActivity.ActivityType type, String subject, String notes) {
        if (this.activities == null) {
            this.activities = new ArrayList<>();
        }
        DealActivity activity = DealActivity.create(this.dealId, this.tenantId, userId, type, subject, notes);
        this.activities.add(activity);
    }

    /**
     * Adds a competitor to the deal
     */
    public void addCompetitor(Competitor competitor) {
        if (this.competitors == null) {
            this.competitors = new ArrayList<>();
        }
        this.competitors.add(competitor);
    }

    /**
     * Adds a team member
     */
    public void addTeamMember(String userId) {
        if (this.teamMemberIds == null) {
            this.teamMemberIds = new ArrayList<>();
        }
        if (!this.teamMemberIds.contains(userId)) {
            this.teamMemberIds.add(userId);
        }
    }

    /**
     * Removes a team member
     */
    public void removeTeamMember(String userId) {
        if (this.teamMemberIds != null) {
            this.teamMemberIds.remove(userId);
        }
    }

    /**
     * Updates the deal amount
     */
    public void updateAmount(BigDecimal newAmount) {
        if (newAmount == null || newAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("amount", "Amount must be positive");
        }
        this.amount = newAmount;
        this.calculateWeightedAmount();
    }

    /**
     * Updates the probability
     */
    public void updateProbability(Integer newProbability) {
        if (newProbability < 0 || newProbability > 100) {
            throw new ValidationException("probability", "Probability must be between 0 and 100");
        }
        this.probability = newProbability;
        this.calculateWeightedAmount();
    }

    /**
     * Requests approval for the deal
     */
    public void requestApproval() {
        if (this.approvalRequired == null || !this.approvalRequired) {
            this.approvalRequired = true;
        }
        this.approvalStatus = ApprovalStatus.PENDING;
    }

    /**
     * Approves the deal
     */
    public void approve(String approver) {
        if (this.approvalStatus != ApprovalStatus.PENDING) {
            throw new ValidationException("Deal is not pending approval");
        }
        this.approvalStatus = ApprovalStatus.APPROVED;
        this.approvedBy = approver;
        this.approvedAt = Instant.now();
    }

    /**
     * Rejects the deal approval
     */
    public void rejectApproval(String approver, String reason) {
        if (this.approvalStatus != ApprovalStatus.PENDING) {
            throw new ValidationException("Deal is not pending approval");
        }
        this.approvalStatus = ApprovalStatus.REJECTED;
        this.approvedBy = approver;
        this.approvedAt = Instant.now();
    }

    /**
     * Adds a tag
     */
    public void addTag(String tag) {
        if (this.tagList == null) {
            this.tagList = new ArrayList<>();
        }
        if (!this.tagList.contains(tag)) {
            this.tagList.add(tag);
            updateTagsString();
        }
    }

    /**
     * Removes a tag
     */
    public void removeTag(String tag) {
        if (this.tagList != null) {
            this.tagList.remove(tag);
            updateTagsString();
        }
    }

    /**
     * Checks if approval is required based on amount
     */
    public boolean checkApprovalRequired(BigDecimal managerThreshold, BigDecimal executiveThreshold) {
        if (this.amount == null) {
            return false;
        }

        if (this.amount.compareTo(executiveThreshold) >= 0) {
            this.approvalRequired = true;
            return true;
        } else if (this.amount.compareTo(managerThreshold) >= 0) {
            this.approvalRequired = true;
            return true;
        }

        this.approvalRequired = false;
        this.approvalStatus = ApprovalStatus.NOT_REQUIRED;
        return false;
    }

    private void calculateWeightedAmount() {
        if (this.amount != null && this.probability != null) {
            this.weightedAmount = this.amount
                    .multiply(BigDecimal.valueOf(this.probability))
                    .divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
        } else {
            this.weightedAmount = BigDecimal.ZERO;
        }
    }

    private void recalculateAmount() {
        if (this.products != null && !this.products.isEmpty()) {
            BigDecimal total = this.products.stream()
                    .map(DealProduct::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (this.discountAmount != null && this.discountAmount.compareTo(BigDecimal.ZERO) > 0) {
                total = total.subtract(this.discountAmount);
            }

            this.amount = total;
        }
        this.calculateWeightedAmount();
    }

    private void calculateDealDuration() {
        if (this.createdDate != null && this.actualCloseDate != null) {
            this.dealDurationDays = (int) java.time.temporal.ChronoUnit.DAYS.between(this.createdDate, this.actualCloseDate);
        }
    }

    private void generateDealCode() {
        if (this.dealId == null) {
            this.dealId = java.util.UUID.randomUUID().toString();
        }
        this.dealCode = "DL-" + this.dealId.substring(0, 8).toUpperCase();
    }

    private void updateTagsString() {
        if (this.tagList != null && !this.tagList.isEmpty()) {
            this.tags = String.join(",", this.tagList);
        } else {
            this.tags = null;
        }
    }

    public void addDomainEvent(Object event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }

    /**
     * Checks if the deal is in the sales pipeline (open)
     */
    public boolean isInPipeline() {
        return this.status == DealStatus.OPEN;
    }

    /**
     * Checks if the deal is closed
     */
    public boolean isClosed() {
        return this.status == DealStatus.WON || this.status == DealStatus.LOST;
    }

    /**
     * Gets the weighted forecast amount
     */
    public BigDecimal getForecastAmount() {
        return this.weightedAmount != null ? this.weightedAmount : BigDecimal.ZERO;
    }
}

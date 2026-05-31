package com.gogidix.finance.cashflow.infrastructure.persistence.mongodb;

import com.gogidix.finance.cashflow.domain.model.CashflowForecast;
import com.gogidix.finance.cashflow.domain.event.CashflowForecastGeneratedEvent;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * MongoDB document entity for storing CashflowForecast domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "cashflow_forecasts")
public class CashflowForecastEntity {

    @Id
    private String id;

    @Indexed
    @Field("forecast_id")
    private String forecastId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("start_date")
    private LocalDate startDate;

    @Field("end_date")
    private LocalDate endDate;

    @Field("period")
    private String period;

    @Field("scenario")
    private String scenario;

    @Field("status")
    private String status;

    @Field("generated_by")
    private String generatedBy;

    @Field("generated_at")
    private Instant generatedAt;

    @Field("last_updated")
    private Instant lastUpdated;

    @Field("total_inflow")
    private BigDecimal totalInflow;

    @Field("total_outflow")
    private BigDecimal totalOutflow;

    @Field("net_cashflow")
    private BigDecimal netCashflow;

    @Field("opening_balance")
    private BigDecimal openingBalance;

    @Field("closing_balance")
    private BigDecimal closingBalance;

    @Field("minimum_balance")
    private BigDecimal minimumBalance;

    @Field("maximum_balance")
    private BigDecimal maximumBalance;

    @Field("minimum_balance_date")
    private LocalDate minimumBalanceDate;

    @Field("maximum_balance_date")
    private LocalDate maximumBalanceDate;

    @Field("version")
    private Integer version;

    @Field("parent_forecast_id")
    private String parentForecastId;

    @Field("is_baseline")
    private Boolean isBaseline;

    @Field("period_data")
    private List<ForecastPeriodDataEmbed> periodData;

    @Field("variances")
    private List<ForecastVarianceEmbed> variances;

    @Field("tags")
    private List<String> tags;

    @Field("notes")
    private String notes;

    @Field("confidence_level")
    private String confidenceLevel;

    @Field("variance_percentage")
    private BigDecimal variancePercentage;

    @Field("domain_events")
    private List<CashflowForecastGeneratedEvent> domainEvents;

    // Default constructor for MongoDB
    public CashflowForecastEntity() {
    }

    // Constructor from domain model
    public CashflowForecastEntity(CashflowForecast forecast) {
        this.forecastId = forecast.getForecastId();
        this.tenantId = forecast.getTenantId();
        this.name = forecast.getName();
        this.description = forecast.getDescription();
        this.startDate = forecast.getStartDate();
        this.endDate = forecast.getEndDate();
        this.period = forecast.getPeriod() != null ? forecast.getPeriod().name() : null;
        this.scenario = forecast.getScenario() != null ? forecast.getScenario().name() : null;
        this.status = forecast.getStatus() != null ? forecast.getStatus().name() : null;
        this.generatedBy = forecast.getGeneratedBy();
        this.generatedAt = forecast.getGeneratedAt();
        this.lastUpdated = forecast.getLastUpdated();
        this.totalInflow = forecast.getTotalInflow();
        this.totalOutflow = forecast.getTotalOutflow();
        this.netCashflow = forecast.getNetCashflow();
        this.openingBalance = forecast.getOpeningBalance();
        this.closingBalance = forecast.getClosingBalance();
        this.minimumBalance = forecast.getMinimumBalance();
        this.maximumBalance = forecast.getMaximumBalance();
        this.minimumBalanceDate = forecast.getMinimumBalanceDate();
        this.maximumBalanceDate = forecast.getMaximumBalanceDate();
        this.version = forecast.getVersion();
        this.parentForecastId = forecast.getParentForecastId();
        this.isBaseline = forecast.getIsBaseline();
        this.tags = forecast.getTags() != null ? new ArrayList<>(forecast.getTags()) : new ArrayList<>();
        this.notes = forecast.getNotes();
        this.confidenceLevel = forecast.getConfidenceLevel() != null ? forecast.getConfidenceLevel().name() : null;
        this.variancePercentage = forecast.getVariancePercentage();
        this.domainEvents = forecast.getDomainEvents() != null ? new ArrayList<>(forecast.getDomainEvents()) : new ArrayList<>();

        // Convert period data
        if (forecast.getPeriodData() != null) {
            this.periodData = new ArrayList<>();
            for (CashflowForecast.ForecastPeriodData data : forecast.getPeriodData()) {
                this.periodData.add(new ForecastPeriodDataEmbed(data));
            }
        }

        // Convert variances
        if (forecast.getVariances() != null) {
            this.variances = new ArrayList<>();
            for (CashflowForecast.ForecastVariance variance : forecast.getVariances()) {
                this.variances.add(new ForecastVarianceEmbed(variance));
            }
        }
    }

    // Convert to domain model
    public CashflowForecast toDomainModel() {
        List<CashflowForecast.ForecastPeriodData> periodDataList = new ArrayList<>();
        if (this.periodData != null) {
            for (ForecastPeriodDataEmbed embed : this.periodData) {
                periodDataList.add(embed.toDomainModel());
            }
        }

        List<CashflowForecast.ForecastVariance> varianceList = new ArrayList<>();
        if (this.variances != null) {
            for (ForecastVarianceEmbed embed : this.variances) {
                varianceList.add(embed.toDomainModel());
            }
        }

        return CashflowForecast.builder()
                .forecastId(this.forecastId)
                .tenantId(this.tenantId)
                .name(this.name)
                .description(this.description)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .period(this.period != null ? CashflowForecast.ForecastPeriod.valueOf(this.period) : null)
                .scenario(this.scenario != null ? CashflowForecast.ForecastScenario.valueOf(this.scenario) : null)
                .status(this.status != null ? CashflowForecast.ForecastStatus.valueOf(this.status) : null)
                .generatedBy(this.generatedBy)
                .generatedAt(this.generatedAt)
                .lastUpdated(this.lastUpdated)
                .totalInflow(this.totalInflow)
                .totalOutflow(this.totalOutflow)
                .netCashflow(this.netCashflow)
                .openingBalance(this.openingBalance)
                .closingBalance(this.closingBalance)
                .minimumBalance(this.minimumBalance)
                .maximumBalance(this.maximumBalance)
                .minimumBalanceDate(this.minimumBalanceDate)
                .maximumBalanceDate(this.maximumBalanceDate)
                .version(this.version)
                .parentForecastId(this.parentForecastId)
                .isBaseline(this.isBaseline)
                .periodData(periodDataList)
                .variances(varianceList)
                .tags(this.tags != null ? new ArrayList<>(this.tags) : new ArrayList<>())
                .notes(this.notes)
                .confidenceLevel(this.confidenceLevel != null ? CashflowForecast.ConfidenceLevel.valueOf(this.confidenceLevel) : null)
                .variancePercentage(this.variancePercentage)
                .domainEvents(this.domainEvents != null ? new ArrayList<>(this.domainEvents) : new ArrayList<>())
                .build();
    }

    // Update from domain model (for partial updates)
    public void updateFrom(CashflowForecast forecast) {
        this.name = forecast.getName();
        this.description = forecast.getDescription();
        this.startDate = forecast.getStartDate();
        this.endDate = forecast.getEndDate();
        this.period = forecast.getPeriod() != null ? forecast.getPeriod().name() : null;
        this.scenario = forecast.getScenario() != null ? forecast.getScenario().name() : null;
        this.status = forecast.getStatus() != null ? forecast.getStatus().name() : null;
        this.generatedBy = forecast.getGeneratedBy();
        this.generatedAt = forecast.getGeneratedAt();
        this.lastUpdated = forecast.getLastUpdated();
        this.totalInflow = forecast.getTotalInflow();
        this.totalOutflow = forecast.getTotalOutflow();
        this.netCashflow = forecast.getNetCashflow();
        this.openingBalance = forecast.getOpeningBalance();
        this.closingBalance = forecast.getClosingBalance();
        this.minimumBalance = forecast.getMinimumBalance();
        this.maximumBalance = forecast.getMaximumBalance();
        this.minimumBalanceDate = forecast.getMinimumBalanceDate();
        this.maximumBalanceDate = forecast.getMaximumBalanceDate();
        this.version = forecast.getVersion();
        this.parentForecastId = forecast.getParentForecastId();
        this.isBaseline = forecast.getIsBaseline();
        this.tags = forecast.getTags() != null ? new ArrayList<>(forecast.getTags()) : new ArrayList<>();
        this.notes = forecast.getNotes();
        this.confidenceLevel = forecast.getConfidenceLevel() != null ? forecast.getConfidenceLevel().name() : null;
        this.variancePercentage = forecast.getVariancePercentage();
        this.domainEvents = forecast.getDomainEvents() != null ? new ArrayList<>(forecast.getDomainEvents()) : new ArrayList<>();

        // Update period data
        if (forecast.getPeriodData() != null) {
            this.periodData = new ArrayList<>();
            for (CashflowForecast.ForecastPeriodData data : forecast.getPeriodData()) {
                this.periodData.add(new ForecastPeriodDataEmbed(data));
            }
        }

        // Update variances
        if (forecast.getVariances() != null) {
            this.variances = new ArrayList<>();
            for (CashflowForecast.ForecastVariance variance : forecast.getVariances()) {
                this.variances.add(new ForecastVarianceEmbed(variance));
            }
        }
    }

    // Embedded class for period data
    public static class ForecastPeriodDataEmbed {
        private LocalDate periodStart;
        private LocalDate periodEnd;
        private BigDecimal openingBalance;
        private BigDecimal inflow;
        private BigDecimal outflow;
        private BigDecimal netCashflow;
        private BigDecimal closingBalance;
        private Integer transactionCount;

        public ForecastPeriodDataEmbed() {
        }

        public ForecastPeriodDataEmbed(CashflowForecast.ForecastPeriodData data) {
            this.periodStart = data.getPeriodStart();
            this.periodEnd = data.getPeriodEnd();
            this.openingBalance = data.getOpeningBalance();
            this.inflow = data.getInflow();
            this.outflow = data.getOutflow();
            this.netCashflow = data.getNetCashflow();
            this.closingBalance = data.getClosingBalance();
            this.transactionCount = data.getTransactionCount();
        }

        public CashflowForecast.ForecastPeriodData toDomainModel() {
            return CashflowForecast.ForecastPeriodData.builder()
                    .periodStart(this.periodStart)
                    .periodEnd(this.periodEnd)
                    .openingBalance(this.openingBalance)
                    .inflow(this.inflow)
                    .outflow(this.outflow)
                    .netCashflow(this.netCashflow)
                    .closingBalance(this.closingBalance)
                    .transactionCount(this.transactionCount)
                    .build();
        }

        // Getters and setters
        public LocalDate getPeriodStart() {
            return periodStart;
        }

        public void setPeriodStart(LocalDate periodStart) {
            this.periodStart = periodStart;
        }

        public LocalDate getPeriodEnd() {
            return periodEnd;
        }

        public void setPeriodEnd(LocalDate periodEnd) {
            this.periodEnd = periodEnd;
        }

        public BigDecimal getOpeningBalance() {
            return openingBalance;
        }

        public void setOpeningBalance(BigDecimal openingBalance) {
            this.openingBalance = openingBalance;
        }

        public BigDecimal getInflow() {
            return inflow;
        }

        public void setInflow(BigDecimal inflow) {
            this.inflow = inflow;
        }

        public BigDecimal getOutflow() {
            return outflow;
        }

        public void setOutflow(BigDecimal outflow) {
            this.outflow = outflow;
        }

        public BigDecimal getNetCashflow() {
            return netCashflow;
        }

        public void setNetCashflow(BigDecimal netCashflow) {
            this.netCashflow = netCashflow;
        }

        public BigDecimal getClosingBalance() {
            return closingBalance;
        }

        public void setClosingBalance(BigDecimal closingBalance) {
            this.closingBalance = closingBalance;
        }

        public Integer getTransactionCount() {
            return transactionCount;
        }

        public void setTransactionCount(Integer transactionCount) {
            this.transactionCount = transactionCount;
        }
    }

    // Embedded class for variance
    public static class ForecastVarianceEmbed {
        private String category;
        private BigDecimal forecastedAmount;
        private BigDecimal actualAmount;
        private BigDecimal variance;
        private BigDecimal variancePercentage;
        private LocalDate periodDate;

        public ForecastVarianceEmbed() {
        }

        public ForecastVarianceEmbed(CashflowForecast.ForecastVariance variance) {
            this.category = variance.getCategory();
            this.forecastedAmount = variance.getForecastedAmount();
            this.actualAmount = variance.getActualAmount();
            this.variance = variance.getVariance();
            this.variancePercentage = variance.getVariancePercentage();
            this.periodDate = variance.getPeriodDate();
        }

        public CashflowForecast.ForecastVariance toDomainModel() {
            return CashflowForecast.ForecastVariance.builder()
                    .category(this.category)
                    .forecastedAmount(this.forecastedAmount)
                    .actualAmount(this.actualAmount)
                    .variance(this.variance)
                    .variancePercentage(this.variancePercentage)
                    .periodDate(this.periodDate)
                    .build();
        }

        // Getters and setters
        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public BigDecimal getForecastedAmount() {
            return forecastedAmount;
        }

        public void setForecastedAmount(BigDecimal forecastedAmount) {
            this.forecastedAmount = forecastedAmount;
        }

        public BigDecimal getActualAmount() {
            return actualAmount;
        }

        public void setActualAmount(BigDecimal actualAmount) {
            this.actualAmount = actualAmount;
        }

        public BigDecimal getVariance() {
            return variance;
        }

        public void setVariance(BigDecimal variance) {
            this.variance = variance;
        }

        public BigDecimal getVariancePercentage() {
            return variancePercentage;
        }

        public void setVariancePercentage(BigDecimal variancePercentage) {
            this.variancePercentage = variancePercentage;
        }

        public LocalDate getPeriodDate() {
            return periodDate;
        }

        public void setPeriodDate(LocalDate periodDate) {
            this.periodDate = periodDate;
        }
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getForecastId() {
        return forecastId;
    }

    public void setForecastId(String forecastId) {
        this.forecastId = forecastId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGeneratedBy() {
        return generatedBy;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(Instant generatedAt) {
        this.generatedAt = generatedAt;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public BigDecimal getTotalInflow() {
        return totalInflow;
    }

    public void setTotalInflow(BigDecimal totalInflow) {
        this.totalInflow = totalInflow;
    }

    public BigDecimal getTotalOutflow() {
        return totalOutflow;
    }

    public void setTotalOutflow(BigDecimal totalOutflow) {
        this.totalOutflow = totalOutflow;
    }

    public BigDecimal getNetCashflow() {
        return netCashflow;
    }

    public void setNetCashflow(BigDecimal netCashflow) {
        this.netCashflow = netCashflow;
    }

    public BigDecimal getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(BigDecimal openingBalance) {
        this.openingBalance = openingBalance;
    }

    public BigDecimal getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(BigDecimal closingBalance) {
        this.closingBalance = closingBalance;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public BigDecimal getMaximumBalance() {
        return maximumBalance;
    }

    public void setMaximumBalance(BigDecimal maximumBalance) {
        this.maximumBalance = maximumBalance;
    }

    public LocalDate getMinimumBalanceDate() {
        return minimumBalanceDate;
    }

    public void setMinimumBalanceDate(LocalDate minimumBalanceDate) {
        this.minimumBalanceDate = minimumBalanceDate;
    }

    public LocalDate getMaximumBalanceDate() {
        return maximumBalanceDate;
    }

    public void setMaximumBalanceDate(LocalDate maximumBalanceDate) {
        this.maximumBalanceDate = maximumBalanceDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getParentForecastId() {
        return parentForecastId;
    }

    public void setParentForecastId(String parentForecastId) {
        this.parentForecastId = parentForecastId;
    }

    public Boolean getIsBaseline() {
        return isBaseline;
    }

    public void setIsBaseline(Boolean isBaseline) {
        this.isBaseline = isBaseline;
    }

    public List<ForecastPeriodDataEmbed> getPeriodData() {
        return periodData;
    }

    public void setPeriodData(List<ForecastPeriodDataEmbed> periodData) {
        this.periodData = periodData;
    }

    public List<ForecastVarianceEmbed> getVariances() {
        return variances;
    }

    public void setVariances(List<ForecastVarianceEmbed> variances) {
        this.variances = variances;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(String confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public BigDecimal getVariancePercentage() {
        return variancePercentage;
    }

    public void setVariancePercentage(BigDecimal variancePercentage) {
        this.variancePercentage = variancePercentage;
    }

    public List<CashflowForecastGeneratedEvent> getDomainEvents() {
        return domainEvents;
    }

    public void setDomainEvents(List<CashflowForecastGeneratedEvent> domainEvents) {
        this.domainEvents = domainEvents;
    }
}

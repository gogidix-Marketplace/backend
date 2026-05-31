package com.gogidix.finance.forecasting.infrastructure.persistence.mongodb;

import com.gogidix.finance.forecasting.domain.model.Forecast;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * MongoDB document entity for storing Forecast domain model.
 * This is the persistence layer representation optimized for MongoDB storage.
 */
@Document(collection = "forecasts")
public class ForecastEntity {

    @Id
    private String id;

    @Indexed
    @Field("forecast_id")
    private String forecastId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("forecast_type")
    private String forecastType;

    @Field("forecast_horizon")
    private String forecastHorizon;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("start_date")
    private Instant startDate;

    @Field("end_date")
    private Instant endDate;

    @Indexed
    @Field("status")
    private String status;

    @Field("created_by")
    private String createdBy;

    @Field("approved_by")
    private String approvedBy;

    @Field("approved_at")
    private Instant approvedAt;

    @Field("rejection_reason")
    private String rejectionReason;

    @Field("currency")
    private String currency;

    @Field("total_forecast_amount")
    private BigDecimal totalForecastAmount;

    @Field("actual_amount")
    private BigDecimal actualAmount;

    @Field("variance_amount")
    private BigDecimal varianceAmount;

    @Field("variance_percentage")
    private BigDecimal variancePercentage;

    @Field("confidence_level")
    private Integer confidenceLevel;

    @Field("data_source")
    private String dataSource;

    @Field("department")
    private String department;

    @Field("category")
    private String category;

    @Field("scenario")
    private String scenario;

    @Field("metrics")
    private List<ForecastMetricEmbed> metrics;

    @Field("notes")
    private String notes;

    @Field("last_regenerated_at")
    private Instant lastRegeneratedAt;

    @Field("regeneration_count")
    private Integer regenerationCount;

    @Field("domain_events")
    private List<Object> domainEvents;

    // Default constructor for MongoDB
    public ForecastEntity() {
    }

    // Constructor from domain model
    public ForecastEntity(Forecast forecast) {
        this.forecastId = forecast.getForecastId();
        this.tenantId = forecast.getTenantId();
        this.forecastType = forecast.getForecastType() != null ? forecast.getForecastType().name() : null;
        this.forecastHorizon = forecast.getForecastHorizon() != null ? forecast.getForecastHorizon().name() : null;
        this.name = forecast.getName();
        this.description = forecast.getDescription();
        this.startDate = forecast.getStartDate();
        this.endDate = forecast.getEndDate();
        this.status = forecast.getStatus() != null ? forecast.getStatus().name() : null;
        this.createdBy = forecast.getCreatedBy();
        this.approvedBy = forecast.getApprovedBy();
        this.approvedAt = forecast.getApprovedAt();
        this.rejectionReason = forecast.getRejectionReason();
        this.currency = forecast.getCurrency();
        this.totalForecastAmount = forecast.getTotalForecastAmount();
        this.actualAmount = forecast.getActualAmount();
        this.varianceAmount = forecast.getVarianceAmount();
        this.variancePercentage = forecast.getVariancePercentage();
        this.confidenceLevel = forecast.getConfidenceLevel();
        this.dataSource = forecast.getDataSource();
        this.department = forecast.getDepartment();
        this.category = forecast.getCategory();
        this.scenario = forecast.getScenario();
        this.notes = forecast.getNotes();
        this.lastRegeneratedAt = forecast.getLastRegeneratedAt();
        this.regenerationCount = forecast.getRegenerationCount();

        // Convert metrics
        if (forecast.getMetrics() != null) {
            this.metrics = new ArrayList<>();
            for (com.gogidix.finance.forecasting.domain.model.ForecastMetric metric : forecast.getMetrics()) {
                this.metrics.add(new ForecastMetricEmbed(metric));
            }
        }

        this.domainEvents = forecast.getDomainEvents() != null ? new ArrayList<>(forecast.getDomainEvents()) : new ArrayList<>();
    }

    // Convert to domain model
    public Forecast toDomainModel() {
        List<com.gogidix.finance.forecasting.domain.model.ForecastMetric> metricList = new ArrayList<>();
        if (this.metrics != null) {
            for (ForecastMetricEmbed embed : this.metrics) {
                metricList.add(embed.toDomainModel());
            }
        }

        return Forecast.builder()
                .forecastId(this.forecastId)
                .tenantId(this.tenantId)
                .forecastType(this.forecastType != null ? Forecast.ForecastType.valueOf(this.forecastType) : null)
                .forecastHorizon(this.forecastHorizon != null ? Forecast.ForecastHorizon.valueOf(this.forecastHorizon) : null)
                .name(this.name)
                .description(this.description)
                .startDate(this.startDate)
                .endDate(this.endDate)
                .status(this.status != null ? Forecast.ForecastStatus.valueOf(this.status) : null)
                .createdBy(this.createdBy)
                .approvedBy(this.approvedBy)
                .approvedAt(this.approvedAt)
                .rejectionReason(this.rejectionReason)
                .currency(this.currency)
                .totalForecastAmount(this.totalForecastAmount)
                .actualAmount(this.actualAmount)
                .varianceAmount(this.varianceAmount)
                .variancePercentage(this.variancePercentage)
                .confidenceLevel(this.confidenceLevel)
                .dataSource(this.dataSource)
                .department(this.department)
                .category(this.category)
                .scenario(this.scenario)
                .metrics(metricList)
                .notes(this.notes)
                .lastRegeneratedAt(this.lastRegeneratedAt)
                .regenerationCount(this.regenerationCount)
                .domainEvents(this.domainEvents != null ? new ArrayList<>(this.domainEvents) : new ArrayList<>())
                .build();
    }

    // Update from domain model (for partial updates)
    public void updateFrom(Forecast forecast) {
        this.forecastId = forecast.getForecastId();
        this.tenantId = forecast.getTenantId();
        this.forecastType = forecast.getForecastType() != null ? forecast.getForecastType().name() : null;
        this.forecastHorizon = forecast.getForecastHorizon() != null ? forecast.getForecastHorizon().name() : null;
        this.name = forecast.getName();
        this.description = forecast.getDescription();
        this.startDate = forecast.getStartDate();
        this.endDate = forecast.getEndDate();
        this.status = forecast.getStatus() != null ? forecast.getStatus().name() : null;
        this.createdBy = forecast.getCreatedBy();
        this.approvedBy = forecast.getApprovedBy();
        this.approvedAt = forecast.getApprovedAt();
        this.rejectionReason = forecast.getRejectionReason();
        this.currency = forecast.getCurrency();
        this.totalForecastAmount = forecast.getTotalForecastAmount();
        this.actualAmount = forecast.getActualAmount();
        this.varianceAmount = forecast.getVarianceAmount();
        this.variancePercentage = forecast.getVariancePercentage();
        this.confidenceLevel = forecast.getConfidenceLevel();
        this.dataSource = forecast.getDataSource();
        this.department = forecast.getDepartment();
        this.category = forecast.getCategory();
        this.scenario = forecast.getScenario();
        this.notes = forecast.getNotes();
        this.lastRegeneratedAt = forecast.getLastRegeneratedAt();
        this.regenerationCount = forecast.getRegenerationCount();

        // Update metrics
        if (forecast.getMetrics() != null) {
            this.metrics = new ArrayList<>();
            for (com.gogidix.finance.forecasting.domain.model.ForecastMetric metric : forecast.getMetrics()) {
                this.metrics.add(new ForecastMetricEmbed(metric));
            }
        }

        this.domainEvents = forecast.getDomainEvents() != null ? new ArrayList<>(forecast.getDomainEvents()) : new ArrayList<>();
    }

    // Embedded class for metrics
    public static class ForecastMetricEmbed {
        private String metricId;
        private String forecastId;
        private String metricName;
        private String metricCode;
        private String category;
        private String subcategory;
        private BigDecimal amount;
        private BigDecimal previousAmount;
        private BigDecimal variance;
        private BigDecimal variancePercentage;
        private String period;
        private Instant periodStart;
        private Instant periodEnd;
        private String metricType;
        private String unit;
        private BigDecimal weight;
        private Integer confidenceLevel;
        private String dataSource;
        private String notes;
        private Integer sortOrder;
        private Boolean isCalculated;
        private String calculationFormula;
        private Instant createdAt;
        private Instant updatedAt;

        public ForecastMetricEmbed() {
        }

        public ForecastMetricEmbed(com.gogidix.finance.forecasting.domain.model.ForecastMetric metric) {
            this.metricId = metric.getMetricId();
            this.forecastId = metric.getForecastId();
            this.metricName = metric.getMetricName();
            this.metricCode = metric.getMetricCode();
            this.category = metric.getCategory();
            this.subcategory = metric.getSubcategory();
            this.amount = metric.getAmount();
            this.previousAmount = metric.getPreviousAmount();
            this.variance = metric.getVariance();
            this.variancePercentage = metric.getVariancePercentage();
            this.period = metric.getPeriod();
            this.periodStart = metric.getPeriodStart();
            this.periodEnd = metric.getPeriodEnd();
            this.metricType = metric.getMetricType() != null ? metric.getMetricType().name() : null;
            this.unit = metric.getUnit();
            this.weight = metric.getWeight();
            this.confidenceLevel = metric.getConfidenceLevel();
            this.dataSource = metric.getDataSource();
            this.notes = metric.getNotes();
            this.sortOrder = metric.getSortOrder();
            this.isCalculated = metric.getIsCalculated();
            this.calculationFormula = metric.getCalculationFormula();
            this.createdAt = metric.getCreatedAt();
            this.updatedAt = metric.getUpdatedAt();
        }

        public com.gogidix.finance.forecasting.domain.model.ForecastMetric toDomainModel() {
            return com.gogidix.finance.forecasting.domain.model.ForecastMetric.builder()
                    .metricId(this.metricId)
                    .forecastId(this.forecastId)
                    .metricName(this.metricName)
                    .metricCode(this.metricCode)
                    .category(this.category)
                    .subcategory(this.subcategory)
                    .amount(this.amount)
                    .previousAmount(this.previousAmount)
                    .variance(this.variance)
                    .variancePercentage(this.variancePercentage)
                    .period(this.period)
                    .periodStart(this.periodStart)
                    .periodEnd(this.periodEnd)
                    .metricType(this.metricType != null ? com.gogidix.finance.forecasting.domain.model.ForecastMetric.MetricType.valueOf(this.metricType) : null)
                    .unit(this.unit)
                    .weight(this.weight)
                    .confidenceLevel(this.confidenceLevel)
                    .dataSource(this.dataSource)
                    .notes(this.notes)
                    .sortOrder(this.sortOrder)
                    .isCalculated(this.isCalculated)
                    .calculationFormula(this.calculationFormula)
                    .createdAt(this.createdAt)
                    .updatedAt(this.updatedAt)
                    .build();
        }

        // Getters and setters
        public String getMetricId() {
            return metricId;
        }

        public void setMetricId(String metricId) {
            this.metricId = metricId;
        }

        public String getForecastId() {
            return forecastId;
        }

        public void setForecastId(String forecastId) {
            this.forecastId = forecastId;
        }

        public String getMetricName() {
            return metricName;
        }

        public void setMetricName(String metricName) {
            this.metricName = metricName;
        }

        public String getMetricCode() {
            return metricCode;
        }

        public void setMetricCode(String metricCode) {
            this.metricCode = metricCode;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getSubcategory() {
            return subcategory;
        }

        public void setSubcategory(String subcategory) {
            this.subcategory = subcategory;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getPreviousAmount() {
            return previousAmount;
        }

        public void setPreviousAmount(BigDecimal previousAmount) {
            this.previousAmount = previousAmount;
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

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public Instant getPeriodStart() {
            return periodStart;
        }

        public void setPeriodStart(Instant periodStart) {
            this.periodStart = periodStart;
        }

        public Instant getPeriodEnd() {
            return periodEnd;
        }

        public void setPeriodEnd(Instant periodEnd) {
            this.periodEnd = periodEnd;
        }

        public String getMetricType() {
            return metricType;
        }

        public void setMetricType(String metricType) {
            this.metricType = metricType;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public BigDecimal getWeight() {
            return weight;
        }

        public void setWeight(BigDecimal weight) {
            this.weight = weight;
        }

        public Integer getConfidenceLevel() {
            return confidenceLevel;
        }

        public void setConfidenceLevel(Integer confidenceLevel) {
            this.confidenceLevel = confidenceLevel;
        }

        public String getDataSource() {
            return dataSource;
        }

        public void setDataSource(String dataSource) {
            this.dataSource = dataSource;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public Integer getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(Integer sortOrder) {
            this.sortOrder = sortOrder;
        }

        public Boolean getIsCalculated() {
            return isCalculated;
        }

        public void setIsCalculated(Boolean isCalculated) {
            this.isCalculated = isCalculated;
        }

        public String getCalculationFormula() {
            return calculationFormula;
        }

        public void setCalculationFormula(String calculationFormula) {
            this.calculationFormula = calculationFormula;
        }

        public Instant getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Instant createdAt) {
            this.createdAt = createdAt;
        }

        public Instant getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
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

    public String getForecastType() {
        return forecastType;
    }

    public void setForecastType(String forecastType) {
        this.forecastType = forecastType;
    }

    public String getForecastHorizon() {
        return forecastHorizon;
    }

    public void setForecastHorizon(String forecastHorizon) {
        this.forecastHorizon = forecastHorizon;
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

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Instant getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Instant approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getTotalForecastAmount() {
        return totalForecastAmount;
    }

    public void setTotalForecastAmount(BigDecimal totalForecastAmount) {
        this.totalForecastAmount = totalForecastAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public BigDecimal getVarianceAmount() {
        return varianceAmount;
    }

    public void setVarianceAmount(BigDecimal varianceAmount) {
        this.varianceAmount = varianceAmount;
    }

    public BigDecimal getVariancePercentage() {
        return variancePercentage;
    }

    public void setVariancePercentage(BigDecimal variancePercentage) {
        this.variancePercentage = variancePercentage;
    }

    public Integer getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(Integer confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getScenario() {
        return scenario;
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    public List<ForecastMetricEmbed> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<ForecastMetricEmbed> metrics) {
        this.metrics = metrics;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getLastRegeneratedAt() {
        return lastRegeneratedAt;
    }

    public void setLastRegeneratedAt(Instant lastRegeneratedAt) {
        this.lastRegeneratedAt = lastRegeneratedAt;
    }

    public Integer getRegenerationCount() {
        return regenerationCount;
    }

    public void setRegenerationCount(Integer regenerationCount) {
        this.regenerationCount = regenerationCount;
    }

    public List<Object> getDomainEvents() {
        return domainEvents;
    }

    public void setDomainEvents(List<Object> domainEvents) {
        this.domainEvents = domainEvents;
    }
}

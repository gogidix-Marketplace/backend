package com.gogidix.hr.globalhrdashboard.domain.model;

import com.gogidix.hr.globalhrdashboard.shared.base.BaseEntity;
import com.gogidix.hr.globalhrdashboard.shared.exception.ValidationException;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Domain Entity - Retention Metric
 * Tracks employee retention and turnover metrics
 */
@Document(collection = "retention_metrics")
public class RetentionMetric extends BaseEntity {

    @Indexed
    private String countryCode;

    @Indexed
    private String countryName;

    @Indexed
    private String regionCode;

    @Indexed
    private String period;

    @Indexed
    private Double retentionRate;

    @Indexed
    private Double turnoverRate;

    @Indexed
    private Integer totalEmployees;

    @Indexed
    private Integer voluntaryDepartures;

    @Indexed
    private Integer involuntaryDepartures;

    private Double avgTenure;

    private Double medianTenure;

    private List<DepartureReason> departureReasons;

    private Double newHireRetentionRate;

    private Integer totalNewHires;

    private Integer retainedNewHires;

    private Double topPerformerRetentionRate;

    private Integer totalTopPerformers;

    private Integer retainedTopPerformers;

    private Double promotionRate;

    private Integer totalPromotions;

    private Double internalMobilityRate;

    @Indexed
    private Instant lastUpdated;

    private String notes;

    private Boolean isActive;

    protected RetentionMetric() {
        super();
        this.departureReasons = new ArrayList<>();
        this.isActive = true;
    }

    public RetentionMetric(String tenantId, String countryCode, String countryName,
                            String regionCode, Integer totalEmployees, Double retentionRate, String period) {
        super(tenantId);
        setCountryCode(countryCode);
        setCountryName(countryName);
        setRegionCode(regionCode);
        setTotalEmployees(totalEmployees);
        setRetentionRate(retentionRate);
        setPeriod(period);
        this.departureReasons = new ArrayList<>();
        this.isActive = true;
        this.lastUpdated = Instant.now();
    }

    public void setCountryCode(String countryCode) {
        if (countryCode == null || countryCode.isBlank()) {
            throw new ValidationException("countryCode", "Country code cannot be null or blank");
        }
        if (countryCode.length() != 2) {
            throw new ValidationException("countryCode", "Country code must be 2 characters (ISO 3166-1 alpha-2)");
        }
        this.countryCode = countryCode.toUpperCase();
        updateTimestamp();
    }

    public void setCountryName(String countryName) {
        if (countryName == null || countryName.isBlank()) {
            throw new ValidationException("countryName", "Country name cannot be null or blank");
        }
        this.countryName = countryName;
        updateTimestamp();
    }

    public void setRegionCode(String regionCode) {
        if (regionCode == null || regionCode.isBlank()) {
            throw new ValidationException("regionCode", "Region code cannot be null or blank");
        }
        this.regionCode = regionCode;
        updateTimestamp();
    }

    public void setTotalEmployees(Integer totalEmployees) {
        if (totalEmployees == null || totalEmployees < 0) {
            throw new ValidationException("totalEmployees", "Total employees must be non-negative");
        }
        this.totalEmployees = totalEmployees;
        this.lastUpdated = Instant.now();
        updateTimestamp();
    }

    public void setRetentionRate(Double retentionRate) {
        if (retentionRate == null || retentionRate < 0 || retentionRate > 100) {
            throw new ValidationException("retentionRate", "Retention rate must be between 0 and 100");
        }
        this.retentionRate = retentionRate;
        calculateTurnoverRate();
        this.lastUpdated = Instant.now();
        updateTimestamp();
    }

    public void setTurnoverRate(Double turnoverRate) {
        if (turnoverRate != null && (turnoverRate < 0 || turnoverRate > 100)) {
            throw new ValidationException("turnoverRate", "Turnover rate must be between 0 and 100");
        }
        this.turnoverRate = turnoverRate;
        updateTimestamp();
    }

    public void setVoluntaryDepartures(Integer voluntaryDepartures) {
        if (voluntaryDepartures != null && voluntaryDepartures < 0) {
            throw new ValidationException("voluntaryDepartures", "Voluntary departures must be non-negative");
        }
        this.voluntaryDepartures = voluntaryDepartures;
        updateTimestamp();
    }

    public void setInvoluntaryDepartures(Integer involuntaryDepartures) {
        if (involuntaryDepartures != null && involuntaryDepartures < 0) {
            throw new ValidationException("involuntaryDepartures", "Involuntary departures must be non-negative");
        }
        this.involuntaryDepartures = involuntaryDepartures;
        updateTimestamp();
    }

    public void setAvgTenure(Double avgTenure) {
        if (avgTenure != null && avgTenure < 0) {
            throw new ValidationException("avgTenure", "Average tenure must be non-negative");
        }
        this.avgTenure = avgTenure;
        updateTimestamp();
    }

    public void setMedianTenure(Double medianTenure) {
        if (medianTenure != null && medianTenure < 0) {
            throw new ValidationException("medianTenure", "Median tenure must be non-negative");
        }
        this.medianTenure = medianTenure;
        updateTimestamp();
    }

    public void setDepartureReasons(List<DepartureReason> departureReasons) {
        this.departureReasons = departureReasons != null ? departureReasons : new ArrayList<>();
        updateTimestamp();
    }

    public void setNewHireRetentionRate(Double newHireRetentionRate) {
        if (newHireRetentionRate != null && (newHireRetentionRate < 0 || newHireRetentionRate > 100)) {
            throw new ValidationException("newHireRetentionRate", "New hire retention rate must be between 0 and 100");
        }
        this.newHireRetentionRate = newHireRetentionRate;
        updateTimestamp();
    }

    public void setTotalNewHires(Integer totalNewHires) {
        if (totalNewHires != null && totalNewHires < 0) {
            throw new ValidationException("totalNewHires", "Total new hires must be non-negative");
        }
        this.totalNewHires = totalNewHires;
        updateTimestamp();
    }

    public void setRetainedNewHires(Integer retainedNewHires) {
        if (retainedNewHires != null && retainedNewHires < 0) {
            throw new ValidationException("retainedNewHires", "Retained new hires must be non-negative");
        }
        this.retainedNewHires = retainedNewHires;
        updateTimestamp();
    }

    public void setTopPerformerRetentionRate(Double topPerformerRetentionRate) {
        if (topPerformerRetentionRate != null && (topPerformerRetentionRate < 0 || topPerformerRetentionRate > 100)) {
            throw new ValidationException("topPerformerRetentionRate", "Top performer retention rate must be between 0 and 100");
        }
        this.topPerformerRetentionRate = topPerformerRetentionRate;
        updateTimestamp();
    }

    public void setTotalTopPerformers(Integer totalTopPerformers) {
        if (totalTopPerformers != null && totalTopPerformers < 0) {
            throw new ValidationException("totalTopPerformers", "Total top performers must be non-negative");
        }
        this.totalTopPerformers = totalTopPerformers;
        updateTimestamp();
    }

    public void setRetainedTopPerformers(Integer retainedTopPerformers) {
        if (retainedTopPerformers != null && retainedTopPerformers < 0) {
            throw new ValidationException("retainedTopPerformers", "Retained top performers must be non-negative");
        }
        this.retainedTopPerformers = retainedTopPerformers;
        updateTimestamp();
    }

    public void setPromotionRate(Double promotionRate) {
        if (promotionRate != null && (promotionRate < 0 || promotionRate > 100)) {
            throw new ValidationException("promotionRate", "Promotion rate must be between 0 and 100");
        }
        this.promotionRate = promotionRate;
        updateTimestamp();
    }

    public void setTotalPromotions(Integer totalPromotions) {
        if (totalPromotions != null && totalPromotions < 0) {
            throw new ValidationException("totalPromotions", "Total promotions must be non-negative");
        }
        this.totalPromotions = totalPromotions;
        updateTimestamp();
    }

    public void setInternalMobilityRate(Double internalMobilityRate) {
        if (internalMobilityRate != null && (internalMobilityRate < 0 || internalMobilityRate > 100)) {
            throw new ValidationException("internalMobilityRate", "Internal mobility rate must be between 0 and 100");
        }
        this.internalMobilityRate = internalMobilityRate;
        updateTimestamp();
    }

    public void setPeriod(String period) {
        if (period == null || period.isBlank()) {
            throw new ValidationException("period", "Period cannot be null or blank");
        }
        this.period = period;
        updateTimestamp();
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setNotes(String notes) {
        this.notes = notes;
        updateTimestamp();
    }

    public void setActive(Boolean active) {
        this.isActive = active;
        updateTimestamp();
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public String getPeriod() {
        return period;
    }

    public Double getRetentionRate() {
        return retentionRate;
    }

    public Double getTurnoverRate() {
        return turnoverRate;
    }

    public Integer getTotalEmployees() {
        return totalEmployees;
    }

    public Integer getVoluntaryDepartures() {
        return voluntaryDepartures;
    }

    public Integer getInvoluntaryDepartures() {
        return involuntaryDepartures;
    }

    public Double getAvgTenure() {
        return avgTenure;
    }

    public Double getMedianTenure() {
        return medianTenure;
    }

    public List<DepartureReason> getDepartureReasons() {
        return departureReasons;
    }

    public Double getNewHireRetentionRate() {
        return newHireRetentionRate;
    }

    public Integer getTotalNewHires() {
        return totalNewHires;
    }

    public Integer getRetainedNewHires() {
        return retainedNewHires;
    }

    public Double getTopPerformerRetentionRate() {
        return topPerformerRetentionRate;
    }

    public Integer getTotalTopPerformers() {
        return totalTopPerformers;
    }

    public Integer getRetainedTopPerformers() {
        return retainedTopPerformers;
    }

    public Double getPromotionRate() {
        return promotionRate;
    }

    public Integer getTotalPromotions() {
        return totalPromotions;
    }

    public Double getInternalMobilityRate() {
        return internalMobilityRate;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public String getNotes() {
        return notes;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public Integer getTotalDepartures() {
        int voluntary = voluntaryDepartures != null ? voluntaryDepartures : 0;
        int involuntary = involuntaryDepartures != null ? involuntaryDepartures : 0;
        return voluntary + involuntary;
    }

    public Double getVoluntaryTurnoverRate() {
        if (totalEmployees == null || totalEmployees == 0) {
            return 0.0;
        }
        int voluntary = voluntaryDepartures != null ? voluntaryDepartures : 0;
        return ((double) voluntary / totalEmployees) * 100;
    }

    public Double getInvoluntaryTurnoverRate() {
        if (totalEmployees == null || totalEmployees == 0) {
            return 0.0;
        }
        int involuntary = involuntaryDepartures != null ? involuntaryDepartures : 0;
        return ((double) involuntary / totalEmployees) * 100;
    }

    public void calculateTurnoverRate() {
        if (retentionRate != null) {
            this.turnoverRate = 100.0 - retentionRate;
        }
    }

    public boolean hasHealthyRetention() {
        return retentionRate != null && retentionRate >= 80.0;
    }

    public boolean hasHighTurnover() {
        return turnoverRate != null && turnoverRate > 20.0;
    }

    public boolean hasGoodTenure() {
        return avgTenure != null && avgTenure >= 2.0;
    }

    public void addDepartureReason(DepartureReason reason) {
        if (this.departureReasons == null) {
            this.departureReasons = new ArrayList<>();
        }
        this.departureReasons.add(reason);
        updateTimestamp();
    }

    public DepartureReason getTopDepartureReason() {
        if (departureReasons == null || departureReasons.isEmpty()) {
            return null;
        }
        return departureReasons.stream()
                .max((r1, r2) -> Integer.compare(r1.getCount(), r2.getCount()))
                .orElse(null);
    }

    public void recordUpdate() {
        this.lastUpdated = Instant.now();
        updateTimestamp();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RetentionMetric that = (RetentionMetric) o;
        return Objects.equals(countryCode, that.countryCode) &&
                Objects.equals(period, that.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), countryCode, period);
    }

    @Override
    public String toString() {
        return "RetentionMetric{" +
                "id='" + id + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", countryName='" + countryName + '\'' +
                ", retentionRate=" + retentionRate +
                ", turnoverRate=" + turnoverRate +
                ", period='" + period + '\'' +
                '}';
    }

    /**
     * Embedded class for departure reasons
     */
    public static class DepartureReason {
        private String reason;
        private String category;
        private Integer count;
        private Double percentage;
        private boolean preventable;
        private String trend;

        public enum Category {
            COMPENSATION, CAREER_GROWTH, WORK_LIFE_BALANCE, MANAGEMENT,
            COMPANY_CULTURE, RELOCATION, PERSONAL, RETIREMENT, OTHER
        }

        public DepartureReason() {
        }

        public DepartureReason(String reason, Category category, Integer count) {
            this.reason = reason;
            this.category = category != null ? category.name() : null;
            this.count = count;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }

        public boolean isPreventable() {
            return preventable;
        }

        public void setPreventable(boolean preventable) {
            this.preventable = preventable;
        }

        public String getTrend() {
            return trend;
        }

        public void setTrend(String trend) {
            this.trend = trend;
        }

        public void calculatePercentage(Integer totalDepartures) {
            if (totalDepartures != null && totalDepartures > 0 && count != null) {
                this.percentage = ((double) count / totalDepartures) * 100;
            }
        }
    }
}

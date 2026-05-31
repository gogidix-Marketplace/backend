package com.gogidix.hr.globalhrdashboard.domain.model;

import com.gogidix.hr.globalhrdashboard.shared.base.BaseEntity;
import com.gogidix.hr.globalhrdashboard.shared.exception.ValidationException;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain Entity - Country Headcount
 * Tracks headcount metrics at the country level
 */
@Document(collection = "country_headcount")
public class CountryHeadcount extends BaseEntity {

    @Indexed
    private String countryCode;

    @Indexed
    private String countryName;

    @Indexed
    private String regionCode;

    @Indexed
    private Integer totalHeadcount;

    private Integer permanentEmployees;

    private Integer contractors;

    private Integer interns;

    @Indexed
    private String department;

    @Indexed
    private String period;

    private Integer yoyChange;

    private Integer momChange;

    private Integer qoqChange;

    private Double femalePercentage;

    private Double malePercentage;

    private Double otherGenderPercentage;

    private Double avgAge;

    private Double avgTenureYears;

    @Indexed
    private Instant lastUpdated;

    private Boolean isActive;

    protected CountryHeadcount() {
        super();
        this.isActive = true;
    }

    public CountryHeadcount(String tenantId, String countryCode, String countryName,
                             String regionCode, Integer totalHeadcount, String period) {
        super(tenantId);
        setCountryCode(countryCode);
        setCountryName(countryName);
        setRegionCode(regionCode);
        setTotalHeadcount(totalHeadcount);
        setPeriod(period);
        this.isActive = true;
        this.lastUpdated = java.time.Instant.now();
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

    public void setTotalHeadcount(Integer totalHeadcount) {
        if (totalHeadcount == null || totalHeadcount < 0) {
            throw new ValidationException("totalHeadcount", "Total headcount must be non-negative");
        }
        this.totalHeadcount = totalHeadcount;
        this.lastUpdated = java.time.Instant.now();
        updateTimestamp();
    }

    public void setPermanentEmployees(Integer permanentEmployees) {
        if (permanentEmployees != null && permanentEmployees < 0) {
            throw new ValidationException("permanentEmployees", "Permanent employees must be non-negative");
        }
        this.permanentEmployees = permanentEmployees;
        updateTimestamp();
    }

    public void setContractors(Integer contractors) {
        if (contractors != null && contractors < 0) {
            throw new ValidationException("contractors", "Contractors must be non-negative");
        }
        this.contractors = contractors;
        updateTimestamp();
    }

    public void setInterns(Integer interns) {
        if (interns != null && interns < 0) {
            throw new ValidationException("interns", "Interns must be non-negative");
        }
        this.interns = interns;
        updateTimestamp();
    }

    public void setDepartment(String department) {
        this.department = department;
        updateTimestamp();
    }

    public void setPeriod(String period) {
        if (period == null || period.isBlank()) {
            throw new ValidationException("period", "Period cannot be null or blank");
        }
        this.period = period;
        updateTimestamp();
    }

    public void setYoyChange(Integer yoyChange) {
        this.yoyChange = yoyChange;
        updateTimestamp();
    }

    public void setMomChange(Integer momChange) {
        this.momChange = momChange;
        updateTimestamp();
    }

    public void setQoqChange(Integer qoqChange) {
        this.qoqChange = qoqChange;
        updateTimestamp();
    }

    public void setFemalePercentage(Double femalePercentage) {
        if (femalePercentage != null && (femalePercentage < 0 || femalePercentage > 100)) {
            throw new ValidationException("femalePercentage", "Female percentage must be between 0 and 100");
        }
        this.femalePercentage = femalePercentage;
        updateTimestamp();
    }

    public void setMalePercentage(Double malePercentage) {
        if (malePercentage != null && (malePercentage < 0 || malePercentage > 100)) {
            throw new ValidationException("malePercentage", "Male percentage must be between 0 and 100");
        }
        this.malePercentage = malePercentage;
        updateTimestamp();
    }

    public void setOtherGenderPercentage(Double otherGenderPercentage) {
        if (otherGenderPercentage != null && (otherGenderPercentage < 0 || otherGenderPercentage > 100)) {
            throw new ValidationException("otherGenderPercentage", "Other gender percentage must be between 0 and 100");
        }
        this.otherGenderPercentage = otherGenderPercentage;
        updateTimestamp();
    }

    public void setAvgAge(Double avgAge) {
        if (avgAge != null && (avgAge < 16 || avgAge > 80)) {
            throw new ValidationException("avgAge", "Average age must be between 16 and 80");
        }
        this.avgAge = avgAge;
        updateTimestamp();
    }

    public void setAvgTenureYears(Double avgTenureYears) {
        if (avgTenureYears != null && avgTenureYears < 0) {
            throw new ValidationException("avgTenureYears", "Average tenure years must be non-negative");
        }
        this.avgTenureYears = avgTenureYears;
        updateTimestamp();
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
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

    public Integer getTotalHeadcount() {
        return totalHeadcount;
    }

    public Integer getPermanentEmployees() {
        return permanentEmployees;
    }

    public Integer getContractors() {
        return contractors;
    }

    public Integer getInterns() {
        return interns;
    }

    public String getDepartment() {
        return department;
    }

    public String getPeriod() {
        return period;
    }

    public Integer getYoyChange() {
        return yoyChange;
    }

    public Integer getMomChange() {
        return momChange;
    }

    public Integer getQoqChange() {
        return qoqChange;
    }

    public Double getFemalePercentage() {
        return femalePercentage;
    }

    public Double getMalePercentage() {
        return malePercentage;
    }

    public Double getOtherGenderPercentage() {
        return otherGenderPercentage;
    }

    public Double getAvgAge() {
        return avgAge;
    }

    public Double getAvgTenureYears() {
        return avgTenureYears;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public Integer getCalculatedNonPermanent() {
        int permanent = permanentEmployees != null ? permanentEmployees : 0;
        int total = totalHeadcount != null ? totalHeadcount : 0;
        return Math.max(0, total - permanent);
    }

    public Double getContractorPercentage() {
        if (totalHeadcount == null || totalHeadcount == 0) {
            return 0.0;
        }
        int contract = contractors != null ? contractors : 0;
        return ((double) contract / totalHeadcount) * 100;
    }

    public Double getInternPercentage() {
        if (totalHeadcount == null || totalHeadcount == 0) {
            return 0.0;
        }
        int intern = interns != null ? interns : 0;
        return ((double) intern / totalHeadcount) * 100;
    }

    public Double getPermanentPercentage() {
        if (totalHeadcount == null || totalHeadcount == 0) {
            return 0.0;
        }
        int permanent = permanentEmployees != null ? permanentEmployees : 0;
        return ((double) permanent / totalHeadcount) * 100;
    }

    public MetricTrend getYoyTrend() {
        if (yoyChange == null || yoyChange == 0) {
            return MetricTrend.STABLE;
        }
        return yoyChange > 0 ? MetricTrend.UP : MetricTrend.DOWN;
    }

    public MetricTrend getMomTrend() {
        if (momChange == null || momChange == 0) {
            return MetricTrend.STABLE;
        }
        return momChange > 0 ? MetricTrend.UP : MetricTrend.DOWN;
    }

    public MetricTrend getQoqTrend() {
        if (qoqChange == null || qoqChange == 0) {
            return MetricTrend.STABLE;
        }
        return qoqChange > 0 ? MetricTrend.UP : MetricTrend.DOWN;
    }

    public boolean isHeadcountGrowing() {
        if (yoyChange != null) {
            return yoyChange > 0;
        }
        if (momChange != null) {
            return momChange > 0;
        }
        return false;
    }

    public void calculateChanges(Integer previousTotal, Integer previousQuarterTotal, Integer previousMonthTotal) {
        if (previousTotal != null) {
            this.yoyChange = totalHeadcount - previousTotal;
        }
        if (previousQuarterTotal != null) {
            this.qoqChange = totalHeadcount - previousQuarterTotal;
        }
        if (previousMonthTotal != null) {
            this.momChange = totalHeadcount - previousMonthTotal;
        }
        updateTimestamp();
    }

    public void recordUpdate() {
        this.lastUpdated = java.time.Instant.now();
        updateTimestamp();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CountryHeadcount that = (CountryHeadcount) o;
        return Objects.equals(countryCode, that.countryCode) &&
                Objects.equals(department, that.department) &&
                Objects.equals(period, that.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), countryCode, department, period);
    }

    @Override
    public String toString() {
        return "CountryHeadcount{" +
                "id='" + id + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", countryName='" + countryName + '\'' +
                ", totalHeadcount=" + totalHeadcount +
                ", department='" + department + '\'' +
                ", period='" + period + '\'' +
                '}';
    }
}

package com.gogidix.hr.globalhrdashboard.domain.model;

import com.gogidix.hr.globalhrdashboard.shared.base.BaseEntity;
import com.gogidix.hr.globalhrdashboard.shared.exception.ValidationException;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Domain Entity - Diversity Metric
 * Tracks diversity and inclusion metrics at country/region level
 */
@Document(collection = "diversity_metrics")
public class DiversityMetric extends BaseEntity {

    @Indexed
    private String countryCode;

    @Indexed
    private String countryName;

    @Indexed
    private String regionCode;

    @Indexed
    private String period;

    @Indexed
    private Integer totalEmployees;

    private Map<String, Integer> genderDistribution;

    private Map<String, Integer> ageDistribution;

    private Map<String, Integer> nationalityDistribution;

    private Map<String, Integer> ethnicityDistribution;

    private Map<String, Integer> educationLevelDistribution;

    @Indexed
    private Double genderDiversityScore;

    @Indexed
    private Double nationalDiversityScore;

    @Indexed
    private Double overallDiversityScore;

    private Double womenInLeadershipPercentage;

    private Integer womenInLeadershipCount;

    private Integer totalLeadershipCount;

    @Indexed
    private Instant lastUpdated;

    private Boolean isActive;

    protected DiversityMetric() {
        super();
        this.genderDistribution = new HashMap<>();
        this.ageDistribution = new HashMap<>();
        this.nationalityDistribution = new HashMap<>();
        this.ethnicityDistribution = new HashMap<>();
        this.educationLevelDistribution = new HashMap<>();
        this.isActive = true;
    }

    public DiversityMetric(String tenantId, String countryCode, String countryName,
                            String regionCode, Integer totalEmployees, String period) {
        super(tenantId);
        setCountryCode(countryCode);
        setCountryName(countryName);
        setRegionCode(regionCode);
        setTotalEmployees(totalEmployees);
        setPeriod(period);
        this.genderDistribution = new HashMap<>();
        this.ageDistribution = new HashMap<>();
        this.nationalityDistribution = new HashMap<>();
        this.ethnicityDistribution = new HashMap<>();
        this.educationLevelDistribution = new HashMap<>();
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

    public void setPeriod(String period) {
        if (period == null || period.isBlank()) {
            throw new ValidationException("period", "Period cannot be null or blank");
        }
        this.period = period;
        updateTimestamp();
    }

    public void setGenderDistribution(Map<String, Integer> genderDistribution) {
        this.genderDistribution = genderDistribution != null ? genderDistribution : new HashMap<>();
        calculateGenderDiversityScore();
        updateTimestamp();
    }

    public void setAgeDistribution(Map<String, Integer> ageDistribution) {
        this.ageDistribution = ageDistribution != null ? ageDistribution : new HashMap<>();
        updateTimestamp();
    }

    public void setNationalityDistribution(Map<String, Integer> nationalityDistribution) {
        this.nationalityDistribution = nationalityDistribution != null ? nationalityDistribution : new HashMap<>();
        calculateNationalDiversityScore();
        updateTimestamp();
    }

    public void setEthnicityDistribution(Map<String, Integer> ethnicityDistribution) {
        this.ethnicityDistribution = ethnicityDistribution != null ? ethnicityDistribution : new HashMap<>();
        updateTimestamp();
    }

    public void setEducationLevelDistribution(Map<String, Integer> educationLevelDistribution) {
        this.educationLevelDistribution = educationLevelDistribution != null ? educationLevelDistribution : new HashMap<>();
        updateTimestamp();
    }

    public void setGenderDiversityScore(Double genderDiversityScore) {
        this.genderDiversityScore = genderDiversityScore;
        updateTimestamp();
    }

    public void setNationalDiversityScore(Double nationalDiversityScore) {
        this.nationalDiversityScore = nationalDiversityScore;
        updateTimestamp();
    }

    public void setOverallDiversityScore(Double overallDiversityScore) {
        this.overallDiversityScore = overallDiversityScore;
        updateTimestamp();
    }

    public void setWomenInLeadershipPercentage(Double womenInLeadershipPercentage) {
        if (womenInLeadershipPercentage != null && (womenInLeadershipPercentage < 0 || womenInLeadershipPercentage > 100)) {
            throw new ValidationException("womenInLeadershipPercentage", "Percentage must be between 0 and 100");
        }
        this.womenInLeadershipPercentage = womenInLeadershipPercentage;
        updateTimestamp();
    }

    public void setWomenInLeadershipCount(Integer womenInLeadershipCount) {
        this.womenInLeadershipCount = womenInLeadershipCount;
        updateTimestamp();
    }

    public void setTotalLeadershipCount(Integer totalLeadershipCount) {
        this.totalLeadershipCount = totalLeadershipCount;
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

    public String getPeriod() {
        return period;
    }

    public Integer getTotalEmployees() {
        return totalEmployees;
    }

    public Map<String, Integer> getGenderDistribution() {
        return genderDistribution;
    }

    public Map<String, Integer> getAgeDistribution() {
        return ageDistribution;
    }

    public Map<String, Integer> getNationalityDistribution() {
        return nationalityDistribution;
    }

    public Map<String, Integer> getEthnicityDistribution() {
        return ethnicityDistribution;
    }

    public Map<String, Integer> getEducationLevelDistribution() {
        return educationLevelDistribution;
    }

    public Double getGenderDiversityScore() {
        return genderDiversityScore;
    }

    public Double getNationalDiversityScore() {
        return nationalDiversityScore;
    }

    public Double getOverallDiversityScore() {
        return overallDiversityScore;
    }

    public Double getWomenInLeadershipPercentage() {
        return womenInLeadershipPercentage;
    }

    public Integer getWomenInLeadershipCount() {
        return womenInLeadershipCount;
    }

    public Integer getTotalLeadershipCount() {
        return totalLeadershipCount;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void addGenderData(String gender, Integer count) {
        if (this.genderDistribution == null) {
            this.genderDistribution = new HashMap<>();
        }
        this.genderDistribution.put(gender, count);
        calculateGenderDiversityScore();
        updateTimestamp();
    }

    public void addAgeGroup(String ageGroup, Integer count) {
        if (this.ageDistribution == null) {
            this.ageDistribution = new HashMap<>();
        }
        this.ageDistribution.put(ageGroup, count);
        updateTimestamp();
    }

    public void addNationality(String nationality, Integer count) {
        if (this.nationalityDistribution == null) {
            this.nationalityDistribution = new HashMap<>();
        }
        this.nationalityDistribution.put(nationality, count);
        calculateNationalDiversityScore();
        updateTimestamp();
    }

    public Double getGenderPercentage(String gender) {
        if (genderDistribution == null || totalEmployees == null || totalEmployees == 0) {
            return 0.0;
        }
        Integer count = genderDistribution.get(gender);
        return count != null ? ((double) count / totalEmployees) * 100 : 0.0;
    }

    public Integer getGenderCount(String gender) {
        if (genderDistribution == null) {
            return 0;
        }
        return genderDistribution.getOrDefault(gender, 0);
    }

    public Double getNationalityPercentage(String nationality) {
        if (nationalityDistribution == null || totalEmployees == null || totalEmployees == 0) {
            return 0.0;
        }
        Integer count = nationalityDistribution.get(nationality);
        return count != null ? ((double) count / totalEmployees) * 100 : 0.0;
    }

    public Integer getTotalNationalities() {
        return nationalityDistribution != null ? nationalityDistribution.size() : 0;
    }

    public Integer getTotalGenderCategories() {
        return genderDistribution != null ? genderDistribution.size() : 0;
    }

    public Double getAverageAge() {
        if (ageDistribution == null || ageDistribution.isEmpty() || totalEmployees == null || totalEmployees == 0) {
            return 0.0;
        }
        double totalAge = 0.0;
        int totalCount = 0;

        for (Map.Entry<String, Integer> entry : ageDistribution.entrySet()) {
            String ageGroup = entry.getKey();
            Integer count = entry.getValue();
            double midPoint = getAgeGroupMidPoint(ageGroup);
            totalAge += midPoint * count;
            totalCount += count;
        }

        return totalCount > 0 ? totalAge / totalCount : 0.0;
    }

    private double getAgeGroupMidPoint(String ageGroup) {
        try {
            if (ageGroup.contains("-")) {
                String[] parts = ageGroup.split("-");
                if (parts.length == 2) {
                    int low = Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
                    int high = Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
                    return (low + high) / 2.0;
                }
            } else if (ageGroup.contains("+")) {
                String num = ageGroup.replaceAll("[^0-9]", "");
                return Double.parseDouble(num) + 5;
            }
        } catch (Exception e) {
            return 35.0;
        }
        return 35.0;
    }

    public void calculateGenderDiversityScore() {
        if (genderDistribution == null || genderDistribution.isEmpty() || totalEmployees == null || totalEmployees == 0) {
            this.genderDiversityScore = 0.0;
            return;
        }

        int totalCategories = genderDistribution.size();
        if (totalCategories <= 1) {
            this.genderDiversityScore = 0.0;
            return;
        }

        double total = 0.0;
        for (Integer count : genderDistribution.values()) {
            double proportion = (double) count / totalEmployees;
            total += proportion * proportion;
        }

        this.genderDiversityScore = (1 - total) * 100;
    }

    public void calculateNationalDiversityScore() {
        if (nationalityDistribution == null || nationalityDistribution.isEmpty() || totalEmployees == null || totalEmployees == 0) {
            this.nationalDiversityScore = 0.0;
            return;
        }

        int totalNationalities = nationalityDistribution.size();
        if (totalNationalities <= 1) {
            this.nationalDiversityScore = 0.0;
            return;
        }

        double total = 0.0;
        for (Integer count : nationalityDistribution.values()) {
            double proportion = (double) count / totalEmployees;
            total += proportion * proportion;
        }

        this.nationalDiversityScore = (1 - total) * 100;
    }

    public void calculateOverallDiversityScore() {
        double genderWeight = 0.4;
        double nationalWeight = 0.3;
        double ageWeight = 0.2;
        double ethnicityWeight = 0.1;

        double genderScore = genderDiversityScore != null ? genderDiversityScore : 0.0;
        double nationalScore = nationalDiversityScore != null ? nationalDiversityScore : 0.0;

        double ageScore = 0.0;
        if (ageDistribution != null && ageDistribution.size() > 1) {
            ageScore = 50.0;
        }

        double ethnicityScore = 0.0;
        if (ethnicityDistribution != null && ethnicityDistribution.size() > 1) {
            ethnicityScore = 50.0;
        }

        this.overallDiversityScore = (genderScore * genderWeight) +
                (nationalScore * nationalWeight) +
                (ageScore * ageWeight) +
                (ethnicityScore * ethnicityWeight);
    }

    public boolean hasGoodGenderBalance() {
        return genderDiversityScore != null && genderDiversityScore >= 50.0;
    }

    public boolean hasHighNationalDiversity() {
        return nationalDiversityScore != null && nationalDiversityScore >= 60.0;
    }

    public boolean hasGoodLeadershipDiversity() {
        return womenInLeadershipPercentage != null && womenInLeadershipPercentage >= 30.0;
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
        DiversityMetric that = (DiversityMetric) o;
        return Objects.equals(countryCode, that.countryCode) &&
                Objects.equals(period, that.period);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), countryCode, period);
    }

    @Override
    public String toString() {
        return "DiversityMetric{" +
                "id='" + id + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", countryName='" + countryName + '\'' +
                ", totalEmployees=" + totalEmployees +
                ", genderDiversityScore=" + genderDiversityScore +
                ", period='" + period + '\'' +
                '}';
    }
}

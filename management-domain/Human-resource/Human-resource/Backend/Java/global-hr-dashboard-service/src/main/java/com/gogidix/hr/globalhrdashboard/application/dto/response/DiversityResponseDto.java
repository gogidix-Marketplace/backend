package com.gogidix.hr.globalhrdashboard.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Response DTO for Diversity Metrics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Diversity metric response")
public class DiversityResponseDto {

    @Schema(description = "Diversity metric ID")
    private String id;

    @Schema(description = "ISO country code")
    private String countryCode;

    @Schema(description = "Country name")
    private String countryName;

    @Schema(description = "Region code")
    private String regionCode;

    @Schema(description = "Period")
    private String period;

    @Schema(description = "Total employees")
    private Integer totalEmployees;

    @Schema(description = "Gender distribution")
    private Map<String, Integer> genderDistribution;

    @Schema(description = "Age distribution")
    private Map<String, Integer> ageDistribution;

    @Schema(description = "Nationality distribution")
    private Map<String, Integer> nationalityDistribution;

    @Schema(description = "Ethnicity distribution")
    private Map<String, Integer> ethnicityDistribution;

    @Schema(description = "Education level distribution")
    private Map<String, Integer> educationLevelDistribution;

    @Schema(description = "Gender diversity score")
    private Double genderDiversityScore;

    @Schema(description = "National diversity score")
    private Double nationalDiversityScore;

    @Schema(description = "Overall diversity score")
    private Double overallDiversityScore;

    @Schema(description = "Women in leadership percentage")
    private Double womenInLeadershipPercentage;

    @Schema(description = "Women in leadership count")
    private Integer womenInLeadershipCount;

    @Schema(description = "Total leadership count")
    private Integer totalLeadershipCount;

    @Schema(description = "Is active")
    private Boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(description = "Last updated timestamp")
    private Instant lastUpdated;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(description = "Created at timestamp")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    @Schema(description = "Updated at timestamp")
    private Instant updatedAt;

    @Schema(description = "Get gender percentage")
    public Double getGenderPercentage(String gender) {
        if (genderDistribution == null || totalEmployees == null || totalEmployees == 0) {
            return 0.0;
        }
        Integer count = genderDistribution.get(gender);
        return count != null ? ((double) count / totalEmployees) * 100 : 0.0;
    }

    @Schema(description = "Get nationality percentage")
    public Double getNationalityPercentage(String nationality) {
        if (nationalityDistribution == null || totalEmployees == null || totalEmployees == 0) {
            return 0.0;
        }
        Integer count = nationalityDistribution.get(nationality);
        return count != null ? ((double) count / totalEmployees) * 100 : 0.0;
    }

    @Schema(description = "Total nationalities")
    public Integer getTotalNationalities() {
        return nationalityDistribution != null ? nationalityDistribution.size() : 0;
    }

    @Schema(description = "Total gender categories")
    public Integer getTotalGenderCategories() {
        return genderDistribution != null ? genderDistribution.size() : 0;
    }

    @Schema(description = "Average age")
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

    @Schema(description = "Has good gender balance")
    public boolean hasGoodGenderBalance() {
        return genderDiversityScore != null && genderDiversityScore >= 50.0;
    }

    @Schema(description = "Has high national diversity")
    public boolean hasHighNationalDiversity() {
        return nationalDiversityScore != null && nationalDiversityScore >= 60.0;
    }

    @Schema(description = "Has good leadership diversity")
    public boolean hasGoodLeadershipDiversity() {
        return womenInLeadershipPercentage != null && womenInLeadershipPercentage >= 30.0;
    }
}

package com.gogidix.globalbusinessmanagement.countryingestion.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.*;

/**
 * Domain model representing country data for global business management.
 * Contains comprehensive country information including geographic, economic,
 * and business-related data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "country_data")
public class CountryData {

    @Id
    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "Country code is required")
    @Pattern(regexp = "^[A-Z]{2}$", message = "ISO 3166-1 alpha-2 code must be 2 uppercase letters")
    private String countryCode;

    @Indexed(unique = true)
    @NotBlank(message = "Country name is required")
    private String countryName;

    @Indexed
    @Pattern(regexp = "^[A-Z]{3}$", message = "ISO 3166-1 alpha-3 code must be 3 uppercase letters")
    private String isoCodeAlpha3;

    @Indexed
    @NotNull(message = "Numeric ISO code is required")
    @Min(value = 1, message = "Numeric ISO code must be positive")
    @Max(value = 999, message = "Numeric ISO code must be less than 1000")
    private Integer isoNumericCode;

    @Indexed
    @NotBlank(message = "Region is required")
    private String region;

    @Indexed
    private String subRegion;

    private String continent;

    @Indexed
    private String capitalCity;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private BigDecimal totalAreaSqKm;

    private BigDecimal landAreaSqKm;

    private BigDecimal waterAreaSqKm;

    @Indexed
    private BigDecimal population;

    private BigDecimal populationDensity;

    private Integer populationGrowthRate;

    private BigDecimal urbanPopulationPercent;

    private BigDecimal ruralPopulationPercent;

    @Indexed
    private String currencyCode;

    private String currencyName;

    private String currencySymbol;

    @Indexed
    private String callingCode;

    @Indexed
    private String internetTld;

    private Set<String> languages = new HashSet<>();

    private String officialLanguage;

    private Set<String> borderCountries = new HashSet<>();

    private String timeZone;

    private Set<String> timeZones = new HashSet<>();

    @Indexed
    private BigDecimal gdpUsd;

    private BigDecimal gdpPerCapitaUsd;

    private BigDecimal gdpGrowthRate;

    private BigDecimal inflationRate;

    private BigDecimal unemploymentRate;

    private BigDecimal interestRate;

    private BigDecimal governmentDebtPercent;

    private BigDecimal foreignExchangeReserves;

    private String economicStatus;

    @Indexed
    private String incomeLevel;

    private String developmentStatus;

    private Integer easeOfDoingBusinessRank;

    private Integer corruptionPerceptionIndex;

    private Integer humanDevelopmentIndex;

    private Double happinessIndex;

    @Indexed
    private String politicalSystem;

    private String legalSystem;

    private String governmentType;

    private LocalDate independenceDate;

    private String euMember;

    private String unMember;

    private String natoMember;

    private String wtoMember;

    private Set<String> tradeAgreements = new HashSet<>();

    private Set<String> regionalOrganizations = new HashSet<>();

    private BigDecimal corporateTaxRate;

    private BigDecimal vatRate;

    private BigDecimal incomeTaxRate;

    private Double importDutyRate;

    private String freeTradeZones;

    private Set<String> majorIndustries = new HashSet<>();

    private Set<String> majorExports = new HashSet<>();

    private Set<String> majorImports = new HashSet<>();

    private BigDecimal exportsUsd;

    private BigDecimal importsUsd;

    private BigDecimal tradeBalanceUsd;

    private String majorTradingPartners;

    @Indexed
    private String dataSource;

    private String dataSourceUrl;

    @Indexed
    private LocalDateTime dataLastUpdated;

    @Indexed
    private LocalDateTime ingestionTimestamp;

    @Indexed
    private String ingestionBatchId;

    @Transient
    private String ingestionStatus;

    @Builder.Default
    private Boolean validated = false;

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private Integer dataQualityScore = 100;

    private String dataQualityNotes;

    @Transient
    private List<String> validationErrors = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> additionalProperties = new HashMap<>();

    @DBRef(lazy = true)
    private List<ValidationError> validationErrorsList;

    /**
     * Calculates the data quality score based on completeness of required fields.
     */
    public void calculateDataQualityScore() {
        int totalFields = 30;
        int completedFields = 0;

        if (countryCode != null && !countryCode.isEmpty()) completedFields++;
        if (countryName != null && !countryName.isEmpty()) completedFields++;
        if (region != null && !region.isEmpty()) completedFields++;
        if (capitalCity != null && !capitalCity.isEmpty()) completedFields++;
        if (population != null) completedFields++;
        if (gdpUsd != null) completedFields++;
        if (currencyCode != null && !currencyCode.isEmpty()) completedFields++;
        if (languages != null && !languages.isEmpty()) completedFields++;
        if (latitude != null && longitude != null) completedFields++;
        if (totalAreaSqKm != null) completedFields++;
        if (callingCode != null && !callingCode.isEmpty()) completedFields++;
        if (internetTld != null && !internetTld.isEmpty()) completedFields++;
        if (gdpPerCapitaUsd != null) completedFields++;
        if (inflationRate != null) completedFields++;
        if (unemploymentRate != null) completedFields++;
        if (incomeLevel != null && !incomeLevel.isEmpty()) completedFields++;
        if (politicalSystem != null && !politicalSystem.isEmpty()) completedFields++;
        if (corporateTaxRate != null) completedFields++;
        if (vatRate != null) completedFields++;
        if (majorIndustries != null && !majorIndustries.isEmpty()) completedFields++;
        if (majorExports != null && !majorExports.isEmpty()) completedFields++;
        if (majorImports != null && !majorImports.isEmpty()) completedFields++;
        if (exportsUsd != null) completedFields++;
        if (importsUsd != null) completedFields++;
        if (dataLastUpdated != null) completedFields++;
        if (dataSource != null && !dataSource.isEmpty()) completedFields++;
        if (timeZone != null && !timeZone.isEmpty()) completedFields++;
        if (officialLanguage != null && !officialLanguage.isEmpty()) completedFields++;
        if (humanDevelopmentIndex != null) completedFields++;

        this.dataQualityScore = (completedFields * 100) / totalFields;
    }

    /**
     * Checks if the country data is considered complete based on required fields.
     */
    public boolean isComplete() {
        return countryCode != null && !countryCode.isEmpty() &&
                countryName != null && !countryName.isEmpty() &&
                region != null && !region.isEmpty() &&
                population != null &&
                currencyCode != null && !currencyCode.isEmpty();
    }

    /**
     * Updates the last updated timestamp to current time.
     */
    public void updateTimestamp() {
        this.dataLastUpdated = LocalDateTime.now();
    }

    /**
     * Gets the country identifier (ISO code).
     */
    public String getCountryIdentifier() {
        return countryCode != null ? countryCode : countryName;
    }
}

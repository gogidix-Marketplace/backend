package com.gogidix.globalbusinessmanagement.countryingestion.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * DTO for CountryData entity.
 * Used for transferring country data between layers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Country Data DTO containing comprehensive country information")
public class CountryDataDto {

    @Schema(description = "Unique identifier")
    private String id;

    @Schema(description = "ISO 3166-1 alpha-2 country code (2 letters)", example = "US")
    @NotBlank(message = "Country code is required")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Country code must be 2 uppercase letters")
    private String countryCode;

    @Schema(description = "Country name", example = "United States")
    @NotBlank(message = "Country name is required")
    private String countryName;

    @Schema(description = "ISO 3166-1 alpha-3 code (3 letters)", example = "USA")
    @Pattern(regexp = "^[A-Z]{3}$", message = "ISO alpha-3 code must be 3 uppercase letters")
    private String isoCodeAlpha3;

    @Schema(description = "ISO numeric code", example = "840")
    @Min(value = 1, message = "Numeric ISO code must be positive")
    @Max(value = 999, message = "Numeric ISO code must be less than 1000")
    private Integer isoNumericCode;

    @Schema(description = "Geographic region", example = "Americas")
    @NotBlank(message = "Region is required")
    private String region;

    @Schema(description = "Sub-region", example = "Northern America")
    private String subRegion;

    @Schema(description = "Continent", example = "North America")
    private String continent;

    @Schema(description = "Capital city", example = "Washington, D.C.")
    private String capitalCity;

    @Schema(description = "Latitude coordinate", example = "37.0902")
    private BigDecimal latitude;

    @Schema(description = "Longitude coordinate", example = "-95.7129")
    private BigDecimal longitude;

    @Schema(description = "Total area in square kilometers", example = "9833520")
    private BigDecimal totalAreaSqKm;

    @Schema(description = "Land area in square kilometers")
    private BigDecimal landAreaSqKm;

    @Schema(description = "Water area in square kilometers")
    private BigDecimal waterAreaSqKm;

    @Schema(description = "Population", example = "331000000")
    private BigDecimal population;

    @Schema(description = "Population density per square kilometer")
    private BigDecimal populationDensity;

    @Schema(description = "Population growth rate percentage")
    private Integer populationGrowthRate;

    @Schema(description = "Urban population percentage")
    private BigDecimal urbanPopulationPercent;

    @Schema(description = "Rural population percentage")
    private BigDecimal ruralPopulationPercent;

    @Schema(description = "Currency code (ISO 4217)", example = "USD")
    private String currencyCode;

    @Schema(description = "Currency name", example = "US Dollar")
    private String currencyName;

    @Schema(description = "Currency symbol", example = "$")
    private String currencySymbol;

    @Schema(description = "International calling code", example = "+1")
    private String callingCode;

    @Schema(description = "Top-level domain", example = ".us")
    private String internetTld;

    @Schema(description = "Languages spoken in the country")
    private Set<String> languages;

    @Schema(description = "Official language")
    private String officialLanguage;

    @Schema(description = "Bordering country codes")
    private Set<String> borderCountries;

    @Schema(description = "Primary time zone", example = "America/New_York")
    private String timeZone;

    @Schema(description = "All time zones in the country")
    private Set<String> timeZones;

    @Schema(description = "GDP in USD", example = "23324808000000")
    private BigDecimal gdpUsd;

    @Schema(description = "GDP per capita in USD", example = "65280")
    private BigDecimal gdpPerCapitaUsd;

    @Schema(description = "GDP growth rate percentage")
    private BigDecimal gdpGrowthRate;

    @Schema(description = "Inflation rate percentage")
    private BigDecimal inflationRate;

    @Schema(description = "Unemployment rate percentage")
    private BigDecimal unemploymentRate;

    @Schema(description = "Interest rate percentage")
    private BigDecimal interestRate;

    @Schema(description = "Government debt as percentage of GDP")
    private BigDecimal governmentDebtPercent;

    @Schema(description = "Foreign exchange reserves in USD")
    private BigDecimal foreignExchangeReserves;

    @Schema(description = "Economic status")
    private String economicStatus;

    @Schema(description = "Income level classification", example = "High")
    private String incomeLevel;

    @Schema(description = "Development status", example = "Developed")
    private String developmentStatus;

    @Schema(description = "Ease of doing business rank")
    private Integer easeOfDoingBusinessRank;

    @Schema(description = "Corruption Perception Index score")
    private Integer corruptionPerceptionIndex;

    @Schema(description = "Human Development Index score (0-1)")
    private Integer humanDevelopmentIndex;

    @Schema(description = "Happiness Index score")
    private Double happinessIndex;

    @Schema(description = "Political system", example = "Federal Republic")
    private String politicalSystem;

    @Schema(description = "Legal system")
    private String legalSystem;

    @Schema(description = "Type of government")
    private String governmentType;

    @Schema(description = "Independence date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate independenceDate;

    @Schema(description = "EU member status", example = "No")
    private String euMember;

    @Schema(description = "UN member status", example = "Yes")
    private String unMember;

    @Schema(description = "NATO member status", example = "Yes")
    private String natoMember;

    @Schema(description = "WTO member status", example = "Yes")
    private String wtoMember;

    @Schema(description = "Trade agreements")
    private Set<String> tradeAgreements;

    @Schema(description = "Regional organizations")
    private Set<String> regionalOrganizations;

    @Schema(description = "Corporate tax rate percentage")
    private BigDecimal corporateTaxRate;

    @Schema(description = "VAT rate percentage")
    private BigDecimal vatRate;

    @Schema(description = "Income tax rate percentage")
    private BigDecimal incomeTaxRate;

    @Schema(description = "Import duty rate percentage")
    private Double importDutyRate;

    @Schema(description = "Free trade zones information")
    private String freeTradeZones;

    @Schema(description = "Major industries")
    private Set<String> majorIndustries;

    @Schema(description = "Major exports")
    private Set<String> majorExports;

    @Schema(description = "Major imports")
    private Set<String> majorImports;

    @Schema(description = "Total exports value in USD")
    private BigDecimal exportsUsd;

    @Schema(description = "Total imports value in USD")
    private BigDecimal importsUsd;

    @Schema(description = "Trade balance in USD")
    private BigDecimal tradeBalanceUsd;

    @Schema(description = "Major trading partners")
    private String majorTradingPartners;

    @Schema(description = "Data source name")
    private String dataSource;

    @Schema(description = "Data source URL")
    private String dataSourceUrl;

    @Schema(description = "Last data update timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataLastUpdated;

    @Schema(description = "Ingestion timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime ingestionTimestamp;

    @Schema(description = "Ingestion batch ID")
    private String ingestionBatchId;

    @Schema(description = "Validation status")
    private Boolean validated;

    @Schema(description = "Active status")
    private Boolean active;

    @Schema(description = "Data quality score (0-100)")
    private Integer dataQualityScore;

    @Schema(description = "Data quality notes")
    private String dataQualityNotes;

    @Schema(description = "Additional properties")
    private Map<String, Object> additionalProperties;

    /**
     * Creates a minimal DTO with essential fields.
     */
    public static CountryDataDto createMinimal(String countryCode, String countryName, String region) {
        return CountryDataDto.builder()
                .countryCode(countryCode)
                .countryName(countryName)
                .region(region)
                .validated(false)
                .active(true)
                .build();
    }

    /**
     * Checks if the DTO has valid coordinates.
     */
    public boolean hasValidCoordinates() {
        return latitude != null && longitude != null &&
                latitude.compareTo(new BigDecimal("-90")) >= 0 &&
                latitude.compareTo(new BigDecimal("90")) <= 0 &&
                longitude.compareTo(new BigDecimal("-180")) >= 0 &&
                longitude.compareTo(new BigDecimal("180")) <= 0;
    }

    /**
     * Gets the country identifier for logging.
     */
    public String getIdentifier() {
        return countryCode != null ? countryCode : countryName;
    }
}

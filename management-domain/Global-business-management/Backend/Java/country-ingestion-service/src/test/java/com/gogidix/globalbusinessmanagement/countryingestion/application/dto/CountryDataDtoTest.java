package com.gogidix.globalbusinessmanagement.countryingestion.application.dto;

import com.gogidix.globalbusinessmanagement.countryingestion.application.dto.CountryDataDto;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CountryDataDtoTest {

        @Test
    void testBuilder() {
        CountryDataDto dto = CountryDataDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .isoCodeAlpha3("test-isoCodeAlpha3")
            .isoNumericCode(42)
            .region("test-region")
            .subRegion("test-subRegion")
            .continent("test-continent")
            .capitalCity("test-capitalCity")
            .latitude(BigDecimal.TEN)
            .longitude(BigDecimal.TEN)
            .totalAreaSqKm(BigDecimal.TEN)
            .landAreaSqKm(BigDecimal.TEN)
            .waterAreaSqKm(BigDecimal.TEN)
            .population(BigDecimal.TEN)
            .populationDensity(BigDecimal.TEN)
            .populationGrowthRate(42)
            .urbanPopulationPercent(BigDecimal.TEN)
            .ruralPopulationPercent(BigDecimal.TEN)
            .currencyCode("test-currencyCode")
            .currencyName("test-currencyName")
            .currencySymbol("test-currencySymbol")
            .callingCode("test-callingCode")
            .internetTld("test-internetTld")
            .languages(null)
            .officialLanguage("test-officialLanguage")
            .borderCountries(null)
            .timeZone("test-timeZone")
            .timeZones(null)
            .gdpUsd(BigDecimal.TEN)
            .gdpPerCapitaUsd(BigDecimal.TEN)
            .gdpGrowthRate(BigDecimal.TEN)
            .inflationRate(BigDecimal.TEN)
            .unemploymentRate(BigDecimal.TEN)
            .interestRate(BigDecimal.TEN)
            .governmentDebtPercent(BigDecimal.TEN)
            .foreignExchangeReserves(BigDecimal.TEN)
            .economicStatus("test-economicStatus")
            .incomeLevel("test-incomeLevel")
            .developmentStatus("test-developmentStatus")
            .easeOfDoingBusinessRank(42)
            .corruptionPerceptionIndex(42)
            .humanDevelopmentIndex(42)
            .happinessIndex(null)
            .politicalSystem("test-politicalSystem")
            .legalSystem("test-legalSystem")
            .governmentType("test-governmentType")
            .independenceDate(LocalDate.of(2025,1,15))
            .euMember("test-euMember")
            .unMember("test-unMember")
            .natoMember("test-natoMember")
            .wtoMember("test-wtoMember")
            .tradeAgreements(null)
            .regionalOrganizations(null)
            .corporateTaxRate(BigDecimal.TEN)
            .vatRate(BigDecimal.TEN)
            .incomeTaxRate(BigDecimal.TEN)
            .importDutyRate(null)
            .freeTradeZones("test-freeTradeZones")
            .majorIndustries(null)
            .majorExports(null)
            .majorImports(null)
            .exportsUsd(BigDecimal.TEN)
            .importsUsd(BigDecimal.TEN)
            .tradeBalanceUsd(BigDecimal.TEN)
            .majorTradingPartners("test-majorTradingPartners")
            .dataSource("test-dataSource")
            .dataSourceUrl("test-dataSourceUrl")
            .dataLastUpdated(LocalDateTime.of(2025,1,15,10,0))
            .ingestionTimestamp(LocalDateTime.of(2025,1,15,10,0))
            .ingestionBatchId("test-ingestionBatchId")
            .validated(true)
            .active(true)
            .dataQualityScore(42)
            .dataQualityNotes("test-dataQualityNotes")
            .additionalProperties(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals("test-countryName", dto.getCountryName());
        assertEquals("test-isoCodeAlpha3", dto.getIsoCodeAlpha3());
        assertEquals(42, dto.getIsoNumericCode());
        assertEquals("test-region", dto.getRegion());
        assertEquals("test-subRegion", dto.getSubRegion());
        assertEquals("test-continent", dto.getContinent());
        assertEquals("test-capitalCity", dto.getCapitalCity());
        assertEquals(BigDecimal.TEN, dto.getLatitude());
        assertEquals(BigDecimal.TEN, dto.getLongitude());
        assertEquals(BigDecimal.TEN, dto.getTotalAreaSqKm());
        assertEquals(BigDecimal.TEN, dto.getLandAreaSqKm());
        assertEquals(BigDecimal.TEN, dto.getWaterAreaSqKm());
        assertEquals(BigDecimal.TEN, dto.getPopulation());
        assertEquals(BigDecimal.TEN, dto.getPopulationDensity());
        assertEquals(42, dto.getPopulationGrowthRate());
        assertEquals(BigDecimal.TEN, dto.getUrbanPopulationPercent());
        assertEquals(BigDecimal.TEN, dto.getRuralPopulationPercent());
        assertEquals("test-currencyCode", dto.getCurrencyCode());
        assertEquals("test-currencyName", dto.getCurrencyName());
        assertEquals("test-currencySymbol", dto.getCurrencySymbol());
        assertEquals("test-callingCode", dto.getCallingCode());
        assertEquals("test-internetTld", dto.getInternetTld());
        assertEquals("test-officialLanguage", dto.getOfficialLanguage());
        assertEquals("test-timeZone", dto.getTimeZone());
        assertEquals(BigDecimal.TEN, dto.getGdpUsd());
        assertEquals(BigDecimal.TEN, dto.getGdpPerCapitaUsd());
        assertEquals(BigDecimal.TEN, dto.getGdpGrowthRate());
        assertEquals(BigDecimal.TEN, dto.getInflationRate());
        assertEquals(BigDecimal.TEN, dto.getUnemploymentRate());
        assertEquals(BigDecimal.TEN, dto.getInterestRate());
        assertEquals(BigDecimal.TEN, dto.getGovernmentDebtPercent());
        assertEquals(BigDecimal.TEN, dto.getForeignExchangeReserves());
        assertEquals("test-economicStatus", dto.getEconomicStatus());
        assertEquals("test-incomeLevel", dto.getIncomeLevel());
        assertEquals("test-developmentStatus", dto.getDevelopmentStatus());
        assertEquals(42, dto.getEaseOfDoingBusinessRank());
        assertEquals(42, dto.getCorruptionPerceptionIndex());
        assertEquals(42, dto.getHumanDevelopmentIndex());
        assertEquals("test-politicalSystem", dto.getPoliticalSystem());
        assertEquals("test-legalSystem", dto.getLegalSystem());
        assertEquals("test-governmentType", dto.getGovernmentType());
        assertEquals(LocalDate.of(2025,1,15), dto.getIndependenceDate());
        assertEquals("test-euMember", dto.getEuMember());
        assertEquals("test-unMember", dto.getUnMember());
        assertEquals("test-natoMember", dto.getNatoMember());
        assertEquals("test-wtoMember", dto.getWtoMember());
        assertEquals(BigDecimal.TEN, dto.getCorporateTaxRate());
        assertEquals(BigDecimal.TEN, dto.getVatRate());
        assertEquals(BigDecimal.TEN, dto.getIncomeTaxRate());
        assertEquals("test-freeTradeZones", dto.getFreeTradeZones());
        assertEquals(BigDecimal.TEN, dto.getExportsUsd());
        assertEquals(BigDecimal.TEN, dto.getImportsUsd());
        assertEquals(BigDecimal.TEN, dto.getTradeBalanceUsd());
        assertEquals("test-majorTradingPartners", dto.getMajorTradingPartners());
        assertEquals("test-dataSource", dto.getDataSource());
        assertEquals("test-dataSourceUrl", dto.getDataSourceUrl());
        assertEquals("test-ingestionBatchId", dto.getIngestionBatchId());
        assertTrue(dto.getValidated());
        assertTrue(dto.getActive());
        assertEquals(42, dto.getDataQualityScore());
        assertEquals("test-dataQualityNotes", dto.getDataQualityNotes());
    }

    @Test
    void testSettersAndGetters() {
        CountryDataDto dto = new CountryDataDto();
        dto.setId("val-id");
        dto.setCountryCode("val-countryCode");
        dto.setCountryName("val-countryName");
        dto.setIsoCodeAlpha3("val-isoCodeAlpha3");
        dto.setIsoNumericCode(99);
        dto.setRegion("val-region");
        dto.setSubRegion("val-subRegion");
        dto.setContinent("val-continent");
        dto.setCapitalCity("val-capitalCity");
        dto.setLatitude(BigDecimal.ONE);
        dto.setLongitude(BigDecimal.ONE);
        dto.setTotalAreaSqKm(BigDecimal.ONE);
        dto.setLandAreaSqKm(BigDecimal.ONE);
        dto.setWaterAreaSqKm(BigDecimal.ONE);
        dto.setPopulation(BigDecimal.ONE);
        dto.setPopulationDensity(BigDecimal.ONE);
        dto.setPopulationGrowthRate(99);
        dto.setUrbanPopulationPercent(BigDecimal.ONE);
        dto.setRuralPopulationPercent(BigDecimal.ONE);
        dto.setCurrencyCode("val-currencyCode");
        dto.setCurrencyName("val-currencyName");
        dto.setCurrencySymbol("val-currencySymbol");
        dto.setCallingCode("val-callingCode");
        dto.setInternetTld("val-internetTld");
        dto.setOfficialLanguage("val-officialLanguage");
        dto.setTimeZone("val-timeZone");
        dto.setGdpUsd(BigDecimal.ONE);
        dto.setGdpPerCapitaUsd(BigDecimal.ONE);
        dto.setGdpGrowthRate(BigDecimal.ONE);
        dto.setInflationRate(BigDecimal.ONE);
        dto.setUnemploymentRate(BigDecimal.ONE);
        dto.setInterestRate(BigDecimal.ONE);
        dto.setGovernmentDebtPercent(BigDecimal.ONE);
        dto.setForeignExchangeReserves(BigDecimal.ONE);
        dto.setEconomicStatus("val-economicStatus");
        dto.setIncomeLevel("val-incomeLevel");
        dto.setDevelopmentStatus("val-developmentStatus");
        dto.setEaseOfDoingBusinessRank(99);
        dto.setCorruptionPerceptionIndex(99);
        dto.setHumanDevelopmentIndex(99);
        dto.setPoliticalSystem("val-politicalSystem");
        dto.setLegalSystem("val-legalSystem");
        dto.setGovernmentType("val-governmentType");
        dto.setIndependenceDate(LocalDate.of(2025,6,1));
        dto.setEuMember("val-euMember");
        dto.setUnMember("val-unMember");
        dto.setNatoMember("val-natoMember");
        dto.setWtoMember("val-wtoMember");
        dto.setCorporateTaxRate(BigDecimal.ONE);
        dto.setVatRate(BigDecimal.ONE);
        dto.setIncomeTaxRate(BigDecimal.ONE);
        dto.setFreeTradeZones("val-freeTradeZones");
        dto.setExportsUsd(BigDecimal.ONE);
        dto.setImportsUsd(BigDecimal.ONE);
        dto.setTradeBalanceUsd(BigDecimal.ONE);
        dto.setMajorTradingPartners("val-majorTradingPartners");
        dto.setDataSource("val-dataSource");
        dto.setDataSourceUrl("val-dataSourceUrl");
        dto.setIngestionBatchId("val-ingestionBatchId");
        dto.setValidated(true);
        dto.setActive(true);
        dto.setDataQualityScore(99);
        dto.setDataQualityNotes("val-dataQualityNotes");
        assertEquals("val-id", dto.getId());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-countryName", dto.getCountryName());
        assertEquals("val-isoCodeAlpha3", dto.getIsoCodeAlpha3());
        assertEquals(99, dto.getIsoNumericCode());
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-subRegion", dto.getSubRegion());
        assertEquals("val-continent", dto.getContinent());
        assertEquals("val-capitalCity", dto.getCapitalCity());
        assertEquals(BigDecimal.ONE, dto.getLatitude());
        assertEquals(BigDecimal.ONE, dto.getLongitude());
        assertEquals(BigDecimal.ONE, dto.getTotalAreaSqKm());
        assertEquals(BigDecimal.ONE, dto.getLandAreaSqKm());
        assertEquals(BigDecimal.ONE, dto.getWaterAreaSqKm());
        assertEquals(BigDecimal.ONE, dto.getPopulation());
        assertEquals(BigDecimal.ONE, dto.getPopulationDensity());
        assertEquals(99, dto.getPopulationGrowthRate());
        assertEquals(BigDecimal.ONE, dto.getUrbanPopulationPercent());
        assertEquals(BigDecimal.ONE, dto.getRuralPopulationPercent());
        assertEquals("val-currencyCode", dto.getCurrencyCode());
        assertEquals("val-currencyName", dto.getCurrencyName());
        assertEquals("val-currencySymbol", dto.getCurrencySymbol());
        assertEquals("val-callingCode", dto.getCallingCode());
        assertEquals("val-internetTld", dto.getInternetTld());
        assertEquals("val-officialLanguage", dto.getOfficialLanguage());
        assertEquals("val-timeZone", dto.getTimeZone());
        assertEquals(BigDecimal.ONE, dto.getGdpUsd());
        assertEquals(BigDecimal.ONE, dto.getGdpPerCapitaUsd());
        assertEquals(BigDecimal.ONE, dto.getGdpGrowthRate());
        assertEquals(BigDecimal.ONE, dto.getInflationRate());
        assertEquals(BigDecimal.ONE, dto.getUnemploymentRate());
        assertEquals(BigDecimal.ONE, dto.getInterestRate());
        assertEquals(BigDecimal.ONE, dto.getGovernmentDebtPercent());
        assertEquals(BigDecimal.ONE, dto.getForeignExchangeReserves());
        assertEquals("val-economicStatus", dto.getEconomicStatus());
        assertEquals("val-incomeLevel", dto.getIncomeLevel());
        assertEquals("val-developmentStatus", dto.getDevelopmentStatus());
        assertEquals(99, dto.getEaseOfDoingBusinessRank());
        assertEquals(99, dto.getCorruptionPerceptionIndex());
        assertEquals(99, dto.getHumanDevelopmentIndex());
        assertEquals("val-politicalSystem", dto.getPoliticalSystem());
        assertEquals("val-legalSystem", dto.getLegalSystem());
        assertEquals("val-governmentType", dto.getGovernmentType());
        assertEquals(LocalDate.of(2025,6,1), dto.getIndependenceDate());
        assertEquals("val-euMember", dto.getEuMember());
        assertEquals("val-unMember", dto.getUnMember());
        assertEquals("val-natoMember", dto.getNatoMember());
        assertEquals("val-wtoMember", dto.getWtoMember());
        assertEquals(BigDecimal.ONE, dto.getCorporateTaxRate());
        assertEquals(BigDecimal.ONE, dto.getVatRate());
        assertEquals(BigDecimal.ONE, dto.getIncomeTaxRate());
        assertEquals("val-freeTradeZones", dto.getFreeTradeZones());
        assertEquals(BigDecimal.ONE, dto.getExportsUsd());
        assertEquals(BigDecimal.ONE, dto.getImportsUsd());
        assertEquals(BigDecimal.ONE, dto.getTradeBalanceUsd());
        assertEquals("val-majorTradingPartners", dto.getMajorTradingPartners());
        assertEquals("val-dataSource", dto.getDataSource());
        assertEquals("val-dataSourceUrl", dto.getDataSourceUrl());
        assertEquals("val-ingestionBatchId", dto.getIngestionBatchId());
        assertTrue(dto.getValidated());
        assertTrue(dto.getActive());
        assertEquals(99, dto.getDataQualityScore());
        assertEquals("val-dataQualityNotes", dto.getDataQualityNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        CountryDataDto dto1 = CountryDataDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .isoCodeAlpha3("test-isoCodeAlpha3")
            .isoNumericCode(42)
            .region("test-region")
            .subRegion("test-subRegion")
            .continent("test-continent")
            .capitalCity("test-capitalCity")
            .latitude(BigDecimal.TEN)
            .longitude(BigDecimal.TEN)
            .totalAreaSqKm(BigDecimal.TEN)
            .landAreaSqKm(BigDecimal.TEN)
            .waterAreaSqKm(BigDecimal.TEN)
            .population(BigDecimal.TEN)
            .populationDensity(BigDecimal.TEN)
            .populationGrowthRate(42)
            .urbanPopulationPercent(BigDecimal.TEN)
            .ruralPopulationPercent(BigDecimal.TEN)
            .currencyCode("test-currencyCode")
            .currencyName("test-currencyName")
            .currencySymbol("test-currencySymbol")
            .callingCode("test-callingCode")
            .internetTld("test-internetTld")
            .languages(null)
            .officialLanguage("test-officialLanguage")
            .borderCountries(null)
            .timeZone("test-timeZone")
            .timeZones(null)
            .gdpUsd(BigDecimal.TEN)
            .gdpPerCapitaUsd(BigDecimal.TEN)
            .gdpGrowthRate(BigDecimal.TEN)
            .inflationRate(BigDecimal.TEN)
            .unemploymentRate(BigDecimal.TEN)
            .interestRate(BigDecimal.TEN)
            .governmentDebtPercent(BigDecimal.TEN)
            .foreignExchangeReserves(BigDecimal.TEN)
            .economicStatus("test-economicStatus")
            .incomeLevel("test-incomeLevel")
            .developmentStatus("test-developmentStatus")
            .easeOfDoingBusinessRank(42)
            .corruptionPerceptionIndex(42)
            .humanDevelopmentIndex(42)
            .happinessIndex(null)
            .politicalSystem("test-politicalSystem")
            .legalSystem("test-legalSystem")
            .governmentType("test-governmentType")
            .independenceDate(LocalDate.of(2025,1,15))
            .euMember("test-euMember")
            .unMember("test-unMember")
            .natoMember("test-natoMember")
            .wtoMember("test-wtoMember")
            .tradeAgreements(null)
            .regionalOrganizations(null)
            .corporateTaxRate(BigDecimal.TEN)
            .vatRate(BigDecimal.TEN)
            .incomeTaxRate(BigDecimal.TEN)
            .importDutyRate(null)
            .freeTradeZones("test-freeTradeZones")
            .majorIndustries(null)
            .majorExports(null)
            .majorImports(null)
            .exportsUsd(BigDecimal.TEN)
            .importsUsd(BigDecimal.TEN)
            .tradeBalanceUsd(BigDecimal.TEN)
            .majorTradingPartners("test-majorTradingPartners")
            .dataSource("test-dataSource")
            .dataSourceUrl("test-dataSourceUrl")
            .dataLastUpdated(LocalDateTime.of(2025,1,15,10,0))
            .ingestionTimestamp(LocalDateTime.of(2025,1,15,10,0))
            .ingestionBatchId("test-ingestionBatchId")
            .validated(true)
            .active(true)
            .dataQualityScore(42)
            .dataQualityNotes("test-dataQualityNotes")
            .additionalProperties(Collections.emptyMap())
            .build();
        CountryDataDto dto2 = CountryDataDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .isoCodeAlpha3("test-isoCodeAlpha3")
            .isoNumericCode(42)
            .region("test-region")
            .subRegion("test-subRegion")
            .continent("test-continent")
            .capitalCity("test-capitalCity")
            .latitude(BigDecimal.TEN)
            .longitude(BigDecimal.TEN)
            .totalAreaSqKm(BigDecimal.TEN)
            .landAreaSqKm(BigDecimal.TEN)
            .waterAreaSqKm(BigDecimal.TEN)
            .population(BigDecimal.TEN)
            .populationDensity(BigDecimal.TEN)
            .populationGrowthRate(42)
            .urbanPopulationPercent(BigDecimal.TEN)
            .ruralPopulationPercent(BigDecimal.TEN)
            .currencyCode("test-currencyCode")
            .currencyName("test-currencyName")
            .currencySymbol("test-currencySymbol")
            .callingCode("test-callingCode")
            .internetTld("test-internetTld")
            .languages(null)
            .officialLanguage("test-officialLanguage")
            .borderCountries(null)
            .timeZone("test-timeZone")
            .timeZones(null)
            .gdpUsd(BigDecimal.TEN)
            .gdpPerCapitaUsd(BigDecimal.TEN)
            .gdpGrowthRate(BigDecimal.TEN)
            .inflationRate(BigDecimal.TEN)
            .unemploymentRate(BigDecimal.TEN)
            .interestRate(BigDecimal.TEN)
            .governmentDebtPercent(BigDecimal.TEN)
            .foreignExchangeReserves(BigDecimal.TEN)
            .economicStatus("test-economicStatus")
            .incomeLevel("test-incomeLevel")
            .developmentStatus("test-developmentStatus")
            .easeOfDoingBusinessRank(42)
            .corruptionPerceptionIndex(42)
            .humanDevelopmentIndex(42)
            .happinessIndex(null)
            .politicalSystem("test-politicalSystem")
            .legalSystem("test-legalSystem")
            .governmentType("test-governmentType")
            .independenceDate(LocalDate.of(2025,1,15))
            .euMember("test-euMember")
            .unMember("test-unMember")
            .natoMember("test-natoMember")
            .wtoMember("test-wtoMember")
            .tradeAgreements(null)
            .regionalOrganizations(null)
            .corporateTaxRate(BigDecimal.TEN)
            .vatRate(BigDecimal.TEN)
            .incomeTaxRate(BigDecimal.TEN)
            .importDutyRate(null)
            .freeTradeZones("test-freeTradeZones")
            .majorIndustries(null)
            .majorExports(null)
            .majorImports(null)
            .exportsUsd(BigDecimal.TEN)
            .importsUsd(BigDecimal.TEN)
            .tradeBalanceUsd(BigDecimal.TEN)
            .majorTradingPartners("test-majorTradingPartners")
            .dataSource("test-dataSource")
            .dataSourceUrl("test-dataSourceUrl")
            .dataLastUpdated(LocalDateTime.of(2025,1,15,10,0))
            .ingestionTimestamp(LocalDateTime.of(2025,1,15,10,0))
            .ingestionBatchId("test-ingestionBatchId")
            .validated(true)
            .active(true)
            .dataQualityScore(42)
            .dataQualityNotes("test-dataQualityNotes")
            .additionalProperties(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountryDataDto dto = CountryDataDto.builder()
                        .id("test-id")
            .countryCode("test-countryCode")
            .countryName("test-countryName")
            .isoCodeAlpha3("test-isoCodeAlpha3")
            .isoNumericCode(42)
            .region("test-region")
            .subRegion("test-subRegion")
            .continent("test-continent")
            .capitalCity("test-capitalCity")
            .latitude(BigDecimal.TEN)
            .longitude(BigDecimal.TEN)
            .totalAreaSqKm(BigDecimal.TEN)
            .landAreaSqKm(BigDecimal.TEN)
            .waterAreaSqKm(BigDecimal.TEN)
            .population(BigDecimal.TEN)
            .populationDensity(BigDecimal.TEN)
            .populationGrowthRate(42)
            .urbanPopulationPercent(BigDecimal.TEN)
            .ruralPopulationPercent(BigDecimal.TEN)
            .currencyCode("test-currencyCode")
            .currencyName("test-currencyName")
            .currencySymbol("test-currencySymbol")
            .callingCode("test-callingCode")
            .internetTld("test-internetTld")
            .languages(null)
            .officialLanguage("test-officialLanguage")
            .borderCountries(null)
            .timeZone("test-timeZone")
            .timeZones(null)
            .gdpUsd(BigDecimal.TEN)
            .gdpPerCapitaUsd(BigDecimal.TEN)
            .gdpGrowthRate(BigDecimal.TEN)
            .inflationRate(BigDecimal.TEN)
            .unemploymentRate(BigDecimal.TEN)
            .interestRate(BigDecimal.TEN)
            .governmentDebtPercent(BigDecimal.TEN)
            .foreignExchangeReserves(BigDecimal.TEN)
            .economicStatus("test-economicStatus")
            .incomeLevel("test-incomeLevel")
            .developmentStatus("test-developmentStatus")
            .easeOfDoingBusinessRank(42)
            .corruptionPerceptionIndex(42)
            .humanDevelopmentIndex(42)
            .happinessIndex(null)
            .politicalSystem("test-politicalSystem")
            .legalSystem("test-legalSystem")
            .governmentType("test-governmentType")
            .independenceDate(LocalDate.of(2025,1,15))
            .euMember("test-euMember")
            .unMember("test-unMember")
            .natoMember("test-natoMember")
            .wtoMember("test-wtoMember")
            .tradeAgreements(null)
            .regionalOrganizations(null)
            .corporateTaxRate(BigDecimal.TEN)
            .vatRate(BigDecimal.TEN)
            .incomeTaxRate(BigDecimal.TEN)
            .importDutyRate(null)
            .freeTradeZones("test-freeTradeZones")
            .majorIndustries(null)
            .majorExports(null)
            .majorImports(null)
            .exportsUsd(BigDecimal.TEN)
            .importsUsd(BigDecimal.TEN)
            .tradeBalanceUsd(BigDecimal.TEN)
            .majorTradingPartners("test-majorTradingPartners")
            .dataSource("test-dataSource")
            .dataSourceUrl("test-dataSourceUrl")
            .dataLastUpdated(LocalDateTime.of(2025,1,15,10,0))
            .ingestionTimestamp(LocalDateTime.of(2025,1,15,10,0))
            .ingestionBatchId("test-ingestionBatchId")
            .validated(true)
            .active(true)
            .dataQualityScore(42)
            .dataQualityNotes("test-dataQualityNotes")
            .additionalProperties(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
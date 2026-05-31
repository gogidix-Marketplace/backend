package com.gogidix.globalbusinessmanagement.countryingestion.domain.repository;

import com.gogidix.globalbusinessmanagement.countryingestion.domain.model.CountryData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CountryData entity.
 * Provides data access operations for country data management.
 */
@Repository
public interface CountryDataRepository extends MongoRepository<CountryData, String> {

    /**
     * Find country data by country code.
     */
    Optional<CountryData> findByCountryCode(String countryCode);

    /**
     * Find country data by country name (case-insensitive).
     */
    Optional<CountryData> findByCountryNameIgnoreCase(String countryName);

    /**
     * Check if country data exists by country code.
     */
    boolean existsByCountryCode(String countryCode);

    /**
     * Find all countries by region.
     */
    List<CountryData> findByRegion(String region);

    /**
     * Find all countries by region with pagination.
     */
    Page<CountryData> findByRegion(String region, Pageable pageable);

    /**
     * Find all countries by sub-region.
     */
    List<CountryData> findBySubRegion(String subRegion);

    /**
     * Find countries by continent.
     */
    List<CountryData> findByContinent(String continent);

    /**
     * Find countries by income level.
     */
    List<CountryData> findByIncomeLevel(String incomeLevel);

    /**
     * Find countries by currency code.
     */
    List<CountryData> findByCurrencyCode(String currencyCode);

    /**
     * Find countries by language.
     */
    @Query("{'languages': {$in: [?0]}}")
    List<CountryData> findByLanguage(String language);

    /**
     * Find countries with population between min and max.
     */
    @Query("{'population': {$gte: ?0, $lte: ?1}}")
    List<CountryData> findByPopulationBetween(Long minPopulation, Long maxPopulation);

    /**
     * Find countries by GDP range.
     */
    @Query("{'gdpUsd': {$gte: ?0, $lte: ?1}}")
    List<CountryData> findByGdpBetween(Long minGdp, Long maxGdp);

    /**
     * Find countries by data source.
     */
    List<CountryData> findByDataSource(String dataSource);

    /**
     * Find countries by ingestion batch ID.
     */
    List<CountryData> findByIngestionBatchId(String batchId);

    /**
     * Find active countries.
     */
    List<CountryData> findByActiveTrue();

    /**
     * Find validated countries.
     */
    List<CountryData> findByValidatedTrue();

    /**
     * Find countries by data quality score threshold.
     */
    @Query("{'dataQualityScore': {$gte: ?0}}")
    List<CountryData> findByDataQualityScoreGreaterThanEqual(Integer minScore);

    /**
     * Find countries updated after a specific date.
     */
    List<CountryData> findByDataLastUpdatedAfter(LocalDateTime date);

    /**
     * Find countries ingested between dates.
     */
    @Query("{'ingestionTimestamp': {$gte: ?0, $lte: ?1}}")
    List<CountryData> findByIngestionTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Search countries by name or code containing the search term.
     */
    @Query("{$or: [" +
            "{'countryName': {$regex: ?0, $options: 'i'}}, " +
            "{'countryCode': {$regex: ?0, $options: 'i'}}" +
            "]}")
    Page<CountryData> searchByCountryNameOrCode(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find all countries sorted by population descending.
     */
    List<CountryData> findByActiveTrueOrderByPopulationDesc();

    /**
     * Find all countries sorted by GDP descending.
     */
    List<CountryData> findByActiveTrueOrderByGdpUsdDesc();

    /**
     * Find countries by political system.
     */
    List<CountryData> findByPoliticalSystem(String politicalSystem);

    /**
     * Find EU member countries.
     */
    @Query("{'euMember': ?0}")
    List<CountryData> findByEuMember(String euMember);

    /**
     * Find UN member countries.
     */
    List<CountryData> findByUnMember(String unMember);

    /**
     * Find countries by multiple criteria.
     */
    @Query("{$and: [" +
            "(?0 == null OR {'region': ?0}), " +
            "(?1 == null OR {'incomeLevel': ?1}), " +
            "(?2 == null OR {'active': ?2})" +
            "]}")
    List<CountryData> findByMultipleCriteria(String region, String incomeLevel, Boolean active);

    /**
     * Count countries by region.
     */
    @Query("{'region': ?0}")
    Long countByRegion(String region);

    /**
     * Count active countries.
     */
    Long countByActiveTrue();

    /**
     * Count validated countries.
     */
    Long countByValidatedTrue();

    /**
     * Find countries with border countries containing a specific country.
     */
    @Query("{'borderCountries': {$in: [?0]}}")
    List<CountryData> findByBorderingCountry(String countryCode);

    /**
     * Find countries with specific trade agreements.
     */
    @Query("{'tradeAgreements': {$in: [?0]}}")
    List<CountryData> findByTradeAgreement(String tradeAgreement);

    /**
     * Find countries with specific industries.
     */
    @Query("{'majorIndustries': {$in: [?0]}}")
    List<CountryData> findByIndustry(String industry);

    /**
     * Get distinct regions.
     */
    @Query(value = "{}", fields = "region")
    List<String> findDistinctRegions();

    /**
     * Get distinct income levels.
     */
    @Query(value = "{}", fields = "incomeLevel")
    List<String> findDistinctIncomeLevels();

    /**
     * Get distinct currencies.
     */
    @Query(value = "{}", fields = "currencyCode")
    List<String> findDistinctCurrencyCodes();

    /**
     * Aggregate countries by region with count.
     */
    @Aggregation(pipeline = {
            "{$group: {_id: '$region', count: {$sum: 1}, " +
                    "totalPopulation: {$sum: '$population'}, " +
                    "totalGdp: {$sum: '$gdpUsd'}}}"
    })
    List<RegionStats> aggregateByRegion();

    /**
     * Delete countries by ingestion batch ID.
     */
    void deleteByIngestionBatchId(String batchId);

    /**
     * Update country data last updated timestamp.
     */
    @Query("{'countryCode': ?0}, {$set: {'dataLastUpdated': ?1}}")
    void updateDataLastUpdatedByCountryCode(String countryCode, LocalDateTime timestamp);

    /**
     * Interface for region statistics aggregation result.
     */
    interface RegionStats {
        String getId();
        Integer getCount();
        Long getTotalPopulation();
        Long getTotalGdp();
    }
}

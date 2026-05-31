package com.gogidix.countryadmin.domain.repository;

import com.gogidix.countryadmin.domain.model.AggregatedCountryReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for AggregatedCountryReport entity.
 */
@Repository
public interface AggregatedCountryReportRepository extends MongoRepository<AggregatedCountryReport, String> {

    /**
     * Find reports by country ID.
     */
    List<AggregatedCountryReport> findByCountryId(String countryId);

    /**
     * Find reports by country ID with pagination.
     */
    Page<AggregatedCountryReport> findByCountryIdOrderByReportPeriodDesc(
            String countryId, Pageable pageable);

    /**
     * Find latest report by country.
     */
    Optional<AggregatedCountryReport> findFirstByCountryIdOrderByReportPeriodDesc(
            String countryId);

    /**
     * Find report by report ID.
     */
    Optional<AggregatedCountryReport> findByReportId(String reportId);

    /**
     * Find reports by country and date range.
     */
    List<AggregatedCountryReport> findByCountryIdAndReportPeriodBetween(
            String countryId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find reports by submission status.
     */
    List<AggregatedCountryReport> findByCountryIdAndSubmittedToHQ(
            String countryId, Boolean submitted);

    /**
     * Find reports that need to be submitted to HQ.
     */
    List<AggregatedCountryReport> findByCountryIdAndSubmittedToHQFalse(
            String countryId);

    /**
     * Find report by HQ reference number.
     */
    Optional<AggregatedCountryReport> findByHqReferenceNumber(String hqReferenceNumber);
}

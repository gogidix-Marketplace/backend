package com.gogidix.customersupport.feedback.domain.repository;

import com.gogidix.customersupport.feedback.domain.model.CSATSurvey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository for CSAT Survey aggregate
 */
@Repository
public interface CSATSurveyRepository extends MongoRepository<CSATSurvey, String> {

    Optional<CSATSurvey> findBySurveyId(String surveyId);

    List<CSATSurvey> findByTenantId(String tenantId);

    Page<CSATSurvey> findByTenantId(String tenantId, Pageable pageable);

    List<CSATSurvey> findByTenantIdAndStatus(String tenantId, CSATSurvey.SurveyStatus status);

    List<CSATSurvey> findByTenantIdAndLocale(String tenantId, String locale);

    @Query("{'tenantId': ?0, 'status': 'ACTIVE', 'activeFrom': {$lte: ?1}, $or: [{'activeUntil': null}, {'activeUntil': {$gte: ?1}}]}")
    List<CSATSurvey> findActiveSurveysForEvent(String tenantId, Instant currentDate);

    List<CSATSurvey> findByTenantIdAndCreatedBy(String tenantId, String createdBy);

    long countByTenantIdAndStatus(String tenantId, CSATSurvey.SurveyStatus status);

    void deleteByTenantIdAndSurveyId(String tenantId, String surveyId);
}

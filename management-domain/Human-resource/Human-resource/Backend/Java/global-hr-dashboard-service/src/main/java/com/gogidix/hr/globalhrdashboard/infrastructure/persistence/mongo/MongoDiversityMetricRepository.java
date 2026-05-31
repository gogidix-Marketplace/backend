package com.gogidix.hr.globalhrdashboard.infrastructure.persistence.mongo;

import com.gogidix.hr.globalhrdashboard.domain.model.DiversityMetric;
import com.gogidix.hr.globalhrdashboard.domain.repository.DiversityMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * MongoDB Repository implementation for DiversityMetric
 * Following Hexagonal Architecture - this is the ADAPTER that implements the PORT
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoDiversityMetricRepository implements DiversityMetricRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public DiversityMetric save(DiversityMetric metric) {
        return mongoTemplate.save(metric);
    }

    @Override
    public Optional<DiversityMetric> findByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, DiversityMetric.class));
    }

    @Override
    public List<DiversityMetric> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, DiversityMetric.class);
    }

    @Override
    public List<DiversityMetric> findByTenantIdAndCountryCode(String tenantId, String countryCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").is(countryCode.toUpperCase())
        );
        return mongoTemplate.find(query, DiversityMetric.class);
    }

    @Override
    public Optional<DiversityMetric> findByTenantIdAndCountryCodeAndPeriod(String tenantId, String countryCode, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").is(countryCode.toUpperCase())
                        .and("period").is(period)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, DiversityMetric.class));
    }

    @Override
    public List<DiversityMetric> findByTenantIdAndPeriod(String tenantId, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        return mongoTemplate.find(query, DiversityMetric.class);
    }

    @Override
    public List<DiversityMetric> findByTenantIdAndRegionCode(String tenantId, String regionCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("regionCode").is(regionCode)
        );
        return mongoTemplate.find(query, DiversityMetric.class);
    }

    @Override
    public List<DiversityMetric> findActiveByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isActive").is(true)
        );
        return mongoTemplate.find(query, DiversityMetric.class);
    }

    @Override
    public List<DiversityMetric> findByTenantIdAndLastUpdatedAfter(String tenantId, Instant lastUpdated) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("lastUpdated").gt(lastUpdated)
        );
        return mongoTemplate.find(query, DiversityMetric.class);
    }

    @Override
    public List<DiversityMetric> findWithHighGenderDiversity(String tenantId, Double minScore) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("genderDiversityScore").gte(minScore)
        );
        return mongoTemplate.find(query, DiversityMetric.class);
    }

    @Override
    public List<DiversityMetric> findWithHighNationalDiversity(String tenantId, Double minScore) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("nationalDiversityScore").gte(minScore)
        );
        return mongoTemplate.find(query, DiversityMetric.class);
    }

    @Override
    public List<DiversityMetric> findWithHighOverallDiversity(String tenantId, Double minScore) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("overallDiversityScore").gte(minScore)
        );
        return mongoTemplate.find(query, DiversityMetric.class);
    }

    @Override
    public Double getAverageGenderDiversityScore(String tenantId, String period) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("tenantId").is(tenantId).and("period").is(period)),
                group().avg("genderDiversityScore").as("averageScore")
        );

        AggregationResults<AverageResult> results = mongoTemplate.aggregate(
                aggregation, DiversityMetric.class, AverageResult.class
        );

        AverageResult result = results.getUniqueMappedResult();
        return result != null ? result.getAverageScore() : 0.0;
    }

    @Override
    public Double getAverageNationalDiversityScore(String tenantId, String period) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("tenantId").is(tenantId).and("period").is(period)),
                group().avg("nationalDiversityScore").as("averageScore")
        );

        AggregationResults<AverageResult> results = mongoTemplate.aggregate(
                aggregation, DiversityMetric.class, AverageResult.class
        );

        AverageResult result = results.getUniqueMappedResult();
        return result != null ? result.getAverageScore() : 0.0;
    }

    @Override
    public Double getAverageOverallDiversityScore(String tenantId, String period) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("tenantId").is(tenantId).and("period").is(period)),
                group().avg("overallDiversityScore").as("averageScore")
        );

        AggregationResults<AverageResult> results = mongoTemplate.aggregate(
                aggregation, DiversityMetric.class, AverageResult.class
        );

        AverageResult result = results.getUniqueMappedResult();
        return result != null ? result.getAverageScore() : 0.0;
    }

    @Override
    public List<DiversityMetric> findMetricsNeedingUpdate(String tenantId, Instant before) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("lastUpdated").lt(before)
                        .orOperator(
                                Criteria.where("lastUpdated").exists(false)
                        )
        );
        return mongoTemplate.find(query, DiversityMetric.class);
    }

    @Override
    public void deleteByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, DiversityMetric.class);
    }

    @Override
    public void deleteByTenantIdAndPeriod(String tenantId, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        mongoTemplate.remove(query, DiversityMetric.class);
    }

    @Override
    public boolean existsByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, DiversityMetric.class);
    }

    @Override
    public List<DiversityMetric> saveAll(List<DiversityMetric> metrics) {
        return metrics.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public long countByTenantIdAndCountryCode(String tenantId, String countryCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").is(countryCode.toUpperCase())
        );
        return mongoTemplate.count(query, DiversityMetric.class);
    }

    @Override
    public List<DiversityMetric> findTrendDataByCountry(String tenantId, String countryCode,
                                                         String startPeriod, String endPeriod) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").is(countryCode.toUpperCase())
                        .and("period").gte(startPeriod).lte(endPeriod)
        ).with(Sort.by(Sort.Direction.ASC, "period"));
        return mongoTemplate.find(query, DiversityMetric.class);
    }

    @Override
    public List<DiversityMetric> findTrendDataByRegion(String tenantId, String regionCode,
                                                        String startPeriod, String endPeriod) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("regionCode").is(regionCode)
                        .and("period").gte(startPeriod).lte(endPeriod)
        ).with(Sort.by(Sort.Direction.ASC, "period"));
        return mongoTemplate.find(query, DiversityMetric.class);
    }

    @Override
    public List<DiversityMetric> findLatestByPeriod(String tenantId, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        return mongoTemplate.find(query, DiversityMetric.class);
    }

    @Override
    public Optional<DiversityMetric> findGlobalAggregateByTenantIdAndPeriod(String tenantId, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
                        .and("countryCode").is("GLOBAL")
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, DiversityMetric.class));
    }

    @Override
    public List<DiversityMetric> findTopCountriesByDiversityScore(String tenantId, String period, int limit) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
                        .and("countryCode").ne("GLOBAL")
        ).with(Sort.by(Sort.Direction.DESC, "overallDiversityScore")).limit(limit);
        return mongoTemplate.find(query, DiversityMetric.class);
    }

    private static class AverageResult {
        private double averageScore;

        public double getAverageScore() {
            return averageScore;
        }

        public void setAverageScore(double averageScore) {
            this.averageScore = averageScore;
        }
    }
}

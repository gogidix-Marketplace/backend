package com.gogidix.hr.globalhrdashboard.infrastructure.persistence.mongo;

import com.gogidix.hr.globalhrdashboard.domain.model.RetentionMetric;
import com.gogidix.hr.globalhrdashboard.domain.repository.RetentionMetricRepository;
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
 * MongoDB Repository implementation for RetentionMetric
 * Following Hexagonal Architecture - this is the ADAPTER that implements the PORT
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoRetentionMetricRepository implements RetentionMetricRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public RetentionMetric save(RetentionMetric metric) {
        return mongoTemplate.save(metric);
    }

    @Override
    public Optional<RetentionMetric> findByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, RetentionMetric.class));
    }

    @Override
    public List<RetentionMetric> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, RetentionMetric.class);
    }

    @Override
    public List<RetentionMetric> findByTenantIdAndCountryCode(String tenantId, String countryCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").is(countryCode.toUpperCase())
        );
        return mongoTemplate.find(query, RetentionMetric.class);
    }

    @Override
    public Optional<RetentionMetric> findByTenantIdAndCountryCodeAndPeriod(String tenantId, String countryCode, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").is(countryCode.toUpperCase())
                        .and("period").is(period)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, RetentionMetric.class));
    }

    @Override
    public List<RetentionMetric> findByTenantIdAndPeriod(String tenantId, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        return mongoTemplate.find(query, RetentionMetric.class);
    }

    @Override
    public List<RetentionMetric> findByTenantIdAndRegionCode(String tenantId, String regionCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("regionCode").is(regionCode)
        );
        return mongoTemplate.find(query, RetentionMetric.class);
    }

    @Override
    public List<RetentionMetric> findActiveByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isActive").is(true)
        );
        return mongoTemplate.find(query, RetentionMetric.class);
    }

    @Override
    public List<RetentionMetric> findByTenantIdAndLastUpdatedAfter(String tenantId, Instant lastUpdated) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("lastUpdated").gt(lastUpdated)
        );
        return mongoTemplate.find(query, RetentionMetric.class);
    }

    @Override
    public List<RetentionMetric> findWithHighRetentionRate(String tenantId, Double minRate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("retentionRate").gte(minRate)
        );
        return mongoTemplate.find(query, RetentionMetric.class);
    }

    @Override
    public List<RetentionMetric> findWithHighTurnoverRate(String tenantId, Double maxRate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("turnoverRate").gt(maxRate)
        );
        return mongoTemplate.find(query, RetentionMetric.class);
    }

    @Override
    public List<RetentionMetric> findWithLowRetentionRate(String tenantId, Double maxRate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("retentionRate").lt(maxRate)
        );
        return mongoTemplate.find(query, RetentionMetric.class);
    }

    @Override
    public Double getAverageRetentionRate(String tenantId, String period) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("tenantId").is(tenantId).and("period").is(period)),
                group().avg("retentionRate").as("averageRate")
        );

        AggregationResults<AverageResult> results = mongoTemplate.aggregate(
                aggregation, RetentionMetric.class, AverageResult.class
        );

        AverageResult result = results.getUniqueMappedResult();
        return result != null ? result.getAverageRate() : 0.0;
    }

    @Override
    public Double getAverageTurnoverRate(String tenantId, String period) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("tenantId").is(tenantId).and("period").is(period)),
                group().avg("turnoverRate").as("averageRate")
        );

        AggregationResults<AverageResult> results = mongoTemplate.aggregate(
                aggregation, RetentionMetric.class, AverageResult.class
        );

        AverageResult result = results.getUniqueMappedResult();
        return result != null ? result.getAverageRate() : 0.0;
    }

    @Override
    public List<RetentionMetric> findMetricsNeedingUpdate(String tenantId, Instant before) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("lastUpdated").lt(before)
                        .orOperator(
                                Criteria.where("lastUpdated").exists(false)
                        )
        );
        return mongoTemplate.find(query, RetentionMetric.class);
    }

    @Override
    public List<RetentionMetric> findTrendDataByCountry(String tenantId, String countryCode,
                                                         String startPeriod, String endPeriod) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").is(countryCode.toUpperCase())
                        .and("period").gte(startPeriod).lte(endPeriod)
        ).with(Sort.by(Sort.Direction.ASC, "period"));
        return mongoTemplate.find(query, RetentionMetric.class);
    }

    @Override
    public List<RetentionMetric> findTrendDataByRegion(String tenantId, String regionCode,
                                                        String startPeriod, String endPeriod) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("regionCode").is(regionCode)
                        .and("period").gte(startPeriod).lte(endPeriod)
        ).with(Sort.by(Sort.Direction.ASC, "period"));
        return mongoTemplate.find(query, RetentionMetric.class);
    }

    @Override
    public void deleteByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, RetentionMetric.class);
    }

    @Override
    public void deleteByTenantIdAndPeriod(String tenantId, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        mongoTemplate.remove(query, RetentionMetric.class);
    }

    @Override
    public boolean existsByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, RetentionMetric.class);
    }

    @Override
    public List<RetentionMetric> saveAll(List<RetentionMetric> metrics) {
        return metrics.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public long countByTenantIdAndCountryCode(String tenantId, String countryCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").is(countryCode.toUpperCase())
        );
        return mongoTemplate.count(query, RetentionMetric.class);
    }

    @Override
    public List<RetentionMetric> findLatestByPeriod(String tenantId, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        return mongoTemplate.find(query, RetentionMetric.class);
    }

    @Override
    public Optional<RetentionMetric> findGlobalAggregateByTenantIdAndPeriod(String tenantId, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
                        .and("countryCode").is("GLOBAL")
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, RetentionMetric.class));
    }

    @Override
    public List<RetentionMetric> findTopCountriesByRetentionRate(String tenantId, String period, int limit) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
                        .and("countryCode").ne("GLOBAL")
        ).with(Sort.by(Sort.Direction.DESC, "retentionRate")).limit(limit);
        return mongoTemplate.find(query, RetentionMetric.class);
    }

    @Override
    public List<RetentionMetric> findBottomCountriesByRetentionRate(String tenantId, String period, int limit) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
                        .and("countryCode").ne("GLOBAL")
        ).with(Sort.by(Sort.Direction.ASC, "retentionRate")).limit(limit);
        return mongoTemplate.find(query, RetentionMetric.class);
    }

    @Override
    public List<RetentionMetric> findByTenantIdAndRegionCodeAndPeriod(String tenantId, String regionCode, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("regionCode").is(regionCode)
                        .and("period").is(period)
        );
        return mongoTemplate.find(query, RetentionMetric.class);
    }

    @Override
    public Double calculateGlobalRetentionRate(String tenantId, String period) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("tenantId").is(tenantId).and("period").is(period)),
                group().sum("totalEmployees").as("totalEmployees")
                        .sum("retentionRate").as("weightedSum")
        );

        AggregationResults<CalculationResult> results = mongoTemplate.aggregate(
                aggregation, RetentionMetric.class, CalculationResult.class
        );

        CalculationResult result = results.getUniqueMappedResult();
        if (result != null && result.getTotalEmployees() > 0) {
            return result.getWeightedSum() / result.getTotalEmployees();
        }
        return 0.0;
    }

    @Override
    public Double calculateGlobalTurnoverRate(String tenantId, String period) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("tenantId").is(tenantId).and("period").is(period)),
                group().avg("turnoverRate").as("averageTurnover")
        );

        AggregationResults<AverageResult> results = mongoTemplate.aggregate(
                aggregation, RetentionMetric.class, AverageResult.class
        );

        AverageResult result = results.getUniqueMappedResult();
        return result != null ? result.getAverageTurnover() : 0.0;
    }

    @Override
    public Double calculateGlobalAverageTenure(String tenantId, String period) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("tenantId").is(tenantId).and("period").is(period)),
                group().avg("avgTenure").as("averageTenure")
        );

        AggregationResults<TenureResult> results = mongoTemplate.aggregate(
                aggregation, RetentionMetric.class, TenureResult.class
        );

        TenureResult result = results.getUniqueMappedResult();
        return result != null ? result.getAverageTenure() : 0.0;
    }

    private static class AverageResult {
        private double averageRate;
        private double averageTurnover;

        public double getAverageRate() {
            return averageRate;
        }

        public void setAverageRate(double averageRate) {
            this.averageRate = averageRate;
        }

        public double getAverageTurnover() {
            return averageTurnover;
        }

        public void setAverageTurnover(double averageTurnover) {
            this.averageTurnover = averageTurnover;
        }
    }

    private static class CalculationResult {
        private int totalEmployees;
        private double weightedSum;

        public int getTotalEmployees() {
            return totalEmployees;
        }

        public void setTotalEmployees(int totalEmployees) {
            this.totalEmployees = totalEmployees;
        }

        public double getWeightedSum() {
            return weightedSum;
        }

        public void setWeightedSum(double weightedSum) {
            this.weightedSum = weightedSum;
        }
    }

    private static class TenureResult {
        private double averageTenure;

        public double getAverageTenure() {
            return averageTenure;
        }

        public void setAverageTenure(double averageTenure) {
            this.averageTenure = averageTenure;
        }
    }
}

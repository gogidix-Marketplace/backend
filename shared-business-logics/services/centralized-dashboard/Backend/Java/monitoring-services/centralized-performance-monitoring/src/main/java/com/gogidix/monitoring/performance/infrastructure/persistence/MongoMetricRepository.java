package com.gogidix.monitoring.performance.infrastructure.persistence;

import com.gogidix.monitoring.performance.domain.model.MetricData;
import com.gogidix.monitoring.performance.domain.port.out.MetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * MongoDB implementation of MetricRepository.
 */
@Repository
@RequiredArgsConstructor
public class MongoMetricRepository implements MetricRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public MetricData save(MetricData metric) {
        return mongoTemplate.save(metric);
    }

    @Override
    public List<MetricData> saveAll(List<MetricData> metrics) {
        return List.copyOf(mongoTemplate.insertAll(metrics));
    }

    @Override
    public List<MetricData> findByTenantIdAndServiceIdAndMetricNameAndTimestampBetween(
            String tenantId, String serviceId, String metricName, Instant from, Instant to) {

        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        if (serviceId != null) {
            query.addCriteria(Criteria.where("serviceId").is(serviceId));
        }
        if (metricName != null) {
            query.addCriteria(Criteria.where("metricName").is(metricName));
        }
        query.addCriteria(Criteria.where("timestamp").gte(from).lte(to));
        query.with(Sort.by(Sort.Direction.DESC, "timestamp"));
        query.limit(10000);

        return mongoTemplate.find(query, MetricData.class);
    }

    @Override
    public List<MetricData> findAggregatedMetrics(String tenantId, String serviceId, String metricName,
                                                  Instant from, Instant to, String aggregation) {

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("tenantId").is(tenantId)
                        .and("timestamp").gte(from).lte(to)),
                Aggregation.group("metricName")
                        .first("metricName").as("metricName")
                        .avg("value").as("value")
        );

        return mongoTemplate.aggregate(agg, "metric_data", MetricData.class)
                .getMappedResults();
    }

    @Override
    public void deleteOlderThan(Instant cutoff) {
        Query query = new Query(Criteria.where("timestamp").lt(cutoff));
        mongoTemplate.remove(query, MetricData.class);
    }
}

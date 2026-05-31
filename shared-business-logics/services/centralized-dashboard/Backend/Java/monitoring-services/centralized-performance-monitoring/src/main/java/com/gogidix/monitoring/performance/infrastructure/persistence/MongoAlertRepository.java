package com.gogidix.monitoring.performance.infrastructure.persistence;

import com.gogidix.monitoring.performance.domain.model.AlertStatus;
import com.gogidix.monitoring.performance.domain.model.PerformanceAlert;
import com.gogidix.monitoring.performance.domain.port.out.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB implementation of AlertRepository.
 */
@Repository
@RequiredArgsConstructor
public class MongoAlertRepository implements AlertRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public PerformanceAlert save(PerformanceAlert alert) {
        return mongoTemplate.save(alert);
    }

    @Override
    public Optional<PerformanceAlert> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, PerformanceAlert.class));
    }

    @Override
    public List<PerformanceAlert> findActiveAlerts(String tenantId, String serviceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        if (serviceId != null) {
            query.addCriteria(Criteria.where("serviceId").is(serviceId));
        }
        query.addCriteria(Criteria.where("status").in(
                AlertStatus.OPEN.getCode(), AlertStatus.ACKNOWLEDGED.getCode()));
        query.with(Sort.by(Sort.Direction.DESC, "triggeredAt"));
        query.limit(1000);

        return mongoTemplate.find(query, PerformanceAlert.class);
    }

    @Override
    public PerformanceAlert updateStatus(String id, String status) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = Update.update("status", status);
        mongoTemplate.updateFirst(query, update, PerformanceAlert.class);
        return findById(id).orElse(null);
    }
}

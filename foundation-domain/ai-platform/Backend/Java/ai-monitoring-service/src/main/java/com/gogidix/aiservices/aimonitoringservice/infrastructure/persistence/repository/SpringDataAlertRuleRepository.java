package com.gogidix.aiservices.aimonitoringservice.infrastructure.persistence.repository;

import com.gogidix.aiservices.aimonitoringservice.domain.model.AlertRuleStatus;
import com.gogidix.aiservices.aimonitoringservice.infrastructure.persistence.document.AlertRuleDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataAlertRuleRepository extends MongoRepository<AlertRuleDocument, String> {

    List<AlertRuleDocument> findByTenantId(String tenantId);

    List<AlertRuleDocument> findByTenantIdAndStatus(String tenantId, AlertRuleStatus status);

    AlertRuleDocument findByAlertIdAndTenantId(String alertId, String tenantId);

    void deleteByAlertIdAndTenantId(String alertId, String tenantId);
}

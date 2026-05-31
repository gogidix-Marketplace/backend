package com.gogidix.customersupport.slamanagement.domain.repository;

import com.gogidix.customersupport.slamanagement.domain.model.SLABreach;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface SLABreachRepository extends MongoRepository<SLABreach, String> {

    List<SLABreach> findByTenantId(String tenantId);

    List<SLABreach> findByTenantIdOrderByBreachDateTimeDesc(String tenantId);

    List<SLABreach> findByTicketId(String ticketId);

    List<SLABreach> findByTenantIdAndSlaPolicyId(String tenantId, String slaPolicyId);

    List<SLABreach> findByTenantIdAndBreachType(String tenantId, SLABreach.BreachType breachType);

    List<SLABreach> findByTenantIdAndBreachDateTimeBetween(String tenantId, Instant startDate, Instant endDate);

    List<SLABreach> findByTenantIdAndResolvedAtIsNull(String tenantId);

    List<SLABreach> findByTenantIdAndAssignedAgentId(String tenantId, String agentId);

    List<SLABreach> findByTenantIdAndIsNotifiedFalse(String tenantId);

    void deleteByTicketId(String ticketId);

    long countByTenantIdAndBreachType(String tenantId, SLABreach.BreachType breachType);

    long countByTenantIdAndResolvedAtIsNull(String tenantId);
}

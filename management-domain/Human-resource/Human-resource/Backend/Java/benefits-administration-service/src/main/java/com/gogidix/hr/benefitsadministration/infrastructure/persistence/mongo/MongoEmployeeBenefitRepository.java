package com.gogidix.hr.benefitsadministration.infrastructure.persistence.mongo;

import com.gogidix.hr.benefitsadministration.domain.model.EmployeeBenefit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository implementation for EmployeeBenefit
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoEmployeeBenefitRepository {

    private final MongoTemplate mongoTemplate;

    public EmployeeBenefit save(EmployeeBenefit employeeBenefit) {
        return mongoTemplate.save(employeeBenefit);
    }

    public Optional<EmployeeBenefit> findById(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id).and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, EmployeeBenefit.class));
    }

    public List<EmployeeBenefit> findByEmployeeId(String employeeId, String tenantId) {
        Query query = Query.query(
                Criteria.where("employeeId").is(employeeId).and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, EmployeeBenefit.class);
    }

    public List<EmployeeBenefit> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, EmployeeBenefit.class);
    }

    public void deleteById(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id).and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, EmployeeBenefit.class);
    }
}

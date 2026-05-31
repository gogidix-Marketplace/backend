package com.gogidix.hr.globalhrdashboard.infrastructure.persistence.mongo;

import com.gogidix.hr.globalhrdashboard.domain.model.CountryHeadcount;
import com.gogidix.hr.globalhrdashboard.domain.repository.CountryHeadcountRepository;
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
 * MongoDB Repository implementation for CountryHeadcount
 * Following Hexagonal Architecture - this is the ADAPTER that implements the PORT
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoCountryHeadcountRepository implements CountryHeadcountRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public CountryHeadcount save(CountryHeadcount headcount) {
        return mongoTemplate.save(headcount);
    }

    @Override
    public Optional<CountryHeadcount> findByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, CountryHeadcount.class));
    }

    @Override
    public List<CountryHeadcount> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, CountryHeadcount.class);
    }

    @Override
    public List<CountryHeadcount> findByTenantIdAndCountryCode(String tenantId, String countryCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").is(countryCode.toUpperCase())
        );
        return mongoTemplate.find(query, CountryHeadcount.class);
    }

    @Override
    public Optional<CountryHeadcount> findByTenantIdAndCountryCodeAndDepartmentAndPeriod(
            String tenantId, String countryCode, String department, String period) {
        Criteria criteria = Criteria.where("tenantId").is(tenantId)
                .and("countryCode").is(countryCode.toUpperCase())
                .and("period").is(period);

        if (department != null && !department.isBlank()) {
            criteria.and("department").is(department);
        } else {
            criteria.and("department").exists(false);
        }

        Query query = Query.query(criteria);
        return Optional.ofNullable(mongoTemplate.findOne(query, CountryHeadcount.class));
    }

    @Override
    public List<CountryHeadcount> findByTenantIdAndPeriod(String tenantId, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        return mongoTemplate.find(query, CountryHeadcount.class);
    }

    @Override
    public List<CountryHeadcount> findByTenantIdAndDepartment(String tenantId, String department) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("department").is(department)
        );
        return mongoTemplate.find(query, CountryHeadcount.class);
    }

    @Override
    public List<CountryHeadcount> findByTenantIdAndRegionCode(String tenantId, String regionCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("regionCode").is(regionCode)
        );
        return mongoTemplate.find(query, CountryHeadcount.class);
    }

    @Override
    public List<CountryHeadcount> findByTenantIdAndCountryCodeAndPeriod(String tenantId, String countryCode, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").is(countryCode.toUpperCase())
                        .and("period").is(period)
        );
        return mongoTemplate.find(query, CountryHeadcount.class);
    }

    @Override
    public List<CountryHeadcount> findActiveByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isActive").is(true)
        );
        return mongoTemplate.find(query, CountryHeadcount.class);
    }

    @Override
    public List<String> findDistinctCountriesByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        query.fields().include("countryCode");
        return mongoTemplate.findDistinct(query, "countryCode", CountryHeadcount.class, String.class);
    }

    @Override
    public List<String> findDistinctDepartmentsByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("department").ne(null)
        );
        query.fields().include("department");
        return mongoTemplate.findDistinct(query, "department", CountryHeadcount.class, String.class);
    }

    @Override
    public List<CountryHeadcount> findByTenantIdAndLastUpdatedAfter(String tenantId, Instant lastUpdated) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("lastUpdated").gt(lastUpdated)
        );
        return mongoTemplate.find(query, CountryHeadcount.class);
    }

    @Override
    public Optional<CountryHeadcount> findGlobalTotalByTenantIdAndPeriod(String tenantId, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
                        .and("countryCode").is("GLOBAL")
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, CountryHeadcount.class));
    }

    @Override
    public List<CountryHeadcount> findTrendDataByCountry(String tenantId, String countryCode,
                                                          String startPeriod, String endPeriod) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").is(countryCode.toUpperCase())
                        .and("period").gte(startPeriod).lte(endPeriod)
        ).with(Sort.by(Sort.Direction.ASC, "period"));
        return mongoTemplate.find(query, CountryHeadcount.class);
    }

    @Override
    public List<CountryHeadcount> findTrendDataByDepartment(String tenantId, String department,
                                                            String startPeriod, String endPeriod) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("department").is(department)
                        .and("period").gte(startPeriod).lte(endPeriod)
        ).with(Sort.by(Sort.Direction.ASC, "period"));
        return mongoTemplate.find(query, CountryHeadcount.class);
    }

    @Override
    public void deleteByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, CountryHeadcount.class);
    }

    @Override
    public void deleteByTenantIdAndPeriod(String tenantId, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        mongoTemplate.remove(query, CountryHeadcount.class);
    }

    @Override
    public boolean existsByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, CountryHeadcount.class);
    }

    @Override
    public List<CountryHeadcount> saveAll(List<CountryHeadcount> headcounts) {
        return headcounts.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public long countByTenantIdAndCountryCode(String tenantId, String countryCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").is(countryCode.toUpperCase())
        );
        return mongoTemplate.count(query, CountryHeadcount.class);
    }

    @Override
    public Integer sumTotalHeadcountByTenantIdAndPeriod(String tenantId, String period) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("tenantId").is(tenantId).and("period").is(period)),
                group().sum("totalHeadcount").as("total")
        );

        AggregationResults<SumResult> results = mongoTemplate.aggregate(
                aggregation, CountryHeadcount.class, SumResult.class
        );

        SumResult result = results.getUniqueMappedResult();
        return result != null ? result.getTotal() : 0;
    }

    @Override
    public List<CountryHeadcount> findByTenantIdAndRegionCodeAndPeriod(String tenantId, String regionCode, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("regionCode").is(regionCode)
                        .and("period").is(period)
        );
        return mongoTemplate.find(query, CountryHeadcount.class);
    }

    @Override
    public List<CountryHeadcount> findLatestByPeriod(String tenantId, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        return mongoTemplate.find(query, CountryHeadcount.class);
    }

    private static class SumResult {
        private int total;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}

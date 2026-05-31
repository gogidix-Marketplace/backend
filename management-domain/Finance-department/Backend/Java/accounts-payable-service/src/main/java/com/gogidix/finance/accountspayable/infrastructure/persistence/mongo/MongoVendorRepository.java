package com.gogidix.finance.accountspayable.infrastructure.persistence.mongo;

import com.gogidix.finance.accountspayable.domain.model.Vendor;
import com.gogidix.finance.accountspayable.domain.repository.VendorRepository;
import com.gogidix.finance.accountspayable.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Vendor
 * Implements vendor persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoVendorRepository implements VendorRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Vendor save(Vendor vendor) {
        log.debug("Saving vendor: {} for tenant: {}",
            vendor.getVendorId(), vendor.getTenantId());
        return mongoTemplate.save(vendor);
    }

    @Override
    public List<Vendor> saveAll(List<Vendor> vendors) {
        return vendors.stream()
            .map(mongoTemplate::save)
            .toList();
    }

    @Override
    public Optional<Vendor> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Vendor.class));
    }

    @Override
    public Optional<Vendor> findByVendorIdAndTenantId(String vendorId, String tenantId) {
        Query query = Query.query(
            Criteria.where("vendorId").is(vendorId)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Vendor.class));
    }

    @Override
    public Optional<Vendor> findByVendorCodeAndTenantId(String vendorCode, String tenantId) {
        Query query = Query.query(
            Criteria.where("vendorCode").is(vendorCode)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Vendor.class));
    }

    @Override
    public List<Vendor> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Vendor.class);
    }

    @Override
    public List<Vendor> findByTenantIdAndStatus(String tenantId, Vendor.VendorStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.find(query, Vendor.class);
    }

    @Override
    public List<Vendor> findByTenantIdAndVendorType(String tenantId, Vendor.VendorType vendorType) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("vendorType").is(vendorType)
        );
        return mongoTemplate.find(query, Vendor.class);
    }

    @Override
    public List<Vendor> findByTenantIdAndIsPreferredVendorTrue(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("isPreferredVendor").is(true)
        );
        return mongoTemplate.find(query, Vendor.class);
    }

    @Override
    public List<Vendor> findByTenantIdAndTagsContaining(String tenantId, String tag) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("tags").is(tag)
        );
        return mongoTemplate.find(query, Vendor.class);
    }

    @Override
    public List<Vendor> searchByName(String tenantId, String searchTerm) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .orOperator(
                    Criteria.where("vendorName").regex(searchTerm, "i"),
                    Criteria.where("vendorCode").regex(searchTerm, "i"),
                    Criteria.where("contactPerson").regex(searchTerm, "i")
                )
        );
        return mongoTemplate.find(query, Vendor.class);
    }

    @Override
    public List<Vendor> findByTenantIdAndEmailContaining(String tenantId, String email) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("email").regex(email, "i")
        );
        return mongoTemplate.find(query, Vendor.class);
    }

    @Override
    public List<Vendor> findByTenantIdAndTaxId(String tenantId, String taxId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("taxId").is(taxId)
        );
        return mongoTemplate.find(query, Vendor.class);
    }

    @Override
    public boolean existsByVendorCodeAndTenantId(String vendorCode, String tenantId) {
        Query query = Query.query(
            Criteria.where("vendorCode").is(vendorCode)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Vendor.class);
    }

    @Override
    public boolean existsByVendorIdAndTenantId(String vendorId, String tenantId) {
        Query query = Query.query(
            Criteria.where("vendorId").is(vendorId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Vendor.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Vendor.class);
    }

    @Override
    public void deleteByVendorIdAndTenantId(String vendorId, String tenantId) {
        Query query = Query.query(
            Criteria.where("vendorId").is(vendorId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Vendor.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, Vendor.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, Vendor.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, Vendor.VendorStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.count(query, Vendor.class);
    }
}

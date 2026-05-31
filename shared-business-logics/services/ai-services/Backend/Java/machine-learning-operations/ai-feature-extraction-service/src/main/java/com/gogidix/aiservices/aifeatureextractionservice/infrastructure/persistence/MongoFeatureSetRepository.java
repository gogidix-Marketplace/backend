package com.gogidix.aiservices.aifeatureextractionservice.infrastructure.persistence;

import com.gogidix.aiservices.aifeatureextractionservice.domain.model.FeatureSet;
import com.gogidix.aiservices.aifeatureextractionservice.domain.port.out.FeatureSetRepositoryPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB implementation of FeatureSetRepositoryPort.
 */
@Repository
public class MongoFeatureSetRepository implements FeatureSetRepositoryPort {

    private final SpringDataFeatureSetRepository repository;
    private final MongoTemplate mongoTemplate;

    public MongoFeatureSetRepository(
            SpringDataFeatureSetRepository repository,
            MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public FeatureSet save(FeatureSet featureSet) {
        return repository.save(featureSet);
    }

    @Override
    public Optional<FeatureSet> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Optional<FeatureSet> findByIdAndTenantId(String id, String tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    @Override
    public List<FeatureSet> findByTenantId(String tenantId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return repository.findByTenantId(tenantId, pageRequest).getContent();
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsById(id);
    }
}

package com.gogidix.sales.dealmanagement.infrastructure.persistence.mongo;

import com.gogidix.sales.dealmanagement.domain.model.Competitor;
import com.gogidix.sales.dealmanagement.domain.repository.CompetitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Competitor Repository Implementation
 */
@Repository
@RequiredArgsConstructor
public class MongoCompetitorRepository implements CompetitorRepository {

    private final CompetitorMongoRepository mongoRepository;

    @Override
    public Competitor save(Competitor competitor) {
        return mongoRepository.save(competitor);
    }

    @Override
    public Optional<Competitor> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Optional<Competitor> findByCompetitorIdAndTenantId(String competitorId, String tenantId) {
        return mongoRepository.findByCompetitorIdAndTenantId(competitorId, tenantId);
    }

    @Override
    public List<Competitor> findByDealIdAndTenantId(String dealId, String tenantId) {
        return mongoRepository.findByDealIdAndTenantId(dealId, tenantId);
    }

    @Override
    public List<Competitor> findAllByTenantId(String tenantId) {
        return mongoRepository.findAllByTenantId(tenantId);
    }

    @Override
    public List<Competitor> findByCompetitorNameAndTenantId(String competitorName, String tenantId) {
        return mongoRepository.findByCompetitorNameAndTenantId(competitorName, tenantId);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public void deleteByCompetitorIdAndTenantId(String competitorId, String tenantId) {
        mongoRepository.deleteByCompetitorIdAndTenantId(competitorId, tenantId);
    }

    @Override
    public void deleteByDealIdAndTenantId(String dealId, String tenantId) {
        mongoRepository.deleteByDealIdAndTenantId(dealId, tenantId);
    }

    @Override
    public boolean existsByCompetitorIdAndTenantId(String competitorId, String tenantId) {
        return mongoRepository.existsByCompetitorIdAndTenantId(competitorId, tenantId);
    }

    /**
     * Spring Data MongoDB inner interface
     */
    interface CompetitorMongoRepository extends MongoRepository<Competitor, String> {

        Optional<Competitor> findByCompetitorIdAndTenantId(String competitorId, String tenantId);

        List<Competitor> findByDealIdAndTenantId(String dealId, String tenantId);

        List<Competitor> findAllByTenantId(String tenantId);

        List<Competitor> findByCompetitorNameAndTenantId(String competitorName, String tenantId);

        void deleteByCompetitorIdAndTenantId(String competitorId, String tenantId);

        void deleteByDealIdAndTenantId(String dealId, String tenantId);

        boolean existsByCompetitorIdAndTenantId(String competitorId, String tenantId);
    }
}

package com.gogidix.digitalmarketing.marketingautomation.domain.repository;

import com.gogidix.digitalmarketing.marketingautomation.domain.model.Campaign;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends MongoRepository<Campaign, String> {
    List<Campaign> findByTenantId(String tenantId);
}
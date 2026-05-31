package com.gogidix.digitalmarketing.countrymarketingdashboard.domain.repository;

import com.gogidix.digitalmarketing.countrymarketingdashboard.domain.model.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;

@Repository
public interface CampaignRepository extends MongoRepository<Campaign, String> {
    List<Campaign> findByTenantIdAndCountry(String tenantId, String country);
    Page<Campaign> findByTenantIdAndCountry(String tenantId, String country, Pageable pageable);
    List<Campaign> findByTenantIdAndStatus(String tenantId, String status);
    List<Campaign> findByTenantIdAndCountryAndStatus(String tenantId, String country, String status);
    long countByTenantIdAndCountryAndStatus(String tenantId, String country, String status);
    List<Campaign> findCampaignsForDashboard(String tenantId, String country);
    List<Campaign> findActiveCampaigns(String tenantId, String country, Instant date);
}

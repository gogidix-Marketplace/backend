package com.gogidix.digitalmarketing.brandmanagement.domain.repository;

import com.gogidix.digitalmarketing.brandmanagement.domain.model.BrandAsset;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandAssetRepository extends MongoRepository<BrandAsset, String> {
    List<BrandAsset> findByTenantId(String tenantId);
}
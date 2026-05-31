package com.gogidix.ecommerce.search.infrastructure.persistence.repository;

import com.gogidix.ecommerce.search.domain.model.Search;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SearchMongoRepository extends MongoRepository<Search, String> {
    List<Search> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}

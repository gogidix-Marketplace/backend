package com.gogidix.ecommerce.search.domain.repository;

import com.gogidix.ecommerce.search.domain.model.Search;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepository extends MongoRepository<Search, String> {
    List<Search> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
}

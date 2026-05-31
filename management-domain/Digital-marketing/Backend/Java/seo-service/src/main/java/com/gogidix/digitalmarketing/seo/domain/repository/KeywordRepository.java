package com.gogidix.digitalmarketing.seo.domain.repository;

import com.gogidix.digitalmarketing.seo.domain.model.Keyword;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends MongoRepository<Keyword, String> {
    List<Keyword> findByTenantId(String tenantId);
}
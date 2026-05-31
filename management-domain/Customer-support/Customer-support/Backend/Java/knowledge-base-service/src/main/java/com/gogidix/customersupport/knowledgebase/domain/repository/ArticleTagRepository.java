package com.gogidix.customersupport.knowledgebase.domain.repository;

import com.gogidix.customersupport.knowledgebase.domain.model.ArticleTag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleTagRepository extends MongoRepository<ArticleTag, String> {

    List<ArticleTag> findByTenantId(String tenantId);

    Optional<ArticleTag> findByName(String name);

    Optional<ArticleTag> findBySlug(String slug);

    List<ArticleTag> findByTenantIdOrderByUsageCountDesc(String tenantId);

    void deleteByTenantIdAndId(String tenantId, String id);

    boolean existsByName(String name);
}

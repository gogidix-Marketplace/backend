package com.gogidix.customersupport.knowledgebase.domain.repository;

import com.gogidix.customersupport.knowledgebase.domain.model.ArticleCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleCategoryRepository extends MongoRepository<ArticleCategory, String> {

    List<ArticleCategory> findByTenantId(String tenantId);

    List<ArticleCategory> findByTenantIdAndIsActiveTrue(String tenantId);

    Optional<ArticleCategory> findBySlug(String slug);

    List<ArticleCategory> findByTenantIdAndParentCategoryId(String tenantId, String parentCategoryId);

    List<ArticleCategory> findByTenantIdAndParentCategoryIdIsNull(String tenantId);

    void deleteByTenantIdAndId(String tenantId, String id);

    boolean existsBySlug(String slug);
}

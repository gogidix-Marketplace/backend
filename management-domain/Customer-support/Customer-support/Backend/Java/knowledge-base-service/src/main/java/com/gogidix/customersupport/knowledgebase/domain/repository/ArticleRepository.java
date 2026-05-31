package com.gogidix.customersupport.knowledgebase.domain.repository;

import com.gogidix.customersupport.knowledgebase.domain.model.Article;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends MongoRepository<Article, String> {

    List<Article> findByTenantId(String tenantId);

    Optional<Article> findByTenantIdAndId(String tenantId, String id);

    Optional<Article> findBySlug(String slug);

    List<Article> findByTenantIdAndStatus(String tenantId, Article.ArticleStatus status);

    List<Article> findByTenantIdAndCategoryId(String tenantId, String categoryId);

    List<Article> findByTenantIdAndTagsContaining(String tenantId, String tag);

    List<Article> findByTenantIdAndAuthorId(String tenantId, String authorId);

    List<Article> findByTenantIdAndIsFeaturedTrue(String tenantId);

    @Query("{ 'tenantId': ?0, 'status': 'PUBLISHED', '$or': [ {'title': { $regex: ?1, $options: 'i' } }, {'content': { $regex: ?1, $options: 'i' } }, {'summary': { $regex: ?1, $options: 'i' } }, {'tags': { $in: [?1] } } ] }")
    List<Article> searchArticles(String tenantId, String keyword);

    @Query("{ 'tenantId': ?0, 'categoryId': ?1, 'status': 'PUBLISHED' }")
    List<Article> findPublishedByCategory(String tenantId, String categoryId);

    @Query(value = "{ 'tenantId': ?0, 'status': 'PUBLISHED' }", sort = "{ 'views': -1 }")
    List<Article> findMostViewed(String tenantId);

    @Query(value = "{ 'tenantId': ?0, 'status': 'PUBLISHED' }", sort = "{ 'helpfulCount': -1 }")
    List<Article> findMostHelpful(String tenantId);

    List<Article> findByTenantIdAndLanguage(String tenantId, String language);

    void deleteByTenantIdAndId(String tenantId, String id);

    boolean existsBySlug(String slug);

    long countByTenantIdAndCategoryId(String tenantId, String categoryId);
}

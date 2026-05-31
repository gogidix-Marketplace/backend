package com.gogidix.corporate.website.domain.repository;

import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.Product;
import com.gogidix.corporate.website.domain.model.Region;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(String id);
    Optional<Product> findByProductKey(String productKey);
    Optional<Product> findBySlug(String slug);
    List<Product> findByStatus(ContentStatus status);
    List<Product> findPublishedProducts();
    List<Product> findPublishedProductsByRegion(Region region);
    List<Product> findFeaturedProducts();
    List<Product> findByCategory(String category);
    List<Product> findByProductType(String productType);
    List<Product> findByTagsContaining(String tag);
    List<Product> searchByKeyword(String keyword, Language language);
    List<Product> findRelatedProducts(String productId, int limit);
    void deleteById(String id);
    boolean existsByProductKey(String productKey);
    boolean existsBySlug(String slug);
    long countByStatus(ContentStatus status);
    long countByCategory(String category);
}

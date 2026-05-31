package com.gogidix.corporatecms.domain.repository;

import com.gogidix.corporatecms.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Product entity.
 */
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    Optional<Product> findBySlugAndDeletedFalse(String slug);

    Optional<Product> findBySkuAndDeletedFalse(String sku);

    List<Product> findByCategoryIdAndDeletedFalse(String categoryId);

    Page<Product> findByCategoryIdAndDeletedFalse(String categoryId, Pageable pageable);

    Page<Product> findByPublishedTrueAndDeletedFalse(Pageable pageable);

    @Query("{'$or': [" +
            "{'name': {$regex: ?0, $options: 'i'}}, " +
            "{'description': {$regex: ?0, $options: 'i'}}, " +
            "{'tagline': {$regex: ?0, $options: 'i'}}" +
            "], 'deleted': false}")
    Page<Product> searchByKeyword(String keyword, Pageable pageable);

    @Query("{'tags': {$in: ?0}, 'deleted': false}")
    List<Product> findByTagsIn(List<String> tags);

    List<Product> findByDeletedFalseOrderBySortOrderAsc();

    Long countByCategoryIdAndDeletedFalse(String categoryId);

    @Query("{'published': true, 'deleted': false}")
    Long countPublishedProducts();
}

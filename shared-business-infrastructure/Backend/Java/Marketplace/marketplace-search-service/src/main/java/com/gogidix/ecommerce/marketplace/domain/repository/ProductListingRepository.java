package com.gogidix.ecommerce.marketplace.domain.repository;

import com.gogidix.ecommerce.marketplace.domain.model.ProductListing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductListingRepository extends MongoRepository<ProductListing, String> {

    Page<ProductListing> findByTenantIdAndIsActive(String tenantId, Boolean isActive, Pageable pageable);

    @Query("{ 'tenantId': ?0, 'isActive': true, '$or': [ " +
           "{ 'name': { $regex: ?1, $options: 'i' } }, " +
           "{ 'description': { $regex: ?1, $options: 'i' } }, " +
           "{ 'tags': { $in: [?1] } } " +
           "]}")
    Page<ProductListing> search(String tenantId, String query, Pageable pageable);

    Page<ProductListing> findByTenantIdAndCategoryIdAndIsActive(String tenantId, String categoryId, Boolean isActive, Pageable pageable);

    @Query("{ 'tenantId': ?0, 'isActive': true, 'price': { $gte: ?1, $lte: ?2 } }")
    Page<ProductListing> findByPriceRange(String tenantId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    Page<ProductListing> findByVendorIdAndIsActive(String vendorId, Boolean isActive, Pageable pageable);
}

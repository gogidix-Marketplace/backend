package com.gogidix.ecommerce.category.domain.repository;

import com.gogidix.ecommerce.category.domain.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    List<Category> findByTenantIdAndIsActiveOrderByDisplayOrder(String tenantId, Boolean isActive);

    List<Category> findByTenantIdAndParentIdAndIsActiveOrderByDisplayOrder(String tenantId, String parentId, Boolean isActive);

    Category findByTenantIdAndCategoryCode(String tenantId, String categoryCode);

    List<Category> findByTenantIdAndPathStartingWith(String tenantId, String path);

    List<Category> findByTenantIdAndLevelAndIsActive(String tenantId, Integer level, Boolean isActive);

    List<Category> findByTenantIdAndParentIdIsNull(String tenantId);

    boolean existsByTenantIdAndCategoryCode(String tenantId, String categoryCode);

    List<Category> findByTenantIdAndParentId(String tenantId, String parentId);
}

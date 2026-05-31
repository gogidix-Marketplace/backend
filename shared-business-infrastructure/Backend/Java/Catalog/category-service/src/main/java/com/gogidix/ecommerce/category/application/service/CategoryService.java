package com.gogidix.ecommerce.category.application.service;

import com.gogidix.ecommerce.category.application.dto.*;
import com.gogidix.ecommerce.category.application.mapper.CategoryMapper;
import com.gogidix.ecommerce.category.domain.model.Category;
import com.gogidix.ecommerce.category.domain.repository.CategoryRepository;
import com.gogidix.ecommerce.category.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> getAllCategories() {
        String tenantId = RequestContextHolder.getTenantId();
        return categoryRepository.findByTenantIdAndIsActiveOrderByDisplayOrder(tenantId, true)
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    public List<CategoryResponse> getRootCategories() {
        String tenantId = RequestContextHolder.getTenantId();
        return categoryRepository.findByTenantIdAndParentIdIsNull(tenantId)
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    public List<CategoryResponse> getSubCategories(String parentId) {
        String tenantId = RequestContextHolder.getTenantId();
        return categoryRepository.findByTenantIdAndParentIdAndIsActiveOrderByDisplayOrder(tenantId, parentId, true)
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        return categoryMapper.toCategoryResponse(category);
    }

    public CategoryResponse getCategoryByCode(String categoryCode) {
        String tenantId = RequestContextHolder.getTenantId();
        Category category = categoryRepository.findByTenantIdAndCategoryCode(tenantId, categoryCode);
        if (category == null) {
            throw new IllegalArgumentException("Category not found with code: " + categoryCode);
        }
        return categoryMapper.toCategoryResponse(category);
    }

    public List<CategoryResponse> getCategoriesByLevel(Integer level) {
        String tenantId = RequestContextHolder.getTenantId();
        return categoryRepository.findByTenantIdAndLevelAndIsActive(tenantId, level, true)
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    public List<CategoryResponse> getCategoryTree() {
        String tenantId = RequestContextHolder.getTenantId();
        List<Category> rootCategories = categoryRepository.findByTenantIdAndParentIdIsNull(tenantId);
        rootCategories.forEach(this::loadChildren);
        return categoryMapper.toCategoryResponseList(rootCategories);
    }

    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        String tenantId = RequestContextHolder.getTenantId();

        if (categoryRepository.existsByTenantIdAndCategoryCode(tenantId, request.categoryCode())) {
            throw new IllegalArgumentException("Category with code already exists: " + request.categoryCode());
        }

        Category category = categoryMapper.toCategory(request);
        category.setTenantId(tenantId);

        if (request.parentId() != null) {
            Category parent = categoryRepository.findById(request.parentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent category not found"));
            category.setLevel(parent.getLevel() != null ? parent.getLevel() + 1 : 1);
            category.setPath(parent.getPath() + "/" + request.categoryCode());
            category.setIsLeaf(false);
            parent.setIsLeaf(false);
            categoryRepository.save(parent);
        } else {
            category.setLevel(0);
            category.setPath(request.categoryCode());
        }

        Category saved = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(saved);
    }

    @Transactional
    public CategoryResponse updateCategory(String id, UpdateCategoryRequest request) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        categoryMapper.updateCategoryFromRequest(existing, request);
        existing.updateTimestamp();

        Category saved = categoryRepository.save(existing);
        return categoryMapper.toCategoryResponse(saved);
    }

    @Transactional
    public void deleteCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        List<Category> children = categoryRepository.findByTenantIdAndParentId(
                RequestContextHolder.getTenantId(), id);

        if (!children.isEmpty()) {
            throw new IllegalStateException("Cannot delete category with children. Move or delete children first.");
        }

        categoryRepository.deleteById(id);
    }

    private void loadChildren(Category category) {
        List<Category> children = categoryRepository.findByTenantIdAndParentIdAndIsActiveOrderByDisplayOrder(
                category.getTenantId(), category.getId(), true);
        category.setChildren(children);
        children.forEach(this::loadChildren);
    }
}

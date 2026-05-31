package com.gogidix.ecommerce.category.interfaces.rest;

import com.gogidix.ecommerce.category.application.dto.*;
import com.gogidix.ecommerce.category.application.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Category Service", description = "APIs for managing category service operations")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieve all categories for a tenant")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/root")
    @Operation(summary = "Get root categories", description = "Retrieve all root level categories")
    public ResponseEntity<List<CategoryResponse>> getRootCategories() {
        return ResponseEntity.ok(categoryService.getRootCategories());
    }

    @GetMapping("/tree")
    @Operation(summary = "Get category tree", description = "Retrieve the complete category tree structure")
    public ResponseEntity<List<CategoryResponse>> getCategoryTree() {
        return ResponseEntity.ok(categoryService.getCategoryTree());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get category by ID", description = "Retrieve a specific category by its ID")
    public ResponseEntity<CategoryResponse> getCategory(@Parameter(description = "Category ID") @PathVariable String id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get category by code", description = "Retrieve a specific category by its code")
    public ResponseEntity<CategoryResponse> getCategoryByCode(@Parameter(description = "Category code") @PathVariable String code) {
        return ResponseEntity.ok(categoryService.getCategoryByCode(code));
    }

    @GetMapping("/{parentId}/children")
    @Operation(summary = "Get subcategories", description = "Retrieve all child categories of a parent category")
    public ResponseEntity<List<CategoryResponse>> getSubCategories(
            @Parameter(description = "Parent category ID") @PathVariable String parentId) {
        return ResponseEntity.ok(categoryService.getSubCategories(parentId));
    }

    @GetMapping("/level/{level}")
    @Operation(summary = "Get categories by level", description = "Retrieve categories at a specific hierarchy level")
    public ResponseEntity<List<CategoryResponse>> getCategoriesByLevel(
            @Parameter(description = "Hierarchy level") @PathVariable Integer level) {
        return ResponseEntity.ok(categoryService.getCategoriesByLevel(level));
    }

    @PostMapping
    @Operation(summary = "Create category", description = "Create a new category")
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CreateCategoryRequest request) {
        return ResponseEntity.ok(categoryService.createCategory(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Update an existing category")
    public ResponseEntity<CategoryResponse> updateCategory(
            @Parameter(description = "Category ID") @PathVariable String id,
            @Valid @RequestBody UpdateCategoryRequest request) {
        return ResponseEntity.ok(categoryService.updateCategory(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category", description = "Delete a category")
    public ResponseEntity<Void> deleteCategory(@Parameter(description = "Category ID") @PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}

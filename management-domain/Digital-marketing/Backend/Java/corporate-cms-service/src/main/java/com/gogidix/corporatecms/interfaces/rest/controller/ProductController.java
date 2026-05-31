package com.gogidix.corporatecms.interfaces.rest.controller;

import com.gogidix.corporatecms.application.dto.ApiResponse;
import com.gogidix.corporatecms.application.dto.PageResponse;
import com.gogidix.corporatecms.application.dto.ProductDTO;
import com.gogidix.corporatecms.domain.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for product catalog management.
 */
@Tag(name = "Products", description = "Product catalog APIs")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create product", description = "Create a new product")
    @PreAuthorize("hasAuthority('PRODUCT_MANAGE')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@Valid @RequestBody ProductDTO dto) {
        ProductDTO product = productService.createProduct(dto);
        return ResponseEntity.status(201).body(ApiResponse.created(product));
    }

    @Operation(summary = "Update product", description = "Update an existing product")
    @PreAuthorize("hasAuthority('PRODUCT_MANAGE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
            @Parameter(description = "Product ID") @PathVariable String id,
            @Valid @RequestBody ProductDTO dto) {
        ProductDTO product = productService.updateProduct(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", product));
    }

    @Operation(summary = "Get product by ID", description = "Retrieve a product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(
            @Parameter(description = "Product ID") @PathVariable String id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    @Operation(summary = "Get product by slug", description = "Retrieve a product by slug")
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductBySlug(
            @Parameter(description = "Product slug") @PathVariable String slug) {
        ProductDTO product = productService.getProductBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    @Operation(summary = "Get product by SKU", description = "Retrieve a product by SKU")
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductBySku(
            @Parameter(description = "Product SKU") @PathVariable String sku) {
        ProductDTO product = productService.getProductBySku(sku);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    @Operation(summary = "Get products by category", description = "Retrieve products filtered by category")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<PageResponse<ProductDTO>>> getProductsByCategory(
            @Parameter(description = "Category ID") @PathVariable String categoryId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponse<ProductDTO> products = productService.getProductsByCategory(categoryId, page, size);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @Operation(summary = "Get published products", description = "Retrieve all published products")
    @GetMapping("/published")
    public ResponseEntity<ApiResponse<PageResponse<ProductDTO>>> getPublishedProducts(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponse<ProductDTO> products = productService.getPublishedProducts(page, size);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @Operation(summary = "Search products", description = "Search products by keyword")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ProductDTO>>> searchProducts(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponse<ProductDTO> products = productService.searchProducts(keyword, page, size);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @Operation(summary = "Publish product", description = "Publish a product")
    @PreAuthorize("hasAuthority('PRODUCT_MANAGE')")
    @PostMapping("/{id}/publish")
    public ResponseEntity<ApiResponse<ProductDTO>> publishProduct(
            @Parameter(description = "Product ID") @PathVariable String id) {
        ProductDTO product = productService.publishProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product published successfully", product));
    }

    @Operation(summary = "Unpublish product", description = "Unpublish a product")
    @PreAuthorize("hasAuthority('PRODUCT_MANAGE')")
    @PostMapping("/{id}/unpublish")
    public ResponseEntity<ApiResponse<ProductDTO>> unpublishProduct(
            @Parameter(description = "Product ID") @PathVariable String id) {
        ProductDTO product = productService.unpublishProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product unpublished successfully", product));
    }

    @Operation(summary = "Delete product", description = "Delete a product")
    @PreAuthorize("hasAuthority('PRODUCT_MANAGE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @Parameter(description = "Product ID") @PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", null));
    }

    @Operation(summary = "Get all products", description = "Retrieve all products")
    @GetMapping
    @PreAuthorize("hasAuthority('PRODUCT_MANAGE')")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }
}

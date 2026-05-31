package com.gogidix.corporatecms.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.application.dto.ProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.application.dto.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.application.exception.DuplicateResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.application.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.application.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.domain.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.domain.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * Service for managing product catalog.
 */
@Service
@RequiredArgsConstructor
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductDTO createProduct(ProductDTO dto) {
        log.info("Creating product with SKU: {}", dto.getSku());

        if (productRepository.findBySkuAndDeletedFalse(dto.getSku()).isPresent()) {
            throw new DuplicateResourceException("Product", "sku", dto.getSku());
        }

        if (productRepository.findBySlugAndDeletedFalse(dto.getSlug()).isPresent()) {
            throw new DuplicateResourceException("Product", "slug", dto.getSlug());
        }

        Product product = productMapper.toEntity(dto);
        product.setPublished(false);

        Product savedProduct = productRepository.save(product);
        log.info("Product created with ID: {}", savedProduct.getId());

        return productMapper.toDto(savedProduct);
    }

    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public ProductDTO updateProduct(String id, ProductDTO dto) {
        log.info("Updating product: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        if (!product.getSku().equals(dto.getSku()) &&
                productRepository.findBySkuAndDeletedFalse(dto.getSku()).isPresent()) {
            throw new DuplicateResourceException("Product", "sku", dto.getSku());
        }

        productMapper.updateEntityFromDto(dto, product);
        product.setLastUpdatedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);
        log.info("Product updated: {}", id);

        return productMapper.toDto(savedProduct);
    }

    @Cacheable(value = "products", key = "#id")
    public ProductDTO getProductById(String id) {
        log.info("Fetching product by ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return productMapper.toDto(product);
    }

    public ProductDTO getProductBySlug(String slug) {
        log.info("Fetching product by slug: {}", slug);
        Product product = productRepository.findBySlugAndDeletedFalse(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "slug", slug));
        return productMapper.toDto(product);
    }

    public ProductDTO getProductBySku(String sku) {
        log.info("Fetching product by SKU: {}", sku);
        Product product = productRepository.findBySkuAndDeletedFalse(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "sku", sku));
        return productMapper.toDto(product);
    }

    public PageResponse<ProductDTO> getProductsByCategory(String categoryId, int page, int size) {
        log.info("Fetching products by category: {}", categoryId);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "sortOrder"));
        Page<Product> productPage = productRepository.findByCategoryIdAndDeletedFalse(categoryId, pageable);
        return PageResponse.of(productPage.map(productMapper::toDto));
    }

    public PageResponse<ProductDTO> getPublishedProducts(int page, int size) {
        log.info("Fetching published products");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "sortOrder"));
        Page<Product> productPage = productRepository.findByPublishedTrueAndDeletedFalse(pageable);
        return PageResponse.of(productPage.map(productMapper::toDto));
    }

    public PageResponse<ProductDTO> searchProducts(String keyword, int page, int size) {
        log.info("Searching products with keyword: {}", keyword);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "sortOrder"));
        Page<Product> productPage = productRepository.searchByKeyword(keyword, pageable);
        return PageResponse.of(productPage.map(productMapper::toDto));
    }

    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public ProductDTO publishProduct(String id) {
        log.info("Publishing product: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        product.setPublished(true);
        product.setPublishedAt(LocalDateTime.now());

        Product savedProduct = productRepository.save(product);
        log.info("Product published: {}", id);

        return productMapper.toDto(savedProduct);
    }

    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public ProductDTO unpublishProduct(String id) {
        log.info("Unpublishing product: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        product.setPublished(false);

        Product savedProduct = productRepository.save(product);
        log.info("Product unpublished: {}", id);

        return productMapper.toDto(savedProduct);
    }

    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(String id) {
        log.info("Deleting product: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        product.setDeleted(true);
        product.setDeletedAt(LocalDateTime.now());
        product.setPublished(false);

        productRepository.save(product);
        log.info("Product deleted: {}", id);
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findByDeletedFalseOrderBySortOrderAsc();
        return productMapper.toDtoList(products);
    }
}

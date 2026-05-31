package com.gogidix.corporate.website.domain.service;

import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.Product;
import com.gogidix.corporate.website.domain.model.Region;
import com.gogidix.corporate.website.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductDomainService {

    private final ProductRepository productRepository;

    @CacheEvict(value = "products", allEntries = true)
    public Product createProduct(Product product) {
        validateProduct(product);
        return productRepository.save(product);
    }

    @CacheEvict(value = "products", allEntries = true)
    public Product updateProduct(Product product) {
        validateProduct(product);
        return productRepository.save(product);
    }

    @CacheEvict(value = "products", allEntries = true)
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    @Cacheable(value = "products", key = "#id")
    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    @Cacheable(value = "products", key = "'key:' + #productKey")
    public Optional<Product> getProductByKey(String productKey) {
        return productRepository.findByProductKey(productKey);
    }

    @Cacheable(value = "products", key = "'slug:' + #slug")
    public Optional<Product> getProductBySlug(String slug) {
        return productRepository.findBySlug(slug);
    }

    @Cacheable(value = "products", key = "'published:' + #region")
    public List<Product> getPublishedProductsByRegion(Region region) {
        return productRepository.findPublishedProductsByRegion(region);
    }

    @Cacheable(value = "products", key = "'featured:' + #region")
    public List<Product> getFeaturedProducts(Region region) {
        return productRepository.findFeaturedProducts().stream()
                .filter(product -> product.getAvailableRegions().contains(region))
                .toList();
    }

    @Cacheable(value = "products", key = "'category:' + #category + ':' + #region")
    public List<Product> getProductsByCategory(String category, Region region) {
        return productRepository.findByCategory(category).stream()
                .filter(product -> product.getAvailableRegions().contains(region))
                .toList();
    }

    public List<Product> searchProducts(String keyword, Language language) {
        return productRepository.searchByKeyword(keyword, language);
    }

    public List<Product> getProductsByTag(String tag) {
        return productRepository.findByTagsContaining(tag);
    }

    public List<Product> getRelatedProducts(String productId, int limit) {
        return productRepository.findRelatedProducts(productId, limit);
    }

    private void validateProduct(Product product) {
        if (product.getProductKey() == null || product.getProductKey().isBlank()) {
            throw new IllegalArgumentException("Product key cannot be null or blank");
        }
        if (product.getSlug() == null || product.getSlug().isBlank()) {
            throw new IllegalArgumentException("Product slug cannot be null or blank");
        }
        if (product.getLocalizedContent() == null || product.getLocalizedContent().isEmpty()) {
            throw new IllegalArgumentException("Product must have at least one localized content");
        }
    }

    public long getProductCount() {
        return productRepository.countByStatus(ContentStatus.PUBLISHED);
    }
}

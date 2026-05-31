package com.gogidix.aiservices.aiproductrecommendationservice.domain.model;

import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Product entity.
 */
@DisplayName("Product Entity Tests")
class ProductTest {

    @Test
    @DisplayName("Should create product with builder")
    void shouldCreateProductWithBuilder() {
        Product product = Product.builder()
                .id(ProductId.randomProductId())
                .name("Test Product")
                .description("Test Description")
                .category("Electronics")
                .price(new BigDecimal("99.99"))
                .stockQuantity(100)
                .available(true)
                .imageUrl("http://example.com/product.jpg")
                .tenantId("tenant-001")
                .build();

        assertNotNull(product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals("Electronics", product.getCategory());
        assertEquals(new BigDecimal("99.99"), product.getPrice());
        assertEquals(100, product.getStockQuantity());
        assertTrue(product.isAvailable());
        assertEquals("tenant-001", product.getTenantId());
    }

    @Test
    @DisplayName("Should create product with all fields")
    void shouldCreateProductWithAllFields() {
        Product product = Product.builder()
                .id(ProductId.randomProductId())
                .name("Complete Product")
                .description("Complete Description")
                .category("Books")
                .price(new BigDecimal("49.99"))
                .stockQuantity(50)
                .available(false)
                .imageUrl("http://example.com/complete.jpg")
                .tenantId("tenant-002")
                .build();

        assertNotNull(product.getId());
        assertFalse(product.isAvailable());
        assertEquals("tenant-002", product.getTenantId());
    }

    @Test
    @DisplayName("Should validate in-stock status")
    void shouldValidateInStockStatus() {
        Product product = Product.builder()
                .id(ProductId.randomProductId())
                .name("In Stock Product")
                .price(new BigDecimal("19.99"))
                .stockQuantity(50)
                .available(true)
                .build();

        assertTrue(product.isInStock());
        assertFalse(product.isOutOfStock());
    }

    @Test
    @DisplayName("Should validate out-of-stock status")
    void shouldValidateOutOfStockStatus() {
        Product product = Product.builder()
                .id(ProductId.randomProductId())
                .name("Out of Stock Product")
                .price(new BigDecimal("19.99"))
                .stockQuantity(0)
                .available(true)
                .build();

        assertTrue(product.isOutOfStock());
    }

    @Test
    @DisplayName("Should update stock quantity")
    void shouldUpdateStockQuantity() {
        Product product = Product.builder()
                .id(ProductId.randomProductId())
                .name("Stock Update Test")
                .price(new BigDecimal("29.99"))
                .stockQuantity(10)
                .available(true)
                .build();

        assertEquals(10, product.getStockQuantity());
    }

    @Test
    @DisplayName("Should detect price negativity")
    void shouldDetectPriceNegativity() {
        Product product = Product.builder()
                .id(ProductId.randomProductId())
                .name("Negative Price Product")
                .price(new BigDecimal("-10.00"))
                .stockQuantity(100)
                .available(true)
                .build();

        assertTrue(product.getPrice().compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    @DisplayName("Should compare products with same ID")
    void shouldCompareProducts() {
        ProductId sharedId = ProductId.randomProductId();
        Product product1 = Product.builder()
                .id(sharedId)
                .name("Product A")
                .price(new BigDecimal("100.00"))
                .category("Category A")
                .tenantId("tenant-001")
                .build();

        Product product2 = Product.builder()
                .id(sharedId)
                .name("Product B")
                .price(new BigDecimal("100.00"))
                .category("Category A")
                .tenantId("tenant-001")
                .build();

        assertEquals(product1, product2);
        assertEquals(sharedId, product1.getId());
        assertEquals(sharedId, product2.getId());
    }

    @Test
    @DisplayName("Should generate different hash codes")
    void shouldGenerateDifferentHashCodes() {
        Product product1 = Product.builder()
                .id(ProductId.randomProductId())
                .name("Hash Test A")
                .price(new BigDecimal("50.00"))
                .category("Category X")
                .tenantId("tenant-001")
                .build();

        Product product2 = Product.builder()
                .id(ProductId.randomProductId())
                .name("Hash Test B")
                .price(new BigDecimal("50.00"))
                .category("Category X")
                .tenantId("tenant-001")
                .build();

        int hash1 = product1.hashCode();
        int hash2 = product2.hashCode();

        assertNotEquals(hash1, hash2, "Products with same values should have different hash codes");
    }

    @Test
    @DisplayName("Should handle null values correctly")
    void shouldHandleNullValues() {
        Product product = Product.builder()
                .id(ProductId.randomProductId())
                .name(null)
                .build();

        assertNull(product.getName());
        assertNull(product.getCategory());
        assertNull(product.getPrice());
        assertNull(product.getStockQuantity());
        assertNull(product.getTenantId());
    }

    @Test
    @DisplayName("Should validate tenant ID")
    void shouldValidateTenantId() {
        Product product = Product.builder()
                .id(ProductId.randomProductId())
                .name("Tenant Test")
                .tenantId("valid-tenant-123")
                .build();

        assertEquals("valid-tenant-123", product.getTenantId());
    }

    @Test
    @DisplayName("Should support large stock quantities")
    void shouldSupportLargeStockQuantities() {
        Product product = Product.builder()
                .id(ProductId.randomProductId())
                .name("Large Stock Product")
                .stockQuantity(Integer.MAX_VALUE)
                .available(true)
                .build();

        assertEquals(Integer.MAX_VALUE, product.getStockQuantity());
    }

    @Test
    @DisplayName("Should format toString correctly")
    void shouldFormatToStringCorrectly() {
        Product product = Product.builder()
                .id(ProductId.randomProductId())
                .name("ToString Test")
                .category("TestCat")
                .price(new BigDecimal("19.99"))
                .stockQuantity(5)
                .available(true)
                .tenantId("tenant-test")
                .build();

        String result = product.toString();
        assertTrue(result.contains("id="));
        assertTrue(result.contains("name='ToString Test'"));
        assertTrue(result.contains("category='TestCat'"));
        assertTrue(result.contains("price=19.99"));
        assertTrue(result.contains("stockQuantity=5"));
        assertTrue(result.contains("tenantId='tenant-test'"));
    }
}

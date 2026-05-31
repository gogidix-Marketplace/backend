package com.gogidix.ecommerce.test.util;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Builder class for creating test data for various e-commerce entities.
 */
public class TestDataBuilder {

    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Creates a map representing a customer for testing purposes.
     *
     * @return a map with customer test data
     */
    public static Map<String, Object> customer() {
        Map<String, Object> customer = new HashMap<>();
        customer.put("id", UUID.randomUUID().toString());
        customer.put("email", "test.customer" + System.currentTimeMillis() + "@example.com");
        customer.put("firstName", "Test");
        customer.put("lastName", "Customer");
        customer.put("phoneNumber", "+1234567890");
        customer.put("active", true);
        customer.put("createdAt", LocalDateTime.now().format(ISO_FORMATTER));
        return customer;
    }

    /**
     * Creates a map representing a customer with custom email.
     *
     * @param email the custom email
     * @return a map with customer test data
     */
    public static Map<String, Object> customerWithEmail(String email) {
        Map<String, Object> customer = customer();
        customer.put("email", email);
        return customer;
    }

    /**
     * Creates a map representing a product for testing purposes.
     *
     * @return a map with product test data
     */
    public static Map<String, Object> product() {
        Map<String, Object> product = new HashMap<>();
        product.put("id", UUID.randomUUID().toString());
        product.put("name", "Test Product");
        product.put("description", "A test product for unit testing");
        product.put("sku", "SKU-" + System.currentTimeMillis());
        product.put("price", new BigDecimal("29.99"));
        product.put("currency", "USD");
        product.put("active", true);
        product.put("stockQuantity", 100);
        product.put("createdAt", LocalDateTime.now().format(ISO_FORMATTER));
        return product;
    }

    /**
     * Creates a map representing a product with custom price.
     *
     * @param price the custom price
     * @return a map with product test data
     */
    public static Map<String, Object> productWithPrice(BigDecimal price) {
        Map<String, Object> product = product();
        product.put("price", price);
        return product;
    }

    /**
     * Creates a map representing a cart for testing purposes.
     *
     * @return a map with cart test data
     */
    public static Map<String, Object> cart() {
        Map<String, Object> cart = new HashMap<>();
        cart.put("id", UUID.randomUUID().toString());
        cart.put("customerId", UUID.randomUUID().toString());
        cart.put("items", new ArrayList<>());
        cart.put("subtotal", BigDecimal.ZERO);
        cart.put("currency", "USD");
        cart.put("createdAt", LocalDateTime.now().format(ISO_FORMATTER));
        cart.put("updatedAt", LocalDateTime.now().format(ISO_FORMATTER));
        return cart;
    }

    /**
     * Creates a map representing a cart with customer ID.
     *
     * @param customerId the customer ID
     * @return a map with cart test data
     */
    public static Map<String, Object> cartWithCustomerId(String customerId) {
        Map<String, Object> cart = cart();
        cart.put("customerId", customerId);
        return cart;
    }

    /**
     * Creates a map representing a cart item for testing purposes.
     *
     * @param productId the product ID
     * @param quantity the quantity
     * @return a map with cart item test data
     */
    public static Map<String, Object> cartItem(String productId, int quantity) {
        Map<String, Object> item = new HashMap<>();
        item.put("id", UUID.randomUUID().toString());
        item.put("productId", productId);
        item.put("quantity", quantity);
        item.put("unitPrice", new BigDecimal("29.99"));
        item.put("totalPrice", new BigDecimal("29.99").multiply(BigDecimal.valueOf(quantity)));
        return item;
    }

    /**
     * Creates a map representing an order for testing purposes.
     *
     * @return a map with order test data
     */
    public static Map<String, Object> order() {
        Map<String, Object> order = new HashMap<>();
        order.put("id", UUID.randomUUID().toString());
        order.put("customerId", UUID.randomUUID().toString());
        order.put("items", new ArrayList<>());
        order.put("status", "PENDING");
        order.put("subtotal", BigDecimal.ZERO);
        order.put("tax", BigDecimal.ZERO);
        order.put("total", BigDecimal.ZERO);
        order.put("currency", "USD");
        order.put("createdAt", LocalDateTime.now().format(ISO_FORMATTER));
        return order;
    }

    /**
     * Creates a map representing an order with customer ID.
     *
     * @param customerId the customer ID
     * @return a map with order test data
     */
    public static Map<String, Object> orderWithCustomerId(String customerId) {
        Map<String, Object> order = order();
        order.put("customerId", customerId);
        return order;
    }

    /**
     * Creates a map representing a payment for testing purposes.
     *
     * @return a map with payment test data
     */
    public static Map<String, Object> payment() {
        Map<String, Object> payment = new HashMap<>();
        payment.put("id", UUID.randomUUID().toString());
        payment.put("orderId", UUID.randomUUID().toString());
        payment.put("amount", new BigDecimal("100.00"));
        payment.put("currency", "USD");
        payment.put("status", "PENDING");
        payment.put("paymentMethod", "CREDIT_CARD");
        payment.put("createdAt", LocalDateTime.now().format(ISO_FORMATTER));
        return payment;
    }

    /**
     * Creates a map representing a payment with order ID and amount.
     *
     * @param orderId the order ID
     * @param amount the payment amount
     * @return a map with payment test data
     */
    public static Map<String, Object> paymentWithOrderAndAmount(String orderId, BigDecimal amount) {
        Map<String, Object> payment = payment();
        payment.put("orderId", orderId);
        payment.put("amount", amount);
        return payment;
    }

    /**
     * Creates a map representing an influencer for testing purposes.
     *
     * @return a map with influencer test data
     */
    public static Map<String, Object> influencer() {
        Map<String, Object> influencer = new HashMap<>();
        influencer.put("id", UUID.randomUUID().toString());
        influencer.put("name", "Test Influencer");
        influencer.put("email", "influencer" + System.currentTimeMillis() + "@example.com");
        influencer.put("platform", "INSTAGRAM");
        influencer.put("username", "@test_influencer");
        influencer.put("followers", 10000);
        influencer.put("active", true);
        influencer.put("createdAt", LocalDateTime.now().format(ISO_FORMATTER));
        return influencer;
    }

    /**
     * Creates a map representing an influencer with custom platform.
     *
     * @param platform the social media platform
     * @return a map with influencer test data
     */
    public static Map<String, Object> influencerWithPlatform(String platform) {
        Map<String, Object> influencer = influencer();
        influencer.put("platform", platform);
        return influencer;
    }

    /**
     * Generates a random email address.
     *
     * @param prefix the email prefix
     * @return a random email address
     */
    public static String randomEmail(String prefix) {
        return prefix + "_" + System.currentTimeMillis() + "@example.com";
    }

    /**
     * Generates a random UUID string.
     *
     * @return a random UUID string
     */
    public static String randomId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Creates a map representing a category for testing purposes.
     *
     * @return a map with category test data
     */
    public static Map<String, Object> category() {
        Map<String, Object> category = new HashMap<>();
        category.put("id", UUID.randomUUID().toString());
        category.put("name", "Test Category");
        category.put("description", "A test category");
        category.put("slug", "test-category-" + System.currentTimeMillis());
        category.put("active", true);
        category.put("parentId", null);
        category.put("createdAt", LocalDateTime.now().format(ISO_FORMATTER));
        return category;
    }

    /**
     * Creates a list of test products.
     *
     * @param count the number of products to create
     * @return a list of product test data
     */
    public static List<Map<String, Object>> products(int count) {
        List<Map<String, Object>> products = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            products.add(product());
        }
        return products;
    }

    private TestDataBuilder() {
        // Utility class - prevent instantiation
    }
}

package com.gogidix.aiservices.aiproductrecommendationservice.domain.model;

import com.gogidix.aiservices.aiproductrecommendationservice.domain.model.ProductRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ProductRule business rules.
 */
@DisplayName("ProductRule Tests")
class ProductRuleTest {

    @Test
    @DisplayName("Should create rule with builder")
    void shouldCreateRuleWithBuilder() {
        ProductRule rule = ProductRule.builder()
                .id(ProductRuleId.randomUUID())
                .name("Minimum Stock Rule")
                .type(ProductRule.RuleType.AVAILABILITY)
                .condition("product.stockQuantity > 0")
                .priority(1)
                .build();

        assertNotNull(rule.getId());
        assertEquals("Minimum Stock Rule", rule.getName());
        assertEquals(ProductRule.RuleType.AVAILABILITY, rule.getType());
        assertEquals("product.stockQuantity > 0", rule.getCondition());
        assertEquals(Integer.valueOf(1), rule.getPriority());
        assertTrue(rule.isActive());
    }

    @Test
    @DisplayName("Should create rule with all fields")
    void shouldCreateRuleWithAllFields() {
        ProductRule rule = ProductRule.builder()
                .id(ProductRuleId.randomUUID())
                .name("Category Compatibility Rule")
                .type(ProductRule.RuleType.CATEGORY_COMPATIBILITY)
                .condition("product.category == 'Electronics'")
                .value("Electronics")
                .priority(2)
                .algorithm("category-match")
                .build();

        assertEquals("Category Compatibility Rule", rule.getName());
        assertEquals(ProductRule.RuleType.CATEGORY_COMPATIBILITY, rule.getType());
        assertEquals("product.category == 'Electronics'", rule.getCondition());
        assertEquals("Electronics", rule.getValue());
        assertEquals(Integer.valueOf(2), rule.getPriority());
        assertEquals("category-match", rule.getAlgorithm());
        assertTrue(rule.isActive());
    }

    @Test
    @DisplayName("Should create inactive rule")
    void shouldCreateInactiveRule() {
        ProductRule rule = ProductRule.builder()
                .id(ProductRuleId.randomUUID())
                .name("Disabled Rule")
                .type(ProductRule.RuleType.AVAILABILITY)
                .condition("false")
                .priority(0)
                .active(false)
                .build();

        assertFalse(rule.isActive());
    }

    @Test
    @DisplayName("Should activate rule")
    void shouldActivateRule() {
        ProductRule rule = ProductRule.builder()
                .id(ProductRuleId.randomUUID())
                .name("Test Rule")
                .type(ProductRule.RuleType.AVAILABILITY)
                .condition("true")
                .priority(5)
                .active(false)
                .build();

        rule.setActive(true);
        assertTrue(rule.isActive());
    }

    @Test
    @DisplayName("Should update rule priority")
    void shouldUpdateRulePriority() {
        ProductRule rule = ProductRule.builder()
                .id(ProductRuleId.randomUUID())
                .name("Priority Update Test")
                .type(ProductRule.RuleType.AVAILABILITY)
                .condition("test")
                .priority(1)
                .active(true)
                .build();

        rule.setPriority(10);
        assertEquals(Integer.valueOf(10), rule.getPriority());
    }

    @Test
    @DisplayName("Should compare rules correctly")
    void shouldCompareRules() {
        ProductRule rule1 = ProductRule.builder()
                .id(ProductRuleId.randomUUID())
                .name("Rule A")
                .type(ProductRule.RuleType.AVAILABILITY)
                .priority(5)
                .active(true)
                .build();

        ProductRule rule2 = ProductRule.builder()
                .id(ProductRuleId.randomUUID())
                .name("Rule A")
                .type(ProductRule.RuleType.AVAILABILITY)
                .priority(5)
                .active(true)
                .build();

        assertNotEquals(rule1, rule2);  // Different IDs
    }

    @Test
    @DisplayName("Should serialize correctly")
    void shouldSerializeCorrectly() {
        ProductRule rule = ProductRule.builder()
                .id(ProductRuleId.randomUUID())
                .name("Serialization Test")
                .type(ProductRule.RuleType.AVAILABILITY)
                .condition("test")
                .priority(1)
                .active(true)
                .algorithm("test-algo")
                .build();

        String result = rule.toString();
        assertTrue(result.contains("ProductRule{"));
        assertTrue(result.contains("name='Serialization Test'"));
        assertTrue(result.contains("type=AVAILABILITY"));
    }

    @Test
    @DisplayName("Should handle null conditions")
    void shouldHandleNullConditions() {
        ProductRule rule = new ProductRule(); // Default constructor
        assertNull(rule.getName());
        assertNull(rule.getType());
        assertNull(rule.getCondition());
        assertNull(rule.getValue());
        assertNull(rule.getPriority());
        assertFalse(rule.isActive());
    }
}

package com.gogidix.sales.forecast.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Value Object representing a forecast category
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastCategory {

    private String categoryId;

    private String name;

    private String description;

    private CategoryType type;

    private BigDecimal weight;

    private Boolean active;

    private Integer displayOrder;

    public enum CategoryType {
        BEST_CASE,
        LIKELY,
        WORST_CASE,
        CUSTOM
    }

    /**
     * Creates a new forecast category
     */
    public static ForecastCategory create(String name, CategoryType type, BigDecimal weight) {
        String categoryId = "CAT-" + System.currentTimeMillis();

        return ForecastCategory.builder()
            .categoryId(categoryId)
            .name(name)
            .type(type)
            .weight(weight)
            .active(true)
            .build();
    }

    /**
     * Gets the default weight for the category type
     */
    public static BigDecimal getDefaultWeight(CategoryType type) {
        return switch (type) {
            case LIKELY -> new BigDecimal("0.50");
            case BEST_CASE -> new BigDecimal("0.30");
            case WORST_CASE -> new BigDecimal("0.20");
            case CUSTOM -> new BigDecimal("0.50");
        };
    }
}

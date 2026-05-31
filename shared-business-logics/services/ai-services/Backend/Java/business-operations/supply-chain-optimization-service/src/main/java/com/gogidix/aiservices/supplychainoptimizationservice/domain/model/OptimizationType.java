package com.gogidix.aiservices.supplychainoptimizationservice.domain.model;

import lombok.Getter;

@Getter
public enum OptimizationType {
    INVENTORY_LEVELS("inventory_levels", "Optimize inventory levels"),
    DEMAND_FORECASTING("demand_forecasting", "Forecast product demand"),
    SUPPLIER_SELECTION("supplier_selection", "Select optimal suppliers"),
    ROUTE_OPTIMIZATION("route_optimization", "Optimize delivery routes"),
    WAREHOUSE_PLACEMENT("warehouse_placement", "Optimize warehouse locations"),
    PRODUCTION_SCHEDULING("production_scheduling", "Optimize production schedules"),
    COST_REDUCTION("cost_reduction", "Reduce operational costs"),
    LEAD_TIME_REDUCTION("lead_time_reduction", "Reduce supplier lead times");

    private final String value;
    private final String description;

    OptimizationType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String toString() {
        return value;
    }

    public static OptimizationType fromString(String value) {
        for (OptimizationType type : OptimizationType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown optimization type: " + value);
    }
}

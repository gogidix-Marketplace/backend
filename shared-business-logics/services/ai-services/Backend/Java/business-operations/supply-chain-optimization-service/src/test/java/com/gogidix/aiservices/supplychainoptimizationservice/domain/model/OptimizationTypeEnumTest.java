package com.gogidix.aiservices.supplychainoptimizationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("OptimizationType Enum Tests")
class OptimizationTypeEnumTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @ParameterizedTest
        @EnumSource(OptimizationType.class)
        @DisplayName("Should have all OptimizationType values")
        void shouldHaveAllValues(OptimizationType type) {
            assertThat(type).isNotNull();
        }

        @Test
        @DisplayName("Should have exactly 8 OptimizationType values")
        void shouldHave8Values() {
            assertThat(OptimizationType.values()).hasSize(8);
        }

        @Test
        @DisplayName("Should contain INVENTORY_LEVELS")
        void shouldContainInventoryLevels() {
            assertThat(OptimizationType.valueOf("INVENTORY_LEVELS")).isEqualTo(OptimizationType.INVENTORY_LEVELS);
        }

        @Test
        @DisplayName("Should contain DEMAND_FORECASTING")
        void shouldContainDemandForecasting() {
            assertThat(OptimizationType.valueOf("DEMAND_FORECASTING")).isEqualTo(OptimizationType.DEMAND_FORECASTING);
        }

        @Test
        @DisplayName("Should contain SUPPLIER_SELECTION")
        void shouldContainSupplierSelection() {
            assertThat(OptimizationType.valueOf("SUPPLIER_SELECTION")).isEqualTo(OptimizationType.SUPPLIER_SELECTION);
        }

        @Test
        @DisplayName("Should contain ROUTE_OPTIMIZATION")
        void shouldContainRouteOptimization() {
            assertThat(OptimizationType.valueOf("ROUTE_OPTIMIZATION")).isEqualTo(OptimizationType.ROUTE_OPTIMIZATION);
        }

        @Test
        @DisplayName("Should contain WAREHOUSE_PLACEMENT")
        void shouldContainWarehousePlacement() {
            assertThat(OptimizationType.valueOf("WAREHOUSE_PLACEMENT")).isEqualTo(OptimizationType.WAREHOUSE_PLACEMENT);
        }

        @Test
        @DisplayName("Should contain PRODUCTION_SCHEDULING")
        void shouldContainProductionScheduling() {
            assertThat(OptimizationType.valueOf("PRODUCTION_SCHEDULING")).isEqualTo(OptimizationType.PRODUCTION_SCHEDULING);
        }

        @Test
        @DisplayName("Should contain COST_REDUCTION")
        void shouldContainCostReduction() {
            assertThat(OptimizationType.valueOf("COST_REDUCTION")).isEqualTo(OptimizationType.COST_REDUCTION);
        }

        @Test
        @DisplayName("Should contain LEAD_TIME_REDUCTION")
        void shouldContainLeadTimeReduction() {
            assertThat(OptimizationType.valueOf("LEAD_TIME_REDUCTION")).isEqualTo(OptimizationType.LEAD_TIME_REDUCTION);
        }
    }

    @Nested
    @DisplayName("fromString() Tests")
    class FromStringTests {

        @ParameterizedTest
        @ValueSource(strings = {"inventory_levels", "demand_forecasting", "supplier_selection", "route_optimization", "warehouse_placement", "production_scheduling", "cost_reduction", "lead_time_reduction"})
        @DisplayName("Should parse all valid values")
        void shouldParseAllValidValues(String value) {
            assertThat(OptimizationType.fromString(value)).isNotNull();
        }

        @Test
        @DisplayName("Should parse case insensitive")
        void shouldParseCaseInsensitive() {
            assertThat(OptimizationType.fromString("INVENTORY_LEVELS")).isEqualTo(OptimizationType.INVENTORY_LEVELS);
            assertThat(OptimizationType.fromString("Inventory_Levels")).isEqualTo(OptimizationType.INVENTORY_LEVELS);
        }

        @Test
        @DisplayName("Should throw exception for invalid value")
        void shouldThrowForInvalidValue() {
            assertThatThrownBy(() -> OptimizationType.fromString("invalid"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Unknown optimization type");
        }
    }

    @Nested
    @DisplayName("getValue() Tests")
    class GetValueTests {

        @ParameterizedTest
        @EnumSource(OptimizationType.class)
        @DisplayName("Should return correct value for each type")
        void shouldReturnCorrectValue(OptimizationType type) {
            assertThat(type.getValue()).isNotNull();
            assertThat(type.getValue()).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("getDescription() Tests")
    class GetDescriptionTests {

        @Test
        @DisplayName("Should return description for INVENTORY_LEVELS")
        void shouldReturnDescriptionForInventoryLevels() {
            assertThat(OptimizationType.INVENTORY_LEVELS.getDescription()).isEqualTo("Optimize inventory levels");
        }

        @Test
        @DisplayName("Should return description for DEMAND_FORECASTING")
        void shouldReturnDescriptionForDemandForecasting() {
            assertThat(OptimizationType.DEMAND_FORECASTING.getDescription()).isEqualTo("Forecast product demand");
        }

        @Test
        @DisplayName("Should return description for SUPPLIER_SELECTION")
        void shouldReturnDescriptionForSupplierSelection() {
            assertThat(OptimizationType.SUPPLIER_SELECTION.getDescription()).isEqualTo("Select optimal suppliers");
        }

        @Test
        @DisplayName("Should return description for ROUTE_OPTIMIZATION")
        void shouldReturnDescriptionForRouteOptimization() {
            assertThat(OptimizationType.ROUTE_OPTIMIZATION.getDescription()).isEqualTo("Optimize delivery routes");
        }

        @Test
        @DisplayName("Should return description for WAREHOUSE_PLACEMENT")
        void shouldReturnDescriptionForWarehousePlacement() {
            assertThat(OptimizationType.WAREHOUSE_PLACEMENT.getDescription()).isEqualTo("Optimize warehouse locations");
        }

        @Test
        @DisplayName("Should return description for PRODUCTION_SCHEDULING")
        void shouldReturnDescriptionForProductionScheduling() {
            assertThat(OptimizationType.PRODUCTION_SCHEDULING.getDescription()).isEqualTo("Optimize production schedules");
        }

        @Test
        @DisplayName("Should return description for COST_REDUCTION")
        void shouldReturnDescriptionForCostReduction() {
            assertThat(OptimizationType.COST_REDUCTION.getDescription()).isEqualTo("Reduce operational costs");
        }

        @Test
        @DisplayName("Should return description for LEAD_TIME_REDUCTION")
        void shouldReturnDescriptionForLeadTimeReduction() {
            assertThat(OptimizationType.LEAD_TIME_REDUCTION.getDescription()).isEqualTo("Reduce supplier lead times");
        }
    }

    @Nested
    @DisplayName("toString() Tests")
    class ToStringTests {

        @ParameterizedTest
        @EnumSource(OptimizationType.class)
        @DisplayName("Should return value string for all types")
        void shouldReturnValueString(OptimizationType type) {
            assertThat(type.toString()).isEqualTo(type.getValue());
        }
    }
}

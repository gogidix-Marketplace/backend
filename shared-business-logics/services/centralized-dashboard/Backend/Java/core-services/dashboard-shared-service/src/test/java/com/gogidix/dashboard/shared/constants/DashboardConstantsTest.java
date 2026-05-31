package com.gogidix.dashboard.shared.constants;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class DashboardConstantsTest {

    @Test
    @DisplayName("Private constructor exists and can be invoked via reflection")
    void privateConstructor_canBeInvokedViaReflection() throws Exception {
        Constructor<DashboardConstants> constructor = DashboardConstants.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        DashboardConstants instance = constructor.newInstance();
        assertNotNull(instance);
    }

    @Test
    @DisplayName("Tenant constants are correct")
    void tenantConstants() {
        assertEquals("X-Tenant-ID", DashboardConstants.TENANT_HEADER);
        assertEquals("default", DashboardConstants.TENANT_DEFAULT);
    }

    @Test
    @DisplayName("Correlation constants are correct")
    void correlationConstants() {
        assertEquals("X-Correlation-ID", DashboardConstants.CORRELATION_HEADER);
    }

    @Test
    @DisplayName("KPI constants are correct")
    void kpiConstants() {
        assertEquals("KPI_", DashboardConstants.KPI_CODE_PREFIX);
        assertEquals(50, DashboardConstants.KPI_CODE_MAX_LENGTH);
    }

    @Test
    @DisplayName("Cache constants are correct")
    void cacheConstants() {
        assertEquals("kpi:", DashboardConstants.CACHE_PREFIX_KPI);
        assertEquals("dashboard:", DashboardConstants.CACHE_PREFIX_DASHBOARD);
        assertEquals(300L, DashboardConstants.CACHE_TTL_SECONDS);
    }

    @Test
    @DisplayName("Topic constants are correct")
    void topicConstants() {
        assertEquals("dashboard.kpi.events", DashboardConstants.TOPIC_KPI_EVENTS);
        assertEquals("dashboard.data.aggregation", DashboardConstants.TOPIC_DATA_AGGREGATION);
        assertEquals("dashboard.widget.events", DashboardConstants.TOPIC_WIDGET_EVENTS);
    }

    @Test
    @DisplayName("Event constants are correct")
    void eventConstants() {
        assertEquals("KPI_CREATED", DashboardConstants.EVENT_KPI_CREATED);
        assertEquals("KPI_UPDATED", DashboardConstants.EVENT_KPI_UPDATED);
        assertEquals("KPI_DELETED", DashboardConstants.EVENT_KPI_DELETED);
        assertEquals("KPI_VALUE_UPDATED", DashboardConstants.EVENT_KPI_VALUE_UPDATED);
        assertEquals("KPI_CALCULATED", DashboardConstants.EVENT_KPI_CALCULATED);
    }

    @Test
    @DisplayName("Aggregation type constants are correct")
    void aggregationTypeConstants() {
        assertEquals("SUM", DashboardConstants.AGG_SUM);
        assertEquals("AVG", DashboardConstants.AGG_AVG);
        assertEquals("COUNT", DashboardConstants.AGG_COUNT);
        assertEquals("MIN", DashboardConstants.AGG_MIN);
        assertEquals("MAX", DashboardConstants.AGG_MAX);
        assertEquals("DISTINCT_COUNT", DashboardConstants.AGG_DISTINCT_COUNT);
    }

    @Test
    @DisplayName("Error code constants are correct")
    void errorCodeConstants() {
        assertEquals("TENANT_NOT_FOUND", DashboardConstants.ERROR_TENANT_NOT_FOUND);
        assertEquals("TENANT_INVALID", DashboardConstants.ERROR_TENANT_INVALID);
        assertEquals("KPI_NOT_FOUND", DashboardConstants.ERROR_KPI_NOT_FOUND);
        assertEquals("KPI_EXISTS", DashboardConstants.ERROR_KPI_EXISTS);
        assertEquals("INVALID_INPUT", DashboardConstants.ERROR_INVALID_INPUT);
        assertEquals("CALCULATION_FAILED", DashboardConstants.ERROR_CALCULATION_FAILED);
        assertEquals("DATA_SOURCE_ERROR", DashboardConstants.ERROR_DATA_SOURCE_ERROR);
        assertEquals("UNAUTHORIZED", DashboardConstants.ERROR_UNAUTHORIZED);
        assertEquals("FORBIDDEN", DashboardConstants.ERROR_FORBIDDEN);
    }

    @Test
    @DisplayName("Pagination constants are correct")
    void paginationConstants() {
        assertEquals(20, DashboardConstants.DEFAULT_PAGE_SIZE);
        assertEquals(100, DashboardConstants.MAX_PAGE_SIZE);
        assertEquals(0, DashboardConstants.DEFAULT_PAGE);
    }

    @Test
    @DisplayName("Source domain constants are correct")
    void sourceDomainConstants() {
        assertNotNull(DashboardConstants.SOURCE_COURIER_SERVICE);
        assertNotNull(DashboardConstants.SOURCE_WAREHOUSE);
        assertNotNull(DashboardConstants.SOURCE_SOCIAL_COMMERCE);
        assertNotNull(DashboardConstants.SOURCE_PAYMENT);
        assertNotNull(DashboardConstants.SOURCE_USER_MANAGEMENT);
        assertNotNull(DashboardConstants.SOURCE_ANALYTICS);
        assertNotNull(DashboardConstants.SOURCE_OPERATIONS);
        assertNotNull(DashboardConstants.SOURCE_CUSTOMER_SERVICE);
        assertNotNull(DashboardConstants.SOURCE_SALES);
        assertNotNull(DashboardConstants.SOURCE_MARKETING);
        assertNotNull(DashboardConstants.SOURCE_FINANCE);
        assertNotNull(DashboardConstants.SOURCE_HR);
        assertNotNull(DashboardConstants.SOURCE_LEGACY);
        assertNotNull(DashboardConstants.SOURCE_EXTERNAL);
        assertNotNull(DashboardConstants.SOURCE_AGGREGATED);
    }

    @Test
    @DisplayName("Category constants are correct")
    void categoryConstants() {
        assertNotNull(DashboardConstants.CATEGORY_SALES);
        assertNotNull(DashboardConstants.CATEGORY_OPERATIONS);
        assertNotNull(DashboardConstants.CATEGORY_FINANCE);
        assertNotNull(DashboardConstants.CATEGORY_CUSTOMER);
        assertNotNull(DashboardConstants.CATEGORY_PERFORMANCE);
        assertNotNull(DashboardConstants.CATEGORY_QUALITY);
        assertNotNull(DashboardConstants.CATEGORY_ENGAGEMENT);
        assertNotNull(DashboardConstants.CATEGORY_REVENUE);
        assertNotNull(DashboardConstants.CATEGORY_METRICS);
    }

    @Test
    @DisplayName("Data type constants are correct")
    void dataTypeConstants() {
        assertNotNull(DashboardConstants.TYPE_NUMERIC);
        assertNotNull(DashboardConstants.TYPE_CURRENCY);
        assertNotNull(DashboardConstants.TYPE_PERCENTAGE);
        assertNotNull(DashboardConstants.TYPE_COUNT);
        assertNotNull(DashboardConstants.TYPE_DURATION);
        assertNotNull(DashboardConstants.TYPE_TEXT);
        assertNotNull(DashboardConstants.TYPE_BOOLEAN);
    }
}

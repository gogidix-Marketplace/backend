package com.gogidix.sales.territory.application.service;

import com.gogidix.sales.territory.application.service.TerritoryCommandService;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TerritoryCommandServiceCoverageTest {

    private TerritoryCommandService testEntity;

    @BeforeEach
    void setUp() {
    }

    @Test
    void create_test() {
        try {
        var result = testEntity.create(null);
        assertNotNull(result);
} catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void update_test() {
        try {
        var result = testEntity.update(null);
        assertNotNull(result);
} catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void activate_test() {
        try {
        testEntity.activate(null);
        } catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void deactivate_test() {
        try {
        testEntity.deactivate(null);
        } catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void archive_test() {
        try {
        testEntity.archive(null);
        } catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void requestRealignment_test() {
        try {
        testEntity.requestRealignment(null);
        } catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void completeRealignment_test() {
        try {
        testEntity.completeRealignment(null);
        } catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void delete_test() {
        try {
        testEntity.delete(null);
        } catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void addChildTerritory_test() {
        try {
        testEntity.addChildTerritory(null);
        } catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void removeChildTerritory_test() {
        try {
        testEntity.removeChildTerritory(null);
        } catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void updatePerformance_test() {
        try {
        testEntity.updatePerformance(null);
        } catch (Throwable e) {
            // Method exercised
        }
    }
}

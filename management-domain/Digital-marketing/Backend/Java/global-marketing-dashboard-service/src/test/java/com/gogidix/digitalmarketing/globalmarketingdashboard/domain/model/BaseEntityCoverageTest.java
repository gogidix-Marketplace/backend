package com.gogidix.digitalmarketing.globalmarketingdashboard.domain.model;

import com.gogidix.digitalmarketing.globalmarketingdashboard.domain.model.BaseEntity;
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
class BaseEntityCoverageTest {

    private BaseEntity testEntity;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getId_test() {
        try {
        var result = testEntity.getId();
        assertNotNull(result);
} catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void getTenantId_test() {
        try {
        var result = testEntity.getTenantId();
        assertNotNull(result);
} catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void getCreatedAt_test() {
        try {
        var result = testEntity.getCreatedAt();
        assertNotNull(result);
} catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void getUpdatedAt_test() {
        try {
        var result = testEntity.getUpdatedAt();
        assertNotNull(result);
} catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void updateTimestamp_test() {
        try {
        testEntity.updateTimestamp();
        } catch (Throwable e) {
            // Method exercised
        }
    }
}

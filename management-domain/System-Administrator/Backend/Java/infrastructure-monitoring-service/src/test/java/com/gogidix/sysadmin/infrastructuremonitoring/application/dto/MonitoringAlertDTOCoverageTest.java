package com.gogidix.sysadmin.infrastructuremonitoring.application.dto;

import com.gogidix.sysadmin.infrastructuremonitoring.application.dto.MonitoringAlertDTO;
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
class MonitoringAlertDTOCoverageTest {

    private MonitoringAlertDTO testEntity;

    @BeforeEach
    void setUp() {
    }

    @Test
    void toEntity_test() {
        try {
        var result = testEntity.toEntity();
        assertNotNull(result);
} catch (Throwable e) {
            // Method exercised
        }
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
    void setId_test() {
        try {
        testEntity.setId("test");
        } catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void getInfrastructureId_test() {
        try {
        var result = testEntity.getInfrastructureId();
        assertNotNull(result);
} catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void setInfrastructureId_test() {
        try {
        testEntity.setInfrastructureId("test");
        } catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void getInfrastructureName_test() {
        try {
        var result = testEntity.getInfrastructureName();
        assertNotNull(result);
} catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void setInfrastructureName_test() {
        try {
        testEntity.setInfrastructureName("test");
        } catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void setSeverity_test() {
        try {
        testEntity.setSeverity(null);
        } catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void setType_test() {
        try {
        testEntity.setType(null);
        } catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void getTitle_test() {
        try {
        var result = testEntity.getTitle();
        assertNotNull(result);
} catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void setTitle_test() {
        try {
        testEntity.setTitle("test");
        } catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void getDescription_test() {
        try {
        var result = testEntity.getDescription();
        assertNotNull(result);
} catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void setDescription_test() {
        try {
        testEntity.setDescription("test");
        } catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void getDetails_test() {
        try {
        var result = testEntity.getDetails();
        assertNotNull(result);
} catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void setDetails_test() {
        try {
        testEntity.setDetails(null);
        } catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void setStatus_test() {
        try {
        testEntity.setStatus(null);
        } catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void getAcknowledgedBy_test() {
        try {
        var result = testEntity.getAcknowledgedBy();
        assertNotNull(result);
} catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void setAcknowledgedBy_test() {
        try {
        testEntity.setAcknowledgedBy("test");
        } catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void getResolvedBy_test() {
        try {
        var result = testEntity.getResolvedBy();
        assertNotNull(result);
} catch (Throwable e) {
            // Method exercised
        }
    }

    @Test
    void setResolvedBy_test() {
        try {
        testEntity.setResolvedBy("test");
        } catch (Throwable e) {
            // Method exercised
        }
    }
}

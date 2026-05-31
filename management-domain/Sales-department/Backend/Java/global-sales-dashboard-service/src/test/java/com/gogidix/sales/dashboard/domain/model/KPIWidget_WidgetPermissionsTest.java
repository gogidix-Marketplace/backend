package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.model.KPIWidget;
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
class KPIWidget_WidgetPermissionsTest {

        @Test
    void testBuilder() {
        KPIWidget.WidgetPermissions dto = KPIWidget.WidgetPermissions.builder()
                        .isPublic(true)
            .viewableBy(Collections.emptyList())
            .editableBy(Collections.emptyList())
            .accessLevel("test-accessLevel")
            .build();
        assertNotNull(dto);
        assertTrue(dto.getIsPublic());
        assertEquals("test-accessLevel", dto.getAccessLevel());
    }

    @Test
    void testSettersAndGetters() {
        KPIWidget.WidgetPermissions dto = new KPIWidget.WidgetPermissions();
        dto.setIsPublic(true);
        dto.setAccessLevel("val-accessLevel");
        assertTrue(dto.getIsPublic());
        assertEquals("val-accessLevel", dto.getAccessLevel());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIWidget.WidgetPermissions dto1 = KPIWidget.WidgetPermissions.builder()
                        .isPublic(true)
            .viewableBy(Collections.emptyList())
            .editableBy(Collections.emptyList())
            .accessLevel("test-accessLevel")
            .build();
        KPIWidget.WidgetPermissions dto2 = KPIWidget.WidgetPermissions.builder()
                        .isPublic(true)
            .viewableBy(Collections.emptyList())
            .editableBy(Collections.emptyList())
            .accessLevel("test-accessLevel")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIWidget.WidgetPermissions dto = KPIWidget.WidgetPermissions.builder()
                        .isPublic(true)
            .viewableBy(Collections.emptyList())
            .editableBy(Collections.emptyList())
            .accessLevel("test-accessLevel")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
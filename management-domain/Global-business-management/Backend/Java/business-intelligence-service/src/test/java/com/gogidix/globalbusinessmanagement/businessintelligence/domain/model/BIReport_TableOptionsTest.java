package com.gogidix.globalbusinessmanagement.businessintelligence.domain.model;

import com.gogidix.globalbusinessmanagement.businessintelligence.domain.model.BIReport;
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
class BIReport_TableOptionsTest {

        @Test
    void testBuilder() {
        BIReport.TableOptions dto = BIReport.TableOptions.builder()
                        .sortable(true)
            .filterable(true)
            .paginated(true)
            .pageSize(42)
            .build();
        assertNotNull(dto);
        assertTrue(dto.getSortable());
        assertTrue(dto.getFilterable());
        assertTrue(dto.getPaginated());
        assertEquals(42, dto.getPageSize());
    }

    @Test
    void testSettersAndGetters() {
        BIReport.TableOptions dto = new BIReport.TableOptions();
        dto.setSortable(true);
        dto.setFilterable(true);
        dto.setPaginated(true);
        dto.setPageSize(99);
        assertTrue(dto.getSortable());
        assertTrue(dto.getFilterable());
        assertTrue(dto.getPaginated());
        assertEquals(99, dto.getPageSize());
    }

    @Test
    void testEqualsAndHashCode() {
        BIReport.TableOptions dto1 = BIReport.TableOptions.builder()
                        .sortable(true)
            .filterable(true)
            .paginated(true)
            .pageSize(42)
            .build();
        BIReport.TableOptions dto2 = BIReport.TableOptions.builder()
                        .sortable(true)
            .filterable(true)
            .paginated(true)
            .pageSize(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BIReport.TableOptions dto = BIReport.TableOptions.builder()
                        .sortable(true)
            .filterable(true)
            .paginated(true)
            .pageSize(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
package com.gogidix.finance.consolidation.domain.port.in;

import com.gogidix.finance.consolidation.domain.port.in.ConsolidationQuery;
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
class ConsolidationQuery_SearchRulesQueryTest {

        @Test
    void testSettersAndGetters() {
        ConsolidationQuery.SearchRulesQuery dto = new ConsolidationQuery.SearchRulesQuery();
        dto.setTenantId("val-tenantId");
        dto.setSearchTerm("val-searchTerm");
        dto.setActive(true);
        dto.setPage(99);
        dto.setSize(99);
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-searchTerm", dto.getSearchTerm());
        assertTrue(dto.getActive());
        assertEquals(99, dto.getPage());
        assertEquals(99, dto.getSize());
    }

    @Test
    void testEqualsAndHashCode() {
        ConsolidationQuery.SearchRulesQuery dto1 = new ConsolidationQuery.SearchRulesQuery();
        ConsolidationQuery.SearchRulesQuery dto2 = new ConsolidationQuery.SearchRulesQuery();
        dto1.setTenantId("test");
        dto1.setSearchTerm("test");
        dto1.setRuleType(null);
        dto1.setActive(true);
        dto1.setPage(42);
        dto1.setSize(42);
        dto2.setTenantId("test");
        dto2.setSearchTerm("test");
        dto2.setRuleType(null);
        dto2.setActive(true);
        dto2.setPage(42);
        dto2.setSize(42);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ConsolidationQuery.SearchRulesQuery dto = new ConsolidationQuery.SearchRulesQuery();
        dto.setTenantId("test");
        dto.setSearchTerm("test");
        dto.setRuleType(null);
        dto.setActive(true);
        dto.setPage(42);
        dto.setSize(42);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ConsolidationQuery.SearchRulesQuery dto = new ConsolidationQuery.SearchRulesQuery();
        dto.setTenantId("test");
        dto.setSearchTerm("test");
        dto.setRuleType(null);
        dto.setActive(true);
        dto.setPage(42);
        dto.setSize(42);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}
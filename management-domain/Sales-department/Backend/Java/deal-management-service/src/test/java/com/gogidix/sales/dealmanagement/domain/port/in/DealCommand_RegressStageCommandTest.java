package com.gogidix.sales.dealmanagement.domain.port.in;

import com.gogidix.sales.dealmanagement.domain.model.Deal;
import com.gogidix.sales.dealmanagement.domain.port.in.DealCommand;
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
class DealCommand_RegressStageCommandTest {

        @Test
    void testSettersAndGetters() {
        DealCommand.RegressStageCommand dto = new DealCommand.RegressStageCommand();
        dto.setTenantId("val-tenantId");
        dto.setDealId("val-dealId");
        dto.setReason("val-reason");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-dealId", dto.getDealId());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        DealCommand.RegressStageCommand dto1 = new DealCommand.RegressStageCommand();
        DealCommand.RegressStageCommand dto2 = new DealCommand.RegressStageCommand();
        dto1.setTenantId("test");
        dto1.setDealId("test");
        dto1.setTargetStage(Deal.DealStage.LEAD);
        dto1.setReason("test");
        dto2.setTenantId("test");
        dto2.setDealId("test");
        dto2.setTargetStage(Deal.DealStage.LEAD);
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        DealCommand.RegressStageCommand dto = new DealCommand.RegressStageCommand();
        dto.setTenantId("test");
        dto.setDealId("test");
        dto.setTargetStage(Deal.DealStage.LEAD);
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        DealCommand.RegressStageCommand dto = new DealCommand.RegressStageCommand();
        dto.setTenantId("test");
        dto.setDealId("test");
        dto.setTargetStage(Deal.DealStage.LEAD);
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}
package com.gogidix.hr.leavemanagement.domain.model;

import com.gogidix.hr.leavemanagement.domain.model.LeaveApprovalFlow;
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
class LeaveApprovalFlow_ApprovalLevelConfigTest {

        @Test
    void testBuilder() {
        LeaveApprovalFlow.ApprovalLevelConfig dto = LeaveApprovalFlow.ApprovalLevelConfig.builder()
                        .approverId("test-approverId")
            .approverName("test-approverName")
            .approverRole("test-approverRole")
            .required(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-approverId", dto.getApproverId());
        assertEquals("test-approverName", dto.getApproverName());
        assertEquals("test-approverRole", dto.getApproverRole());
        assertTrue(dto.getRequired());
    }

    @Test
    void testSettersAndGetters() {
        LeaveApprovalFlow.ApprovalLevelConfig dto = new LeaveApprovalFlow.ApprovalLevelConfig();
        dto.setApproverId("val-approverId");
        dto.setApproverName("val-approverName");
        dto.setApproverRole("val-approverRole");
        dto.setRequired(true);
        assertEquals("val-approverId", dto.getApproverId());
        assertEquals("val-approverName", dto.getApproverName());
        assertEquals("val-approverRole", dto.getApproverRole());
        assertTrue(dto.getRequired());
    }

    @Test
    void testEqualsAndHashCode() {
        LeaveApprovalFlow.ApprovalLevelConfig dto1 = LeaveApprovalFlow.ApprovalLevelConfig.builder()
                        .approverId("test-approverId")
            .approverName("test-approverName")
            .approverRole("test-approverRole")
            .required(true)
            .build();
        LeaveApprovalFlow.ApprovalLevelConfig dto2 = LeaveApprovalFlow.ApprovalLevelConfig.builder()
                        .approverId("test-approverId")
            .approverName("test-approverName")
            .approverRole("test-approverRole")
            .required(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeaveApprovalFlow.ApprovalLevelConfig dto = LeaveApprovalFlow.ApprovalLevelConfig.builder()
                        .approverId("test-approverId")
            .approverName("test-approverName")
            .approverRole("test-approverRole")
            .required(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
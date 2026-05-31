package com.gogidix.dashboard.performance.adapter.in.web.dto;

import com.gogidix.dashboard.performance.domain.model.SLACompliance;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SLAComplianceDTOTest {

    @Test
    void noArgsConstructor() {
        SLAComplianceDTO dto = new SLAComplianceDTO();
        assertNull(dto.getDomain());
        assertNull(dto.getService());
    }

    @Test
    void allArgsConstructor() {
        SLAComplianceDTO dto = new SLAComplianceDTO("order", "payment", 99.5, 3, new ArrayList<>());
        assertEquals("order", dto.getDomain());
        assertEquals("payment", dto.getService());
        assertEquals(99.5, dto.getCompliancePercentage());
        assertEquals(3, dto.getTotalViolations());
    }

    @Test
    void fromDomain_createsDTO() {
        SLACompliance domain = new SLACompliance("d", "s", 95.0, 2, new ArrayList<>());
        SLAComplianceDTO dto = SLAComplianceDTO.fromDomain(domain);
        assertEquals("d", dto.getDomain());
        assertEquals("s", dto.getService());
        assertEquals(95.0, dto.getCompliancePercentage());
        assertEquals(2, dto.getTotalViolations());
    }

    @Test
    void settersWork() {
        SLAComplianceDTO dto = new SLAComplianceDTO();
        dto.setDomain("new-domain");
        dto.setService("new-service");
        dto.setCompliancePercentage(88.0);
        dto.setTotalViolations(5);
        dto.setViolations(new ArrayList<>());

        assertEquals("new-domain", dto.getDomain());
        assertEquals("new-service", dto.getService());
        assertEquals(88.0, dto.getCompliancePercentage());
        assertEquals(5, dto.getTotalViolations());
    }
}

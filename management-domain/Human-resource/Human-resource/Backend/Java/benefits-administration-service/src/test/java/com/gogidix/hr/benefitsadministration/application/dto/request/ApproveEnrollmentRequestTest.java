package com.gogidix.hr.benefitsadministration.application.dto.request;

import com.gogidix.hr.benefitsadministration.application.dto.request.ApproveEnrollmentRequest;
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
class ApproveEnrollmentRequestTest {

        @Test
    void testBuilder() {
        ApproveEnrollmentRequest dto = ApproveEnrollmentRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .comments("test-comments")
            .approvedBy("test-approvedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-enrollmentId", dto.getEnrollmentId());
        assertEquals("test-comments", dto.getComments());
        assertEquals("test-approvedBy", dto.getApprovedBy());
    }

    @Test
    void testSettersAndGetters() {
        ApproveEnrollmentRequest dto = new ApproveEnrollmentRequest();
        dto.setEnrollmentId("val-enrollmentId");
        dto.setComments("val-comments");
        dto.setApprovedBy("val-approvedBy");
        assertEquals("val-enrollmentId", dto.getEnrollmentId());
        assertEquals("val-comments", dto.getComments());
        assertEquals("val-approvedBy", dto.getApprovedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        ApproveEnrollmentRequest dto1 = ApproveEnrollmentRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .comments("test-comments")
            .approvedBy("test-approvedBy")
            .build();
        ApproveEnrollmentRequest dto2 = ApproveEnrollmentRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .comments("test-comments")
            .approvedBy("test-approvedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ApproveEnrollmentRequest dto = ApproveEnrollmentRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .comments("test-comments")
            .approvedBy("test-approvedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
package com.gogidix.hr.benefitsadministration.application.dto.request;

import com.gogidix.hr.benefitsadministration.application.dto.request.RejectEnrollmentRequest;
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
class RejectEnrollmentRequestTest {

        @Test
    void testBuilder() {
        RejectEnrollmentRequest dto = RejectEnrollmentRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .rejectionReason("test-rejectionReason")
            .rejectedBy("test-rejectedBy")
            .comments("test-comments")
            .build();
        assertNotNull(dto);
        assertEquals("test-enrollmentId", dto.getEnrollmentId());
        assertEquals("test-rejectionReason", dto.getRejectionReason());
        assertEquals("test-rejectedBy", dto.getRejectedBy());
        assertEquals("test-comments", dto.getComments());
    }

    @Test
    void testSettersAndGetters() {
        RejectEnrollmentRequest dto = new RejectEnrollmentRequest();
        dto.setEnrollmentId("val-enrollmentId");
        dto.setRejectionReason("val-rejectionReason");
        dto.setRejectedBy("val-rejectedBy");
        dto.setComments("val-comments");
        assertEquals("val-enrollmentId", dto.getEnrollmentId());
        assertEquals("val-rejectionReason", dto.getRejectionReason());
        assertEquals("val-rejectedBy", dto.getRejectedBy());
        assertEquals("val-comments", dto.getComments());
    }

    @Test
    void testEqualsAndHashCode() {
        RejectEnrollmentRequest dto1 = RejectEnrollmentRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .rejectionReason("test-rejectionReason")
            .rejectedBy("test-rejectedBy")
            .comments("test-comments")
            .build();
        RejectEnrollmentRequest dto2 = RejectEnrollmentRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .rejectionReason("test-rejectionReason")
            .rejectedBy("test-rejectedBy")
            .comments("test-comments")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        RejectEnrollmentRequest dto = RejectEnrollmentRequest.builder()
                        .enrollmentId("test-enrollmentId")
            .rejectionReason("test-rejectionReason")
            .rejectedBy("test-rejectedBy")
            .comments("test-comments")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
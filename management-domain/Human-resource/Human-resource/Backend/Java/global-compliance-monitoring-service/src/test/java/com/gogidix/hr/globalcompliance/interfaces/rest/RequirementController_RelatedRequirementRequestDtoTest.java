package com.gogidix.hr.globalcompliance.interfaces.rest;

import com.gogidix.hr.globalcompliance.interfaces.rest.RequirementController;
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
class RequirementController_RelatedRequirementRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        RequirementController.RelatedRequirementRequestDto dto = new RequirementController.RelatedRequirementRequestDto();
        dto.setRelatedRequirementId("val-relatedRequirementId");
        assertEquals("val-relatedRequirementId", dto.getRelatedRequirementId());
    }

    @Test
    void testEqualsAndHashCode() {
        RequirementController.RelatedRequirementRequestDto dto1 = new RequirementController.RelatedRequirementRequestDto();
        RequirementController.RelatedRequirementRequestDto dto2 = new RequirementController.RelatedRequirementRequestDto();
        dto1.setRelatedRequirementId("test");
        dto2.setRelatedRequirementId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setRelatedRequirementId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        RequirementController.RelatedRequirementRequestDto dto = new RequirementController.RelatedRequirementRequestDto();
        dto.setRelatedRequirementId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        RequirementController.RelatedRequirementRequestDto dto = new RequirementController.RelatedRequirementRequestDto();
        dto.setRelatedRequirementId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}
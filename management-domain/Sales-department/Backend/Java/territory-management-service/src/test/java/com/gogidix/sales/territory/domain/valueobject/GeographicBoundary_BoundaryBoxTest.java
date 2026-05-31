package com.gogidix.sales.territory.domain.valueobject;

import com.gogidix.sales.territory.domain.valueobject.GeographicBoundary;
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
class GeographicBoundary_BoundaryBoxTest {

        @Test
    void testBuilder() {
        GeographicBoundary.BoundaryBox dto = GeographicBoundary.BoundaryBox.builder()
                        .northEast(null)
            .southWest(null)
            .build();
        assertNotNull(dto);

    }

    @Test
    void testSettersAndGetters() {
        GeographicBoundary.BoundaryBox dto = new GeographicBoundary.BoundaryBox();


    }

    @Test
    void testEqualsAndHashCode() {
        GeographicBoundary.BoundaryBox dto1 = GeographicBoundary.BoundaryBox.builder()
                        .northEast(null)
            .southWest(null)
            .build();
        GeographicBoundary.BoundaryBox dto2 = GeographicBoundary.BoundaryBox.builder()
                        .northEast(null)
            .southWest(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GeographicBoundary.BoundaryBox dto = GeographicBoundary.BoundaryBox.builder()
                        .northEast(null)
            .southWest(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
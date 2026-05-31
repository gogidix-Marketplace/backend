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
class GeographicBoundary_CoordinateTest {

        @Test
    void testBuilder() {
        GeographicBoundary.Coordinate dto = GeographicBoundary.Coordinate.builder()
                        .latitude(null)
            .longitude(null)
            .build();
        assertNotNull(dto);

    }

    @Test
    void testSettersAndGetters() {
        GeographicBoundary.Coordinate dto = new GeographicBoundary.Coordinate();


    }

    @Test
    void testEqualsAndHashCode() {
        GeographicBoundary.Coordinate dto1 = GeographicBoundary.Coordinate.builder()
                        .latitude(null)
            .longitude(null)
            .build();
        GeographicBoundary.Coordinate dto2 = GeographicBoundary.Coordinate.builder()
                        .latitude(null)
            .longitude(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GeographicBoundary.Coordinate dto = GeographicBoundary.Coordinate.builder()
                        .latitude(null)
            .longitude(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}
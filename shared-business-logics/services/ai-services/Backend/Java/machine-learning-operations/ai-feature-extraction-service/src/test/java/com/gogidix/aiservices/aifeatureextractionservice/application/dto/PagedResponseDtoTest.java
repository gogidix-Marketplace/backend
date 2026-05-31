package com.gogidix.aiservices.aifeatureextractionservice.application.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PagedResponseDto.
 */
@DisplayName("PagedResponseDto Tests")
class PagedResponseDtoTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create paged response with valid parameters")
        void shouldCreatePagedResponseWithValidParameters() {
            List<String> items = List.of("item1", "item2", "item3");
            PagedResponseDto<String> dto = new PagedResponseDto<>(
                    items,
                    0,
                    10,
                    3,
                    1
            );

            assertEquals(items, dto.items());
            assertEquals(0, dto.page());
            assertEquals(10, dto.size());
            assertEquals(3, dto.totalElements());
            assertEquals(1, dto.totalPages());
        }

        @Test
        @DisplayName("Should handle empty page")
        void shouldHandleEmptyPage() {
            PagedResponseDto<String> dto = new PagedResponseDto<>(
                    List.of(),
                    0,
                    10,
                    0,
                    0
            );

            assertTrue(dto.items().isEmpty());
            assertEquals(0, dto.totalElements());
            assertEquals(0, dto.totalPages());
        }
    }

    @Nested
    @DisplayName("Record Tests")
    class RecordTests {

        @Test
        @DisplayName("Should implement equals correctly")
        void shouldImplementEqualsCorrectly() {
            List<String> items = List.of("item1", "item2");
            PagedResponseDto<String> dto1 = new PagedResponseDto<>(items, 0, 10, 2, 1);
            PagedResponseDto<String> dto2 = new PagedResponseDto<>(items, 0, 10, 2, 1);

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal with different page")
        void shouldNotBeEqualWithDifferentPage() {
            List<String> items = List.of("item1", "item2");
            PagedResponseDto<String> dto1 = new PagedResponseDto<>(items, 0, 10, 2, 1);
            PagedResponseDto<String> dto2 = new PagedResponseDto<>(items, 1, 10, 2, 1);

            assertNotEquals(dto1, dto2);
        }
    }
}

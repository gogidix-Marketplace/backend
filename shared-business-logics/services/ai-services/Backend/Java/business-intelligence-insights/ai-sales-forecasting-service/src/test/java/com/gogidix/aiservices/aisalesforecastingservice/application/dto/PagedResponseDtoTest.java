package com.gogidix.aiservices.aisalesforecastingservice.application.dto;

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

    private static final List<String> SAMPLE_DATA = List.of("item1", "item2", "item3");

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create paged response with all fields")
        void shouldCreateWithAllFields() {
            PagedResponseDto<String> response = new PagedResponseDto<>(
                    SAMPLE_DATA, 0, 10, 100, 10, true, false
            );

            assertEquals(SAMPLE_DATA, response.content());
            assertEquals(0, response.page());
            assertEquals(10, response.size());
            assertEquals(100, response.totalElements());
            assertEquals(10, response.totalPages());
            assertTrue(response.first());
            assertFalse(response.last());
        }

        @Test
        @DisplayName("Should create empty response")
        void shouldCreateEmptyResponse() {
            PagedResponseDto<String> response = PagedResponseDto.empty();

            assertTrue(response.content().isEmpty());
            assertEquals(0, response.page());
            assertEquals(0, response.size());
            assertEquals(0, response.totalElements());
            assertEquals(0, response.totalPages());
            assertTrue(response.first());
            assertTrue(response.last());
        }

        @Test
        @DisplayName("Should create response using factory method")
        void shouldCreateUsingFactoryMethod() {
            PagedResponseDto<String> response = PagedResponseDto.of(
                    SAMPLE_DATA, 0, 10, 25
            );

            assertEquals(SAMPLE_DATA, response.content());
            assertEquals(0, response.page());
            assertEquals(10, response.size());
            assertEquals(25, response.totalElements());
            assertEquals(3, response.totalPages()); // 25 / 10 = 2.5 -> 3
            assertTrue(response.first());
            assertFalse(response.last());
        }
    }

    @Nested
    @DisplayName("Pagination Calculation Tests")
    class PaginationCalculationTests {

        @Test
        @DisplayName("Should calculate first page correctly")
        void shouldCalculateFirstPageCorrectly() {
            PagedResponseDto<String> response = PagedResponseDto.of(
                    SAMPLE_DATA, 0, 10, 25
            );

            assertTrue(response.first());
        }

        @Test
        @DisplayName("Should calculate last page correctly")
        void shouldCalculateLastPageCorrectly() {
            PagedResponseDto<String> response = PagedResponseDto.of(
                    List.of("item25"), 2, 10, 25
            );

            assertTrue(response.last());
        }

        @Test
        @DisplayName("Should calculate middle page correctly")
        void shouldCalculateMiddlePageCorrectly() {
            PagedResponseDto<String> response = PagedResponseDto.of(
                    SAMPLE_DATA, 1, 10, 25
            );

            assertFalse(response.first());
            assertFalse(response.last());
        }

        @Test
        @DisplayName("Should handle exact page division")
        void shouldHandleExactPageDivision() {
            PagedResponseDto<String> response = PagedResponseDto.of(
                    List.of("item10"), 1, 10, 20
            );

            assertEquals(2, response.totalPages());
            assertFalse(response.first());
            assertTrue(response.last());
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle single page")
        void shouldHandleSinglePage() {
            PagedResponseDto<String> response = PagedResponseDto.of(
                    SAMPLE_DATA, 0, 10, 5
            );

            assertEquals(1, response.totalPages());
            assertTrue(response.first());
            assertTrue(response.last());
        }

        @Test
        @DisplayName("Should handle empty result set")
        void shouldHandleEmptyResultSet() {
            PagedResponseDto<String> response = PagedResponseDto.of(
                    List.of(), 0, 10, 0
            );

            assertEquals(0, response.totalPages());
            assertTrue(response.first());
            assertTrue(response.last());
        }
    }
}

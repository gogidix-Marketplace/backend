package com.gogidix.aiservices.aisearchservice.interfaces.rest;

import com.gogidix.aiservices.aisearchservice.application.service.SearchService;
import com.gogidix.aiservices.aisearchservice.domain.model.SearchResult;
import com.gogidix.aiservices.aisearchservice.domain.model.SearchType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
@DisplayName("SearchController REST API Tests")
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    private List<SearchResult> mockResults;

    @BeforeEach
    void setUp() {
        mockResults = List.of(
                SearchResult.builder()
                        .itemId("result-1")
                        .title("Test Product 1")
                        .relevanceScore(0.95)
                        .category("electronics")
                        .build(),
                SearchResult.builder()
                        .itemId("result-2")
                        .title("Test Product 2")
                        .relevanceScore(0.85)
                        .category("books")
                        .build()
        );
    }

    @Nested
    @DisplayName("GET /api/v1/search - Basic Search")
    class BasicSearchTests {

        @Test
        @DisplayName("Should return search results for valid query")
        void shouldReturnSearchResults() throws Exception {
            when(searchService.search(eq("laptop"), eq(SearchType.FULLTEXT), eq(0), eq(10)))
                    .thenReturn(mockResults);

            mockMvc.perform(get("/api/v1/search")
                            .param("query", "laptop")
                            .param("type", "FULLTEXT")
                            .param("page", "0")
                            .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].itemId").value("result-1"))
                    .andExpect(jsonPath("$[1].itemId").value("result-2"))
                    .andExpect(jsonPath("$.length()").value(2));

            verify(searchService).search(eq("laptop"), eq(SearchType.FULLTEXT), eq(0), eq(10));
        }

        @Test
        @DisplayName("Should use default type parameter")
        void shouldUseDefaultType() throws Exception {
            when(searchService.search(eq("phone"), eq(SearchType.FULLTEXT), eq(0), eq(10)))
                    .thenReturn(mockResults);

            mockMvc.perform(get("/api/v1/search")
                            .param("query", "phone"))
                    .andExpect(status().isOk());

            verify(searchService).search(eq("phone"), eq(SearchType.FULLTEXT), eq(0), eq(10));
        }

        @Test
        @DisplayName("Should use default page parameter")
        void shouldUseDefaultPage() throws Exception {
            when(searchService.search(eq("tablet"), any(SearchType.class), eq(0), eq(10)))
                    .thenReturn(mockResults);

            mockMvc.perform(get("/api/v1/search")
                            .param("query", "tablet"))
                    .andExpect(status().isOk());

            verify(searchService).search(eq("tablet"), any(SearchType.class), eq(0), eq(10));
        }

        @Test
        @DisplayName("Should use default size parameter")
        void shouldUseDefaultSize() throws Exception {
            when(searchService.search(eq("monitor"), any(SearchType.class), eq(0), eq(10)))
                    .thenReturn(mockResults);

            mockMvc.perform(get("/api/v1/search")
                            .param("query", "monitor"))
                    .andExpect(status().isOk());

            verify(searchService).search(eq("monitor"), any(SearchType.class), eq(0), eq(10));
        }

        @Test
        @DisplayName("Should handle custom page and size parameters")
        void shouldHandleCustomPageAndSize() throws Exception {
            when(searchService.search(eq("camera"), any(SearchType.class), eq(2), eq(25)))
                    .thenReturn(mockResults);

            mockMvc.perform(get("/api/v1/search")
                            .param("query", "camera")
                            .param("page", "2")
                            .param("size", "25"))
                    .andExpect(status().isOk());

            verify(searchService).search(eq("camera"), any(SearchType.class), eq(2), eq(25));
        }

        @Test
        @DisplayName("Should return empty list when no results")
        void shouldReturnEmptyList() throws Exception {
            when(searchService.search(anyString(), any(SearchType.class), anyInt(), anyInt()))
                    .thenReturn(List.of());

            mockMvc.perform(get("/api/v1/search")
                            .param("query", "nonexistent"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/search/filtered - Filtered Search")
    class FilteredSearchTests {

        @Test
        @DisplayName("Should return filtered search results")
        void shouldReturnFilteredSearchResults() throws Exception {
            List<String> filters = List.of("category:electronics", "price:0-100");
            when(searchService.searchWithFilters(eq("laptop"), eq(filters), eq(SearchType.FULLTEXT)))
                    .thenReturn(mockResults);

            mockMvc.perform(post("/api/v1/search/filtered")
                            .param("query", "laptop")
                            .param("type", "FULLTEXT")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("[\"category:electronics\", \"price:0-100\"]"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2));

            verify(searchService).searchWithFilters(eq("laptop"), eq(filters), eq(SearchType.FULLTEXT));
        }

        @Test
        @DisplayName("Should use default type for filtered search")
        void shouldUseDefaultTypeForFiltered() throws Exception {
            when(searchService.searchWithFilters(anyString(), anyList(), eq(SearchType.FULLTEXT)))
                    .thenReturn(mockResults);

            mockMvc.perform(post("/api/v1/search/filtered")
                            .param("query", "phone")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("[]"))
                    .andExpect(status().isOk());

            verify(searchService).searchWithFilters(eq("phone"), eq(List.of()), eq(SearchType.FULLTEXT));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/search/suggestions - Search Suggestions")
    class SuggestionsTests {

        @Test
        @DisplayName("Should return search suggestions")
        void shouldReturnSearchSuggestions() throws Exception {
            List<String> suggestions = List.of("laptop case", "laptop stand", "laptop charger");
            when(searchService.getSuggestions(eq("laptop")))
                    .thenReturn(suggestions);

            mockMvc.perform(get("/api/v1/search/suggestions")
                            .param("q", "laptop"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0]").value("laptop case"))
                    .andExpect(jsonPath("$[1]").value("laptop stand"))
                    .andExpect(jsonPath("$.length()").value(3));

            verify(searchService).getSuggestions(eq("laptop"));
        }

        @Test
        @DisplayName("Should return empty suggestions for unknown query")
        void shouldReturnEmptySuggestions() throws Exception {
            when(searchService.getSuggestions(anyString()))
                    .thenReturn(List.of());

            mockMvc.perform(get("/api/v1/search/suggestions")
                            .param("q", "xyzabc"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/search/index - Index Document")
    class IndexDocumentTests {

        @Test
        @DisplayName("Should index document successfully")
        void shouldIndexDocument() throws Exception {
            doNothing().when(searchService).indexDocument(eq("doc-123"), anyMap());

            mockMvc.perform(post("/api/v1/search/index")
                            .param("documentId", "doc-123")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"title\":\"Test\",\"content\":\"Content\"}"))
                    .andExpect(status().isNoContent());

            verify(searchService).indexDocument(eq("doc-123"), anyMap());
        }

        @Test
        @DisplayName("Should handle indexing with empty document")
        void shouldHandleEmptyDocument() throws Exception {
            doNothing().when(searchService).indexDocument(eq("doc-456"), anyMap());

            mockMvc.perform(post("/api/v1/search/index")
                            .param("documentId", "doc-456")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isNoContent());

            verify(searchService).indexDocument(eq("doc-456"), anyMap());
        }
    }

    @Nested
    @DisplayName("Search Type Parameter Tests")
    class SearchTypeTests {

        @Test
        @DisplayName("Should handle all search types")
        void shouldHandleAllSearchTypes() throws Exception {
            when(searchService.search(anyString(), any(SearchType.class), anyInt(), anyInt()))
                    .thenReturn(mockResults);

            SearchType[] types = {SearchType.FULLTEXT, SearchType.SEMANTIC, SearchType.FUZZY, SearchType.FACETED, SearchType.HYBRID};

            for (SearchType type : types) {
                mockMvc.perform(get("/api/v1/search")
                                .param("query", "test")
                                .param("type", type.name()))
                        .andExpect(status().isOk());
            }
        }
    }

    @Nested
    @DisplayName("Response Format Tests")
    class ResponseFormatTests {

        @Test
        @DisplayName("Should return proper JSON structure for search results")
        void shouldReturnProperJsonStructure() throws Exception {
            when(searchService.search(anyString(), any(SearchType.class), anyInt(), anyInt()))
                    .thenReturn(mockResults);

            mockMvc.perform(get("/api/v1/search")
                            .param("query", "test"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].itemId").exists())
                    .andExpect(jsonPath("$[0].title").exists());
        }
    }
}

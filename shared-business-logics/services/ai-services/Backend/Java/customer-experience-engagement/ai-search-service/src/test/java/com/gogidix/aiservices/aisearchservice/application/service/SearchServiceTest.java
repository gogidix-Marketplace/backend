package com.gogidix.aiservices.aisearchservice.application.service;

import com.gogidix.aiservices.aisearchservice.domain.model.SearchQuery;
import com.gogidix.aiservices.aisearchservice.domain.model.SearchResult;
import com.gogidix.aiservices.aisearchservice.domain.model.SearchType;
import com.gogidix.aiservices.aisearchservice.domain.port.out.SearchEnginePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private SearchEnginePort searchEngine;

    @InjectMocks
    private SearchService searchService;

    @Test
    void shouldSearch() {
        List<SearchResult> results = List.of(
                SearchResult.builder()
                        .itemId("item-1")
                        .title("Test Product")
                        .relevanceScore(0.95)
                        .build()
        );

        when(searchEngine.search(any(), any())).thenReturn(results);

        List<SearchResult> searchResults = searchService.search("laptop", SearchType.FULLTEXT, 0, 10);

        assertThat(searchResults).hasSize(1);
    }

    @Test
    void shouldGetSuggestions() {
        List<String> suggestions = List.of("laptop", "laptop stand", "laptop bag");

        when(searchEngine.getSuggestions(any())).thenReturn(suggestions);

        List<String> result = searchService.getSuggestions("lap");

        assertThat(result).hasSize(3);
    }
}

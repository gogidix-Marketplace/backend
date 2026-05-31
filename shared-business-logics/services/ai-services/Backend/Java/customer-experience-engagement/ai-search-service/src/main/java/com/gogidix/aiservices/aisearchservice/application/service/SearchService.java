package com.gogidix.aiservices.aisearchservice.application.service;

import com.gogidix.aiservices.aisearchservice.domain.model.SearchQuery;
import com.gogidix.aiservices.aisearchservice.domain.model.SearchResult;
import com.gogidix.aiservices.aisearchservice.domain.model.SearchType;
import com.gogidix.aiservices.aisearchservice.domain.port.out.SearchEnginePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchEnginePort searchEngine;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;

    public List<SearchResult> search(String query, SearchType type, Integer page, Integer size) {
        SearchQuery searchQuery = SearchQuery.builder()
                .query(query)
                .page(page != null ? page : DEFAULT_PAGE)
                .size(size != null ? size : DEFAULT_SIZE)
                .build();

        return searchEngine.search(searchQuery, type != null ? type : SearchType.FULLTEXT);
    }

    public List<SearchResult> searchWithFilters(String query, List<String> filters, SearchType type) {
        SearchQuery searchQuery = SearchQuery.builder()
                .query(query)
                .filters(filters)
                .page(DEFAULT_PAGE)
                .size(DEFAULT_SIZE)
                .build();

        return searchEngine.searchWithFilters(searchQuery, filters);
    }

    public List<String> getSuggestions(String partialQuery) {
        return searchEngine.getSuggestions(partialQuery);
    }

    public void indexDocument(String documentId, Map<String, Object> document) {
        searchEngine.indexDocument(documentId, document);
    }
}

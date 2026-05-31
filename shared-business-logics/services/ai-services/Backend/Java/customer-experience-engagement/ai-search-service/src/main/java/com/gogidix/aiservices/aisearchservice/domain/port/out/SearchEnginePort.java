package com.gogidix.aiservices.aisearchservice.domain.port.out;

import com.gogidix.aiservices.aisearchservice.domain.model.SearchQuery;
import com.gogidix.aiservices.aisearchservice.domain.model.SearchResult;
import com.gogidix.aiservices.aisearchservice.domain.model.SearchType;

import java.util.List;
import java.util.Map;

public interface SearchEnginePort {
    List<SearchResult> search(SearchQuery query, SearchType type);

    List<SearchResult> searchWithFilters(SearchQuery query, List<String> filters);

    List<String> getSuggestions(String partialQuery);

    void indexDocument(String documentId, Map<String, Object> document);
}

package com.gogidix.aiservices.aisearchservice.domain.model;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public class SearchQuery {
    private final String query;
    private final List<String> filters;
    private final String sort;
    private final int page;
    private final int size;
    private final List<String> categories;
    private final Map<String, String> facetFilters;

    public String getQuery() { return query; }
    public List<String> getFilters() { return filters; }
    public String getSort() { return sort; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public List<String> getCategories() { return categories; }
    public Map<String, String> getFacetFilters() { return facetFilters; }
}

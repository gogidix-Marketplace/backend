package com.gogidix.aiservices.aisearchservice.interfaces.rest;

import com.gogidix.aiservices.aisearchservice.application.service.SearchService;
import com.gogidix.aiservices.aisearchservice.domain.model.SearchResult;
import com.gogidix.aiservices.aisearchservice.domain.model.SearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<List<SearchResult>> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "FULLTEXT") SearchType type,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        List<SearchResult> results = searchService.search(query, type, page, size);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/filtered")
    public ResponseEntity<List<SearchResult>> searchWithFilters(
            @RequestParam String query,
            @RequestParam(defaultValue = "FULLTEXT") SearchType type,
            @RequestBody List<String> filters) {

        List<SearchResult> results = searchService.searchWithFilters(query, filters, type);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> getSuggestions(@RequestParam String q) {
        List<String> suggestions = searchService.getSuggestions(q);
        return ResponseEntity.ok(suggestions);
    }

    @PostMapping("/index")
    public ResponseEntity<Void> indexDocument(
            @RequestParam String documentId,
            @RequestBody Map<String, Object> document) {

        searchService.indexDocument(documentId, document);
        return ResponseEntity.noContent().build();
    }
}

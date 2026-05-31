package com.gogidix.ecommerce.search.application.service;

import com.gogidix.ecommerce.search.application.dto.*;
import com.gogidix.ecommerce.search.application.mapper.SearchMapper;
import com.gogidix.ecommerce.search.domain.model.Search;
import com.gogidix.ecommerce.search.domain.repository.SearchRepository;
import com.gogidix.ecommerce.search.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    private final SearchRepository repository;
    private final SearchMapper mapper;

    public SearchService(SearchRepository repository, SearchMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<SearchResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public SearchResponse getById(String id) {
        Search entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Search not found"));
        return mapper.toResponse(entity);
    }

    public SearchResponse create(CreateSearchRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        Search entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        Search saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public SearchResponse update(String id, UpdateSearchRequest request) {
        Search entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Search not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        Search saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Search not found"));
        repository.deleteById(id);
    }
}

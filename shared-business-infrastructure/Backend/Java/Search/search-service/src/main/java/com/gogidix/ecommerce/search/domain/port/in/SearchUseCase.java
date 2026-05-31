package com.gogidix.ecommerce.search.domain.port.in;

import com.gogidix.ecommerce.search.application.dto.*;
import java.util.List;

public interface SearchUseCase {
    SearchResponse create(CreateSearchRequest request);
    SearchResponse update(String id, UpdateSearchRequest request);
    void delete(String id);
    SearchResponse getById(String id);
    List<SearchResponse> getAll();
}

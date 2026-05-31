package com.gogidix.ecommerce.search.application.mapper;

import com.gogidix.ecommerce.search.application.dto.*;
import com.gogidix.ecommerce.search.domain.model.Search;
import org.springframework.stereotype.Component;

@Component
public class SearchMapper {

    public SearchResponse toResponse(Search entity) {
        if (entity == null) return null;
        return new SearchResponse(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getType(),
            entity.getIndexName(),
            entity.getQueryType(),
            entity.getResultCount(),
            entity.getIsActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public Search toEntity(CreateSearchRequest request) {
        Search entity = new Search();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setType(request.type());
        entity.setIndexName(request.indexName());
        entity.setQueryType(request.queryType());
        entity.setResultCount(request.resultCount());
        entity.setIsActive(true);
        return entity;
    }

    public void updateFromRequest(Search entity, UpdateSearchRequest request) {
        if (request.name() != null) entity.setName(request.name());
        if (request.description() != null) entity.setDescription(request.description());
        if (request.type() != null) entity.setType(request.type());
        if (request.indexName() != null) entity.setIndexName(request.indexName());
        if (request.queryType() != null) entity.setQueryType(request.queryType());
        if (request.resultCount() != null) entity.setResultCount(request.resultCount());
        if (request.isActive() != null) entity.setIsActive(request.isActive());
    }
}

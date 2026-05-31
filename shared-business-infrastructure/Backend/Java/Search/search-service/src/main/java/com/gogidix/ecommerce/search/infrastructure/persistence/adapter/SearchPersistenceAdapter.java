package com.gogidix.ecommerce.search.infrastructure.persistence.adapter;

import com.gogidix.ecommerce.search.domain.model.Search;
import com.gogidix.ecommerce.search.domain.port.out.SearchRepositoryPort;
import com.gogidix.ecommerce.search.infrastructure.persistence.repository.SearchMongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SearchPersistenceAdapter implements SearchRepositoryPort {

    private final SearchMongoRepository mongoRepository;

    public SearchPersistenceAdapter(SearchMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Search save(Search entity) { return mongoRepository.save(entity); }

    @Override
    public Optional<Search> findById(String id) { return mongoRepository.findById(id); }

    @Override
    public List<Search> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        return mongoRepository.findByTenantIdAndIsActive(tenantId, isActive);
    }

    @Override
    public void deleteById(String id) { mongoRepository.deleteById(id); }
}

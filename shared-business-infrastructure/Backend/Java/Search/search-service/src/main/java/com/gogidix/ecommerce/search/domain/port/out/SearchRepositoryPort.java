package com.gogidix.ecommerce.search.domain.port.out;

import com.gogidix.ecommerce.search.domain.model.Search;
import java.util.List;
import java.util.Optional;

public interface SearchRepositoryPort {
    Search save(Search entity);
    Optional<Search> findById(String id);
    List<Search> findByTenantIdAndIsActive(String tenantId, Boolean isActive);
    void deleteById(String id);
}

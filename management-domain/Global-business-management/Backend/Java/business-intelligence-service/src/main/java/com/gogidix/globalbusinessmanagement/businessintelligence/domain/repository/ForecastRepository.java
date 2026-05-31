package com.gogidix.globalbusinessmanagement.businessintelligence.domain.repository;

import com.gogidix.globalbusinessmanagement.businessintelligence.domain.model.Forecast;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ForecastRepository extends MongoRepository<Forecast, String> {
    List<Forecast> findByTenantId(String tenantId);
}

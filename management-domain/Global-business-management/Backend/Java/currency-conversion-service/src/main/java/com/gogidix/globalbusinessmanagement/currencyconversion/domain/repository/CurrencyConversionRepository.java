package com.gogidix.globalbusinessmanagement.currencyconversion.domain.repository;

import com.gogidix.globalbusinessmanagement.currencyconversion.domain.model.CurrencyConversion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyConversionRepository extends MongoRepository<CurrencyConversion, String> {
    List<CurrencyConversion> findByTenantId(String tenantId);
}

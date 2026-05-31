package com.gogidix.globalbusinessmanagement.localization.domain.repository;

import com.gogidix.globalbusinessmanagement.localization.domain.model.LocalizationEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalizationEntryRepository extends MongoRepository<LocalizationEntry, String> {
    List<LocalizationEntry> findByTenantId(String tenantId);
}

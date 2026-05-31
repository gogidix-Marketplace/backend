package com.gogidix.hr.countryhrmanagement.domain.repository;

import com.gogidix.hr.countryhrmanagement.domain.model.CountryHRConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryHRConfigRepository extends MongoRepository<CountryHRConfig, String> {}

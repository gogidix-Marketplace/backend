package com.gogidix.hr.countryhrmanagement.domain.repository;

import com.gogidix.hr.countryhrmanagement.domain.model.LaborLaw;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaborLawRepository extends MongoRepository<LaborLaw, String> {}

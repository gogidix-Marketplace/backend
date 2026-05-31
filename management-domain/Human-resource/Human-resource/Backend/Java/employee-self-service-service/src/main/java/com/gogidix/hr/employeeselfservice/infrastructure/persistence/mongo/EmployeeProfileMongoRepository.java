package com.gogidix.hr.employeeselfservice.infrastructure.persistence.mongo;

import com.gogidix.hr.employeeselfservice.domain.model.EmployeeProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeProfileMongoRepository extends MongoRepository<EmployeeProfile, String> {}

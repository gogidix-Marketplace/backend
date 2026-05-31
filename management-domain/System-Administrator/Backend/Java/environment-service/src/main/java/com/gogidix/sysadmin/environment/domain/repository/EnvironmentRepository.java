package com.gogidix.sysadmin.environment.domain.repository;
import com.gogidix.sysadmin.environment.domain.model.Environment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface EnvironmentRepository extends MongoRepository<Environment, String> {}

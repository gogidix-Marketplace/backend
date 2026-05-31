package com.gogidix.sysadmin.configuration.domain.repository;
import com.gogidix.sysadmin.configuration.domain.model.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ConfigurationRepository extends MongoRepository<Configuration, String> {}

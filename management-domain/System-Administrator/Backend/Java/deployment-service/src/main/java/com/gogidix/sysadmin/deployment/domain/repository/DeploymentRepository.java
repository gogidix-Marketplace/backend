package com.gogidix.sysadmin.deployment.domain.repository;
import com.gogidix.sysadmin.deployment.domain.model.Deployment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface DeploymentRepository extends MongoRepository<Deployment, String> {}

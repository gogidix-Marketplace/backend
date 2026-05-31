package com.gogidix.sysadmin.accesscontrol.domain.repository;
import com.gogidix.sysadmin.accesscontrol.domain.model.AccessPolicy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AccessPolicyRepository extends MongoRepository<AccessPolicy, String> {}

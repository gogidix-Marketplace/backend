package com.gogidix.sysadmin.securitymonitoring.domain.repository;
import com.gogidix.sysadmin.securitymonitoring.domain.model.SecurityEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SecurityEventRepository extends MongoRepository<SecurityEvent, String> {}

package com.gogidix.sysadmin.alertmanagement.domain.repository;
import com.gogidix.sysadmin.alertmanagement.domain.model.Alert;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface AlertRepository extends MongoRepository<Alert, String> {}

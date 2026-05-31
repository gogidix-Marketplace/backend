package com.gogidix.sysadmin.incidentmanagement.domain.repository;
import com.gogidix.sysadmin.incidentmanagement.domain.model.Incident;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface IncidentRepository extends MongoRepository<Incident, String> {}

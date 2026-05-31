package com.gogidix.hr.training.domain.repository;

import com.gogidix.hr.training.domain.model.TrainingProgram;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingProgramRepository extends MongoRepository<TrainingProgram, String> {
    List<TrainingProgram> findByTenantId(String tenantId);
}

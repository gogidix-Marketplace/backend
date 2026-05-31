package com.gogidix.hr.employeeselfservice.application.service;

import com.gogidix.hr.employeeselfservice.domain.model.EmployeeProfile;
import com.gogidix.hr.employeeselfservice.infrastructure.persistence.mongo.EmployeeProfileMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeSelfService {
    private final EmployeeProfileMongoRepository repository;

    public EmployeeProfile create(EmployeeProfile profile) { return repository.save(profile); }
    public EmployeeProfile getById(String id) { return repository.findById(id).orElse(null); }
    public List<EmployeeProfile> getAll() { return repository.findAll(); }
    public EmployeeProfile update(EmployeeProfile profile) { return repository.save(profile); }
    public void delete(String id) { repository.deleteById(id); }
}

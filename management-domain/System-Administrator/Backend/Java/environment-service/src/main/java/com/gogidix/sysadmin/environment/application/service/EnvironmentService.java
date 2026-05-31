package com.gogidix.sysadmin.environment.application.service;
import com.gogidix.sysadmin.environment.domain.model.Environment;
import com.gogidix.sysadmin.environment.domain.repository.EnvironmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class EnvironmentService {
    private final EnvironmentRepository repository;
    public Environment create(Environment entity) { return repository.save(entity); }
    public Environment getById(String id) { return repository.findById(id).orElse(null); }
    public List<Environment> getAll() { return repository.findAll(); }
    public void delete(String id) { repository.deleteById(id); }
}

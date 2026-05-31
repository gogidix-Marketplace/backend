package com.gogidix.sysadmin.deployment.application.service;
import com.gogidix.sysadmin.deployment.domain.model.Deployment;
import com.gogidix.sysadmin.deployment.domain.repository.DeploymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class DeploymentService {
    private final DeploymentRepository repository;
    public Deployment create(Deployment entity) { return repository.save(entity); }
    public Deployment getById(String id) { return repository.findById(id).orElse(null); }
    public List<Deployment> getAll() { return repository.findAll(); }
    public void delete(String id) { repository.deleteById(id); }
}

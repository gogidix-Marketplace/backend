package com.gogidix.sysadmin.configuration.application.service;
import com.gogidix.sysadmin.configuration.domain.model.Configuration;
import com.gogidix.sysadmin.configuration.domain.repository.ConfigurationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ConfigurationService {
    private final ConfigurationRepository repository;
    public Configuration create(Configuration entity) { return repository.save(entity); }
    public Configuration getById(String id) { return repository.findById(id).orElse(null); }
    public List<Configuration> getAll() { return repository.findAll(); }
    public void delete(String id) { repository.deleteById(id); }
}

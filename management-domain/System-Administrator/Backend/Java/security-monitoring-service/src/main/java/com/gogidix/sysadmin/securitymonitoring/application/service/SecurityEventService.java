package com.gogidix.sysadmin.securitymonitoring.application.service;
import com.gogidix.sysadmin.securitymonitoring.domain.model.SecurityEvent;
import com.gogidix.sysadmin.securitymonitoring.domain.repository.SecurityEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class SecurityEventService {
    private final SecurityEventRepository repository;
    public SecurityEvent create(SecurityEvent entity) { return repository.save(entity); }
    public SecurityEvent getById(String id) { return repository.findById(id).orElse(null); }
    public List<SecurityEvent> getAll() { return repository.findAll(); }
    public void delete(String id) { repository.deleteById(id); }
}

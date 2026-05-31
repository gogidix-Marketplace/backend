package com.gogidix.sysadmin.audit.application.service;

import com.gogidix.sysadmin.audit.domain.model.AuditLog;
import com.gogidix.sysadmin.audit.domain.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository repository;

    public AuditLog create(AuditLog entity) { return repository.save(entity); }
    public AuditLog getById(String id) { return repository.findById(id).orElse(null); }
    public List<AuditLog> getAll() { return repository.findAll(); }
    public void delete(String id) { repository.deleteById(id); }
}

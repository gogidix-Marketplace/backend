package com.gogidix.sysadmin.accesscontrol.application.service;
import com.gogidix.sysadmin.accesscontrol.domain.model.AccessPolicy;
import com.gogidix.sysadmin.accesscontrol.domain.repository.AccessPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class AccessPolicyService {
    private final AccessPolicyRepository repository;
    public AccessPolicy create(AccessPolicy entity) { return repository.save(entity); }
    public AccessPolicy getById(String id) { return repository.findById(id).orElse(null); }
    public List<AccessPolicy> getAll() { return repository.findAll(); }
    public void delete(String id) { repository.deleteById(id); }
}

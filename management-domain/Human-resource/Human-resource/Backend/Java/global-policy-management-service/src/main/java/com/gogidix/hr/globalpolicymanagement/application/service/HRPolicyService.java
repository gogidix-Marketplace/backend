package com.gogidix.hr.globalpolicymanagement.application.service;

import com.gogidix.hr.globalpolicymanagement.domain.model.HRPolicy;
import com.gogidix.hr.globalpolicymanagement.domain.repository.HRPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HRPolicyService {
    private final HRPolicyRepository repository;

    public HRPolicy create(HRPolicy policy) { return repository.save(policy); }
    public HRPolicy getById(String id) { return repository.findById(id).orElse(null); }
    public List<HRPolicy> getAll() { return repository.findAll(); }
    public HRPolicy update(HRPolicy policy) { return repository.save(policy); }
    public void delete(String id) { repository.deleteById(id); }
}

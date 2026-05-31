package com.gogidix.sysadmin.performancemetrics.application.service;
import com.gogidix.sysadmin.performancemetrics.domain.model.PerformanceMetric;
import com.gogidix.sysadmin.performancemetrics.domain.repository.PerformanceMetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class PerformanceMetricService {
    private final PerformanceMetricRepository repository;
    public PerformanceMetric create(PerformanceMetric entity) { return repository.save(entity); }
    public PerformanceMetric getById(String id) { return repository.findById(id).orElse(null); }
    public List<PerformanceMetric> getAll() { return repository.findAll(); }
    public void delete(String id) { repository.deleteById(id); }
}

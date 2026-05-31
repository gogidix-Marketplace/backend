package com.gogidix.sysadmin.incidentmanagement.application.service;
import com.gogidix.sysadmin.incidentmanagement.domain.model.Incident;
import com.gogidix.sysadmin.incidentmanagement.domain.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class IncidentService {
    private final IncidentRepository repository;
    public Incident create(Incident entity) { return repository.save(entity); }
    public Incident getById(String id) { return repository.findById(id).orElse(null); }
    public List<Incident> getAll() { return repository.findAll(); }
    public void delete(String id) { repository.deleteById(id); }
}

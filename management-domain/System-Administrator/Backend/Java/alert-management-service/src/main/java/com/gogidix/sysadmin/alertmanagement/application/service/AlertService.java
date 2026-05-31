package com.gogidix.sysadmin.alertmanagement.application.service;
import com.gogidix.sysadmin.alertmanagement.domain.model.Alert;
import com.gogidix.sysadmin.alertmanagement.domain.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class AlertService {
    private final AlertRepository repository;
    public Alert create(Alert entity) { return repository.save(entity); }
    public Alert getById(String id) { return repository.findById(id).orElse(null); }
    public List<Alert> getAll() { return repository.findAll(); }
    public void delete(String id) { repository.deleteById(id); }
}

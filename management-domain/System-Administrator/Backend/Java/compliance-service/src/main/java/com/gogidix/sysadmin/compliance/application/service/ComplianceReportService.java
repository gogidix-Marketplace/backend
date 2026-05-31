package com.gogidix.sysadmin.compliance.application.service;
import com.gogidix.sysadmin.compliance.domain.model.ComplianceReport;
import com.gogidix.sysadmin.compliance.domain.repository.ComplianceReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ComplianceReportService {
    private final ComplianceReportRepository repository;
    public ComplianceReport create(ComplianceReport entity) { return repository.save(entity); }
    public ComplianceReport getById(String id) { return repository.findById(id).orElse(null); }
    public List<ComplianceReport> getAll() { return repository.findAll(); }
    public void delete(String id) { repository.deleteById(id); }
}

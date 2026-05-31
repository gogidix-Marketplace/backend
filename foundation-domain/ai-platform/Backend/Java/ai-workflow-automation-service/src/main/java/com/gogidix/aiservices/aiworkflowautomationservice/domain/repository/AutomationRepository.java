package com.gogidix.aiservices.aiworkflowautomationservice.domain.repository;
import com.gogidix.aiservices.aiworkflowautomationservice.domain.model.Automation;
import java.util.List;
import java.util.Optional;
public interface AutomationRepository {
    Automation save(Automation automation);
    Optional<Automation> findById(String id);
    Optional<Automation> findByAutomationIdAndTenantId(String automationId, String tenantId);
    List<Automation> findByTenantId(String tenantId);
    void deleteById(String id);
}

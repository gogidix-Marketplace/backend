package com.gogidix.aiservices.aiworkflowautomationservice.application.service;
import com.gogidix.aiservices.aiworkflowautomationservice.domain.model.Automation;
import com.gogidix.aiservices.aiworkflowautomationservice.domain.model.AutomationAction;
import com.gogidix.aiservices.aiworkflowautomationservice.domain.repository.AutomationRepository;
import com.gogidix.aiservices.aiworkflowautomationservice.shared.exception.NotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class AutomationService {
    private final AutomationRepository repository;
    public AutomationService(AutomationRepository repository) { this.repository = repository; }
    public Automation createAutomation(String tenantId, String name, Automation.TriggerType triggerType, String schedule, List<AutomationAction> actions) {
        Automation automation = new Automation(tenantId, name, triggerType, actions);
        automation.setSchedule(schedule);
        return repository.save(automation);
    }
    public Automation getAutomationById(String automationId, String tenantId) {
        return repository.findByAutomationIdAndTenantId(automationId, tenantId)
                .orElseThrow(() -> new NotFoundException("Automation", automationId));
    }
    public List<Automation> getAutomationsByTenant(String tenantId) { return repository.findByTenantId(tenantId); }
    public void triggerAutomation(String automationId, String tenantId) {
        Automation automation = getAutomationById(automationId, tenantId);
        if (automation.canExecute()) {
            automation.recordExecution();
            repository.save(automation);
        }
    }
}

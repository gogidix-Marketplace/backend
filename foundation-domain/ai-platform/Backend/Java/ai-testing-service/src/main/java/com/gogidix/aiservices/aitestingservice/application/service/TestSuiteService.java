package com.gogidix.aiservices.aitestingservice.application.service;
import com.gogidix.aiservices.aitestingservice.domain.model.TestRun;
import com.gogidix.aiservices.aitestingservice.domain.model.TestSuite;
import com.gogidix.aiservices.aitestingservice.domain.repository.TestSuiteRepository;
import com.gogidix.aiservices.aitestingservice.shared.exception.NotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class TestSuiteService {
    private final TestSuiteRepository repository;
    public TestSuiteService(TestSuiteRepository repository) { this.repository = repository; }
    public TestSuite createSuite(String tenantId, String name) {
        TestSuite suite = new TestSuite(tenantId, name);
        return repository.save(suite);
    }
    public TestSuite getSuiteById(String suiteId, String tenantId) {
        return repository.findBySuiteIdAndTenantId(suiteId, tenantId)
                .orElseThrow(() -> new NotFoundException("TestSuite", suiteId));
    }
    public List<TestSuite> getSuitesByTenant(String tenantId) { return repository.findByTenantId(tenantId); }
    public TestRun runSuite(String suiteId, String tenantId) {
        TestSuite suite = getSuiteById(suiteId, tenantId);
        suite.startExecution();
        repository.save(suite);
        TestRun run = new TestRun(suiteId, tenantId);
        suite.complete();
        repository.save(suite);
        run.complete(TestRun.TestRunStatus.PASSED);
        return run;
    }
}

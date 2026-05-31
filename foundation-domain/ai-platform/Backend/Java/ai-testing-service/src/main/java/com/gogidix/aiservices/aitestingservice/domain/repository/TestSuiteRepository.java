package com.gogidix.aiservices.aitestingservice.domain.repository;
import com.gogidix.aiservices.aitestingservice.domain.model.TestSuite;
import java.util.List;
import java.util.Optional;
public interface TestSuiteRepository {
    TestSuite save(TestSuite suite);
    Optional<TestSuite> findById(String id);
    Optional<TestSuite> findBySuiteIdAndTenantId(String suiteId, String tenantId);
    List<TestSuite> findByTenantId(String tenantId);
    void deleteById(String id);
}

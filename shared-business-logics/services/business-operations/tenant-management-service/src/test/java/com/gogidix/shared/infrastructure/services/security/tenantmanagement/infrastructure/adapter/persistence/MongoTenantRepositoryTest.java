package com.gogidix.shared.infrastructure.services.security.tenantmanagement.infrastructure.adapter.persistence;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.model.Tenant;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.shared.requestcontext.TenantContext;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.shared.requestcontext.TenantContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MongoTenantRepository Tests")
class MongoTenantRepositoryTest {

    @Mock private MongoTemplate mongoTemplate;
    private MongoTenantRepository repository;

    @BeforeEach
    void setUp() {
        repository = new MongoTenantRepository(mongoTemplate);
        TenantContextHolder.setContext(TenantContext.builder().tenantId("t1").build());
    }

    @AfterEach
    void tearDown() {
        TenantContextHolder.clearContext();
    }

    @Test
    void shouldSave() {
        Tenant tenant = Tenant.builder().tenantId("t1").build();
        when(mongoTemplate.save(any())).thenReturn(tenant);
        assertNotNull(repository.save(tenant));
    }

    @Test
    void shouldFindByTenantId() {
        when(mongoTemplate.findOne(any(Query.class), eq(Tenant.class))).thenReturn(Tenant.builder().build());
        assertTrue(repository.findByTenantId("t1").isPresent());
    }

    @Test
    void shouldExistsByTenantId() {
        when(mongoTemplate.exists(any(Query.class), eq(Tenant.class))).thenReturn(true);
        assertTrue(repository.existsByTenantId("t1"));
    }

    @Test
    void shouldExistsByDomain() {
        when(mongoTemplate.exists(any(Query.class), eq(Tenant.class))).thenReturn(false);
        assertFalse(repository.existsByDomain("test.com"));
    }

    @Test
    void shouldFindByDomain() {
        when(mongoTemplate.findOne(any(Query.class), eq(Tenant.class))).thenReturn(null);
        assertTrue(repository.findByDomain("test.com").isEmpty());
    }

    @Test
    void shouldFindAll() {
        when(mongoTemplate.find(any(Query.class), eq(Tenant.class))).thenReturn(List.of());
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void shouldDeleteByTenantId() {
        repository.deleteByTenantId("t1");
        verify(mongoTemplate).remove(any(Query.class), eq(Tenant.class));
    }

    @Test
    void shouldFindByStatus() {
        when(mongoTemplate.find(any(Query.class), eq(Tenant.class))).thenReturn(List.of());
        assertTrue(repository.findByStatus(Tenant.TenantStatus.ACTIVE).isEmpty());
    }

    @Test
    void shouldFindByPlan() {
        when(mongoTemplate.find(any(Query.class), eq(Tenant.class))).thenReturn(List.of());
        assertTrue(repository.findByPlan(Tenant.TenantPlan.FREE).isEmpty());
    }
}

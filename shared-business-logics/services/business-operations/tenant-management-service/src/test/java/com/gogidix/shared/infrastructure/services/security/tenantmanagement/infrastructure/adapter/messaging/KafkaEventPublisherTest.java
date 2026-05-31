package com.gogidix.shared.infrastructure.services.security.tenantmanagement.infrastructure.adapter.messaging;

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
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("KafkaEventPublisher Tests")
class KafkaEventPublisherTest {

    @Mock private KafkaTemplate<String, com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.event.DomainEvent> kafkaTemplate;
    private KafkaEventPublisher publisher;

    @BeforeEach
    void setUp() {
        publisher = new KafkaEventPublisher(kafkaTemplate);
        TenantContextHolder.setContext(TenantContext.builder().tenantId("t1").build());
    }

    @AfterEach
    void tearDown() {
        TenantContextHolder.clearContext();
    }

    @Test
    void shouldPublishEvent() {
        when(kafkaTemplate.send(any(), any(), any()))
            .thenReturn(CompletableFuture.completedFuture(null));

        Tenant tenant = Tenant.builder().id("1").tenantId("t1").name("Test").status(Tenant.TenantStatus.ACTIVE).plan(Tenant.TenantPlan.FREE).build();
        com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.event.TenantCreatedEvent event =
            new com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.event.TenantCreatedEvent(tenant);

        publisher.publish(event);
        verify(kafkaTemplate).send(any(), any(), any());
    }
}

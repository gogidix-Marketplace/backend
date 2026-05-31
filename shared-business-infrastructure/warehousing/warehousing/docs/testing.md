# Testing Guide - Warehousing Domain

## Overview

This guide covers testing strategies and practices for the Shared Warehousing Core domain, including unit tests, integration tests, and end-to-end tests.

## Table of Contents

- [Testing Pyramid](#testing-pyramid)
- [Unit Testing](#unit-testing)
- [Integration Testing](#integration-testing)
- [E2E Testing](#e2e-testing)
- [Test Data Management](#test-data-management)
- [Test Reports](#test-reports)
- [CI/CD Integration](#cicd-integration)

---

## Testing Pyramid

```
                    /\
                   /  \
                  / E2E \
                 /______\
                /        \
               /Integration\
              /__________  \
             /            \ \
            /  Unit Tests   \\
           /________________  \
```

| Level | Tool | Coverage Target | Execution Time |
|-------|------|-----------------|----------------|
| Unit Tests | JUnit 5, Mockito | 80%+ code coverage | < 5 min |
| Integration Tests | Testcontainers, REST Assured | 70%+ API coverage | < 15 min |
| E2E Tests | Playwright | Critical user flows | < 30 min |

---

## Unit Testing

### Structure

```
src/test/java/
├── unit/
│   ├── domain/
│   │   ├── entity/
│   │   │   └── InventoryItemTest.java
│   │   ├── service/
│   │   │   └── InventoryServiceTest.java
│   │   └── events/
│   │       └── InventoryEventTest.java
│   ├── application/
│   │   ├── service/
│   │   │   └── InventoryApplicationServiceTest.java
│   │   └── handlers/
│   │       └── CreateItemCommandHandlerTest.java
│   └── interfaces/
│       └── rest/
│           ├── InventoryControllerTest.java
│           └── dto/
│               └── InventoryItemDtoTest.java
└── resources/
    ├── application-test.yml
    └── test-data.json
```

### Example Unit Test

```java
package com.gogidix.shared.warehousing.inventory.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("InventoryItem Domain Tests")
class InventoryItemTest {

    @Test
    @DisplayName("Should create inventory item with valid data")
    void shouldCreateInventoryItem() {
        // Given
        String tenantId = "tenant-001";
        String sku = "ITEM-001";
        Integer quantity = 100;

        // When
        InventoryItem item = new InventoryItem();
        item.setTenantId(tenantId);
        item.setSku(sku);
        item.setQuantity(quantity);

        // Then
        assertThat(item.getTenantId()).isEqualTo(tenantId);
        assertThat(item.getSku()).isEqualTo(sku);
        assertThat(item.getQuantity()).isEqualTo(quantity);
    }

    @Test
    @DisplayName("Should adjust inventory quantity")
    void shouldAdjustQuantity() {
        // Given
        InventoryItem item = new InventoryItem();
        item.setQuantity(100);

        // When
        item.adjustQuantity(50);

        // Then
        assertThat(item.getQuantity()).isEqualTo(150);
    }

    @Test
    @DisplayName("Should throw exception when quantity goes negative")
    void shouldThrowWhenQuantityNegative() {
        // Given
        InventoryItem item = new InventoryItem();
        item.setQuantity(10);

        // When & Then
        assertThatThrownBy(() -> item.adjustQuantity(-20))
            .isInstanceOf(InvalidQuantityException.class)
            .hasMessageContaining("Quantity cannot be negative");
    }

    @Test
    @DisplayName("Should check if item is below reorder threshold")
    void shouldCheckLowStock() {
        // Given
        InventoryItem item = new InventoryItem();
        item.setQuantity(5);
        item.setReorderThreshold(10);

        // When
        boolean isLowStock = item.isBelowReorderThreshold();

        // Then
        assertThat(isLowStock).isTrue();
    }
}
```

### Mockito Examples

```java
package com.gogidix.shared.warehousing.inventory.domain.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryItemRepository repository;

    @InjectMocks
    private InventoryDomainService service;

    @Test
    @DisplayName("Should allocate stock when available")
    void shouldAllocateStock() {
        // Given
        String itemId = "item-001";
        Integer requestedQty = 50;

        InventoryItem item = new InventoryItem();
        item.setId(itemId);
        item.setQuantity(100);

        when(repository.findById(itemId)).thenReturn(Optional.of(item));

        // When
        boolean allocated = service.allocateStock(itemId, requestedQty);

        // Then
        assertThat(allocated).isTrue();
        verify(repository).save(argThat(i ->
            i.getQuantity() == 50 && i.getReservedQuantity() == 50
        ));
    }

    @Test
    @DisplayName("Should not allocate stock when insufficient")
    void shouldNotAllocateInsufficientStock() {
        // Given
        String itemId = "item-001";
        Integer requestedQty = 150;

        InventoryItem item = new InventoryItem();
        item.setId(itemId);
        item.setQuantity(100);

        when(repository.findById(itemId)).thenReturn(Optional.of(item));

        // When
        boolean allocated = service.allocateStock(itemId, requestedQty);

        // Then
        assertThat(allocated).isFalse();
        verify(repository, never()).save(any());
    }
}
```

---

## Integration Testing

### Testcontainers Configuration

```java
package com.gogidix.shared.warehousing;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class TestContainerConfig {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(
        DockerImageName.parse("mongo:6.0")
    );

    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(
        DockerImageName.parse("confluentinc/cp-kafka:7.5.0")
    );

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }
}
```

### Integration Test Example

```java
package com.gogidix.shared.warehousing.inventory.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class InventoryIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Should create item via REST API")
    void shouldCreateItem() {
        // Given
        CreateItemRequest request = new CreateItemRequest();
        request.setSku("INTEGRATION-001");
        request.setName("Integration Test Item");
        request.setQuantity(100);

        // When
        ResponseEntity<InventoryItemDto> response = restTemplate.postForEntity(
            "/api/v1/inventory/items",
            new HttpEntity<>(request, createHeaders()),
            InventoryItemDto.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getSku()).isEqualTo("INTEGRATION-001");
    }

    @Test
    @DisplayName("Should handle concurrent stock updates")
    void shouldHandleConcurrentUpdates() {
        // Given
        String itemId = createTestItem();
        int threadCount = 10;
        int adjustmentsPerThread = 10;

        // When - Execute parallel adjustments
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<Boolean>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(() -> {
                for (int j = 0; j < adjustmentsPerThread; j++) {
                    restTemplate.postForObject(
                        "/api/v1/inventory/adjust",
                        new AdjustmentRequest(itemId, 1, "TEST"),
                        Void.class
                    );
                }
                return true;
            }));
        }

        // Then - Verify all adjustments were applied
        futures.forEach(f -> assertThat(f.get()).isTrue());

        InventoryItemDto item = restTemplate.getForObject(
            "/api/v1/inventory/items/" + itemId,
            InventoryItemDto.class
        );
        assertThat(item.getQuantity()).isEqualTo(100 + (threadCount * adjustmentsPerThread));
    }
}
```

### Kafka Integration Test

```java
package com.gogidix.shared.warehousing.inventory.messaging;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"inventory.adjusted"})
class KafkaIntegrationTest {

    @Autowired
    private KafkaTemplate<String, InventoryEvent> kafkaTemplate;

    @Autowired
    private InventoryEventConsumer consumer;

    @Test
    @DisplayName("Should consume inventory adjustment event")
    void shouldConsumeEvent() {
        // Given
        InventoryEvent event = new InventoryEvent();
        event.setItemId("item-001");
        event.setAdjustment(10);
        event.setReason("STOCK_IN");

        // When
        kafkaTemplate.send("inventory.adjusted", event);

        // Then
        await().atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                assertThat(consumer.getReceivedEvents()).hasSize(1);
                assertThat(consumer.getReceivedEvents().get(0).getReason())
                    .isEqualTo("STOCK_IN");
            });
    }
}
```

---

## E2E Testing

### Playwright Configuration

E2E tests are located in the `e2e/` directory:

```
e2e/
├── playwright.config.ts
├── package.json
├── tests/
│   ├── api/
│   │   ├── inventory-flow.spec.ts
│   │   ├── fulfillment-flow.spec.ts
│   │   └── storage-flow.spec.ts
│   ├── dashboards/
│   │   ├── partners-dashboard.spec.ts
│   │   └── private-storage-dashboard.spec.ts
│   ├── mobile/
│   │   ├── vendor-app.spec.ts
│   │   └── staff-app.spec.ts
│   └── cross-service/
│       └── complete-fulfillment.spec.ts
└── helpers/
    ├── api-client.ts
    └── test-data.ts
```

### Run E2E Tests

```bash
cd e2e
npm install
npx playwright install --with-deps

# Run all tests
npm test

# Run specific test suites
npm run test:api
npm run test:web
npm run test:mobile
npm run test:cross-service

# Run with UI
npm run test:ui

# Debug mode
npm run test:debug
```

### E2E Test Example

```typescript
import { test, expect } from '@playwright/test';

test.describe('Inventory API Flow', () => {
  test('should create, update, and delete inventory item', async ({ request }) => {
    // Create item
    const createResponse = await request.post('/api/v1/inventory/items', {
      data: {
        sku: 'E2E-ITEM-001',
        name: 'E2E Test Item',
        quantity: 100
      },
      headers: { 'X-Tenant-ID': 'test-tenant' }
    });
    expect(createResponse.ok()).toBeTruthy();
    const item = await createResponse.json();
    const itemId = item.id;

    // Update item
    const updateResponse = await request.put(`/api/v1/inventory/items/${itemId}`, {
      data: { name: 'Updated Item Name' },
      headers: { 'X-Tenant-ID': 'test-tenant' }
    });
    expect(updateResponse.ok()).toBeTruthy();

    // Delete item
    const deleteResponse = await request.delete(`/api/v1/inventory/items/${itemId}`, {
      headers: { 'X-Tenant-ID': 'test-tenant' }
    });
    expect(deleteResponse.status()).toBe(204);
  });
});
```

---

## Test Data Management

### Test Data Builders

```java
package com.gogidix.shared.warehousing.test;

public class InventoryItemBuilder {
    private String tenantId = "test-tenant";
    private String sku = "TEST-ITEM";
    private Integer quantity = 100;
    private String locationId = "ZONE-A-AISLE-01";

    public static InventoryItemBuilder anItem() {
        return new InventoryItemBuilder();
    }

    public InventoryItemBuilder withTenantId(String tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public InventoryItemBuilder withSku(String sku) {
        this.sku = sku;
        return this;
    }

    public InventoryItemBuilder withQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public InventoryItem build() {
        InventoryItem item = new InventoryItem();
        item.setTenantId(tenantId);
        item.setSku(sku);
        item.setQuantity(quantity);
        item.setLocationId(locationId);
        return item;
    }
}

// Usage
InventoryItem item = InventoryItemBuilder.anItem()
    .withSku("CUSTOM-SKU")
    .withQuantity(500)
    .build();
```

### Test Fixtures

```javascript
// e2e/helpers/test-data.ts
export class TestDataGenerator {
  static generateSKU(): string {
    return `TEST-SKU-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
  }

  static generateOrderNumber(): string {
    return `ORDER-${Date.now()}`;
  }

  static generateInventoryItem() {
    return {
      sku: this.generateSKU(),
      name: 'Test Inventory Item',
      quantity: Math.floor(Math.random() * 1000) + 1,
      unitOfMeasure: 'EA',
      locationId: 'ZONE-A-AISLE-01-SHELF-01-BIN-01',
      tenantId: 'test-tenant-001'
    };
  }
}
```

---

## Test Reports

### JaCoCo Configuration

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
        <execution>
            <id>check</id>
            <goals>
                <goal>check</goal>
            </goals>
            <configuration>
                <rules>
                    <rule>
                        <element>BUNDLE</element>
                        <limits>
                            <limit>
                                <counter>INSTRUCTION</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.80</minimum>
                            </limit>
                        </limits>
                    </rule>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### Generate Coverage Report

```bash
# Run tests with coverage
mvn clean test jacoco:report

# View report
open target/site/jacoco/index.html
```

### Playwright HTML Report

```bash
# Run tests with HTML report
npx playwright test --reporter=html

# View report
npx playwright show-report
```

---

## CI/CD Integration

### GitHub Actions Workflow

The `.github/workflows/warehousing-ci-cd.yml` includes:

1. **Unit Tests** - Run on every push
2. **Integration Tests** - Run after unit tests pass
3. **E2E Tests** - Run on deployment to staging
4. **Coverage Upload** - Upload to Codecov

### Test Commands in CI

```bash
# Unit tests with coverage
mvn clean test jacoco:report

# Integration tests
mvn verify -DskipUnitTests

# E2E tests (in staging)
cd e2e && npm test --project=api-tests
```

### Test Results

- **JUnit XML** - Stored as `test-results-junit.xml`
- **Coverage** - Stored as `coverage.xml` for Codecov
- **Playwright** - HTML report uploaded as artifact

---

## Best Practices

### Unit Tests

1. **Test behavior, not implementation**
2. **Use descriptive test names** (should..., when...)
3. **Arrange-Act-Assert pattern**
4. **One assertion per test** (logical assertion)
5. **Mock external dependencies**

### Integration Tests

1. **Test API contracts**
2. **Test database interactions**
3. **Test message publishing/consuming**
4. **Use Testcontainers for external services**
5. **Clean up test data after tests**

### E2E Tests

1. **Test critical user flows only**
2. **Use Page Object Model for UI tests**
3. **Make tests idempotent**
4. **Use realistic test data**
5. **Parallelize when possible**

### Test Organization

```java
// Good - Descriptive and organized
@Test
@DisplayName("Should throw exception when adjusting quantity below zero")
void shouldThrowWhenAdjustingBelowZero() { }

// Bad - Vague
@Test
void test7() { }
```

---

## Performance Testing

### JMeter Test Plan

```xml
<jmeterTestPlan>
  <hashTree>
    <TestPlan>
      <stringProp name="TestPlan.comments">Warehousing API Load Test</stringProp>
    </TestPlan>
    <hashTree>
      <ThreadGroup>
        <stringProp name="ThreadGroup.num_threads">100</stringProp>
        <stringProp name="ThreadGroup.ramp_time">10</stringProp>
      </ThreadGroup>
      <hashTree>
        <HTTPSamplerProxy>
          <stringProp name="HTTPSampler.domain">api.warehousing.gogidix.com</stringProp>
          <stringProp name="HTTPSampler.path">/api/v1/inventory/items</stringProp>
        </HTTPSamplerProxy>
      </hashTree>
    </hashTree>
  </hashTree>
</jmeterTestPlan>
```

### Load Test Commands

```bash
# Run JMeter test
jmeter -n -t warehousing-load-test.jmx -l results.jtl -e -o report/

# Run with k6
k6 run --vus 100 --duration 5m inventory-load-test.js
```

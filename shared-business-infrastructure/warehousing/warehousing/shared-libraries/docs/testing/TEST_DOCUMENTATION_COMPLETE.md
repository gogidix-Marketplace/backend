# Shared Libraries - Test Documentation

**Module:** gogidix-foundation-shared-libraries  
**Version:** 1.0.0  
**Test Framework:** JUnit 5, Vitest (Frontend)  
**Last Updated:** 2025-10-26

---

## Test Strategy Overview

### Testing Pyramid

```
        /\
       /  \  E2E Tests (10%)
      /----\
     /      \  Integration Tests (30%)
    /--------\
   /          \ Unit Tests (60%)
  /____________\
```

### Test Coverage Goals
- **Unit Tests**: 80% code coverage
- **Integration Tests**: Critical paths covered
- **E2E Tests**: User journeys covered

---

## 1. Unit Testing

### Frameworks & Tools
- **JUnit 5** - Java unit testing
- **Mockito** - Mocking framework
- **AssertJ** - Fluent assertions
- **Vitest** - Frontend testing (React components)

### Unit Test Structure

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUser() {
        // Given
        User user = User.builder()
            .username("john.doe")
            .email("john.doe@example.com")
            .build();
        
        when(userRepository.save(any(User.class)))
            .thenReturn(user);
        
        // When
        User created = userService.createUser(user);
        
        // Then
        assertThat(created).isNotNull();
        assertThat(created.getUsername()).isEqualTo("john.doe");
        verify(userRepository).save(user);
    }
}
```

---

## 2. Integration Testing

### TestContainers Integration

```java
@SpringBootTest
@Testcontainers
class UserIntegrationTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = 
        new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    
    @Container
    static GenericContainer<?> redis = 
        new GenericContainer<>("redis:7")
            .withExposedPorts(6379);
    
    @Autowired
    private UserService userService;
    
    @Test
    void shouldPersistAndRetrieveUser() {
        // Test with real database
        User user = userService.createUser(
            new User("john.doe", "john@example.com")
        );
        
        User retrieved = userService.findById(user.getId());
        assertThat(retrieved).isEqualTo(user);
    }
}
```

---

## 3. Library-Specific Test Strategies

### shared-model Tests
**Focus**: Domain model integrity

```java
@Test
void baseEntityShouldHaveRequiredFields() {
    User user = new User();
    user.setId("user-123");
    user.setCreatedAt(LocalDateTime.now());
    
    assertThat(user.getId()).isNotNull();
    assertThat(user.getCreatedAt()).isNotNull();
    assertThat(user.getStatus()).isEqualTo(EntityStatus.DRAFT);
}

@Test
void entityStatusTransitionShouldBeValid() {
    User user = new User();
    user.setStatus(EntityStatus.DRAFT);
    
    user.setStatus(EntityStatus.ACTIVE); // Valid transition
    assertThat(user.getStatus()).isEqualTo(EntityStatus.ACTIVE);
    
    // Invalid transition should throw exception
    assertThatThrownBy(() -> user.setStatus(EntityStatus.DELETED))
        .isInstanceOf(IllegalStateException.class);
}
```

---

### shared-security Tests
**Focus**: Authentication & Authorization

```java
@Test
void shouldGenerateValidJWT() {
    User user = new User("john.doe", "john@example.com");
    user.setRoles(List.of(new Role("USER")));
    
    String token = jwtProvider.generateToken(user);
    
    assertThat(token).isNotNull();
    assertThat(jwtProvider.validateToken(token)).isTrue();
    assertThat(jwtProvider.getUsernameFromToken(token))
        .isEqualTo("john.doe");
}

@Test
void shouldEnforceRoleBasedAccess() {
    User user = new User("john.doe", "john@example.com");
    user.setRoles(List.of(new Role("USER")));
    
    assertThat(rbacService.hasPermission(user, "read:profile"))
        .isTrue();
    assertThat(rbacService.hasPermission(user, "admin:users"))
        .isFalse();
}

@Test
void shouldHashPasswordSecurely() {
    String password = "SecurePass123!";
    String hashed = passwordEncoder.encode(password);
    
    assertThat(hashed).isNotEqualTo(password);
    assertThat(passwordEncoder.matches(password, hashed)).isTrue();
}
```

---

### shared-validation Tests
**Focus**: Validation rules

```java
@Test
void shouldValidateEmailFormat() {
    EmailValidator validator = new EmailValidator();
    
    assertThat(validator.isValid("john.doe@example.com")).isTrue();
    assertThat(validator.isValid("invalid-email")).isFalse();
    assertThat(validator.isValid("")).isFalse();
}

@Test
void shouldValidatePhoneNumber() {
    PhoneValidator validator = new PhoneValidator();
    
    assertThat(validator.isValid("+1-555-123-4567", "US")).isTrue();
    assertThat(validator.isValid("123", "US")).isFalse();
}

@Test
void shouldValidateBusinessRules() {
    OrderValidator validator = new OrderValidator();
    Order order = new Order();
    order.setTotalAmount(BigDecimal.valueOf(-100)); // Invalid
    
    ValidationResult result = validator.validate(order);
    
    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors())
        .contains("Total amount must be positive");
}
```

---

### shared-utilities Tests
**Focus**: Utility functions

```java
@Test
void shouldFormatDateCorrectly() {
    LocalDateTime date = LocalDateTime.of(2025, 10, 26, 10, 30);
    
    String formatted = DateUtil.format(date, "yyyy-MM-dd HH:mm");
    
    assertThat(formatted).isEqualTo("2025-10-26 10:30");
}

@Test
void shouldParseJsonSuccessfully() {
    String json = "{\"name\":\"John\",\"age\":30}";
    
    Map<String, Object> parsed = JsonUtil.parse(json);
    
    assertThat(parsed).containsEntry("name", "John");
    assertThat(parsed).containsEntry("age", 30);
}

@Test
void shouldEncryptAndDecrypt() {
    String plaintext = "Sensitive data";
    
    String encrypted = CryptoUtil.encrypt(plaintext);
    String decrypted = CryptoUtil.decrypt(encrypted);
    
    assertThat(decrypted).isEqualTo(plaintext);
    assertThat(encrypted).isNotEqualTo(plaintext);
}
```

---

### shared-messaging Tests
**Focus**: Message handling

```java
@Test
void shouldCreateMessageWithRequiredFields() {
    Message message = Message.builder()
        .messageType(MessageType.EMAIL)
        .content("Test message")
        .recipients(List.of("user@example.com"))
        .build();
    
    assertThat(message.getMessageType()).isEqualTo(MessageType.EMAIL);
    assertThat(message.getStatus()).isEqualTo(MessageStatus.DRAFT);
}

@Test
void shouldValidateMessageBeforeSending() {
    Message message = new Message();
    // Missing required fields
    
    assertThatThrownBy(() -> messagingService.send(message))
        .isInstanceOf(ValidationException.class)
        .hasMessageContaining("Recipients required");
}
```

---

### shared-audit Tests
**Focus**: Audit logging

```java
@Test
void shouldRecordAuditEvent() {
    AuditEvent event = AuditEvent.builder()
        .eventType("USER_LOGIN")
        .userId("user-123")
        .action("login")
        .status("SUCCESS")
        .build();
    
    auditService.record(event);
    
    verify(auditRepository).save(event);
}

@Test
void shouldQueryAuditEvents() {
    LocalDateTime startDate = LocalDateTime.now().minusDays(7);
    LocalDateTime endDate = LocalDateTime.now();
    
    List<AuditEvent> events = auditService.findEvents(
        "user-123",
        startDate,
        endDate
    );
    
    assertThat(events).isNotEmpty();
}
```

---

### shared-exceptions Tests
**Focus**: Exception hierarchy

```java
@Test
void shouldCreateBusinessException() {
    BusinessException ex = new BusinessException(
        "Invalid operation",
        "BUSINESS_ERROR"
    );
    
    assertThat(ex.getMessage()).isEqualTo("Invalid operation");
    assertThat(ex.getErrorCode()).isEqualTo("BUSINESS_ERROR");
    assertThat(ex.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
}

@Test
void shouldIncludeContextInException() {
    Map<String, Object> context = Map.of(
        "userId", "user-123",
        "operation", "deleteUser"
    );
    
    ResourceNotFoundException ex = new ResourceNotFoundException(
        "User",
        "user-123",
        context
    );
    
    assertThat(ex.getContext()).containsEntry("userId", "user-123");
}
```

---

### shared-testing Tests
**Focus**: Test utilities

```java
@Test
void shouldGenerateMockData() {
    User user = MockDataGenerator.user()
        .withUsername("john.doe")
        .withEmail("john@example.com")
        .build();
    
    assertThat(user.getUsername()).isEqualTo("john.doe");
    assertThat(user.getId()).isNotNull();
}

@Test
void shouldProvideTestFixtures() {
    User user = TestFixtures.standardUser();
    
    assertThat(user).isNotNull();
    assertThat(user.getRoles()).contains(new Role("USER"));
}
```

---

### gogidix-ui-library Tests (Frontend)
**Focus**: React component testing

```typescript
import { render, screen, fireEvent } from '@testing-library/react';
import { Button } from '@gogidix/ui-library';

describe('Button Component', () => {
  it('renders button with text', () => {
    render(<Button>Click Me</Button>);
    expect(screen.getByText('Click Me')).toBeInTheDocument();
  });

  it('calls onClick handler when clicked', () => {
    const handleClick = vi.fn();
    render(<Button onClick={handleClick}>Click Me</Button>);
    
    fireEvent.click(screen.getByText('Click Me'));
    expect(handleClick).toHaveBeenCalledTimes(1);
  });

  it('applies correct variant styling', () => {
    render(<Button variant="contained">Button</Button>);
    const button = screen.getByRole('button');
    expect(button).toHaveClass('MuiButton-contained');
  });
});
```

---

## 4. Test Execution

### Running Tests

#### Backend (Maven)
```bash
# Run all tests
mvn test

# Run tests for specific library
cd shared-security
mvn test

# Run tests with coverage
mvn clean test jacoco:report

# Run integration tests only
mvn verify -P integration-tests

# Skip tests
mvn clean install -DskipTests
```

#### Frontend (NPM)
```bash
# Run all tests
npm test

# Run tests in watch mode
npm test -- --watch

# Run tests with coverage
npm run test:coverage

# Run specific test file
npm test Button.test.tsx
```

---

## 5. Test Coverage Reports

### Coverage Goals by Library

| Library | Unit Coverage | Integration Coverage | Overall Target |
|---------|---------------|---------------------|----------------|
| shared-model | 80% | 70% | 75% |
| shared-security | 85% | 80% | 82% |
| shared-validation | 90% | 75% | 85% |
| shared-testing | N/A | N/A | N/A (Test library) |
| shared-audit | 80% | 70% | 75% |
| shared-utilities | 85% | 75% | 80% |
| shared-exceptions | 90% | N/A | 90% |
| shared-messaging | 80% | 70% | 75% |
| gogidix-ui-library | 80% | N/A | 80% |

### Generating Coverage Reports

#### JaCoCo (Java)
```bash
mvn clean test jacoco:report
# Report: target/site/jacoco/index.html
```

#### Vitest (Frontend)
```bash
npm run test:coverage
# Report: coverage/index.html
```

---

## 6. Continuous Integration

### GitHub Actions / GitLab CI

```yaml
test:
  stage: test
  script:
    - mvn clean test
    - npm run test:coverage
  coverage: '/Total.*?(\d+\.?\d*)%/'
  artifacts:
    reports:
      junit:
        - '**/target/surefire-reports/TEST-*.xml'
      coverage_report:
        coverage_format: cobertura
        path: target/site/jacoco/jacoco.xml
```

---

## 7. Test Data Management

### Test Database
- **PostgreSQL TestContainer** for integration tests
- **H2 in-memory database** for unit tests
- **Test fixtures** for common scenarios

### Test Data Fixtures
```java
public class TestFixtures {
    public static User standardUser() {
        return User.builder()
            .id("test-user-123")
            .username("testuser")
            .email("test@example.com")
            .status(EntityStatus.ACTIVE)
            .roles(List.of(new Role("USER")))
            .build();
    }
    
    public static User adminUser() {
        return User.builder()
            .id("admin-user-456")
            .username("admin")
            .email("admin@example.com")
            .status(EntityStatus.ACTIVE)
            .roles(List.of(new Role("ADMIN")))
            .build();
    }
}
```

---

## 8. Best Practices

### Unit Testing
✅ Test one thing at a time  
✅ Use descriptive test names  
✅ Follow AAA pattern (Arrange, Act, Assert)  
✅ Mock external dependencies  
✅ Keep tests fast (<100ms)

### Integration Testing
✅ Use TestContainers for real dependencies  
✅ Test critical user journeys  
✅ Clean up test data after each test  
✅ Use realistic test data  
✅ Test error scenarios

### General
✅ Maintain test independence  
✅ Don't test framework code  
✅ Keep tests readable  
✅ Review test coverage regularly  
✅ Fix flaky tests immediately

---

## 9. Test Utilities (shared-testing)

### Available Test Utilities
- **MockDataGenerator**: Generate test data
- **TestFixtures**: Predefined test objects
- **CustomAssertions**: Domain-specific assertions
- **TestContainers Support**: Database, Redis, Kafka
- **Performance Test Utilities**: Load testing helpers

---

## 10. Test Execution Summary

### Quick Test Commands

```bash
# Test all backend libraries
for dir in shared-*/; do
    cd "$dir"
    mvn test
    cd ..
done

# Test frontend library
cd frontend/web/gogidix-ui-library
npm test
```

---

**Status:** ✅ Test Documentation Complete  
**Coverage:** All 9 libraries documented  
**Last Updated:** 2025-10-26

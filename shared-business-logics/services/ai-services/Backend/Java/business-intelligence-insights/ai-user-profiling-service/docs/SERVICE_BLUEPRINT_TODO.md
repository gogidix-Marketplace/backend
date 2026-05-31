# AI User Profileation Service - Production Blueprint TODO

**Service:** ai-customer-segmentation-service
**Blueprint Version:** 1.0
**Date Created:** 2026-02-10
**Status:** PENDING APPROVAL
**Package:** com.gogidix.aiservices.aicustomersegmentation

---

## MANDATORY STANDARDS (All Services MUST Follow)

| Standard | Value |
|----------|-------|
| Spring Boot | 3.1.5 |
| Java | 17+ |
| Maven | 3.9.12 |
| Package | com.gogidix.aiservices.{service-camelcase} |
| Architecture | Hexagonal (Ports & Adapters) |
| Test Coverage | >= 70% |
| Build Tool | Maven 3.9.12 |
| Database | MongoDB (primary), Redis (cache) |
| Messaging | Kafka |
| API Documentation | OpenAPI 3.0 |

---

## SERVICE ACCEPTANCE CRITERIA

Before marking any service as PRODUCTION READY, ALL of the following MUST be verified:

- [ ] `mvn clean compile` executes without errors
- [ ] `mvn clean package -DskipTests` creates executable JAR
- [ ] `mvn test` - All unit tests pass
- [ ] `mvn jacoco:report` - Coverage >= 70%
- [ ] `mvn verify` - Integration tests pass
- [ ] Smoke tests pass (health check, application startup)
- [ ] No empty folders exist in the entire project
- [ ] Docker image builds successfully
- [ ] GitHub workflows execute successfully
- [ ] Documentation is complete

---

## FOLDER STRUCTURE (MANDATORY FOR ALL SERVICES)

```
service-name/
├── pom.xml                          (Maven configuration)
├── Dockerfile                       (Container build)
├── railway.json                     (Deployment config)
├── .github/
│   └── workflows/
│       ├── build.yml
│       ├── test.yml
│       └── deploy.yml
├── docs/                            (Mandatory documentation)
│   ├── SERVICE_BLUEPRINT_TODO.md   (this file)
│   ├── api-specification.md
│   └── runbook.md
└── src/
    ├── main/
    │   ├── java/com/gogidix/aiservices/{service}/
    │   │   ├── {Service}Application.java
    │   │   │
    │   │   ├── domain/
    │   │   │   ├── model/           # Domain entities (ZERO framework deps)
    │   │   │   ├── aggregate/       # DDD Aggregates
    │   │   │   ├── event/           # Domain Events
    │   │   │   ├── policy/          # Business Rules
    │   │   │   ├── repository/      # Repository Interfaces
    │   │   │   └── port/
    │   │   │       ├── in/          # Input Ports (Commands/Queries)
    │   │   │       └── out/         # Output Ports
    │   │   │
    │   │   ├── application/
    │   │   │   ├── command/         # Command DTOs
    │   │   │   ├── query/           # Query DTOs
    │   │   │   ├── service/         # Application Services
    │   │   │   ├── dto/
    │   │   │   │   ├── request/
    │   │   │   │   └── response/
    │   │   │   └── mapper/          # MapStruct Mappers
    │   │   │
    │   │   ├── infrastructure/
    │   │   │   ├── persistence/
    │   │   │   │   ├── mongo/       # MongoDB Implementation
    │   │   │   │   └── redis/       # Redis Cache
    │   │   │   ├── messaging/
    │   │   │   │   ├── kafka/
    │   │   │   │   └── events/
    │   │   │   ├── security/        # JWT, TenantInterceptor
    │   │   │   ├── adapter/         # External Adapters
    │   │   │   │   ├── rest/
    │   │   │   │   └── storage/
    │   │   │   └── config/
    │   │   │
    │   │   ├── interfaces/
    │   │   │   ├── rest/            # REST Controllers
    │   │   │   └── dto/
    │   │   │
    │   │   └── shared/
    │   │       ├── requestcontext/  # RequestContext, Holder
    │   │       ├── exception/       # Custom Exceptions
    │   │       └── util/
    │   │
    │   └── resources/
    │       ├── application.yml
    │       ├── application-dev.yml
    │       ├── application-test.yml
    │       ├── application-prod.yml
    │       ├── logback-spring.xml
    │       └── META-INF/openapi/
    │           └── openapi.yaml
    │
    └── test/
        ├── java/com/gogidix/aiservices/{service}/
        │   ├── unit/
        │   │   ├── domain/
        │   │   ├── application/
        │   │   ├── infrastructure/
        │   │   └── interfaces/
        │   ├── integration/
        │   └── architecture/
        └── resources/
            └── fixtures/
```

---

## IMPLEMENTATION PHASES (20 Tasks - MANDATORY ORDER)

### PHASE 1: Project Configuration (pom.xml)

**Task:** Update pom.xml with production-ready dependencies

**Dependencies to Add:**
- [ ] spring-boot-starter-data-mongodb
- [ ] spring-boot-starter-data-redis
- [ ] spring-kafka
- [ ] mapstruct (1.5.5.Final)
- [ ] springdoc-openapi-starter-webmvc-ui (2.3.0)
- [ ] jacoco-maven-plugin
- [ ] archunit (test)
- [ ] testcontainers (mongodb, kafka - test)

**Build Plugins:**
- [ ] spring-boot-maven-plugin
- [ ] jacoco-maven-plugin (coverage reporting)
- [ ] maven-surefire-plugin (test execution)
- [ ] maven-failsafe-plugin (integration tests)

**Profiles:**
- [ ] dev
- [ ] test
- [ ] prod

---

### PHASE 2: Application Configuration Files

**Files to Create:**

- [ ] `src/main/resources/application.yml`
  - Server configuration (port: 8080, context-path: /api/v1)
  - Spring application name
  - MongoDB configuration
  - Redis configuration
  - Kafka configuration
  - Actuator endpoints (health, info, metrics, prometheus)
  - OpenAPI configuration
  - Logging pattern with tenantId, traceId, spanId

- [ ] `src/main/resources/application-dev.yml`
- [ ] `src/main/resources/application-test.yml`
- [ ] `src/main/resources/application-prod.yml`
- [ ] `src/main/resources/logback-spring.xml`
- [ ] `src/main/resources/META-INF/openapi/openapi.yaml`

---

### PHASE 3: Shared Layer Components

**Package:** `com.gogidix.aiservices.aicustomersegmentation.shared`

- [ ] `requestcontext/RequestContext.java` - Immutable context holder (tenantId, userId, correlationId, metadata)
- [ ] `requestcontext/RequestContextHolder.java` - ThreadLocal holder with get(), set(), clear(), require()
- [ ] `requestcontext/RequestContextFilter.java` - HTTP filter for context extraction
- [ ] `exception/BaseDomainException.java` - Base exception class
- [ ] `exception/NotFoundException.java`
- [ ] `exception/ValidationException.java`
- [ ] `exception/ConflictException.java`
- [ ] `exception/BusinessException.java`
- [ ] `exception/ErrorResponse.java` - Error response DTO
- [ ] `util/` - Utility classes as needed

---

### PHASE 4: Domain Layer - Model

**Package:** `com.gogidix.aiservices.aicustomersegmentation.domain`

**model/ (ZERO framework dependencies):**
- [ ] `UserProfile.java`
  - Fields: id, tenantId, name, description, criteria, customerCount, status, createdAt, updatedAt
  - Methods: activate(), deactivate(), addCriteria(), removeCriteria(), validate()
  - Annotations: @Document, @TypeAlias, @Id, @Indexed, @Field

- [ ] `ProfileCriteria.java` (Value Object)
  - Fields: type, operator, value, logicalOperator

- [ ] `UserProfile.java` (Value Object)
  - Fields: customerId, tenantId, attributes, lastActivityDate

- [ ] `ProfileType.java` (Enum)
  - Values: BEHAVIORAL, DEMOGRAPHIC, TRANSACTIONAL, CUSTOM

**aggregate/:
- [ ] `UserProfileAggregate.java` - DDD Aggregate Root

**event/:
- [ ] `DomainEvent.java` (base interface)
- [ ] `ProfileCreatedEvent.java`
- [ ] `ProfileUpdatedEvent.java`
- [ ] `ProfileDeletedEvent.java`
- [ ] `UsersAddedToProfileEvent.java`

---

### PHASE 5: Domain Layer - Policies & Ports

**policy/:
- [ ] `MaxProfilesPerTenantPolicy.java` - Business rule: max segments per tenant
- [ ] `ProfileNameUniquePolicy.java` - Business rule: unique segment name per tenant
- [ ] `MinimumUserCountPolicy.java` - Business rule: minimum customers for activation

**repository/ (Interfaces only):
- [ ] `UserProfileRepository.java`
- [ ] `ProfileCriteriaRepository.java`

**port/in/:
- [ ] `CreateProfileCommand.java`
- [ ] `UpdateProfileCommand.java`
- [ ] `DeleteProfileCommand.java`
- [ ] `GetProfileQuery.java`
- [ ] `ListProfilesQuery.java`
- [ ] `ProfileAnalysisQuery.java`

**port/out/:
- [ ] `UserProfileRepositoryPort.java`
- [ ] `ExternalUserDataProviderPort.java`
- [ ] `ProfileEventPublisherPort.java`

---

### PHASE 6: Application Layer - Commands & DTOs

**Package:** `com.gogidix.aiservices.aicustomersegmentation.application`

**command/:
- [ ] `CreateUserProfileCommand.java`
- [ ] `UpdateUserProfileCommand.java`
- [ ] `DeleteUserProfileCommand.java`
- [ ] `AnalyzeUserProfilesCommand.java`
- [ ] `AddUsersToProfileCommand.java`
- [ ] `RemoveUsersFromProfileCommand.java`

**query/:
- [ ] `GetUserProfileQuery.java`
- [ ] `ListUserProfilesQuery.java`
- [ ] `SearchProfilesQuery.java`

**dto/request/:
- [ ] `CreateProfileRequestDto.java` (@Valid, validation annotations)
- [ ] `UpdateProfileRequestDto.java`
- [ ] `ProfileSearchRequestDto.java`
- [ ] `AnalyzeProfileRequestDto.java`

**dto/response/:
- [ ] `UserProfileResponseDto.java`
- [ ] `ProfileAnalysisResponseDto.java`
- [ ] `PagedResponseDto.java` (generic)
- [ ] `ErrorResponseDto.java`

---

### PHASE 7: Application Layer - Services & Mappers

**service/:
- [ ] `UserProfileCommandService.java` (@Service, @Transactional)
  - handle(CreateUserProfileCommand)
  - handle(UpdateUserProfileCommand)
  - handle(DeleteUserProfileCommand)

- [ ] `UserProfileQueryService.java` (@Service, @ReadOnly)
  - execute(GetUserProfileQuery)
  - execute(ListUserProfilesQuery)

- [ ] `ProfileAnalysisService.java` (@Service)
  - analyze(AnalyzeUserProfilesCommand)

- [ ] `ProfileOrchestrationService.java` (@Service)
  - orchestrateProfileCreation()
  - orchestrateProfileUpdate()

**mapper/:
- [ ] `UserProfileMapper.java` (@Mapper, MapStruct)
- [ ] `ProfileCriteriaMapper.java`

---

### PHASE 8: Infrastructure Layer - Persistence

**Package:** `com.gogidix.aiservices.aicustomersegmentation.infrastructure`

**persistence/mongo/:
- [ ] `MongoUserProfileRepository.java` (@Repository, implements port)
  - All queries MUST filter by tenantId
  - save(), findById(), findAll(), deleteById(), findByTenantId()

- [ ] `UserProfileDocument.java` (@Document, MongoDB schema)
  - tenantId indexed (compound with id)

- [ ] `ProfileDocumentConverter.java`

**persistence/redis/:
- [ ] `RedisProfileCacheStore.java`
- [ ] `ProfileCacheConfig.java`

**config/:
- [ ] `ApplicationConfig.java`
- [ ] `MongoDBConfig.java`
- [ ] `RedisConfig.java`
- [ ] `KafkaConfig.java`
- [ ] `WebConfig.java` (CORS, etc.)

---

### PHASE 9: Infrastructure Layer - Messaging & Security

**messaging/kafka/:
- [ ] `KafkaProfileEventPublisher.java` (@Component)
- [ ] `KafkaProfileEventConsumer.java` (@KafkaListener)
- [ ] Kafka configuration (topics, partitions)

**messaging/events/:
- [ ] `ProfileEventPublisher.java` (interface)

**security/:
- [ ] `TenantInterceptor.java` (@Component, @Order(HIGHEST_PRECEDENCE))
  - Extract tenant from JWT
  - Validate tenant exists
  - Set RequestContext
  - Clear after completion

- [ ] `SecurityConfig.java`
- [ ] `JwtAuthenticationFilter.java`

**adapter/rest/:
- [ ] `ExternalUserApiClient.java` (if needed)

**adapter/storage/:
- [ ] `S3StorageAdapter.java` (if needed)

---

### PHASE 10: Interfaces Layer - REST Controllers

**Package:** `com.gogidix.aiservices.aicustomersegmentation.interfaces`

**rest/:
- [ ] `UserProfileController.java`
  - POST /api/v1/segments - Create segment
  - GET /api/v1/segments - List all segments (paginated)
  - GET /api/v1/segments/{id} - Get segment by ID
  - PUT /api/v1/segments/{id} - Update segment
  - DELETE /api/v1/segments/{id} - Delete segment
  - POST /api/v1/segments/{id}/analyze - Analyze segment
  - POST /api/v1/segments/{id}/customers - Add customers
  - DELETE /api/v1/segments/{id}/customers - Remove customers

  Annotations: @RestController, @RequestMapping, @Tag, @SecurityRequirement, @CrossOrigin

- [ ] `HealthController.java` - Actuator health check
- [ ] `GlobalExceptionHandler.java` (@RestControllerAdvice)
  - handleNotFound()
  - handleValidation()
  - handleConflict()
  - handleGeneric()

**dto/:
- [ ] `ApiErrorResponse.java`

---

### PHASE 11: CI/CD (GitHub Workflows)

**Folder:** `.github/workflows/`

- [ ] `build.yml`
  - Checkout code
  - Setup JDK 17
  - Cache Maven dependencies
  - mvn clean compile
  - Upload artifact

- [ ] `test.yml`
  - mvn clean test
  - mvn jacoco:report
  - Verify coverage >= 70%
  - Upload coverage to Codecov/GitHub

- [ ] `deploy.yml`
  - mvn clean package
  - Build and push Docker image
  - Deploy to staging/production

---

### PHASE 12: Container & Deployment

**Service Root Files:**

- [ ] `Dockerfile` (multi-stage build)
  - Stage 1: Maven builder
  - Stage 2: JRE runtime
  - Health check
  - Non-root user

- [ ] `railway.json`
  - Build configuration
  - Health check path
  - Restart policy

- [ ] `.dockerignore`

- [ ] Verify: `mvn clean package -DskipTests` produces `target/*.jar`

---

### PHASE 13: Unit Tests - Domain Layer

**Folder:** `src/test/java/.../unit/domain/`

- [ ] `UserProfileTest.java`
  - testCreateProfile()
  - testActivateProfile()
  - testDeactivateProfile()
  - testAddCriteria()
  - testRemoveCriteria()
  - testValidation()
  - **Coverage target: 90%+**

- [ ] `ProfileCriteriaTest.java`
- [ ] `MaxProfilesPerTenantPolicyTest.java`
- [ ] `ProfileNameUniquePolicyTest.java`
- [ ] `ProfileCreatedEventTest.java`

---

### PHASE 14: Unit Tests - Application Layer

**Folder:** `src/test/java/.../unit/application/`

- [ ] `UserProfileCommandServiceTest.java`
  - testCreateProfile()
  - testUpdateProfile()
  - testDeleteProfile()
  - Use @Mock for dependencies

- [ ] `UserProfileQueryServiceTest.java`
- [ ] `ProfileAnalysisServiceTest.java`
- [ ] **Coverage target: 80%+**

---

### PHASE 15: Unit Tests - Infrastructure Layer

**Folder:** `src/test/java/.../unit/infrastructure/`

- [ ] `MongoUserProfileRepositoryTest.java`
  - Test CRUD operations
  - Test tenant filtering
  - Use @Mock for MongoTemplate

- [ ] `RedisProfileCacheStoreTest.java`
- [ ] `KafkaProfileEventPublisherTest.java`
- [ ] `TenantInterceptorTest.java`
- [ ] **Coverage target: 70%+**

---

### PHASE 16: Unit Tests - Interfaces Layer

**Folder:** `src/test/java/.../unit/interfaces/`

- [ ] `UserProfileControllerTest.java`
  - Use MockMvc
  - testCreateEndpoint()
  - testGetEndpoint()
  - testListEndpoint()
  - testUpdateEndpoint()
  - testDeleteEndpoint()
  - Test error scenarios

- [ ] `GlobalExceptionHandlerTest.java`
- [ ] **Coverage target: 80%+**

---

### PHASE 17: Integration Tests

**Folder:** `src/test/java/.../integration/`

- [ ] `UserProfileIntegrationTest.java`
  - @SpringBootTest
  - Testcontainers for MongoDB, Redis, Kafka
  - Test full CRUD flow
  - Test event publishing

- [ ] `TenantIsolationTest.java`
  - CRITICAL: Verify tenant data isolation
  - Test cross-tenant access prevention

- [ ] `ProfileAnalysisIntegrationTest.java`

---

### PHASE 18: Architecture & Smoke Tests

**Folder:** `src/test/java/.../architecture/`

- [ ] `HexArchitectureTest.java` (ArchUnit)
  - Domain layer has no framework dependencies
  - Ports interfaces rules
  - Dependency direction rules
  - No cyclic dependencies

**Smoke Tests:**
- [ ] `HealthCheckTest.java`
  - GET /actuator/health returns 200

- [ ] `ApplicationStartupTest.java`
  - @SpringBootTest loads context successfully

**Fixtures:**
- [ ] `src/test/resources/fixtures/customer-segment-test-data.json`
- [ ] `src/test/resources/fixtures/segment-criteria-test-data.json`

---

### PHASE 19: Documentation

**Folder:** `docs/`

- [ ] `api-specification.md`
  - All endpoints
  - Request/response examples
  - Error codes
  - Authentication details

- [ ] `runbook.md`
  - How to deploy
  - Environment variables
  - How to run locally
  - Troubleshooting guide

- [ ] `README.md`
  - Service overview
  - Prerequisites
  - Setup instructions
  - Build & run commands

---

### PHASE 20: Build & Verification

**Execute and Verify:**

1. [ ] `mvn clean compile` - Must complete without errors
2. [ ] `mvn clean package -DskipTests` - Must create `target/*.jar`
3. [ ] `mvn test` - All tests must pass
4. [ ] `mvn jacoco:report` - Coverage >= 70%
5. [ ] `mvn verify` - Integration tests pass
6. [ ] Verify JAR exists and is executable
7. [ ] Run smoke tests (health check)
8. [ ] Verify no empty folders exist
9. [ ] Build Docker image successfully
10. [ ] Run GitHub workflows successfully

---

## CERTIFICATION CHECKLIST

Upon completion of ALL 20 phases, certify below:

**Developer Certification:**
- [ ] All phases completed
- [ ] All acceptance criteria met
- [ ] Code reviewed
- [ ] Tests passing with >= 70% coverage
- [ ] Documentation complete

**Signature:** ____________________ **Date:** ________

**Tech Lead Certification:**
- [ ] Architecture review passed
- [ ] Security review passed
- [ ] Performance review passed
- [ ] Production readiness confirmed

**Signature:** ____________________ **Date:** ________

---

## BLUEPRINT VERSION CONTROL

| Version | Date | Changes | Author |
|---------|------|---------|--------|
| 1.0 | 2026-02-10 | Initial blueprint creation | Claude |

---

**THIS BLUEPRINT IS MANDATORY FOR ALL AI SERVICES IN THE FOUNDATION DOMAIN**

**Any deviation requires explicit approval from the Architecture Team.**

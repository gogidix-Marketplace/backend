# AGENT S2 - LEAD MANAGEMENT SERVICE

**Agent ID:** S2
**Domain:** Sales-Departments
**Service:** lead-management-service
**Priority:** P0 (CRITICAL)

---

## 🎯 SERVICE PURPOSE

Implement a comprehensive lead management system for tracking and managing sales leads through the pipeline. This service handles:
- Lead capture and creation
- Lead qualification and scoring
- Lead pipeline stage management
- Lead assignment to sales reps
- Lead conversion tracking

---

## 📋 DOMAIN MODEL SPECIFICATION

### Entity 1: Lead
```java
@Document(collection = "leads")
public class Lead extends BaseEntity {
    private String leadNumber;             // e.g., "LEAD-2024-001234"
    private String contactName;            // Full name
    private String email;
    private String phone;
    private String company;
    private String industry;
    private String title;                  // Job title
    private LeadSource source;             // WEBSITE, REFERRAL, EVENT, OUTREACH
    private LeadStatus status;             // NEW, CONTACTED, QUALIFIED, PROPOSAL, WON, LOST
    private Integer score;                 // Lead quality score (0-100)
    private Double estimatedValue;         // Potential deal value
    private String currency;
    private String assignedTo;             // Sales rep user ID
    private String assignedToName;
    private LocalDate assignedDate;
    private LeadStage stage;               // AWARENESS, CONSIDERATION, DECISION
    private Integer probability;           // Win probability %
    private LocalDate expectedCloseDate;
    private String country;                // ISO country code
    private String region;
    private List<LeadActivity> activities;  // Interaction history
    private Map<String, Object> metadata;

    // Business logic
    public void qualify(Integer score);
    public void disqualify(String reason);
    public void convertToOpportunity();
    public void assignTo(String salesRepId);
    public void updateStage(LeadStage newStage);
    public boolean isHot();               // Score > 70
    public boolean isCold();              // Score < 30
}
```

### Entity 2: LeadActivity
```java
@Document(collection = "lead_activities")
public class LeadActivity extends BaseEntity {
    private String leadId;                 // Parent lead
    private ActivityType type;            // CALL, EMAIL, MEETING, NOTE
    private String description;
    private String performedBy;           // User who did it
    private Instant activityDate;
    private String outcome;               // INTERESTED, NOT_INTERESTED, FOLLOW_UP
    private LocalDate followUpDate;
    private String notes;

    // Business logic
    public boolean requiresFollowUp();
}
```

### Entity 3: LeadScoreRule
```java
@Document(collection = "lead_score_rules")
public class LeadScoreRule extends BaseEntity {
    private String ruleName;               // e.g., "High Value Industry Rule"
    private String criteriaField;         // industry, companySize, title
    private String criteriaValue;         // Technology, 500+, CEO
    private Integer points;                // Score to add
    private boolean enabled;

    // Business logic
    public boolean matches(Lead lead);
    public Integer calculateScore(Lead lead);
}
```

---

## 🔌 API ENDPOINTS REQUIRED

### Lead Management (28 endpoints)

#### POST /api/v1/leads
- Create new lead

#### GET /api/v1/leads
- Get all leads (paginated, filterable)

#### GET /api/v1/leads/{id}
- Get lead by ID

#### PUT /api/v1/leads/{id}
- Update lead

#### DELETE /api/v1/leads/{id}
- Delete lead

#### POST /api/v1/leads/{id}/qualify
- Qualify lead

#### POST /api/v1/leads/{id}/disqualify
- Disqualify lead

#### POST /api/v1/leads/{id}/assign
- Assign lead to sales rep

#### GET /api/v1/leads/by-status/{status}
- Get leads by status

#### GET /api/v1/leads/by-stage/{stage}
- Get leads by pipeline stage

#### GET /api/v1/leads/hot
- Get hot leads (score > 70)

#### GET /api/v1/leads/cold
- Get cold leads (score < 30)

#### GET /api/v1/leads/unassigned
- Get unassigned leads

#### POST /api/v1/leads/{id}/convert
- Convert lead to opportunity

### Lead Scoring (8 endpoints)

#### POST /api/v1/leads/{id}/score
- Manually score lead

#### GET /api/v1/score-rules
- Get all scoring rules

#### POST /api/v1/score-rules
- Create scoring rule

#### POST /api/v1/leads/bulk-score
- Score all leads

### Lead Activities (10 endpoints)

#### POST /api/v1/leads/{id}/activities
- Add activity to lead

#### GET /api/v1/leads/{id}/activities
- Get lead activities

#### GET /api/v1/leads/{id}/timeline
- Get lead timeline

### Dashboard (8 endpoints)

#### GET /api/v1/dashboard/sales-rep
- Sales rep dashboard (my leads)

#### GET /api/v1/dashboard/pipeline
- Pipeline dashboard (funnel)

#### GET /api/v1/dashboard/performance
- Lead performance metrics

---

## ✅ ACCEPTANCE CRITERIA

1. **Lead Scoring:**
   - Automatic scoring based on rules
   - Manual override capability
   - Score updates when lead changes

2. **Pipeline Management:**
   - Accurate stage tracking
   - Pipeline velocity calculation
   - Conversion tracking

3. **Multi-Tenancy:**
   - Each tenant's leads isolated
   - Sales reps see only their tenant's leads

4. **Assignment:**
   - Round-robin assignment
   - Manual assignment
   - Re-assignment capability

---

## 🔧 CONFIGURATION

```yaml
spring:
  data:
    mongodb:
      database: management_sales
  redis:
    host: localhost
    port: 6379

# Lead management specific
lead:
  default-score: 50
  hot-lead-threshold: 70
  cold-lead-threshold: 30
  auto-assign: true
```

---

## 📁 FILE STRUCTURE TO CREATE

```
lead-management-service/
├── pom.xml
├── src/main/java/
│   ├── LeadManagementServiceApplication.java
│   ├── domain/
│   │   ├── model/
│   │   │   ├── BaseEntity.java
│   │   │   ├── Lead.java
│   │   │   ├── LeadActivity.java
│   │   │   └── LeadScoreRule.java
│   │   ├── repository/
│   │   │   ├── BaseRepository.java
│   │   │   ├── LeadRepository.java
│   │   │   ├── LeadActivityRepository.java
│   │   │   └── LeadScoreRuleRepository.java
│   │   └── enums/
│   │       ├── LeadStatus.java
│   │       ├── LeadSource.java
│   │       └── LeadStage.java
│   ├── application/
│   │   ├── command/
│   │   │   ├── LeadCommandService.java
│   │   │   ├── LeadScoringService.java
│   │   │   └── LeadAssignmentService.java
│   │   ├── query/
│   │   │   └── LeadQueryService.java
│   │   └── dto/
│   │       ├── CreateLeadRequest.java
│   │       ├── UpdateLeadRequest.java
│   │       └── LeadDTO.java
│   ├── infrastructure/
│   │   ├── config/
│   │   │   ├── MongoDBConfig.java
│   │   │   ├── RedisConfig.java
│   │   │   └── SecurityConfig.java
│   │   └── security/
│   │       └── TenantContextFilter.java
│   ├── interfaces/
│   │   └── rest/
│   │       └── LeadController.java
│   └── shared/
│       └── requestcontext/
│           ├── RequestContext.java
│           └── RequestContextHolder.java
└── src/main/resources/
    └── application.yml
```

---

## 🧪 TESTS REQUIRED

- LeadTest (18+ tests)
- LeadActivityTest (12+ tests)
- LeadScoringServiceTest (15+ tests)
- LeadAssignmentServiceTest (12+ tests)
- LeadControllerIntegrationTest (15+ tests)
- TenantIsolationTest (CRITICAL)
- HexagonalArchitectureTest (15 rules)

---

**Gold Standard Reference:** `../../Executive-domain/Backend/Java/executive-dashboard-service/Shared/executive-analytics-service`
**Base Path:** `Sales-Departments/Backend/Java/sales-service/Country/lead-management-service`

**READ THESE BEFORE STARTING:**
1. AGENT_INSTRUCTIONS.md
2. PRD_SALES_DEPARTMENTS.md
3. Gold-standard service (executive-analytics-service)

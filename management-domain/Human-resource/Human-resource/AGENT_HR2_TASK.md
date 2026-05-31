# AGENT HR2 - PAYROLL SERVICE

**Agent ID:** HR2
**Domain:** Human-Resource
**Service:** payroll-service
**Priority:** P0 (CRITICAL)

---

## 🎯 SERVICE PURPOSE

Implement a comprehensive payroll processing service for country-specific payroll operations. This service handles:
- Payroll processing for employees
- Salary calculations
- Tax deductions and compliance
- Payslip generation
- Payment processing integration

---

## 📋 DOMAIN MODEL SPECIFICATION

### Entity 1: Payroll
```java
@Document(collection = "payrolls")
public class Payroll extends BaseEntity {
    private String payrollName;             // e.g., "January 2024 Payroll"
    private String countryCode;            // ISO country code
    private String period;                 // "2024-01"
    private PayrollStatus status;          // DRAFT, PROCESSING, PROCESSED, PAID, CANCELLED
    private LocalDate processingDate;     // When payroll runs
    private LocalDate payDate;            // When employees get paid
    private String currency;              // USD, EUR, NGN, KES, etc.
    private Double totalGrossPay;         // Sum of all gross pay
    private Double totalNetPay;           // Sum of all net pay
    private Double totalTaxes;            // Total tax deductions
    private Double totalDeductions;       // All deductions
    private Integer employeeCount;         // Number of employees
    private List<PayrollEntry> entries;    // Individual employee payroll
    private String approvalStatus;         // APPROVED, PENDING, REJECTED
    private String approvedBy;
    private Instant approvedAt;

    // Business logic
    public void process();
    public void approve(String userId);
    public void calculateTotals();
    public boolean isReadyForProcessing();
}
```

### Entity 2: PayrollEntry
```java
@Document(collection = "payroll_entries")
public class PayrollEntry extends BaseEntity {
    private String payrollId;              // Parent payroll
    private String employeeId;
    private String employeeName;
    private String department;
    private String position;
    private Double grossSalary;           // Before deductions
    private Double basicSalary;
    private Double allowances;
    private Double overtimePay;
    private Double bonuses;
    private Double grossPay;
    private Double taxDeduction;
    private Double pensionDeduction;
    private Double healthInsuranceDeduction;
    private Double otherDeductions;
    private Double totalDeductions;
    private Double netPay;                // Take-home pay
    private String currency;
    private String paymentMethod;          // BANK_TRANSFER, CASH, CHECK
    private String bankAccount;
    private String bankName;

    // Business logic
    public void calculateNetPay();
    public Double getTotalEarnings();
    public Double getTotalDeductions();
}
```

### Entity 3: TaxRule
```java
@Document(collection = "tax_rules")
public class TaxRule extends BaseEntity {
    private String countryCode;            // ISO country code
    private String ruleName;
    private TaxType taxType;              // INCOME_TAX, PENSION, SOCIAL_SECURITY
    private Double rate;                   // Percentage
    private Double thresholdMin;          // Apply only above this
    private Double thresholdMax;          // Cap at this amount
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private boolean enabled;

    // Business logic
    public Double calculateTax(Double taxableAmount);
    public boolean isApplicable(Double amount);
}
```

---

## 🔌 API ENDPOINTS REQUIRED

### Payroll Management (25 endpoints)

#### POST /api/v1/payrolls
- Create new payroll

#### GET /api/v1/payrolls
- Get all payrolls (paginated)

#### GET /api/v1/payrolls/{id}
- Get payroll by ID

#### PUT /api/v1/payrolls/{id}
- Update payroll

#### POST /api/v1/payrolls/{id}/process
- Process payroll (calculate all entries)

#### POST /api/v1/payrolls/{id}/approve
- Approve payroll for payment

#### GET /api/v1/payrolls/by-period/{period}
- Get payroll by period

#### GET /api/v1/payrolls/by-country/{countryCode}
- Get payrolls by country

#### POST /api/v1/payrolls/{id}/entries
- Add employee to payroll

#### GET /api/v1/payrolls/{id}/entries
- Get all payroll entries

#### PUT /api/v1/payrolls/{payrollId}/entries/{entryId}
- Update payroll entry

#### DELETE /api/v1/payrolls/{id}
- Delete payroll

### Tax Management (12 endpoints)

#### GET /api/v1/tax-rules
- Get all tax rules

#### POST /api/v1/tax-rules
- Create tax rule

#### GET /api/v1/tax-rules/{countryCode}
- Get tax rules by country

#### PUT /api/v1/tax-rules/{id}
- Update tax rule

#### POST /api/v1/tax-rules/{id}/calculate
- Calculate tax for amount

### Payslip Generation (8 endpoints)

#### POST /api/v1/payrolls/{id}/payslips
- Generate payslips for payroll

#### GET /api/v1/payslips/{payrollId}/{employeeId}
- Get employee payslip

#### GET /api/v1/payslips/{payrollId}/download
- Download all payslips (PDF)

### Dashboard (5 endpoints)

#### GET /api/v1/dashboard/payroll-summary
- Payroll summary dashboard

#### GET /api/v1/dashboard/{period}/statistics
- Statistics for period

---

## ✅ ACCEPTANCE CRITERIA

1. **Payroll Processing:**
   - Correctly calculate gross to net pay
   - Apply country-specific tax rules
   - Handle multiple currencies

2. **Compliance:**
   - Follow country tax regulations
   - Generate audit trail
   - Support approval workflow

3. **Multi-Tenancy:**
   - Each tenant's payroll isolated
   - Country-specific rules per tenant

4. **Accuracy:**
   - Penny-accurate calculations
   - No rounding errors

---

## 🔧 CONFIGURATION

```yaml
spring:
  data:
    mongodb:
      database: management_hr
  redis:
    host: localhost
    port: 6379

# Payroll specific
payroll:
  default-currency: USD
  supported-currencies: USD,EUR,GBP,NGN,KES,ZAR
  processing-timeout-minutes: 30
```

---

## 📁 FILE STRUCTURE TO CREATE

```
payroll-service/
├── pom.xml
├── src/main/java/
│   ├── PayrollServiceApplication.java
│   ├── domain/
│   │   ├── model/
│   │   │   ├── BaseEntity.java
│   │   │   ├── Payroll.java
│   │   │   ├── PayrollEntry.java
│   │   │   ├── TaxRule.java
│   │   │   └── Payslip.java
│   │   ├── repository/
│   │   │   ├── BaseRepository.java
│   │   │   ├── PayrollRepository.java
│   │   │   ├── PayrollEntryRepository.java
│   │   │   └── TaxRuleRepository.java
│   │   └── enums/
│   │       ├── PayrollStatus.java
│   │       └── TaxType.java
│   ├── application/
│   │   ├── command/
│   │   │   ├── PayrollCommandService.java
│   │   │   ├── PayrollProcessingService.java
│   │   │   └── PayslipGenerationService.java
│   │   ├── query/
│   │   │   ├── PayrollQueryService.java
│   │   │   └── TaxRuleQueryService.java
│   │   └── dto/
│   │       ├── CreatePayrollRequest.java
│   │       ├── CreatePayrollEntryRequest.java
│   │       └── PayslipDTO.java
│   ├── infrastructure/
│   │   ├── config/
│   │   │   ├── MongoDBConfig.java
│   │   │   ├── RedisConfig.java
│   │   │   └── SecurityConfig.java
│   │   └── security/
│   │       └── TenantContextFilter.java
│   ├── interfaces/
│   │   └── rest/
│   │       ├── PayrollController.java
│   │       ├── TaxRuleController.java
│   │       └── PayslipController.java
│   └── shared/
│       └── requestcontext/
│           ├── RequestContext.java
│           └── RequestContextHolder.java
└── src/main/resources/
    └── application.yml
```

---

## 🧪 TESTS REQUIRED

- PayrollTest (15+ tests)
- PayrollEntryTest (15+ tests)
- TaxRuleTest (12+ tests)
- PayrollProcessingServiceTest (15+ tests)
- PayrollControllerIntegrationTest (13+ tests)
- TenantIsolationTest (CRITICAL)
- HexagonalArchitectureTest (15 rules)

---

**Gold Standard Reference:** `../../Executive-domain/Backend/Java/executive-dashboard-service/Shared/executive-analytics-service`
**Base Path:** `Human-resource/Backend/Java/human-resource-service/Country/payroll-service`

**READ THESE BEFORE STARTING:**
1. AGENT_INSTRUCTIONS.md
2. PRD_HUMAN_RESOURCE.md
3. Gold-standard service (executive-analytics-service)

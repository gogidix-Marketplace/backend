# AGENT F2 - ACCOUNTS PAYABLE SERVICE

**Agent ID:** F2
**Domain:** Finance-Department
**Service:** accounts-payable-service
**Priority:** P0 (CRITICAL)

---

## 🎯 SERVICE PURPOSE

Implement a comprehensive accounts payable service for managing vendor invoices and payments. This service handles:
- Vendor invoice capture and processing
- Invoice approval workflows
- Payment scheduling
- Vendor management
- Payment tracking

---

## 📋 DOMAIN MODEL SPECIFICATION

### Entity 1: VendorInvoice
```java
@Document(collection = "vendor_invoices")
public class VendorInvoice extends BaseEntity {
    private String invoiceNumber;          // Unique invoice number
    private String vendorId;               // Vendor reference
    private String vendorName;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private Double amount;                 // Invoice amount
    private Double taxAmount;
    private Double totalAmount;
    private String currency;               // USD, EUR, NGN, etc.
    private InvoiceStatus status;         // PENDING, APPROVED, PAID, OVERDUE, CANCELLED
    private String category;               // SERVICES, GOODS, SUBSCRIPTION
    private String description;
    private String department;             // Cost center
    private String approvedBy;
    private Instant approvedAt;
    private PaymentSchedule paymentSchedule;
    private List<InvoiceLineItem> lineItems;
    private String notes;
    private String attachmentUrl;         // PDF scan

    // Business logic
    public void approve(String userId);
    public void reject(String userId, String reason);
    public boolean isOverdue();
    public boolean canSchedulePayment();
}
```

### Entity 2: InvoiceLineItem
```java
@Document(collection = "invoice_line_items")
public class InvoiceLineItem extends BaseEntity {
    private String invoiceId;              // Parent invoice
    private String itemDescription;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private String accountCode;           // GL account
    private String costCenter;
    private String taxCode;
}
```

### Entity 3: Vendor
```java
@Document(collection = "vendors")
public class Vendor extends BaseEntity {
    private String vendorName;
    private String vendorCode;
    private String contactEmail;
    private String contactPhone;
    private String address;
    private String city;
    private String country;
    private String currency;               // Preferred currency
    private VendorPaymentTerms paymentTerms;  // NET30, NET60, etc.
    private String taxId;                 // VAT/Tax ID
    private String bankAccount;
    private String bankName;
    private boolean active;
    private Double creditLimit;

    // Business logic
    public boolean canAcceptInvoice(Double amount);
    public int getPaymentDays();
}
```

### Entity 4: Payment
```java
@Document(collection = "payments")
public class Payment extends BaseEntity {
    private String paymentNumber;          // e.g., "PAY-2024-001"
    private String invoiceId;
    private String vendorId;
    private Double amount;
    private String currency;
    private LocalDate scheduledDate;
    private LocalDate paidDate;
    private PaymentStatus status;          // SCHEDULED, PROCESSING, COMPLETED, FAILED
    private String paymentMethod;          // WIRE, CHECK, ACH
    private String reference;             // Transaction reference
    private String processedBy;

    // Business logic
    public void process();
    public void complete(String reference);
    public void fail(String reason);
}
```

---

## 🔌 API ENDPOINTS REQUIRED

### Invoice Management (25 endpoints)

#### POST /api/v1/invoices
- Create new vendor invoice

#### GET /api/v1/invoices
- Get all invoices (paginated)

#### GET /api/v1/invoices/{id}
- Get invoice by ID

#### PUT /api/v1/invoices/{id}
- Update invoice

#### DELETE /api/v1/invoices/{id}
- Delete invoice

#### POST /api/v1/invoices/{id}/approve
- Approve invoice

#### POST /api/v1/invoices/{id}/reject
- Reject invoice

#### GET /api/v1/invoices/by-status/{status}
- Get invoices by status

#### GET /api/v1/invoices/by-vendor/{vendorId}
- Get invoices by vendor

#### GET /api/v1/invoices/overdue
- Get overdue invoices

#### GET /api/v1/invoices/due-soon
- Get invoices due soon (next 7 days)

### Vendor Management (15 endpoints)

#### GET /api/v1/vendors
- Get all vendors

#### POST /api/v1/vendors
- Create new vendor

#### GET /api/v1/vendors/{id}
- Get vendor by ID

#### PUT /api/v1/vendors/{id}
- Update vendor

#### GET /api/v1/vendors/active
- Get active vendors

#### POST /api/v1/vendors/{id}/deactivate
- Deactivate vendor

### Payment Processing (12 endpoints)

#### POST /api/v1/payments
- Schedule payment

#### GET /api/v1/payments
- Get all payments

#### GET /api/v1/payments/{id}
- Get payment by ID

#### POST /api/v1/payments/{id}/process
- Process payment

#### GET /api/v1/payments/status/{status}
- Get payments by status

### Dashboard (8 endpoints)

#### GET /api/v1/dashboard/ap-summary
- Accounts payable summary

#### GET /api/v1/dashboard/cash-requirement
- Cash requirement forecast

#### GET /api/v1/dashboard/aging
- Invoice aging report

---

## ✅ ACCEPTANCE CRITERIA

1. **Invoice Processing:**
   - Accurate line item totals
   - Tax calculations
   - Approval workflow

2. **Payment Scheduling:**
   - Schedule future payments
   - Cash flow forecasting
   - Batch processing

3. **Multi-Tenancy:**
   - Each tenant's vendors isolated
   - Separate payment tracking

4. **Multi-Currency:**
   - Support multiple currencies
   - Accurate conversions

---

## 🔧 CONFIGURATION

```yaml
spring:
  data:
    mongodb:
      database: management_finance
  redis:
    host: localhost
    port: 6379

# Accounts payable specific
ap:
  default-payment-terms: NET30
  supported-payment-terms: NET15,NET30,NET60,NET90
  auto-approve-threshold: 5000  # Auto-approve invoices below this
```

---

## 📁 FILE STRUCTURE TO CREATE

```
accounts-payable-service/
├── pom.xml
├── src/main/java/
│   ├── AccountsPayableServiceApplication.java
│   ├── domain/
│   │   ├── model/
│   │   │   ├── BaseEntity.java
│   │   │   ├── VendorInvoice.java
│   │   │   ├── InvoiceLineItem.java
│   │   │   ├── Vendor.java
│   │   │   └── Payment.java
│   │   ├── repository/
│   │   │   ├── BaseRepository.java
│   │   │   ├── VendorInvoiceRepository.java
│   │   │   ├── InvoiceLineItemRepository.java
│   │   │   ├── VendorRepository.java
│   │   │   └── PaymentRepository.java
│   │   └── enums/
│   │       ├── InvoiceStatus.java
│   │       ├── PaymentStatus.java
│   │       └── VendorPaymentTerms.java
│   ├── application/
│   │   ├── command/
│   │   │   ├── InvoiceCommandService.java
│   │   │   ├── PaymentProcessingService.java
│   │   │   └── InvoiceApprovalService.java
│   │   ├── query/
│   │   │   └── InvoiceQueryService.java
│   │   └── dto/
│   │       ├── CreateInvoiceRequest.java
│   │       ├── CreatePaymentRequest.java
│   │       └── InvoiceDTO.java
│   ├── infrastructure/
│   │   ├── config/
│   │   │   ├── MongoDBConfig.java
│   │   │   ├── RedisConfig.java
│   │   │   └── SecurityConfig.java
│   │   └── security/
│   │       └── TenantContextFilter.java
│   ├── interfaces/
│   │   └── rest/
│   │       ├── InvoiceController.java
│   │       ├── VendorController.java
│   │       └── PaymentController.java
│   └── shared/
│       └── requestcontext/
│           ├── RequestContext.java
│           └── RequestContextHolder.java
└── src/main/resources/
    └── application.yml
```

---

## 🧪 TESTS REQUIRED

- VendorInvoiceTest (15+ tests)
- VendorTest (12+ tests)
- PaymentTest (12+ tests)
- InvoiceApprovalServiceTest (12+ tests)
- PaymentProcessingServiceTest (15+ tests)
- InvoiceControllerIntegrationTest (13+ tests)
- TenantIsolationTest (CRITICAL)
- HexagonalArchitectureTest (15 rules)

---

**Gold Standard Reference:** `../../Executive-domain/Backend/Java/executive-dashboard-service/Shared/executive-analytics-service`
**Base Path:** `Finance-department/Backend/Java/finance-service/Accounts/accounts-payable-service`

**READ THESE BEFORE STARTING:**
1. AGENT_INSTRUCTIONS.md
2. PRD_FINANCE_DEPARTMENT.md
3. Gold-standard service (executive-analytics-service)

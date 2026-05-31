# =============================================================================
# Kafka Event Schemas - Management-Domain
# =============================================================================
# This document defines all Kafka event schemas used across the Management-Domain.
# All events MUST include tenantId for multi-tenancy.
# =============================================================================

## -----------------------------------------------------------------------------
## SHARED EVENT ENVELOPE (All events must follow this structure)
## -----------------------------------------------------------------------------

{
  "eventEnvelope": {
    "eventId": "uuid",
    "eventType": "event.type.name",
    "tenantId": "tenant-123",
    "correlationId": "corr-456",
    "timestamp": "2024-01-29T10:00:00Z",
    "version": "1.0",
    "source": "service-name",
    "data": { /* event-specific payload */ }
  }
}

## -----------------------------------------------------------------------------
## EXECUTIVE-DOMAIN EVENTS
## -----------------------------------------------------------------------------

### Executive Approval Events
{
  "executive.approval.requested": {
    "approvalId": "uuid",
    "approver": "userId",
    "approvalType": "STRATEGIC_INITIATIVE|BUDGET|POLICY",
    "entityType": "domain.entity",
    "entityId": "entity-uuid",
    "tenantId": "tenant-123",
    "requestedBy": "userId",
    "urgency": "HIGH|MEDIUM|LOW",
    "deadline": "2024-01-30T10:00:00Z"
  },
  "executive.approval.approved": {
    "approvalId": "uuid",
    "approver": "userId",
    "comments": "Approved with conditions",
    "approvedAt": "2024-01-29T11:00:00Z"
  },
  "executive.approval.rejected": {
    "approvalId": "uuid",
    "approver": "userId",
    "reason": "Does not meet criteria",
    "rejectedAt": "2024-01-29T11:00:00Z"
  }
}

### Executive Alert Events
{
  "executive.alert.triggered": {
    "alertId": "uuid",
    "alertType": "KPI_THRESHOLD|INCIDENT|COMPLIANCE",
    "severity": "CRITICAL|HIGH|MEDIUM|LOW",
    "title": "Alert title",
    "message": "Detailed message",
    "domain": "domain-name",
    "metrics": {},
    "tenantId": "tenant-123"
  }
}

## -----------------------------------------------------------------------------
## HUMAN-RESOURCE EVENTS
## -----------------------------------------------------------------------------

{
  "hr.employee.created": {
    "employeeId": "uuid",
    "tenantId": "tenant-123",
    "country": "IE",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@gogidix.com",
    "department": "Engineering",
    "role": "Software Engineer",
    "createdAt": "2024-01-29T10:00:00Z"
  },
  "hr.employee.terminated": {
    "employeeId": "uuid",
    "tenantId": "tenant-123",
    "terminatedAt": "2024-01-29T10:00:00Z",
    "reason": "Resignation",
    "terminatedBy": "userId"
  },
  "hr.payroll.processed": {
    "payrollRunId": "uuid",
    "tenantId": "tenant-123",
    "country": "IE",
    "period": "2024-01",
    "processedAt": "2024-01-29T10:00:00Z",
    "employeeCount": 150,
    "totalAmount": 150000.00,
    "currency": "EUR"
  },
  "hr.document.delivery.requested": {
    "requestId": "uuid",
    "tenantId": "tenant-123",
    "documentType": "CONTRACT|PAYSLIP|TAX_DOCUMENT",
    "recipientId": "employee-uuid",
    "deliveryMethod": "EMAIL|COURIER|ELECTRONIC",
    "priority": "HIGH|NORMAL|LOW"
  }
}

## -----------------------------------------------------------------------------
## SALES-DEPARTMENTS EVENTS
## -----------------------------------------------------------------------------

{
  "sales.lead.created": {
    "leadId": "uuid",
    "tenantId": "tenant-123",
    "country": "IE",
    "companyName": "Acme Corp",
    "contactName": "Jane Smith",
    "email": "jane@acme.com",
    "source": "MARKETING|REFERRAL|OUTREACH",
    "score": 85,
    "assignedTo": "sales-rep-uuid",
    "createdAt": "2024-01-29T10:00:00Z"
  },
  "sales.deal.created": {
    "dealId": "uuid",
    "tenantId": "tenant-123",
    "leadId": "lead-uuid",
    "dealName": "Acme Enterprise Deal",
    "value": 50000.00,
    "currency": "EUR",
    "stage": "PROSPECTING|QUALIFICATION|PROPOSAL|NEGOTIATION|CLOSED_WON|CLOSED_LOST",
    "probability": 0.60,
    "expectedCloseDate": "2024-03-31",
    "createdAt": "2024-01-29T10:00:00Z"
  },
  "sales.customer.onboarded": {
    "onboardingId": "uuid",
    "tenantId": "tenant-123",
    "customerId": "customer-uuid",
    "onboardedBy": "sales-rep-uuid",
    "products": ["product-1", "product-2"],
    "onboardingDate": "2024-01-29T10:00:00Z"
  }
}

## -----------------------------------------------------------------------------
## DIGITAL-MARKETING EVENTS
## -----------------------------------------------------------------------------

{
  "marketing.campaign.started": {
    "campaignId": "uuid",
    "tenantId": "tenant-123",
    "country": "IE",
    "campaignName": "Q1 Promotion",
    "type": "EMAIL|SOCIAL|SEARCH|DISPLAY",
    "channel": "EMAIL|LINKEDIN|GOOGLE|FACEBOOK",
    "budget": 10000.00,
    "currency": "EUR",
    "startDate": "2024-01-29",
    "endDate": "2024-02-28"
  },
  "marketing.lead.generated": {
    "leadId": "uuid",
    "tenantId": "tenant-123",
    "campaignId": "campaign-uuid",
    "source": "EMAIL|SOCIAL|WEB",
    "capturedAt": "2024-01-29T10:00:00Z",
    "data": {}
  }
}

## -----------------------------------------------------------------------------
## CUSTOMER-SUPPORT EVENTS
## -----------------------------------------------------------------------------

{
  "support.ticket.created": {
    "ticketId": "uuid",
    "tenantId": "tenant-123",
    "customerId": "customer-uuid",
    "subject": "Issue description",
    "priority": "CRITICAL|HIGH|MEDIUM|LOW",
    "category": "TECHNICAL|BILLING|GENERAL",
    "status": "OPEN|IN_PROGRESS|RESOLVED|CLOSED",
    "channel": "EMAIL|CHAT|PHONE|PORTAL",
    "createdAt": "2024-01-29T10:00:00Z"
  },
  "support.ticket.escalated": {
    "ticketId": "uuid",
    "tenantId": "tenant-123",
    "escalatedTo": "support-level-2",
    "reason": "Complex technical issue",
    "escalatedAt": "2024-01-29T11:00:00Z"
  }
}

## -----------------------------------------------------------------------------
## SYSTEM-ADMINISTRATOR EVENTS
## -----------------------------------------------------------------------------

{
  "system.incident.detected": {
    "incidentId": "uuid",
    "tenantId": "SYSTEM",  # System-level events
    "severity": "CRITICAL|HIGH|MEDIUM|LOW",
    "service": "service-name",
    "type": "INFRASTRUCTURE|SECURITY|PERFORMANCE",
    "title": "Incident title",
    "description": "Detailed description",
    "affectedDomains": ["domain-1", "domain-2"],
    "detectedAt": "2024-01-29T10:00:00Z"
  },
  "system.user.provisioned": {
    "provisioningId": "uuid",
    "tenantId": "tenant-123",
    "userId": "user-uuid",
    "action": "CREATE|UPDATE|DEACTIVATE|DELETE",
    "provisionedAt": "2024-01-29T10:00:00Z",
    "provisionedBy": "admin-uuid"
  }
}

## -----------------------------------------------------------------------------
## GLOBAL-BUSINESS-MANAGEMENT EVENTS
## -----------------------------------------------------------------------------

{
  "business.data.ingested": {
    "ingestionId": "uuid",
    "tenantId": "tenant-123",
    "source": "COUNTRY_ADMIN",
    "sourceCountry": "IE",
    "dataType": "SALES|FINANCE|OPERATIONS",
    "recordCount": 1500,
    "ingestedAt": "2024-01-29T10:00:00Z"
  },
  "business.report.generated": {
    "reportId": "uuid",
    "tenantId": "tenant-123",
    "reportType": "REGIONAL|GLOBAL",
    "region": "EUROPE",
    "period": "2024-01",
    "generatedAt": "2024-01-29T10:00:00Z"
  }
}

## -----------------------------------------------------------------------------
## FINANCE-DEPARTMENT EVENTS
## -----------------------------------------------------------------------------

{
  "finance.invoice.processed": {
    "invoiceId": "uuid",
    "tenantId": "tenant-123",
    "invoiceNumber": "INV-2024-001",
    "vendorId": "vendor-uuid",
    "amount": 5000.00,
    "currency": "EUR",
    "dueDate": "2024-02-28",
    "processedAt": "2024-01-29T10:00:00Z"
  },
  "finance.budget.approved": {
    "budgetId": "uuid",
    "tenantId": "tenant-123",
    "fiscalYear": "2024",
    "department": "Engineering",
    "budgetAmount": 1000000.00,
    "currency": "EUR",
    "approvedBy": "exec-uuid",
    "approvedAt": "2024-01-29T10:00:00Z"
  }
}

## -----------------------------------------------------------------------------
## KAFKA TOPIC NAMING CONVENTION
## -----------------------------------------------------------------------------

# Topics are named as: {domain}.{entity}.{action}
# Examples:
# - executive.approval.requested
# - hr.employee.created
# - sales.deal.won
# - finance.invoice.paid

# Topic Naming Rules:
# 1. Use lowercase
# 2. Separate parts with dots
# 3. Domain: executive, hr, sales, marketing, support, admin, business, finance
# 4. Entity: resource name (singular)
# 5. Action: created, updated, deleted, requested, approved, etc.

## -----------------------------------------------------------------------------
## RETENTION POLICY
## -----------------------------------------------------------------------------

# Default retention: 7 days
# Critical events (approvals, incidents): 30 days
# Compliance events (finance, hr): 365 days

## -----------------------------------------------------------------------------
## PARTITIONING STRATEGY
## -----------------------------------------------------------------------------

# Partition key: tenantId
# This ensures all events for a tenant go to the same partition
# enabling efficient tenant-specific consumption

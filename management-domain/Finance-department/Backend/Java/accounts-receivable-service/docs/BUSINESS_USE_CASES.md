# Accounts Receivable Service - Business Use Cases

## Overview

The Accounts Receivable Service manages customer invoicing, payment collection, and credit management.

---

## 1. Customer Management

### 1.1 Customer Registration
Onboarding new customers with credit assessment and payment terms setup.

**Key Steps:**
1. Collect customer information
2. Perform credit check
3. Set credit limit and payment terms
4. Create customer record in ACTIVE status
5. Publish CustomerCreated event

### 1.2 Credit Management
Managing customer credit limits and collection stages.

**Key Steps:**
1. Assess customer payment history
2. Update credit limits as needed
3. Monitor outstanding balances
4. Escalate collections based on overdue stages

---

## 2. Invoice Processing

### 2.1 Invoice Generation
Creating and sending invoices to customers.

**Key Steps:**
1. Gather billing information
2. Calculate line items and taxes
3. Generate invoice number
4. Send to customer (email/paper)
5. Track delivery status

### 2.2 Payment Application
Applying customer payments to outstanding invoices.

**Key Steps:**
1. Receive payment from customer
2. Match payment to invoices
3. Apply payment allocation
4. Update customer balance
5. Generate receipt

---

## 3. Collections Management

### 3.1 Overdue Monitoring
Tracking and managing overdue invoices.

**Key Steps:**
1. Daily scan for overdue invoices
2. Update customer collection stage
3. Generate aging report
4. Trigger collection workflows

### 3.2 Collection Actions
Escalating collection activities based on overdue stage.

**Stages:**
- CURRENT: No action needed
- SOON_DUE: Send payment reminder
- OVERDUE_1_30: Send first collection notice
- OVERDUE_31_60: Send second notice, call customer
- OVERDUE_61_90: Formal demand letter
- OVERDUE_90_PLUS: Escalate to collections agency

---

## 4. Reporting

### 4.1 Aging Report
Categorizing outstanding invoices by age.

**Buckets:**
- Current (not yet due)
- 1-30 days past due
- 31-60 days past due
- 61-90 days past due
- 90+ days past due

### 4.2 Cash Forecast
Predicting incoming cash flow from expected payments.

**Inputs:**
- Outstanding invoices
- Payment terms
- Historical payment patterns

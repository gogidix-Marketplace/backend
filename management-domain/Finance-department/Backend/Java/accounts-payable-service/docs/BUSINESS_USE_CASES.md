# Accounts Payable Service - Business Use Cases

## Overview

The Accounts Payable Service manages the complete accounts payable workflow, from vendor onboarding to invoice processing and payment execution. This document outlines the key business use cases supported by the service.

---

## 1. Vendor Management

### 1.1 Vendor Registration

**Business Context:** Onboarding new vendors into the system.

**Actors:** AP Clerk, Vendor Administrator

**Preconditions:**
- User is authenticated with appropriate permissions
- Vendor tax ID is available

**Main Flow:**
1. User initiates vendor registration
2. System validates vendor code uniqueness
3. System checks for existing vendor with same tax ID
4. User enters vendor details:
   - Vendor code and name
   - Vendor type (Corporation, Individual, LLC, etc.)
   - Tax ID
   - Contact information
   - Billing and shipping addresses
   - Bank information
   - Payment terms
5. System creates vendor with status `PENDING_APPROVAL`
6. System publishes `VendorRegistered` event
7. Notification sent to approvers

**Alternative Flows:**
- **Duplicate Vendor Code:** System returns error suggesting alternate codes
- **Duplicate Tax ID:** System returns warning about existing vendor with same tax ID
- **Invalid Bank Details:** System validates routing number format

**Postconditions:**
- Vendor exists in `PENDING_APPROVAL` status
- No payments can be processed until activation

---

### 1.2 Vendor Activation

**Business Context:** Approving a new vendor after due diligence.

**Actors:** AP Manager, Finance Controller

**Preconditions:**
- Vendor exists in `PENDING_APPROVAL` status
- Due diligence completed (credit check, compliance review)

**Main Flow:**
1. User requests vendor activation
2. System displays vendor details for review
3. User confirms activation
4. System updates vendor status to `ACTIVE`
5. System records activation timestamp
6. System publishes `VendorActivated` event
7. Vendor can now receive payments

**Alternative Flows:**
- **Failed Credit Check:** User may `SUSPEND` or `BLACKLIST` instead of activate
- **Incomplete Documentation:** User requests additional information before activation

---

### 1.3 Vendor Payment Terms Update

**Business Context:** Negotiating new payment terms with existing vendor.

**Actors:** Procurement Manager, AP Manager

**Preconditions:**
- Vendor is `ACTIVE`

**Main Flow:**
1. User initiates payment terms update
2. User enters new terms:
   - Payment terms description (e.g., "Net 60")
   - Payment days (e.g., 60)
   - Discount percentage for early payment
3. System updates vendor record
4. System applies new terms to future invoices
5. System logs change for audit trail

---

### 1.4 Vendor Blacklisting

**Business Context:** Blocking a vendor due to fraud, compliance issues, or repeated problems.

**Actors:** Finance Controller, Compliance Officer

**Preconditions:**
- Vendor exists in any status

**Main Flow:**
1. User initiates vendor blacklisting
2. User provides reason for blacklisting
3. System updates vendor status to `BLACKLISTED`
4. System prevents:
   - New invoice creation
   - Payment processing
   - Vendor modifications
5. System publishes `VendorBlacklisted` event
6. Compliance team notified for review

**Postconditions:**
- All pending payments to vendor are cancelled
- All pending invoices from vendor are flagged

---

## 2. Invoice Processing

### 2.1 Invoice Capture

**Business Context:** Recording a new invoice received from a vendor.

**Actors:** AP Clerk

**Preconditions:**
- Vendor exists and is `ACTIVE`

**Main Flow:**
1. User initiates invoice creation
2. User selects or enters vendor
3. System auto-populates vendor terms and currency
4. User enters invoice details:
   - Invoice number (unique per vendor)
   - Invoice date
   - Due date (auto-calculated from payment terms)
   - Invoice amount
   - Line items
   - General ledger account
   - Department/cost center
5. User attaches invoice PDF/scanned copy
6. System creates invoice in `DRAFT` status
7. System validates against purchase order (if referenced)
8. System calculates tax and discounts

**Alternative Flows:**
- **Duplicate Invoice Number:** System warns if invoice number exists for vendor
- **No PO Matched:** Invoice flagged for manual review if PO required
- **Amount Variance:** System flags if invoice amount differs from PO by > threshold

---

### 2.2 Invoice Submission

**Business Context:** Submitting invoice for approval workflow.

**Actors:** AP Clerk

**Preconditions:**
- Invoice is in `DRAFT` status
- All required fields populated
- Invoice has valid PO reference (if required by policy)

**Main Flow:**
1. User reviews draft invoice
2. User submits for approval
3. System validates all required fields
4. System changes status to `PENDING`
5. System routes to appropriate approver based on:
   - Invoice amount
   - Department
   - GL account
6. System publishes `InvoiceSubmitted` event
7. Approvers receive notification

**Alternative Flows:**
- **Validation Failure:** System displays missing fields, prevents submission
- **Approval Level Required:** System determines approval level (Manager, Finance, Executive)

---

### 2.3 Multi-Level Invoice Approval

**Business Context:** Progressive approval workflow for larger invoices.

**Actors:** Department Manager, Finance Manager, CFO

**Preconditions:**
- Invoice is in `PENDING` status
- User is designated approver

**Main Flow:**
1. User receives approval notification
2. User reviews invoice details and attachments
3. User approves or rejects

**Approval Decision:**
- **Manager Approval:**
  - Required for invoices below $5,000
  - Status remains `PENDING` (awaiting Finance approval)
  - Approver recorded on invoice

- **Finance Approval:**
  - Required for invoices $5,000 - $25,000
  - Status changes to `APPROVED`
  - Payment processing can begin

- **Executive Approval:**
  - Required for invoices above $25,000
  - Status changes to `APPROVED`
  - CFO approval recorded

**Rejection Decision:**
1. User enters rejection reason
2. System changes status to `REJECTED`
3. AP Clerk notified with rejection reason
4. Invoice cannot be resubmitted without corrections

---

### 2.4 Invoice Payment Processing

**Business Context:** Processing payment for approved invoice.

**Actors:** AP Specialist, Payment System

**Preconditions:**
- Invoice is in `APPROVED` status
- Payment terms due date has arrived or is within payment window

**Main Flow:**
1. Payment scheduling process identifies approved invoices
2. System groups invoices by vendor for potential consolidation
3. User reviews payment batch
4. User confirms payment
5. System creates payment record
6. System submits payment to banking system
7. On confirmation:
   - System updates invoice status to `PAID`
   - System updates payment status to `COMPLETED`
   - System publishes payment events
   - System updates vendor balance

**Alternative Flows:**
- **Payment Failure:** System records failure reason, retries based on configured policy
- **Partial Payment:** System supports partial payments with status `PARTIALLY_PAID`
- **Early Payment Discount:** System captures discount if paid within discount period

---

### 2.5 Invoice Overdue Management

**Business Context:** Identifying and managing overdue invoices.

**Actors:** AP Manager, System Scheduler

**Preconditions:**
- Invoice is in `APPROVED` or `PENDING` status
- Current date is past due date

**Main Flow:**
1. Scheduled job runs daily
2. System identifies invoices past due date
3. System updates status to `OVERDUE`
4. System calculates aging:
   - 1-30 days past due
   - 31-60 days past due
   - 61-90 days past due
   - 90+ days past due
5. System sends escalation notifications
6. Vendor payment history updated

**Postconditions:**
- Vendor may be flagged for potential payment holds
- Additional approval required for future orders

---

## 3. Payment Processing

### 3.1 Single Invoice Payment

**Business Context:** Processing payment for a single approved invoice.

**Actors:** AP Specialist

**Preconditions:**
- Invoice is `APPROVED`
- Vendor bank details on file

**Main Flow:**
1. User initiates payment from invoice
2. System displays payment details:
   - Invoice amount
   - Available discounts
   - Payment method
   - Estimated settlement date
3. User confirms payment
4. System creates payment record in `PENDING` status
5. System processes payment via selected method
6. System updates invoice and payment statuses

---

### 3.2 Consolidated Payment

**Business Context:** Combining multiple invoices into single payment.

**Actors:** AP Specialist

**Preconditions:**
- Multiple invoices exist for same vendor
- All invoices are `APPROVED`
- No payment holds on vendor

**Main Flow:**
1. User selects vendor for payment
2. System displays all approved, unpaid invoices
3. User selects invoices to pay:
   - All invoices
   - Specific invoices
4. System calculates total payment amount
5. User selects payment method and date
6. System creates single payment with multiple allocations
7. Payment processed as single transaction

**Benefits:**
- Reduced banking fees
- Simplified reconciliation
- Better cash flow management

---

### 3.3 Scheduled Payment

**Business Context:** Scheduling payment for future date.

**Actors:** AP Specialist

**Preconditions:**
- Invoice is `APPROVED`
- Payment date is on or after due date

**Main Flow:**
1. User creates payment from invoice
2. User selects schedule date
3. System creates payment in `SCHEDULED` status
4. On scheduled date:
   - System changes status to `PROCESSING`
   - Payment initiated
   - Status updated to `COMPLETED` on success

**Alternative Flows:**
- **Invoice Cancelled:** Scheduled payment cancelled
- **Vendor Blacklisted:** Scheduled payment cancelled

---

### 3.4 International Payment

**Business Context:** Processing payment to foreign vendor with currency conversion.

**Actors:** AP Specialist, Treasury

**Preconditions:**
- Vendor uses foreign currency
- Exchange rate available for payment date

**Main Flow:**
1. User initiates payment
2. System displays foreign currency amount
3. System fetches current exchange rate
4. System displays converted amount in base currency
5. User confirms:
   - Foreign currency amount
   - Exchange rate used
   - Total in base currency
6. System records both amounts on payment
7. Payment processed with currency details

**Postconditions:**
- General ledger records both amounts
- Foreign exchange gain/loss calculated on settlement

---

### 3.5 Payment Reconciliation

**Business Context:** Matching bank statements to processed payments.

**Actors:** AP Specialist, Reconciliation System

**Preconditions:**
- Bank statement imported
- Payments exist in `COMPLETED` status

**Main Flow:**
1. System imports bank statement
2. System matches payments by:
   - Payment reference
   - Amount
   - Date
   - Vendor
3. User reviews unmatched items
4. User manually matches where needed
5. System updates payment records with transaction details
6. Reconciliation report generated

---

## 4. Reporting and Analytics

### 4.1 Cash Flow Forecasting

**Business Context:** Predicting future cash outflows for AP.

**Actors:** Finance Manager, CFO

**Preconditions:**
- Approved invoices exist
- Scheduled payments exist

**Main Flow:**
1. System aggregates:
   - Approved, unpaid invoices
   - Scheduled payments
2. System groups by due date
3. System generates daily/weekly cash forecast
4. System displays:
   - Total AP for period
   - Payments due by date
   - Vendor breakdown
   - Discount opportunities

---

### 4.2 Vendor Performance Analysis

**Business Context:** Evaluating vendor payment performance.

**Actors:** Procurement, Finance

**Preconditions:**
- Payment history exists for vendor

**Main Flow:**
1. User selects vendor
2. System calculates metrics:
   - Average payment time
   - On-time payment percentage
   - Discount capture rate
   - Invoice accuracy rate
   - Dispute frequency
3. System generates vendor scorecard
4. System compares to industry benchmarks

---

### 4.3 AP Aging Report

**Business Context:** Understanding outstanding payment obligations.

**Actors:** Finance Manager, Treasurer

**Preconditions:**
- Unpaid invoices exist

**Main Flow:**
1. System retrieves all unpaid invoices
2. System categorizes by aging bucket:
   - Current (not yet due)
   - 0-30 days past due
   - 31-60 days past due
   - 61-90 days past due
   - 90+ days past due
3. System calculates totals by bucket
4. System generates report by:
   - Vendor
   - Department
   - Cost center
5. Export available for analysis

---

## 5. Compliance and Audit

### 5.1 Audit Trail Access

**Business Context:** Retrieving complete history of invoice/payment changes.

**Actors:** Auditor, Compliance Officer

**Preconditions:**
- User has audit permissions

**Main Flow:**
1. User requests audit trail for entity
2. System retrieves:
   - Creation details (who, when)
   - All modifications (who, when, what changed)
   - Status transitions
   - Approvals and rejections
   - Payment history
3. System displays chronological history
4. Export available for audit documentation

---

### 5.2 Duplicate Invoice Detection

**Business Context:** Preventing duplicate payments.

**Actors:** AP Clerk, System

**Preconditions:**
- New invoice being created

**Main Flow:**
1. User enters invoice number and vendor
2. System checks for:
   - Same invoice number + vendor combination
   - Similar amounts within date range
   - Same PO already invoiced
3. System displays potential duplicates
4. User confirms if duplicate or legitimately different
5. System flags for review if potential duplicate

---

### 5.3 Regulatory Reporting

**Business Context:** Generating reports for tax and regulatory compliance.

**Actors:** Finance Team

**Preconditions:**
- Reporting period ended

**Main Flow:**
1. User selects report type:
   - 1099 preparation (US)
   - VAT reporting (EU)
   - Tax summary
2. System aggregates vendor payments
3. System applies tax rules:
   - Reportable entities
   - Threshold amounts
   - Tax ID requirements
4. System generates report file
5. System flags missing information:
   - Missing tax IDs
   - Incomplete addresses

---

## 6. Exception Handling

### 6.1 Invoice Dispute Resolution

**Business Context:** Managing disputed invoices.

**Actors:** AP Clerk, Vendor

**Preconditions:**
- Invoice is in dispute

**Main Flow:**
1. User flags invoice as disputed
2. User enters dispute reason and details
3. System prevents payment processing
4. System notifies vendor
5. Upon resolution:
   - User updates invoice or confirms for payment
   - Dispute flag removed
   - Payment proceeds if approved

---

### 6.2 Payment Failure Handling

**Business Context:** Managing failed payment attempts.

**Actors:** AP Specialist, System

**Preconditions:**
- Payment attempt failed

**Main Flow:**
1. System receives payment failure
2. System updates payment status to `FAILED`
3. System records failure reason
4. System initiates retry logic:
   - Immediate retry for transient errors
   - Scheduled retry for insufficient funds
   - Manual intervention for blocked accounts
5. AP Specialist notified for required actions

---

## 7. Integration Use Cases

### 7.1 Procurement Integration

**Business Context:** Linking invoices to purchase orders.

**Preconditions:**
- PO exists in procurement system

**Main Flow:**
1. Invoice created with PO reference
2. System validates against PO:
   - PO exists and is open
   - Invoice amount within PO tolerance
   - Line items match PO items
3. Three-way match performed (PO → Receipt → Invoice)
4. Invoice auto-approved if match successful

---

### 7.2 General Ledger Integration

**Business Context:** Recording AP transactions in GL.

**Preconditions:**
- GL accounts configured

**Main Flow:**
1. Invoice approved triggers GL entry:
   - Debit: Expense/Asset account
   - Credit: Accounts Payable
2. Payment processed triggers GL entry:
   - Debit: Accounts Payable
   - Credit: Cash/Bank
3. Discounts taken trigger GL entry
4. Currency adjustments recorded

---

### 7.3 Treasury Integration

**Business Context:** Providing payment data for cash management.

**Preconditions:**
- Treasury system configured

**Main Flow:**
1. Scheduled payments shared with treasury
2. Payment confirmations sent to treasury
3. Bank balance data consumed from treasury
4. Funding recommendations received

---

## 8. User Role Matrix

| Use Case | AP Clerk | AP Manager | Finance Manager | CFO | Auditor |
|-----------|----------|------------|-----------------|-----|---------|
| Vendor Registration | ✓ | ✓ | | | |
| Vendor Activation | | ✓ | ✓ | | |
| Invoice Capture | ✓ | ✓ | | | |
| Invoice Approval | | ✓ | ✓ | ✓ | |
| Payment Processing | ✓ | ✓ | | | |
| Payment Reconciliation | ✓ | ✓ | | | |
| Reporting View | ✓ | ✓ | ✓ | ✓ | ✓ |
| Audit Access | | | | | ✓ |
| Vendor Blacklist | | | ✓ | ✓ | |

---

## 9. Service Level Agreements

### Invoice Processing
- **Capture to Submission:** < 4 business hours
- **Submission to Approval:** < 2 business days (standard), < 4 hours (urgent)
- **Approval to Payment:** As per payment terms

### Payment Processing
- **Payment Initiation:** < 1 business day from approval
- **Bank Transfer Confirmation:** < 2 business days
- **Same-Day Payments:** Available by 2 PM cutoff

### System Availability
- **Uptime:** 99.9% during business hours
- **Response Time:** < 500ms for API calls
- **Data Refresh:** Real-time for critical data

---

## 10. Future Enhancements

### Planned Use Cases
1. **AI-Powered Invoice Recognition**: Automatic data extraction from PDF invoices
2. **Dynamic Discount Optimization**: Recommending optimal payment timing
3. **Supply Chain Finance**: Integration with vendor financing programs
4. **Mobile Approvals**: Native app for invoice approval on mobile devices
5. **Blockchain Payment Verification**: Immutable payment verification network

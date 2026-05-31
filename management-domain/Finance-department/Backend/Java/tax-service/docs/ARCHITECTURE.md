# Tax Service - Architecture Documentation

## Overview

The Tax Service manages tax calculations, filings, and compliance across multiple jurisdictions. It handles sales tax, VAT, GST, use tax, and income tax provisions.

## Architecture

### Technology Stack
- **Framework**: Spring Boot 3.1.5
- **Language**: Java 17
- **Database**: MongoDB
- **Message Broker**: Apache Kafka
- **External Integrations**: Tax APIs (Avalara, Vertex, etc.)

### Domain Models

#### TaxAuthority
- Represents a tax jurisdiction (IRS, state tax authorities)
- Manages filing requirements and deadlines
- Tracks tax rates and rules

#### TaxReturn
- Represents a filed tax return
- Tracks return status and payments
- Maintains audit trail

#### TaxCalculation
- Stores tax calculation results
- Links to source transactions
- Supports exemption handling

#### TaxRate
- Defines tax rates by jurisdiction and product type
- Handles effective date ranges
- Supports compound tax scenarios

## API Endpoints

### Tax Calculations
- `POST /api/v1/tax/calculate` - Calculate tax on a transaction
- `POST /api/v1/tax/calculate/batch` - Batch calculate taxes

### Tax Returns
- `POST /api/v1/tax/returns` - Create tax return
- `GET /api/v1/tax/returns` - List returns by period
- `PUT /api/v1/tax/returns/{returnId}/file` - File tax return
- `POST /api/v1/tax/returns/{returnId}/pay` - Record tax payment

### Tax Configuration
- `GET /api/v1/tax/rates` - List tax rates
- `PUT /api/v1/tax/rates/{rateId}` - Update tax rate
- `GET /api/v1/tax/authorities` - List tax authorities

## Data Model

### Tax Returns Collection
```javascript
{
  returnId: "string",
  tenantId: "string",
  taxAuthorityId: "string",
  returnType: "SALES_TAX",
  periodStart: ISODate,
  periodEnd: ISODate,
  filingStatus: "FILED",
  totalTax: NumberDecimal,
  totalPayments: NumberDecimal,
  balanceDue: NumberDecimal
}
```

## Event Integration

### Consumed Events
- InvoiceGenerated - Calculate sales tax
- PaymentProcessed - Calculate use tax
- TaxRuleChanged - Update rate cache

### Published Events
- TaxCalculated - Notify accounting systems
- TaxReturnFiled - Trigger payment processing
- TaxPaymentMade - Update ledger

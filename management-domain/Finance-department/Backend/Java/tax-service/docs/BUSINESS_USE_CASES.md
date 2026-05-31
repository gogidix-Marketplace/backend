# Tax Service - Business Use Cases

## Primary Use Cases

### 1. Sales Tax Calculation on Invoices
- **Actors**: Accounts Receivable, Billing System
- **Description**: Automatically calculate correct tax based on location
- **Process**:
  1. Invoice created with delivery address
  2. Tax service determines jurisdiction
  3. Apply applicable tax rates (state, county, city, special districts)
  4. Return tax breakdown for invoice

### 2. Multi-Jurisdiction Tax Filing
- **Actors**: Tax Compliance Team
- **Description**: File tax returns in multiple jurisdictions
- **Process**:
  1. System aggregates taxable transactions by jurisdiction
  2. Generate tax return with required breakdowns
  3. Review and approve return
  4. File electronically or prepare for paper filing
  5. Schedule payments

### 3. Tax Audit Support
- **Actors**: CPAs, Auditors
- **Description**: Provide complete tax transaction history
- **Process**:
  1. Query transactions by date range and jurisdiction
  2. Export audit trail with calculations
  3. Provide exemption documentation
  4. Support tax notice responses

### 4. Use Tax Calculation
- **Actors**: Procurement, AP
- **Description**: Calculate tax on purchases where vendor didn't charge
- **Process**:
  1. Identify taxable purchase
  2. Determine buyer's jurisdiction
  3. Calculate use tax owed
  4. Accrue liability for payment

### 5. Tax Exemption Management
- **Actors**: Tax Manager, Sales
- **Description**: Manage customer and product exemptions
- **Process**:
  1. Create exemption certificates
  2. Link to customers or products
  3. Validate certificates before applying
  4. Track certificate expiration

### 6. Nexus Management
- **Actors**: Tax Compliance Team
- **Description**: Track where tax obligations exist
- **Process**:
  1. Monitor economic nexus thresholds
  2. Alert when threshold approaching
  3. Register in new jurisdictions
  4. Update calculation rules

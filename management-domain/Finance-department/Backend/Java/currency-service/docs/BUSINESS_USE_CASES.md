# Currency Service - Business Use Cases

## Overview

The Currency Service handles all currency-related operations including currency management, exchange rate updates, and currency conversions.

---

## 1. Currency Management

### 1.1 Currency Definition
Defining supported currencies for the organization.

**Key Steps:**
1. Validate ISO 4217 currency code
2. Set currency attributes (symbol, decimal places)
3. Associate with countries using the currency
4. Activate currency for use

### 1.2 Currency Lifecycle
Managing currency status throughout its lifecycle.

**States:**
- PENDING_ACTIVATION - Initial state
- ACTIVE - Available for transactions
- INACTIVE - Disabled but retained
- SUSPENDED - Temporarily unavailable

---

## 2. Exchange Rate Management

### 2.1 Rate Updates
Updating exchange rates from external sources.

**Key Steps:**
1. Fetch rates from ECB, Federal Reserve, or other sources
2. Validate rates against acceptable ranges
3. Store rates with timestamp
4. Publish rate update events

### 2.2 Rate Validation
Ensuring exchange rates are within acceptable bounds.

**Validation Rules:**
- Rate change < 10% from previous rate
- Rate source is trusted
- Rate timestamp is recent (< 24 hours)

---

## 3. Currency Conversion

### 3.1 Real-time Conversion
Converting amounts between currencies.

**Key Steps:**
1. Get current exchange rate
2. Apply rate to source amount
3. Round to target currency decimal places
4. Record conversion for audit

### 3.2 Historical Conversion
Converting using historical exchange rates.

**Use Cases:**
- Financial reporting for past periods
- Audit trail reconstruction
- Analytics on historical transactions

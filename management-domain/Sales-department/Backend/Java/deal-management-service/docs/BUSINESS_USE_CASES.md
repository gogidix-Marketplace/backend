# Deal Management Service - Business Use Cases

## Overview

The Deal Management Service manages all sales opportunities (deals) throughout their lifecycle, from initial lead qualification through to closing (won or lost).

## Primary Use Cases

### 1. Create Deal from Qualified Lead

**Actor:** Sales Representative

**Goal:** Create a new sales opportunity from a qualified lead

**Preconditions:**
- Lead has been qualified
- Account information available
- Initial deal estimate provided

**Flow:**
1. Sales rep converts qualified lead to deal
2. System creates deal in LEAD stage
3. Default probability set to 10%
4. Owner assigned to creating rep
5. Deal code auto-generated (DL-XXXXXXXX)
6. DEAL_CREATED event published

**Postconditions:**
- Deal visible in pipeline
- Forecast updated with weighted amount
- Activities can be tracked

### 2. Pipeline Stage Progression

**Actor:** Sales Representative

**Goal:** Advance deal through sales pipeline stages

**Preconditions:**
- Deal exists and is in OPEN status
- Required activities completed for current stage

**Flow:**
1. Rep completes required stage activities
2. Rep advances to next stage
3. System updates probability based on stage
4. Weighted amount recalculated
5. Activity logged with timestamp
6. DEAL_STAGE_CHANGED event published

**Postconditions:**
- Deal in new stage
- Forecast updated
- Stage progression tracked for analytics

### 3. Deal Valuation and Forecasting

**Actor:** Sales Manager

**Goal:** Calculate accurate sales forecast

**Preconditions:**
- Multiple deals in pipeline
- Various stages with different probabilities

**Flow:**
1. System aggregates all open deals
2. Calculates weighted amount for each (amount × probability)
3. Groups by stage for pipeline view
4. Total forecast = sum of weighted amounts
5. Categorizes by territory, owner, product

**Postconditions:**
- Accurate forecast available
- Pipeline visibility for management
- Trend analysis enabled

### 4. Large Deal Approval Workflow

**Actor:** Sales Manager / Sales Director

**Goal:** Ensure proper approval for significant deals

**Preconditions:**
- Deal amount exceeds threshold
- Approval policy configured

**Flow:**
1. Deal created/updated above threshold
2. System checks approval requirements
3. Approval request initiated
4. Manager/director receives notification
5. Approver reviews deal details
6. Approval/rejection decision recorded
7. Deal can only proceed when approved

**Postconditions:**
- Large deals properly reviewed
- Compliance maintained
- Audit trail created

### 5. Competitive Deal Tracking

**Actor:** Sales Representative

**Goal:** Track competitor involvement in deal

**Preconditions:**
- Competitor identified
- Competitive intelligence gathered

**Flow:**
1. Rep adds competitor to deal
2. Records competitor strengths/weaknesses
3. Sets threat level
4. Team can access competitive info
5. Strategy adjusted based on competition

**Postconditions:**
- Competitive visibility
- Informed sales strategy
- Win/loss analysis data

### 6. Deal Closure (Won)

**Actor:** Sales Representative

**Goal:** Successfully close deal as won

**Preconditions:**
- Contract signed or verbal commitment
- Final amount confirmed

**Flow:**
1. Rep marks deal as won
2. System sets stage to CLOSED_WON
3. Probability set to 100%
4. Actual close date recorded
5. Deal duration calculated
6. Commission calculated
7. DEAL_WON event published
8. Revenue tracking notified

**Postconditions:**
- Deal removed from pipeline
- Revenue recognized
- Commission calculated
- Analytics updated

### 7. Deal Closure (Lost)

**Actor:** Sales Representative

**Goal:** Document lost deal with reasoning

**Preconditions:**
- Deal no longer viable
- Loss reason identified

**Flow:**
1. Rep marks deal as lost
2. Selects loss reason (PRICE, FEATURES, TIMING, COMPETITOR, etc.)
3. Provides detailed explanation
4. System sets stage to CLOSED_LOST
5. Probability set to 0%
6. Weighted amount zeroed
7. DEAL_LOST event published
8. Analysis data captured

**Postconditions:**
- Deal removed from active pipeline
- Win/loss analysis enriched
- Process improvement insights

### 8. Multi-Product Deal Management

**Actor:** Sales Representative

**Goal:** Manage deals with multiple product lines

**Preconditions:**
- Customer requires multiple products
- Product catalog available

**Flow:**
1. Rep adds products to deal
2. Sets quantity and unit price for each
3. Applies discounts as needed
4. System calculates total amount
5. Deal amount automatically updated
6. Weighted forecast recalculated

**Postconditions:**
- Accurate deal valuation
- Product-level visibility
- Bundle analysis enabled

### 9. Deal Regression and Recovery

**Actor:** Sales Manager

**Goal:** Handle stalled or regressed deals

**Preconditions:**
- Deal not progressing
- Customer objections identified

**Flow:**
1. Deal regressed to appropriate stage
2. Reason documented
3. Recovery activities planned
4. Owner may be reassigned
5. Special attention flagged

**Postconditions:**
- Accurate pipeline representation
- Recovery process initiated
- Stage regression tracked

### 10. Territory and Owner Reassignment

**Actor:** Sales Manager

**Goal:** Reassign deals between territories/owners

**Preconditions:**
- Territory realignment
- Employee departure
- Load balancing needed

**Flow:**
1. Manager selects deals for reassignment
2. New owner specified
3. Reason for change documented
4. Activity logged
5. New owner notified
6. Deal follows new owner's pipeline

**Postconditions:**
- Ownership transferred
- Forecast updated by territory
- Transition documented

## Integration Points

### Upstream Services
- **Lead Management Service**: Converts qualified leads to deals
- **Account Management Service**: Provides account context
- **Product Catalog**: Product pricing and availability

### Downstream Services
- **Revenue Tracking Service**: Recognizes won deal revenue
- **Commission Service**: Calculates sales commissions
- **Analytics Service**: Aggregates deal metrics
- **Forecast Service**: Uses weighted pipeline for predictions

## Business Rules

1. **Deal Amount**
   - Must be positive
   - Auto-calculated from product line items
   - Discounts applied before final amount

2. **Stage Transitions**
   - Must follow sequential order (no skipping)
   - Cannot advance from CLOSED_WON or CLOSED_LOST
   - Regression allowed with documented reason

3. **Probability**
   - Default by stage (10%, 20%, 40%, 60%, 80%, 100%, 0%)
   - Can be manually overridden
   - Weighted amount = amount × probability / 100

4. **Approval Workflow**
   - Manager threshold: deals above $50,000
   - Executive threshold: deals above $100,000
   - Pending deals cannot close until approved

5. **Closed Deals**
   - Cannot be modified
   - Cannot be deleted (audit trail)
   - Actual close date immutable

## Key Performance Indicators

1. **Pipeline Velocity**: Average time through stages
2. **Win Rate**: Closed won / (Closed won + Closed lost)
3. **Average Deal Size**: Mean amount of closed deals
4. **Sales Cycle Length**: Days from creation to close
5. **Conversion Rate**: Stage-to-stage conversion percentages

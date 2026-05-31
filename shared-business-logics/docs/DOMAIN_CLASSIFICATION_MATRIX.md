# Gogidix Ecosystem Domain Classification Matrix
## Financial-Grade vs Enterprise-Grade Testing Tiers

**Generated**: 2026-04-05
**Purpose**: Classify all 600+ services by testing tier for Financial-Grade (85/75/60) vs Enterprise-Grade (70/60/optional)

---

## TIER CLASSIFICATION RULES

### Tier 1: Financial-Grade Services
**Thresholds**: 85% line, 75% branch, 60% mutation

**Service Name Patterns** (apply across ALL domains):
- **Security**: `*-security-*`, `*-auth-*`, `*-fraud-*`, `*-risk-*`, `*-compliance-*`
- **Financial**: `*-payment-*`, `*-transaction-*`, `*-settlement-*`, `*-billing-*`, `*-invoice-*`, `*-accounting-*`, `*-ledger-*`
- **Risk/Fraud**: `*-aml-*`, `*-kyc-*`, `*-risk-*`, `*-audit-*`
- **Governance**: `*-policy-*`, `*-governance-*`

### Tier 2: Enterprise-Grade Services
**Thresholds**: 70% line, 60% branch, no mutation required

**All other services**:
- **Customer Experience**: `*-engagement-*`, `*-experience-*`, `*-interaction-*`
- **Analytics**: `*-analytics-*`, `*-insights-*`, `*-reporting-*`
- **Operations**: `*-notification-*`, `*-monitoring-*`, `*-logging-*`, `*-scheduling-*`
- **Content**: `*-content-*`, `*-document-*`, `*-media-*`
- **Communication**: `*-messaging-*`, `*-chat-*`, `*-email-*`

---

## DOMAIN-BY-DOMAIN CLASSIFICATION

### 1. shared-business-logics

#### Category: security-fraud-prevention
| Service | Tier | Justification |
|---------|------|----------------|
| ai-fraud-detection-service | **Financial** | Core fraud detection ✅ TESTED |
| ai-security-analysis-service | **Financial** | Security analysis |

#### Category: business-operations
| Service | Tier | Justification |
|---------|------|----------------|
| All services | **Enterprise** | Business operations |

#### Category: customer-experience-engagement
| Service | Tier | Justification |
|---------|------|----------------|
| All services (ai-translation, ai-voice-assistant, etc.) | **Enterprise** | Customer engagement |

#### Category: data-analytics
| Service | Tier | Justification |
|---------|------|----------------|
| All services | **Enterprise** | Analytics/reporting |

#### Category: machine-learning-operations
| Service | Tier | Justification |
|---------|------|----------------|
| All services | **Enterprise** | ML operations |

#### Category: content-document-processing
| Service | Tier | Justification |
|---------|------|----------------|
| All services | **Enterprise** | Content processing |

---

### 2. shared-business-infrastructure

#### Category: shared-ecommerce-core/Payment
| Service | Tier | Justification |
|---------|------|----------------|
| **payment-gateway-service** | **Financial** | Payment gateway |
| **payment-service** | **Financial** | Payment processing |
| **payment-method-service** | **Financial** | Payment methods |
| billing-service | **Financial** | Billing transactions |
| invoice-service | **Financial** | Invoice generation |
| All other ecommerce services | **Enterprise** | E-commerce operations |

#### Category: shared-courier-core
| Service | Tier | Justification |
|---------|------|----------------|
| All services | **Enterprise** | Courier/logistics operations |

#### Category: shared-air-freight-core
| Service | Tier | Justification |
|---------|------|----------------|
| All services | **Enterprise** | Air freight operations |

#### Category: shared-ocean-shipping-core
| Service | Tier | Justification |
|---------|------|----------------|
| All services | **Enterprise** | Ocean shipping operations |

#### Category: shared-haulage-core
| Service | Tier | Justification |
|---------|------|----------------|
| All services | **Enterprise** | Haulage operations |

#### Category: shared-procurement-core
| Service | Tier | Justification |
|---------|------|----------------|
| All services | **Enterprise** | Procurement operations |

#### Category: shared-warehousing-core
| Service | Tier | Justification |
|---------|------|----------------|
| All services | **Enterprise** | Warehousing operations |

#### Category: shared-admin-core
| Service | Tier | Justification |
|---------|------|----------------|
| All services | **Enterprise** | Admin operations |

#### Backend Types
| Type | Language | Testing Framework |
|------|----------|-------------------|
| Backend/Java | Java 17 | JUnit 5, Mockito |
| Backend/Nodes | Node.js | Jest, Mocha |

---

### 3. Public-User-Domain

#### Sub-Domain: public-payment
| Service | Tier | Justification |
|---------|------|----------------|
| **payment-processing-service** | **Financial** | Payment processing |
| wallet-service | **Enterprise** | Wallet management |
| pricing-service | **Enterprise** | Pricing logic |
| refund-service | **Financial** | Refund transactions |
| transaction-history-service | **Financial** | Transaction records |

#### Sub-Domain: public-user-management
| Service | Tier | Justification |
|---------|------|----------------|
| **authentication-service** | **Financial** | User auth (security) |
| **kyc-service** | **Financial** | KYC compliance |
| **password-management-service** | **Financial** | Security (passwords) |
| **mfa-management-service** | **Financial** | Security (MFA) |
| **oauth-management-service** | **Financial** | Security (OAuth) |
| user-registration-service | **Enterprise** | User registration |
| profile-management-service | **Enterprise** | Profile management |
| user-verification-service | **Enterprise** | User verification |

#### Sub-Domain: public-booking
| Service | Tier | Justification |
|---------|------|----------------|
| booking-orchestration-service | **Enterprise** | Booking orchestration |
| All other booking services | **Enterprise** | Booking operations |

---

### 4. Foundation-domain

#### Category: ai-shared-infrastructure-Platform
| Service | Tier | Justification |
|---------|------|----------------|
| **ai-gateway-service** | **Financial** | API gateway (security) |
| ai-monitoring-service | **Enterprise** | Monitoring |
| ai-orchestration-service | **Enterprise** | Orchestration |
| ai-testing-service | **Enterprise** | Testing infrastructure |

#### Category: shared-libraries
| Service | Tier | Justification |
|---------|------|----------------|
| **shared-security-service** | **Financial** | Security libraries ✅ |
| **shared-audit-service** | **Financial** | Audit logging |
| All other shared libraries | **Enterprise** | Infrastructure |

---

### 5. Business-domain

#### Sub-Domain: Country-Finance-Dashboard
| Service | Tier | Justification |
|---------|------|----------------|
| **country-finance-service** | **Financial** | Financial data |

#### Sub-Domain: Country-Admin-Dashboard
| Service | Tier | Justification |
|---------|------|----------------|
| country-admin-service | **Enterprise** | Admin operations |

#### Sub-Domain: E-commerce/Courier-services
| Service | Tier | Justification |
|---------|------|----------------|
| **route-optimization-service** | **Enterprise** | Route optimization |
| All other services | **Enterprise** | E-commerce |

---

### 6. Management-domain
*(Analysis needed - patterns similar to Business-domain)*

---

## FINANCIAL-GRADE SERVICE SUMMARY

| Domain | Financial-Grade Services | Enterprise-Grade Services |
|--------|-------------------------|--------------------------|
| **shared-business-logics** | 2 (ai-fraud-detection, ai-security-analysis) | ~50+ |
| **shared-business-infrastructure** | 5 (payment-gateway, payment-service, payment-method, billing, invoice) | ~35+ |
| **Public-User-Domain** | 6 (payment-processing, refund, kyc, password, oauth, authentication) | ~40+ |
| **Foundation-domain** | 3 (ai-gateway, shared-security, shared-audit) | ~15+ |
| **Business-domain** | 1 (country-finance) | ~15+ |
| **Management-domain** | TBD (analysis needed) | TBD |

**Estimated Total**: ~30-35 Financial-Grade services | ~600+ Enterprise-Grade services
**Total Domains**: 6 domains

---

## PRIORITY SEQUENCE FOR TESTING

### Wave 1: Critical Financial Services (HIGH PRIORITY)
1. ✅ ai-fraud-detection-service (shared-business-logics) - **COMPLETE**
2. ai-security-analysis-service (shared-business-logics)
3. payment-gateway-service (shared-business-infrastructure)
4. payment-service (shared-business-infrastructure)
5. payment-processing-service (Public-User-Domain)
6. authentication-service (Public-User-Domain)
7. kyc-service (Public-User-Domain)
8. ai-gateway-service (Foundation-domain)

### Wave 2: Supporting Financial Services
9. All remaining payment/transaction services (billing, invoice, settlement)
10. Risk and compliance services
11. Audit and governance services

### Wave 3: Enterprise-Grade Services (BATCH PARALLEL)
12. All remaining ~600+ Enterprise-Grade services (can run in parallel across 6 domains)

---

## NEXT ACTIONS

1. ✅ **Blueprint created** - `docs/TESTING_BLUEPRINT.md`
2. ⏳ **Service classification** - This document
3. ⏳ **Build two-tier CI/CD pipeline** - gogididix-testing workflows
4. ⏳ **Test Wave 1 services** - Priority Financial-Grade services

---

**Generated for**: Cross-domain agent coordination
**Contact**: shared-business-logics (lead domain for testing approach)

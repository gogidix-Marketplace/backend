# Gogidix Ecosystem Testing Blueprint
## Cross-Domain Replication Guide

**Version**: 1.0
**Last Updated**: 2026-04-05
**Purpose**: Standardize testing approach across all domains (shared-business-logics, Foundation-domain, Management-domain, Public-User-Domain)

---

## SERVICE TIER CLASSIFICATION

### Tier 1: Financial-Grade (Critical Services)
**Use for**: Security, payments, risk, compliance, financial transactions

| Criteria | Threshold |
|----------|-----------|
| Line Coverage | 85% |
| Branch Coverage | 75% |
| Mutation Coverage | 60% |
| Test Types | Unit + Integration + Mutation |

**Service Patterns** (apply to all domains):
- `*-fraud-*`, `*-security-*`, `*-auth-*`
- `payment-*`, `transaction-*`, `settlement-*`
- `risk-*`, `compliance-*`, `aml-*`, `kyc-*`
- `accounting-*`, `ledger-*`, `billing-*`, `invoice-*`

### Tier 2: Enterprise-Grade (Standard Services)
**Use for**: Customer experience, analytics, operational services

| Criteria | Threshold |
|----------|-----------|
| Line Coverage | 70% |
| Branch Coverage | 60% |
| Mutation Coverage | Not required |
| Test Types | Unit + Integration |

**Service Patterns** (apply to all domains):
- `*-engagement-*`, `*-experience-*`
- `*-analytics-*`, `*-processing-*`, `*-validation-*`
- `notification-*`, `*-logging-*`, `*-monitoring-*`

---

## TWO-TIER CI/CD ARCHITECTURE

```
┌─────────────────────────────────────────────────────────────────────┐
│                    gogidix-testing Repository                       │
│  Purpose: Centralized testing infrastructure for ALL services        │
│                                                                     │
│  Workflows:                                                          │
│  ├── .github/workflows/financial-grade-test.yml    (Tier 1)         │
│  └── .github/workflows/enterprise-grade-test.yml    (Tier 2)         │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│              Main Repository CI/CD (per domain)                     │
│  Purpose: Deployment gates with quality checks                      │
│                                                                     │
│  Pipeline Stages:                                                   │
│  1. Code Quality (SonarQube, SpotBugs, Checkstyle)                 │
│  2. Security Scan (OWASP, Snyk, Trivy)                             │
│  3. Dependency Validation                                          │
│  4. Integration Tests                                              │
│  5. Deployment (staging → production)                              │
└─────────────────────────────────────────────────────────────────────┘
```

---

## REPLICATION STEPS FOR EACH DOMAIN

### Step 1: Classify Services (1 hour per domain)

```bash
# Run service classifier script
./scripts/classify-services-by-tier.sh <domain-path>

# Output: services-tier1.txt, services-tier2.txt
```

**Classification Rules**:
```
IF service-name CONTAINS: fraud, security, auth, payment, transaction,
   risk, compliance, aml, kyc, accounting, ledger, billing, invoice
THEN → Tier 1 (Financial-Grade)
ELSE → Tier 2 (Enterprise-Grade)
```

### Step 2: Configure Service pom.xml

Add to each service's `pom.xml`:

```xml
<properties>
    <!-- Tier 1: Financial-Grade -->
    <fg.line.minimum>85</fg.line.minimum>
    <fg.branch.minimum>75</fg.branch.minimum>
    <fg.mutation.minimum>60</fg.mutation.minimum>

    <!-- Tier 2: Enterprise-Grade -->
    <eg.line.minimum>70</eg.line.minimum>
    <eg.branch.minimum>60</eg.branch.minimum>
    <eg.mutation.skip>true</eg.mutation.skip>
</properties>
```

### Step 3: Run Tests via gogidix-testing

```bash
# Tier 1 Services (Financial-Grade)
gh workflow run financial-grade-test.yml \
  -f service=<service-name> \
  -f test_type=full \
  -f skip_mutation=false

# Tier 2 Services (Enterprise-Grade)
gh workflow run enterprise-grade-test.yml \
  -f service=<service-name> \
  -f test_type=coverage \
  -f skip_mutation=true
```

### Step 4: Configure Main Repository CI/CD

Add to each domain's main repository `.github/workflows/ci-cd.yml`:

```yaml
name: Domain CI/CD

on:
  push:
    branches: [main, dev]
  pull_request:
    branches: [main]

jobs:
  quality-gate:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Code Quality Check
        run: mvn verify
      - name: Security Scan
        run: trivy fs .
      - name: Deploy
        if: github.ref == 'refs/heads/main'
        run: ./scripts/deploy.sh
```

---

## SERVICE CATALOG TEMPLATE

| Domain | Service | Tier | Line | Branch | Mutation | Status |
|--------|---------|------|------|--------|----------|--------|
| shared-business-logics | ai-fraud-detection-service | 1 | 85% | 75% | 60% | 🔄 In Progress |
| shared-business-logics | ai-security-analysis-service | 1 | 85% | 75% | 60% | ⏳ Pending |
| shared-business-logics | ai-translation-service | 2 | 70% | 60% | N/A | ⏳ Pending |
| Foundation-domain | [service-name] | ? | ? | ? | ? | ⏳ Pending |
| Management-domain | [service-name] | ? | ? | ? | ? | ⏳ Pending |
| Public-User-Domain | [service-name] | ? | ? | ? | ? | ⏳ Pending |

---

## AGENT HANDOFF INSTRUCTIONS

### For Other Domain Agents:

1. **Copy this blueprint** to your domain's `/docs/TESTING_BLUEPRINT.md`
2. **Run classification script** on your services
3. **Configure pom.xml** files according to tier
4. **Execute tests** via gogidix-testing workflows
5. **Update catalog** with results
6. **Report back** with any issues or improvements

### Weekly Sync:

- Each domain reports tested services count
- Update master catalog at `gogidix-testing/docs/master-catalog.md`
- Identify blockers and share solutions

---

## QUICK REFERENCE COMMANDS

```bash
# Run Financial-Grade test (Tier 1)
mvn clean verify -Pfinancial-grade

# Run Enterprise-Grade test (Tier 2)
mvn clean verify -Penterprise-grade

# Generate coverage report
mvn jacoco:report

# Run mutation tests (Tier 1 only)
mvn pitest:mutationCoverage

# Check current status
./scripts/check-test-status.sh <service-name>
```

---

## SUPPORT CONTACTS

- **gogidix-testing Issues**: https://github.com/ggx-commerce/gogidix-testing/issues
- **Blueprint Updates**: Submit PR to this document
- **Agent Coordination**: @shared-business-logics (lead domain)

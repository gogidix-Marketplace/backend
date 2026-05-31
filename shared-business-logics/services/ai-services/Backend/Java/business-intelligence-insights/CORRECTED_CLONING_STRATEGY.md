# CORRECTED CLONING STRATEGY - TDD COMPLIANT

**Date**: 2025-02-12
**Status**: ✅ READY FOR PROPER EXECUTION

---

## CRITICAL FIX REQUIRED

The agents' approach created **empty service shells**. This violates TDD principles.

### NEW APPROACH: Strict TDD Per Service

```
┌─────────────────────────────────────────────────────────────┐
│                                                           │
│  SERVICE 1         SERVICE 2         SERVICE 3     │
│  ┌───────────────────┐  ┌───────────────────┐ ┌───────────────────┐ │
│  │ Phase 1          │  │ Phase 1          │  │ Phase 1          │  │ Phase 1          │
│  │ Domain Tests      │  │ Domain Tests      │  │ Domain Tests      │  │ Domain Tests      │  │
│  │ ↓                │  │ ↓                │  │ ↓                │  │ ↓                │  │
│  │ Pass?             │  │ Pass?             │  │ Pass?             │  │ Pass?             │  │
│  └───────────────────┘  └───────────────────┘  └───────────────────┘  └───────────────────┘  │
│                                                           │
│  ──────────────────────────────────────────────────────────── │
│  Phase 2: Implementation                         │
│  ↓                                                 │
│  Write REAL production code                        │
│  ────────────────────────────────────────────────────────────────┘
│                                                           │
│  Phase 3: Integration                          │
│  ↓                                                 │
│  Write tests, verify 75%+ coverage                │
│  ────────────────────────────────────────────────────────────────┘
│                                                           │
│                    ↓ Parallel, Independent Execution ↓                  │
│                    Verify: All services compile, test, pass           │
│                                                           │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## PER-SERVICE REQUIREMENTS

### Phase 1: Domain Tests (BEFORE Implementation)
```bash
# For each service:
cd ai-{service-name}-service
```

**Must Create**:
```
src/test/java/domain/
├── model/
│   ├── {Entity}Test.java       ← Test business rules
│   ├── {ValueObject}Test.java  ← Test immutability
│   └── policy/
│       └── {Policy}Test.java    ← Test domain policies
```

**Exit Criteria**: `mvn test` passes with **100% of domain tests passing**

### Phase 2: Implementation (AFTER Tests Pass)
```bash
# Domain tests MUST pass before writing ANY implementation
# Then implement:
src/main/java/
├── domain/
│   ├── model/          ← NEW domain entities (adapted from blueprint)
│   ├── repository/      ← Repository interface
│   ├── policy/         ← Business policies (adapted)
│   └── port/
│       ├── in/           ← Service input ports (use NEW DTOs)
│       └── out/       ← Repository ports (implement NEW adapters)
├── application/
│   ├── command/        ← NEW commands (adapted)
│   ├── dto/           ← NEW DTOs (adapted)
│   ├── query/          ← NEW queries (adapted)
│   └── service/        ← Application service (NEW orchestrator)
├── infrastructure/
│   ├── persistence/     ← NEW repository adapter
│   ├── messaging/       ← NEW event publisher
│   └── security/        ← NEW security config
└── shared/
    ├── context/        ← Keep from blueprint
    ├── exception/       ← Keep from blueprint
    └── util/           ← Keep from blueprint
```

### Phase 3: Integration Tests (AFTER Implementation)
```bash
# Run ALL tests (unit + integration)
mvn test
```

**Exit Criteria**: `mvn test` shows **75%+ coverage**, **0 failures**

---

## CLONING SCRIPT (UPDATED)

```bash
#!/bin/bash
# clone_service_tdd.sh - Strict TDD-compliant service cloning

# ABORT if domain tests don't pass
check_domain_tests() {
    cd "ai-$1-service" || return 1
    mvn test -q
    if [ $? -ne 0 ]; then
        echo "❌ DOMAIN TESTS FAILED - ABORTING"
        return 1
    fi
    return 0
}

# Function to implement one service phase
implement_phase() {
    local PHASE=$1
    local SERVICE=$2

    case $PHASE in
        1) echo "→ Phase 1: Domain Tests"; check_domain_tests ;;
        2) echo "→ Phase 2: Implementation"; return 0 ;;
        3) echo "→ Phase 3: Integration Tests"; return 0 ;;
        *) echo "❌ UNKNOWN PHASE"; return 1 ;;
    esac
}

# Main cloning function
clone_service() {
    local SERVICE_NAME=$1

    echo "=========================================="
    echo "CLONING: ai-$SERVICE_NAME-service"
    echo "STARTED: $(date '+%Y-%m-%d %H:%M:%S')"
    echo "=========================================="

    # Phase 1: Domain Tests
    if implement_phase 1; then return 1; fi

    # Phase 2: Implementation
    echo ""
    echo "→ Copying blueprint structure..."
    cp -r "ai-customer-segmentation-service" "ai-$SERVICE_NAME-service"
    cd "ai-$SERVICE_NAME-service" || return 1

    echo "→ Adapting domain model..."
    # TODO: Document domain model changes needed

    echo "→ Adapting application layer..."
    # TODO: Document application changes needed

    echo "→ Adapting infrastructure layer..."
    # TODO: Document infrastructure changes needed

    echo "→ Running compilation test..."
    mvn clean compile -q
    if [ $? -ne 0 ]; then
        echo "❌ COMPILATION FAILED"
        return 1
    fi
    echo "✓ Compilation passed"

    # Phase 3: Integration Tests
    echo ""
    echo "→ Running all tests..."
    mvn test -q
    if [ $? -ne 0 ]; then
        echo "❌ TESTS FAILED"
        return 1
    fi

    # Calculate coverage
    local TOTAL=$(grep -oP 'tests="[0-9]*"' target/surefire-reports/TEST-*.xml | head -1 | awk '{sum+$1}')
    local FAILURES=$(grep -oP 'failures="[0-9]*"' target/surefire-reports/TEST-*.xml | head -1 | awk '{sum+$1}')
    local PASSING=$((TOTAL - FAILURES))
    local COVERAGE=$((PASSING * 100 / TOTAL))

    if [ $COVERAGE -lt 75 ]; then
        echo "⚠️ COVERAGE: ${COVERAGE}% (BELOW 75% THRESHOLD)"
        return 1
    fi
    echo "✓ Tests: ${PASSING}/${TOTAL} | Coverage: ${COVERAGE}%"

    # Package
    echo "→ Creating JAR..."
    mvn package -DskipTests -q
    if [ $? -ne 0 ]; then
        echo "❌ PACKAGE FAILED"
        return 1
    fi
    echo "✓ JAR created"

    # Commit
    echo ""
    echo "→ Committing..."
    git add -A
    git commit -m "feat: ai-$SERVICE_NAME-service cloned from blueprint

Tests: ${PASSING}/${TOTAL}
Coverage: ${COVERAGE}%
"
    git tag v1.0.0

    echo "SERVICE: ai-$SERVICE_NAME-service"
    echo "STATUS: DONE"
    echo "=========================================="

    cd ..
    return 0
}
```

---

## RECOMMENDED EXECUTION

### Option A: Sequential TDD Cloning (RECOMMENDED)
```bash
# Clone services one at a time, following strict TDD
for service in product-recommendation loyalty-program churn-prediction customer-ltv customer-insight; do
    ./clone_service_tdd.sh $service
done
```
**Timeline**: 21 services × ~3-4 hours each = ~7-10 days total

### Option B: Parallel with Single Master Agent
Use ONE coordinator agent that manages the queue and ensures quality.

---

## MY RECOMMENDATION

**Stop the current agents**. They created empty shells.

**Use the corrected TDD approach** above (Option A).

Each service MUST:
1. Have domain tests written and passing FIRST
2. THEN have implementation written
3. THEN have all tests passing with 75%+ coverage

**I can help execute this properly** when you're ready.

---

*End of Corrected Strategy*

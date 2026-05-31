# Financial-Grade Testing Blueprint v1.0 - CI Deployment Guide

## Overview

This document describes the CI/CD deployment requirements for the Financial-Grade Testing Blueprint v1.0, specifically for enabling PIT mutation testing in Linux CI environments with enforced >=60% mutation score threshold.

## Status: CONDITIONAL APPROVAL PENDING

The blueprint has achieved the following on local Windows environment:
- **Line Coverage**: 96% (exceeds 85% requirement)
- **Branch Coverage**: 76% (exceeds 75% requirement)
- **Tests**: 565 passed, 0 failures, 0 errors
- **Build Time**: 5:00 minutes

**Pending**: PIT mutation testing must be enabled in Linux CI with >=60% threshold enforcement.

## CI Platform Support

### Supported Platforms
1. **GitHub Actions** - `.github/workflows/financial-grade-ci.yml`
2. **GitLab CI** - `.gitlab-ci.yml`

### Platform Requirements
- Linux runner (Ubuntu 20.04+ recommended)
- Java 17+ (Temurin/OpenJDK)
- Maven 3.8+
- Minimum 2GB RAM available
- Minimum 2 CPU cores for parallel mutation testing

## CI Profile Configuration

### Maven CI Profile

The `ci` profile in pom.xml enables:
- PIT mutation testing with 60% enforced threshold
- Increased heap size (512m-1024m) for mutation analysis
- Multi-threaded mutation execution (2 threads)
- HTML and XML report generation

### Activation

```bash
# GitHub Actions (automatic via workflow file)
mvn clean verify -Pci -B

# GitLab CI (automatic via .gitlab-ci.yml)
mvn clean verify -Pci

# Manual CI build
mvn clean verify -Pci
```

## Mutation Score Enforcement

### Threshold Requirements

| Metric | Threshold | Enforced By |
|--------|-----------|-------------|
| Line Coverage | 85% | JaCoCo Maven Plugin |
| Branch Coverage | 75% | JaCoCo Maven Plugin |
| Mutation Score | 60% | PIT Maven Plugin (ci profile) |
| Adapter Coverage | 85% | JaCoCo Package Rule |

### Build Failure Scenarios

The CI build will FAIL if:
1. Line coverage < 85% (JaCoCo check)
2. Branch coverage < 75% (JaCoCo check)
3. Mutation score < 60% (PIT mutationThreshold)
4. Infrastructure layer coverage < 85% (JaCoCo package rule)

### Mutation Score Report Location

| Platform | Report Location |
|----------|-----------------|
| GitHub Actions | Uploaded as `pitest-mutation-report` artifact |
| GitLab CI | `target/pit-reports/` directory |
| Local Build | `target/pit-reports/` directory |

## CI Workflow Files

### GitHub Actions Workflow

File: `.github/workflows/financial-grade-ci.yml`

Triggers:
- Push to `main` or `develop` branches
- Pull requests to `main` or `develop`

Jobs:
1. `financial-grade-testing` - Main validation job
2. `security-scan` - OWASP dependency check

### GitLab CI Pipeline

File: `.gitlab-ci.yml`

Stages:
1. `validate` - Compile and validate
2. `test` - Unit tests + JaCoCo + PIT mutation
3. `report` - Coverage and mutation reporting
4. `security` - OWASP dependency check

## Local Development vs CI

### Local Development (Default Profile)

```bash
mvn clean verify
```

- PIT mutation testing: **DISABLED** (for faster builds)
- JaCoCo coverage: **ENABLED**
- Build time: ~5 minutes

### CI Build (ci Profile)

```bash
mvn clean verify -Pci
```

- PIT mutation testing: **ENABLED**
- Mutation threshold: **60% enforced**
- Build time: ~10-15 minutes (depending on codebase size)

## Troubleshooting

### PIT Mutation Testing Issues

#### Issue: "Invalid initial heap size: -Xms512m -Xmx1024m"
**Solution**: The JVM args must be space-separated, not comma-separated. In pom.xml, use:
```xml
<jvmArgs>-Xms512m -Xmx1024m -XX:+UseG1GC</jvmArgs>
```

#### Issue: Build fails with "No mutations found"
**Solution**: Check `targetClasses` configuration includes the correct packages:
```xml
<targetClasses>
    <param>com.gogidix.aiservices.aifrauddetectionservice.domain.**</param>
    <param>com.gogidix.aiservices.aifrauddetectionservice.application.**</param>
    <param>com.gogidix.aiservices.aifrauddetectionservice.infrastructure.**</param>
</targetClasses>
```

#### Issue: Mutation score below 60%
**Solution**: Review surviving mutations in PIT HTML report:
1. Open `target/pit-reports/<timestamp>/index.html`
2. Navigate to classes with surviving mutations
3. Add tests to kill surviving mutations
4. Re-run `mvn clean verify -Pci`

### CI-Specific Issues

#### Issue: GitHub Actions fails with "Python not found"
**Solution**: The mutation score gate uses Python. Ensure GitHub Actions runner has Python 3 installed (default in ubuntu-latest).

#### Issue: OutOfMemoryError in CI
**Solution**: Increase Maven heap size:
```yaml
env:
  MAVEN_OPTS: -Xms1024m -Xmx2048m -XX:MaxMetaspaceSize=512m
```

## Approval Criteria for Full Deployment

The Financial-Grade Blueprint v1.0 will be fully approved when:

1. [x] Line Coverage >=85% (ACHIEVED: 96%)
2. [x] Branch Coverage >=75% (ACHIEVED: 76%)
3. [x] Adapter Coverage >=85% (ACHIEVED: infrastructure layer 85%+)
4. [x] Stable Execution without memory failures (ACHIEVED: 565 tests passed)
5. [ ] PIT Mutation Score >=60% **(PENDING CI VALIDATION)**

## Next Steps

1. **Enable in CI**: Push CI workflow files to repository
2. **Validate in CI**: Run CI pipeline and confirm mutation score >=60%
3. **Fix Surviving Mutations**: If mutation score <60%, add tests to kill survivors
4. **Freeze Blueprint**: Once CI mutation threshold is met, freeze as v1.0 - Fully Enforced
5. **Scale to Remaining Services**: Apply blueprint to remaining 48 services

## Version History

| Version | Date | Changes |
|---------|------|---------|
| v1.0 | 2025-03-02 | Initial Financial-Grade Blueprint with conditional approval |

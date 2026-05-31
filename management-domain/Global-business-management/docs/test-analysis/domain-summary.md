# Domain Test Analysis Summary - Global Business Management

## Overview
- **Domain**: Global Business Management
- **Analysis Date**: 2026-03-06T05:59:47Z
- **Total Services**: 15
- **Total Classes**: 127
- **Classes with Tests**: 28
- **Overall Test Coverage**: 22%
- **Total Implementation Gaps**: 0

## Service Summary

| Service | Total Classes | Classes with Tests | Test Coverage | Implementation Gaps |
|---------|---------------|-------------------|---------------|---------------------|
| batch-aggregation-service | 3 | 3 | 100% | 0 |
| business-intelligence-service | 7 | 5 | 71% | 0 |
| multi-currency-service | 15 | 1 | 6% | 0 |
| currency-conversion-service | 3 | 2 | 66% | 0 |
| country-ingestion-service | 18 | 3 | 16% | 0 |
| export-service | 3 | 1 | 33% | 0 |
| global-business-dashboard-service | 27 | 6 | 22% | 0 |
| kafka-ingestion-service | 3 | 1 | 33% | 0 |
| localization-service | 3 | 1 | 33% | 0 |
| regional-aggregation-service | 7 | 1 | 14% | 0 |
| regional-analytics-service | 2 | 0 | 0% | 0 |
| regional-dashboard-service | 6 | 0 | 0% | 0 |
| report-builder-service | 2 | 0 | 0% | 0 |
| scheduled-report-service | 2 | 0 | 0% | 0 |
| data-validation-service | 26 | 3 | 11% | 0 |

## Coverage Distribution

### High Coverage (>70%)
- batch-aggregation-service: 100%
- business-intelligence-service: 71%

### Medium Coverage (30-69%)
- currency-conversion-service: 66%
- export-service: 33%
- kafka-ingestion-service: 33%
- localization-service: 33%

### Low Coverage (1-29%)
- country-ingestion-service: 16%
- regional-aggregation-service: 14%
- global-business-dashboard-service: 22%
- data-validation-service: 11%
- multi-currency-service: 6%

### No Coverage (0%)
- regional-analytics-service
- regional-dashboard-service
- report-builder-service
- scheduled-report-service

## Detailed Implementation Gaps by Type

| Gap Type | Count |
|----------|-------|
| TODO | 0 |
| FIXME | 0 |
| STUB | 0 |
| NOT_IMPLEMENTED | 0 |
| XXX | 0 |

## Recommendations

1. **Priority Services for Test Coverage**:
   - regional-analytics-service (0% - 2 classes)
   - regional-dashboard-service (0% - 6 classes)
   - report-builder-service (0% - 2 classes)
   - scheduled-report-service (0% - 2 classes)

2. **Services Needing Improvement**:
   - data-validation-service (11% - 26 classes) - Critical for data integrity
   - multi-currency-service (6% - 15 classes) - Important for global operations
   - country-ingestion-service (16% - 18 classes) - Core service

3. **Well-Tested Services** (maintain current standards):
   - batch-aggregation-service (100%)
   - business-intelligence-service (71%)
   - currency-conversion-service (66%)

## Individual Service Reports

Each service has detailed reports in:
`[service-root]/docs/test-analysis/test-coverage-report.md`
`[service-root]/docs/test-analysis/implementation-gaps.json`

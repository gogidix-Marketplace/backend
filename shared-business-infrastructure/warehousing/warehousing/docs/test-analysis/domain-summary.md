# Test Analysis Domain Summary - shared-warehousing-core

## Overview
- **Domain**: shared-warehousing-core
- **Analysis Date**: 2026-03-06T11:04:00.513Z
- **Total Services**: 43
- **Total Classes**: 656
- **Total Classes with Tests**: 14
- **Overall Test Coverage**: 2.13%
- **Total Implementation Gaps**: 0

## Service Breakdown

| Service | Classes | Tested | Coverage % | Gaps |
|---------|---------|--------|------------|------|
| access-service | 19 | 0 | 0.00% | 0 |
| availability-service | 30 | 0 | 0.00% | 0 |
| batch-service | 29 | 0 | 0.00% | 0 |
| bin-service | 3 | 0 | 0.00% | 0 |
| carrier-service | 7 | 0 | 0.00% | 0 |
| cycle-counting-service | 17 | 0 | 0.00% | 0 |
| expiration-service | 17 | 0 | 0.00% | 0 |
| fulfillment-analytics-service | 5 | 0 | 0.00% | 0 |
| fulfillment-core-service | 15 | 2 | 13.33% | 0 |
| inventory-analytics-service | 29 | 0 | 0.00% | 0 |
| inventory-core-service | 21 | 5 | 23.81% | 0 |
| label-service | 10 | 0 | 0.00% | 0 |
| location-service | 7 | 0 | 0.00% | 0 |
| location-service | 9 | 0 | 0.00% | 0 |
| order-service | 6 | 0 | 0.00% | 0 |
| packing-service | 14 | 0 | 0.00% | 0 |
| picking-service | 14 | 0 | 0.00% | 0 |
| pricing-service | 32 | 0 | 0.00% | 0 |
| public-availability-service | 14 | 0 | 0.00% | 0 |
| public-booking-service | 14 | 0 | 0.00% | 0 |
| public-pricing-service | 11 | 0 | 0.00% | 0 |
| putaway-service | 13 | 0 | 0.00% | 0 |
| quality-service | 20 | 0 | 0.00% | 0 |
| receipt-service | 17 | 0 | 0.00% | 0 |
| receiving-service | 6 | 0 | 0.00% | 0 |
| reorder-service | 17 | 0 | 0.00% | 0 |
| reporting-service | 0 | 0 | 0.00% | 0 |
| returns-service | 21 | 0 | 0.00% | 0 |
| self-storage-service | 9 | 0 | 0.00% | 0 |
| self-storage-service | 4 | 0 | 0.00% | 0 |
| serialization-service | 32 | 0 | 0.00% | 0 |
| shelf-service | 3 | 0 | 0.00% | 0 |
| shipping-service | 24 | 0 | 0.00% | 0 |
| space-allocation-service | 17 | 0 | 0.00% | 0 |
| space-service | 19 | 2 | 10.53% | 0 |
| stock-service | 11 | 0 | 0.00% | 0 |
| stock-service | 24 | 3 | 12.50% | 0 |
| tenant-config-service | 18 | 2 | 11.11% | 0 |
| vendor-sync-service | 10 | 0 | 0.00% | 0 |
| warehouse-analytics-service | 50 | 0 | 0.00% | 0 |
| warehouse-config-service | 15 | 0 | 0.00% | 0 |
| warehouse-config-service | 0 | 0 | 0.00% | 0 |
| zone-service | 3 | 0 | 0.00% | 0 |

## Gap Summary by Type

Gap counts are aggregated across all services. See individual service reports for details.

## Recommendations

1. **Priority Services**: Focus on services with lowest test coverage
2. **Gap Resolution**: Address HIGH severity gaps first
3. **Test Strategy**: Implement tests for critical business logic classes

## Detailed Reports

Individual service reports are located in:
- `docs/test-analysis/test-coverage-report.md` in each service directory
- `docs/test-analysis/implementation-gaps.json` in each service directory

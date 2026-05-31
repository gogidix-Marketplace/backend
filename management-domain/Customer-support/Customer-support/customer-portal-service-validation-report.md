# BLUEPRINT VALIDATION REPORT: Customer Portal Service

**Generated:** 2026-03-24
**Service Path:** Customer-support/Backend/Java/customer-portal-service

## SUMMARY

| Metric | Compliance |
|--------|------------|
| **Overall** | **60%** |
| POM Configuration | 75% |
| Domain Model Pattern | 40% |
| Structure & Packages | 65% |

## POM

### Passes
- **Spring Boot 3.2.0 (3.x series)**: Current version meets Financial-Grade requirements
- **Java 17**: Correct Java version specified
- **Coverage minimum 0.85 (>=85%)**: Meets Financial-Grade coverage threshold
- **JaCoCo plugin present**: Coverage tool configured correctly
- **PIT mutation plugin present**: Mutation testing configured with 60% threshold

### Gaps
- **Missing haltOnFailure in PIT configuration**: Mutation testing should halt build on failure
- **PIT mutation threshold 60%**: Below recommended Financial-Grade threshold of 75%
- **Missing Spring Cloud dependencies**: Should include spring-cloud-dependencies for resilience patterns

## DOMAIN_MODEL

### Critical Issues
- **Uses Lombok @SuperBuilder**: Should implement manual Builder pattern instead
- **Extends BaseEntity**: Financial-Grade blueprint has NO base class inheritance
- **Has @Document annotation on domain model**: Should be separate Entity class with toDomainModel() method

### Minor Issues
- **Fields not final**: Should be immutable for Financial-Grade compliance
- **Missing manual Builder pattern**: Domain models lack explicit Builder class

## STRUCTURE

### Passes
- Test ratio present

### Issues
- **Missing 8 packages**: domain/port/in, domain/port/out, domain/policy, domain/service, application/dto/request, application/dto/response, infrastructure/messaging, infrastructure/messaging/kafka

## RECOMMENDED ACTIONS

### Critical (Must Fix)
1. Remove BaseEntity inheritance from CustomerProfile and TicketHistory domain models
2. Remove @SuperBuilder and implement manual Builder pattern
3. Create separate MongoDB Entity class with @Document annotation
4. Add toDomainModel() conversion method in Entity class

### Major (Should Fix)
5. Make domain model fields final for immutability
6. Increase PIT mutation threshold from 60% to 75%
7. Add haltOnFailure=true to PIT plugin configuration
8. Add Spring Cloud dependencies for circuit breaker patterns

### Minor (Nice to Fix)
9. Create missing domain packages (port/in, port/out, policy, service)
10. Split application/dto into request and response sub-packages
11. Add infrastructure/messaging and infrastructure/messaging/kafka packages

## CLASSIFICATION: YELLOW

This service requires significant domain model refactoring to meet Financial-Grade standards. POM configuration is mostly compliant but needs minor enhancements.

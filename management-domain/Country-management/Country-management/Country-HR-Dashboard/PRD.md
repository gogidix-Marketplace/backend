# Country HR Dashboard - Product Requirements Document

> **Version:** 1.0.0
> **Last Updated:** 2026-03-18
> **Domain:** business-domain/Country-HR-Dashboard
> **Purpose**: Country Human Resources Operations

---

## Domain Purpose

Provides country-level HR operations and reports HR data to management-domain/Human-resource.

---

## Key Features

- Employee management
- Payroll administration
- Leave management
- Performance reviews
- Training programs
- HR compliance

---

## Technology Stack

- **React** + **TypeScript**
- **Java** + **Spring Boot**
- **PostgreSQL** (HR data)

---

## Integration Points

### Reports Sent To
- management-domain/Human-resource

### Kafka Topics (Produced)
- `country.hr.daily` - Daily HR summary
- `country.hr.payroll` - Payroll data

---

*For more details, see main documentation in Gogidix-ecosystem-architectural/*

**Document Owner:** Country Operations Team

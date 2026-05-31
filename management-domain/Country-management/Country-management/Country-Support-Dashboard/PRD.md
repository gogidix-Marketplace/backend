# Country Support Dashboard - Product Requirements Document

> **Version:** 1.0.0
> **Last Updated:** 2026-03-18
> **Domain:** business-domain/Country-Support-Dashboard
> **Purpose**: Country Customer Support Operations

---

## Domain Purpose

Provides country-level customer support operations and reports support metrics to management-domain/Customer-support.

---

## Key Features

- Support ticket management
- Customer issue tracking
- Support team performance
- SLA monitoring
- Knowledge base
- Live chat support

---

## Technology Stack

- **React** + **TypeScript** (Frontend)
- **Java** + **Spring Boot** (Backend)
- **MongoDB** (tickets)

---

## Integration Points

### Reports Sent To
- management-domain/Customer-support

### Kafka Topics (Produced)
- `country.support.daily` - Daily support summary
- `country.support.sla` - SLA compliance metrics

---

*For more details, see main documentation in Gogidix-ecosystem-architectural/*

**Document Owner:** Country Operations Team

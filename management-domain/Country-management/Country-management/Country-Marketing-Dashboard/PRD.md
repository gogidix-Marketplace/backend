# Country Marketing Dashboard - Product Requirements Document

> **Version:** 1.0.0
> **Last Updated:** 2026-03-18
> **Domain:** business-domain/Country-Marketing-Dashboard
> **Purpose**: Country Marketing Operations

---

## Domain Purpose

Provides country-level marketing operations and reports marketing data to management-domain/Digital-marketing.

---

## Key Features

- Local marketing campaigns
- Lead generation
- Marketing analytics
- Brand management
- Social media management
- Content management

---

## Technology Stack

- **React** + **TypeScript**
- **Java** + **Spring Boot**
- **MongoDB** (campaigns)

---

## Integration Points

### Reports Sent To
- management-domain/Digital-marketing

### Kafka Topics (Produced)
- `country.marketing.daily` - Daily marketing summary
- `country.marketing.leads` - Lead generation data

---

*For more details, see main documentation in Gogidix-ecosystem-architectural/*

**Document Owner:** Country Operations Team

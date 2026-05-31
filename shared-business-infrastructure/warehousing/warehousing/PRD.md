# Shared Warehousing Core - Product Requirements Document

> **Version:** 1.0.0
> **Last Updated:** 2026-03-18
> **Domain:** shared-business-infrastructure/shared-warehousing-core
> **Purpose**: Warehouse Aggregation Platform

---

## Domain Purpose

Aggregates SME warehouse providers and provides warehousing services including inventory storage, fulfillment, and warehouse management system (WMS) integration.

---

## Services Overview

| Service | Purpose |
|---------|---------|
| inventory-service | Core inventory management |
| warehouse-integration-service | WMS integration |
| fulfillment-service | Order fulfillment |
| storage-service | Public storage booking |
| fulfillment-orchestrator-service | Fulfillment workflow |

---

## Technology Stack

- **Java 17** + **Spring Boot 3.1.5**
- **MongoDB** (primary)
- **Apache Kafka** (events)

---

## Integration Points

### Consumers
- business-domain/E-commerce (fulfillment)
- Public-User-Domain (storage booking)

---

*For more details, see main documentation in Gogidix-ecosystem-architectural/*

**Document Owner:** Warehousing Team

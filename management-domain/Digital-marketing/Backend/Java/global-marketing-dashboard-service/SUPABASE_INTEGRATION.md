# Global Marketing Dashboard Service - Supabase Integration

## Overview

This document describes the Supabase integration for the Global Marketing Dashboard Service.

## Supabase Project Details

- **Project URL**: https://jbtozjinbrfibzwxbnpl.supabase.co
- **Project Reference**: jbtozjinbrfibzwxbnpl
- **Anon Key**: sb_publishable_s_d7BtMw3aMElRNRpQWt2g_5g97orsX
- **Database**: PostgreSQL (hosted by Supabase)

## Changes Made

### 1. Maven Dependencies (pom.xml)

**Removed:**
- `spring-boot-starter-data-mongodb`
- `spring-boot-starter-oauth2-resource-server`

**Added:**
- `spring-boot-starter-data-jpa`
- `org.postgresql:postgresql:42.6.0`
- `com.zaxxer:HikariCP`
- `io.supabase:supabase-java:2.3.1`
- `io.supabase:gotrue-jwt:1.1.1`

### 2. Configuration (application.yml)

**Supabase Configuration Added:**
```yaml
supabase:
  project-url: ${SUPABASE_URL:https://jbtozjinbrfibzwxbnpl.supabase.co}
  project-ref: ${SUPABASE_PROJECT:jbtozjinbrfibzwxbnpl}
  anon-key: ${SUPABASE_ANON_KEY:sb_publishable_s_d7BtMw3aMElRNRpQWt2g_5g97orsX}
  jwt:
    secret: ${SUPABASE_JWT_SECRET:default-secret-key-change-in-production}
    expiration: ${SUPABASE_JWT_EXPIRATION:3600000}
  api:
    timeout: 30000
  realtime:
    enabled: true
```

**DataSource Configuration:**
```yaml
datasource:
  url: jdbc:postgresql://postgres:[YOUR-PASSWORD]@db.jbtozjinbrfibzwxbnpl.supabase.co:5432/postgres
  driver-class-name: org.postgresql.Driver
```

### 3. Entity Changes

**MongoDB → Supabase (JPA/Hibernate):**

| Old Class (MongoDB) | New Class (Supabase) |
|------------------------|----------------------|
| `@Document(collection)` | `@Table(name)` |
| `@TypeAlias` | N/A |
| Spring Data MongoDB | Spring Data JPA |
| `Instant timestamp` | `Instant timestamp` + `LocalDateTime created_at` |
| `ObjectId id` | `Long id` with `@GeneratedValue` |
| Manual `touch()` | `@PreUpdate` hook |

### 4. New Architecture

```
┌─────────────────────────────────────────────────────────────────────────┐
│                    Global Marketing Dashboard Service                │
├─────────────────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │                  Supabase                         │   │
│  │                  (PostgreSQL + Auth + Realtime)           │   │
│  └─────────────────────────────────────────────────────────────────┘   │
│                                                             │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │              Application Layer (Spring Boot)              │   │
│  │                                                        │   │
│  │  ┌──────────────────────────────────────────────────┐   │   │
│  │  │  Controller              │   │
│  │  │                        │   │   │
│  │  └──────────────────────────────────────────┘   │   │
│  │                                                        │   │
│  └─────────────────────────────────────────────────────────────────┘   │
│                                                             │
└─────────────────────────────────────────────────────────────────────────┘
```

## File Structure

### Configuration
```
src/main/resources/
├── application.yml
└── application-prod.yml
```

### Domain Layer
```
src/main/java/com/gogidix/marketing/globaldashboard/domain/model/
├── DashboardMetric.java (Supabase entity)
├── CountryStats.java (Supabase entity)
└── GlobalAlert.java (Supabase entity)
```

### Repository Layer
```
src/main/java/com/gogidix/marketing/globaldashboard/domain/repository/
├── DashboardMetricRepository.java (JPA)
├── CountryStatsRepository.java (JPA)
└── GlobalAlertRepository.java (JPA)
```

### Service Layer
```
src/main/java/com/gogidix/marketing/globaldashboard/infrastructure/service/
└── SupabaseService.java
```

### Security Layer
```
src/main/java/com/gogidix/marketing/globaldashboard/infrastructure/config/
├── SupabaseConfig.java
├── SupabaseSecurityConfig.java
├── SupabaseAuthenticationManager.java
├── SupabaseAuthenticationToken.java
├── SupabaseJwtAuthenticationFilter.java
├── SupabaseJwtAuthorizationFilter.java
├── SupabaseAuthenticationEntryPoint.java
└── SupabaseAuditorProvider.java
```

### Controller Layer
```
src/main/java/com/gogidix/marketing/globaldashboard/application/controller/
├── AuthController.java (Supabase auth endpoints)
└── DashboardController.java (Supabase integration)
```

## API Endpoints

### Authentication Endpoints

| Method | Endpoint | Description |
|---------|-----------|-------------|
| POST | `/api/auth/signin` | Sign in with email/password, returns JWT |
| POST | `/api/auth/validate` | Validate JWT token, returns user info |
| POST | `/api/auth/refresh` | Refresh JWT token |
| POST | `/api/auth/signout` | Sign out (invalidates token) |

### Dashboard Endpoints

| Method | Endpoint | Description |
|---------|-----------|-------------|
| GET | `/api/dashboard/metrics` | Get all metrics (filtered by tenant) |
| GET | `/api/dashboard/metrics/{id}` | Get metric by ID |
| POST | `/api/dashboard/metrics` | Create new metric |
| PUT | `/api/dashboard/metrics/{id}` | Update metric |
| DELETE | `/api/dashboard/metrics/{id}` | Delete metric |
| GET | `/api/dashboard/country-stats` | Get all country stats (filtered by tenant) |
| POST | `/api/dashboard/country-stats` | Create new country stats |
| GET | `/api/dashboard/alerts` | Get all alerts (filtered) |
| POST | `/api/dashboard/alerts` | Create new alert |
| POST | `/api/dashboard/alerts/{id}/acknowledge` | Acknowledge alert |
| POST | `/api/dashboard/alerts/{id}/resolve` | Resolve alert |
| GET | `/api/dashboard/statistics` | Get dashboard statistics |

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `SUPABASE_URL` | https://jbtozjinbrfibzwxbnpl.supabase.co | Supabase project URL |
| `SUPABASE_PROJECT` | jbtozjinbrfibzwxbnpl | Supabase project reference |
| `SUPABASE_ANON_KEY` | sb_publishable_s_d7BtMw3aMElRNRpQWt2g_5g97orsX | Supabase anon key |
| `SUPABASE_JWT_SECRET` | default-secret-key-change-in-production | Supabase JWT secret |
| `SUPABASE_JWT_EXPIRATION` | 3600000 | Token expiration (1 hour) |
| `SERVICE_PORT` | 8080 | Service port |

## Migration Guide

### From MongoDB to Supabase

1. **Data Migration**
   - Export existing MongoDB data
   - Import to Supabase PostgreSQL
   - Use Supabase SQL Editor for data mapping

2. **Authentication Migration**
   - Update client authentication to use `/api/auth/signin`
   - Replace OAuth2 resource server calls with Supabase JWT

3. **Testing**
   - Test endpoints with local Supabase project
   - Verify JWT token validation
   - Test multi-tenant isolation

## Building the Service

```bash
# Navigate to service directory
cd x-gogidix-domain/Management-domain/Digital-marketing/Backend/Java/global-marketing-dashboard-service

# Build with Maven (ensure Maven 3.9.12 is in PATH)
export PATH="/c/Users/TEMP.LAPTOP-1QDBFFCA/Desktop/apache-maven-3.9.12/bin:$PATH"
mvn clean package

# Run the service
mvn spring-boot:run
```

## Next Steps

1. [ ] Build the service with Maven
2. [ ] Test authentication endpoints
3. [ ] Test dashboard endpoints
4. [ ] Verify multi-tenant isolation
5. [ ] Deploy to production

## Notes

- All MongoDB repositories have been replaced with Supabase services
- JWT authentication now uses Supabase GoTrue JWT
- Real-time subscriptions can be enabled via Supabase Realtime
- Database migrations can be managed via Supabase Migrations

---

**Generated**: 2024-03-27
**Project**: Gogidix Global Marketing Dashboard Service

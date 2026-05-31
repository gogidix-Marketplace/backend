# JWT Authentication Setup Guide

**Version:** 1.0  
**Last Updated:** 2026-03-03  
**Auth Service:** http://localhost:8081/auth-service/realms/gogidix

---

## Overview

All services in the Gogidix ecosystem now use JWT-based authentication through a centralized auth-service. This guide covers the setup and configuration required for deploying services with JWT authentication.

---

## Architecture

```
┌─────────────┐     JWT Token      ┌──────────────┐
│   Client    │ ─────────────────► │   Service    │
│ (Frontend)  │                    │  (Backend)    │
└─────────────┘                    └──────────────┘
       │                                   │
       │                                   │
       └───────────────────────────────────┘
                   │ Validates JWT
                   ▼
          ┌────────────────┐
          │ auth-service   │
          │ (Keycloak)     │
          │ Port: 8081     │
          └────────────────┘
```

---

## Prerequisites

1. **auth-service** must be running and accessible
2. Services must have Spring Security OAuth2 dependencies
3. JWKS endpoint must be accessible: `http://localhost:8081/auth-service/realms/gogidix/protocol/openid-connect/certs`

---

## Configuration

### 1. Maven Dependencies

Ensure your `pom.xml` includes:

```xml
<dependencies>
    <!-- Spring Security OAuth2 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
    </dependency>
</dependencies>
```

### 2. application.yml Configuration

Add JWT configuration to your `src/main/resources/application.yml`:

```yaml
spring:
  application:
    name: your-service-name
  
  # Security Configuration for JWT
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8081/auth-service/realms/gogidix
          jwk-set-uri: http://localhost:8081/auth-service/realms/gogidix/protocol/openid-connect/certs
```

### 3. SecurityConfig.java

Each service has a `SecurityConfig.java` in the infrastructure/security package with:

- JWT decoding from auth-service JWKS endpoint
- Role extraction from `realm_access.roles` claim
- Public endpoints for health checks and Swagger UI

---

## JWT Token Structure

### Claims

```json
{
  "sub": "user-123",
  "tenant_id": "tenant-001",
  "realm_access": {
    "roles": ["USER", "ADMIN"]
  },
  "exp": 1234567890
}
```

### Role Mapping

JWT roles are automatically mapped to Spring Security roles:
- `realm_access.roles[0]` → `ROLE_USER`
- `realm_access.roles[1]` → `ROLE_ADMIN`

---

## Public Endpoints

The following endpoints are publicly accessible (no authentication required):

- `/actuator/health` - Health check
- `/actuator/info` - Application info
- `/swagger-ui/**` - Swagger UI
- `/api-docs/**` - OpenAPI documentation
- `/swagger-ui.html` - Swagger UI

All other endpoints require valid JWT authentication.

---

## Deployment Steps

### 1. Start auth-service

```bash
cd auth-service
docker-compose up -d
```

### 2. Verify auth-service is running

```bash
curl http://localhost:8081/auth-service/realms/gogidix/.well-known/openid-configuration
```

### 3. Start your services

```bash
java -jar your-service.jar
```

### 4. Test authentication

```bash
# Get a token from auth-service
TOKEN=$(curl -X POST http://localhost:8081/auth-service/realms/gogidix/protocol/openid-connect/token \
  -d "client_id=your-client" \
  -d "username=your-username" \
  -d "password=your-password" \
  -d "grant_type=password" | jq -r '.access_token')

# Access protected endpoint
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8081/api/v1/protected-endpoint
```

---

## Environment Variables

For production deployment, use environment variables:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: ${AUTH_SERVICE_ISSUER_URI:http://localhost:8081/auth-service/realms/gogidix}
          jwk-set-uri: ${AUTH_SERVICE_JWKS_URI:http://localhost:8081/auth-service/realms/gogidix/protocol/openid-connect/certs}
```

---

## Troubleshooting

### Issue: 401 Unauthorized

**Cause:** Invalid or missing JWT token

**Solution:**
1. Verify token is not expired
2. Check token issuer matches configured issuer-uri
3. Ensure auth-service is accessible

### Issue: JWKS download failed

**Cause:** Cannot reach auth-service JWKS endpoint

**Solution:**
1. Verify auth-service is running
2. Check network connectivity
3. Verify JWKS URI is correct

### Issue: Role-based access not working

**Cause:** Roles not properly extracted from token

**Solution:**
1. Verify token contains `realm_access.roles` claim
2. Check role prefix is `ROLE_`
3. Ensure `@PreAuthorize` annotations use correct role names

---

## Services Status

| Domain | Services | JWT Status |
|--------|----------|------------|
| Executive-domain | 10 | ✅ Complete |
| Digital-marketing | 15 | ✅ Complete |
| Finance-department | 17 | ✅ Complete |
| Human-resource | 14 | ✅ Complete |
| Sales-department | 12 | ✅ Complete |
| Customer-support | 12 | ✅ Complete |
| Global-business-management | 15 | ✅ Complete |
| System-Administrator | 14 | ✅ Complete |
| shared-business-infrastructure | 100+ | ✅ Complete |

---

## Support

For issues or questions:
1. Check auth-service logs
2. Review application logs for security errors
3. Verify JWT token at https://jwt.io/

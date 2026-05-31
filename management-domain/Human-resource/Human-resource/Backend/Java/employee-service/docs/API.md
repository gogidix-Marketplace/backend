# Employee Service - API Documentation

## Base URL

```
Production:  https://api.gogidix.com/hr/employee-service/v1
Development: https://dev-api.gogidix.com/hr/employee-service/v1
```

## Authentication

All requests require a valid JWT token in the Authorization header:

```
Authorization: Bearer <jwt_token>
```

### Required Headers

| Header | Description | Example |
|--------|-------------|---------|
| `Authorization` | JWT bearer token | `Bearer eyJhbGc...` |
| `X-Tenant-ID` | Tenant identifier | `tenant-123` |
| `Content-Type` | Request content type | `application/json` |

---

## Endpoints

### 1. Create Employee

Create a new employee record.

**Endpoint**: `POST /api/employees`

**Permissions**: `HR_ADMIN`

**Request Body**:

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@gogidix.com",
  "department": "Engineering",
  "position": "Software Engineer",
  "level": "MID_LEVEL",
  "employmentType": "PERMANENT",
  "hireDate": "2024-03-01",
  "salary": 75000.00,
  "currency": "USD",
  "salaryFrequency": "MONTHLY",
  "managerId": "mgr-001",
  "countryCode": "US",
  "location": "New York",
  "workSchedule": "FULL_TIME"
}
```

**Response**: `201 Created`

```json
{
  "id": "emp-12345678",
  "employeeNumber": "EMP-A1B2C3D4",
  "tenantId": "tenant-123",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@gogidx.com",
  "department": "Engineering",
  "position": "Software Engineer",
  "level": "MID_LEVEL",
  "status": "PENDING_ONBOARDING",
  "active": true,
  "hireDate": "2024-03-01",
  "salary": 75000.00,
  "createdAt": "2024-02-23T10:30:00Z",
  "createdBy": "hr-user"
}
```

**Error Responses**:

| Code | Description |
|------|-------------|
| 400 | Validation error |
| 409 | Email already exists |
| 422 | Unprocessable entity |

---

### 2. Get Employee by ID

Retrieve a specific employee by ID.

**Endpoint**: `GET /api/employees/{id}`

**Permissions**: `ALL_USERS`

**Path Parameters**:

| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | string | Employee ID |

**Response**: `200 OK`

```json
{
  "id": "emp-12345678",
  "employeeNumber": "EMP-A1B2C3D4",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@gogidix.com",
  "status": "ACTIVE",
  "level": "MID_LEVEL",
  "department": "Engineering",
  "position": "Software Engineer",
  "hireDate": "2024-03-01",
  "yearsOfService": 0,
  "skills": ["Java", "Spring", "React"],
  "certifications": [],
  "directReports": []
}
```

**Error Responses**:

| Code | Description |
|------|-------------|
| 404 | Employee not found |

---

### 3. Get Employee by Number

Retrieve an employee by their employee number.

**Endpoint**: `GET /api/employees/number/{employeeNumber}`

**Permissions**: `ALL_USERS`

**Path Parameters**:

| Parameter | Type | Description |
|-----------|------|-------------|
| `employeeNumber` | string | Employee number (e.g., EMP-A1B2C3D4) |

**Response**: Same as Get Employee by ID

---

### 4. Search Employees

Search for employees with pagination.

**Endpoint**: `GET /api/employees/search`

**Permissions**: `HR_STAFF`

**Query Parameters**:

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `q` | string | No | Search term (searches name, email, department) |
| `status` | string | No | Filter by status (ACTIVE, INACTIVE, etc.) |
| `department` | string | No | Filter by department ID |
| `level` | string | No | Filter by employee level |
| `page` | integer | No | Page number (default: 0) |
| `size` | integer | No | Page size (default: 20, max: 100) |
| `sort` | string | No | Sort field (default: lastName) |
| `direction` | string | No | Sort direction (ASC, DESC, default: ASC) |

**Example Request**:

```
GET /api/employees/search?q=John&status=ACTIVE&department=dept-001&page=0&size=20
```

**Response**: `200 OK`

```json
{
  "content": [
    {
      "id": "emp-001",
      "employeeNumber": "EMP-A1B2C3D4",
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@gogidix.com",
      "status": "ACTIVE",
      "department": "Engineering"
    }
  ],
  "pageable": {
    "page": 0,
    "size": 20,
    "total": 45,
    "totalPages": 3
  }
}
```

---

### 5. Get Active Employees

Get all active employees for the current tenant.

**Endpoint**: `GET /api/employees/active`

**Permissions**: `HR_STAFF`

**Response**: `200 OK`

```json
[
  {
    "id": "emp-001",
    "employeeNumber": "EMP-A1B2C3D4",
    "firstName": "John",
    "lastName": "Doe",
    "status": "ACTIVE"
  }
]
```

---

### 6. Update Employee

Update employee information.

**Endpoint**: `PUT /api/employees/{id}`

**Permissions**: `HR_ADMIN`, `EMPLOYEE (own basic info only)`

**Request Body** (partial update supported):

```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@gogidix.com",
  "phone": "+1-555-1234",
  "mobile": "+1-555-5678",
  "location": "San Francisco",
  "bio": "Senior software engineer"
}
```

**Response**: `200 OK`

```json
{
  "id": "emp-001",
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@gogidix.com",
  "updatedAt": "2024-02-23T11:00:00Z"
}
```

---

### 7. Activate Employee

Activate an employee pending onboarding.

**Endpoint**: `POST /api/employees/{id}/activate`

**Permissions**: `HR_ADMIN`

**Response**: `200 OK`

```json
{
  "id": "emp-001",
  "status": "ACTIVE",
  "active": true,
  "verified": true,
  "activatedAt": "2024-02-23T11:00:00Z"
}
```

---

### 8. Deactivate Employee

Deactivate an active employee.

**Endpoint**: `POST /api/employees/{id}/deactivate`

**Permissions**: `HR_ADMIN`

**Response**: `200 OK`

```json
{
  "id": "emp-001",
  "status": "INACTIVE",
  "active": false,
  "deactivatedAt": "2024-02-23T11:00:00Z"
}
```

---

### 9. Terminate Employee

Terminate an employee's employment.

**Endpoint**: `POST /api/employees/{id}/terminate`

**Permissions**: `HR_ADMIN`

**Request Body**:

```json
{
  "reason": "Performance issues",
  "category": "INVOLUNTARY",
  "lastWorkingDay": "2024-04-15"
}
```

**Response**: `200 OK`

```json
{
  "id": "emp-001",
  "status": "TERMINATED",
  "active": false,
  "terminationReason": "Performance issues",
  "terminationCategory": "INVOLUNTARY",
  "terminationDate": "2024-02-23T11:00:00Z",
  "lastWorkingDay": "2024-04-15"
}
```

---

### 10. Process Resignation

Process an employee resignation.

**Endpoint**: `POST /api/employees/{id}/resign`

**Permissions**: `HR_ADMIN`

**Request Body**:

```json
{
  "reason": "New opportunity",
  "lastWorkingDay": "2024-04-01",
  "rehireEligibility": "YES"
}
```

**Response**: `200 OK`

```json
{
  "id": "emp-001",
  "status": "RESIGNED",
  "active": false,
  "exitReason": "New opportunity",
  "lastWorkingDay": "2024-04-01",
  "rehireEligibility": "YES"
}
```

---

### 11. Promote Employee

Promote an employee to a higher level.

**Endpoint**: `POST /api/employees/{id}/promote`

**Permissions**: `HR_MANAGER`

**Request Body**:

```json
{
  "newLevel": "SENIOR",
  "newPosition": "Senior Software Engineer",
  "newSalary": 90000.00
}
```

**Response**: `200 OK`

```json
{
  "id": "emp-001",
  "level": "SENIOR",
  "position": "Senior Software Engineer",
  "salary": 90000.00,
  "promotedAt": "2024-02-23T11:00:00Z"
}
```

---

### 12. Transfer Employee

Transfer an employee to a new department.

**Endpoint**: `POST /api/employees/{id}/transfer`

**Permissions**: `HR_MANAGER`

**Request Body**:

```json
{
  "newDepartmentId": "dept-456",
  "newDepartment": "Sales",
  "newPosition": "Sales Engineer",
  "newManagerId": "mgr-789"
}
```

**Response**: `200 OK`

```json
{
  "id": "emp-001",
  "departmentId": "dept-456",
  "department": "Sales",
  "position": "Sales Engineer",
  "managerId": "mgr-789",
  "transferredAt": "2024-02-23T11:00:00Z"
}
```

---

### 13. Update Salary

Update an employee's salary.

**Endpoint**: `POST /api/employees/{id}/salary`

**Permissions**: `HR_ADMIN`

**Request Body**:

```json
{
  "newSalary": 85000.00,
  "reason": "Annual merit increase"
}
```

**Response**: `200 OK`

```json
{
  "id": "emp-001",
  "salary": 85000.00,
  "salaryUpdatedAt": "2024-02-23T11:00:00Z"
}
```

---

### 14. Add Skill

Add a skill to an employee's profile.

**Endpoint**: `POST /api/employees/{id}/skills`

**Permissions**: `HR_STAFF`, `EMPLOYEE (own profile)`

**Request Body**:

```json
{
  "skill": "Kubernetes"
}
```

**Response**: `200 OK`

```json
{
  "id": "emp-001",
  "skills": ["Java", "Spring", "React", "Kubernetes"]
}
```

---

### 15. Add Certification

Add a certification to an employee's profile.

**Endpoint**: `POST /api/employees/{id}/certifications`

**Permissions**: `HR_STAFF`, `EMPLOYEE (own profile)`

**Request Body**:

```json
{
  "certification": "AWS Solutions Architect Professional"
}
```

**Response**: `200 OK`

```json
{
  "id": "emp-001",
  "certifications": ["AWS Solutions Architect Professional"]
}
```

---

### 16. Add Direct Report

Add a direct report to a manager.

**Endpoint**: `POST /api/managers/{managerId}/direct-reports`

**Permissions**: `HR_ADMIN`

**Request Body**:

```json
{
  "employeeId": "emp-002"
}
```

**Response**: `200 OK`

```json
{
  "managerId": "mgr-001",
  "directReportIds": ["emp-002", "emp-003"],
  "directReportCount": 2
}
```

---

### 17. Get Employee Count

Get employee counts by various criteria.

**Endpoint**: `GET /api/employees/count`

**Permissions**: `HR_STAFF`

**Query Parameters**:

| Parameter | Type | Description |
|-----------|------|-------------|
| `status` | string | Filter by employee status |
| `department` | string | Filter by department ID |

**Example Request**:

```
GET /api/employees/count?status=ACTIVE
```

**Response**: `200 OK`

```json
{
  "count": 156
}
```

---

### 18. Bulk Operations

Perform bulk operations on multiple employees.

**Endpoint**: `POST /api/employees/bulk`

**Permissions**: `HR_ADMIN`

**Request Body**:

```json
{
  "operation": "UPDATE_DEPARTMENT",
  "employeeIds": ["emp-001", "emp-002", "emp-003"],
  "parameters": {
    "newDepartmentId": "dept-456",
    "newDepartment": "Sales"
  }
}
```

**Supported Operations**:

| Operation | Description |
|-----------|-------------|
| `UPDATE_DEPARTMENT` | Bulk department transfer |
| `UPDATE_STATUS` | Bulk status update |
| `EXPORT` | Export employee data |

**Response**: `200 OK`

```json
{
  "operationId": "bulk-op-123",
  "status": "COMPLETED",
  "processed": 3,
  "succeeded": 3,
  "failed": 0
}
```

---

## Enums

### EmployeeStatus

| Value | Description |
|-------|-------------|
| `ACTIVE` | Employee is active |
| `INACTIVE` | Employee is inactive |
| `ON_LEAVE` | Employee is on leave |
| `TERMINATED` | Employment terminated |
| `RESIGNED` | Employee resigned |
| `PENDING_ONBOARDING` | Awaiting onboarding completion |
| `PENDING_TERMINATION` | Awaiting termination processing |

### EmploymentType

| Value | Description |
|-------|-------------|
| `PERMANENT` | Permanent employment |
| `CONTRACT` | Contract employment |
| `INTERN` | Intern |
| `CONSULTANT` | Consultant |
| `TEMPORARY` | Temporary employment |
| `FREELANCE` | Freelance contractor |

### EmployeeLevel

| Value | Description |
|-------|-------------|
| `ENTRY` | Entry level |
| `JUNIOR` | Junior level |
| `MID_LEVEL` | Mid-level |
| `SENIOR` | Senior level |
| `LEAD` | Lead/Principal |
| `MANAGER` | Manager |
| `DIRECTOR` | Director |
| `VP` | Vice President |
| `EXECUTIVE` | Executive/C-level |

---

## Error Responses

All error responses follow this structure:

```json
{
  "timestamp": "2024-02-23T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "email",
      "message": "Email is required"
    }
  ],
  "path": "/api/employees"
}
```

### Common Error Codes

| Code | Title | Description |
|------|-------|-------------|
| 400 | Bad Request | Invalid request data |
| 401 | Unauthorized | Missing or invalid token |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Resource already exists |
| 422 | Unprocessable Entity | Business rule violation |
| 500 | Internal Server Error | Server error |

---

## Rate Limiting

| Tier | Limit | Window |
|------|-------|--------|
| Free | 100 requests | 1 minute |
| Standard | 1000 requests | 1 minute |
| Enterprise | Unlimited | - |

Rate limit headers are included in responses:

```
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 950
X-RateLimit-Reset: 1708694400
```

---

## SDK Examples

### Java (Spring RestTemplate)

```java
String url = "https://api.gogidix.com/hr/employee-service/v1/employees/{id}";
ResponseEntity<Employee> response = restTemplate.getForEntity(url, Employee.class, employeeId);
```

### JavaScript (axios)

```javascript
const response = await axios.get(
  'https://api.gogidix.com/hr/employee-service/v1/employees/emp-001',
  {
    headers: {
      'Authorization': `Bearer ${token}`,
      'X-Tenant-ID': 'tenant-123'
    }
  }
);
```

### Python (requests)

```python
response = requests.get(
    'https://api.gogidix.com/hr/employee-service/v1/employees/emp-001',
    headers={
        'Authorization': f'Bearer {token}',
        'X-Tenant-ID': 'tenant-123'
    }
)
```

---

## Changelog

### Version 1.2.0 (2024-03-01)
- Added bulk operations endpoint
- Enhanced search with filters

### Version 1.1.0 (2024-02-15)
- Added skill and certification management
- Added direct report management

### Version 1.0.0 (2024-01-01)
- Initial release

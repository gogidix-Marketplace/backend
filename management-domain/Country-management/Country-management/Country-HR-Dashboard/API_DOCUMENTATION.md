# Country HR Dashboard - API Documentation

## Base URL
```
http://localhost:8080/api/v1/country-hr
```

## Authentication
All requests require JWT token in Authorization header:
```
Authorization: Bearer <token>
```

Country code is passed via header:
```
X-Country-Code: NGA
```

---

## Dashboard APIs

### Get Dashboard Overview
```http
GET /dashboard/overview
```

**Response:**
```json
{
  "healthScore": {
    "score": 94,
    "trend": 2,
    "level": "Excellent"
  },
  "employeeStats": {
    "totalEmployees": 347,
    "activeEmployees": 320,
    "onLeave": 23,
    "newHiresThisMonth": 8,
    "onboarding": 5
  },
  "recruitmentStats": {
    "activeJobs": 12,
    "totalApplicants": 145,
    "newApplicantsThisMonth": 45,
    "offersSent": 8,
    "hiredThisMonth": 5,
    "timeToHire": 28.0
  },
  "payrollSummary": {
    "currentPeriod": "2026-02",
    "status": "Processing",
    "employeesToPay": 320,
    "totalGrossPay": 145000000.00,
    "totalNetPay": 119500000.00,
    "nextPayDate": "2026-02-28"
  },
  "trainingStats": {
    "activePrograms": 8,
    "totalEnrollments": 156,
    "completionRate": 78.5,
    "averageRating": 4.6
  },
  "leaveStats": {
    "pendingRequests": 12,
    "onLeaveToday": 23,
    "scheduledLeaves": 45
  },
  "complianceStats": {
    "score": 94,
    "criticalIssues": 0,
    "warnings": 2,
    "pendingAcknowledgments": 5
  },
  "alerts": [
    {
      "type": "PROBATION_ENDING",
      "message": "5 employees completing probation this week",
      "priority": "INFO",
      "actionUrl": "/employees?status=PROBATION"
    }
  ],
  "departmentBreakdown": {
    "Sales": 26,
    "Operations": 19,
    "Finance": 10,
    "Tech": 13
  }
}
```

---

## Employee APIs

### Search Employees
```http
GET /employees?countryCode=NGA&page=0&size=20&sortBy=lastName&sortDir=ASC
```

**Query Parameters:**
- `countryCode` (required): Country code
- `search` (optional): Search term for name/email/ID
- `department` (optional): Filter by department
- `status` (optional): Filter by status (ACTIVE, ON_LEAVE, etc.)
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 20)
- `sortBy` (optional): Sort field (default: lastName)
- `sortDir` (optional): Sort direction ASC/DESC (default: ASC)

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "employeeId": "NGA-000001",
      "firstName": "John",
      "lastName": "Okon",
      "email": "john.okon@gogidix.com",
      "phone": "+2348034567890",
      "department": "Sales",
      "jobTitle": "Sales Representative",
      "employmentType": "FULL_TIME",
      "status": "ACTIVE",
      "workLocation": "Lagos",
      "managerId": "NGA-000045",
      "hireDate": "2023-01-15",
      "salary": 450000.00,
      "currency": "NGN",
      "yearsOfService": 1
    }
  ],
  "pageable": {
    "page": 0,
    "size": 20
  },
  "totalElements": 347,
  "totalPages": 18
}
```

### Get Employee by ID
```http
GET /employees/{id}
```

### Get Employee by Employee ID
```http
GET /employees/employee-id/{employeeId}
```

### Create Employee
```http
POST /employees
Content-Type: application/json
X-Country-Code: NGA
```

**Request Body:**
```json
{
  "firstName": "Jane",
  "lastName": "Adebayo",
  "email": "jane.adebayo@gogidix.com",
  "phone": "+2348034567891",
  "gender": "Female",
  "dateOfBirth": "1990-05-15",
  "address": "123 Main Street",
  "city": "Lagos",
  "state": "Lagos",
  "department": "Finance",
  "jobTitle": "Accountant",
  "employmentType": "FULL_TIME",
  "workLocation": "Lagos",
  "hireDate": "2024-02-01",
  "probationEndDate": "2024-05-01",
  "salary": 500000.00,
  "currency": "NGN",
  "managerId": "NGA-000045",
  "benefitsEligible": true,
  "pensionEligible": true
}
```

**Response:** `201 Created` with created employee object

### Update Employee
```http
PUT /employees/{employeeId}
Content-Type: application/json
```

### Terminate Employee
```http
POST /employees/{employeeId}/terminate
Content-Type: application/json
```

**Request Body:**
```json
{
  "reason": "Resignation",
  "terminationDate": "2024-02-28"
}
```

### Get Department Statistics
```http
GET /employees/stats/department?countryCode=NGA
```

**Response:**
```json
{
  "Sales": 89,
  "Operations": 67,
  "Finance": 34,
  "Tech": 45,
  "Marketing": 34,
  "HR": 23,
  "Exec": 12
}
```

### Get Employees Ending Probation
```http
GET /employees/probation-ending?countryCode=NGA&days=7
```

---

## Leave Management APIs

### Get Leave Requests
```http
GET /leave/requests?countryCode=NGA&status=PENDING&page=0&size=20
```

**Query Parameters:**
- `countryCode` (required): Country code
- `status` (optional): PENDING, APPROVED, REJECTED, ACTIVE
- `page` (optional): Page number
- `size` (optional): Page size

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "employeeId": "NGA-000001",
      "employeeName": "John Okon",
      "department": "Sales",
      "leaveType": "ANNUAL",
      "startDate": "2024-02-10",
      "endDate": "2024-02-15",
      "days": 6,
      "reason": "Family vacation",
      "status": "PENDING",
      "createdAt": "2024-02-01T10:30:00"
    }
  ],
  "totalElements": 12
}
```

### Get Pending Leave Requests
```http
GET /leave/requests/pending?countryCode=NGA
```

### Create Leave Request
```http
POST /leave/requests
Content-Type: application/json
```

**Request Body:**
```json
{
  "employeeId": "NGA-000001",
  "leaveType": "ANNUAL",
  "startDate": "2024-03-01",
  "endDate": "2024-03-07",
  "reason": "Personal travel"
}
```

### Approve Leave Request
```http
POST /leave/requests/{id}/approve
Content-Type: application/json
```

**Request Body:**
```json
{
  "approvedBy": "NGA-000045"
}
```

### Reject Leave Request
```http
POST /leave/requests/{id}/reject
Content-Type: application/json
```

**Request Body:**
```json
{
  "rejectionReason": "Insufficient staff coverage"
}
```

### Get Employee Leave Balance
```http
GET /leave/balance/{employeeId}
```

**Response:**
```json
{
  "ANNUAL": {
    "total": 21,
    "used": 5,
    "pending": 3,
    "available": 13
  },
  "SICK": {
    "total": 15,
    "used": 2,
    "pending": 0,
    "available": 13
  }
}
```

---

## Recruitment APIs

### Get Job Postings
```http
GET /recruitment/jobs?countryCode=NGA&status=PUBLISHED
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "jobCode": "NGA-JOB-001",
      "title": "Sales Representative",
      "department": "Sales",
      "location": "Lagos",
      "employmentType": "FULL_TIME",
      "status": "PUBLISHED",
      "applicationsReceived": 45,
      "positionsFilled": 3,
      "vacancyCount": 5,
      "publishedDate": "2024-01-15",
      "applicationDeadline": "2024-03-31"
    }
  ]
}
```

### Create Job Posting
```http
POST /recruitment/jobs
Content-Type: application/json
```

### Get Applicants for Job
```http
GET /recruitment/jobs/{jobId}/applicants
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "firstName": "Amaka",
      "lastName": "Okafor",
      "email": "amaka.o@email.com",
      "status": "SCREENED",
      "rating": 4.5,
      "matchScore": 92.5,
      "appliedDate": "2024-02-05",
      "source": "LinkedIn"
    }
  ]
}
```

---

## Performance APIs

### Get Performance Reviews
```http
GET /performance/reviews?countryCode=NGA&cycle=Q4&year=2023
```

### Get Employee Reviews
```http
GET /performance/employee/{employeeId}
```

**Response:**
```json
{
  "reviews": [
    {
      "id": 1,
      "cycle": "Q4",
      "year": 2023,
      "overallScore": 4.2,
      "overallRating": "Exceeds Expectations",
      "goalAchievementScore": 4.5,
      "competencyScore": 4.0,
      "reviewer": "Sarah Okon",
      "status": "COMPLETED"
    }
  ]
}
```

---

## Training APIs

### Get Training Programs
```http
GET /training/programs?countryCode=NGA&status=ACTIVE
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "programCode": "NGA-TR-001",
      "title": "Leadership Development",
      "trainingType": "HYBRID",
      "deliveryMethod": "Hybrid",
      "durationHours": 40,
      "status": "ACTIVE",
      "enrolledCount": 45,
      "completedCount": 12,
      "completionRate": 27,
      "averageRating": 4.7
    }
  ]
}
```

### Enroll Employee in Training
```http
POST /training/programs/{programId}/enroll
Content-Type: application/json
```

**Request Body:**
```json
{
  "employeeId": "NGA-000001"
}
```

---

## Compliance APIs

### Get Compliance Records
```http
GET /compliance/records?countryCode=NGA
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "category": "Employee Records",
      "title": "Employee File Audit",
      "status": "COMPLIANT",
      "dueDate": "2024-04-15",
      "lastAuditDate": "2024-01-15",
      "complianceScore": 100.0
    }
  ]
}
```

### Get Pending Policy Acknowledgments
```http
GET /compliance/policies/pending?countryCode=NGA
```

---

## Error Responses

All errors follow this format:

```json
{
  "timestamp": "2024-02-25T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 999",
  "path": "/api/v1/country-hr/employees/999"
}
```

Common HTTP Status Codes:
- `200` - Success
- `201` - Created
- `400` - Bad Request (validation error)
- `401` - Unauthorized
- `404` - Not Found
- `409` - Conflict (duplicate resource)
- `500` - Internal Server Error

---

## API Documentation (Swagger)

Interactive API documentation available at:
```
http://localhost:8080/api/v1/country-hr/swagger-ui.html
```

OpenAPI JSON at:
```
http://localhost:8080/api/v1/country-hr/api-docs
```

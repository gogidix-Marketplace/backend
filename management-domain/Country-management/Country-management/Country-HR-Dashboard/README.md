# Country HR Dashboard - Implementation Summary

## Overview

The Country HR Dashboard is a production-ready HR management system for country-level operations within the Gogidix Ecosystem. It handles all HR operations at the country level and reports to the Human-resource HQ service.

## Architecture

```
Country-HR-Dashboard/
├── Backend/Java/country-hr-service/     # Spring Boot 3.2.3 backend
├── Frontends/country-hr-web-dashboard/  # Next.js 14 frontend
├── docker-compose.yml                    # Complete stack deployment
└── README.md                            # This file
```

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.3
- **Java Version**: 17
- **Database**: PostgreSQL 16
- **ORM**: Hibernate/JPA
- **Message Queue**: Kafka
- **API Documentation**: SpringDoc OpenAPI 3
- **Build Tool**: Maven

### Frontend
- **Framework**: Next.js 14 (App Router)
- **Language**: TypeScript
- **State Management**: Zustand + TanStack Query
- **UI**: Tailwind CSS + Radix UI
- **Charts**: Recharts
- **HTTP Client**: Axios

## Features Implemented

### 1. Employee Management
- Employee directory with search and filters
- Employee onboarding workflow
- Employee profile management
- Department assignment
- Employment type tracking
- Probation period monitoring

### 2. Leave Management
- Leave request submission
- Leave approval workflow
- Leave balance tracking
- Leave types: Annual, Sick, Maternity, Paternity, Study
- Attendance tracking

### 3. Recruitment (ATS)
- Job posting management
- Applicant tracking system
- Interview scheduling
- Offer management
- Pipeline visualization

### 4. Performance Management
- Performance review cycles
- Goal setting (OKRs/KPIs)
- Competency assessment
- 360-degree feedback
- Performance improvement plans

### 5. Payroll Integration
- Payroll calculation
- Payslip generation
- Tax deductions
- Pension calculations
- Benefits administration

### 6. Training & Development
- Training program management
- Employee enrollment
- Progress tracking
- Certification management
- Completion reporting

### 7. Compliance Management
- Labor law compliance tracking
- Policy acknowledgment
- Audit management
- Compliance scoring
- Risk assessment

### 8. HQ Integration
- Kafka-based metrics streaming
- REST API client for HQ communication
- Daily HR metrics reporting
- Policy synchronization
- Budget tracking

## API Endpoints

### Dashboard
- `GET /api/v1/country-hr/dashboard/overview` - Get dashboard overview
- `GET /api/v1/country-hr/dashboard/health-score` - Get HR health score

### Employees
- `GET /api/v1/country-hr/employees` - Search employees (paginated)
- `GET /api/v1/country-hr/employees/{id}` - Get employee by ID
- `GET /api/v1/country-hr/employees/employee-id/{employeeId}` - Get by employee ID
- `POST /api/v1/country-hr/employees` - Create employee
- `PUT /api/v1/country-hr/employees/{employeeId}` - Update employee
- `POST /api/v1/country-hr/employees/{employeeId}/terminate` - Terminate employee
- `GET /api/v1/country-hr/employees/stats/department` - Department statistics
- `GET /api/v1/country-hr/employees/stats/status` - Status statistics

### Leave Management
- `GET /api/v1/country-hr/leave/requests` - Get leave requests (paginated)
- `GET /api/v1/country-hr/leave/requests/pending` - Get pending requests
- `POST /api/v1/country-hr/leave/requests` - Create leave request
- `POST /api/v1/country-hr/leave/requests/{id}/approve` - Approve request
- `POST /api/v1/country-hr/leave/requests/{id}/reject` - Reject request
- `GET /api/v1/country-hr/leave/balance/{employeeId}` - Get leave balance

### Recruitment
- `GET /api/v1/country-hr/recruitment/jobs` - Get job postings
- `POST /api/v1/country-hr/recruitment/jobs` - Create job posting
- `PUT /api/v1/country-hr/recruitment/jobs/{id}` - Update job posting
- `GET /api/v1/country-hr/recruitment/jobs/{id}/applicants` - Get applicants

### Performance
- `GET /api/v1/country-hr/performance/reviews` - Get performance reviews
- `POST /api/v1/country-hr/performance/reviews` - Create review
- `GET /api/v1/country-hr/performance/employee/{employeeId}` - Get employee reviews

## Running the Application

### Using Docker Compose (Recommended)

```bash
cd Country-HR-Dashboard
docker-compose up -d
```

Services will be available at:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- API Documentation: http://localhost:8080/api/v1/country-hr/swagger-ui.html
- PostgreSQL: localhost:5432
- Kafka: localhost:9092

### Backend Only

```bash
cd Backend/Java/country-hr-service
mvn spring-boot:run
```

### Frontend Only

```bash
cd Frontends/country-hr-web-dashboard
npm install
npm run dev
```

## Environment Variables

### Backend
```properties
DATABASE_URL=jdbc:postgresql://localhost:5432/country_hr
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=postgres
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
COUNTRY_CODE=NGA
COUNTRY_NAME=Nigeria
COUNTRY_CURRENCY=NGN
HQ_HR_SERVICE_URL=http://hq-hr-service:8080/api/v1/hq-hr
SPRING_PROFILES_ACTIVE=dev
```

### Frontend
```bash
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1/country-hr
```

## Database Schema

### Employees Table
- Stores employee personal and employment information
- Indexed on email, status, department, country code

### Leave_Requests Table
- Tracks all leave requests with approval workflow
- Linked to employees table

### Leave_Balances Table
- Maintains leave balance per employee per year
- Tracks total, used, and pending days

### Performance_Reviews Table
- Stores performance review data per cycle
- Includes goal achievement and competency scores

### Job_Postings Table
- Manages recruitment job postings
- Tracks applicant counts and fill status

### Applicants Table
- ATS for tracking job applicants
- Pipeline status from application to hire

### Payrolls Table
- Payroll run information per period
- Totals for gross pay, deductions, net pay

### Payslips Table
- Individual employee payslip details
- Generated per payroll run

### Training_Programs Table
- Training program catalog
- Enrollment and completion tracking

### Compliance_Records Table
- Compliance tracking per category
- Audit dates and scores

## Testing

### Backend Tests
```bash
cd Backend/Java/country-hr-service
mvn test
```

Test coverage target: 80%+

### Frontend Tests
```bash
cd Frontends/country-hr-web-dashboard
npm test
```

## Deployment

### Production Build

Backend:
```bash
mvn clean package -DskipTests
docker build -t country-hr-backend:latest .
```

Frontend:
```bash
npm run build
docker build -t country-hr-frontend:latest .
```

### Docker Images
- `country-hr-backend:latest` - Spring Boot backend
- `country-hr-frontend:latest` - Next.js frontend

## Integration with HQ

### Kafka Topics
- `hr.employee.created` - New employee events
- `hr.employee.updated` - Employee update events
- `hr.employee.terminated` - Termination events
- `hr.leave.request` - Leave request events
- `hr.payroll.metrics` - Payroll metrics
- `hr.recruitment.metrics` - Recruitment metrics
- `hr.metrics.daily` - Daily HR summary

### HQ API Calls
- POST `/api/v1/hq-hr/country-metrics` - Send daily metrics
- GET `/api/v1/hq-hr/policies/country/{code}` - Fetch HR policies
- GET `/api/v1/hq-hr/budget/{country}/{period}` - Get budget allocation
- POST `/api/v1/hq-hr/hiring/completion` - Report hiring

## Security

- JWT-based authentication
- Role-based access control (Country HR Manager, Department Manager, Specialist)
- Country-level data isolation
- Audit logging for sensitive operations

## Monitoring

- Actuator endpoints at `/actuator`
- Prometheus metrics export
- Health check endpoints
- Circuit breaker pattern for HQ integration

## Support

For issues and questions, please contact the Gogidix development team.

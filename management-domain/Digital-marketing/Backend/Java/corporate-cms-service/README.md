# Corporate CMS Service

A headless CMS backend for managing corporate website content, built with Spring Boot 3.2 and MongoDB.

## Features

- **Content Management**: Full CRUD operations for pages, blog posts, press releases, and resources
- **Media Management**: Upload and manage images, videos, and documents
- **Workflow Approval**: Multi-step content approval workflow
- **Role-Based Access Control**: ADMIN, CONTENT_EDITOR, PRODUCT_MANAGER, HR_MANAGER, PR_MANAGER, VIEWER
- **Version Control**: Track content changes with version history
- **Publishing/Scheduling**: Schedule content for future publishing
- **Product Catalog**: Manage product information
- **Career Management**: Job postings and applications
- **Lead Management**: Marketing lead tracking
- **Analytics Dashboard**: Content and user metrics

## Technology Stack

- Java 21
- Spring Boot 3.2.5
- Spring Data MongoDB
- Spring Security with JWT
- MapStruct
- OpenAPI 3.0 (SpringDoc)
- Maven
- Docker

## Project Structure

```
corporate-cms-service/
├── src/
│   ├── main/
│   │   ├── java/com/gogidix/corporatecms/
│   │   │   ├── application/          # Application layer
│   │   │   │   ├── config/           # Configuration
│   │   │   │   ├── dto/              # Data Transfer Objects
│   │   │   │   ├── exception/        # Exception handlers
│   │   │   │   ├── mapper/           # MapStruct mappers
│   │   │   │   └── security/         # Security configuration
│   │   │   ├── domain/               # Domain layer
│   │   │   │   ├── enums/            # Enumerations
│   │   │   │   ├── model/            # Domain models
│   │   │   │   ├── repository/       # Repository interfaces
│   │   │   │   └── service/          # Business logic
│   │   │   ├── infrastructure/       # Infrastructure layer
│   │   │   │   └── adapter/          # External adapters
│   │   │   ├── interfaces/           # Interface layer
│   │   │   │   └── rest/             # REST controllers
│   │   │   └── CorporateCmsApplication.java
│   │   └── resources/
│   │       └── application.yml       # Configuration
│   └── test/                         # Unit tests
├── Dockerfile
├── pom.xml
└── README.md
```

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- MongoDB 4.4+

### Local Development

1. Clone the repository:
```bash
git clone <repository-url>
cd corporate-cms-service
```

2. Configure MongoDB in `application.yml`:
```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017
      database: corporate_cms_dev
```

3. Build and run:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080/api/cms/v1`

### Docker

Build the Docker image:
```bash
docker build -t corporate-cms-service:latest .
```

Run the container:
```bash
docker run -p 8080:8080 \
  -e SPRING_DATA_MONGODB_URI=mongodb://host.docker.internal:27017/corporate_cms \
  corporate-cms-service:latest
```

## API Documentation

Once the application is running, access the Swagger UI at:
```
http://localhost:8080/api/cms/v1/swagger-ui.html
```

OpenAPI spec available at:
```
http://localhost:8080/api/cms/v1/api-docs
```

## Authentication

The API uses JWT tokens for authentication. First, obtain a token:

```bash
curl -X POST http://localhost:8080/api/cms/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail": "admin", "password": "password"}'
```

Then include the token in subsequent requests:

```bash
curl -X GET http://localhost:8080/api/cms/v1/content \
  -H "Authorization: Bearer <your-token>"
```

## Roles and Permissions

| Role | Permissions |
|------|-------------|
| ADMIN | Full access to all resources |
| CONTENT_EDITOR | Content read/write, media management |
| PRODUCT_MANAGER | Product catalog management |
| HR_MANAGER | Job/career management |
| PR_MANAGER | Press release management |
| VIEWER | Read-only access |

## API Endpoints

### Content
- `POST /content` - Create content
- `GET /content/{id}` - Get content by ID
- `GET /content/slug/{slug}` - Get content by slug
- `GET /content/published` - Get published content
- `PATCH /content/{id}/status` - Update content status
- `POST /content/{id}/publish` - Publish content

### Media
- `POST /media/upload` - Upload media file
- `GET /media/{id}` - Get media by ID
- `GET /media/{id}/download` - Download media
- `DELETE /media/{id}` - Delete media

### Workflow
- `POST /workflows/content/{contentId}` - Initiate workflow
- `GET /workflows/pending-approvals` - Get pending approvals
- `POST /workflows/{id}/approve` - Approve workflow
- `POST /workflows/{id}/reject` - Reject workflow

### Products
- `POST /products` - Create product
- `GET /products` - Get all products
- `GET /products/published` - Get published products

### Jobs
- `POST /jobs` - Create job posting
- `GET /jobs/open` - Get open jobs
- `POST /jobs/{id}/publish` - Publish job

### Leads
- `POST /leads` - Create lead
- `GET /leads/status/{status}` - Get leads by status
- `PATCH /leads/{id}/status` - Update lead status

### Analytics
- `GET /analytics/dashboard` - Get dashboard data

## Running Tests

```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report
```

Coverage report will be generated in `target/site/jacoco/index.html`

## Configuration

Key configuration options in `application.yml`:

```yaml
# Server
server.port: 8080
server.servlet.context-path: /api/cms/v1

# JWT
jwt.secret: your-secret-key
jwt.expiration: 86400000  # 24 hours

# File Upload
storage.upload-dir: ./uploads
storage.max-file-size: 52428800  # 50MB

# Pagination
pagination.default-page-size: 20
pagination.max-page-size: 100
```

## License

Apache License 2.0

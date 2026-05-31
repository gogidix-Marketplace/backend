# Digital Marketing Domain - API Documentation

## Overview

This document provides comprehensive API documentation for all services within the Digital Marketing Domain, including the 14 core marketing services and 2 new corporate website services.

## Table of Contents

- [Base URLs](#base-urls)
- [Authentication](#authentication)
- [Corporate Website Service APIs](#corporate-website-service-apis)
- [Corporate CMS Service APIs](#corporate-cms-service-apis)
- [Core Marketing Service APIs](#core-marketing-service-apis)
- [Common Error Responses](#common-error-responses)

---

## Base URLs

| Environment | Corporate Website | Corporate CMS | Marketing Services |
|-------------|-------------------|---------------|-------------------|
| Production | `https://api.gogidix.com/website/api/v1` | `https://api.gogidix.com/cms/api/cms/v1` | `https://api.gogidix.com/marketing/{service}/api/v1` |
| Development | `http://localhost:8095/api/v1` | `http://localhost:8096/api/cms/v1` | `http://localhost:8081-8094/api/v1` |

## Authentication

### JWT Authentication

Most API endpoints require a valid JWT token in the Authorization header:

```http
Authorization: Bearer <jwt-token>
```

### API Key Authentication

Public-facing endpoints may use API key authentication:

```http
X-API-Key: <your-api-key>
```

---

## Corporate Website Service APIs

Base URL: `http://localhost:8095/api/v1`

### Pages API

#### GET /pages

Get all published pages for a specific region and language.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter (NG, KE, GH, ZA, etc.) Default: NG |
| language | string | No | Language filter (en, fr, es, pt, ar) Default: en |

**Response (200 OK):**
```json
[
  {
    "id": "page-123",
    "pageKey": "home",
    "path": "/",
    "title": "Welcome to Gogidix",
    "content": "<p>Full page content...</p>",
    "metaDescription": "Leading digital solutions provider",
    "metaKeywords": ["digital", "solutions"],
    "ogImage": "https://cdn.gogidix.com/og-home.jpg",
    "language": "en",
    "region": "NG",
    "inNavigation": true,
    "sortOrder": 1,
    "lastModified": "2024-02-01T10:00:00Z"
  }
]
```

#### GET /pages/navigation

Get pages that should appear in site navigation.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Same as GET /pages but filtered for navigation items

#### GET /pages/{id}

Get a specific page by ID.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Page ID |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| language | string | No | Language filter. Default: en |

**Response (200 OK):**
```json
{
  "id": "page-123",
  "pageKey": "about",
  "path": "/about",
  "title": "About Us",
  "content": "<p>Our company story...</p>",
  "metaDescription": "Learn about Gogidix",
  "metaKeywords": ["about", "company"],
  "ogImage": "https://cdn.gogidix.com/og-about.jpg",
  "language": "en",
  "lastModified": "2024-02-01T10:00:00Z"
}
```

#### GET /pages/key/{pageKey}

Get a page by its key identifier.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| pageKey | string | Yes | Page key (e.g., "home", "about", "contact") |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Same as GET /pages/{id}

#### GET /pages/path/{path}

Get a page by its URL path.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| path | string | Yes | URL path (e.g., "products/logistics") |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Same as GET /pages/{id}

#### GET /pages/search

Search pages by keyword.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| keyword | string | Yes | Search keyword |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of page objects

#### GET /pages/tags/{tag}

Get pages tagged with a specific tag.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| tag | string | Yes | Tag name |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of page objects

---

### Products API

#### GET /products

Get all published products for a region.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |
| language | string | No | Language filter. Default: en |

**Response (200 OK):**
```json
[
  {
    "id": "prod-123",
    "productKey": "logistics-platform",
    "slug": "logistics-platform",
    "name": "Logistics Platform",
    "shortDescription": "End-to-end logistics management",
    "longDescription": "<p>Full description...</p>",
    "productType": "PLATFORM",
    "category": "Logistics",
    "features": [
      {
        "icon": "truck",
        "title": "Fleet Management",
        "description": "Track your entire fleet",
        "included": true
      }
    ],
    "availableRegions": ["NG", "KE", "GH"],
    "imageUrl": "https://cdn.gogidix.com/products/logistics.jpg",
    "imageAlt": "Logistics Platform",
    "gallery": ["image1.jpg", "image2.jpg"],
    "pricing": {
      "basePrice": 5000,
      "currency": "USD",
      "billingCycle": "monthly",
      "displayPricing": true,
      "startingFromText": "Starting at $5,000/month"
    },
    "requiresContact": true,
    "ctaText": "Request Demo",
    "ctaLink": "/contact/demo",
    "status": "PUBLISHED",
    "launchDate": "2024-01-01",
    "tags": ["logistics", "fleet", "tracking"],
    "featured": true,
    "sortOrder": 1
  }
]
```

#### GET /products/featured

Get featured products.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of featured product objects

#### GET /products/category/{category}

Get products by category.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| category | string | Yes | Category name |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of product objects

#### GET /products/{id}

Get a specific product by ID.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Product ID |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Single product object

#### GET /products/key/{productKey}

Get a product by its key.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| productKey | string | Yes | Product key |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Single product object

#### GET /products/slug/{slug}

Get a product by slug.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| slug | string | Yes | Product slug |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Single product object

#### GET /products/search

Search products by keyword.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| keyword | string | Yes | Search keyword |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of product objects

#### GET /products/tag/{tag}

Get products by tag.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| tag | string | Yes | Tag name |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of product objects

#### GET /products/{id}/related

Get related products.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Product ID |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| limit | integer | No | Max products. Default: 4 |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of related product objects

---

### Blog API

#### GET /blog

Get all published blog posts.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |
| language | string | No | Language filter. Default: en |

**Response (200 OK):**
```json
[
  {
    "id": "blog-123",
    "slug": "future-of-logistics",
    "title": "The Future of Logistics in Africa",
    "excerpt": "Exploring how technology is transforming...",
    "content": "<p>Full blog post content...</p>",
    "author": {
      "id": "user-456",
      "name": "John Doe",
      "avatar": "https://cdn.gogidix.com/avatars/john.jpg"
    },
    "category": "Industry Insights",
    "tags": ["logistics", "technology", "africa"],
    "featuredImage": "https://cdn.gogidix.com/blog/logistics-future.jpg",
    "publishedAt": "2024-02-01T10:00:00Z",
    "readingTime": 5,
    "viewCount": 1250,
    "likeCount": 85,
    "featured": true,
    "language": "en"
  }
]
```

#### GET /blog/featured

Get featured blog posts.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of featured blog posts

#### GET /blog/recent

Get recent blog posts.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |
| limit | integer | No | Max posts. Default: 10 |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of recent blog posts

#### GET /blog/{id}

Get a blog post by ID.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Blog post ID |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Single blog post object with full content

#### GET /blog/slug/{slug}

Get a blog post by slug.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| slug | string | Yes | Blog post slug |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Single blog post object

#### GET /blog/category/{category}

Get blog posts by category.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| category | string | Yes | Category name |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of blog posts

#### GET /blog/tag/{tag}

Get blog posts by tag.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| tag | string | Yes | Tag name |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of blog posts

#### GET /blog/author/{authorId}

Get blog posts by author.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| authorId | string | Yes | Author ID |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of blog posts

#### GET /blog/{id}/related

Get related blog posts.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Blog post ID |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| limit | integer | No | Max posts. Default: 5 |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of related blog posts

#### GET /blog/search

Search blog posts by keyword.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| keyword | string | Yes | Search keyword |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of blog posts

#### POST /blog/{id}/like

Like a blog post.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Blog post ID |

**Response (200 OK):** Empty response

---

### Case Studies API

#### GET /case-studies

Get all published case studies.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |
| language | string | No | Language filter. Default: en |

**Response (200 OK):**
```json
[
  {
    "id": "case-123",
    "slug": "retail-transformation",
    "title": "Digital Transformation for Leading Retailer",
    "excerpt": "How we helped a major retailer transform...",
    "clientName": "Africa Retail Corp",
    "clientLogo": "https://cdn.gogidix.com/clients/arc.png",
    "industry": "Retail",
    "projectDuration": "6 months",
    "challenge": "<p>The client faced...</p>",
    "solution": "<p>Our solution included...</p>",
    "results": "<p>Results achieved...</p>",
    "metrics": [
      {
        "label": "Revenue Increase",
        "value": "45",
        "unit": "%",
        "description": "Year over year"
      }
    ],
    "technologies": ["React", "Node.js", "MongoDB"],
    "services": ["Web Development", "Mobile Apps"],
    "heroImage": "https://cdn.gogidix.com/cases/retail.jpg",
    "testimonial": "Gogidix delivered exceptional results",
    "testimonialAuthor": "Jane Smith",
    "testimonialRole": "CTO",
    "testimonialImage": "https://cdn.gogidix.com/avatars/jane.jpg",
    "publishDate": "2024-01-15",
    "viewCount": 850,
    "featured": true,
    "tags": ["retail", "transformation"]
  }
]
```

#### GET /case-studies/featured

Get featured case studies.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of featured case studies

#### GET /case-studies/{id}

Get a case study by ID.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Case study ID |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Single case study object

#### GET /case-studies/slug/{slug}

Get a case study by slug.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| slug | string | Yes | Case study slug |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Single case study object

#### GET /case-studies/industry/{industry}

Get case studies by industry.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| industry | string | Yes | Industry name |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of case studies

#### GET /case-studies/client/{clientName}

Get case studies by client.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| clientName | string | Yes | Client name |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of case studies

#### GET /case-studies/service/{service}

Get case studies by service.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| service | string | Yes | Service name |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of case studies

#### GET /case-studies/technology/{technology}

Get case studies by technology.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| technology | string | Yes | Technology name |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of case studies

#### GET /case-studies/search

Search case studies by keyword.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| keyword | string | Yes | Search keyword |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of case studies

#### GET /case-studies/{id}/related

Get related case studies.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Case study ID |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| limit | integer | No | Max studies. Default: 4 |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of related case studies

---

### Press Releases API

#### GET /press

Get all published press releases.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |
| language | string | No | Language filter. Default: en |

**Response (200 OK):**
```json
[
  {
    "id": "press-123",
    "slug": "series-b-announcement",
    "title": "Gogidix Raises $50M in Series B Funding",
    "excerpt": "Leading digital platform secures major investment...",
    "content": "<p>Full press release...</p>",
    "releaseDate": "2024-02-01",
    "contactName": "Sarah Johnson",
    "contactEmail": "press@gogidix.com",
    "contactPhone": "+234 123 456 7890",
    "mediaContacts": ["investors@gogidix.com"],
    "organization": "Gogidix",
    "tickerSymbol": "GOGI",
    "embargoed": false,
    "immediateRelease": true,
    "tags": ["funding", "investment", "growth"],
    "publishedAt": "2024-02-01T09:00:00Z"
  }
]
```

#### GET /press/recent

Get recent press releases.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| limit | integer | No | Max releases. Default: 10 |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of recent press releases

#### GET /press/{id}

Get a press release by ID.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Press release ID |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Single press release object

#### GET /press/slug/{slug}

Get a press release by slug.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| slug | string | Yes | Press release slug |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Single press release object

#### GET /press/search

Search press releases by keyword.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| keyword | string | Yes | Search keyword |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of press releases

#### GET /press/media

Get media contact information and recent non-embargoed releases.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| language | string | No | Language filter. Default: en |

**Response (200 OK):**
```json
{
  "mediaContacts": [
    {
      "name": "Sarah Johnson",
      "role": "Head of Communications",
      "email": "press@gogidix.com",
      "phone": "+234 123 456 7890"
    }
  ],
  "recentReleases": [...]
}
```

---

### Careers API

#### GET /careers

Get all open job positions.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |
| language | string | No | Language filter. Default: en |

**Response (200 OK):**
```json
[
  {
    "id": "job-123",
    "jobKey": "senior-backend-engineer",
    "slug": "senior-backend-engineer",
    "title": "Senior Backend Engineer",
    "shortDescription": "Join our engineering team...",
    "longDescription": "<p>Full job description...</p>",
    "department": "Engineering",
    "employmentType": "FULL_TIME",
    "experienceLevel": "SENIOR",
    "location": "Lagos, Nigeria",
    "remote": true,
    "availableRegions": ["NG", "KE", "GH"],
    "responsibilities": [
      "Design and implement backend services",
      "Mentor junior engineers"
    ],
    "requirements": [
      "5+ years of experience",
      "Proficiency in Java/Spring Boot"
    ],
    "benefits": [
      "Competitive salary",
      "Health insurance",
      "Remote work"
    ],
    "salaryMin": 150000,
    "salaryMax": 200000,
    "salaryCurrency": "USD",
    "applicationUrl": "https://gogidix.com/apply/senior-backend-engineer",
    "applicationEmail": "jobs@gogidix.com",
    "deadline": "2024-03-31",
    "publishDate": "2024-02-01",
    "status": "OPEN",
    "tags": ["backend", "java", "spring"],
    "featured": true,
    "sortOrder": 1
  }
]
```

#### GET /careers/featured

Get featured job positions.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of featured job positions

#### GET /careers/{id}

Get a job position by ID.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Job ID |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Single job position object

#### GET /careers/slug/{slug}

Get a job position by slug.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| slug | string | Yes | Job slug |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Single job position object

#### GET /careers/department/{department}

Get jobs by department.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| department | string | Yes | Department name |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of job positions

#### GET /careers/location/{location}

Get jobs by location.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| location | string | Yes | Location name |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of job positions

#### GET /careers/remote

Get remote job positions.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of remote job positions

#### GET /careers/closing-soon

Get jobs closing within 7 days.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of job positions closing soon

#### GET /careers/search

Search job positions by keyword.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| keyword | string | Yes | Search keyword |
| language | string | No | Language filter. Default: en |

**Response (200 OK):** Array of job positions

---

### Lead Capture API

#### POST /leads

Capture a lead from website forms.

**Request Body:**
```json
{
  "type": "demo_request",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@company.com",
  "phone": "+234 123 456 7890",
  "company": "Acme Corp",
  "jobTitle": "CTO",
  "country": "Nigeria",
  "message": "I'm interested in a demo",
  "productInterest": "logistics-platform",
  "source": "corporate-website",
  "utmSource": "google",
  "utmMedium": "cpc",
  "utmCampaign": "brand_awareness"
}
```

**Response (201 Created):**
```json
{
  "success": true,
  "message": "Lead captured successfully",
  "leadId": "lead-456",
  "nextSteps": "A representative will contact you within 24 hours"
}
```

#### GET /leads/health

Check lead service health.

**Response (200 OK):**
```json
{
  "status": "UP",
  "enabled": true
}
```

---

### Sitemap API

#### GET /sitemap/xml

Generate XML sitemap for search engines.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |

**Response (200 OK):** XML sitemap content

#### GET /sitemap/json

Generate JSON sitemap with all URLs.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |

**Response (200 OK):**
```json
[
  {
    "url": "https://gogidix.com/",
    "lastModified": "2024-02-01T10:00:00Z",
    "changeFrequency": "weekly",
    "priority": 1.0
  },
  {
    "url": "https://gogidix.com/products/logistics-platform",
    "lastModified": "2024-02-01T10:00:00Z",
    "changeFrequency": "monthly",
    "priority": 0.8
  }
]
```

#### GET /sitemap/sitemap.xml

Standard sitemap endpoint for search engines.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| region | string | No | Region filter. Default: NG |

**Response (200 OK):** XML sitemap content

---

## Corporate CMS Service APIs

Base URL: `http://localhost:8096/api/cms/v1`

**Authentication Required:** All endpoints require JWT authentication

### Authentication API

#### POST /auth/login

Authenticate and receive JWT token.

**Request Body:**
```json
{
  "usernameOrEmail": "admin@gogidix.com",
  "password": "securePassword123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 86400,
  "user": {
    "id": "user-123",
    "email": "admin@gogidix.com",
    "firstName": "Admin",
    "lastName": "User",
    "role": "ADMIN"
  }
}
```

#### POST /auth/register

Register a new user (requires admin privileges).

**Request Body:**
```json
{
  "email": "john.doe@gogidix.com",
  "firstName": "John",
  "lastName": "Doe",
  "password": "securePassword123",
  "role": "CONTENT_EDITOR"
}
```

**Response (201 Created):** User object

#### POST /auth/refresh

Refresh JWT token.

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response (200 OK):** New token and user object

---

### Content API

#### POST /content

Create new content.

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "title": "Welcome to Gogidix",
  "slug": "welcome-gogidix",
  "type": "PAGE",
  "content": "<p>Welcome to our platform...</p>",
  "excerpt": "Discover Gogidix solutions",
  "status": "DRAFT",
  "language": "en",
  "tags": ["welcome", "about"],
  "category": "About",
  "featured": false,
  "allowComments": false,
  "metaTitle": "Welcome to Gogidix - Digital Solutions",
  "metaDescription": "Leading digital solutions provider in Africa",
  "metaKeywords": ["digital", "solutions", "africa"],
  "ogImage": "https://cdn.gogidix.com/og-welcome.jpg"
}
```

**Response (201 Created):** Content object

#### PUT /content/{id}

Update content.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Content ID |

**Request Body:** Same as POST /content

**Response (200 OK):** Updated content object

#### GET /content/{id}

Get content by ID.

**Headers:** `Authorization: Bearer <token>` (for unpublished content)

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Content ID |

**Response (200 OK):**
```json
{
  "id": "content-123",
  "title": "Welcome to Gogidix",
  "slug": "welcome-gogidix",
  "type": "PAGE",
  "content": "<p>Full content...</p>",
  "status": "PUBLISHED",
  "language": "en",
  "tags": ["welcome", "about"],
  "createdAt": "2024-02-01T10:00:00Z",
  "updatedAt": "2024-02-01T11:00:00Z",
  "createdBy": "user-123",
  "updatedBy": "user-123"
}
```

#### GET /content/slug/{slug}

Get content by slug.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| slug | string | Yes | Content slug |

**Response (200 OK):** Content object

#### GET /content/type/{type}

Get content by type with pagination.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| type | string | Yes | Content type (PAGE, BLOG, PRESS_RELEASE, etc.) |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| page | integer | No | Page number. Default: 0 |
| size | integer | No | Page size. Default: 20 |

**Response (200 OK):** Paginated content list

#### GET /content/status/{status}

Get content by status.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| status | string | Yes | Content status (DRAFT, PUBLISHED, etc.) |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| page | integer | No | Page number. Default: 0 |
| size | integer | No | Page size. Default: 20 |

**Response (200 OK):** Paginated content list

#### GET /content/search

Search content by keyword.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| keyword | string | Yes | Search keyword |
| page | integer | No | Page number. Default: 0 |
| size | integer | No | Page size. Default: 20 |

**Response (200 OK):** Paginated search results

#### GET /content/published

Get all published content.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| page | integer | No | Page number. Default: 0 |
| size | integer | No | Page size. Default: 20 |

**Response (200 OK):** Paginated published content

#### PATCH /content/{id}/status

Update content status.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Content ID |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| status | string | Yes | New status |

**Response (200 OK):** Updated content object

#### POST /content/{id}/publish

Publish content.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Content ID |

**Response (200 OK):** Published content object

#### POST /content/{id}/unpublish

Unpublish content.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Content ID |

**Response (200 OK):** Unpublished content object

#### DELETE /content/{id}

Delete content (soft delete).

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Content ID |

**Response (200 OK):** Success confirmation

#### POST /content/{id}/restore

Restore deleted content.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Content ID |

**Response (200 OK):** Restored content object

#### GET /content/author/{authorId}

Get content by author.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| authorId | string | Yes | Author user ID |

**Response (200 OK):** Array of content objects

#### GET /content/tags

Get content by tags.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| tags | string | Yes | Comma-separated tag list |

**Response (200 OK):** Array of content objects

---

### Media API

#### POST /media/upload

Upload a single media file.

**Headers:** `Authorization: Bearer <token>`, `Content-Type: multipart/form-data`

**Form Data:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| file | file | Yes | Media file to upload |
| folder | string | No | Target folder. Default: uploads |

**Response (201 Created):**
```json
{
  "id": "media-123",
  "fileName": "hero-banner.jpg",
  "originalFileName": "hero-banner.jpg",
  "mimeType": "image/jpeg",
  "size": 524288,
  "folder": "uploads/images",
  "url": "https://cdn.gogidix.com/uploads/images/hero-banner.jpg",
  "thumbnailUrl": "https://cdn.gogidix.com/uploads/images/thumbs/hero-banner.jpg",
  "altText": "Hero banner image",
  "caption": "Main website banner",
  "uploadedBy": "user-123",
  "uploadedAt": "2024-02-01T10:00:00Z"
}
```

#### POST /media/upload/multiple

Upload multiple media files.

**Headers:** `Authorization: Bearer <token>`, `Content-Type: multipart/form-data`

**Form Data:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| files | file[] | Yes | Media files to upload |
| folder | string | No | Target folder. Default: uploads |

**Response (201 Created):** Array of media objects

#### GET /media/{id}

Get media by ID.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Media ID |

**Response (200 OK):** Media object

#### GET /media/type/{type}

Get media by type with pagination.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| type | string | Yes | Media type (IMAGE, VIDEO, DOCUMENT, etc.) |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| page | integer | No | Page number. Default: 0 |
| size | integer | No | Page size. Default: 20 |

**Response (200 OK):** Paginated media list

#### GET /media/folder/{folder}

Get media from a specific folder.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| folder | string | Yes | Folder path |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| page | integer | No | Page number. Default: 0 |
| size | integer | No | Page size. Default: 20 |

**Response (200 OK):** Paginated media list

#### GET /media/search

Search media by keyword.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| keyword | string | Yes | Search keyword |
| page | integer | No | Page number. Default: 0 |
| size | integer | No | Page size. Default: 20 |

**Response (200 OK):** Paginated search results

#### PUT /media/{id}

Update media metadata.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Media ID |

**Request Body:**
```json
{
  "altText": "Updated alt text",
  "caption": "Updated caption",
  "tags": ["banner", "hero"]
}
```

**Response (200 OK):** Updated media object

#### DELETE /media/{id}

Delete media file.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Media ID |

**Response (200 OK):** Success confirmation

#### GET /media/{id}/download

Download media file.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Media ID |

**Response (200 OK):** File download

#### GET /media/uploader/{uploaderId}

Get media uploaded by a specific user.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| uploaderId | string | Yes | Uploader user ID |

**Response (200 OK):** Array of media objects

---

### Workflow API

#### POST /workflows/content/{contentId}

Initiate approval workflow for content.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| contentId | string | Yes | Content ID |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| comment | string | No | Initial comment |

**Response (201 Created):**
```json
{
  "id": "workflow-123",
  "contentId": "content-456",
  "status": "PENDING_APPROVAL",
  "currentStep": "editor_review",
  "initiatedBy": "user-123",
  "initiatedAt": "2024-02-01T10:00:00Z",
  "actions": [
    {
      "step": "editor_review",
      "action": "SUBMIT",
      "performedBy": "user-123",
      "performedAt": "2024-02-01T10:00:00Z",
      "comment": "Ready for review"
    }
  ]
}
```

#### GET /workflows/content/{contentId}

Get workflow for specific content.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| contentId | string | Yes | Content ID |

**Response (200 OK):** Workflow object

#### GET /workflows/status/{status}

Get workflows by status.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| status | string | Yes | Workflow status |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| page | integer | No | Page number. Default: 0 |
| size | integer | No | Page size. Default: 20 |

**Response (200 OK):** Paginated workflow list

#### GET /workflows/pending-approvals

Get pending approvals for current user.

**Headers:** `Authorization: Bearer <token>`

**Response (200 OK):** Array of workflow objects

#### POST /workflows/{id}/approve

Approve a workflow step.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Workflow ID |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| comment | string | No | Approval comment |

**Response (200 OK):** Updated workflow object

#### POST /workflows/{id}/reject

Reject a workflow.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Workflow ID |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| comment | string | Yes | Rejection reason |

**Response (200 OK):** Updated workflow object

#### POST /workflows/{id}/cancel

Cancel a workflow.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Workflow ID |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| comment | string | No | Cancellation reason |

**Response (200 OK):** Updated workflow object

#### GET /workflows/overdue

Get all overdue workflows.

**Headers:** `Authorization: Bearer <token>`

**Response (200 OK):** Array of workflow objects

---

### Products API (CMS)

#### POST /products

Create a new product.

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "name": "Logistics Platform",
  "slug": "logistics-platform",
  "shortDescription": "End-to-end logistics management",
  "longDescription": "<p>Full description...</p>",
  "productType": "PLATFORM",
  "category": "Logistics",
  "status": "DRAFT",
  "features": [
    {
      "title": "Real-time Tracking",
      "description": "Track shipments in real-time",
      "icon": "tracking"
    }
  ],
  "availableRegions": ["NG", "KE", "GH"],
  "imageUrl": "https://cdn.gogidix.com/products/logistics.jpg",
  "imageAlt": "Logistics Platform",
  "pricing": {
    "basePrice": 5000,
    "currency": "USD",
    "billingCycle": "monthly"
  },
  "requiresContact": true,
  "ctaText": "Request Demo",
  "ctaLink": "/contact/demo"
}
```

**Response (201 Created):** Product object

#### PUT /products/{id}

Update a product.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Product ID |

**Request Body:** Same as POST /products

**Response (200 OK):** Updated product object

#### GET /products/{id}

Get a product by ID.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Product ID |

**Response (200 OK):** Product object

#### GET /products/slug/{slug}

Get a product by slug.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| slug | string | Yes | Product slug |

**Response (200 OK):** Product object

#### GET /products/sku/{sku}

Get a product by SKU.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| sku | string | Yes | Product SKU |

**Response (200 OK):** Product object

#### GET /products/category/{categoryId}

Get products by category with pagination.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| categoryId | string | Yes | Category ID |

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| page | integer | No | Page number. Default: 0 |
| size | integer | No | Page size. Default: 20 |

**Response (200 OK):** Paginated product list

#### GET /products/published

Get all published products with pagination.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| page | integer | No | Page number. Default: 0 |
| size | integer | No | Page size. Default: 20 |

**Response (200 OK):** Paginated product list

#### GET /products/search

Search products by keyword.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| keyword | string | Yes | Search keyword |
| page | integer | No | Page number. Default: 0 |
| size | integer | No | Page size. Default: 20 |

**Response (200 OK):** Paginated search results

#### POST /products/{id}/publish

Publish a product.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Product ID |

**Response (200 OK):** Published product object

#### POST /products/{id}/unpublish

Unpublish a product.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Product ID |

**Response (200 OK):** Unpublished product object

#### DELETE /products/{id}

Delete a product.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Product ID |

**Response (200 OK):** Success confirmation

#### GET /products

Get all products (admin only).

**Headers:** `Authorization: Bearer <token>`

**Response (200 OK):** Array of all product objects

---

### Jobs API (CMS)

#### POST /jobs

Create a new job posting.

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "title": "Senior Backend Engineer",
  "slug": "senior-backend-engineer",
  "shortDescription": "Join our engineering team...",
  "longDescription": "<p>Full job description...</p>",
  "department": "Engineering",
  "employmentType": "FULL_TIME",
  "experienceLevel": "SENIOR",
  "location": "Lagos, Nigeria",
  "remote": true,
  "availableRegions": ["NG", "KE", "GH"],
  "responsibilities": ["Design backend services"],
  "requirements": ["5+ years experience"],
  "benefits": ["Health insurance"],
  "salaryMin": 150000,
  "salaryMax": 200000,
  "salaryCurrency": "USD",
  "applicationUrl": "https://gogidix.com/apply",
  "deadline": "2024-03-31",
  "status": "DRAFT"
}
```

**Response (201 Created):** Job object

#### PUT /jobs/{id}

Update a job posting.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Job ID |

**Request Body:** Same as POST /jobs

**Response (200 OK):** Updated job object

#### GET /jobs/{id}

Get a job by ID.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Job ID |

**Response (200 OK):** Job object

#### GET /jobs/open

Get all open jobs.

**Response (200 OK):** Array of open job objects

#### POST /jobs/{id}/publish

Publish a job posting.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Job ID |

**Response (200 OK):** Published job object

#### POST /jobs/{id}/close

Close a job posting.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Job ID |

**Response (200 OK):** Closed job object

#### DELETE /jobs/{id}

Delete a job posting.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Job ID |

**Response (200 OK):** Success confirmation

---

### Leads API (CMS)

#### POST /leads

Create a new lead.

**Headers:** `Authorization: Bearer <token>` (for internal creation)

**Request Body:**
```json
{
  "type": "demo_request",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@company.com",
  "phone": "+234 123 456 7890",
  "company": "Acme Corp",
  "jobTitle": "CTO",
  "country": "Nigeria",
  "message": "Interested in demo",
  "productInterest": "logistics-platform",
  "source": "corporate-website",
  "status": "NEW"
}
```

**Response (201 Created):** Lead object

#### GET /leads

Get all leads with pagination.

**Headers:** `Authorization: Bearer <token>`

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| status | string | No | Filter by status |
| type | string | No | Filter by type |
| page | integer | No | Page number. Default: 0 |
| size | integer | No | Page size. Default: 20 |

**Response (200 OK):** Paginated lead list

#### GET /leads/{id}

Get a lead by ID.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Lead ID |

**Response (200 OK):** Lead object

#### PATCH /leads/{id}/status

Update lead status.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Lead ID |

**Request Body:**
```json
{
  "status": "CONTACTED",
  "note": "Initial contact made via phone"
}
```

**Response (200 OK):** Updated lead object

#### POST /leads/{id}/assign

Assign lead to a user.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Lead ID |

**Request Body:**
```json
{
  "assignedTo": "user-456"
}
```

**Response (200 OK):** Updated lead object

---

### Analytics API (CMS)

#### GET /analytics/dashboard

Get comprehensive analytics dashboard data.

**Headers:** `Authorization: Bearer <token>`

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| startDate | string | No | Start date (ISO-8601). Default: 30 days ago |
| endDate | string | No | End date (ISO-8601). Default: now |

**Response (200 OK):**
```json
{
  "contentStats": {
    "total": 450,
    "published": 380,
    "draft": 50,
    "pendingReview": 20
  },
  "mediaStats": {
    "total": 1250,
    "images": 800,
    "videos": 150,
    "documents": 300
  },
  "userStats": {
    "total": 25,
    "active": 20,
    "admins": 5,
    "editors": 10
  },
  "productStats": {
    "total": 15,
    "published": 12,
    "draft": 3
  },
  "jobStats": {
    "open": 8,
    "closed": 45,
    "onHold": 2
  },
  "leadStats": {
    "total": 1250,
    "new": 350,
    "contacted": 500,
    "qualified": 250,
    "converted": 150
  },
  "workflowStats": {
    "pending": 15,
    "approved": 300,
    "rejected": 25
  },
  "recentActivity": [
    {
      "type": "CONTENT_PUBLISHED",
      "description": "Blog post published",
      "user": "John Doe",
      "timestamp": "2024-02-01T10:00:00Z"
    }
  ]
}
```

#### GET /analytics/content

Get content statistics.

**Headers:** `Authorization: Bearer <token>`

**Response (200 OK):** Content statistics object

#### GET /analytics/media

Get media statistics.

**Headers:** `Authorization: Bearer <token>`

**Response (200 OK):** Media statistics object

#### GET /analytics/users

Get user statistics.

**Headers:** `Authorization: Bearer <token>`

**Response (200 OK):** User statistics object

#### GET /analytics/products

Get product statistics.

**Headers:** `Authorization: Bearer <token>`

**Response (200 OK):** Product statistics object

#### GET /analytics/jobs

Get job statistics.

**Headers:** `Authorization: Bearer <token>`

**Response (200 OK):** Job statistics object

#### GET /analytics/leads

Get lead statistics.

**Headers:** `Authorization: Bearer <token>`

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| startDate | string | No | Start date (ISO-8601) |
| endDate | string | No | End date (ISO-8601) |

**Response (200 OK):** Lead statistics object

#### GET /analytics/workflows

Get workflow statistics.

**Headers:** `Authorization: Bearer <token>`

**Response (200 OK):** Workflow statistics object

---

### User API

#### GET /users

Get all users with pagination.

**Headers:** `Authorization: Bearer <token>`

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| role | string | No | Filter by role |
| status | string | No | Filter by status |
| page | integer | No | Page number. Default: 0 |
| size | integer | No | Page size. Default: 20 |

**Response (200 OK):** Paginated user list

#### GET /users/{id}

Get a user by ID.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | User ID |

**Response (200 OK):** User object

#### PUT /users/{id}

Update a user.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | User ID |

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "role": "CONTENT_EDITOR",
  "status": "ACTIVE"
}
```

**Response (200 OK):** Updated user object

#### DELETE /users/{id}

Delete a user.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | User ID |

**Response (200 OK):** Success confirmation

#### POST /users/{id}/deactivate

Deactivate a user.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | User ID |

**Response (200 OK):** Updated user object

#### POST /users/{id}/activate

Activate a user.

**Headers:** `Authorization: Bearer <token>`

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | User ID |

**Response (200 OK):** Updated user object

---

## Core Marketing Service APIs

### Campaign Management Service (Port 8081)

Base URL: `http://localhost:8081/api/campaign/v1`

#### GET /campaigns
Get all campaigns with filtering.

#### POST /campaigns
Create a new campaign.

#### GET /campaigns/{id}
Get campaign by ID.

#### PUT /campaigns/{id}
Update a campaign.

#### DELETE /campaigns/{id}
Delete a campaign.

#### POST /campaigns/{id}/launch
Launch a campaign.

#### POST /campaigns/{id}/pause
Pause a campaign.

### Email Marketing Service (Port 8082)

Base URL: `http://localhost:8082/api/email/v1`

#### GET /campaigns
Get all email campaigns.

#### POST /campaigns
Create email campaign.

#### POST /campaigns/{id}/send
Send email campaign.

#### GET /templates
Get email templates.

#### POST /templates
Create email template.

#### GET /lists
Get email lists.

#### POST /lists/subscribers
Add subscriber to list.

### Social Media Service (Port 8083)

Base URL: `http://localhost:8083/api/social/v1`

#### GET /posts
Get social media posts.

#### POST /posts
Create social media post.

#### POST /posts/{id}/publish
Publish social media post.

#### GET /accounts
Get connected social accounts.

#### POST /accounts/connect
Connect social account.

#### GET /analytics
Get social media analytics.

### SEO Service (Port 8084)

Base URL: `http://localhost:8084/api/seo/v1`

#### GET /keywords
Get tracked keywords.

#### POST /keywords
Add keyword to track.

#### GET /backlinks
Get backlink data.

#### GET /rankings
Get keyword rankings.

#### GET /audit
Get SEO audit results.

### Analytics Service (Port 8086)

Base URL: `http://localhost:8086/api/analytics/v1`

#### GET /metrics
Get marketing metrics.

#### GET /campaigns/analytics
Get campaign analytics.

#### GET /channels/analytics
Get channel analytics.

#### GET /reports
Get analytics reports.

#### POST /reports
Create report.

#### POST /reports/{id}/generate
Generate report.

### Lead Generation Service (Port 8087)

Base URL: `http://localhost:8087/api/lead/v1`

#### GET /leads
Get all leads.

#### POST /leads
Create lead.

#### GET /leads/{id}
Get lead by ID.

#### PATCH /leads/{id}/status
Update lead status.

#### POST /leads/{id}/convert
Convert lead to opportunity.

#### GET /pipelines
Get sales pipelines.

---

## Common Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2024-02-01T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for field 'email'",
  "path": "/api/v1/leads"
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2024-02-01T10:00:00Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid or expired JWT token",
  "path": "/api/cms/v1/content"
}
```

### 403 Forbidden
```json
{
  "timestamp": "2024-02-01T10:00:00Z",
  "status": 403,
  "error": "Forbidden",
  "message": "Access denied for this resource",
  "path": "/api/cms/v1/users"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-02-01T10:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Resource not found with id: xyz",
  "path": "/api/v1/pages/xyz"
}
```

### 409 Conflict
```json
{
  "timestamp": "2024-02-01T10:00:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "Resource already exists",
  "path": "/api/cms/v1/content"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2024-02-01T10:00:00Z",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred",
  "path": "/api/v1/products"
}
```

### 503 Service Unavailable
```json
{
  "timestamp": "2024-02-01T10:00:00Z",
  "status": 503,
  "error": "Service Unavailable",
  "message": "Lead capture service is currently disabled",
  "path": "/api/v1/leads"
}
```

---

## Rate Limiting

API rate limits are enforced per tenant:

- **Default Limit**: 1000 requests per hour
- **Burst Limit**: 100 requests per minute

Rate limit headers are included in responses:

```
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 950
X-RateLimit-Reset: 1705305600
```

---

## Interactive Documentation

Swagger UI is available for all services:

- **Corporate Website**: `http://localhost:8095/api/v1/swagger-ui.html`
- **Corporate CMS**: `http://localhost:8096/api/cms/v1/swagger-ui.html`
- **Analytics**: `http://localhost:8086/api/analytics/v1/swagger-ui.html`

---

## Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | 2024-01 | Initial API documentation for core marketing services |
| 2.0.0 | 2024-02 | Added Corporate Website and Corporate CMS service APIs |

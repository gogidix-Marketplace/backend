# GOGIDIX CORPORATE WEBSITE - MOCK FLOW

**Version:** 1.0
**Domain:** Corporate Website
**Frontend:** corporate-website-public
**Last Updated:** 2025-02-08

---

## TABLE OF CONTENTS

1. [API Architecture](#1-api-architecture)
2. [Data Models & Types](#2-data-models--types)
3. [API Endpoints](#3-api-endpoints)
4. [State Management](#4-state-management)
5. [Mock Data Examples](#5-mock-data-examples)
6. [Webhook Events](#6-webhook-events)

---

## 1. API ARCHITECTURE

### 1.1 Base URLs

```
Production:  https://api.gogidix.com/public/v1
Staging:     https://api-staging.gogidix.com/public/v1
Development: https://api-dev.gogidix.com/public/v1
```

### 1.2 Authentication

Public website uses API Key authentication for write operations:

```typescript
const headers = {
  'X-API-Key': apiKey,
  'Content-Type': 'application/json',
  'Accept-Language': locale,
};
```

### 1.3 Response Format

```typescript
interface ApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  errors?: ValidationError[];
  meta?: ResponseMeta;
  timestamp: string;
  requestId: string;
}

interface ResponseMeta {
  version: string;
  rateLimit?: {
    remaining: number;
    resetAt: string;
  };
}
```

---

## 2. DATA MODELS & TYPES

### 2.1 Product Models

```typescript
// Product Category
interface ProductCategory {
  id: string;
  slug: string;
  name: string;
  description: string;
  icon: string;
  type: 'logistics' | 'ecommerce' | 'procurement' | 'business-ops' | 'enterprise' | 'infrastructure';
  order: number;
  products: ProductSummary[];
}

// Product Summary
interface ProductSummary {
  id: string;
  slug: string;
  name: string;
  tagline: string;
  description: string;
  icon: string;
  category: string;
  status: 'available' | 'beta' | 'coming-soon';
  features: string[];
  pricing?: PricingSummary;
}

// Product Detail
interface ProductDetail extends ProductSummary {
  longDescription: string;
  benefits: Benefit[];
  useCases: UseCase[];
  integrations: Integration[];
  caseStudies: CaseStudySummary[];
  documentation: DocumentationLink[];
  screenshots: Screenshot[];
  videos: Video[];
}

// Pricing
interface PricingSummary {
  startingAt: number;
  currency: string;
  period: 'month' | 'year';
  hasFreeTier: boolean;
  hasEnterprise: boolean;
}

interface PricingPlan {
  id: string;
  name: string;
  description: string;
  price: number;
  currency: string;
  period: 'month' | 'year';
  features: PricingFeature[];
  cta: string;
  popular?: boolean;
}

interface PricingFeature {
  name: string;
  included: boolean;
  limit?: number;
}

// Benefit
interface Benefit {
  id: string;
  title: string;
  description: string;
  icon: string;
  metric?: string;
}

// Use Case
interface UseCase {
  id: string;
  title: string;
  description: string;
  industry: string;
  companySize: string[];
  outcomes: string[];
}

// Integration
interface Integration {
  id: string;
  name: string;
  description: string;
  logo: string;
  category: string;
  certified: boolean;
}
```

### 2.2 Content Models

```typescript
// Blog Post
interface BlogPost {
  id: string;
  slug: string;
  title: string;
  excerpt: string;
  content: string;
  author: Author;
  category: BlogCategory;
  tags: string[];
  publishedAt: string;
  readTime: number;
  featured: boolean;
  coverImage?: string;
  seo: SEOData;
}

// Case Study
interface CaseStudy {
  id: string;
  slug: string;
  title: string;
  customer: CompanyProfile;
  industry: string;
  region: string;
  challenge: string;
  solution: string;
  results: CaseStudyResult[];
  testimonial?: Testimonial;
  coverImage: string;
  gallery: string[];
  publishedAt: string;
}

interface CaseStudyResult {
  metric: string;
  before?: string;
  after: string;
  improvement?: string;
}

// Press Release
interface PressRelease {
  id: string;
  slug: string;
  title: string;
  content: string;
  publishedAt: string;
  contact: PressContact;
  attachments: Attachment[];
}

// Documentation Page
interface DocumentationPage {
  id: string;
  slug: string;
  title: string;
  content: string;
  category: DocumentationCategory;
  order: number;
  updatedAt: string;
  toc?: TableOfContents;
}
```

### 2.3 Career Models

```typescript
// Job Posting
interface JobPosting {
  id: string;
  slug: string;
  title: string;
  department: Department;
  location: Location;
  type: EmploymentType;
  level: ExperienceLevel;
  description: string;
  responsibilities: string[];
  requirements: Requirement[];
  benefits: string[];
  salary?: SalaryRange;
  remote: boolean;
  status: 'open' | 'closed' | 'on-hold';
  publishedAt: string;
  expiresAt: string;
  applyUrl: string;
}

type EmploymentType = 'full-time' | 'part-time' | 'contract' | 'internship';
type ExperienceLevel = 'entry' | 'mid' | 'senior' | 'lead' | 'executive';
type Department = 'engineering' | 'product' | 'sales' | 'marketing' | 'operations' | 'finance' | 'hr';

interface Location {
  country: string;
  city: string;
  remote?: boolean;
}

interface SalaryRange {
  min: number;
  max: number;
  currency: string;
  period: 'hour' | 'month' | 'year';
}

interface Requirement {
  type: 'must-have' | 'nice-to-have';
  category: string;
  items: string[];
}

// Job Application
interface JobApplication {
  id: string;
  jobId: string;
  applicant: ApplicantInfo;
  resume: FileInfo;
  coverLetter?: string;
  portfolioUrl?: string;
  linkedinUrl?: string;
  githubUrl?: string;
  submittedAt: string;
  status: 'submitted' | 'under-review' | 'shortlisted' | 'interview' | 'offered' | 'rejected';
}

interface ApplicantInfo {
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  country: string;
  city: string;
}
```

### 2.4 Developer Models

```typescript
// API Documentation
interface APIDocumentation {
  id: string;
  slug: string;
  name: string;
  description: string;
  version: string;
  baseUrl: string;
  authentication: AuthScheme[];
  endpoints: APIEndpoint[];
  webhooks: WebhookDefinition[];
  sdks: SDKInfo[];
  guides: QuickStartGuide[];
}

interface APIEndpoint {
  method: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';
  path: string;
  summary: string;
  description: string;
  parameters: Parameter[];
  requestBody?: RequestBody;
  responses: ResponseDefinition[];
  examples: CodeExample[];
}

interface Parameter {
  name: string;
  in: 'path' | 'query' | 'header';
  type: string;
  required: boolean;
  description: string;
  schema?: any;
}

// SDK Info
interface SDKInfo {
  language: string;
  name: string;
  version: string;
  repository: string;
  npmPackage?: string;
  pipPackage?: string;
  composerPackage?: string;
  documentationUrl: string;
  installCommand: string;
}

// Webhook Definition
interface WebhookDefinition {
  event: string;
  description: string;
  payload: WebhookPayload;
  retryPolicy: RetryPolicy;
}

interface WebhookPayload {
  eventType: string;
  timestamp: string;
  data: any;
  signature: string;
}

interface RetryPolicy {
  maxAttempts: number;
  backoff: 'linear' | 'exponential';
  retryAfter: number;
}
```

### 2.5 Partner Models

```typescript
// Partner Program
interface PartnerProgram {
  id: string;
  slug: string;
  name: string;
  type: PartnerType;
  description: string;
  benefits: string[];
  requirements: PartnerRequirement[];
  applicationProcess: ApplicationStep[];
  resources: PartnerResource[];
}

type PartnerType = 'white-label' | 'technology' | 'system-integrator' | 'marketplace' | 'referral';

interface PartnerRequirement {
  category: string;
  items: string[];
}

interface ApplicationStep {
  step: number;
  title: string;
  description: string;
  duration: string;
}

// Partner Application
interface PartnerApplication {
  id: string;
  programId: string;
  company: CompanyInfo;
  contact: ContactInfo;
  capabilities: Capability[];
  businessModel: string;
  targetMarkets: string[];
  estimatedVolume: string;
  submittedAt: string;
  status: 'submitted' | 'under-review' | 'approved' | 'rejected' | 'more-info-needed';
}
```

---

## 3. API ENDPOINTS

### 3.1 Content Endpoints

```typescript
// GET /content/products
interface GetProductsEndpoint {
  GET: {
    query: {
      category?: string;
      type?: string;
      status?: string;
      locale?: string;
    };
    response: ProductSummary[];
  };
}

// GET /content/products/:slug
interface GetProductEndpoint {
  GET: {
    params: { slug: string };
    query: { locale?: string };
    response: ProductDetail;
  };
}

// GET /content/blog
interface GetBlogPostsEndpoint {
  GET: {
    query: {
      category?: string;
      tag?: string;
      featured?: boolean;
      page?: number;
      limit?: number;
      locale?: string;
    };
    response: PaginatedResponse<BlogPost>;
  };
}

// GET /content/blog/:slug
interface GetBlogPostEndpoint {
  GET: {
    params: { slug: string };
    query: { locale?: string };
    response: BlogPost;
  };
}

// GET /content/case-studies
interface GetCaseStudiesEndpoint {
  GET: {
    query: {
      industry?: string;
      product?: string;
      region?: string;
      featured?: boolean;
      locale?: string;
    };
    response: CaseStudy[];
  };
}

// GET /content/press-releases
interface GetPressReleasesEndpoint {
  GET: {
    query: {
      year?: number;
      locale?: string;
    };
    response: PressRelease[];
  };
}
```

### 3.2 Career Endpoints

```typescript
// GET /careers/jobs
interface GetJobsEndpoint {
  GET: {
    query: {
      department?: Department;
      location?: string;
      type?: EmploymentType;
      level?: ExperienceLevel;
      remote?: boolean;
      locale?: string;
    };
    response: JobPosting[];
  };
}

// GET /careers/jobs/:slug
interface GetJobEndpoint {
  GET: {
    params: { slug: string };
    query: { locale?: string };
    response: JobPosting;
  };
}

// POST /careers/jobs/:id/apply
interface ApplyJobEndpoint {
  POST: {
    params: { id: string };
    request: JobApplication;
    response: {
      applicationId: string;
      submittedAt: string;
      nextSteps: string[];
    };
  };
}
```

### 3.3 Developer Endpoints

```typescript
// GET /docs/api
interface GetAPIDocsEndpoint {
  GET: {
    query: {
      product?: string;
      version?: string;
    };
    response: APIDocumentation[];
  };
}

// GET /docs/api/:product
interface GetProductAPIDocsEndpoint {
  GET: {
    params: { product: string };
    query: { version?: string };
    response: APIDocumentation;
  };
}

// GET /docs/sdks
interface GetSDKsEndpoint {
  GET: {
    response: SDKInfo[];
  };
}

// POST /developer/sandbox
interface CreateSandboxAccountEndpoint {
  POST: {
    request: {
      email: string;
      company?: string;
      useCase?: string;
    };
    response: {
      apiKey: string;
      apiSecret: string;
      sandboxUrl: string;
      credits: number;
    };
  };
}

// GET /developer/status
interface GetSystemStatusEndpoint {
  GET: {
    response: SystemStatus;
  };
}
```

### 3.4 Partner Endpoints

```typescript
// GET /partners/programs
interface GetPartnerProgramsEndpoint {
  GET: {
    query: {
      type?: PartnerType;
      locale?: string;
    };
    response: PartnerProgram[];
  };
}

// GET /partners/programs/:slug
interface GetPartnerProgramEndpoint {
  GET: {
    params: { slug: string };
    query: { locale?: string };
    response: PartnerProgram;
  };
}

// POST /partners/apply
interface SubmitPartnerApplicationEndpoint {
  POST: {
    request: PartnerApplication;
    response: {
      applicationId: string;
      submittedAt: string;
      estimatedReviewTime: string;
    };
  };
}
```

### 3.5 Contact & Lead Endpoints

```typescript
// POST /contact/demo
interface RequestDemoEndpoint {
  POST: {
    request: DemoRequest;
    response: {
      requestId: string;
      submittedAt: string;
      followUpExpected: string;
    };
  };
}

interface DemoRequest {
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  company: string;
  companySize: string;
  country: string;
  productsOfInterest: string[];
  message?: string;
}

// POST /contact/sales
interface ContactSalesEndpoint {
  POST: {
    request: SalesInquiry;
    response: {
      inquiryId: string;
      submittedAt: string;
      assignedTo: string;
    };
  };
}

// POST /contact/support
interface ContactSupportEndpoint {
  POST: {
    request: SupportInquiry;
    response: {
      ticketId: string;
      ticketNumber: string;
      submittedAt: string;
    };
  };
}

// POST /newsletter/subscribe
interface SubscribeNewsletterEndpoint {
  POST: {
    request: {
      email: string;
      interests?: string[];
      locale?: string;
    };
    response: {
      subscribed: boolean;
      confirmationSent: boolean;
    };
  };
}
```

---

## 4. STATE MANAGEMENT

### 4.1 Store Structure (Zustand)

```typescript
interface CorporateWebsiteStore {
  // Content state
  content: {
    products: ProductSummary[];
    activeProduct: ProductDetail | null;
    blogPosts: BlogPost[];
    activeBlogPost: BlogPost | null;
    caseStudies: CaseStudy[];
    loading: boolean;
    error: string | null;
  };

  // Career state
  careers: {
    jobs: JobPosting[];
    activeJob: JobPosting | null;
    filters: JobFilters;
    application: JobApplication | null;
    loading: boolean;
    error: string | null;
  };

  // Developer state
  developer: {
    apiDocs: APIDocumentation[];
    activeAPI: APIDocumentation | null;
    sdks: SDKInfo[];
    sandboxAccount: SandboxAccount | null;
    systemStatus: SystemStatus | null;
    loading: boolean;
    error: string | null;
  };

  // Partner state
  partners: {
    programs: PartnerProgram[];
    activeProgram: PartnerProgram | null;
    application: PartnerApplication | null;
    loading: boolean;
    error: string | null;
  };

  // UI state
  ui: {
    locale: string;
    theme: 'light' | 'dark';
    sidebarOpen: boolean;
    mobileMenuOpen: boolean;
    searchOpen: boolean;
    activeModal: string | null;
    toast: Toast[];
  };

  // Lead state
  leads: {
    demoRequest: DemoRequest | null;
    salesInquiry: SalesInquiry | null;
    supportInquiry: SupportInquiry | null;
  };
}
```

### 4.2 React Query Keys

```typescript
const queryKeys = {
  // Content
  products: (category?: string) => ['content', 'products', category] as const,
  product: (slug: string) => ['content', 'product', slug] as const,
  blogPosts: (category?: string) => ['content', 'blog', category] as const,
  blogPost: (slug: string) => ['content', 'blog', slug] as const,
  caseStudies: (product?: string) => ['content', 'case-studies', product] as const,
  pressReleases: (year?: number) => ['content', 'press-releases', year] as const,

  // Careers
  jobs: (filters?: JobFilters) => ['careers', 'jobs', filters] as const,
  job: (slug: string) => ['careers', 'job', slug] as const,

  // Developer
  apiDocs: (product?: string) => ['developer', 'api-docs', product] as const,
  sdks: () => ['developer', 'sdks'] as const,
  systemStatus: () => ['developer', 'status'] as const,

  // Partners
  partnerPrograms: (type?: PartnerType) => ['partners', 'programs', type] as const,
  partnerProgram: (slug: string) => ['partners', 'program', slug] as const,
};
```

---

## 5. MOCK DATA EXAMPLES

### 5.1 Mock Product Data

```json
{
  "id": "prod-courier-cloud",
  "slug": "courier-management-cloud",
  "name": "Courier Management Cloud",
  "tagline": "Complete courier operations platform with 25+ integrated services",
  "description": "End-to-end courier management solution from package creation to final delivery",
  "icon": "/products/icons/courier.svg",
  "category": "logistics",
  "status": "available",
  "features": [
    "Package delivery & routing",
    "Real-time GPS tracking",
    "Driver fleet management",
    "Automated dispatch",
    "Route optimization with AI",
    "Proof of delivery capture",
    "Customer notifications",
    "Analytics dashboard"
  ],
  "pricing": {
    "startingAt": 49,
    "currency": "USD",
    "period": "month",
    "hasFreeTier": true,
    "hasEnterprise": true
  },
  "benefits": [
    {
      "id": "benefit-1",
      "title": "60% Faster Delivery",
      "description": "AI-powered route optimization reduces delivery times by up to 60%",
      "icon": "/icons/lightning.svg",
      "metric": "60% reduction"
    },
    {
      "id": "benefit-2",
      "title": "40% Cost Savings",
      "description": "Optimize resources and reduce operational costs significantly",
      "icon": "/icons/cost-down.svg",
      "metric": "40% savings"
    },
    {
      "id": "benefit-3",
      "title": "99.8% On-Time Rate",
      "description": "Reliable delivery with industry-leading on-time performance",
      "icon": "/icons/check-circle.svg",
      "metric": "99.8% on-time"
    }
  ],
  "useCases": [
    {
      "id": "usecase-1",
      "title": "Last-Mile Delivery",
      "description": "Perfect for companies managing last-mile delivery operations in urban areas",
      "industry": "Logistics",
      "companySize": ["SME", "Enterprise"],
      "outcomes": ["Reduced delivery times", "Lower fuel costs", "Happier customers"]
    },
    {
      "id": "usecase-2",
      "title": "E-commerce Fulfillment",
      "description": "Integrate with your e-commerce platform for seamless order fulfillment",
      "industry": "E-commerce",
      "companySize": ["SME", "Mid-Market"],
      "outcomes": ["Automated shipping", "Real-time updates", "Easy returns"]
    }
  ],
  "integrations": [
    {
      "id": "int-shopify",
      "name": "Shopify",
      "description": "Sync orders and automate shipping",
      "logo": "/integrations/shopify.svg",
      "category": "E-commerce",
      "certified": true
    },
    {
      "id": "int-woocommerce",
      "name": "WooCommerce",
      "description": "Seamless WooCommerce integration",
      "logo": "/integrations/woocommerce.svg",
      "category": "E-commerce",
      "certified": true
    }
  ],
  "documentation": [
    {
      "id": "doc-quickstart",
      "title": "Quick Start Guide",
      "url": "/docs/courier/quickstart",
      "type": "guide"
    },
    {
      "id": "doc-api-reference",
      "title": "API Reference",
      "url": "/docs/courier/api",
      "type": "api"
    }
  ]
}
```

### 5.2 Mock Blog Post

```json
{
  "id": "blog-001",
  "slug": "announcing-gogidix-2-0",
  "title": "Announcing Gogidix 2.0: The Next Generation of Logistics Platform",
  "excerpt": "We're excited to introduce Gogidix 2.0, featuring AI-powered route optimization, expanded global coverage, and a completely redesigned developer experience.",
  "content": "<p>Full blog post content here...</p>",
  "author": {
    "id": "author-001",
    "name": "Chinedu Okafor",
    "role": "CEO",
    "avatar": "/authors/chinedu.jpg",
    "bio": "Founder and CEO of Gogidix"
  },
  "category": {
    "id": "cat-product",
    "slug": "product-updates",
    "name": "Product Updates"
  },
  "tags": ["product", "release", "ai", "logistics"],
  "publishedAt": "2025-02-08T10:00:00Z",
  "readTime": 8,
  "featured": true,
  "coverImage": "/blog/covers/gogidix-2-0.jpg",
  "seo": {
    "title": "Announcing Gogidix 2.0 | Gogidix Blog",
    "description": "Introducing Gogidix 2.0 with AI-powered route optimization and expanded global coverage",
    "keywords": ["gogidix", "release", "logistics platform", "ai"],
    "ogImage": "/blog/covers/gogidix-2-0-og.jpg"
  }
}
```

### 5.3 Mock Job Posting

```json
{
  "id": "job-001",
  "slug": "senior-fullstack-engineer",
  "title": "Senior Full Stack Engineer",
  "department": "engineering",
  "location": {
    "country": "Nigeria",
    "city": "Lagos",
    "remote": true
  },
  "type": "full-time",
  "level": "senior",
  "description": "We're looking for a Senior Full Stack Engineer to join our growing engineering team and help build the future of logistics in Africa.",
  "responsibilities": [
    "Design and implement scalable web applications using React and Node.js",
    "Collaborate with product managers and designers to ship new features",
    "Write clean, maintainable, and well-tested code",
    "Mentor junior engineers and conduct code reviews",
    "Contribute to architectural decisions and technical strategy"
  ],
  "requirements": [
    {
      "type": "must-have",
      "category": "Experience",
      "items": [
        "5+ years of professional software development experience",
        "3+ years with React and TypeScript",
        "2+ years with Node.js and Express",
        "Experience with RESTful APIs and GraphQL"
      ]
    },
    {
      "type": "must-have",
      "category": "Skills",
      "items": [
        "Strong understanding of computer science fundamentals",
        "Experience with cloud platforms (AWS/GCP/Azure)",
        "Knowledge of database systems (PostgreSQL, MongoDB)",
        "Understanding of CI/CD pipelines"
      ]
    },
    {
      "type": "nice-to-have",
      "category": "Bonus",
      "items": [
        "Experience with logistics or e-commerce platforms",
        "Knowledge of Docker and Kubernetes",
        "Contributions to open source projects",
        "Experience working in distributed teams"
      ]
    }
  ],
  "benefits": [
    "Competitive salary and equity package",
    "Remote-first work culture",
    "Health insurance coverage",
    "Learning and development budget",
    "Generous vacation policy",
    "Parental leave",
    "Modern equipment and home office setup"
  ],
  "salary": {
    "min": 8000000,
    "max": 15000000,
    "currency": "NGN",
    "period": "year"
  },
  "remote": true,
  "status": "open",
  "publishedAt": "2025-02-01T00:00:00Z",
  "expiresAt": "2025-03-01T23:59:59Z",
  "applyUrl": "/careers/jobs/senior-fullstack-engineer/apply"
}
```

### 5.4 Mock System Status

```json
{
  "page": {
    "id": "status-page",
    "name": "Gogidix Status",
    "updated": "2025-02-08T14:30:00Z",
    "url": "https://status.gogidix.com"
  },
  "status": "operational",
  "components": [
    {
      "id": "courier-api",
      "name": "Courier API",
      "status": "operational",
      "description": "Package delivery and tracking services",
      "uptime": 99.99,
      "lastIncident": null
    },
    {
      "id": "warehouse-api",
      "name": "Warehouse API",
      "status": "operational",
      "description": "Inventory and warehouse management",
      "uptime": 99.98,
      "lastIncident": null
    },
    {
      "id": "ecommerce-api",
      "name": "E-commerce API",
      "status": "operational",
      "description": "Marketplace and order management",
      "uptime": 99.95,
      "lastIncident": {
        "date": "2025-02-01T10:15:00Z",
        "duration": "23 minutes",
        "description": "Brief degradation in order processing"
      }
    },
    {
      "id": "payment-gateway",
      "name": "Payment Gateway",
      "status": "operational",
      "description": "Payment processing services",
      "uptime": 99.99,
      "lastIncident": null
    },
    {
      "id": "developer-portal",
      "name": "Developer Portal",
      "status": "operational",
      "description": "API documentation and developer resources",
      "uptime": 100,
      "lastIncident": null
    }
  ],
  "incidents": [],
  "scheduledMaintenance": [
    {
      "id": "maintenance-001",
      "title": "Scheduled Maintenance - Courier API",
      "scheduledFor": "2025-02-15T02:00:00Z",
      "expectedDuration": "30 minutes",
      "description": "Routine maintenance to improve performance",
      "affectedComponents": ["courier-api"]
    }
  ],
  "metrics": {
    "uptime30Days": 99.97,
    "uptime90Days": 99.96,
    "avgResponseTime": 45,
    "p95ResponseTime": 120,
    "p99ResponseTime": 250
  }
}
```

---

## 6. WEBHOOK EVENTS

### 6.1 Career Application Events

```typescript
// application.received
interface ApplicationReceivedEvent {
  eventType: 'application.received';
  timestamp: string;
  data: {
    applicationId: string;
    jobId: string;
    jobTitle: string;
    applicant: {
      name: string;
      email: string;
      phone: string;
    };
  };
}

// application.status_changed
interface ApplicationStatusChangedEvent {
  eventType: 'application.status_changed';
  timestamp: string;
  data: {
    applicationId: string;
    previousStatus: string;
    newStatus: string;
    reason?: string;
  };
}
```

### 6.2 Partner Application Events

```typescript
// partner.application.submitted
interface PartnerApplicationSubmittedEvent {
  eventType: 'partner.application.submitted';
  timestamp: string;
  data: {
    applicationId: string;
    programId: string;
    programName: string;
    company: {
      name: string;
      website: string;
      country: string;
    };
  };
}

// partner.application.approved
interface PartnerApplicationApprovedEvent {
  eventType: 'partner.application.approved';
  timestamp: string;
  data: {
    applicationId: string;
    programId: string;
    partnerId: string;
    nextSteps: string[];
  };
}
```

### 6.3 Lead Events

```typescript
// lead.demo_requested
interface DemoRequestedEvent {
  eventType: 'lead.demo_requested';
  timestamp: string;
  data: {
    leadId: string;
    name: string;
    email: string;
    company: string;
    country: string;
    productsOfInterest: string[];
    estimatedDealSize?: string;
  };
}

// lead.qualified
interface LeadQualifiedEvent {
  eventType: 'lead.qualified';
  timestamp: string;
  data: {
    leadId: string;
    qualificationScore: number;
    assignedTo: string;
    followUpDate: string;
  };
}
```

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2025-02-08 | Initial Mock Flow Documentation |

---

**Document End**

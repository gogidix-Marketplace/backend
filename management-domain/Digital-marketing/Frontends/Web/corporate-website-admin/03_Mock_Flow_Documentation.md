# GOGIDIX CORPORATE WEBSITE ADMIN - MOCK FLOW

**Version:** 1.0
**Domain:** Corporate Website
**Frontend:** corporate-website-admin
**Last Updated:** 2025-02-08

---

## TABLE OF CONTENTS

1. [API Architecture](#1-api-architecture)
2. [Data Models & Types](#2-data-models--types)
3. [API Endpoints](#3-api-endpoints)
4. [State Management](#4-state-management)
5. [Mock Data Examples](#5-mock-data-examples)

---

## 1. API ARCHITECTURE

### 1.1 Base URLs

```
Production:  https://admin.gogidix.com/api/v1
Staging:     https://admin-staging.gogidix.com/api/v1
Development: https://admin-dev.gogidix.com/api/v1
```

### 1.2 Authentication

All admin requests require JWT authentication:

```typescript
const headers = {
  'Authorization': `Bearer ${accessToken}`,
  'Content-Type': 'application/json',
};
```

### 1.3 Response Format

```typescript
interface AdminApiResponse<T> {
  success: boolean;
  data: T;
  message?: string;
  errors?: ValidationError[];
  pagination?: PaginationInfo;
  timestamp: string;
  requestId: string;
}
```

---

## 2. DATA MODELS & TYPES

### 2.1 Content Management Models

```typescript
// Page Content
interface PageContent {
  id: string;
  slug: string;
  title: string;
  type: PageType;
  status: ContentStatus;
  language: string;
  content: ContentBlocks[];
  seo: SEOData;
  publishedAt?: Date;
  publishedBy?: string;
  createdAt: Date;
  updatedAt: Date;
  createdBy: string;
  updatedBy: string;
  version: number;
}

type PageType = 'homepage' | 'about' | 'contact' | 'product_overview' | 'solution' | 'custom';
type ContentStatus = 'draft' | 'pending_review' | 'scheduled' | 'published' | 'archived';

interface ContentBlocks {
  id: string;
  type: BlockType;
  order: number;
  content: Record<string, any>;
}

type BlockType =
  | 'hero'
  | 'text'
  | 'feature_cards'
  | 'image'
  | 'video'
  | 'testimonial'
  | 'cta'
  | 'pricing_table'
  | 'comparison_table'
  | 'form'
  | 'custom';

interface SEOData {
  metaTitle: string;
  metaDescription: string;
  keywords: string[];
  ogImage?: string;
  canonical?: string;
  noindex?: boolean;
}

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
  featuredImage?: string;
  gallery?: string[];
  status: ContentStatus;
  publishedAt?: Date;
  scheduledFor?: Date;
  createdAt: Date;
  updatedAt: Date;
  seo: SEOData;
}

// Press Release
interface PressRelease {
  id: string;
  slug: string;
  title: string;
  content: string;
  summary: string;
  contact: PressContact;
  attachments: Attachment[];
  status: ContentStatus;
  publishedAt?: Date;
  scheduledFor?: Date;
  createdAt: Date;
  updatedAt: Date;
  seo: SEOData;
}
```

### 2.2 Product Catalog Models

```typescript
// Product
interface Product {
  id: string;
  slug: string;
  name: string;
  tagline: string;
  description: string;
  longDescription: string;
  category: ProductCategory;
  subcategory?: string;
  type: ProductType;
  status: ProductStatus;
  icon: string;
  images: ProductImage[];
  features: ProductFeature[];
  benefits: Benefit[];
  useCases: UseCase[];
  pricing?: ProductPricing;
  seo: SEOData;
  createdAt: Date;
  updatedAt: Date;
}

type ProductCategory =
  | 'logistics'
  | 'ecommerce'
  | 'procurement'
  | 'business-ops'
  | 'enterprise'
  | 'infrastructure';

type ProductType = 'saas' | 'white-label' | 'api' | 'managed-service';
type ProductStatus = 'active' | 'beta' | 'coming-soon' | 'deprecated';

interface ProductFeature {
  id: string;
  name: string;
  description: string;
  icon: string;
  available: boolean;
}

interface ProductPricing {
  plans: PricingPlan[];
  hasFreeTier: boolean;
  customPricingAvailable: boolean;
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

// Product Integration
interface ProductIntegration {
  id: string;
  productId: string;
  name: string;
  description: string;
  logo: string;
  category: string;
  certificationLevel?: 'basic' | 'certified' | 'premium';
  documentationUrl?: string;
}
```

### 2.3 Career Models

```typescript
// Job Posting (Admin)
interface JobPostingAdmin {
  id: string;
  slug: string;
  title: string;
  department: Department;
  location: JobLocation;
  type: EmploymentType;
  level: ExperienceLevel;
  description: string;
  responsibilities: string[];
  requirements: RequirementGroup[];
  benefits: string[];
  salary?: SalaryRange;
  remote: boolean;
  status: JobStatus;
  publishedAt?: Date;
  expiresAt?: Date;
  applicationCount: number;
  newApplications: number;
  createdAt: Date;
  updatedAt: Date;
  createdBy: string;
}

type JobStatus = 'draft' | 'open' | 'closed' | 'on-hold' | 'archived';

interface JobLocation {
  country: string;
  city: string;
  remote?: boolean;
}

interface RequirementGroup {
  type: 'must-have' | 'nice-to-have';
  category: string;
  items: string[];
}

// Job Application (Admin)
interface JobApplicationAdmin {
  id: string;
  jobId: string;
  jobTitle: string;
  applicant: ApplicantInfo;
  resume: ResumeInfo;
  coverLetter?: string;
  portfolioUrl?: string;
  linkedinUrl?: string;
  githubUrl?: string;
  submittedAt: Date;
  status: ApplicationStatus;
  stage: ApplicationStage;
  rating?: number;
  notes: ApplicationNote[];
  interviewSchedule?: InterviewSchedule[];
  createdAt: Date;
  updatedAt: Date;
}

type ApplicationStatus = 'submitted' | 'under-review' | 'shortlisted' | 'interview' | 'offered' | 'hired' | 'rejected';
type ApplicationStage = 'screening' | 'technical' | 'cultural' | 'offer' | 'closed';
```

### 2.4 Developer Resource Models

```typescript
// API Documentation
interface APIDocumentationAdmin {
  id: string;
  product: string;
  version: string;
  name: string;
  description: string;
  baseUrl: string;
  endpoints: APIEndpointAdmin[];
  authentication: AuthScheme[];
  status: 'active' | 'deprecated' | 'beta';
  publishedAt?: Date;
  createdAt: Date;
  updatedAt: Date;
}

interface APIEndpointAdmin {
  id: string;
  method: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';
  path: string;
  summary: string;
  description: string;
  parameters: ParameterAdmin[];
  requestBody?: RequestBodyAdmin;
  responses: ResponseDefinition[];
  codeExamples: CodeExample[];
  deprecated?: boolean;
}

// SDK Info
interface SDKInfoAdmin {
  id: string;
  language: string;
  name: string;
  version: string;
  description: string;
  repository: string;
  npmPackage?: string;
  pipPackage?: string;
  composerPackage?: string;
  documentationUrl: string;
  installCommand: string;
  status: 'active' | 'deprecated' | 'beta';
  downloadCount: number;
  updatedAt: Date;
}
```

### 2.5 Lead Models

```typescript
// Demo Request
interface DemoRequest {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  company: string;
  companySize: CompanySize;
  country: string;
  productsOfInterest: string[];
  message?: string;
  status: LeadStatus;
  priority: LeadPriority;
  assignedTo?: string;
  source: LeadSource;
  submittedAt: Date;
  lastFollowUp?: Date;
  nextFollowUp?: Date;
  notes: LeadNote[];
}

type LeadStatus = 'new' | 'contacted' | 'qualified' | 'demo_scheduled' | 'proposal' | 'won' | 'lost';
type LeadPriority = 'low' | 'medium' | 'high';
type LeadSource = 'website' | 'referral' | 'partner' | 'event' | 'cold_outreach';
type CompanySize = '1-10' | '11-50' | '51-200' | '201-500' | '501-1000' | '1000+';

// Sales Inquiry
interface SalesInquiry {
  id: string;
  type: InquiryType;
  contact: ContactInfo;
  company: string;
  country: string;
  requirements: string;
  budget?: string;
  timeline?: string;
  status: LeadStatus;
  assignedTo?: string;
  submittedAt: Date;
  notes: LeadNote[];
}

type InquiryType = 'white-label' | 'api_integration' | 'partnership' | 'enterprise' | 'other';
```

### 2.6 Analytics Models

```typescript
// Site Analytics
interface SiteAnalytics {
  period: DateRange;
  summary: AnalyticsSummary;
  traffic: TrafficMetrics;
  content: ContentMetrics;
  conversions: ConversionMetrics;
  seo: SEOMetrics;
}

interface AnalyticsSummary {
  visits: number;
  uniqueVisitors: number;
  pageViews: number;
  avgSessionDuration: number;
  bounceRate: number;
  conversionRate: number;
}

interface TrafficMetrics {
  sources: TrafficSource[];
  topPages: PageMetric[];
  geographic: GeographicMetric[];
  devices: DeviceMetric[];
}

interface ContentMetrics {
  topPages: PageMetric[];
  topProducts: ProductMetric[];
  topBlogPosts: BlogMetric[];
  avgTimeOnPage: number;
  exitPages: PageMetric[];
}

interface ConversionMetrics {
  funnels: ConversionFunnel[];
  totalConversions: number;
  conversionRate: number;
  bySource: ConversionBySource[];
  byProduct: ConversionByProduct[];
}

interface SEOMetrics {
  ranking: KeywordRanking[];
  backlinks: BacklinkInfo[];
  technicalIssues: TechnicalIssue[];
  performance: PerformanceMetrics;
}
```

---

## 3. API ENDPOINTS

### 3.1 Content Endpoints

```typescript
// GET /content/pages
interface GetPagesEndpoint {
  GET: {
    query: {
      type?: PageType;
      status?: ContentStatus;
      language?: string;
      page?: number;
      limit?: number;
    };
    response: PaginatedResponse<PageContent>;
  };
}

// GET /content/pages/:id
interface GetPageEndpoint {
  GET: {
    params: { id: string };
    response: PageContent;
  };
}

// POST /content/pages
interface CreatePageEndpoint {
  POST: {
    request: CreatePageDto;
    response: PageContent;
  };
}

// PUT /content/pages/:id
interface UpdatePageEndpoint {
  PUT: {
    params: { id: string };
    request: UpdatePageDto;
    response: PageContent;
  };
}

// POST /content/pages/:id/publish
interface PublishPageEndpoint {
  POST: {
    params: { id: string };
    request: {
      scheduledFor?: Date;
    };
    response: PageContent;
  };
}

// GET /content/blog
interface GetBlogPostsEndpoint {
  GET: {
    query: {
      category?: string;
      status?: ContentStatus;
      page?: number;
      limit?: number;
    };
    response: PaginatedResponse<BlogPost>;
  };
}

// POST /content/blog
interface CreateBlogPostEndpoint {
  POST: {
    request: CreateBlogPostDto;
    response: BlogPost;
  };
}

// PUT /content/blog/:id
interface UpdateBlogPostEndpoint {
  PUT: {
    params: { id: string };
    request: UpdateBlogPostDto;
    response: BlogPost;
  };
}

// GET /content/press
interface GetPressReleasesEndpoint {
  GET: {
    query: {
      year?: number;
      status?: ContentStatus;
    };
    response: PressRelease[];
  };
}
```

### 3.2 Product Catalog Endpoints

```typescript
// GET /products
interface GetProductsEndpoint {
  GET: {
    query: {
      category?: ProductCategory;
      type?: ProductType;
      status?: ProductStatus;
    };
    response: Product[];
  };
}

// POST /products
interface CreateProductEndpoint {
  POST: {
    request: CreateProductDto;
    response: Product;
  };
}

// PUT /products/:id
interface UpdateProductEndpoint {
  PUT: {
    params: { id: string };
    request: UpdateProductDto;
    response: Product;
  };
}

// PUT /products/:id/pricing
interface UpdateProductPricingEndpoint {
  PUT: {
    params: { id: string };
    request: { pricing: ProductPricing };
    response: Product;
  };
}

// GET /products/:id/integrations
interface GetProductIntegrationsEndpoint {
  GET: {
    params: { id: string };
    response: ProductIntegration[];
  };
}

// POST /products/:id/integrations
interface AddProductIntegrationEndpoint {
  POST: {
    params: { id: string };
    request: CreateProductIntegrationDto;
    response: ProductIntegration;
  };
}
```

### 3.3 Career Endpoints

```typescript
// GET /careers/jobs
interface GetJobsEndpoint {
  GET: {
    query: {
      department?: Department;
      location?: string;
      status?: JobStatus;
    };
    response: JobPostingAdmin[];
  };
}

// POST /careers/jobs
interface CreateJobEndpoint {
  POST: {
    request: CreateJobDto;
    response: JobPostingAdmin;
  };
}

// PUT /careers/jobs/:id
interface UpdateJobEndpoint {
  PUT: {
    params: { id: string };
    request: UpdateJobDto;
    response: JobPostingAdmin;
  };
}

// POST /careers/jobs/:id/publish
interface PublishJobEndpoint {
  POST: {
    params: { id: string };
    request: { expiresAt?: Date };
    response: JobPostingAdmin;
  };
}

// GET /careers/applications
interface GetApplicationsEndpoint {
  GET: {
    query: {
      jobId?: string;
      status?: ApplicationStatus;
      department?: Department;
      page?: number;
      limit?: number;
    };
    response: PaginatedResponse<JobApplicationAdmin>;
  };
}

// PUT /careers/applications/:id/status
interface UpdateApplicationStatusEndpoint {
  PUT: {
    params: { id: string };
    request: { status: ApplicationStatus; note?: string };
    response: JobApplicationAdmin;
  };
}

// POST /careers/applications/:id/notes
interface AddApplicationNoteEndpoint {
  POST: {
    params: { id: string };
    request: { note: string; private: boolean };
    response: JobApplicationAdmin;
  };
}
```

### 3.4 Lead Endpoints

```typescript
// GET /leads/demo-requests
interface GetDemoRequestsEndpoint {
  GET: {
    query: {
      status?: LeadStatus;
      priority?: LeadPriority;
      assignedTo?: string;
      startDate?: Date;
      endDate?: Date;
    };
    response: DemoRequest[];
  };
}

// PUT /leads/demo-requests/:id/status
interface UpdateLeadStatusEndpoint {
  PUT: {
    params: { id: string };
    request: { status: LeadStatus; note?: string };
    response: DemoRequest;
  };
}

// PUT /leads/demo-requests/:id/assign
interface AssignLeadEndpoint {
  PUT: {
    params: { id: string };
    request: { assignedTo: string };
    response: DemoRequest;
  };
}

// POST /leads/demo-requests/:id/followup
interface ScheduleFollowUpEndpoint {
  POST: {
    params: { id: string };
    request: { scheduledFor: Date; note: string };
    response: DemoRequest;
  };
}
```

### 3.5 Analytics Endpoints

```typescript
// GET /analytics/summary
interface GetAnalyticsSummaryEndpoint {
  GET: {
    query: {
      period: 'today' | 'week' | 'month' | 'quarter' | 'year';
      startDate?: Date;
      endDate?: Date;
    };
    response: SiteAnalytics;
  };
}

// GET /analytics/traffic
interface GetTrafficEndpoint {
  GET: {
    query: {
      period: string;
      granularity: 'hour' | 'day' | 'week' | 'month';
    };
    response: TrafficMetrics;
  };
}

// GET /analytics/content
interface GetContentAnalyticsEndpoint {
  GET: {
    query: {
      period: string;
      limit?: number;
    };
    response: ContentMetrics;
  };
}

// GET /analytics/conversions
interface GetConversionsEndpoint {
  GET: {
    query: {
      period: string;
      funnel?: string;
    };
    response: ConversionMetrics;
  };
}

// GET /analytics/seo
interface GetSEOEndpoint {
  GET: {
    response: SEOMetrics;
  };
}
```

---

## 4. STATE MANAGEMENT

### 4.1 Store Structure (Zustand)

```typescript
interface AdminDashboardStore {
  // Auth state
  auth: {
    user: AdminUser | null;
    accessToken: string | null;
    permissions: Permission[];
    isAuthenticated: boolean;
  };

  // Content state
  content: {
    pages: PageContent[];
    activePage: PageContent | null;
    blogPosts: BlogPost[];
    activeBlogPost: BlogPost | null;
    pressReleases: PressRelease[];
    loading: boolean;
    error: string | null;
  };

  // Product catalog state
  products: {
    items: Product[];
    activeProduct: Product | null;
    integrations: ProductIntegration[];
    loading: boolean;
    error: string | null;
  };

  // Careers state
  careers: {
    jobs: JobPostingAdmin[];
    activeJob: JobPostingAdmin | null;
    applications: JobApplicationAdmin[];
    activeApplication: JobApplicationAdmin | null;
    stats: CareerStats;
    loading: boolean;
    error: string | null;
  };

  // Leads state
  leads: {
    demoRequests: DemoRequest[];
    salesInquiries: SalesInquiry[];
    supportTickets: SupportTicket[];
    activeLead: DemoRequest | null;
    loading: boolean;
    error: string | null;
  };

  // Analytics state
  analytics: {
    summary: SiteAnalytics | null;
    traffic: TrafficMetrics | null;
    content: ContentMetrics | null;
    conversions: ConversionMetrics | null;
    seo: SEOMetrics | null;
    loading: boolean;
    error: string | null;
  };

  // UI state
  ui: {
    sidebarOpen: boolean;
    selectedLanguage: string;
    notifications: Notification[];
    confirmDialog: ConfirmDialog | null;
  };
}
```

### 4.2 React Query Keys

```typescript
const queryKeys = {
  // Content
  pages: (filters?: PageFilters) => ['content', 'pages', filters] as const,
  page: (id: string) => ['content', 'page', id] as const,
  blogPosts: (filters?: BlogFilters) => ['content', 'blog', filters] as const,
  blogPost: (id: string) => ['content', 'blog', id] as const,
  pressReleases: (year?: number) => ['content', 'press', year] as const,

  // Products
  products: (filters?: ProductFilters) => ['products', filters] as const,
  product: (id: string) => ['products', id] as const,
  productIntegrations: (id: string) => ['products', id, 'integrations'] as const,

  // Careers
  jobs: (filters?: JobFilters) => ['careers', 'jobs', filters] as const,
  job: (id: string) => ['careers', 'job', id] as const,
  applications: (filters?: ApplicationFilters) => ['careers', 'applications', filters] as const,
  application: (id: string) => ['careers', 'application', id] as const,

  // Leads
  demoRequests: (filters?: LeadFilters) => ['leads', 'demo', filters] as const,
  salesInquiries: (filters?: LeadFilters) => ['leads', 'sales', filters] as const,

  // Analytics
  analyticsSummary: (period: string) => ['analytics', 'summary', period] as const,
  traffic: (period: string) => ['analytics', 'traffic', period] as const,
  content: (period: string) => ['analytics', 'content', period] as const,
  conversions: (period: string) => ['analytics', 'conversions', period] as const,
  seo: () => ['analytics', 'seo'] as const,
};
```

---

## 5. MOCK DATA EXAMPLES

### 5.1 Mock Dashboard Data

```json
{
  "summary": {
    "pages": { "total": 127, "active": 98, "draft": 29 },
    "blogPosts": { "total": 48, "published": 35, "draft": 13 },
    "careers": { "openJobs": 12, "newApplications": 45 },
    "products": { "active": 32, "beta": 5 }
  },
  "sitePerformance": {
    "visitorsToday": 12456,
    "pageViews": 45678,
    "avgSessionDuration": 204,
    "bounceRate": 32,
    "vsYesterday": { "visitors": 15, "pageViews": 8, "duration": -5, "bounce": -2 }
  },
  "pendingActions": [
    { "type": "blog", "count": 3, "priority": "high" },
    { "type": "applications", "count": 5, "priority": "medium" },
    { "type": "leads", "count": 2, "priority": "high" },
    { "type": "product", "count": 1, "priority": "low" }
  ],
  "recentActivity": [
    { "user": "Sarah", "action": "published", "entity": "blog post", "name": "Q1 Updates", "time": "5 min ago" },
    { "user": "John", "action": "created", "entity": "job posting", "name": "Senior Dev", "time": "1 hour ago" },
    { "user": "Mike", "action": "updated", "entity": "product pricing", "name": "Courier Cloud", "time": "2 hours ago" },
    { "user": "Emma", "action": "submitted", "entity": "press release", "name": "Partnership", "time": "3 hours ago" }
  ]
}
```

### 5.2 Mock Content Data

```json
{
  "pages": [
    {
      "id": "page-001",
      "slug": "home",
      "title": "Home",
      "type": "homepage",
      "status": "published",
      "language": "en",
      "content": [
        { "id": "block-001", "type": "hero", "order": 1, "content": { "headline": "The Global Logistics & E-commerce Platform", "subtext": "Powering businesses across 50+ countries", "cta": { "text": "Get Started", "link": "/products/start" } } },
        { "id": "block-002", "type": "feature_cards", "order": 2, "content": { "title": "Our Products", "cards": [...] } }
      ],
      "seo": { "metaTitle": "Gogidix | Global Logistics & E-commerce Platform", "metaDescription": "Powering businesses worldwide with unified logistics and e-commerce infrastructure." },
      "publishedAt": "2025-01-01T00:00:00Z",
      "updatedBy": "sarah.j@gogidix.com",
      "updatedAt": "2025-02-08T10:00:00Z"
    }
  ],
  "blogPosts": [
    {
      "id": "blog-001",
      "slug": "announcing-gogidix-2-0",
      "title": "Announcing Gogidix 2.0: The Next Generation",
      "excerpt": "We're excited to introduce Gogidix 2.0 with AI-powered route optimization...",
      "author": { "id": "author-001", "name": "Chinedu Okafor", "role": "CEO", "avatar": "/authors/chinedu.jpg" },
      "category": { "id": "cat-product", "name": "Product Updates" },
      "status": "published",
      "publishedAt": "2025-02-08T10:00:00Z",
      "readTime": 8
    }
  ]
}
```

### 5.3 Mock Product Data

```json
{
  "products": [
    {
      "id": "prod-courier-cloud",
      "slug": "courier-management-cloud",
      "name": "Courier Management Cloud",
      "tagline": "Complete courier operations platform with 25+ integrated services",
      "category": "logistics",
      "type": "saas",
      "status": "active",
      "features": [
        { "id": "feat-001", "name": "Package Management", "description": "End-to-end package lifecycle tracking", "icon": "package", "available": true },
        { "id": "feat-002", "name": "Real-time Tracking", "description": "GPS tracking for every shipment", "icon": "location", "available": true },
        { "id": "feat-003", "name": "AI Routing", "description": "Intelligent route optimization", "icon": "route", "available": true }
      ],
      "pricing": {
        "plans": [
          { "id": "plan-starter", "name": "Starter", "price": 49, "currency": "USD", "period": "month", "features": [...], "popular": false },
          { "id": "plan-pro", "name": "Professional", "price": 199, "currency": "USD", "period": "month", "features": [...], "popular": true },
          { "id": "plan-enterprise", "name": "Enterprise", "price": null, "currency": "USD", "period": "month", "features": [...], "popular": false }
        ],
        "hasFreeTier": true,
        "customPricingAvailable": true
      }
    }
  ]
}
```

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2025-02-08 | Initial Mock Flow Documentation |

---

**Document End**

# GOGIDIX CORPORATE WEBSITE ADMIN - CONTENT CMS

**Version:** 1.0
**Domain:** Corporate Website
**Frontend:** corporate-website-admin
**Last Updated:** 2025-02-08

---

## TABLE OF CONTENTS

1. [CMS Overview](#cms-overview)
2. [Content Types](#content-types)
3. [Editor Features](#editor-features)
4. [Media Management](#media-management)
5. [Content Workflows](#content-workflows)

---

## 1. CMS OVERVIEW

### CMS Architecture

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                         CONTENT MANAGEMENT SYSTEM                                           │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │                        CONTENT EDITORS                                                    │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐                         │  │
│  │  │ Page Editor  │  │ Blog Editor  │  │ Press Editor │  │ Product     │                         │  │
│  │  │             │  │             │  │             │  │ Editor      │                         │  │
│  │  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘                         │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │                        CONTENT BLOCKS                                                     │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  Hero • Text • Feature Cards • Image • Video • Testimonial • CTA • Pricing • Form      │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │                        MEDIA LIBRARY                                                      │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  Images • Videos • Documents • Audio • Icons                                              │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │                        CONTENT VERSIONING                                                   │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  Version History • Rollback • Drafts • Scheduling                                         │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │                        SEO TOOLS                                                          │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  Meta Tags • Open Graph • Sitemap • Robots.txt • Canonical                              │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

### Supported Content Types

| Content Type | Description | Template Options | Custom Fields |
|--------------|-------------|------------------|---------------|
| **Page** | Standalone pages (home, about, contact) | Homepage, About, Contact, Custom | ✓ |
| **Blog Post** | News, updates, articles | Standard, Guest Author, Press Release | ✓ |
| **Press Release** | Official press communications | Standard, With Media | ✓ |
| **Product** | Product catalog entries | Standard, Enterprise, API-only | ✓ |
| **Job Posting** | Career opportunities | Standard, Executive, Internship | ✓ |
| **Case Study** | Customer success stories | Standard, Video-First | ✓ |
| **Event** | Webinars, conferences, meetups | Upcoming, Past, Registration | ✓ |

---

## 2. CONTENT TYPES

### Page Content Structure

```typescript
interface PageContent {
  id: string;
  slug: string;
  title: string;
  type: PageType;

  // Content Blocks
  blocks: ContentBlock[];

  // Configuration
  settings: {
    showInNav: boolean;
    allowComments: boolean;
    requireAuth: boolean;
    customCSS?: string;
    customJS?: string;
  };

  // SEO
  seo: {
    metaTitle: string;
    metaDescription: string;
    ogImage?: string;
    canonical?: string;
    noindex?: boolean;
  };

  // Publishing
  status: ContentStatus;
  publishedAt?: Date;
  scheduledFor?: Date;

  // Versioning
  version: number;
  versions: PageVersion[];
}

interface ContentBlock {
  id: string;
  type: BlockType;
  order: number;

  // Common Properties
  visible: boolean;

  // Type-specific Content
  content: BlockContent;

  // Styling
  styles?: BlockStyles;
}

type BlockType =
  | 'hero'           // Hero section with headline, subtext, CTA
  | 'text'           // Rich text content
  | 'html'           // Raw HTML
  | 'columns'        // Multi-column layout
  | 'feature_cards'  // Feature showcase cards
  | 'testimonials'   // Customer testimonials
  | 'pricing_table'  // Pricing comparison table
  | 'cta'            // Call-to-action block
  | 'image_gallery'  // Image gallery/grid
  | 'video'          // Video embed
  | 'form'           // Embedd form
  | 'stats'          // Statistics/numbers
  | 'team'           // Team member profiles
  | 'faq'            // Accordion FAQ
  | 'divider'        // Visual divider
  | 'spacer'         // Space
  | 'code_block'     // Code snippet
  | 'custom';        // Custom block
```

### Blog Post Structure

```typescript
interface BlogPost {
  id: string;
  slug: string;
  title: string;
  excerpt: string;
  content: string;  // Rich text content

  // Classification
  category: BlogCategory;
  tags: string[];

  // Media
  featuredImage?: MediaItem;
  gallery?: MediaItem[];

  // Author
  author: {
    id: string;
    name: string;
    slug: string;
    avatar?: MediaItem;
    bio?: string;
  };

  // Publishing
  status: ContentStatus;
  publishedAt?: Date;
  scheduledFor?: Date;

  // SEO
  seo: SEOData;

  // Engagement
  viewCount: number;
  readTime: number;

  // Related
  relatedPosts?: string[];  // Post IDs
}
```

---

## 3. EDITOR FEATURES

### Rich Text Editor

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  Rich Text Editor                                                                              │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │ [B] [I] [U] [S] | [≡] | H1 [H2] [H3] |  •  1   | [↑] [↓] | Undo  | Redo                         │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │ Link │ Image │ Video │ Table │ Code Quote │ Emoji │ Media │ Insert Dynamic Content           │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │                                                                                   │  │
│  │  Content editing area with live preview...                                   │  │
│  │                                                                                   │  │
│  │                                                                                   │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │ [HTML] [Preview] [Fullscreen]    Word count: 542    Last saved: 2 min ago      [Save]  │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

### Block Builder

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  Page Block Builder                                                                            │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  Available Blocks:                                                                            │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │ [🎨 Hero] [📝 Text] [🖼️ Image] [🎬 Video] [💬 Testimonials]                              │  │
│  │ [💰 Pricing] [🔘 Feature Cards] [📊 Stats] [📢 CTA] [📏 Code] [📑 FAQ]              │  │
│  │ [🖼️ Gallery] [📋 Table] [👥 Team] [➗ Divider] [⬛ Spacer] [🔧 Custom]              │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  Page Structure (Drag to reorder):                                                              │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │ ┌─────────────────────────────────────────────────────────────────────────────────┐ │  │
│  │ │ 1. Hero Section                                                     [Edit] [×]       │ │  │
│  │ │   Headline: "The Global Logistics & E-commerce Platform"                       │ │  │
│  │ │   CTA: "Get Started" →                                                            │ │  │
│  │ └─────────────────────────────────────────────────────────────────────────────────┘ │  │
│  │ ┌─────────────────────────────────────────────────────────────────────────────────┐ │  │
│  │ │ 2. Trusted By Section                                             [Edit] [×]       │ │  │
│  │ │   Showing 5 company logos                                                        │ │  │
│  │ └─────────────────────────────────────────────────────────────────────────────────┘ │  │
│  │ ┌─────────────────────────────────────────────────────────────────────────────────┐ │  │
│  │ │ 3. Product Categories                                             [Edit] [×]       │ │  │
│  │ │   4 category cards                                                               │ │  │
│  │ └─────────────────────────────────────────────────────────────────────────────────┘ │  │
│  │ [+ Add Block]                                                                               │  │
│  │                                                                                             │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  [Preview Page] [Publish Changes] [Save Draft]                                                          │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

### SEO Configuration

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  SEO Configuration                                                                             │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  BASIC SEO                                                                                 │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  Meta Title:                                                                                │  │
│  │  [Gogidix | Global Logistics & E-commerce Platform                         ] 60/70    │  │
│  │                                                                                            │  │
│  │  Meta Description:                                                                          │  │
│  │  [Powering businesses worldwide with unified logistics and e-commerce...] 160/160   │  │
│  │                                                                                            │  │
│  │  Keywords:                                                                                  │  │
│  │  [logistics, e-commerce, shipping, warehouse, fulfillment                ] [Add]     │  │
│  │  logistics [×] e-commerce [×] shipping [×] warehouse [×] fulfillment [×]          │  │
│  │                                                                                            │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  OPEN GRAPH                                                                                 │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  OG Image:                               [Browse...]                          [Clear]      │  │
│  │  Current: /uploads/og-images/home-og.jpg                                          │  │
│  │                                                                                            │  │
│  │  OG Type:        [website ▼]                                                         │  │
│  │  OG Title:       [Uses meta title ▼]                                                 │  │
│  │  OG Description: [Uses meta description ▼]                                          │  │
│  │                                                                                            │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  ADVANCED                                                                                  │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  Canonical URL:     [/home                                              ]                  │  │
│  │  No Index:          ☑ Don't index this page                                            │  │
│  │  No Follow:         ☑ Don't follow links                                              │  │
│  │                                                                                            │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  [Preview Google Search Result] [Check SEO Score]                                            │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

## 4. MEDIA MANAGEMENT

### Media Library

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  Media Library                                            [+ Upload] [New Folder]        │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  Folder: [Root ▼]    Type: [All ▼]    Date: [Newest ▼]              Search: [________] [🔍] │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  📁 hero-images           📁 product-icons        📁 team-photos                       │  │
│  │  📁 press-assets          📁 blog-thumbnails       📁 documents                        │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │ ┌────────────────────┐  ┌────────────────────┐  ┌────────────────────┐  ┌─────────────────┐│  │
│  │ │ [Image Preview]   │  │ [Image Preview]   │  │ [Image Preview]   │  │ [Image Preview] ││  │
│  │ │                   │  │                   │  │                   │  │                  ││  │
│  │ │ hero-bg-1.jpg     │  │ product-icon.png   │  │ ceo-photo.jpg     │  │ brochure.pdf     ││  │
│  │ │                   │  │                   │  │                   │  │                  ││  │
│  │ │ 📊 2.4 MB         │  │ 📊 45 KB          │  │ 📊 1.2 MB         │  │ 📊 8.7 MB        ││  │
│  │ │                   │  │                   │  │                   │  │                  ││  │
│  │ │ ✓ Used           │  │                   │  │ ✓ Used (3)       │  │                  ││  │
│  │ └────────────────────┘  └────────────────────┘  └────────────────────┘  └─────────────────┘│  │
│  │                                                                                             │  │
│  │  ┌────────────────────┐  ┌────────────────────┐  ┌────────────────────┐  ┌─────────────────┐│  │
│  │ │ [Image Preview]   │  │ [Image Preview]   │  │ [Image Preview]   │  │ [Image Preview] ││  │
│  │ │                   │  │                   │  │                   │  │                  ││  │
│  │ │ ...               │  │ ...               │  │ ...               │  │ ...              ││  │
│  │ │                   │  │                   │  │                   │  │                  ││  │
│  │ └────────────────────┘  └────────────────────┘  └────────────────────┘  └─────────────────┘│  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  Showing 1-12 of 156 items                                                   Rows: [12 ▼] [Load More]    │
│                                                                                                │
│  [Bulk Edit] [Delete Selected] [Download Selected]                                                   │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

### Image Editor

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  Image Editor                                    [×] Close                                                    │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │                                                                                           │  │
│  │                    [Image Preview Area]                                       │  │
│  │                           1024 x 768 px                                      │  │
│  │                                                                                           │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  Edit                                                                                     │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  Crop [┇] • Rotate ↻ • Adjust ◑ • Brightness ◐ • Contrast ◐ • Saturation ◐                │  │
│  │                                                                                          │  │
│  │  Filters: [None ▼] • Grayscale • Sepia • Blur • Sharpen                                  │  │
│  │                                                                                          │  │
│  │  Presets:  [Original] [Vivid] [Soft] [Warm] [Cool]                                  │  │
│  │                                                                                          │  │
│  │  Resize to: [Original ▼] [1920x1080] [1280x720] [Custom ______]                         │  │
│  │                                                                                          │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  Alt Text: [Enter image description for accessibility...]                            │  │
│  │  Title:    [Optional image title]                                                       │  │
│  │  Caption:  [Optional image caption]                                                     │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  [Cancel]    [Save Copy]    [Replace Original]                                                    │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

## 5. CONTENT WORKFLOWS

### Approval Workflow Configuration

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  Workflow Settings                                                                             │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  CONTENT APPROVAL WORKFLOW                                                               │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │                                                                                           │  │
│  │  Enable Approval Workflow:   ☑ On                                                      │  │
│  │                                                                                           │  │
│  │  Approvers by Content Type:                                                              │  │
│  │  ┌─────────────────────────────────────────────────────────────────────────────────┐│  │
│  │  │ Content Type       │    Required Approver(s)                          │       │ │  │
│  │  ├────────────────────┼────────────────────────────────────────────────────┼───────┤ │  │
│  │  │ Homepage           │    ☑ Content Manager, Digital Marketing Lead     │       │ │  │
│  │  │ About/Careers      │    ☐ Content Manager only                          │       │ │  │
│  │  │ Press Releases      │    ☑ PR Manager, Content Manager                  │       │ │  │
│  │  │ Blog Posts          │    ☐ Content Manager only (auto-publish after X hours) ││  │
│  │  │ Product Pages       │    ☑ Product Manager, Content Manager              │       │ │  │
│  │  │ Job Postings        │    ☑ HR Manager, Content Manager                  │       │ │  │
│  │  └────────────────────┴────────────────────────────────────────────────────┴───────┘ │  │
│  │                                                                                           │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  AUTO-PUBLISHING SETTINGS                                                                 │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  Auto-publish after approval: ☑ On                                                     │  │
│  │  Publish immediately: ☐ On                                                            │  │
│  │  Scheduled publish: ☐ On                                                              │  │
│  │                                                                                           │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  NOTIFICATIONS                                                                            │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │  Notify creator on:                                                                     │  │
│  │  ☑ Approval received                                                                      │  │
│  │  ☑ Approval required (with reason)                                                      │  │
│  │  ☑ Content published                                                                     │  │
│  │  ☑ Content rejected (with reason)                                                       │  │
│  │                                                                                           │  │
│  │  Notify approvers on:                                                                   │  │
│  │  ☑ New submission requiring review                                                      │  │
│  │  ☑ Changes made to submitted content                                                   │  │
│  │                                                                                           │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  [Save Changes]                                                                               │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

### Content Scheduling

```
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│  Schedule Publishing                                                                          │
├────────────────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                                │
│  ┌────────────────────────────────────────────────────────────────────────────────────────┐  │
│  │  SCHEDULED CONTENT                                                                         │  │
│  ├────────────────────────────────────────────────────────────────────────────────────────┤  │
│  │                                                                                           │  │
│  │  ┌────────────────────────────────────────────────────────────────────────────────┐  │  │
│  │  │  Blog Post                  │  Scheduled For         │  Status    │  Actions  │  │  │
│  │  ├───────────────────────────┼──────────────────────────┼───────────┼──────────┤  │  │
│  │  │  "Q1 Product Updates"     │  Feb 15, 2025 10:00    │  Pending   │  [Edit]   │  │  │
│  │  │  by Sarah                │                          │           │  [Cancel] │  │  │
│  │  ├───────────────────────────┼──────────────────────────┼───────────┼──────────┤  │  │
│  │  │  "Team Growth Story"      │  Feb 18, 2025 09:00    │  Pending   │  [Edit]   │  │  │
│  │  │  by John                 │                          │           │  [Cancel] │  │  │
│  │  ├───────────────────────────┼──────────────────────────┼───────────┼──────────┤  │  │
│  │  │  "New Partnership"        │  Feb 20, 2025 14:00    │  Pending   │  [Edit]   │  │  │
│  │  │  PR by Emma               │                          │           │  [Cancel] │  │  │
│  │  └───────────────────────────┴──────────────────────────┴───────────┴──────────┘  │  │
│  │                                                                                           │  │
│  │  [+ Schedule New Content]                                                               │  │
│  └────────────────────────────────────────────────────────────────────────────────────────┘  │
│                                                                                                │
│  Calendar View: [List ▼] [Month ▼]                                                               │
│                                                                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
```

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2025-02-08 | Initial Content CMS Documentation |

---

**Document End**

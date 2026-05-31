# FIELD SALES REPRESENTATIVE APP - UI FLOW DOCUMENTATION

**Version:** 1.0
**Domain:** Business Domain
**Subdomain:** Country-Sales-Dashboard
**Frontend:** field-sales-rep-app (Mobile)
**Framework:** React Native + Expo + TypeScript
**State Management:** Redux Toolkit + RTK Query
**Last Updated:** 2026-03-08

---

## TABLE OF CONTENTS

1. [App Overview](#1-app-overview)
2. [User Personas & Roles](#2-user-personas--roles)
3. [Navigation Architecture](#3-navigation-architecture)
4. [UI Flow Diagrams](#4-ui-flow-diagrams)
5. [Feature-Specific Flows](#5-feature-specific-flows)
6. [Cross-Domain Integration Flows](#6-cross-domain-integration-flows)
7. [Authentication & Onboarding](#7-authentication--onboarding)
8. [Error Handling & States](#8-error-handling--states)

---

## 1. APP OVERVIEW

### 1.1 Purpose

The Field Sales Representative App is a mobile application designed for Sales Representatives (company employees) who conduct field operations across their assigned territories. The app enables:

1. **Daily Route Management** - Plan and navigate daily sales routes efficiently
2. **Partner Onboarding** - Register and verify new partners on-site during visits
3. **Lead Capture** - Capture leads and opportunities during field visits
4. **Customer Visits** - Log and track customer/partner site visits
5. **Order Entry** - Create orders on-site during customer meetings
6. **Performance Tracking** - View targets, achievements, and commission progress
7. **Communication** - Stay connected with the country sales office

### 1.2 App Scope

**Primary Features:**
| Feature | Description |
|---------|-------------|
| **Route Management** | Daily route planning, GPS navigation, visit optimization |
| **Check-in/Check-out** | GPS-based location verification for site visits |
| **Partner Onboarding** | Document capture, photo capture, application submission |
| **Lead Management** | Lead creation, qualification, follow-up scheduling |
| **Order Entry** | Product catalog search, order creation, signature capture |
| **Customer Management** | Customer profiles, visit history, notes |
| **Expenses** - Field expense tracking and reimbursement submission |
| **Performance** | Targets, achievements, leaderboards |
| **Communication** | Messages from office, announcements |

### 1.3 User Context

```
┌─────────────────────────────────────────────────────────────────────────┐
│                       FIELD SALES REP APP USERS                         │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│   ┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐     │
│   │ Sales Executive  │    │ Sales Rep       │    │ Field Sales     │     │
│   │ (Senior)        │    │ (Mid-Level)     │    │ Agent           │     │
│   │                 │    │                 │    │                 │     │
│   │ Multiple        │    │ Assigned        │    │ Single          │     │
│   │ Territories     │    │ Territory       │    │ Territory       │     │
│   │ High Quota      │    │ Moderate Quota  │    │ Base Quota      │     │
│   └─────────────────┘    └─────────────────┘    └─────────────────┘     │
│                                                                          │
│   All users:                                                              │
│   • Assigned to ONE country                                              │
│   • Report to Country Sales Manager                                      │
│   • Use app daily for field operations                                   │
│   • Require offline capability                                           │
└─────────────────────────────────────────────────────────────────────────┘
```

### 1.4 Relationship to Ecosystem

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        ECOSYSTEM INTEGRATION                            │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│   FIELD SALES REP APP                                                    │
│          │                                                                │
│          ├──────────────► Country Sales Dashboard (Web)                  │
│          │                • Route assignment                              │
│          │                • Lead allocation                              │
│          │                • Performance reporting                        │
│          │                                                                │
│          ├──────────────► Partner Onboarding Service                      │
│          │                • Partner registration                          │
│          │                • Document verification                         │
│          │                                                                │
│          ├──────────────► Shared Business Infrastructure                 │
│          │                • Courier Services Core                        │
│          │                • Haulage Services Core                        │
│          │                • Warehousing Services Core                    │
│          │                • E-commerce Services Core                     │
│          │                                                                │
│          └──────────────► Location Services                              │
│                          • GPS tracking                                   │
│                          • Check-in verification                         │
│                          • Route optimization                             │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 2. USER PERSONAS & ROLES

### 2.1 Sales Executive (Senior)

**Profile:**
- 5+ years of sales experience
- Manages multiple territories or regions
- High sales quota (₦50M+ monthly target)
- May have junior reps reporting to them

**Key Needs:**
- Comprehensive territory overview
- Team performance visibility (if leads others)
- Advanced partner onboarding capabilities
- Detailed analytics and reporting

**App Permissions:**
- Full access to all features
- Can view team performance (if designated as lead)
- Can approve certain partner applications
- Can override routing suggestions

### 2.2 Sales Representative (Mid-Level)

**Profile:**
- 2-5 years of sales experience
- Manages assigned territory
- Moderate sales quota (₦25-50M monthly target)
- Independent field work

**Key Needs:**
- Efficient daily route planning
- Quick lead capture
- Partner onboarding tools
- Performance tracking against targets

**App Permissions:**
- Full access to personal features
- Can submit partner applications
- Can create and manage own leads/orders
- View personal performance only

### 2.3 Field Sales Agent

**Profile:**
- Entry-level sales role (0-2 years experience)
- Assigned specific territory or postal codes
- Base sales quota (₦10-25M monthly target)
- May focus on specific product lines

**Key Needs:**
- Simple, guided workflows
- Clear daily task list
- Easy lead capture
- Training resources accessible in-app

**App Permissions:**
- Access to assigned features
- Can submit partner applications (requires approval)
- Can create leads and orders
- View personal performance

---

## 3. NAVIGATION ARCHITECTURE

### 3.1 Primary Navigation Structure

```
┌─────────────────────────────────────────────────────────────────────────┐
│                      FIELD SALES REP APP                               │
│                         Bottom Tab Navigation                          │
├─────────────────────────────────────────────────────────────────────────┤
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐ │
│  │   Home   │  │  Routes  │  │ Partners │  │   Leads  │  │  Profile │ │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘  └──────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
```

### 3.2 Navigation Hierarchy

```
Field Sales Rep App
│
├── 🏠 Home
│   ├── Today's Overview
│   ├── Quick Actions
│   ├── Pending Tasks
│   ├── Performance Summary
│   └── Announcements
│
├── 🗺️ Routes & Visits
│   ├── Today's Route
│   │   ├── Planned stops
│   │   ├── Navigation
│   │   ├── Check-in/Check-out
│   │   └── Visit notes
│   ├── Route Planning
│   │   ├── Manual stop addition
│   │   ├── Route optimization
│   │   └── Recurring visits
│   ├── Visit History
│   │   ├── Past visits
│   │   ├── Customer profiles
│   │   └── Visit notes history
│   └── Map View
│       ├── Territory overview
│       ├── Partner locations
│       └── Lead pins
│
├── 🤝 Partners
│   ├── Partner Directory
│   │   ├── By partner type
│   │   ├── By territory
│   │   └── Search & filter
│   ├── Onboard New Partner
│   │   ├── Partner type selection
│   │   ├── Business information
│   │   ├── Document capture
│   │   ├── Photo capture
│   │   ├── Application review
│   │   └── Submission
│   ├── Pending Applications
│   │   ├── Draft applications
│   │   ├── Under review
│   │   └── Action required
│   └── Partner Performance
│       ├── Commission earned
│       ├── Order volume
│       └── Activity tracking
│
├── 📊 Leads & Orders
│   ├── Leads
│   │   ├── My leads
│   │   ├── Lead details
│   │   ├── Lead qualification
│   │   ├── Follow-up tasks
│   │   └── Create new lead
│   ├── Orders
│   │   ├── New order
│   │   │   ├── Product catalog
│   │   │   ├── Cart
│   │   │   │   ├── Customer selection
│   │   │   │   ├── Products
│   │   │   │   ├── Pricing
│   │   │   │   └── Checkout
│   │   ├── Order history
│   │   ├── Order tracking
│   │   └── Returns
│   ├── Customers
│   │   ├── Customer directory
│   │   ├── Customer profiles
│   │   ├── Visit history
│   │   └── Communication log
│   └── Products
│       ├── Catalog
│       ├── Search
│       ├── Favorites
│       └── Stock availability
│
├── 💼 More
│   ├── 📈 Performance
│   │   ├── Daily targets
│   │   ├── Monthly achievements
│   │   ├── Commission tracker
│   │   ├── Leaderboard
│   │   └── Reports
│   ├── 💰 Expenses
│   │   ├── New expense
│   │   ├── Expense history
│   │   ├── Pending reimbursements
│   │   └── Receipts
│   ├── 📱 Communications
│   │   ├── Messages
│   │   ├── Announcements
│   │   ├── Support
│   │   └── Notifications
│   ├── 📚 Resources
│   │   ├── Training materials
│   │   ├── Product guides
│   │   ├── Sales scripts
│   │   └── Policy documents
│   ├── ⚙️ Settings
│   │   ├── Profile settings
│   │   ├── App preferences
│   │   ├── Offline settings
│   │   └── Notifications
│   └── 🚪 Logout
│
└── 🔐 Auth Flow
    ├── Login
    ├── First-time setup
    └── Offline mode
```

---

## 4. UI FLOW DIAGRAMS

### 4.1 App Launch Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         APP LAUNCH FLOW                                │
└─────────────────────────────────────────────────────────────────────────┘

User taps app icon
      │
      ▼
┌─────────────────────────────────────────┐
│ Splash Screen                           │
│ • Gogidix logo                         │
│ • "Field Sales" tagline                 │
│ • Loading indicator                     │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Check Authentication                    │
│ • Is user logged in?                   │
│ • Is token valid?                      │
│ • Offline data available?              │
└─────────────────────────────────────────┘
      │
      ├─ Not Logged In ────────────────────────────────────────────────┐
      │                                                                   │
      ▼                                                                   │
┌─────────────────────────────────────────┐                                 │
│ Login Screen                            │                                 │
│ • Email / Phone                         │                                 │
│ • Password / PIN                        │                                 │
│ • Biometric (if enabled)                │                                 │
│ • "Login with Company SSO" (optional)    │                                 │
└─────────────────────────────────────────┘                                 │
      │                                                                   │
      ▼                                                                   │
┌─────────────────────────────────────────┐                                 │
│ Authenticate                           │                                 │
│ • Validate credentials                 │                                 │
│ • Fetch user profile                   │                                 │
│ • Load territory assignment             │                                 │
│ • Sync initial data                     │                                 │
└─────────────────────────────────────────┘                                 │
      │                                                                   │
      └───────────────────────────────────────────────────────────────────┘

      │ Logged In / Authenticated
      ▼
┌─────────────────────────────────────────┐
│ Load Home Screen                        │
│ • Today's overview                      │
│ • Route for today                       │
│ • Pending tasks                         │
│ • Performance snapshot                  │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Ready for Use                           │
│ • Bottom navigation active               │
│ • Location services enabled              │
│ • Background sync started               │
└─────────────────────────────────────────┘
```

### 4.2 Daily Startup Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         DAILY STARTUP FLOW                             │
└─────────────────────────────────────────────────────────────────────────┘

App opens (already authenticated)
      │
      ▼
┌─────────────────────────────────────────┐
│ Check Today's Route                     │
│ • Is route pre-planned?                 │
│ • Any changes from manager?             │
│ • New urgent stops added?               │
└─────────────────────────────────────────┘
      │
      ├─ Route Exists ──────────────────┐     ┌─ No Route ────────────────┐
      │                                  │     │                          │
      ▼                                  ▼     ▼                          │
┌───────────────────────┐    ┌───────────────────────┐   │
│ Load Today's Route    │    │ Plan Route           │   │
│ • Display stops        │    │ • Show suggestions   │   │
│ • Show sequence        │    │ • Allow manual add    │   │
│ • Start navigation     │    │ • Optimize route      │   │
└───────────────────────┘    └───────────────────────┘   │
      │                                  │                    │
      ▼                                  ▼                    │
┌─────────────────────────────────────────┐                               │
│ Show Today's Overview                   │                               │
│ • First stop highlighted                 │                               │
│ • Time to first stop                     │                               │
│ • Today's targets                        │                               │
│ • Quick actions available                │                               │
└─────────────────────────────────────────┘                               │
                                                                          │
                                                                          ▼
                                                          ┌─────────────────────────────────┐
                                                          │ Display Today's Plan             │
                                                          │ • Empty route state               │
                                                          │ • "Start planning your route"    │
                                                          │ • Quick add button                │
                                                          └─────────────────────────────────┘
```

### 4.3 Partner Onboarding Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                    PARTNER ONBOARDING FLOW                             │
└─────────────────────────────────────────────────────────────────────────┘

User taps: Onboard New Partner
      │
      ▼
┌─────────────────────────────────────────┐
│ Select Partner Type                     │
│ ┌─────────────────────────────────┐   │
│ │ • Courier Partner               │   │
│ │ • Haulage Partner               │   │
│ │ • Warehouse Partner             │   │
│ │ • E-commerce Vendor             │   │
│ │ • Logistics Provider            │   │
│ └─────────────────────────────────┘   │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Business Information                    │
│ • Business name                         │
│ • Registration number                   │
│ • Business type                         │
│ • Contact person                        │
│ • Phone number                          │
│ • Email address                         │
│ • Physical address                      │
│   [Use current location]                │
│   [Search on map]                       │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Services Offered                        │
│ • Select service types                   │
│ • Service coverage areas                 │
│ • Fleet/Infrastructure size              │
│ • Operational capacity                   │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Document Capture                         │
│ • Business registration                 │
│ • Tax certificate                        │
│ • Insurance certificate                 │
│ • ID documents                           │
│   [Camera] [Gallery] [Scan]              │
│   [Crop & Rotate]                        │
│   [Retake]                               │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Photo Capture                            │
│ • Business premises                      │
│ • Fleet/Vehicles                        │
│ • Warehouse/Facility                    │
│   [Camera] [Gallery]                     │
│   [Add multiple]                         │
│   [Geo-tag automatically]                │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Review & Submit                          │
│ • Summary of all information             │
│ • Document thumbnails                    │
│ • Photo gallery                          │
│ • Edit if needed                         │
│ • [Save as Draft] [Submit]               │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Submission Confirmation                  │
│ • Application submitted                  │
│ • Reference number                      │
│ • Expected processing time              │
│ • Track status in Partners → Pending     │
│ • [View Application] [Add Another]       │
└─────────────────────────────────────────┘
```

### 4.4 Visit Check-in/Check-out Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                    VISIT CHECK-IN/CHECK-OUT FLOW                        │
└─────────────────────────────────────────────────────────────────────────┘

Arriving at customer/partner location
      │
      ▼
┌─────────────────────────────────────────┐
│ Location Verification                    │
│ • GPS matches location?                  │
│ • Within geofence?                      │
│ • Tolerance: 100 meters                 │
└─────────────────────────────────────────┘
      │
      ├─ Verified ──────────────────────────────┐     ┌─ Not Verified ─────┐
      │                                        │     │                    │
      ▼                                        ▼     ▼                    │
┌─────────────────────────────────────────┐   ┌─────────────────────────────────┐
│ Check-in Screen                         │   │ Location Mismatch                │
│ • Customer/Partner name                 │   │ • Current location shown          │
│ • Address                               │   │ • Expected location shown         │
│ • Visit purpose                         │   │ • [Override] [Cancel]              │
│   [Dropdown: Sales call, Follow-up,     │   │ • Photo verification required      │
│    Partner review, Delivery, etc.]      │   └─────────────────────────────────┘
│ • [Check In]                            │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Check-in Confirmed                       │
│ • Timestamp captured                    │
│ • Location coordinates stored           │
│ • Timer started for visit duration       │
│ • [View Tasks] [Add Notes] [Start Order] │
└─────────────────────────────────────────┘
      │
      │ During visit...
      │
      ├─ Add Notes ────────────────────────────────────────────────────────┐
      │                                                                   │
      ▼                                                                   │
┌─────────────────────────────────────────┐                                 │
│ Visit Notes                             │                                 │
│ • Text notes                           │                                 │
│ • [Voice memo]                         │                                 │
│ • [Photo]                              │                                 │
│ • Tag for easy retrieval               │                                 │
│ • Save to visit log                    │                                 │
└─────────────────────────────────────────┘                                 │
                                                                          │
      ├─ Create Order ──────────────────────────────────────────────────┤
      │                                                                   │
      ▼                                                                   │
┌─────────────────────────────────────────┐                                 │
│ New Order                               │                                 │
│ • Select customer                      │                                 │
│ • Browse catalog                       │                                 │
│ • Add products to cart                 │                                 │
│ • Apply pricing/discounts              │                                 │
│ • Capture signature                    │                                 │
│ • Submit order                         │                                 │
└─────────────────────────────────────────┘                                 │
                                                                          │
      │ Visit complete...
      │
      ▼
┌─────────────────────────────────────────┐
│ Check-out Prompt                        │
│ "Ready to check out?"                   │
│ • Visit duration: 45 minutes            │
│ • [Add final notes]                     │
│ • [Check Out]                           │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Check-out Complete                       │
│ • Location captured (optional)           │
│ • Visit logged                          │
│ • Next stop highlighted                  │
│ • [Navigate to next] [End route]        │
└─────────────────────────────────────────┘
```

### 4.5 Lead Capture Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         LEAD CAPTURE FLOW                              │
└─────────────────────────────────────────────────────────────────────────┘

User taps: Create New Lead
      │
      ▼
┌─────────────────────────────────────────┐
│ Lead Source                             │
│ "Where did this lead come from?"         │
│ ┌─────────────────────────────────┐   │
│ │ • Cold Call                      │   │
│ │ • Field Visit                   │   │
│ │ • Referral                      │   │
│ │ • Event                         │   │
│ │ • Website Inquiry               │   │
│ │ • Partner Referral              │   │
│ │ • Other                         │   │
│ └─────────────────────────────────┘   │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Contact Information                     │
│ • Contact name *                        │
│ • Company name                          │
│ • Phone number *                        │
│ • Email address                         │
│ • [Copy from partner/customer]          │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Lead Details                             │
│ • Product/service interest *            │
│ • Estimated value                       │
│ • Purchase timeline                     │
│ • Decision maker                        │
│ • Competition                           │
│ • Notes                                 │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Location (Optional)                      │
│ • Address                               │
│ • [Use current GPS]                     │
│ • Territory assignment                   │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Follow-up Actions                        │
│ • Schedule follow-up                    │
│   [Date picker] [Time picker]           │
│ • Set reminder                          │
│ • Assign to self/team                   │
│ • Add tasks                              │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Save Lead                               │
│ • Lead score calculated                  │
│ • Territory assigned                     │
│ • Follow-up scheduled                   │
│ • [View Lead] [Create Another]           │
└─────────────────────────────────────────┘
```

---

## 5. FEATURE-SPECIFIC FLOWS

### 5.1 Route Management Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                       ROUTE MANAGEMENT FLOW                             │
└─────────────────────────────────────────────────────────────────────────┘

Open Routes Tab
      │
      ├─ Today's Route ────────────────────────────────────────────────┐
      │                                                                   │
      ▼                                                                   │
┌─────────────────────────────────────────┐                                 │
│ Today's Route Display                    │                                 │
│ • Route overview for today               │                                 │
│ • List of stops in sequence              │                                 │
│ • ┌─────────────────────────────────┐   │                                 │
│ │ Stop 1: 09:00                      │   │                                 │
│ │ Customer: ABC Logistics             │   │                                 │
│ │ Address: 123 Commerce St            │   │                                 │
│ │ Purpose: Partner Review             │   │                                 │
│ │ Status: Upcoming                   │   │                                 │
│ │ [Navigate] [Details] [Skip]         │   │                                 │
│ └─────────────────────────────────┘   │                                 │
│ • Progress indicator (1/8)              │                                 │
│ • [Start Navigation] [Optimize]         │                                 │
└─────────────────────────────────────────┘                                 │
                                                                          │
      ├─ Navigate ────────────────────────────────────────────────────────┤
      │                                                                   │
      ▼                                                                   │
┌─────────────────────────────────────────┐                                 │
│ Navigation Mode                         │                                 │
│ • Turn-by-turn navigation                │                                 │
│ • ETA to destination                     │                                 │
│ • [Recalculate] [Stop Nav]               │                                 │
│ • Overview map                          │                                 │
│ • Arrival notification                   │                                 │
└─────────────────────────────────────────┘                                 │
                                                                          │
      ├─ Optimize Route ──────────────────────────────────────────────────┤
      │                                                                   │
      ▼                                                                   │
┌─────────────────────────────────────────┐                                 │
│ Route Optimization                       │                                 │
│ • Analyzing current route                │                                 │
│ • Traffic conditions                     │                                 │
│ • Distance/time optimization             │                                 │
│ • New sequence proposed                  │                                 │
│ • [Apply] [Discard]                     │                                 │
└─────────────────────────────────────────┘                                 │
                                                                          │
      └───────────────────────────────────────────────────────────────────┘
```

### 5.2 Order Entry Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         ORDER ENTRY FLOW                                │
└─────────────────────────────────────────────────────────────────────────┘

Tap: New Order
      │
      ▼
┌─────────────────────────────────────────┐
│ Select Customer                         │
│ • Search customers                      │
│ • [Recent] [Partners] [Favorites]       │
│ • Or create new customer                │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Product Catalog                         │
│ • Categories                             │
│ • Search products                       │
│ • [Scan barcode]                        │
│ • Product details                       │
│   • Image, price, stock                │
│   • [Add to cart]                        │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Shopping Cart                           │
│ • Line items                             │
│ • Quantities                            │
│ • Pricing                               │
│ • Discounts (if authorized)              │
│ • Tax calculation                       │
│ • [Add more] [Checkout]                 │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Checkout                                │
│ • Order summary                         │
│ • Delivery date                         │
│ • Payment terms                         │
│ • Special instructions                   │
│ • [Create Order]                        │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Order Confirmation                      │
│ • Order number generated                 │
│ • Customer signature capture             │
│ • Email copy option                     │
│ • [View Order] [New Order]               │
└─────────────────────────────────────────┘
```

### 5.3 Expense Submission Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                       EXPENSE SUBMISSION FLOW                          │
└─────────────────────────────────────────────────────────────────────────┘

Tap: More → Expenses → New Expense
      │
      ▼
┌─────────────────────────────────────────┐
│ Expense Type                            │
│ ┌─────────────────────────────────┐   │
│ │ • Travel (Mileage/Fuel)           │   │
│ │ • Meals                           │   │
│ │ • Accommodation                   │   │
│ │ • Client Entertainment           │   │
│ │ • Supplies/Materials              │   │
│ │ • Communication                   │   │
│ │ • Other                           │   │
│ └─────────────────────────────────┘   │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Expense Details                          │
│ • Date *                                │
│ • Amount *                              │
│ • Currency                              │
│ • Description *                         │
│ • Related customer/partner              │
│ • Receipt upload                         │
│   [Camera] [Gallery]                    │
│   [Required if > ₦5,000]                │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Mileage Calculation (if Travel)          │
│ • Start location                         │
│ • End location                           │
│ • Distance auto-calculated               │
│ • Rate applied                           │
│ • Total amount                           │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Review & Submit                          │
│ • Expense summary                        │
│ • Policy check                           │
│ • Approval required?                     │
│ • [Save as Draft] [Submit]               │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Submission Confirmation                  │
│ • Expense submitted                      │
│ • Reference number                      │
│ • Track in Pending expenses              │
│ • [Add Another] [View Expenses]          │
└─────────────────────────────────────────┘
```

### 5.4 Performance Dashboard Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                       PERFORMANCE DASHBOARD FLOW                       │
└─────────────────────────────────────────────────────────────────────────┘

Tap: More → Performance
      │
      ▼
┌─────────────────────────────────────────┐
│ Performance Overview                    │
│ • Current period summary                 │
│ ┌─────────────────────────────────┐   │
│ │ 🎯 Target Achievement            │   │
│ │ ┌───────────────────────────┐   │   │
│ │ │ Revenue: 78%                │   │   │
│ │ │ │████████████████████      │   │   │
│ │ │ ₦39M / ₦50M                │   │   │
│ │ └───────────────────────────┘   │   │
│ │                                   │   │
│ │ 🤝 Partners Onboarded: 5        │   │
│ │ 📊 Leads Generated: 23          │   │
│ │ 💼 Orders Created: 18           │   │
│ └─────────────────────────────────┘   │
│ • [View Details] for each metric         │
└─────────────────────────────────────────┘
      │
      ├─ Detailed Metrics ──────────────────────────────────────────────┐
      │                                                                   │
      ▼                                                                   │
┌─────────────────────────────────────────┐                                 │
│ Detailed Metrics View                   │                                 │
│ ┌─────────────────────────────────┐   │                                 │
│ │ Revenue                           │   │                                 │
│ │ • Daily breakdown                 │   │                                 │
│ │ • By product category             │   │                                 │
│ │ • By customer/partner             │   │                                 │
│ │                                   │   │                                 │
│ │ Commission                        │   │                                 │
│ │ • Earned this month               │   │                                 │
│ │ • Pending                         │   │                                 │
│ │ • Payment history                 │   │                                 │
│ │                                   │   │                                 │
│ │ Leaderboard                        │   │
│ │ • Country ranking                 │   │                                 │
│ │ • Top performers                  │   │                                 │
│ └─────────────────────────────────┘   │                                 │
└─────────────────────────────────────────┘                                 │
                                                                          │
      └───────────────────────────────────────────────────────────────────┘
```

---

## 6. CROSS-DOMAIN INTEGRATION FLOWS

### 6.1 Partner Services Integration

```
┌─────────────────────────────────────────────────────────────────────────┐
│                  PARTNER SERVICES INTEGRATION                          │
└─────────────────────────────────────────────────────────────────────────┘

Field Sales Rep onboards a Courier Partner
      │
      ▼
┌─────────────────────────────────────────┐
│ Partner Type Selection                    │
│ User selects: Courier Partner            │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Connect to Courier Services Core         │
│ POST /api/v1/courier/partners/register    │
│ {                                        │
│   partnerType: "COURIER_SME",          │
│   businessName: "Express Deliveries",  │
│   // ... business details             │
│ }                                        │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Partner Data Stored                      │
│ • Partner profile created                │
│ • Territory assigned                     │
│ • Commission tier set                   │
│ • Notification sent to Country Sales    │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Connected Services                       │
│ Partner can now:                         │
│ • Receive orders through courier core    │
│ • Access courier partner dashboard        │
│ • Track deliveries                       │
│ • View earnings                          │
└─────────────────────────────────────────┘
```

### 6.2 Location Services Integration

```
┌─────────────────────────────────────────────────────────────────────────┐
│                  LOCATION SERVICES INTEGRATION                         │
└─────────────────────────────────────────────────────────────────────────┘

Field Sales Rep checks in at location
      │
      ▼
┌─────────────────────────────────────────┐
│ GPS Location Capture                     │
│ • Latitude, longitude captured           │
│ • Accuracy verified (< 100m)            │
│ • Timestamp recorded                    │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Geofence Verification                    │
│ • Check against customer/partner location│
│ • Within allowed radius?                │
│ • Flag if outside tolerance              │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Location Services API                    │
│ POST /api/v1/location/checkin            │
│ {                                        │
│   salesRepId: "SR-NGA-001",            │
│   location: {                          │
│     latitude: 6.5244,                 │
│     longitude: 3.3792,                │
│     accuracy: 15.5                     │
│   },                                   │
│   customerId: "CUST-1234",             │
│   visitType: "SALES_CALL"              │
│ }                                        │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Visit Logged                             │
│ • Check-in confirmed                    │
│ • Visit timer started                   │
│ • Location stored with visit record     │
└─────────────────────────────────────────┘
```

---

## 7. AUTHENTICATION & ONBOARDING

### 7.1 Authentication Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                       AUTHENTICATION FLOW                                │
└─────────────────────────────────────────────────────────────────────────┘

First time user
      │
      ▼
┌─────────────────────────────────────────┐
│ Login Screen                             │
│ • Email / Employee ID                   │
│ • Password                              │
│ • [Forgot Password?]                    │
│ • [Login with SSO] (if enabled)          │
└─────────────────────────────────────────┘
      │
      ▼
┌─────────────────────────────────────────┐
│ Authentication                          │
│ POST /api/v1/sales/auth/login             │
│ • Validate credentials                  │
│ • Fetch user profile                    │
│ • Get territory assignment              │
│ • Load permissions                      │
└─────────────────────────────────────────┘
      │
      ├─ New User (First Login) ─────────────────────────────────────────┐
      │                                                                   │
      ▼                                                                   │
┌─────────────────────────────────────────┐                                 │
│ First-Time Setup                        │                                 │
│ • Profile completion                    │                                 │
│ • Photo upload                          │                                 │
│ • Biometric setup (optional)            │                                 │
│ • Notification preferences              │                                 │
│ • Offline mode setup                    │                                 │
└─────────────────────────────────────────┘                                 │
      │                                                                   │
      ▼                                                                   │
│ Onboarding Complete ──────────────────────────────────────────────────────│
                                                                           │
      └─ Returning User ──────────────────────────────────────────────────┘
                                                                           │
                                                                           ▼
                                                            ┌─────────────────────────────────┐
                                                            │ Load Today's Overview            │
                                                            │ • Route for today                │
                                                            │ • Pending tasks                 │
                                                            │ • Quick actions                 │
                                                            └─────────────────────────────────┘
```

### 7.2 Offline Mode Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         OFFLINE MODE FLOW                                │
└─────────────────────────────────────────────────────────────────────────┘

Network unavailable
      │
      ▼
┌─────────────────────────────────────────┐
│ Detect Offline Mode                     │
│ • No network connection detected          │
│ • Switch to offline mode                │
│ • Show indicator: "Offline Mode"        │
└─────────────────────────────────────────┘
      │
      ├─ Has Cached Data ──────────────────────────────────────────────────┐
      │                                                                   │
      ▼                                                                   │
┌─────────────────────────────────────────┐                                 │
│ Offline Features Available                │                                 │
│ ✅ View today's route (cached)            │                                 │
│ ✅ View customer/partner data (cached)    │                                 │
│ ✅ Create leads (stored locally)          │                                 │
│ ✅ Create orders (stored locally)         │                                 │
│ ✅ Capture partner applications (local)   │                                 │
│ ✅ Check-in/check-out (GPS still works)    │                                 │
│ ❌ Real-time updates                     │                                 │
│ ❌ New data sync                          │                                 │
│ ❌ Map navigation (requires online)        │                                 │
└─────────────────────────────────────────┘                                 │
                                                                          │
      └───────────────────────────────────────────────────────────────────┘

Network restored
      │
      ▼
┌─────────────────────────────────────────┐
│ Sync Offline Data                        │
│ • Upload pending leads                   │
│ • Upload pending orders                  │
│ • Upload partner applications            │
│ • Upload check-ins                      │
│ • Fetch updates from server              │
│ • Sync: "23 items uploaded"              │
└─────────────────────────────────────────┘
```

---

## 8. ERROR HANDLING & STATES

### 8.1 Loading States

| Screen | Loading State |
|--------|---------------|
| Login | Spinner with "Authenticating..." |
| Routes | Skeleton stops list |
| Partners | Skeleton cards |
| Lead Form | Disabled fields while creating |
| Order Submit | Full-screen overlay with progress |

### 8.2 Error States

| Error Type | Display | User Action |
|------------|---------|-------------|
| Network Error | "No connection. Retry?" | [Retry] [Use Offline] |
| Auth Failed | "Invalid credentials" | [Try Again] [Forgot Password] |
| GPS Error | "Location unavailable. Enable GPS?" | [Settings] [Enter Manually] |
| Submit Failed | "Submission failed. Saved as draft." | [Retry] [View Drafts] |
| Sync Failed | "Sync failed. Will retry later." | [Force Sync] |

### 8.3 Empty States

| Screen | Empty State | Action |
|--------|-----------|--------|
| Today's Route | "No stops planned for today" | [Plan Route] [View Tasks] |
| Partners | "No partners in your territory yet" | [Onboard Partner] |
| Leads | "No leads yet. Start capturing!" | [Create Lead] |
| Orders | "No orders this month" | [Create Order] |
| Expenses | "No expenses submitted" | [Add Expense] |

---

**End of UI Flow Documentation v1.0**

**Next:** [02_Wireframes_Documentation.md](./02_Wireframes_Documentation.md) - Visual layout specifications

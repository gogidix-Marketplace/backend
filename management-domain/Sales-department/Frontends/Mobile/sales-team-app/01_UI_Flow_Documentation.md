# SALES TEAM PARTNERS APP - UI FLOW DOCUMENTATION

**Version:** 1.0
**Domain:** Management Domain
**Subdomain:** Sales-Departments
**Frontend:** sales-team-app (Mobile)
**Framework:** React Native + Expo + TypeScript
**State Management:** Redux Toolkit + RTK Query
**Last Updated:** 2025-02-16

---

## TABLE OF CONTENTS

1. [App Overview](#1-app-overview)
2. [User Personas & Roles](#2-user-personas--roles)
3. [Navigation Architecture](#3-navigation-architecture)
4. [UI Flow Diagrams](#4-ui-flow-diagrams)
5. [Cross-Domain Integration Flows](#5-cross-domain-integration-flows)
6. [Authentication & Onboarding](#6-authentication--onboarding)
7. [Error Handling & States](#7-error-handling--states)

---

## 1. APP OVERVIEW

### 1.1 Purpose

The Sales Team Partners App is a mobile application designed for Sales Partners to:
1. **Onboard new partners** across all shared-business-infrastructure domains
2. **Acquire customers** from business-domain marketplaces
3. **Manage AI-generated leads** based on regional sales allocation
4. **Track commissions** across both partner referrals and customer sales
5. **Communicate with prospects** through integrated messaging tools

### 1.2 App Scope

**Partner Onboarding (Shared-Business-Infrastructure):**
| Partner Type | Domain | Dashboard |
|--------------|--------|-----------|
| Courier Sales Partners | shared-courier-core | Courier-Partners-Dashboard |
| Haulage Sales Partners | shared-haulage-core | Haulage-Partners-Dashboard |
| Warehouse Sales Partners | shared-warehousing-core | Warehouse-Partners-Dashboard |
| E-commerce Sales Partners | shared-ecommerce-core | Ecommerce-Vendors-Dashboard |
| Air Freight Sales Partners | shared-air-freight-core | agents-dashboard |
| Location Agent Sales Partners | shared-courier-core | Location-Agents-Dashboard |
| Wholesale Sales Partners | shared-ecommerce-core | wholesalers-dashboard |
| Influencer Sales Partners | shared-ecommerce-core | Influencers-Dashboard |

**Customer Sales (Business-Domain):**
| Customer Type | Portal | Description |
|---------------|--------|-------------|
| Individual Marketplace Buyers | consumer-portal/app | E-commerce purchases |
| Individual Courier Users | consumer-portal/app | Package shipping |
| Individual Storage Customers | consumer-portal/app | Personal storage |
| Corporate Buyers | corporate-portal/app | B2B purchasing |
| Corporate Logistics | corporate-portal/app | Business logistics |

### 1.3 Key Features

```
┌─────────────────────────────────────────────────────────────────────┐
│                      SALES TEAM PARTNERS APP                        │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐    │
│  │   PARTNER       │  │   CUSTOMER      │  │   AI LEAD       │    │
│  │   ONBOARDING    │  │   SALES         │  │   MANAGEMENT    │    │
│  │                 │  │                 │  │                 │    │
│  │ • Registration  │  │ • Prospecting   │  │ • Lead Queue    │    │
│  │ • Verification  │  │ • Presentations │  │ • Scoring       │    │
│  │ • Training      │  │ • Closing       │  │ • Assignment    │    │
│  │ • Launch        │  │ • Onboarding    │  │ • Follow-ups    │    │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘    │
│                                                                      │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐    │
│  │   COMMISSION    │  │   COMMUNICATION │  │   TERRITORY     │    │
│  │   TRACKING      │  │   TOOLS         │  │   MANAGEMENT    │    │
│  │                 │  │                 │  │                 │    │
│  │ • Partner Refs  │  │ • In-App Chat   │  │ • Allocation    │    │
│  │ • Customer Sales│  │ • Email Templates│  │ • Boundaries   │    │
│  │ • Bonuses       │  │ • Call Logging  │  │ • Performance   │    │
│  │ • Payouts       │  │ • Meeting Sched │  │ • Leads/Month   │    │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘    │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### 1.4 Integration Architecture

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         INTEGRATION LAYER                              │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │                    FOUNDATION-DOMAIN (AI Services)              │   │
│  │  • lead-generation-ai-service                                   │   │
│  │  • ai-customer-segmentation-service                             │   │
│  │  • ai-user-profiling-service                                    │   │
│  └─────────────────────────────────────────────────────────────────┘   │
│                                    ↓                                    │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │              MANAGEMENT-DOMAIN (Sales-Departments)              │   │
│  │  • sales-web-dashboard (HQ) - Global oversight                  │   │
│  │  • country-sales-web-dashboard (Business-Domain) - Regional     │   │
│  │  • lead-management-service                                     │   │
│  │  • commission-calculator-service                                │   │
│  └─────────────────────────────────────────────────────────────────┘   │
│                                    ↓                                    │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │          SHARED-BUSINESS-INFRASTRUCTURE (Partner Dashboards)    │   │
│  │  • Courier-Partners-Dashboard                                   │   │
│  │  • Haulage-Partners-Dashboard                                   │   │
│  │  • Warehouse-Partners-Dashboard                                 │   │
│  │  • Ecommerce-Vendors-Dashboard                                  │   │
│  │  • agents-dashboard (Air/Ocean)                                 │   │
│  │  • wholesalers-dashboard                                        │   │
│  │  • Influencers-Dashboard                                        │   │
│  └─────────────────────────────────────────────────────────────────┘   │
│                                    ↓                                    │
│  ┌─────────────────────────────────────────────────────────────────┐   │
│  │              BUSINESS-DOMAIN (Customer Portals)                 │   │
│  │  • consumer-portal/app (Individual)                             │   │
│  │  • corporate-portal/app (Corporate)                             │   │
│  │  • individual-portal/app (Logistics)                            │   │
│  └─────────────────────────────────────────────────────────────────┘   │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 2. USER PERSONAS & ROLES

### 2.1 Sales Partner Representative

**Description:** Independent sales representative working on commission basis

**Profile:**
- Works from their location (remote/field)
- Assigned to specific region/territory
- Receives AI-generated leads from country sales allocation
- Sells both partner referrals and direct customer accounts

**Key Capabilities:**
```
┌─────────────────────────────────────────────────────────────┐
│              SALES PARTNER REPRESENTATIVE                    │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Partner Onboarding:                                        │
│  • Register new partners                                    │
│  • Upload partner documents                                 │
│  • Guide partner through setup                              │
│  • Track partner activation                                 │
│                                                              │
│  Customer Sales:                                            │
│  • Receive AI-generated leads                               │
│  • Prospect and qualify customers                           │
│  • Create and manage opportunities                          │
│  • Close deals and onboard customers                        │
│                                                              │
│  Commission:                                                │
│  • View partner referral bonuses                            │
│  • View customer sales commissions                          │
│  • Track payout schedule                                    │
│  • Access commission history                                │
│                                                              │
│  Territory:                                                 │
│  • View assigned territory                                  │
│  • Check lead allocation                                    │
│  • Report territory activity                                │
│  • Request territory expansion                              │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

**Access Scope:**
- Can ONLY access data within assigned territory
- Can ONLY onboard partners in approved categories
- Can ONLY view leads assigned to them
- Commission view is personal only

### 2.2 Regional Sales Partner Manager

**Description:** Manages a team of Sales Partners in a region

**Profile:**
- Oversees multiple Sales Partners
- Coordinated with Country Sales Director
- Reviews partner onboarding quality
- Approves high-value deals

**Key Capabilities:**
```
┌─────────────────────────────────────────────────────────────┐
│          REGIONAL SALES PARTNER MANAGER                      │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  Team Management:                                           │
│  • View all assigned partners                               │
│  • Monitor partner activity                                 │
│  • Review partner onboarding quality                        │
│  • Provide coaching and feedback                            │
│                                                              │
│  Lead Management:                                           │
│  • View regional lead pool                                  │
│  • Reassign leads between partners                          │
│  • Monitor lead conversion rates                            │
│  • Request additional lead allocation                       │
│                                                              │
│  Deal Oversight:                                            │
│  • View all deals in region                                 │
│  • Approve/discount requests                                │
│  • Assist with complex deals                                │
│  • Review closed deals                                      │
│                                                              │
│  Reporting:                                                 │
│  • Generate regional reports                                │
│  • View team performance                                    │
│  • Track regional quotas                                    │
│  • Commission oversight (view only)                         │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

**Access Scope:**
- Can view all data for assigned region
- Can reassign leads within region
- Cannot modify commission rates
- Cannot access other regions

### 2.3 Country Sales Director (View Only)

**Description:** Country-level sales leadership with oversight of Sales Partners

**Access Scope:**
- Read-only view of all Sales Partner activities in country
- Approves partner onboarding quotas
- Sets commission structures
- Reviews regional performance

---

## 3. NAVIGATION ARCHITECTURE

### 3.1 Bottom Tab Navigation (Primary)

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         SALES TEAM PARTNERS APP                         │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐       │
│  │   🏠    │  │   👥    │  │   💼    │  │   💰    │  │   👤    │       │
│  │  Home   │  │  Leads  │  │ Deals   │  | Commission │  Profile  │       │
│  └─────────┘  └─────────┘  └─────────┘  └─────────┘  └─────────┘       │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

### 3.2 Tab Contents

#### 🏠 Home Tab
```
Home/
├── Dashboard Overview
│   ├── Today's Summary
│   ├── Pending Actions
│   ├── Quick Actions
│   └── Performance Snapshot
├── My Territory
│   ├── Territory Map
│   ├── Lead Allocation
│   ├── Partner Count by Type
│   └── Customer Count by Type
└── Notifications Center
```

#### 👥 Leads Tab
```
Leads/
├── Lead Queue
│   ├── AI-Generated Leads
│   ├── Partner Referrals
│   ├── Customer Inquiries
│   └── Follow-up Reminders
├── Lead Details
│   ├── Lead Profile
│   ├── AI Score & Segmentation
│   ├── Suggested Actions
│   └── Communication History
└── Lead Actions
    ├── Contact Lead
    ├── Schedule Follow-up
    ├── Convert to Deal
    └── Disqualify
```

#### 💼 Deals Tab
```
Deals/
├── Pipeline View
│   ├── New Opportunities
│   ├── Qualified Leads
│   ├── Proposal Stage
│   ├── Negotiation Stage
│   └── Closing Stage
├── Deal Types
│   ├── Partner Onboarding Deals
│   │   ├── Courier
│   │   ├── Haulage
│   │   ├── Warehouse
│   │   ├── E-commerce
│   │   ├── Air Freight
│   │   ├── Location Agent
│   │   ├── Wholesale
│   │   └── Influencer
│   └── Customer Sales Deals
│       ├── Individual (Marketplace, Courier, Storage)
│       └── Corporate (B2B, Logistics)
└── Deal Actions
    ├── Update Stage
    ├── Add Activity
    ├── Request Approval
    └── Close Deal
```

#### 💰 Commission Tab
```
Commission/
├── Commission Overview
│   ├── Current Month Earnings
│   ├── Pending Commissions
│   ├── Commission Tiers
│   └── Payout Schedule
├── Commission Breakdown
│   ├── Partner Referral Bonuses
│   ├── Customer Sales Commissions
│   ├── Performance Bonuses
│   └── Special Incentives
└── Commission History
    ├── Monthly Summary
    ├── Transaction Details
    ├── Payout Status
    └── Tax Documents
```

#### 👤 Profile Tab
```
Profile/
├── My Profile
│   ├── Personal Information
│   ├── Sales Partner ID
│   ├── Territory Assignment
│   └── Commission Tier
├── Performance
│   ├── Sales Metrics
│   ├── Conversion Rates
│   ├── Leaderboard Position
│   └── Achievements
├── Resources
│   ├── Training Materials
│   ├── Product Catalogs
│   ├── Marketing Assets
│   └── Sales Playbooks
├── Support
│   ├── Help Center
│   ├── Contact Support
│   ├── FAQ
│   └── Feedback
└── Settings
    ├── Notifications
    ├── Privacy & Security
    ├── App Preferences
    └── Logout
```

### 3.3 Secondary Navigation (Drawer/Menu)

```
┌─────────────────────────────────────┐
│              MENU                   │
├─────────────────────────────────────┤
│ 📊 Reports                          │
│ 🎯 Territory Management             │
│ 📚 Training & Resources             │
│ 💬 Communication Center             │
│ 📅 Calendar & Tasks                 │
│ 🏢 Partner Directory                │
│ 📈 Analytics Dashboard              │
│ ⚙️ Settings                         │
│ ❓ Help & Support                   │
└─────────────────────────────────────┘
```

---

## 4. UI FLOW DIAGRAMS

### 4.1 App Entry Flow

```
┌─────────────┐
│   App Launch │
└──────┬──────┘
       │
       ▼
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Check      │────▶│  Not Logged │────▶│   Login     │
│  Auth State  │     │     In      │     │   Screen    │
└──────┬──────┘     └─────────────┘     └──────┬──────┘
       │                                        │
       │                                        ├─▶ Email/Password
       │                                        ├─▶ Phone OTP
       ▼                                        └─▶ Biometric (if enabled)
┌─────────────┐
│   Logged    │
│     In      │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│   Check      │─────▶ Onboarding Required?
│  Onboarding │         Yes: ──▶ Onboarding Flow
│   Status    │         No:  ──▶ Home Dashboard
└──────┬──────┘
       │
       ▼
┌─────────────┐
│   Check      │─────▶ Territory Assigned?
│  Territory   │         No:  ──▶ Contact Admin Screen
│  Assignment  │         Yes: ──▶ Load Territory Data
└──────┬──────┘
       │
       ▼
┌─────────────┐
│   Check      │─────▶ New Leads Available?
│  Lead Queue  │         Yes: ──▶ Show Lead Badge
│              │         No:  ──▶ Home Dashboard
└──────┬──────┘
       │
       ▼
┌─────────────┐
│   Home      │
│  Dashboard  │
└─────────────┘
```

### 4.2 Partner Onboarding Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        PARTNER ONBOARDING FLOW                          │
└─────────────────────────────────────────────────────────────────────────┘

Home → "+ New Partner" Button
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    SELECT PARTNER TYPE                               │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐                   │
│  │ Courier │ │ Haulage │ │Warehouse│ │E-commerce│                  │
│  └─────────┘ └─────────┘ └─────────┘ └─────────┘                   │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐                   │
│  │Air/Ocean│ │ Location│ │Wholesale│ │Influencer│                  │
│  └─────────┘ └─────────┘ └─────────┘ └─────────┘                   │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    PARTNER BASIC INFORMATION                         │
│  • Business Name                                                     │
│  • Contact Person Name                                               │
│  • Phone Number                                                      │
│  • Email Address                                                     │
│  • Business Address                                                  │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    PARTNER TYPE-SPECIFIC FIELDS                      │
│                                                                      │
│  [Courier/Haulage]              [Warehouse]                          │
│  • Fleet Size                   • Storage Capacity                  │
│  • Vehicle Types                • Facility Size                     │
│  • Service Areas                • Location Count                    │
│                                • Security Features                  │
│                                                                      │
│  [E-commerce/Wholesale]         [Air/Ocean]                          │
│  • Product Categories           • Agent License                     │
│  • Monthly Volume               • Port Associations                 │
│  • Warehouse Access             • Trade Routes                      │
│                                                                      │
│  [Location Agent]               [Influencer]                         │
│  • Location Type                • Social Media Platform             │
│  • Facility Size                • Follower Count                    │
│  • Operating Hours              • Engagement Rate                   │
│                                • Content Niche                      │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    DOCUMENT UPLOAD                                   │
│  • Business Registration Certificate                                 │
│  • Tax Registration                                                  │
│  • ID Document (Contact Person)                                      │
│  • Bank Account Details                                              │
│  • Type-Specific Certifications                                      │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    TERRITORY & LOCATION                              │
│  • Operating Area(s)                                                 │
│  • Service Coverage Radius                                           │
│  • Primary Location Coordinates (GPS)                                │
│  • Additional Locations                                              │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    REVIEW & SUBMIT                                   │
│  • Summary of All Information                                        │
│  • Commission Tier Preview                                           │
│  • Terms & Conditions                                                │
│  • Digital Signature                                                 │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    SUBMISSION CONFIRMATION                           │
│  • Application Reference Number                                     │
│  • Expected Processing Time                                          │
│  • Next Steps                                                        │
│  • Contact Support Option                                            │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
    Return to Dashboard
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    PARTNER TRACKING                                  │
│  My Partners → View Status:                                          │
│  • Pending Review                                                    │
│  • Under Verification                                                │
│  • Approved - Awaiting Setup                                         │
│  • Active                                                            │
│  • Rejected                                                          │
└─────────────────────────────────────────────────────────────────────┘
```

### 4.3 Customer Sales Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         CUSTOMER SALES FLOW                            │
└─────────────────────────────────────────────────────────────────────────┘

Home → "+ New Customer" OR Select from AI Lead
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    SELECT CUSTOMER TYPE                              │
│  ┌─────────────────┐              ┌─────────────────┐               │
│  │   INDIVIDUAL    │              │    CORPORATE    │               │
│  │                 │              │                 │               │
│  │ • Marketplace   │              │ • B2B Purchasing│               │
│  │ • Courier       │              │ • Corporate     │               │
│  │ • Storage       │              │   Logistics     │               │
│  │ • Logistics     │              │ • Fleet         │               │
│  └─────────────────┘              └─────────────────┘               │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    CUSTOMER INFORMATION                              │
│  • Customer Name                                                     │
│  • Phone Number                                                      │
│  • Email Address                                                     │
│  • Address/Location                                                  │
│  [Corporate Only]                                                    │
│  • Company Name                                                      │
│  • Industry                                                          │
│  • Company Size                                                      │
│  • Decision Maker Contact                                            │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    INTEREST AREA                                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐              │
│  │ Marketplace  │  │   Courier    │  │  Warehousing │              │
│  │  Shopping    │  │  Services    │  │    Storage   │              │
│  └──────────────┘  └──────────────┘  └──────────────┘              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐              │
│  │ Air Freight  │  │ Ocean Freight│  │   Haulage    │              │
│  └──────────────┘  └──────────────┘  └──────────────┘              │
│                                                                      │
│  [Multiple Selection Allowed]                                        │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    QUALIFICATION                                     │
│  • Budget Range                                                      │
│  • Timeline                                                          │
│  • Decision Authority                                                │
│  • Pain Points/Needs                                                 │
│  • Current Solution Provider (if any)                                │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    CREATE OPPORTUNITY                                │
│  • Opportunity Name                                                  │
│  • Estimated Value                                                   │
│  • Expected Close Date                                               │
│  • Probability (%)                                                   │
│  • Next Action                                                       │
│  • Assign to Pipeline                                                │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    OPPORTUNITY CREATED                               │
│  • Opportunity ID                                                    │
│  • Added to Pipeline                                                 │
│  • Follow-up Task Scheduled                                         │
│  • Notification Sent                                                 │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
    Return to Dashboard
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    OPPORTUNITY MANAGEMENT                           │
│  Deals → Select Opportunity →                                        │
│  • Update Stage                                                     │
│  • Log Activity                                                     │
│  • Schedule Follow-up                                               │
│  • Request Approval (if needed)                                      │
│  • Close Deal                                                        │
└─────────────────────────────────────────────────────────────────────┘
```

### 4.4 AI Lead Management Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        AI LEAD MANAGEMENT FLOW                         │
└─────────────────────────────────────────────────────────────────────────┘

Home → Leads Tab → AI-Generated Leads
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    AI LEAD QUEUE                                     │
│  ┌───────────────────────────────────────────────────────────────┐ │
│  │ LEAD CARDS WITH AI INSIGHTS:                                  │ │
│  │                                                               │ │
│  │ ┌─────────────────────────────────────────────────────────┐  │ │
│  │ │ 🎯 Score: 85/100          🔥 High Priority              │  │ │
│  │ │                                                           │ │
│  │ │ John Doe                                                 │  │
│  │ │ Potential: Warehouse Partner - Large Capacity            │  │ │
│  │ │ Location: North Industrial Zone, Lagos                  │  │ │
│  │ │                                                           │  │ │
│  │ │ 🤖 AI Insights:                                          │  │ │
│  │ │ • 90% probability to convert                             │  │ │
│  │ │ • Similar to your top 3 converters                       │  │ │
│  │ │ • Best contact time: 10AM - 12PM                         │  │ │
│  │ │ Suggested: Focus on security features                   │  │ │
│  │ └─────────────────────────────────────────────────────────┘  │ │
│  └───────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼ (Select Lead)
┌─────────────────────────────────────────────────────────────────────┐
│                    LEAD DETAILS SCREEN                               │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │ CONTACT INFORMATION                                          │   │
│  │ • Name, Phone, Email, Address                               │   │
│  │ • Business Name, Website, Social Media                      │   │
│  ├─────────────────────────────────────────────────────────────┤   │
│  │ AI ANALYSIS                                                  │   │
│  │ • Lead Score                                                │   │
│  │ • Customer Segment                                          │   │
│  │ • Predicted Value                                           │   │
│  │ • Recommended Approach                                      │   │
│  │ • Similar Converted Leads                                   │   │
│  ├─────────────────────────────────────────────────────────────┤   │
│  │ ACTIVITY HISTORY                                             │   │
│  │ • AI-generated source                                       │   │
│  │ • Previous interactions (if any)                            │   │
│  │ • Communication history                                     │   │
│  ├─────────────────────────────────────────────────────────────┤   │
│  │ SUGGESTED ACTIONS                                           │   │
│  │ • [1] Call lead today (high urgency)                        │   │
│  │ • [2] Send partner information pack                         │   │
│  │ • [3] Schedule facility visit                               │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    TAKE ACTION                                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐              │
│  │ 📞 Call Lead │  │ 💬 Send SMS  │  │ 📧 Send Email │              │
│  └──────────────┘  └──────────────┘  └──────────────┘              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐              │
│  │ 📅 Schedule  │  │ 💼 Convert   │  │ ❌ Decline   │              │
│  │    Meeting   │  │  to Deal     │  │   Lead       │              │
│  └──────────────┘  └──────────────┘  └──────────────┘              │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼ (After Action)
┌─────────────────────────────────────────────────────────────────────┐
│                    LOG ACTIVITY                                       │
│  • Action Type                                                       │
│  • Outcome                                                           │
│  • Next Follow-up Date                                               │
│  • Notes                                                             │
│  • Remind Me On                                                      │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    LEAD STATUS UPDATED                               │
│  • New → Contacted → Qualified → Proposal → Negotiation → Closed     │
└─────────────────────────────────────────────────────────────────────┘
```

### 4.5 Commission Tracking Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                      COMMISSION TRACKING FLOW                          │
└─────────────────────────────────────────────────────────────────────────┘

Home → Commission Tab
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    COMMISSION OVERVIEW                               │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │ THIS MONTH                                                  │   │
│  │                                                               │   │
│  │  Total Earnings:          ₦125,000                           │   │
│  │  ┌─────────────────────────────────────────────────────┐    │   │
│  │  │ Partner Referrals:     ₦45,000  (36%)               │    │   │
│  │  │ Customer Sales:        ₦80,000  (64%)               │    │   │
│  │  └─────────────────────────────────────────────────────┘    │   │
│  │                                                               │   │
│  │  Pending Commissions:    ₦35,000                            │   │
│  │  Commission Tier:         Gold (5%)                        │   │
│  │  Next Payout:            2025-03-01                         │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼ (Tap Breakdown)
┌─────────────────────────────────────────────────────────────────────┐
│                    COMMISSION BREAKDOWN                              │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │ PARTNER REFERRAL BONUSES                                    │   │
│  │  ┌────────────────────────────────────────────────────┐     │   │
│  │  │ Courier Partner - ABC Logistics             +₦5,000 │     │   │
│  │  │ Warehouse Partner - SecureStore Ltd         +₦15,000│     │   │
│  │  │ E-commerce Vendor - TechStore               +₦10,000│     │   │
│  │  │ Influencer - FashionNigeria                +₦8,000 │     │   │
│  │  │ Wholesale Partner - BulkTraders            +₦7,000 │     │   │
│  │  └────────────────────────────────────────────────────┘     │   │
│  │  Total Partner Referrals:                         ₦45,000 │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │ CUSTOMER SALES COMMISSIONS                                   │   │
│  │  ┌────────────────────────────────────────────────────┐     │   │
│  │  │ Corporate - TechCorp Ltd                   +₦35,000 │     │   │
│  │  │ Individual - Multiple retail sales           +₦25,000│     │   │
│  │  │ Logistics - FMCG Company                   +₦20,000 │     │   │
│  │  └────────────────────────────────────────────────────┘     │   │
│  │  Total Customer Sales:                           ₦80,000 │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │ PERFORMANCE BONUSES                                          │   │
│  │  • Tier Achievement Bonus (Gold)                  +₦0      │   │
│  │  • Monthly Target Achievement (85%)              +₦0      │   │
│  │  • New Partner Record Bonus                      +₦0      │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼ (Tap Transaction)
┌─────────────────────────────────────────────────────────────────────┐
│                    TRANSACTION DETAILS                               │
│  Transaction ID: COM-2025-001456                                     │
│  Type: Partner Referral Bonus                                       │
│  Partner: Courier Partner - ABC Logistics                           │
│  Date: 2025-02-10                                                    │
│  Amount: ₦5,000                                                      │
│  Status: ✅ Paid                                                    │
│  Payout Date: 2025-03-01                                            │
│                                                                      │
│  Commission Calculation:                                             │
│  Partner Registration Fee: ₦50,000                                   │
│  Referral Bonus Rate: 10%                                           │
│  Commission: ₦5,000                                                 │
│                                                                      │
│  [View Partner Details] [Download Receipt]                          │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 5. CROSS-DOMAIN INTEGRATION FLOWS

### 5.1 Partner Onboarding Integration

```
┌─────────────────────────────────────────────────────────────────────────┐
│              PARTNER ONBOARDING - CROSS-DOMAIN FLOW                     │
└─────────────────────────────────────────────────────────────────────────┘

Sales Team Partners App
        │
        │ 1. Submit Partner Application
        │    - Partner Type Selection
        │    - Basic Information
        │    - Documents
        ▼
┌─────────────────────────────────────────────────────────────────────┐
│           MANAGEMENT-DOMAIN (Sales-Departments)                      │
│                                                                       │
│  sales-web-dashboard (HQ) receives application                       │
│  • Assigns to Country Sales Director                                  │
│  • Routes to appropriate regional queue                               │
│  • Creates partner onboarding task                                    │
└─────────────────────────────────────────────────────────────────────┘
        │
        │ 2. Regional Review
        ▼
┌─────────────────────────────────────────────────────────────────────┐
│           BUSINESS-DOMAIN (Country-Sales-Dashboard)                  │
│                                                                       │
│  country-sales-web-dashboard receives for review                     │
│  • Country Sales Director reviews application                        │
│  • Verifies territory alignment                                      │
│  • Approves/rejects partner onboarding                               │
└─────────────────────────────────────────────────────────────────────┘
        │
        │ 3. Approved
        ▼
┌─────────────────────────────────────────────────────────────────────┐
│           SHARED-BUSINESS-INFRASTRUCTURE                             │
│                                                                       │
│  Routes to appropriate partner dashboard:                             │
│                                                                       │
│  [Courier] → Courier-Partners-Dashboard                              │
│  [Haulage] → Haulage-Partners-Dashboard                              │
│  [Warehouse] → Warehouse-Partners-Dashboard                          │
│  [E-commerce] → Ecommerce-Vendors-Dashboard                          │
│  [Air/Ocean] → agents-dashboard                                      │
│  [Location Agent] → Location-Agents-Dashboard                        │
│  [Wholesale] → wholesalers-dashboard                                 │
│  [Influencer] → Influencers-Dashboard                                │
│                                                                       │
│  • Creates partner account                                           │
│  • Sends welcome email + onboarding instructions                     │
│  • Sets up commission tracking for Sales Partner                     │
└─────────────────────────────────────────────────────────────────────┘
        │
        │ 4. Partner Account Created
        ▼
┌─────────────────────────────────────────────────────────────────────┐
│           SALES TEAM PARTNERS APP (Notification)                     │
│                                                                       │
│  • Partner activated notification                                    │
│  • Commission tracking begins                                        │
│  • Partner added to "My Partners" list                               │
│  • Referral bonus eligibility starts                                 │
└─────────────────────────────────────────────────────────────────────┘
```

### 5.2 Customer Sales Integration

```
┌─────────────────────────────────────────────────────────────────────────┐
│               CUSTOMER SALES - CROSS-DOMAIN FLOW                        │
└─────────────────────────────────────────────────────────────────────────┘

Sales Team Partners App
        │
        │ 1. Customer Opportunity Created
        ▼
┌─────────────────────────────────────────────────────────────────────┐
│           MANAGEMENT-DOMAIN (Sales-Departments)                      │
│                                                                       │
│  lead-management-service records opportunity                         │
│  • Assigns to Sales Partner                                         │
│  • Links to territory                                               │
│  • Tracks in pipeline                                               │
└─────────────────────────────────────────────────────────────────────┘
        │
        │ 2. Lead Scoring & Segmentation
        ▼
┌─────────────────────────────────────────────────────────────────────┐
│           FOUNDATION-DOMAIN (AI Services)                            │
│                                                                       │
│  ai-customer-segmentation-service analyzes customer                  │
│  • Assigns customer segment                                         │
│  • Calculates propensity scores                                     │
│  • Suggests best approach                                           │
│  • Returns insights to Sales Team App                                │
└─────────────────────────────────────────────────────────────────────┘
        │
        │ 3. Sales Process (Meeting, Proposal, Negotiation)
        ▼
┌─────────────────────────────────────────────────────────────────────┐
│           SALES TEAM PARTNERS APP                                    │
│                                                                       │
│  • Log activities                                                   │
│  • Update deal stage                                                │
│  • Request approvals                                                │
│  • Generate proposals                                               │
└─────────────────────────────────────────────────────────────────────┘
        │
        │ 4. Deal Won
        ▼
┌─────────────────────────────────────────────────────────────────────┐
│           BUSINESS-DOMAIN (Customer Portals)                         │
│                                                                       │
│  Routes to appropriate portal:                                       │
│                                                                       │
│  [Individual] → consumer-portal/app                                  │
│  [Corporate] → corporate-portal/app                                  │
│                                                                       │
│  • Creates customer account                                         │
│  • Sends onboarding email                                           │
│  • Sets commission tracking                                         │
└─────────────────────────────────────────────────────────────────────┘
        │
        │ 5. Commission Calculated
        ▼
┌─────────────────────────────────────────────────────────────────────┐
│           MANAGEMENT-DOMAIN (commission-calculator-service)          │
│                                                                       │
│  • Calculates commission based on deal value                        │
│  • Applies tier rates                                               │
│  • Records commission for Sales Partner                             │
│  • Schedules payout                                                 │
└─────────────────────────────────────────────────────────────────────┘
```

### 5.3 AI Lead Assignment Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│              AI LEAD ASSIGNMENT - CROSS-DOMAIN FLOW                    │
└─────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│           FOUNDATION-DOMAIN (lead-generation-ai-service)             │
│                                                                       │
│  AI generates leads based on:                                        │
│  • Regional sales allocation (from Country-Sales-Dashboard)         │
│  • Market analysis                                                   │
│  • Partner opportunity indicators                                   │
│  • Customer potential indicators                                    │
└─────────────────────────────────────────────────────────────────────┘
        │
        │ 1. New Leads Generated
        ▼
┌─────────────────────────────────────────────────────────────────────┐
│           MANAGEMENT-DOMAIN (Sales-Departments)                      │
│                                                                       │
│  lead-management-service receives AI leads                           │
│  • Segment by territory                                              │
│  • Score by probability                                              │
│  • Assign to Sales Partners based on:                                │
│    - Territory assignment                                            │
│    - Current workload                                                │
│    - Performance tier                                               │
│    - Partner type specialization                                     │
└─────────────────────────────────────────────────────────────────────┘
        │
        │ 2. Leads Assigned to Sales Partners
        ▼
┌─────────────────────────────────────────────────────────────────────┐
│           BUSINESS-DOMAIN (Country-Sales-Dashboard)                  │
│                                                                       │
│  country-sales-web-dashboard monitors:                               │
│  • Lead distribution by region                                       │
│  • Conversion rates by Sales Partner                                 │
│  • Territory performance                                             │
│  • Lead quality feedback to AI                                       │
└─────────────────────────────────────────────────────────────────────┘
        │
        │ 3. Push Notification
        ▼
┌─────────────────────────────────────────────────────────────────────┐
│           SALES TEAM PARTNERS APP                                    │
│                                                                       │
│  • New lead notification                                             │
│  • Lead appears in AI Leads tab                                     │
│  • AI insights and recommendations displayed                         │
│  • Suggested actions provided                                        │
│                                                                       │
│  Sales Partner:                                                      │
│  • Reviews AI lead                                                   │
│  • Takes action (call, email, visit)                                 │
│  • Logs activity                                                     │
│  • Converts to deal or disqualifies                                 │
└─────────────────────────────────────────────────────────────────────┘
        │
        │ 4. Activity Feedback
        ▼
┌─────────────────────────────────────────────────────────────────────┐
│           FOUNDATION-DOMAIN (AI Services)                            │
│                                                                       │
│  AI learns from outcomes:                                            │
│  • Which leads converted                                             │
│  • Best approaches for different segments                            │
│  • Optimal contact times                                             │
│  • Improves future lead scoring                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 6. AUTHENTICATION & ONBOARDING

### 6.1 Authentication Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                      AUTHENTICATION FLOW                               │
└─────────────────────────────────────────────────────────────────────────┘

App Launch
    │
    ▼
┌─────────────────┐
│   Splash Screen │
│   (3 seconds)   │
└────────┬────────┘
         │
         ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    CHECK AUTHENTICATION STATE                        │
│                                                                      │
│  Check Secure Storage for:                                           │
│  • Auth token                                                        │
│  • User ID                                                           │
│  • Territory assignment                                              │
└─────────────────────────────────────────────────────────────────────┘
         │
    ┌────┴────┐
    │         │
    ▼         ▼
No Token    Token Found
    │         │
    ▼         ▼
┌─────────┐ ┌─────────────────────────────────────────────────────────┐
│  Login  │ │                    VALIDATE TOKEN                       │
│  Screen │ │                                                         │
└────┬────┘ │ • Check token expiration                               │
     │      │ • Verify with backend                                   │
     │      └─────────────────────────────────────────────────────────┘
     │                    │
     │            ┌───────┴────────┐
     │            │                │
     │        Valid            Invalid/Expired
     │            │                │
     │            ▼                ▼
     │      ┌─────────┐      ┌─────────┐
     │      │  Home   │      │  Login  │
     │      │Dashboard│      │  Screen │
     │      └─────────┘      └─────────┘
     │
     ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    LOGIN OPTIONS                                     │
│                                                                      │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐  │
│  │  Email/Password  │  │  Phone Number   │  │  Biometric       │  │
│  │                  │  │  (OTP)          │  │  (If enrolled)   │  │
│  └──────────────────┘  └──────────────────┘  └──────────────────┘  │
│                                                                      │
│  First Time? → Register as Sales Partner                             │
└─────────────────────────────────────────────────────────────────────┘
```

### 6.2 Sales Partner Registration Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                   SALES PARTNER REGISTRATION                           │
└─────────────────────────────────────────────────────────────────────────┘

Login Screen → "Register as Sales Partner"
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    STEP 1: PERSONAL INFORMATION                       │
│  • Full Name                                                         │
│  • Phone Number                                                      │
│  • Email Address                                                     │
│  • Date of Birth                                                     │
│  • National ID Number                                                │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    STEP 2: VERIFICATION                               │
│  • Phone OTP Verification                                            │
│  • Email Verification Link                                           │
│  • ID Document Upload                                                │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    STEP 3: SALES EXPERIENCE                           │
│  • Years of Sales Experience                                         │
│  • Industries Sold In                                                │
│  • Average Monthly Sales Volume                                      │
│  • Current/Previous Employment                                       │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    STEP 4: PREFERENCE SELECTION                       │
│  Preferred Partner Types (Select up to 3):                           │
│  □ Courier Partners                                                  │
│  □ Haulage Partners                                                  │
│  □ Warehouse Partners                                                │
│  □ E-commerce Vendors                                                │
│  □ Air/Ocean Freight Agents                                          │
│  □ Location Agents                                                   │
│  □ Wholesale Partners                                                │
│  □ Influencers                                                       │
│                                                                      │
│  Preferred Customer Types:                                           │
│  □ Individual Consumers                                              │
│  □ Corporate Customers                                              │
│                                                                      │
│  Preferred Territory:                                                │
│  □ North Region                                                      │
│  □ South Region                                                      │
│  □ East Region                                                       │
│  □ West Region                                                       │
│  □ Any (Flexible)                                                    │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    STEP 5: BANK INFORMATION                          │
│  • Bank Name                                                         │
│  • Account Number                                                    │
│  • Account Name                                                      │
│  • BVN (for verification)                                           │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    STEP 6: AGREEMENTS                                 │
│  □ I agree to the Sales Partner Terms & Conditions                  │
│  □ I agree to the Commission Structure                               │
│  □ I agree to the Code of Conduct                                    │
│  □ I consent to background verification                              │
│  • Digital Signature                                                │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    SUBMISSION                                         │
│  Application submitted to:                                           │
│  • Management-Domain (Sales-Departments)                             │
│  • Country-Sales-Dashboard (for regional review)                     │
│                                                                      │
│  Expected Response Time: 3-5 business days                           │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    APPLICATION TRACKING                              │
│  Status: Pending Review → → Under Verification → Approved/Rejected   │
│                                                                      │
│  Notifications sent at each stage                                    │
└─────────────────────────────────────────────────────────────────────┘
```

### 6.3 First-Time Onboarding Flow

```
┌─────────────────────────────────────────────────────────────────────────┐
│                  FIRST-TIME ONBOARDING (After Approval)                │
└─────────────────────────────────────────────────────────────────────────┘

First Login After Approval
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    WELCOME SCREEN                                    │
│  Welcome, [Name]!                                                   │
│  You're approved as a Sales Partner                                 │
│                                                                      │
│  Your Details:                                                       │
│  • Partner ID: SP-2025-XXXX                                          │
│  • Territory: [Assigned Territory]                                  │
│  • Commission Tier: Bronze                                          │
│                                                                      │
│  Let's get you started →                                            │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    TERRITORY OVERVIEW                                │
│  • View your assigned territory on map                              │
│  • See allocated lead count                                         │
│  • View sales targets                                               │
│  • Understand commission structure                                  │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    QUICK TRAINING                                    │
│  □ Partner Onboarding Training (5 min)                              │
│  □ Customer Sales Training (5 min)                                  │
│  □ Commission Understanding (3 min)                                 │
│  □ App Navigation Guide (3 min)                                     │
│                                                                      │
│  [Watch Now] [Skip for Later]                                       │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    DOWNLOAD RESOURCES                                │
│  • Sales Partner Handbook (PDF)                                     │
│  • Partner Type Brochures (PDF)                                     │
│  • Commission Structure Card (PDF)                                  │
│  • Business Card Template                                           │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    FIRST ACTIONS                                     │
│  Here's what you can do now:                                        │
│                                                                      │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐ │
│  │  View AI Leads   │  │  Add Partner     │  │  Add Customer    │ │
│  │  [X leads wait.] │  │  Start selling!  │  │  Start selling!  │ │
│  └──────────────────┘  └──────────────────┘  └──────────────────┘ │
└─────────────────────────────────────────────────────────────────────┘
       │
       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    READY TO START                                    │
│  All set! You can now:                                              │
│  • View and contact your AI-assigned leads                          │
│  • Register new partners                                            │
│  • Add customer opportunities                                       │
│  • Track your earnings                                              │
│                                                                      │
│  Need help? Tap the ? icon anytime                                  │
│                                                                      │
│  [Go to Dashboard]                                                  │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 7. ERROR HANDLING & STATES

### 7.1 Loading States

```
┌─────────────────────────────────────────────────────────────────────┐
│                         LOADING STATES                              │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  INITIAL LOAD                                                       │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                                                               │   │
│  │                    [GOGIDIX LOGO]                            │   │
│  │                                                               │   │
│  │                   Loading your data...                       │   │
│  │                                                               │   │
│  │                     ● ● ● ● ●                                │   │
│  │                                                               │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  PULL TO REFRESH                                                    │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  ↓ Pull to refresh...                                       │   │
│  │  ↻ Refreshing...                                           │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  INLINE LOADING (Button)                                            │
│  ┌────────────────────────┐                                         │
│  │  [Submit] → [Submit ●] │                                        │
│  └────────────────────────┘                                         │
│                                                                      │
│  FULLSCREEN OVERLAY                                                 │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  [Dimmed Background]                                        │   │
│  │  ┌─────────────────┐                                         │   │
│  │  │   Processing...  │                                         │   │
│  │  │    ◵ ◵ ◵        │                                         │   │
│  │  └─────────────────┘                                         │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### 7.2 Error States

```
┌─────────────────────────────────────────────────────────────────────┐
│                          ERROR STATES                               │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  NETWORK ERROR                                                       │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                                                               │   │
│  │              ⚠️  CONNECTION ERROR                            │   │
│  │                                                               │   │
│  │         Unable to connect to the server.                     │   │
│  │         Please check your internet connection.               │   │
│  │                                                               │   │
│  │              [Retry]           [Go Offline]                   │   │
│  │                                                               │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  SERVER ERROR (500)                                                  │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                                                               │   │
│  │              🔄 SERVER ERROR                                │   │
│  │                                                               │   │
│  │         Something went wrong on our end.                     │   │
│  │         Our team has been notified.                          │   │
│  │                                                               │   │
│  │              [Retry]           [Contact Support]             │   │
│  │                                                               │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  NOT FOUND (404)                                                    │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                                                               │   │
│  │              🔍 NOT FOUND                                   │   │
│  │                                                               │   │
│  │         The requested resource was not found.                │   │
│  │         It may have been moved or deleted.                   │   │
│  │                                                               │   │
│  │              [Go Back]           [Go Home]                    │   │
│  │                                                               │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  VALIDATION ERROR                                                   │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │  ┌─────────────────────────────────────────────────────┐     │   │
│  │  │ ⚠️ Phone number is required                          │     │   │
│  │  └─────────────────────────────────────────────────────┘     │   │
│  │  Phone: [_________________]                                 │   │
│  │                                    ⚠️ Please enter a valid │   │
│  │                                    phone number            │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  AUTHENTICATION ERROR                                               │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                                                               │   │
│  │              🔐 SESSION EXPIRED                              │   │
│  │                                                               │   │
│  │         Your session has expired. Please login again.        │   │
│  │                                                               │   │
│  │                       [Login]                                 │   │
│  │                                                               │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  PERMISSION ERROR                                                   │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                                                               │   │
│  │              🚫 ACCESS DENIED                                │   │
│  │                                                               │   │
│  │         You don't have permission to access this.            │   │
│  │         Contact your administrator if you believe this       │   │
│  │         is an error.                                          │   │
│  │                                                               │   │
│  │                       [Go Back]                               │   │
│  │                                                               │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### 7.3 Empty States

```
┌─────────────────────────────────────────────────────────────────────┐
│                          EMPTY STATES                               │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  NO LEADS                                                           │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                                                               │   │
│  │              📭 NO LEADS YET                                 │   │
│  │                                                               │   │
│  │         You don't have any assigned leads right now.         │   │
│  │         New leads will appear here based on your             │   │
│  │         territory allocation.                                │   │
│  │                                                               │   │
│  │         In the meantime, you can:                            │   │
│  │         • Add a new partner                                  │   │
│  │         • Add a new customer                                 │   │
│  │                                                               │   │
│  │              [+ Add Partner]    [+ Add Customer]             │   │
│  │                                                               │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  NO DEALS                                                           │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                                                               │   │
│  │              💼 NO ACTIVE DEALS                              │   │
│  │                                                               │   │
│  │         Your deal pipeline is empty. Start adding            │   │
│  │         opportunities from your leads or create new          │   │
│  │         customer deals.                                      │   │
│  │                                                               │   │
│  │              [View Leads]       [+ New Deal]                 │   │
│  │                                                               │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  NO COMMISSION YET                                                  │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                                                               │   │
│  │              💰 NO COMMISSIONS YET                           │   │
│  │                                                               │   │
│  │         You haven't earned any commissions yet.              │   │
│  │         Close your first deal to start earning!              │   │
│  │                                                               │   │
│  │         Commission Structure:                                │   │
│  │         • Partner Referrals: 5-15% bonus                     │   │
│  │         • Customer Sales: 3-10% commission                   │   │
│  │                                                               │   │
│  │              [View Opportunities]                             │   │
│  │                                                               │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  NO PARTNERS                                                        │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                                                               │   │
│  │              🤝 NO PARTNERS YET                              │   │
│  │                                                               │   │
│  │         You haven't onboarded any partners yet.             │   │
│  │         Partners are a great source of recurring            │   │
│  │         commission through their platform activities.       │   │
│  │                                                               │   │
│  │              [+ Add First Partner]                           │   │
│  │                                                               │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### 7.4 Success States

```
┌─────────────────────────────────────────────────────────────────────┐
│                         SUCCESS STATES                              │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│  PARTNER ADDED                                                       │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                                                               │   │
│  │              ✅ PARTNER ADDED SUCCESSFULLY                   │   │
│  │                                                               │   │
│  │         ABC Logistics has been submitted for review.         │   │
│  │         Reference: PARTNER-2025-001234                       │   │
│  │                                                               │   │
│  │         Estimated approval time: 3-5 business days           │   │
│  │                                                               │   │
│  │              [View Partner]    [Add Another]                 │   │
│  │                                                               │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  DEAL CLOSED                                                        │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                                                               │   │
│  │              🎉 DEAL CLOSED!                                 │   │
│  │                                                               │   │
│  │         Congratulations! You've closed a deal with:          │   │
│  │         TechCorp Ltd                                         │   │
│  │         Deal Value: ₦1,500,000                               │   │
│  │         Your Commission: ₦75,000                             │   │
│  │                                                               │   │
│  │              [View Deal]       [Share Success]               │   │
│  │                                                               │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
│  COMMISSION PAID                                                     │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                                                               │   │
│  │              💵 COMMISSION PAID                              │   │
│  │                                                               │   │
│  │         Your commission for February 2025 has been           │   │
│  │         deposited to your account.                           │   │
│  │         Amount: ₦125,000                                     │   │
│  │         Bank: GTBank ******1234                              │   │
│  │                                                               │   │
│  │              [View Details]    [Download Receipt]            │   │
│  │                                                               │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

---

**END OF UI FLOW DOCUMENTATION**

**Next:** 02_Wireframes_Documentation.md

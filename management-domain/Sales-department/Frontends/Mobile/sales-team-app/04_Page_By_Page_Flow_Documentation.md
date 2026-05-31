# SALES TEAM PARTNERS APP - PAGE BY PAGE FLOW DOCUMENTATION

**Version:** 1.0
**Domain:** Management Domain
**Subdomain:** Sales-Departments
**Frontend:** sales-team-app (Mobile)
**Last Updated:** 2025-02-16

---

## TABLE OF CONTENTS

1. [Navigation Tree](#1-navigation-tree)
2. [Authentication Pages](#2-authentication-pages)
3. [Onboarding Pages](#3-onboarding-pages)
4. [Home & Dashboard Pages](#4-home--dashboard-pages)
5. [Leads Management Pages](#5-leads-management-pages)
6. [Partner Onboarding Pages](#6-partner-onboarding-pages)
7. [Customer Sales Pages](#7-customer-sales-pages)
8. [Commission Pages](#8-commission-pages)
9. [Profile & Settings Pages](#9-profile--settings-pages)
10. [Shared Components](#10-shared-components)

---

## 1. NAVIGATION TREE

```
sales-team-app/
│
├── AUTHENTICATION FLOW
│   ├── Splash Screen
│   ├── Login Screen
│   ├── Registration Screen (6 steps)
│   └── Forgot Password Screen
│
├── BOTTOM TAB NAVIGATION
│   ├── 🏠 Home Tab
│   │   ├── Dashboard Overview
│   │   ├── My Territory
│   │   ├── Quick Actions
│   │   └── Notifications Center
│   │
│   ├── 👥 Leads Tab
│   │   ├── AI-Generated Leads
│   │   ├── Partner Referrals
│   │   ├── Customer Inquiries
│   │   ├── Follow-up Reminders
│   │   └── Lead Detail [id]
│   │
│   ├── 💼 Deals Tab
│   │   ├── Pipeline Overview
│   │   ├── Partner Onboarding Deals
│   │   ├── Customer Sales Deals
│   │   └── Deal Detail [id]
│   │
│   ├── 💰 Commission Tab
│   │   ├── Commission Overview
│   │   ├── Commission Breakdown
│   │   ├── Transaction Detail [id]
│   │   ├── Commission History
│   │   └── Payout Schedule
│   │
│   └── 👤 Profile Tab
│       ├── My Profile
│       ├── Performance Stats
│       ├── Resources
│       ├── Support
│       └── Settings
│
├── DRAWER NAVIGATION
│   ├── Reports
│   ├── Territory Management
│   ├── Training & Resources
│   ├── Communication Center
│   ├── Calendar & Tasks
│   ├── Partner Directory
│   ├── Analytics Dashboard
│   └── Help & Support
│
└── MODALS / BOTTOM SHEETS
    ├── Action Bottom Sheet (Lead actions)
    ├── Filter Bottom Sheet
    ├── Sort Bottom Sheet
    ├── Share Bottom Sheet
    └── Confirmation Modals
```

---

## 2. AUTHENTICATION PAGES

### 2.1 Splash Screen

| Property | Value |
|----------|-------|
| **Route** | `/splash` |
| **Type** | Full Screen |
| **Duration** | 3 seconds |
| **Next** | Auto-redirect to Login or Dashboard |

**Layout:**
```
┌─────────────────────────────────────────────────────────────────────────┐
│                                                                          │
│                          ┌─────────────────┐                             │
│                          │    ╲    ╱       │                             │
│                          │     ╲  ╱        │                             │
│                          │      ╳         │  Logo + Brand              │
│                          │     ╱  ╲        │                             │
│                          │    ╱    ╲       │                             │
│                          └─────────────────┘                             │
│                                                                          │
│                        Sales Team Partners                               │
│                                                                          │
│                          ● ● ● ● ●                                      │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

**Components:**
- `SplashLogo` - Centered app logo
- `LoadingIndicator` - Animated dots
- Auto-redirect logic in `useEffect`

---

### 2.2 Login Screen

| Property | Value |
|----------|-------|
| **Route** | `/login` |
| **Type** | Full Screen |
| **Auth Required** | No |

**State:**
```typescript
interface LoginState {
  emailOrPhone: string;
  password: string;
  showPassword: boolean;
  isLoading: boolean;
  error?: string;
}
```

**Form Fields:**
| Field | Type | Required | Validation |
|-------|------|----------|------------|
| emailOrPhone | text | Yes | Email or phone format |
| password | password | Yes | Min 6 chars |

**Actions:**
- `signIn()` - Submit credentials
- `navigateToRegister()` - Go to registration
- `forgotPassword()` - Reset password flow
- `socialLogin(provider)` - Google/Apple OAuth

---

### 2.3 Registration Screen (Stepper)

| Property | Value |
|----------|-------|
| **Route** | `/register` |
| **Type** | Full Screen Stepper |
| **Steps** | 6 |

**Step 1: Personal Information**
```typescript
interface PersonalInfo {
  firstName: string;
  lastName: string;
  phone: string;
  email: string;
  dateOfBirth: string;
  nationalId: string;
}
```

**Step 2: Verification**
- Phone OTP input
- Email verification link sent
- ID document upload

**Step 3: Sales Experience**
```typescript
interface SalesExperience {
  yearsOfExperience: number;
  industries: string[];
  averageMonthlyVolume: string;
  currentEmployment?: string;
}
```

**Step 4: Preferences**
- Partner types (multi-select, max 3)
- Customer types (multi-select)
- Territory preference (dropdown)

**Step 5: Bank Information**
```typescript
interface BankInfo {
  bankName: string;
  accountNumber: string;
  accountName: string;
  bvn: string;
}
```

**Step 6: Agreements**
- Terms & Conditions checkbox
- Commission Structure checkbox
- Code of Conduct checkbox
- Background Check Consent
- Digital Signature canvas

---

### 2.4 Forgot Password Screen

| Property | Value |
|----------|-------|
| **Route** | `/forgot-password` |
| **Type** | Modal / Screen |

**Flow:**
1. Enter email/phone
2. Receive OTP
3. Enter OTP
4. Create new password
5. Confirm password
6. Success → redirect to Login

---

## 3. ONBOARDING PAGES

### 3.1 Welcome Screen (First Login After Approval)

| Property | Value |
|----------|-------|
| **Route** | `/welcome` |
| **Type** | Full Screen |
| **Shown Once** | Yes |

**Content:**
- Welcome message with partner name
- Partner ID display
- Territory assignment
- Commission tier
- "Let's get started" button

---

### 3.2 Territory Overview

| Property | Value |
|----------|-------|
| **Route** | `/onboarding/territory` |
| **Type** | Full Screen |

**Content:**
- Map view of assigned territory
- Lead allocation count
- Sales targets
- Key locations

---

### 3.3 Quick Training Videos

| Property | Value |
|----------|-------|
| **Route** | `/onboarding/training` |
| **Type** | Full Screen |

**Videos:**
1. Partner Onboarding (5 min)
2. Customer Sales (5 min)
3. Commission Understanding (3 min)
4. App Navigation (3 min)

---

## 4. HOME & DASHBOARD PAGES

### 4.1 Home Dashboard

| Property | Value |
|----------|-------|
| **Route** | `/home` (default tab) |
| **Type** | Tab Screen |
| **Auth Required** | Yes |

**State:**
```typescript
interface DashboardState {
  user: User;
  summary: {
    newLeads: number;
    activeDeals: number;
    monthlyEarnings: number;
    conversionRate: number;
  };
  todayPriorities: Priority[];
  recentActivity: Activity[];
  territory: TerritorySummary;
}
```

**Components:**
- `DashboardHeader` - Greeting + date
- `MetricCards` - 4 key metrics
- `PrioritySection` - Today's priority items
- `QuickActions` - Action buttons grid
- `RecentActivity` - Activity feed
- `TerritoryCard` - Territory overview

---

### 4.2 My Territory Page

| Property | Value |
|----------|-------|
| **Route** | `/territory` |
| **Type** | Full Screen |

**Content:**
- Interactive map view
- Territory boundaries
- Partner locations (pins)
- Customer locations (pins)
- Lead locations (pins)
- Statistics sidebar

---

### 4.3 Notifications Center

| Property | Value |
|----------|-------|
| **Route** | `/notifications` |
| **Type** | Full Screen |

**Notification Types:**
- New lead assigned
- Deal stage change
- Commission earned
- Partner activated
- Follow-up reminder
- System announcements

---

## 5. LEADS MANAGEMENT PAGES

### 5.1 Leads Tab (Main)

| Property | Value |
|----------|-------|
| **Route** | `/leads` (tab) |
| **Type** | Tab Screen |

**Sub-Tabs:**
- AI-Generated (default)
- Partner Referrals
- Customer Inquiries
- Follow-up Reminders
- All Leads

**State:**
```typescript
interface LeadsState {
  activeTab: 'AI_GENERATED' | 'PARTNER_REF' | 'CUSTOMER' | 'FOLLOW_UP' | 'ALL';
  leads: Lead[];
  filters: LeadFilters;
  sortBy: 'SCORE' | 'DATE' | 'NAME' | 'TYPE';
  searchQuery: string;
  loading: boolean;
}
```

**Components:**
- `LeadsTabs` - Tab switcher
- `LeadsFilterBar` - Filter + Sort + Search
- `LeedsList` - Scrollable list
- `LeadCard` - Individual lead item
- `EmptyState` - No leads message
- `PullToRefresh` - Refresh indicator

---

### 5.2 Lead Detail Page

| Property | Value |
|----------|-------|
| **Route** | `/leads/:leadId` |
| **Type** | Full Screen |

**State:**
```typescript
interface LeadDetailState {
  lead: Lead;
  aiInsights: AIInsights;
  activities: Activity[];
  suggestedActions: string[];
  similarLeads: Lead[];
}
```

**Sections:**
1. **Header** - Back button + Lead score + Menu
2. **Contact Info Card** - Name, phone, email, location
3. **AI Insights** - Score, recommendations, similar leads
4. **Business Details** - For partner leads
5. **Activity History** - Timeline of interactions
6. **Suggested Actions** - AI-recommended next steps
7. **Action Buttons** - Call, SMS, Email, Schedule, Convert

**Actions:**
- `callLead()` - Initiate phone call
- `sendSMS()` - Open SMS with template
- `sendEmail()` - Open email client
- `scheduleMeeting()` - Calendar integration
- `convertToDeal()` - Create opportunity
- `disqualifyLead()` - Remove from pipeline

---

### 5.3 Convert to Deal Modal

| Property | Value |
|----------|-------|
| **Route** | Modal |
| **Type** | Bottom Sheet / Modal |

**Form Fields:**
- Deal name (auto-filled)
- Estimated value
- Expected close date
- Probability (slider)
- Initial stage (dropdown)
- Notes

**Validation:**
- Value > 0
- Close date > today
- Probability 10-100

---

## 6. PARTNER ONBOARDING PAGES

### 6.1 Partner Type Selection

| Property | Value |
|----------|-------|
| **Route** | `/partners/add/type` |
| **Type** | Full Screen |

**Grid Options:**
- Courier Partner
- Haulage Partner
- Warehouse Partner
- E-commerce Vendor
- Air/Ocean Agent
- Location Agent
- Wholesale Partner
- Influencer Partner

---

### 6.2 Partner Registration Form (Stepper)

| Property | Value |
|----------|-------|
| **Route** | `/partners/add/:type` |
| **Type** | Full Screen Stepper |
| **Steps** | 5 |

**Step 1: Basic Information**
```typescript
interface PartnerBasicInfo {
  businessName: string;
  contactPerson: string;
  phone: string;
  email: string;
  address: Address;
}
```

**Step 2: Type-Specific Fields**
- Varies by partner type
- Dynamic form based on selection

**Step 3: Documents Upload**
- Business registration
- Tax certificate
- ID document
- Bank details
- Type-specific certifications

**Step 4: Territory & Location**
- Operating areas
- Service coverage
- GPS coordinates
- Additional locations

**Step 5: Review & Submit**
- Summary display
- Terms acceptance
- Digital signature
- Submit button

---

### 6.3 Partner Detail/Status Page

| Property | Value |
|----------|-------|
| **Route** | `/partners/:partnerId` |
| **Type** | Full Screen |

**Content:**
- Partner status badge
- Business information
- Contact details
- Application timeline
- Referral bonus info
- Action buttons (based on status)

---

### 6.4 My Partners Page

| Property | Value |
|----------|-------|
| **Route** | `/partners` |
| **Type** | Full Screen |

**Sections:**
- Filter by status
- Filter by type
- Search by name
- Sort by date/name
- Partner cards list

---

## 7. CUSTOMER SALES PAGES

### 7.1 Deals Tab (Main)

| Property | Value |
|----------|-------|
| **Route** | `/deals` (tab) |
| **Type** | Tab Screen |

**Sub-Tabs:**
- Pipeline Overview (default)
- Partner Onboarding Deals
- Customer Sales Deals
- Closed Deals

**Pipeline View:**
- Kanban-style stages
- Drag-and-drop (future)
- Stage counts
- Total pipeline value

---

### 7.2 Deal Detail Page

| Property | Value |
|----------|-------|
| **Route** | `/deals/:dealId` |
| **Type** | Full Screen |

**Sections:**
1. **Header** - Deal name + Stage badge
2. **Deal Value Card** - Value + Commission
3. **Customer/Partner Info** - Contact details
4. **Progress Bar** - Stage progress
5. **Stage Timeline** - Visual pipeline
6. **Activity Feed** - All interactions
7. **Next Action** - Due date + action
8. **Action Buttons** - Update, Log, Close

---

### 7.3 Update Deal Stage Modal

| Property | Value |
|----------|-------|
| **Route** | Modal |
| **Type** | Bottom Sheet |

**Options:**
- Select new stage
- Add activity note
- Schedule next action
- Update probability
- Save button

---

### 7.4 Close Deal Modal

| Property | Value |
|----------|-------|
| **Route** | Modal |
| **Type** | Full Screen Modal |

**Form Fields:**
- Final deal value
- Closed date
- Products/services
- Payment terms
- Notes

**Outcome:**
- Lost option (with reason)
- Triggers commission calculation

---

### 7.5 Add Customer Opportunity

| Property | Value |
|----------|-------|
| **Route** | `/deals/add` |
| **Type** | Full Screen |

**Steps:**
1. Customer Type (Individual/Corporate)
2. Customer Information
3. Interest Areas
4. Qualification
5. Create Opportunity

---

## 8. COMMISSION PAGES

### 8.1 Commission Tab (Main)

| Property | Value |
|----------|-------|
| **Route** | `/commission` (tab) |
| **Type** | Tab Screen |

**Sections:**
- This Month Summary
- Commission Breakdown
- Pending Commissions
- Payout Schedule
- Transaction History

---

### 8.2 Commission Overview

| Property | Value |
|----------|-------|
| **Route** | `/commission` (default view) |
| **Type** | Tab Content |

**Content:**
- Hero metric: Total earnings
- Comparison to last month
- Pending amount
- Commission tier badge
- Next payout date

---

### 8.3 Commission Breakdown

| Property | Value |
|----------|-------|
| **Route** | `/commission/breakdown` |
| **Type** | Full Screen |

**Sections:**
- Partner Referral Bonuses (list)
- Customer Sales Commissions (list)
- Performance Bonuses (list)
- Donut chart visualization
- Monthly trend chart

---

### 8.4 Transaction Detail Page

| Property | Value |
|----------|-------|
| **Route** | `/commission/:transactionId` |
| **Type** | Full Screen |

**Content:**
- Transaction ID
- Type (bonus/commission)
- Amount
- Source (deal/partner)
- Date
- Status (pending/paid)
- Payout date
- Calculation breakdown
- Download receipt button

---

### 8.5 Commission History

| Property | Value |
|----------|-------|
| **Route** | `/commission/history` |
| **Type** | Full Screen |

**Content:**
- Month selector
- Monthly summaries
- Transaction list
- Filter by type
- Filter by status
- Export option

---

### 8.6 Commission Tier Info

| Property | Value |
|----------|-------|
| **Route** | `/commission/tiers` |
| **Type** | Full Screen / Modal |

**Content:**
- Current tier badge
- Tier progress bar
- Next tier requirements
- Tier benefits comparison
- Tier achievement history

---

## 9. PROFILE & SETTINGS PAGES

### 9.1 Profile Tab (Main)

| Property | Value |
|----------|-------|
| **Route** | `/profile` (tab) |
| **Type** | Tab Screen |

**Sections:**
- Profile header (avatar, name, ID)
- Performance stats card
- Menu items (Resources, Support, Settings)

---

### 9.2 My Profile Page

| Property | Value |
|----------|-------|
| **Route** | `/profile/me` |
| **Type** | Full Screen |

**Sections:**
- Personal information
- Sales Partner ID
- Territory assignment
- Commission tier
- Edit profile button

---

### 9.3 Performance Stats

| Property | Value |
|----------|-------|
| **Route** | `/profile/performance` |
| **Type** | Full Screen |

**Metrics:**
- Total partners onboarded
- Total customers acquired
- Active deals count
- Closed deals count
- Conversion rate
- Total earnings
- Leaderboard position
- Achievements

---

### 9.4 Resources Page

| Property | Value |
|----------|-------|
| **Route** | `/profile/resources` |
| **Type** | Full Screen |

**Content:**
- Training materials (videos, PDFs)
- Product catalogs
- Marketing assets
- Sales playbooks
- Commission structure card
- Business card template

---

### 9.5 Support Page

| Property | Value |
|----------|-------|
| **Route** | `/profile/support` |
| **Type** | Full Screen |

**Options:**
- Help Center
- Contact Support (chat, email, phone)
- FAQ
- Report an Issue
- Feedback form

---

### 9.6 Settings Page

| Property | Value |
|----------|-------|
| **Route** | `/profile/settings` |
| **Type** | Full Screen |

**Sections:**
- Notifications (push, email, SMS)
- Privacy & Security
- App Preferences (theme, language)
- Account Settings
- Linked Accounts
- Logout button

---

## 10. SHARED COMPONENTS

### 10.1 Navigation Components

**BottomTabBar**
- 5 tabs (Home, Leads, Deals, Commission, Profile)
- Badge indicators
- Active/inactive states

**Header**
- Back button (conditional)
- Title
- Action buttons (conditional)
- Menu button (conditional)

**DrawerNavigation**
- Hamburger trigger
- Menu items
- Active state indicator
- Logout button

---

### 10.2 Card Components

**LeadCard**
- Score badge
- Name
- Type indicator
- Location
- Time stamp
- Action buttons

**DealCard**
- Deal name
- Stage badge
- Value
- Commission
- Progress bar

**PartnerCard**
- Partner type
- Business name
- Status badge
- Date
- Referral bonus

**CommissionCard**
- Transaction type
- Amount
- Source
- Status badge
- Date

---

### 10.3 Input Components

**FormInput**
- Label
- Input field
- Error message
- Helper text
- Optional icon

**FormSelect**
- Label
- Dropdown
- Options list
- Selected value display

**FormMultiSelect**
- Label
- Checkbox list
- Select all option
- Count display

**FileUpload**
- Drag/drop zone
- File list
- Progress indicators
- Remove buttons

**DatePicker**
- Calendar display
- Date selection
- Min/max constraints
- Preset ranges

---

### 10.4 Feedback Components

**LoadingSpinner**
- Centered
- Size variants
- With/without text

**ErrorState**
- Icon
- Message
- Retry button
- Contact support option

**EmptyState**
- Icon
- Message
- Action button(s)

**SuccessModal**
- Icon
- Title
- Message
- Primary action
- Secondary action

**ConfirmationModal**
- Title
- Message
- Cancel button
- Confirm button (destructive styling if needed)

**ToastNotification**
- Message
- Type (success, error, info, warning)
- Duration
- Action button (optional)

---

### 10.5 Visualization Components

**MetricCard**
- Label
- Value (large)
- Change indicator (arrow + percentage)
- Trend icon (up/down)

**ProgressBar**
- Progress value
- Total value
- Color by stage
- Animated

**DonutChart**
- Data segments
- Legend
- Center text
- Tap for details

**LineChart**
- Data points
- X/Y axis labels
- Trend line
- Tap indicators

**ScoreBadge**
- Circular display
- Score value
- Color gradient
- Label

---

### 10.6 List Components

**LeadsList**
- Section headers
- Lead cards
- Pull to refresh
- Infinite scroll
- Loading indicator

**DealsList**
- Stage sections
- Deal cards
- Collapse/expand
- Swipe actions

**TransactionList**
- Grouped by date
- Transaction cards
- Status badges
- Tap for details

**ActivityTimeline**
- Vertical line
- Activity items
- Time stamps
- Icons by type

---

## PAGE TRANSITIONS

### Standard Navigation

```typescript
// Stack navigation (screens)
navigation.navigate('ScreenName', { params })

// Tab navigation
navigation.navigate('TabName')

// Reset stack (replace)
navigation.reset({
  routes: [{ name: 'ScreenName' }]
})

// Go back
navigation.goBack()
```

### Modal Transitions

```typescript
// Bottom sheet
navigation.navigate('BottomSheet', { params })

// Full screen modal
navigation.navigate('Modal', { params })

// Close modal
navigation.goBack()
```

### Deep Linking

```
sales-team-app://                          → Launch app
sales-team-app://leads                    → Leads tab
sales-team-app://leads/LEAD-123           → Lead detail
sales-team-app://deals/DEAL-456           → Deal detail
sales-team-app://commission/COM-789       → Transaction detail
sales-team-app://profile/settings         → Settings
```

---

**END OF PAGE BY PAGE FLOW DOCUMENTATION**

**All 4 documentation files completed:**
- ✅ 01_UI_Flow_Documentation.md
- ✅ 02_Wireframes_Documentation.md
- ✅ 03_Mock_Flow_Documentation.md
- ✅ 04_Page_By_Page_Flow_Documentation.md

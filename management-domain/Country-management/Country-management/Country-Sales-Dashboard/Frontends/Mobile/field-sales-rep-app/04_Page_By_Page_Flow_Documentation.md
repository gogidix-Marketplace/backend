# FIELD SALES REPRESENTATIVE APP - PAGE BY PAGE FLOW DOCUMENTATION

**Version:** 1.0
**Domain:** Business Domain
**Subdomain:** Country-Sales-Dashboard
**Frontend:** field-sales-rep-app (Mobile)
**Date:** 2026-03-08

---

## TABLE OF CONTENTS

1. [Page Tree](#page-tree)
2. [Authentication Flow](#authentication-flow)
3. [Home Dashboard Flow](#home-dashboard-flow)
4. [Route Management Flow](#route-management-flow)
5. [Check-In/Check-Out Flow](#check-incheck-out-flow)
6. [Partner Directory Flow](#partner-directory-flow)
7. [Partner Onboarding Flow](#partner-onboarding-flow)
8. [Lead Capture Flow](#lead-capture-flow)
9. [Product Catalog Flow](#product-catalog-flow)
10. [Order Entry Flow](#order-entry-flow)
11. [Performance Dashboard Flow](#performance-dashboard-flow)
12. [Settings Flow](#settings-flow)
13. [Permission Matrix](#permission-matrix)

---

## PAGE TREE

```
Field Sales Rep App
├── / (Splash/Launch)
├── /login
├── /onboarding
├── / (home) - Tab Navigator
│   ├── /home
│   │   ├── /today-summary
│   │   ├── /quick-actions
│   │   └── /alerts
│   ├── /routes
│   │   ├── /route-list
│   │   ├── /route-detail
│   │   ├── /route-map
│   │   └── /route-stop
│   ├── /partners
│   │   ├── /partner-directory
│   │   ├── /partner-detail
│   │   ├── /onboarding
│   │   │   ├── /onboarding-step-1
│   │   │   ├── /onboarding-step-2
│   │   │   └── /onboarding-review
│   │   └── /partner-activity
│   ├── /leads
│   │   ├── /lead-list
│   │   ├── /lead-detail
│   │   ├── /create-lead
│   │   └── /lead-conversion
│   └── /more
│       ├── /performance
│       ├── /orders
│       │   ├── /order-list
│       │   ├── /create-order
│       │   └── /order-detail
│       ├── /products
│       │   ├── /product-catalog
│       │   └── /product-detail
│       ├── /expenses
│       │   ├── /expense-list
│       │   └── /submit-expense
│       ├── /notifications
│       └── /settings
│           ├── /profile
│           ├── /preferences
│           └── /offline-mode
```

---

## AUTHENTICATION FLOW

### Login Page Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Launch App      │────>│ Check Auth      │────>│ Route to Screen │
│                 │     │ Token Valid?    │     │                 │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                              │
                ┌─────────────┴─────────────┐
                ▼                           ▼
         ┌──────────────┐           ┌──────────────┐
         │ Valid Token  │           │ No/Invalid   │
         │ → Home       │           │ → Login      │
         └──────────────┘           └──────────────┘
                                            │
                                            ▼
         ┌─────────────────────────────────────────────┐
         │              Login Screen                    │
         │  • Email/Phone Input                         │
         │  • Password Input                            │
         │  • "Forgot Password" Link                    │
         │  • Login Button                              │
         └─────────────────────────────────────────────┘
                              │
                              ▼
         ┌─────────────────────────────────────────────┐
         │            Authenticate                      │
         │  • Validate credentials                     │
         │  • Fetch user profile                        │
         │  • Get assigned routes                       │
         │  • Download offline data                     │
         └─────────────────────────────────────────────┘
                              │
                ┌─────────────┴─────────────┐
                ▼                           ▼
         ┌──────────────┐           ┌──────────────┐
         │ Success      │           │ Failure      │
         │ → Home       │           │ Show Error   │
         └──────────────┘           └──────────────┘
```

### Login Page Elements

| Element | Type | Required | Validation |
|---------|------|----------|------------|
| Email/Phone | Text Input | Yes | Valid email or phone format |
| Password | Password Input | Yes | Min 8 characters |
| Login Button | Button | Yes | Enabled when valid |
| Forgot Password | Link | No | - |
| Biometric Login | Button | No | Device supported |
| Stay Signed In | Toggle | No | Default: true |

---

## HOME DASHBOARD FLOW

### Home Dashboard Entry Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ User Logs In    │────>│ Load Dashboard  │────>│ Display Today's │
│                 │     │ Data            │     │ Summary         │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
        ┌───────────────────────────────────────────────┼───────────────────────────────┐
        ▼                                               ▼                               ▼
 ┌──────────────┐                               ┌──────────────┐               ┌──────────────┐
 │ Today's      │                               │ Quick        │               │ Alerts       │
 │ Progress     │                               │ Actions      │               │ Badge        │
 │ Card         │                               │ Card         │               │              │
 └──────────────┘                               └──────────────┘               └──────────────┘
        │                                               │                               │
        ▼                                               ▼                               ▼
 ┌──────────────┐                               ┌──────────────┐               ┌──────────────┐
 │ • Stops      │                               │ • Check In   │               │ • Route      │
 │   Completed  │                               │ • Add Lead   │               │   Changes    │
 │ • Pending    │                               │ • Create     │               │ • Order      │
 │   Stops      │                               │   Order      │               │   Updates    │
 │ • Check-In   │                               │ • View       │               │ • Pending    │
 │   Status     │                               │   Products   │               │   Tasks      │
 └──────────────┘                               └──────────────┘               └──────────────┘
```

### Dashboard Metrics

| Metric | Description | Data Source | Tap Action |
|--------|-------------|-------------|------------|
| Today's Progress | Completion percentage of today's route | Local state | View route detail |
| Check-In Status | Current check-in location/time | GPS/Local | Check in/out |
| Pending Orders | Orders awaiting submission | Local storage | View orders |
| New Leads | Leads captured today | Local/API | View leads |
| Partners Visited | Count of partner visits today | Local state | View activity log |
| Today's Sales | Total sales value today | Local/API | View sales details |

### Quick Actions

| Action | Description | Navigation |
|--------|-------------|------------|
| Check In | Open check-in screen with GPS | /check-in |
| Add Lead | Open lead capture form | /leads/create |
| Create Order | Start new order | /orders/create |
| View Products | Browse product catalog | /products |
| My Performance | View performance stats | /performance |
| Expenses | Submit expenses | /expenses |

---

## ROUTE MANAGEMENT FLOW

### Route List Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Open Routes     │────>│ Load Routes     │────>│ Display Routes  │
│ Tab             │     │ For Date Range  │     │ List            │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
        ┌───────────────────────────────────────────────┼───────────────────────────────┐
        ▼                                               ▼                               ▼
 ┌──────────────┐                               ┌──────────────┐               ┌──────────────┐
 │ Today        │                               │ This Week    │               │ Upcoming    │
 │ Route        │                               │ Routes       │               │ Routes       │
 └──────────────┘                               └──────────────┘               └──────────────┘
        │                                               │                               │
        ▼                                               ▼                               ▼
 ┌──────────────┐                               ┌──────────────┐               ┌──────────────┐
 │ Route Card   │                               │ Route Card   │               │ Route Card   │
 │ • Date       │                               │ • Date       │               │ • Date       │
 │ • Status     │                               │ • Status     │               │ • Status     │
 │ • Stop Count │                               │ • Stop Count │               │ • Stop Count │
 │ • ETA        │                               │ • ETA        │               │ • ETA        │
 └──────────────┘                               └──────────────┘               └──────────────┘
        │
        ▼
┌─────────────────┐
│ Tap Route       │────> Route Detail Screen
└─────────────────┘
```

### Route Detail Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Route Selected  │────>│ Load Route      │────>│ Display Route   │
│                 │     │ Details         │     │ Overview        │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
        ┌───────────────────────────────────────────────┼───────────────────────────────┐
        ▼                                               ▼                               ▼
 ┌──────────────┐                               ┌──────────────┐               ┌──────────────┐
 │ Route Info   │                               │ Stops List   │               │ Actions      │
 │ • Route Name │                               │ • All Stops  │               │ • Start      │
 │ • Date       │                               │ • With Times │               │   Route      │
 │ • Total      │                               │ • Check-In   │               │ • Navigate   │
 │   Distance   │                               │   Status     │               │ • Call       │
 │ • Est. Time  │                               └──────────────┘               │   Support    │
 └──────────────┘                                                       └──────────────┘
```

### Route Stop Actions

| Action | Description | Conditions |
|--------|-------------|------------|
| Navigate | Open maps app with directions | GPS enabled |
| Check In | Check in at this location | Within geofence or override |
| Call Partner | Initiate phone call | Partner phone exists |
| View Partner | Open partner detail page | - |
| Add Note | Add visit note | - |
| Skip Stop | Skip this stop (reason required) | - |
| Mark Complete | Mark stop as completed | Checked in |

### Route Status Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Route Created   │────>│ Sales Rep       │────>│ Route In        │
│ (PLANNED)       │     │ Starts Route    │     │ PROGRESS        │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                                                        ▼
┌─────────────────────────────────────────────────────────────────┐
│                     Process Each Stop                           │
│  • Navigate to stop                                              │
│  • Check in at location                                          │
│  • Complete visit activities                                     │
│  • Check out / Mark complete                                     │
└─────────────────────────────────────────────────────────────────┘
                                                        │
                                                        ▼
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ All Stops       │────>│ Route           │────>│ Route           │
│ Completed       │     │ Completed       │     │ COMPLETED       │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

---

## CHECK-IN/CHECK-OUT FLOW

### Check-In Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Tap Check In    │────>│ Capture GPS     │────>│ Verify          │
│ Button/Stop     │     │ Location        │     │ Location        │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                ┌───────────────────────────────────────┼───────────────────────────────┐
                ▼                                       ▼                               ▼
         ┌──────────────┐                       ┌──────────────┐               ┌──────────────┐
         │ Within      │                       │ Near (50-    │               │ Far (>100m)  │
         │ Geofence    │                       │ 100m)        │               │              │
         │ (≤50m)      │                       │ ⚠️ Warning   │               │ 🔴 Error     │
         │ ✅ Verified │                       └──────────────┘               └──────────────┘
         └──────────────┘                               │                               │
                │                                       │                               │
                ▼                                       ▼                               ▼
         ┌──────────────┐                       ┌──────────────┐               ┌──────────────┐
         │ Confirm     │                       │ Allow with   │               │ Require     │
         │ Check-In    │                       │ Override     │               │ Photo Proof  │
         │             │                       │ Option       │               │ + Reason     │
         └──────────────┘                       └──────────────┘               └──────────────┘
                │                                       │                               │
                └───────────────────────────────────────┴───────────────────────────────┘
                                                        │
                                                        ▼
                                         ┌─────────────────────────┐
                                         │   Check-In Confirmed    │
                                         │   • Timestamp recorded  │
                                         │   • Location saved      │
                                         │   • Timer started       │
                                         │   • Notification sent   │
                                         └─────────────────────────┘
```

### Check-Out Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Tap Check Out   │────>│ Confirm Check   │────>│ Complete Visit   │
│ Button          │     │ Out             │     │ Summary         │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
        ┌───────────────────────────────────────────────┼───────────────────────────────┐
        ▼                                               ▼                               ▼
 ┌──────────────┐                               ┌──────────────┐               ┌──────────────┐
 │ Record Visit │                               │ Add Next     │               │ Go to Next  │
 │ Notes        │                               │ Action       │               │ Stop        │
 └──────────────┘                               └──────────────┘               └──────────────┘
        │                                               │                               │
        ▼                                               ▼                               ▼
 ┌──────────────┐                               ┌──────────────┐               ┌──────────────┐
 │ Create Order │                               │ Schedule     │               │ Navigate    │
 │ (Optional)   │                               │ Follow-Up    │               │ to Next     │
 └──────────────┘                               └──────────────┘               └──────────────┘
```

### Geofence Verification

| Distance | Status | Action Required | Color Indicator |
|----------|--------|-----------------|-----------------|
| 0-50m | Verified | None | Green |
| 51-100m | Warning | Optional override | Yellow |
| 101-200m | Mismatch | Photo + Reason required | Orange |
| 200m+ | Error | Manager approval required | Red |

### Check-In Data Captured

| Data Field | Type | Source | Required |
|------------|------|--------|----------|
| Check-In ID | UUID | Generated | Yes |
| Sales Rep ID | String | Auth context | Yes |
| Partner ID | String | Selected | Yes |
| Timestamp | DateTime | Device | Yes |
| Latitude | Decimal | GPS | Yes |
| Longitude | Decimal | GPS | Yes |
| Accuracy | Decimal | GPS | Yes |
| Verified | Boolean | Geofence check | Yes |
| Distance From Target | Decimal | Calculated | If > 50m |
| Photo Proof | URL | Camera upload | If required |
| Override Reason | Text | User input | If override |

---

## PARTNER DIRECTORY FLOW

### Partner List Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Open Partners   │────>│ Load Partners   │────>│ Display Partner │
│ Tab             │     │ (Synced/Local)  │     │ List            │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
        ┌───────────────────────────────────────────────┼───────────────────────────────┐
        ▼                                               ▼                               ▼
 ┌──────────────┐                               ┌──────────────┐               ┌──────────────┐
 │ Search       │                               │ Filter By    │               │ Sort By     │
 │ Bar          │                               │ Category     │               │ Name/Date   │
 └──────────────┘                               └──────────────┘               └──────────────┘
        │                                               │                               │
        ▼                                               ▼                               ▼
┌─────────────────┐                             ┌─────────────────┐             ┌─────────────────┐
│ Partner Cards   │                             │ Filter Chips:   │             │ Sort Options:   │
│ • Name          │                             │ • All           │             │ • Name A-Z      │
│ • Category      │                             │ • Retailers     │             │ • Name Z-A      │
│ • Location      │                             │ • Restaurants   │             │ • Nearest       │
│ • Status        │                             │ • Services      │             │ • Recently     │
│ • Last Visit    │                             │ • Prospects     │             │   Visited       │
└─────────────────┘                             └─────────────────┘             └─────────────────┘
```

### Partner Detail Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Partner Card    │────>│ Load Partner    │────>│ Display Partner │
│ Tapped          │     │ Full Details    │     │ Detail Screen   │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
        ┌───────────────────────────────────────────────┼───────────────────────────────┐
        ▼                                               ▼                               ▼
 ┌──────────────┐                               ┌──────────────┐               ┌──────────────┐
 │ Partner Info │                               │ Activity     │               │ Quick       │
 │ • Name       │                               │ History      │               │ Actions     │
 │ • Business   │                               │ • Visits     │               │ • Call      │
 │ • Address    │                               │ • Orders     │               │ • Navigate  │
 │ • Contact    │                               │ • Issues     │               │ • Email     │
 │ • Status     │                               └──────────────┘               │ • Check In  │
 └──────────────┘                                                               └──────────────┘
```

### Partner Quick Actions

| Action | Description | Navigation/Action |
|--------|-------------|-------------------|
| Call | Dial partner phone | `tel:` link |
| Navigate | Open maps with directions | Maps app |
| Email | Open email composer | Mailto link |
| Check In | Initiate check-in flow | /check-in |
| Create Order | Start new order | /orders/create?partnerId=X |
| View History | See all past interactions | /partner/[id]/history |
| Edit Partner | Update partner details | /partner/[id]/edit |
| Add Note | Add activity note | Modal dialog |

---

## PARTNER ONBOARDING FLOW

### Onboarding Overview Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Tap Onboard     │────>│ Onboarding      │────>│ Step 1:         │
│ New Partner     │     │ Wizard Started  │     │ Basic Info      │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                                                        ▼
┌─────────────────────────────────────────────────────────────────┐
│                     Step 1: Basic Information                   │
│  • Business Name                                                │
│  • Business Type (Retailer/Restaurant/Service/Other)            │
│  • Owner Name                                                   │
│  • Contact Phone                                                │
│  • Email                                                        │
└─────────────────────────────────────────────────────────────────┘
                                                        │
                                                        ▼
┌─────────────────────────────────────────────────────────────────┐
│                     Step 2: Location                            │
│  • Address Search/Entry                                         │
│  • GPS Location Capture                                         │
│  • Operating Area/Radius                                        │
│  • Geofence Configuration                                       │
└─────────────────────────────────────────────────────────────────┘
                                                        │
                                                        ▼
┌─────────────────────────────────────────────────────────────────┐
│                     Step 3: Business Details                     │
│  • Business Registration Number (if applicable)                  │
│  • Tax ID                                                       │
│  • Bank Details (for payments)                                  │
│  • Preferred Commission Structure                               │
└─────────────────────────────────────────────────────────────────┘
                                                        │
                                                        ▼
┌─────────────────────────────────────────────────────────────────┐
│                     Step 4: Products/Services                    │
│  • Product Categories of Interest                               │
│  • Expected Volume                                               │
│  • Storage Capacity                                             │
│  • Delivery Requirements                                        │
└─────────────────────────────────────────────────────────────────┘
                                                        │
                                                        ▼
┌─────────────────────────────────────────────────────────────────┐
│                     Step 5: Documents                            │
│  • Business Registration (Photo/Upload)                          │
│  • ID Document (Owner)                                          │
│  • Proof of Address                                             │
│  • Bank Statement/Confirmation                                  │
│  • Agreement Signature                                           │
└─────────────────────────────────────────────────────────────────┘
                                                        │
                                                        ▼
┌─────────────────────────────────────────────────────────────────┐
│                     Review & Submit                             │
│  • Summary of all entered data                                  │
│  • Document attachment preview                                  │
│  • Edit any section                                             │
│  • Submit for Approval                                          │
└─────────────────────────────────────────────────────────────────┘
```

### Onboarding Step Validation

| Step | Required Fields | Validation | Next Step Enabled |
|------|----------------|------------|-------------------|
| 1. Basic Info | Business Name, Phone, Email | Phone format, Email format | All valid |
| 2. Location | Address, GPS Coordinates | GPS captured, within country | Location captured |
| 3. Business Details | Varies by type | Format checks | Required fields filled |
| 4. Products | At least one category | Selection made | Category selected |
| 5. Documents | Registration, ID, Address | File uploaded, readable | All docs uploaded |

### Onboarding Actions

| Action | Description | When Available |
|--------|-------------|----------------|
| Save as Draft | Save progress, continue later | Any time after Step 1 |
| Next | Proceed to next step | Current step valid |
| Previous | Go back to previous step | Any time after Step 1 |
| Edit Section | Jump to specific step | Review screen |
| Submit | Submit for approval | All steps complete |
| Upload Photo | Take photo or select from gallery | Document steps |
| Locate on Map | Verify GPS location | Step 2 |

---

## LEAD CAPTURE FLOW

### Lead List Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Open Leads Tab  │────>│ Load Leads      │────>│ Display Lead    │
│                 │     │ (Synced/Local)  │     │ List            │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
        ┌───────────────────────────────────────────────┼───────────────────────────────┐
        ▼                                               ▼                               ▼
 ┌──────────────┐                               ┌──────────────┐               ┌──────────────┐
 │ Status Tabs  │                               │ Search       │               │ FAB: Add    │
│ • New         │                               │ Bar          │               │ New Lead    │
│ • Contacted   │                               └──────────────┘               └──────────────┘
│ • Qualified   │
│ • Converted   │
│ • Lost        │
└──────────────┘
```

### Create Lead Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Tap Add Lead    │────>│ Lead Form       │────>│ Capture Lead    │
│ (FAB)           │     │ Displayed       │     │ Information     │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
        ┌───────────────────────────────────────────────┼───────────────────────────────┐
        ▼                                               ▼                               ▼
 ┌──────────────┐                               ┌──────────────┐               ┌──────────────┐
 │ Contact Info │                               │ Business     │               │ Interest    │
 │ • Name       │                               │ Info         │               │ • Products  │
 │ • Phone      │                               │ • Business   │               │ • Category  │
 │ • Email      │                               │   Name       │               │ • Volume    │
 │ • Title      │                               │ • Location   │               │ • Budget    │
 └──────────────┘                               └──────────────┘               └──────────────┘
        │                                               │                               │
        └───────────────────────────────────────────────┴───────────────────────────────┘
                                                        │
                                                        ▼
                                         ┌─────────────────────────┐
                                         │   Additional Info       │
                                         │   • Source (Walk-in/Ref)│
                                         │   • Notes               │
                                         │   • Follow-up Date      │
                                         │   • Photo (Optional)    │
                                         └─────────────────────────┘
                                                        │
                                                        ▼
                                         ┌─────────────────────────┐
                                         │   Save Lead             │
                                         │   • Assign to Rep       │
                                         │   • Set Status          │
                                         │   • Schedule Follow-up  │
                                         └─────────────────────────┘
```

### Lead Conversion Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Lead Detail     │────>│ Tap Convert     │────>│ Conversion      │
│ View            │     │ Button          │     │ Options         │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
        ┌───────────────────────────────────────────────┼───────────────────────────────┐
        ▼                                               ▼                               ▼
 ┌──────────────┐                               ┌──────────────┐               ┌──────────────┐
 │ Convert to   │                               │ Schedule    │               │ Mark as     │
 │ Partner      │                               │ Follow-up   │               │ Lost        │
 │ (Onboarding) │                               │             │               │             │
 └──────────────┘                               └──────────────┘               └──────────────┘
        │
        ▼
┌─────────────────────────────────────────────────────────────────┐
│                     Partner Onboarding                          │
│  • Pre-fill information from lead                               │
│  • Complete remaining partner registration steps                 │
│  • Create partner profile                                       │
└─────────────────────────────────────────────────────────────────┘
```

### Lead Status Transitions

| Current Status | Possible Next Status | Action Required |
|----------------|---------------------|-----------------|
| New | Contacted | Log contact activity |
| New | Qualified | Qualification criteria met |
| New | Lost | Lost reason required |
| Contacted | Qualified | Qualification criteria met |
| Contacted | New | Re-open (reset) |
| Contacted | Lost | Lost reason required |
| Qualified | Converted | Convert to partner/order |
| Qualified | Lost | Lost reason required |
| Lost | New | Re-open (manager approval) |

---

## PRODUCT CATALOG FLOW

### Product Catalog Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Open Products   │────>│ Load Products   │────>│ Display Product │
│ (from More/     │     │ (Cached/Online) │     │ Catalog         │
│ Quick Actions)  │     │                 │     │                 │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
        ┌───────────────────────────────────────────────┼───────────────────────────────┐
        ▼                                               ▼                               ▼
 ┌──────────────┐                               ┌──────────────┐               ┌──────────────┐
 │ Category     │                               │ Search       │               │ Sort/Filter │
 │ List         │                               │ Bar          │               │ Options     │
└──────────────┘                               └──────────────┘               └──────────────┘
        │
        ▼
┌─────────────────────────────────────────────────────────────────┐
│                     Product Cards                               │
│  • Product Image                                                │
│  • Product Name                                                 │
│  • SKU/Code                                                     │
│  • Price                                                        │
│  • Stock Status                                                 │
│  • Wholesale/Retail Prices                                      │
└─────────────────────────────────────────────────────────────────┘
```

### Product Detail Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Product Card    │────>│ Load Product    │────>│ Display Product │
│ Tapped          │     │ Full Details    │     │ Details         │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
        ┌───────────────────────────────────────────────┼───────────────────────────────┐
        ▼                                               ▼                               ▼
 ┌──────────────┐                               ┌──────────────┐               ┌──────────────┐
 │ Product Info │                               │ Pricing     │               │ Actions     │
 │ • Image      │                               │ • Wholesale │               │ • Add to    │
 │ • Name       │                               │ • Retail    │               │   Order     │
 │ • SKU        │                               │ • Margins   │               │ • Share     │
 │ • Category   │                               │ • Discounts │               │ • View      │
 │ • Description│                               └──────────────┘               │   Stock     │
 │ • Specs      │                                                               └──────────────┘
 └──────────────┘
```

### Product Search/Filter Options

| Filter | Options | Description |
|--------|---------|-------------|
| Category | All categories | Filter by product category |
| In Stock | Yes/No/All | Show available products |
| Price Range | Min/Max | Filter by price |
| Search | Name/SKU | Text search |

---

## ORDER ENTRY FLOW

### Create Order Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Tap Create      │────>│ Select Partner  │────>│ Order Header    │
│ Order           │     │ (or pre-fill)   │     │ Screen          │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                                                        ▼
┌─────────────────────────────────────────────────────────────────┐
│                     Add Line Items                             │
│  • Search/Add Products                                         │
│  • Set Quantities                                               │
│  • View Prices                                                  │
│  • Calculate Totals                                             │
└─────────────────────────────────────────────────────────────────┘
                                                        │
                                                        ▼
┌─────────────────────────────────────────────────────────────────┐
│                     Order Details                               │
│  • Delivery Date                                                │
│  • Delivery Address                                             │
│  • Payment Terms                                                │
│  • Special Instructions                                         │
└─────────────────────────────────────────────────────────────────┘
                                                        │
                                                        ▼
┌─────────────────────────────────────────────────────────────────┐
│                     Review Order                                │
│  • Summary of all items                                         │
│  • Total calculation                                            │
│  • Discount application                                         │
│  • Tax calculation                                              │
└─────────────────────────────────────────────────────────────────┘
                                                        │
                        ┌───────────────────────┴───────────────────────┐
                        ▼                                               ▼
                 ┌──────────────┐                               ┌──────────────┐
                 │ Submit Order │                               │ Save Draft  │
                 │              │                               │              │
                 └──────────────┘                               └──────────────┘
                        │                                               │
                        ▼                                               ▼
                 ┌─────────────────────────┐                 ┌─────────────────────────┐
                 │   Order Created         │                 │   Draft Saved           │
                 │   • Order ID generated  │                 │   • Available in drafts  │
                 │   • Notification sent   │                 │   • Can resume later     │
                 └─────────────────────────┘                 └─────────────────────────┘
```

### Order Actions

| Action | Description | When Available |
|--------|-------------|----------------|
| Add Item | Add product to order | Edit mode |
| Update Qty | Change item quantity | Edit mode |
| Remove Item | Delete line item | Edit mode |
| Apply Discount | Add discount code/percentage | With permission |
| Set Delivery | Configure delivery | Review screen |
| Submit | Finalize and submit | Valid order |
| Save Draft | Save for later | Any time |
| Print | Print order summary | After submit |

---

## PERFORMANCE DASHBOARD FLOW

### Performance Dashboard Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Open More Tab   │────>│ Tap Performance │────>│ Load Performance│
│                 │     │                 │     │ Data            │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
        ┌───────────────────────────────────────────────┼───────────────────────────────┐
        ▼                                               ▼                               ▼
 ┌──────────────┐                               ┌──────────────┐               ┌──────────────┐
 │ Time Range   │                               │ KPI Cards    │               │ Charts      │
│ • Today       │                               │ • Sales      │               │ • Sales Trend│
│ • This Week   │                               │ • Visits     │               │ • Visit     │
│ • This Month  │                               │ • Orders     │               │   Trend     │
│ • Custom      │                               │ • Leads      │               │ • Top       │
└──────────────┘                               │ • Conversion │               │   Products  │
                                                └──────────────┘               └──────────────┘
```

### Performance KPIs

| KPI | Description | Calculation | Display |
|-----|-------------|-------------|---------|
| Sales Today | Total sales value | Sum of order totals | Currency |
| Visits Today | Partner visits completed | Count of check-ins | Number |
| Orders Today | Orders submitted | Count of orders | Number |
| Leads Today | New leads captured | Count of new leads | Number |
| Conversion Rate | Leads to partners ratio | Converted/Total | Percentage |
| Avg Order Value | Average order size | Total Sales/Orders | Currency |
| Route Completion | Route progress | Completed stops/Total | Percentage |

---

## SETTINGS FLOW

### Settings Menu Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ Open More Tab   │────>│ Tap Settings    │────>│ Display Settings│
│                 │     │                 │     │ Menu            │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
        ┌───────────────────────────────────────────────┼───────────────────────────────┐
        ▼                                               ▼                               ▼
 ┌──────────────┐                               ┌──────────────┐               ┌──────────────┐
 │ Profile      │                               │ Preferences │               │ App Settings│
│ • Edit Profile│                               │ • Language  │               │ • Theme     │
│ • Change PWD  │                               │ • Currency  │               │ • Notifications│
└──────────────┘                               │ • Units     │               │ • Sound     │
                                                └──────────────┘               └──────────────┘
                                                │                               │
                                                ▼                               ▼
                                         ┌──────────────┐               ┌──────────────┐
                                         │ Data & Sync  │               │ Support     │
│ • Offline Mode│               │ • Help      │
│ • Sync Now    │               │ • Contact   │
│ • Clear Cache │               │ • About     │
                                         └──────────────┘               └──────────────┘
```

### Settings Options

| Setting | Options | Default | Description |
|---------|---------|---------|-------------|
| Language | English, French, Portuguese | Country default | App display language |
| Currency | Local currency, USD | Country currency | Display currency |
| Units | Metric, Imperial | Country standard | Measurement units |
| Theme | Light, Dark, System | System | App appearance |
| Notifications | On/Off | On | Push notifications |
| Sound | On/Off | On | App sounds |
| Auto Sync | WiFi only, Always | WiFi only | Data sync mode |
| Location Services | Always, When in use | When in use | GPS permission |

---

## PERMISSION MATRIX

### User Roles

| Role | Description |
|------|-------------|
| Sales Executive | Senior sales representative with full access |
| Sales Representative | Standard sales rep with core functionality |
| Field Sales Agent | Limited access, may require approvals |

### Role-Based Access Control

| Feature | Sales Executive | Sales Representative | Field Sales Agent |
|---------|----------------|---------------------|-------------------|
| View Dashboard | ✓ | ✓ | ✓ |
| View Routes | ✓ | ✓ | Own only |
| Manage Routes | ✓ | Own only | Own only |
| Check In/Out | ✓ | ✓ | ✓ |
| Partner Directory | ✓ | ✓ | ✓ |
| Create Partner | ✓ | ✓ | Submit for approval |
| Edit Partner | ✓ | Own created | No |
| Capture Lead | ✓ | ✓ | ✓ |
| Convert Lead | ✓ | ✓ | Submit for approval |
| Create Order | ✓ | ✓ | ✓ |
| Apply Discount | ✓ | Up to limit | No |
| Submit Expense | ✓ | ✓ | ✓ (with approval) |
| View Performance | ✓ | Own only | Own only |
| View Reports | ✓ | ✓ | No |
| Export Data | ✓ | No | No |

### Approval Limits

| Action | Sales Executive | Sales Representative | Field Sales Agent |
|--------|----------------|---------------------|-------------------|
| Order Value | Unlimited | Up to country limit | Up to ₦100,000 |
| Discount Percentage | Up to 15% | Up to 10% | Requires approval |
| Expense Amount | Up to ₦50,000 | Up to ₦25,000 | Up to ₦10,000 |
| Partner Creation | Auto-approved | Auto-approved | Manager approval |
| Lead Conversion | Auto-approved | Auto-approved | Manager approval |

---

## OFFLINE MODE FLOW

### Offline Detection Flow

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│ App Active      │────>│ Connectivity    │────>│ Update Mode     │
│                 │     │ Check           │     │ Indicator       │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                ┌───────────────────────────────────────┴───────────────────────────────┐
                ▼                                                                       ▼
         ┌──────────────┐                                                       ┌──────────────┐
         │ Online Mode  │                                                       │ Offline Mode  │
         │              │                                                       │              │
         │ • Live API   │                                                       │ • Local      │
         │ • Real-time  │                                                       │   storage    │
         │ • Sync       │                                                       │ • Queue      │
         └──────────────┘                                                       │   actions    │
                                                                                └──────────────┘
```

### Offline Capabilities

| Feature | Online | Offline | Sync Behavior |
|---------|--------|---------|---------------|
| View Dashboard | Live | Cached data | Refresh when online |
| View Routes | Live | Cached | Sync when online |
| Check In/Out | Live | Queue | Upload when online |
| Create Partner | Live | Draft | Submit when online |
| Capture Lead | Live | Save locally | Sync when online |
| Create Order | Live | Draft | Submit when online |
| View Products | Live | Cached | Update when online |
| Performance | Live | Cached data | Refresh when online |

### Sync Queue Priorities

| Priority | Action Types |
|----------|--------------|
| Critical | Check-ins, Check-outs |
| High | New orders, Lead conversions |
| Medium | Partner updates, Expense submissions |
| Low | Performance data, Product catalog |

---

## ERROR HANDLING FLOW

### Common Error Scenarios

| Error | Display | Action |
|-------|---------|--------|
| No Internet | Banner + cached data | Show offline mode |
| GPS Disabled | Modal prompt | Open settings |
| Camera Denied | Modal prompt | Open settings |
| Login Failed | Error message | Retry |
| Sync Failed | Toast notification | Retry in background |
| Order Submit Failed | Error + retry | Save as draft |

---

## SUMMARY

**Field Sales Representative App** - Complete page-by-page flow documentation for mobile application used by field sales teams at the country level.

**Key Features:**
- Route management with GPS navigation
- Check-in/check-out with geofence verification
- Partner onboarding workflows
- Lead capture and conversion
- Order entry with offline support
- Performance tracking
- Expense submission

**Technology Stack:**
- React Native + Expo
- TypeScript
- Redux Toolkit + RTK Query
- AsyncStorage (offline)
- Expo Camera + Location

**Document End**

# WAREHOUSE-PARTNERS-DASHBOARD - UI FLOW DOCUMENTATION

**Domain:** Shared Business Infrastructure
**Subdomain:** Warehousing Core
**Application:** Warehouse Partners Web Dashboard
**Partner Type:** Warehouse Facilities
**Version:** 1.0
**Date:** 2026-02-14
**Path:** `shared-business-infrastructure/shared-warehousing-core/Frontends/Warehouse-Partners-Dashboard/`

---

## TABLE OF CONTENTS

1. [Overview](#overview)
2. [Application Entry FLOWs](#application-entry-FLOWs)
3. [Authentication FLOWs](#authentication-FLOWs)
4. [Dashboard Navigation FLOWs](#dashboard-navigation-FLOWs)
5. [Feature-Specific FLOWs](#feature-specific-FLOWs)
6. [Integration FLOWs](#integration-FLOWs)
7. [Exit FLOWs](#exit-FLOWs)

---

## 1. OVERVIEW

### 1.1 Domain Context

```
Gogidix ECOSYSTEM
|
|-- Foundation Domain (Shared Infrastructure)
|-- Management Domain (Executive, HR, Finance, etc.)
|-- Business Domain
|   |-- Logistics Subdomain
|   |   |-- Air Freight Core
|   |   |-- Ocean Shipping Core
|   |   |-- Courier Services Core
|   |   `-- Warehousing Core (This Document)
|   |       |
|   |       `-- Warehouse-Partners-Dashboard
|   |           |
|   |           `-- Purpose: Warehouse facility operations management
|   |           `-- Users: Warehouse Managers, Supervisors, Staff
|   |           `-- Scope: Dual facility management
|   |           `               - E-commerce fulfillment operations
|   |           `               - Personal storage management
|   |           `               - Staff operations & coordination
|   |           `               - Inbound/outbound processing
|   |           `               - Courier/e-commerce integrations
|   |
|   `-- Procurement Subdomain
|
`-- Data FLOW: Warehouse-Partners-Dashboard <-- E-commerce Vendors
                                   <-- Personal Storage Customers
                                   <-- Courier Services
                                   <-- Warehouse Staff Mobile App
```

### 1.2 User Roles & Entry Points

```
+-----------------------------------------------------------------------------+
|                    WAREHOUSE-PARTNERS-DASHBOARD USERS              |
+-----------------------------------------------------------------------------+
|                                                                              |
|   +------------------+  +------------------+  +------------------+            |
|   |Warehouse         |  |Operations        |  |Shift             |            |
|   |Manager           |  |Manager           |  |Supervisor        |            |
|   |                  |  |                  |  |                  |            |
│   |Full Warehouse   |  |Daily Operations  |  |Shift Oversight   |            |
│   |Management       |  |Oversight         |  |Staff Coordination |            |
│   |Access           |  |                  |  |                  |            |
│   |                  |  |Fulfillment &      |  |Staff Performance |            |
│   |Business Strategy |  |Inventory Mgmt     |  |Training          |            |
│   +------------------+  +------------------+  +------------------+            |
|                                                                              |
|   +------------------+                                                          |
|   |Finance Manager    |                                                          |
|   |                  |                                                          |
│   |Pricing & Revenue  |                                                          |
│   |Financial Reports  |                                                          |
│   +------------------+                                                          |
|                                                                              |
|   +------------------+  +------------------+                                       |
|   |Fulfillment        |  |Storage           |                                       |
|   |Coordinator       |  |Manager           |                                       |
|   |                  |  |                  |                                       |
|   |E-commerce        |  |Personal Storage   |                                       |
|   |Orders            |  |Customer Management|                                       |
|   +------------------+  +------------------+                                       |
+-----------------------------------------------------------------------------+
```

### 1.3 Dashboard Structure

```
Warehouse Partners Web Dashboard
|
+-- Overview Dashboard
|   |-- Operational Metrics
|   |-- Storage Utilization
|   |-- Staff Performance
|   |-- Revenue Snapshot
|   `-- Quick Actions
|
+-- Operations Management
|   |-- E-commerce Fulfillment
|   |   |-- Vendor Orders
|   |   |-- Order Processing
|   |   |-- Picking & Packing
|   |   |-- Fulfillment Status
|   |   `-- Returns Management
|   |-- Personal Storage Management
|   |   |-- Storage Units
|   |   |-- Customer Accounts
|   |   |-- Access Logs
|   |   |-- Billing
|   |   `-- Contract Management
|
+-- Inventory Management
|   |-- Stock Overview
|   |-- Inbound Processing
|   |-- Outbound Processing
|   |-- Stock Transfers
|   |-- Low Stock Alerts
|   |-- Cycle Counting
|   `-- Damage Reports
|
+-- Staff Management
|   |-- Staff Directory
|   |-- Staff Onboarding
|   |-- Shift Management
|   |-- Performance Tracking
|   |-- Attendance
|   |-- Task Assignment
|   `-- Training & Certifications
|
+-- Integrations Hub
|   |-- Courier Services Integration
|   |-- E-commerce Vendors Integration
|   |-- API Connections
|   `-- Webhook Management
|
+-- Facility Management
|   |-- Warehouse Profile
|   |-- Storage Spaces
|   |-- Equipment Management
|   |-- Maintenance Schedules
|   |-- Security & Access
|   `-- Expansion Planning
|
+-- Reports & Analytics
|   |-- Performance Reports
|   |-- Inventory Reports
|   |-- Fulfillment Reports
|   |-- Staff Productivity
|   |-- Financial Reports
|   `-- Custom Reports
|
`-- Settings
    |-- Company Profile
    |-- Facility Configuration
    |-- Pricing & Rates
    |-- Team Management
    |-- Notification Preferences
    |-- API Keys
```

---

## 2. APPLICATION ENTRY FLOWS

### 2.1 Main Entry FLOW

```
+--------+
|   User   |
|  Opens   |
|   App    |
+----+-------+
     |
     v
+-------------------------------------------------------------------------+
|                    CHECK AUTHENTICATION                                     |
|  +------------------------------------------------------------------+  |
|  |  Is user already logged in?                             |  |
|  |  (Check: localStorage.authToken, cookie)                   |  |
|  +------------------------------------------------------------------+  |
+----+----------------------------+-------------------------------+
     | YES                                                      | NO
     v                                                          v
+--------------+                                    +--------------+
|  RESTORE    |                                    |  LOGIN PAGE  |
|  SESSION    |                                    |              |
|  * Load     |                                    |  [See Section 3]|
|  user data |                                    +--------------+
|  Load       |
|  warehouse  |
|  facility  |
|  mode       |
|  * Navigate  |
|    to       |
|  facility   |
|  dashboard  |
+------+-----------+
     |
     v
+-------------------------------------------------------------------------+
|                        SELECT FACILITY MODE                             |
|  +------------------------------------------------------------------+  |
|  |  Select primary facility view to manage:                  |  |
|  +------------------------------------------------------------------+  |
+----+----------------------------+-------------------------------+
     | E-COMMERCE                 | PERSONAL STORAGE               |
     v                             v
+--------------+            +--------------+
|  E-commerce    |            |  Personal     |
|  Fulfillment   |            |  Storage      |
|  Dashboard   |            |  Dashboard    |
+--------------+            +--------------+
```

### 2.2 Warehouse Partner Registration FLOW

```
+-------------------------------------------------------------------------+
|                    NEW WAREHOUSE PARTNER REGISTRATION                |
|  Trigger: New warehouse facility signing up                             |
+----+--------------------------------------------------------------------+
     |
     v
+-------------------------------------------------------------------------+
|                        STEP 1: FACILITY INFO                          |
|  +------------------------------------------------------------------+  |
|  |  * Warehouse Name                                         |  |
|  |  * Business Type (LLC, Corp, Sole Prop, Partnership) |  |
|  |  * Registration Number                                    |  |
|  |  * Tax ID                                                 |  |
|  |  * Years in Operation                                     |  |
|  |  * Facility Size (sq meters)                              |  |
|  |  * Number of Storage Units                                 |  |
|  +------------------------------------------------------------------+  |
|                                     [Continue ->]                   |
+-------------------------------------------------------------------------+
     |
     v
+-------------------------------------------------------------------------+
|                        STEP 2: FACILITY CAPABILITIES                     |
|  +------------------------------------------------------------------+  |
|  |  Storage Capacity:                                         |  |
|  |  +--------------------------------------------------------+  |  |
|  |  |  Total Storage Space: _____ sq meters                 |  |  |
|  |  |  Number of Storage Units: _____                         |  |  |
|  |  +--------------------------------------------------------+  |  |
|  |                                                                 |  |
|  |  Facility Types Available:                                  |  |
|  |  +--------------------------------------------------------+  |  |
|  |  | [x] E-commerce Fulfillment                              |  |  |
|  |  | [x] Personal Storage                                    |  |  |
|  |  | [ ] Cold Storage                                      |  |  |
|  |  | [ ] Hazardous Materials                                |  |  |
|  |  | [ ] Document Storage                                  |  |  |
|  |  +--------------------------------------------------------+  |  |
|  +------------------------------------------------------------------+  |
|                                     [Continue ->]                   |
+-------------------------------------------------------------------------+
     |
     v
+-------------------------------------------------------------------------+
|                        STEP 3: LOCATION DETAILS                         |
|  +------------------------------------------------------------------+  |
|  |  Warehouse Address                                         |  |
|  |  * Street Address                                           |  |
|  |  * Landmark                                                 |  |
|  |  * City/Area                                                |  |
|  |  * State/Region                                             |  |
|  |  * GPS Coordinates (Auto-detected/Manual)                 |  |
|  +------------------------------------------------------------------+  |
|                                     [Continue ->]                   |
+-------------------------------------------------------------------------+
     |
     v
+-------------------------------------------------------------------------+
|                        STEP 4: EQUIPMENT & INFRASTRUCTURE             |
|  +------------------------------------------------------------------+  |
|  |  Available Equipment:                                       |  |
|  |  +--------------------------------------------------------+  |  |
|  |  | [x] Forklifts                                               |  |  |
|  |  | [x] Pallet Jacks                                            |  |  |
|  |  | [x] Conveyor Systems                                       |  |  |
|  |  | [x] Barcode Scanners                                        |  |  |
|  |  | [x] CCTV Cameras                                            |  |  |
|  |  | [x] Security Systems                                       |  |  |
|  |  | [x] Fire Safety Systems                                   |  |  |
|  |  | [ ] Climate Control                                       |  |  |
|  |  | [ ] 24/7 Security                                         |  |  |
|  |  +--------------------------------------------------------+  |  |
|  +------------------------------------------------------------------+  |
|                                     [Continue ->]                   |
+-------------------------------------------------------------------------+
     |
     v
+-------------------------------------------------------------------------+
|                        STEP 5: PRICING STRUCTURE                       |
|  +------------------------------------------------------------------+  |
|  |  Service Pricing                                             |  |
|  +--------------------------------------------------------+  |  |
|  |  E-commerce Fulfillment:                                |  |  |
|  |  +--------------------------------------------------------+  |  |
|  |  | Storage Fee: N____ per cubic meter per month       |  |  |
|  |  | Fulfillment Fee: N____ per order processed            |  |  |
|  |  | Picking Fee: N____ per item                          |  |  |
|  |  | Packing Fee: N____ per order                          |  |  |
|  |  +--------------------------------------------------------+  |  |
|  |                                                                 |  |
|  |  Personal Storage:                                         |  |
|  |  +--------------------------------------------------------+  |  |
|  |  | Storage Unit Fee: N____ per month                     |  |  |
|  |  | Small Unit: N____ (1-5 sq m)                         |  |  |
|  |  | Medium Unit: N____ (6-15 sq m)                       |  |  |
|  |  | Large Unit: N____ (16-30 sq m)                       |  |  |
|  |  +--------------------------------------------------------+  |  |
|  +------------------------------------------------------------------+  |
|                                     [Continue ->]                   |
+-------------------------------------------------------------------------+
     |
     v
+-------------------------------------------------------------------------+
|                        STEP 6: STAFF REQUIREMENTS                       |
|  +------------------------------------------------------------------+  |
|  |  Expected Staff Count: _____                                |  |
|  +--------------------------------------------------------+  |  |
|  |                                                                 |  |
|  |  Staff Roles to be Managed:                                  |  |
|  |  +--------------------------------------------------------+  |  |
|  |  | [x] Warehouse Supervisors                               |  |  |
|  |  | [x] Pickers                                                 |  |  |
|  |  | [x] Packers                                                 |  |  |
|  |  | [x] Inventory Coordinators                               |  |  |
|  |  | [x] Forklift Operators                                    |  |  |
|  |  | [x] Security Personnel                                   |  |  |
|  |  | [ ] Delivery Drivers                                     |  |  |
|  |  +--------------------------------------------------------+  |  |
|  +------------------------------------------------------------------+  |
|                                     [Continue ->]                   |
+-------------------------------------------------------------------------+
     |
     v
+-------------------------------------------------------------------------+
|                        STEP 7: BANK DETAILS                            |
|  +------------------------------------------------------------------+  |
|  |  Bank Account Details for Commission Payouts:                |  |
|  |  +--------------------------------------------------------+  |  |
|  |  |  * Bank Name                                           |  |  |
|  |  |  * Account Number                                      |  |  |
|  |  |  * Account Name                                        |  |  |
|  |  |  * Account Type (Savings, Current, etc.)                |  |  |
|  |  |  * BVN (Bank Verification Number)                       |  |  |
|  |  |  * Bank Sort Code                                       |  |  |
|  +--------------------------------------------------------+  |  |
|                                     [Continue ->]                   |
+-------------------------------------------------------------------------+
     |
     v
+-------------------------------------------------------------------------+
|                        STEP 8: ADMIN ACCOUNT SETUP                       |
|  +------------------------------------------------------------------+  |
|  |  Primary Admin Details:                                      |  |
|  +--------------------------------------------------------+  |  |
|  |  * Full Name                                                |  |  |
|  |  * Email Address                                            |  |  |
|  |  * Phone Number                                             |  |  |
|  |  * Password                                                |  |  |
|  |  * Confirm Password                                        |  |  |
|  +--------------------------------------------------------+  |  |
|                                     [Submit Application ->]      |
+-------------------------------------------------------------------------+
     |
     v
+-------------------------------------------------------------------------+
|                   APPLICATION SUBMITTED                                   |
|  +------------------------------------------------------------------+  |
|  |                                                                   |  |
|  |  [+] Application submitted successfully!                 |  |
|  |                                                                   |  |
|  |  Reference ID: WH-2024-XXXXX                                    |  |
|  |                                                                   |  |
|  |  What happens next:                                          |  |
|  |  * Our team will review your application within 3-5 business days|  |
|  |  * Site inspection may be scheduled                           |  |
|  |  * Equipment verification will be conducted                     |  |
|  |  * Once approved, you'll receive onboarding instructions      |  |
|  |                                                                   |  |
|  |  [Track Application Status]  [Return to Home]                 |  |  |
|  |                                                                   |  |
|  +------------------------------------------------------------------+  |
+-------------------------------------------------------------------------+
```

---

## 3. AUTHENTICATION FLOWS

### 3.1 Login FLOW

```
+-------------------------------------------------------------------------+
|                          LOGIN SCREEN                                |
+-------------------------------------------------------------------------+
|                                                                              |
|                    +-----------------+                                    |
|                    |   Gogidix       |                                    |
|                    |   Warehouse      |                                    |
|                    |   Partners     |                                    |
|                    |   Platform      |                                    |
|                    +-----------------+                                    |
|                                                                              |
|                    +-------------------------+                             |
|                    |  Email / Facility ID  |                             |
|                    +-------------------------+                             |
|                                                                              |
|                    +-------------------------+                             |
|                    |  Password    [eye]  |                             |
|                    +-------------------------+                             |
|                                                                              |
|                    [ ] Remember me                                        |
|                    Forgot password?                                       |
|                                                                              |
|                    +-------------------------+                             |
|                    |     SIGN IN        |                             |
|                    +-------------------------+                             |
|                                                                              |
|                    OR                                                       |
|                                                                              |
|                    [ SSO with Microsoft ]                                  |
|                    [ SSO with Google ]                                     |
|                                                                              |
|                    New warehouse partner? [Apply Now]                        |
|                                                                              |
+-------------------------------------------------------------------------+
     |
     | User enters credentials and clicks Sign In
     v
+-------------------------------------------------------------------------+
|                      VALIDATE CREDENTIALS                             |
|  +------------------------------------------------------------------+  |
|  |  POST /api/v1/warehouse-partners/auth/login                   |  |
|  |  Request: { email, password }                               |  |
|  |  Response: { token, user, role, warehouse, facilityMode }  |  |
|  +------------------------------------------------------------------+  |
+----+----------------------------+-------------------------------+
     | VALID                                                    | INVALID
     v                                                          v
+--------------+                                    +--------------+
|  Store Token |                                    |  Show Error:  |
|  Load User   |                                    |  "Invalid    |
|  Load Warehouse|                                    |  credentials" |
|  Check Status|                                    |  Allow retry  |
+------+-----------+                                    +--------------+
     |
     v
+-------------------------------------------------------------------------+
|                    SELECT FACILITY VIEW                             |
|  +------------------------------------------------------------------+  |
|  |  Choose primary facility view to manage:                   |  |
|  +------------------------------------------------------------------+  |
+----+----------------------------+-------------------------------+
     | E-COMMERCE                 | PERSONAL STORAGE               |
     v                             v
+--------------+            +--------------+
|  E-commerce    |            |  Personal     |
|  Fulfillment   |            |  Storage      |
|  Dashboard   |            |  Dashboard    |
+--------------+            +--------------+
```

---

## 4. DASHBOARD NAVIGATION FLOWS

### 4.1 Dual Facility Dashboard Navigation FLOW

```
                    +-------------+
                    │ WAREHOUSE    │
                    │   PARTNER    │
                    │   DASHBOARD  │
                    │    (Home)     │
                    +------+-------+
                             |
        +---------------+---------------+---------------+
        |               |               |               |
        v               v               v
+-------------+   +-------------+   +-------------+
| Sidebar Nav |   | Toggle      |   | Main Content|
|            |   | Facility    |   |             |
| Overview    |   | Mode Switch |   |             |
| E-commerce  |   | [E-commerce |   | Facility    |
| Fulfillment |   |  Personal]   |   | Stats       |
| Personal    |   +-------------+   | Staff Count  |
| Storage    |                      | Orders       |
| Staff      |   +-------------+   | Utilization |
| Inventory  |                      | Revenue      |
| Integrations|                      |             |
| Reports    |                      |             |
| Settings    |                      |             |
+-------------+                      |             |

                    SWITCHING FACILITY VIEWS:

                    +-------------------------+
                    | FACILITY MODE TOGGLE|
                    +-------------------------+
                    |
                    Current: E-commerce Fulfillment
                    Switch to: Personal Storage

                    OR

                    Current: Personal Storage
                    Switch to: E-commerce Fulfillment
```

### 4.2 Role-Based Navigation FLOW

```
+-------------------------------------------------------------------------+
|                      ROLE-BASED ACCESS CONTROL                        |
+-------------------------------------------------------------------------+

User authenticates
      |
      v
+-------------------------+
| Determine user role    |
+-------------------------+
| * Warehouse Manager - Full Access                       |
| * Operations Manager - Ops Access                         |
| * Shift Supervisor - Staff Oversight                        |
| * Fulfillment Coordinator - E-commerce Orders                 |
| * Storage Manager - Personal Storage                         |
| * Staff Member - Mobile App Access                           |
+-------------------------+
      |
      +-- Warehouse Manager ---------------------------------------------+
      |                                                                  v
      |                    +-------------------------+                       |
      |                    | Full Dashboard Access|                       |
      |                    +-------------------------+                       |
      |                    | * Overview               |                       |
      |                    | * E-commerce Fulfillment |                       |
      |                    | * Personal Storage        |                       |
      |                    | * Inventory Management   |                       |
      |                    | * Staff Management        |                       |
      |                    | * Courier Integration    |                       |
      |                    | * E-commerce Integration  |                       |
      |                    | * Reports & Analytics       |                       |
      |                    | * Settings (Full)          |                       |
      |                    +-------------------------+                       |
      |
      +-- Operations Manager -------------------------------------------+
      |                                                                  v
      |                    +-------------------------+                       |
      |                    | Operations Access         |                       |
      |                    +-------------------------+                       |
      |                    | * Overview (Ops metrics)   |                       |
      |                    | * E-commerce Orders (Full) |                       |
      |                    | * Inventory (Full)         |                       |
      |                    | * Staff (View/Edit)       |                       |
      |                    | * Fulfillment Coordination|                       |
      |                    | * Reports (Ops reports)    |                       |
      |                    +-------------------------+                       |
      |
      +-- Fulfillment Coordinator ---------------------------------------
      |                                                                  v
      |                    +-------------------------+                       |
      |                    | Fulfillment Coordinator   |                       |
      |                    +-------------------------+                       |
      |                    | * Vendor Orders             |                       |
      |                    | * Order Processing         |                       |
      |                    | * Picking & Packing         |                       |
      |                    | * Fulfillment Status        |                       |
      |                    | * Returns Management        |                       |
      |                    +-------------------------+                       |
      |
      +-- Storage Manager ----------------------------------------------
      |                                                                  v
      |                    +-------------------------+                       |
      |                    | Storage Manager           |                       |
      |                    +-------------------------+                       |
      |                    | * Personal Storage Units     |                       |
      |                    | * Customer Accounts         |                       |
      |                    | * Access Logs                |                       |
      |                    | * Billing                    |                       |
      |                    +-------------------------+                       |
```

---

## 5. FEATURE-SPECIFIC FLOWS

### 5.1 E-commerce Fulfillment FLOW

```
+-------------------------------------------------------------------------+
|                   E-COMMERCE FULFILLMENT - MAIN FLOW                 |
+-------------------------------------------------------------------------+

Warehouse dashboard receives e-commerce vendor order
      |
      v
+-------------------------------------------------------------------------+
|                    INCOMING VENDOR ORDER                            |
|  +------------------------------------------------------------------+  |
|  |  E-COMMERCE INTEGRATION - ORDER STREAM                     |  |
|  |  +--------------------------------------------------------+  |  |
|  |  |  Real-time orders from connected vendors           |  |  |
|  |  |  WebSocket: wss://api.Gogidix.com/v1/...    |  |  |
|  |  +--------------------------------------------------------+  |  |
|  +------------------------------------------------------------------+  |
|                                                                              |
|  +------------------------------------------------------------------+  |
|  |  FILTERS & SEARCH                                                 |  |
|  |  [Search by Order ID/Vendor] [Status v] [Priority v]  |  |  |
|  +------------------------------------------------------------------+  |
|                                                                              |
|  +------------------------------------------------------------------+  |
|  |  [Auto-Assign: ON]  [Auto-Process: ON]  [Sound: ON]       |  |  |
|  +------------------------------------------------------------------+  |
|                                                                              |
|  +------------------------------------------------------------------+  |
|  |  INCOMING ORDERS (45 new)                                       |  |  |
|  |  +-------------------------------------------------------------+  |  |
|  |  |  | ORD    │Vendor    │Items  │Priority│Status    │  |  |
|  |  ├────────┼──────────┼───────┼────────┼───────┤  |  |
|  |  │#ORD-45│TechNova   │3 items│Standard│New      │  │  |
|  |  │        │Ltd       │       │        │[Process]│  │  |
|  |  │        │          │       │        │[View]   │  │  |
|  |  ├────────┼──────────┼───────┼────────┼────────┤  │  |
|  |  │#ORD-46│BulkBuy    │50     │Express│Assigned│  │  |
|  |  │        │Nigeria  │items  │        │[Process]│  │  |
|  |  │        │          │       │        │        │  │  |
|  |  └-------------------------------------------------------------┘  |  |
|  +------------------------------------------------------------------+  |
+-------------------------------------------------------------------------+
      |
      | User clicks [Process] on Order #ORD-45
      v
+-------------------------------------------------------------------------+
|                    ORDER PROCESSING - ASSIGNMENT                       │
|  +------------------------------------------------------------------+  |
|  |  ORDER #ORD-45 - VENDOR ORDER PROCESSING                     |  |
|  |  +--------------------------------------------------------+  |  |
|  |  |  Order Details:                                         |  |  |
|  |  |  * Vendor: TechNova Nigeria Ltd                           |  |  |
|  |  |  * Vendor ID: EV-001                                     |  |  |
|  |  |  * Order Type: E-commerce Fulfillment                       |  |  |
|  |  *  * Items: 3 x Wireless Earbuds (SKU: WE-001-BLK)      |  |  |
|  |  |  *  * Total Items: 3                                        |  |  |
|  |  *  * Priority: Standard                                     |  |  |
|  |  *  * Storage Location: Shelf A-12, Zone B                    |  |  |
|  |  +--------------------------------------------------------+  |  |
|  |                                                                 |  |
|  |  Staff Assignment Options:                                    |  | |
|  |  +--------------------------------------------------------+  |  |
|  |  |  [ ] Auto-Assign to Available Staff                       |  |  |
|  |  |  [ ] Manual Assignment                                      |  |  |
|  |  +--------------------------------------------------------+  |  |
|  +------------------------------------------------------------------+  |
|                                     [Assign Staff ->]                   |
+-------------------------------------------------------------------------+
      |
      | User selects [Auto-Assign to Available Staff]
      v
+-------------------------------------------------------------------------+
|                    AVAILABLE STAFF                                          │
|  +------------------------------------------------------------------+  |
|  |  AVAILABLE PICKERS (5):                                         |  |
|  |  +--------+  +--------+  +--------+  +--------+   |  |  |
|  |  |Picker 1|  |Picker 2|  |Picker 3|  |Picker 4|  |Picker 5| |  |
|  │  |Emeka O.|  |Chidi A.|  |Tunde M.|  |Femi K. |  |Bayo S. |  |  |
|  │  |Zone A  |  |Zone A  |  |Zone B  |  |Zone C  |  |Zone A  |  |  |
|  │  |Idle   |  |On Duty |  |Idle   |  |On Duty |  |Idle   |  |  |
|  │  |[Assign]|  |[Assign]|  |[Assign]|  |[Assign]|  |[Assign]|  |  |
|  |  +--------+  +--------+  +--------+  +--------+   |  |  |
|  +------------------------------------------------------------------+  |
|                                     [Auto-Assign Best Picker]              │
+-------------------------------------------------------------------------+
      |
      | System selects Picker 2 (Chidi A.)
      v
+-------------------------------------------------------------------------+
|                    PICKING INSTRUCTIONS                                    │
|  +------------------------------------------------------------------+  |
|  |  [Check] Picker Assigned Successfully                            │  |  |
|  |                                                                 |  |
|  |  Picker: Chidi A. (Staff ID: STF-002)                        │  |  |
|  |  Order: ORD-45                                                 │  |  |
|  |  Items: 3 x WE-001-BLK (Wireless Earbuds)                      │  │
|  |  Location: Shelf A-12, Zone B                                  │  │  |
|  |                                                                 |  |
|  |  Picker has been notified via mobile app                         │  │  |  |
|  |                                                                 |  |
|  |  [View Order]  [Assign Another Order]  [Back to Queue]      │  │ |
|  +------------------------------------------------------------------+  |
+-------------------------------------------------------------------------+
```

### 5.2 Personal Storage Management FLOW

```
+-------------------------------------------------------------------------+
|                   PERSONAL STORAGE MANAGEMENT - MAIN FLOW                 │
+-------------------------------------------------------------------------+

Warehouse manager switches to Personal Storage view
      |
      v
+-------------------------------------------------------------------------+
|                    PERSONAL STORAGE DASHBOARD                           │
|  +------------------------------------------------------------------+  |
|  |  FACILITY MODE: Personal Storage                               |  |  |
|  +------------------------------------------------------------------+  |
|                                                                              |
|  +------------------------------------------------------------------+  |
|  |  STORAGE UNITS OVERVIEW                                       |  |  |
|  |  +-------------------------------------------------------------+  |  |
|  |  │ Total Units: 150                                         │  |  |
|  |  │ Occupied: 124 (83%)                                       │  |  |
|  |  │ Available: 26 (17%)                                        │  |  |
|  |  │ Revenue This Month: N312,000                             │  |  |
|  |  └-------------------------------------------------------------+  |  |
|  +------------------------------------------------------------------+  |
|                                                                              |
|  +------------------------------------------------------------------+  |
|  |  [View All Units]  [New Customer Registration]  [Access Logs]  |  |
|  +------------------------------------------------------------------+  |
+-------------------------------------------------------------------------+
      |
      | User clicks [View All Units]
      v
+-------------------------------------------------------------------------+
|                    STORAGE UNITS DIRECTORY                                │
|  +------------------------------------------------------------------+  |
|  |  FILTERS & SEARCH                                                 |  |
|  |  [Search by Customer Name] [Unit Number v] [Status v]    |  |  |
|  +------------------------------------------------------------------+  |  |
|                                                                              |
|  +------------------------------------------------------------------+  |
|  |  STORAGE UNITS (150 total)                                       |  |  |
|  |  +-------------------------------------------------------------+  |  |
|  |  │ UNIT   │ CUSTOMER        │ SIZE   │ STATUS │RENEWAL │ACTION│  |  |
|  │ ├────────┼───────────────┼───────┼────────┼──────┤  │  │
|  │ │PSU-001 │Amaka Okafor    │Small  │Active │2026-03│[View] │  │  │
|  │ │        │                │(1-5m) │[●]    │      │[Edit] │  │  │
|  │ ├────────┼───────────────┼───────┼────────┼──────┤  │  │
|  │ │PSU-002 │Ibrahim M.     │Medium │Active │2026-04│[View] │  │  │
|  │ │        │                │(6-15m)│[●]    │      │[Edit] │  │  │
|  │ ├────────┼───────────────┼───────┼────────┼──────┤  │  │
|  │ │PSU-003 │Femi Adeoye     │Large  │Active │2026-02│[View] │  │  │
| │ │        │                │(16-30m)│[●]    │      │[Edit] │  │  │
|  │ └────────┴───────────────┴───────┴────────┴──────┘  │  │
|  +------------------------------------------------------------------+  |
+-------------------------------------------------------------------------+
```

### 5.3 Inbound Package Processing FLOW

```
+-------------------------------------------------------------------------+
|                   INBOUND PACKAGE PROCESSING FLOW                         │
+-------------------------------------------------------------------------+

New packages arrive at warehouse (from vendor or courier)
      |
      v
+-------------------------------------------------------------------------+
|                    PACKAGE RECEIPT                                       │
|  +------------------------------------------------------------------+  |
|  |  DELIVERY NOTIFICATION:                                         |  |
|  |  +--------------------------------------------------------+  |  |
|  |  | [New Inbound Shipment]                                      |  |  |
|  |  |  Source: TechNova Nigeria Ltd / Courier Partner             |  |  |
|  |  |  |  Estimated Arrival: 10 minutes                           |  |  |
|  |  |  +--------------------------------------------------------+  |  |
|  +------------------------------------------------------------------+  |
+-------------------------------------------------------------------------+
     |
     | Staff member (receiving dock) opens mobile app
     v
+-------------------------------------------------------------------------+
|                    MOBILE APP - RECEIVING SCREEN                           │
│  +------------------------------------------------------------------+  |
│  │  [Check] New Incoming Shipment                            │  │
│  │                                                                 │  │
│  │  Source: TechNova Nigeria Ltd                              │  │
│  │  │  │
│  │  ┌─────────────────────────────────────────────────────────┐│ │
│  │  │ [Scan Barcode/QR] [Enter Tracking Number Manually]      │ │ │
│  │  └─────────────────────────────────────────────────────────┘│ │
│  +------------------------------------------------------------------+  |
+-------------------------------------------------------------------------+
     |
     | Staff scans package barcode
     v
+-------------------------------------------------------------------------+
|                    PACKAGE VERIFIED                                       │
│  +------------------------------------------------------------------+  │
│  │  [Package Verified]                                            │  │
│  │                                                                 │  │
│  │  Package ID: PKG-2024-XXXXX                                   │  │
│  │  Tracking: TRK-123456789                                       │  │
│  │  Contents: 3 x Wireless Earbuds                               │  │
│  │  │  │
│  │  VENDOR: TechNova Nigeria Ltd (EV-001)                      │  │
│  │  │  │
│  │  [Confirm Receipt] [Report Issue]                            │  │  │
│  +------------------------------------------------------------------+  │
+-------------------------------------------------------------------------+
     |
     | Staff confirms receipt
     v
+-------------------------------------------------------------------------+
|                    STORAGE ASSIGNMENT                                     │
│  +------------------------------------------------------------------+  │
│  │  Storage Location:                                         │  │
│  │  +--------------------------------------------------------+  │  │
│  │  | [ ] Auto-assign based on category                      │ │ │
│  │  | [ ] Manual assignment                                      │ │ │
│  │  +--------------------------------------------------------+  │ │
│  │                                                                 │  │
│  │  Suggested Location: Zone B - Shelf 15                         │  │ │
│  │                                                                 │  │
│  │  [Confirm Storage]                                             │  │  │
│  +------------------------------------------------------------------+  │
+-------------------------------------------------------------------------+
     |
     v
+-------------------------------------------------------------------------+
|                    PACKAGE STORED - AWAITING PUT-AWAY                          │
│  +------------------------------------------------------------------+  │
│  │  [Package Stored Successfully]                                   │  │
│  │                                                                 │  │
│  │  Package: PKG-2024-XXXXX                                   │  │
│  │  Location: Zone B - Shelf 15                                 │  │
│  │  │  │
│  │  Next: Wait for vendor/customer pickup or courier pickup │  │
│  │  │  │
│  │  [View Package]  [Process Another Package]                  │  │  │
│  +------------------------------------------------------------------+  │
+-------------------------------------------------------------------------+
```

### 5.4 Outbound Fulfillment FLOW

```
+-------------------------------------------------------------------------+
|                   OUTBOUND FULFILLMENT FLOW                              │
+-------------------------------------------------------------------------+

E-commerce vendor order ready for fulfillment
      |
      v
+-------------------------------------------------------------------------+
|                    ORDER PICKING REQUEST                                   │
|  +------------------------------------------------------------------+  │
│  |  ORDER: ORD-2024-XXXXX                                    │  │
│  │  ┌--------------------------------------------------------+  │  │
│  │  │  |  Order ID: ORD-45                     │  │  │
│  │  │  |  Customer: Femi Adeoye                 │  │  │
│  │  │  │  Items: 2 x WE-001-BLK (Wireless Earbuds)│  │  │
│  │  │  │  │  │  │  │
│  │  │  └--------------------------------------------------------+  │  │
│  +------------------------------------------------------------------+  │
|                                     [Generate Pick Ticket ->]     │
+-------------------------------------------------------------------------+
     |
     v
+-------------------------------------------------------------------------+
|                    PICK TICKET GENERATED                                   │
│  +------------------------------------------------------------------+  │
│  │  [Check] Pick Ticket Generated                                 │  │
│  │                                                                 │  │
│  │  Pick Ticket: PT-2024-XXXXX                                  │  │
│  │  Priority: Standard                                          │  │
│  │  Items: 2 x WE-001-BLK                                      │  │
│  │  Location: Zone A - Shelf 12                                 │  │  │
│  │  │  │
│  │  Available Pickers (3):                                         │  │
│  │  ┌--------+  +--------+  +--------+                          │  │
│  │  │Picker 1│  │Picker 2│  │Picker 3│                          │  │
│  │  │Emeka O.│  │Chidi A.│  │Tunde M.│                          │  │
│  │  │Zone A  │  │Zone A  │  │Zone B  │                          │  │
│  │  │Idle   │  │On Duty│  │Idle   │                          │  │
│  │  │[Assign]│  │[Assign]│  │[Assign]│                          │  │
│  │  └--------+  └--------+  └--------+                          │  │
│  +------------------------------------------------------------------+  │
|                                     [Auto-Assign Best Picker]      │ │
+-------------------------------------------------------------------------+
     |
     | Picker (Chidi A.) receives notification on mobile app
     v
+-------------------------------------------------------------------------+
|                    PICKER MOBILE APP - PICKING TASK                             │
│  +------------------------------------------------------------------+  │
│  │  [New Picking Task]                                         │  │
│  │  │  │
│  │  Pick Ticket: PT-2024-XXXXX                                  │  │
│  │  Priority: Standard                                            │  │
│  │  │  │
│  │  ┌────────────────────────────────────────────────────────┐│ │
│  │  │ Items to Pick:                                          │ │ │
│  │  │ ┌────────────────────────────────────────────────────┐│ │ │
│  │  │ │ • 2 x WE-001-BLK (Wireless Earbuds)                │ │ │ │
│  │  │ │ Location: Zone A - Shelf 12                         │ │ │ │
│  │  │ └────────────────────────────────────────────────────┘│ │ │
│  │  └────────────────────────────────────────────────────────┘│ │ │
│  +------------------------------------------------------------------+  │
│                                                                     │ │
│  │  ┌────────────────────────────────────────────────────────┐│ │ │
│  │  │ [Scan Item Barcode]  [Manual Confirm]                 │ │ │
│  │  └────────────────────────────────────────────────────────┘│ │ │
│  +------------------------------------------------------------------+ │
+-------------------------------------------------------------------------+
     |
     | Picker scans item barcode
     v
+-------------------------------------------------------------------------+
|                    ITEM CONFIRMED                                         │
│  +------------------------------------------------------------------+  │
│  │  [Item Confirmed]                                            │  │
│  │  │  │
│  │  Item: WE-001-BLK                                             │  │  │
│  │  │  │
│  │  Scanned Quantity: 1                                      │  │  │
│  │  Required: 2                                               │  │  │
│  │  │  │
│  │  [Scan Next Item]                                            │  │ │
│  +------------------------------------------------------------------+  │
+-------------------------------------------------------------------------+
     |
     | Picker confirms all items
     v
+-------------------------------------------------------------------------+
|                    PACKING STAGE                                         │
│  +------------------------------------------------------------------+  │
│  │  [All Items Confirmed]                                         │  │
│  │  │  │
│  │  Proceed to Packing Station: Zone B - Packing Table 3     │  │ │
│  │  │  │
│  │  [Confirm Ready to Pack]                                   │  │ │
│  +------------------------------------------------------------------+ │
+-------------------------------------------------------------------------+
     |
     | Item moves to packing station
     v
+-------------------------------------------------------------------------+
|                    ORDER COMPLETED                                         │
│  +------------------------------------------------------------------+  │
│  │  [Check] Order Fulfillment Completed                            │  │  │
│  │  │  │
│  │  Order: ORD-2024-XXXXX                                       │  │ │
│  │  │  │
│  │  Items picked and packed, ready for courier pickup       │  │ │
│  │  │  │
│  │  [Generate Shipping Label]  [Request Courier Pickup]  │  │ │
│  +------------------------------------------------------------------+ │
+-------------------------------------------------------------------------+
```

---

## 6. INTEGRATION FLOWS

### 6.1 E-commerce Vendor Integration FLOW

```
+-----------------------+                                    +-----------------------+
|  E-commerce      |                                    |  Warehouse      |
|  Vendors        |                                    |  Partners      |
|  Dashboard       |                                    |  Dashboard     |
+---------+-----------+                                    +-----------+-----------+
          |                                                          |
          | 1. Real-time Inventory Sync                             |
          +--------------------------------------------------------->|
          |    POST /api/v1/warehouse-partners/inventory/sync        |
          |    {vendorId, productId, quantity, location}        |
          |                                                          |
          |2. Order Fulfillment Request                            |
          +--------------------------------------------------------->|
          |    POST /api/v1/warehouse-partners/orders/fulfillment     |
          |    {orderId, vendorId, items, deliveryAddress}        |
          |                                                          |
          |3. Fulfillment Status Updates                            |
          +--------------------------------------------------------->|
          |    PUT /api/v1/warehouse-partners/orders/{id}/status      |
          |                                                          |
          |4. Pickup Scheduling                                       |
          +<---------------------------------------------------------|
          |    Courier scheduled for pickup from warehouse            |
          |                                                          |
          |5. Delivery Confirmation                                     |
          +--------------------------------------------------------->|
          |    Package delivered, signature collected                  |
          |                                                          |
          |6. Inventory Updated                                        |
          |<---------------------------------------------------------|
          |    Stock levels updated in vendor dashboard
```

### 6.2 Courier Services Integration FLOW

```
+-----------------------+                                    +-----------------------+
|  Courier         |                                    |  Warehouse      |
|  Services       |                                    |  Partners      |
|  Dashboard       |                                    |  Dashboard     |
+---------+-----------+                                    +-----------+-----------+
          |                                                          |
          | 1. Pickup Request from Warehouse                           |
          +--------------------------------------------------------->|
          |    POST /api/v1/courier-services/warehouse/pickup     |
          |    {warehouseId, packages, pickupLocation}           |
          |                                                          |
          |2. Courier Assignment                                     |
          |<---------------------------------------------------------|
          |    Courier assigned for pickup                            |
          |                                                          |
          |3. Package Handover                                         |
          +--------------------------------------------------------->|
          |    Warehouse staff hands packages to courier              |
          |    PUT /api/v1/courier-services/warehouse/handover     |
          |                                                          |
          |4. Delivery Tracking                                       |
          |<---------------------------------------------------------|
          |    Real-time tracking updates                           |
          |                                                          |
          |5. Delivery Confirmation                                     |
          +--------------------------------------------------------->|
          |    Delivery completed, signed POD                           |
          |                                                          |
          |6. Fulfillment Complete                                      |
          |<---------------------------------------------------------|
          |    Order fulfillment updated in warehouse dashboard         |
```

---

## 7. EXIT FLOWS

### 7.1 Logout FLOW

```
+-------------------------------------------------------------------------+
|                       LOGOUT INITIATED                              |
|  Trigger: User clicks logout from user menu                             |
+----+--------------------------------------------------------------------+
     |
     v
+-------------------------------------------------------------------------+
|                      CONFIRM LOGOUT                                   |
|  +------------------------------------------------------------------+  |
|  |  Are you sure you want to logout?                            |  |
|  |  Unsaved changes may be lost.                                |  |
|  |                                                                 |  |
|  |  [Cancel]  [Logout]                                       |  |
|  +------------------------------------------------------------------+  |
+----+-------------------------------+---------------------------+
     | Cancel                   | Confirm Logout
     v                          v
+--------------+      +-----------------------------------------------------+
|  Return to    |      |              CLEAR SESSION                    |
|  Dashboard  |      |  * Call POST /api/v1/warehouse-partners/auth/logout  |
|              |      |  * Clear localStorage (token, user, warehouse)    |
|              |      |  * Clear sessionStorage                         |
|              |      |  * Clear cookies                                 |
|              |      |  * Close WebSocket connections                  |
|              |      |  * Navigate to Login screen                      |
|              |      +-----------------------------------------------------+
+--------------+
```

---

## END OF DOCUMENTATION

**Document Version:** 1.0
**Last Updated:** 2026-02-14
**Next Review:** 2026-03-14

**Key Features:**
- Dual facility management (E-commerce Fulfillment + Personal Storage)
- Staff management with mobile app integration
- Complete partner onboarding with 8 steps
- Inbound/outbound package processing
- Courier services integration
- E-commerce vendor integration for fulfillment

**Related Documents:**
- 02_Wireframes_Documentation.md
- 03_Mock_Flow_Documentation.md
- 04_Page_By_Page_Flow_Documentation.md

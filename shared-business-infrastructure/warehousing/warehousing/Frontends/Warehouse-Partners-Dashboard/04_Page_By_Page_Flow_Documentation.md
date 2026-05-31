# Warehouse Partners Dashboard - Page By Page Flow Documentation

## Overview

This document provides detailed page-by-page specifications for the Warehouse Partners Dashboard, covering all screens for both E-commerce Fulfillment and Personal Storage facility modes.

---

## Table of Contents

1. [Authentication Pages](#1-authentication-pages)
2. [Onboarding Pages](#2-onboarding-pages)
3. [Dashboard Pages](#3-dashboard-pages)
4. [E-commerce Fulfillment Pages](#4-ecommerce-fulfillment-pages)
5. [Personal Storage Pages](#5-personal-storage-pages)
6. [Inventory Management Pages](#6-inventory-management-pages)
7. [Staff Management Pages](#7-staff-management-pages)
8. [Courier Integration Pages](#8-courier-integration-pages)
9. [Vendor Integration Pages](#9-vendor-integration-pages)
10. [Settings Pages](#10-settings-pages)
11. [Mobile App Pages](#11-mobile-app-pages)

---

## 1. Authentication Pages

### 1.1 Login Page

**URL**: `/warehouse/login`

**Purpose**: Authenticate warehouse partners and provide facility mode selection

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────┐
│                              [Gogidix Logo]                         │
│                        Warehouse Partners Portal                     │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│                           Welcome Back                              │
│                                                                     │
│  Email Address                                                      │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ user@warehousepartner.com                                    │ │
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  Password                                                           │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ ••••••••••••••••                                             │ │
│  └─────────────────────────────────────────────────────────────────┘│
│                                                  [Forgot Password?] │
│                                                                     │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │                        [ Login ]                                ││
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  Don't have an account? [Register your warehouse]                   │
│                                                                     │
│                        © 2026 Gogidix                               │
└─────────────────────────────────────────────────────────────────────┘
```

**Form Fields**:
| Field | Type | Required | Validation |
|-------|------|----------|------------|
| Email | Email | Yes | Valid email format, registered partner |
| Password | Password | Yes | Minimum 8 characters |

**Actions**:
- **Login**: Validates credentials and redirects to facility mode selection
- **Forgot Password**: Redirects to password reset flow
- **Register**: Redirects to warehouse registration page

**Error States**:
- Invalid credentials: "Invalid email or password. Please try again."
- Account inactive: "Your account is pending approval. Contact support for assistance."
- Multiple failed attempts: "Too many failed attempts. Please reset your password."

---

### 1.2 Facility Mode Selection Page

**URL**: `/warehouse/select-mode`

**Purpose**: Allow warehouse partners with dual facilities to select which mode to access

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────┐
│                              [Gogidix Logo]                         │
│                      Select Facility Mode                           │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  Welcome, [Warehouse Name]                                          │
│  Please select the facility mode you want to access:                │
│                                                                     │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ ┌─────────────────────────────────────────────────────────────┐ ││
│  │ │                                                             │ ││
│  │ │   [Box Icon]                                    [Box Icon]  │ ││
│  │ │                                                             │ ││
│  │ │         E-commerce                  Personal Storage         │ ││
│  │ │       Fulfillment Center               Facility             │ ││
│  │ │                                                             │ ││
│  │ │   Manage vendor orders,          Manage storage units,      │ ││
│  │ │   inventory, and staff            customer accounts,        │ ││
│  │ │   for order fulfillment           and facility access        │ ││
│  │ │                                                             │ ││
│  │ │                  [ Select Mode ]         [ Select Mode ]    │ ││
│  │ └─────────────────────────────────────────────────────────────┘ ││
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  Remember my preference  ☑                                         │
│                                                                     │
│                        © 2026 Gogidix                               │
└─────────────────────────────────────────────────────────────────────┘
```

**Behavior**:
- If warehouse has only one facility type, skip this page
- "Remember my preference" stores mode in browser storage for 30 days
- Selection redirects to appropriate dashboard

---

### 1.3 Password Reset Page

**URL**: `/warehouse/reset-password`

**Purpose**: Allow warehouse partners to reset forgotten passwords

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────┐
│                              [Gogidix Logo]                         │
│                        Reset Your Password                          │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  Enter your registered email address and we'll send you a          │
│  secure link to reset your password.                                │
│                                                                     │
│  Email Address                                                      │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ user@warehousepartner.com                                    │ │
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │                   [ Send Reset Link ]                           ││
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  [Back to Login]                                                    │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

**States**:
1. **Initial**: Display email input form
2. **Submitting**: Show loading spinner
3. **Success**: "Check your email for the reset link. Link expires in 1 hour."
4. **Error**: "Email not found. Please check and try again."

---

## 2. Onboarding Pages

### 2.1 Registration Landing Page

**URL**: `/warehouse/register`

**Purpose**: Entry point for new warehouse partner registration

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────┐
│                              [Gogidix Logo]                         │
│                    Become a Gogidix Warehouse Partner               │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  Join our network of professional warehouse facilities and          │
│  unlock new revenue streams:                                        │
│                                                                     │
│  • E-commerce Fulfillment - Store and fulfill orders for            │
│    thousands of Gogidix vendors                                     │
│  • Personal Storage - Offer secure self-storage to customers        │
│  • Or Both - Maximize your facility utilization                     │
│                                                                     │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │                        [ Get Started ]                           ││
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  Already a partner? [Login here]                                    │
│                                                                     │
│                        © 2026 Gogidix                               │
└─────────────────────────────────────────────────────────────────────┘
```

---

### 2.2 Step 1: Facility Information

**URL**: `/warehouse/register/step-1`

**Purpose**: Collect basic facility details

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────┐
│                          Warehouse Registration                      │
│  Step 1 of 8: Facility Information                    ████░░░░░░░░░░  │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  Tell us about your facility:                                       │
│                                                                     │
│  Facility Name *                                                    │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ Example: Lagos Central Warehouse                              │ │
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  Facility Type *                                                    │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ E-commerce Fulfillment Center    │                              ││
│  │ Personal Storage Facility         │                              ││
│  │ Both (Dual Facility)              │                              ││
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  Facility Size (sq ft) *                                           │
│  ┌───────────────────────────┐                                      │
│  │ 15,000                   │                                      │
│  └───────────────────────────┘                                      │
│                                                                     │
│  Year Established *                                                 │
│  ┌───────────────────────────┐                                      │
│  │ 2019                     │                                      │
│  └───────────────────────────┘                                      │
│                                                                     │
│  Description                                                       │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ Describe your facility, capabilities, and operations...       │ │
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│           [ Previous ]              [ Continue ]                    │
└─────────────────────────────────────────────────────────────────────┘
```

**Form Fields**:
| Field | Type | Required | Validation |
|-------|------|----------|------------|
| Facility Name | Text | Yes | 3-100 characters |
| Facility Type | Select | Yes | Must select at least one |
| Facility Size | Number | Yes | 500-500000 sq ft |
| Year Established | Number | Yes | 1900-current year |
| Description | Textarea | No | Max 500 characters |

---

### 2.3 Step 2: Facility Capabilities

**URL**: `/warehouse/register/step-2`

**Purpose**: Define facility operational capabilities

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────┐
│                          Warehouse Registration                      │
│  Step 2 of 8: Facility Capabilities                  ██████░░░░░░░░  │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  What services can your facility provide?                           │
│                                                                     │
│  Storage Capabilities                                               │
│  ☑ Pallet Storage      ☑ Bin/Shelf Storage   ☑ Cold Storage       │
│  ☑ Hazardous Storage   ☑ Fragile Item Storage ☑ Bulk Storage      │
│                                                                     │
│  Fulfillment Capabilities (if applicable)                           │
│  ☑ Pick and Pack       ☑ Kit Assembly       ☑ Returns Processing  │
│  ☑ Labeling            ☑ Quality Control     ☑ Gift Wrapping      │
│                                                                     │
│  Equipment Available                                                │
│  ☑ Forklifts           ☑ Pallet Jacks       ☑ Conveyor Systems   │
│  ☑ Barcode Scanners    ☑ Dock Levelers      ☑ Loading Docks      │
│                                                                     │
│  Special Features                                                   │
│  ☑ Climate Control     ☑ 24/7 Security      ☑ Fire Suppression   │
│  ☑ CCTV Monitoring     ☑ Pest Control       ☑ Backup Power       │
│                                                                     │
│  Operating Hours                                                    │
│  Days: [☑ Mon] [☑ Tue] [☑ Wed] [☑ Thu] [☑ Fri] [☑ Sat] [☑ Sun]   │
│  Hours: [06:00 AM] to [10:00 PM]                                   │
│                                                                     │
│           [ Previous ]              [ Continue ]                    │
└─────────────────────────────────────────────────────────────────────┘
```

**Validation**:
- At least 3 capabilities must be selected
- Operating hours must be valid time range
- At least one operating day must be selected

---

### 2.4 Step 3: Location Details

**URL**: `/warehouse/register/step-3`

**Purpose**: Capture facility location and service area

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────┐
│                          Warehouse Registration                      │
│  Step 3 of 8: Location Details                      ████████░░░░░░  │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  Facility Location                                                  │
│                                                                     │
│  Address Line 1 *                                                   │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ 123, Warehouse Road, Ikeja                                    │ │
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  Address Line 2                                                     │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │                                                              │ │
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  City *                  State *                   Country *       │
│  ┌──────────────┐       ┌──────────────┐        ┌──────────────┐  │
│  │ Lagos        │       │ Lagos        │        │ Nigeria      │  │
│  └──────────────┘       └──────────────┘        └──────────────┘  │
│                                                                     │
│  Postal Code *                                                      │
│  ┌───────────────────────────┐                                      │
│  │ 100001                   │                                      │
│  └───────────────────────────┘                                      │
│                                                                     │
│  Service Area (radius for customer pickups, etc.)                   │
│  Radius: [25] km                                                   │
│                                                                     │
│  [Verify Address on Map]                                            │
│                                                                     │
│           [ Previous ]              [ Continue ]                    │
└─────────────────────────────────────────────────────────────────────┘
```

---

### 2.5 Step 4: Equipment & Infrastructure

**URL**: `/warehouse/register/step-4`

**Purpose**: Detail facility equipment and infrastructure

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────┐
│                          Warehouse Registration                      │
│  Step 4 of 8: Equipment & Infrastructure            ██████████░░░░  │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  Loading Infrastructure                                             │
│                                                                     │
│  Number of Loading Docks *                                          │
│  ┌───────────────────────────┐                                      │
│  │ 6                        │                                      │
│  └───────────────────────────┘                                      │
│                                                                     │
│  Dock Type                                                          │
│  ☑ Ground Level    ☑ Elevated     ☑ Enclosed                      │
│                                                                     │
│  Storage Infrastructure                                             │
│                                                                     │
│  Total Storage Capacity *                                           │
│  ┌───────────────────────────┐                                      │
│  │ 15,000                   │                                      │
│  └───────────────────────────┘                                      │
│  pallet positions / storage units                                  │
│                                                                     │
│  Storage Zones                                                      │
│  Zone A: [General Merchandise ] Size: [5000] sq ft                 │
│  Zone B: [Electronics         ] Size: [3000] sq ft                 │
│  [+ Add another zone]                                              │
│                                                                     │
│  Equipment Inventory                                                │
│  Equipment Type              Quantity        Condition             │
│  ┌─────────────────────────┬─────────────┬───────────────────────┐ │
│  │ Forklift              │ 3          │ [Good ▼]            │ │
│  ├─────────────────────────┼─────────────┼───────────────────────┤ │
│  │ Pallet Jack           │ 8          │ [Good ▼]            │ │
│  ├─────────────────────────┼─────────────┼───────────────────────┤ │
│  │ [+ Add Equipment]      │            │                      │ │
│  └─────────────────────────┴─────────────┴───────────────────────┘ │
│                                                                     │
│           [ Previous ]              [ Continue ]                    │
└─────────────────────────────────────────────────────────────────────┘
```

---

### 2.6 Step 5: Pricing Structure

**URL**: `/warehouse/register/step-5`

**Purpose**: Define pricing for services offered

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────┐
│                          Warehouse Registration                      │
│  Step 5 of 8: Pricing Structure                    ████████████░░  │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  E-commerce Fulfillment Pricing (if applicable)                     │
│                                                                     │
│  Storage Rates (per pallet/month)                                   │
│  Small Pallet:  ₦[1,500]        Medium Pallet:  ₦[2,000]          │
│  Large Pallet:  ₦[2,500]                                            │
│                                                                     │
│  Fulfillment Fees (per order)                                       │
│  Standard Pick & Pack:  ₦[500]                                     │
│  Express Pick & Pack:    ₦[750]                                    │
│  Returns Processing:     ₦[300]                                    │
│                                                                     │
│  Personal Storage Pricing (if applicable)                           │
│                                                                     │
│  Unit Type                Monthly Rate    Deposit                  │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ Small (1.5m x 2m)    │ ₦[15,000]   │ ₦[15,000]              ││
│  ├─────────────────────────────────────────────────────────────────┤│
│  │ Medium (2m x 3m)     │ ₦[25,000]   │ ₦[25,000]              ││
│  ├─────────────────────────────────────────────────────────────────┤│
│  │ Large (3m x 4m)      │ ₦[40,000]   │ ₦[40,000]              ││
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  Additional Services                                                │
│  ☑ Climate Control Surcharge:    +20%                              │
│  ☑ 24/7 Access Surcharge:        +15%                              │
│  ☑ Insurance Coverage:           ₦500/month                        │
│                                                                     │
│           [ Previous ]              [ Continue ]                    │
└─────────────────────────────────────────────────────────────────────┘
```

---

### 2.7 Step 6: Staff Requirements

**URL**: `/warehouse/register/step-6`

**Purpose**: Define staffing needs and primary contacts

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────┐
│                          Warehouse Registration                      │
│  Step 6 of 8: Staff Requirements                    █████████████░  │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  Current Staffing                                                   │
│                                                                     │
│  Management Staff                                                   │
│  Warehouse Manager: [1]    Operations Manager: [1]                 │
│  Shift Supervisors: [3]                                            │
│                                                                     │
│  Operational Staff                                                 │
│  Pickers/Packers: [12]    Receiving Staff: [4]                    │
│  Facility Staff: [3]                                               │
│                                                                     │
│  Primary Contact Person *                                           │
│  Full Name *                                                        │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ Sarah Mitchell                                               │ │
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  Position *                                                         │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ Warehouse Manager                                            │ │
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  Phone *                                                            │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ +234 801 234 5678                                            │ │
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  Email *                                                            │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ sarah.mitchell@warehousepartner.com                           │ │
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│           [ Previous ]              [ Continue ]                    │
└─────────────────────────────────────────────────────────────────────┘
```

---

### 2.8 Step 7: Bank Details

**URL**: `/warehouse/register/step-7`

**Purpose**: Collect payment information for earnings/payouts

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────┐
│                          Warehouse Registration                      │
│  Step 7 of 8: Bank Details                          ██████████████░ │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  Bank Account Information for Payments                              │
│                                                                     │
│  Account Holder Name *                                              │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ Lagos Central Warehouse Ltd                                   │ │
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  Bank Name *                                                        │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ [Select Bank...]                                              │ │
│  │ • Access Bank                                                   ││
│  │ • First Bank                                                    ││
│  │ • GTBank                                                        ││
│  │ • UBA                                                           ││
│  │ • Zenith Bank                                                   ││
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  Account Number *                                                   │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ 0123456789                                                    │ │
│  └─────────────────────────────────────────────────────────────────┘│
│                                                  [Verify Account]   │
│  ✓ Account Name Verified: Lagos Central Warehouse Ltd             │
│                                                                     │
│  BVN (Bank Verification Number) *                                   │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ 12345678901                                                   │ │
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  Tax Information                                                    │
│  Tax ID: [TIN-123456789-0001]                                      │
│                                                                     │
│           [ Previous ]              [ Continue ]                    │
└─────────────────────────────────────────────────────────────────────┘
```

**Security Notes**:
- All bank details are encrypted in transit and at rest
- BVN is used for identity verification only
- Account name verification uses bank API

---

### 2.9 Step 8: Admin Account Setup

**URL**: `/warehouse/register/step-8`

**Purpose**: Create primary admin account for the facility

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────┐
│                          Warehouse Registration                      │
│  Step 8 of 8: Admin Account Setup                    ███████████████│
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  Create your administrator account                                  │
│                                                                     │
│  Your Information                                                   │
│  First Name *                                                       │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ Sarah                                                       │ │
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  Last Name *                                                        │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ Mitchell                                                    │ │
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  Email Address * (This will be your username)                       │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ sarah.mitchell@warehousepartner.com                           │ │
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  Phone Number *                                                     │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ +234 801 234 5678                                            │ │
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  Create Password *                                                  │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ ••••••••••••••••                                             │ │
│  └─────────────────────────────────────────────────────────────────┘│
│  Must be at least 8 characters with uppercase, lowercase, & number │
│                                                                     │
│  Confirm Password *                                                 │
│  ┌─────────────────────────────────────────────────────────────────┐│
│  │ ••••••••••••••••                                             │ │
│  └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│  ☑ I agree to the Gogidix Warehouse Partner Terms of Service       │
│  ☑ I agree to the Privacy Policy                                   │
│                                                                     │
│           [ Previous ]              [ Submit Registration ]         │
└─────────────────────────────────────────────────────────────────────┘
```

**Success State**:
```
┌─────────────────────────────────────────────────────────────────────┐
│                        Registration Submitted!                      │
│                                                                     │
│  Thank you for registering as a Gogidix Warehouse Partner!         │
│                                                                     │
│  Your application (Ref: WH-2026-0042) is being reviewed.           │
│  This typically takes 1-2 business days.                            │
│                                                                     │
│  You will receive an email at:                                     │
│  sarah.mitchell@warehousepartner.com                               │
│                                                                     │
│  What happens next:                                                │
│  1. Our team will verify your facility details                     │
│  2. We may contact you to schedule a facility inspection           │
│  3. Once approved, you'll receive onboarding instructions          │
│                                                                     │
│  Reference Number: WH-2026-0042                                     │
│                                                                     │
│  [Track Application Status]  [Return to Home]                       │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 3. Dashboard Pages

### 3.1 E-commerce Fulfillment Dashboard

**URL**: `/warehouse/dashboard/fulfillment`

**Purpose**: Main operational hub for e-commerce fulfillment operations

**Page Layout**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [☰] Gogidix Fulfillment          [Facility Mode Toggle] [🔔] [👤]           │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                         Search │
│ ┌───────────────────────────────────────────────────────────────────────────┐ │
│ │ Lagos Central Warehouse - E-commerce Fulfillment         Feb 15, 2026    │ │
│ │ 6:00 AM - 10:00 PM                                      18/24 staff on   │ │
└───────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌────────────┐ ┌────────────┐ ┌────────────┐ ┌────────────┐ ┌────────────┐ │
│ │ Pending    │ │ Ready to   │ │ Awaiting   │ │ Active    │ │ Pickups    │ │
│ │ Orders     │ │ Pick       │ │ Pack       │ │ Staff     │ │ Scheduled  │ │
│ │     47     │ │     23     │ │     15     │ │   18/24   │ │     5      │ │
│ │  ↑ 12      │ │  8 assign  │ │  7 packing│ │  6 shift  │ │  Next: 9AM │
│ │ [View All] │ │ [Assign]   │ │ [View]    │ │ [View]    │ │ [Schedule] │ │
│ └────────────┘ └────────────┘ └────────────┘ └────────────┘ └────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Activity Timeline                                          [View All]   │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ 07:28  New order from TechZone Electronics                      [GO-04723]│ │
│ │ 07:25  Pick ticket completed by Emmanuel Adebayo                           │ │
│ │ 07:22  Package received from FashionHub Nigeria                  [PKG-1023]│ │
│ │ 07:15  Grace Okonkwo clocked in                                            │ │
│ │ 07:10  Order GO-04720 marked "Awaiting Pickup"                             │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌───────────────────────────────────────┐ ┌───────────────────────────────┐ │
│ │ Today's Performance                   │ │ Pending Orders by Vendor      │ │
│ ├───────────────────────────────────────┤ ├───────────────────────────────┤ │
│ │ Orders Completed:     35              │ │ TechZone Electronics    15   │ │
│ │ Items Picked:         187             │ │ FashionHub Nigeria      12   │ │
│ │ Packages Received:    8               │ │ HomeEssentials Store    8    │ │
│ │ Average Pick Time:    4.1 min         │ │ SportsPro Nigeria       7    │ │
│ │ Orders On Time:       97%             │ │ BeautyBliss Cosmetics   5    │ │
│ └───────────────────────────────────────┘ └───────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────────┘
```

**Dashboard Components**:

| Component | Description | Refresh Rate |
|-----------|-------------|--------------|
| Pending Orders | Orders awaiting processing | Real-time (WebSocket) |
| Ready to Pick | Orders assigned and ready for picking | Real-time |
| Awaiting Pack | Orders picked, need packing | Real-time |
| Active Staff | Staff currently on shift | Real-time |
| Pickups Scheduled | Courier pickups for today | Real-time |
| Activity Timeline | Recent system events | Real-time |
| Performance Metrics | Daily KPIs | Every 5 minutes |
| Vendor Breakdown | Orders by vendor | Every 5 minutes |

---

### 3.2 Personal Storage Dashboard

**URL**: `/warehouse/dashboard/storage`

**Purpose**: Main operational hub for personal storage facility operations

**Page Layout**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [☰] Gogidix Storage                   [Facility Mode Toggle] [🔔] [👤]           │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                         Search │
│ ┌───────────────────────────────────────────────────────────────────────────┐ │
│ │ Gogidix Self Storage - Abuja                            Feb 15, 2026    │ │
│ │ 24/7 Access                                             143 customers   │ │
└───────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌────────────┐ ┌────────────┐ ┌────────────┐ ┌────────────┐ ┌────────────┐ │
│ │ Occupied   │ │ Available  │ │ Active     │ │ Access     │ │ Payments   │ │
│ │ Units      │ │ Units      │ │ Customers  │ │ Requests   │ │ Due        │ │
│ │   167/200  │ │     33     │ │    143     │ │     7      │ │    12      │ │
│ │  83.5%     │ │ Various    │ │  4 new     │ │  2 pending │ │  ₦245K     │ │
│ │ [Directory]│ │ [View All] │ │ [View All] │ │ [Review]   │ │ [View All] │ │
│ └────────────┘ └────────────┘ └────────────┘ └────────────┘ └────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Today's Access Schedule                                                │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ 10:00  Ngozi Adeleke           Unit A-015    Scheduled     ✓ Approved │ │
│ │ 11:30  Ibrahim Kalu            Unit B-042    Scheduled     ✓ Approved │ │
│ │ 02:00  Adaeze Nwosu            Unit C-012    Walk-in       - Pending  │ │
│ │ 03:30  Chinedu Okafor          Unit D-001    Scheduled     ✓ Approved │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌───────────────────────────────────────┐ ┌───────────────────────────────┐ │
│ │ Occupancy by Unit Size               │ │ Revenue Overview               │ │
│ ├───────────────────────────────────────┤ ├───────────────────────────────┤ │
│ │ Small      ████████░░ 67% (45/67)   │ │ This Month:    ₦3,850,000      │ │
│ │ Medium     ██████████ 83% (75/90)   │ │ Pending:        ₦245,000        │ │
│ │ Large      ███████░░░ 57% (47/82)   │ │ Overdue:        ₦85,000         │ │
│ │ Extra-Large ████░░░░░░ 33% (10/30)  │ │ Collection Rate:94%             │ │
│ └───────────────────────────────────────┘ └───────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 4. E-commerce Fulfillment Pages

### 4.1 Vendor Orders List

**URL**: `/warehouse/orders/vendor-orders`

**Purpose**: View and manage all orders from e-commerce vendors

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [←] Vendor Orders                                     [New Orders Badge: 12]    │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│ Filters:                                                                     │
│ [All Orders ▼] [All Vendors ▼] [All Priorities ▼] [All Statuses ▼]          │
│ Date Range: [Feb 15, 2026] to [Feb 15, 2026]                                 │
│                                                                     [Reset]   │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Order ID    │ Vendor              │ Items │ Priority │ Status       │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ GO-04723    │ TechZone Electron.. │ 3     │ HIGH     │ Ready Pick   │ │
│ │             │ [Assign] [View]                                         │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ GO-04722    │ FashionHub Nigeria  │ 5     │ NORMAL   │ Processing   │ │
│ │             │ [Assign] [View]                                         │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ GO-04721    │ HomeEssentials Store│ 2     │ NORMAL   │ Processing   │ │
│ │             │ [Assign] [View]                                         │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ GO-04720    │ SportsPro Nigeria   │ 8     │ HIGH     │ Ready Pick   │ │
│ │             │ [Assign] [View]                                         │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│                                      Showing 1-47 of 47 orders                │
│                         [Previous] [1] [2] [3] [Next]                       │
└─────────────────────────────────────────────────────────────────────────────────┘
```

**Table Actions**:
- **Assign**: Open staff assignment modal
- **View**: Open order detail panel
- **Export**: Export orders to CSV/Excel
- **Print**: Print pick tickets

---

### 4.2 Order Detail Page

**URL**: `/warehouse/orders/:orderId`

**Purpose**: View complete order details and manage order fulfillment

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [←] Order Details: GO-2026-04723                                   [Cancel]   │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│ ┌────────────────────────────────────────┐ ┌────────────────────────────────┐ │
│ │ Order Information                      │ │ Vendor Information             │ │
│ ├────────────────────────────────────────┤ ├────────────────────────────────┤ │
│ │ Order ID: GO-2026-04723              │ │ TechZone Electronics           │ │
│ │ Created: Feb 15, 2026 07:28 AM       │ │ Storage: Gogidix Warehousing    │ │
│ │ Priority: HIGH                        │ │ Since: Jan 2024                 │ │
│ │ Status: Ready to Pick                │ │ Contact: Amaka Obi              │ │
│ │                                        │ │ [Contact Vendor]               │ │
│ │ ┌────────────────────────────────────┐│ │                                 │ │
│ │ │ [Assign to Staff] [Mark Urgent]   ││ │ Total Orders: 234               │ │
│ │ └────────────────────────────────────┘│ │ This Month: 45                 │ │
│ └────────────────────────────────────────┘ └────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Customer & Shipping                                                   │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Customer: John Doe                                                    │ │
│ │ Email: john.doe@email.com                                             │ │
│ │ Phone: +234 802 345 6789                                              │ │
│ │                                                                        │ │
│ │ Shipping Address:                                                     │ │
│ │ 12, Adetokunbo Ademola Street                                         │ │
│ │ Victoria Island, Lagos                                                │ │
│ │ Nigeria                                                                │ │
│ │                                                                        │ │
│ │ Delivery Method: Express (Same-day)  Deadline: Feb 15, 2:00 PM       │ │
│ │ Special Instructions: Fragile items - handle with care                │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Items to Pick (3 items)                                                │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ ┌───────────────────────────────────────────────────────────────────┐ ││
│ │ │ 1. Wireless Bluetooth Headphones              Zone B, R12, S4, B  │ ││
│ │ │    SKU: TECH-001-BT | Qty: 1 | [View Image]                      │ ││
│ │ │    Status: Available                                              │ ││
│ │ ├───────────────────────────────────────────────────────────────────┤ ││
│ │ │ 2. USB-C Charging Cable                        Zone B, R12, S5, A  │ ││
│ │ │    SKU: TECH-002-USB | Qty: 2 | [View Image]                     │ ││
│ │ │    Status: Available                                              │ ││
│ │ ├───────────────────────────────────────────────────────────────────┤ ││
│ │ │ 3. Phone Stand Holder                             Zone B, R13, S2, C│ ││
│ │ │    SKU: TECH-003-STAND | Qty: 1 | [View Image]                   │ ││
│ │ │    Status: Available                                              │ ││
│ │ └───────────────────────────────────────────────────────────────────┘ ││
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Fulfillment Timeline                                                   │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ ✓ Order Received         Feb 15, 07:28 AM                               │ │
│ │ ✓ Inventory Verified    Feb 15, 07:29 AM                               │ │
│ │ → Ready to Pick         Current Status                                  │ │
│ │ ○ Picking in Progress   Pending                                        │ │
│ │ ○ Packed                Pending                                        │ │
│ │ ○ Awaiting Courier      Pending                                        │ │
│ │ ○ In Transit            Pending                                        │ │
│ │ ○ Delivered             Pending                                        │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Actions                                                                 │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ [Assign to Staff] [Mark as Picked] [Report Issue] [Contact Vendor]     │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

### 4.3 Staff Assignment Modal

**Purpose**: Assign orders to available warehouse staff

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [×] Assign Order to Staff                                              [Save]   │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│ Order: GO-2026-04723                                                         │
│ Items: 3 | Estimated Pick Time: 5 minutes                                     │
│                                                                              │
│ Available Staff:                                                              │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ ┌─────────────────────────────────────────────────────────────────────┐ ││
│ │ │ ● Emmanuel Adebayo                                    [Select]       │ ││
│ │ │                                                                        │ ││
│ │ │   Active Tasks: 2 | Completed Today: 15                               │ ││
│ │ │   Rating: 4.8/5 ⭐⭐⭐⭐⭐  Zone: B (Assigned)                     │ ││
│ │ │   Specializes in: Electronics, Small items                           │ ││
│ │ └─────────────────────────────────────────────────────────────────────┘ ││
│ │                                                                          │ │
│ │ ┌─────────────────────────────────────────────────────────────────────┐ ││
│ │ │ ○ Grace Okonkwo                                      [Select]       │ ││
│ │ │                                                                        │ ││
│ │ │   Active Tasks: 1 | Completed Today: 12                               │ ││
│ │ │   Rating: 4.9/5 ⭐⭐⭐⭐⭐  Zone: A (Available)                     │ ││
│ │ │   Specializes in: Apparel, General merchandise                       │ ││
│ │ └─────────────────────────────────────────────────────────────────────┘ ││
│ │                                                                          │ │
│ │ ┌─────────────────────────────────────────────────────────────────────┐ ││
│ │ │ ○ Ibrahim Musa                                      [Select]       │ ││
│ │ │                                                                        │ ││
│ │ │   Active Tasks: 0 | Completed Today: 18                               │ ││
│ │ │   Rating: 4.7/5 ⭐⭐⭐⭐☆  Zone: All (On Break)                    │ ││
│ │ │   Specializes in: Heavy items, Bulk picking                          │ ││
│ │ └─────────────────────────────────────────────────────────────────────┘ ││
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ Split Options:                                                               │
│ ● Single Picker  ○ Split by Zone  ○ Split by Item Count                     │
│                                                                              │
│ Priority Assignment: ☑ Enable for HIGH priority orders                      │
│                                                                              │
│                                    [Cancel]              [Confirm Assign]   │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

### 4.4 Inbound Packages List

**URL**: `/warehouse/inbound/packages`

**Purpose**: View and manage incoming packages from vendors

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [←] Inbound Packages                                        [Today: 12 pkgs]    │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│ Tabs: [Awaiting Arrival] [In Receiving] [Received] [Verified] [All]          │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Package ID │ Vendor       │ Items │ Status      │ Received By │ Action│ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ PKG-1024   │ TechZone     │ 18    │ Verifying  │ Chioma E.   │ [View]│ │
│ │            │ Electronics  │       │             │             │       │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ PKG-1023   │ FashionHub   │ 25    │ Received    │ Chioma E.   │ [View]│ │
│ │            │ Nigeria      │       │             │             │       │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ PKG-1025   │ HomeEssent.. │ 12    │ In Transit  │ -           │ [View]│ │
│ │            │              │       │ ETA: 2PM    │             │       │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Recent Advance Shipping Notices                                          │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ SHP-TECH-0246  TechZone Electronics  ETA: Tomorrow 9AM  50 items       │ │
│ │ SHP-FASH-0089  FashionHub Nigeria      ETA: Feb 17     35 items       │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│                                    [+ Register New ASN]                     │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

### 4.5 Package Detail & Verification

**URL**: `/warehouse/inbound/packages/:packageId`

**Purpose**: View package details and manage receiving/verification process

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [←] Package Details: PKG-2026-1024                                [Complete]  │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│ ┌────────────────────────────────────────┐ ┌────────────────────────────────┐ │
│ │ Package Information                    │ │ Vendor Information             │ │
│ ├────────────────────────────────────────┤ ├────────────────────────────────┤ │
│ │ Package ID: PKG-2026-1024             │ │ TechZone Electronics           │ │
│ │ ASN: SHP-TECH-2026-0245               │ │ Storage Type: Gogidix          │ │
│ │ Status: Verifying                      │ │ Contact: Amaka Obi              │ │
│ │ Received: Feb 15, 09:22 AM            │ │ Phone: +234 802 XXX XXXX        │ │
│ │ Received By: Chioma Eze               │ │                                │ │
│ │ Location: Receiving Dock A            │ │ [Contact Vendor]               │ │
│ └────────────────────────────────────────┘ └────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Expected Items (18 items, 4 SKUs)                    Verified: 15/18    │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ ┌───────────────────────────────────────────────────────────────────┐ ││
│ │ │ ✓ Wireless Headphones (TECH-001-BT)                              │ ││
│ │ │    Expected: 10 | Verified: 10 | Storage: Zone B, R15, S2       │ ││
│ │ ├───────────────────────────────────────────────────────────────────┤ ││
│ │ │ ✓ USB-C Cables (TECH-002-USB)                                    │ ││
│ │ │    Expected: 5 | Verified: 5 | Storage: Zone B, R15, S3         │ ││
│ │ ├───────────────────────────────────────────────────────────────────┤ ││
│ │ │ → Phone Stands (TECH-003-STAND)  [VERIFYING 0/3]                 │ ││
│ │ │    Expected: 3 | Verified: 0 | Storage: Zone B, R16, S1         │ ││
│ │ │    [Start Verification]                                          │ ││
│ │ ├───────────────────────────────────────────────────────────────────┤ ││
│ │ │ ○ Laptop Sleeves (TECH-004-LS) - NEW SKU                         │ ││
│ │ │    Expected: 0 | Verified: 0 | Storage: TBD                      │ ││
│ │ │    [Create Storage Location]                                      │ ││
│ │ └───────────────────────────────────────────────────────────────────┘ ││
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Discrepancies (if any)                                                 │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ No discrepancies reported                                               │ │
│ │ [Report Discrepancy]                                                   │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Actions                                                                 │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ [Generate Storage Labels] [Report Issue] [Put-away All] [Notify Vendor]│ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 5. Personal Storage Pages

### 5.1 Storage Units Directory

**URL**: `/warehouse/storage/units`

**Purpose**: View and manage all storage units in the facility

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [←] Storage Units Directory                                   [33 Available]   │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│ Filters:                                                                     │
│ [All Units ▼] [All Sizes ▼] [All Statuses ▼]                                 │
│ Search: [Search by unit number, customer name...]                            │
│ Sort: [Unit Number ▼]                                                        │
│                                                                              │
│ View: [Grid] [List] [Map]                                                    │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ ┌───────────┐ ┌───────────┐ ┌───────────┐ ┌───────────┐ ┌───────────┐  │ │
│ │ │ A-001     │ │ A-002     │ │ A-003     │ │ A-004     │ │ A-005     │  │ │
│ │ │ Small     │ │ Medium    │ │ Large     │ │ Small     │ │ Medium    │  │ │
│ │ │ ████████ │ │ ████████ │ │ ░░░░░░░░░░░ │ │ ░░░░░░░░░░░ │ │ ████████ │  │ │
│ │ │ Occupied  │ │ Occupied  │ │ Available │ │ Available │ │ Occupied  │  │ │
│ │ │ Amaka O.  │ │ Tunde B.  │ │           │ │           │ │ Chioma E. │  │ │
│ │ │ ₦15K/mo   │ │ ₦25K/mo   │ │ ₦40K/mo   │ │ ₦15K/mo   │ │ ₦25K/mo   │  │ │
│ │ │ [View]    │ │ [View]    │ │ [Rent]    │ │ [Rent]    │ │ [View]    │  │ │
│ │ └───────────┘ └───────────┘ └───────────┘ └───────────┘ └───────────┘  │ │
│ │                                                                          │ │
│ │ ┌───────────┐ ┌───────────┐ ┌───────────┐ ┌───────────┐ ┌───────────┐  │ │
│ │ │ A-006     │ │ A-007     │ │ A-008     │ │ A-009     │ │ A-010     │  │ │
│ │ │ Small     │ │ Medium    │ │ Large     │ │ Small     │ │ Medium    │  │ │
│ │ │ ░░░░░░░░░░░ │ │ ████████ │ │ ████████ │ │ ░░░░░░░░░░░ │ │ ████████ │  │ │
│ │ │ Available │ │ Occupied  │ │ Occupied  │ │ Available │ │ Occupied  │  │ │
│ │ │           │ │ Emeka N.  │ │ Fatima M. │ │           │ │ Yusuf A.  │  │ │
│ │ │ ₦15K/mo   │ │ ₦25K/mo   │ │ ₦40K/mo   │ │ ₦15K/mo   │ │ ₦25K/mo   │  │ │
│ │ │ [Rent]    │ │ [View]    │ │ [View]    │ │ [Rent]    │ │ [View]    │  │ │
│ │ └───────────┘ └───────────┘ └───────────┘ └───────────┘ └───────────┘  │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ Legend:  ██████ Occupied  ░░░░░░ Available  🟡 Reserved  🔴 Maintenance      │
│                                                                              │
│                           [+ Add New Unit]  [Export Directory]               │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

### 5.2 Unit Detail Page

**URL**: `/warehouse/storage/units/:unitId`

**Purpose**: View and manage individual storage unit details

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [←] Unit Details: A-015                                         [Edit Unit]   │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│ ┌────────────────────────────────────────┐ ┌────────────────────────────────┐ │
│ │ Unit Information                       │ │ Customer Information           │ │
│ ├────────────────────────────────────────┤ ├────────────────────────────────┤ │
│ │ Unit ID: A-015                        │ │ Customer: Adaeze Nwosu          │ │
│ │ Size: Medium (2m x 3m x 2.5m)         │ │ Customer ID: CUST-2024-089      │ │
│ │ Type: Climate Controlled               │ │ Member since: Jan 15, 2024      │ │
│ │ Features:                              │ │ Phone: +234 803 XXX XXXX        │
│ │ • Climate Control                     │ │ Email: adaeze.n@email.com       │ │
│ │ • Ground Floor Access                 │ │                                │ │
│ │ • Alarm System                        │ │ [View Customer Profile]         │
│ │                                        │ │ [Contact Customer]              │ │
│ │ Status: OCCUPIED                      │ │                                │ │
│ └────────────────────────────────────────┘ └────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Rental Information                                                     │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Start Date: January 15, 2024                                            │ │
│ │ Rental Term: Month-to-month                                             │ │
│ │ Monthly Rate: ₦25,000                                                   │ │
│ │ Security Deposit: ₦25,000                                               │ │
│ │                                                                        │ │
│ │ Payment Status: ✓ CURRENT                                              │ │
│ │ Next Payment Due: February 28, 2026                                     │ │
│ │ Amount: ₦25,000                                                         │ │
│ │                                                                        │ │
│ │ Payment History: [View All 26 payments]                                │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Access History (Last 10 visits)                                         │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Feb 15, 2026  08:00 AM  Scheduled access granted  Duration: 30 min      │ │
│ │ Feb 10, 2026  02:15 PM  Walk-in access granted     Duration: 45 min      │ │
│ │ Feb 5, 2026   10:30 AM  Scheduled access granted  Duration: 1 hr 15 min  │ │
│ │ Jan 28, 2026  04:00 PM  Walk-in access granted     Duration: 20 min      │ │
│ │ Jan 20, 2026  11:00 AM  Scheduled access granted  Duration: 50 min      │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Actions                                                                 │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ [Grant Access] [Schedule Access] [Record Payment] [End Rental]          │ │
│ │ [Generate Access Report] [View Contract] [Issue Warning]                │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

### 5.3 Grant Access Modal

**Purpose**: Grant immediate or scheduled access to customer's unit

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [×] Grant Access to Customer                                                          │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│ Unit: A-015                                                                   │
│ Customer: Adaeze Nwosu                                                         │
│ Account Status: ✓ Active | ✓ Current on payments                              │
│                                                                              │
│ Access Type:                                                                  │
│ ● Standard (Customer only)                                                    │
│ ○ With Guest (1 additional person - max)                                      │
│ ○ With Vehicle (Loading/unloading at dock)                                    │
│                                                                              │
│ Access Mode:                                                                  │
│ ● Immediate Access                                                            │
│ ○ Scheduled Access                                                            │
│   Date: [Feb 15, 2026]  Time: [10:00 AM]                                     │
│                                                                              │
│ Duration:                                                                     │
│ ● Standard (1 hour)                                                           │
│ ○ Extended (2 hours)                                                          │
│ ○ Full day (until close)                                                      │
│ ○ Custom duration: [    ] hours                                              │
│                                                                              │
│ Purpose of Visit (optional):                                                  │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Retrieve seasonal items                                                │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ Access Code Options:                                                         │
│ ● Auto-generate (recommended)                                                │
│ ○ Use customer's permanent code                                              │
│ ○ Create custom one-time code                                                │
│                                                                              │
│ Notifications:                                                                │
│ ☑ Send SMS to customer with access code                                      │
│ ☑ Email confirmation to customer                                             │
│ ☑ Notify security desk                                                       │
│                                                                              │
│                                    [Cancel]              [Grant Access]       │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

### 5.4 Customer Directory

**URL**: `/warehouse/storage/customers`

**Purpose**: View and manage all personal storage customers

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [←] Customer Directory                                        [143 Active]     │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│ Filters: [All Customers ▼] [All Statuses ▼] [All Unit Sizes ▼]               │
│ Search: [Search by name, phone, email, unit...]                             │
│ Sort: [Name ▼]                                                                │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Customer            │ Unit  │ Status    │ Payments    │ Action          │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Adaeze Nwosu        │ A-015 │ Active    │ Current     │ [View] [Msg]    │ │
│ │ +234 803 XXX XXXX   │       │ Since Jan │ Due Feb 28  │                 │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Ibrahim Kalu        │ B-042 │ Active    │ Current     │ [View] [Msg]    │ │
│ │ +234 804 XXX XXXX   │       │ Since Mar │ Due Feb 20  │                 │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Ngozi Adeleke       │ A-008 │ Active    │ Current     │ [View] [Msg]    │ │
│ │ +234 805 XXX XXXX   │       │ Since Feb │ Due Feb 25  │                 │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Chinedu Okafor      │ C-028 │ Reserved  │ Pending     │ [View] [Msg]    │ │
│ │ +234 806 XXX XXXX   │       │ Starts    │ Starts Feb   │                 │
│ │                     │       │ Feb 20    │ 20          │                 │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│                                    [+ New Customer] [Export List]             │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

### 5.5 New Customer Registration

**URL**: `/warehouse/storage/customers/new`

**Purpose**: Register new personal storage customer

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [←] New Customer Registration                                                 │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│ Step 1 of 3: Customer Information                                             │
│                                                                              │
│ Personal Information:                                                         │
│ First Name *                    Last Name *                                   │
│ ┌──────────────────────┐      ┌──────────────────────┐                        │
│ │ Nnamdi              │      │ Okoro               │                        │
│ └──────────────────────┘      └──────────────────────┘                        │
│                                                                              │
│ Phone *                         Email *                                       │
│ ┌──────────────────────┐      ┌──────────────────────┐                        │
│ │ +234 802 XXX XXXX    │      │ nnamdi.o@email.com  │                        │
│ └──────────────────────┘      └──────────────────────┘                        │
│                                                                              │
│ Address *                                                                     │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ 12, Adeniyi Jones, Ikeja, Lagos                                        │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ Identification:                                                               │
│ ID Type: [National ID ▼]   ID Number: [12345678901]                         │
│ Upload ID: [Choose File]  [Browse...]                                        │
│                                                                              │
│ Emergency Contact:                                                            │
│ Name *              Relationship *       Phone *                             │
│ ┌────────────┐       ┌────────────┐       ┌──────────────────┐              │
│ │ Chinedu O. │       │ Brother    │       │ +234 803 XXX XXXX │              │
│ └────────────┘       └────────────┘       └──────────────────┘              │
│                                                                              │
│                            [Previous]                    [Next: Select Unit]  │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 6. Inventory Management Pages

### 6.1 Inventory Overview

**URL**: `/warehouse/inventory`

**Purpose**: View overall inventory status across all zones

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [←] Inventory Overview                                        [Last sync: Just now]│
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│ ┌────────────┐ ┌────────────┐ ┌────────────┐ ┌────────────┐ ┌────────────┐ │
│ │ Total      │ │ In Stock   │ │ Low Stock  │ │ Out of     │ │ Total      │ │
│ │ SKUs       │ │            │ │ Alert      │ │ Stock      │ │ Items      │ │
│ │   1,247    │ │   1,180    │ │     42     │ │     25     │ │   45,892   │ │
│ │ [View All] │ │ 94.6%      │ │ [Review]   │ │ [Review]   │ │            │ │
│ └────────────┘ └────────────┘ └────────────┘ └────────────┘ └────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Inventory by Zone                                                      │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ ┌───────────────────────────────────────────────────────────────────┐ ││
│ │ │ Zone A - General Merchandise                              78.5%   │ ││
│ │ │ ████████████████████████████████████░░░░░░░░░░░░░░░░░░░░░░░░░░░   │ ││
│ │ │ 312 SKUs | 12,450 items | 34 low stock | 8 out of stock           │ ││
│ │ │ [View Zone]                                                      │ ││
│ │ ├───────────────────────────────────────────────────────────────────┤ ││
│ │ │ Zone B - Electronics                                        85.2%   │ ││
│ │ │ ███████████████████████████████████████████░░░░░░░░░░░░░░░░░░░░   │ ││
│ │ │ 185 SKUs | 8,234 items | 5 low stock | 2 out of stock            │ ││
│ │ │ [View Zone]                                                      │ ││
│ │ ├───────────────────────────────────────────────────────────────────┤ ││
│ │ │ Zone C - Apparel                                            72.8%   │ ││
│ │ │ ██████████████████████████████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░   │ ││
│ │ │ 267 SKUs | 15,678 items | 18 low stock | 10 out of stock          │ ││
│ │ │ [View Zone]                                                      │ ││
│ │ └───────────────────────────────────────────────────────────────────┘ ││
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Low Stock Alerts (Requires Attention)                                  │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ SKU         │ Product           │ Zone │ Stock │ Reorder │ Action     │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ TECH-001-BT │ Wireless Headph.. │ B    │ 3     │ Notify  │ [Notify]   │ │
│ │ FASH-004-SC │ Women's Scarf     │ A    │ 5     │ Notify  │ [Notify]   │ │
│ │ HOME-012-TB │ Table Lamp        │ A    │ 2     │ Notify  │ [Notify]   │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│                                    [Export Inventory] [Sync All Vendors]     │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

### 6.2 Zone Inventory Detail

**URL**: `/warehouse/inventory/zone/:zoneId`

**Purpose**: View detailed inventory for a specific zone

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [←] Zone B - Electronics Inventory                                           │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│ Zone Statistics:                                                              │
│ 185 SKUs | 8,234 items | 85.2% full | 5 low stock | 2 out of stock            │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Row Overview                                                            │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Row 10: ████████████░░░░░░  65% | 245 SKUs | [View]                     │ │
│ │ Row 11: ██████████████████  85% | 312 SKUs | [View]                     │ │
│ │ Row 12: ████████████████████ 92% | 189 SKUs | [View]                     │ │
│ │ Row 13: ████████░░░░░░░░░░░  45% | 156 SKUs | [View]                     │ │
│ │ Row 14: ████████████████░░░░  75% | 278 SKUs | [View]                     │ │
│ │ Row 15: ██████████████████░░  80% | 201 SKUs | [View]                     │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Items in Row 12 (Most Popular)                                          │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Shelf │ Bin │ SKU           │ Product             │ Stock │ Capacity │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ 4     │ B   │ TECH-001-BT   │ Wireless Headphones │ 15/50 │ 30%      │ │
│ │ 4     │ C   │ TECH-002-USB  │ USB-C Cable         │ 42/50 │ 84%      │ │
│ │ 5     │ A   │ TECH-003-STAND│ Phone Stand         │ 28/30 │ 93%      │ │
│ │ 5     │ B   │ TECH-004-LS   │ Laptop Sleeve       │ 18/25 │ 72%      │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Inventory Actions                                                       │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ [Add New SKU] [Adjust Stock] [Move Items] [Cycle Count] [Print Labels]  │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 7. Staff Management Pages

### 7.1 Staff Directory

**URL**: `/warehouse/staff`

**Purpose**: View and manage all warehouse staff

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [←] Staff Directory                                              [24 Total Staff]│
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│ Filters: [All Roles ▼] [All Shifts ▼] [All Statuses ▼]                        │
│ Search: [Search by name, ID, phone...]                                       │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Staff Member          │ Role        │ Status   │ Tasks  │ Performance │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Emmanuel Adebayo      │ Picker      │ Picking  │ 3/5    │ 4.8/5 ⭐⭐⭐⭐⭐│ │
│ │ STF-2024-015          │             │          │        │ 15 orders    │ │
│ │ +234 810 XXX XXXX     │             │          │        │ [View]      │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Grace Okonkwo         │ Picker      │ Packing  │ 1/5    │ 4.9/5 ⭐⭐⭐⭐⭐│ │
│ │ STF-2024-018          │             │          │        │ 18 orders    │ │
│ │ +234 811 XXX XXXX     │             │          │        │ [View]      │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Chioma Eze            │ Receiving   │ Idle     │ 0/5    │ 4.7/5 ⭐⭐⭐⭐⭐│ │
│ │ STF-2024-021          │             │          │        │ 8 packages   │ │
│ │ +234 812 XXX XXXX     │             │          │        │ [View]      │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Ibrahim Musa         │ Picker      │ Break    │ 0/5    │ 4.7/5 ⭐⭐⭐⭐⭐│ │
│ │ STF-2024-023          │             │          │        │ 18 orders    │ │
│ │ +234 813 XXX XXXX     │             │          │        │ [View]      │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│                                    [+ Add Staff] [View Schedule]             │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

### 7.2 Staff Detail Page

**URL**: `/warehouse/staff/:staffId`

**Purpose**: View detailed staff information and performance

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [←] Staff Details: Emmanuel Adebayo                                          │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│ ┌────────────────────────────────────────┐ ┌────────────────────────────────┐ │
│ │ Staff Information                      │ │ Current Status                 │ │
│ ├────────────────────────────────────────┤ ├────────────────────────────────┤ │
│ │ Name: Emmanuel Adebayo                 │ │ Status: Picking                │ │
│ │ ID: STF-2024-015                      │ │ Current Task: GO-04723         │ │
│ │ Role: Picker/Packer                   │ │ Started: 07:35 AM              │ │
│ │ Shift: Morning (6AM-2PM)              │ │ Progress: 1/3 items             │ │
│ │ Phone: +234 810 XXX XXXX              │ │ Location: Zone B, R12           │ │
│ │ Email: emmanuel.a@staff.gogidix.com   │ │                                │ │
│ │ Hire Date: Jan 15, 2024               │ │ [Send Message] [View Task]      │ │
│ │                                        │ │ [Reassign] [Call]             │ │
│ │ [Edit Profile] [Reset Password]       │ │                                │ │
│ └────────────────────────────────────────┘ └────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Today's Performance (Feb 15, 2026)                                      │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Orders Picked:     15                                                  │ │
│ │ Items Picked:      52                                                  │ │
│ │ Average Pick Time: 4.2 min/order                                       │ │
│ │ Accuracy:          100% (0 errors)                                     │ │
│ │ Current Task:      Picking Order GO-04723 (1 of 3 items)               │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Weekly Performance (Feb 10-15)                                          │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ ┌─────────────────────────────────────────────────────────────────────┐ ││
│ │ │ Rating: 4.8/5 ⭐⭐⭐⭐⭐                                           │ ││
│ │ │                                                                  │ ││
│ │ │ Orders: 87  Items: 312  Avg Time: 4.1 min  Attendance: 6/6 shifts│ ││
│ │ └─────────────────────────────────────────────────────────────────────┘ ││
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Performance Trend (Last 30 Days)                                        │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ [Chart showing daily order counts and pick times...]                    │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Actions                                                                 │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ [Send Message] [View History] [Adjust Tasks] [Schedule Review]          │ │
│ │ [View Timesheet] [Assign Training] [Edit Profile]                        │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

### 7.3 Shift Schedule

**URL**: `/warehouse/staff/schedule`

**Purpose**: View and manage staff shift schedules

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [←] Shift Schedule                                        [Week of Feb 14-20]  │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│ Navigation: [◀ Previous Week] [This Week] [Next Week ▼]                      │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │          │ Mon 14    │ Tue 15    │ Wed 16    │ Thu 17    │ Fri 18    │  │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Morning  │ 18/20     │ 18/20     │ 20/20     │ 19/20     │ 20/20     │  │ │
│ │ 6AM-2PM  │ 2 absent  │ 2 absent  │ Full      │ 1 sick   │ Full      │  │ │
│ │          │ [Edit]    │ [Edit]    │ [Edit]    │ [Edit]    │ [Edit]    │  │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Afteroon │ 8/10      │ 8/10      │ 10/10     │ 9/10      │ 10/10     │  │ │
│ │ 2PM-10PM │ 2 gaps    │ 2 gaps    │ Full      │ 1 gap    │ Full      │  │ │
│ │          │ [Edit]    │ [Edit]    │ [Edit]    │ [Edit]    │ [Edit]    │  │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Staff Schedule Detail - Tuesday, Feb 15                                 │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Staff Member      │ Morning │ Afternoon │ Status      │ Notes         │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Grace Okonkwo    │ ✓       │ ✓        │ Working     │               │ │
│ │ Emmanuel A.     │ ✓       │ -         │ Working     │               │ │
│ │ Chioma Eze      │ ✓       │ ✓        │ Working     │               │ │
│ │ Ibrahim Musa    │ ✓       │ -         │ SICK       │ On leave      │ │
│ │ Amina Yusuf     │ -       │ ✓        │ PERSONAL   │ Personal      │ │
│ │ Oluwaseun A.    │ -       │ -         │ OFF        │ New hire      │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Actions                                                                 │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ [Edit Schedule] [Fill Open Slots] [Approve Time-off] [Export Schedule]  │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 8. Courier Integration Pages

### 8.1 Courier Pickup Schedule

**URL**: `/warehouse/courier/schedule`

**Purpose**: Schedule and manage courier pickups for fulfilled orders

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [←] Courier Pickup Schedule                                   [Today: 5 pickups]│
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│ Tabs: [Today] [Upcoming] [Past] [All]                                        │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Time    │ Courier        │ Orders   │ Status    │ Action              │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ 9:00 AM │ SwiftExpress   │ 3        │ Scheduled │ [View] [Modify]     │ │
│ │         │ ETA: 15 min    │          │           │                     │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ 11:00 AM│ FastTrack      │ 1        │ Scheduled │ [View] [Modify]     │ │
│ │         │ GO-04723       │          │           │                     │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ 2:00 PM │ SwiftExpress   │ 2        │ Pending   │ [Assign] [View]     │ │
│ │         │                │          │           │                     │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ 4:30 PM │ DASH Logistics │ 5        │ Pending   │ [Assign] [View]     │ │
│ │         │                │          │           │                     │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Orders Awaiting Pickup                                                  │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ GO-04723  TechZone Electronics     READY  HIGH  [Schedule Pickup]       │ │
│ │ GO-04720  SportsPro Nigeria        READY  HIGH  [Schedule Pickup]       │ │
│ │ GO-04718  FashionHub Nigeria       READY  NORMAL [Schedule Pickup]       │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│                                    [Schedule New Pickup]                     │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

### 8.2 Schedule Pickup Modal

**Purpose**: Schedule courier pickup for completed orders

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [×] Schedule Courier Pickup                                                   │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│ Select Orders to Pickup (3 orders selected)                                   │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ ☑ GO-04723  TechZone Electronics   3 items  3.2 kg  Lagos Island       │ │
│ │ ☑ GO-04720  SportsPro Nigeria      8 items  4.5 kg  Port Harcourt      │ │
│ │ ☑ GO-04718  FashionHub Nigeria     5 items  2.8 kg  Abuja              │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ Total Weight: 10.5 kg | Total Value: ₦245,000                                │
│                                                                              │
│ Available Couriers:                                                           │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ ● SwiftExpress                                     Rate: ₦4,200        │ │
│ │                                                                        │ │
│ │   Pickup: 11:00 AM - 12:00 PM                                        │ │
│ │   Delivery: Same day for Lagos, Next day for others                    │ │
│ │   Rating: 4.7/5 ⭐⭐⭐⭐⭐                                              │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ ○ FastTrack Logistics                               Rate: ₦3,900        │ │
│ │                                                                        │ │
│ │   Pickup: 11:30 AM - 12:30 PM                                        │ │
│ │   Delivery: Same day for Lagos, Next day for others                    │ │
│ │   Rating: 4.5/5 ⭐⭐⭐⭐☆                                              │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ Pickup Time Window:                                                           │
│ ● 11:00 AM - 12:00 PM  ○ 12:00 PM - 1:00 PM  ○ 2:00 PM - 3:00 PM            │
│                                                                              │
│ Pickup Instructions:                                                          │
│ ☑ Call upon arrival  ☑ Meet at receiving dock  ☐ Require signature          │
│                                                                              │
│ Special Instructions:                                                         │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Fragile items in GO-04723 - handle with care                           │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│                                    [Cancel]              [Schedule Pickup]   │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 9. Vendor Integration Pages

### 9.1 E-commerce Vendors List

**URL**: `/warehouse/vendors`

**Purpose**: View all e-commerce vendors using the warehouse

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [←] E-commerce Vendors                                        [45 Active Vendors]│
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│ Filters: [All Vendors ▼] [All Storage Types ▼] [All Statuses ▼]               │
│ Search: [Search by vendor name, contact...]                                  │
│ Sort: [Name ▼]                                                                │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Vendor              │ Storage   │ Orders │ Items  │ Status    │ Action│ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ TechZone            │ Gogidix   │ 234   │ 1,245 │ Active   │ [View]│ │
│ │ Electronics        │           │       │       │          │       │ │
│ │ Amaka Obi           │           │       │       │          │ [Msg] │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ FashionHub          │ Gogidix   │ 189   │ 987   │ Active   │ [View]│ │
│ │ Nigeria            │           │       │       │          │       │ │
│ │ Tunde Bakare       │           │       │       │          │ [Msg] │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ HomeEssentials      │ Self      │ 45    │ 312   │ Active   │ [View]│ │
│ │ Store              │ Storage   │       │       │          │       │ │
│ │ Chioma Ezenwa      │           │       │       │          │ [Msg] │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│                                    [Add Vendor] [Export List]                 │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

### 9.2 Vendor Detail Page

**URL**: `/warehouse/vendors/:vendorId`

**Purpose**: View detailed vendor information and activity

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [←] Vendor Details: TechZone Electronics                                     │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│ ┌────────────────────────────────────────┐ ┌────────────────────────────────┐ │
│ │ Vendor Information                     │ │ Storage Information            │ │
│ ├────────────────────────────────────────┤ ├────────────────────────────────┤ │
│ │ Company: TechZone Electronics         │ │ Storage Type: Gogidix          │ │
│ │ Contact: Amaka Obi                    │ │ Warehousing                     │ │
│ │ Position: Operations Manager          │ │                                │ │
│ │ Email: amaka.o@techzone.ng            │ │ Assigned Zones: B               │
│ │ Phone: +234 802 XXX XXXX              │ │ Zone B: 185 SKUs | 8,234 items │ │
│ │                                       │ │ Utilization: 85.2%              │ │
│ │ Since: January 2024                   │ │                                │ │
│ │ Rating: 4.8/5 ⭐⭐⭐⭐⭐              │ │ [View Inventory]               │ │
│ │                                       │ │ [Request Pickup]               │ │
│ │ [Contact Vendor] [Send Message]       │ │                                │ │
│ └────────────────────────────────────────┘ └────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Vendor Activity (This Month)                                            │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ • Total Orders: 45                                                      │ │
│ │ • Items Shipped: 234                                                    │ │
│ │ • Packages Received: 8                                                  │ │
│ │ • Storage Fee: ₦125,000                                                 │ │
│ │ • Fulfillment Fee: ₦22,500                                              │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Recent Orders                                                           │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ GO-04723  Feb 15  3 items  HIGH  Ready to Pick   [View]                 │ │
│ │ GO-04718  Feb 14  5 items  NORMAL Awaiting Pack   [View]                 │ │
│ │ GO-04715  Feb 14  2 items  NORMAL Delivered       [View]                 │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Actions                                                                 │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ [Contact Vendor] [View All Orders] [View Inventory] [Send Statement]     │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 10. Settings Pages

### 10.1 Facility Settings

**URL**: `/warehouse/settings/facility`

**Purpose**: Configure facility-level settings

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [←] Facility Settings                                                        │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Basic Information                                                       │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Facility Name                                                            │ │
│ │ ┌─────────────────────────────────────────────────────────────────────┐│ │
│ │ │ Lagos Central Warehouse                                           ││ │
│ │ └─────────────────────────────────────────────────────────────────────┘│ │
│ │                                                                        │ │
│ │ Facility Type                                                           │ │
│ │ ☑ E-commerce Fulfillment  ☑ Personal Storage                          │ │
│ │                                                                        │ │
│ │ Address                                                                 │ │
│ │ ┌─────────────────────────────────────────────────────────────────────┐│ │
│ │ │ 123, Warehouse Road, Ikeja, Lagos, 100001                         ││ │
│ │ └─────────────────────────────────────────────────────────────────────┘│ │
│ │                                                                        │ │
│ │ [Update Information]                                                   │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Operating Hours                                                         │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ ☑ Different hours for E-commerce vs Personal Storage                    │ │
│ │                                                                        │ │
│ │ E-commerce Fulfillment:                                                 │ │
│ │ [☑ Mon] [☑ Tue] [☑ Wed] [☑ Thu] [☑ Fri] [☑ Sat] [☑ Sun]                  │ │
│ │ From: [06:00 AM] To: [10:00 PM]                                        │ │
│ │                                                                        │ │
│ │ Personal Storage:                                                       │ │
│ │ [☑ Mon] [☑ Tue] [☑ Wed] [☑ Thu] [☑ Fri] [☑ Sat] [☑ Sun]                  │ │
│ │ From: [06:00 AM] To: [10:00 PM]                                        │ │
│ │ 24/7 Customer Access: ☑ Enabled                                         │ │
│ │                                                                        │ │
│ │ [Update Hours]                                                         │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Capacity Settings                                                       │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Maximum Daily Orders: [500]                                             │ │
│ │ Maximum Pick Tickets Per Staff: [50]                                    │ │
│ │ Low Stock Threshold: [%]                                                │ │
│ │                                                                        │ │
│ │ [Update Settings]                                                      │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

### 10.2 Pricing Settings

**URL**: `/warehouse/settings/pricing`

**Purpose**: Configure pricing for services

**Page Elements**:
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ [←] Pricing Settings                                                         │
├─────────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│ Tabs: [E-commerce Fulfillment] [Personal Storage] [Additional Services]       │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Storage Rates (per pallet/month)                                        │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Small Pallet (up to 1m x 1m)                                             │ │
│ │ ┌─────────────────────────────────────────────────────────────────────┐│ │
│ │ │ ₦ [1,500]                                                        ││ │
│ │ └─────────────────────────────────────────────────────────────────────┘│ │
│ │                                                                        │ │
│ │ Medium Pallet (up to 1.2m x 1m)                                         │ │
│ │ ┌─────────────────────────────────────────────────────────────────────┐│ │
│ │ │ ₦ [2,000]                                                        ││ │
│ │ └─────────────────────────────────────────────────────────────────────┘│ │
│ │                                                                        │ │
│ │ Large Pallet (up to 1.2m x 1.2m)                                        │ │
│ │ ┌─────────────────────────────────────────────────────────────────────┐│ │
│ │ │ ₦ [2,500]                                                        ││ │
│ │ └─────────────────────────────────────────────────────────────────────┘│ │
│ │                                                                        │ │
│ │ [Update Rates]                                                         │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
│                                                                              │
│ ┌─────────────────────────────────────────────────────────────────────────┐ │
│ │ Fulfillment Fees (per order)                                            │ │
│ ├─────────────────────────────────────────────────────────────────────────┤ │
│ │ Standard Pick & Pack                                                    │ │
│ │ ┌─────────────────────────────────────────────────────────────────────┐│ │
│ │ │ ₦ [500]                                                          ││ │
│ │ └─────────────────────────────────────────────────────────────────────┘│ │
│ │                                                                        │ │
│ │ Express Pick & Pack                                                    │ │
│ │ ┌─────────────────────────────────────────────────────────────────────┐│ │
│ │ │ ₦ [750]                                                          ││ │
│ │ └─────────────────────────────────────────────────────────────────────┘│ │
│ │                                                                        │ │
│ │ Returns Processing                                                      │ │
│ │ ┌─────────────────────────────────────────────────────────────────────┐│ │
│ │ │ ₦ [300]                                                          ││ │
│ │ └─────────────────────────────────────────────────────────────────────┘│ │
│ │                                                                        │ │
│ │ [Update Fees]                                                          │ │
│ └─────────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────────┘
```

---

## 11. Mobile App Pages

### 11.1 Staff Login (Mobile)

**Purpose**: Authenticate warehouse staff on mobile devices

**Page Elements**:
```
┌─────────────────────────────┐
│      [Gogidix Logo]         │
│   Warehouse Staff App       │
├─────────────────────────────┤
│                             │
│   Welcome Back!             │
│   Sign in to continue       │
│                             │
│  Email / Staff ID           │
│  ┌───────────────────────┐ │
│  │                       │ │
│  └───────────────────────┘ │
│                             │
│  Password                   │
│  ┌───────────────────────┐ │
│  │ ••••••••             │ │
│  └───────────────────────┘ │
│                             │
│  ┌───────────────────────┐ │
│  │      Login           │ │
│  └───────────────────────┘ │
│                             │
│  Forgot password?           │
│                             │
└─────────────────────────────┘
```

---

### 11.2 Clock In/Out (Mobile)

**Purpose**: Staff to clock in/out of shifts

**Page Elements**:
```
┌─────────────────────────────┐
│  Good Morning, Emmanuel!    │
│                             │
│  ┌─────────────────────┐   │
│  │                     │   │
│  │    [Clock Icon]     │   │
│  │                     │   │
│  │     Clock In        │   │
│  │                     │   │
│  └─────────────────────┘   │
│                             │
│  Shift: Morning             │
│  6:00 AM - 2:00 PM         │
│                             │
│  Location:                 │
│  ✓ Lagos Central Warehouse │
│                             │
│  [Clock In]                │
└─────────────────────────────┘
```

---

### 11.3 Pick Ticket (Mobile)

**Purpose**: Staff to pick items for orders

**Page Elements**:
```
┌─────────────────────────────┐
│  Pick Ticket: GO-04723     │
│  Item 1 of 3               │
│  TechZone Electronics      │
│  Priority: HIGH            │
├─────────────────────────────┤
│                             │
│  Pick this item:           │
│                             │
│  ┌─────────────────────┐   │
│  │                     │   │
│  │   [Product Image]   │   │
│  │                     │   │
│  └─────────────────────┘   │
│                             │
│  Wireless Bluetooth         │
│  Headphones                 │
│  SKU: TECH-001-BT           │
│  Qty: 1                     │
│                             │
│  Location:                 │
│  Zone B, Row 12,            │
│  Shelf 4, Bin B             │
│                             │
│  ┌─────────────────────┐   │
│  │  [Scan Barcode]     │   │
│  └─────────────────────┘   │
│                             │
│  [Manual Confirm]          │
│  [Item Issue]              │
└─────────────────────────────┘
```

---

### 11.4 Package Receiving (Mobile)

**Purpose**: Staff to receive and verify inbound packages

**Page Elements**:
```
┌─────────────────────────────┐
│  Package Verification       │
│  PKG-2026-1024             │
│  TechZone Electronics      │
├─────────────────────────────┤
│                             │
│  Verify items received:    │
│                             │
│  SKU: TECH-001-BT          │
│  Wireless Headphones        │
│  Expected: 10              │
│  Scanned: 7/10             │
│                             │
│  ┌─────────────────────┐   │
│  │  [Scan Item]        │   │
│  └─────────────────────┘   │
│                             │
│  Progress:                 │
│  ████████░░ 70%            │
│  7 of 10 items            │
│                             │
│  [Report Discrepancy]      │
│  [Complete Verification]   │
└─────────────────────────────┘
```

---

## Summary

This page-by-page documentation covers all major screens and flows for the Warehouse Partners Dashboard, including:

1. **Authentication**: Login, facility mode selection, password reset
2. **Onboarding**: 8-step registration process
3. **Dashboard**: Dual-mode dashboards for E-commerce and Personal Storage
4. **E-commerce Operations**: Order management, picking, packing, receiving
5. **Personal Storage**: Unit management, customer access, rentals
6. **Inventory**: Zone-based inventory management
7. **Staff**: Directory, scheduling, performance tracking
8. **Courier**: Pickup scheduling and coordination
9. **Vendor**: E-commerce vendor management
10. **Settings**: Facility configuration and pricing
11. **Mobile App**: Staff operations on mobile devices

All pages follow consistent design patterns with the orange-themed color scheme and support real-time updates via WebSocket connections.

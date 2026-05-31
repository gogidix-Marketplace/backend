# Warehouse Partners Dashboard - Mock Flow Documentation

## Overview

This document provides comprehensive mock flows that simulate real-world user journeys through the Warehouse Partners Dashboard. Each flow represents a complete user scenario from login to task completion, showcasing the dual facility management system (E-commerce Fulfillment + Personal Storage).

---

## User Profiles

### Profile 1: Warehouse Manager (E-commerce Fulfillment Focus)
- **Name**: Sarah Mitchell
- **Role**: Warehouse Manager at Gogidix Fulfillment Center - Lagos
- **Facility Type**: E-commerce Fulfillment Center (15,000 sq ft)
- **Staff Count**: 24 warehouse staff, 6 shift supervisors
- **Primary Responsibilities**: Order fulfillment, vendor coordination, staff management
- **Facility Mode**: Primarily uses E-commerce Fulfillment mode

### Profile 2: Storage Manager (Personal Storage Focus)
- **Name**: Chinedu Okafor
- **Role**: Storage Manager at Gogidix Self Storage - Abuja
- **Facility Type**: Personal Storage Facility (200+ storage units)
- **Staff Count**: 12 facility staff, 4 customer service reps
- **Primary Responsibilities**: Unit management, customer accounts, access control
- **Facility Mode**: Primarily uses Personal Storage mode

### Profile 3: Warehouse Staff Member (Mobile App User)
- **Name**: Emmanuel Adebayo
- **Role**: Warehouse Staff (Picker/Packer)
- **Facility**: Gogidix Fulfillment Center - Lagos
- **Shift**: Morning Shift (6:00 AM - 2:00 PM)
- **Primary Tasks**: Receiving packages, picking orders, packing shipments
- **Device**: Mobile App with barcode scanner

---

## Mock Flow 1: Morning Warehouse Operations Check (E-commerce Fulfillment Mode)

**User**: Sarah Mitchell (Warehouse Manager)
**Scenario**: Starting the day with overview of E-commerce fulfillment operations

### Flow Steps:

#### Step 1: Login and Dashboard Access
```
User Action: Sarah navigates to warehouse.gogidix.com
Enter credentials: sarah.mitchell@warehousepartner.com / *********
Click: Login Button

System Response:
- Authentication successful
- Facility Mode Selection displayed
```

#### Step 2: Facility Mode Selection
```
User Action: Selects "E-commerce Fulfillment Mode"
Click: Continue Button

System Response:
- Dashboard loads with E-commerce fulfillment view
- Today's date: February 15, 2026, 7:30 AM
- Real-time WebSocket connection established
```

#### Step 3: Dashboard Overview Review
```
Dashboard Display:

Header:
- Facility: Gogidix Fulfillment Center - Lagos
- Mode: E-commerce Fulfillment
- Date/Time: February 15, 2026 | 07:32 AM

KPI Cards:
┌─────────────────────────────────────────────────────────┐
│ Pending Orders          │     47                        │
│ ↑ 12 from yesterday     │     [View All]                │
├─────────────────────────────────────────────────────────┤
│ Orders Ready to Pick    │     23                        │
│ 8 assigned to staff     │     [Assign Now]              │
├─────────────────────────────────────────────────────────┤
│ Orders Awaiting Pack    │     15                        │
│ 7 at packing stations   │     [View Station Status]     │
├─────────────────────────────────────────────────────────┤
│ Active Staff           │     18 / 24                   │
│ 6 on shift              │     [View Staff]              │
├─────────────────────────────────────────────────────────┤
│ Today's Pickups        │     5 Scheduled               │
│ Next: 9:00 AM           │     [View Schedule]           │
└─────────────────────────────────────────────────────────┘

Activity Timeline:
07:28 AM - New order received from TechZone Electronics
07:25 AM - Pick ticket completed by Emmanuel Adebayo
07:22 AM - Package received from vendor FashionHub Nigeria
07:15 AM - Staff member Grace Okonkwo clocked in
07:10 AM - Order status updated to "Awaiting Pickup"
```

#### Step 4: Review Pending Orders
```
User Action: Clicks [View All] on Pending Orders card

System Response:
Vendor Orders Table displayed:
┌────────────────────────────────────────────────────────────────────┐
│ Order ID │ Vendor            │ Items │ Priority │ Status          │
├────────────────────────────────────────────────────────────────────┤
│ GO-2026-04723 │ TechZone Electronics  │ 3   │ HIGH  │ Ready to Pick    │
│ GO-2026-04722 │ FashionHub Nigeria    │ 5   │ NORMAL│ Processing       │
│ GO-2026-04721 │ HomeEssentials Store  │ 2   │ NORMAL│ Processing       │
│ GO-2026-04720 │ SportsPro Nigeria     │ 8   │ HIGH  │ Ready to Pick    │
│ GO-2026-04719 │ BeautyBliss Cosmetics │ 4   │ NORMAL│ Processing       │
└────────────────────────────────────────────────────────────────────┘

User Action: Clicks on GO-2026-04723 (HIGH priority order)

System Response:
Order Detail Panel opens:
- Order ID: GO-2026-04723
- Vendor: TechZone Electronics
- Customer: John Doe (Lagos, Island)
- Items: 3 items
  1. Wireless Bluetooth Headphones x1 (SKU: TECH-001-BT)
  2. USB-C Charging Cable x2 (SKU: TECH-002-USB)
  3. Phone Stand Holder x1 (SKU: TECH-003-STAND)
- Order Time: February 15, 2026, 07:28 AM
- Delivery Deadline: February 15, 2026, 02:00 PM (Same-day)
- Special Instructions: Fragile items - handle with care
```

#### Step 5: Assign Order to Staff
```
User Action: Clicks [Assign to Staff] button

System Response:
Staff Selection Modal displayed:
┌────────────────────────────────────────────────────────────────┐
│ Select Available Staff Member                                   │
├────────────────────────────────────────────────────────────────┤
│ ┌────────────────────────────────────────────────────────────┐ │
│ │ Emmanuel Adebayo                            [Available]     │ │
│ │ Active Tasks: 2 | Completed Today: 15                       │ │
│ │ Rating: 4.8/5                              [Select]         │ │
│ └────────────────────────────────────────────────────────────┘ │
│ ┌────────────────────────────────────────────────────────────┐ │
│ │ Grace Okonkwo                                [Available]     │ │
│ │ Active Tasks: 1 | Completed Today: 12                       │ │
│ │ Rating: 4.9/5                              [Select]         │ │
│ └────────────────────────────────────────────────────────────┘ │
│ ┌────────────────────────────────────────────────────────────┐ │
│ │ Ibrahim Musa                               [On Break]       │ │
│ │ Active Tasks: 0 | Completed Today: 18                       │ │
│ │ Rating: 4.7/5                                                │ │
│ └────────────────────────────────────────────────────────────┘ │
└────────────────────────────────────────────────────────────────┘

User Action: Selects Emmanuel Adebayo
Click: [Confirm Assignment]

System Response:
- Success notification: "Order GO-2026-04723 assigned to Emmanuel Adebayo"
- Pick ticket generated and sent to staff mobile app
- Real-time update: Order status changed to "Picking in Progress"
```

#### Step 6: Monitor Staff Activity
```
User Action: Navigates to Staff Directory

System Response:
Staff Directory displayed with real-time status:
┌──────────────────────────────────────────────────────────────────┐
│ Staff Member          │ Status      │ Active Tasks │ Completed   │
├──────────────────────────────────────────────────────────────────┤
│ Emmanuel Adebayo      │ Picking     │ 3            │ 15          │
│ Grace Okonkwo         │ Packing     │ 1            │ 12          │
│ Ibrahim Musa          │ On Break    │ 0            │ 18          │
│ Chioma Eze            │ Receiving   │ 1            │ 8           │
│ David Nnamdi          │ Idle        │ 0            │ 14          │
└──────────────────────────────────────────────────────────────────┘

User Action: Clicks on Emmanuel Adebayo

System Response:
Staff Detail Panel:
- Name: Emmanuel Adebayo
- Role: Picker/Packer
- Shift: Morning (6:00 AM - 2:00 PM)
- Status: Currently Picking
- Current Task: Order GO-2026-04723
  * Started: 07:35 AM
  * Progress: 1 of 3 items picked
  * Location: Zone B, Row 12, Shelf 4
- Today's Performance:
  * Orders Picked: 15
  * Items Picked: 47
  * Average Pick Time: 4.2 minutes/order
```

#### Step 7: Check Incoming Packages
```
User Action: Navigates to Inbound Packages

System Response:
Inbound Package Queue:
┌────────────────────────────────────────────────────────────────────┐
│ Package ID │ Vendor          │ Items │ Status      │ Received By  │
├────────────────────────────────────────────────────────────────────┤
│ PKG-2026-1023 │ FashionHub Nigeria  │ 25   │ Received    │ Chioma Eze   │
│ PKG-2026-1024 │ TechZone Electronics │ 18   │ Verifying  │ Chioma Eze   │
│ PKG-2026-1025 │ HomeEssentials Store │ 12   │ In Transit │ -            │
└────────────────────────────────────────────────────────────────────┘

User Action: Clicks on PKG-2026-1024

System Response:
Package Detail Panel:
- Package ID: PKG-2026-1024
- Vendor: TechZone Electronics
- Items: 18 items
  * Wireless Headphones x10
  * USB-C Cables x5
  * Phone Stands x3
- Status: Currently verifying
- Received By: Chioma Eze
- Received At: 07:22 AM
- Expected Storage Location: Zone B, Row 15, Shelf 2-4
```

---

## Mock Flow 2: Morning Operations Check (Personal Storage Mode)

**User**: Chinedu Okafor (Storage Manager)
**Scenario**: Starting day with overview of Personal Storage facility operations

### Flow Steps:

#### Step 1: Login and Facility Mode Selection
```
User Action: Chinedu navigates to warehouse.gogidix.com
Enter credentials: chinedu.okafor@storagepartner.com / *********
Selects: "Personal Storage Mode"
Click: Continue Button

System Response:
- Dashboard loads with Personal Storage view
- Today's date: February 15, 2026, 8:00 AM
```

#### Step 2: Dashboard Overview Review
```
Dashboard Display:

Header:
- Facility: Gogidix Self Storage - Abuja
- Mode: Personal Storage
- Date/Time: February 15, 2026 | 08:02 AM

KPI Cards:
┌─────────────────────────────────────────────────────────┐
│ Occupied Units          │     167 / 200                │
│ 83.5% occupied           │     [View Directory]         │
├─────────────────────────────────────────────────────────┤
│ Available Units         │     33                        │
│ Various sizes           │     [View Available]         │
├─────────────────────────────────────────────────────────┤
│ Active Customers        │     143                       │
│ 4 new this month        │     [View Customers]         │
├─────────────────────────────────────────────────────────┤
│ Access Requests Today   │     7                         │
│ 2 pending approval      │     [Review Requests]        │
├─────────────────────────────────────────────────────────┤
│ Payments Due This Week  │     12                        │
│ ₦245,000 total          │     [View Payments]          │
└─────────────────────────────────────────────────────────┘

Activity Timeline:
08:00 AM - Customer Adaeze Nwosu accessed Unit A-015
07:45 AM - Payment received from Ibrahim Kalu (Unit B-042)
07:30 AM - New rental agreement started - Unit C-028
07:15 AM - Staff member Fatima Mohammed completed facility check
07:00 AM - Overnight security scan completed - All secure
```

#### Step 3: Review Storage Units Directory
```
User Action: Clicks [View Directory]

System Response:
Storage Units Directory displayed:
┌──────────────────────────────────────────────────────────────────────────┐
│ Unit    │ Size    │ Customer          │ Status      │ Rent   │ Due Date │
├──────────────────────────────────────────────────────────────────────────┤
│ A-001   │ Small   │ Amaka Obi         │ Occupied    │ ₦15K  │ Feb 20   │
│ A-002   │ Medium  │ Tunde Bakare      │ Occupied    │ ₦25K  │ Feb 18   │
│ A-003   │ Large   │ -                 │ Available   │ ₦40K  │ -        │
│ A-004   │ Small   │ -                 │ Available   │ ₦15K  │ -        │
│ A-005   │ Medium  │ Chioma Ezenwa     │ Occupied    │ ₦25K  │ Feb 25   │
└──────────────────────────────────────────────────────────────────────────┘

Filter Options: [All Units] [Occupied Only] [Available Only] [Overdue]
Sort By: [Unit Number ▼] [Size] [Customer Name] [Due Date]

User Action: Applies filter "Available Only"

System Response:
Available Units displayed (33 units):
┌──────────────────────────────────────────────────────────────────────────┐
│ Unit    │ Size      │ Dimensions        │ Features              │ Action │
├──────────────────────────────────────────────────────────────────────────┤
│ A-003   │ Large     │ 3m x 4m x 3m      │ Climate Control       │ [Rent] │
│ A-004   │ Small     │ 1.5m x 2m x 2.5m  │ Standard              │ [Rent] │
│ A-008   │ Medium    │ 2m x 3m x 2.5m    │ Climate Control       │ [Rent] │
│ A-012   │ Large     │ 3m x 4m x 3m      │ Climate + Alarm       │ [Rent] │
│ B-001   │ Small     │ 1.5m x 2m x 2.5m  │ Ground Floor          │ [Rent] │
└──────────────────────────────────────────────────────────────────────────┘
```

#### Step 4: Process Access Request
```
User Action: Navigates to Access Requests
Clicks on pending request from Ngozi Adeleke

System Response:
Access Request Detail:
┌────────────────────────────────────────────────────────────────────┐
│ Access Request Details                                             │
├────────────────────────────────────────────────────────────────────┤
│ Request ID:        ACC-2026-0245                                   │
│ Customer:          Ngozi Adeleke                                   │
│ Unit:              A-015                                           │
│ Request Type:      Scheduled Access                                │
│ Scheduled Time:    February 15, 2026, 10:00 AM                     │
│ Purpose:           Retrieve seasonal items                         │
│ Additional People: 1 (Son - Adeyemi Adeleke)                      │
│ Access Code:       7842                                            │
│ Requested At:      February 14, 2026, 06:30 PM                     │
│ Status:            Pending Approval                                │
├────────────────────────────────────────────────────────────────────┤
│ Customer Account Status:                                           │
│ - Rental Status: Active (since Jan 2024)                          │
│ - Payment Status: Current - Next due Feb 28                       │
│ - Access History: 23 visits, No violations                        │
│ - Account Standing: Good                                          │
├────────────────────────────────────────────────────────────────────┤
│                      [Approve]    [Deny]    [Request Info]        │
└────────────────────────────────────────────────────────────────────┘

User Action: Clicks [Approve]

System Response:
- Success notification: "Access request approved"
- SMS sent to customer: "Your access to Unit A-015 is confirmed for Feb 15, 10:00 AM. Access code: 7842"
- Access code activated for one-time use
```

#### Step 5: Monitor Facility Access
```
User Action: Navigates to Access Logs

System Response:
Today's Access Log:
┌──────────────────────────────────────────────────────────────────────┐
│ Time    │ Customer          │ Unit    │ Access Type     │ Status    │
├──────────────────────────────────────────────────────────────────────┤
│ 08:00 AM │ Adaeze Nwosu      │ A-015   │ Scheduled       │ Completed │
│ 07:30 AM │ Staff - Fatima    │ All     │ Facility Check  │ Completed │
│ 07:00 AM │ Security System   │ All     │ Overnight Scan  │ Completed │
└──────────────────────────────────────────────────────────────────────┘

Live Access Monitoring:
┌────────────────────────────────────────────────────────────────────┐
│ Currently Active Access Sessions                                   │
├────────────────────────────────────────────────────────────────────┤
│ No active sessions                                                 │
└────────────────────────────────────────────────────────────────────┘
```

---

## Mock Flow 3: Warehouse Staff Daily Operations (Mobile App)

**User**: Emmanuel Adebayo (Warehouse Staff)
**Scenario**: Daily picking and packing operations using mobile app

### Flow Steps:

#### Step 1: Staff Login (Mobile App)
```
User Action: Emmanuel opens Gogidix Warehouse Staff App
Enter credentials: emmanuel.adebayo@staff.gogidix.com / *********
Click: Login Button

System Response:
- Authentication successful
- Clock-in prompt displayed
```

#### Step 2: Clock In for Shift
```
User Action: Clicks [Clock In]

System Response:
┌─────────────────────────────────────────┐
│ Good Morning, Emmanuel!                 │
│                                         │
│ Shift: Morning (6:00 AM - 2:00 PM)     │
│ Clock In Time: 06:00 AM                 │
│                                         │
│ You have 3 orders assigned:             │
│ • GO-2026-04723 (HIGH) - 3 items        │
│ • GO-2026-04718 (NORMAL) - 5 items      │
│ • GO-2026-04715 (NORMAL) - 2 items      │
│                                         │
│ [Start First Order]                     │
└─────────────────────────────────────────┘

User Action: Clicks [Start First Order]
```

#### Step 3: Pick Order - First Item
```
System Response:
Pick Ticket Display:
┌─────────────────────────────────────────────────────────────────────┐
│ Order: GO-2026-04723                        Item 1 of 3            │
│ Vendor: TechZone Electronics                                        │
│ Priority: HIGH                              Started: 07:35 AM       │
├─────────────────────────────────────────────────────────────────────┤
│ Pick Item:                                                           │
│ • Wireless Bluetooth Headphones x1                                  │
│ • SKU: TECH-001-BT                                                  │
│ • Location: Zone B, Row 12, Shelf 4, Bin B                         │
│                                                                     │
│ [Scan Barcode]  [Manual Confirm]  [Item Issue]                      │
└─────────────────────────────────────────────────────────────────────┘

User Action: Navigates to Zone B, Row 12, Shelf 4
Scans barcode on item using device camera

System Response:
┌─────────────────────────────────────────┐
│ ✓ Item Confirmed!                       │
│                                          │
│ Wireless Bluetooth Headphones            │
│ SKU: TECH-001-BT                         │
│ Quantity: 1 of 1                         │
│                                          │
│ [Next Item]                              │
└─────────────────────────────────────────┘
```

#### Step 4: Pick Order - Remaining Items
```
User Action: Clicks [Next Item]

System Response:
Pick Ticket Display - Item 2:
┌─────────────────────────────────────────────────────────────────────┐
│ Order: GO-2026-04723                        Item 2 of 3            │
│ Vendor: TechZone Electronics                                        │
├─────────────────────────────────────────────────────────────────────┤
│ Pick Item:                                                           │
│ • USB-C Charging Cable x2                                           │
│ • SKU: TECH-002-USB                                                 │
│ • Location: Zone B, Row 12, Shelf 5, Bin A                         │
│                                                                     │
│ [Scan Barcode]  [Manual Confirm]  [Item Issue]                      │
└─────────────────────────────────────────────────────────────────────┘

User Action: Scans barcode
Repeats for Item 3 (Phone Stand)

System Response:
┌─────────────────────────────────────────┐
│ ✓ All Items Picked!                     │
│                                          │
│ Order: GO-2026-04723                     │
│ Total Items: 3 of 3                      │
│ Completed in: 5 minutes                  │
│                                          │
│ Proceed to Packing Station 3             │
│                                          │
│ [Confirm Completion]                     │
└─────────────────────────────────────────┘
```

#### Step 5: Receive New Package Assignment
```
User Action: After completing order, checks task queue
Notification displayed:

┌─────────────────────────────────────────┐
│ New Task Assigned                       │
│                                          │
│ Inbound Package: PKG-2026-1024          │
│ Vendor: TechZone Electronics            │
│ Items: 18 items                          │
│ Task: Verify and store received items   │
│ Location: Receiving Dock                 │
│                                          │
│ [Accept Task]  [View Details]           │
└─────────────────────────────────────────┘

User Action: Clicks [Accept Task]

System Response:
Receiving Task Display:
┌─────────────────────────────────────────────────────────────────────┐
│ Package Verification: PKG-2026-1024                                  │
│ Vendor: TechZone Electronics                                        │
├─────────────────────────────────────────────────────────────────────┤
│ Expected Items:                                                      │
│ • Wireless Headphones x10 - SKU: TECH-001-BT                        │
│ • USB-C Cables x5 - SKU: TECH-002-USB                               │
│ • Phone Stands x3 - SKU: TECH-003-STAND                             │
│                                                                     │
│ Scan each item to verify:                                           │
│ Scanned: 0 / 18                                                     │
│                                                                     │
│ [Scan Item]  [Report Discrepancy]  [Complete Verification]          │
└─────────────────────────────────────────────────────────────────────┘
```

#### Step 6: Complete Package Verification
```
User Action: Scans all 18 items one by one

System Response (after each scan):
Item 1: ✓ Wireless Headphones (1/10)
Item 2: ✓ Wireless Headphones (2/10)
...
Item 18: ✓ Phone Stand (3/3)

Final confirmation:
┌─────────────────────────────────────────┐
│ ✓ Package Verified!                     │
│                                          │
│ Package: PKG-2026-1024                  │
│ Items Verified: 18 / 18                  │
│ Discrepancies: None                      │
│                                          │
│ Storage Location:                        │
│ Zone B, Row 15, Shelf 2-4               │
│                                          │
│ [Confirm Storage]  [Print Labels]        │
└─────────────────────────────────────────┘
```

---

## Mock Flow 4: E-commerce Vendor Order Processing

**User**: Sarah Mitchell (Warehouse Manager)
**Scenario**: Processing high-volume order from e-commerce vendor

### Flow Steps:

#### Step 1: Receive Order Notification
```
System Notification (WebSocket):
┌─────────────────────────────────────────────────────────────────────┐
│ New Order Received                                                   │
├─────────────────────────────────────────────────────────────────────┤
│ Vendor: FashionHub Nigeria                                          │
│ Order ID: GO-2026-04750                                             │
│ Items: 12 items (5 products)                                        │
│ Priority: HIGH (Express Delivery)                                   │
│ Deadline: Today, 1:00 PM                                            │
│                                                                     │
│ [View Order]  [Assign Staff]                                        │
└─────────────────────────────────────────────────────────────────────┘

User Action: Clicks [View Order]
```

#### Step 2: Review Order Details
```
System Response:
Order Detail Panel:
┌─────────────────────────────────────────────────────────────────────┐
│ Order Details: GO-2026-04750                                        │
├─────────────────────────────────────────────────────────────────────┤
│ Vendor Information:                                                 │
│ • Company: FashionHub Nigeria                                       │
│ • Contact: Amaka Obi (Operations Manager)                           │
│ • Storage Type: Gogidix Warehousing                                 │
│                                                                     │
│ Order Information:                                                   │
│ • Customer: Aisha Bello (Port Harcourt)                             │
│ • Items: 12 items across 5 products                                 │
│ • Order Value: ₦85,500                                              │
│ • Shipping: Express (Same-day for PH)                               │
│ • Deadline: February 15, 2026, 1:00 PM                              │
│                                                                     │
│ Items to Pick:                                                       │
│ 1. Women's Handbag - Leather Brown x1 (SKU: FASH-001-HB)            │
│    Zone A, Row 5, Shelf 2, Bin A                                    │
│ 2. Men's Leather Belt x2 (SKU: FASH-002-BL)                         │
│    Zone A, Row 5, Shelf 3, Bin C                                    │
│ 3. Sunglasses - Designer x1 (SKU: FASH-003-SG)                      │
│    Zone A, Row 6, Shelf 1, Bin B                                    │
│ 4. Women's Scarf - Silk x3 (SKU: FASH-004-SC)                       │
│    Zone A, Row 6, Shelf 2, Bin A                                    │
│ 5. Men's Watch - Analog x5 (SKU: FASH-005-WA)                       │
│    Zone A, Row 7, Shelf 1, Bin D                                    │
│                                                                     │
│ Special Instructions:                                                │
│ • Gift wrapping required                                            │
│ • Include gift note (provided by customer)                          │
│ • Fragile items (sunglasses) - extra padding                        │
│                                                                     │
│ [Assign to Picker]  [View Inventory]  [Contact Vendor]              │
└─────────────────────────────────────────────────────────────────────┘
```

#### Step 3: Assign Multiple Staff for Bulk Order
```
User Action: Clicks [Assign to Picker]

System Response:
Staff Assignment Modal:
┌─────────────────────────────────────────────────────────────────────┐
│ Assign Order to Staff Member(s)                                     │
├─────────────────────────────────────────────────────────────────────┤
│ Order: GO-2026-04750                                                │
│ Total Items: 12 (in 5 different locations)                          │
│ Suggested: Split among 2 pickers for efficiency                     │
│                                                                     │
│ Available Staff:                                                     │
│ ┌───────────────────────────────────────────────────────────────┐  │
│ │ Grace Okonkwo                                 [Select]         │  │
│ │ Zone Assignment: A (Rows 1-7) | Active Tasks: 1              │  │
│ └───────────────────────────────────────────────────────────────┘  │
│ ┌───────────────────────────────────────────────────────────────┐  │
│ │ David Nnamdi                                 [Select]         │  │
│ │ Zone Assignment: A (Rows 1-7) | Active Tasks: 0              │  │
│ └───────────────────────────────────────────────────────────────┘  │
│                                                                     │
│ Split Option:  [Single Picker]    [Split Between 2]                │
│                                                                     │
│ [Cancel]  [Confirm Assignment]                                      │
└─────────────────────────────────────────────────────────────────────┘

User Action: Selects both Grace and David
Selects: [Split Between 2]
Clicks: [Confirm Assignment]

System Response:
- Success notification: "Order split and assigned to 2 staff members"
- Pick tickets generated:
  * Grace: Items 1, 2 (Zone A, Row 5)
  * David: Items 3, 4, 5 (Zone A, Rows 6-7)
- Real-time update sent to both staff mobile apps
```

#### Step 4: Monitor Picking Progress
```
User Action: Navigates to Active Orders monitoring

System Response:
Real-time Order Status:
┌─────────────────────────────────────────────────────────────────────┐
│ Order: GO-2026-04750                          HIGH Priority        │
│ Progress: 4 of 12 items picked (33%)                               │
│ Time Remaining: 2 hours 45 minutes                                  │
├─────────────────────────────────────────────────────────────────────┤
│ Staff Assignment:                                                   │
│ ┌─────────────────────────────────────────────────────────────────┐ │
│ │ Grace Okonkwo                                                    │ │
│ │ Progress: 3 of 3 items ✓ COMPLETE                               │ │
│ │ Items: Handbag, Belts (2)                                       │ │
│ │ Completed: 07:58 AM (6 minutes)                                 │ │
│ └─────────────────────────────────────────────────────────────────┘ │
│ ┌─────────────────────────────────────────────────────────────────┐ │
│ │ David Nnamdi                                                     │ │
│ │ Progress: 1 of 9 items picking...                               │ │
│ │ Current: Sunglasses (Zone A, Row 6, Shelf 1)                    │ │
│ │ Started: 07:59 AM                                               │ │
│ └─────────────────────────────────────────────────────────────────┘ │
│                                                                     │
│ [Reassign Items]  [Send Reminder]  [View Details]                   │
└─────────────────────────────────────────────────────────────────────┘
```

#### Step 5: Complete Order at Packing Station
```
Wait for picking to complete...

System Notification:
┌─────────────────────────────────────────┐
│ Order Ready for Packing                  │
│                                          │
│ Order: GO-2026-04750                     │
│ All items picked - 12 of 12              │
│ Take to Packing Station 2                │
│                                          │
│ [View Packing Instructions]              │
└─────────────────────────────────────────┘

User Action: Clicks [View Packing Instructions]

System Response:
Packing Instructions:
┌─────────────────────────────────────────────────────────────────────┐
│ Packing Station Instructions: GO-2026-04750                         │
├─────────────────────────────────────────────────────────────────────┤
│ Items Picked:                                                       │
│ ✓ Women's Handbag - Leather Brown x1                               │
│ ✓ Men's Leather Belt x2                                            │
│ ✓ Sunglasses - Designer x1                                         │
│ ✓ Women's Scarf - Silk x3                                          │
│ ✓ Men's Watch - Analog x5                                          │
│                                                                     │
│ Special Requirements:                                               │
│ • Gift wrapping: YES (Use premium gift wrap)                       │
│ • Gift note: "Happy Birthday from Mom!"                            │
│ • Fragile handling: Sunglasses need bubble wrap                    │
│ • Box Size: Large (Order total size)                               │
│                                                                     │
│ Shipping Label:                                                     │
│ • Courier: SwiftExpress (Express Delivery)                         │
│ • Destination: Port Harcourt                                        │
│ • Service: Same-day (if before 12 PM)                              │
│                                                                     │
│ [Print Shipping Label]  [Confirm Packed]  [Report Issue]           │
└─────────────────────────────────────────────────────────────────────┘
```

#### Step 6: Schedule Courier Pickup
```
User Action: After packing, clicks [Schedule Courier Pickup]

System Response:
Courier Scheduling Modal:
┌─────────────────────────────────────────────────────────────────────┐
│ Schedule Courier Pickup: GO-2026-04750                              │
├─────────────────────────────────────────────────────────────────────┤
│ Package Details:                                                    │
│ • Weight: 3.5 kg                                                   │
│ • Dimensions: 30x25x15 cm                                          │
│ • Value: ₦85,500                                                   │
│                                                                     │
│ Available Couriers for Express Delivery:                           │
│ ┌─────────────────────────────────────────────────────────────────┐│
│ │ SwiftExpress                                   [Select]          ││
│ │ Pickup: 11:00 AM - 12:00 PM | Delivery: Same day               ││
│ │ Rate: ₦3,500                                                    ││
│ └─────────────────────────────────────────────────────────────────┘│
│ ┌─────────────────────────────────────────────────────────────────┐│
│ │ FastTrack Logistics                            [Select]          ││
│ │ Pickup: 11:30 AM - 12:30 PM | Delivery: Same day               ││
│ │ Rate: ₦3,200                                                    ││
│ └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│ Preferred Pickup Time:                                              │
│ [10:00 AM - 11:00 AM] [11:00 AM - 12:00 PM] [12:00 PM - 1:00 PM]  │
│                                                                     │
│ Pickup Instructions:                                                │
│ [ ] Call upon arrival                                               │
│ [x] Meet at receiving dock                                         │
│                                                                     │
│ [Schedule Pickup]  [Cancel]                                         │
└─────────────────────────────────────────────────────────────────────┘

User Action: Selects SwiftExpress
Selects: [11:00 AM - 12:00 PM]
Clicks: [Schedule Pickup]

System Response:
- Success notification: "Pickup scheduled with SwiftExpress for 11:00 AM - 12:00 PM"
- Notification sent to courier partner
- Order status updated: "Awaiting Courier Pickup"
- Vendor (FashionHub) notified via email
```

---

## Mock Flow 5: Personal Storage Customer Onboarding

**User**: Chinedu Okafor (Storage Manager)
**Scenario**: Onboarding new personal storage customer

### Flow Steps:

#### Step 1: Receive New Rental Inquiry
```
System Notification:
┌─────────────────────────────────────────────────────────────────────┐
│ New Storage Inquiry                                                 │
├─────────────────────────────────────────────────────────────────────┤
│ Customer: Nnamdi Okoro                                              │
│ Phone: +234 802 345 6789                                           │
│ Email: nnamdi.okoro@email.com                                      │
│ Interested Size: Medium                                             │
│ Preferred Start: February 20, 2026                                  │
│ Duration: 6 months (minimum)                                       │
│                                                                     │
│ [View Inquiry]  [Contact Customer]                                  │
└─────────────────────────────────────────────────────────────────────┘

User Action: Clicks [View Inquiry]
```

#### Step 2: Review Inquiry and Available Units
```
System Response:
Customer Inquiry Detail:
┌─────────────────────────────────────────────────────────────────────┐
│ Customer Information: Nnamdi Okoro                                  │
├─────────────────────────────────────────────────────────────────────┤
│ Inquiry Details:                                                    │
│ • Name: Nnamdi Okoro                                                │
│ • Phone: +234 802 345 6789                                         │
│ • Email: nnamdi.okoro@email.com                                    │
│ • Address: Ikeja, Lagos                                            │
│                                                                     │
│ Storage Requirements:                                               │
│ • Size: Medium (2m x 3m x 2.5m minimum)                            │
│ • Items: Household goods, furniture, boxes                          │
│ • Duration: 6+ months                                              │
│ • Preferred Start: February 20, 2026                                │
│ • Features Needed: Climate control preferred                        │
│                                                                     │
│ Available Medium Units:                                             │
│ ┌─────────────────────────────────────────────────────────────────┐│
│ │ A-008                          [Select for Tour]                 ││
│ │ Medium | 2m x 3m x 2.5m      | ₦25,000/month                   ││
│ │ Features: Climate Control   | Ground Floor Access              ││
│ └─────────────────────────────────────────────────────────────────┘│
│ ┌─────────────────────────────────────────────────────────────────┐│
│ │ A-010                          [Select for Tour]                 ││
│ │ Medium | 2m x 3m x 2.5m      | ₦25,000/month                   ││
│ │ Features: Climate Control   | First Floor (Elevator Access)    ││
│ └─────────────────────────────────────────────────────────────────┘│
│ ┌─────────────────────────────────────────────────────────────────┐│
│ │ B-005                          [Select for Tour]                 ││
│ │ Medium | 2.2m x 3.2m x 2.5m  | ₦28,000/month                   ││
│ │ Features: Climate + Alarm   | Ground Floor, Corner Unit        ││
│ └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│ [Schedule Tour]  [Create Rental Agreement]  [Contact Customer]      │
└─────────────────────────────────────────────────────────────────────┘
```

#### Step 3: Schedule Facility Tour
```
User Action: Clicks [Schedule Tour]

System Response:
Tour Scheduling Modal:
┌─────────────────────────────────────────────────────────────────────┐
│ Schedule Facility Tour: Nnamdi Okoro                                │
├─────────────────────────────────────────────────────────────────────┤
│ Customer Contact: +234 802 345 6789                                 │
│                                                                     │
│ Preferred Tour Date/Time:                                           │
│ Date: [February 17, 2026 ▼]                                        │
│ Time: [10:00 AM ▼]                                                 │
│                                                                     │
│ Units to Show:                                                      │
│ ☑ A-008 (Medium, Climate, Ground Floor)                            │
│ ☑ B-005 (Medium, Climate + Alarm, Corner)                          │
│                                                                     │
│ Tour Guide:                                                         │
│ [Auto-assign] [Select Staff]                                       │
│                                                                     │
│ Additional Notes:                                                    │
│ [Enter notes about customer requirements...]                        │
│                                                                     │
│ [Schedule Tour]  [Save as Draft]  [Cancel]                          │
└─────────────────────────────────────────────────────────────────────┘

User Action: Enters date, time, units
Clicks: [Schedule Tour]

System Response:
- Success notification: "Tour scheduled for February 17, 10:00 AM"
- Confirmation SMS sent to customer
- Calendar event created
```

#### Step 4: Create Rental Agreement (After Tour)
```
User Action: Customer completed tour, wants Unit B-005
Navigates to Customers section
Clicks: [New Rental Agreement]

System Response:
New Rental Agreement Form:
┌─────────────────────────────────────────────────────────────────────┐
│ Create New Rental Agreement                                         │
├─────────────────────────────────────────────────────────────────────┤
│ Step 1: Customer Information                                        │
│                                                                     │
│ First Name: [Nnamdi                      ]                          │
│ Last Name: [Okoro                       ]                          │
│ Phone: [+234 802 345 6789            ]                          │
│ Email: [nnamdi.okoro@email.com       ]                          │
│ Address: [12, Adeniyi Jones, Ikeja  ]                          │
│             [Lagos, Nigeria           ]                          │
│                                                                     │
│ ID Type: [National ID ▼]                                            │
│ ID Number: [12345678901            ]                               │
│ Upload ID: [Choose File]                                            │
│                                                                     │
│ Emergency Contact:                                                  │
│ Name: [Chinedu Okoro               ]                               │
│ Relationship: [Brother               ]                              │
│ Phone: [+234 803 456 7890        ]                               │
│                                                                     │
│ [Next: Unit Selection]                                              │
└─────────────────────────────────────────────────────────────────────┘

User Action: Fills in customer information
Clicks: [Next: Unit Selection]
```

#### Step 5: Select Unit and Terms
```
System Response:
Step 2: Unit Selection & Terms
┌─────────────────────────────────────────────────────────────────────┐
│ Select Unit:                                                        │
│ Available Units: [Medium ▼]                                         │
│                                                                     │
│ ┌─────────────────────────────────────────────────────────────────┐│
│ │ B-005                                          [Select Unit]     ││
│ │ Size: 2.2m x 3.2m x 2.5m | Monthly Rate: ₦28,000               ││
│ │ Features: Climate Control + Alarm System                       ││
│ └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│ Rental Terms:                                                       │
│ Start Date: [February 20, 2026 ▼]                                  │
│ Minimum Duration: [6 months ▼]                                     │
│                                                                     │
│ Payment Schedule:                                                   │
│ ☑ Monthly ☐ Quarterly ☐ Annual                                     │
│                                                                     │
│ Pricing Summary:                                                    │
│ • Monthly Rent: ₦28,000                                            │
│ • Security Deposit: ₦28,000 (1 month)                              │
│ • Administrative Fee: ₦5,000                                       │
│ • First Payment Total: ₦61,000 (Rent + Deposit + Admin)            │
│                                                                     │
│ Payment Method:                                                     │
│ ☑ Bank Transfer ☑ Card Payment ☑ USSD                             │
│                                                                     │
│ Agreement Terms: ☑ I agree to the terms and conditions             │
│                                                                     │
│ [Back]  [Generate Agreement]  [Complete Onboarding]                │
└─────────────────────────────────────────────────────────────────────┘

User Action: Selects Unit B-005, fills terms
Clicks: [Complete Onboarding]
```

#### Step 6: Complete Customer Registration
```
System Response:
┌─────────────────────────────────────────────────────────────────────┐
│ Customer Registration Complete!                                     │
├─────────────────────────────────────────────────────────────────────┤
│ ✓ Customer account created                                         │
│ ✓ Rental agreement generated                                       │
│ ✓ Unit B-005 assigned                                              │
│ ✓ Access code generated                                            │
│                                                                     │
│ Customer Details:                                                   │
│ • Name: Nnamdi Okoro                                               │
│ • Unit: B-005                                                      │
│ • Start Date: February 20, 2026                                     │
│ • Access Code: 5839 (Auto-generated, changeable)                   │
│                                                                     │
│ Next Steps:                                                         │
│ 1. Send welcome email with access instructions                      │
│ 2. Schedule payment collection (₦61,000)                            │
│ 3. Activate unit access for Feb 20                                  │
│                                                                     │
│ [Send Welcome Email]  [Print Agreement]  [View Customer Profile]    │
└─────────────────────────────────────────────────────────────────────┘

User Action: Clicks [Send Welcome Email]

System Response:
- Success notification: "Welcome email sent to nnamdi.okoro@email.com"
- SMS sent with access code: 5839
- Customer profile activated
- Unit B-005 status changed to "Reserved - Starting Feb 20"
```

---

## Mock Flow 6: Inbound Package Processing from E-commerce Vendor

**User**: Sarah Mitchell (Warehouse Manager) & Chioma Eze (Receiving Staff)
**Scenario**: Processing inbound shipment from e-commerce vendor using Gogidix warehousing

### Flow Steps:

#### Step 1: Receive Advance Shipping Notice
```
System Notification:
┌─────────────────────────────────────────────────────────────────────┐
│ Advance Shipping Notice (ASN)                                      │
├─────────────────────────────────────────────────────────────────────┤
│ Vendor: TechZone Electronics                                       │
│ Shipment ID: SHP-TECH-2026-0245                                    │
│ ETA: February 15, 2026, 09:00 AM                                   │
│                                                                     │
│ Expected Contents:                                                  │
│ • 50x Wireless Headphones (TECH-001-BT)                           │
│ • 30x USB-C Cables (TECH-002-USB)                                  │
│ • 20x Phone Stands (TECH-003-STAND)                                │
│ • 15x Laptop Sleeves (TECH-004-LS) - NEW ITEM                     │
│                                                                     │
│ Total Items: 115 items (4 SKUs)                                    │
│ Estimated Weight: 45 kg                                            │
│                                                                     │
│ [View ASN Details]  [Prepare Receiving]                             │
└─────────────────────────────────────────────────────────────────────┘

User Action: Sarah clicks [Prepare Receiving]
```

#### Step 2: Assign Receiving Staff
```
System Response:
Receiving Preparation:
┌─────────────────────────────────────────────────────────────────────┐
│ Prepare Receiving: SHP-TECH-2026-0245                               │
├─────────────────────────────────────────────────────────────────────┤
│ Shipment Details:                                                   │
│ • Vendor: TechZone Electronics                                     │
│ • ETA: Today, 9:00 AM                                              │
│ • Items: 115 items (4 SKUs)                                        │
│                                                                     │
│ Assign Receiving Staff:                                             │
│ Available Receiving Staff:                                          │
│ ┌─────────────────────────────────────────────────────────────────┐│
│ │ Chioma Eze                                     [Assign]         ││
│ │ Role: Receiving Specialist | Current Tasks: 0                  ││
│ └─────────────────────────────────────────────────────────────────┘│
│ ┌─────────────────────────────────────────────────────────────────┐│
│ │ Ibrahim Musa                                   [Assign]         ││
│ │ Role: General Staff | Current Tasks: 1 (Completing soon)       ││
│ └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│ Suggested Storage:                                                  │
│ Based on SKU categories, suggest Zone B:                           │
│ • Row 15: TECH-001 (Headphones)                                    │
│ • Row 16: TECH-002 (Cables), TECH-003 (Stands)                     │
│ • Row 17: TECH-004 (Laptop Sleeves) - NEW                          │
│                                                                     │
│ [Confirm Assignment]  [Modify Storage]                              │
└─────────────────────────────────────────────────────────────────────┘

User Action: Assigns Chioma Eze
Clicks: [Confirm Assignment]

System Response:
- Task assigned to Chioma Eze (Mobile App notification)
- Receiving Dock A reserved for this shipment
```

#### Step 3: Shipment Arrival (Staff Mobile App)
```
User: Chioma Eze (at Receiving Dock A)
Action: Opens mobile app task

System Response (Mobile):
┌─────────────────────────────────────────────────────────────────────┐
│ Receiving Task: SHP-TECH-2026-0245                                  │
├─────────────────────────────────────────────────────────────────────┤
│ Vendor: TechZone Electronics                                        │
│ Location: Receiving Dock A                                          │
│ Status: Awaiting Arrival                                            │
│                                                                     │
│ Expected Items:                                                     │
│ • 50x Wireless Headphones (TECH-001-BT)                           │
│ • 30x USB-C Cables (TECH-002-USB)                                  │
│ • 20x Phone Stands (TECH-003-STAND)                                │
│ • 15x Laptop Sleeves (TECH-004-LS) - NEW                           │
│                                                                     │
│ When shipment arrives:                                              │
│ 1. Confirm delivery                                                 │
│ 2. Scan delivery waybill                                            │
│ 3. Verify each item                                                │
│                                                                     │
│ [Shipment Arrived - Start Receiving]                                │
└─────────────────────────────────────────────────────────────────────┘

User Action (Chioma): Clicks [Shipment Arrived - Start Receiving]
```

#### Step 4: Verify Delivery Waybill
```
System Response:
Waybill Scanning:
┌─────────────────────────────────────────────────────────────────────┐
│ Scan Delivery Waybill                                              │
├─────────────────────────────────────────────────────────────────────┤
│ Please scan the waybill barcode on the shipping document           │
│                                                                     │
│ [Scan Barcode]  [Enter Manually]                                    │
└─────────────────────────────────────────────────────────────────────┘

User Action (Chioma): Scans waybill barcode

System Response:
┌─────────────────────────────────────────┐
│ ✓ Waybill Verified!                     │
│                                          │
│ Waybill: WB-TECH-0245                   │
│ Matches ASN: SHP-TECH-2026-0245         │
│                                          │
│ Begin item verification                 │
│                                          │
│ [Start Verification]                    │
└─────────────────────────────────────────┘
```

#### Step 5: Item Verification Process
```
User Action: Clicks [Start Verification]

System Response:
Item Verification Display:
┌─────────────────────────────────────────────────────────────────────┐
│ Item Verification: 1 of 4 SKUs                                       │
├─────────────────────────────────────────────────────────────────────┤
│ Verify: Wireless Headphones (TECH-001-BT)                           │
│ Expected: 50 units                                                  │
│                                                                     │
│ Scan each item to verify quantity:                                  │
│ Scanned: 0 / 50                                                     │
│                                                                     │
│ [Scan Item]  [Manual Count]  [Report Discrepancy]                   │
└─────────────────────────────────────────────────────────────────────┘

User Action (Chioma): Scans items one by one
(Bulk scan available for same items)

After scanning all 50:
┌─────────────────────────────────────────┐
│ ✓ SKU Verified!                         │
│                                          │
│ Wireless Headphones (TECH-001-BT)        │
│ Expected: 50 | Scanned: 50              │
│ Status: Match                            │
│                                          │
│ [Next SKU]                               │
└─────────────────────────────────────────┘

Repeats for remaining SKUs...
```

#### Step 6: Handle New Item (TECH-004)
```
System Response:
Item Verification: 4 of 4 SKUs
┌─────────────────────────────────────────────────────────────────────┐
│ Verify: Laptop Sleeves (TECH-004-LS) - NEW SKU                      │
├─────────────────────────────────────────────────────────────────────┤
│ ⚠ This is a new SKU. Create storage location first.                 │
│                                                                     │
│ Expected: 15 units                                                  │
│                                                                     │
│ Create Storage Location:                                            │
│ Zone: [B ▼] Row: [17 ▼] Shelf: [1 ▼] Bin: [A-C ▼]                 │
│                                                                     │
│ [Create & Continue]  [Verify First to Confirm]                      │
└─────────────────────────────────────────────────────────────────────┘

User Action (Chioma): Selects storage location
Clicks: [Create & Continue]

System Response:
- New SKU location created in system
- Continues with scanning 15 laptop sleeves
```

#### Step 7: Complete Receiving
```
After all items verified:

System Response:
┌─────────────────────────────────────────┐
│ ✓ Shipment Verified!                    │
│                                          │
│ Shipment: SHP-TECH-2026-0245            │
│ Total SKUs: 4 of 4                      │
│ Total Items: 115 of 115                 │
│ Discrepancies: None                     │
│                                          │
│ Status: Ready for Put-away              │
│                                          │
│ Generate storage labels?                │
│                                          │
│ [Generate Labels] [Skip] [View Report]  │
└─────────────────────────────────────────┘

User Action (Chioma): Clicks [Generate Labels]

System Response:
- 115 storage labels generated for printing
- Put-away task created
- Vendor inventory updated in real-time
- Manager (Sarah) notified of completion
```

#### Step 8: Manager Confirmation
```
User: Sarah Mitchell (Dashboard)
Action: Receives completion notification

System Response:
┌─────────────────────────────────────────────────────────────────────┐
│ Shipment Received Successfully                                       │
├─────────────────────────────────────────────────────────────────────┤
│ Shipment: SHP-TECH-2026-0245                                       │
│ Vendor: TechZone Electronics                                       │
│ Received By: Chioma Eze                                             │
│ Completed At: 09:45 AM                                              │
│                                                                     │
│ Summary:                                                            │
│ • 4 SKUs received                                                   │
│ • 115 items verified (100% match)                                   │
│ • New SKU added: TECH-004-LS (Laptop Sleeves)                      │
│ • All items in Receiving Dock A - Ready for put-away                │
│                                                                     │
│ Vendor (TechZone) has been notified                                 │
│ Their inventory updated in real-time                                │
│                                                                     │
│ [View Put-away Task]  [Notify Vendor of Completion]                 │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Mock Flow 7: Personal Storage Customer Access & Management

**User**: Chinedu Okafor (Storage Manager)
**Scenario**: Customer access and facility management

### Flow Steps:

#### Step 1: Handle Walk-in Customer Access Request
```
In-person Customer: Adaeze Nwosu arrives at facility
Wants to access Unit A-015 unexpectedly

User Action: Chinedu opens customer access panel

System Response:
Customer Access Panel:
┌─────────────────────────────────────────────────────────────────────┐
│ Grant Access to Customer                                            │
├─────────────────────────────────────────────────────────────────────┤
│ Customer Lookup:                                                     │
│ [Search by Name] [Search by Unit] [Search by Phone]                 │
│                                                                     │
│ ┌─────────────────────────────────────────────────────────────────┐│
│ │ Adaeze Nwosu                                   [Select]          ││
│ │ Unit: A-015 | Phone: ***4567 | Customer since: Jan 2024        ││
│ └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│ Customer Account Status:                                            │
│ • Rental Status: ACTIVE                                            │
│ • Payment Status: CURRENT (Next due: Feb 28)                       │
│ • Access History: 23 visits, No issues                             │
│                                                                     │
│ Access Grant Options:                                               │
│ ☑ Standard Access (Customer only)                                   │
│ ☐ With Guest (1 additional person)                                 │
│ ☐ With Vehicle (Loading/Unloading)                                 │
│                                                                     │
│ Access Duration:                                                    │
│ ☑ Standard (1 hour) ☐ Extended (2 hours) ☐ Full day                │
│                                                                     │
│ Purpose of Visit:                                                   │
│ [Retrieve items ☰]                                                 │
│                                                                     │
│ [Generate Access Code]  [Direct Grant Access]                       │
└─────────────────────────────────────────────────────────────────────┘

User Action: Verifies customer in person
Selects: Standard Access
Clicks: [Direct Grant Access]

System Response:
┌─────────────────────────────────────────┐
│ ✓ Access Granted!                       │
│                                          │
│ Customer: Adaeze Nwosu                   │
│ Unit: A-015                              │
│ Access Type: Standard                    │
│ Duration: 1 hour                         │
│                                          │
│ Access Code: 4821                       │
│ Valid until: 10:45 AM                    │
│                                          │
│ [Print Pass] [Notify Security]           │
└─────────────────────────────────────────┘

Customer uses code 4821 to access Unit A-015
```

#### Step 2: Monitor Active Access Session
```
User Action: Navigates to Live Access Monitoring

System Response:
┌─────────────────────────────────────────────────────────────────────┐
│ Active Access Sessions - February 15, 2026                          │
├─────────────────────────────────────────────────────────────────────┤
│ ┌─────────────────────────────────────────────────────────────────┐│
│ │ Session: ACT-2026-0156                      In Progress         ││
│ │ Customer: Adaeze Nwosu                                          ││
│ │ Unit: A-015                                                     ││
│ │ Started: 09:45 AM                                               ││
│ │ Duration: 15 minutes / 1 hour                                   ││
│ │ Access Type: Standard (Customer only)                           ││
│ │                                                                  ││
│ │ [Extend Time] [Contact Customer] [End Session]                  ││
│ └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│ Security Status:                                                    │
│ • Entry Log: 09:45 AM - Code 4821 verified                         │
│ • Unit Access: Granted - Door unlocked                              │
│ • Facility Location: Zone A, Ground Floor                           │
│ • CCTV: Camera A-015 active                                         │
└─────────────────────────────────────────────────────────────────────┘
```

#### Step 3: Customer Completes Access Session
```
Customer leaves facility at 10:30 AM

System Auto-Response:
┌─────────────────────────────────────────────────────────────────────┐
│ Session Completed                                                    │
├─────────────────────────────────────────────────────────────────────┤
│ Customer: Adaeze Nwosu                                              │
│ Unit: A-015                                                         │
│ Session Duration: 45 minutes                                        │
│ Entry Time: 09:45 AM                                                │
│ Exit Time: 10:30 AM                                                 │
│                                                                     │
│ Actions Taken:                                                       │
│ ✓ Door locked                                                       │
│ ✓ Access code expired                                               │
│ ✓ Session logged to customer history                                │
│                                                                     │
│ [View Customer History]  [Close]                                    │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Mock Flow 8: Staff Management and Performance Tracking

**User**: Sarah Mitchell (Warehouse Manager)
**Scenario**: Managing warehouse staff performance and operations

### Flow Steps:

#### Step 1: Review Daily Staff Performance
```
User Action: Navigates to Staff Performance Dashboard

System Response:
Daily Performance Report - February 15, 2026:
┌─────────────────────────────────────────────────────────────────────┐
│ Staff Performance Summary                                           │
├─────────────────────────────────────────────────────────────────────┤
│ On Duty: 18 / 24 staff                                             │
│ Current Shift: Morning (6:00 AM - 2:00 PM)                          │
│                                                                     │
│ Performance Metrics:                                                │
│ • Orders Completed: 47                                              │
│ • Items Picked: 234                                                │
│ • Packages Received: 12                                            │
│ • Average Pick Time: 4.1 min/order                                  │
│                                                                     │
│ Top Performers Today:                                               │
│ ┌─────────────────────────────────────────────────────────────────┐│
│ │ 1. Grace Okonkwo                              Orders: 18         ││
│ │    Items Picked: 89 | Avg Time: 3.2 min | Rating: 4.9/5        ││
│ ├─────────────────────────────────────────────────────────────────┤│
│ │ 2. Emmanuel Adebayo                           Orders: 15         ││
│ │    Items Picked: 52 | Avg Time: 4.2 min | Rating: 4.8/5        ││
│ ├─────────────────────────────────────────────────────────────────┤│
│ │ 3. Chioma Eze                                 Packages: 12       ││
│ │    Received & Verified: 234 items | Rating: 4.7/5              ││
│ └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│ [View Full Staff List]  [Generate Report]  [Assign Tasks]           │
└─────────────────────────────────────────────────────────────────────┘
```

#### Step 2: Individual Staff Performance Review
```
User Action: Clicks on Emmanuel Adebayo

System Response:
Staff Detail: Emmanuel Adebayo
┌─────────────────────────────────────────────────────────────────────┐
│ Emmanuel Adebayo | Picker/Packer | ID: STF-2024-015                │
├─────────────────────────────────────────────────────────────────────┤
│ Current Status: Picking (Order GO-2026-04752)                       │
│ Shift: Morning (6:00 AM - 2:00 PM) | On duty since 06:00 AM        │
│                                                                     │
│ Today's Performance (Feb 15):                                       │
│ • Orders Picked: 15                                                │
│ • Items Picked: 52                                                │
│ • Average Time: 4.2 min/order                                      │
│ • Accuracy: 100% (0 errors)                                        │
│ • Current Task: Picking Order GO-2026-04752 (2 of 5 items)         │
│                                                                     │
│ This Week (Feb 10-15):                                              │
│ • Total Orders: 87                                                 │
│ • Items Picked: 312                                               │
│ • Weekly Rating: 4.8/5 ⭐⭐⭐⭐⭐                                    │
│ • Attendance: 6/6 shifts                                           │
│                                                                     │
│ Performance Trend:                                                  │
│ [Chart showing last 30 days - consistently above average]           │
│                                                                     │
│ Strengths:                                                          │
│ • High accuracy in picking                                         │
│ • Excellent with fragile items                                     │
│ • Good teamwork (helps other staff)                                │
│                                                                     │
│ Areas for Improvement:                                              │
│ • Slightly above average pick time (target: <4 min)                │
│                                                                     │
│ [Send Message]  [View History]  [Adjust Tasks]  [Schedule Review]   │
└─────────────────────────────────────────────────────────────────────┘
```

#### Step 3: Onboard New Staff Member
```
User Action: Clicks [Add New Staff Member]

System Response:
New Staff Member Registration:
┌─────────────────────────────────────────────────────────────────────┐
│ Register New Staff Member                                           │
├─────────────────────────────────────────────────────────────────────┤
│ Personal Information:                                               │
│ First Name: [Oluwaseun              ]                               │
│ Last Name: [Akinyemi                ]                               │
│ Phone: [+234 804 567 8901         ]                               │
│ Email: [oluwaseun.a@staff.gogidix.com                            ]  │
│ Date of Birth: [March 15, 1992       ]                              │
│                                                                     │
│ Employment Details:                                                 │
│ Role: [Picker/Packer ▼]                                            │
│ Start Date: [February 20, 2026 ▼]                                   │
│ Shift Assignment: [Morning ▼] (6:00 AM - 2:00 PM)                   │
│ Employment Type: [Full-time ▼]                                      │
│                                                                     │
│ Emergency Contact:                                                  │
│ Name: [Funke Akinyemi              ]                               │
│ Relationship: [Spouse                ]                              │
│ Phone: [+234 805 678 9012         ]                               │
│                                                                     │
│ Identification:                                                     │
│ ID Type: [National ID ▼]                                           │
│ ID Number: [98765432110             ]                              │
│ Upload: [Choose File]                                               │
│                                                                     │
│ Bank Details (for salary):                                          │
│ Bank: [Access Bank ▼]                                              │
│ Account Number: [1234567890           ]                            │
│ Account Name: [Auto-verify on save      ]                           │
│                                                                     │
│ [Save & Send Invite]  [Save as Draft]  [Cancel]                     │
└─────────────────────────────────────────────────────────────────────┘

User Action: Fills in all details
Clicks: [Save & Send Invite]

System Response:
┌─────────────────────────────────────────┐
│ ✓ Staff Member Registered!              │
│                                          │
│ Name: Oluwaseun Akinyemi                 │
│ Role: Picker/Packer                       │
│ Start Date: February 20, 2026             │
│ Staff ID: STF-2026-028                   │
│                                          │
│ Next Steps:                               │
│ ✓ Welcome email sent                     │
│ ✓ Mobile app download link sent          │
│ ✓ Training schedule created              │
│ ✓ Badge printing pending                 │
│                                          │
│ [Print Badge] [View Profile] [Close]     │
└─────────────────────────────────────────┘
```

#### Step 4: Manage Shift Scheduling
```
User Action: Navigates to Shift Scheduler

System Response:
Weekly Shift Schedule - Week of Feb 14-20, 2026:
┌─────────────────────────────────────────────────────────────────────┐
│ Shift Overview                                                      │
├─────────────────────────────────────────────────────────────────────┤
│ Morning Shift (6AM-2PM) | Afternoon Shift (2PM-10PM)                │
│                                                                     │
│ Feb 15 (Today) - Morning:                                           │
│ Staff: 18 / 20 scheduled                                             │
│ Absences: 2 (Ibrahim Musa - Sick, Amina Yusuf - Personal)           │
│                                                                     │
│ Feb 15 (Today) - Afternoon:                                         │
│ Staff: 8 / 10 scheduled                                             │
│ Gaps: 2 staff needed                                                │
│                                                                     │
│ Weekly Schedule:                                                    │
│ ┌──────────┬─────────────┬─────────────┬─────────────┬─────────────┐│
│ │ Staff    │ Feb 15      │ Feb 16      │ Feb 17      │ Feb 18      ││
│ │          │ M    A      │ M    A      │ M    A      │ M    A      ││
│ ├──────────┼─────────────┼─────────────┼─────────────┼─────────────┤│
│ │ Grace O  │ ✓    -      │ ✓    ✓      │ ✓    -      │ ✓    ✓      ││
│ │ Emmanuel │ ✓    -      │ ✓    -      │ ✓    ✓      │ -    ✓      ││
│ │ Chioma E │ ✓    -      │ -    ✓      │ ✓    -      │ ✓    -      ││
│ │ David N  │ ✓    ✓      │ ✓    -      │ -    ✓      │ ✓    ✓      ││
│ │ Oluwaseun│ -    -      │ ✓    -      │ ✓    -      │ ✓    -      ││
│ └──────────┴─────────────┴─────────────┴─────────────┴─────────────┘│
│ Legend: ✓ = Scheduled, - = Off                                      │
│                                                                     │
│ [Edit Schedule]  [Fill Open Slots]  [Approve Time-off]              │
└─────────────────────────────────────────────────────────────────────┘

User Action: Clicks [Fill Open Slots]

System Response:
┌─────────────────────────────────────────────────────────────────────┐
│ Fill Open Slot - Afternoon Shift Feb 15                             │
├─────────────────────────────────────────────────────────────────────┤
│ Shift: Afternoon (2:00 PM - 10:00 PM)                               │
│ Date: February 15, 2026                                             │
│ Open Slots: 2                                                       │
│                                                                     │
│ Available Staff (Not scheduled today):                              │
│ ┌─────────────────────────────────────────────────────────────────┐│
│ │ Oluwaseun Akinyemi                 [Assign to Afternoon]        ││
│ │ New hire, starts Feb 20, training available                    ││
│ └─────────────────────────────────────────────────────────────────┘│
│ ┌─────────────────────────────────────────────────────────────────┐│
│ │ Chioma Eze                           [Assign to Afternoon]        ││
│ │ Morning shift today, available for double shift                 ││
│ └─────────────────────────────────────────────────────────────────┘│
│                                                                     │
│ [Call Available Staff]  [Post Open Slot]                            │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Summary

These mock flows demonstrate the comprehensive functionality of the Warehouse Partners Dashboard across both facility types:

### E-commerce Fulfillment Mode:
- Order management from receipt to courier handoff
- Staff task assignment and monitoring
- Inbound package processing and verification
- Real-time inventory synchronization
- Courier integration and scheduling

### Personal Storage Mode:
- Unit management and availability
- Customer onboarding and rental agreements
- Access control and session monitoring
- Payment tracking and account management

### Mobile App Capabilities:
- Order picking with barcode scanning
- Package receiving and verification
- Real-time task updates
- Communication with warehouse management

### Staff Management:
- Performance tracking and metrics
- Shift scheduling
- New staff onboarding
- Task assignment and monitoring

All flows integrate seamlessly with the broader Gogidix ecosystem including e-commerce vendors, courier services, and marketplace platforms.

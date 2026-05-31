# SALES TEAM PARTNERS APP - MOCK FLOW DOCUMENTATION

**Version:** 1.0
**Domain:** Management Domain
**Subdomain:** Sales-Departments
**Frontend:** sales-team-app (Mobile)
**Last Updated:** 2025-02-16

---

## TABLE OF CONTENTS

1. [Mock Scenarios Overview](#1-mock-scenarios-overview)
2. [User Profiles](#2-user-profiles)
3. [Mock Data Sets](#3-mock-data-sets)
4. [API Integration Points](#4-api-integration-points)
5. [End-to-End Flows](#5-end-to-end-flows)

---

## 1. MOCK SCENARIOS OVERVIEW

### 1.1 Scenario Categories

```
┌─────────────────────────────────────────────────────────────────────────┐
│                      MOCK SCENARIO CATEGORIES                            │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│  ONBOARDING SCENARIOS                                                    │
│  • New Sales Partner Registration                                        │
│  • First Login & Setup                                                   │
│  • Territory Assignment                                                  │
│  • Training Completion                                                   │
│                                                                          │
│  PARTNER ONBOARDING SCENARIOS                                            │
│  • Courier Partner Registration                                          │
│  • Warehouse Partner Registration                                        │
│  • E-commerce Vendor Registration                                        │
│  • Influencer Registration                                               │
│                                                                          │
│  CUSTOMER SALES SCENARIOS                                                │
│  • AI Lead Assignment & Contact                                          │
│  • Corporate Deal Creation & Closing                                     │
│  • Individual Customer Acquisition                                       │
│  • Deal Stage Progression                                               │
│                                                                          │
│  COMMISSION SCENARIOS                                                    │
│  • Partner Referral Bonus Earned                                         │
│  • Customer Sales Commission Calculation                                 │
│  • Tier Achievement Bonus                                                │
│  • Monthly Payout Processing                                             │
│                                                                          │
│  CROSS-DOMAIN SCENARIOS                                                  │
│  • Lead Sync from AI Services                                            │
│  • Partner Status Update from Core                                       │
│  • Commission Calculation from Management-Domain                         │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

### 1.2 Mock Flow Summary

| Scenario | User | Entry Point | Exit Point | Key Integration |
|----------|------|-------------|------------|-----------------|
| New Partner Registration | Unregistered User | App Launch | Dashboard Approved | Country-Sales-Dashboard |
| AI Lead Assignment | Sales Partner | Push Notification | Lead Detail | lead-generation-ai-service |
| Courier Partner Onboarding | Sales Partner | Add Partner | Partner Active | Courier-Partners-Dashboard |
| Corporate Deal Closing | Sales Partner | Lead Detail | Commission Calculated | commission-calculator-service |
| Commission Payout | Sales Partner | Commission Tab | Bank Deposit | payment-processing-service |

---

## 2. USER PROFILES

### 2.1 Sales Partner Representative

```json
{
  "userProfile": {
    "id": "SP-2025-00452",
    "personalInfo": {
      "firstName": "John",
      "lastName": "Okafor",
      "email": "john.okafor@email.com",
      "phone": "+2348012345678",
      "dateOfBirth": "1985-03-15",
      "nationalId": "12345678901",
      "address": {
        "street": "123 Adetokunbo Ademola Street",
        "city": "Victoria Island",
        "state": "Lagos",
        "country": "Nigeria",
        "postalCode": "101241"
      }
    },
    "salesInfo": {
      "partnerId": "SP-2025-00452",
      "registrationDate": "2025-01-15",
      "status": "ACTIVE",
      "territory": {
        "id": "TERR-LAG-NORTH-001",
        "name": "North Lagos Zone",
        "regions": ["Ikeja", "Ojota", "Ogba", "Ikeja GRA"],
        "leadAllocation": 50,
        "currentLeads": 15
      },
      "commissionTier": "GOLD",
      "commissionRate": 0.05,
      "specializations": [
        "COURIER",
        "WAREHOUSE",
        "ECOMMERCE"
      ]
    },
    "bankInfo": {
      "bankName": "GTBank",
      "accountNumber": "1234567890",
      "accountName": "John Okafor",
      "bvn": "12345678901"
    },
    "performance": {
      "totalPartners": 12,
      "totalCustomers": 48,
      "activeDeals": 8,
      "closedDeals": 35,
      "conversionRate": 0.85,
      "totalEarnings": 525000,
      "currentMonthEarnings": 125000
    }
  }
}
```

### 2.2 Regional Sales Partner Manager

```json
{
  "userProfile": {
    "id": "SPM-2025-00023",
    "personalInfo": {
      "firstName": "Sarah",
      "lastName": "Eze",
      "email": "sarah.eze@gogidix.com",
      "phone": "+2348023456789"
    },
    "role": "REGIONAL_MANAGER",
    "assignedRegion": {
      "id": "REG-LAG-001",
      "name": "Lagos Region",
      "territories": ["North Lagos", "Mainland", "Island", "Ikorodu"]
    },
    "managedPartners": 25,
    "teamSize": 25,
    "regionalTargets": {
      "monthly": 5000000,
      "current": 4250000,
      "achievement": 85
    }
  }
}
```

---

## 3. MOCK DATA SETS

### 3.1 AI-Generated Leads

```json
{
  "aiLeads": [
    {
      "leadId": "LEAD-AI-2025-04521",
      "source": "AI_GENERATED",
      "sourceService": "lead-generation-ai-service",
      "generatedAt": "2025-02-16T08:30:00Z",
      "assignedTo": "SP-2025-00452",
      "territory": "TERR-LAG-NORTH-001",
      "score": {
        "overall": 85,
        "conversionProbability": 90,
        "urgency": "HIGH",
        "valuePotential": "MEDIUM-HIGH"
      },
      "leadInfo": {
        "type": "PARTNER",
        "partnerType": "WAREHOUSE",
        "contactPerson": {
          "name": "John Doe",
          "phone": "+2348034567890",
          "email": "john.doe@securestore.com",
          "role": "Operations Manager"
        },
        "business": {
          "name": "SecureStore Nigeria Ltd",
          "industry": "Warehousing & Logistics",
          "location": {
            "address": "45 Industrial Avenue, Ikeja",
            "city": "Ikeja",
            "state": "Lagos",
            "coordinates": {
              "latitude": 6.5944,
              "longitude": 3.3867
            }
          },
          "estimatedCapacity": "5000 sqm",
          "employeeCount": 25,
          "yearsInBusiness": 8
        }
      },
      "aiInsights": {
        "recommendedApproach": "Focus on security features and facility management tools",
        "bestContactTime": "10:00-12:00",
        "similarConvertedLeads": ["LEAD-2024-01234", "LEAD-2024-01567"],
        "conversionFactors": [
          "High storage demand in area",
          "Expanding business operations",
          "Recently secured new contracts"
        ],
        "potentialObjections": [
          "Competitive pricing concerns",
          "Integration with existing systems"
        ],
        "estimatedValue": {
          "min": 50000,
          "max": 100000,
          "confidence": 75
        }
      },
      "segmentation": {
        "customerSegment": "SMALL_MEDIUM_BUSINESS",
        "industryVertical": "LOGISTICS",
        "behavioralProfile": "QUALITY_SEEKING",
        "decisionMakerType": "ANALYTICAL"
      },
      "status": "NEW",
      "followUpDate": "2025-02-16",
      "activities": []
    },
    {
      "leadId": "LEAD-AI-2025-04522",
      "source": "AI_GENERATED",
      "score": {
        "overall": 72,
        "conversionProbability": 78,
        "urgency": "MEDIUM",
        "valuePotential": "MEDIUM"
      },
      "leadInfo": {
        "type": "PARTNER",
        "partnerType": "ECOMMERCE",
        "contactPerson": {
          "name": "Jane Smith",
          "phone": "+2348045678901",
          "email": "jane@techstore.ng"
        },
        "business": {
          "name": "TechStore Nigeria",
          "industry": "E-commerce / Electronics",
          "location": {
            "address": "23 Wuse 2, Abuja",
            "city": "Abuja",
            "state": "FCT"
          },
          "productCategories": ["Electronics", "Gadgets", "Accessories"],
          "monthlyVolume": "₦5,000,000"
        }
      },
      "aiInsights": {
        "recommendedApproach": "Highlight vendor dashboard features and analytics",
        "bestContactTime": "14:00-16:00",
        "estimatedValue": {
          "min": 30000,
          "max": 75000
        }
      },
      "status": "NEW"
    },
    {
      "leadId": "LEAD-AI-2025-04523",
      "source": "AI_GENERATED",
      "score": {
        "overall": 68,
        "conversionProbability": 75,
        "urgency": "MEDIUM",
        "valuePotential": "HIGH"
      },
      "leadInfo": {
        "type": "CUSTOMER",
        "customerType": "CORPORATE",
        "contactPerson": {
          "name": "Chinedu Okafor",
          "phone": "+2348056789012",
          "email": "chinedu@techcorp.ng",
          "role": "Procurement Manager"
        },
        "business": {
          "name": "TechCorp Nigeria Ltd",
          "industry": "Technology / IT Services",
          "location": {
            "address": "12 Opebi Road, Ikeja",
            "city": "Ikeja",
            "state": "Lagos"
          },
          "companySize": "MEDIUM (100-500 employees)",
          "potentialServices": ["Courier", "Warehousing", "Air Freight"]
        }
      },
      "aiInsights": {
        "recommendedApproach": "Comprehensive logistics solution presentation",
        "bestContactTime": "09:00-11:00",
        "estimatedValue": {
          "min": 100000,
          "max": 250000
        }
      },
      "status": "NEW"
    }
  ]
}
```

### 3.2 Partner Types & Specifications

```json
{
  "partnerTypes": {
    "COURIER": {
      "id": "PT-COURIER",
      "name": "Courier Partner",
      "dashboard": "Courier-Partners-Dashboard",
      "domain": "shared-courier-core",
      "color": "#2196F3",
      "icon": "🚚",
      "registrationFields": [
        {
          "name": "fleetSize",
          "type": "NUMBER",
          "label": "Fleet Size",
          "required": true,
          "validation": "min:1"
        },
        {
          "name": "vehicleTypes",
          "type": "MULTI_SELECT",
          "label": "Vehicle Types",
          "options": ["Motorcycle", "Van", "Truck", "Bike"]
        },
        {
          "name": "serviceAreas",
          "type": "TEXT_AREA",
          "label": "Service Coverage Areas"
        },
        {
          "name": "operatingLicense",
          "type": "FILE_UPLOAD",
          "label": "Operating License Document"
        }
      ],
      "commissionStructure": {
        "referralBonus": 0.05,
        "tiers": {
          "BRONZE": 0.05,
          "SILVER": 0.075,
          "GOLD": 0.10,
          "PLATINUM": 0.15
        }
      }
    },
    "HAULAGE": {
      "id": "PT-HAULAGE",
      "name": "Haulage Partner",
      "dashboard": "Haulage-Partners-Dashboard",
      "domain": "shared-haulage-core",
      "color": "#795548",
      "icon": "🚛",
      "registrationFields": [
        {
          "name": "fleetSize",
          "type": "NUMBER",
          "label": "Number of Trucks"
        },
        {
          "name": "truckTypes",
          "type": "MULTI_SELECT",
          "label": "Truck Types",
          "options": ["Flatbed", "Container", "Tanker", "Box Truck"]
        },
        {
          "name": "routeCoverage",
          "type": "TEXT_AREA",
          "label": "Route Coverage"
        }
      ],
      "commissionStructure": {
        "referralBonus": 0.06
      }
    },
    "WAREHOUSE": {
      "id": "PT-WAREHOUSE",
      "name": "Warehouse Partner",
      "dashboard": "Warehouse-Partners-Dashboard",
      "domain": "shared-warehousing-core",
      "color": "#607D8B",
      "icon": "📦",
      "registrationFields": [
        {
          "name": "storageCapacity",
          "type": "NUMBER",
          "label": "Storage Capacity (sqm)"
        },
        {
          "name": "facilityCount",
          "type": "NUMBER",
          "label": "Number of Facilities"
        },
        {
          "name": "securityFeatures",
          "type": "MULTI_SELECT",
          "label": "Security Features",
          "options": ["CCTV", "24/7 Security", "Fire Suppression", "Climate Control"]
        }
      ],
      "commissionStructure": {
        "referralBonus": 0.07
      }
    },
    "ECOMMERCE": {
      "id": "PT-ECOMMERCE",
      "name": "E-commerce Vendor",
      "dashboard": "Ecommerce-Vendors-Dashboard",
      "domain": "shared-ecommerce-core",
      "color": "#9C27B0",
      "icon": "🛒",
      "registrationFields": [
        {
          "name": "productCategories",
          "type": "MULTI_SELECT",
          "label": "Product Categories",
          "options": ["Electronics", "Fashion", "Food", "Home & Garden", "Other"]
        },
        {
          "name": "monthlyVolume",
          "type": "CURRENCY",
          "label": "Estimated Monthly Sales Volume"
        },
        {
          "name": "warehouseAccess",
          "type": "BOOLEAN",
          "label": "Has Warehouse Access?"
        }
      ],
      "commissionStructure": {
        "referralBonus": 0.05
      }
    },
    "AIR_OCEAN": {
      "id": "PT-AIR_OCEAN",
      "name": "Air/Ocean Freight Agent",
      "dashboard": "agents-dashboard",
      "domain": "shared-air-freight-core",
      "color": "#00BCD4",
      "icon": "✈️",
      "registrationFields": [
        {
          "name": "agentLicense",
          "type": "FILE_UPLOAD",
          "label": "Freight Agent License"
        },
        {
          "name": "portAssociations",
          "type": "MULTI_SELECT",
          "label": "Port Associations",
          "options": ["Lagos Apapa", "Tin Can", "Murtala Muhammed", "Other"]
        },
        {
          "name": "tradeRoutes",
          "type": "MULTI_SELECT",
          "label": "Primary Trade Routes",
          "options": ["Europe", "Asia", "Americas", "Middle East", "Africa"]
        }
      ],
      "commissionStructure": {
        "referralBonus": 0.08
      }
    },
    "LOCATION_AGENT": {
      "id": "PT-LOCATION_AGENT",
      "name": "Location Agent",
      "dashboard": "Location-Agents-Dashboard",
      "domain": "shared-courier-core",
      "color": "#FF5722",
      "icon": "📍",
      "registrationFields": [
        {
          "name": "locationType",
          "type": "SELECT",
          "label": "Location Type",
          "options": ["Shop", "Kiosk", "Gas Station", "Supermarket"]
        },
        {
          "name": "facilitySize",
          "type": "SELECT",
          "label": "Facility Size",
          "options": ["Small (<20sqm)", "Medium (20-50sqm)", "Large (>50sqm)"]
        },
        {
          "name": "operatingHours",
          "type": "TEXT",
          "label": "Operating Hours"
        }
      ],
      "commissionStructure": {
        "referralBonus": 0.04
      }
    },
    "WHOLESALE": {
      "id": "PT-WHOLESALE",
      "name": "Wholesale Partner",
      "dashboard": "wholesalers-dashboard",
      "domain": "shared-ecommerce-core",
      "color": "#3F51B5",
      "icon": "📦",
      "registrationFields": [
        {
          "name": "businessType",
          "type": "SELECT",
          "label": "Business Type",
          "options": ["Distributor", "Retailer", "Wholesaler", "Trading Company"]
        },
        {
          "name": "annualRevenue",
          "type": "CURRENCY",
          "label": "Annual Revenue Range"
        },
        {
          "name": "productCategories",
          "type": "MULTI_SELECT",
          "label": "Product Categories of Interest"
        }
      ],
      "commissionStructure": {
        "referralBonus": 0.06
      }
    },
    "INFLUENCER": {
      "id": "PT-INFLUENCER",
      "name": "Influencer Partner",
      "dashboard": "Influencers-Dashboard",
      "domain": "shared-ecommerce-core",
      "color": "#E91E63",
      "icon": "📱",
      "registrationFields": [
        {
          "name": "platforms",
          "type": "MULTI_SELECT",
          "label": "Social Media Platforms",
          "options": ["Instagram", "TikTok", "YouTube", "Twitter/X", "Facebook"]
        },
        {
          "name": "followerCount",
          "type": "NUMBER",
          "label": "Total Followers (across all platforms)"
        },
        {
          "name": "engagementRate",
          "type": "NUMBER",
          "label": "Average Engagement Rate (%)"
        },
        {
          "name": "contentNiche",
          "type": "MULTI_SELECT",
          "label": "Content Niche",
          "options": ["Fashion", "Tech", "Lifestyle", "Food", "Beauty", "Other"]
        }
      ],
      "commissionStructure": {
        "referralBonus": 0.03
      }
    }
  }
}
```

### 3.3 Commission Structure Mock

```json
{
  "commissionStructure": {
    "partnerReferralBonuses": {
      "description": "One-time bonus when referred partner activates",
      "tiers": {
        "BRONZE": {
          "minSalesVolume": 0,
          "rate": 0.05,
          "example": "₦50,000 partner fee = ₦2,500 bonus"
        },
        "SILVER": {
          "minSalesVolume": 1000000,
          "rate": 0.075,
          "example": "₦100,000 partner fee = ₦7,500 bonus"
        },
        "GOLD": {
          "minSalesVolume": 5000000,
          "rate": 0.10,
          "example": "₦200,000 partner fee = ₦20,000 bonus"
        },
        "PLATINUM": {
          "minSalesVolume": 15000000,
          "rate": 0.15,
          "example": "₦500,000 partner fee = ₦75,000 bonus"
        }
      }
    },
    "customerSalesCommissions": {
      "description": "Ongoing commission from customer sales",
      "individualCustomers": {
        "rateRange": "0.03 - 0.05",
        "tiers": {
          "BRONZE": 0.03,
          "SILVER": 0.035,
          "GOLD": 0.04,
          "PLATINUM": 0.05
        },
        "example": "₦100,000 order = ₦3,000 - ₦5,000 commission"
      },
      "corporateCustomers": {
        "rateRange": "0.05 - 0.10",
        "tiers": {
          "BRONZE": 0.05,
          "SILVER": 0.065,
          "GOLD": 0.08,
          "PLATINUM": 0.10
        },
        "example": "₦1,500,000 deal = ₦75,000 - ₦150,000 commission"
      }
    },
    "performanceBonuses": {
      "tierAchievement": {
        "description": "Bonus for reaching next tier",
        "amount": 25000,
        "condition": "Reach next commission tier"
      },
      "monthlyTarget": {
        "description": "Additional percentage on all commissions when target met",
        "rate": 0.10,
        "condition": "Achieve 100% of monthly target"
      },
      "newPartnerRecord": {
        "description": "Bonus for breaking personal partner onboarding record",
        "amount": 10000,
        "condition": "Exceed previous monthly partner onboarding record"
      }
    },
    "payoutSchedule": {
      "frequency": "MONTHLY",
      "payoutDay": 1,
      "processingDays": 3,
      "example": "February commissions paid on March 1st"
    }
  }
}
```

### 3.4 Deals Pipeline Mock

```json
{
  "dealsPipeline": [
    {
      "dealId": "DEAL-2025-01234",
      "opportunityId": "OPP-2025-04567",
      "leadId": "LEAD-AI-2025-04450",
      "type": "CUSTOMER_SALE",
      "customerType": "CORPORATE",
      "title": "TechCorp Nigeria Ltd - Corporate Logistics Contract",
      "stage": "NEGOTIATION",
      "probability": 75,
      "value": 1500000,
      "commissionValue": 120000,
      "currency": "NGN",
      "customer": {
        "name": "TechCorp Nigeria Ltd",
        "industry": "Technology",
        "contactPerson": "Chinedu Okafor",
        "phone": "+2348056789012",
        "email": "chinedu@techcorp.ng",
        "location": "Ikeja, Lagos"
      },
      "services": ["COURIER", "WAREHOUSE", "AIR_FREIGHT"],
      "expectedCloseDate": "2025-02-28",
      "createdDate": "2025-02-01",
      "lastActivity": "2025-02-15",
      "nextAction": "Follow up on contract review",
      "activities": [
        {
          "date": "2025-02-01",
          "type": "CALL",
          "notes": "Initial discovery call - discussed full logistics solution"
        },
        {
          "date": "2025-02-08",
          "type": "MEETING",
          "notes": "On-site presentation to procurement team"
        },
        {
          "date": "2025-02-12",
          "type": "EMAIL",
          "notes": "Sent proposal with pricing and service agreement"
        },
        {
          "date": "2025-02-15",
          "type": "CALL",
          "notes": "Followed up on proposal - client reviewing with legal"
        }
      ]
    },
    {
      "dealId": "DEAL-2025-01235",
      "type": "CUSTOMER_SALE",
      "customerType": "INDIVIDUAL",
      "title": "Multiple retail customer acquisitions",
      "stage": "CLOSING",
      "probability": 90,
      "value": 500000,
      "commissionValue": 25000,
      "services": ["MARKETPLACE"],
      "expectedCloseDate": "2025-02-20",
      "activities": []
    }
  ]
}
```

---

## 4. API INTEGRATION POINTS

### 4.1 Foundation-Domain AI Services

```
┌─────────────────────────────────────────────────────────────────────────┐
│              FOUNDATION-DOMAIN AI SERVICES INTEGRATION                  │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│  1. LEAD GENERATION AI SERVICE                                          │
│     ┌──────────────────────────────────────────────────────────────┐   │
│     │ SERVICE: lead-generation-ai-service                          │   │
│     │ BASE URL: api.foundation.gogidix.com/ai/lead-generation      │   │
│     └──────────────────────────────────────────────────────────────┘   │
│                                                                          │
│     ENDPOINTS:                                                          │
│     • GET  /api/v1/leads/assigned/{partnerId}                          │
│       → Get AI-assigned leads for Sales Partner                         │
│       → Response: Lead[] with AI scores & insights                      │
│                                                                          │
│     • POST /api/v1/leads/{leadId}/feedback                             │
│       → Submit lead outcome for AI learning                             │
│       → Body: { outcome: "CONVERTED"|"LOST", factors: {...} }           │
│                                                                          │
│     • GET  /api/v1/leads/ai-insights/{leadId}                          │
│       → Get detailed AI analysis for lead                               │
│       → Response: { score, segments, recommendations, similarLeads }     │
│                                                                          │
│                                                                          │
│  2. AI CUSTOMER SEGMENTATION SERVICE                                    │
│     ┌──────────────────────────────────────────────────────────────┐   │
│     │ SERVICE: ai-customer-segmentation-service                     │   │
│     │ BASE URL: api.foundation.gogidix.com/ai/segmentation          │   │
│     └──────────────────────────────────────────────────────────────┘   │
│                                                                          │
│     ENDPOINTS:                                                          │
│     • POST /api/v1/segment/analyze                                     │
│       → Analyze customer/lead for segmentation                          │
│       → Body: { customerData: {...} }                                   │
│       → Response: { segment, profile, propensityScores }                │
│                                                                          │
│     • GET  /api/v1/segment/definitions                                 │
│       → Get all customer segment definitions                            │
│       → Response: { segments: [{id, name, characteristics}] }           │
│                                                                          │
│                                                                          │
│  3. AI USER PROFILING SERVICE                                           │
│     ┌──────────────────────────────────────────────────────────────┐   │
│     │ SERVICE: ai-user-profiling-service                            │   │
│     │ BASE URL: api.foundation.gogidix.com/ai/profiling             │   │
│     └──────────────────────────────────────────────────────────────┘   │
│                                                                          │
│     ENDPOINTS:                                                          │
│     • GET  /api/v1/profile/{customerId}                               │
│       → Get AI profile for customer                                     │
│       → Response: { profile, preferences, behaviorPatterns }            │
│                                                                          │
│     • POST /api/v1/profile/predict                                     │
│       → Predict customer behavior                                       │
│       → Body: { customerId, context }                                   │
│       → Response: { predictions, confidence, recommendations }           │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

### 4.2 Management-Domain Sales Services

```
┌─────────────────────────────────────────────────────────────────────────┐
│            MANAGEMENT-DOMAIN SALES SERVICES INTEGRATION                 │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│  1. LEAD MANAGEMENT SERVICE                                             │
│     ┌──────────────────────────────────────────────────────────────┐   │
│     │ SERVICE: lead-management-service                              │   │
│     │ BASE URL: api.management.gogidix.com/sales/leads              │   │
│     └──────────────────────────────────────────────────────────────┘   │
│                                                                          │
│     ENDPOINTS:                                                          │
│     • GET  /api/v1/partners/{partnerId}/leads                          │
│       → Get all leads assigned to Sales Partner                         │
│                                                                          │
│     • POST /api/v1/leads                                               │
│       → Create new lead (manual entry)                                  │
│                                                                          │
│     • PUT  /api/v1/leads/{leadId}/status                              │
│       → Update lead status                                             │
│                                                                          │
│     • POST /api/v1/leads/{leadId}/convert                             │
│       → Convert lead to deal/opportunity                                │
│                                                                          │
│                                                                          │
│  2. COMMISSION CALCULATOR SERVICE                                       │
│     ┌──────────────────────────────────────────────────────────────┐   │
│     │ SERVICE: commission-calculator-service                        │   │
│     │ BASE URL: api.management.gogidix.com/sales/commission         │   │
│     └──────────────────────────────────────────────────────────────┘   │
│                                                                          │
│     ENDPOINTS:                                                          │
│     • GET  /api/v1/partners/{partnerId}/commission/summary             │
│       → Get commission summary for partner                              │
│       → Response: { total, pending, paid, tier, breakdown }            │
│                                                                          │
│     • GET  /api/v1/partners/{partnerId}/commission/history             │
│       → Get commission transaction history                              │
│                                                                          │
│     • POST /api/v1/commission/calculate                                │
│       → Calculate commission for deal                                   │
│       → Body: { dealValue, type, partnerTier }                          │
│       → Response: { commission, bonus, total }                          │
│                                                                          │
│     • GET  /api/v1/partners/{partnerId}/commission/tier                │
│       → Get current commission tier and progress                         │
│       → Response: { currentTier, nextTier, progress, requirements }     │
│                                                                          │
│                                                                          │
│  3. SALES WEB DASHBOARD (HQ)                                            │
│     ┌──────────────────────────────────────────────────────────────┐   │
│     │ SERVICE: sales-web-dashboard-service                          │   │
│     │ BASE URL: api.management.gogidix.com/sales/hq                 │   │
│     └──────────────────────────────────────────────────────────────┘   │
│                                                                          │
│     ENDPOINTS:                                                          │
│     • POST /api/v1/partners/register                                   │
│       → Register new Sales Partner application                          │
│                                                                          │
│     • GET  /api/v1/partners/{partnerId}/approval-status                │
│       → Check partner application approval status                        │
│                                                                          │
│     • GET  /api/v1/partners/{partnerId}/territory                      │
│       → Get assigned territory details                                  │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

### 4.3 Business-Domain Country Sales Services

```
┌─────────────────────────────────────────────────────────────────────────┐
│          BUSINESS-DOMAIN COUNTRY SALES SERVICES INTEGRATION             │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│  1. COUNTRY SALES DASHBOARD SERVICE                                      │
│     ┌──────────────────────────────────────────────────────────────┐   │
│     │ SERVICE: country-sales-dashboard-service                       │   │
│     │ BASE URL: api.business.gogidix.com/country-sales              │   │
│     └──────────────────────────────────────────────────────────────┘   │
│                                                                          │
│     ENDPOINTS:                                                          │
│     • GET  /api/v1/leads/territory/{territoryId}                      │
│       → Get leads for territory (read-only for partners)               │
│                                                                          │
│     • GET  /api/v1/partners/{partnerId}/allocation                     │
│       → Get lead allocation for partner                                 │
│       → Response: { allocated, remaining, nextAllocationDate }         │
│                                                                          │
│     • POST /api/v1/deals                                               │
│       → Create customer deal                                            │
│                                                                          │
│     • PUT  /api/v1/deals/{dealId}/stage                                │
│       → Update deal stage                                               │
│                                                                          │
│     • POST /api/v1/deals/{dealId}/close                                │
│       → Close deal and trigger commission calculation                   │
│                                                                          │
│                                                                          │
│  2. PARTNER ONBOARDING ROUTING                                          │
│     Routes to appropriate shared-business-infrastructure dashboard:      │
│                                                                          │
│     • POST /api/v1/partners/courier/register                            │
│       → to Courier-Partners-Dashboard                                   │
│                                                                          │
│     • POST /api/v1/partners/haulage/register                            │
│       → to Haulage-Partners-Dashboard                                   │
│                                                                          │
│     • POST /api/v1/partners/warehouse/register                          │
│       → to Warehouse-Partners-Dashboard                                 │
│                                                                          │
│     • POST /api/v1/partners/ecommerce/register                          │
│       → to Ecommerce-Vendors-Dashboard                                  │
│                                                                          │
│     • POST /api/v1/partners/air-ocean/register                          │
│       → to agents-dashboard (Air/Ocean)                                  │
│                                                                          │
│     • POST /api/v1/partners/location-agent/register                     │
│       → to Location-Agents-Dashboard                                    │
│                                                                          │
│     • POST /api/v1/partners/wholesale/register                          │
│       → to wholesalers-dashboard                                        │
│                                                                          │
│     • POST /api/v1/partners/influencer/register                         │
│       → to Influencers-Dashboard                                        │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

### 4.4 Shared-Business-Infrastructure Partner Dashboards

```
┌─────────────────────────────────────────────────────────────────────────┐
│       SHARED-BUSINESS-INFRASTRUCTURE PARTNER DASHBOARD INTEGRATION      │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│  READ-ONLY ACCESS (for Sales Partner visibility):                        │
│                                                                          │
│  • GET  /api/v1/partners/{partnerId}/status                             │
│    → Check partner activation status                                    │
│    → Returns from appropriate dashboard                                  │
│                                                                          │
│  • GET  /api/v1/partners/{partnerId}/performance                        │
│    → View partner performance (for commission eligibility)               │
│                                                                          │
│  • GET  /api/v1/partners/{partnerId}/referrals                         │
│    → View referrals made by partner                                     │
│                                                                          │
│  • POST /api/v1/partners/{partnerId}/activities                         │
│    → Log sales activity on partner account                              │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 5. END-TO-END FLOWS

### 5.1 New Sales Partner Registration Flow

```json
{
  "flowName": "New Sales Partner Registration",
  "description": "Complete flow from app download to first approved lead",
  "actor": "Unregistered User",
  "steps": [
    {
      "step": 1,
      "name": "App Download & Launch",
      "action": "User downloads and opens app",
      "system": {
        "displays": "Splash screen → Login screen",
        "options": ["Login", "Register as Sales Partner"]
      }
    },
    {
      "step": 2,
      "name": "Initiate Registration",
      "action": "User taps 'Register as Sales Partner'",
      "screen": "Step 1: Personal Information",
      "fields": [
        "Full Name",
        "Phone Number",
        "Email Address",
        "Date of Birth",
        "National ID Number"
      ],
      "validation": "OTP verification for phone and email"
    },
    {
      "step": 3,
      "name": "Sales Experience",
      "screen": "Step 3: Sales Experience",
      "fields": [
        "Years of Experience",
        "Industries Sold In",
        "Average Monthly Sales Volume",
        "Current/Previous Employment"
      ]
    },
    {
      "step": 4,
      "name": "Preference Selection",
      "screen": "Step 4: Preferences",
      "fields": [
        "Preferred Partner Types (select up to 3)",
        "Preferred Customer Types",
        "Preferred Territory (region flexibility)"
      ]
    },
    {
      "step": 5,
      "name": "Bank Information",
      "screen": "Step 5: Bank Details",
      "fields": [
        "Bank Name",
        "Account Number",
        "Account Name",
        "BVN (for verification)"
      ]
    },
    {
      "step": 6,
      "name": "Agreements",
      "screen": "Step 6: Terms & Conditions",
      "actions": [
        "Review Sales Partner Terms",
        "Review Commission Structure",
        "Accept Code of Conduct",
        "Consent to background check",
        "Digital Signature"
      ]
    },
    {
      "step": 7,
      "name": "Submit Application",
      "action": "User submits registration",
      "apiCall": {
        "service": "sales-web-dashboard-service",
        "endpoint": "POST /api/v1/partners/register",
        "payload": {
          "application": {
            "personalInfo": "...",
            "salesExperience": "...",
            "preferences": "...",
            "bankInfo": "...",
            "agreements": ["accepted"]
          }
        }
      },
      "response": {
        "applicationId": "APP-2025-XXXX",
        "status": "PENDING_REVIEW",
        "expectedResponseTime": "3-5 business days"
      }
    },
    {
      "step": 8,
      "name": "Application Review",
      "action": "System routes application for review",
      "routing": {
        "to": "Country-Sales-Dashboard",
        "assignedTo": "Country Sales Director",
        "region": "Based on preferred territory"
      }
    },
    {
      "step": 9,
      "name": "Background Verification",
      "action": "System runs background checks",
      "verifications": [
        "BVN validation",
        "National ID verification",
        "Employment verification",
        "References check"
      ]
    },
    {
      "step": 10,
      "name": "Approval Decision",
      "action": "Country Sales Director reviews and decides",
      "possibleOutcomes": [
        {
          "outcome": "APPROVED",
          "nextSteps": [
            "Account creation",
            "Territory assignment",
            "Partner ID generation",
            "Welcome email sent"
          ]
        },
        {
          "outcome": "REJECTED",
          "reason": "Does not meet criteria",
          "notification": "Email with explanation"
        },
        {
          "outcome": "MORE_INFO_REQUIRED",
          "action": "Request additional information"
        }
      ]
    },
    {
      "step": 11,
      "name": "Account Activation",
      "action": "Upon approval, account is activated",
      "system": {
        "creates": [
          "Sales Partner account",
          "Territory assignment",
          "Commission tier (Bronze)",
          "Lead allocation from AI service"
        ]
      }
    },
    {
      "step": 12,
      "name": "First Login Notification",
      "action": "User receives notification",
      "notification": {
        "type": "PUSH",
        "title": "Welcome to Sales Team Partners!",
        "body": "Your account is ready. Log in to get started.",
        "deepLink": "sales-team-app://login"
      }
    },
    {
      "step": 13,
      "name": "First Login",
      "action": "User logs in for first time",
      "screens": [
        "Welcome screen",
        "Territory overview",
        "Quick training (optional)",
        "Resources download",
        "Ready to start"
      ]
    },
    {
      "step": 14,
      "name": "First Leads Assigned",
      "action": "AI assigns leads based on territory",
      "apiCall": {
        "service": "lead-generation-ai-service",
        "trigger": "Partner activation",
        "action": "Assign leads from territory pool"
      },
      "result": "5-10 new AI leads appear in Leads tab"
    }
  ]
}
```

### 5.2 AI Lead to Deal Conversion Flow

```json
{
  "flowName": "AI Lead to Deal Conversion",
  "description": "Complete flow from AI lead assignment to deal closing",
  "actor": "Sales Partner Representative",
  "steps": [
    {
      "step": 1,
      "name": "AI Lead Assignment",
      "trigger": "New leads available from AI service",
      "apiCall": {
        "service": "lead-generation-ai-service",
        "endpoint": "GET /api/v1/leads/assigned/{partnerId}",
        "frequency": "Every 2 hours or on push"
      },
      "notification": {
        "type": "PUSH",
        "title": "You have 5 new AI-assigned leads",
        "body": "Tap to view your new leads and start selling!",
        "deepLink": "sales-team-app://leads/ai-generated"
      }
    },
    {
      "step": 2,
      "name": "View Lead List",
      "screen": "Leads Tab → AI Generated",
      "display": [
        "Lead cards with AI score",
        "Partner/Customer type indicator",
        "Location",
        "Time since assignment"
      ]
    },
    {
      "step": 3,
      "name": "Open Lead Details",
      "action": "User taps lead card",
      "screen": "Lead Detail Screen",
      "displays": [
        "AI score and confidence",
        "Contact information",
        "Business details",
        "AI insights and recommendations",
        "Similar converted leads",
        "Suggested actions"
      ]
    },
    {
      "step": 4,
      "name": "Take Action",
      "action": "User selects action from bottom sheet",
      "options": [
        {
          "action": "CALL",
          "system": "Initiates phone call",
          "logs": "Call logged automatically"
        },
        {
          "action": "SMS",
          "system": "Opens SMS with template message"
        },
        {
          "action": "EMAIL",
          "system": "Opens email client with template"
        },
        {
          "action": "SCHEDULE MEETING",
          "system": "Calendar integration"
        }
      ]
    },
    {
      "step": 5,
      "name": "Log Activity",
      "action": "After contact, log interaction",
      "screen": "Activity Log Form",
      "fields": [
        "Activity type (call, email, meeting, visit)",
        "Outcome (interested, not interested, follow-up needed)",
        "Notes",
        "Next follow-up date"
      ],
      "apiCall": {
        "service": "lead-management-service",
        "endpoint": "POST /api/v1/leads/{leadId}/activities",
        "feedback": "Sent to AI service for learning"
      }
    },
    {
      "step": 6,
      "name": "Convert to Deal",
      "action": "When lead qualifies, convert to deal",
      "screen": "Convert to Deal Form",
      "fields": [
        "Deal name",
        "Estimated value",
        "Expected close date",
        "Probability (%)",
        "Initial stage"
      ],
      "apiCall": {
        "service": "lead-management-service",
        "endpoint": "POST /api/v1/leads/{leadId}/convert",
        "creates": "New deal/opportunity"
      }
    },
    {
      "step": 7,
      "name": "Deal Management",
      "action": "Manage deal through pipeline",
      "screen": "Deals Tab → Deal Detail",
      "stages": [
        "NEW",
        "QUALIFIED",
        "PROPOSAL",
        "NEGOTIATION",
        "CLOSING",
        "CLOSED_WON",
        "CLOSED_LOST"
      ],
      "updates": "Stage changes logged with timestamp"
    },
    {
      "step": 8,
      "name": "Deal Closing",
      "action": "Deal won - customer closes",
      "screen": "Close Deal Form",
      "fields": [
        "Final deal value",
        "Closed date",
        "Products/services purchased",
        "Payment terms"
      ],
      "apiCall": {
        "service": "country-sales-dashboard-service",
        "endpoint": "POST /api/v1/deals/{dealId}/close"
      }
    },
    {
      "step": 9,
      "name": "Commission Calculation",
      "action": "System calculates commission",
      "apiCall": {
        "service": "commission-calculator-service",
        "endpoint": "POST /api/v1/commission/calculate",
        "payload": {
          "dealId": "DEAL-2025-XXXX",
          "dealValue": 1500000,
          "dealType": "CORPORATE_CUSTOMER",
          "partnerTier": "GOLD"
        },
        "response": {
          "commission": 120000,
          "bonus": 0,
          "total": 120000,
          "status": "PENDING",
          "payoutDate": "2025-03-01"
        }
      }
    },
    {
      "step": 10,
      "name": "Commission Recorded",
      "action": "Commission added to partner balance",
      "notification": {
        "type": "PUSH",
        "title": "🎉 Deal Closed!",
        "body": "TechCorp Ltd - ₦120,000 commission earned!",
        "deepLink": "sales-team-app://commission"
      }
    },
    {
      "step": 11,
      "name": "AI Feedback Loop",
      "action": "Outcome sent to AI for learning",
      "apiCall": {
        "service": "lead-generation-ai-service",
        "endpoint": "POST /api/v1/leads/{leadId}/feedback",
        "payload": {
          "outcome": "CONVERTED",
          "dealValue": 1500000,
          "conversionTime": "15 days",
          "factors": [
            "AI recommendation was accurate",
            "Best contact time was correct",
            "Similar leads pattern confirmed"
          ]
        }
      },
      "result": "AI improves future lead scoring"
    }
  ]
}
```

### 5.3 Partner Onboarding Flow

```json
{
  "flowName": "Partner Onboarding (Sales Team Referral)",
  "description": "Complete flow from partner registration to activation with commission",
  "actor": "Sales Partner Representative",
  "steps": [
    {
      "step": 1,
      "name": "Initiate Partner Onboarding",
      "action": "Sales Partner taps '+ Add Partner'",
      "screen": "Partner Type Selection",
      "options": [
        "Courier Partner",
        "Haulage Partner",
        "Warehouse Partner",
        "E-commerce Vendor",
        "Air/Ocean Agent",
        "Location Agent",
        "Wholesale Partner",
        "Influencer"
      ]
    },
    {
      "step": 2,
      "name": "Select Partner Type",
      "action": "Sales Partner selects partner type",
      "example": "Courier Partner selected",
      "nextScreen": "Basic Information"
    },
    {
      "step": 3,
      "name": "Collect Basic Information",
      "screen": "Partner Basic Information",
      "fields": [
        "Business Name",
        "Contact Person Name",
        "Phone Number",
        "Email Address",
        "Business Address"
      ]
    },
    {
      "step": 4,
      "name": "Collect Type-Specific Information",
      "screen": "Partner Type-Specific Fields",
      "exampleCourier": [
        "Fleet Size",
        "Vehicle Types",
        "Service Areas"
      ]
    },
    {
      "step": 5,
      "name": "Document Upload",
      "screen": "Document Upload",
      "documents": [
        "Business Registration Certificate",
        "Tax Registration",
        "ID Document (Contact Person)",
        "Bank Account Details",
        "Type-Specific Certifications"
      ]
    },
    {
      "step": 6,
      "name": "Territory & Location",
      "screen": "Territory Assignment",
      "fields": [
        "Operating Area(s)",
        "Service Coverage Radius",
        "Primary Location (GPS)",
        "Additional Locations"
      ]
    },
    {
      "step": 7,
      "name": "Review & Submit",
      "screen": "Review Summary",
      "displays": [
        "Summary of all information",
        "Commission tier preview",
        "Terms & Conditions",
        "Digital Signature"
      ]
    },
    {
      "step": 8,
      "name": "Submit Application",
      "action": "Sales Partner submits partner application",
      "apiCall": {
        "service": "country-sales-dashboard-service",
        "endpoint": "POST /api/v1/partners/register",
        "payload": {
          "partnerType": "COURIER",
          "referredBy": "SP-2025-00452",
          "businessInfo": {...},
          "documents": [...]
        }
      },
      "response": {
        "applicationId": "PARTNER-APP-2025-XXXX",
        "status": "PENDING_REVIEW",
        "expectedApprovalTime": "3-5 business days"
      }
    },
    {
      "step": 9,
      "name": "Routing for Approval",
      "action": "Application routed to appropriate channels",
      "routing": {
        "to": [
          "Country Sales Director (approval)",
          "Courier-Partners-Dashboard (account creation)"
        ],
        "parallel": true
      }
    },
    {
      "step": 10,
      "name": "Country Sales Director Review",
      "action": "Regional director reviews application",
      "checks": [
        "Territory alignment",
        "Sales Partner credibility",
        "Duplicate check"
      ]
    },
    {
      "step": 11,
      "name": "Partner Dashboard Processing",
      "action": "Courier-Partners-Dashboard creates account",
      "system": {
        "creates": [
          "Partner account",
          "Dashboard access",
          "Onboarding email",
          "Welcome materials"
        ]
      }
    },
    {
      "step": 12,
      "name": "Partner Activation",
      "action": "Partner account activated",
      "status": "ACTIVE",
      "notifications": [
        {
          "to": "Sales Partner",
          "message": "ABC Logistics is now active! Referral bonus tracking started."
        },
        {
          "to": "New Partner",
          "message": "Welcome to Gogidix Courier Partners!"
        }
      ]
    },
    {
      "step": 13,
      "name": "Referral Bonus Tracking",
      "action": "Commission system tracks referral",
      "tracking": {
        "partnerId": "PARTNER-2025-XXXX",
        "referredBy": "SP-2025-00452",
        "bonusEligibility": "AFTER_FIRST_TRANSACTION",
        "bonusAmount": "CALCULATED_ON_ACTIVATION"
      }
    },
    {
      "step": 14,
      "name": "Bonus Eligibility & Payout",
      "action": "When partner completes first transaction",
      "trigger": "Partner first service transaction",
      "apiCall": {
        "service": "commission-calculator-service",
        "endpoint": "POST /api/v1/commission/partner-bonus",
        "payload": {
          "referralPartnerId": "SP-2025-00452",
          "newPartnerId": "PARTNER-2025-XXXX",
          "registrationFee": 50000
        },
        "response": {
          "bonus": 5000,
          "status": "EARNED",
          "payoutDate": "Next cycle"
        }
      }
    }
  ]
}
```

### 5.4 Monthly Commission Payout Flow

```json
{
  "flowName": "Monthly Commission Payout",
  "description": "End-to-end flow from commission earning to bank deposit",
  "actor": "System (Automated) + Sales Partner",
  "steps": [
    {
      "step": 1,
      "name": "Commission Accumulation",
      "action": "Commissions earned throughout month",
      "sources": [
        "Partner referral bonuses (one-time)",
        "Customer sales commissions (ongoing)",
        "Performance bonuses (conditional)"
      ]
    },
    {
      "step": 2,
      "name": "Month-End Processing",
      "trigger": "Last day of month",
      "action": "Commission calculator service processes all commissions",
      "apiCall": {
        "service": "commission-calculator-service",
        "endpoint": "POST /api/v1/commission/process-monthly",
        "scheduled": "00:00 on 1st of next month"
      }
    },
    {
      "step": 3,
      "name": "Commission Validation",
      "action": "System validates all commission records",
      "validations": [
        "Deal closed and confirmed",
        "Customer payment received",
        "Partner activation verified",
        "No disputes or refunds"
      ]
    },
    {
      "step": 4,
      "name": "Generate Payout File",
      "action": "Generate payment batch for processing",
      "output": {
        "payoutBatchId": "PAY-2025-02-001",
        "totalAmount": 125000,
        "partnerCount": 25,
        "processingDate": "2025-03-01"
      }
    },
    {
      "step": 5,
      "name": "Payment Processing",
      "action": "Submit to payment processor",
      "apiCall": {
        "service": "payment-processing-service",
        "endpoint": "POST /api/v1/payments/batch",
        "payload": {
          "batchId": "PAY-2025-02-001",
          "payments": [
            {
              "partnerId": "SP-2025-00452",
              "amount": 125000,
              "bankAccount": "1234567890",
              "bank": "GTBank",
              "reference": "COM-SP-2025-00452-FEB2025"
            }
          ]
        }
      }
    },
    {
      "step": 6,
      "name": "Bank Transfer Initiation",
      "action": "Payment processor initiates bank transfers",
      "timeline": "Same day or next business day"
    },
    {
      "step": 7,
      "name": "Credit Notification",
      "action": "Partners receive credit alerts",
      "notification": {
        "type": "PUSH + EMAIL",
        "title": "💵 Commission Paid!",
        "body": "₦125,000 has been deposited to your GTBank account ****1234",
        "deepLink": "sales-team-app://commission/COM-2025-02145"
      }
    },
    {
      "step": 8,
      "name": "Payout Receipt",
      "action": "Generate receipt for download",
      "availableIn": "Commission tab → Transaction details"
    },
    {
      "step": 9,
      "name": "Tax Document Generation",
      "action": "Generate tax documents for the year",
      "frequency": "Annually (January)",
      "documents": [
        "Commission Summary",
        "Tax Certificate (Form W2 equivalent)"
      ]
    }
  ]
}
```

---

**END OF MOCK FLOW DOCUMENTATION**

**Next:** 04_Page_By_Page_Flow_Documentation.md

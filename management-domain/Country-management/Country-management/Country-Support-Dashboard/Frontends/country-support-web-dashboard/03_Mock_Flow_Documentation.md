# COUNTRY SUPPORT DASHBOARD - MOCK FLOW

**Version:** 1.0
**Last Updated:** 2025-02-08

---

## API ENDPOINTS

```
GET /support/dashboard/summary
GET /support/tickets
POST /support/tickets
GET /support/team/performance
```

---

## DATA MODELS

```typescript
interface Ticket {
  id: string;
  customer: CustomerInfo;
  category: string;
  priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'URGENT';
  status: 'OPEN' | 'IN_PROGRESS' | 'RESOLVED' | 'CLOSED';
  assignedTo: string;
  slaDeadline: Date;
}
```

---

**Document End**
# COUNTRY MARKETING DASHBOARD - MOCK FLOW

**Version:** 1.0
**Last Updated:** 2025-02-08

---

## API ENDPOINTS

```
GET /marketing/dashboard/summary
GET /marketing/campaigns
POST /marketing/campaigns
GET /marketing/analytics
```

---

## DATA MODELS

```typescript
interface Campaign {
  id: string;
  name: string;
  channel: 'EMAIL' | 'SMS' | 'SOCIAL' | 'ADS';
  status: 'DRAFT' | 'ACTIVE' | 'PAUSED' | 'COMPLETED';
  budget: number;
  spent: number;
  roi: number;
}
```

---

**Document End**

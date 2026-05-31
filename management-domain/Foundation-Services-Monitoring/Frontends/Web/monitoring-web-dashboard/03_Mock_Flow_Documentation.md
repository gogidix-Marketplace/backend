# MONITORING DASHBOARD - MOCK FLOW

**Version:** 1.0
**Last Updated:** 2026-02-23

---

## API ENDPOINTS

```
GET  /monitoring/dashboard/overview
GET  /monitoring/services/health
GET  /monitoring/services/{serviceId}/metrics
GET  /monitoring/alerts/active
POST /monitoring/alerts/{alertId}/acknowledge
GET  /monitoring/logs/stream
```

---

## DATA MODELS

```typescript
interface ServiceHealth {
  serviceId: string;
  serviceName: string;
  status: 'OPERATIONAL' | 'DEGRADED' | 'CRITICAL' | 'UNKNOWN';
  uptime: number;
  responseTime: number;
  requestRate: number;
  errorRate: number;
  lastIncident?: Date;
}

interface MonitoringAlert {
  id: string;
  severity: 'INFO' | 'WARNING' | 'ERROR' | 'CRITICAL';
  serviceId: string;
  serviceName: string;
  metric: string;
  threshold: number;
  currentValue: number;
  message: string;
  status: 'ACTIVE' | 'ACKNOWLEDGED' | 'RESOLVED';
  createdAt: Date;
  acknowledgedAt?: Date;
  resolvedAt?: Date;
}

interface ServiceMetrics {
  serviceId: string;
  timestamp: Date;
  responseTime: MetricDataPoint[];
  requestRate: MetricDataPoint[];
  errorRate: MetricDataPoint[];
  cpuUsage: number;
  memoryUsage: number;
  diskUsage: number;
  networkIO: number;
}

interface MetricDataPoint {
  timestamp: Date;
  value: number;
}
```

---

## MOCK DATA

### Service Health Mock

```typescript
const mockServiceHealth: ServiceHealth[] = [
  {
    serviceId: 'svc_api_gateway',
    serviceName: 'API Gateway',
    status: 'OPERATIONAL',
    uptime: 99.99,
    responseTime: 45,
    requestRate: 1245,
    errorRate: 0.01,
    lastIncident: new Date('2026-02-16T10:30:00Z')
  },
  {
    serviceId: 'svc_auth',
    serviceName: 'Auth Service',
    status: 'OPERATIONAL',
    uptime: 99.95,
    responseTime: 32,
    requestRate: 856,
    errorRate: 0.05,
    lastIncident: new Date('2026-02-20T14:22:00Z')
  },
  {
    serviceId: 'svc_notification',
    serviceName: 'Notification Service',
    status: 'DEGRADED',
    uptime: 98.50,
    responseTime: 450,
    requestRate: 234,
    errorRate: 2.3,
    lastIncident: new Date('2026-02-23T09:15:00Z')
  },
  {
    serviceId: 'svc_file_storage',
    serviceName: 'File Storage',
    status: 'OPERATIONAL',
    uptime: 99.90,
    responseTime: 120,
    requestRate: 445,
    errorRate: 0.1,
    lastIncident: new Date('2026-02-18T08:45:00Z')
  },
  {
    serviceId: 'svc_cache',
    serviceName: 'Cache Service',
    status: 'OPERATIONAL',
    uptime: 99.98,
    responseTime: 8,
    requestRate: 5670,
    errorRate: 0.02,
    lastIncident: new Date('2026-02-10T16:20:00Z')
  },
  {
    serviceId: 'svc_message_queue',
    serviceName: 'Message Queue',
    status: 'OPERATIONAL',
    uptime: 99.92,
    responseTime: 15,
    requestRate: 3240,
    errorRate: 0.08,
    lastIncident: new Date('2026-02-19T11:30:00Z')
  },
  {
    serviceId: 'svc_ai_orchestration',
    serviceName: 'AI Orchestration',
    status: 'CRITICAL',
    uptime: 94.20,
    responseTime: 0,
    requestRate: 0,
    errorRate: 100,
    lastIncident: new Date('2026-02-23T10:30:00Z')
  },
  {
    serviceId: 'svc_email',
    serviceName: 'Email Service',
    status: 'OPERATIONAL',
    uptime: 99.85,
    responseTime: 234,
    requestRate: 156,
    errorRate: 0.15,
    lastIncident: new Date('2026-02-17T13:00:00Z')
  }
];
```

### Active Alerts Mock

```typescript
const mockActiveAlerts: MonitoringAlert[] = [
  {
    id: 'alert_001',
    severity: 'CRITICAL',
    serviceId: 'svc_ai_orchestration',
    serviceName: 'AI Orchestration',
    metric: 'health_check',
    threshold: 1,
    currentValue: 0,
    message: 'Service is not responding. Health checks failing.',
    status: 'ACTIVE',
    createdAt: new Date('2026-02-23T10:30:00Z')
  },
  {
    id: 'alert_002',
    severity: 'WARNING',
    serviceId: 'svc_notification',
    serviceName: 'Notification Service',
    metric: 'response_time',
    threshold: 200,
    currentValue: 450,
    message: 'Response time exceeded threshold: 450ms (threshold: 200ms)',
    status: 'ACTIVE',
    createdAt: new Date('2026-02-23T10:07:00Z')
  },
  {
    id: 'alert_003',
    severity: 'WARNING',
    serviceId: 'svc_cache',
    serviceName: 'Cache Service',
    metric: 'memory_usage',
    threshold: 80,
    currentValue: 82,
    message: 'Memory usage at 82% (threshold: 80%)',
    status: 'ACTIVE',
    createdAt: new Date('2026-02-23T09:30:00Z')
  }
];
```

### Service Metrics Mock

```typescript
const mockServiceMetrics: Record<string, ServiceMetrics> = {
  svc_api_gateway: {
    serviceId: 'svc_api_gateway',
    timestamp: new Date('2026-02-23T11:00:00Z'),
    responseTime: generateTimeSeries(24, 30, 100),
    requestRate: generateTimeSeries(24, 800, 1500),
    errorRate: generateTimeSeries(24, 0, 0.5),
    cpuUsage: 45,
    memoryUsage: 52,
    diskUsage: 28,
    networkIO: 82
  },
  svc_notification: {
    serviceId: 'svc_notification',
    timestamp: new Date('2026-02-23T11:00:00Z'),
    responseTime: generateTimeSeries(24, 200, 500),
    requestRate: generateTimeSeries(24, 150, 300),
    errorRate: generateTimeSeries(24, 0, 5),
    cpuUsage: 78,
    memoryUsage: 65,
    diskUsage: 45,
    networkIO: 91
  }
};

function generateTimeSeries(hours: number, min: number, max: number): MetricDataPoint[] {
  const data: MetricDataPoint[] = [];
  const now = Date.now();
  for (let i = hours; i >= 0; i--) {
    data.push({
      timestamp: new Date(now - i * 3600000),
      value: Math.random() * (max - min) + min
    });
  }
  return data;
}
```

### Log Stream Mock

```typescript
const mockLogEntries = [
  {
    timestamp: new Date('2026-02-23T10:45:32Z'),
    level: 'INFO',
    service: 'API Gateway',
    message: 'GET /api/v1/users - 200 - 45ms',
    details: { method: 'GET', path: '/api/v1/users', status: 200, duration: 45 }
  },
  {
    timestamp: new Date('2026-02-23T10:45:31Z'),
    level: 'INFO',
    service: 'Auth Service',
    message: 'GET /api/v1/auth/validate - 200 - 32ms',
    details: { method: 'GET', path: '/api/v1/auth/validate', status: 200, duration: 32 }
  },
  {
    timestamp: new Date('2026-02-23T10:45:30Z'),
    level: 'WARN',
    service: 'API Gateway',
    message: 'GET /api/v1/analytics - 429 - Rate limit exceeded',
    details: { method: 'GET', path: '/api/v1/analytics', status: 429, reason: 'Rate limit' }
  },
  {
    timestamp: new Date('2026-02-23T10:45:29Z'),
    level: 'INFO',
    service: 'API Gateway',
    message: 'POST /api/v1/tickets - 201 - 67ms',
    details: { method: 'POST', path: '/api/v1/tickets', status: 201, duration: 67 }
  },
  {
    timestamp: new Date('2026-02-23T10:45:28Z'),
    level: 'ERROR',
    service: 'AI Orchestration',
    message: 'Health check failed',
    details: { error: 'Connection timeout', retryAttempt: 3 }
  }
];
```

---

## STATE MANAGEMENT

### Zustand Store Structure

```typescript
interface MonitoringStore {
  // State
  services: ServiceHealth[];
  alerts: MonitoringAlert[];
  selectedService: string | null;
  timeRange: TimeRange;
  autoRefresh: boolean;

  // Actions
  setServices: (services: ServiceHealth[]) => void;
  setAlerts: (alerts: MonitoringAlert[]) => void;
  selectService: (serviceId: string | null) => void;
  setTimeRange: (range: TimeRange) => void;
  toggleAutoRefresh: () => void;
  acknowledgeAlert: (alertId: string) => Promise<void>;
  refreshData: () => Promise<void>;
}

type TimeRange = '1h' | '6h' | '24h' | '7d' | '30d';
```

---

## WEBSOCKET EVENTS

### Incoming Events

```typescript
// Service status change
interface ServiceStatusEvent {
  type: 'service.status';
  data: {
    serviceId: string;
    status: 'OPERATIONAL' | 'DEGRADED' | 'CRITICAL' | 'UNKNOWN';
    timestamp: Date;
  };
}

// New alert
interface AlertEvent {
  type: 'alert.created';
  data: MonitoringAlert;
}

// Alert resolved
interface AlertResolvedEvent {
  type: 'alert.resolved';
  data: {
    alertId: string;
    resolvedAt: Date;
  };
}

// Metrics update
interface MetricsUpdateEvent {
  type: 'metrics.update';
  data: {
    serviceId: string;
    metrics: Partial<ServiceMetrics>;
  };
}

// Log entry
interface LogEvent {
  type: 'log.entry';
  data: {
    serviceId: string;
    level: 'INFO' | 'WARN' | 'ERROR' | 'DEBUG';
    message: string;
    timestamp: Date;
  };
}
```

---

## ERROR HANDLING

### Standard Error Format

```typescript
interface MonitoringApiError {
  success: false;
  error: {
    code: string;
    message: string;
    details?: any;
    timestamp: Date;
    requestId: string;
  };
}
```

### Common Error Codes

| Code | HTTP Status | Description | Retryable |
|------|-------------|-------------|-----------|
| `MON_001` | 401 | Invalid authentication token | No |
| `MON_002` | 403 | Insufficient permissions | No |
| `MON_003` | 404 | Service not found | No |
| `MON_004` | 500 | Metrics service unavailable | Yes |
| `MON_005` | 503 | Service dependencies down | Yes |
| `MON_006` | 429 | Rate limit exceeded | Yes |

---

## VERSION HISTORY

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2026-02-23 | Initial Mock Flow Documentation |

---

**Document End**

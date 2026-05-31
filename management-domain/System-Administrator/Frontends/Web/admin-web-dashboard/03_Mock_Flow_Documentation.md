# HQ ADMIN DASHBOARD - MOCK FLOW

**Version:** 1.0
**Last Updated:** 2025-02-08

---

## API ENDPOINTS

```
GET /admin/dashboard/system-health
GET /admin/users
POST /admin/users
PUT /admin/users/:id
GET /admin/security/events
GET /admin/audit/logs
```

---

## DATA MODELS

```typescript
interface SystemUser {
  id: string;
  email: string;
  roles: string[];
  domains: string[];
  permissions: Permission[];
}

interface SecurityEvent {
  id: string;
  type: string;
  severity: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
  description: string;
  userId?: string;
  timestamp: Date;
}
```

---

## MOCK DATA

### System Users Mock

```typescript
const mockSystemUsers: SystemUser[] = [
  {
    id: 'usr_admin_001',
    email: 'super.admin@gogidix.com',
    roles: ['SYSTEM_ADMINISTRATOR'],
    domains: ['all'],
    permissions: [
      'users.create', 'users.read', 'users.update', 'users.delete',
      'security.manage', 'system.configure', 'audit.read'
    ],
    status: 'ACTIVE',
    lastLogin: new Date('2025-02-08T09:30:00Z'),
    createdAt: new Date('2024-01-15T00:00:00Z')
  },
  {
    id: 'usr_sec_001',
    email: 'security.officer@gogidix.com',
    roles: ['GLOBAL_SECURITY_OFFICER'],
    domains: ['all'],
    permissions: [
      'users.read', 'users.update',
      'security.manage', 'security.events', 'audit.read'
    ],
    status: 'ACTIVE',
    lastLogin: new Date('2025-02-08T08:15:00Z'),
    createdAt: new Date('2024-03-01T00:00:00Z')
  },
  {
    id: 'usr_ops_001',
    email: 'devops.lead@gogidix.com',
    roles: ['DEVOPS_ENGINEER'],
    domains: ['system'],
    permissions: [
      'system.configure', 'system.deploy', 'system.monitor'
    ],
    status: 'ACTIVE',
    lastLogin: new Date('2025-02-08T07:45:00Z'),
    createdAt: new Date('2024-02-20T00:00:00Z')
  }
];
```

### Security Events Mock

```typescript
const mockSecurityEvents: SecurityEvent[] = [
  {
    id: 'sec_evt_001',
    type: 'MULTIPLE_FAILED_LOGIN',
    severity: 'HIGH',
    description: '5 failed login attempts from IP 192.168.1.100',
    userId: 'usr_unknown',
    ipAddress: '192.168.1.100',
    timestamp: new Date('2025-02-08T10:30:00Z'),
    status: 'INVESTIGATING',
    assignedTo: 'usr_sec_001'
  },
  {
    id: 'sec_evt_002',
    type: 'PERMISSION_ESCALATION',
    severity: 'CRITICAL',
    description: 'Unauthorized attempt to assign admin privileges',
    userId: 'usr_admin_003',
    ipAddress: '10.0.0.45',
    timestamp: new Date('2025-02-08T09:15:00Z'),
    status: 'ESCALATED',
    assignedTo: 'usr_admin_001'
  },
  {
    id: 'sec_evt_003',
    type: 'SUSPICIOUS_API_CALLS',
    severity: 'MEDIUM',
    description: 'Unusual pattern of API calls from user account',
    userId: 'usr_sales_012',
    ipAddress: '203.45.67.89',
    timestamp: new Date('2025-02-08T08:00:00Z'),
    status: 'NEW',
    assignedTo: null
  }
];
```

### System Health Mock

```typescript
const mockSystemHealth = {
  overallStatus: 'HEALTHY',
  uptime: '99.98%',
  lastUpdate: new Date('2025-02-08T11:00:00Z'),
  services: [
    { name: 'API Gateway', status: 'OPERATIONAL', responseTime: 45, uptime: '99.99%' },
    { name: 'Auth Service', status: 'OPERATIONAL', responseTime: 32, uptime: '99.95%' },
    { name: 'User Service', status: 'OPERATIONAL', responseTime: 28, uptime: '99.97%' },
    { name: 'Audit Service', status: 'DEGRADED', responseTime: 450, uptime: '98.50%' },
    { name: 'Notification Service', status: 'OPERATIONAL', responseTime: 55, uptime: '99.90%' }
  ],
  domains: [
    { name: 'HR', status: 'OPERATIONAL', activeUsers: 145 },
    { name: 'Sales', status: 'OPERATIONAL', activeUsers: 312 },
    { name: 'Finance', status: 'OPERATIONAL', activeUsers: 89 },
    { name: 'GBM', status: 'OPERATIONAL', activeUsers: 67 },
    { name: 'Support', status: 'OPERATIONAL', activeUsers: 198 },
    { name: 'Marketing', status: 'DEGRADED', activeUsers: 124 }
  ],
  alerts: [
    {
      id: 'alert_001',
      severity: 'WARNING',
      service: 'Audit Service',
      message: 'Response time elevated. Investigating.',
      timestamp: new Date('2025-02-08T10:45:00Z')
    }
  ]
};
```

### Audit Logs Mock

```typescript
const mockAuditLogs = [
  {
    id: 'audit_001',
    action: 'USER_CREATED',
    entity: 'SystemUser',
    entityId: 'usr_new_001',
    performedBy: 'usr_admin_001',
    timestamp: new Date('2025-02-08T10:30:00Z'),
    details: {
      userEmail: 'new.user@gogidix.com',
      roles: ['DOMAIN_ADMIN'],
      domains: ['hr']
    },
    ipAddress: '10.0.0.10'
  },
  {
    id: 'audit_002',
    action: 'PERMISSION_GRANTED',
    entity: 'Permission',
    entityId: 'perm_security_manage',
    performedBy: 'usr_admin_001',
    timestamp: new Date('2025-02-08T10:15:00Z'),
    details: {
      targetUserId: 'usr_sec_002',
      permission: 'security.manage'
    },
    ipAddress: '10.0.0.10'
  },
  {
    id: 'audit_003',
    action: 'SECURITY_EVENT_ESCALATED',
    entity: 'SecurityEvent',
    entityId: 'sec_evt_002',
    performedBy: 'usr_sec_001',
    timestamp: new Date('2025-02-08T09:20:00Z'),
    details: {
      eventSeverity: 'CRITICAL',
      escalatedTo: 'usr_admin_001'
    },
    ipAddress: '10.0.0.15'
  }
];
```

---

## ERROR RESPONSES

### Standard Error Format

```typescript
interface ApiError {
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
| `AUTH_001` | 401 | Invalid credentials | No |
| `AUTH_002` | 401 | Session expired | No |
| `AUTH_003` | 403 | Insufficient permissions | No |
| `AUTH_004` | 429 | Too many login attempts | Yes (after delay) |
| `USR_001` | 404 | User not found | No |
| `USR_002` | 409 | User already exists | No |
| `USR_003` | 400 | Invalid user data | No |
| `SEC_001` | 500 | Security service unavailable | Yes |
| `SYS_001` | 503 | System maintenance | Yes |
| `SYS_002` | 429 | Rate limit exceeded | Yes |

### Error Response Examples

```json
// 401 Unauthorized - Invalid Token
{
  "success": false,
  "error": {
    "code": "AUTH_001",
    "message": "Invalid authentication token",
    "timestamp": "2025-02-08T11:00:00Z",
    "requestId": "req_abc123"
  }
}

// 403 Forbidden - Insufficient Permissions
{
  "success": false,
  "error": {
    "code": "AUTH_003",
    "message": "Insufficient permissions to perform this action",
    "details": {
      "required": ["users.delete"],
      "granted": ["users.read", "users.update"]
    },
    "timestamp": "2025-02-08T11:00:00Z",
    "requestId": "req_def456"
  }
}

// 409 Conflict - User Exists
{
  "success": false,
  "error": {
    "code": "USR_002",
    "message": "User with this email already exists",
    "details": {
      "email": "existing.user@gogidix.com",
      "existingUserId": "usr_12345"
    },
    "timestamp": "2025-02-08T11:00:00Z",
    "requestId": "req_ghi789"
  }
}
```

---

**Document End**

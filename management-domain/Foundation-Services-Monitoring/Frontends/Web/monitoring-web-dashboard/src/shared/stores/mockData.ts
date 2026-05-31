import type {
  Service,
  Alert,
  ServiceMetrics,
  DashboardStats,
  ServiceDependency,
  Incident,
} from '@shared/types'

// Mock Services Data
export const MOCK_SERVICES: Service[] = [
  {
    id: 'svc_api_gateway',
    name: 'API Gateway',
    type: 'api_gateway',
    description: 'Central API gateway for all incoming requests',
    status: 'healthy',
    version: '2.4.1',
    uptime: 99.99,
    responseTime: 45,
    errorRate: 0.01,
    throughput: 12500,
    lastChecked: new Date().toISOString(),
    region: 'us-east-1',
    dependencies: ['svc_auth', 'svc_cache'],
    dependents: ['svc_web', 'svc_mobile'],
    endpoint: 'https://api.gogidix.com',
    healthCheckUrl: 'https://api.gogidix.com/health',
  },
  {
    id: 'svc_auth',
    name: 'Authentication Service',
    type: 'auth_service',
    description: 'Handles user authentication and authorization',
    status: 'healthy',
    version: '1.8.3',
    uptime: 99.95,
    responseTime: 32,
    errorRate: 0.02,
    throughput: 4200,
    lastChecked: new Date().toISOString(),
    region: 'us-east-1',
    dependencies: ['svc_database', 'svc_cache'],
    dependents: ['svc_api_gateway'],
    endpoint: 'https://auth.gogidix.com',
    healthCheckUrl: 'https://auth.gogidix.com/health',
  },
  {
    id: 'svc_notification',
    name: 'Notification Service',
    type: 'notification_service',
    description: 'Sends email, push, and SMS notifications',
    status: 'degraded',
    version: '2.1.0',
    uptime: 98.50,
    responseTime: 450,
    errorRate: 2.5,
    throughput: 850,
    lastChecked: new Date().toISOString(),
    region: 'us-east-1',
    dependencies: ['svc_message_queue', 'svc_database'],
    dependents: ['svc_api_gateway', 'svc_web'],
    endpoint: 'https://notification.gogidix.com',
    healthCheckUrl: 'https://notification.gogidix.com/health',
  },
  {
    id: 'svc_storage',
    name: 'File Storage Service',
    type: 'file_storage',
    description: 'Object storage for files and documents',
    status: 'healthy',
    version: '3.0.2',
    uptime: 99.90,
    responseTime: 120,
    errorRate: 0.05,
    throughput: 3200,
    lastChecked: new Date().toISOString(),
    region: 'us-east-1',
    dependencies: ['svc_database', 'svc_cdn'],
    dependents: ['svc_api_gateway', 'svc_web'],
    endpoint: 'https://storage.gogidix.com',
    healthCheckUrl: 'https://storage.gogidix.com/health',
  },
  {
    id: 'svc_cache',
    name: 'Cache Service',
    type: 'cache_service',
    description: 'Redis-based caching layer',
    status: 'healthy',
    version: '6.2.5',
    uptime: 99.98,
    responseTime: 8,
    errorRate: 0.01,
    throughput: 18000,
    lastChecked: new Date().toISOString(),
    region: 'us-east-1',
    dependencies: [],
    dependents: ['svc_api_gateway', 'svc_auth', 'svc_storage'],
    endpoint: 'redis://cache.gogidix.com',
  },
  {
    id: 'svc_message_queue',
    name: 'Message Queue',
    type: 'message_queue',
    description: 'RabbitMQ-based message broker',
    status: 'healthy',
    version: '3.9.0',
    uptime: 99.92,
    responseTime: 15,
    errorRate: 0.02,
    throughput: 5600,
    lastChecked: new Date().toISOString(),
    region: 'us-east-1',
    dependencies: [],
    dependents: ['svc_notification', 'svc_ai'],
    endpoint: 'amqp://queue.gogidix.com',
  },
  {
    id: 'svc_ai',
    name: 'AI Orchestration Service',
    type: 'ai_orchestration',
    description: 'AI model orchestration and inference',
    status: 'critical',
    version: '1.2.4',
    uptime: 94.20,
    responseTime: 0,
    errorRate: 100,
    throughput: 0,
    lastChecked: new Date().toISOString(),
    region: 'us-west-2',
    dependencies: ['svc_database', 'svc_cache', 'svc_message_queue'],
    dependents: ['svc_api_gateway', 'svc_web'],
    endpoint: 'https://ai.gogidix.com',
    healthCheckUrl: 'https://ai.gogidix.com/health',
  },
  {
    id: 'svc_email',
    name: 'Email Service',
    type: 'email_service',
    description: 'Transactional email sending',
    status: 'healthy',
    version: '1.5.2',
    uptime: 99.85,
    responseTime: 234,
    errorRate: 0.1,
    throughput: 1200,
    lastChecked: new Date().toISOString(),
    region: 'us-east-1',
    dependencies: ['svc_message_queue'],
    dependents: ['svc_notification'],
    endpoint: 'https://email.gogidix.com',
  },
  {
    id: 'svc_database',
    name: 'Primary Database',
    type: 'database',
    description: 'PostgreSQL primary database',
    status: 'healthy',
    version: '14.5',
    uptime: 99.99,
    responseTime: 18,
    errorRate: 0,
    throughput: 8500,
    lastChecked: new Date().toISOString(),
    region: 'us-east-1',
    dependencies: [],
    dependents: ['svc_auth', 'svc_storage', 'svc_ai'],
    endpoint: 'postgresql://db.gogidix.com',
  },
  {
    id: 'svc_cdn',
    name: 'CDN Service',
    type: 'cdn',
    description: 'Content delivery network',
    status: 'healthy',
    version: '4.1.0',
    uptime: 99.99,
    responseTime: 25,
    errorRate: 0,
    throughput: 25000,
    lastChecked: new Date().toISOString(),
    region: 'global',
    dependencies: ['svc_storage'],
    dependents: [],
    endpoint: 'https://cdn.gogidix.com',
  },
]

// Mock Alerts Data
export const MOCK_ALERTS: Alert[] = [
  {
    id: 'alt_001',
    title: 'AI Orchestration Service Down',
    description: 'The AI Orchestration service is not responding. Health checks failing for 5 minutes.',
    severity: 'critical',
    status: 'active',
    serviceId: 'svc_ai',
    serviceName: 'AI Orchestration Service',
    metric: 'health_check',
    threshold: 1,
    currentValue: 0,
    triggeredAt: new Date(Date.now() - 5 * 60 * 1000).toISOString(),
    actions: [],
  },
  {
    id: 'alt_002',
    title: 'Notification Service Response Time Degraded',
    description: 'Response time exceeded threshold: 450ms (threshold: 200ms)',
    severity: 'warning',
    status: 'active',
    serviceId: 'svc_notification',
    serviceName: 'Notification Service',
    metric: 'response_time',
    threshold: 200,
    currentValue: 450,
    triggeredAt: new Date(Date.now() - 23 * 60 * 1000).toISOString(),
    actions: [],
  },
  {
    id: 'alt_003',
    title: 'Cache Service Memory High',
    description: 'Memory usage at 82% (threshold: 80%)',
    severity: 'warning',
    status: 'active',
    serviceId: 'svc_cache',
    serviceName: 'Cache Service',
    metric: 'memory_usage',
    threshold: 80,
    currentValue: 82,
    triggeredAt: new Date(Date.now() - 60 * 60 * 1000).toISOString(),
    actions: [],
  },
  {
    id: 'alt_004',
    title: 'API Gateway Error Rate Spike',
    description: 'Error rate temporarily spiked to 2.5%',
    severity: 'error',
    status: 'acknowledged',
    serviceId: 'svc_api_gateway',
    serviceName: 'API Gateway',
    metric: 'error_rate',
    threshold: 1,
    currentValue: 0.01,
    triggeredAt: new Date(Date.now() - 2 * 60 * 60 * 1000).toISOString(),
    acknowledgedAt: new Date(Date.now() - 90 * 60 * 1000).toISOString(),
    acknowledgedBy: 'Mike Chen',
    actions: [
      {
        id: 'act_001',
        type: 'acknowledged',
        userId: 'usr-devops-001',
        userName: 'Mike Chen',
        timestamp: new Date(Date.now() - 90 * 60 * 1000).toISOString(),
        note: 'Investigating the spike. Looks like a transient issue.',
      },
    ],
  },
  {
    id: 'alt_005',
    title: 'Email Service Queue Building Up',
    description: 'Email queue depth exceeded 1000 messages',
    severity: 'warning',
    status: 'resolved',
    serviceId: 'svc_email',
    serviceName: 'Email Service',
    metric: 'queue_depth',
    threshold: 1000,
    currentValue: 150,
    triggeredAt: new Date(Date.now() - 4 * 60 * 60 * 1000).toISOString(),
    acknowledgedAt: new Date(Date.now() - 3 * 60 * 60 * 1000).toISOString(),
    acknowledgedBy: 'Sarah Johnson',
    resolvedAt: new Date(Date.now() - 2 * 60 * 60 * 1000).toISOString(),
    resolvedBy: 'Sarah Johnson',
    actions: [
      {
        id: 'act_002',
        type: 'acknowledged',
        userId: 'usr-sre-001',
        userName: 'Sarah Johnson',
        timestamp: new Date(Date.now() - 3 * 60 * 60 * 1000).toISOString(),
        note: 'Added more workers to process the queue.',
      },
      {
        id: 'act_003',
        type: 'resolved',
        userId: 'usr-sre-001',
        userName: 'Sarah Johnson',
        timestamp: new Date(Date.now() - 2 * 60 * 60 * 1000).toISOString(),
        note: 'Queue cleared. Service back to normal.',
      },
    ],
  },
]

// Mock Dashboard Stats
export const MOCK_DASHBOARD_STATS: DashboardStats = {
  totalServices: MOCK_SERVICES.length,
  healthyServices: MOCK_SERVICES.filter(s => s.status === 'healthy').length,
  degradedServices: MOCK_SERVICES.filter(s => s.status === 'degraded').length,
  criticalServices: MOCK_SERVICES.filter(s => s.status === 'critical').length,
  unknownServices: MOCK_SERVICES.filter(s => s.status === 'unknown').length,
  activeAlerts: MOCK_ALERTS.filter(a => a.status === 'active').length,
  criticalAlerts: MOCK_ALERTS.filter(a => a.status === 'active' && a.severity === 'critical').length,
  averageResponseTime: Math.round(
    MOCK_SERVICES
      .filter(s => s.status !== 'critical')
      .reduce((sum, s) => sum + s.responseTime, 0) /
      MOCK_SERVICES.filter(s => s.status !== 'critical').length
  ),
  averageUptime: Math.round(
    MOCK_SERVICES.reduce((sum, s) => sum + s.uptime, 0) / MOCK_SERVICES.length * 100
  ) / 100,
}

// Mock Service Dependencies
export const MOCK_DEPENDENCIES: ServiceDependency[] = [
  { from: 'svc_api_gateway', to: 'svc_auth', type: 'synchronous', strength: 1 },
  { from: 'svc_api_gateway', to: 'svc_cache', type: 'synchronous', strength: 0.8 },
  { from: 'svc_api_gateway', to: 'svc_notification', type: 'asynchronous', strength: 0.3 },
  { from: 'svc_api_gateway', to: 'svc_storage', type: 'synchronous', strength: 0.6 },
  { from: 'svc_api_gateway', to: 'svc_ai', type: 'synchronous', strength: 0.4 },
  { from: 'svc_auth', to: 'svc_database', type: 'synchronous', strength: 1 },
  { from: 'svc_auth', to: 'svc_cache', type: 'synchronous', strength: 0.9 },
  { from: 'svc_notification', to: 'svc_message_queue', type: 'asynchronous', strength: 1 },
  { from: 'svc_notification', to: 'svc_database', type: 'synchronous', strength: 0.5 },
  { from: 'svc_storage', to: 'svc_database', type: 'synchronous', strength: 1 },
  { from: 'svc_storage', to: 'svc_cdn', type: 'asynchronous', strength: 0.7 },
  { from: 'svc_ai', to: 'svc_database', type: 'synchronous', strength: 0.8 },
  { from: 'svc_ai', to: 'svc_cache', type: 'synchronous', strength: 0.6 },
  { from: 'svc_ai', to: 'svc_message_queue', type: 'asynchronous', strength: 0.4 },
  { from: 'svc_email', to: 'svc_message_queue', type: 'asynchronous', strength: 1 },
]

// Mock Incidents
export const MOCK_INCIDENTS: Incident[] = [
  {
    id: 'inc_001',
    title: 'AI Orchestration Service Outage',
    description: 'The AI Orchestration service is completely down. All health checks failing. Users unable to access AI features.',
    severity: 'critical',
    status: 'open',
    serviceId: 'svc_ai',
    assignedTo: 'Mike Chen',
    createdBy: 'System',
    createdAt: new Date(Date.now() - 5 * 60 * 1000).toISOString(),
    alerts: ['alt_001'],
    updates: [
      {
        id: 'upd_001',
        incidentId: 'inc_001',
        message: 'Incident created automatically from critical alert.',
        userId: 'system',
        userName: 'System',
        timestamp: new Date(Date.now() - 5 * 60 * 1000).toISOString(),
      },
    ],
  },
  {
    id: 'inc_002',
    title: 'Notification Service Performance Degradation',
    description: 'Notification service experiencing high latency. Email notifications delayed.',
    severity: 'warning',
    status: 'investigating',
    serviceId: 'svc_notification',
    assignedTo: 'Sarah Johnson',
    createdBy: 'Mike Chen',
    createdAt: new Date(Date.now() - 30 * 60 * 1000).toISOString(),
    alerts: ['alt_002'],
    updates: [
      {
        id: 'upd_002',
        incidentId: 'inc_002',
        message: 'Investigating the root cause. Looking at recent deployments.',
        userId: 'usr-sre-001',
        userName: 'Sarah Johnson',
        timestamp: new Date(Date.now() - 25 * 60 * 1000).toISOString(),
      },
    ],
  },
]

// Generate mock metrics data for charts
export function generateMockMetrics(
  serviceId: string,
  hours: number = 24
): { timestamp: string; value: number }[] {
  const data: { timestamp: string; value: number }[] = []
  const now = Date.now()
  const interval = (hours * 60 * 60 * 1000) / 100 // 100 data points

  for (let i = 0; i < 100; i++) {
    const timestamp = new Date(now - (100 - i) * interval)
    // Add some randomness and patterns
    let baseValue = 50
    if (serviceId === 'svc_ai') {
      baseValue = 0 // Service is down
    } else if (serviceId === 'svc_notification') {
      baseValue = 400 + Math.random() * 100
    } else {
      baseValue = 30 + Math.random() * 30
    }

    data.push({
      timestamp: timestamp.toISOString(),
      value: Math.round(baseValue),
    })
  }

  return data
}

// Mock metrics by service
export const MOCK_METRICS: Record<string, { timestamp: string; value: number }[]> = {
  svc_api_gateway: generateMockMetrics('svc_api_gateway'),
  svc_auth: generateMockMetrics('svc_auth'),
  svc_notification: generateMockMetrics('svc_notification'),
  svc_ai: generateMockMetrics('svc_ai'),
  svc_cache: generateMockMetrics('svc_cache'),
  svc_storage: generateMockMetrics('svc_storage'),
  svc_message_queue: generateMockMetrics('svc_message_queue'),
  svc_email: generateMockMetrics('svc_email'),
  svc_database: generateMockMetrics('svc_database'),
  svc_cdn: generateMockMetrics('svc_cdn'),
}

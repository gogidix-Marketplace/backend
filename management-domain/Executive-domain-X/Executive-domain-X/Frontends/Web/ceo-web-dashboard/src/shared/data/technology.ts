export const infrastructureSummary = {
  kafkaClusters: 4,
  mongodbClusters: 6,
  postgresInstances: 12,
  redisClusters: 5,
  rabbitmqCluster: 2,
  elasticCluster: 3,
  totalHealthy: 31,
  totalComponents: 32,
}

export const deploymentPipeline = {
  active: 3,
  queued: 7,
  completedToday: 24,
  successRate: 96.8,
  pending: [
    { unit: 'E-Commerce', service: 'search-service', version: 'v4.2.1', environment: 'production' },
    { unit: 'Haulage', service: 'route-optimization-service', version: 'v2.8.0', environment: 'staging' },
    { unit: 'Courier', service: 'pricing-service', version: 'v3.1.4', environment: 'production' },
    { unit: 'Warehousing', service: 'picking-service', version: 'v1.9.2', environment: 'production' },
    { unit: 'Air Freight', service: 'awb-service', version: 'v2.3.0', environment: 'staging' },
    { unit: 'Admin Core', service: 'auth-service', version: 'v5.0.1', environment: 'production' },
    { unit: 'E-Commerce', service: 'recommendation-service', version: 'v3.5.0', environment: 'staging' },
  ],
}

export const securityPosture: Array<{
  framework: string
  score: number
  status: 'compliant' | 'partial' | 'non_compliant'
  lastAudit: string
}> = [
  { framework: 'ISO 27001', score: 92, status: 'compliant', lastAudit: '2026-03-15' },
  { framework: 'SOC 2 Type II', score: 88, status: 'compliant', lastAudit: '2026-02-28' },
  { framework: 'PCI DSS v4.0', score: 85, status: 'partial', lastAudit: '2026-01-20' },
  { framework: 'GDPR', score: 90, status: 'compliant', lastAudit: '2026-03-01' },
  { framework: 'NDPR (Nigeria)', score: 94, status: 'compliant', lastAudit: '2026-02-15' },
  { framework: 'OWASP Top 10', score: 87, status: 'compliant', lastAudit: '2026-04-01' },
  { framework: 'NIST CSF', score: 82, status: 'partial', lastAudit: '2026-01-10' },
]

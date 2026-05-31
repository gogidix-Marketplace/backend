import type { ServiceHealthStatus, BusinessUnitSlug } from '@shared/types'

const unitSlugs: BusinessUnitSlug[] = [
  'courier',
  'ecommerce',
  'warehousing',
  'air-freight',
  'ocean-shipping',
  'haulage',
  'procurement',
  'admin-core',
]

const serviceCounts: Record<BusinessUnitSlug, number> = {
  courier: 38,
  ecommerce: 34,
  warehousing: 32,
  'air-freight': 35,
  'ocean-shipping': 36,
  haulage: 33,
  procurement: 30,
  'admin-core': 34,
}

const basePorts: Record<BusinessUnitSlug, number> = {
  courier: 8000,
  ecommerce: 8100,
  warehousing: 8200,
  'air-freight': 8300,
  'ocean-shipping': 8400,
  haulage: 8500,
  procurement: 8600,
  'admin-core': 8700,
}

const statusCycle: Array<'healthy' | 'degraded' | 'offline'> = [
  'healthy', 'healthy', 'healthy', 'healthy', 'healthy', 'healthy',
  'healthy', 'healthy', 'healthy', 'healthy', 'healthy', 'healthy',
  'healthy', 'degraded', 'offline',
]

function generateServices(): ServiceHealthStatus[] {
  const services: ServiceHealthStatus[] = []
  let globalIdx = 0

  for (const unitId of unitSlugs) {
    const count = serviceCounts[unitId]
    const basePort = basePorts[unitId]
    for (let i = 0; i < count; i++) {
      globalIdx++
      const status = statusCycle[globalIdx % statusCycle.length]
      services.push({
        name: `${unitId}-service-${i + 1}`,
        port: basePort + i,
        status,
        uptime: status === 'healthy' ? 99.9 : status === 'degraded' ? 95.2 : 0,
        responseTime: status === 'healthy' ? 42 : status === 'degraded' ? 320 : 0,
        cpu: status === 'healthy' ? 35 : status === 'degraded' ? 78 : 0,
        memory: status === 'healthy' ? 48 : status === 'degraded' ? 85 : 0,
        region: 'west-africa',
        businessUnit: unitId,
      })
    }
  }

  return services
}

export const allServices: ServiceHealthStatus[] = generateServices()

export const servicesByUnit = unitSlugs.reduce(
  (acc, unitId) => {
    acc[unitId] = allServices.filter((s) => s.businessUnit === unitId)
    return acc
  },
  {} as Record<BusinessUnitSlug, ServiceHealthStatus[]>
)

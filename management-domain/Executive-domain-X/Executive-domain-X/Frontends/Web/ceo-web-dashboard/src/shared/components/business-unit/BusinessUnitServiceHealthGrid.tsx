import { allServices, servicesByUnit } from '@shared/data/service-health'
import { businessUnits } from '@shared/data/business-units'
import type { BusinessUnitSlug } from '@shared/types'
import { cn } from '@shared/utils/cn'

export interface BusinessUnitServiceHealthGridProps {
  unitId?: BusinessUnitSlug | 'all'
}

const statusColors: Record<string, string> = {
  healthy: '#10B981',
  degraded: '#EF4444',
  offline: '#6B7280',
}

function getStatusForUnit(services: { status: string }[]): string {
  const offline = services.filter((s) => s.status === 'offline').length
  const degraded = services.filter((s) => s.status === 'degraded').length
  if (offline > 0) return 'degraded'
  if (degraded > 0) return 'warning'
  return 'healthy'
}

export function BusinessUnitServiceHealthGrid({
  unitId = 'all',
}: BusinessUnitServiceHealthGridProps) {
  const filteredSlugs: BusinessUnitSlug[] =
    unitId === 'all'
      ? businessUnits.map((u) => u.id)
      : [unitId]

  const totalCount =
    unitId === 'all'
      ? allServices.length
      : (servicesByUnit[unitId as BusinessUnitSlug]?.length ?? 0)

  return (
    <div className="space-y-6">
      {filteredSlugs.map((slug) => {
        const unitServices = servicesByUnit[slug]
        if (!unitServices) return null

        const unit = businessUnits.find((u) => u.id === slug)
        const overallStatus = getStatusForUnit(unitServices)
        const healthyCount = unitServices.filter((s) => s.status === 'healthy').length
        const degradedCount = unitServices.filter((s) => s.status === 'degraded').length
        const offlineCount = unitServices.filter((s) => s.status === 'offline').length

        return (
          <div key={slug}>
            <div className="mb-2 flex items-center gap-2">
              <span
                className="h-3 w-3 rounded-full"
                style={{ backgroundColor: unit?.color ?? '#6B7280' }}
              />
              <span className="text-sm font-semibold text-slate-900 dark:text-slate-100">
                {unit?.name ?? slug}
              </span>
              <span className="text-xs text-slate-500">
                ({unitServices.length} services)
              </span>
              <span
                className={cn(
                  'ml-2 rounded-full px-2 py-0.5 text-[10px] font-semibold uppercase',
                  overallStatus === 'healthy' &&
                    'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400',
                  overallStatus === 'warning' &&
                    'bg-amber-100 text-amber-700 dark:bg-amber-900/30 dark:text-amber-400',
                  overallStatus === 'degraded' &&
                    'bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400'
                )}
              >
                {overallStatus}
              </span>
              <span className="ml-auto flex gap-3 text-[10px] text-slate-500">
                <span>
                  <span className="font-medium text-green-600">{healthyCount}</span> ok
                </span>
                <span>
                  <span className="font-medium text-red-600">{degradedCount}</span> degraded
                </span>
                <span>
                  <span className="font-medium text-slate-500">{offlineCount}</span> offline
                </span>
              </span>
            </div>
            <div className="flex flex-wrap" style={{ gap: '4px' }}>
              {unitServices.map((service, idx) => (
                <div
                  key={`${service.name}-${idx}`}
                  className="rounded-sm"
                  style={{
                    backgroundColor: statusColors[service.status],
                    width: '8px',
                    height: '8px',
                  }}
                  title={`${service.name}: ${service.status}`}
                />
              ))}
            </div>
          </div>
        )
      })}

      <div className="flex items-center gap-4 border-t pt-3 text-xs text-slate-500">
        <span className="font-medium">
          Total: {totalCount} services
        </span>
        <span className="flex items-center gap-1">
          <span
            className="inline-block rounded-sm"
            style={{ backgroundColor: statusColors.healthy, width: '8px', height: '8px' }}
          />
          Healthy
        </span>
        <span className="flex items-center gap-1">
          <span
            className="inline-block rounded-sm"
            style={{ backgroundColor: statusColors.degraded, width: '8px', height: '8px' }}
          />
          Degraded
        </span>
        <span className="flex items-center gap-1">
          <span
            className="inline-block rounded-sm"
            style={{ backgroundColor: statusColors.offline, width: '8px', height: '8px' }}
          />
          Offline
        </span>
      </div>
    </div>
  )
}

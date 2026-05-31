import { Card, CardContent } from '@shared/components/ui/card'
import { businessUnits } from '@shared/data/business-units'
import { cn } from '@shared/utils/cn'
import { TrendingUp, TrendingDown } from 'lucide-react'

export interface BusinessUnitRevenueGridProps {
  period?: string
}

function formatCurrency(value: number): string {
  if (value >= 1000000) {
    return `$${(value / 1000000).toFixed(1)}M`
  }
  if (value >= 1000) {
    return `$${(value / 1000).toFixed(0)}K`
  }
  return `$${value}`
}

export function BusinessUnitRevenueGrid({ period }: BusinessUnitRevenueGridProps) {
  return (
    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
      {businessUnits.map((unit) => {
        const isPositive = unit.revenueGrowth >= 0
        const healthRatio = `${unit.healthyServices}/${unit.serviceCount}`

        return (
          <Card key={unit.id} className="overflow-hidden">
            <div
              className="h-full"
              style={{ borderLeft: `4px solid ${unit.color}` }}
            >
              <CardContent className="p-4 space-y-3">
                <div className="flex items-center gap-2">
                  <span
                    className="flex h-8 w-8 items-center justify-center rounded-lg text-xs font-bold text-white"
                    style={{ backgroundColor: unit.color }}
                  >
                    {unit.icon}
                  </span>
                  <span className="text-sm font-semibold text-slate-900 dark:text-slate-100 truncate">
                    {unit.name}
                  </span>
                </div>

                <div className="space-y-1">
                  <p className="text-2xl font-bold text-slate-900 dark:text-slate-50">
                    {formatCurrency(unit.revenue)}
                  </p>
                  {period && (
                    <p className="text-xs text-slate-500">{period}</p>
                  )}
                </div>

                <div className="flex items-center gap-1.5">
                  {isPositive ? (
                    <TrendingUp className="h-3.5 w-3.5 text-green-500" />
                  ) : (
                    <TrendingDown className="h-3.5 w-3.5 text-red-500" />
                  )}
                  <span
                    className={cn(
                      'text-sm font-medium',
                      isPositive
                        ? 'text-green-600 dark:text-green-400'
                        : 'text-red-600 dark:text-red-400'
                    )}
                  >
                    {isPositive ? '+' : ''}
                    {unit.revenueGrowth}%
                  </span>
                </div>

                <div className="flex items-center justify-between text-xs text-slate-500 dark:text-slate-400">
                  <span>
                    Services:{' '}
                    <span className="font-medium text-slate-700 dark:text-slate-300">
                      {unit.serviceCount}
                    </span>
                  </span>
                  <span>
                    Health:{' '}
                    <span
                      className={cn(
                        'font-medium',
                        unit.healthyServices === unit.serviceCount
                          ? 'text-green-600 dark:text-green-400'
                          : 'text-amber-600 dark:text-amber-400'
                      )}
                    >
                      {healthRatio}
                    </span>
                  </span>
                </div>
              </CardContent>
            </div>
          </Card>
        )
      })}
    </div>
  )
}

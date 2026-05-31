import { Card, CardContent } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { departments } from '@shared/data/departments'
import { cn } from '@shared/utils/cn'
import { AlertTriangle, CheckCircle } from 'lucide-react'

export interface DepartmentHealthGridProps {
  role?: string
}

function getScoreColor(score: number): string {
  if (score > 90) return 'text-green-600 dark:text-green-400'
  if (score >= 80) return 'text-amber-600 dark:text-amber-400'
  return 'text-red-600 dark:text-red-400'
}

function getScoreBg(score: number): string {
  if (score > 90) return 'bg-green-500'
  if (score >= 80) return 'bg-amber-500'
  return 'bg-red-500'
}

export function DepartmentHealthGrid({ role }: DepartmentHealthGridProps) {
  return (
    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
      {departments.map((dept) => {
        const scoreColor = getScoreColor(dept.healthScore)
        const scoreBg = getScoreBg(dept.healthScore)

        return (
          <Card key={dept.departmentId} className="transition-all hover:shadow-md">
            <CardContent className="p-4 space-y-3">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <span
                    className="flex h-8 w-8 items-center justify-center rounded-lg bg-slate-100 dark:bg-slate-800 text-sm font-bold text-slate-700 dark:text-slate-300"
                    style={{ color: dept.color }}
                  >
                    {dept.icon}
                  </span>
                  <span className="text-sm font-semibold text-slate-900 dark:text-slate-100 truncate">
                    {dept.name}
                  </span>
                </div>
              </div>

              <div className="flex items-center gap-2">
                <span className={cn('text-2xl font-bold', scoreColor)}>
                  {dept.healthScore}
                </span>
                <span className="text-xs text-slate-500">/ 100</span>
                <div className="ml-auto">
                  {dept.healthScore > 90 ? (
                    <CheckCircle className="h-4 w-4 text-green-500" />
                  ) : (
                    <AlertTriangle className="h-4 w-4 text-amber-500" />
                  )}
                </div>
              </div>

              <div className="h-2 w-full overflow-hidden rounded-full bg-slate-200 dark:bg-slate-700">
                <div
                  className={cn('h-full transition-all', scoreBg)}
                  style={{ width: `${dept.healthScore}%` }}
                />
              </div>

              <div className="space-y-1">
                <p className="text-xs text-slate-500">{dept.keyMetric}</p>
                <p className="text-sm font-medium text-slate-700 dark:text-slate-300">
                  {dept.keyMetricValue}
                </p>
              </div>

              <div className="flex items-center justify-between text-xs text-slate-500 dark:text-slate-400">
                <span>
                  Services:{' '}
                  <span className="font-medium text-slate-700 dark:text-slate-300">
                    {dept.serviceCount}
                  </span>
                </span>
                <span className="flex items-center gap-1">
                  Alerts:{' '}
                  <Badge
                    variant={
                      dept.alertCount > 3 ? 'destructive' : 'secondary'
                    }
                    className="text-[10px] px-1.5 py-0"
                  >
                    {dept.alertCount}
                  </Badge>
                </span>
                <span>
                  Pending:{' '}
                  <span className="font-medium text-slate-700 dark:text-slate-300">
                    {dept.pendingApprovals}
                  </span>
                </span>
              </div>
            </CardContent>
          </Card>
        )
      })}
    </div>
  )
}

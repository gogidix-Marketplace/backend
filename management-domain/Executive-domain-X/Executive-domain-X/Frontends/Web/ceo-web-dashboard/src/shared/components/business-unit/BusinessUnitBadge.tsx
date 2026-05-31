import type { BusinessUnitSlug } from '@shared/types'
import { businessUnitMap } from '@shared/data/business-units'
import { cn } from '@shared/utils/cn'

export interface BusinessUnitBadgeProps {
  unitId: BusinessUnitSlug
  size?: 'sm' | 'md'
}

export function BusinessUnitBadge({ unitId, size = 'md' }: BusinessUnitBadgeProps) {
  const unit = businessUnitMap[unitId]
  if (!unit) return null

  return (
    <div
      className={cn(
        'inline-flex items-center gap-2 rounded-lg',
        size === 'sm' ? 'px-2 py-1 text-xs' : 'px-3 py-1.5 text-sm'
      )}
      style={{ borderLeft: `4px solid ${unit.color}` }}
    >
      <span
        className={cn(
          'flex items-center justify-center rounded-full bg-slate-100 dark:bg-slate-800 font-semibold',
          size === 'sm' ? 'h-5 w-5 text-[10px]' : 'h-6 w-6 text-xs'
        )}
        style={{ color: unit.color }}
      >
        {unit.icon}
      </span>
      <span className="font-medium text-slate-700 dark:text-slate-300">{unit.name}</span>
    </div>
  )
}

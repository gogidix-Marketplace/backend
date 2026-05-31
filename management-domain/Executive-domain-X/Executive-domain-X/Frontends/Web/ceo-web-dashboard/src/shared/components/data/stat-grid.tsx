import { cn } from '@shared/utils/cn'
import { MetricCard, type MetricCardProps } from './metric-card'

/**
 * StatGrid - Responsive grid of metric cards
 *
 * Provides a responsive grid layout for displaying multiple metric cards
 */

export interface StatGridProps {
  children: React.ReactNode
  columns?: {
    sm?: number
    md?: number
    lg?: number
    xl?: number
  }
  gap?: 'sm' | 'md' | 'lg'
}

const gapClasses = {
  sm: 'gap-2',
  md: 'gap-4',
  lg: 'gap-6',
}

export function StatGrid({ children, columns = { sm: 1, md: 2, lg: 4 }, gap = 'md' }: StatGridProps) {
  const gridClass = cn(
    'grid',
    columns.sm && `grid-cols-${columns.sm}`,
    columns.md && `md:grid-cols-${columns.md}`,
    columns.lg && `lg:grid-cols-${columns.lg}`,
    columns.xl && `xl:grid-cols-${columns.xl}`,
    gapClasses[gap]
  )

  return <div className={gridClass}>{children}</div>
}

/**
 * Quick helper to create a stat grid from data
 */
export interface Stat {
  label: string
  value: string | number
  unit?: string
  change?: number
  trend?: 'up' | 'down' | 'neutral'
  icon?: MetricCardProps['icon']
  color?: MetricCardProps['color']
  onClick?: () => void
}

export function QuickStatGrid({
  stats,
  columns,
  gap = 'md',
}: {
  stats: Stat[]
  columns?: StatGridProps['columns']
  gap?: StatGridProps['gap']
}) {
  return (
    <StatGrid columns={columns} gap={gap}>
      {stats.map((stat, index) => (
        <MetricCard key={index} {...stat} />
      ))}
    </StatGrid>
  )
}

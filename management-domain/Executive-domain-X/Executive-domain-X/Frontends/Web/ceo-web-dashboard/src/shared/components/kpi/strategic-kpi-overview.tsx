import { Card, CardContent, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { cn, formatCurrency, formatPercentage } from '@shared/utils/cn'
import { KPICard } from '@shared/components/data/kpi-card'
import { TrendingUp, TrendingDown, DollarSign, Users, Globe, Target, ArrowUpRight } from 'lucide-react'

/**
 * StrategicKPIOverview Component
 *
 * 5 KPI cards: Revenue, Growth, Domains, Countries, Active Users
 * Each card: icon, label, value, change indicator, status dot, sparkline
 * Click handler for drill-down to detail view
 * Real-time updates via WebSocket
 */

export interface StrategicKPI {
  id: string
  name: string
  value: number
  target: number
  unit: string
  change: number
  trend: 'up' | 'down' | 'neutral'
  status: 'on_track' | 'at_risk' | 'behind' | 'ahead'
  icon: React.ReactNode
  sparkline?: number[]
}

const iconMap = {
  revenue: DollarSign,
  growth: TrendingUp,
  domains: Globe,
  countries: Globe,
  activeUsers: Users,
  target: Target,
}

export interface StrategicKPIOverviewProps {
  kpis?: StrategicKPI[]
  loading?: boolean
  onKPIClick?: (kpi: StrategicKPI) => void
  realTime?: boolean
}

const defaultKPIs: StrategicKPI[] = [
  {
    id: '1',
    name: 'Total Revenue',
    value: 42500000,
    target: 50000000,
    unit: 'USD',
    change: 8.5,
    trend: 'up',
    status: 'on_track',
    icon: <DollarSign className="h-4 w-4" />,
    sparkline: [35, 38, 40, 39, 42, 44, 42.5],
  },
  {
    id: '2',
    name: 'Growth Rate',
    value: 18.5,
    target: 20,
    unit: '%',
    change: 2.3,
    trend: 'up',
    status: 'at_risk',
    icon: <TrendingUp className="h-4 w-4" />,
    sparkline: [12, 14, 15, 16, 17, 18, 18.5],
  },
  {
    id: '3',
    name: 'Active Domains',
    value: 8,
    target: 10,
    unit: '',
    change: 0,
    trend: 'neutral',
    status: 'behind',
    icon: <Globe className="h-4 w-4" />,
    sparkline: [6, 7, 7, 8, 8, 8, 8],
  },
  {
    id: '4',
    name: 'Countries',
    value: 12,
    target: 15,
    unit: '',
    change: 8.3,
    trend: 'up',
    status: 'on_track',
    icon: <Globe className="h-4 w-4" />,
    sparkline: [8, 9, 10, 11, 11, 12, 12],
  },
  {
    id: '5',
    name: 'Active Users',
    value: 1250000,
    target: 1500000,
    unit: '',
    change: 15.2,
    trend: 'up',
    status: 'ahead',
    icon: <Users className="h-4 w-4" />,
    sparkline: [800, 900, 1000, 1100, 1150, 1200, 1250],
  },
]

export function StrategicKPIOverview({
  kpis = defaultKPIs,
  loading = false,
  onKPIClick,
  realTime = false,
}: StrategicKPIOverviewProps) {
  return (
    <Card>
      <CardHeader>
        <div className="flex items-center justify-between">
          <CardTitle>Strategic KPI Overview</CardTitle>
          <div className="flex items-center gap-2">
            {realTime && (
              <Badge variant="outline" className="gap-1">
                <span className="h-2 w-2 rounded-full bg-green-500 animate-pulse" />
                Live
              </Badge>
            )}
            <button
              className="text-sm text-primary hover:underline flex items-center gap-1"
              onClick={() => {/* Navigate to detail view */}}
            >
              View All
              <ArrowUpRight className="h-3 w-3" />
            </button>
          </div>
        </div>
      </CardHeader>
      <CardContent>
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-5">
          {kpis.map((kpi) => (
            <KPICard
              key={kpi.id}
              title={kpi.name}
              value={kpi.unit === 'currency' || kpi.unit === 'USD'
                ? formatCurrency(kpi.value)
                : kpi.unit === '%'
                ? `${kpi.value}%`
                : kpi.value.toLocaleString()}
              unit={kpi.unit === 'currency' || kpi.unit === 'USD' ? '' : kpi.unit}
              target={kpi.unit === 'currency' || kpi.unit === 'USD'
                ? formatCurrency(kpi.target)
                : kpi.unit === '%'
                ? `${kpi.target}%`
                : kpi.target.toLocaleString()}
              change={kpi.change}
              trend={kpi.trend}
              status={kpi.status}
              icon={kpi.icon}
              sparkline={kpi.sparkline}
              onClick={() => onKPIClick?.(kpi)}
              loading={loading}
              size="compact"
            />
          ))}
        </div>
      </CardContent>
    </Card>
  )
}

import { ArrowUpRight, ArrowDownRight, TrendingUp, TrendingDown, Minus, Globe, DollarSign, Users, Building2, AlertCircle, CheckCircle } from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@shared/components/ui/table'
import { cn, formatCurrency, formatPercentage, calculateProgress, getStatusColor } from '@shared/utils/cn'
import { gbmKPIData, regionalMetricsData } from '@shared/mock-data'

function KPICard({
  kpi,
}: {
  kpi: {
    name: string
    value: number
    target: number
    unit: string
    change: number
    trend: 'up' | 'down' | 'neutral'
    status: 'on_track' | 'at_risk' | 'behind' | 'ahead'
  }
}) {
  const progress = calculateProgress(kpi.value, kpi.target)
  const statusConfig = {
    on_track: { label: 'On Track', color: 'bg-green-500' },
    at_risk: { label: 'At Risk', color: 'bg-yellow-500' },
    behind: { label: 'Behind', color: 'bg-red-500' },
    ahead: { label: 'Ahead', color: 'bg-blue-500' },
  }

  const TrendIcon = kpi.trend === 'up' ? TrendingUp : kpi.trend === 'down' ? TrendingDown : Minus

  return (
    <Card>
      <CardHeader className="pb-3">
        <div className="flex items-start justify-between">
          <CardTitle className="text-sm font-medium">{kpi.name}</CardTitle>
          <Badge variant={kpi.status === 'on_track' || kpi.status === 'ahead' ? 'success' : 'warning'}>
            {statusConfig[kpi.status].label}
          </Badge>
        </div>
      </CardHeader>
      <CardContent className="space-y-3">
        <div className="flex items-baseline gap-2">
          <span className="text-2xl font-bold">
            {kpi.unit === 'USD' || kpi.unit === 'customers' || kpi.unit === 'businesses'
              ? kpi.unit === 'USD'
                ? formatCurrency(kpi.value)
                : kpi.value.toLocaleString()
              : `${kpi.value}${kpi.unit}`}
          </span>
          {kpi.unit !== '%' && kpi.unit !== 'customers' && kpi.unit !== 'businesses' && (
            <span className="text-sm text-muted-foreground">
              of {kpi.unit === 'USD' ? formatCurrency(kpi.target) : `${kpi.target}${kpi.unit}`}
            </span>
          )}
        </div>

        <div className="flex items-center gap-2 text-sm">
          <TrendIcon className={cn(
            'h-4 w-4',
            kpi.trend === 'up' ? 'text-green-500' : kpi.trend === 'down' ? 'text-red-500' : 'text-gray-500'
          )} />
          <span className={cn(
            kpi.change > 0 ? 'text-green-600' : kpi.change < 0 ? 'text-red-600' : 'text-gray-600'
          )}>
            {formatPercentage(kpi.change)} from last month
          </span>
        </div>

        <div className="space-y-1">
          <div className="flex justify-between text-xs text-muted-foreground">
            <span>Progress</span>
            <span>{Math.round(progress)}%</span>
          </div>
          <div className="h-2 w-full overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
            <div
              className={cn('h-full transition-all', statusConfig[kpi.status].color)}
              style={{ width: `${Math.min(progress, 100)}%` }}
            />
          </div>
        </div>
      </CardContent>
    </Card>
  )
}

export default function OverviewPage() {
  // Transform KPI data for display
  const kpiDisplayData = gbmKPIData.map(kpi => ({
    name: kpi.name,
    value: kpi.value,
    target: kpi.target,
    unit: kpi.unit === 'USD' ? 'USD' : kpi.unit,
    change: kpi.trendValue,
    trend: kpi.trend,
    status: kpi.status,
  }))

  const recentAlerts = [
    { id: '1', type: 'warning', title: 'Europe Region at Risk', description: 'Revenue declining by 5.2%, requires attention', time: '2 hours ago' },
    { id: '2', type: 'success', title: 'Africa West Exceeding Targets', description: '18.7% growth, ahead of projections', time: '5 hours ago' },
    { id: '3', type: 'info', title: 'Monthly Aggregation Complete', description: 'All regional data successfully aggregated', time: '1 day ago' },
    { id: '4', type: 'warning', title: 'Currency Fluctuation Alert', description: 'NGN rate changed by more than 5%', time: '1 day ago' },
  ]

  const quickActions = [
    { id: '1', title: 'Generate Report', description: 'Create a new custom report', icon: FileText, href: '/reports' },
    { id: '2', title: 'Run Aggregation', description: 'Trigger data aggregation', icon: Database, href: '/aggregation' },
    { id: '3', title: 'Export Data', description: 'Download business data', icon: Download, href: '/export' },
    { id: '4', title: 'View Dashboards', description: 'Access BI dashboards', icon: BarChart3, href: '/bi' },
  ]

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header">
        <h1 className="page-title">Global Business Overview</h1>
        <p className="page-description">
          Strategic health and performance metrics across all regions
        </p>
      </div>

      {/* Global Health Score */}
      <Card className="gbm-gradient text-white">
        <CardContent className="p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-white/80">Global Business Health Score</p>
              <div className="flex items-baseline gap-2">
                <span className="text-4xl font-bold">87</span>
                <span className="text-lg text-white/80">/100</span>
                <Badge className="ml-2 bg-white/20 text-white hover:bg-white/30">
                  On Track
                </Badge>
              </div>
              <p className="mt-2 text-sm text-white/70">
                All regions performing within acceptable parameters
              </p>
            </div>
            <div className="hidden md:block">
              <svg width="120" height="120" viewBox="0 0 120 120">
                <circle
                  cx="60"
                  cy="60"
                  r="50"
                  fill="none"
                  stroke="rgba(255,255,255,0.2)"
                  strokeWidth="10"
                />
                <circle
                  cx="60"
                  cy="60"
                  r="50"
                  fill="none"
                  stroke="white"
                  strokeWidth="10"
                  strokeDasharray={`${87 * 3.14} 314`}
                  strokeDashoffset="0"
                  transform="rotate(-90 60 60)"
                  strokeLinecap="round"
                />
              </svg>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* KPI Cards */}
      <div className="metric-grid">
        {kpiDisplayData.map((kpi) => (
          <KPICard key={kpi.name} kpi={kpi} />
        ))}
      </div>

      {/* Two Column Layout */}
      <div className="grid gap-6 md:grid-cols-2">
        {/* Regional Performance Table */}
        <Card className="md:col-span-2">
          <CardHeader>
            <div className="flex items-center justify-between">
              <div>
                <CardTitle>Regional Performance</CardTitle>
                <CardDescription>Revenue by region vs. target</CardDescription>
              </div>
              <Button variant="outline" size="sm">
                View All Regions
              </Button>
            </div>
          </CardHeader>
          <CardContent>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Region</TableHead>
                  <TableHead>Countries</TableHead>
                  <TableHead>Revenue</TableHead>
                  <TableHead>Target</TableHead>
                  <TableHead>Progress</TableHead>
                  <TableHead>Growth</TableHead>
                  <TableHead>Status</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {regionalMetricsData.map((region) => {
                  const progress = calculateProgress(region.totalRevenue, region.revenueTarget)
                  return (
                    <TableRow key={region.id}>
                      <TableCell className="font-medium">{region.regionName}</TableCell>
                      <TableCell>{region.countries.length}</TableCell>
                      <TableCell>{formatCurrency(region.totalRevenue)}</TableCell>
                      <TableCell>{formatCurrency(region.revenueTarget)}</TableCell>
                      <TableCell>
                        <div className="flex items-center gap-2">
                          <div className="h-2 w-24 overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
                            <div
                              className={cn(
                                'h-full',
                                progress >= 100 ? 'bg-green-500' : progress >= 80 ? 'bg-blue-500' : 'bg-yellow-500'
                              )}
                              style={{ width: `${Math.min(progress, 100)}%` }}
                            />
                          </div>
                          <span className="text-xs">{Math.round(progress)}%</span>
                        </div>
                      </TableCell>
                      <TableCell>
                        <span
                          className={cn(
                            'flex items-center gap-1 text-sm',
                            region.growthRate > 0 ? 'text-green-600' : 'text-red-600'
                          )}
                        >
                          {region.growthRate > 0 ? (
                            <ArrowUpRight className="h-4 w-4" />
                          ) : (
                            <ArrowDownRight className="h-4 w-4" />
                          )}
                          {formatPercentage(region.growthRate)}
                        </span>
                      </TableCell>
                      <TableCell>
                        <Badge className={getStatusColor(region.status)}>
                          {region.status.replace('_', ' ')}
                        </Badge>
                      </TableCell>
                    </TableRow>
                  )
                })}
              </TableBody>
            </Table>
          </CardContent>
        </Card>

        {/* Recent Alerts */}
        <Card>
          <CardHeader>
            <CardTitle>Recent Alerts</CardTitle>
            <CardDescription>System notifications and insights</CardDescription>
          </CardHeader>
          <CardContent className="space-y-4">
            {recentAlerts.map((alert) => (
              <div
                key={alert.id}
                className="flex gap-3 rounded-lg border p-3 hover:bg-slate-50 dark:hover:bg-slate-800"
              >
                <div className={cn('mt-0.5 flex h-6 w-6 items-center justify-center rounded-full', {
                  'bg-yellow-100': alert.type === 'warning',
                  'bg-green-100': alert.type === 'success',
                  'bg-blue-100': alert.type === 'info',
                })}>
                  {alert.type === 'warning' && <AlertCircle className="h-4 w-4 text-yellow-600" />}
                  {alert.type === 'success' && <CheckCircle className="h-4 w-4 text-green-600" />}
                  {alert.type === 'info' && <Globe className="h-4 w-4 text-blue-600" />}
                </div>
                <div className="flex-1">
                  <div className="flex items-start justify-between">
                    <p className="font-medium">{alert.title}</p>
                    <span className="text-xs text-muted-foreground">{alert.time}</span>
                  </div>
                  <p className="text-sm text-muted-foreground">{alert.description}</p>
                </div>
              </div>
            ))}
          </CardContent>
        </Card>

        {/* Quick Actions */}
        <Card>
          <CardHeader>
            <CardTitle>Quick Actions</CardTitle>
            <CardDescription>Frequently used tasks</CardDescription>
          </CardHeader>
          <CardContent className="grid gap-3 sm:grid-cols-2">
            {quickActions.map((action) => (
              <button
                key={action.id}
                className="flex flex-col items-start gap-2 rounded-lg border p-4 text-left transition-colors hover:bg-slate-50 dark:hover:bg-slate-800"
              >
                <action.icon className="h-5 w-5 text-[#1E88E5]" />
                <div>
                  <p className="font-medium">{action.title}</p>
                  <p className="text-sm text-muted-foreground">{action.description}</p>
                </div>
              </button>
            ))}
          </CardContent>
        </Card>
      </div>
    </div>
  )
}

// Import icons for quick actions
import { FileText, Database, Download, BarChart3 } from 'lucide-react'

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@shared/components/ui/tabs'
import { Progress } from '@shared/components/ui/progress'
import { cn, formatCurrency, formatPercentage } from '@shared/utils/cn'
import { Target, Flag, TrendingUp, TrendingDown, CheckCircle2, Clock, AlertCircle } from 'lucide-react'
import * as React from 'react'

// Mock data
const strategicKPIs = [
  {
    id: '1',
    name: 'Market Share Growth',
    value: 15.2,
    target: 20,
    unit: '%',
    trend: 'up' as const,
    status: 'on_track' as const,
    owner: 'CMO',
    lastUpdated: '2024-03-01',
    // Historical data for trend chart
    history: [12.1, 12.8, 13.5, 14.2, 14.8, 15.2],
  },
  {
    id: '2',
    name: 'Digital Transformation',
    value: 72,
    target: 100,
    unit: '%',
    trend: 'up' as const,
    status: 'on_track' as const,
    owner: 'CTO',
    lastUpdated: '2024-02-28',
    history: [45, 52, 58, 64, 69, 72],
  },
  {
    id: '3',
    name: 'Talent Retention Rate',
    value: 85,
    target: 90,
    unit: '%',
    trend: 'down' as const,
    status: 'at_risk' as const,
    owner: 'CHRO',
    lastUpdated: '2024-02-27',
    history: [92, 90, 88, 87, 86, 85],
  },
  {
    id: '4',
    name: 'Sustainability Goals',
    value: 45,
    target: 60,
    unit: '%',
    trend: 'up' as const,
    status: 'behind' as const,
    owner: 'CSO',
    lastUpdated: '2024-03-01',
    history: [25, 30, 35, 40, 42, 45],
  },
]

const goalsAndOKRs = [
  {
    id: '1',
    objective: 'Expand Global Market Presence',
    progress: 68,
    status: 'on_track' as const,
    keyResults: [
      { title: 'Launch in 3 new countries', target: 3, current: 2 },
      { title: 'Achieve $50M revenue from new markets', target: 50, current: 32, unit: 'M$' },
      { title: 'Establish 50 new partnerships', target: 50, current: 41 },
    ],
    owner: 'Sales VP',
    period: 'Q1 2024',
  },
  {
    id: '2',
    objective: 'Achieve Carbon Neutrality',
    progress: 42,
    status: 'behind' as const,
    keyResults: [
      { title: 'Reduce carbon emissions by 50%', target: 50, current: 35, unit: '%' },
      { title: 'Transition to 100% renewable energy', target: 100, current: 60, unit: '%' },
      { title: 'Offset 10,000 tons of CO2', target: 10, current: 4, unit: 'K tons' },
    ],
    owner: 'CSO',
    period: 'Q1 2024',
  },
  {
    id: '3',
    objective: 'Deliver Industry-Leading Customer Experience',
    progress: 91,
    status: 'ahead' as const,
    keyResults: [
      { title: 'Achieve 95% CSAT score', target: 95, current: 96, unit: '%' },
      { title: 'Reduce response time to 2 hours', target: 2, current: 1.5, unit: 'hrs' },
      { title: 'Implement 24/7 support globally', target: 1, current: 1, unit: 'done' },
    ],
    owner: 'Customer Experience VP',
    period: 'Q1 2024',
  },
]

const strategicInitiatives = [
  {
    id: '1',
    name: 'AI-Powered Analytics Platform',
    description: 'Build enterprise-wide AI analytics for data-driven decisions',
    status: 'in_progress' as const,
    priority: 'high' as const,
    owner: 'CTO Office',
    budget: 5000000,
    spent: 2750000,
    startDate: '2024-01-01',
    targetDate: '2024-12-31',
    milestones: [
      { name: 'Phase 1 - Infrastructure', status: 'completed' as const },
      { name: 'Phase 2 - Data Integration', status: 'in_progress' as const },
      { name: 'Phase 3 - ML Models', status: 'pending' as const },
      { name: 'Phase 4 - Deployment', status: 'pending' as const },
    ],
  },
  {
    id: '2',
    name: 'Global Expansion - Asia Pacific',
    description: 'Enter 5 new markets in APAC region',
    status: 'on_track' as const,
    priority: 'medium' as const,
    owner: 'International VP',
    budget: 15000000,
    spent: 8000000,
    startDate: '2024-01-01',
    targetDate: '2024-06-30',
    milestones: [
      { name: 'Market Research', status: 'completed' as const },
      { name: 'Legal Setup', status: 'completed' as const },
      { name: 'Team Hiring', status: 'in_progress' as const },
      { name: 'Launch Operations', status: 'pending' as const },
    ],
  },
  {
    id: '3',
    name: 'Digital Workplace Transformation',
    description: 'Modernize internal tools and collaboration platforms',
    status: 'at_risk' as const,
    priority: 'high' as const,
    owner: 'CIO Office',
    budget: 3500000,
    spent: 2100000,
    startDate: '2024-01-01',
    targetDate: '2024-09-30',
    milestones: [
      { name: 'Vendor Selection', status: 'completed' as const },
      { name: 'Pilot Program', status: 'in_progress' as const },
      { name: 'Full Rollout', status: 'pending' as const },
      { name: 'Training Complete', status: 'pending' as const },
    ],
  },
]

function StatusIcon({ status }: { status: string }) {
  switch (status) {
    case 'completed':
      return <CheckCircle2 className="h-4 w-4 text-green-500" />
    case 'in_progress':
      return <Clock className="h-4 w-4 text-blue-500" />
    case 'pending':
      return <AlertCircle className="h-4 w-4 text-gray-400" />
    default:
      return null
  }
}

// Consistent status badge component with uniform styling
function StatusBadge({ status }: { status: string }) {
  const statusConfig = {
    on_track: {
      label: 'ON TRACK',
      bgColor: 'bg-emerald-100 dark:bg-emerald-900/30',
      textColor: 'text-emerald-700 dark:text-emerald-400',
      borderColor: 'border-emerald-200 dark:border-emerald-800',
    },
    at_risk: {
      label: 'AT RISK',
      bgColor: 'bg-amber-100 dark:bg-amber-900/30',
      textColor: 'text-amber-700 dark:text-amber-400',
      borderColor: 'border-amber-200 dark:border-amber-800',
    },
    behind: {
      label: 'BEHIND',
      bgColor: 'bg-amber-100 dark:bg-amber-900/30',
      textColor: 'text-amber-700 dark:text-amber-400',
      borderColor: 'border-amber-200 dark:border-amber-800',
    },
    ahead: {
      label: 'AHEAD',
      bgColor: 'bg-blue-100 dark:bg-blue-900/30',
      textColor: 'text-blue-700 dark:text-blue-400',
      borderColor: 'border-blue-200 dark:border-blue-800',
    },
  }

  const config = statusConfig[status as keyof typeof statusConfig] || statusConfig.on_track

  return (
    <span
      className={cn(
        'inline-flex items-center px-2.5 py-1 text-xs font-semibold rounded-md border',
        config.bgColor,
        config.textColor,
        config.borderColor
      )}
    >
      {config.label}
    </span>
  )
}

// Mini sparkline chart component
function SparklineChart({
  data,
  trend,
}: {
  data: number[]
  trend: 'up' | 'down' | 'neutral'
}) {
  const max = Math.max(...data)
  const min = Math.min(...data)
  const range = max - min || 1

  const points = data
    .map((value, index) => {
      const x = (index / (data.length - 1)) * 100
      const y = 100 - ((value - min) / range) * 100
      return `${x},${y}`
    })
    .join(' ')

  const trendColor = trend === 'up' ? 'text-emerald-500' : trend === 'down' ? 'text-red-500' : 'text-slate-500'

  return (
    <div className="h-10 w-full">
      <svg
        viewBox="0 0 100 100"
        preserveAspectRatio="none"
        className={cn('h-full w-full', trendColor)}
      >
        <polyline
          fill="none"
          stroke="currentColor"
          strokeWidth="3"
          strokeLinecap="round"
          strokeLinejoin="round"
          points={points}
        />
      </svg>
    </div>
  )
}

function KPICard({ kpi }: { kpi: typeof strategicKPIs[0] }) {
  const progress = (kpi.value / kpi.target) * 100

  return (
    <Card>
      <CardHeader className="pb-3">
        <div className="flex items-start justify-between">
          <CardTitle className="text-sm font-medium">{kpi.name}</CardTitle>
          <StatusBadge status={kpi.status} />
        </div>
      </CardHeader>
      <CardContent className="space-y-3">
        <div className="flex items-baseline gap-2">
          <span className="text-2xl font-bold">
            {kpi.unit === '%' ? `${kpi.value}${kpi.unit}` : kpi.value}
          </span>
          <span className="text-sm text-muted-foreground">/ {kpi.target}{kpi.unit}</span>
        </div>

        {/* Trend Chart */}
        {kpi.history && (
          <div className="space-y-1">
            <div className="flex items-center gap-1 text-xs text-muted-foreground">
              {kpi.trend === 'up' ? (
                <TrendingUp className="h-3 w-3 text-emerald-500" />
              ) : (
                <TrendingDown className="h-3 w-3 text-red-500" />
              )}
              <span>6-Month Trend</span>
            </div>
            <SparklineChart data={kpi.history} trend={kpi.trend} />
          </div>
        )}

        <div className="space-y-1">
          <div className="flex justify-between text-xs text-muted-foreground">
            <span>Progress</span>
            <span>{Math.round(progress)}%</span>
          </div>
          <Progress value={progress} className="h-2" />
        </div>
        <div className="flex items-center justify-between text-xs text-muted-foreground">
          <span>Owner: {kpi.owner}</span>
          <span>Updated: {kpi.lastUpdated}</span>
        </div>
      </CardContent>
    </Card>
  )
}

export default function StrategyPage() {
  return (
    <div className="space-y-6 animate-in fade-in-0 duration-500">
      {/* Page Header */}
      <div className="page-header">
        <h1 className="page-title">Strategic Management</h1>
        <p className="page-description">
          Track strategic KPIs, goals, OKRs, and initiatives with AI-powered insights
        </p>
      </div>

      {/* Tabs */}
      <Tabs defaultValue="kpis" className="space-y-6">
        <TabsList className="grid w-full grid-cols-3 lg:w-auto lg:inline-grid">
          <TabsTrigger value="kpis">
            <Target className="mr-2 h-4 w-4" />
            Strategic KPIs
          </TabsTrigger>
          <TabsTrigger value="okrs">
            <Flag className="mr-2 h-4 w-4" />
            Goals & OKRs
          </TabsTrigger>
          <TabsTrigger value="initiatives">
            <TrendingUp className="mr-2 h-4 w-4" />
            Initiatives
          </TabsTrigger>
        </TabsList>

        {/* Strategic KPIs Tab */}
        <TabsContent value="kpis" className="space-y-6">
          <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
            {strategicKPIs.map((kpi) => (
              <KPICard key={kpi.id} kpi={kpi} />
            ))}
          </div>
        </TabsContent>

        {/* Goals & OKRs Tab */}
        <TabsContent value="okrs" className="space-y-6">
          {goalsAndOKRs.map((okr) => (
            <Card key={okr.id}>
              <CardHeader>
                <div className="flex items-start justify-between">
                  <div className="space-y-1">
                    <CardTitle>{okr.objective}</CardTitle>
                    <CardDescription>
                      Owner: {okr.owner} • Period: {okr.period}
                    </CardDescription>
                  </div>
                  <StatusBadge status={okr.status} />
                </div>
                <div className="space-y-1">
                  <div className="flex justify-between text-sm">
                    <span>Overall Progress</span>
                    <span className="font-medium">{okr.progress}%</span>
                  </div>
                  <Progress value={okr.progress} className="h-2" />
                </div>
              </CardHeader>
              <CardContent>
                <h4 className="mb-3 text-sm font-medium">Key Results</h4>
                <div className="space-y-3">
                  {okr.keyResults.map((kr, idx) => {
                    const krProgress = (kr.current / kr.target) * 100
                    return (
                      <div key={idx} className="space-y-1">
                        <div className="flex justify-between text-sm">
                          <span>{kr.title}</span>
                          <span className="text-muted-foreground">
                            {kr.current} / {kr.target} {kr.unit}
                          </span>
                        </div>
                        <div className="h-1.5 w-full overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
                          <div
                            className={cn(
                              'h-full',
                              krProgress >= 100 ? 'bg-green-500' : krProgress >= 75 ? 'bg-blue-500' : 'bg-yellow-500'
                            )}
                            style={{ width: `${Math.min(krProgress, 100)}%` }}
                          />
                        </div>
                      </div>
                    )
                  })}
                </div>
              </CardContent>
            </Card>
          ))}
        </TabsContent>

        {/* Strategic Initiatives Tab */}
        <TabsContent value="initiatives" className="space-y-6">
          {strategicInitiatives.map((initiative) => {
            const budgetProgress = (initiative.spent / initiative.budget) * 100
            return (
              <Card key={initiative.id}>
                <CardHeader>
                  <div className="flex items-start justify-between">
                    <div className="space-y-1">
                      <CardTitle>{initiative.name}</CardTitle>
                      <CardDescription className="text-base">
                        {initiative.description}
                      </CardDescription>
                      <div className="flex flex-wrap gap-4 text-sm text-muted-foreground">
                        <span>Owner: {initiative.owner}</span>
                        <span>•</span>
                        <span>
                          Start: {initiative.startDate} → Target: {initiative.targetDate}
                        </span>
                      </div>
                    </div>
                    <div className="flex flex-col items-end gap-2">
                      <Badge
                        variant={
                          initiative.priority === 'high'
                            ? 'destructive'
                            : initiative.priority === 'urgent'
                            ? 'destructive'
                            : 'default'
                        }
                      >
                        {initiative.priority}
                      </Badge>
                      <StatusBadge status={initiative.status} />
                    </div>
                  </div>
                </CardHeader>
                <CardContent className="space-y-6">
                  {/* Budget */}
                  <div className="space-y-2">
                    <div className="flex justify-between text-sm">
                      <span className="font-medium">Budget Utilization</span>
                      <span>
                        {formatCurrency(initiative.spent)} / {formatCurrency(initiative.budget)}
                      </span>
                    </div>
                    <Progress value={budgetProgress} className="h-2" />
                  </div>

                  {/* Milestones */}
                  <div>
                    <h4 className="mb-3 text-sm font-medium">Milestones</h4>
                    <div className="grid gap-2 md:grid-cols-2 lg:grid-cols-4">
                      {initiative.milestones.map((milestone, idx) => (
                        <div
                          key={idx}
                          className="flex items-center gap-2 rounded-lg border p-3"
                        >
                          <StatusIcon status={milestone.status} />
                          <span className="text-sm">{milestone.name}</span>
                        </div>
                      ))}
                    </div>
                  </div>

                  {/* Actions */}
                  <div className="flex gap-2">
                    <Button variant="outline" size="sm">
                      View Details
                    </Button>
                    <Button size="sm" className="executive-gold-bg text-white">
                      Update Progress
                    </Button>
                  </div>
                </CardContent>
              </Card>
            )
          })}
        </TabsContent>
      </Tabs>
    </div>
  )
}

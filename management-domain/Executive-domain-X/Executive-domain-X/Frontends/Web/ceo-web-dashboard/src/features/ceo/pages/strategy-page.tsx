import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@shared/components/ui/tabs'
import { KPICard } from '@shared/components/data/kpi-card'
import { ProgressBar } from '@shared/components/data/progress-bar'
import { cn, formatCurrency, formatPercentage } from '@shared/utils/cn'
import { Plus, Edit2, Trash2, Target, Award, CheckCircle2, Clock, AlertCircle } from 'lucide-react'

/**
 * CEO Strategy Page
 *
 * Components:
 * - StrategicKPIManagement - Create, edit, delete strategic KPIs
 * - OKRTracking - Objectives and Key Results visualization
 * - InitiativesTracker - Strategic initiatives with progress tracking
 */

// KPI Management
interface StrategicKPI {
  id: string
  name: string
  category: 'Revenue' | 'Growth' | 'Customer' | 'Employee' | 'Innovation'
  currentValue: number
  targetValue: number
  unit: string
  owner: string
  status: 'On Track' | 'At Risk' | 'Behind' | 'Ahead'
  lastUpdated: string
}

const mockKPIs: StrategicKPI[] = [
  {
    id: '1',
    name: 'Annual Recurring Revenue',
    category: 'Revenue',
    currentValue: 42500000,
    targetValue: 50000000,
    unit: 'USD',
    owner: 'CFO',
    status: 'On Track',
    lastUpdated: '2024-03-15',
  },
  {
    id: '2',
    name: 'Customer Acquisition Cost',
    category: 'Growth',
    currentValue: 250,
    targetValue: 200,
    unit: '$',
    owner: 'CMO',
    status: 'At Risk',
    lastUpdated: '2024-03-14',
  },
  {
    id: '3',
    name: 'Employee Net Promoter Score',
    category: 'Employee',
    currentValue: 42,
    targetValue: 50,
    unit: '',
    owner: 'CHRO',
    status: 'Behind',
    lastUpdated: '2024-03-13',
  },
]

// OKR Tracking
interface Objective {
  id: string
  title: string
  quarter: string
  progress: number
  status: 'On Track' | 'At Risk' | 'Behind'
  keyResults: KeyResult[]
}

interface KeyResult {
  id: string
  description: string
  currentValue: number
  targetValue: number
  unit: string
  progress: number
  owner: string
}

const mockObjectives: Objective[] = [
  {
    id: '1',
    title: 'Expand European Market Presence',
    quarter: 'Q2 2026',
    progress: 65,
    status: 'On Track',
    keyResults: [
      { id: '1-1', description: 'Launch in Germany', currentValue: 1, targetValue: 1, unit: '', progress: 100, owner: 'Regional Director EU' },
      { id: '1-2', description: 'Launch in France', currentValue: 0, targetValue: 1, unit: '', progress: 0, owner: 'Regional Director EU' },
      { id: '1-3', description: 'Achieve €5M revenue', currentValue: 3200000, targetValue: 5000000, unit: 'EUR', progress: 64, owner: 'VP Sales EU' },
    ],
  },
  {
    id: '2',
    title: 'Improve Product Innovation Velocity',
    quarter: 'Q2 2026',
    progress: 40,
    status: 'At Risk',
    keyResults: [
      { id: '2-1', description: 'Release 3 major features', currentValue: 1, targetValue: 3, unit: 'features', progress: 33, owner: 'CTO' },
      { id: '2-2', description: 'Reduce time-to-market by 30%', currentValue: 15, targetValue: 30, unit: '%', progress: 50, owner: 'VP Engineering' },
    ],
  },
]

// Initiatives Tracker
interface Initiative {
  id: string
  name: string
  description: string
  priority: 'P1' | 'P2' | 'P3'
  status: 'Planned' | 'In Progress' | 'Complete' | 'Delayed'
  progress: number
  startDate: string
  endDate: string
  owner: string
  budget: number
  budgetUsed: number
  milestones: Milestone[]
  riskLevel: 'Low' | 'Medium' | 'High'
}

interface Milestone {
  id: string
  name: string
  targetDate: string
  status: 'Pending' | 'Complete' | 'Overdue'
}

const mockInitiatives: Initiative[] = [
  {
    id: '1',
    name: 'AI-Powered Analytics Platform',
    description: 'Build comprehensive AI analytics for all domains',
    priority: 'P1',
    status: 'In Progress',
    progress: 45,
    startDate: '2024-01-01',
    endDate: '2024-06-30',
    owner: 'CTO',
    budget: 5000000,
    budgetUsed: 2250000,
    riskLevel: 'Medium',
    milestones: [
      { id: '1-1', name: 'Data Pipeline Complete', targetDate: '2024-02-28', status: 'Complete' },
      { id: '1-2', name: 'ML Models Deployed', targetDate: '2024-04-30', status: 'Pending' },
      { id: '1-3', name: 'User Acceptance Testing', targetDate: '2024-06-15', status: 'Pending' },
    ],
  },
  {
    id: '2',
    name: 'European Expansion',
    description: 'Expand operations to 5 new European markets',
    priority: 'P1',
    status: 'In Progress',
    progress: 60,
    startDate: '2024-01-15',
    endDate: '2024-12-31',
    owner: 'COO',
    budget: 15000000,
    budgetUsed: 9000000,
    riskLevel: 'High',
    milestones: [
      { id: '2-1', name: 'Germany Launch', targetDate: '2024-03-31', status: 'Complete' },
      { id: '2-2', name: 'France Launch', targetDate: '2024-06-30', status: 'Pending' },
      { id: '2-3', name: 'UK Expansion', targetDate: '2024-09-30', status: 'Pending' },
    ],
  },
]

export default function StrategyPage() {
  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header">
        <h1 className="page-title">Strategic Management</h1>
        <p className="page-description">
          Track and manage strategic objectives, KPIs, and initiatives
        </p>
      </div>

      <Tabs defaultValue="kpis" className="space-y-6">
        <TabsList className="grid w-full max-w-md grid-cols-3">
          <TabsTrigger value="kpis">Strategic KPIs</TabsTrigger>
          <TabsTrigger value="okr">OKRs</TabsTrigger>
          <TabsTrigger value="initiatives">Initiatives</TabsTrigger>
        </TabsList>

        {/* KPIs Tab */}
        <TabsContent value="kpis" className="space-y-6">
          <div className="flex items-center justify-between">
            <div>
              <h2 className="text-xl font-semibold">Strategic KPIs</h2>
              <p className="text-sm text-muted-foreground">
                Key performance indicators across all business categories
              </p>
            </div>
            <Button className="gap-2">
              <Plus className="h-4 w-4" />
              Add KPI
            </Button>
          </div>

          <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
            {mockKPIs.map((kpi) => (
              <Card key={kpi.id}>
                <CardHeader className="pb-3">
                  <div className="flex items-start justify-between">
                    <div>
                      <CardTitle className="text-base">{kpi.name}</CardTitle>
                      <CardDescription className="text-xs">{kpi.category}</CardDescription>
                    </div>
                    <Badge
                      variant={
                        kpi.status === 'On Track' || kpi.status === 'Ahead'
                          ? 'success'
                          : 'warning'
                      }
                    >
                      {kpi.status}
                    </Badge>
                  </div>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div>
                    <div className="flex items-baseline justify-between mb-2">
                      <span className="text-2xl font-bold">
                        {kpi.unit === 'USD'
                          ? formatCurrency(kpi.currentValue)
                          : `${kpi.currentValue}${kpi.unit}`}
                      </span>
                      <span className="text-sm text-muted-foreground">
                        of {kpi.unit === 'USD' ? formatCurrency(kpi.targetValue) : `${kpi.targetValue}${kpi.unit}`}
                      </span>
                    </div>
                    <ProgressBar
                      value={(kpi.currentValue / kpi.targetValue) * 100}
                      max={100}
                      size="sm"
                    />
                  </div>

                  <div className="flex items-center justify-between text-xs text-muted-foreground">
                    <span>Owner: {kpi.owner}</span>
                    <span>Updated: {new Date(kpi.lastUpdated).toLocaleDateString()}</span>
                  </div>

                  <div className="flex gap-2">
                    <Button variant="outline" size="sm" className="flex-1">
                      <Edit2 className="h-3 w-3 mr-1" />
                      Edit
                    </Button>
                    <Button variant="outline" size="sm" className="flex-1">
                      <Target className="h-3 w-3 mr-1" />
                      Details
                    </Button>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </TabsContent>

        {/* OKRs Tab */}
        <TabsContent value="okr" className="space-y-6">
          <div className="flex items-center justify-between">
            <div>
              <h2 className="text-xl font-semibold">Objectives & Key Results</h2>
              <p className="text-sm text-muted-foreground">
                Quarterly objectives with measurable key results
              </p>
            </div>
            <div className="flex gap-2">
              <Button variant="outline">Previous Quarter</Button>
              <Button>Next Quarter</Button>
              <Button className="gap-2">
                <Plus className="h-4 w-4" />
                New Objective
              </Button>
            </div>
          </div>

          <div className="grid gap-6">
            {mockObjectives.map((objective) => (
              <Card key={objective.id}>
                <CardHeader>
                  <div className="flex items-start justify-between">
                    <div className="space-y-1">
                      <div className="flex items-center gap-2">
                        <Award className="h-5 w-5 text-purple-500" />
                        <CardTitle>{objective.title}</CardTitle>
                        <Badge variant="outline">{objective.quarter}</Badge>
                      </div>
                      <CardDescription>
                        <div className="flex items-center gap-4 mt-2">
                          <span>Overall Progress</span>
                          <ProgressBar
                            value={objective.progress}
                            max={100}
                            size="sm"
                            showPercentage
                          />
                          <Badge
                            variant={objective.status === 'On Track' ? 'success' : 'warning'}
                          >
                            {objective.status}
                          </Badge>
                        </div>
                      </CardDescription>
                    </div>
                  </div>
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">
                    {objective.keyResults.map((kr) => (
                      <div
                        key={kr.id}
                        className="flex items-center justify-between p-3 rounded-lg border"
                      >
                        <div className="flex-1">
                          <div className="flex items-center gap-2 mb-1">
                            <Target className="h-4 w-4 text-muted-foreground" />
                            <span className="font-medium">{kr.description}</span>
                            <Badge variant="outline" className="text-xs">
                              {kr.owner}
                            </Badge>
                          </div>
                          <div className="flex items-center gap-4 mt-2">
                            <ProgressBar value={kr.progress} max={100} size="sm" />
                            <span className="text-sm text-muted-foreground min-w-[100px] text-right">
                              {kr.currentValue} / {kr.targetValue} {kr.unit}
                            </span>
                          </div>
                        </div>
                        {kr.progress === 100 ? (
                          <CheckCircle2 className="h-5 w-5 text-green-500 ml-4" />
                        ) : kr.progress < 25 ? (
                          <AlertCircle className="h-5 w-5 text-amber-500 ml-4" />
                        ) : (
                          <Clock className="h-5 w-5 text-blue-500 ml-4" />
                        )}
                      </div>
                    ))}
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </TabsContent>

        {/* Initiatives Tab */}
        <TabsContent value="initiatives" className="space-y-6">
          <div className="flex items-center justify-between">
            <div>
              <h2 className="text-xl font-semibold">Strategic Initiatives</h2>
              <p className="text-sm text-muted-foreground">
                Track progress on major strategic initiatives
              </p>
            </div>
            <Button className="gap-2">
              <Plus className="h-4 w-4" />
              New Initiative
            </Button>
          </div>

          <div className="grid gap-6">
            {mockInitiatives.map((initiative) => (
              <Card key={initiative.id}>
                <CardHeader>
                  <div className="flex items-start justify-between">
                    <div className="space-y-1">
                      <div className="flex items-center gap-2">
                        <CardTitle>{initiative.name}</CardTitle>
                        <Badge
                          variant={
                            initiative.priority === 'P1'
                              ? 'destructive'
                              : initiative.priority === 'P2'
                              ? 'default'
                              : 'secondary'
                          }
                        >
                          {initiative.priority}
                        </Badge>
                        <Badge
                          variant={
                            initiative.status === 'In Progress'
                              ? 'default'
                              : initiative.status === 'Complete'
                              ? 'success'
                              : initiative.status === 'Delayed'
                              ? 'destructive'
                              : 'secondary'
                          }
                        >
                          {initiative.status}
                        </Badge>
                      </div>
                      <CardDescription>{initiative.description}</CardDescription>
                    </div>
                  </div>
                </CardHeader>
                <CardContent className="space-y-4">
                  {/* Progress */}
                  <div>
                    <div className="flex items-center justify-between mb-2">
                      <span className="text-sm font-medium">Progress</span>
                      <span className="text-sm text-muted-foreground">{initiative.progress}%</span>
                    </div>
                    <ProgressBar value={initiative.progress} max={100} />
                  </div>

                  {/* Budget */}
                  <div className="grid grid-cols-2 gap-4 text-sm">
                    <div>
                      <span className="text-muted-foreground">Budget:</span>{' '}
                      {formatCurrency(initiative.budget)}
                    </div>
                    <div>
                      <span className="text-muted-foreground">Used:</span>{' '}
                      {formatCurrency(initiative.budgetUsed)} ({formatPercentage((initiative.budgetUsed / initiative.budget) * 100)})
                    </div>
                    <div>
                      <span className="text-muted-foreground">Owner:</span> {initiative.owner}
                    </div>
                    <div>
                      <span className="text-muted-foreground">Risk:</span>{' '}
                      <Badge
                        variant={initiative.riskLevel === 'High' ? 'destructive' : initiative.riskLevel === 'Medium' ? 'default' : 'secondary'}
                        className="text-xs"
                      >
                        {initiative.riskLevel}
                      </Badge>
                    </div>
                  </div>

                  {/* Milestones */}
                  <div>
                    <h4 className="text-sm font-medium mb-2">Milestones</h4>
                    <div className="space-y-2">
                      {initiative.milestones.map((milestone) => (
                        <div
                          key={milestone.id}
                          className="flex items-center justify-between p-2 rounded bg-slate-50 dark:bg-slate-800"
                        >
                          <div className="flex items-center gap-2">
                            {milestone.status === 'Complete' ? (
                              <CheckCircle2 className="h-4 w-4 text-green-500" />
                            ) : milestone.status === 'Overdue' ? (
                              <AlertCircle className="h-4 w-4 text-red-500" />
                            ) : (
                              <Clock className="h-4 w-4 text-muted-foreground" />
                            )}
                            <span className="text-sm">{milestone.name}</span>
                          </div>
                          <span className="text-xs text-muted-foreground">
                            {new Date(milestone.targetDate).toLocaleDateString()}
                          </span>
                        </div>
                      ))}
                    </div>
                  </div>

                  <div className="flex gap-2">
                    <Button variant="outline" size="sm" className="flex-1">
                      <Edit2 className="h-3 w-3 mr-1" />
                      Edit
                    </Button>
                    <Button variant="outline" size="sm" className="flex-1">
                      <Target className="h-3 w-3 mr-1" />
                      Details
                    </Button>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </TabsContent>
      </Tabs>
    </div>
  )
}

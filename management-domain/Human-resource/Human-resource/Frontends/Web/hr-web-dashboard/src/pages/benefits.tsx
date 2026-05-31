import { useState } from 'react'
import {
  Heart,
  Shield,
  Eye,
  Download,
  Users,
  DollarSign,
  CheckCircle,
  Plus,
} from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Button } from '@shared/components/ui/button'
import { Badge } from '@shared/components/ui/badge'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@shared/components/ui/table'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@shared/components/ui/tabs'
import { cn, formatCurrency } from '@shared/utils/cn'

const benefitPlans = [
  {
    id: 'bp-001',
    type: 'HEALTH',
    name: 'Comprehensive Health Plan',
    provider: 'BlueCross BlueShield',
    premium: 450,
    coverage: 'EMPLOYEE_SPOUSE',
    enrolled: 245,
    status: 'active',
    description: 'Full medical coverage including hospitalization, outpatient, and preventive care',
  },
  {
    id: 'bp-002',
    type: 'DENTAL',
    name: 'Dental Care Plus',
    provider: 'Delta Dental',
    premium: 85,
    coverage: 'EMPLOYEE_ONLY',
    enrolled: 182,
    status: 'active',
    description: 'Comprehensive dental coverage including preventive, basic, and major services',
  },
  {
    id: 'bp-003',
    type: 'VISION',
    name: 'Vision Essentials',
    provider: 'VSP',
    premium: 35,
    coverage: 'EMPLOYEE_ONLY',
    enrolled: 156,
    status: 'active',
    description: 'Eye exams, glasses, and contact lens coverage',
  },
  {
    id: 'bp-004',
    type: 'LIFE',
    name: 'Group Life Insurance',
    provider: 'MetLife',
    premium: 120,
    coverage: 'FAMILY',
    enrolled: 312,
    status: 'active',
    description: 'Life insurance coverage at 3x annual salary',
  },
  {
    id: 'bp-005',
    type: 'RETIREMENT',
    name: '401(k) Retirement Plan',
    provider: 'Fidelity',
    premium: 0,
    coverage: 'EMPLOYEE_ONLY',
    enrolled: 420,
    status: 'active',
    description: 'Employer matched retirement savings plan up to 6%',
  },
]

const enrollments = [
  { id: 'be-001', employeeName: 'John Smith', benefitType: 'HEALTH', planName: 'Comprehensive Health Plan', coverage: 'EMPLOYEE_SPOUSE', premium: 450, startDate: '2024-01-01', dependents: 1 },
  { id: 'be-002', employeeName: 'Sarah Johnson', benefitType: 'HEALTH', planName: 'Comprehensive Health Plan', coverage: 'FAMILY', premium: 680, startDate: '2024-01-01', dependents: 3 },
  { id: 'be-003', employeeName: 'Michael Chen', benefitType: 'DENTAL', planName: 'Dental Care Plus', coverage: 'EMPLOYEE_ONLY', premium: 85, startDate: '2024-01-01', dependents: 0 },
  { id: 'be-004', employeeName: 'Emily Williams', benefitType: 'RETIREMENT', planName: '401(k) Retirement Plan', coverage: 'EMPLOYEE_ONLY', premium: 0, startDate: '2023-06-01', dependents: 0 },
  { id: 'be-005', employeeName: 'David Okafor', benefitType: 'LIFE', planName: 'Group Life Insurance', coverage: 'EMPLOYEE_SPOUSE', premium: 120, startDate: '2024-01-01', dependents: 1 },
]

const benefitTypeConfig: Record<string, { label: string; color: string; icon: string }> = {
  HEALTH: { label: 'Health', color: 'bg-blue-100 text-blue-700', icon: '🏥' },
  DENTAL: { label: 'Dental', color: 'bg-purple-100 text-purple-700', icon: '🦷' },
  VISION: { label: 'Vision', color: 'bg-green-100 text-green-700', icon: '👁️' },
  LIFE: { label: 'Life Insurance', color: 'bg-orange-100 text-orange-700', icon: '🛡️' },
  RETIREMENT: { label: 'Retirement', color: 'bg-teal-100 text-teal-700', icon: '💰' },
  OTHER: { label: 'Other', color: 'bg-slate-100 text-slate-700', icon: '📋' },
}

export default function BenefitsPage() {
  const totalEnrolled = enrollments.length
  const totalPremium = enrollments.reduce((sum, e) => sum + e.premium, 0)
  const activePlans = benefitPlans.filter(p => p.status === 'active').length

  return (
    <div className="space-y-6">
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        <div className="page-header">
          <h1 className="page-title">Benefits Administration</h1>
          <p className="page-description">Manage employee benefits, plans, and enrollments</p>
        </div>
        <div className="flex items-center gap-2">
          <Button variant="outline" size="sm">
            <Download className="mr-2 h-4 w-4" />
            Export Report
          </Button>
          <Button variant="hr" size="sm">
            <Plus className="mr-2 h-4 w-4" />
            New Plan
          </Button>
        </div>
      </div>

      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-blue-100 p-2 dark:bg-blue-900/20">
                <Heart className="h-4 w-4 text-blue-600 dark:text-blue-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{activePlans}</p>
                <p className="text-xs text-muted-foreground">Active Plans</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-green-100 p-2 dark:bg-green-900/20">
                <Users className="h-4 w-4 text-green-600 dark:text-green-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{totalEnrolled}</p>
                <p className="text-xs text-muted-foreground">Enrolled Employees</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-purple-100 p-2 dark:bg-purple-900/20">
                <DollarSign className="h-4 w-4 text-purple-600 dark:text-purple-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{formatCurrency(totalPremium, 'USD')}</p>
                <p className="text-xs text-muted-foreground">Monthly Premium</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-teal-100 p-2 dark:bg-teal-900/20">
                <CheckCircle className="h-4 w-4 text-teal-600 dark:text-teal-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">87%</p>
                <p className="text-xs text-muted-foreground">Participation Rate</p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      <Tabs defaultValue="plans" className="space-y-4">
        <TabsList>
          <TabsTrigger value="plans">Benefit Plans</TabsTrigger>
          <TabsTrigger value="enrollments">Enrollments</TabsTrigger>
          <TabsTrigger value="costs">Cost Analysis</TabsTrigger>
        </TabsList>

        <TabsContent value="plans" className="space-y-4">
          <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
            {benefitPlans.map((plan) => {
              const config = benefitTypeConfig[plan.type] || benefitTypeConfig.OTHER
              return (
                <Card key={plan.id}>
                  <CardHeader>
                    <div className="flex items-center justify-between">
                      <div className="flex items-center gap-2">
                        <span className="text-xl">{config.icon}</span>
                        <Badge className={cn('border-0', config.color)}>{config.label}</Badge>
                      </div>
                      <Badge variant="success">Active</Badge>
                    </div>
                    <CardTitle className="text-base mt-2">{plan.name}</CardTitle>
                    <CardDescription>{plan.provider}</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <p className="text-sm text-muted-foreground mb-4">{plan.description}</p>
                    <div className="grid grid-cols-2 gap-3 text-sm">
                      <div>
                        <span className="text-muted-foreground">Premium</span>
                        <p className="font-medium">{plan.premium > 0 ? formatCurrency(plan.premium, 'USD') + '/mo' : 'Variable'}</p>
                      </div>
                      <div>
                        <span className="text-muted-foreground">Enrolled</span>
                        <p className="font-medium">{plan.enrolled} employees</p>
                      </div>
                      <div>
                        <span className="text-muted-foreground">Coverage</span>
                        <p className="font-medium">{plan.coverage.replace('_', ' ')}</p>
                      </div>
                    </div>
                    <div className="flex gap-2 mt-4">
                      <Button size="sm" variant="outline" className="flex-1">
                        <Eye className="mr-2 h-3 w-3" />
                        Details
                      </Button>
                      <Button size="sm" variant="hr" className="flex-1">
                        <Shield className="mr-2 h-3 w-3" />
                        Manage
                      </Button>
                    </div>
                  </CardContent>
                </Card>
              )
            })}
          </div>
        </TabsContent>

        <TabsContent value="enrollments" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Benefit Enrollments</CardTitle>
              <CardDescription>Employee benefit enrollment details</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Employee</TableHead>
                    <TableHead>Benefit Type</TableHead>
                    <TableHead>Plan</TableHead>
                    <TableHead>Coverage</TableHead>
                    <TableHead className="text-right">Premium</TableHead>
                    <TableHead>Dependents</TableHead>
                    <TableHead>Start Date</TableHead>
                    <TableHead className="text-right">Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {enrollments.map((enrollment) => {
                    const config = benefitTypeConfig[enrollment.benefitType] || benefitTypeConfig.OTHER
                    return (
                      <TableRow key={enrollment.id}>
                        <TableCell className="font-medium">{enrollment.employeeName}</TableCell>
                        <TableCell>
                          <Badge className={cn('border-0', config.color)}>{config.icon} {config.label}</Badge>
                        </TableCell>
                        <TableCell>{enrollment.planName}</TableCell>
                        <TableCell>{enrollment.coverage.replace('_', ' ')}</TableCell>
                        <TableCell className="text-right font-mono">
                          {enrollment.premium > 0 ? formatCurrency(enrollment.premium, 'USD') + '/mo' : '-'}
                        </TableCell>
                        <TableCell>{enrollment.dependents}</TableCell>
                        <TableCell>{enrollment.startDate}</TableCell>
                        <TableCell className="text-right">
                          <Button size="sm" variant="outline">View</Button>
                        </TableCell>
                      </TableRow>
                    )
                  })}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="costs" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Benefits Cost Analysis</CardTitle>
              <CardDescription>Monthly cost breakdown by benefit type</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {[
                  { type: 'HEALTH', label: 'Health Insurance', employees: 245, avgPremium: 565, totalCost: 138425 },
                  { type: 'DENTAL', label: 'Dental Plan', employees: 182, avgPremium: 85, totalCost: 15470 },
                  { type: 'VISION', label: 'Vision Plan', employees: 156, avgPremium: 35, totalCost: 5460 },
                  { type: 'LIFE', label: 'Life Insurance', employees: 312, avgPremium: 120, totalCost: 37440 },
                  { type: 'RETIREMENT', label: '401(k) Match', employees: 420, avgPremium: 0, totalCost: 89500 },
                ].map((item) => {
                  const config = benefitTypeConfig[item.type] || benefitTypeConfig.OTHER
                  return (
                    <div key={item.type} className="rounded-lg border p-4">
                      <div className="flex items-center justify-between">
                        <div className="flex items-center gap-3">
                          <span className="text-2xl">{config.icon}</span>
                          <div>
                            <p className="font-semibold">{item.label}</p>
                            <p className="text-sm text-muted-foreground">{item.employees} employees enrolled</p>
                          </div>
                        </div>
                        <div className="text-right">
                          <p className="font-semibold">{formatCurrency(item.totalCost, 'USD')}/mo</p>
                          {item.avgPremium > 0 && (
                            <p className="text-sm text-muted-foreground">
                              Avg: {formatCurrency(item.avgPremium, 'USD')}/employee
                            </p>
                          )}
                        </div>
                      </div>
                    </div>
                  )
                })}
              </div>
              <div className="mt-4 rounded-lg bg-slate-100 dark:bg-slate-800 p-4">
                <div className="flex items-center justify-between">
                  <span className="font-semibold">Total Monthly Cost</span>
                  <span className="text-xl font-bold">{formatCurrency(286295, 'USD')}</span>
                </div>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}

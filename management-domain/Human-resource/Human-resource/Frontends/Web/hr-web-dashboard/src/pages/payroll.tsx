import { useState } from 'react'
import {
  DollarSign,
  Download,
  FileText,
  TrendingUp,
  TrendingDown,
  Calendar,
  Filter,
  CheckCircle,
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
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@shared/components/ui/select'
import { formatCurrency, cn, formatNumber } from '@shared/utils/cn'
import { payrollEntries } from '@shared/data/mockData'

const periodOptions = [
  { value: '2024-03', label: 'March 2024' },
  { value: '2024-02', label: 'February 2024' },
  { value: '2024-01', label: 'January 2024' },
]

export default function PayrollPage() {
  const [selectedPeriod, setSelectedPeriod] = useState('2024-03')
  const [selectedCountry, setSelectedCountry] = useState('ALL')

  const totalGrossPay = payrollEntries.reduce((sum, entry) => sum + entry.grossSalary, 0)
  const totalNetPay = payrollEntries.reduce((sum, entry) => sum + entry.netPay, 0)
  const totalTaxes = payrollEntries.reduce((sum, entry) => sum + entry.tax, 0)

  const stats = [
    {
      label: 'Total Gross Pay',
      value: formatCurrency(totalGrossPay, 'USD'),
      change: '+2.5%',
      trend: 'up',
      icon: DollarSign,
      color: 'text-green-600',
      bgClass: 'bg-green-100',
    },
    {
      label: 'Total Net Pay',
      value: formatCurrency(totalNetPay, 'USD'),
      change: '+2.3%',
      trend: 'up',
      icon: TrendingUp,
      color: 'text-blue-600',
      bgClass: 'bg-blue-100',
    },
    {
      label: 'Total Taxes',
      value: formatCurrency(totalTaxes, 'USD'),
      change: '-0.5%',
      trend: 'down',
      icon: TrendingDown,
      color: 'text-red-600',
      bgClass: 'bg-red-100',
    },
    {
      label: 'Payroll Run',
      value: 'Mar 28',
      change: 'In 5 days',
      trend: 'neutral',
      icon: Calendar,
      color: 'text-purple-600',
      bgClass: 'bg-purple-100',
    },
  ]

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        <div className="page-header">
          <h1 className="page-title">Payroll Management</h1>
          <p className="page-description">
            Manage compensation, salaries, and payroll processing
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Button variant="outline" size="sm">
            <Download className="mr-2 h-4 w-4" />
            Export Report
          </Button>
          <Button variant="hr" size="sm">
            <CheckCircle className="mr-2 h-4 w-4" />
            Run Payroll
          </Button>
        </div>
      </div>

      {/* Stats */}
      <div className="grid gap-4 md:grid-cols-4">
        {stats.map((stat) => (
          <Card key={stat.label}>
            <CardContent className="p-4">
              <div className="flex items-center gap-3">
                <div className={cn('rounded-lg p-2', stat.bgClass)}>
                  <stat.icon className={cn('h-4 w-4', stat.color)} />
                </div>
                <div>
                  <p className="text-2xl font-bold">{stat.value}</p>
                  <div className="flex items-center gap-1">
                    <p className="text-xs text-muted-foreground">{stat.label}</p>
                    {stat.change && (
                      <>
                        <span className="text-xs text-muted-foreground">•</span>
                        <span
                          className={cn(
                            'text-xs',
                            stat.trend === 'up' ? 'text-green-600' : stat.trend === 'down' ? 'text-red-600' : 'text-muted-foreground'
                          )}
                        >
                          {stat.change}
                        </span>
                      </>
                    )}
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Filters */}
      <Card>
        <CardContent className="p-4">
          <div className="flex flex-wrap items-center gap-4">
            <div className="flex items-center gap-2">
              <span className="text-sm font-medium">Pay Period:</span>
              <Select value={selectedPeriod} onValueChange={setSelectedPeriod}>
                <SelectTrigger className="w-[180px]">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  {periodOptions.map((option) => (
                    <SelectItem key={option.value} value={option.value}>
                      {option.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
            <div className="flex items-center gap-2">
              <span className="text-sm font-medium">Country:</span>
              <Select value={selectedCountry} onValueChange={setSelectedCountry}>
                <SelectTrigger className="w-[180px]">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ALL">All Countries</SelectItem>
                  <SelectItem value="NG">🇳🇬 Nigeria</SelectItem>
                  <SelectItem value="KE">🇰🇪 Kenya</SelectItem>
                  <SelectItem value="US">🇺🇸 United States</SelectItem>
                  <SelectItem value="GB">🇬🇧 United Kingdom</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <Button variant="outline" size="sm">
              <Filter className="mr-2 h-4 w-4" />
              More Filters
            </Button>
          </div>
        </CardContent>
      </Card>

      <Tabs defaultValue="payroll" className="space-y-4">
        <TabsList>
          <TabsTrigger value="payroll">Payroll Run</TabsTrigger>
          <TabsTrigger value="payslips">Payslips</TabsTrigger>
          <TabsTrigger value="tax">Tax & Deductions</TabsTrigger>
          <TabsTrigger value="benefits">Benefits</TabsTrigger>
          <TabsTrigger value="reports">Reports</TabsTrigger>
        </TabsList>

        <TabsContent value="payroll" className="space-y-4">
          <Card>
            <CardHeader>
              <div className="flex items-center justify-between">
                <div>
                  <CardTitle>Payroll Entries</CardTitle>
                  <CardDescription>
                    {payrollEntries.length} employees for {periodOptions.find(p => p.value === selectedPeriod)?.label}
                  </CardDescription>
                </div>
                <Badge variant="outline" className="bg-yellow-100 text-yellow-800 border-yellow-200">
                  Draft
                </Badge>
              </div>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Employee</TableHead>
                    <TableHead>Department</TableHead>
                    <TableHead className="text-right">Basic Salary</TableHead>
                    <TableHead className="text-right">Allowances</TableHead>
                    <TableHead className="text-right">Gross Pay</TableHead>
                    <TableHead className="text-right">Deductions</TableHead>
                    <TableHead className="text-right">Tax</TableHead>
                    <TableHead className="text-right">Net Pay</TableHead>
                    <TableHead className="text-right">Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {payrollEntries.map((entry) => (
                    <TableRow key={entry.id}>
                      <TableCell>
                        <div>
                          <p className="font-medium">{entry.employeeName}</p>
                          <p className="text-xs text-muted-foreground">{entry.position}</p>
                        </div>
                      </TableCell>
                      <TableCell>{entry.department}</TableCell>
                      <TableCell className="text-right font-mono text-sm">
                        {formatCurrency(entry.basicSalary, entry.currency)}
                      </TableCell>
                      <TableCell className="text-right font-mono text-sm">
                        {formatCurrency(entry.allowances, entry.currency)}
                      </TableCell>
                      <TableCell className="text-right font-mono text-sm font-medium">
                        {formatCurrency(entry.grossSalary, entry.currency)}
                      </TableCell>
                      <TableCell className="text-right font-mono text-sm text-red-600">
                        {formatCurrency(entry.deductions, entry.currency)}
                      </TableCell>
                      <TableCell className="text-right font-mono text-sm text-muted-foreground">
                        {formatCurrency(entry.tax, entry.currency)}
                      </TableCell>
                      <TableCell className="text-right font-mono text-sm font-medium text-green-600">
                        {formatCurrency(entry.netPay, entry.currency)}
                      </TableCell>
                      <TableCell className="text-right">
                        <Button size="sm" variant="outline">
                          <FileText className="mr-2 h-3 w-3" />
                          View
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="payslips" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Payslip Management</CardTitle>
              <CardDescription>
                Generate and distribute employee payslips
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className="text-center py-12 text-muted-foreground">
                <FileText className="h-12 w-12 mx-auto mb-4 opacity-50" />
                <p>Payslip generation interface</p>
                <p className="text-sm">Select employees and generate payslips for the period</p>
                <div className="flex justify-center gap-2 mt-4">
                  <Button variant="outline">Generate All</Button>
                  <Button variant="hr">Generate Selected</Button>
                </div>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="tax" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Tax Configuration</CardTitle>
              <CardDescription>
                Manage tax rules and deduction rates by country
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                <div className="border rounded-lg p-4">
                  <div className="flex items-center justify-between mb-4">
                    <div>
                      <h4 className="font-semibold">🇳🇬 Nigeria - PAYE Tax</h4>
                      <p className="text-sm text-muted-foreground">Personal Income Tax</p>
                    </div>
                    <Badge variant="success">Active</Badge>
                  </div>
                  <div className="space-y-2 text-sm">
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">First 300,000/year</span>
                      <span className="font-medium">7%</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">Next 300,000/year</span>
                      <span className="font-medium">11%</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">Next 500,000/year</span>
                      <span className="font-medium">15%</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">Next 500,000/year</span>
                      <span className="font-medium">19%</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">Above 2,100,000/year</span>
                      <span className="font-medium">24%</span>
                    </div>
                  </div>
                </div>

                <div className="border rounded-lg p-4">
                  <div className="flex items-center justify-between mb-4">
                    <div>
                      <h4 className="font-semibold">🇺🇸 United States - Federal Tax</h4>
                      <p className="text-sm text-muted-foreground">IRS Tax Brackets (2024)</p>
                    </div>
                    <Badge variant="success">Active</Badge>
                  </div>
                  <Button variant="outline" size="sm">View Details</Button>
                </div>

                <div className="border rounded-lg p-4">
                  <div className="flex items-center justify-between mb-4">
                    <div>
                      <h4 className="font-semibold">🇬🇧 United Kingdom - PAYE</h4>
                      <p className="text-sm text-muted-foreground">HMRC Tax & NI</p>
                    </div>
                    <Badge variant="success">Active</Badge>
                  </div>
                  <Button variant="outline" size="sm">View Details</Button>
                </div>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="benefits" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Benefits Administration</CardTitle>
              <CardDescription>
                Manage employee benefits and deductions
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className="grid gap-4 md:grid-cols-3">
                <div className="border rounded-lg p-4">
                  <div className="flex items-center gap-3 mb-3">
                    <div className="h-10 w-10 rounded-lg bg-blue-100 flex items-center justify-center">
                      <span className="text-xl">🏥</span>
                    </div>
                    <div>
                      <h4 className="font-semibold">Health Insurance</h4>
                      <p className="text-xs text-muted-foreground">245 enrolled</p>
                    </div>
                  </div>
                  <Button variant="outline" size="sm" className="w-full">
                    Manage
                  </Button>
                </div>

                <div className="border rounded-lg p-4">
                  <div className="flex items-center gap-3 mb-3">
                    <div className="h-10 w-10 rounded-lg bg-purple-100 flex items-center justify-center">
                      <span className="text-xl">🦷</span>
                    </div>
                    <div>
                      <h4 className="font-semibold">Dental Plan</h4>
                      <p className="text-xs text-muted-foreground">182 enrolled</p>
                    </div>
                  </div>
                  <Button variant="outline" size="sm" className="w-full">
                    Manage
                  </Button>
                </div>

                <div className="border rounded-lg p-4">
                  <div className="flex items-center gap-3 mb-3">
                    <div className="h-10 w-10 rounded-lg bg-green-100 flex items-center justify-center">
                      <span className="text-xl">💰</span>
                    </div>
                    <div>
                      <h4 className="font-semibold">Retirement</h4>
                      <p className="text-xs text-muted-foreground">312 enrolled</p>
                    </div>
                  </div>
                  <Button variant="outline" size="sm" className="w-full">
                    Manage
                  </Button>
                </div>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="reports" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Payroll Reports</CardTitle>
              <CardDescription>
                Generate and export payroll reports
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className="grid gap-4 md:grid-cols-2">
                <div className="border rounded-lg p-4 hover:bg-slate-50 dark:hover:bg-slate-800 transition-colors cursor-pointer">
                  <div className="flex items-center gap-3">
                    <FileText className="h-8 w-8 text-blue-600" />
                    <div>
                      <h4 className="font-semibold">Payroll Summary</h4>
                      <p className="text-sm text-muted-foreground">
                        Monthly payroll breakdown by department
                      </p>
                    </div>
                  </div>
                </div>

                <div className="border rounded-lg p-4 hover:bg-slate-50 dark:hover:bg-slate-800 transition-colors cursor-pointer">
                  <div className="flex items-center gap-3">
                    <FileText className="h-8 w-8 text-green-600" />
                    <div>
                      <h4 className="font-semibold">Tax Report</h4>
                      <p className="text-sm text-muted-foreground">
                        Tax deductions and contributions
                      </p>
                    </div>
                  </div>
                </div>

                <div className="border rounded-lg p-4 hover:bg-slate-50 dark:hover:bg-slate-800 transition-colors cursor-pointer">
                  <div className="flex items-center gap-3">
                    <FileText className="h-8 w-8 text-purple-600" />
                    <div>
                      <h4 className="font-semibold">Benefits Cost Analysis</h4>
                      <p className="text-sm text-muted-foreground">
                        Company benefits expenditure
                      </p>
                    </div>
                  </div>
                </div>

                <div className="border rounded-lg p-4 hover:bg-slate-50 dark:hover:bg-slate-800 transition-colors cursor-pointer">
                  <div className="flex items-center gap-3">
                    <FileText className="h-8 w-8 text-orange-600" />
                    <div>
                      <h4 className="font-semibold">Year-to-Date Summary</h4>
                      <p className="text-sm text-muted-foreground">
                        Cumulative payroll data for the year
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}

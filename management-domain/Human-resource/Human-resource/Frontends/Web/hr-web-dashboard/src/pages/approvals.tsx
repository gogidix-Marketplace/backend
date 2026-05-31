import { useState } from 'react'
import {
  CheckCircle,
  XCircle,
  Clock,
  Filter,
  Download,
  DollarSign,
  Calendar,
  BookOpen,
  TrendingUp,
  ArrowUpRight,
  ArrowDownRight,
  User,
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
import { cn, formatDate, formatCurrency } from '@shared/utils/cn'
import { approvalRequests } from '@shared/data/mockData'

const typeConfig: Record<string, { label: string; icon: React.ComponentType<{ className?: string }>; color: string }> = {
  LEAVE: { label: 'Leave', icon: Calendar, color: 'bg-blue-100 text-blue-700 dark:bg-blue-900/20 dark:text-blue-400' },
  SALARY_CHANGE: { label: 'Salary Change', icon: DollarSign, color: 'bg-green-100 text-green-700 dark:bg-green-900/20 dark:text-green-400' },
  PROMOTION: { label: 'Promotion', icon: TrendingUp, color: 'bg-purple-100 text-purple-700 dark:bg-purple-900/20 dark:text-purple-400' },
  EXPENSE: { label: 'Expense', icon: DollarSign, color: 'bg-orange-100 text-orange-700 dark:bg-orange-900/20 dark:text-orange-400' },
  TRAINING: { label: 'Training', icon: BookOpen, color: 'bg-teal-100 text-teal-700 dark:bg-teal-900/20 dark:text-teal-400' },
}

const priorityConfig: Record<string, { label: string; variant: 'default' | 'success' | 'warning' | 'destructive' | 'secondary' }> = {
  urgent: { label: 'Urgent', variant: 'destructive' },
  high: { label: 'High', variant: 'warning' },
  medium: { label: 'Medium', variant: 'info' },
  low: { label: 'Low', variant: 'secondary' },
}

export default function ApprovalsPage() {
  const [statusFilter, setStatusFilter] = useState<string>('all')
  const [typeFilter, setTypeFilter] = useState<string>('all')

  const filtered = approvalRequests.filter(a => {
    const matchStatus = statusFilter === 'all' || a.status === statusFilter
    const matchType = typeFilter === 'all' || a.type === typeFilter
    return matchStatus && matchType
  })

  const pendingCount = approvalRequests.filter(a => a.status === 'PENDING').length
  const approvedCount = approvalRequests.filter(a => a.status === 'APPROVED').length
  const rejectedCount = approvalRequests.filter(a => a.status === 'REJECTED').length
  const totalAmount = approvalRequests.filter(a => a.amount).reduce((sum, a) => sum + (a.amount || 0), 0)

  return (
    <div className="space-y-6">
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        <div className="page-header">
          <h1 className="page-title">Approvals</h1>
          <p className="page-description">Review and manage pending approval requests</p>
        </div>
        <div className="flex items-center gap-2">
          <Button variant="outline" size="sm">
            <Download className="mr-2 h-4 w-4" />
            Export
          </Button>
          <Button variant="hr" size="sm">
            <Clock className="mr-2 h-4 w-4" />
            Approval History
          </Button>
        </div>
      </div>

      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-yellow-100 p-2 dark:bg-yellow-900/20">
                <Clock className="h-4 w-4 text-yellow-600 dark:text-yellow-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{pendingCount}</p>
                <p className="text-xs text-muted-foreground">Pending</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-green-100 p-2 dark:bg-green-900/20">
                <CheckCircle className="h-4 w-4 text-green-600 dark:text-green-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{approvedCount}</p>
                <p className="text-xs text-muted-foreground">Approved</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-red-100 p-2 dark:bg-red-900/20">
                <XCircle className="h-4 w-4 text-red-600 dark:text-red-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{rejectedCount}</p>
                <p className="text-xs text-muted-foreground">Rejected</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-blue-100 p-2 dark:bg-blue-900/20">
                <DollarSign className="h-4 w-4 text-blue-600 dark:text-blue-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{formatCurrency(totalAmount, 'USD')}</p>
                <p className="text-xs text-muted-foreground">Total Amount</p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      <Tabs defaultValue="pending" className="space-y-4">
        <TabsList>
          <TabsTrigger value="pending">
            Pending
            {pendingCount > 0 && (
              <Badge variant="destructive" className="ml-2 h-5 w-5 p-0 text-xs flex items-center justify-center">
                {pendingCount}
              </Badge>
            )}
          </TabsTrigger>
          <TabsTrigger value="all">All Requests</TabsTrigger>
          <TabsTrigger value="history">History</TabsTrigger>
        </TabsList>

        <TabsContent value="pending" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Pending Approvals</CardTitle>
              <CardDescription>Requests awaiting your review and decision</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {approvalRequests.filter(a => a.status === 'PENDING').map((request) => {
                  const config = typeConfig[request.type] || typeConfig.LEAVE
                  const Icon = config.icon
                  return (
                    <div key={request.id} className="flex items-start gap-4 rounded-lg border p-4 hover:bg-slate-50 dark:hover:bg-slate-800 transition-colors">
                      <div className={cn('rounded-lg p-2 mt-0.5', config.color)}>
                        <Icon className="h-5 w-5" />
                      </div>
                      <div className="flex-1 min-w-0">
                        <div className="flex items-center justify-between">
                          <div>
                            <p className="font-medium">{request.title}</p>
                            <p className="text-sm text-muted-foreground">{request.description}</p>
                          </div>
                          <Badge variant={priorityConfig[request.priority]?.variant || 'secondary'}>
                            {request.priority}
                          </Badge>
                        </div>
                        <div className="flex items-center gap-4 mt-2 text-sm text-muted-foreground">
                          <div className="flex items-center gap-1">
                            <User className="h-3 w-3" />
                            {request.requestedBy}
                          </div>
                          <div className="flex items-center gap-1">
                            <Calendar className="h-3 w-3" />
                            {formatDate(request.requestedAt)}
                          </div>
                          {request.amount && (
                            <div className="flex items-center gap-1">
                              <DollarSign className="h-3 w-3" />
                              {formatCurrency(request.amount, 'USD')}
                            </div>
                          )}
                        </div>
                      </div>
                      <div className="flex items-center gap-2 shrink-0">
                        <Button size="sm" variant="outline" className="text-red-600 hover:text-red-700 hover:bg-red-50">
                          <XCircle className="mr-1 h-4 w-4" />
                          Reject
                        </Button>
                        <Button size="sm" variant="hr" className="text-white">
                          <CheckCircle className="mr-1 h-4 w-4" />
                          Approve
                        </Button>
                      </div>
                    </div>
                  )
                })}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="all" className="space-y-4">
          <Card>
            <CardContent className="p-4">
              <div className="flex flex-col md:flex-row gap-4">
                <Select value={statusFilter} onValueChange={setStatusFilter}>
                  <SelectTrigger className="w-full md:w-[180px]">
                    <SelectValue placeholder="Status" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">All Statuses</SelectItem>
                    <SelectItem value="PENDING">Pending</SelectItem>
                    <SelectItem value="APPROVED">Approved</SelectItem>
                    <SelectItem value="REJECTED">Rejected</SelectItem>
                  </SelectContent>
                </Select>
                <Select value={typeFilter} onValueChange={setTypeFilter}>
                  <SelectTrigger className="w-full md:w-[180px]">
                    <SelectValue placeholder="Type" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">All Types</SelectItem>
                    <SelectItem value="LEAVE">Leave</SelectItem>
                    <SelectItem value="SALARY_CHANGE">Salary</SelectItem>
                    <SelectItem value="PROMOTION">Promotion</SelectItem>
                    <SelectItem value="EXPENSE">Expense</SelectItem>
                    <SelectItem value="TRAINING">Training</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>All Approval Requests</CardTitle>
              <CardDescription>{filtered.length} request(s) found</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Type</TableHead>
                    <TableHead>Title</TableHead>
                    <TableHead>Requested By</TableHead>
                    <TableHead>Date</TableHead>
                    <TableHead>Priority</TableHead>
                    <TableHead className="text-right">Amount</TableHead>
                    <TableHead>Status</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {filtered.map((request) => {
                    const config = typeConfig[request.type] || typeConfig.LEAVE
                    return (
                      <TableRow key={request.id}>
                        <TableCell>
                          <Badge variant="outline" className={cn('border-0', config.color)}>
                            {config.label}
                          </Badge>
                        </TableCell>
                        <TableCell>
                          <div>
                            <p className="font-medium">{request.title}</p>
                            <p className="text-xs text-muted-foreground">{request.description}</p>
                          </div>
                        </TableCell>
                        <TableCell>{request.requestedBy}</TableCell>
                        <TableCell>{formatDate(request.requestedAt)}</TableCell>
                        <TableCell>
                          <Badge variant={priorityConfig[request.priority]?.variant || 'secondary'}>
                            {request.priority}
                          </Badge>
                        </TableCell>
                        <TableCell className="text-right">
                          {request.amount ? formatCurrency(request.amount, 'USD') : '-'}
                        </TableCell>
                        <TableCell>
                          <Badge variant={request.status === 'PENDING' ? 'warning' : request.status === 'APPROVED' ? 'success' : 'destructive'}>
                            {request.status}
                          </Badge>
                        </TableCell>
                      </TableRow>
                    )
                  })}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="history" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Approval History</CardTitle>
              <CardDescription>Recent approval decisions</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                {[
                  { title: 'Sick Leave Request', type: 'LEAVE', action: 'Approved', by: 'Sarah Johnson', date: '2024-03-04', employee: 'Michael Chen' },
                  { title: 'Maternity Leave', type: 'LEAVE', action: 'Approved', by: 'Amanda Sterling', date: '2024-01-25', employee: 'David Okafor' },
                  { title: 'Conference Travel Budget', type: 'EXPENSE', action: 'Rejected', by: 'Robert Chen', date: '2024-03-01', employee: 'Emily Williams' },
                  { title: 'AWS Training Enrollment', type: 'TRAINING', action: 'Approved', by: 'Sarah Johnson', date: '2024-02-28', employee: 'John Smith' },
                ].map((item, i) => {
                  const config = typeConfig[item.type] || typeConfig.LEAVE
                  return (
                    <div key={i} className="flex items-center gap-4 rounded-lg border p-3">
                      <div className={cn(
                        'rounded-full p-1.5',
                        item.action === 'Approved' ? 'bg-green-100 text-green-600' : 'bg-red-100 text-red-600'
                      )}>
                        {item.action === 'Approved' ? <CheckCircle className="h-4 w-4" /> : <XCircle className="h-4 w-4" />}
                      </div>
                      <div className="flex-1">
                        <p className="text-sm font-medium">{item.title} — {item.employee}</p>
                        <p className="text-xs text-muted-foreground">
                          {item.action} by {item.by} on {item.date}
                        </p>
                      </div>
                      <Badge variant={item.action === 'Approved' ? 'success' : 'destructive'}>
                        {item.action}
                      </Badge>
                    </div>
                  )
                })}
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}

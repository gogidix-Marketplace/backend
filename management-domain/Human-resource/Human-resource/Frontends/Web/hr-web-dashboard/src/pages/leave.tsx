import { useState } from 'react'
import { Calendar, Clock, CheckCircle, XCircle, Filter, Plus, User } from 'lucide-react'
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
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@shared/components/ui/dialog'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import { Textarea } from '@shared/components/ui/textarea'
import { cn, formatDate } from '@shared/utils/cn'
import { leaveRequests, leaveBalances } from '@shared/data/mockData'
import type { LeaveStatus, LeaveType } from '@shared/types'

const leaveTypeConfig: Record<LeaveType, { label: string; color: string; bgClass: string }> = {
  ANNUAL: { label: 'Annual', color: 'text-blue-600', bgClass: 'bg-blue-100' },
  SICK: { label: 'Sick', color: 'text-red-600', bgClass: 'bg-red-100' },
  MATERNITY: { label: 'Maternity', color: 'text-purple-600', bgClass: 'bg-purple-100' },
  PATERNITY: { label: 'Paternity', color: 'text-indigo-600', bgClass: 'bg-indigo-100' },
  UNPAID: { label: 'Unpaid', color: 'text-gray-600', bgClass: 'bg-gray-100' },
  COMPASSIONATE: { label: 'Compassionate', color: 'text-orange-600', bgClass: 'bg-orange-100' },
  STUDY: { label: 'Study', color: 'text-teal-600', bgClass: 'bg-teal-100' },
  SABBATICAL: { label: 'Sabbatical', color: 'text-cyan-600', bgClass: 'bg-cyan-100' },
}

const statusConfig: Record<LeaveStatus, { label: string; variant: 'success' | 'warning' | 'destructive' | 'secondary' | 'teal' }> = {
  PENDING: { label: 'Pending', variant: 'warning' },
  APPROVED: { label: 'Approved', variant: 'success' },
  REJECTED: { label: 'Rejected', variant: 'destructive' },
  CANCELLED: { label: 'Cancelled', variant: 'secondary' },
}

export default function LeavePage() {
  const [selectedStatus, setSelectedStatus] = useState<string>('all')
  const [requestDialogOpen, setRequestDialogOpen] = useState(false)
  const [balanceDialogOpen, setBalanceDialogOpen] = useState(false)
  const [selectedEmployee, setSelectedEmployee] = useState<string | null>(null)

  const filteredRequests = leaveRequests.filter((req) =>
    selectedStatus === 'all' ? true : req.status === selectedStatus
  )

  const pendingCount = leaveRequests.filter((r) => r.status === 'PENDING').length
  const approvedCount = leaveRequests.filter((r) => r.status === 'APPROVED').length

  const stats = [
    { label: 'Pending Requests', value: pendingCount, color: 'bg-yellow-500' },
    { label: 'Approved', value: approvedCount, color: 'bg-green-500' },
    { label: 'On Leave Today', value: 12, color: 'bg-blue-500' },
    { label: 'This Week', value: 8, color: 'bg-purple-500' },
  ]

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        <div className="page-header">
          <h1 className="page-title">Leave Management</h1>
          <p className="page-description">
            Manage employee leave requests and balances
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Button variant="outline" size="sm">
            <Calendar className="mr-2 h-4 w-4" />
            Leave Calendar
          </Button>
          <Button variant="hr" size="sm" onClick={() => setRequestDialogOpen(true)}>
            <Plus className="mr-2 h-4 w-4" />
            Request Leave
          </Button>
        </div>
      </div>

      {/* Stats */}
      <div className="grid gap-4 md:grid-cols-4">
        {stats.map((stat) => (
          <Card key={stat.label}>
            <CardContent className="p-4">
              <div className="flex items-center gap-3">
                <div className={cn('rounded-lg p-2', stat.color, 'bg-opacity-20')}>
                  <Clock className="h-4 w-4" />
                </div>
                <div>
                  <p className="text-2xl font-bold">{stat.value}</p>
                  <p className="text-xs text-muted-foreground">{stat.label}</p>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      <Tabs defaultValue="requests" className="space-y-4">
        <TabsList>
          <TabsTrigger value="requests">Leave Requests</TabsTrigger>
          <TabsTrigger value="balances">Leave Balances</TabsTrigger>
          <TabsTrigger value="calendar">Calendar View</TabsTrigger>
          <TabsTrigger value="policy">Leave Policy</TabsTrigger>
        </TabsList>

        <TabsContent value="requests" className="space-y-4">
          {/* Filters */}
          <Card>
            <CardContent className="p-4">
              <div className="flex items-center gap-4">
                <div className="flex items-center gap-2">
                  <Filter className="h-4 w-4 text-muted-foreground" />
                  <span className="text-sm font-medium">Filter:</span>
                </div>
                <Select value={selectedStatus} onValueChange={setSelectedStatus}>
                  <SelectTrigger className="w-[180px]">
                    <SelectValue placeholder="All Statuses" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">All Statuses</SelectItem>
                    <SelectItem value="PENDING">Pending</SelectItem>
                    <SelectItem value="APPROVED">Approved</SelectItem>
                    <SelectItem value="REJECTED">Rejected</SelectItem>
                    <SelectItem value="CANCELLED">Cancelled</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </CardContent>
          </Card>

          {/* Requests Table */}
          <Card>
            <CardHeader>
              <CardTitle>Leave Requests</CardTitle>
              <CardDescription>
                {filteredRequests.length} request(s) found
              </CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Employee</TableHead>
                    <TableHead>Type</TableHead>
                    <TableHead>Dates</TableHead>
                    <TableHead>Duration</TableHead>
                    <TableHead>Reason</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead className="text-right">Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {filteredRequests.map((request) => (
                    <TableRow key={request.id}>
                      <TableCell className="font-medium">{request.employeeName}</TableCell>
                      <TableCell>
                        <Badge
                          variant="outline"
                          className={cn(
                            'border-0',
                            leaveTypeConfig[request.type].bgClass,
                            leaveTypeConfig[request.type].color
                          )}
                        >
                          {leaveTypeConfig[request.type].label}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        <div className="text-sm">
                          <div>{formatDate(request.startDate)}</div>
                          <div className="text-muted-foreground">to {formatDate(request.endDate)}</div>
                        </div>
                      </TableCell>
                      <TableCell>
                        <div className="flex items-center gap-1">
                          <Clock className="h-3 w-3 text-muted-foreground" />
                          {request.days} day(s)
                        </div>
                      </TableCell>
                      <TableCell className="max-w-[200px] truncate">
                        {request.reason}
                      </TableCell>
                      <TableCell>
                        <Badge variant={statusConfig[request.status].variant}>
                          {statusConfig[request.status].label}
                        </Badge>
                      </TableCell>
                      <TableCell className="text-right">
                        {request.status === 'PENDING' && (
                          <div className="flex items-center justify-end gap-1">
                            <Button
                              size="icon"
                              variant="ghost"
                              className="h-8 w-8 text-green-600 hover:text-green-700 hover:bg-green-100"
                            >
                              <CheckCircle className="h-4 w-4" />
                            </Button>
                            <Button
                              size="icon"
                              variant="ghost"
                              className="h-8 w-8 text-red-600 hover:text-red-700 hover:bg-red-100"
                            >
                              <XCircle className="h-4 w-4" />
                            </Button>
                          </div>
                        )}
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="balances" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Employee Leave Balances</CardTitle>
              <CardDescription>
                View and manage leave balances for all employees
              </CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Employee</TableHead>
                    <TableHead>Annual</TableHead>
                    <TableHead>Sick</TableHead>
                    <TableHead>Maternity</TableHead>
                    <TableHead>Paternity</TableHead>
                    <TableHead>Unpaid</TableHead>
                    <TableHead className="text-right">Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {Object.values(leaveBalances).map((balance) => {
                    const employee = balance.employeeId // In real app, would fetch employee details
                    return (
                      <TableRow key={balance.employeeId}>
                        <TableCell className="font-medium">{balance.employeeId}</TableCell>
                        <TableCell>
                          <div className="text-sm">
                            {balance.annual.used} / {balance.annual.total}
                          </div>
                          <div className="h-1.5 w-full bg-slate-200 rounded-full mt-1">
                            <div
                              className="h-full bg-blue-500 rounded-full"
                              style={{
                                width: `${(balance.annual.used / balance.annual.total) * 100}%`,
                              }}
                            />
                          </div>
                        </TableCell>
                        <TableCell>
                          <div className="text-sm">
                            {balance.sick.used} / {balance.sick.total}
                          </div>
                          <div className="h-1.5 w-full bg-slate-200 rounded-full mt-1">
                            <div
                              className="h-full bg-red-500 rounded-full"
                              style={{
                                width: `${(balance.sick.used / balance.sick.total) * 100}%`,
                              }}
                            />
                          </div>
                        </TableCell>
                        <TableCell>
                          {balance.maternity.used} / {balance.maternity.total}
                        </TableCell>
                        <TableCell>
                          {balance.paternity.used} / {balance.paternity.total}
                        </TableCell>
                        <TableCell>
                          {balance.unpaid.used} / {balance.unpaid.total}
                        </TableCell>
                        <TableCell className="text-right">
                          <Button size="sm" variant="outline" onClick={() => setBalanceDialogOpen(true)}>
                            View Details
                          </Button>
                        </TableCell>
                      </TableRow>
                    )
                  })}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="calendar" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Leave Calendar</CardTitle>
              <CardDescription>
                Visual overview of scheduled leaves
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className="text-center py-12 text-muted-foreground">
                <Calendar className="h-12 w-12 mx-auto mb-4 opacity-50" />
                <p>Calendar view will be displayed here</p>
                <p className="text-sm">Integration with calendar component coming soon</p>
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="policy" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Leave Policy</CardTitle>
              <CardDescription>
                Company leave policies and entitlements
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="grid gap-4 md:grid-cols-2">
                <div className="border rounded-lg p-4">
                  <h4 className="font-semibold mb-2">Annual Leave</h4>
                  <p className="text-sm text-muted-foreground">
                    Full-time employees are entitled to 25 days of paid annual leave per year,
                    pro-rated for part-time employees.
                  </p>
                </div>
                <div className="border rounded-lg p-4">
                  <h4 className="font-semibold mb-2">Sick Leave</h4>
                  <p className="text-sm text-muted-foreground">
                    Employees are entitled to 10 days of paid sick leave per year.
                  </p>
                </div>
                <div className="border rounded-lg p-4">
                  <h4 className="font-semibold mb-2">Maternity Leave</h4>
                  <p className="text-sm text-muted-foreground">
                    Female employees are entitled to 90 days of paid maternity leave.
                  </p>
                </div>
                <div className="border rounded-lg p-4">
                  <h4 className="font-semibold mb-2">Paternity Leave</h4>
                  <p className="text-sm text-muted-foreground">
                    Male employees are entitled to 14 days of paid paternity leave.
                  </p>
                </div>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>

      {/* Request Leave Dialog */}
      <Dialog open={requestDialogOpen} onOpenChange={setRequestDialogOpen}>
        <DialogContent className="sm:max-w-[500px]">
          <DialogHeader>
            <DialogTitle>Request Leave</DialogTitle>
            <DialogDescription>
              Submit a new leave request for approval
            </DialogDescription>
          </DialogHeader>
          <div className="space-y-4 py-4">
            <div className="space-y-2">
              <Label htmlFor="employee">Employee</Label>
              <Select>
                <SelectTrigger id="employee">
                  <SelectValue placeholder="Select employee" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="emp-001">John Smith</SelectItem>
                  <SelectItem value="emp-002">Sarah Johnson</SelectItem>
                  <SelectItem value="emp-003">Michael Chen</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-2">
              <Label htmlFor="leaveType">Leave Type</Label>
              <Select>
                <SelectTrigger id="leaveType">
                  <SelectValue placeholder="Select leave type" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="ANNUAL">Annual Leave</SelectItem>
                  <SelectItem value="SICK">Sick Leave</SelectItem>
                  <SelectItem value="MATERNITY">Maternity Leave</SelectItem>
                  <SelectItem value="PATERNITY">Paternity Leave</SelectItem>
                  <SelectItem value="UNPAID">Unpaid Leave</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label htmlFor="startDate">Start Date</Label>
                <Input type="date" id="startDate" />
              </div>
              <div className="space-y-2">
                <Label htmlFor="endDate">End Date</Label>
                <Input type="date" id="endDate" />
              </div>
            </div>
            <div className="space-y-2">
              <Label htmlFor="reason">Reason</Label>
              <Textarea
                id="reason"
                placeholder="Provide a reason for the leave request..."
                rows={3}
              />
            </div>
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setRequestDialogOpen(false)}>
              Cancel
            </Button>
            <Button variant="hr" onClick={() => setRequestDialogOpen(false)}>
              Submit Request
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  )
}

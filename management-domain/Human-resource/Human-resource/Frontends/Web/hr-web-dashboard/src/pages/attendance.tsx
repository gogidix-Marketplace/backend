import { useState } from 'react'
import {
  Clock,
  Users,
  CheckCircle,
  AlertTriangle,
  Download,
  Filter,
  Calendar,
  MapPin,
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
import { cn } from '@shared/utils/cn'
import { timeEntries } from '@shared/data/mockData'

const entryStatusConfig: Record<string, { label: string; variant: 'success' | 'warning' | 'destructive' | 'secondary' | 'info' }> = {
  PRESENT: { label: 'Present', variant: 'success' },
  ABSENT: { label: 'Absent', variant: 'destructive' },
  LATE: { label: 'Late', variant: 'warning' },
  HALF_DAY: { label: 'Half Day', variant: 'info' },
}

const weeklySummary = [
  { day: 'Monday', date: '2024-03-04', present: 2450, absent: 85, late: 120, overtime: 320 },
  { day: 'Tuesday', date: '2024-03-05', present: 2480, absent: 72, late: 95, overtime: 280 },
  { day: 'Wednesday', date: '2024-03-06', present: 2465, absent: 78, late: 105, overtime: 310 },
  { day: 'Thursday', date: '2024-03-07', present: 2440, absent: 90, late: 115, overtime: 295 },
  { day: 'Friday', date: '2024-03-08', present: 2390, absent: 120, late: 130, overtime: 180 },
]

export default function AttendancePage() {
  const [selectedStatus, setSelectedStatus] = useState<string>('all')

  const filteredEntries = timeEntries.filter(e =>
    selectedStatus === 'all' || e.status === selectedStatus
  )

  const totalPresent = weeklySummary.reduce((sum, d) => sum + d.present, 0)
  const totalAbsent = weeklySummary.reduce((sum, d) => sum + d.absent, 0)
  const totalLate = weeklySummary.reduce((sum, d) => sum + d.late, 0)
  const totalOvertime = weeklySummary.reduce((sum, d) => sum + d.overtime, 0)
  const avgAttendance = Math.round((totalPresent / (totalPresent + totalAbsent)) * 100)

  return (
    <div className="space-y-6">
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        <div className="page-header">
          <h1 className="page-title">Time & Attendance</h1>
          <p className="page-description">Track employee attendance, clock-ins, and overtime</p>
        </div>
        <div className="flex items-center gap-2">
          <Button variant="outline" size="sm">
            <Download className="mr-2 h-4 w-4" />
            Export
          </Button>
          <Button variant="hr" size="sm">
            <Calendar className="mr-2 h-4 w-4" />
            View Calendar
          </Button>
        </div>
      </div>

      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-green-100 p-2 dark:bg-green-900/20">
                <CheckCircle className="h-4 w-4 text-green-600 dark:text-green-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{avgAttendance}%</p>
                <p className="text-xs text-muted-foreground">Attendance Rate</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-blue-100 p-2 dark:bg-blue-900/20">
                <Users className="h-4 w-4 text-blue-600 dark:text-blue-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{(totalPresent / 5).toLocaleString()}</p>
                <p className="text-xs text-muted-foreground">Avg Daily Present</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-yellow-100 p-2 dark:bg-yellow-900/20">
                <AlertTriangle className="h-4 w-4 text-yellow-600 dark:text-yellow-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{(totalLate / 5).toLocaleString()}</p>
                <p className="text-xs text-muted-foreground">Avg Daily Late</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-purple-100 p-2 dark:bg-purple-900/20">
                <Clock className="h-4 w-4 text-purple-600 dark:text-purple-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{(totalOvertime / 5).toLocaleString()}h</p>
                <p className="text-xs text-muted-foreground">Avg Daily Overtime</p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      <Tabs defaultValue="weekly" className="space-y-4">
        <TabsList>
          <TabsTrigger value="weekly">Weekly Summary</TabsTrigger>
          <TabsTrigger value="entries">Time Entries</TabsTrigger>
          <TabsTrigger value="overtime">Overtime</TabsTrigger>
        </TabsList>

        <TabsContent value="weekly" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Weekly Attendance Summary</CardTitle>
              <CardDescription>Week of March 4-8, 2024</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Day</TableHead>
                    <TableHead>Date</TableHead>
                    <TableHead className="text-right">Present</TableHead>
                    <TableHead className="text-right">Absent</TableHead>
                    <TableHead className="text-right">Late</TableHead>
                    <TableHead className="text-right">Overtime (hrs)</TableHead>
                    <TableHead className="text-right">Attendance %</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {weeklySummary.map((day) => {
                    const attendance = Math.round((day.present / (day.present + day.absent)) * 100)
                    return (
                      <TableRow key={day.day}>
                        <TableCell className="font-medium">{day.day}</TableCell>
                        <TableCell>{day.date}</TableCell>
                        <TableCell className="text-right">
                          <Badge variant="success">{day.present.toLocaleString()}</Badge>
                        </TableCell>
                        <TableCell className="text-right">
                          <Badge variant="destructive">{day.absent}</Badge>
                        </TableCell>
                        <TableCell className="text-right">
                          <Badge variant="warning">{day.late}</Badge>
                        </TableCell>
                        <TableCell className="text-right">{day.overtime}h</TableCell>
                        <TableCell className="text-right">
                          <div className="flex items-center justify-end gap-2">
                            <div className="h-2 w-16 overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
                              <div
                                className={cn(
                                  'h-full rounded-full',
                                  attendance >= 95 ? 'bg-green-500' : attendance >= 90 ? 'bg-yellow-500' : 'bg-red-500'
                                )}
                                style={{ width: `${attendance}%` }}
                              />
                            </div>
                            <span className="font-medium w-12 text-right">{attendance}%</span>
                          </div>
                        </TableCell>
                      </TableRow>
                    )
                  })}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="entries" className="space-y-4">
          <Card>
            <CardContent className="p-4">
              <div className="flex flex-col md:flex-row gap-4">
                <div className="flex-1">
                  <Select value={selectedStatus} onValueChange={setSelectedStatus}>
                    <SelectTrigger className="w-full md:w-[180px]">
                      <SelectValue placeholder="Filter status" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="all">All Statuses</SelectItem>
                      <SelectItem value="PRESENT">Present</SelectItem>
                      <SelectItem value="ABSENT">Absent</SelectItem>
                      <SelectItem value="LATE">Late</SelectItem>
                      <SelectItem value="HALF_DAY">Half Day</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
                <Button variant="outline" size="icon">
                  <Filter className="h-4 w-4" />
                </Button>
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Time Entries</CardTitle>
              <CardDescription>{filteredEntries.length} entry(ies) found</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Employee</TableHead>
                    <TableHead>Date</TableHead>
                    <TableHead>Clock In</TableHead>
                    <TableHead>Clock Out</TableHead>
                    <TableHead>Hours</TableHead>
                    <TableHead>Overtime</TableHead>
                    <TableHead>Status</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {filteredEntries.map((entry) => (
                    <TableRow key={entry.id}>
                      <TableCell className="font-medium">{entry.employeeName}</TableCell>
                      <TableCell>{entry.date}</TableCell>
                      <TableCell>{entry.clockIn}</TableCell>
                      <TableCell>{entry.clockOut || '-'}</TableCell>
                      <TableCell>{entry.hours}h</TableCell>
                      <TableCell>{entry.overtimeHours > 0 ? `${entry.overtimeHours}h` : '-'}</TableCell>
                      <TableCell>
                        <Badge variant={entryStatusConfig[entry.status]?.variant || 'secondary'}>
                          {entryStatusConfig[entry.status]?.label || entry.status}
                        </Badge>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="overtime" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Overtime Summary</CardTitle>
              <CardDescription>Employee overtime hours for the current period</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {[
                  { name: 'John Smith', department: 'Engineering', hours: 12.5, approved: 10, pending: 2.5 },
                  { name: 'Sarah Johnson', department: 'Engineering', hours: 8, approved: 8, pending: 0 },
                  { name: 'Emily Williams', department: 'Marketing', hours: 6, approved: 4, pending: 2 },
                  { name: 'Michael Chen', department: 'Sales', hours: 3, approved: 3, pending: 0 },
                ].map((emp) => (
                  <div key={emp.name} className="flex items-center gap-4 rounded-lg border p-4">
                    <div className="flex-1">
                      <div className="flex items-center justify-between">
                        <div>
                          <p className="font-medium">{emp.name}</p>
                          <p className="text-sm text-muted-foreground">{emp.department}</p>
                        </div>
                        <div className="text-right">
                          <p className="font-semibold">{emp.hours}h total</p>
                          <p className="text-sm text-muted-foreground">
                            {emp.approved}h approved • {emp.pending}h pending
                          </p>
                        </div>
                      </div>
                      <div className="mt-2">
                        <div className="h-2 w-full overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
                          <div className="h-full rounded-full bg-blue-500" style={{ width: `${(emp.approved / emp.hours) * 100}%` }} />
                        </div>
                      </div>
                    </div>
                    {emp.pending > 0 && (
                      <Button size="sm" variant="outline">Approve</Button>
                    )}
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}

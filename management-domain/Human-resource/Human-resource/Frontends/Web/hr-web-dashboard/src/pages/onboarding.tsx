import { useState } from 'react'
import {
  UserPlus,
  CheckCircle,
  Clock,
  FileText,
  Mail,
  Shield,
  Laptop,
  Users,
  BookOpen,
  CreditCard,
  AlertCircle,
} from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Button } from '@shared/components/ui/button'
import { Badge } from '@shared/components/ui/badge'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@shared/components/ui/tabs'
import { cn } from '@shared/utils/cn'

const onboardingTasks = [
  { id: 'ot-001', title: 'Welcome Email Sent', category: 'Communication', icon: Mail, completed: true, assignee: 'System' },
  { id: 'ot-002', title: 'Contract Signed', category: 'Documentation', icon: FileText, completed: true, assignee: 'HR Manager' },
  { id: 'ot-003', title: 'IT Account Created', category: 'IT Setup', icon: Laptop, completed: true, assignee: 'IT Department' },
  { id: 'ot-004', title: 'Access Badge Issued', category: 'Security', icon: Shield, completed: false, assignee: 'Security' },
  { id: 'ot-005', title: 'Team Introduction', category: 'Orientation', icon: Users, completed: false, assignee: 'Team Lead' },
  { id: 'ot-006', title: 'Training Module 1', category: 'Training', icon: BookOpen, completed: false, assignee: 'L&D' },
  { id: 'ot-007', title: 'Payroll Setup', category: 'Finance', icon: CreditCard, completed: false, assignee: 'Payroll' },
  { id: 'ot-008', title: 'Probation Goals Set', category: 'Performance', icon: CheckCircle, completed: false, assignee: 'Manager' },
]

const newHires = [
  { id: 'nh-001', name: 'Jane Williams', position: 'Software Engineer', department: 'Engineering', startDate: '2024-03-18', progress: 75, status: 'in_progress' },
  { id: 'nh-002', name: 'Alex Thompson', position: 'Sales Associate', department: 'Sales', startDate: '2024-03-20', progress: 38, status: 'in_progress' },
  { id: 'nh-003', name: 'Maria Santos', position: 'UX Designer', department: 'Design', startDate: '2024-03-25', progress: 0, status: 'not_started' },
  { id: 'nh-004', name: 'Ahmed Hassan', position: 'DevOps Engineer', department: 'Engineering', startDate: '2024-03-15', progress: 100, status: 'completed' },
  { id: 'nh-005', name: 'Lisa Park', position: 'Marketing Analyst', department: 'Marketing', startDate: '2024-03-22', progress: 50, status: 'in_progress' },
]

const offboardingTasks = [
  { id: 'obt-001', employeeName: 'Robert Kim', position: 'Sales Manager', lastDay: '2024-03-29', completedTasks: 5, totalTasks: 8 },
  { id: 'obt-002', employeeName: 'Susan Blake', position: 'Support Agent', lastDay: '2024-04-05', completedTasks: 2, totalTasks: 8 },
]

function SimpleProgress({ value, className }: { value: number; className?: string }) {
  return (
    <div className={cn('h-2 w-full overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800', className)}>
      <div
        className={cn(
          'h-full rounded-full transition-all',
          value === 100 ? 'bg-green-500' : value >= 50 ? 'bg-blue-500' : 'bg-yellow-500'
        )}
        style={{ width: `${value}%` }}
      />
    </div>
  )
}

export default function OnboardingPage() {
  const [tasks, setTasks] = useState(onboardingTasks)

  const completedCount = newHires.filter(n => n.status === 'completed').length
  const inProgressCount = newHires.filter(n => n.status === 'in_progress').length
  const notStartedCount = newHires.filter(n => n.status === 'not_started').length

  const selectedHire = newHires[0]
  const selectedTasks = tasks
  const selectedCompleted = selectedTasks.filter(t => t.completed).length

  const toggleTask = (taskId: string) => {
    setTasks(prev => prev.map(t => t.id === taskId ? { ...t, completed: !t.completed } : t))
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        <div className="page-header">
          <h1 className="page-title">Onboarding</h1>
          <p className="page-description">Manage new hire onboarding and offboarding processes</p>
        </div>
        <div className="flex items-center gap-2">
          <Button variant="outline" size="sm">
            <FileText className="mr-2 h-4 w-4" />
            Templates
          </Button>
          <Button variant="hr" size="sm">
            <UserPlus className="mr-2 h-4 w-4" />
            New Checklist
          </Button>
        </div>
      </div>

      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-blue-100 p-2 dark:bg-blue-900/20">
                <UserPlus className="h-4 w-4 text-blue-600 dark:text-blue-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{newHires.length}</p>
                <p className="text-xs text-muted-foreground">New Hires</p>
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
                <p className="text-2xl font-bold">{completedCount}</p>
                <p className="text-xs text-muted-foreground">Completed</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-yellow-100 p-2 dark:bg-yellow-900/20">
                <Clock className="h-4 w-4 text-yellow-600 dark:text-yellow-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{inProgressCount}</p>
                <p className="text-xs text-muted-foreground">In Progress</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-purple-100 p-2 dark:bg-purple-900/20">
                <AlertCircle className="h-4 w-4 text-purple-600 dark:text-purple-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{notStartedCount}</p>
                <p className="text-xs text-muted-foreground">Not Started</p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      <Tabs defaultValue="new-hires" className="space-y-4">
        <TabsList>
          <TabsTrigger value="new-hires">New Hires</TabsTrigger>
          <TabsTrigger value="checklist">Checklist</TabsTrigger>
          <TabsTrigger value="offboarding">Offboarding</TabsTrigger>
        </TabsList>

        <TabsContent value="new-hires" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Current New Hires</CardTitle>
              <CardDescription>Employees currently in the onboarding process</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {newHires.map((hire) => (
                  <div key={hire.id} className="flex items-center gap-4 rounded-lg border p-4 hover:bg-slate-50 dark:hover:bg-slate-800 transition-colors cursor-pointer">
                    <div className="flex h-10 w-10 items-center justify-center rounded-full bg-blue-100 dark:bg-blue-900/20">
                      <span className="text-sm font-semibold text-blue-600 dark:text-blue-400">
                        {hire.name.split(' ').map(n => n[0]).join('')}
                      </span>
                    </div>
                    <div className="flex-1 min-w-0">
                      <div className="flex items-center justify-between">
                        <div>
                          <p className="font-medium">{hire.name}</p>
                          <p className="text-sm text-muted-foreground">{hire.position} • {hire.department}</p>
                        </div>
                        <Badge
                          variant={hire.status === 'completed' ? 'success' : hire.status === 'in_progress' ? 'warning' : 'secondary'}
                        >
                          {hire.status === 'completed' ? 'Completed' : hire.status === 'in_progress' ? 'In Progress' : 'Not Started'}
                        </Badge>
                      </div>
                      <div className="mt-2 flex items-center gap-3">
                        <SimpleProgress value={hire.progress} className="flex-1" />
                        <span className="text-xs text-muted-foreground w-10">{hire.progress}%</span>
                      </div>
                      <p className="text-xs text-muted-foreground mt-1">Start date: {hire.startDate}</p>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="checklist" className="space-y-4">
          <div className="grid gap-6 lg:grid-cols-3">
            <Card className="lg:col-span-2">
              <CardHeader>
                <div className="flex items-center justify-between">
                  <div>
                    <CardTitle>Onboarding Checklist</CardTitle>
                    <CardDescription>{selectedHire.name} — {selectedCompleted}/{selectedTasks.length} tasks completed</CardDescription>
                  </div>
                  <SimpleProgress value={Math.round((selectedCompleted / selectedTasks.length) * 100)} className="w-32" />
                </div>
              </CardHeader>
              <CardContent>
                <div className="space-y-3">
                  {selectedTasks.map((task) => {
                    const Icon = task.icon
                    return (
                      <div
                        key={task.id}
                        className={cn(
                          'flex items-center gap-3 rounded-lg border p-3 transition-colors cursor-pointer',
                          task.completed
                            ? 'bg-green-50 dark:bg-green-900/10 border-green-200 dark:border-green-800'
                            : 'hover:bg-slate-50 dark:hover:bg-slate-800'
                        )}
                        onClick={() => toggleTask(task.id)}
                      >
                        <div className={cn(
                          'flex h-6 w-6 items-center justify-center rounded-full border-2 flex-shrink-0',
                          task.completed
                            ? 'border-green-500 bg-green-500'
                            : 'border-slate-300 dark:border-slate-600'
                        )}>
                          {task.completed && <CheckCircle className="h-4 w-4 text-white" />}
                        </div>
                        <Icon className={cn('h-4 w-4 flex-shrink-0', task.completed ? 'text-green-600' : 'text-muted-foreground')} />
                        <div className="flex-1">
                          <p className={cn('text-sm font-medium', task.completed && 'line-through text-muted-foreground')}>
                            {task.title}
                          </p>
                          <p className="text-xs text-muted-foreground">{task.category} • {task.assignee}</p>
                        </div>
                      </div>
                    )
                  })}
                </div>
              </CardContent>
            </Card>

            <div className="space-y-4">
              <Card>
                <CardHeader>
                  <CardTitle className="text-base">New Hire Details</CardTitle>
                </CardHeader>
                <CardContent className="space-y-3">
                  <div>
                    <p className="text-xs text-muted-foreground">Name</p>
                    <p className="text-sm font-medium">{selectedHire.name}</p>
                  </div>
                  <div>
                    <p className="text-xs text-muted-foreground">Position</p>
                    <p className="text-sm font-medium">{selectedHire.position}</p>
                  </div>
                  <div>
                    <p className="text-xs text-muted-foreground">Department</p>
                    <p className="text-sm font-medium">{selectedHire.department}</p>
                  </div>
                  <div>
                    <p className="text-xs text-muted-foreground">Start Date</p>
                    <p className="text-sm font-medium">{selectedHire.startDate}</p>
                  </div>
                  <div>
                    <p className="text-xs text-muted-foreground">Manager</p>
                    <p className="text-sm font-medium">Sarah Johnson</p>
                  </div>
                  <div>
                    <p className="text-xs text-muted-foreground">Buddy</p>
                    <p className="text-sm font-medium">John Smith</p>
                  </div>
                </CardContent>
              </Card>

              <Card>
                <CardHeader>
                  <CardTitle className="text-base">Day 1 Schedule</CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="space-y-2">
                    <div className="flex gap-2 text-sm">
                      <span className="text-muted-foreground w-16">09:00</span>
                      <span>Welcome & Office Tour</span>
                    </div>
                    <div className="flex gap-2 text-sm">
                      <span className="text-muted-foreground w-16">10:00</span>
                      <span>IT Setup & Equipment</span>
                    </div>
                    <div className="flex gap-2 text-sm">
                      <span className="text-muted-foreground w-16">11:30</span>
                      <span>Team Introduction</span>
                    </div>
                    <div className="flex gap-2 text-sm">
                      <span className="text-muted-foreground w-16">12:30</span>
                      <span>Welcome Lunch</span>
                    </div>
                    <div className="flex gap-2 text-sm">
                      <span className="text-muted-foreground w-16">14:00</span>
                      <span>HR Orientation</span>
                    </div>
                    <div className="flex gap-2 text-sm">
                      <span className="text-muted-foreground w-16">15:30</span>
                      <span>1:1 with Manager</span>
                    </div>
                  </div>
                </CardContent>
              </Card>
            </div>
          </div>
        </TabsContent>

        <TabsContent value="offboarding" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Active Offboarding</CardTitle>
              <CardDescription>Employees currently in the offboarding process</CardDescription>
            </CardHeader>
            <CardContent>
              {offboardingTasks.length === 0 ? (
                <div className="text-center py-8 text-muted-foreground">
                  <Users className="h-12 w-12 mx-auto mb-4 opacity-50" />
                  <p>No active offboarding processes</p>
                </div>
              ) : (
                <div className="space-y-4">
                  {offboardingTasks.map((emp) => (
                    <div key={emp.id} className="flex items-center gap-4 rounded-lg border p-4">
                      <div className="flex-1">
                        <p className="font-medium">{emp.employeeName}</p>
                        <p className="text-sm text-muted-foreground">{emp.position} • Last day: {emp.lastDay}</p>
                        <div className="mt-2 flex items-center gap-3">
                          <SimpleProgress value={Math.round((emp.completedTasks / emp.totalTasks) * 100)} className="flex-1 max-w-xs" />
                          <span className="text-xs text-muted-foreground">{emp.completedTasks}/{emp.totalTasks} tasks</span>
                        </div>
                      </div>
                      <Button size="sm" variant="outline">View Checklist</Button>
                    </div>
                  ))}
                </div>
              )}
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}

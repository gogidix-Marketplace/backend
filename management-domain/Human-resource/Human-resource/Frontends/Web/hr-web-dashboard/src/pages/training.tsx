import { useState } from 'react'
import {
  BookOpen,
  Plus,
  Users,
  Clock,
  CheckCircle,
  Calendar,
  Filter,
  Download,
  Search,
} from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Button } from '@shared/components/ui/button'
import { Badge } from '@shared/components/ui/badge'
import { Input } from '@shared/components/ui/input'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@shared/components/ui/table'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@shared/components/ui/tabs'
import { cn } from '@shared/utils/cn'
import { trainingPrograms } from '@shared/data/mockData'

const programStatusConfig: Record<string, { label: string; variant: 'default' | 'success' | 'warning' | 'destructive' | 'secondary' | 'info' }> = {
  DRAFT: { label: 'Draft', variant: 'secondary' },
  PUBLISHED: { label: 'Published', variant: 'info' },
  IN_PROGRESS: { label: 'In Progress', variant: 'warning' },
  COMPLETED: { label: 'Completed', variant: 'success' },
}

const mockEnrollments = [
  { id: 'enr-001', employeeName: 'John Smith', programTitle: 'Leadership Excellence Program', progress: 45, status: 'IN_PROGRESS', enrolledAt: '2024-03-15' },
  { id: 'enr-002', employeeName: 'Sarah Johnson', programTitle: 'Cloud Architecture Certification', progress: 72, status: 'IN_PROGRESS', enrolledAt: '2024-03-01' },
  { id: 'enr-003', employeeName: 'Michael Chen', programTitle: 'Leadership Excellence Program', progress: 100, status: 'COMPLETED', enrolledAt: '2024-03-15', completedAt: '2024-04-20' },
  { id: 'enr-004', employeeName: 'Emily Williams', programTitle: 'Cloud Architecture Certification', progress: 30, status: 'IN_PROGRESS', enrolledAt: '2024-03-01' },
  { id: 'enr-005', employeeName: 'David Okafor', programTitle: 'Leadership Excellence Program', progress: 0, status: 'ENROLLED', enrolledAt: '2024-03-16' },
]

export default function TrainingPage() {
  const [searchQuery, setSearchQuery] = useState('')

  const totalPrograms = trainingPrograms.length
  const activePrograms = trainingPrograms.filter(p => p.status === 'IN_PROGRESS').length
  const totalEnrolled = mockEnrollments.length
  const completionRate = Math.round(mockEnrollments.filter(e => e.status === 'COMPLETED').length / totalEnrolled * 100)

  const filteredEnrollments = mockEnrollments.filter(e =>
    e.employeeName.toLowerCase().includes(searchQuery.toLowerCase()) ||
    e.programTitle.toLowerCase().includes(searchQuery.toLowerCase())
  )

  return (
    <div className="space-y-6">
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        <div className="page-header">
          <h1 className="page-title">Training & Development</h1>
          <p className="page-description">Manage training programs and employee development</p>
        </div>
        <div className="flex items-center gap-2">
          <Button variant="outline" size="sm">
            <Download className="mr-2 h-4 w-4" />
            Export
          </Button>
          <Button variant="hr" size="sm">
            <Plus className="mr-2 h-4 w-4" />
            New Program
          </Button>
        </div>
      </div>

      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-blue-100 p-2 dark:bg-blue-900/20">
                <BookOpen className="h-4 w-4 text-blue-600 dark:text-blue-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{totalPrograms}</p>
                <p className="text-xs text-muted-foreground">Total Programs</p>
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
                <p className="text-2xl font-bold">{activePrograms}</p>
                <p className="text-xs text-muted-foreground">Active Programs</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-purple-100 p-2 dark:bg-purple-900/20">
                <Users className="h-4 w-4 text-purple-600 dark:text-purple-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{totalEnrolled}</p>
                <p className="text-xs text-muted-foreground">Enrolled</p>
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
                <p className="text-2xl font-bold">{completionRate}%</p>
                <p className="text-xs text-muted-foreground">Completion Rate</p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      <Tabs defaultValue="programs" className="space-y-4">
        <TabsList>
          <TabsTrigger value="programs">Programs</TabsTrigger>
          <TabsTrigger value="enrollments">Enrollments</TabsTrigger>
          <TabsTrigger value="catalog">Course Catalog</TabsTrigger>
        </TabsList>

        <TabsContent value="programs" className="space-y-4">
          <div className="grid gap-4 md:grid-cols-2">
            {trainingPrograms.map((program) => (
              <Card key={program.id}>
                <CardHeader>
                  <div className="flex items-center justify-between">
                    <div>
                      <CardTitle className="text-base">{program.title}</CardTitle>
                      <CardDescription>{program.category}</CardDescription>
                    </div>
                    <Badge variant={programStatusConfig[program.status]?.variant || 'secondary'}>
                      {programStatusConfig[program.status]?.label || program.status}
                    </Badge>
                  </div>
                </CardHeader>
                <CardContent>
                  <p className="text-sm text-muted-foreground mb-4">{program.description}</p>
                  <div className="grid grid-cols-2 gap-3 text-sm">
                    <div>
                      <span className="text-muted-foreground">Instructor:</span>
                      <p className="font-medium">{program.instructor}</p>
                    </div>
                    <div>
                      <span className="text-muted-foreground">Duration:</span>
                      <p className="font-medium">{program.duration} hours</p>
                    </div>
                    <div>
                      <span className="text-muted-foreground">Period:</span>
                      <p className="font-medium">{program.startDate} — {program.endDate}</p>
                    </div>
                    <div>
                      <span className="text-muted-foreground">Capacity:</span>
                      <p className="font-medium">{program.enrolled}/{program.capacity}</p>
                    </div>
                  </div>
                  <div className="mt-4">
                    <div className="flex items-center justify-between text-sm mb-1">
                      <span className="text-muted-foreground">Enrollment</span>
                      <span className="font-medium">{Math.round((program.enrolled / program.capacity) * 100)}%</span>
                    </div>
                    <div className="h-2 w-full overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
                      <div
                        className={cn(
                          'h-full rounded-full',
                          program.enrolled >= program.capacity ? 'bg-red-500' : 'bg-blue-500'
                        )}
                        style={{ width: `${(program.enrolled / program.capacity) * 100}%` }}
                      />
                    </div>
                  </div>
                  <div className="flex gap-2 mt-4">
                    <Button size="sm" variant="outline" className="flex-1">View Details</Button>
                    <Button size="sm" variant="hr" className="flex-1" disabled={program.enrolled >= program.capacity}>
                      {program.enrolled >= program.capacity ? 'Full' : 'Enroll'}
                    </Button>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </TabsContent>

        <TabsContent value="enrollments" className="space-y-4">
          <Card>
            <CardContent className="p-4">
              <div className="flex flex-col md:flex-row gap-4">
                <div className="relative flex-1">
                  <Search className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                  <Input
                    placeholder="Search by employee or program..."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    className="pl-9"
                  />
                </div>
                <Button variant="outline" size="icon">
                  <Filter className="h-4 w-4" />
                </Button>
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Training Enrollments</CardTitle>
              <CardDescription>{filteredEnrollments.length} enrollment(s)</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Employee</TableHead>
                    <TableHead>Program</TableHead>
                    <TableHead>Progress</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Enrolled</TableHead>
                    <TableHead className="text-right">Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {filteredEnrollments.map((enrollment) => (
                    <TableRow key={enrollment.id}>
                      <TableCell className="font-medium">{enrollment.employeeName}</TableCell>
                      <TableCell>{enrollment.programTitle}</TableCell>
                      <TableCell>
                        <div className="flex items-center gap-2">
                          <div className="h-2 w-24 overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
                            <div
                              className={cn(
                                'h-full rounded-full',
                                enrollment.progress === 100 ? 'bg-green-500' : enrollment.progress >= 50 ? 'bg-blue-500' : 'bg-yellow-500'
                              )}
                              style={{ width: `${enrollment.progress}%` }}
                            />
                          </div>
                          <span className="text-sm">{enrollment.progress}%</span>
                        </div>
                      </TableCell>
                      <TableCell>
                        <Badge variant={enrollment.status === 'COMPLETED' ? 'success' : enrollment.status === 'IN_PROGRESS' ? 'info' : 'secondary'}>
                          {enrollment.status.replace('_', ' ')}
                        </Badge>
                      </TableCell>
                      <TableCell>{enrollment.enrolledAt}</TableCell>
                      <TableCell className="text-right">
                        <Button size="sm" variant="outline">View</Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="catalog" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Course Catalog</CardTitle>
              <CardDescription>Browse available training courses</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="grid gap-4 md:grid-cols-3">
                {[
                  { title: 'Leadership Fundamentals', category: 'Leadership', duration: 20, level: 'Beginner' },
                  { title: 'Advanced Project Management', category: 'Management', duration: 30, level: 'Advanced' },
                  { title: 'Data Analytics Bootcamp', category: 'Technical', duration: 40, level: 'Intermediate' },
                  { title: 'Communication Skills', category: 'Soft Skills', duration: 15, level: 'Beginner' },
                  { title: 'Cybersecurity Awareness', category: 'Compliance', duration: 8, level: 'Beginner' },
                  { title: 'Cloud Migration Strategy', category: 'Technical', duration: 25, level: 'Advanced' },
                ].map((course, i) => (
                  <div key={i} className="border rounded-lg p-4 hover:bg-slate-50 dark:hover:bg-slate-800 transition-colors cursor-pointer">
                    <Badge variant="outline" className="mb-2">{course.category}</Badge>
                    <h4 className="font-semibold mb-1">{course.title}</h4>
                    <div className="flex items-center gap-3 text-sm text-muted-foreground">
                      <span>{course.duration} hrs</span>
                      <span>{course.level}</span>
                    </div>
                    <Button size="sm" variant="outline" className="w-full mt-3">
                      Enroll
                    </Button>
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

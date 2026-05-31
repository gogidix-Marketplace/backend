import { useState } from 'react'
import {
  Briefcase,
  Plus,
  Search,
  Filter,
  Download,
  MapPin,
  Calendar,
  DollarSign,
  Users,
  ChevronRight,
  UserCheck,
  Clock,
  Eye,
} from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
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
import { cn, formatCurrency, formatDate } from '@shared/utils/cn'
import { jobRequisitions, candidates } from '@shared/data/mockData'

const stageConfig: Record<string, { label: string; variant: 'default' | 'success' | 'warning' | 'destructive' | 'secondary' | 'info' | 'teal' }> = {
  APPLIED: { label: 'Applied', variant: 'secondary' },
  SCREENING: { label: 'Screening', variant: 'info' },
  INTERVIEW: { label: 'Interview', variant: 'warning' },
  OFFER: { label: 'Offer', variant: 'teal' },
  HIRED: { label: 'Hired', variant: 'success' },
  REJECTED: { label: 'Rejected', variant: 'destructive' },
}

const requisitionStatusConfig: Record<string, { label: string; variant: 'default' | 'success' | 'warning' | 'destructive' | 'secondary' }> = {
  DRAFT: { label: 'Draft', variant: 'secondary' },
  PENDING_APPROVAL: { label: 'Pending Approval', variant: 'warning' },
  APPROVED: { label: 'Approved', variant: 'success' },
  FILLED: { label: 'Filled', variant: 'default' },
  CANCELLED: { label: 'Cancelled', variant: 'destructive' },
}

export default function RecruitmentPage() {
  const [searchQuery, setSearchQuery] = useState('')
  const [statusFilter, setStatusFilter] = useState<string>('all')

  const totalOpen = jobRequisitions.filter(j => j.status === 'APPROVED').length
  const totalCandidates = candidates.length
  const interviewStage = candidates.filter(c => c.stage === 'INTERVIEW').length
  const hiredThisMonth = candidates.filter(c => c.stage === 'HIRED').length

  const filteredJobs = jobRequisitions.filter(j =>
    statusFilter === 'all' || j.status === statusFilter
  )

  const filteredCandidates = candidates.filter(c =>
    c.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
    c.position.toLowerCase().includes(searchQuery.toLowerCase())
  )

  const pipelineStages = [
    { stage: 'APPLIED', count: candidates.filter(c => c.stage === 'APPLIED').length, color: 'bg-slate-500' },
    { stage: 'SCREENING', count: candidates.filter(c => c.stage === 'SCREENING').length, color: 'bg-blue-500' },
    { stage: 'INTERVIEW', count: candidates.filter(c => c.stage === 'INTERVIEW').length, color: 'bg-yellow-500' },
    { stage: 'OFFER', count: candidates.filter(c => c.stage === 'OFFER').length, color: 'bg-teal-500' },
    { stage: 'HIRED', count: candidates.filter(c => c.stage === 'HIRED').length, color: 'bg-green-500' },
    { stage: 'REJECTED', count: candidates.filter(c => c.stage === 'REJECTED').length, color: 'bg-red-500' },
  ]

  return (
    <div className="space-y-6">
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        <div className="page-header">
          <h1 className="page-title">Recruitment</h1>
          <p className="page-description">Manage job openings and candidate pipeline</p>
        </div>
        <div className="flex items-center gap-2">
          <Button variant="outline" size="sm">
            <Download className="mr-2 h-4 w-4" />
            Export
          </Button>
          <Button variant="hr" size="sm">
            <Plus className="mr-2 h-4 w-4" />
            New Requisition
          </Button>
        </div>
      </div>

      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-blue-100 p-2 dark:bg-blue-900/20">
                <Briefcase className="h-4 w-4 text-blue-600 dark:text-blue-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{totalOpen}</p>
                <p className="text-xs text-muted-foreground">Open Positions</p>
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
                <p className="text-2xl font-bold">{totalCandidates}</p>
                <p className="text-xs text-muted-foreground">Total Candidates</p>
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
                <p className="text-2xl font-bold">{interviewStage}</p>
                <p className="text-xs text-muted-foreground">In Interview</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-purple-100 p-2 dark:bg-purple-900/20">
                <UserCheck className="h-4 w-4 text-purple-600 dark:text-purple-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{hiredThisMonth}</p>
                <p className="text-xs text-muted-foreground">Hired This Month</p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      <Tabs defaultValue="pipeline" className="space-y-4">
        <TabsList>
          <TabsTrigger value="pipeline">Pipeline</TabsTrigger>
          <TabsTrigger value="requisitions">Job Requisitions</TabsTrigger>
          <TabsTrigger value="candidates">Candidates</TabsTrigger>
        </TabsList>

        <TabsContent value="pipeline" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Recruitment Pipeline</CardTitle>
              <CardDescription>Candidate progress through hiring stages</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="flex items-center gap-2 overflow-x-auto pb-2">
                {pipelineStages.map((stage, i) => (
                  <div key={stage.stage} className="flex items-center">
                    <div className="flex flex-col items-center min-w-[120px] p-4 rounded-lg border">
                      <div className={cn('h-8 w-8 rounded-full flex items-center justify-center text-white text-sm font-bold', stage.color)}>
                        {stage.count}
                      </div>
                      <span className="text-xs font-medium mt-2">{stage.stage}</span>
                    </div>
                    {i < pipelineStages.length - 1 && (
                      <ChevronRight className="h-4 w-4 text-muted-foreground mx-1 flex-shrink-0" />
                    )}
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Recent Candidates</CardTitle>
              <CardDescription>Latest applicants in the pipeline</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Candidate</TableHead>
                    <TableHead>Position</TableHead>
                    <TableHead>Department</TableHead>
                    <TableHead>Stage</TableHead>
                    <TableHead>Applied</TableHead>
                    <TableHead className="text-right">Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {candidates.map((candidate) => (
                    <TableRow key={candidate.id}>
                      <TableCell>
                        <div>
                          <p className="font-medium">{candidate.name}</p>
                          <p className="text-xs text-muted-foreground">{candidate.email}</p>
                        </div>
                      </TableCell>
                      <TableCell>{candidate.position}</TableCell>
                      <TableCell>{candidate.department}</TableCell>
                      <TableCell>
                        <Badge variant={stageConfig[candidate.stage]?.variant || 'secondary'}>
                          {stageConfig[candidate.stage]?.label || candidate.stage}
                        </Badge>
                      </TableCell>
                      <TableCell>{formatDate(candidate.appliedDate)}</TableCell>
                      <TableCell className="text-right">
                        <Button size="sm" variant="outline">
                          <Eye className="mr-2 h-3 w-3" />
                          Review
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="requisitions" className="space-y-4">
          <Card>
            <CardHeader>
              <div className="flex items-center justify-between">
                <div>
                  <CardTitle>Job Requisitions</CardTitle>
                  <CardDescription>{filteredJobs.length} open requisition(s)</CardDescription>
                </div>
                <Select value={statusFilter} onValueChange={setStatusFilter}>
                  <SelectTrigger className="w-[180px]">
                    <SelectValue placeholder="Filter status" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">All Statuses</SelectItem>
                    <SelectItem value="DRAFT">Draft</SelectItem>
                    <SelectItem value="PENDING_APPROVAL">Pending</SelectItem>
                    <SelectItem value="APPROVED">Approved</SelectItem>
                    <SelectItem value="FILLED">Filled</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Position</TableHead>
                    <TableHead>Department</TableHead>
                    <TableHead>Location</TableHead>
                    <TableHead>Type</TableHead>
                    <TableHead className="text-right">Salary Range</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Created</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {filteredJobs.map((job) => (
                    <TableRow key={job.id}>
                      <TableCell className="font-medium">{job.title}</TableCell>
                      <TableCell>{job.department}</TableCell>
                      <TableCell>
                        <div className="flex items-center gap-1 text-sm">
                          <MapPin className="h-3 w-3 text-muted-foreground" />
                          {job.location}
                        </div>
                      </TableCell>
                      <TableCell>
                        <Badge variant="outline">{job.type.replace('_', ' ')}</Badge>
                      </TableCell>
                      <TableCell className="text-right font-mono text-sm">
                        {formatCurrency(job.salaryMin, 'USD')} - {formatCurrency(job.salaryMax, 'USD')}
                      </TableCell>
                      <TableCell>
                        <Badge variant={requisitionStatusConfig[job.status]?.variant || 'secondary'}>
                          {requisitionStatusConfig[job.status]?.label || job.status}
                        </Badge>
                      </TableCell>
                      <TableCell>{formatDate(job.createdAt)}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="candidates" className="space-y-4">
          <Card>
            <CardContent className="p-4">
              <div className="flex flex-col md:flex-row gap-4">
                <div className="relative flex-1">
                  <Search className="absolute left-3 top-3 h-4 w-4 text-muted-foreground" />
                  <Input
                    placeholder="Search candidates by name or position..."
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
              <CardTitle>All Candidates</CardTitle>
              <CardDescription>{filteredCandidates.length} candidate(s) found</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Name</TableHead>
                    <TableHead>Email</TableHead>
                    <TableHead>Position</TableHead>
                    <TableHead>Stage</TableHead>
                    <TableHead>Notes</TableHead>
                    <TableHead>Applied</TableHead>
                    <TableHead className="text-right">Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {filteredCandidates.map((c) => (
                    <TableRow key={c.id}>
                      <TableCell className="font-medium">{c.name}</TableCell>
                      <TableCell className="text-sm text-muted-foreground">{c.email}</TableCell>
                      <TableCell>{c.position}</TableCell>
                      <TableCell>
                        <Badge variant={stageConfig[c.stage]?.variant || 'secondary'}>
                          {stageConfig[c.stage]?.label || c.stage}
                        </Badge>
                      </TableCell>
                      <TableCell className="max-w-[200px] truncate text-sm text-muted-foreground">
                        {c.notes}
                      </TableCell>
                      <TableCell>{formatDate(c.appliedDate)}</TableCell>
                      <TableCell className="text-right">
                        <Button size="sm" variant="outline">
                          <Eye className="mr-2 h-3 w-3" />
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
      </Tabs>
    </div>
  )
}

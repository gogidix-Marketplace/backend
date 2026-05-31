import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@shared/components/ui/tabs'
import { Input } from '@shared/components/ui/input'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@shared/components/ui/select'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@shared/components/ui/table'
import { cn, formatDateTime } from '@shared/utils/cn'
import { FileText, Download, Calendar, Search, Plus, FileSpreadsheet, Presentation } from 'lucide-react'
import { useState } from 'react'

// Mock data
const executiveReports = [
  {
    id: '1',
    name: 'Q1 2024 Executive Summary',
    type: 'executive',
    description: 'Comprehensive summary of Q1 performance across all domains',
    generatedBy: 'System',
    generatedAt: '2024-04-01T08:00:00Z',
    format: 'pdf',
    size: 2450000,
    downloadUrl: '/reports/q1-2024-executive-summary.pdf',
  },
  {
    id: '2',
    name: 'February 2024 Performance Report',
    type: 'executive',
    description: 'Monthly performance analysis and KPI tracking',
    generatedBy: 'John Mitchell',
    generatedAt: '2024-03-01T08:00:00Z',
    format: 'pdf',
    size: 1850000,
    downloadUrl: '/reports/feb-2024-performance.pdf',
  },
  {
    id: '3',
    name: 'Strategic Initiatives Status Report',
    type: 'executive',
    description: 'Current status of all strategic initiatives',
    generatedBy: 'System',
    generatedAt: '2024-02-28T08:00:00Z',
    format: 'pdf',
    size: 1520000,
    downloadUrl: '/reports/strategic-initiatives-status.pdf',
  },
]

const boardPresentations = [
  {
    id: '101',
    name: 'Q1 2024 Board Presentation',
    type: 'board',
    description: 'Quarterly board meeting presentation materials',
    generatedBy: 'John Mitchell',
    generatedAt: '2024-03-25T08:00:00Z',
    format: 'powerpoint',
    size: 8500000,
    downloadUrl: '/reports/q1-2024-board-presentation.pptx',
  },
  {
    id: '102',
    name: 'Annual Strategic Plan 2024',
    type: 'board',
    description: 'Annual strategic plan and objectives',
    generatedBy: 'Executive Office',
    generatedAt: '2024-01-15T08:00:00Z',
    format: 'powerpoint',
    size: 12000000,
    downloadUrl: '/reports/annual-strategic-plan-2024.pptx',
  },
]

const scheduledReports = [
  {
    id: '201',
    name: 'Monthly Executive Summary',
    frequency: 'monthly',
    nextRunDate: '2024-04-01T08:00:00Z',
    format: 'pdf',
    recipients: ['board@gogidix.com', 'executives@gogidix.com'],
    status: 'active',
  },
  {
    id: '202',
    name: 'Weekly KPI Dashboard',
    frequency: 'weekly',
    nextRunDate: '2024-03-11T08:00:00Z',
    format: 'excel',
    recipients: ['executives@gogidix.com'],
    status: 'active',
  },
  {
    id: '203',
    name: 'Quarterly Financial Report',
    frequency: 'quarterly',
    nextRunDate: '2024-04-15T08:00:00Z',
    format: 'pdf',
    recipients: ['board@gogidix.com'],
    status: 'active',
  },
]

const reportTemplates = [
  {
    id: '301',
    name: 'Executive Summary',
    description: 'High-level overview of company performance',
    type: 'executive',
    icon: 'FileText',
  },
  {
    id: '302',
    name: 'Financial Report',
    description: 'Detailed financial analysis and metrics',
    type: 'financial',
    icon: 'FileSpreadsheet',
  },
  {
    id: '303',
    name: 'Board Presentation',
    description: 'Presentation materials for board meetings',
    type: 'board',
    icon: 'Presentation',
  },
  {
    id: '304',
    name: 'Domain Performance',
    description: 'Performance analysis by department/domain',
    type: 'operational',
    icon: 'FileText',
  },
  {
    id: '305',
    name: 'Custom Report',
    description: 'Create a custom report with selected metrics',
    type: 'custom',
    icon: 'FileText',
  },
]

function getFormatIcon(format: string) {
  switch (format) {
    case 'pdf':
      return <FileText className="h-5 w-5 text-red-500" />
    case 'excel':
      return <FileSpreadsheet className="h-5 w-5 text-green-500" />
    case 'powerpoint':
      return <Presentation className="h-5 w-5 text-orange-500" />
    default:
      return <FileText className="h-5 w-5" />
  }
}

function formatFileSize(bytes: number): string {
  if (bytes < 1000000) return `${(bytes / 1000).toFixed(1)} KB`
  return `${(bytes / 1000000).toFixed(1)} MB`
}

export default function ReportsPage() {
  const [searchQuery, setSearchQuery] = useState('')
  const [selectedType, setSelectedType] = useState<string>('all')

  const filteredReports = executiveReports.filter((report) => {
    const matchesSearch =
      report.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      report.description.toLowerCase().includes(searchQuery.toLowerCase())
    const matchesType = selectedType === 'all' || report.type === selectedType
    return matchesSearch && matchesType
  })

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div className="page-header">
          <h1 className="page-title">Reports</h1>
          <p className="page-description">
            Executive reports, board presentations, and scheduled reports
          </p>
        </div>
        <Button className="executive-gold-bg text-white">
          <Plus className="mr-2 h-4 w-4" />
          Create Report
        </Button>
      </div>

      {/* Tabs */}
      <Tabs defaultValue="executive" className="space-y-6">
        <TabsList>
          <TabsTrigger value="executive">Executive Reports</TabsTrigger>
          <TabsTrigger value="board">Board Presentations</TabsTrigger>
          <TabsTrigger value="scheduled">Scheduled Reports</TabsTrigger>
          <TabsTrigger value="templates">Templates</TabsTrigger>
        </TabsList>

        {/* Executive Reports */}
        <TabsContent value="executive" className="space-y-4">
          {/* Search and Filter */}
          <Card>
            <CardContent className="pt-6">
              <div className="flex flex-wrap items-center gap-4">
                <div className="relative flex-1 min-w-[300px]">
                  <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                  <Input
                    placeholder="Search reports..."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    className="pl-9"
                  />
                </div>
                <Select value={selectedType} onValueChange={setSelectedType}>
                  <SelectTrigger className="w-[180px]">
                    <SelectValue placeholder="Report Type" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">All Types</SelectItem>
                    <SelectItem value="executive">Executive</SelectItem>
                    <SelectItem value="financial">Financial</SelectItem>
                    <SelectItem value="operational">Operational</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </CardContent>
          </Card>

          {/* Reports List */}
          <div className="grid gap-4">
            {filteredReports.map((report) => (
              <Card key={report.id} className="hover:shadow-md transition-shadow">
                <CardContent className="p-6">
                  <div className="flex items-start justify-between">
                    <div className="flex items-start gap-4">
                      <div className="mt-1">{getFormatIcon(report.format)}</div>
                      <div className="space-y-1">
                        <h3 className="font-semibold text-lg">{report.name}</h3>
                        <p className="text-sm text-muted-foreground">
                          {report.description}
                        </p>
                        <div className="flex flex-wrap items-center gap-4 text-sm text-muted-foreground">
                          <span>Generated by {report.generatedBy}</span>
                          <span>•</span>
                          <span>{formatDateTime(report.generatedAt)}</span>
                          <span>•</span>
                          <span>{formatFileSize(report.size)}</span>
                        </div>
                      </div>
                    </div>
                    <Button variant="outline" size="sm">
                      <Download className="mr-2 h-4 w-4" />
                      Download
                    </Button>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </TabsContent>

        {/* Board Presentations */}
        <TabsContent value="board" className="space-y-4">
          <div className="grid gap-4">
            {boardPresentations.map((presentation) => (
              <Card key={presentation.id} className="hover:shadow-md transition-shadow">
                <CardContent className="p-6">
                  <div className="flex items-start justify-between">
                    <div className="flex items-start gap-4">
                      <div className="mt-1">{getFormatIcon(presentation.format)}</div>
                      <div className="space-y-1">
                        <h3 className="font-semibold text-lg">{presentation.name}</h3>
                        <p className="text-sm text-muted-foreground">
                          {presentation.description}
                        </p>
                        <div className="flex flex-wrap items-center gap-4 text-sm text-muted-foreground">
                          <span>Generated by {presentation.generatedBy}</span>
                          <span>•</span>
                          <span>{formatDateTime(presentation.generatedAt)}</span>
                          <span>•</span>
                          <span>{formatFileSize(presentation.size)}</span>
                        </div>
                      </div>
                    </div>
                    <Button variant="outline" size="sm">
                      <Download className="mr-2 h-4 w-4" />
                      Download
                    </Button>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </TabsContent>

        {/* Scheduled Reports */}
        <TabsContent value="scheduled">
          <Card>
            <CardHeader>
              <div className="flex items-center justify-between">
                <div>
                  <CardTitle>Scheduled Reports</CardTitle>
                  <CardDescription>Automatically generated and delivered reports</CardDescription>
                </div>
                <Button variant="outline" size="sm">
                  <Plus className="mr-2 h-4 w-4" />
                  Add Schedule
                </Button>
              </div>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Report Name</TableHead>
                    <TableHead>Frequency</TableHead>
                    <TableHead>Next Run</TableHead>
                    <TableHead>Format</TableHead>
                    <TableHead>Recipients</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {scheduledReports.map((report) => (
                    <TableRow key={report.id}>
                      <TableCell className="font-medium">{report.name}</TableCell>
                      <TableCell className="capitalize">{report.frequency}</TableCell>
                      <TableCell>{formatDateTime(report.nextRunDate)}</TableCell>
                      <TableCell className="uppercase">{report.format}</TableCell>
                      <TableCell className="max-w-xs truncate">
                        {report.recipients.join(', ')}
                      </TableCell>
                      <TableCell>
                        <Badge variant={report.status === 'active' ? 'success' : 'secondary'}>
                          {report.status}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        <div className="flex gap-2">
                          <Button variant="ghost" size="sm">Edit</Button>
                          <Button variant="ghost" size="sm" className="text-destructive">
                            Delete
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Templates */}
        <TabsContent value="templates">
          <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
            {reportTemplates.map((template) => {
              const Icon = template.icon === 'FileSpreadsheet' ? FileSpreadsheet :
                           template.icon === 'Presentation' ? Presentation : FileText
              return (
                <Card key={template.id} className="hover:shadow-md transition-shadow cursor-pointer">
                  <CardHeader>
                    <div className="flex items-center gap-3">
                      <div className="flex h-12 w-12 items-center justify-center rounded-lg bg-slate-100 dark:bg-slate-800">
                        <Icon className="h-6 w-6 text-[#0D47A1]" />
                      </div>
                      <div>
                        <CardTitle className="text-base">{template.name}</CardTitle>
                        <Badge variant="outline" className="mt-1">
                          {template.type}
                        </Badge>
                      </div>
                    </div>
                  </CardHeader>
                  <CardContent>
                    <p className="text-sm text-muted-foreground">{template.description}</p>
                    <Button variant="outline" className="mt-4 w-full" size="sm">
                      Use Template
                    </Button>
                  </CardContent>
                </Card>
              )
            })}
          </div>
        </TabsContent>
      </Tabs>
    </div>
  )
}

import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@shared/components/ui/tabs'
import { FileText, Calendar, Download, Share, Plus, Search, Filter } from 'lucide-react'
import { cn } from '@shared/utils/cn'

/**
 * CEO Reports Page
 *
 * Components:
 * - ReportLibrary - Executive reports catalog
 * - ReportScheduler - Automated report generation
 * - ReportViewer - Interactive report display
 */

type ReportCategory = 'executive' | 'board' | 'financial' | 'operational' | 'custom'
type ReportFormat = 'pdf' | 'excel' | 'powerpoint' | 'web'
type ReportStatus = 'ready' | 'generating' | 'scheduled'

interface Report {
  id: string
  name: string
  category: ReportCategory
  description: string
  lastGenerated: string
  format: ReportFormat[]
  status: ReportStatus
  size?: string
  scheduled?: boolean
  scheduleFrequency?: 'daily' | 'weekly' | 'monthly' | 'quarterly'
}

interface ScheduledReport {
  id: string
  reportId: string
  reportName: string
  frequency: 'daily' | 'weekly' | 'monthly' | 'quarterly'
  recipients: string[]
  nextRun: string
  formats: ReportFormat[]
  enabled: boolean
}

const mockReports: Report[] = [
  {
    id: '1',
    name: 'Monthly Executive Summary',
    category: 'executive',
    description: 'Comprehensive overview of all business metrics and strategic initiatives',
    lastGenerated: '2024-03-01',
    format: ['pdf', 'powerpoint'],
    status: 'ready',
    size: '2.4 MB',
    scheduled: true,
    scheduleFrequency: 'monthly',
  },
  {
    id: '2',
    name: 'Board Presentation - Q1 2026',
    category: 'board',
    description: 'Quarterly board meeting presentation with financial highlights',
    lastGenerated: '2024-02-28',
    format: ['powerpoint', 'pdf'],
    status: 'ready',
    size: '5.8 MB',
  },
  {
    id: '3',
    name: 'Financial Consolidation Report',
    category: 'financial',
    description: 'Consolidated financial statements across all domains and regions',
    lastGenerated: '2024-03-01',
    format: ['pdf', 'excel'],
    status: 'ready',
    size: '1.2 MB',
    scheduled: true,
    scheduleFrequency: 'monthly',
  },
  {
    id: '4',
    name: 'Operational Performance Dashboard',
    category: 'operational',
    description: 'Real-time operational metrics and KPIs',
    lastGenerated: '2024-03-01',
    format: ['web', 'pdf'],
    status: 'ready',
    scheduled: true,
    scheduleFrequency: 'daily',
  },
  {
    id: '5',
    name: 'Regional Performance Analysis',
    category: 'executive',
    description: 'Detailed analysis of performance by geographic region',
    lastGenerated: '2024-02-25',
    format: ['pdf', 'excel'],
    status: 'ready',
    size: '3.1 MB',
  },
]

const mockScheduledReports: ScheduledReport[] = [
  {
    id: '1',
    reportId: '1',
    reportName: 'Monthly Executive Summary',
    frequency: 'monthly',
    recipients: ['board@gogidix.com', 'executive@gogidix.com'],
    nextRun: '2024-04-01',
    formats: ['pdf', 'powerpoint'],
    enabled: true,
  },
  {
    id: '2',
    reportId: '3',
    reportName: 'Financial Consolidation Report',
    frequency: 'monthly',
    recipients: ['cfo@gogidix.com', 'ceo@gogidix.com'],
    nextRun: '2024-04-01',
    formats: ['pdf', 'excel'],
    enabled: true,
  },
  {
    id: '3',
    reportId: '4',
    reportName: 'Operational Performance Dashboard',
    frequency: 'daily',
    recipients: ['executive@gogidix.com'],
    nextRun: '2024-03-02',
    formats: ['web'],
    enabled: true,
  },
]

export default function ReportsPage() {
  const [searchQuery, setSearchQuery] = useState('')
  const [selectedCategory, setSelectedCategory] = useState<ReportCategory | 'all'>('all')
  const [activeTab, setActiveTab] = useState('library')

  const filteredReports = mockReports.filter(report => {
    const matchesSearch = report.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      report.description.toLowerCase().includes(searchQuery.toLowerCase())
    const matchesCategory = selectedCategory === 'all' || report.category === selectedCategory
    return matchesSearch && matchesCategory
  })

  const categoryColors: Record<ReportCategory, string> = {
    executive: 'bg-purple-100 text-purple-700 dark:bg-purple-900/20 dark:text-purple-400',
    board: 'bg-blue-100 text-blue-700 dark:bg-blue-900/20 dark:text-blue-400',
    financial: 'bg-green-100 text-green-700 dark:bg-green-900/20 dark:text-green-400',
    operational: 'bg-amber-100 text-amber-700 dark:bg-amber-900/20 dark:text-amber-400',
    custom: 'bg-slate-100 text-slate-700 dark:bg-slate-900/20 dark:text-slate-400',
  }

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header">
        <h1 className="page-title">Reports</h1>
        <p className="page-description">
          Access executive reports, board presentations, and scheduled reports
        </p>
      </div>

      <Tabs value={activeTab} onValueChange={setActiveTab} className="space-y-6">
        <TabsList>
          <TabsTrigger value="library">Report Library</TabsTrigger>
          <TabsTrigger value="scheduled">Scheduled Reports</TabsTrigger>
          <TabsTrigger value="custom">Custom Reports</TabsTrigger>
        </TabsList>

        {/* Report Library */}
        <TabsContent value="library" className="space-y-6">
          {/* Search and Filter */}
          <div className="flex items-center gap-4">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
              <Input
                placeholder="Search reports..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-9"
              />
            </div>
            <div className="flex gap-2">
              {(['all', 'executive', 'board', 'financial', 'operational'] as const).map((cat) => (
                <Button
                  key={cat}
                  variant={selectedCategory === cat ? 'default' : 'outline'}
                  size="sm"
                  onClick={() => setSelectedCategory(cat)}
                >
                  {cat === 'all' ? 'All' : cat.charAt(0).toUpperCase() + cat.slice(1)}
                </Button>
              ))}
            </div>
          </div>

          {/* Reports Grid */}
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
            {filteredReports.map((report) => (
              <Card key={report.id} className="hover:shadow-md transition-shadow">
                <CardHeader>
                  <div className="flex items-start justify-between">
                    <div className="flex-1">
                      <div className="flex items-center gap-2 mb-2">
                        <FileText className="h-5 w-5 text-muted-foreground" />
                        <CardTitle className="text-base">{report.name}</CardTitle>
                      </div>
                      <CardDescription className="line-clamp-2">
                        {report.description}
                      </CardDescription>
                    </div>
                  </div>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div className="flex items-center gap-2 flex-wrap">
                    <Badge className={categoryColors[report.category]}>
                      {report.category}
                    </Badge>
                    {report.scheduled && (
                      <Badge variant="outline" className="gap-1">
                        <Calendar className="h-3 w-3" />
                        {report.scheduleFrequency}
                      </Badge>
                    )}
                  </div>

                  <div className="text-sm text-muted-foreground space-y-1">
                    <div className="flex justify-between">
                      <span>Last generated:</span>
                      <span>{new Date(report.lastGenerated).toLocaleDateString()}</span>
                    </div>
                    {report.size && (
                      <div className="flex justify-between">
                        <span>Size:</span>
                        <span>{report.size}</span>
                      </div>
                    )}
                  </div>

                  <div className="flex gap-2">
                    {report.format.map((format) => (
                      <Badge key={format} variant="secondary" className="text-xs">
                        {format.toUpperCase()}
                      </Badge>
                    ))}
                  </div>

                  <div className="flex gap-2">
                    <Button variant="outline" size="sm" className="flex-1">
                      <Download className="h-3 w-3 mr-1" />
                      Download
                    </Button>
                    <Button variant="outline" size="sm" className="flex-1">
                      <Share className="h-3 w-3 mr-1" />
                      Share
                    </Button>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </TabsContent>

        {/* Scheduled Reports */}
        <TabsContent value="scheduled" className="space-y-6">
          <div className="flex items-center justify-between">
            <div>
              <h2 className="text-xl font-semibold">Scheduled Reports</h2>
              <p className="text-sm text-muted-foreground">
                Configure automated report generation and delivery
              </p>
            </div>
            <Button className="gap-2">
              <Plus className="h-4 w-4" />
              New Schedule
            </Button>
          </div>

          <div className="space-y-4">
            {mockScheduledReports.map((scheduled) => (
              <Card key={scheduled.id}>
                <CardContent className="p-4">
                  <div className="flex items-center justify-between">
                    <div className="flex-1">
                      <div className="flex items-center gap-3">
                        <FileText className="h-5 w-5 text-muted-foreground" />
                        <div>
                          <h3 className="font-medium">{scheduled.reportName}</h3>
                          <p className="text-sm text-muted-foreground">
                            Next run: {new Date(scheduled.nextRun).toLocaleDateString()}
                          </p>
                        </div>
                      </div>
                      <div className="flex items-center gap-4 mt-2 text-sm">
                        <span>
                          <span className="text-muted-foreground">Frequency:</span>{' '}
                          <Badge variant="outline">{scheduled.frequency}</Badge>
                        </span>
                        <span>
                          <span className="text-muted-foreground">Formats:</span>{' '}
                          {scheduled.formats.map(f => (
                            <Badge key={f} variant="secondary" className="text-xs ml-1">
                              {f.toUpperCase()}
                            </Badge>
                          ))}
                        </span>
                        <span>
                          <span className="text-muted-foreground">Recipients:</span>{' '}
                          {scheduled.recipients.length}
                        </span>
                      </div>
                    </div>
                    <div className="flex items-center gap-2">
                      <Button variant="outline" size="sm">
                        <Filter className="h-4 w-4 mr-1" />
                        Edit
                      </Button>
                      <Button variant="outline" size="sm">
                        <Download className="h-4 w-4 mr-1" />
                        Run Now
                      </Button>
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        </TabsContent>

        {/* Custom Reports */}
        <TabsContent value="custom" className="space-y-6">
          <Card>
            <CardContent className="flex flex-col items-center justify-center py-12">
              <FileText className="h-12 w-12 text-muted-foreground mb-4" />
              <h3 className="text-lg font-semibold mb-2">Create Custom Report</h3>
              <p className="text-sm text-muted-foreground text-center mb-6 max-w-md">
                Build custom reports by selecting data sources, metrics, and visualization formats.
              </p>
              <Button className="gap-2">
                <Plus className="h-4 w-4" />
                Create New Report
              </Button>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}

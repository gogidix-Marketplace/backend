import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@shared/components/ui/table'
import { cn, formatDate } from '@shared/utils/cn'
import { gbmReportsData, reportTemplatesData } from '@shared/mock-data'
import { FileText, Download, Calendar, Filter, Plus, Clock, File, Search } from 'lucide-react'

export default function ReportsPage() {
  const [viewMode, setViewMode] = useState<'list' | 'create' | 'templates'>('list')
  const [selectedTemplate, setSelectedTemplate] = useState<string | null>(null)

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header flex items-center justify-between">
        <div>
          <h1 className="page-title">Reports</h1>
          <p className="page-description">
            Generate, schedule, and manage business reports
          </p>
        </div>
        <div className="flex gap-2">
          <Button variant="outline" onClick={() => setViewMode('templates')}>
            Templates
          </Button>
          <Button className="gap-2" onClick={() => setViewMode('create')}>
            <Plus className="h-4 w-4" />
            Create Report
          </Button>
        </div>
      </div>

      {viewMode === 'list' && (
        <>
          {/* Summary Cards */}
          <div className="grid gap-4 md:grid-cols-4">
            <Card>
              <CardHeader className="pb-3">
                <CardTitle className="text-sm font-medium">Total Reports</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{gbmReportsData.length}</div>
                <p className="text-xs text-muted-foreground">generated reports</p>
              </CardContent>
            </Card>
            <Card>
              <CardHeader className="pb-3">
                <CardTitle className="text-sm font-medium">Scheduled</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{gbmReportsData.filter(r => r.schedule).length}</div>
                <p className="text-xs text-muted-foreground">automatic reports</p>
              </CardContent>
            </Card>
            <Card>
              <CardHeader className="pb-3">
                <CardTitle className="text-sm font-medium">Templates</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{reportTemplatesData.length}</div>
                <p className="text-xs text-muted-foreground">available</p>
              </CardContent>
            </Card>
            <Card>
              <CardHeader className="pb-3">
                <CardTitle className="text-sm font-medium">Downloads</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">1.2K</div>
                <p className="text-xs text-muted-foreground">this month</p>
              </CardContent>
            </Card>
          </div>

          {/* Filter Bar */}
          <Card>
            <CardContent className="pt-6">
              <div className="flex items-center gap-4">
                <div className="relative flex-1">
                  <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
                  <Input
                    type="search"
                    placeholder="Search reports..."
                    className="pl-9"
                  />
                </div>
                <Button variant="outline">
                  <Filter className="mr-2 h-4 w-4" />
                  Filter
                </Button>
              </div>
            </CardContent>
          </Card>

          {/* Reports Table */}
          <Card>
            <CardHeader>
              <CardTitle>Recent Reports</CardTitle>
              <CardDescription>Generated and scheduled reports</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Report Name</TableHead>
                    <TableHead>Type</TableHead>
                    <TableHead>Format</TableHead>
                    <TableHead>Created By</TableHead>
                    <TableHead>Created</TableHead>
                    <TableHead>Schedule</TableHead>
                    <TableHead>Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {gbmReportsData.map((report) => (
                    <TableRow key={report.id}>
                      <TableCell>
                        <div className="flex items-center gap-2">
                          <FileText className="h-4 w-4 text-muted-foreground" />
                          <span className="font-medium">{report.name}</span>
                        </div>
                      </TableCell>
                      <TableCell>
                        <Badge variant="outline">{report.type}</Badge>
                      </TableCell>
                      <TableCell>
                        <Badge variant="secondary">{report.format.toUpperCase()}</Badge>
                      </TableCell>
                      <TableCell>{report.createdBy}</TableCell>
                      <TableCell>{formatDate(report.createdAt)}</TableCell>
                      <TableCell>
                        {report.schedule ? (
                          <Badge variant="success">
                            <Clock className="inline h-3 w-3 mr-1" />
                            {report.schedule.frequency}
                          </Badge>
                        ) : (
                          <span className="text-muted-foreground">-</span>
                        )}
                      </TableCell>
                      <TableCell>
                        <div className="flex items-center gap-2">
                          <Button variant="ghost" size="icon">
                            <Download className="h-4 w-4" />
                          </Button>
                          <Button variant="ghost" size="icon">
                            <Calendar className="h-4 w-4" />
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </>
      )}

      {viewMode === 'templates' && (
        <>
          <Card>
            <CardHeader>
              <CardTitle>Report Templates</CardTitle>
              <CardDescription>Pre-configured templates for common reports</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
                {reportTemplatesData.map((template) => (
                  <Card key={template.id} className="cursor-pointer hover:border-[#1E88E5]" onClick={() => {
                    setSelectedTemplate(template.id)
                    setViewMode('create')
                  }}>
                    <CardHeader>
                      <CardTitle className="text-base">{template.name}</CardTitle>
                      <CardDescription>{template.description}</CardDescription>
                    </CardHeader>
                    <CardContent>
                      <div className="flex items-center justify-between">
                        <Badge variant="outline">{template.category}</Badge>
                        <Button size="sm" variant="gbm">Use Template</Button>
                      </div>
                    </CardContent>
                  </Card>
                ))}
              </div>
            </CardContent>
          </Card>
        </>
      )}

      {viewMode === 'create' && (
        <>
          <Card>
            <CardHeader>
              <CardTitle>Create New Report</CardTitle>
              <CardDescription>Configure and generate a custom report</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="grid gap-2">
                <Label>Report Name</Label>
                <Input placeholder="Enter report name" />
              </div>
              <div className="grid gap-2">
                <Label>Report Type</Label>
                <select className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm">
                  <option>Executive</option>
                  <option>Regional</option>
                  <option>Country</option>
                  <option>Currency</option>
                  <option>Custom</option>
                </select>
              </div>
              <div className="grid gap-2">
                <Label>Format</Label>
                <select className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm">
                  <option>PDF</option>
                  <option>Excel</option>
                  <option>PowerPoint</option>
                  <option>CSV</option>
                </select>
              </div>
              <div className="grid gap-2">
                <Label>Data Range</Label>
                <select className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm">
                  <option>Last 7 days</option>
                  <option>Last 30 days</option>
                  <option>Last 90 days</option>
                  <option>This quarter</option>
                  <option>Custom range</option>
                </select>
              </div>
              <div className="grid gap-2">
                <Label>Regions</Label>
                <select className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm">
                  <option>All regions</option>
                  <option>North America</option>
                  <option>Europe</option>
                  <option>Africa</option>
                </select>
              </div>
              <div className="border-t pt-4">
                <Label className="mb-2 block">Schedule (Optional)</Label>
                <div className="grid gap-4 md:grid-cols-3">
                  <select className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm">
                    <option>One-time</option>
                    <option>Daily</option>
                    <option>Weekly</option>
                    <option>Monthly</option>
                  </select>
                  <Input type="time" defaultValue="09:00" />
                  <Input placeholder="Recipients" />
                </div>
              </div>
              <div className="flex justify-end gap-4 pt-4">
                <Button variant="outline" onClick={() => setViewMode('list')}>Cancel</Button>
                <Button>Generate Report</Button>
              </div>
            </CardContent>
          </Card>
        </>
      )}
    </div>
  )
}

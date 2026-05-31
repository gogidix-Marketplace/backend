import { useState } from 'react'
import { FileCheck, Search, Download, AlertCircle, CheckCircle2 } from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Progress } from '@shared/components/ui/progress'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@shared/components/ui/table'
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from '@shared/components/ui/tabs'
import { cn } from '@shared/utils/cn'
import { mockComplianceReports } from '@shared/data/mockData'

export default function CompliancePage() {
  const [searchQuery, setSearchQuery] = useState('')
  const [selectedTab, setSelectedTab] = useState('overview')

  const frameworkConfig = {
    SOC2: { label: 'SOC 2 Type II', color: 'bg-blue-500' },
    ISO27001: { label: 'ISO 27001', color: 'bg-green-500' },
    GDPR: { label: 'GDPR', color: 'bg-purple-500' },
    HIPAA: { label: 'HIPAA', color: 'bg-orange-500' },
    PCI_DSS: { label: 'PCI DSS', color: 'bg-red-500' },
  }

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div className="page-header">
          <h1 className="page-title">Compliance & Audits</h1>
          <p className="page-description">
            Monitor compliance status and manage audit requirements
          </p>
        </div>
        <div className="flex gap-2">
          <Button variant="outline">
            <Download className="mr-2 h-4 w-4" />
            Export Reports
          </Button>
          <Button variant="admin">
            Schedule Audit
          </Button>
        </div>
      </div>

      {/* Compliance Score */}
      <Card className="bg-gradient-to-r from-green-500 to-emerald-600 text-white">
        <CardContent className="p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-white/80">Overall Compliance Score</p>
              <div className="flex items-baseline gap-2">
                <span className="text-4xl font-bold">94</span>
                <span className="text-lg text-white/80">/100</span>
                <Badge className="ml-2 bg-white/20 text-white hover:bg-white/30">
                  Compliant
                </Badge>
              </div>
              <p className="mt-2 text-sm text-white/70">
                {mockComplianceReports.filter(r => r.status === 'compliant').length} of {mockComplianceReports.length} frameworks compliant
              </p>
            </div>
            <div className="hidden md:block">
              <div className="flex h-20 w-20 items-center justify-center rounded-full bg-white/20">
                <FileCheck className="h-10 w-10" />
              </div>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Framework Stats */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>SOC 2</CardDescription>
            <CardTitle className="text-2xl text-green-600">98%</CardTitle>
            <Progress value={98} className="h-1.5 mt-2" />
          </CardHeader>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>ISO 27001</CardDescription>
            <CardTitle className="text-2xl text-green-600">95%</CardTitle>
            <Progress value={95} className="h-1.5 mt-2" />
          </CardHeader>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>GDPR</CardDescription>
            <CardTitle className="text-2xl text-yellow-600">87%</CardTitle>
            <Progress value={87} className="h-1.5 mt-2" />
          </CardHeader>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>HIPAA</CardDescription>
            <CardTitle className="text-2xl text-green-600">96%</CardTitle>
            <Progress value={96} className="h-1.5 mt-2" />
          </CardHeader>
        </Card>
      </div>

      {/* Search */}
      <div className="relative">
        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
        <Input
          placeholder="Search compliance reports, findings, or controls..."
          className="pl-9"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </div>

      {/* Tabs */}
      <Tabs value={selectedTab} onValueChange={setSelectedTab}>
        <TabsList>
          <TabsTrigger value="overview">Overview</TabsTrigger>
          <TabsTrigger value="frameworks">Frameworks</TabsTrigger>
          <TabsTrigger value="findings">Findings</TabsTrigger>
          <TabsTrigger value="controls">Controls</TabsTrigger>
        </TabsList>

        {/* Overview Tab */}
        <TabsContent value="overview">
          <Card>
            <CardHeader>
              <CardTitle>Compliance Frameworks</CardTitle>
              <CardDescription>Status of all compliance frameworks</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Framework</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Score</TableHead>
                    <TableHead>Last Audit</TableHead>
                    <TableHead>Next Audit</TableHead>
                    <TableHead>Open Findings</TableHead>
                    <TableHead className="text-right">Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {mockComplianceReports.map((report) => (
                    <TableRow key={report.id}>
                      <TableCell className="font-medium">{report.name}</TableCell>
                      <TableCell>
                        <Badge
                          variant={report.status === 'compliant' ? 'success' : report.status === 'non_compliant' ? 'destructive' : 'warning'}
                          className="capitalize"
                        >
                          {report.status.replace('_', ' ')}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        <div className="flex items-center gap-2">
                          <span className="font-medium">{report.score}%</span>
                          <Progress value={report.score} className="h-1.5 w-16" />
                        </div>
                      </TableCell>
                      <TableCell>{new Date(report.lastAudit).toLocaleDateString()}</TableCell>
                      <TableCell>{new Date(report.nextAudit).toLocaleDateString()}</TableCell>
                      <TableCell>
                        {report.findings.length > 0 ? (
                          <Badge variant="warning">{report.findings.length} open</Badge>
                        ) : (
                          <Badge variant="success">None</Badge>
                        )}
                      </TableCell>
                      <TableCell className="text-right">
                        <Button variant="ghost" size="sm">View Details</Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Frameworks Tab */}
        <TabsContent value="frameworks">
          <div className="grid gap-6 md:grid-cols-2">
            {mockComplianceReports.map((report) => (
              <Card key={report.id}>
                <CardHeader>
                  <div className="flex items-center justify-between">
                    <div>
                      <CardTitle>{report.name}</CardTitle>
                      <CardDescription>{frameworkConfig[report.framework].label}</CardDescription>
                    </div>
                    <Badge
                      variant={report.status === 'compliant' ? 'success' : 'warning'}
                      className="capitalize"
                    >
                      {report.status.replace('_', ' ')}
                    </Badge>
                  </div>
                </CardHeader>
                <CardContent className="space-y-4">
                  <div>
                    <div className="flex items-center justify-between text-sm mb-2">
                      <span>Compliance Score</span>
                      <span className="font-medium">{report.score}%</span>
                    </div>
                    <Progress value={report.score} />
                  </div>
                  <div className="grid grid-cols-2 gap-4 text-sm">
                    <div>
                      <p className="text-muted-foreground">Last Audit</p>
                      <p className="font-medium">{new Date(report.lastAudit).toLocaleDateString()}</p>
                    </div>
                    <div>
                      <p className="text-muted-foreground">Next Audit</p>
                      <p className="font-medium">{new Date(report.nextAudit).toLocaleDateString()}</p>
                    </div>
                  </div>
                  {report.findings.length > 0 && (
                    <div>
                      <p className="text-sm font-medium mb-2">Open Findings</p>
                      <div className="space-y-2">
                        {report.findings.map((finding) => (
                          <div key={finding.id} className="flex items-center gap-2 rounded-lg border p-2">
                            <AlertCircle className={cn(
                              'h-4 w-4',
                              finding.severity === 'critical' ? 'text-red-500' :
                              finding.severity === 'high' ? 'text-orange-500' : 'text-yellow-500'
                            )} />
                            <div className="flex-1">
                              <p className="text-sm">{finding.description}</p>
                              <p className="text-xs text-muted-foreground">
                                Due: {finding.dueDate ? new Date(finding.dueDate).toLocaleDateString() : 'Not set'}
                              </p>
                            </div>
                            <Badge variant="outline" className="capitalize text-xs">
                              {finding.severity}
                            </Badge>
                          </div>
                        ))}
                      </div>
                    </div>
                  )}
                  <Button variant="outline" size="sm" className="w-full">
                    View Full Report
                  </Button>
                </CardContent>
              </Card>
            ))}
          </div>
        </TabsContent>

        {/* Findings Tab */}
        <TabsContent value="findings">
          <Card>
            <CardHeader>
              <CardTitle>Compliance Findings</CardTitle>
              <CardDescription>Open findings from all compliance frameworks</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Finding</TableHead>
                    <TableHead>Framework</TableHead>
                    <TableHead>Severity</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Due Date</TableHead>
                    <TableHead className="text-right">Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {mockComplianceReports.flatMap(report =>
                    report.findings.map(finding => ({
                      ...finding,
                      framework: report.framework,
                      frameworkName: report.name,
                    }))
                  ).map((finding, i) => (
                    <TableRow key={i}>
                      <TableCell>
                        <p className="font-medium">{finding.description}</p>
                      </TableCell>
                      <TableCell>
                        <Badge variant="outline">{finding.frameworkName}</Badge>
                      </TableCell>
                      <TableCell>
                        <Badge
                          variant={
                            finding.severity === 'critical' ? 'destructive' :
                            finding.severity === 'high' ? 'warning' : 'secondary'
                          }
                          className="capitalize"
                        >
                          {finding.severity}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        <Badge
                          variant={finding.status === 'resolved' ? 'success' : finding.status === 'in_progress' ? 'info' : 'warning'}
                          className="capitalize"
                        >
                          {finding.status.replace('_', ' ')}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        <span className={cn(
                          'text-sm',
                          finding.dueDate && new Date(finding.dueDate) < new Date(Date.now() + 7 * 24 * 60 * 60 * 1000)
                            ? 'text-red-600 font-medium'
                            : 'text-muted-foreground'
                        )}>
                          {finding.dueDate ? new Date(finding.dueDate).toLocaleDateString() : 'Not set'}
                        </span>
                      </TableCell>
                      <TableCell className="text-right">
                        <Button variant="ghost" size="sm">Update</Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Controls Tab */}
        <TabsContent value="controls">
          <div className="grid gap-6 md:grid-cols-2">
            <Card>
              <CardHeader>
                <CardTitle>Active Controls</CardTitle>
                <CardDescription>Implemented security controls</CardDescription>
              </CardHeader>
              <CardContent className="space-y-3">
                {['Access Control', 'Encryption', 'Audit Logging', 'Network Security', 'Data Backup', 'Incident Response'].map((control, i) => (
                  <div key={i} className="flex items-center justify-between rounded-lg border p-3">
                    <div className="flex items-center gap-3">
                      <CheckCircle2 className="h-5 w-5 text-green-500" />
                      <span className="font-medium">{control}</span>
                    </div>
                    <Badge variant="success">Implemented</Badge>
                  </div>
                ))}
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle>Pending Controls</CardTitle>
                <CardDescription>Controls requiring implementation or review</CardDescription>
              </CardHeader>
              <CardContent className="space-y-3">
                <div className="flex items-center justify-between rounded-lg border p-3">
                  <div className="flex items-center gap-3">
                    <AlertCircle className="h-5 w-5 text-yellow-500" />
                    <span className="font-medium">Multi-Factor Authentication</span>
                  </div>
                  <Badge variant="warning">In Progress</Badge>
                </div>
                <div className="flex items-center justify-between rounded-lg border p-3">
                  <div className="flex items-center gap-3">
                    <AlertCircle className="h-5 w-5 text-orange-500" />
                    <span className="font-medium">Data Loss Prevention</span>
                  </div>
                  <Badge variant="warning">Planned</Badge>
                </div>
              </CardContent>
            </Card>
          </div>
        </TabsContent>
      </Tabs>
    </div>
  )
}

import { useState } from 'react'
import { Shield, AlertTriangle, Search, Filter, Eye, Download, CheckCircle, XCircle } from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
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
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@shared/components/ui/select'
import { cn, formatDateTime } from '@shared/utils/cn'
import { mockThreats, mockVulnerabilities, mockAuditLogs } from '@shared/data/mockData'

export default function SecurityPage() {
  const [searchQuery, setSearchQuery] = useState('')
  const [selectedTab, setSelectedTab] = useState('threats')

  const threatTypeConfig = {
    malware: { label: 'Malware', color: 'bg-red-500' },
    phishing: { label: 'Phishing', color: 'bg-orange-500' },
    ddos: { label: 'DDoS', color: 'bg-purple-500' },
    intrusion: { label: 'Intrusion', color: 'bg-red-600' },
    data_breach: { label: 'Data Breach', color: 'bg-red-700' },
    other: { label: 'Other', color: 'bg-slate-500' },
  }

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div className="page-header">
          <h1 className="page-title">Security Monitoring</hTitle>
          <p className="page-description">
            Track threats, vulnerabilities, and security events
          </p>
        </div>
        <div className="flex gap-2">
          <Button variant="outline">
            <Download className="mr-2 h-4 w-4" />
            Export Report
          </Button>
          <Button variant="admin">
            <Shield className="mr-2 h-4 w-4" />
            Security Settings
          </Button>
        </div>
      </div>

      {/* Security Score */}
      <Card className="bg-gradient-to-r from-red-500 to-orange-500 text-white">
        <CardContent className="p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm text-white/80">Security Posture Score</p>
              <div className="flex items-baseline gap-2">
                <span className="text-4xl font-bold">87</span>
                <span className="text-lg text-white/80">/100</span>
                <Badge className="ml-2 bg-white/20 text-white hover:bg-white/30">
                  Good
                </Badge>
              </div>
              <p className="mt-2 text-sm text-white/70">
                {mockThreats.filter(t => t.status === 'detected' || t.status === 'investigating').length} active threats · {mockVulnerabilities.filter(v => v.status === 'open' || v.status === 'in_progress').length} open vulnerabilities
              </p>
            </div>
            <div className="hidden md:block">
              <div className="flex h-20 w-20 items-center justify-center rounded-full bg-white/20">
                <Shield className="h-10 w-10" />
              </div>
            </div>
          </div>
        </CardContent>
      </Card>

      {/* Stats */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>Active Threats</CardDescription>
            <CardTitle className="text-2xl text-red-600">
              {mockThreats.filter(t => t.status !== 'mitigated' && t.status !== 'false_positive').length}
            </CardTitle>
          </CardHeader>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>Critical Vulnerabilities</CardDescription>
            <CardTitle className="text-2xl text-orange-600">
              {mockVulnerabilities.filter(v => v.severity === 'critical').length}
            </CardTitle>
          </CardHeader>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>Security Events (24h)</CardDescription>
            <CardTitle className="text-2xl">1,247</CardTitle>
          </CardHeader>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardDescription>Failed Logins (24h)</CardDescription>
            <CardTitle className="text-2xl text-yellow-600">23</CardTitle>
          </CardHeader>
        </Card>
      </div>

      {/* Search */}
      <div className="relative">
        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
        <Input
          placeholder="Search threats, vulnerabilities, or events..."
          className="pl-9"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </div>

      {/* Tabs */}
      <Tabs value={selectedTab} onValueChange={setSelectedTab}>
        <TabsList>
          <TabsTrigger value="threats">
            Threats ({mockThreats.length})
          </TabsTrigger>
          <TabsTrigger value="vulnerabilities">
            Vulnerabilities ({mockVulnerabilities.length})
          </TabsTrigger>
          <TabsTrigger value="audit">Audit Log</TabsTrigger>
        </TabsList>

        {/* Threats Tab */}
        <TabsContent value="threats">
          <Card>
            <CardHeader>
              <div className="flex items-center justify-between">
                <div>
                  <CardTitle>Security Threats</CardTitle>
                  <CardDescription>Detected security threats and incidents</CardDescription>
                </div>
                <Select defaultValue="all">
                  <SelectTrigger className="w-40">
                    <SelectValue placeholder="Filter" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">All Status</SelectItem>
                    <SelectItem value="detected">Detected</SelectItem>
                    <SelectItem value="investigating">Investigating</SelectItem>
                    <SelectItem value="mitigated">Mitigated</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Threat</TableHead>
                    <TableHead>Type</TableHead>
                    <TableHead>Severity</TableHead>
                    <TableHead>Source</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Detected</TableHead>
                    <TableHead className="text-right">Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {mockThreats.map((threat) => (
                    <TableRow key={threat.id}>
                      <TableCell>
                        <div>
                          <p className="font-medium">{threat.description}</p>
                          <p className="text-xs text-muted-foreground">{threat.target}</p>
                        </div>
                      </TableCell>
                      <TableCell>
                        <Badge variant="outline" className="capitalize">
                          {threatTypeConfig[threat.type].label}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        <Badge
                          variant={
                            threat.severity === 'critical' ? 'destructive' :
                            threat.severity === 'high' ? 'warning' : 'secondary'
                          }
                          className="capitalize"
                        >
                          {threat.severity}
                        </Badge>
                      </TableCell>
                      <TableCell className="font-mono text-xs">{threat.source}</TableCell>
                      <TableCell>
                        <Badge
                          variant={threat.status === 'mitigated' ? 'success' : threat.status === 'investigating' ? 'warning' : 'destructive'}
                          className="capitalize"
                        >
                          {threat.status.replace('_', ' ')}
                        </Badge>
                      </TableCell>
                      <TableCell>{formatDateTime(threat.detectedAt)}</TableCell>
                      <TableCell className="text-right">
                        <Button variant="ghost" size="icon">
                          <Eye className="h-4 w-4" />
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Vulnerabilities Tab */}
        <TabsContent value="vulnerabilities">
          <Card>
            <CardHeader>
              <div className="flex items-center justify-between">
                <div>
                  <CardTitle>Known Vulnerabilities</CardTitle>
                  <CardDescription>Security vulnerabilities requiring attention</CardDescription>
                </div>
                <div className="flex gap-2">
                  <Select defaultValue="all">
                    <SelectTrigger className="w-40">
                      <SelectValue placeholder="Severity" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="all">All Severity</SelectItem>
                      <SelectItem value="critical">Critical</SelectItem>
                      <SelectItem value="high">High</SelectItem>
                      <SelectItem value="medium">Medium</SelectItem>
                      <SelectItem value="low">Low</SelectItem>
                    </SelectContent>
                  </Select>
                  <Select defaultValue="all">
                    <SelectTrigger className="w-40">
                      <SelectValue placeholder="Status" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="all">All Status</SelectItem>
                      <SelectItem value="open">Open</SelectItem>
                      <SelectItem value="in_progress">In Progress</SelectItem>
                      <SelectItem value="patched">Patched</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
              </div>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Vulnerability</TableHead>
                    <TableHead>CVE ID</TableHead>
                    <TableHead>Severity</TableHead>
                    <TableHead>Component</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Due Date</TableHead>
                    <TableHead className="text-right">Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {mockVulnerabilities.map((vuln) => (
                    <TableRow key={vuln.id}>
                      <TableCell>
                        <p className="font-medium">{vuln.title}</p>
                        <p className="text-xs text-muted-foreground line-clamp-1">{vuln.description}</p>
                      </TableCell>
                      <TableCell>
                        {vuln.cveId ? (
                          <Badge variant="outline" className="font-mono">{vuln.cveId}</Badge>
                        ) : (
                          <span className="text-xs text-muted-foreground">N/A</span>
                        )}
                      </TableCell>
                      <TableCell>
                        <Badge
                          variant={
                            vuln.severity === 'critical' ? 'destructive' :
                            vuln.severity === 'high' ? 'warning' : 'secondary'
                          }
                          className="capitalize"
                        >
                          {vuln.severity}
                        </Badge>
                      </TableCell>
                      <TableCell>{vuln.affectedComponent}</TableCell>
                      <TableCell>
                        <Badge
                          variant={vuln.status === 'patched' ? 'success' : vuln.status === 'in_progress' ? 'warning' : 'destructive'}
                          className="capitalize"
                        >
                          {vuln.status.replace('_', ' ')}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        {vuln.dueDate ? (
                          <span className={cn(
                            'text-sm',
                            new Date(vuln.dueDate) < new Date(Date.now() + 7 * 24 * 60 * 60 * 1000) && vuln.status !== 'patched'
                              ? 'text-red-600 font-medium'
                              : 'text-muted-foreground'
                          )}>
                            {new Date(vuln.dueDate).toLocaleDateString()}
                          </span>
                        ) : (
                          <span className="text-xs text-muted-foreground">Not set</span>
                        )}
                      </TableCell>
                      <TableCell className="text-right">
                        <Button variant="ghost" size="sm">
                          {vuln.status === 'patched' ? 'View' : 'Fix'}
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Audit Log Tab */}
        <TabsContent value="audit">
          <Card>
            <CardHeader>
              <CardTitle>Security Audit Log</CardTitle>
              <CardDescription>Recent security-related events and actions</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Timestamp</TableHead>
                    <TableHead>Action</TableHead>
                    <TableHead>Actor</TableHead>
                    <TableHead>Target</TableHead>
                    <TableHead>Details</TableHead>
                    <TableHead>Result</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {mockAuditLogs
                    .filter(log => log.category === 'security' || log.category === 'auth')
                    .map((log) => (
                      <TableRow key={log.id}>
                        <TableCell className="text-sm text-muted-foreground">
                          {formatDateTime(log.timestamp)}
                        </TableCell>
                        <TableCell className="font-medium">{log.action.replace(/_/g, ' ')}</TableCell>
                        <TableCell>{log.actor}</TableCell>
                        <TableCell>{log.target || '-'}</TableCell>
                        <TableCell className="text-sm text-muted-foreground max-w-xs truncate">
                          {JSON.stringify(log.details)}
                        </TableCell>
                        <TableCell>
                          {log.result === 'success' ? (
                            <CheckCircle className="h-4 w-4 text-green-500" />
                          ) : (
                            <XCircle className="h-4 w-4 text-red-500" />
                          )}
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

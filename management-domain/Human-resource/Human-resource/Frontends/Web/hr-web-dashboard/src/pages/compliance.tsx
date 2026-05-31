import {
  Shield,
  CheckCircle,
  AlertTriangle,
  Clock,
  FileText,
  BookOpen,
  Award,
  Plus,
  Download,
  Filter,
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
import { cn } from '@shared/utils/cn'
import { complianceItems } from '@shared/data/mockData'

const typeConfig: Record<string, { label: string; icon: React.ComponentType<{ className?: string }>; color: string }> = {
  POLICY: { label: 'Policy', icon: FileText, color: 'bg-blue-100 text-blue-700 dark:bg-blue-900/20 dark:text-blue-400' },
  TRAINING: { label: 'Training', icon: BookOpen, color: 'bg-green-100 text-green-700 dark:bg-green-900/20 dark:text-green-400' },
  DOCUMENT: { label: 'Document', icon: FileText, color: 'bg-purple-100 text-purple-700 dark:bg-purple-900/20 dark:text-purple-400' },
  CERTIFICATION: { label: 'Certification', icon: Award, color: 'bg-orange-100 text-orange-700 dark:bg-orange-900/20 dark:text-orange-400' },
}

const statusConfig: Record<string, { label: string; variant: 'success' | 'warning' | 'destructive' | 'secondary' }> = {
  COMPLIANT: { label: 'Compliant', variant: 'success' },
  PENDING: { label: 'Pending', variant: 'warning' },
  OVERDUE: { label: 'Overdue', variant: 'destructive' },
  EXEMPT: { label: 'Exempt', variant: 'secondary' },
}

const additionalPolicies = [
  { id: 'pol-001', title: 'Anti-Harassment Policy', version: '3.2', lastUpdated: '2024-01-15', status: 'active', department: 'All' },
  { id: 'pol-002', title: 'Data Protection Policy', version: '2.1', lastUpdated: '2024-02-01', status: 'active', department: 'All' },
  { id: 'pol-003', title: 'Remote Work Policy', version: '1.5', lastUpdated: '2024-01-20', status: 'active', department: 'All' },
  { id: 'pol-004', title: 'Travel & Expense Policy', version: '4.0', lastUpdated: '2023-12-10', status: 'under_review', department: 'All' },
  { id: 'pol-005', title: 'Code of Conduct', version: '5.1', lastUpdated: '2024-03-01', status: 'active', department: 'All' },
]

const upcomingAudits = [
  { id: 'aud-001', title: 'Quarterly Safety Audit', date: '2024-04-15', auditor: 'Internal Audit Team', status: 'Scheduled' },
  { id: 'aud-002', title: 'Annual Compliance Review', date: '2024-05-01', auditor: 'External - KPMG', status: 'Pending' },
  { id: 'aud-003', title: 'Data Privacy Assessment', date: '2024-04-20', auditor: 'DPO Office', status: 'Scheduled' },
]

export default function CompliancePage() {
  const compliantCount = complianceItems.filter(c => c.status === 'COMPLIANT').length
  const pendingCount = complianceItems.filter(c => c.status === 'PENDING').length
  const overdueCount = complianceItems.filter(c => c.status === 'OVERDUE').length
  const complianceRate = Math.round((compliantCount / complianceItems.length) * 100)

  return (
    <div className="space-y-6">
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        <div className="page-header">
          <h1 className="page-title">Compliance</h1>
          <p className="page-description">HR compliance tracking, policies, and audits</p>
        </div>
        <div className="flex items-center gap-2">
          <Button variant="outline" size="sm">
            <Download className="mr-2 h-4 w-4" />
            Export Report
          </Button>
          <Button variant="hr" size="sm">
            <Plus className="mr-2 h-4 w-4" />
            New Compliance Item
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
                <p className="text-2xl font-bold">{complianceRate}%</p>
                <p className="text-xs text-muted-foreground">Compliance Rate</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-blue-100 p-2 dark:bg-blue-900/20">
                <Shield className="h-4 w-4 text-blue-600 dark:text-blue-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{compliantCount}</p>
                <p className="text-xs text-muted-foreground">Compliant</p>
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
                <p className="text-2xl font-bold">{pendingCount}</p>
                <p className="text-xs text-muted-foreground">Pending</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-red-100 p-2 dark:bg-red-900/20">
                <AlertTriangle className="h-4 w-4 text-red-600 dark:text-red-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{overdueCount}</p>
                <p className="text-xs text-muted-foreground">Overdue</p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      <Tabs defaultValue="tracking" className="space-y-4">
        <TabsList>
          <TabsTrigger value="tracking">Compliance Tracking</TabsTrigger>
          <TabsTrigger value="policies">Policies</TabsTrigger>
          <TabsTrigger value="audits">Audits</TabsTrigger>
        </TabsList>

        <TabsContent value="tracking" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Compliance Items</CardTitle>
              <CardDescription>Track mandatory compliance requirements</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Type</TableHead>
                    <TableHead>Title</TableHead>
                    <TableHead>Description</TableHead>
                    <TableHead>Due Date</TableHead>
                    <TableHead>Assignee</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead className="text-right">Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {complianceItems.map((item) => {
                    const config = typeConfig[item.type] || typeConfig.POLICY
                    const Icon = config.icon
                    return (
                      <TableRow key={item.id}>
                        <TableCell>
                          <div className={cn('inline-flex items-center gap-1.5 rounded-full px-2.5 py-0.5 text-xs font-medium', config.color)}>
                            <Icon className="h-3 w-3" />
                            {config.label}
                          </div>
                        </TableCell>
                        <TableCell className="font-medium">{item.title}</TableCell>
                        <TableCell className="max-w-[250px] truncate text-sm text-muted-foreground">{item.description}</TableCell>
                        <TableCell>{item.dueDate}</TableCell>
                        <TableCell className="text-sm">{item.assignee || '-'}</TableCell>
                        <TableCell>
                          <Badge variant={statusConfig[item.status]?.variant || 'secondary'}>
                            {statusConfig[item.status]?.label || item.status}
                          </Badge>
                        </TableCell>
                        <TableCell className="text-right">
                          <Button size="sm" variant="outline">
                            {item.status === 'PENDING' ? 'Complete' : 'View'}
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

        <TabsContent value="policies" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>HR Policies</CardTitle>
              <CardDescription>Company-wide HR policies and procedures</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                {additionalPolicies.map((policy) => (
                  <div key={policy.id} className="flex items-center justify-between rounded-lg border p-4 hover:bg-slate-50 dark:hover:bg-slate-800 transition-colors cursor-pointer">
                    <div className="flex items-center gap-3">
                      <div className="rounded-lg bg-slate-100 dark:bg-slate-800 p-2">
                        <FileText className="h-4 w-4 text-slate-600 dark:text-slate-400" />
                      </div>
                      <div>
                        <p className="font-medium">{policy.title}</p>
                        <p className="text-sm text-muted-foreground">
                          v{policy.version} • Last updated: {policy.lastUpdated} • {policy.department}
                        </p>
                      </div>
                    </div>
                    <div className="flex items-center gap-2">
                      <Badge variant={policy.status === 'active' ? 'success' : 'warning'}>
                        {policy.status === 'active' ? 'Active' : 'Under Review'}
                      </Badge>
                      <Button size="sm" variant="outline">View</Button>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="audits" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Upcoming Audits</CardTitle>
              <CardDescription>Scheduled compliance audits and reviews</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                {upcomingAudits.map((audit) => (
                  <div key={audit.id} className="flex items-center justify-between rounded-lg border p-4">
                    <div className="flex items-center gap-3">
                      <div className="rounded-lg bg-orange-100 dark:bg-orange-900/20 p-2">
                        <Shield className="h-4 w-4 text-orange-600 dark:text-orange-400" />
                      </div>
                      <div>
                        <p className="font-medium">{audit.title}</p>
                        <p className="text-sm text-muted-foreground">
                          {audit.date} • {audit.auditor}
                        </p>
                      </div>
                    </div>
                    <div className="flex items-center gap-2">
                      <Badge variant={audit.status === 'Scheduled' ? 'info' : 'warning'}>
                        {audit.status}
                      </Badge>
                      <Button size="sm" variant="outline">Details</Button>
                    </div>
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

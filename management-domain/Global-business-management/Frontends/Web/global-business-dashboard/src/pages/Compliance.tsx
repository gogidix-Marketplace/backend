import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import {
  ShieldCheck,
  AlertTriangle,
  FileCheck,
  Clock,
  CheckCircle,
  XCircle,
} from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '../components/common/Card';
import { Badge } from '../components/common/Badge';
import { Progress } from '../components/common/Progress';
import { Button } from '../components/common/Button';
import { DataTable } from '../components/tables/DataTable';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '../components/common/Tabs';
import { Modal, ModalBody, ModalFooter, ModalHeader } from '../components/common/Modal';
import { mockComplianceMetrics } from '../utils/mock-data';

const regulationColumns = [
  {
    id: 'name',
    header: 'Regulation',
    accessor: 'name' as const,
    cell: (row: any) => (
      <div>
        <p className="font-medium">{row.name}</p>
        <p className="text-sm text-muted-foreground">{row.category}</p>
      </div>
    ),
  },
  {
    id: 'status',
    header: 'Status',
    accessor: 'status' as const,
    cell: (row: any) => {
      const variants: Record<string, any> = {
        compliant: { variant: 'success', label: 'Compliant' },
        'non-compliant': { variant: 'destructive', label: 'Non-Compliant' },
        pending: { variant: 'warning', label: 'Pending Review' },
        exempt: { variant: 'secondary', label: 'Exempt' },
      };
      const config = variants[row.status] || variants.pending;
      return <Badge variant={config.variant} size="sm">{config.label}</Badge>;
    },
  },
  {
    id: 'severity',
    header: 'Severity',
    accessor: 'severity' as const,
    cell: (row: any) => {
      const variants: Record<string, any> = {
        high: { variant: 'destructive', label: 'High' },
        medium: { variant: 'warning', label: 'Medium' },
        low: { variant: 'info', label: 'Low' },
      };
      const config = variants[row.severity] || variants.low;
      return <Badge variant={config.variant} size="sm">{config.label}</Badge>;
    },
  },
  {
    id: 'nextReview',
    header: 'Next Review',
    accessor: 'nextReview' as const,
    cell: (row: any) => new Date(row.nextReview).toLocaleDateString(),
  },
];

const auditColumns = [
  {
    id: 'type',
    header: 'Audit Type',
    accessor: 'type' as const,
    cell: (row: any) => <span className="font-medium">{row.type}</span>,
  },
  {
    id: 'status',
    header: 'Status',
    accessor: 'status' as const,
    cell: (row: any) => {
      const variants: Record<string, any> = {
        completed: { variant: 'success', label: 'Completed' },
        'in-progress': { variant: 'info', label: 'In Progress' },
        scheduled: { variant: 'secondary', label: 'Scheduled' },
      };
      const config = variants[row.status] || variants.scheduled;
      return <Badge variant={config.variant} size="sm">{config.label}</Badge>;
    },
  },
  {
    id: 'date',
    header: 'Date',
    accessor: 'date' as const,
    cell: (row: any) => new Date(row.date).toLocaleDateString(),
  },
  {
    id: 'auditor',
    header: 'Auditor',
    accessor: 'auditor' as const,
  },
  {
    id: 'findings',
    header: 'Findings',
    accessor: 'findings' as const,
    cell: (row: any) => (
      <div className="flex items-center gap-2">
        <span>{row.findings}</span>
        {row.critical > 0 && (
          <Badge variant="destructive" size="sm">
            {row.critical} Critical
          </Badge>
        )}
      </div>
    ),
  },
];

const incidentColumns = [
  {
    id: 'type',
    header: 'Incident Type',
    accessor: 'type' as const,
    cell: (row: any) => <span className="font-medium">{row.type}</span>,
  },
  {
    id: 'severity',
    header: 'Severity',
    accessor: 'severity' as const,
    cell: (row: any) => {
      const variants: Record<string, any> = {
        critical: { variant: 'destructive', label: 'Critical' },
        high: { variant: 'destructive', label: 'High' },
        medium: { variant: 'warning', label: 'Medium' },
        low: { variant: 'info', label: 'Low' },
      };
      const config = variants[row.severity] || variants.low;
      return <Badge variant={config.variant} size="sm">{config.label}</Badge>;
    },
  },
  {
    id: 'status',
    header: 'Status',
    accessor: 'status' as const,
    cell: (row: any) => {
      const variants: Record<string, any> = {
        open: { variant: 'destructive', label: 'Open' },
        investigating: { variant: 'warning', label: 'Investigating' },
        resolved: { variant: 'success', label: 'Resolved' },
        closed: { variant: 'secondary', label: 'Closed' },
      };
      const config = variants[row.status] || variants.open;
      return <Badge variant={config.variant} size="sm">{config.label}</Badge>;
    },
  },
  {
    id: 'reportedAt',
    header: 'Reported',
    accessor: 'reportedAt' as const,
    cell: (row: any) => new Date(row.reportedAt).toLocaleDateString(),
  },
];

export const Compliance: React.FC = () => {
  const { t } = useTranslation();
  const [selectedRegulation, setSelectedRegulation] = useState<any>(null);

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">{t('compliance.title')}</h1>
          <p className="text-muted-foreground">Compliance and risk management</p>
        </div>
        <Button>
          <FileCheck className="h-4 w-4 mr-2" />
          Run Audit
        </Button>
      </div>

      {/* Summary Cards */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Compliance Score</CardTitle>
            <ShieldCheck className="h-4 w-4 text-green-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-green-600">
              {mockComplianceMetrics.overallScore}%
            </div>
            <Progress value={mockComplianceMetrics.overallScore} className="mt-2" />
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Regulations</CardTitle>
            <FileCheck className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {mockComplianceMetrics.regulations.filter((r: any) => r.status === 'compliant').length}/
              {mockComplianceMetrics.regulations.length}
            </div>
            <p className="text-xs text-green-600 mt-1">Fully compliant</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Training</CardTitle>
            <CheckCircle className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{mockComplianceMetrics.trainingsCompleted}</div>
            <p className="text-xs text-muted-foreground mt-1">
              {mockComplianceMetrics.trainingsPending} pending
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Open Incidents</CardTitle>
            <AlertTriangle className="h-4 w-4 text-yellow-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{mockComplianceMetrics.incidents.length}</div>
            <p className="text-xs text-yellow-600 mt-1">Requires attention</p>
          </CardContent>
        </Card>
      </div>

      {/* Tabs */}
      <Tabs defaultValue="regulations">
        <TabsList>
          <TabsTrigger value="regulations">Regulations</TabsTrigger>
          <TabsTrigger value="audits">Audits</TabsTrigger>
          <TabsTrigger value="incidents">Incidents</TabsTrigger>
          <TabsTrigger value="training">Training</TabsTrigger>
        </TabsList>

        <TabsContent value="regulations" className="mt-6">
          <DataTable
            data={mockComplianceMetrics.regulations}
            columns={regulationColumns}
            keyField="id"
            title="Regulatory Compliance"
            searchable
            sortable
            onRowClick={setSelectedRegulation}
          />
        </TabsContent>

        <TabsContent value="audits" className="mt-6">
          <DataTable
            data={mockComplianceMetrics.audits}
            columns={auditColumns}
            keyField="id"
            title="Audit History"
            searchable
            sortable
          />
        </TabsContent>

        <TabsContent value="incidents" className="mt-6">
          <DataTable
            data={mockComplianceMetrics.incidents}
            columns={incidentColumns}
            keyField="id"
            title="Security Incidents"
            searchable
            sortable
          />
        </TabsContent>

        <TabsContent value="training" className="mt-6">
          <Card>
            <CardHeader>
              <CardTitle>Training Progress</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-6">
                <div>
                  <div className="flex justify-between mb-2">
                    <span>Completed Trainings</span>
                    <span className="font-semibold">{mockComplianceMetrics.trainingsCompleted}</span>
                  </div>
                  <Progress
                    value={(mockComplianceMetrics.trainingsCompleted /
                      (mockComplianceMetrics.trainingsCompleted + mockComplianceMetrics.trainingsPending)) * 100}
                    showLabel
                  />
                </div>

                <div className="border-t pt-4">
                  <h4 className="font-semibold mb-4">Upcoming Training</h4>
                  <div className="space-y-3">
                    {['GDPR Compliance', 'SOX Training', 'Security Awareness'].map((training, i) => (
                      <div key={i} className="flex items-center justify-between p-3 border rounded-lg">
                        <div className="flex items-center gap-3">
                          <Clock className="h-4 w-4 text-yellow-500" />
                          <span className="font-medium">{training}</span>
                        </div>
                        <Button variant="outline" size="sm">
                          Start
                        </Button>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>

      {/* Regulation Detail Modal */}
      <Modal
        isOpen={!!selectedRegulation}
        onClose={() => setSelectedRegulation(null)}
        title={selectedRegulation?.name}
      >
        {selectedRegulation && (
          <>
            <ModalBody>
              <div className="space-y-4">
                <div>
                  <label className="text-sm font-medium text-muted-foreground">Category</label>
                  <p className="font-medium">{selectedRegulation.category}</p>
                </div>
                <div>
                  <label className="text-sm font-medium text-muted-foreground">Status</label>
                  <div className="mt-1">
                    <Badge
                      variant={selectedRegulation.status === 'compliant' ? 'success' : 'warning'}
                      size="sm"
                    >
                      {selectedRegulation.status}
                    </Badge>
                  </div>
                </div>
                <div className="grid gap-4 md:grid-cols-2">
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">Last Review</label>
                    <p className="font-medium">{new Date(selectedRegulation.lastReview).toLocaleDateString()}</p>
                  </div>
                  <div>
                    <label className="text-sm font-medium text-muted-foreground">Next Review</label>
                    <p className="font-medium">{new Date(selectedRegulation.nextReview).toLocaleDateString()}</p>
                  </div>
                </div>
              </div>
            </ModalBody>
            <ModalFooter>
              <Button variant="outline" onClick={() => setSelectedRegulation(null)}>
                Close
              </Button>
              <Button>View Full Report</Button>
            </ModalFooter>
          </>
        )}
      </Modal>
    </div>
  );
};

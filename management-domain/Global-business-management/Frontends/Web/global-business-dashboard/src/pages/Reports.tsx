import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { FileText, Download, Share2, Calendar, Filter, Plus, Trash2, Eye } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '../components/common/Card';
import { Button } from '../components/common/Button';
import { Badge } from '../components/common/Badge';
import { Select } from '../components/common/Select';
import { Modal, ModalBody, ModalFooter, ModalHeader } from '../components/common/Modal';
import { Input } from '../components/common/Input';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '../components/common/Tabs';
import { DataTable } from '../components/tables/DataTable';
import { ReportType, ReportStatus } from '../types';

const mockReports = [
  {
    id: '1',
    name: 'Q4 2023 Financial Report',
    type: 'quarterly' as ReportType,
    status: 'published' as ReportStatus,
    createdAt: '2024-01-15',
    createdBy: 'John Smith',
  },
  {
    id: '2',
    name: 'Annual Compliance Summary',
    type: 'annual' as ReportType,
    status: 'approved' as ReportStatus,
    createdAt: '2024-01-10',
    createdBy: 'System',
  },
  {
    id: '3',
    name: 'Monthly Performance - December',
    type: 'monthly' as ReportType,
    status: 'draft' as ReportStatus,
    createdAt: '2024-01-05',
    createdBy: 'Jane Doe',
  },
  {
    id: '4',
    name: 'Regional Analysis - NA',
    type: 'custom' as ReportType,
    status: 'pending' as ReportStatus,
    createdAt: '2024-01-03',
    createdBy: 'Bob Johnson',
  },
];

export const Reports: React.FC = () => {
  const { t } = useTranslation();
  const [showGenerateModal, setShowGenerateModal] = useState(false);
  const [selectedTab, setSelectedTab] = useState('all');

  const reportTypeOptions = [
    { value: 'quarterly', label: t('reports.formats.quarterly') },
    { value: 'annual', label: t('reports.formats.annual') },
    { value: 'monthly', label: t('reports.formats.monthly') },
    { value: 'custom', label: t('reports.formats.custom') },
  ];

  const exportFormatOptions = [
    { value: 'pdf', label: 'PDF' },
    { value: 'excel', label: 'Excel' },
    { value: 'csv', label: 'CSV' },
    { value: 'json', label: 'JSON' },
  ];

  const statusVariants: Record<string, any> = {
    published: { variant: 'success', label: 'Published' },
    approved: { variant: 'success', label: 'Approved' },
    pending: { variant: 'warning', label: 'Pending' },
    draft: { variant: 'secondary', label: 'Draft' },
  };

  const columns = [
    {
      id: 'name',
      header: t('common.name'),
      accessor: 'name' as const,
      cell: (row: any) => (
        <div className="flex items-center gap-3">
          <div className="flex items-center justify-center w-10 h-10 rounded-lg bg-primary/10 text-primary">
            <FileText className="h-5 w-5" />
          </div>
          <div>
            <p className="font-medium">{row.name}</p>
            <p className="text-sm text-muted-foreground">{row.type}</p>
          </div>
        </div>
      ),
    },
    {
      id: 'status',
      header: t('common.status'),
      accessor: 'status' as const,
      cell: (row: any) => {
        const config = statusVariants[row.status] || statusVariants.draft;
        return <Badge variant={config.variant} size="sm">{config.label}</Badge>;
      },
    },
    {
      id: 'createdAt',
      header: t('reports.createdAt'),
      accessor: 'createdAt' as const,
      cell: (row: any) => new Date(row.createdAt).toLocaleDateString(),
    },
    {
      id: 'createdBy',
      header: t('reports.createdBy'),
      accessor: 'createdBy' as const,
    },
    {
      id: 'actions',
      header: t('tables.actions'),
      cell: () => (
        <div className="flex items-center gap-2">
          <Button variant="ghost" size="icon">
            <Eye className="h-4 w-4" />
          </Button>
          <Button variant="ghost" size="icon">
            <Download className="h-4 w-4" />
          </Button>
          <Button variant="ghost" size="icon">
            <Trash2 className="h-4 w-4 text-destructive" />
          </Button>
        </div>
      ),
    },
  ];

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">{t('reports.title')}</h1>
          <p className="text-muted-foreground">Generate and manage business reports</p>
        </div>
        <Button onClick={() => setShowGenerateModal(true)}>
          <Plus className="h-4 w-4 mr-2" />
          {t('reports.generateReport')}
        </Button>
      </div>

      {/* Tabs */}
      <Tabs value={selectedTab} onValueChange={(v) => setSelectedTab(v)}>
        <TabsList>
          <TabsTrigger value="all">All Reports</TabsTrigger>
          <TabsTrigger value="recent">Recent</TabsTrigger>
          <TabsTrigger value="scheduled">Scheduled</TabsTrigger>
        </TabsList>

        <TabsContent value="all" className="mt-6">
          <DataTable
            data={mockReports}
            columns={columns}
            keyField="id"
            searchable
            sortable
            pagination={{
              pageSize: 10,
              currentPage: 0,
              onPageChange: () => {},
            }}
          />
        </TabsContent>

        <TabsContent value="recent" className="mt-6">
          <DataTable
            data={mockReports.slice(0, 2)}
            columns={columns}
            keyField="id"
            searchable
            sortable
          />
        </TabsContent>

        <TabsContent value="scheduled" className="mt-6">
          <Card>
            <CardContent className="p-12 text-center">
              <Calendar className="h-12 w-12 mx-auto mb-4 text-muted-foreground" />
              <h3 className="text-lg font-semibold mb-2">No Scheduled Reports</h3>
              <p className="text-muted-foreground mb-4">
                Create scheduled reports to automate your reporting workflow.
              </p>
              <Button variant="outline">
                <Plus className="h-4 w-4 mr-2" />
                Schedule Report
              </Button>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>

      {/* Generate Report Modal */}
      <Modal
        isOpen={showGenerateModal}
        onClose={() => setShowGenerateModal(false)}
        title={t('reports.generateReport')}
        size="lg"
      >
        <ModalBody>
          <div className="space-y-4">
            <Input
              label="Report Name"
              placeholder="Enter report name"
            />

            <Select
              label={t('reports.reportType')}
              options={reportTypeOptions}
              placeholder="Select report type"
            />

            <div className="grid gap-4 md:grid-cols-2">
              <Input
                label={t('common.date')}
                type="date"
              />
              <Select
                label={t('reports.reportFormat')}
                options={exportFormatOptions}
                placeholder="Select format"
              />
            </div>

            <div className="space-y-2">
              <label className="text-sm font-medium">Include Sections</label>
              <div className="grid gap-2 md:grid-cols-2">
                {['Financial Summary', 'Regional Breakdown', 'Customer Metrics', 'Operational Data'].map((section) => (
                  <label key={section} className="flex items-center gap-2 p-3 border rounded-lg cursor-pointer hover:bg-accent">
                    <input type="checkbox" className="rounded" defaultChecked={section !== 'Operational Data'} />
                    <span className="text-sm">{section}</span>
                  </label>
                ))}
              </div>
            </div>
          </div>
        </ModalBody>
        <ModalFooter>
          <Button variant="outline" onClick={() => setShowGenerateModal(false)}>
            {t('common.cancel')}
          </Button>
          <Button>{t('reports.generate')}</Button>
        </ModalFooter>
      </Modal>
    </div>
  );
};

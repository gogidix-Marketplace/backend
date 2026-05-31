import React from 'react';
import { Box, Chip } from '@mui/material';
import PageHeader from '@/components/common/PageHeader';
import DataTable from '@/components/common/DataTable';

const mockDemoRequests = [
  {
    id: '1',
    firstName: 'John',
    lastName: 'Doe',
    email: 'john@company.com',
    company: 'Acme Corp',
    companySize: '50-100',
    interests: ['Enterprise Plan', 'API Access'],
    status: 'new',
    source: 'Website',
    createdAt: '2024-01-15T10:00:00Z',
  },
  {
    id: '2',
    firstName: 'Jane',
    lastName: 'Smith',
    email: 'jane@startup.io',
    company: 'Startup IO',
    companySize: '10-50',
    interests: ['Growth Plan'],
    status: 'contacted',
    source: 'LinkedIn',
    createdAt: '2024-01-14T14:30:00Z',
  },
];

const LeadsDemoRequests: React.FC = () => {
  const getStatusColor = (status: string) => {
    switch (status) {
      case 'new': return 'success';
      case 'contacted': return 'info';
      case 'qualified': return 'primary';
      case 'converted': return undefined;
      case 'lost': return 'error';
      default: return 'default';
    }
  };

  const columns = [
    {
      id: 'name',
      label: 'Name',
      render: (_: string, row: any) => `${row.firstName} ${row.lastName}`,
    },
    { id: 'email', label: 'Email' },
    { id: 'company', label: 'Company' },
    { id: 'companySize', label: 'Company Size' },
    {
      id: 'interests',
      label: 'Interests',
      render: (_: string, row: any) => row.interests?.join(', ') || '-',
    },
    {
      id: 'status',
      label: 'Status',
      render: (_: string, row: any) => (
        <Chip label={row.status} size="small" color={getStatusColor(row.status) as any} />
      ),
    },
    { id: 'source', label: 'Source' },
    {
      id: 'createdAt',
      label: 'Created',
      render: (_: string, row: any) => new Date(row.createdAt).toLocaleDateString(),
    },
  ];

  return (
    <Box>
      <PageHeader
        title="Demo Requests"
        subtitle="Manage product demo requests"
      />

      <DataTable
        columns={columns}
        rows={mockDemoRequests}
        total={mockDemoRequests.length}
        onEdit={(row) => console.log('View lead:', row)}
      />
    </Box>
  );
};

export default LeadsDemoRequests;

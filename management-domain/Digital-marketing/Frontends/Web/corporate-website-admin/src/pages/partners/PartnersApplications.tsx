import React from 'react';
import { Box } from '@mui/material';
import PageHeader from '@/components/common/PageHeader';
import DataTable from '@/components/common/DataTable';

const mockApplications = [
  {
    id: '1',
    programId: '1',
    program: { name: 'Referral Partner Program' },
    companyName: 'Tech Solutions Inc',
    contactName: 'John Doe',
    email: 'john@techsolutions.com',
    website: 'https://techsolutions.com',
    status: 'pending',
    appliedAt: '2024-01-15T10:00:00Z',
  },
  {
    id: '2',
    programId: '2',
    program: { name: 'Technology Partner Program' },
    companyName: 'CloudSoft',
    contactName: 'Jane Smith',
    email: 'jane@cloudsoft.com',
    website: 'https://cloudsoft.com',
    status: 'approved',
    appliedAt: '2024-01-14T09:00:00Z',
  },
];

const PartnersApplications: React.FC = () => {
  const columns = [
    { id: 'companyName', label: 'Company' },
    { id: 'contactName', label: 'Contact' },
    { id: 'email', label: 'Email' },
    {
      id: 'program',
      label: 'Program',
      render: (_: string, row: any) => row.program?.name || '-',
    },
    {
      id: 'status',
      label: 'Status',
      render: (_: string, row: any) => (
        <span style={{
          color: row.status === 'approved' ? 'green' : row.status === 'rejected' ? 'red' : 'orange'
        }}>
          {row.status}
        </span>
      ),
    },
    {
      id: 'appliedAt',
      label: 'Applied',
      render: (_: string, row: any) => new Date(row.appliedAt).toLocaleDateString(),
    },
  ];

  return (
    <Box>
      <PageHeader
        title="Partner Applications"
        subtitle="Review and manage partner program applications"
      />

      <DataTable
        columns={columns}
        rows={mockApplications}
        total={mockApplications.length}
        onEdit={(row) => console.log('Review application:', row)}
      />
    </Box>
  );
};

export default PartnersApplications;

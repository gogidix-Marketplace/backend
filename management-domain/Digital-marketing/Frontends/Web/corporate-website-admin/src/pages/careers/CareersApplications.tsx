import React from 'react';
import { Box, Chip } from '@mui/material';
import PageHeader from '@/components/common/PageHeader';
import DataTable from '@/components/common/DataTable';

const mockApplications = [
  {
    id: '1',
    jobId: '1',
    job: { title: 'Senior Frontend Developer' },
    applicant: { firstName: 'John', lastName: 'Doe', email: 'john@example.com' },
    status: 'new',
    stage: 'applied',
    appliedAt: '2024-01-15T10:00:00Z',
  },
  {
    id: '2',
    jobId: '1',
    job: { title: 'Senior Frontend Developer' },
    applicant: { firstName: 'Jane', lastName: 'Smith', email: 'jane@example.com' },
    status: 'reviewing',
    stage: 'interview',
    appliedAt: '2024-01-14T09:00:00Z',
  },
];

const CareersApplications: React.FC = () => {
  const columns = [
    {
      id: 'applicant',
      label: 'Applicant',
      render: (_: string, row: any) => `${row.applicant.firstName} ${row.applicant.lastName}`,
    },
    {
      id: 'job',
      label: 'Position',
      render: (_: string, row: any) => row.job?.title || '-',
    },
    {
      id: 'email',
      label: 'Email',
      render: (_: string, row: any) => row.applicant.email,
    },
    {
      id: 'status',
      label: 'Status',
      render: (_: string, row: any) => (
        <Chip
          label={row.status}
          size="small"
          color={row.status === 'new' ? 'success' : 'default'}
        />
      ),
    },
    {
      id: 'stage',
      label: 'Stage',
      render: (_: string, row: any) => row.stage.replace('_', ' '),
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
        title="Job Applications"
        subtitle="Review and manage job applications"
      />

      <DataTable
        columns={columns}
        rows={mockApplications}
        total={mockApplications.length}
        onEdit={(row) => console.log('View application:', row)}
      />
    </Box>
  );
};

export default CareersApplications;

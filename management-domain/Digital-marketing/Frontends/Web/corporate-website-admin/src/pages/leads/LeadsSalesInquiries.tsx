import React from 'react';
import { Box, Chip } from '@mui/material';
import PageHeader from '@/components/common/PageHeader';
import DataTable from '@/components/common/DataTable';

const mockInquiries = [
  {
    id: '1',
    firstName: 'John',
    lastName: 'Doe',
    email: 'john@enterprise.com',
    company: 'Enterprise Inc',
    inquiryType: 'sales',
    subject: 'Enterprise pricing inquiry',
    message: 'We are interested in enterprise pricing for 500+ users.',
    budget: '$50,000+',
    priority: 'high',
    status: 'new',
    createdAt: '2024-01-15T10:00:00Z',
  },
  {
    id: '2',
    firstName: 'Sarah',
    lastName: 'Johnson',
    email: 'sarah@tech.co',
    company: 'Tech Co',
    inquiryType: 'partnership',
    subject: 'Potential partnership opportunity',
    message: 'We would like to discuss a potential partnership.',
    budget: '$10,000 - $25,000',
    priority: 'normal',
    status: 'qualified',
    createdAt: '2024-01-14T14:30:00Z',
  },
];

const LeadsSalesInquiries: React.FC = () => {
  const getPriorityColor = (priority: string) => {
    switch (priority) {
      case 'urgent': return 'error';
      case 'high': return 'warning';
      case 'normal': return 'info';
      case 'low': return 'default';
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
    { id: 'subject', label: 'Subject' },
    {
      id: 'inquiryType',
      label: 'Type',
      render: (_: string, row: any) => row.inquiryType.replace('_', ' '),
    },
    { id: 'budget', label: 'Budget' },
    {
      id: 'priority',
      label: 'Priority',
      render: (_: string, row: any) => (
        <Chip label={row.priority} size="small" color={getPriorityColor(row.priority) as any} />
      ),
    },
    {
      id: 'status',
      label: 'Status',
      render: (_: string, row: any) => row.status,
    },
    {
      id: 'createdAt',
      label: 'Created',
      render: (_: string, row: any) => new Date(row.createdAt).toLocaleDateString(),
    },
  ];

  return (
    <Box>
      <PageHeader
        title="Sales Inquiries"
        subtitle="Manage sales inquiries and opportunities"
      />

      <DataTable
        columns={columns}
        rows={mockInquiries}
        total={mockInquiries.length}
        onEdit={(row) => console.log('View inquiry:', row)}
      />
    </Box>
  );
};

export default LeadsSalesInquiries;

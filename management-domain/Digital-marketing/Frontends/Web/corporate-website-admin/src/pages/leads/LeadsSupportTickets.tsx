import React from 'react';
import { Box, Chip } from '@mui/material';
import PageHeader from '@/components/common/PageHeader';
import DataTable from '@/components/common/DataTable';

const mockTickets = [
  {
    id: '1',
    ticketNumber: 'SUP-2024-001',
    customerName: 'John Doe',
    customerEmail: 'john@example.com',
    category: 'technical',
    subject: 'Unable to connect to API',
    description: 'Getting 401 errors when trying to authenticate.',
    priority: 'high',
    status: 'open',
    assignedTo: 'Support Agent',
    createdAt: '2024-01-15T10:00:00Z',
  },
  {
    id: '2',
    ticketNumber: 'SUP-2024-002',
    customerName: 'Jane Smith',
    customerEmail: 'jane@example.com',
    category: 'billing',
    subject: 'Invoice discrepancy',
    description: 'The invoice amount does not match our contract.',
    priority: 'normal',
    status: 'in_progress',
    assignedTo: 'Billing Team',
    createdAt: '2024-01-14T14:30:00Z',
  },
  {
    id: '3',
    ticketNumber: 'SUP-2024-003',
    customerName: 'Bob Johnson',
    customerEmail: 'bob@example.com',
    category: 'feature_request',
    subject: 'Request for custom integration',
    description: 'We would like to integrate with our custom CRM.',
    priority: 'low',
    status: 'resolved',
    assignedTo: 'Product Team',
    createdAt: '2024-01-13T09:00:00Z',
  },
];

const LeadsSupportTickets: React.FC = () => {
  const getStatusColor = (status: string) => {
    switch (status) {
      case 'open': return 'error';
      case 'in_progress': return 'warning';
      case 'waiting_on_customer': return 'info';
      case 'resolved': return 'success';
      case 'closed': return 'default';
      default: return 'default';
    }
  };

  const columns = [
    { id: 'ticketNumber', label: 'Ticket #' },
    { id: 'customerName', label: 'Customer' },
    { id: 'customerEmail', label: 'Email' },
    {
      id: 'category',
      label: 'Category',
      render: (_: string, row: any) => row.category.replace('_', ' '),
    },
    { id: 'subject', label: 'Subject' },
    {
      id: 'priority',
      label: 'Priority',
      render: (_: string, row: any) => (
        <Chip label={row.priority} size="small" color={row.priority === 'urgent' || row.priority === 'high' ? 'error' : 'default'} />
      ),
    },
    {
      id: 'status',
      label: 'Status',
      render: (_: string, row: any) => (
        <Chip label={row.status.replace('_', ' ')} size="small" color={getStatusColor(row.status) as any} />
      ),
    },
    { id: 'assignedTo', label: 'Assigned To' },
    {
      id: 'createdAt',
      label: 'Created',
      render: (_: string, row: any) => new Date(row.createdAt).toLocaleDateString(),
    },
  ];

  return (
    <Box>
      <PageHeader
        title="Support Tickets"
        subtitle="Manage customer support tickets"
      />

      <DataTable
        columns={columns}
        rows={mockTickets}
        total={mockTickets.length}
        onEdit={(row) => console.log('View ticket:', row)}
      />
    </Box>
  );
};

export default LeadsSupportTickets;

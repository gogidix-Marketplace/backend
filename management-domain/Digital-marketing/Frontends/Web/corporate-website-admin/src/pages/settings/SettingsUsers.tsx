import React, { useState } from 'react';
import { Box, Button, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Stack, FormControl, InputLabel, Select, MenuItem, Chip, Avatar, Card, CardContent } from '@mui/material';
import { Add, Person } from '@mui/icons-material';
import PageHeader from '@/components/common/PageHeader';
import DataTable from '@/components/common/DataTable';
import { useSnackbar } from '@/contexts/SnackbarContext';
import { User, UserRole } from '@/types';

const mockUsers: User[] = [
  {
    id: '1',
    email: 'admin@gogidix.com',
    firstName: 'Admin',
    lastName: 'User',
    role: 'admin',
    permissions: [],
    createdAt: '2023-01-01',
    updatedAt: '2024-01-01',
  },
  {
    id: '2',
    email: 'editor@gogidix.com',
    firstName: 'Content',
    lastName: 'Editor',
    role: 'editor',
    permissions: ['content:read', 'content:write'],
    createdAt: '2023-06-15',
    updatedAt: '2024-01-01',
  },
  {
    id: '3',
    email: 'author@gogidix.com',
    firstName: 'Blog',
    lastName: 'Author',
    role: 'author',
    permissions: ['content:read', 'content:write'],
    department: 'Marketing',
    createdAt: '2023-08-20',
    updatedAt: '2024-01-01',
  },
];

const SettingsUsers: React.FC = () => {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [openDialog, setOpenDialog] = useState(false);
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    role: 'viewer' as UserRole,
  });

  const { showSuccess, showError } = useSnackbar();

  const handleOpenDialog = () => {
    setFormData({ firstName: '', lastName: '', email: '', role: 'viewer' });
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
  };

  const handleSubmit = () => {
    try {
      // Simulate API call
      showSuccess('User invited successfully');
      handleCloseDialog();
    } catch {
      showError('Failed to invite user');
    }
  };

  const columns = [
    {
      id: 'name',
      label: 'Name',
      render: (_: string, row: User) => (
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <Avatar>
            <Person />
          </Avatar>
          <span>{row.firstName} {row.lastName}</span>
        </Box>
      ),
    },
    { id: 'email', label: 'Email' },
    {
      id: 'role',
      label: 'Role',
      render: (_: string, row: User) => (
        <Chip label={row.role} size="small" color={row.role === 'admin' ? 'primary' : 'default'} />
      ),
    },
    {
      id: 'department',
      label: 'Department',
      render: (_: string, row: User) => row.department || '-',
    },
    {
      id: 'permissions',
      label: 'Permissions',
      render: (_: string, row: User) => row.permissions.length || 'All',
    },
  ];

  return (
    <Box>
      <PageHeader
        title="Users & Permissions"
        subtitle="Manage user accounts and access control"
        action={{
          label: 'Invite User',
          onClick: handleOpenDialog,
          variant: 'contained',
          startIcon: <Add />,
        }}
      />

      <DataTable
        columns={columns}
        rows={mockUsers}
        total={mockUsers.length}
        page={page}
        rowsPerPage={rowsPerPage}
        onPageChange={setPage}
        onRowsPerPageChange={setRowsPerPage}
        onEdit={(row) => console.log('Edit user:', row)}
      />

      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
        <DialogTitle>Invite New User</DialogTitle>
        <DialogContent>
          <Stack spacing={3} sx={{ mt: 2 }}>
            <Stack direction="row" spacing={2}>
              <TextField
                label="First Name"
                fullWidth
                value={formData.firstName}
                onChange={(e) => setFormData({ ...formData, firstName: e.target.value })}
              />
              <TextField
                label="Last Name"
                fullWidth
                value={formData.lastName}
                onChange={(e) => setFormData({ ...formData, lastName: e.target.value })}
              />
            </Stack>
            <TextField
              label="Email"
              fullWidth
              type="email"
              value={formData.email}
              onChange={(e) => setFormData({ ...formData, email: e.target.value })}
            />
            <FormControl fullWidth>
              <InputLabel>Role</InputLabel>
              <Select
                value={formData.role}
                label="Role"
                onChange={(e) => setFormData({ ...formData, role: e.target.value as UserRole })}
              >
                <MenuItem value="admin">Admin</MenuItem>
                <MenuItem value="editor">Editor</MenuItem>
                <MenuItem value="author">Author</MenuItem>
                <MenuItem value="contributor">Contributor</MenuItem>
                <MenuItem value="viewer">Viewer</MenuItem>
              </Select>
            </FormControl>
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button onClick={handleSubmit} variant="contained">Send Invite</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default SettingsUsers;

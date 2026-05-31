import React, { useState } from 'react';
import { Box, Button, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Stack, FormControl, InputLabel, Select, MenuItem } from '@mui/material';
import { Add } from '@mui/icons-material';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getIntegrations, createIntegration, updateIntegration, deleteIntegration } from '@/services/productsApi';
import { queryKeys } from '@/services/api';
import { Integration, IntegrationCategory, IntegrationStatus } from '@/types';
import PageHeader from '@/components/common/PageHeader';
import DataTable from '@/components/common/DataTable';
import MediaLibrary from '@/components/media/MediaLibrary';
import { useSnackbar } from '@/contexts/SnackbarContext';

const categories: IntegrationCategory[] = ['crm', 'analytics', 'communication', 'productivity', 'ecommerce', 'other'];
const statuses: IntegrationStatus[] = ['native', 'partner', 'community', 'beta'];

const ProductsIntegrations: React.FC = () => {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [openDialog, setOpenDialog] = useState(false);
  const [openMediaLibrary, setOpenMediaLibrary] = useState(false);
  const [editingIntegration, setEditingIntegration] = useState<Integration | null>(null);
  const [formData, setFormData] = useState({
    name: '',
    slug: '',
    description: '',
    category: 'other' as IntegrationCategory,
    status: 'community' as IntegrationStatus,
    documentationUrl: '',
    logoUrl: '',
  });

  const { showSuccess, showError } = useSnackbar();
  const queryClient = useQueryClient();

  const { data, isLoading } = useQuery({
    queryKey: queryKeys.products.integrations(),
    queryFn: () => getIntegrations({ page: page + 1, limit: rowsPerPage }),
  });

  const createMutation = useMutation({
    mutationFn: createIntegration,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.products.integrations() });
      showSuccess('Integration created successfully');
      handleCloseDialog();
    },
    onError: () => showError('Failed to create integration'),
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: Partial<Integration> }) =>
      updateIntegration(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.products.integrations() });
      showSuccess('Integration updated successfully');
      handleCloseDialog();
    },
    onError: () => showError('Failed to update integration'),
  });

  const deleteMutation = useMutation({
    mutationFn: deleteIntegration,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.products.integrations() });
      showSuccess('Integration deleted successfully');
    },
    onError: () => showError('Failed to delete integration'),
  });

  const handleOpenDialog = (integration?: Integration) => {
    if (integration) {
      setEditingIntegration(integration);
      setFormData({
        name: integration.name,
        slug: integration.slug,
        description: integration.description,
        category: integration.category,
        status: integration.status,
        documentationUrl: integration.documentationUrl || '',
        logoUrl: integration.logo?.url || '',
      });
    } else {
      setEditingIntegration(null);
      setFormData({
        name: '',
        slug: '',
        description: '',
        category: 'other',
        status: 'community',
        documentationUrl: '',
        logoUrl: '',
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingIntegration(null);
  };

  const handleSubmit = () => {
    const data = {
      ...formData,
      logo: formData.logoUrl ? { id: formData.logoUrl, url: formData.logoUrl } : undefined,
    };
    if (editingIntegration) {
      updateMutation.mutate({ id: editingIntegration.id, data });
    } else {
      createMutation.mutate(data);
    }
  };

  const handleMediaSelect = (urls: string[]) => {
    if (urls.length > 0) {
      setFormData({ ...formData, logoUrl: urls[0] });
    }
    setOpenMediaLibrary(false);
  };

  const columns = [
    { id: 'name', label: 'Integration Name' },
    {
      id: 'category',
      label: 'Category',
      render: (_: string, row: Integration) => row.category.toUpperCase(),
    },
    {
      id: 'status',
      label: 'Status',
      render: (_: string, row: Integration) => {
        const colors: Record<IntegrationStatus, string> = {
          native: 'success',
          partner: 'primary',
          community: 'info',
          beta: 'warning',
        };
        return <span style={{ color: colors[row.status] }}>{row.status}</span>;
      },
    },
  ];

  return (
    <Box>
      <PageHeader
        title="Integrations"
        subtitle="Manage third-party integrations for your products"
        action={{
          label: 'Add Integration',
          onClick: () => handleOpenDialog(),
          variant: 'contained',
        }}
      />

      <DataTable
        columns={columns}
        rows={data?.data || []}
        total={data?.pagination?.total || 0}
        page={page}
        rowsPerPage={rowsPerPage}
        onPageChange={setPage}
        onRowsPerPageChange={setRowsPerPage}
        onEdit={handleOpenDialog}
        onDelete={(row) => deleteMutation.mutate(row.id)}
        loading={isLoading}
      />

      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
        <DialogTitle>{editingIntegration ? 'Edit Integration' : 'Create Integration'}</DialogTitle>
        <DialogContent>
          <Stack spacing={3} sx={{ mt: 2 }}>
            <TextField
              label="Integration Name"
              fullWidth
              value={formData.name}
              onChange={(e) => {
                setFormData({ ...formData, name: e.target.value });
                if (!editingIntegration) {
                  setFormData({
                    ...formData,
                    name: e.target.value,
                    slug: e.target.value.toLowerCase().replace(/\s+/g, '-'),
                  });
                }
              }}
            />
            <TextField
              label="Slug"
              fullWidth
              value={formData.slug}
              onChange={(e) => setFormData({ ...formData, slug: e.target.value })}
            />
            <TextField
              label="Description"
              fullWidth
              multiline
              rows={3}
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
            />
            <FormControl fullWidth>
              <InputLabel>Category</InputLabel>
              <Select
                value={formData.category}
                label="Category"
                onChange={(e) => setFormData({ ...formData, category: e.target.value as IntegrationCategory })}
              >
                {categories.map((cat) => (
                  <MenuItem key={cat} value={cat}>
                    {cat.toUpperCase()}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <FormControl fullWidth>
              <InputLabel>Status</InputLabel>
              <Select
                value={formData.status}
                label="Status"
                onChange={(e) => setFormData({ ...formData, status: e.target.value as IntegrationStatus })}
              >
                {statuses.map((status) => (
                  <MenuItem key={status} value={status}>
                    {status.charAt(0).toUpperCase() + status.slice(1)}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <TextField
              label="Documentation URL"
              fullWidth
              value={formData.documentationUrl}
              onChange={(e) => setFormData({ ...formData, documentationUrl: e.target.value })}
            />
            <Button variant="outlined" onClick={() => setOpenMediaLibrary(true)} fullWidth>
              {formData.logoUrl ? 'Change Logo' : 'Select Logo'}
            </Button>
            {formData.logoUrl && (
              <Box sx={{ mt: 1, textAlign: 'center' }}>
                <img src={formData.logoUrl} alt="Logo" style={{ maxHeight: 60 }} />
              </Box>
            )}
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button onClick={handleSubmit} variant="contained">
            {editingIntegration ? 'Update' : 'Create'}
          </Button>
        </DialogActions>
      </Dialog>

      <MediaLibrary
        open={openMediaLibrary}
        onClose={() => setOpenMediaLibrary(false)}
        onSelect={handleMediaSelect}
      />
    </Box>
  );
};

export default ProductsIntegrations;

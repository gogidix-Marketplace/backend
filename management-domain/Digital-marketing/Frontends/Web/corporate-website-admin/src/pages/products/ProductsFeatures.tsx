import React, { useState } from 'react';
import { Box, Button, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Stack, FormControl, InputLabel, Select, MenuItem, Checkbox, FormControlLabel } from '@mui/material';
import { Add } from '@mui/icons-material';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getFeatures, createFeature, updateFeature, deleteFeature, getProducts } from '@/services/productsApi';
import { queryKeys } from '@/services/api';
import { ProductFeature } from '@/types';
import PageHeader from '@/components/common/PageHeader';
import DataTable from '@/components/common/DataTable';
import { useSnackbar } from '@/contexts/SnackbarContext';

const ProductsFeatures: React.FC = () => {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [openDialog, setOpenDialog] = useState(false);
  const [editingFeature, setEditingFeature] = useState<ProductFeature | null>(null);
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    productId: '',
    icon: '',
    highlighted: false,
    order: 0,
  });

  const { showSuccess, showError } = useSnackbar();
  const queryClient = useQueryClient();

  const { data, isLoading } = useQuery({
    queryKey: queryKeys.products.features(),
    queryFn: () => getFeatures({ page: page + 1, limit: rowsPerPage }),
  });

  const { data: products } = useQuery({
    queryKey: queryKeys.products.list(),
    queryFn: () => getProducts(),
  });

  const createMutation = useMutation({
    mutationFn: createFeature,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.products.features() });
      showSuccess('Feature created successfully');
      handleCloseDialog();
    },
    onError: () => showError('Failed to create feature'),
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: Partial<ProductFeature> }) =>
      updateFeature(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.products.features() });
      showSuccess('Feature updated successfully');
      handleCloseDialog();
    },
    onError: () => showError('Failed to update feature'),
  });

  const deleteMutation = useMutation({
    mutationFn: deleteFeature,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.products.features() });
      showSuccess('Feature deleted successfully');
    },
    onError: () => showError('Failed to delete feature'),
  });

  const handleOpenDialog = (feature?: ProductFeature) => {
    if (feature) {
      setEditingFeature(feature);
      setFormData({
        name: feature.name,
        description: feature.description,
        productId: feature.productId,
        icon: feature.icon || '',
        highlighted: feature.highlighted,
        order: feature.order,
      });
    } else {
      setEditingFeature(null);
      setFormData({
        name: '',
        description: '',
        productId: '',
        icon: '',
        highlighted: false,
        order: 0,
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingFeature(null);
  };

  const handleSubmit = () => {
    if (editingFeature) {
      updateMutation.mutate({ id: editingFeature.id, data: formData });
    } else {
      createMutation.mutate(formData);
    }
  };

  const columns = [
    { id: 'name', label: 'Name' },
    { id: 'description', label: 'Description' },
    {
      id: 'productId',
      label: 'Product ID',
      render: (_: string, row: ProductFeature) => row.productId,
    },
    {
      id: 'highlighted',
      label: 'Highlighted',
      render: (_: string, row: ProductFeature) => (row.highlighted ? 'Yes' : 'No'),
    },
    { id: 'order', label: 'Order' },
  ];

  return (
    <Box>
      <PageHeader
        title="Product Features"
        subtitle="Manage features for your products"
        action={{
          label: 'Add Feature',
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
        <DialogTitle>{editingFeature ? 'Edit Feature' : 'Create Feature'}</DialogTitle>
        <DialogContent>
          <Stack spacing={3} sx={{ mt: 2 }}>
            <TextField
              label="Feature Name"
              fullWidth
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
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
              <InputLabel>Product</InputLabel>
              <Select
                value={formData.productId}
                label="Product"
                onChange={(e) => setFormData({ ...formData, productId: e.target.value })}
              >
                {products?.data?.map((product) => (
                  <MenuItem key={product.id} value={product.id}>
                    {product.name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <TextField
              label="Icon (emoji or code)"
              fullWidth
              value={formData.icon}
              onChange={(e) => setFormData({ ...formData, icon: e.target.value })}
            />
            <TextField
              label="Display Order"
              type="number"
              fullWidth
              value={formData.order}
              onChange={(e) => setFormData({ ...formData, order: parseInt(e.target.value) || 0 })}
            />
            <FormControlLabel
              control={
                <Checkbox
                  checked={formData.highlighted}
                  onChange={(e) => setFormData({ ...formData, highlighted: e.target.checked })}
                />
              }
              label="Highlighted Feature"
            />
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button onClick={handleSubmit} variant="contained">
            {editingFeature ? 'Update' : 'Create'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default ProductsFeatures;

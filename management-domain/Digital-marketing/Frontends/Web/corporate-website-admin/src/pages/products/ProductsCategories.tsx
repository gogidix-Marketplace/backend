import React, { useState } from 'react';
import { Box, Button, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Stack } from '@mui/material';
import { Add, Folder } from '@mui/icons-material';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getCategories, createCategory, updateCategory, deleteCategory } from '@/services/productsApi';
import { queryKeys } from '@/services/api';
import { ProductCategory } from '@/types';
import PageHeader from '@/components/common/PageHeader';
import DataTable from '@/components/common/DataTable';
import { useSnackbar } from '@/contexts/SnackbarContext';

const ProductsCategories: React.FC = () => {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [openDialog, setOpenDialog] = useState(false);
  const [editingCategory, setEditingCategory] = useState<ProductCategory | null>(null);
  const [formData, setFormData] = useState({
    name: '',
    slug: '',
    description: '',
    icon: '',
    parentId: '',
  });

  const { showSuccess, showError } = useSnackbar();
  const queryClient = useQueryClient();

  const { data, isLoading } = useQuery({
    queryKey: queryKeys.products.categories(),
    queryFn: () => getCategories({ page: page + 1, limit: rowsPerPage }),
  });

  const createMutation = useMutation({
    mutationFn: createCategory,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.products.categories() });
      showSuccess('Category created successfully');
      handleCloseDialog();
    },
    onError: () => showError('Failed to create category'),
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: Partial<ProductCategory> }) =>
      updateCategory(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.products.categories() });
      showSuccess('Category updated successfully');
      handleCloseDialog();
    },
    onError: () => showError('Failed to update category'),
  });

  const deleteMutation = useMutation({
    mutationFn: deleteCategory,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.products.categories() });
      showSuccess('Category deleted successfully');
    },
    onError: () => showError('Failed to delete category'),
  });

  const handleOpenDialog = (category?: ProductCategory) => {
    if (category) {
      setEditingCategory(category);
      setFormData({
        name: category.name,
        slug: category.slug,
        description: category.description || '',
        icon: category.icon || '',
        parentId: category.parentId || '',
      });
    } else {
      setEditingCategory(null);
      setFormData({
        name: '',
        slug: '',
        description: '',
        icon: '',
        parentId: '',
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingCategory(null);
  };

  const handleSubmit = () => {
    if (editingCategory) {
      updateMutation.mutate({ id: editingCategory.id, data: formData });
    } else {
      createMutation.mutate(formData);
    }
  };

  const columns = [
    {
      id: 'name',
      label: 'Name',
      render: (_: string, row: ProductCategory) => (
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
          {row.icon && <span>{row.icon}</span>}
          {row.name}
        </Box>
      ),
    },
    { id: 'slug', label: 'Slug' },
    {
      id: 'description',
      label: 'Description',
      render: (_: string, row: ProductCategory) => row.description || '-',
    },
    {
      id: 'parentId',
      label: 'Parent',
      render: (_: string, row: ProductCategory) => (row.parentId ? 'Yes' : 'No'),
    },
  ];

  return (
    <Box>
      <PageHeader
        title="Product Categories"
        subtitle="Organize your products into categories"
        action={{
          label: 'Add Category',
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
        <DialogTitle>{editingCategory ? 'Edit Category' : 'Create Category'}</DialogTitle>
        <DialogContent>
          <Stack spacing={3} sx={{ mt: 2 }}>
            <TextField
              label="Name"
              fullWidth
              value={formData.name}
              onChange={(e) => {
                setFormData({ ...formData, name: e.target.value });
                if (!editingCategory) {
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
            <TextField
              label="Icon (emoji)"
              fullWidth
              value={formData.icon}
              onChange={(e) => setFormData({ ...formData, icon: e.target.value })}
              helperText="Enter an emoji as the category icon"
            />
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button onClick={handleSubmit} variant="contained">
            {editingCategory ? 'Update' : 'Create'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default ProductsCategories;

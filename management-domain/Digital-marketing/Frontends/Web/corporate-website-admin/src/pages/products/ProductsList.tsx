import React, { useState } from 'react';
import { Box, Button, Dialog, DialogTitle, DialogContent, DialogActions, TextField, FormControl, InputLabel, Select, MenuItem, Stack, Checkbox, FormControlLabel, Grid, Card, CardMedia, IconButton } from '@mui/material';
import { Add, Delete, Image as ImageIcon } from '@mui/icons-material';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getProducts, createProduct, updateProduct, deleteProduct, getCategories } from '@/services/productsApi';
import { queryKeys } from '@/services/api';
import { Product, ContentStatus } from '@/types';
import PageHeader from '@/components/common/PageHeader';
import DataTable from '@/components/common/DataTable';
import MediaLibrary from '@/components/media/MediaLibrary';
import { useSnackbar } from '@/contexts/SnackbarContext';

const ProductsList: React.FC = () => {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [openDialog, setOpenDialog] = useState(false);
  const [openMediaLibrary, setOpenMediaLibrary] = useState(false);
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);
  const [selectedImages, setSelectedImages] = useState<string[]>([]);
  const [formData, setFormData] = useState({
    name: '',
    slug: '',
    description: '',
    longDescription: '',
    status: 'draft' as ContentStatus,
    categoryId: '',
    featured: false,
  });

  const { showSuccess, showError } = useSnackbar();
  const queryClient = useQueryClient();

  const { data, isLoading } = useQuery({
    queryKey: queryKeys.products.list(),
    queryFn: () => getProducts({ page: page + 1, limit: rowsPerPage }),
  });

  const { data: categories } = useQuery({
    queryKey: queryKeys.products.categories(),
    queryFn: () => getCategories(),
  });

  const createMutation = useMutation({
    mutationFn: createProduct,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.products.list() });
      showSuccess('Product created successfully');
      handleCloseDialog();
    },
    onError: () => showError('Failed to create product'),
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: Partial<Product> }) =>
      updateProduct(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.products.list() });
      showSuccess('Product updated successfully');
      handleCloseDialog();
    },
    onError: () => showError('Failed to update product'),
  });

  const deleteMutation = useMutation({
    mutationFn: deleteProduct,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.products.list() });
      showSuccess('Product deleted successfully');
    },
    onError: () => showError('Failed to delete product'),
  });

  const handleOpenDialog = (product?: Product) => {
    if (product) {
      setEditingProduct(product);
      setSelectedImages(product.images?.map((img) => img.id) || []);
      setFormData({
        name: product.name,
        slug: product.slug,
        description: product.description,
        longDescription: product.longDescription || '',
        status: product.status,
        categoryId: product.category?.id || '',
        featured: product.featured,
      });
    } else {
      setEditingProduct(null);
      setSelectedImages([]);
      setFormData({
        name: '',
        slug: '',
        description: '',
        longDescription: '',
        status: 'draft',
        categoryId: '',
        featured: false,
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingProduct(null);
  };

  const handleSubmit = () => {
    const data = {
      ...formData,
      images: selectedImages.map((id) => ({ id, url: id })),
    };
    if (editingProduct) {
      updateMutation.mutate({ id: editingProduct.id, data });
    } else {
      createMutation.mutate(data);
    }
  };

  const handleMediaSelect = (urls: string[]) => {
    setSelectedImages([...selectedImages, ...urls]);
    setOpenMediaLibrary(false);
  };

  const handleRemoveImage = (index: number) => {
    setSelectedImages(selectedImages.filter((_, i) => i !== index));
  };

  const columns = [
    {
      id: 'name',
      label: 'Product',
      render: (_: string, row: Product) => (
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          {row.logo && (
            <Box
              component="img"
              src={row.logo.url}
              alt={row.name}
              sx={{ width: 40, height: 40, objectFit: 'contain' }}
            />
          )}
          <span>{row.name}</span>
        </Box>
      ),
    },
    {
      id: 'category',
      label: 'Category',
      render: (_: string, row: Product) => row.category?.name || '-',
    },
    {
      id: 'featured',
      label: 'Featured',
      render: (_: string, row: Product) => (row.featured ? 'Yes' : 'No'),
    },
    {
      id: 'status',
      label: 'Status',
      render: (_: string, row: Product) => row.status,
    },
  ];

  return (
    <Box>
      <PageHeader
        title="Products"
        subtitle="Manage your product catalog"
        action={{
          label: 'Add Product',
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

      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="md" fullWidth>
        <DialogTitle>{editingProduct ? 'Edit Product' : 'Create Product'}</DialogTitle>
        <DialogContent>
          <Stack spacing={3} sx={{ mt: 2 }}>
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <TextField
                  label="Product Name"
                  fullWidth
                  value={formData.name}
                  onChange={(e) => {
                    setFormData({ ...formData, name: e.target.value });
                    if (!editingProduct) {
                      setFormData({
                        ...formData,
                        name: e.target.value,
                        slug: e.target.value.toLowerCase().replace(/\s+/g, '-'),
                      });
                    }
                  }}
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  label="Slug"
                  fullWidth
                  value={formData.slug}
                  onChange={(e) => setFormData({ ...formData, slug: e.target.value })}
                />
              </Grid>
            </Grid>
            <TextField
              label="Short Description"
              fullWidth
              multiline
              rows={2}
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
            />
            <TextField
              label="Long Description"
              fullWidth
              multiline
              rows={4}
              value={formData.longDescription}
              onChange={(e) => setFormData({ ...formData, longDescription: e.target.value })}
            />
            <FormControl fullWidth>
              <InputLabel>Category</InputLabel>
              <Select
                value={formData.categoryId}
                label="Category"
                onChange={(e) => setFormData({ ...formData, categoryId: e.target.value })}
              >
                {categories?.data?.map((cat) => (
                  <MenuItem key={cat.id} value={cat.id}>
                    {cat.name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <FormControl fullWidth>
              <InputLabel>Status</InputLabel>
              <Select
                value={formData.status}
                label="Status"
                onChange={(e) => setFormData({ ...formData, status: e.target.value as ContentStatus })}
              >
                <MenuItem value="draft">Draft</MenuItem>
                <MenuItem value="published">Published</MenuItem>
                <MenuItem value="archived">Archived</MenuItem>
              </Select>
            </FormControl>
            <FormControlLabel
              control={
                <Checkbox
                  checked={formData.featured}
                  onChange={(e) => setFormData({ ...formData, featured: e.target.checked })}
                />
              }
              label="Featured Product"
            />
            <Box>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 1 }}>
                <Typography variant="subtitle2">Product Images</Typography>
                <Button
                  size="small"
                  startIcon={<ImageIcon />}
                  onClick={() => setOpenMediaLibrary(true)}
                >
                  Add Images
                </Button>
              </Box>
              <Grid container spacing={1}>
                {selectedImages.map((url, index) => (
                  <Grid item key={index}>
                    <Card sx={{ position: 'relative', width: 100, height: 100 }}>
                      <CardMedia
                        component="img"
                        image={url}
                        alt={`Product ${index + 1}`}
                        sx={{ width: '100%', height: '100%', objectFit: 'cover' }}
                      />
                      <IconButton
                        size="small"
                        sx={{ position: 'absolute', top: 4, right: 4, bgcolor: 'background.paper' }}
                        onClick={() => handleRemoveImage(index)}
                      >
                        <Delete fontSize="small" />
                      </IconButton>
                    </Card>
                  </Grid>
                ))}
              </Grid>
            </Box>
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button onClick={handleSubmit} variant="contained">
            {editingProduct ? 'Update' : 'Create'}
          </Button>
        </DialogActions>
      </Dialog>

      <MediaLibrary
        open={openMediaLibrary}
        onClose={() => setOpenMediaLibrary(false)}
        onSelect={handleMediaSelect}
        multiple
      />
    </Box>
  );
};

export default ProductsList;

import React, { useState } from 'react';
import { Box, Button, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Stack, FormControl, InputLabel, Select, MenuItem, Checkbox, FormControlLabel, Card, CardContent, Typography } from '@mui/material';
import { Add } from '@mui/icons-material';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getPricingPlans, createPricingPlan, updatePricingPlan, deletePricingPlan, getProducts } from '@/services/productsApi';
import { queryKeys } from '@/services/api';
import { PricingPlan, BillingCycle } from '@/types';
import PageHeader from '@/components/common/PageHeader';
import DataTable from '@/components/common/DataTable';
import { useSnackbar } from '@/contexts/SnackbarContext';

const billingCycles: BillingCycle[] = ['monthly', 'quarterly', 'annual', 'custom'];

const ProductsPricing: React.FC = () => {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [openDialog, setOpenDialog] = useState(false);
  const [editingPlan, setEditingPlan] = useState<PricingPlan | null>(null);
  const [featuresList, setFeaturesList] = useState<string[]>([]);
  const [featureInput, setFeatureInput] = useState('');
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    price: 0,
    currency: 'USD',
    billingCycle: 'monthly' as BillingCycle,
    productId: '',
    highlighted: false,
    order: 0,
  });

  const { showSuccess, showError } = useSnackbar();
  const queryClient = useQueryClient();

  const { data, isLoading } = useQuery({
    queryKey: queryKeys.products.pricing(),
    queryFn: () => getPricingPlans({ page: page + 1, limit: rowsPerPage }),
  });

  const { data: products } = useQuery({
    queryKey: queryKeys.products.list(),
    queryFn: () => getProducts(),
  });

  const createMutation = useMutation({
    mutationFn: createPricingPlan,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.products.pricing() });
      showSuccess('Pricing plan created successfully');
      handleCloseDialog();
    },
    onError: () => showError('Failed to create pricing plan'),
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: Partial<PricingPlan> }) =>
      updatePricingPlan(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.products.pricing() });
      showSuccess('Pricing plan updated successfully');
      handleCloseDialog();
    },
    onError: () => showError('Failed to update pricing plan'),
  });

  const deleteMutation = useMutation({
    mutationFn: deletePricingPlan,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.products.pricing() });
      showSuccess('Pricing plan deleted successfully');
    },
    onError: () => showError('Failed to delete pricing plan'),
  });

  const handleOpenDialog = (plan?: PricingPlan) => {
    if (plan) {
      setEditingPlan(plan);
      setFeaturesList(plan.features || []);
      setFormData({
        name: plan.name,
        description: plan.description || '',
        price: plan.price,
        currency: plan.currency,
        billingCycle: plan.billingCycle,
        productId: plan.productId,
        highlighted: plan.highlighted,
        order: plan.order,
      });
    } else {
      setEditingPlan(null);
      setFeaturesList([]);
      setFormData({
        name: '',
        description: '',
        price: 0,
        currency: 'USD',
        billingCycle: 'monthly',
        productId: '',
        highlighted: false,
        order: 0,
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingPlan(null);
  };

  const handleAddFeature = () => {
    if (featureInput && !featuresList.includes(featureInput)) {
      setFeaturesList([...featuresList, featureInput]);
      setFeatureInput('');
    }
  };

  const handleRemoveFeature = (index: number) => {
    setFeaturesList(featuresList.filter((_, i) => i !== index));
  };

  const handleSubmit = () => {
    const data = { ...formData, features: featuresList };
    if (editingPlan) {
      updateMutation.mutate({ id: editingPlan.id, data });
    } else {
      createMutation.mutate(data);
    }
  };

  const columns = [
    { id: 'name', label: 'Plan Name' },
    {
      id: 'price',
      label: 'Price',
      render: (_: string, row: PricingPlan) =>
        `${row.currency} ${row.price}/${row.billingCycle}`,
    },
    {
      id: 'billingCycle',
      label: 'Billing Cycle',
      render: (_: string, row: PricingPlan) => row.billingCycle,
    },
    {
      id: 'highlighted',
      label: 'Featured',
      render: (_: string, row: PricingPlan) => (row.highlighted ? 'Yes' : 'No'),
    },
    { id: 'order', label: 'Display Order' },
  ];

  return (
    <Box>
      <PageHeader
        title="Pricing Plans"
        subtitle="Manage pricing plans for your products"
        action={{
          label: 'Add Plan',
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
        <DialogTitle>{editingPlan ? 'Edit Pricing Plan' : 'Create Pricing Plan'}</DialogTitle>
        <DialogContent>
          <Stack spacing={3} sx={{ mt: 2 }}>
            <TextField
              label="Plan Name"
              fullWidth
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            />
            <TextField
              label="Description"
              fullWidth
              multiline
              rows={2}
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
            />
            <Stack direction="row" spacing={2}>
              <TextField
                label="Price"
                type="number"
                fullWidth
                value={formData.price}
                onChange={(e) => setFormData({ ...formData, price: parseFloat(e.target.value) || 0 })}
              />
              <FormControl fullWidth>
                <InputLabel>Currency</InputLabel>
                <Select
                  value={formData.currency}
                  label="Currency"
                  onChange={(e) => setFormData({ ...formData, currency: e.target.value })}
                >
                  <MenuItem value="USD">USD</MenuItem>
                  <MenuItem value="EUR">EUR</MenuItem>
                  <MenuItem value="GBP">GBP</MenuItem>
                  <MenuItem value="CAD">CAD</MenuItem>
                </Select>
              </FormControl>
            </Stack>
            <FormControl fullWidth>
              <InputLabel>Billing Cycle</InputLabel>
              <Select
                value={formData.billingCycle}
                label="Billing Cycle"
                onChange={(e) => setFormData({ ...formData, billingCycle: e.target.value as BillingCycle })}
              >
                {billingCycles.map((cycle) => (
                  <MenuItem key={cycle} value={cycle}>
                    {cycle.charAt(0).toUpperCase() + cycle.slice(1)}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
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
              label="Highlight this plan"
            />
            <Card>
              <CardContent>
                <Typography variant="subtitle2" gutterBottom>Plan Features</Typography>
                <Stack spacing={2}>
                  <Box sx={{ display: 'flex', gap: 1 }}>
                    <TextField
                      fullWidth
                      size="small"
                      value={featureInput}
                      onChange={(e) => setFeatureInput(e.target.value)}
                      onKeyPress={(e) => e.key === 'Enter' && (e.preventDefault(), handleAddFeature())}
                      placeholder="Add a feature"
                    />
                    <Button variant="outlined" onClick={handleAddFeature}>Add</Button>
                  </Box>
                  <Stack spacing={1}>
                    {featuresList.map((feature, index) => (
                      <Box
                        key={index}
                        sx={{
                          display: 'flex',
                          justifyContent: 'space-between',
                          alignItems: 'center',
                          p: 1,
                          bgcolor: 'background.default',
                          borderRadius: 1,
                        }}
                      >
                        <Typography variant="body2">{feature}</Typography>
                        <Button size="small" color="error" onClick={() => handleRemoveFeature(index)}>
                          Remove
                        </Button>
                      </Box>
                    ))}
                  </Stack>
                </Stack>
              </CardContent>
            </Card>
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button onClick={handleSubmit} variant="contained">
            {editingPlan ? 'Update' : 'Create'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default ProductsPricing;

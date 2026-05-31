import React, { useState } from 'react';
import { Box, Button, Dialog, DialogTitle, DialogContent, DialogActions, TextField, FormControl, InputLabel, Select, MenuItem, Stack, Card, CardContent } from '@mui/material';
import { Add } from '@mui/icons-material';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getPressReleases, createPressRelease, updatePressRelease, deletePressRelease } from '@/services/contentApi';
import { queryKeys } from '@/services/api';
import { PressRelease, ContentStatus } from '@/types';
import PageHeader from '@/components/common/PageHeader';
import DataTable from '@/components/common/DataTable';
import RichTextEditor from '@/components/editor/RichTextEditor';
import { useSnackbar } from '@/contexts/SnackbarContext';

const ContentPressReleases: React.FC = () => {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [openDialog, setOpenDialog] = useState(false);
  const [editingRelease, setEditingRelease] = useState<PressRelease | null>(null);
  const [formData, setFormData] = useState({
    title: '',
    slug: '',
    content: '',
    excerpt: '',
    status: 'draft' as ContentStatus,
    releaseDate: new Date().toISOString().split('T')[0],
    contactName: '',
    contactEmail: '',
    contactPhone: '',
  });

  const { showSuccess, showError } = useSnackbar();
  const queryClient = useQueryClient();

  const { data, isLoading } = useQuery({
    queryKey: queryKeys.content.press(),
    queryFn: () => getPressReleases({ page: page + 1, limit: rowsPerPage }),
  });

  const createMutation = useMutation({
    mutationFn: createPressRelease,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.content.press() });
      showSuccess('Press release created successfully');
      handleCloseDialog();
    },
    onError: () => showError('Failed to create press release'),
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: Partial<PressRelease> }) =>
      updatePressRelease(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.content.press() });
      showSuccess('Press release updated successfully');
      handleCloseDialog();
    },
    onError: () => showError('Failed to update press release'),
  });

  const deleteMutation = useMutation({
    mutationFn: deletePressRelease,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.content.press() });
      showSuccess('Press release deleted successfully');
    },
    onError: () => showError('Failed to delete press release'),
  });

  const handleOpenDialog = (release?: PressRelease) => {
    if (release) {
      setEditingRelease(release);
      setFormData({
        title: release.title,
        slug: release.slug,
        content: release.content,
        excerpt: release.excerpt,
        status: release.status,
        releaseDate: release.releaseDate.split('T')[0],
        contactName: release.contactInfo?.name || '',
        contactEmail: release.contactInfo?.email || '',
        contactPhone: release.contactInfo?.phone || '',
      });
    } else {
      setEditingRelease(null);
      setFormData({
        title: '',
        slug: '',
        content: '',
        excerpt: '',
        status: 'draft',
        releaseDate: new Date().toISOString().split('T')[0],
        contactName: '',
        contactEmail: '',
        contactPhone: '',
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingRelease(null);
  };

  const handleSubmit = () => {
    const data = {
      ...formData,
      contactInfo: {
        name: formData.contactName,
        email: formData.contactEmail,
        phone: formData.contactPhone,
      },
    };
    if (editingRelease) {
      updateMutation.mutate({ id: editingRelease.id, data });
    } else {
      createMutation.mutate(data);
    }
  };

  const columns = [
    { id: 'title', label: 'Title' },
    {
      id: 'releaseDate',
      label: 'Release Date',
      render: (_: string, row: PressRelease) => new Date(row.releaseDate).toLocaleDateString(),
    },
    {
      id: 'status',
      label: 'Status',
      render: (_: string, row: PressRelease) => row.status,
    },
    {
      id: 'createdAt',
      label: 'Created',
      render: (_: string, row: PressRelease) => new Date(row.createdAt).toLocaleDateString(),
    },
  ];

  return (
    <Box>
      <PageHeader
        title="Press Releases"
        subtitle="Manage your press releases"
        action={{
          label: 'Add Press Release',
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

      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="lg" fullWidth>
        <DialogTitle>{editingRelease ? 'Edit Press Release' : 'Create Press Release'}</DialogTitle>
        <DialogContent>
          <Stack spacing={3} sx={{ mt: 2 }}>
            <TextField
              label="Title"
              fullWidth
              value={formData.title}
              onChange={(e) => {
                setFormData({ ...formData, title: e.target.value });
                if (!editingRelease) {
                  setFormData({
                    ...formData,
                    title: e.target.value,
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
              label="Excerpt"
              fullWidth
              multiline
              rows={2}
              value={formData.excerpt}
              onChange={(e) => setFormData({ ...formData, excerpt: e.target.value })}
            />
            <TextField
              label="Release Date"
              type="date"
              fullWidth
              InputLabelProps={{ shrink: true }}
              value={formData.releaseDate}
              onChange={(e) => setFormData({ ...formData, releaseDate: e.target.value })}
            />
            <FormControl fullWidth>
              <InputLabel>Status</InputLabel>
              <Select
                value={formData.status}
                label="Status"
                onChange={(e) => setFormData({ ...formData, status: e.target.value as ContentStatus })}
              >
                <MenuItem value="draft">Draft</MenuItem>
                <MenuItem value="pending_review">Pending Review</MenuItem>
                <MenuItem value="published">Published</MenuItem>
                <MenuItem value="archived">Archived</MenuItem>
              </Select>
            </FormControl>
            <Card>
              <CardContent>
                <Typography variant="subtitle2" gutterBottom>Contact Information</Typography>
                <Stack spacing={2}>
                  <TextField
                    label="Contact Name"
                    fullWidth
                    value={formData.contactName}
                    onChange={(e) => setFormData({ ...formData, contactName: e.target.value })}
                  />
                  <TextField
                    label="Contact Email"
                    fullWidth
                    type="email"
                    value={formData.contactEmail}
                    onChange={(e) => setFormData({ ...formData, contactEmail: e.target.value })}
                  />
                  <TextField
                    label="Contact Phone"
                    fullWidth
                    value={formData.contactPhone}
                    onChange={(e) => setFormData({ ...formData, contactPhone: e.target.value })}
                  />
                </Stack>
              </CardContent>
            </Card>
            <RichTextEditor
              content={formData.content}
              onChange={(content) => setFormData({ ...formData, content })}
              placeholder="Press release content..."
              minHeight={300}
            />
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button onClick={handleSubmit} variant="contained">
            {editingRelease ? 'Update' : 'Create'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default ContentPressReleases;

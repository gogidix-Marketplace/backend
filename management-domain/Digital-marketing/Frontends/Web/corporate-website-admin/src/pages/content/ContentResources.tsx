import React, { useState } from 'react';
import { Box, Button, Dialog, DialogTitle, DialogContent, DialogActions, TextField, FormControl, InputLabel, Select, MenuItem, Stack, Checkbox, FormControlLabel } from '@mui/material';
import { Add } from '@mui/icons-material';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getResources, createResource, updateResource, deleteResource } from '@/services/contentApi';
import { queryKeys } from '@/services/api';
import { Resource, ContentStatus, ResourceType, ResourceCategory } from '@/types';
import PageHeader from '@/components/common/PageHeader';
import DataTable from '@/components/common/DataTable';
import MediaLibrary from '@/components/media/MediaLibrary';
import { useSnackbar } from '@/contexts/SnackbarContext';

const resourceTypes: ResourceType[] = ['ebook', 'whitepaper', 'case_study', 'infographic', 'video', 'webinar', 'template', 'other'];
const categories: ResourceCategory[] = [
  { id: '1', name: 'Guides', slug: 'guides' },
  { id: '2', name: 'Case Studies', slug: 'case-studies' },
  { id: '3', name: 'White Papers', slug: 'white-papers' },
  { id: '4', name: 'Ebooks', slug: 'ebooks' },
];

const ContentResources: React.FC = () => {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [openDialog, setOpenDialog] = useState(false);
  const [openMediaLibrary, setOpenMediaLibrary] = useState(false);
  const [editingResource, setEditingResource] = useState<Resource | null>(null);
  const [formData, setFormData] = useState({
    title: '',
    slug: '',
    description: '',
    type: 'ebook' as ResourceType,
    categoryId: '',
    fileUrl: '',
    externalLink: '',
    thumbnailUrl: '',
    status: 'draft' as ContentStatus,
    featured: false,
  });

  const { showSuccess, showError } = useSnackbar();
  const queryClient = useQueryClient();

  const { data, isLoading } = useQuery({
    queryKey: queryKeys.content.resources(),
    queryFn: () => getResources({ page: page + 1, limit: rowsPerPage }),
  });

  const createMutation = useMutation({
    mutationFn: createResource,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.content.resources() });
      showSuccess('Resource created successfully');
      handleCloseDialog();
    },
    onError: () => showError('Failed to create resource'),
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: Partial<Resource> }) =>
      updateResource(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.content.resources() });
      showSuccess('Resource updated successfully');
      handleCloseDialog();
    },
    onError: () => showError('Failed to update resource'),
  });

  const deleteMutation = useMutation({
    mutationFn: deleteResource,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.content.resources() });
      showSuccess('Resource deleted successfully');
    },
    onError: () => showError('Failed to delete resource'),
  });

  const handleOpenDialog = (resource?: Resource) => {
    if (resource) {
      setEditingResource(resource);
      setFormData({
        title: resource.title,
        slug: resource.slug,
        description: resource.description,
        type: resource.type,
        categoryId: resource.category?.id || '',
        fileUrl: resource.file?.url || '',
        externalLink: resource.externalLink || '',
        thumbnailUrl: resource.thumbnail?.url || '',
        status: resource.status,
        featured: resource.featured,
      });
    } else {
      setEditingResource(null);
      setFormData({
        title: '',
        slug: '',
        description: '',
        type: 'ebook',
        categoryId: '',
        fileUrl: '',
        externalLink: '',
        thumbnailUrl: '',
        status: 'draft',
        featured: false,
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingResource(null);
  };

  const handleSubmit = () => {
    const data = {
      ...formData,
      file: formData.fileUrl ? { id: formData.fileUrl, url: formData.fileUrl } : undefined,
      thumbnail: formData.thumbnailUrl ? { id: formData.thumbnailUrl, url: formData.thumbnailUrl } : undefined,
    };
    if (editingResource) {
      updateMutation.mutate({ id: editingResource.id, data });
    } else {
      createMutation.mutate(data);
    }
  };

  const handleMediaSelect = (urls: string[]) => {
    if (urls.length > 0) {
      setFormData({ ...formData, fileUrl: urls[0] });
    }
    setOpenMediaLibrary(false);
  };

  const handleThumbnailSelect = (urls: string[]) => {
    if (urls.length > 0) {
      setFormData({ ...formData, thumbnailUrl: urls[0] });
    }
    setOpenMediaLibrary(false);
  };

  const columns = [
    { id: 'title', label: 'Title' },
    {
      id: 'type',
      label: 'Type',
      render: (_: string, row: Resource) => row.type.replace('_', ' '),
    },
    {
      id: 'category',
      label: 'Category',
      render: (_: string, row: Resource) => row.category?.name || '-',
    },
    {
      id: 'downloadCount',
      label: 'Downloads',
      render: (_: string, row: Resource) => row.downloadCount || 0,
    },
    {
      id: 'featured',
      label: 'Featured',
      render: (_: string, row: Resource) => (row.featured ? 'Yes' : 'No'),
    },
    {
      id: 'status',
      label: 'Status',
      render: (_: string, row: Resource) => row.status,
    },
  ];

  return (
    <Box>
      <PageHeader
        title="Resources"
        subtitle="Manage ebooks, whitepapers, case studies, and more"
        action={{
          label: 'Add Resource',
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
        <DialogTitle>{editingResource ? 'Edit Resource' : 'Create Resource'}</DialogTitle>
        <DialogContent>
          <Stack spacing={3} sx={{ mt: 2 }}>
            <TextField
              label="Title"
              fullWidth
              value={formData.title}
              onChange={(e) => {
                setFormData({ ...formData, title: e.target.value });
                if (!editingResource) {
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
              label="Description"
              fullWidth
              multiline
              rows={3}
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
            />
            <FormControl fullWidth>
              <InputLabel>Type</InputLabel>
              <Select
                value={formData.type}
                label="Type"
                onChange={(e) => setFormData({ ...formData, type: e.target.value as ResourceType })}
              >
                {resourceTypes.map((type) => (
                  <MenuItem key={type} value={type}>
                    {type.replace('_', ' ').toUpperCase()}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <FormControl fullWidth>
              <InputLabel>Category</InputLabel>
              <Select
                value={formData.categoryId}
                label="Category"
                onChange={(e) => setFormData({ ...formData, categoryId: e.target.value })}
              >
                {categories.map((cat) => (
                  <MenuItem key={cat.id} value={cat.id}>
                    {cat.name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <TextField
              label="External Link"
              fullWidth
              value={formData.externalLink}
              onChange={(e) => setFormData({ ...formData, externalLink: e.target.value })}
              helperText="Use this instead of uploading a file"
            />
            <Box>
              <Button variant="outlined" onClick={() => setOpenMediaLibrary(true)} fullWidth>
                {formData.fileUrl ? 'Change File' : 'Select File'}
              </Button>
              {formData.fileUrl && (
                <Typography variant="caption" sx={{ mt: 1, display: 'block' }}>
                  Selected: {formData.fileUrl}
                </Typography>
              )}
            </Box>
            <Box>
              <Button variant="outlined" onClick={() => setOpenMediaLibrary(true)} fullWidth>
                {formData.thumbnailUrl ? 'Change Thumbnail' : 'Select Thumbnail'}
              </Button>
              {formData.thumbnailUrl && (
                <Box sx={{ mt: 1 }}>
                  <img src={formData.thumbnailUrl} alt="Thumbnail" style={{ maxHeight: 100 }} />
                </Box>
              )}
            </Box>
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
              label="Featured Resource"
            />
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button onClick={handleSubmit} variant="contained">
            {editingResource ? 'Update' : 'Create'}
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

export default ContentResources;

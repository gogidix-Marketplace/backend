import React, { useState } from 'react';
import {
  Box,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Stack,
} from '@mui/material';
import { Add } from '@mui/icons-material';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getPages, createPage, updatePage, deletePage } from '@/services/contentApi';
import { queryKeys } from '@/services/api';
import { Page, ContentStatus } from '@/types';
import PageHeader from '@/components/common/PageHeader';
import DataTable from '@/components/common/DataTable';
import RichTextEditor from '@/components/editor/RichTextEditor';
import { useSnackbar } from '@/contexts/SnackbarContext';

const ContentPages: React.FC = () => {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [openDialog, setOpenDialog] = useState(false);
  const [editingPage, setEditingPage] = useState<Page | null>(null);
  const [formData, setFormData] = useState({
    title: '',
    slug: '',
    content: '',
    status: 'draft' as ContentStatus,
    template: 'default',
  });

  const { showSuccess, showError } = useSnackbar();
  const queryClient = useQueryClient();

  const { data, isLoading } = useQuery({
    queryKey: queryKeys.content.pages(),
    queryFn: () => getPages({ page: page + 1, limit: rowsPerPage }),
  });

  const createMutation = useMutation({
    mutationFn: createPage,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.content.pages() });
      showSuccess('Page created successfully');
      handleCloseDialog();
    },
    onError: () => showError('Failed to create page'),
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: Partial<Page> }) =>
      updatePage(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.content.pages() });
      showSuccess('Page updated successfully');
      handleCloseDialog();
    },
    onError: () => showError('Failed to update page'),
  });

  const deleteMutation = useMutation({
    mutationFn: deletePage,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.content.pages() });
      showSuccess('Page deleted successfully');
    },
    onError: () => showError('Failed to delete page'),
  });

  const handleOpenDialog = (page?: Page) => {
    if (page) {
      setEditingPage(page);
      setFormData({
        title: page.title,
        slug: page.slug,
        content: page.content,
        status: page.status,
        template: page.template,
      });
    } else {
      setEditingPage(null);
      setFormData({
        title: '',
        slug: '',
        content: '',
        status: 'draft',
        template: 'default',
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingPage(null);
  };

  const handleSubmit = () => {
    if (editingPage) {
      updateMutation.mutate({ id: editingPage.id, data: formData });
    } else {
      createMutation.mutate(formData);
    }
  };

  const columns = [
    { id: 'title', label: 'Title', render: (_: string, row: Page) => row.title },
    { id: 'slug', label: 'Slug', render: (_: string, row: Page) => `/${row.slug}` },
    {
      id: 'status',
      label: 'Status',
      render: (_: string, row: Page) => {
        const colors: Record<ContentStatus, string> = {
          draft: 'default',
          pending_review: 'warning',
          scheduled: 'info',
          published: 'success',
          archived: 'default',
        };
        return <span style={{ color: colors[row.status] }}>{row.status}</span>;
      },
    },
    {
      id: 'template',
      label: 'Template',
      render: (_: string, row: Page) => row.template,
    },
    {
      id: 'updatedAt',
      label: 'Last Updated',
      render: (_: string, row: Page) =>
        new Date(row.updatedAt).toLocaleDateString(),
    },
  ];

  return (
    <Box>
      <PageHeader
        title="Pages"
        subtitle="Manage your website pages"
        action={{ label: 'Add Page', onClick: () => handleOpenDialog(), variant: 'contained' }}
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
        <DialogTitle>{editingPage ? 'Edit Page' : 'Create Page'}</DialogTitle>
        <DialogContent>
          <Stack spacing={3} sx={{ mt: 2 }}>
            <TextField
              label="Title"
              fullWidth
              value={formData.title}
              onChange={(e) => {
                setFormData({ ...formData, title: e.target.value });
                if (!editingPage) {
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
            <FormControl fullWidth>
              <InputLabel>Status</InputLabel>
              <Select
                value={formData.status}
                label="Status"
                onChange={(e) => setFormData({ ...formData, status: e.target.value as ContentStatus })}
              >
                <MenuItem value="draft">Draft</MenuItem>
                <MenuItem value="pending_review">Pending Review</MenuItem>
                <MenuItem value="scheduled">Scheduled</MenuItem>
                <MenuItem value="published">Published</MenuItem>
                <MenuItem value="archived">Archived</MenuItem>
              </Select>
            </FormControl>
            <FormControl fullWidth>
              <InputLabel>Template</InputLabel>
              <Select
                value={formData.template}
                label="Template"
                onChange={(e) => setFormData({ ...formData, template: e.target.value })}
              >
                <MenuItem value="default">Default</MenuItem>
                <MenuItem value="landing">Landing Page</MenuItem>
                <MenuItem value="blog">Blog</MenuItem>
                <MenuItem value="full-width">Full Width</MenuItem>
              </Select>
            </FormControl>
            <RichTextEditor
              content={formData.content}
              onChange={(content) => setFormData({ ...formData, content })}
              placeholder="Page content..."
              minHeight={400}
            />
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button onClick={handleSubmit} variant="contained">
            {editingPage ? 'Update' : 'Create'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default ContentPages;

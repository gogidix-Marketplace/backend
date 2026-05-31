import React, { useState } from 'react';
import { Box, Button, Dialog, DialogTitle, DialogContent, DialogActions, TextField, FormControl, InputLabel, Select, MenuItem, Stack, Chip } from '@mui/material';
import { Add } from '@mui/icons-material';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getBlogPosts, createBlogPost, updateBlogPost, deleteBlogPost } from '@/services/contentApi';
import { queryKeys } from '@/services/api';
import { BlogPost, ContentStatus } from '@/types';
import PageHeader from '@/components/common/PageHeader';
import DataTable from '@/components/common/DataTable';
import RichTextEditor from '@/components/editor/RichTextEditor';
import { useSnackbar } from '@/contexts/SnackbarContext';

const ContentBlog: React.FC = () => {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [openDialog, setOpenDialog] = useState(false);
  const [editingPost, setEditingPost] = useState<BlogPost | null>(null);
  const [selectedTags, setSelectedTags] = useState<string[]>([]);
  const [tagInput, setTagInput] = useState('');
  const [formData, setFormData] = useState({
    title: '',
    slug: '',
    content: '',
    excerpt: '',
    status: 'draft' as ContentStatus,
  });

  const { showSuccess, showError } = useSnackbar();
  const queryClient = useQueryClient();

  const { data, isLoading } = useQuery({
    queryKey: queryKeys.content.blog(),
    queryFn: () => getBlogPosts({ page: page + 1, limit: rowsPerPage }),
  });

  const createMutation = useMutation({
    mutationFn: createBlogPost,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.content.blog() });
      showSuccess('Blog post created successfully');
      handleCloseDialog();
    },
    onError: () => showError('Failed to create blog post'),
  });

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: Partial<BlogPost> }) =>
      updateBlogPost(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.content.blog() });
      showSuccess('Blog post updated successfully');
      handleCloseDialog();
    },
    onError: () => showError('Failed to update blog post'),
  });

  const deleteMutation = useMutation({
    mutationFn: deleteBlogPost,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: queryKeys.content.blog() });
      showSuccess('Blog post deleted successfully');
    },
    onError: () => showError('Failed to delete blog post'),
  });

  const handleOpenDialog = (post?: BlogPost) => {
    if (post) {
      setEditingPost(post);
      setSelectedTags(post.tags || []);
      setFormData({
        title: post.title,
        slug: post.slug,
        content: post.content,
        excerpt: post.excerpt,
        status: post.status,
      });
    } else {
      setEditingPost(null);
      setSelectedTags([]);
      setFormData({
        title: '',
        slug: '',
        content: '',
        excerpt: '',
        status: 'draft',
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingPost(null);
    setTagInput('');
  };

  const handleSubmit = () => {
    const data = { ...formData, tags: selectedTags };
    if (editingPost) {
      updateMutation.mutate({ id: editingPost.id, data });
    } else {
      createMutation.mutate(data);
    }
  };

  const handleAddTag = () => {
    if (tagInput && !selectedTags.includes(tagInput)) {
      setSelectedTags([...selectedTags, tagInput]);
      setTagInput('');
    }
  };

  const handleDeleteTag = (tag: string) => {
    setSelectedTags(selectedTags.filter((t) => t !== tag));
  };

  const columns = [
    { id: 'title', label: 'Title' },
    {
      id: 'status',
      label: 'Status',
      render: (_: string, row: BlogPost) => (
        <Chip
          label={row.status}
          size="small"
          color={
            row.status === 'published'
              ? 'success'
              : row.status === 'pending_review'
              ? 'warning'
              : 'default'
          }
        />
      ),
    },
    {
      id: 'viewCount',
      label: 'Views',
      render: (_: string, row: BlogPost) => row.viewCount || 0,
    },
    {
      id: 'publishedAt',
      label: 'Published',
      render: (_: string, row: BlogPost) =>
        row.publishedAt ? new Date(row.publishedAt).toLocaleDateString() : '-',
    },
    {
      id: 'updatedAt',
      label: 'Last Updated',
      render: (_: string, row: BlogPost) =>
        new Date(row.updatedAt).toLocaleDateString(),
    },
  ];

  return (
    <Box>
      <PageHeader
        title="Blog Posts"
        subtitle="Manage your blog content"
        action={{
          label: 'Add Post',
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
        <DialogTitle>{editingPost ? 'Edit Blog Post' : 'Create Blog Post'}</DialogTitle>
        <DialogContent>
          <Stack spacing={3} sx={{ mt: 2 }}>
            <TextField
              label="Title"
              fullWidth
              value={formData.title}
              onChange={(e) => {
                setFormData({ ...formData, title: e.target.value });
                if (!editingPost) {
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
            <Box>
              <TextField
                label="Tags"
                fullWidth
                value={tagInput}
                onChange={(e) => setTagInput(e.target.value)}
                onKeyPress={(e) => e.key === 'Enter' && (e.preventDefault(), handleAddTag())}
                helperText="Press Enter to add a tag"
              />
              <Box sx={{ mt: 1, display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                {selectedTags.map((tag) => (
                  <Chip
                    key={tag}
                    label={tag}
                    onDelete={() => handleDeleteTag(tag)}
                    size="small"
                  />
                ))}
              </Box>
            </Box>
            <RichTextEditor
              content={formData.content}
              onChange={(content) => setFormData({ ...formData, content })}
              placeholder="Blog post content..."
              minHeight={400}
            />
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button onClick={handleSubmit} variant="contained">
            {editingPost ? 'Update' : 'Create'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default ContentBlog;

import React, { useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import {
  Box,
  Button,
  Card,
  CardContent,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Grid,
  IconButton,
  TextField,
  Typography,
  Chip,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
} from '@mui/material';
import { Add as AddIcon, Description as DescriptionIcon, Delete as DeleteIcon } from '@mui/icons-material';
import { devToolsApi } from '../api/devTools';

function Documentation() {
  const [open, setOpen] = useState(false);
  const queryClient = useQueryClient();
  const projectId = 'default';

  const { data: projects, isLoading } = useQuery({
    queryKey: ['doc-projects', projectId],
    queryFn: () => devToolsApi.getProjects(projectId).then((res) => res.data),
  });

  const createMutation = useMutation({
    mutationFn: (data) => devToolsApi.createProject(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['doc-projects'] });
      setOpen(false);
    },
  });

  const generateMutation = useMutation({
    mutationFn: ({ id, formats }) =>
      devToolsApi.generateDocumentation(id, {
        generatedBy: 'user',
        formats,
      }),
  });

  const deleteMutation = useMutation({
    mutationFn: (uuid) => devToolsApi.deleteProject(uuid),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['doc-projects'] });
    },
  });

  const handleCreate = () => {
    setOpen(true);
  };

  const handleSave = (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = {
      name: formData.get('name'),
      description: formData.get('description'),
      projectId,
      sourcePath: formData.get('sourcePath'),
      outputFormat: formData.get('outputFormat'),
      enabled: true,
    };
    createMutation.mutate(data);
  };

  const handleGenerate = (project) => {
    generateMutation.mutate({
      id: project.id,
      formats: ['markdown', 'html'],
    });
  };

  return (
    <Box>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
        <Typography variant="h4">Documentation Generator</Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={handleCreate}
          sx={{ bgcolor: '#ff9800', '&:hover': { bgcolor: '#fb8c00' } }}
        >
          New Project
        </Button>
      </Box>

      <Grid container spacing={2}>
        {isLoading ? (
          <Grid item xs={12}>
            <Typography>Loading...</Typography>
          </Grid>
        ) : projects?.content?.length > 0 ? (
          projects.content.map((project) => (
            <Grid item xs={12} md={6} key={project.uuid}>
              <Card sx={{ bgcolor: '#1e1e1e', border: '1px solid #333' }}>
                <CardContent>
                  <Box display="flex" justifyContent="space-between" alignItems="start">
                    <Box sx={{ flex: 1 }}>
                      <Typography variant="h6">{project.name}</Typography>
                      <Typography variant="body2" color="text.secondary" paragraph>
                        {project.description || 'No description'}
                      </Typography>
                      <Box mb={1}>
                        <Chip label={project.outputFormat || 'markdown'} size="small" sx={{ mr: 1 }} />
                        <Chip
                          label={project.enabled ? 'Enabled' : 'Disabled'}
                          size="small"
                          color={project.enabled ? 'success' : 'default'}
                        />
                      </Box>
                      {project.sourcePath && (
                        <Typography variant="caption" color="text.secondary">
                          Source: {project.sourcePath}
                        </Typography>
                      )}
                    </Box>
                    <Box display="flex" flexDirection="column" gap={1}>
                      <Button
                        size="small"
                        variant="contained"
                        onClick={() => handleGenerate(project)}
                        disabled={generateMutation.isPending}
                        sx={{ minWidth: 100 }}
                      >
                        Generate
                      </Button>
                      <IconButton
                        color="error"
                        onClick={() => deleteMutation.mutate(project.uuid)}
                        disabled={deleteMutation.isPending}
                      >
                        <DeleteIcon />
                      </IconButton>
                    </Box>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          ))
        ) : (
          <Grid item xs={12}>
            <Card sx={{ bgcolor: '#1e1e1e', border: '1px solid #333', p: 3, textAlign: 'center' }}>
              <DescriptionIcon sx={{ fontSize: 60, color: '#555', mb: 2 }} />
              <Typography variant="body1" color="text.secondary" paragraph>
                No documentation projects found. Create your first project to get started.
              </Typography>
            </Card>
          </Grid>
        )}
      </Grid>

      {/* Create Project Dialog */}
      <Dialog open={open} onClose={() => setOpen(false)} maxWidth="md" fullWidth>
        <DialogTitle>Create Documentation Project</DialogTitle>
        <form onSubmit={handleSave}>
          <DialogContent>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  name="name"
                  label="Project Name"
                  fullWidth
                  required
                  sx={{ '& .MuiInputBase-root': { bgcolor: '#2a2a2a' } }}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  name="description"
                  label="Description"
                  fullWidth
                  multiline
                  rows={2}
                  sx={{ '& .MuiInputBase-root': { bgcolor: '#2a2a2a' } }}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  name="sourcePath"
                  label="Source Path"
                  fullWidth
                  placeholder="/path/to/source/code"
                  sx={{ '& .MuiInputBase-root': { bgcolor: '#2a2a2a' } }}
                />
              </Grid>
              <Grid item xs={6}>
                <FormControl fullWidth>
                  <InputLabel>Output Format</InputLabel>
                  <Select name="outputFormat" label="Output Format" defaultValue="markdown">
                    <MenuItem value="markdown">Markdown</MenuItem>
                    <MenuItem value="html">HTML</MenuItem>
                    <MenuItem value="pdf">PDF</MenuItem>
                  </Select>
                </FormControl>
              </Grid>
            </Grid>
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setOpen(false)}>Cancel</Button>
            <Button type="submit" variant="contained" disabled={createMutation.isPending}>
              Create Project
            </Button>
          </DialogActions>
        </form>
      </Dialog>

      {generateMutation.isPending && (
        <Box
          sx={{
            position: 'fixed',
            bottom: 0,
            left: 0,
            right: 0,
            p: 2,
            bgcolor: '#1e1e1e',
            borderTop: '1px solid #333',
          }}
        >
          <Typography>Generating documentation...</Typography>
        </Box>
      )}
    </Box>
  );
}

export default Documentation;

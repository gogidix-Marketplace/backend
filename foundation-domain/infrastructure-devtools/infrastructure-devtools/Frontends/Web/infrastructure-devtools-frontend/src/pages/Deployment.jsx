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
  LinearProgress,
} from '@mui/material';
import { Add as AddIcon, PlayArrow as PlayIcon, Delete as DeleteIcon } from '@mui/icons-material';
import Editor from '@monaco-editor/react';
import { devToolsApi } from '../api/devTools';

function Deployment() {
  const [open, setOpen] = useState(false);
  const [script, setScript] = useState('');
  const [rollbackScript, setRollbackScript] = useState('');
  const queryClient = useQueryClient();
  const projectId = 'default';

  const { data: jobs, isLoading } = useQuery({
    queryKey: ['deploy-jobs', projectId],
    queryFn: () => devToolsApi.getJobs(projectId).then((res) => res.data),
  });

  const createMutation = useMutation({
    mutationFn: (data) => devToolsApi.createJob(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['deploy-jobs'] });
      setOpen(false);
    },
  });

  const executeMutation = useMutation({
    mutationFn: ({ id, version, commit }) =>
      devToolsApi.executeJob(id, { executedBy: 'user', version, commitSha: commit }),
  });

  const deleteMutation = useMutation({
    mutationFn: (uuid) => devToolsApi.deleteJob(uuid),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['deploy-jobs'] });
    },
  });

  const handleCreate = () => {
    setScript('');
    setRollbackScript('');
    setOpen(true);
  };

  const handleSave = (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = {
      name: formData.get('name'),
      description: formData.get('description'),
      projectId,
      type: formData.get('type') || 'script',
      targetEnvironment: formData.get('targetEnvironment'),
      deploymentScript: script,
      rollbackScript: rollbackScript,
      enabled: true,
    };
    createMutation.mutate(data);
  };

  const getEnvironmentColor = (env) => {
    switch (env?.toLowerCase()) {
      case 'production':
        return '#f44336';
      case 'staging':
        return '#ff9800';
      case 'development':
        return '#2196f3';
      default:
        return '#9e9e9e';
    }
  };

  return (
    <Box>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
        <Typography variant="h4">Deployment</Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={handleCreate}
          sx={{ bgcolor: '#4caf50', '&:hover': { bgcolor: '#43a047' } }}
        >
          New Deployment Job
        </Button>
      </Box>

      <Grid container spacing={2}>
        {isLoading ? (
          <Grid item xs={12}>
            <Typography>Loading...</Typography>
          </Grid>
        ) : jobs?.content?.length > 0 ? (
          jobs.content.map((job) => (
            <Grid item xs={12} md={6} key={job.uuid}>
              <Card sx={{ bgcolor: '#1e1e1e', border: '1px solid #333' }}>
                <CardContent>
                  <Box display="flex" justifyContent="space-between" alignItems="start">
                    <Box sx={{ flex: 1 }}>
                      <Typography variant="h6">{job.name}</Typography>
                      <Typography variant="body2" color="text.secondary">
                        {job.description || 'No description'}
                      </Typography>
                      <Box mt={1}>
                        <Chip
                          label={job.targetEnvironment}
                          size="small"
                          sx={{
                            bgcolor: getEnvironmentColor(job.targetEnvironment),
                            color: 'white',
                            mr: 1,
                          }}
                        />
                        <Chip label={job.type} size="small" variant="outlined" />
                      </Box>
                    </Box>
                    <Box>
                      <IconButton color="primary">
                        <PlayIcon />
                      </IconButton>
                      <IconButton
                        color="error"
                        onClick={() => deleteMutation.mutate(job.uuid)}
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
              <Typography variant="body1" color="text.secondary">
                No deployment jobs found. Create your first deployment job to get started.
              </Typography>
            </Card>
          </Grid>
        )}
      </Grid>

      {/* Create Job Dialog */}
      <Dialog open={open} onClose={() => setOpen(false)} maxWidth="lg" fullWidth>
        <DialogTitle>Create New Deployment Job</DialogTitle>
        <form onSubmit={handleSave}>
          <DialogContent>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  name="name"
                  label="Job Name"
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
              <Grid item xs={6}>
                <FormControl fullWidth required>
                  <InputLabel>Type</InputLabel>
                  <Select name="type" label="Type" defaultValue="script">
                    <MenuItem value="script">Script</MenuItem>
                    <MenuItem value="docker">Docker</MenuItem>
                    <MenuItem value="ssh">SSH</MenuItem>
                    <MenuItem value="kubernetes">Kubernetes</MenuItem>
                  </Select>
                </FormControl>
              </Grid>
              <Grid item xs={6}>
                <FormControl fullWidth required>
                  <InputLabel>Environment</InputLabel>
                  <Select name="targetEnvironment" label="Environment" defaultValue="development">
                    <MenuItem value="development">Development</MenuItem>
                    <MenuItem value="staging">Staging</MenuItem>
                    <MenuItem value="production">Production</MenuItem>
                  </Select>
                </FormControl>
              </Grid>
              <Grid item xs={12}>
                <Typography variant="subtitle2" gutterBottom>
                  Deployment Script
                </Typography>
                <Box sx={{ border: '1px solid #333', borderRadius: 1 }}>
                  <Editor
                    height="200px"
                    defaultLanguage="bash"
                    theme="vs-dark"
                    value={script}
                    onChange={(value) => setScript(value || '')}
                    options={{
                      minimap: { enabled: false },
                      fontSize: 14,
                    }}
                  />
                </Box>
              </Grid>
              <Grid item xs={12}>
                <Typography variant="subtitle2" gutterBottom>
                  Rollback Script (Optional)
                </Typography>
                <Box sx={{ border: '1px solid #333', borderRadius: 1 }}>
                  <Editor
                    height="150px"
                    defaultLanguage="bash"
                    theme="vs-dark"
                    value={rollbackScript}
                    onChange={(value) => setRollbackScript(value || '')}
                    options={{
                      minimap: { enabled: false },
                      fontSize: 14,
                    }}
                  />
                </Box>
              </Grid>
            </Grid>
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setOpen(false)}>Cancel</Button>
            <Button type="submit" variant="contained" disabled={createMutation.isPending}>
              Create Job
            </Button>
          </DialogActions>
        </form>
      </Dialog>

      {executeMutation.isPending && (
        <Box sx={{ position: 'fixed', bottom: 0, left: 0, right: 0, p: 2, bgcolor: '#1e1e1e' }}>
          <Box display="flex" alignItems="center" gap={2}>
            <LinearProgress sx={{ flex: 1 }} />
            <Typography>Deploying...</Typography>
          </Box>
        </Box>
      )}
    </Box>
  );
}

export default Deployment;

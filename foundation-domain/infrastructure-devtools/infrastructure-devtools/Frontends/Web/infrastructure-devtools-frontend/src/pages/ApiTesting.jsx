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
  List,
  ListItem,
  ListItemText,
  TextField,
  Typography,
  Chip,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
} from '@mui/material';
import { Add as AddIcon, PlayArrow as PlayIcon, Delete as DeleteIcon } from '@mui/icons-material';
import { devToolsApi } from '../api/devTools';

function ApiTesting() {
  const [open, setOpen] = useState(false);
  const [selectedTest, setSelectedTest] = useState(null);
  const queryClient = useQueryClient();
  const projectId = 'default';

  const { data: tests, isLoading } = useQuery({
    queryKey: ['api-tests', projectId],
    queryFn: () => devToolsApi.getTestCases(projectId).then((res) => res.data),
  });

  const createMutation = useMutation({
    mutationFn: (data) => devToolsApi.createTestCase(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['api-tests'] });
      setOpen(false);
    },
  });

  const executeMutation = useMutation({
    mutationFn: (id) => devToolsApi.executeTestCase(id),
  });

  const deleteMutation = useMutation({
    mutationFn: (uuid) => devToolsApi.deleteTestCase(uuid),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['api-tests'] });
    },
  });

  const handleCreate = () => {
    setSelectedTest(null);
    setOpen(true);
  };

  const handleSave = (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = {
      name: formData.get('name'),
      description: formData.get('description'),
      projectId,
      method: formData.get('method'),
      url: formData.get('url'),
      expectedStatusCode: parseInt(formData.get('expectedStatusCode')),
      enabled: true,
    };
    createMutation.mutate(data);
  };

  return (
    <Box>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
        <Typography variant="h4">API Testing</Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={handleCreate}
          sx={{ bgcolor: '#00bcd4', '&:hover': { bgcolor: '#00acc1' } }}
        >
          New Test Case
        </Button>
      </Box>

      <Grid container spacing={2}>
        {isLoading ? (
          <Grid item xs={12}>
            <Typography>Loading...</Typography>
          </Grid>
        ) : tests?.content?.length > 0 ? (
          tests.content.map((test) => (
            <Grid item xs={12} md={6} key={test.uuid}>
              <Card sx={{ bgcolor: '#1e1e1e', border: '1px solid #333' }}>
                <CardContent>
                  <Box display="flex" justifyContent="space-between" alignItems="start">
                    <Box>
                      <Typography variant="h6">{test.name}</Typography>
                      <Typography variant="body2" color="text.secondary">
                        {test.description || 'No description'}
                      </Typography>
                      <Box mt={1}>
                        <Chip label={test.method} size="small" sx={{ mr: 1 }} />
                        <Chip label={test.environment || 'default'} size="small" variant="outlined" />
                      </Box>
                    </Box>
                    <Box>
                      <IconButton
                        color="primary"
                        onClick={() => executeMutation.mutate(test.id)}
                        disabled={executeMutation.isPending}
                      >
                        <PlayIcon />
                      </IconButton>
                      <IconButton
                        color="error"
                        onClick={() => deleteMutation.mutate(test.uuid)}
                        disabled={deleteMutation.isPending}
                      >
                        <DeleteIcon />
                      </IconButton>
                    </Box>
                  </Box>
                  <Typography variant="caption" color="text.secondary" sx={{ mt: 1, display: 'block' }}>
                    {test.method} {test.url}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))
        ) : (
          <Grid item xs={12}>
            <Card sx={{ bgcolor: '#1e1e1e', border: '1px solid #333', p: 3, textAlign: 'center' }}>
              <Typography variant="body1" color="text.secondary">
                No test cases found. Create your first test case to get started.
              </Typography>
            </Card>
          </Grid>
        )}
      </Grid>

      <Dialog open={open} onClose={() => setOpen(false)} maxWidth="md" fullWidth>
        <DialogTitle>Create New Test Case</DialogTitle>
        <form onSubmit={handleSave}>
          <DialogContent>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  name="name"
                  label="Test Name"
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
                  <InputLabel>Method</InputLabel>
                  <Select name="method" label="Method" defaultValue="GET">
                    <MenuItem value="GET">GET</MenuItem>
                    <MenuItem value="POST">POST</MenuItem>
                    <MenuItem value="PUT">PUT</MenuItem>
                    <MenuItem value="DELETE">DELETE</MenuItem>
                    <MenuItem value="PATCH">PATCH</MenuItem>
                  </Select>
                </FormControl>
              </Grid>
              <Grid item xs={6}>
                <TextField
                  name="expectedStatusCode"
                  label="Expected Status"
                  type="number"
                  fullWidth
                  required
                  defaultValue={200}
                  sx={{ '& .MuiInputBase-root': { bgcolor: '#2a2a2a' } }}
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  name="url"
                  label="URL"
                  fullWidth
                  required
                  placeholder="https://api.example.com/endpoint"
                  sx={{ '& .MuiInputBase-root': { bgcolor: '#2a2a2a' } }}
                />
              </Grid>
            </Grid>
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setOpen(false)}>Cancel</Button>
            <Button type="submit" variant="contained" disabled={createMutation.isPending}>
              Create
            </Button>
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
}

export default ApiTesting;

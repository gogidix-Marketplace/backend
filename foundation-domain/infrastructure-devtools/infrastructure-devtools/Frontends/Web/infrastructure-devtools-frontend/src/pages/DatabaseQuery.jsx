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
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from '@mui/material';
import {
  Add as AddIcon,
  PlayArrow as PlayIcon,
  Delete as DeleteIcon,
  Code as CodeIcon,
} from '@mui/icons-material';
import Editor from '@monaco-editor/react';
import { devToolsApi } from '../api/devTools';

function DatabaseQuery() {
  const [open, setOpen] = useState(false);
  const [editorOpen, setEditorOpen] = useState(false);
  const [queryResults, setQueryResults] = useState(null);
  const [selectedQuery, setSelectedQuery] = useState(null);
  const [querySql, setQuerySql] = useState('');
  const queryClient = useQueryClient();
  const projectId = 'default';

  const { data: queries, isLoading } = useQuery({
    queryKey: ['db-queries', projectId],
    queryFn: () => devToolsApi.getQueries(projectId).then((res) => res.data),
  });

  const createMutation = useMutation({
    mutationFn: (data) => devToolsApi.createQuery(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['db-queries'] });
      setOpen(false);
    },
  });

  const executeMutation = useMutation({
    mutationFn: ({ id, sql, params }) =>
      id ? devToolsApi.executeQuery(id, params || {}) : devToolsApi.executeAdHocQuery(sql, 'default', params || {}),
    onSuccess: (res) => {
      setQueryResults(res.data);
      setEditorOpen(true);
    },
  });

  const deleteMutation = useMutation({
    mutationFn: (uuid) => devToolsApi.deleteQuery(uuid),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['db-queries'] });
    },
  });

  const handleCreate = () => {
    setSelectedQuery(null);
    setQuerySql('');
    setOpen(true);
  };

  const handleSave = (e) => {
    e.preventDefault();
    const formData = new FormData(e.target);
    const data = {
      name: formData.get('name'),
      description: formData.get('description'),
      projectId,
      databaseName: formData.get('databaseName') || 'default',
      query: querySql,
      queryType: 'SELECT',
      enabled: true,
    };
    createMutation.mutate(data);
  };

  const handleExecute = (query) => {
    executeMutation.mutate({ id: query.id, sql: null, params: {} });
  };

  const handleAdHocExecute = () => {
    executeMutation.mutate({ id: null, sql: querySql, params: {} });
  };

  return (
    <Box>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
        <Typography variant="h4">Database Query</Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={handleCreate}
          sx={{ bgcolor: '#ff4081', '&:hover': { bgcolor: '#e91e63' } }}
        >
          New Query
        </Button>
      </Box>

      <Grid container spacing={2} mb={3}>
        {isLoading ? (
          <Grid item xs={12}>
            <Typography>Loading...</Typography>
          </Grid>
        ) : queries?.content?.length > 0 ? (
          queries.content.map((query) => (
            <Grid item xs={12} md={6} key={query.uuid}>
              <Card sx={{ bgcolor: '#1e1e1e', border: '1px solid #333' }}>
                <CardContent>
                  <Box display="flex" justifyContent="space-between" alignItems="start">
                    <Box sx={{ flex: 1 }}>
                      <Typography variant="h6">{query.name}</Typography>
                      <Typography variant="body2" color="text.secondary">
                        {query.description || 'No description'}
                      </Typography>
                      <Box mt={1}>
                        <Chip label={query.databaseName} size="small" sx={{ mr: 1 }} />
                        <Chip label={query.queryType || 'SELECT'} size="small" variant="outlined" />
                      </Box>
                    </Box>
                    <Box>
                      <IconButton
                        color="primary"
                        onClick={() => handleExecute(query)}
                        disabled={executeMutation.isPending}
                      >
                        <PlayIcon />
                      </IconButton>
                      <IconButton
                        color="error"
                        onClick={() => deleteMutation.mutate(query.uuid)}
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
                No saved queries found. Create your first query to get started.
              </Typography>
            </Card>
          </Grid>
        )}
      </Grid>

      {/* Query Results Dialog */}
      <Dialog
        open={editorOpen}
        onClose={() => setEditorOpen(false)}
        maxWidth="lg"
        fullWidth
      >
        <DialogTitle>Query Results</DialogTitle>
        <DialogContent>
          {queryResults?.rows && queryResults.rows.length > 0 ? (
            <TableContainer component={Paper} sx={{ bgcolor: '#1e1e1e' }}>
              <Table>
                <TableHead>
                  <TableRow>
                    {Object.keys(queryResults.rows[0]).map((key) => (
                      <TableCell key={key}>{key}</TableCell>
                    ))}
                  </TableRow>
                </TableHead>
                <TableBody>
                  {queryResults.rows.map((row, idx) => (
                    <TableRow key={idx}>
                      {Object.values(row).map((val, i) => (
                        <TableCell key={i}>{String(val)}</TableCell>
                      ))}
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          ) : (
            <Typography color="text.secondary">No results</Typography>
          )}
          {queryResults?.truncated && (
            <Typography variant="caption" color="warning" sx={{ mt: 1, display: 'block' }}>
              Results truncated. Use maxRows to show more.
            </Typography>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setEditorOpen(false)}>Close</Button>
        </DialogActions>
      </Dialog>

      {/* Create/Edit Dialog */}
      <Dialog open={open} onClose={() => setOpen(false)} maxWidth="lg" fullWidth>
        <DialogTitle>Create New Query</DialogTitle>
        <form onSubmit={handleSave}>
          <DialogContent>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  name="name"
                  label="Query Name"
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
                  sx={{ '& .MuiInputBase-root': { bgcolor: '#2a2a2a' } }}
                />
              </Grid>
              <Grid item xs={6}>
                <TextField
                  name="databaseName"
                  label="Database"
                  fullWidth
                  defaultValue="default"
                  sx={{ '& .MuiInputBase-root': { bgcolor: '#2a2a2a' } }}
                />
              </Grid>
              <Grid item xs={12}>
                <Typography variant="subtitle2" gutterBottom>
                  SQL Query
                </Typography>
                <Box sx={{ border: '1px solid #333', borderRadius: 1 }}>
                  <Editor
                    height="200px"
                    defaultLanguage="sql"
                    theme="vs-dark"
                    value={querySql}
                    onChange={(value) => setQuerySql(value || '')}
                    options={{
                      minimap: { enabled: false },
                      fontSize: 14,
                      lineNumbers: 'on',
                    }}
                  />
                </Box>
              </Grid>
            </Grid>
          </DialogContent>
          <DialogActions>
            <Button onClick={() => setOpen(false)}>Cancel</Button>
            <Button type="button" onClick={handleAdHocExecute} disabled={executeMutation.isPending}>
              Run Query
            </Button>
            <Button type="submit" variant="contained" disabled={createMutation.isPending}>
              Save Query
            </Button>
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
}

export default DatabaseQuery;

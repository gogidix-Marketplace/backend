import React, { useState } from 'react';
import { Box, Typography, Card, CardContent, Accordion, AccordionSummary, AccordionDetails, Chip, Button, TextField, Dialog, DialogTitle, DialogContent, DialogActions, Stack } from '@mui/material';
import { ExpandMore, Add, Code, Get, Post, Put, Delete, Patch } from '@mui/icons-material';
import PageHeader from '@/components/common/PageHeader';
import RichTextEditor from '@/components/editor/RichTextEditor';

const DeveloperApiDocs: React.FC = () => {
  const [openDialog, setOpenDialog] = useState(false);
  const [selectedVersion, setSelectedVersion] = useState('v2');

  // Mock API documentation data
  const apiSections = [
    {
      title: 'Authentication',
      endpoints: [
        {
          method: 'POST',
          path: '/auth/login',
          summary: 'User login',
          description: 'Authenticate a user with email and password',
        },
        {
          method: 'POST',
          path: '/auth/refresh',
          summary: 'Refresh token',
          description: 'Get a new access token using refresh token',
        },
      ],
    },
    {
      title: 'Content',
      endpoints: [
        {
          method: 'GET',
          path: '/content/pages',
          summary: 'List pages',
          description: 'Get a paginated list of all pages',
        },
        {
          method: 'GET',
          path: '/content/pages/:id',
          summary: 'Get page',
          description: 'Get a single page by ID',
        },
        {
          method: 'POST',
          path: '/content/pages',
          summary: 'Create page',
          description: 'Create a new page',
        },
        {
          method: 'PUT',
          path: '/content/pages/:id',
          summary: 'Update page',
          description: 'Update an existing page',
        },
        {
          method: 'DELETE',
          path: '/content/pages/:id',
          summary: 'Delete page',
          description: 'Delete a page',
        },
      ],
    },
    {
      title: 'Products',
      endpoints: [
        {
          method: 'GET',
          path: '/products',
          summary: 'List products',
          description: 'Get a paginated list of all products',
        },
        {
          method: 'GET',
          path: '/products/:id',
          summary: 'Get product',
          description: 'Get a single product by ID',
        },
      ],
    },
  ];

  const getMethodIcon = (method: string) => {
    const icons: Record<string, React.ReactNode> = {
      GET: <Get />,
      POST: <Post />,
      PUT: <Put />,
      DELETE: <Delete />,
      PATCH: <Patch />,
    };
    return icons[method];
  };

  const getMethodColor = (method: string): 'success' | 'info' | 'warning' | 'error' | 'default' => {
    const colors: Record<string, 'success' | 'info' | 'warning' | 'error' | 'default'> = {
      GET: 'success',
      POST: 'info',
      PUT: 'warning',
      DELETE: 'error',
      PATCH: 'default',
    };
    return colors[method];
  };

  return (
    <Box>
      <PageHeader
        title="API Documentation"
        subtitle="Manage and view API documentation for developers"
        action={{
          label: 'Add Endpoint',
          onClick: () => setOpenDialog(true),
          variant: 'contained',
        }}
      />

      <Box sx={{ mb: 3, display: 'flex', gap: 2, alignItems: 'center' }}>
        <Typography variant="body2">Version:</Typography>
        {['v1', 'v2', 'beta'].map((version) => (
          <Chip
            key={version}
            label={version}
            onClick={() => setSelectedVersion(version)}
            color={selectedVersion === version ? 'primary' : 'default'}
            sx={{ cursor: 'pointer' }}
          />
        ))}
        <TextField
          size="small"
          placeholder="Search endpoints..."
          sx={{ ml: 'auto', minWidth: 300 }}
        />
      </Box>

      <Box sx={{ display: 'flex', gap: 3 }}>
        <Box sx={{ flex: 1 }}>
          {apiSections.map((section, index) => (
            <Card key={index} sx={{ mb: 2 }}>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  {section.title}
                </Typography>
                <Stack spacing={1}>
                  {section.endpoints.map((endpoint, idx) => (
                    <Card
                      key={idx}
                      variant="outlined"
                      sx={{ cursor: 'pointer', '&:hover': { bgcolor: 'action.hover' } }}
                    >
                      <Box sx={{ p: 2 }}>
                        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 1 }}>
                          <Chip
                            icon={getMethodIcon(endpoint.method)}
                            label={endpoint.method}
                            size="small"
                            color={getMethodColor(endpoint.method)}
                          />
                          <Typography variant="body2" sx={{ fontFamily: 'monospace' }}>
                            {endpoint.path}
                          </Typography>
                        </Box>
                        <Typography variant="subtitle2">{endpoint.summary}</Typography>
                        <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5 }}>
                          {endpoint.description}
                        </Typography>
                      </Box>
                    </Card>
                  ))}
                </Stack>
              </CardContent>
            </Card>
          ))}
        </Box>
      </Box>

      <Dialog open={openDialog} onClose={() => setOpenDialog(false)} maxWidth="lg" fullWidth>
        <DialogTitle>Add API Endpoint</DialogTitle>
        <DialogContent>
          <Stack spacing={3} sx={{ mt: 2 }}>
            <TextField label="Endpoint Title" fullWidth />
            <Stack direction="row" spacing={2}>
              <FormControl fullWidth>
                <TextField
                  select
                  label="Method"
                  defaultValue="GET"
                  SelectProps={{ native: true }}
                >
                  <option value="GET">GET</option>
                  <option value="POST">POST</option>
                  <option value="PUT">PUT</option>
                  <option value="DELETE">DELETE</option>
                  <option value="PATCH">PATCH</option>
                </TextField>
              </FormControl>
              <TextField label="Path" fullWidth placeholder="/api/endpoint" />
            </Stack>
            <TextField label="Summary" fullWidth />
            <RichTextEditor
              content=""
              onChange={() => {}}
              placeholder="Endpoint description..."
              minHeight={200}
            />
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)}>Cancel</Button>
          <Button variant="contained">Save</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default DeveloperApiDocs;

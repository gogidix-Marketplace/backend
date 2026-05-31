import React, { useState } from 'react';
import { Box, Button, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Stack, FormControl, InputLabel, Select, MenuItem, Card, CardContent, Typography, Chip, Switch, FormControlLabel } from '@mui/material';
import { Add, AutoFixHigh } from '@mui/icons-material';
import PageHeader from '@/components/common/PageHeader';
import { Workflow, WorkflowType } from '@/types';

const mockWorkflows: Workflow[] = [
  {
    id: '1',
    name: 'Content Approval Workflow',
    description: 'Auto-assign content for review when status changes to pending_review',
    type: 'content_approval',
    triggers: [{ type: 'status_change', config: { from: 'draft', to: 'pending_review' } }],
    actions: [{ type: 'assign_user', config: { userId: 'editor-id' } }],
    active: true,
    createdAt: '2023-10-01',
    updatedAt: '2024-01-01',
  },
  {
    id: '2',
    name: 'Lead Assignment',
    description: 'Assign new demo requests to sales team based on company size',
    type: 'lead_assignment',
    triggers: [{ type: 'form_submission', config: { form: 'demo_request' } }],
    actions: [{ type: 'assign_user', config: { userId: 'sales-id' } }],
    conditions: [{ field: 'companySize', operator: 'greater_than', value: '100' }],
    active: true,
    createdAt: '2023-11-15',
    updatedAt: '2024-01-01',
  },
  {
    id: '3',
    name: 'Partner Application Notification',
    description: 'Notify partner manager of new applications',
    type: 'partner_application',
    triggers: [{ type: 'form_submission', config: { form: 'partner_application' } }],
    actions: [{ type: 'send_email', config: { template: 'new_partner_application' } }],
    active: false,
    createdAt: '2023-12-01',
    updatedAt: '2024-01-01',
  },
];

const SettingsWorkflows: React.FC = () => {
  const [openDialog, setOpenDialog] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    type: 'content_approval' as WorkflowType,
  });

  const handleOpenDialog = () => {
    setFormData({ name: '', description: '', type: 'content_approval' });
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
  };

  const handleSubmit = () => {
    console.log('Create workflow:', formData);
    handleCloseDialog();
  };

  const handleToggleActive = (workflowId: string) => {
    console.log('Toggle workflow:', workflowId);
  };

  return (
    <Box>
      <PageHeader
        title="Workflows"
        subtitle="Automate business processes with custom workflows"
        action={{
          label: 'Create Workflow',
          onClick: handleOpenDialog,
          variant: 'contained',
          startIcon: <Add />,
        }}
      />

      <Stack spacing={2}>
        {mockWorkflows.map((workflow) => (
          <Card key={workflow.id}>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'start', justifyContent: 'space-between' }}>
                <Box sx={{ flex: 1 }}>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 1 }}>
                    <Typography variant="h6">{workflow.name}</Typography>
                    <Chip label={workflow.type.replace('_', ' ')} size="small" variant="outlined" />
                    <Chip
                      label={workflow.active ? 'Active' : 'Inactive'}
                      size="small"
                      color={workflow.active ? 'success' : 'default'}
                    />
                  </Box>
                  <Typography variant="body2" color="text.secondary" gutterBottom>
                    {workflow.description}
                  </Typography>
                  <Stack direction="row" spacing={2} sx={{ mt: 2 }}>
                    <Typography variant="caption">
                      Trigger: <strong>{workflow.triggers[0].type.replace('_', ' ')}</strong>
                    </Typography>
                    <Typography variant="caption">
                      Action: <strong>{workflow.actions[0].type.replace('_', ' ')}</strong>
                    </Typography>
                  </Stack>
                </Box>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                  <FormControlLabel
                    control={
                      <Switch
                        checked={workflow.active}
                        onChange={() => handleToggleActive(workflow.id)}
                      />
                    }
                    label=""
                  />
                  <Button size="small" variant="outlined">Edit</Button>
                </Box>
              </Box>
            </CardContent>
          </Card>
        ))}
      </Stack>

      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
        <DialogTitle>Create Workflow</DialogTitle>
        <DialogContent>
          <Stack spacing={3} sx={{ mt: 2 }}>
            <TextField
              label="Workflow Name"
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
            <FormControl fullWidth>
              <InputLabel>Workflow Type</InputLabel>
              <Select
                value={formData.type}
                label="Workflow Type"
                onChange={(e) => setFormData({ ...formData, type: e.target.value as WorkflowType })}
              >
                <MenuItem value="content_approval">Content Approval</MenuItem>
                <MenuItem value="lead_assignment">Lead Assignment</MenuItem>
                <MenuItem value="partner_application">Partner Application</MenuItem>
                <MenuItem value="job_application">Job Application</MenuItem>
                <MenuItem value="custom">Custom</MenuItem>
              </Select>
            </FormControl>
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button onClick={handleSubmit} variant="contained">Create</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default SettingsWorkflows;

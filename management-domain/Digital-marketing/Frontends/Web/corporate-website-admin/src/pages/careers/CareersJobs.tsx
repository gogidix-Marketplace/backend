import React, { useState } from 'react';
import { Box, Button, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Stack, FormControl, InputLabel, Select, MenuItem, Checkbox, FormControlLabel } from '@mui/material';
import { Add, Work } from '@mui/icons-material';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import PageHeader from '@/components/common/PageHeader';
import DataTable from '@/components/common/DataTable';
import { useSnackbar } from '@/contexts/SnackbarContext';
import { Job, JobStatus, EmploymentType, ExperienceLevel } from '@/types';

// Mock API functions
const getJobs = async () => ({ data: mockJobs, pagination: { total: mockJobs.length } });
const createJob = async (data: any) => ({ job: { ...data, id: Math.random().toString() } });
const updateJob = async ({ id, data }: any) => ({ job: { ...data, id } });
const deleteJob = async (id: string) => {};

const mockJobs: Job[] = [
  {
    id: '1',
    title: 'Senior Frontend Developer',
    slug: 'senior-frontend-developer',
    description: 'We are looking for a senior frontend developer...',
    responsibilities: ['Build modern web applications', 'Mentor junior developers'],
    requirements: ['5+ years experience', 'React expertise'],
    benefits: ['Remote work', 'Health insurance'],
    location: { type: 'remote' },
    type: 'full_time',
    department: 'Engineering',
    experience: 'senior',
    salary: { min: 80000, max: 120000, currency: 'USD', period: 'yearly' },
    status: 'open',
    featured: true,
    createdAt: '2024-01-01',
    updatedAt: '2024-01-01',
  },
];

const CareersJobs: React.FC = () => {
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [openDialog, setOpenDialog] = useState(false);
  const [editingJob, setEditingJob] = useState<Job | null>(null);
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    department: '',
    type: 'full_time' as EmploymentType,
    experience: 'mid' as ExperienceLevel,
    location: 'remote' as string,
    status: 'draft' as JobStatus,
    featured: false,
  });

  const { showSuccess, showError } = useSnackbar();
  const queryClient = useQueryClient();

  const { data, isLoading } = useQuery({
    queryKey: ['careers', 'jobs'],
    queryFn: () => getJobs(),
  });

  const createMutation = useMutation({
    mutationFn: createJob,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['careers', 'jobs'] });
      showSuccess('Job created successfully');
      handleCloseDialog();
    },
    onError: () => showError('Failed to create job'),
  });

  const updateMutation = useMutation({
    mutationFn: updateJob,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['careers', 'jobs'] });
      showSuccess('Job updated successfully');
      handleCloseDialog();
    },
    onError: () => showError('Failed to update job'),
  });

  const deleteMutation = useMutation({
    mutationFn: deleteJob,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['careers', 'jobs'] });
      showSuccess('Job deleted successfully');
    },
    onError: () => showError('Failed to delete job'),
  });

  const handleOpenDialog = (job?: Job) => {
    if (job) {
      setEditingJob(job);
      setFormData({
        title: job.title,
        description: job.description,
        department: job.department,
        type: job.type,
        experience: job.experience || 'mid',
        location: typeof job.location === 'object' ? job.location.type : job.location,
        status: job.status,
        featured: job.featured,
      });
    } else {
      setEditingJob(null);
      setFormData({
        title: '',
        description: '',
        department: '',
        type: 'full_time',
        experience: 'mid',
        location: 'remote',
        status: 'draft',
        featured: false,
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingJob(null);
  };

  const handleSubmit = () => {
    if (editingJob) {
      updateMutation.mutate({ id: editingJob.id, data: formData });
    } else {
      createMutation.mutate(formData);
    }
  };

  const columns = [
    { id: 'title', label: 'Position' },
    { id: 'department', label: 'Department' },
    {
      id: 'type',
      label: 'Type',
      render: (_: string, row: Job) => row.type.replace('_', ' '),
    },
    {
      id: 'location',
      label: 'Location',
      render: (_: string, row: Job) => {
        if (typeof row.location === 'object') return row.location.type;
        return row.location;
      },
    },
    {
      id: 'status',
      label: 'Status',
      render: (_: string, row: Job) => row.status,
    },
    {
      id: 'featured',
      label: 'Featured',
      render: (_: string, row: Job) => (row.featured ? 'Yes' : 'No'),
    },
  ];

  return (
    <Box>
      <PageHeader
        title="Jobs"
        subtitle="Manage job postings"
        action={{
          label: 'Add Job',
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
        <DialogTitle>{editingJob ? 'Edit Job' : 'Create Job'}</DialogTitle>
        <DialogContent>
          <Stack spacing={3} sx={{ mt: 2 }}>
            <TextField
              label="Job Title"
              fullWidth
              value={formData.title}
              onChange={(e) => setFormData({ ...formData, title: e.target.value })}
            />
            <TextField
              label="Description"
              fullWidth
              multiline
              rows={4}
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
            />
            <TextField
              label="Department"
              fullWidth
              value={formData.department}
              onChange={(e) => setFormData({ ...formData, department: e.target.value })}
            />
            <Stack direction="row" spacing={2}>
              <FormControl fullWidth>
                <InputLabel>Employment Type</InputLabel>
                <Select
                  value={formData.type}
                  label="Employment Type"
                  onChange={(e) => setFormData({ ...formData, type: e.target.value as EmploymentType })}
                >
                  <MenuItem value="full_time">Full Time</MenuItem>
                  <MenuItem value="part_time">Part Time</MenuItem>
                  <MenuItem value="contract">Contract</MenuItem>
                  <MenuItem value="internship">Internship</MenuItem>
                </Select>
              </FormControl>
              <FormControl fullWidth>
                <InputLabel>Experience Level</InputLabel>
                <Select
                  value={formData.experience}
                  label="Experience Level"
                  onChange={(e) => setFormData({ ...formData, experience: e.target.value as ExperienceLevel })}
                >
                  <MenuItem value="entry">Entry Level</MenuItem>
                  <MenuItem value="mid">Mid Level</MenuItem>
                  <MenuItem value="senior">Senior</MenuItem>
                  <MenuItem value="lead">Lead</MenuItem>
                </Select>
              </FormControl>
            </Stack>
            <FormControl fullWidth>
              <InputLabel>Location Type</InputLabel>
              <Select
                value={formData.location}
                label="Location Type"
                onChange={(e) => setFormData({ ...formData, location: e.target.value })}
              >
                <MenuItem value="remote">Remote</MenuItem>
                <MenuItem value="onsite">On-site</MenuItem>
                <MenuItem value="hybrid">Hybrid</MenuItem>
              </Select>
            </FormControl>
            <FormControl fullWidth>
              <InputLabel>Status</InputLabel>
              <Select
                value={formData.status}
                label="Status"
                onChange={(e) => setFormData({ ...formData, status: e.target.value as JobStatus })}
              >
                <MenuItem value="draft">Draft</MenuItem>
                <MenuItem value="open">Open</MenuItem>
                <MenuItem value="closed">Closed</MenuItem>
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
              label="Featured Job"
            />
          </Stack>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button onClick={handleSubmit} variant="contained">
            {editingJob ? 'Update' : 'Create'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default CareersJobs;

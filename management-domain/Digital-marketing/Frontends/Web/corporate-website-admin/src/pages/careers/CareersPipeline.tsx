import React from 'react';
import { Box, Grid, Card, CardContent, Typography, Stack, Avatar, Chip } from '@mui/material';
import { Person } from '@mui/icons-material';
import PageHeader from '@/components/common/PageHeader';

const pipelineStages = [
  { id: 'applied', title: 'Applied', count: 12 },
  { id: 'screening', title: 'Screening', count: 8 },
  { id: 'interview', title: 'Interview', count: 5 },
  { id: 'assessment', title: 'Assessment', count: 3 },
  { id: 'offer', title: 'Offer', count: 2 },
  { id: 'hired', title: 'Hired', count: 1 },
];

const mockCandidates = [
  { id: '1', name: 'John Doe', position: 'Senior Frontend Developer', stage: 'interview' },
  { id: '2', name: 'Jane Smith', position: 'Backend Developer', stage: 'screening' },
  { id: '3', name: 'Bob Johnson', position: 'Full Stack Developer', stage: 'applied' },
];

const CareersPipeline: React.FC = () => {
  return (
    <Box>
      <PageHeader
        title="Hiring Pipeline"
        subtitle="Track candidates through the hiring process"
      />

      <Grid container spacing={2}>
        {pipelineStages.map((stage) => (
          <Grid item xs={12} sm={6} md={4} lg={2} key={stage.id}>
            <Card sx={{ height: '100%' }}>
              <CardContent>
                <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                  {stage.title}
                </Typography>
                <Typography variant="h4">{stage.count}</Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      <Typography variant="h6" sx={{ mt: 4, mb: 2 }}>
        Recent Candidates
      </Typography>

      <Stack spacing={2}>
        {mockCandidates.map((candidate) => (
          <Card key={candidate.id}>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                <Avatar>
                  <Person />
                </Avatar>
                <Box sx={{ flex: 1 }}>
                  <Typography variant="subtitle1">{candidate.name}</Typography>
                  <Typography variant="body2" color="text.secondary">
                    {candidate.position}
                  </Typography>
                </Box>
                <Chip label={candidate.stage} size="small" />
              </Box>
            </CardContent>
          </Card>
        ))}
      </Stack>
    </Box>
  );
};

export default CareersPipeline;

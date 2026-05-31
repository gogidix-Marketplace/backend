import React from 'react';
import { useQuery } from '@tanstack/react-query';
import {
  Box,
  Grid,
  Card,
  CardContent,
  Typography,
  Chip,
  LinearProgress,
} from '@mui/material';
import {
  ApiIcon,
  StorageIcon,
  BugReportIcon,
  CloudUploadIcon,
  DescriptionIcon,
  CheckCircleIcon,
} from '@mui/icons-material';
import { devToolsApi } from '../api/devTools';
import { useNavigate } from 'react-router-dom';

const StatCard = ({ title, value, icon, color, onClick }) => (
  <Card
    sx={{
      bgcolor: '#1e1e1e',
      border: '1px solid #333',
      cursor: onClick ? 'pointer' : 'default',
      '&:hover': onClick ? { borderColor: color } : {},
    }}
    onClick={onClick}
  >
    <CardContent>
      <Box display="flex" alignItems="center" justifyContent="space-between">
        <Box>
          <Typography variant="body2" color="text.secondary" gutterBottom>
            {title}
          </Typography>
          <Typography variant="h4" component="div" sx={{ color }}>
            {value}
          </Typography>
        </Box>
        <Box sx={{ fontSize: 40, color }}>{icon}</Box>
      </Box>
    </CardContent>
  </Card>
);

const ToolCard = ({ name, description, enabled, onClick }) => (
  <Card
    sx={{
      bgcolor: '#1e1e1e',
      border: '1px solid #333',
      cursor: 'pointer',
      '&:hover': { borderColor: '#00bcd4' },
    }}
    onClick={onClick}
  >
    <CardContent>
      <Box display="flex" alignItems="center" justifyContent="space-between" mb={1}>
        <Typography variant="h6">{name}</Typography>
        <Chip
          label={enabled ? 'Enabled' : 'Disabled'}
          color={enabled ? 'success' : 'default'}
          size="small"
        />
      </Box>
      <Typography variant="body2" color="text.secondary">
        {description}
      </Typography>
    </CardContent>
  </Card>
);

function Dashboard() {
  const navigate = useNavigate();
  const projectId = 'default';

  const { data: tools } = useQuery({
    queryKey: ['tools'],
    queryFn: () => devToolsApi.getTools().then((res) => res.data),
  });

  const { data: apiStats } = useQuery({
    queryKey: ['api-stats', projectId],
    queryFn: () => devToolsApi.getTestStatistics(projectId).then((res) => res.data),
  });

  const { data: dbStats } = useQuery({
    queryKey: ['db-stats', projectId],
    queryFn: () => devToolsApi.getQueryStatistics(projectId).then((res) => res.data),
  });

  const { data: deployStats } = useQuery({
    queryKey: ['deploy-stats', projectId],
    queryFn: () => devToolsApi.getDeploymentStatistics(projectId).then((res) => res.data),
  });

  const apiTests = apiStats?.totalTests || 0;
  const dbQueries = dbStats?.totalQueries || 0;
  const deployJobs = deployStats?.totalJobs || 0;

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Developer Portal Dashboard
      </Typography>
      <Typography variant="body1" color="text.secondary" paragraph>
        Welcome to the Infrastructure DevTools dashboard. Access all your development tools from here.
      </Typography>

      <Grid container spacing={3} sx={{ mt: 2 }}>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="API Tests"
            value={apiTests}
            icon={<ApiIcon sx={{ fontSize: 40 }} />}
            color="#00bcd4"
            onClick={() => navigate('/api-testing')}
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Database Queries"
            value={dbQueries}
            icon={<StorageIcon sx={{ fontSize: 40 }} />}
            color="#ff4081"
            onClick={() => navigate('/database')}
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Deployment Jobs"
            value={deployJobs}
            icon={<CloudUploadIcon sx={{ fontSize: 40 }} />}
            color="#4caf50"
            onClick={() => navigate('/deployment')}
          />
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <StatCard
            title="Documentation Projects"
            value="0"
            icon={<DescriptionIcon sx={{ fontSize: 40 }} />}
            color="#ff9800"
            onClick={() => navigate('/documentation')}
          />
        </Grid>
      </Grid>

      <Typography variant="h5" sx={{ mt: 6, mb: 2 }}>
        Available Tools
      </Typography>

      <Grid container spacing={2}>
        {tools?.tools?.map((tool) => (
          <Grid item xs={12} md={6} key={tool.name}>
            <ToolCard
              name={tool.name}
              description={tool.description}
              enabled={tool.enabled}
              onClick={() => navigate(tool.endpoint)}
            />
          </Grid>
        ))}
      </Grid>
    </Box>
  );
}

export default Dashboard;

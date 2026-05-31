import React, { useState } from 'react';
import { Box, Grid, Card, CardContent, Typography, FormControl, Select, MenuItem, Stack } from '@mui/material';
import { TrendingUp, People, RemoveRedEye, Schedule } from '@mui/icons-material';
import PageHeader from '@/components/common/PageHeader';
import StatCard from '@/components/dashboard/StatCard';
import AnalyticsChart from '@/components/dashboard/AnalyticsChart';

const AnalyticsSite: React.FC = () => {
  const [period, setPeriod] = useState('last_30_days');

  const mockStats = {
    visitors: { current: 45231, change: 12.5 },
    pageViews: { current: 123456, change: 8.2 },
    sessions: { current: 38945, change: 15.3 },
    avgDuration: { current: '4:32', change: -2.1 },
  };

  const mockTopPages = [
    { page: '/pricing', views: 12453, bounceRate: 32 },
    { page: '/features', views: 9834, bounceRate: 28 },
    { page: '/about', views: 7654, bounceRate: 45 },
    { page: '/blog/getting-started', views: 6543, bounceRate: 22 },
    { page: '/contact', views: 5432, bounceRate: 38 },
  ];

  return (
    <Box>
      <PageHeader
        title="Site Analytics"
        subtitle="Track website traffic and user engagement"
      />

      <Box sx={{ mb: 3, display: 'flex', justifyContent: 'flex-end' }}>
        <FormControl size="small" sx={{ minWidth: 150 }}>
          <Select
            value={period}
            onChange={(e) => setPeriod(e.target.value)}
          >
            <MenuItem value="today">Today</MenuItem>
            <MenuItem value="yesterday">Yesterday</MenuItem>
            <MenuItem value="last_7_days">Last 7 Days</MenuItem>
            <MenuItem value="last_30_days">Last 30 Days</MenuItem>
            <MenuItem value="last_90_days">Last 90 Days</MenuItem>
          </Select>
        </FormControl>
      </Box>

      <Grid container spacing={3} sx={{ mb: 3 }}>
        <Grid item xs={12} sm={6} lg={3}>
          <StatCard
            title="Unique Visitors"
            value={mockStats.visitors.current}
            change={mockStats.visitors.change}
            icon={<People />}
            color="primary"
          />
        </Grid>
        <Grid item xs={12} sm={6} lg={3}>
          <StatCard
            title="Page Views"
            value={mockStats.pageViews.current}
            change={mockStats.pageViews.change}
            icon={<RemoveRedEye />}
            color="secondary"
          />
        </Grid>
        <Grid item xs={12} sm={6} lg={3}>
          <StatCard
            title="Sessions"
            value={mockStats.sessions.current}
            change={mockStats.sessions.change}
            icon={<TrendingUp />}
            color="success"
          />
        </Grid>
        <Grid item xs={12} sm={6} lg={3}>
          <StatCard
            title="Avg. Session Duration"
            value={mockStats.avgDuration.current}
            change={mockStats.avgDuration.change}
            icon={<Schedule />}
            color="warning"
          />
        </Grid>
      </Grid>

      <Grid container spacing={3}>
        <Grid item xs={12} lg={8}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>Traffic Overview</Typography>
              <AnalyticsChart period={period} />
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} lg={4}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>Top Pages</Typography>
              <Stack spacing={2}>
                {mockTopPages.map((page, index) => (
                  <Box
                    key={index}
                    sx={{
                      display: 'flex',
                      justifyContent: 'space-between',
                      alignItems: 'center',
                      py: 1,
                      borderBottom: index < mockTopPages.length - 1 ? '1px solid' : 'none',
                      borderColor: 'divider',
                    }}
                  >
                    <Box>
                      <Typography variant="body2" fontWeight={500}>{page.page}</Typography>
                      <Typography variant="caption" color="text.secondary">
                        {page.views.toLocaleString()} views
                      </Typography>
                    </Box>
                    <Typography variant="caption" color="text.secondary">
                      {page.bounceRate}% bounce
                    </Typography>
                  </Box>
                ))}
              </Stack>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
};

export default AnalyticsSite;

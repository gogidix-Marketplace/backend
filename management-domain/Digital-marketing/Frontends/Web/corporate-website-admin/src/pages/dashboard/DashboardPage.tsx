import React, { useState } from 'react';
import {
  Box,
  Grid,
  Card,
  CardContent,
  Typography,
  Button,
  Select,
  MenuItem,
  FormControl,
} from '@mui/material';
import {
  TrendingUp,
  People,
  Description,
  ShoppingBag,
  ArrowForward,
} from '@mui/icons-material';
import { useQuery } from '@tanstack/react-query';
import { getAnalyticsOverview, getAnalyticsOverview as getAnalytics } from '@/services/analyticsApi';
import PageHeader from '@/components/common/PageHeader';
import StatCard from '@/components/dashboard/StatCard';
import AnalyticsChart from '@/components/dashboard/AnalyticsChart';
import RecentActivityTable from '@/components/dashboard/RecentActivityTable';

type AnalyticsPeriod = 'today' | 'yesterday' | 'last_7_days' | 'last_30_days' | 'last_90_days';

const periods: Array<{ value: AnalyticsPeriod; label: string }> = [
  { value: 'today', label: 'Today' },
  { value: 'yesterday', label: 'Yesterday' },
  { value: 'last_7_days', label: 'Last 7 Days' },
  { value: 'last_30_days', label: 'Last 30 Days' },
  { value: 'last_90_days', label: 'Last 90 Days' },
];

const DashboardPage: React.FC = () => {
  const [period, setPeriod] = useState<AnalyticsPeriod>('last_30_days');

  const { data: analytics, isLoading } = useQuery({
    queryKey: ['analytics', 'overview', period],
    queryFn: () => getAnalytics(period),
  });

  return (
    <Box>
      <PageHeader
        title="Dashboard"
        subtitle="Overview of your website performance and activity"
      />

      <Box sx={{ mb: 3, display: 'flex', justifyContent: 'flex-end' }}>
        <FormControl size="small">
          <Select
            value={period}
            onChange={(e) => setPeriod(e.target.value as AnalyticsPeriod)}
            sx={{ minWidth: 150 }}
          >
            {periods.map((p) => (
              <MenuItem key={p.value} value={p.value}>
                {p.label}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      </Box>

      <Grid container spacing={3}>
        {/* Stats Cards */}
        <Grid item xs={12} sm={6} lg={3}>
          <StatCard
            title="Total Visitors"
            value={analytics?.metrics.uniqueVisitors || 0}
            change={12.5}
            icon={<People />}
            color="primary"
            loading={isLoading}
          />
        </Grid>
        <Grid item xs={12} sm={6} lg={3}>
          <StatCard
            title="Page Views"
            value={analytics?.metrics.pageViews || 0}
            change={8.2}
            icon={<Description />}
            color="secondary"
            loading={isLoading}
          />
        </Grid>
        <Grid item xs={12} sm={6} lg={3}>
          <StatCard
            title="Demo Requests"
            value={analytics?.conversions.demoRequests || 0}
            change={-3.1}
            icon={<ShoppingBag />}
            color="success"
            loading={isLoading}
          />
        </Grid>
        <Grid item xs={12} sm={6} lg={3}>
          <StatCard
            title="Conversion Rate"
            value={`${analytics?.conversions.conversionRate || 0}%`}
            change={5.4}
            icon={<TrendingUp />}
            color="warning"
            loading={isLoading}
          />
        </Grid>

        {/* Visitor Chart */}
        <Grid item xs={12} lg={8}>
          <Card>
            <CardContent>
              <Box
                sx={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                  mb: 2,
                }}
              >
                <Typography variant="h6">Visitor Trends</Typography>
                <Button
                  size="small"
                  endIcon={<ArrowForward />}
                  onClick={() => window.location.assign('/analytics/site')}
                >
                  View Details
                </Button>
              </Box>
              <AnalyticsChart period={period} loading={isLoading} />
            </CardContent>
          </Card>
        </Grid>

        {/* Top Content */}
        <Grid item xs={12} lg={4}>
          <Card sx={{ height: '100%' }}>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Top Pages
              </Typography>
              <Box sx={{ mt: 2 }}>
                {analytics?.content.topPages?.slice(0, 5).map((page, index) => (
                  <Box
                    key={index}
                    sx={{
                      display: 'flex',
                      justifyContent: 'space-between',
                      py: 1,
                      borderBottom:
                        index < 4 ? '1px solid' : 'none',
                      borderColor: 'divider',
                    }}
                  >
                    <Typography variant="body2" noWrap sx={{ maxWidth: '70%' }}>
                      {page.page}
                    </Typography>
                    <Typography
                      variant="body2"
                      color="text.secondary"
                    >
                      {page.views.toLocaleString()}
                    </Typography>
                  </Box>
                ))}
              </Box>
            </CardContent>
          </Card>
        </Grid>

        {/* Traffic Sources */}
        <Grid item xs={12} lg={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Traffic Sources
              </Typography>
              <Box sx={{ mt: 2 }}>
                {analytics?.visitors.topSources?.map((source, index) => (
                  <Box
                    key={index}
                    sx={{
                      display: 'flex',
                      alignItems: 'center',
                      py: 1,
                    }}
                  >
                    <Box sx={{ width: 100 }}>
                      <Typography variant="body2">
                        {source.source}
                      </Typography>
                    </Box>
                    <Box sx={{ flex: 1, mx: 2 }}>
                      <Box
                        sx={{
                          height: 8,
                          bgcolor: 'action.hover',
                          borderRadius: 4,
                          overflow: 'hidden',
                        }}
                      >
                        <Box
                          sx={{
                            height: '100%',
                            bgcolor: 'primary.main',
                            width: `${(source.visitors / analytics.visitors.topSources[0].visitors) * 100}%`,
                          }}
                        />
                      </Box>
                    </Box>
                    <Typography
                      variant="body2"
                      color="text.secondary"
                      sx={{ minWidth: 60, textAlign: 'right' }}
                    >
                      {source.visitors.toLocaleString()}
                    </Typography>
                  </Box>
                ))}
              </Box>
            </CardContent>
          </Card>
        </Grid>

        {/* Recent Activity */}
        <Grid item xs={12} lg={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Recent Activity
              </Typography>
              <RecentActivityTable />
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
};

export default DashboardPage;

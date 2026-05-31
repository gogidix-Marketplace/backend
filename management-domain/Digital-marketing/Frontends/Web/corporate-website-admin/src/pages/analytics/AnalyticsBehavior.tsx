import React, { useState } from 'react';
import { Box, Grid, Card, CardContent, Typography, FormControl, Select, MenuItem, LinearProgress } from '@mui/material';
import PageHeader from '@/components/common/PageHeader';

const AnalyticsBehavior: React.FC = () => {
  const [period, setPeriod] = useState('last_30_days');

  const mockBehaviorData = {
    newVsReturning: { new: 65, returning: 35 },
    devices: { desktop: 58, mobile: 38, tablet: 4 },
    topCountries: [
      { country: 'United States', visitors: 15432, percentage: 42 },
      { country: 'United Kingdom', visitors: 8765, percentage: 24 },
      { country: 'Germany', visitors: 5432, percentage: 15 },
      { country: 'Canada', visitors: 3210, percentage: 9 },
      { country: 'Australia', visitors: 2187, percentage: 6 },
    ],
    topReferrers: [
      { source: 'Google', visitors: 12453, percentage: 34 },
      { source: 'Direct', visitors: 8765, percentage: 24 },
      { source: 'LinkedIn', visitors: 5432, percentage: 15 },
      { source: 'Twitter', visitors: 3210, percentage: 9 },
      { source: 'Facebook', visitors: 2187, percentage: 6 },
    ],
  };

  return (
    <Box>
      <PageHeader
        title="User Behavior"
        subtitle="Understand how users interact with your website"
      />

      <Box sx={{ mb: 3, display: 'flex', justifyContent: 'flex-end' }}>
        <FormControl size="small" sx={{ minWidth: 150 }}>
          <Select
            value={period}
            onChange={(e) => setPeriod(e.target.value)}
          >
            <MenuItem value="today">Today</MenuItem>
            <MenuItem value="last_7_days">Last 7 Days</MenuItem>
            <MenuItem value="last_30_days">Last 30 Days</MenuItem>
            <MenuItem value="last_90_days">Last 90 Days</MenuItem>
          </Select>
        </FormControl>
      </Box>

      <Grid container spacing={3}>
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>New vs. Returning Visitors</Typography>
              <Box sx={{ mt: 2 }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                  <Typography variant="body2">New Visitors</Typography>
                  <Typography variant="body2" fontWeight={600}>{mockBehaviorData.newVsReturning.new}%</Typography>
                </Box>
                <LinearProgress
                  variant="determinate"
                  value={mockBehaviorData.newVsReturning.new}
                  sx={{ mb: 2 }}
                />
                <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                  <Typography variant="body2">Returning Visitors</Typography>
                  <Typography variant="body2" fontWeight={600}>{mockBehaviorData.newVsReturning.returning}%</Typography>
                </Box>
                <LinearProgress
                  variant="determinate"
                  value={mockBehaviorData.newVsReturning.returning}
                  color="secondary"
                />
              </Box>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>Device Breakdown</Typography>
              <Box sx={{ mt: 2 }}>
                <Box sx={{ mb: 2 }}>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                    <Typography variant="body2">Desktop</Typography>
                    <Typography variant="body2" fontWeight={600}>{mockBehaviorData.devices.desktop}%</Typography>
                  </Box>
                  <LinearProgress variant="determinate" value={mockBehaviorData.devices.desktop} />
                </Box>
                <Box sx={{ mb: 2 }}>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                    <Typography variant="body2">Mobile</Typography>
                    <Typography variant="body2" fontWeight={600}>{mockBehaviorData.devices.mobile}%</Typography>
                  </Box>
                  <LinearProgress variant="determinate" value={mockBehaviorData.devices.mobile} color="secondary" />
                </Box>
                <Box>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                    <Typography variant="body2">Tablet</Typography>
                    <Typography variant="body2" fontWeight={600}>{mockBehaviorData.devices.tablet}%</Typography>
                  </Box>
                  <LinearProgress variant="determinate" value={mockBehaviorData.devices.tablet} color="info" />
                </Box>
              </Box>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>Top Countries</Typography>
              <Box sx={{ mt: 2 }}>
                {mockBehaviorData.topCountries.map((country, index) => (
                  <Box
                    key={index}
                    sx={{
                      display: 'flex',
                      alignItems: 'center',
                      py: 1,
                      borderBottom: index < mockBehaviorData.topCountries.length - 1 ? '1px solid' : 'none',
                      borderColor: 'divider',
                    }}
                  >
                    <Box sx={{ flex: 1 }}>
                      <Typography variant="body2">{country.country}</Typography>
                    </Box>
                    <Box sx={{ width: 100, mr: 2 }}>
                      <Typography variant="body2" color="text.secondary" align="right">
                        {country.visitors.toLocaleString()}
                      </Typography>
                    </Box>
                    <Box sx={{ width: 40 }}>
                      <Typography variant="body2" fontWeight={600}>{country.percentage}%</Typography>
                    </Box>
                  </Box>
                ))}
              </Box>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>Top Referrers</Typography>
              <Box sx={{ mt: 2 }}>
                {mockBehaviorData.topReferrers.map((referrer, index) => (
                  <Box
                    key={index}
                    sx={{
                      display: 'flex',
                      alignItems: 'center',
                      py: 1,
                      borderBottom: index < mockBehaviorData.topReferrers.length - 1 ? '1px solid' : 'none',
                      borderColor: 'divider',
                    }}
                  >
                    <Box sx={{ flex: 1 }}>
                      <Typography variant="body2">{referrer.source}</Typography>
                    </Box>
                    <Box sx={{ width: 100, mr: 2 }}>
                      <Typography variant="body2" color="text.secondary" align="right">
                        {referrer.visitors.toLocaleString()}
                      </Typography>
                    </Box>
                    <Box sx={{ width: 40 }}>
                      <Typography variant="body2" fontWeight={600}>{referrer.percentage}%</Typography>
                    </Box>
                  </Box>
                ))}
              </Box>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
};

export default AnalyticsBehavior;

import React from 'react';
import { Box, Grid, Card, CardContent, Typography, Stack, LinearProgress } from '@mui/material';
import PageHeader from '@/components/common/PageHeader';

const mockFunnels = [
  {
    name: 'Product Demo Funnel',
    overallConversionRate: 12.5,
    steps: [
      { name: 'Landing Page', count: 10000, dropOffCount: 0, conversionRate: 100 },
      { name: 'View Pricing', count: 4500, dropOffCount: 5500, conversionRate: 45 },
      { name: 'Request Demo', count: 1200, dropOffCount: 3300, conversionRate: 26.7 },
      { name: 'Complete Demo', count: 750, dropOffCount: 450, conversionRate: 62.5 },
      { name: 'Convert to Customer', count: 1250, dropOffCount: -500, conversionRate: 166.7 },
    ],
  },
  {
    name: 'Trial Sign-up Funnel',
    overallConversionRate: 8.3,
    steps: [
      { name: 'Visit Homepage', count: 15000, dropOffCount: 0, conversionRate: 100 },
      { name: 'Click Start Free Trial', count: 3000, dropOffCount: 12000, conversionRate: 20 },
      { name: 'Sign Up Form', count: 2000, dropOffCount: 1000, conversionRate: 66.7 },
      { name: 'Confirm Email', count: 1500, dropOffCount: 500, conversionRate: 75 },
      { name: 'Complete Onboarding', count: 1250, dropOffCount: 250, conversionRate: 83.3 },
    ],
  },
];

const AnalyticsFunnels: React.FC = () => {
  return (
    <Box>
      <PageHeader
        title="Conversion Funnels"
        subtitle="Track user journeys and conversion rates"
      />

      <Grid container spacing={3}>
        {mockFunnels.map((funnel, funnelIndex) => (
          <Grid item xs={12} key={funnelIndex}>
            <Card>
              <CardContent>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                  <Typography variant="h6">{funnel.name}</Typography>
                  <Typography variant="body2" color="text.secondary">
                    Overall Conversion: <strong>{funnel.overallConversionRate}%</strong>
                  </Typography>
                </Box>

                <Stack spacing={2}>
                  {funnel.steps.map((step, stepIndex) => (
                    <Box key={stepIndex}>
                      <Box
                        sx={{
                          display: 'flex',
                          justifyContent: 'space-between',
                          alignItems: 'center',
                          mb: 1,
                        }}
                      >
                        <Box sx={{ flex: 1 }}>
                          <Typography variant="body2" fontWeight={500}>
                            {step.name}
                          </Typography>
                        </Box>
                        <Box sx={{ minWidth: 200, display: 'flex', justifyContent: 'space-between' }}>
                          <Typography variant="body2" color="text.secondary">
                            {step.count.toLocaleString()} visitors
                          </Typography>
                          <Typography
                                variant="body2"
                                color={step.conversionRate < 50 ? 'error' : 'success.main'}
                              >
                            {step.conversionRate}%
                          </Typography>
                        </Box>
                      </Box>
                      <LinearProgress
                        variant="determinate"
                        value={step.conversionRate}
                        color={step.conversionRate < 50 ? 'error' : 'primary'}
                        sx={{ height: 8, borderRadius: 4 }}
                      />
                      {step.dropOffCount > 0 && (
                        <Typography variant="caption" color="text.secondary">
                          {step.dropOffCount.toLocaleString()} dropped off
                        </Typography>
                      )}
                    </Box>
                  ))}
                </Stack>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default AnalyticsFunnels;

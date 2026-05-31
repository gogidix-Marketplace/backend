import React from 'react';
import { Box, Grid, Card, CardContent, Typography, Button, Stack, Switch, FormControlLabel, Divider } from '@mui/material';
import { Add, Settings as SettingsIcon } from '@mui/icons-material';
import PageHeader from '@/components/common/PageHeader';

const mockIntegrations = [
  {
    id: 'google-analytics',
    name: 'Google Analytics',
    description: 'Track website traffic and user behavior',
    category: 'analytics',
    icon: 'GA',
    active: true,
    configured: true,
  },
  {
    id: 'hubspot',
    name: 'HubSpot CRM',
    description: 'Sync leads and contacts with HubSpot',
    category: 'crm',
    icon: 'HS',
    active: true,
    configured: true,
  },
  {
    id: 'salesforce',
    name: 'Salesforce',
    description: 'Connect with Salesforce CRM',
    category: 'crm',
    icon: 'SF',
    active: false,
    configured: false,
  },
  {
    id: 'slack',
    name: 'Slack',
    description: 'Get notifications in Slack channels',
    category: 'communication',
    icon: 'SL',
    active: true,
    configured: true,
  },
  {
    id: 'mailchimp',
    name: 'Mailchimp',
    description: 'Email marketing automation',
    category: 'email',
    icon: 'MC',
    active: false,
    configured: true,
  },
  {
    id: 'aws-s3',
    name: 'AWS S3',
    description: 'Store media files in S3 buckets',
    category: 'storage',
    icon: 'S3',
    active: false,
    configured: false,
  },
];

const SettingsIntegrations: React.FC = () => {
  return (
    <Box>
      <PageHeader
        title="Integrations"
        subtitle="Connect with third-party services and tools"
        action={{
          label: 'Browse Integrations',
          onClick: () => console.log('Browse integrations'),
          variant: 'outlined',
          startIcon: <Add />,
        }}
      />

      <Grid container spacing={3}>
        {mockIntegrations.map((integration) => (
          <Grid item xs={12} sm={6} md={4} key={integration.id}>
            <Card>
              <CardContent>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                  <Box
                    sx={{
                      width: 48,
                      height: 48,
                      borderRadius: 2,
                      bgcolor: integration.configured ? 'primary.main' : 'action.disabledBackground',
                      color: 'white',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      mr: 2,
                      fontSize: 14,
                      fontWeight: 'bold',
                    }}
                  >
                    {integration.icon}
                  </Box>
                  <Box>
                    <Typography variant="subtitle1">{integration.name}</Typography>
                    <Typography variant="caption" color="text.secondary">
                      {integration.category}
                    </Typography>
                  </Box>
                </Box>
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  {integration.description}
                </Typography>
                <Divider sx={{ my: 2 }} />
                <Stack direction="row" spacing={1} alignItems="center">
                  {integration.configured ? (
                    <>
                      <FormControlLabel
                        control={<Switch checked={integration.active} />}
                        label="Enabled"
                      />
                      <Button size="small" startIcon={<SettingsIcon />}>Configure</Button>
                    </>
                  ) : (
                    <Button variant="outlined" size="small" fullWidth>Connect</Button>
                  )}
                </Stack>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default SettingsIntegrations;

import React from 'react';
import { Box, Grid, Card, CardContent, CardMedia, Typography, Chip, Button } from '@mui/material';
import { Code, GitHub, Description } from '@mui/icons-material';
import PageHeader from '@/components/common/PageHeader';

const sdkData = [
  {
    name: 'JavaScript SDK',
    language: 'JavaScript',
    version: '2.5.0',
    status: 'stable',
    description: 'Official JavaScript SDK for browser and Node.js environments',
    icon: '{JS}',
  },
  {
    name: 'Python SDK',
    language: 'Python',
    version: '1.8.0',
    status: 'stable',
    description: 'Python SDK for server-side integrations',
    icon: '{Py}',
  },
  {
    name: 'Go SDK',
    language: 'Go',
    version: '0.5.0',
    status: 'beta',
    description: 'Go SDK for high-performance applications',
    icon: '{Go}',
  },
  {
    name: 'PHP SDK',
    language: 'PHP',
    version: '1.2.0',
    status: 'stable',
    description: 'PHP SDK for legacy systems',
    icon: '{PHP}',
  },
];

const DeveloperSdks: React.FC = () => {
  return (
    <Box>
      <PageHeader
        title="SDKs"
        subtitle="Official Software Development Kits"
      />

      <Grid container spacing={3}>
        {sdkData.map((sdk) => (
          <Grid item xs={12} sm={6} lg={4} key={sdk.name}>
            <Card sx={{ height: '100%' }}>
              <CardContent>
                <Box
                  sx={{
                    width: 60,
                    height: 60,
                    borderRadius: 2,
                    bgcolor: 'primary.main',
                    color: 'white',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    mb: 2,
                    fontSize: 20,
                    fontWeight: 'bold',
                  }}
                >
                  {sdk.icon}
                </Box>
                <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 1 }}>
                  <Typography variant="h6">{sdk.name}</Typography>
                  <Chip
                    label={sdk.status}
                    size="small"
                    color={sdk.status === 'stable' ? 'success' : 'warning'}
                  />
                </Box>
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  {sdk.description}
                </Typography>
                <Box sx={{ display: 'flex', gap: 1, mt: 2 }}>
                  <Chip label={`v${sdk.version}`} size="small" variant="outlined" />
                  <Chip label={sdk.language} size="small" variant="outlined" />
                </Box>
                <Box sx={{ display: 'flex', gap: 1, mt: 2 }}>
                  <Button size="small" startIcon={<Description />}>Docs</Button>
                  <Button size="small" startIcon={<GitHub />}>GitHub</Button>
                  <Button size="small" startIcon={<Code />}>Examples</Button>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default DeveloperSdks;

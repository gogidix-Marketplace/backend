import React from 'react';
import { Box, Grid, Card, CardContent, Typography, Button, Stack, Chip } from '@mui/material';
import { Add, Handshake } from '@mui/icons-material';
import PageHeader from '@/components/common/PageHeader';

const mockPrograms = [
  {
    id: '1',
    name: 'Referral Partner Program',
    type: 'referral',
    description: 'Earn commissions for referring new customers',
    status: 'published',
    partnersCount: 45,
  },
  {
    id: '2',
    name: 'Technology Partner Program',
    type: 'technology',
    description: 'Integrate your product with ours',
    status: 'published',
    partnersCount: 12,
  },
  {
    id: '3',
    name: 'Reseller Program',
    type: 'reseller',
    description: 'Resell our products to your customers',
    status: 'draft',
    partnersCount: 0,
  },
];

const PartnersPrograms: React.FC = () => {
  return (
    <Box>
      <PageHeader
        title="Partner Programs"
        subtitle="Manage your partner programs"
        action={{
          label: 'Add Program',
          onClick: () => console.log('Add program'),
          variant: 'contained',
          startIcon: <Add />,
        }}
      />

      <Grid container spacing={3}>
        {mockPrograms.map((program) => (
          <Grid item xs={12} md={4} key={program.id}>
            <Card>
              <CardContent>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                  <Box
                    sx={{
                      width: 48,
                      height: 48,
                      borderRadius: 2,
                      bgcolor: 'primary.main',
                      color: 'white',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      mr: 2,
                    }}
                  >
                    <Handshake />
                  </Box>
                  <Box sx={{ flex: 1 }}>
                    <Typography variant="h6">{program.name}</Typography>
                    <Chip label={program.type} size="small" sx={{ mt: 0.5 }} />
                  </Box>
                </Box>
                <Typography variant="body2" color="text.secondary" gutterBottom>
                  {program.description}
                </Typography>
                <Stack direction="row" spacing={2} sx={{ mt: 2 }}>
                  <Typography variant="body2">
                    <strong>{program.partnersCount}</strong> partners
                  </Typography>
                  <Chip
                    label={program.status}
                    size="small"
                    color={program.status === 'published' ? 'success' : 'default'}
                  />
                </Stack>
                <Stack direction="row" spacing={1} sx={{ mt: 2 }}>
                  <Button size="small" variant="outlined">Edit</Button>
                  <Button size="small">View Partners</Button>
                </Stack>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default PartnersPrograms;

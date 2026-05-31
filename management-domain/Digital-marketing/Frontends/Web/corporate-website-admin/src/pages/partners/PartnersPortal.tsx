import React from 'react';
import { Box, Grid, Card, CardContent, Typography, Avatar, Chip } from '@mui/material';
import { Business } from '@mui/icons-material';
import PageHeader from '@/components/common/PageHeader';

const mockPartners = [
  {
    id: '1',
    name: 'Tech Solutions Inc',
    logo: '',
    type: 'referral',
    tier: 'gold',
    status: 'active',
    joinedAt: '2023-06-15',
  },
  {
    id: '2',
    name: 'CloudSoft',
    logo: '',
    type: 'technology',
    tier: 'platinum',
    status: 'active',
    joinedAt: '2023-05-20',
  },
  {
    id: '3',
    name: 'Digital Agency Pro',
    logo: '',
    type: 'reseller',
    tier: 'silver',
    status: 'active',
    joinedAt: '2023-08-10',
  },
];

const PartnersPortal: React.FC = () => {
  const getTierColor = (tier: string) => {
    switch (tier) {
      case 'platinum': return '#E5E4E2';
      case 'gold': return '#FFD700';
      case 'silver': return '#C0C0C0';
      case 'bronze': return '#CD7F32';
      default: return '#999';
    }
  };

  return (
    <Box>
      <PageHeader
        title="Partners"
        subtitle="View and manage all partners"
      />

      <Grid container spacing={3}>
        {mockPartners.map((partner) => (
          <Grid item xs={12} sm={6} md={4} key={partner.id}>
            <Card>
              <CardContent>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                  <Avatar sx={{ width: 56, height: 56, mr: 2, bgcolor: 'primary.main' }}>
                    <Business />
                  </Avatar>
                  <Box>
                    <Typography variant="h6">{partner.name}</Typography>
                    <Chip label={partner.tier} size="small" sx={{ bgcolor: getTierColor(partner.tier), mt: 0.5 }} />
                  </Box>
                </Box>
                <Stack spacing={1}>
                  <Typography variant="body2" color="text.secondary">
                    Type: {partner.type}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Status: <span style={{ color: partner.status === 'active' ? 'green' : 'red' }}>
                      {partner.status}
                    </span>
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Joined: {new Date(partner.joinedAt).toLocaleDateString()}
                  </Typography>
                </Stack>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default PartnersPortal;

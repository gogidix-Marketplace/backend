import React from 'react';
import { Box, Container, Typography, Button, Card, CardContent } from '@mui/material';
import { Home, ErrorOutline } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

const NotFoundPage: React.FC = () => {
  const navigate = useNavigate();

  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        bgcolor: 'background.default',
      }}
    >
      <Container maxWidth="md">
        <Card sx={{ textAlign: 'center', py: 8 }}>
          <CardContent>
            <ErrorOutline sx={{ fontSize: 120, color: 'text.disabled', mb: 2 }} />
            <Typography variant="h2" gutterBottom>
              404
            </Typography>
            <Typography variant="h5" gutterBottom color="text.secondary">
              Page Not Found
            </Typography>
            <Typography variant="body1" color="text.secondary" sx={{ mb: 4 }}>
              The page you are looking for doesn't exist or has been moved.
            </Typography>
            <Button
              variant="contained"
              size="large"
              startIcon={<Home />}
              onClick={() => navigate('/dashboard')}
            >
              Go to Dashboard
            </Button>
          </CardContent>
        </Card>
      </Container>
    </Box>
  );
};

export default NotFoundPage;

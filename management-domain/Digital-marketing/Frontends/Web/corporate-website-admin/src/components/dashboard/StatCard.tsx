import React from 'react';
import { Card, CardContent, Box, Typography, SxProps } from '@mui/material';
import { TrendingUp as TrendingUpIcon, TrendingDown as TrendingDownIcon } from '@mui/icons-material';

interface StatCardProps {
  title: string;
  value: number | string;
  change?: number;
  icon: React.ReactNode;
  color: 'primary' | 'secondary' | 'success' | 'warning' | 'error' | 'info';
  loading?: boolean;
  sx?: SxProps;
}

const StatCard: React.FC<StatCardProps> = ({
  title,
  value,
  change,
  icon,
  color,
  loading = false,
  sx,
}) => {
  const getColorValue = () => {
    switch (color) {
      case 'primary':
        return 'primary.main';
      case 'secondary':
        return 'secondary.main';
      case 'success':
        return 'success.main';
      case 'warning':
        return 'warning.main';
      case 'error':
        return 'error.main';
      case 'info':
        return 'info.main';
      default:
        return 'primary.main';
    }
  };

  return (
    <Card sx={sx}>
      <CardContent>
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <Box>
            <Typography variant="body2" color="text.secondary" gutterBottom>
              {title}
            </Typography>
            {loading ? (
              <Typography variant="h4">-</Typography>
            ) : (
              <Typography variant="h4" component="div">
                {typeof value === 'number' ? value.toLocaleString() : value}
              </Typography>
            )}
            {change !== undefined && !loading && (
              <Box
                sx={{
                  display: 'flex',
                  alignItems: 'center',
                  mt: 1,
                  color: change >= 0 ? 'success.main' : 'error.main',
                }}
              >
                {change >= 0 ? (
                  <TrendingUpIcon fontSize="small" />
                ) : (
                  <TrendingDownIcon fontSize="small" />
                )}
                <Typography variant="body2" sx={{ ml: 0.5 }}>
                  {Math.abs(change)}%
                </Typography>
              </Box>
            )}
          </Box>
          <Box
            sx={{
              width: 56,
              height: 56,
              borderRadius: 2,
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              bgcolor: getColorValue(),
              color: 'white',
            }}
          >
            {icon}
          </Box>
        </Box>
      </CardContent>
    </Card>
  );
};

export default StatCard;

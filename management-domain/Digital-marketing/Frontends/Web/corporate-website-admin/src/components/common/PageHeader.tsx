import React from 'react';
import { Box, Typography, Breadcrumbs, Link, Button, SxProps } from '@mui/material';
import { useNavigate } from 'react-router-dom';

interface PageHeaderProps {
  title: string;
  subtitle?: string;
  breadcrumb?: Array<{ label: string; path?: string }>;
  action?: {
    label: string;
    onClick: () => void;
    variant?: 'text' | 'outlined' | 'contained';
  };
  sx?: SxProps;
}

const PageHeader: React.FC<PageHeaderProps> = ({
  title,
  subtitle,
  breadcrumb,
  action,
  sx,
}) => {
  const navigate = useNavigate();

  return (
    <Box sx={{ mb: 3, ...sx }}>
      {breadcrumb && (
        <Breadcrumbs sx={{ mb: 1 }}>
          {breadcrumb.map((item, index) => (
            <Link
              key={index}
              underline="hover"
              color="inherit"
              href={item.path ? item.path : undefined}
              onClick={(e) => {
                if (!item.path) e.preventDefault();
                else navigate(item.path);
              }}
            >
              {item.label}
            </Link>
          ))}
        </Breadcrumbs>
      )}
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'flex-start',
          flexWrap: 'wrap',
          gap: 2,
        }}
      >
        <Box>
          <Typography variant="h4" component="h1" gutterBottom={false}>
            {title}
          </Typography>
          {subtitle && (
            <Typography variant="body2" color="text.secondary" sx={{ mt: 0.5 }}>
              {subtitle}
            </Typography>
          )}
        </Box>
        {action && (
          <Button
            variant={action.variant || 'contained'}
            onClick={action.onClick}
          >
            {action.label}
          </Button>
        )}
      </Box>
    </Box>
  );
};

export default PageHeader;

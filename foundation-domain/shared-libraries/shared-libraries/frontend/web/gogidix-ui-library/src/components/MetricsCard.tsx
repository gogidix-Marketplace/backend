import React from 'react'
import { Card, CardContent, Typography, Box, IconButton } from '@mui/material'
import { SvgIconComponent } from '@mui/icons-material'

export interface MetricsCardProps {
  title: string
  value: string
  change?: string
  trend?: 'up' | 'down' | 'neutral'
  icon?: React.ReactElement<SvgIconComponent>
  color?: string
  loading?: boolean
  onClick?: () => void
}

export const MetricsCard: React.FC<MetricsCardProps> = ({
  title,
  value,
  change,
  trend = 'neutral',
  icon,
  color = '#1976d2',
  loading = false,
  onClick,
}) => {
  const getTrendColor = () => {
    switch (trend) {
      case 'up': return '#4caf50'
      case 'down': return '#f44336'
      default: return '#757575'
    }
  }

  return (
    <Card 
      sx={{ 
        height: '100%', 
        cursor: onClick ? 'pointer' : 'default',
        '&:hover': onClick ? { boxShadow: 3 } : {}
      }}
      onClick={onClick}
    >
      <CardContent>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
          {icon && (
            <Box
              sx={{
                bgcolor: color,
                color: 'white',
                borderRadius: 1,
                p: 1,
                mr: 2,
                display: 'flex',
                alignItems: 'center',
              }}
            >
              {icon}
            </Box>
          )}
          <Typography variant="h6" component="div" noWrap>
            {title}
          </Typography>
        </Box>
        
        <Typography 
          variant="h4" 
          component="div" 
          sx={{ mb: 1, fontWeight: 'bold' }}
        >
          {loading ? '...' : value}
        </Typography>
        
        {change && (
          <Typography
            variant="body2"
            sx={{
              color: getTrendColor(),
              fontWeight: 'medium',
            }}
          >
            {change}
          </Typography>
        )}
      </CardContent>
    </Card>
  )
}
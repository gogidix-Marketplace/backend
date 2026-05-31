import React from 'react';
import { Box, useTheme } from '@mui/material';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';

interface AnalyticsChartProps {
  period: string;
  loading?: boolean;
}

const AnalyticsChart: React.FC<AnalyticsChartProps> = ({ period, loading }) => {
  const theme = useTheme();

  // Mock data - in real app, this would come from the API
  const generateData = () => {
    const data = [];
    const points = period === 'today' || period === 'yesterday' ? 24 : 30;
    for (let i = 0; i < points; i++) {
      data.push({
        name: period === 'today' || period === 'yesterday' ? `${i}:00` : `Day ${i + 1}`,
        visitors: Math.floor(Math.random() * 5000) + 1000,
        pageViews: Math.floor(Math.random() * 10000) + 2000,
      });
    }
    return data;
  };

  const data = generateData();

  if (loading) {
    return (
      <Box sx={{ height: 300, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
        Loading...
      </Box>
    );
  }

  return (
    <ResponsiveContainer width="100%" height={300}>
      <LineChart data={data} margin={{ top: 5, right: 30, left: 20, bottom: 5 }}>
        <CartesianGrid strokeDasharray="3 3" stroke={theme.palette.divider} />
        <XAxis
          dataKey="name"
          stroke={theme.palette.text.secondary}
          style={{ fontSize: 12 }}
        />
        <YAxis
          stroke={theme.palette.text.secondary}
          style={{ fontSize: 12 }}
        />
        <Tooltip
          contentStyle={{
            backgroundColor: theme.palette.background.paper,
            border: `1px solid ${theme.palette.divider}`,
            borderRadius: 8,
          }}
        />
        <Line
          type="monotone"
          dataKey="visitors"
          stroke={theme.palette.primary.main}
          strokeWidth={2}
          dot={false}
          name="Visitors"
        />
        <Line
          type="monotone"
          dataKey="pageViews"
          stroke={theme.palette.secondary.main}
          strokeWidth={2}
          dot={false}
          name="Page Views"
        />
      </LineChart>
    </ResponsiveContainer>
  );
};

export default AnalyticsChart;

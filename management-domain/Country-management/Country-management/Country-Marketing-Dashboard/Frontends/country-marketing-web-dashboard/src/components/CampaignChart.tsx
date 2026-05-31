'use client';

import { Bar, BarChart, CartesianGrid, Legend, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts';

const data = [
  { name: 'Jan', impressions: 4500, clicks: 890, conversions: 120 },
  { name: 'Feb', impressions: 5200, clicks: 1100, conversions: 145 },
  { name: 'Mar', impressions: 4800, clicks: 950, conversions: 130 },
  { name: 'Apr', impressions: 6100, clicks: 1300, conversions: 175 },
  { name: 'May', impressions: 5800, clicks: 1250, conversions: 160 },
  { name: 'Jun', impressions: 6700, clicks: 1450, conversions: 190 },
];

export default function CampaignChart() {
  return (
    <ResponsiveContainer width="100%" height={300}>
      <BarChart data={data}>
        <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#e5e7eb" />
        <XAxis
          dataKey="name"
          axisLine={false}
          tickLine={false}
          tick={{ fill: '#6b7280', fontSize: 12 }}
        />
        <YAxis
          axisLine={false}
          tickLine={false}
          tick={{ fill: '#6b7280', fontSize: 12 }}
        />
        <Tooltip
          contentStyle={{
            backgroundColor: 'white',
            border: '1px solid #e5e7eb',
            borderRadius: '8px',
          }}
        />
        <Legend />
        <Bar dataKey="impressions" fill="#93c5fd" name="Impressions" radius={[4, 4, 0, 0]} />
        <Bar dataKey="clicks" fill="#60a5fa" name="Clicks" radius={[4, 4, 0, 0]} />
        <Bar dataKey="conversions" fill="#2563eb" name="Conversions" radius={[4, 4, 0, 0]} />
      </BarChart>
    </ResponsiveContainer>
  );
}

'use client';

import { LucideIcon } from 'lucide-react';

interface MetricCardProps {
  title: string;
  value: string | number;
  icon: React.ReactNode;
  color: 'primary' | 'success' | 'warning' | 'danger';
  change?: string;
}

const colorClasses = {
  primary: 'border-primary-500 bg-primary-50',
  success: 'border-success-500 bg-success-50',
  warning: 'border-warning-500 bg-warning-50',
  danger: 'border-danger-500 bg-danger-50',
};

const iconColorClasses = {
  primary: 'text-primary-600',
  success: 'text-success-600',
  warning: 'text-warning-600',
  danger: 'text-danger-600',
};

export default function MetricCard({ title, value, icon, color, change }: MetricCardProps) {
  return (
    <div className={`metric-card ${colorClasses[color]}`}>
      <div className="flex items-center justify-between">
        <div>
          <p className="text-sm font-medium text-gray-600">{title}</p>
          <p className="text-2xl font-bold text-gray-900 mt-1">{value}</p>
          {change && (
            <p className="text-xs text-gray-500 mt-1">{change}</p>
          )}
        </div>
        <div className={`p-3 rounded-lg ${iconColorClasses[color]}`}>
          {icon}
        </div>
      </div>
    </div>
  );
}

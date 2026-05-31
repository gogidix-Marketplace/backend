'use client';

import React from 'react';

interface MetricCardProps {
  title: string;
  value: string | number;
  change?: string;
  changeType?: 'positive' | 'negative' | 'neutral';
  icon?: string;
}

export const MetricCard: React.FC<MetricCardProps> = ({
  title,
  value,
  change,
  changeType = 'neutral',
  icon,
}) => {
  const getChangeColor = () => {
    switch (changeType) {
      case 'positive':
        return 'text-green-600 bg-green-50';
      case 'negative':
        return 'text-red-600 bg-red-50';
      default:
        return 'text-gray-600 bg-gray-50';
    }
  };

  const getChangeIcon = () => {
    if (changeType === 'positive') {
      return <span className="text-green-600">↑</span>;
    }
    if (changeType === 'negative') {
      return <span className="text-red-600">↓</span>;
    }
    return <span className="text-gray-600">−</span>;
  };

  return (
    <div className="bg-white rounded-lg shadow p-6 hover:shadow-lg transition-shadow">
      <div className="flex items-start justify-between">
        <div className="flex-1">
          <p className="text-sm font-medium text-gray-600">{title}</p>
          <p className="text-2xl font-bold text-gray-900 mt-1">{value}</p>
          {change && (
            <div className={`flex items-center mt-2 px-2 py-1 rounded-full text-xs font-medium w-fit ${getChangeColor()}`}>
              {getChangeIcon()}
              <span className="ml-1">{change}</span>
            </div>
          )}
        </div>
        {icon && (
          <div className="text-3xl ml-4">{icon}</div>
        )}
      </div>
    </div>
  );
};

export default MetricCard;

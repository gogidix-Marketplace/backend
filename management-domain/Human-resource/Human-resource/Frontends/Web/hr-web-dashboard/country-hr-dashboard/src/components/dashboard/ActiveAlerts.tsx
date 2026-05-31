'use client';

import React from 'react';

interface Alert {
  id: string;
  type: 'compliance' | 'deadline' | 'attention' | 'info';
  severity: 'high' | 'medium' | 'low';
  title: string;
  description: string;
  country?: string;
  dueDate?: string;
}

export const ActiveAlerts: React.FC = () => {
  // Mock alerts data - in real app, this would come from API
  const alerts: Alert[] = [
    {
      id: '1',
      type: 'compliance',
      severity: 'high',
      title: 'Compliance documents expiring',
      description: '5 employees have expiring work permits in Germany',
      country: 'DE',
      dueDate: '2024-03-15',
    },
    {
      id: '2',
      type: 'deadline',
      severity: 'medium',
      title: 'Performance review deadline',
      description: 'Annual reviews due for 23 employees in France',
      country: 'FR',
      dueDate: '2024-03-30',
    },
    {
      id: '3',
      type: 'attention',
      severity: 'medium',
      title: 'Open positions alert',
      description: '3 critical roles unfilled for more than 60 days',
      dueDate: '2024-03-10',
    },
    {
      id: '4',
      type: 'info',
      severity: 'low',
      title: 'New policy update',
      description: 'Remote work policy has been updated for all regions',
    },
  ];

  const getSeverityColor = (severity: string) => {
    switch (severity) {
      case 'high':
        return 'bg-red-100 text-red-800 border-red-200';
      case 'medium':
        return 'bg-yellow-100 text-yellow-800 border-yellow-200';
      case 'low':
        return 'bg-blue-100 text-blue-800 border-blue-200';
      default:
        return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  };

  const getTypeIcon = (type: string) => {
    switch (type) {
      case 'compliance':
        return (
          <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
          </svg>
        );
      case 'deadline':
        return (
          <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        );
      case 'attention':
        return (
          <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
          </svg>
        );
      default:
        return (
          <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
        );
    }
  };

  return (
    <div className="bg-white rounded-lg shadow p-6">
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-lg font-semibold text-gray-900">Active Alerts</h2>
        <a href="/alerts" className="text-blue-600 hover:text-blue-700 text-sm font-medium">
          View all →
        </a>
      </div>

      <div className="space-y-4">
        {alerts.map((alert) => (
          <div
            key={alert.id}
            className={`p-4 rounded-lg border ${getSeverityColor(alert.severity)} hover:shadow-md transition-shadow cursor-pointer`}
          >
            <div className="flex items-start">
              <div className="flex-shrink-0 mt-0.5">{getTypeIcon(alert.type)}</div>
              <div className="ml-3 flex-1">
                <div className="flex items-center justify-between">
                  <h3 className="text-sm font-semibold">{alert.title}</h3>
                  {alert.country && (
                    <span className="text-xs px-2 py-1 bg-white rounded-full">{alert.country}</span>
                  )}
                </div>
                <p className="text-sm mt-1 opacity-90">{alert.description}</p>
                {alert.dueDate && (
                  <p className="text-xs mt-2 opacity-75">
                    Due: {new Date(alert.dueDate).toLocaleDateString()}
                  </p>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ActiveAlerts;

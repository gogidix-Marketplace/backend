'use client';

import { useEffect } from 'react';
import useSWR from 'swr';
import api from '@/lib/api';
import type { DashboardMetrics } from '@/types';
import {
  Ticket,
  TrendingUp,
  TrendingDown,
  Users,
  AlertCircle,
  CheckCircle,
  Clock,
} from 'lucide-react';
import { cn, formatMinutes, formatHours } from '@/lib/utils';

const StatCard = ({
  title,
  value,
  change,
  icon: Icon,
  color = 'primary',
}: {
  title: string;
  value: string | number;
  change?: number;
  icon: any;
  color?: 'primary' | 'success' | 'warning' | 'danger';
}) => {
  const colorClasses = {
    primary: 'bg-primary-500 text-white',
    success: 'bg-success-500 text-white',
    warning: 'bg-warning-500 text-white',
    danger: 'bg-danger-500 text-white',
  };

  return (
    <div className="bg-white rounded-lg shadow p-6">
      <div className="flex items-center justify-between">
        <div>
          <p className="text-sm font-medium text-gray-600">{title}</p>
          <p className="mt-2 text-3xl font-semibold text-gray-900">{value}</p>
          {change !== undefined && (
            <p className={cn('mt-2 text-sm flex items-center', change >= 0 ? 'text-success-600' : 'text-danger-600')}>
              {change >= 0 ? <TrendingUp className="w-4 h-4 mr-1" /> : <TrendingDown className="w-4 h-4 mr-1" />}
              {Math.abs(change)}% from last week
            </p>
          )}
        </div>
        <div className={cn('p-3 rounded-lg', colorClasses[color])}>
          <Icon className="w-6 h-6" />
        </div>
      </div>
    </div>
  );
};

export default function DashboardPage() {
  const { data: metrics, isLoading, error } = useSWR<DashboardMetrics>(
    '/dashboard/metrics',
    () => api.getDashboardMetrics(7),
    { refreshInterval: 30000 } // Refresh every 30 seconds
  );

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="bg-danger-50 border border-danger-200 rounded-lg p-4">
        <p className="text-danger-800">Failed to load dashboard data. Please try again later.</p>
      </div>
    );
  }

  if (!metrics) return null;

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-gray-900">Support Dashboard</h1>
        <p className="text-gray-600">Overview of country support operations</p>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <StatCard
          title="Total Tickets"
          value={metrics.totalTickets}
          change={5}
          icon={Ticket}
          color="primary"
        />
        <StatCard
          title="Open Tickets"
          value={metrics.openTickets}
          icon={Clock}
          color="warning"
        />
        <StatCard
          title="Resolved Today"
          value={metrics.resolvedTickets}
          change={12}
          icon={CheckCircle}
          color="success"
        />
        <StatCard
          title="Escalated"
          value={metrics.escalatedTickets}
          icon={AlertCircle}
          color="danger"
        />
      </div>

      {/* Two column layout */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* SLA Metrics */}
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">SLA Performance</h2>
          <div className="space-y-4">
            <div>
              <div className="flex justify-between text-sm mb-1">
                <span className="text-gray-600">Response SLA</span>
                <span className="font-medium">{metrics.slaResponseRate?.toFixed(1)}%</span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div
                  className={cn('h-2 rounded-full', metrics.slaResponseRate && metrics.slaResponseRate >= 95 ? 'bg-success-500' : 'bg-warning-500')}
                  style={{ width: `${metrics.slaResponseRate || 0}%` }}
                />
              </div>
            </div>
            <div>
              <div className="flex justify-between text-sm mb-1">
                <span className="text-gray-600">Resolution SLA</span>
                <span className="font-medium">{metrics.slaResolutionRate?.toFixed(1)}%</span>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-2">
                <div
                  className={cn('h-2 rounded-full', metrics.slaResolutionRate && metrics.slaResolutionRate >= 90 ? 'bg-success-500' : 'bg-warning-500')}
                  style={{ width: `${metrics.slaResolutionRate || 0}%` }}
                />
              </div>
            </div>
            <div className="pt-4 border-t border-gray-200 grid grid-cols-3 gap-4 text-center">
              <div>
                <p className="text-2xl font-semibold text-gray-900">
                  {formatMinutes(metrics.avgResponseTimeMinutes || 0)}
                </p>
                <p className="text-xs text-gray-600">Avg Response</p>
              </div>
              <div>
                <p className="text-2xl font-semibold text-gray-900">
                  {formatHours(metrics.avgResolutionTimeHours || 0)}
                </p>
                <p className="text-xs text-gray-600">Avg Resolution</p>
              </div>
              <div>
                <p className="text-2xl font-semibold text-danger-600">{metrics.slaBreachedCount}</p>
                <p className="text-xs text-gray-600">SLA Breached</p>
              </div>
            </div>
          </div>
        </div>

        {/* Team Status */}
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Team Status</h2>
          <div className="grid grid-cols-2 gap-4">
            <div className="bg-success-50 rounded-lg p-4 text-center">
              <p className="text-2xl font-semibold text-success-600">{metrics.availableAgents}</p>
              <p className="text-sm text-gray-600">Available</p>
            </div>
            <div className="bg-warning-50 rounded-lg p-4 text-center">
              <p className="text-2xl font-semibold text-warning-600">{metrics.busyAgents}</p>
              <p className="text-sm text-gray-600">Busy</p>
            </div>
            <div className="bg-gray-50 rounded-lg p-4 text-center">
              <p className="text-2xl font-semibold text-gray-600">{metrics.offlineAgents}</p>
              <p className="text-sm text-gray-600">Offline</p>
            </div>
            <div className="bg-primary-50 rounded-lg p-4 text-center">
              <p className="text-2xl font-semibold text-primary-600">{metrics.totalAgents}</p>
              <p className="text-sm text-gray-600">Total Agents</p>
            </div>
          </div>
        </div>

        {/* Customer Satisfaction */}
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Customer Satisfaction</h2>
          <div className="flex items-center justify-center mb-4">
            <div className="text-center">
              <p className="text-5xl font-bold text-gray-900">
                {metrics.averageRating?.toFixed(1) || '0.0'}
              </p>
              <p className="text-sm text-gray-600">Average Rating</p>
            </div>
          </div>
          <div className="space-y-2">
            <div className="flex justify-between text-sm">
              <span className="text-success-600">Positive ({metrics.positiveFeedback})</span>
              <span className="text-gray-600">{calculatePercentage(metrics.positiveFeedback, metrics.totalFeedback)}%</span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-2">
              <div className="bg-success-500 h-2 rounded-full" style={{ width: `${calculatePercentage(metrics.positiveFeedback, metrics.totalFeedback)}%` }} />
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-gray-600">Neutral ({metrics.neutralFeedback})</span>
              <span className="text-gray-600">{calculatePercentage(metrics.neutralFeedback, metrics.totalFeedback)}%</span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-2">
              <div className="bg-gray-400 h-2 rounded-full" style={{ width: `${calculatePercentage(metrics.neutralFeedback, metrics.totalFeedback)}%` }} />
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-danger-600">Negative ({metrics.negativeFeedback})</span>
              <span className="text-gray-600">{calculatePercentage(metrics.negativeFeedback, metrics.totalFeedback)}%</span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-2">
              <div className="bg-danger-500 h-2 rounded-full" style={{ width: `${calculatePercentage(metrics.negativeFeedback, metrics.totalFeedback)}%` }} />
            </div>
          </div>
        </div>

        {/* Active Escalations */}
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Escalations to HQ</h2>
          <div className="grid grid-cols-3 gap-4 text-center">
            <div>
              <p className="text-2xl font-semibold text-warning-600">{metrics.pendingEscalations}</p>
              <p className="text-xs text-gray-600">Pending</p>
            </div>
            <div>
              <p className="text-2xl font-semibold text-primary-600">{metrics.activeEscalations}</p>
              <p className="text-xs text-gray-600">In Progress</p>
            </div>
            <div>
              <p className="text-2xl font-semibold text-success-600">{metrics.resolvedEscalations}</p>
              <p className="text-xs text-gray-600">Resolved</p>
            </div>
          </div>
          {metrics.escalationRate && (
            <div className="mt-4 pt-4 border-t border-gray-200">
              <p className="text-sm text-gray-600">
                Escalation Rate: <span className="font-semibold">{metrics.escalationRate.toFixed(1)}%</span>
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

function calculatePercentage(value: number, total: number): number {
  if (total === 0) return 0;
  return Math.round((value / total) * 100);
}

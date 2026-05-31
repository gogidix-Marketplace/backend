'use client';

import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { useAuth } from '../../hooks/useAuth';
import { hrApi } from '../../api/hrApi';
import { ProtectedRoute } from '../../components/common/ProtectedRoute';
import { DashboardLayout } from '../../components/layout/DashboardLayout';
import { HealthScoreCard } from '../../components/dashboard/HealthScoreCard';
import { MetricCard } from '../../components/dashboard/MetricCard';
import { CountryCard } from '../../components/dashboard/CountryCard';
import { RecruitmentPipeline } from '../../components/dashboard/RecruitmentPipeline';
import { ActiveAlerts } from '../../components/dashboard/ActiveAlerts';

const DashboardOverview: React.FC = () => {
  const { user } = useAuth();
  const [selectedTimeRange, setSelectedTimeRange] = useState<'30d' | '90d' | '1y'>('90d');

  // Fetch dashboard overview data
  const { data: overview, isLoading: overviewLoading } = useQuery({
    queryKey: ['dashboard-overview', selectedTimeRange],
    queryFn: () => hrApi.getOverview(),
  });

  // Fetch countries data
  const { data: countries, isLoading: countriesLoading } = useQuery({
    queryKey: ['countries'],
    queryFn: () => hrApi.getCountries(),
  });

  // Fetch recruitment data
  const { data: jobs } = useQuery({
    queryKey: ['global-jobs'],
    queryFn: () => hrApi.getGlobalJobs(),
  });

  const { data: pipeline } = useQuery({
    queryKey: ['global-pipeline'],
    queryFn: () => hrApi.getGlobalPipeline(),
  });

  if (overviewLoading || countriesLoading) {
    return (
      <DashboardLayout>
        <div className="flex items-center justify-center h-64">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>
      </DashboardLayout>
    );
  }

  // Calculate health score color
  const getHealthScoreColor = (score: number) => {
    if (score >= 80) return 'text-green-600';
    if (score >= 60) return 'text-yellow-600';
    return 'text-red-600';
  };

  const healthScore = overview?.healthScore || 0;

  return (
    <ProtectedRoute>
      <DashboardLayout>
        <div className="space-y-6">
          {/* Header */}
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">Global HR Overview</h1>
              <p className="text-gray-600">Welcome back, {user?.firstName}!</p>
            </div>
            <div className="flex items-center space-x-4">
              {/* Time range selector */}
              <select
                value={selectedTimeRange}
                onChange={(e) => setSelectedTimeRange(e.target.value as any)}
                className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
              >
                <option value="30d">Last 30 days</option>
                <option value="90d">Last 90 days</option>
                <option value="1y">Last year</option>
              </select>
              <button className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition">
                Generate Report
              </button>
            </div>
          </div>

          {/* Health Score Banner */}
          <HealthScoreCard score={healthScore} />

          {/* Key Metrics */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            <MetricCard
              title="Total Employees"
              value={overview?.totalEmployees || 0}
              change="+2.5%"
              changeType="positive"
              icon={
                <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                </svg>
              }
            />
            <MetricCard
              title="Total Countries"
              value={overview?.totalCountries || 0}
              change="+1"
              changeType="neutral"
              icon={
                <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3.055 11H5a2 2 0 012 2v1a2 2 0 002 2 2 2 0 012 2v2.945M8 3.935V5.5A2.5 2.5 0 0010.5 8h.5a2 2 0 012 2 2 2 0 104 0 2 2 0 012-2h1.064M15 20.488V18a2 2 0 012-2h3.064M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              }
            />
            <MetricCard
              title="Active Jobs"
              value={overview?.recruitment?.totalJobs || 0}
              change="+12"
              changeType="positive"
              icon={
                <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                </svg>
              }
            />
            <MetricCard
              title="Total Payroll"
              value={`$${((overview?.payroll?.totalPayroll || 0) / 1000000).toFixed(1)}M`}
              change="+3.2%"
              changeType="positive"
              icon={
                <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              }
            />
          </div>

          {/* Countries Overview */}
          <div className="bg-white rounded-lg shadow p-6">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-lg font-semibold text-gray-900">Workforce by Country</h2>
              <a href="/countries" className="text-blue-600 hover:text-blue-700 text-sm font-medium">
                View all →
              </a>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {countries?.slice(0, 6).map((country) => (
                <CountryCard
                  key={country.countryCode}
                  country={country}
                  onClick={() => window.location.href = `/countries/${country.countryCode}`}
                />
              ))}
            </div>
          </div>

          {/* Recruitment Pipeline */}
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <RecruitmentPipeline pipeline={pipeline} />
            <ActiveAlerts />
          </div>
        </div>
      </DashboardLayout>
    </ProtectedRoute>
  );
};

export default DashboardOverview;

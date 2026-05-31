'use client';

import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { useAuth } from '../../hooks/useAuth';
import { globalHrApi } from '../../api/globalHrApi';
import { DashboardLayout } from '../../components/layout/GlobalDashboardLayout';
import { GlobalHealthScoreCard } from '../../components/dashboard/GlobalHealthScoreCard';
import { WorldMapCard } from '../../components/dashboard/WorldMapCard';
import { MetricCard } from '../../components/dashboard/MetricCard';
import { CountryComparisonCard } from '../../components/dashboard/CountryComparisonCard';
import { GlobalPipeline } from '../../components/dashboard/GlobalPipeline';
import { GlobalInsights } from '../../components/dashboard/GlobalInsights';
import { RegionalSummaryCard } from '../../components/dashboard/RegionalSummaryCard';

const GlobalDashboard: React.FC = () => {
  const { user } = useAuth();
  const [selectedRegion, setSelectedRegion] = useState<string>('all');
  const [selectedTimeRange, setSelectedTimeRange] = useState<'30d' | '90d' | '1y'>('90d');

  const { data: overview, isLoading: overviewLoading } = useQuery({
    queryKey: ['global-overview', selectedTimeRange],
    queryFn: () => globalHrApi.getGlobalOverview(),
  });

  const { data: countries } = useQuery({
    queryKey: ['countries-comparison', selectedRegion],
    queryFn: () => globalHrApi.getCountriesComparison({ regions: selectedRegion === 'all' ? undefined : [selectedRegion] }),
  });

  const { data: insights } = useQuery({
    queryKey: ['global-insights'],
    queryFn: () => globalHrApi.getGlobalInsights(),
  });

  const { data: regions } = useQuery({
    queryKey: ['regional-summary'],
    queryFn: () => globalHrApi.getRegionalSummary(),
  });

  if (overviewLoading) {
    return (
      <DashboardLayout>
        <div className="flex items-center justify-center h-64">
          <div className="animate-spin rounded-full h-16 w-16 border-b-4 border-blue-600"></div>
        </div>
      </DashboardLayout>
    );
  }

  const topCountries = countries?.slice(0, 6) || [];

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div>
            <div className="flex items-center gap-2">
              <span className="text-2xl">🌍</span>
              <h1 className="text-2xl font-bold text-gray-900">Global HR Overview</h1>
            </div>
            <p className="text-gray-600 mt-1">
              Multi-country HR Management Dashboard
            </p>
          </div>
          <div className="flex items-center gap-4">
            <select
              value={selectedRegion}
              onChange={(e) => setSelectedRegion(e.target.value)}
              className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
            >
              <option value="all">All Regions</option>
              <option value="africa">Africa</option>
              <option value="europe">Europe</option>
              <option value="asia">Asia Pacific</option>
              <option value="americas">Americas</option>
            </select>
            <select
              value={selectedTimeRange}
              onChange={(e) => setSelectedTimeRange(e.target.value as any)}
              className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
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

        {/* Global Health Score Banner */}
        <GlobalHealthScoreCard
          score={overview?.globalHealthScore || 0}
          worldwideHeadcount={overview?.worldwideHeadcount || 0}
          totalCountries={overview?.totalCountries || 0}
          growthRate={overview?.workforceTrends?.growthRate || 0}
        />

        {/* Key Metrics Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          <MetricCard
            title="Worldwide Headcount"
            value={overview?.worldwideHeadcount?.toLocaleString() || '0'}
            change={`+${overview?.workforceTrends?.growthRate?.toFixed(1) || 0}%`}
            changeType="positive"
            icon="👥"
          />
          <MetricCard
            title="Total Countries"
            value={overview?.totalCountries || 0}
            change={`${overview?.totalRegions || 0} regions`}
            changeType="neutral"
            icon="🌎"
          />
          <MetricCard
            title="Global Recruitment"
            value={`${overview?.globalRecruitment?.totalActiveJobs || 0} jobs`}
            change={`${overview?.globalRecruitment?.totalApplicants || 0} applicants`}
            changeType="positive"
            icon="💼"
          />
          <MetricCard
            title="Total Payroll (USD)"
            value={`$${((overview?.globalPayroll?.totalPayrollUSD || 0) / 1000000).toFixed(1)}M`}
            change={`+${overview?.workforceTrends?.growthRate?.toFixed(1) || 0}%`}
            changeType="positive"
            icon="💰"
          />
        </div>

        {/* World Map & Regional Summary */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <WorldMapCard
            countries={countries || []}
            onCountryClick={(code) => {/* Navigate to country detail */}}
          />
          <div className="lg:col-span-2">
            <RegionalSummaryCard regions={regions || []} />
          </div>
        </div>

        {/* Top Countries Comparison */}
        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-lg font-semibold text-gray-900">Country Performance Overview</h2>
            <a href="/countries" className="text-blue-600 hover:text-blue-700 text-sm font-medium">
              View all countries →
            </a>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            {topCountries.map((country) => (
              <CountryComparisonCard
                key={country.countryCode}
                country={country}
                onClick={() => {/* Navigate to country detail */}}
              />
            ))}
          </div>
        </div>

        {/* Global Recruitment Pipeline */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <GlobalPipeline recruitment={overview?.globalRecruitment} />
          <GlobalInsights insights={insights || []} />
        </div>

        {/* Workforce Trends */}
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Global Workforce Trends</h2>
          <div className="h-64 flex items-center justify-center bg-gray-50 rounded-lg">
            <div className="text-center text-gray-500">
              <svg className="h-12 w-12 mx-auto mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 12l3-3 3 3 4-4M8 21l4-4 4 4M3 4h18M4 4h16v12a1 1 0 01-1 1H5a1 1 0 01-1-1V4z" />
              </svg>
              <p>12-month headcount trend chart</p>
              <p className="text-xs mt-1">Showing growth across all countries</p>
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default GlobalDashboard;

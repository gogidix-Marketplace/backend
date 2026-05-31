'use client';

import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { DashboardLayout } from '../../components/layout/GlobalDashboardLayout';
import { globalHrApi } from '../../api/globalHrApi';

const RecruitmentPage: React.FC = () => {
  const [selectedCountry, setSelectedCountry] = useState<string>('all');
  const [activeTab, setActiveTab] = useState<'overview' | 'by-country' | 'metrics'>('overview');

  const { data: recruitment } = useQuery({
    queryKey: ['global-recruitment', selectedCountry],
    queryFn: () => globalHrApi.getGlobalRecruitment(),
  });

  const { data: byCountry } = useQuery({
    queryKey: ['recruitment-by-country'],
    queryFn: () => globalHrApi.getRecruitmentByCountry(),
  });

  const totalCandidates = recruitment?.totalApplicants || 0;
  const hires = recruitment?.globalPipeline.find(p => p.stage === 'hired')?.count || 0;
  const conversionRate = totalCandidates > 0 ? (hires / totalCandidates * 100).toFixed(1) : '0';

  return (
    <DashboardLayout>
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Global Talent Acquisition</h1>
            <p className="text-gray-600">Worldwide recruitment pipeline and metrics</p>
          </div>
          <button className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
            + Post Global Job
          </button>
        </div>

        {/* Summary Cards */}
        <div className="grid grid-cols-1 md:grid-cols-5 gap-4">
          <div className="bg-white rounded-lg shadow p-6">
            <p className="text-sm text-gray-600">Active Jobs</p>
            <p className="text-3xl font-bold text-gray-900">{recruitment?.totalActiveJobs || 0}</p>
            <p className="text-xs text-gray-500 mt-1">Worldwide</p>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <p className="text-sm text-gray-600">Applicants</p>
            <p className="text-3xl font-bold text-blue-600">{recruitment?.totalApplicants?.toLocaleString() || 0}</p>
            <p className="text-xs text-gray-500 mt-1">Total pipeline</p>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <p className="text-sm text-gray-600">In Pipeline</p>
            <p className="text-3xl font-bold text-purple-600">
              {recruitment?.globalPipeline.find(p => p.stage === 'interview')?.count || 0}
            </p>
            <p className="text-xs text-gray-500 mt-1">Interview stage</p>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <p className="text-sm text-gray-600">Hired</p>
            <p className="text-3xl font-bold text-green-600">{hires}</p>
            <p className="text-xs text-gray-500 mt-1">{conversionRate}% conversion</p>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <p className="text-sm text-gray-600">Time to Fill</p>
            <p className="text-3xl font-bold text-gray-900">{recruitment?.averageTimeToFill || 0}</p>
            <p className="text-xs text-gray-500 mt-1">Average days</p>
          </div>
        </div>

        {/* Tabs Content */}
        <div className="bg-white rounded-lg shadow">
          <div className="border-b border-gray-200">
            <nav className="flex -mb-px">
              {[
                { id: 'overview', label: 'Overview' },
                { id: 'by-country', label: 'By Country' },
                { id: 'metrics', label: 'Metrics & KPIs' },
              ].map((tab) => (
                <button
                  key={tab.id}
                  onClick={() => setActiveTab(tab.id as any)}
                  className={`px-6 py-4 text-sm font-medium border-b-2 transition-colors ${
                    activeTab === tab.id
                      ? 'border-blue-500 text-blue-600'
                      : 'border-transparent text-gray-500 hover:text-gray-700'
                  }`}
                >
                  {tab.label}
                </button>
              ))}
            </nav>
          </div>

          <div className="p-6">
            {activeTab === 'overview' && (
              <div className="space-y-6">
                {/* Pipeline Funnel */}
                <div>
                  <h3 className="text-lg font-semibold text-gray-900 mb-4">Global Pipeline Funnel</h3>
                  <div className="flex items-center justify-between">
                    {recruitment?.globalPipeline.map((stage, index) => (
                      <div key={stage.stage} className="flex-1 text-center">
                        <div
                          className={`mx-1 py-8 rounded-lg ${
                            stage.stage === 'hired' ? 'bg-green-100' :
                            stage.stage === 'offer' ? 'bg-pink-100' :
                            stage.stage === 'interview' ? 'bg-purple-100' :
                            stage.stage === 'screened' ? 'bg-indigo-100' :
                            'bg-blue-100'
                          }`}
                          style={{
                            width: `${100 - (index * 10)}%`,
                            marginLeft: `${index * 5}%`,
                          }}
                        >
                          <p className="text-2xl font-bold text-gray-900">{stage.count}</p>
                          <p className="text-sm text-gray-600 capitalize">{stage.stage}</p>
                          <p className="text-xs text-gray-500">{stage.percentage.toFixed(1)}%</p>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>

                {/* Cost Metrics */}
                <div className="grid grid-cols-2 gap-4">
                  <div className="bg-gray-50 rounded-lg p-4">
                    <p className="text-sm text-gray-600">Cost per Hire</p>
                    <p className="text-2xl font-bold text-gray-900">${recruitment?.costPerHire || 0}</p>
                    <p className="text-xs text-green-600 mt-1">↓ 5% vs last year</p>
                  </div>
                  <div className="bg-gray-50 rounded-lg p-4">
                    <p className="text-sm text-gray-600">Total Recruitment Spend</p>
                    <p className="text-2xl font-bold text-gray-900">
                      ${(recruitment?.costPerHire * hires / 1000).toFixed(0)}K
                    </p>
                    <p className="text-xs text-gray-500 mt-1">This quarter</p>
                  </div>
                </div>
              </div>
            )}

            {activeTab === 'by-country' && (
              <div>
                <h3 className="text-lg font-semibold text-gray-900 mb-4">Recruitment by Country</h3>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                  {byCountry?.map((country) => (
                    <div key={country.countryCode} className="bg-gray-50 rounded-lg p-4">
                      <div className="flex items-center justify-between mb-3">
                        <div className="flex items-center gap-2">
                          <span className="text-xl">🌍</span>
                          <span className="font-medium text-gray-900">{country.countryName}</span>
                        </div>
                        <span className="text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded-full">
                          {country.countryCode}
                        </span>
                      </div>
                      <div className="grid grid-cols-2 gap-2">
                        <div>
                          <p className="text-xs text-gray-500">Applicants</p>
                          <p className="text-lg font-semibold text-blue-600">{country.applicants}</p>
                        </div>
                        <div>
                          <p className="text-xs text-gray-500">Hires</p>
                          <p className="text-lg font-semibold text-green-600">{country.hires}</p>
                        </div>
                      </div>
                      <div className="mt-2">
                        <p className="text-xs text-gray-500">Conversion Rate</p>
                        <div className="w-full bg-gray-200 rounded-full h-2">
                          <div
                            className="bg-green-500 h-2 rounded-full"
                            style={{ width: `${(country.hires / country.applicants * 100).toFixed(1)}%` }}
                          />
                        </div>
                        <p className="text-xs text-right text-gray-600 mt-1">
                          {(country.hires / country.applicants * 100).toFixed(1)}%
                        </p>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {activeTab === 'metrics' && (
              <div>
                <h3 className="text-lg font-semibold text-gray-900 mb-4">Recruitment KPIs</h3>
                <div className="space-y-4">
                  {[
                    { metric: 'Time to Fill', current: recruitment?.averageTimeToFill || 0, target: 30, unit: 'days' },
                    { metric: 'Cost per Hire', current: recruitment?.costPerHire || 0, target: 4000, unit: 'USD' },
                    { metric: 'Offer Acceptance Rate', current: 85, target: 80, unit: '%' },
                    { metric: 'Quality of Hire', current: 4.2, target: 4.0, unit: '/5' },
                  ].map((kpi) => {
                    const isOnTrack = kpi.current <= kpi.target || kpi.current >= kpi.target;
                    return (
                      <div key={kpi.metric} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                        <div>
                          <p className="font-medium text-gray-900">{kpi.metric}</p>
                          <p className="text-sm text-gray-600">Target: {kpi.target} {kpi.unit}</p>
                        </div>
                        <div className="text-right">
                          <p className={`text-lg font-semibold ${isOnTrack ? 'text-green-600' : 'text-red-600'}`}>
                            {kpi.current} {kpi.unit}
                          </p>
                          <p className={`text-xs ${isOnTrack ? 'text-green-600' : 'text-red-600'}`}>
                            {isOnTrack ? '✓ On track' : '⚠️ Needs attention'}
                          </p>
                        </div>
                      </div>
                    );
                  })}
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default RecruitmentPage;

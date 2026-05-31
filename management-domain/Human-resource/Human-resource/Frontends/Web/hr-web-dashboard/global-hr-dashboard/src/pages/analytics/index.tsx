'use client';

import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { DashboardLayout } from '../../components/layout/GlobalDashboardLayout';
import { globalHrApi } from '../../api/globalHrApi';

const AnalyticsPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'workforce' | 'retention' | 'diversity' | 'trends'>('trends');

  const { data: trends } = useQuery({
    queryKey: ['workforce-trends'],
    queryFn: () => globalHrApi.getWorkforceTrends(12),
  });

  const { data: diversity } = useQuery({
    queryKey: ['diversity-metrics'],
    queryFn: () => globalHrApi.getDiversityMetrics(),
  });

  const { data: retention } = useQuery({
    queryKey: ['retention-analysis'],
    queryFn: () => globalHrApi.getRetentionAnalysis(),
  });

  return (
    <DashboardLayout>
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Global HR Analytics</h1>
            <p className="text-gray-600">Strategic insights and workforce planning</p>
          </div>
          <button className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
            Export Analytics
          </button>
        </div>

        {/* Key Metrics */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div className="bg-white rounded-lg shadow p-6">
            <p className="text-sm text-gray-600">Growth Rate</p>
            <p className={`text-2xl font-bold ${trends?.growthRate >= 0 ? 'text-green-600' : 'text-red-600'}`}>
              {trends?.growthRate >= 0 ? '+' : ''}{trends?.growthRate?.toFixed(1) || 0}%
            </p>
            <p className="text-xs text-gray-500 mt-1">Year over year</p>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <p className="text-sm text-gray-600">Turnover Rate</p>
            <p className="text-2xl font-bold text-gray-900">{trends?.turnoverRate || 0}%</p>
            <p className="text-xs text-green-600 mt-1">↓ 1.2% vs last year</p>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <p className="text-sm text-gray-600">Retention Rate</p>
            <p className="text-2xl font-bold text-green-600">{trends?.retentionRate || 0}%</p>
            <p className="text-xs text-gray-500 mt-1">Industry avg: 88%</p>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <p className="text-sm text-gray-600">Diversity Index</p>
            <p className="text-2xl font-bold text-blue-600">{trends?.diversityIndex || 0}</p>
            <p className="text-xs text-green-600 mt-1">↑ 3 points</p>
          </div>
        </div>

        {/* Tabs */}
        <div className="bg-white rounded-lg shadow">
          <div className="border-b border-gray-200">
            <nav className="flex -mb-px">
              {[
                { id: 'trends', label: 'Workforce Trends' },
                { id: 'workforce', label: 'Workforce Planning' },
                { id: 'retention', label: 'Retention Analysis' },
                { id: 'diversity', label: 'Diversity & Inclusion' },
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
            {activeTab === 'trends' && (
              <div className="space-y-6">
                {/* Headcount Trend */}
                <div>
                  <h3 className="text-lg font-semibold text-gray-900 mb-4">12-Month Headcount Trend</h3>
                  <div className="h-64 flex items-center justify-center bg-gray-50 rounded-lg">
                    <div className="text-center text-gray-500">
                      <svg className="h-12 w-12 mx-auto mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 12l3-3 3 3 3 4-4M8 21l4-4 4 4M3 4h18M4 4h16v12a1 1 0 001 1H5a1 1 0 01-1-1V4z" />
                      </svg>
                      <p>Headcount trend chart</p>
                      <p className="text-xs mt-1">Monthly headcount, hires, and departures</p>
                    </div>
                  </div>
                </div>

                {/* Monthly Breakdown */}
                <div className="overflow-x-auto">
                  <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                      <tr>
                        <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Month</th>
                        <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Headcount</th>
                        <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Hires</th>
                        <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Departures</th>
                        <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Net Change</th>
                      </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                      {trends?.monthlyHeadcount.slice(-6).map((month) => (
                        <tr key={month.month}>
                          <td className="px-4 py-2 text-sm text-gray-900">{month.month}</td>
                          <td className="px-4 py-2 text-sm text-gray-900">{month.headcount.toLocaleString()}</td>
                          <td className="px-4 py-2 text-sm text-green-600">+{month.hires}</td>
                          <td className="px-4 py-2 text-sm text-red-600">-{month.departures}</td>
                          <td className={`px-4 py-2 text-sm font-semibold ${month.hires - month.departures >= 0 ? 'text-green-600' : 'text-red-600'}`}>
                            {month.hires - month.departures >= 0 ? '+' : ''}{month.hires - month.departures}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            )}

            {activeTab === 'retention' && (
              <div>
                <h3 className="text-lg font-semibold text-gray-900 mb-4">Global Retention Analysis</h3>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div className="bg-green-50 border border-green-200 rounded-lg p-4">
                    <h4 className="font-medium text-green-800">Top Performers</h4>
                    <div className="mt-2 space-y-2">
                      {['Nigeria: 94%', 'Kenya: 91%', 'Ireland: 96%'].map((item) => (
                        <div key={item} className="flex justify-between text-sm">
                          <span className="text-gray-600">{item.split(':')[0]}</span>
                          <span className="font-semibold text-green-700">{item.split(':')[1]}</span>
                        </div>
                      ))}
                    </div>
                  </div>
                  <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
                    <h4 className="font-medium text-yellow-800">Needs Attention</h4>
                    <div className="mt-2 space-y-2">
                      {['South Africa: 82%', 'Egypt: 79%', 'Brazil: 85%'].map((item) => (
                        <div key={item} className="flex justify-between text-sm">
                          <span className="text-gray-600">{item.split(':')[0]}</span>
                          <span className="font-semibold text-yellow-700">{item.split(':')[1]}</span>
                        </div>
                      ))}
                    </div>
                  </div>
                  <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
                    <h4 className="font-medium text-blue-800">Key Factors</h4>
                    <div className="mt-2 space-y-2 text-sm text-gray-600">
                      <div className="flex justify-between">
                        <span>Salary competitiveness</span>
                        <span className="font-medium">High impact</span>
                      </div>
                      <div className="flex justify-between">
                        <span>Career development</span>
                        <span className="font-medium">Medium impact</span>
                      </div>
                      <div className="flex justify-between">
                        <span>Work-life balance</span>
                        <span className="font-medium">Medium impact</span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            )}

            {activeTab === 'diversity' && (
              <div>
                <h3 className="text-lg font-semibold text-gray-900 mb-4">Global Diversity & Inclusion</h3>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div className="bg-gray-50 rounded-lg p-6">
                    <h4 className="font-medium text-gray-900 mb-4">By Region</h4>
                    <div className="space-y-3">
                      {[
                        { region: 'Africa', diversity: 42, female: 38 },
                        { region: 'Europe', diversity: 35, female: 41 },
                        { region: 'Asia Pacific', diversity: 58, female: 45 },
                        { region: 'Americas', diversity: 48, female: 46 },
                      ].map((item) => (
                        <div key={item.region}>
                          <div className="flex justify-between mb-1">
                            <span className="text-sm font-medium text-gray-700">{item.region}</span>
                            <span className="text-sm font-semibold text-gray-900">{item.diversity}%</span>
                          </div>
                          <div className="flex h-2 rounded-full overflow-hidden">
                            <div className="bg-blue-500" style={{ width: `${item.female}%` }}></div>
                            <div className="bg-purple-500" style={{ width: `${100 - item.female}%` }}></div>
                          </div>
                          <div className="flex justify-between text-xs text-gray-500 mt-1">
                            <span>F: {item.female}%</span>
                            <span>M: {100 - item.female}%</span>
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>

                  <div className="bg-gray-50 rounded-lg p-6">
                    <h4 className="font-medium text-gray-900 mb-4">Leadership Diversity</h4>
                    <div className="space-y-3">
                      {[
                        { level: 'C-Level', diversity: 28, target: 35 },
                        { level: 'VP Level', diversity: 35, target: 40 },
                        { level: 'Director', diversity: 38, target: 45 },
                        { level: 'Manager', diversity: 45, target: 50 },
                      ].map((item) => (
                        <div key={item.level} className="flex items-center justify-between">
                          <span className="text-sm text-gray-700">{item.level}</span>
                          <div className="flex-1 mx-4">
                            <div className="w-full bg-gray-200 rounded-full h-2">
                              <div
                                className={`h-2 rounded-full ${
                                  item.diversity >= item.target ? 'bg-green-500' : 'bg-yellow-500'
                                }`}
                                style={{ width: `${item.diversity}%` }}
                              />
                            </div>
                          </div>
                          <span className="text-sm text-gray-900">{item.diversity}% / {item.target}%</span>
                        </div>
                      ))}
                    </div>
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default AnalyticsPage;

'use client';

import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { ProtectedRoute } from '../../components/common/ProtectedRoute';
import { DashboardLayout } from '../../components/layout/DashboardLayout';
import { hrApi } from '../../api/hrApi';

const AnalyticsPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'workforce' | 'retention' | 'diversity' | 'insights'>('workforce');

  const { data: insights } = useQuery({
    queryKey: ['insights'],
    queryFn: () => hrApi.getInsights(),
  });

  return (
    <ProtectedRoute>
      <DashboardLayout>
        <div className="space-y-6">
          {/* Header */}
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">Analytics & Insights</h1>
              <p className="text-gray-600">Data-driven insights for strategic HR decisions</p>
            </div>
            <button className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
              Export Dashboard
            </button>
          </div>

          {/* Tabs */}
          <div className="bg-white rounded-lg shadow">
            <div className="border-b border-gray-200">
              <nav className="flex -mb-px">
                {[
                  { id: 'workforce', label: 'Workforce Planning' },
                  { id: 'retention', label: 'Retention Analysis' },
                  { id: 'diversity', label: 'Diversity & Inclusion' },
                  { id: 'insights', label: 'AI Insights' },
                ].map((tab) => (
                  <button
                    key={tab.id}
                    onClick={() => setActiveTab(tab.id as any)}
                    className={`px-6 py-4 text-sm font-medium border-b-2 transition-colors ${
                      activeTab === tab.id
                        ? 'border-blue-500 text-blue-600'
                        : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
                    }`}
                  >
                    {tab.label}
                  </button>
                ))}
              </nav>
            </div>

            <div className="p-6">
              {activeTab === 'workforce' && (
                <div className="space-y-6">
                  {/* Headcount Trends */}
                  <div>
                    <h3 className="text-lg font-semibold text-gray-900 mb-4">Headcount Trends</h3>
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
                      <div className="bg-gray-50 rounded-lg p-4">
                        <p className="text-sm text-gray-600">Current Headcount</p>
                        <p className="text-2xl font-bold text-gray-900">3,450</p>
                        <p className="text-xs text-green-600 mt-1">+8.2% vs last year</p>
                      </div>
                      <div className="bg-gray-50 rounded-lg p-4">
                        <p className="text-sm text-gray-600">Projected Growth (Q4)</p>
                        <p className="text-2xl font-bold text-gray-900">3,650</p>
                        <p className="text-xs text-blue-600 mt-1">+5.8% projected</p>
                      </div>
                      <div className="bg-gray-50 rounded-lg p-4">
                        <p className="text-sm text-gray-600">Open Positions</p>
                        <p className="text-2xl font-bold text-gray-900">142</p>
                        <p className="text-xs text-yellow-600 mt-1">42 critical roles</p>
                      </div>
                    </div>

                    {/* Mock Chart Area */}
                    <div className="bg-gray-50 rounded-lg p-6 h-64 flex items-center justify-center">
                      <div className="text-center text-gray-500">
                        <svg className="h-12 w-12 mx-auto mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 12l3-3 3 3 4-4M8 21l4-4 4 4M3 4h18M4 4h16v12a1 1 0 01-1 1H5a1 1 0 01-1-1V4z" />
                        </svg>
                        <p>Headcount trend chart visualization</p>
                        <p className="text-xs mt-1">Last 12 months by country</p>
                      </div>
                    </div>
                  </div>

                  {/* Department Breakdown */}
                  <div>
                    <h3 className="text-lg font-semibold text-gray-900 mb-4">Headcount by Department</h3>
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                      {[
                        { dept: 'Engineering', count: 850, growth: '+12%' },
                        { dept: 'Sales', count: 620, growth: '+8%' },
                        { dept: 'Marketing', count: 340, growth: '+5%' },
                        { dept: 'Operations', count: 480, growth: '+3%' },
                        { dept: 'Finance', count: 180, growth: '+2%' },
                        { dept: 'HR', count: 120, growth: '+4%' },
                        { dept: 'Legal', count: 85, growth: '+1%' },
                        { dept: 'Support', count: 775, growth: '+10%' },
                      ].map((item) => (
                        <div key={item.dept} className="bg-gray-50 rounded-lg p-4">
                          <p className="text-sm text-gray-600">{item.dept}</p>
                          <p className="text-xl font-bold text-gray-900">{item.count}</p>
                          <p className="text-xs text-green-600">{item.growth} YoY</p>
                        </div>
                      ))}
                    </div>
                  </div>
                </div>
              )}

              {activeTab === 'retention' && (
                <div className="space-y-6">
                  {/* Retention Metrics */}
                  <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                    <div className="bg-gray-50 rounded-lg p-4">
                      <p className="text-sm text-gray-600">Overall Retention Rate</p>
                      <p className="text-2xl font-bold text-green-600">92%</p>
                      <p className="text-xs text-gray-500 mt-1">Industry avg: 88%</p>
                    </div>
                    <div className="bg-gray-50 rounded-lg p-4">
                      <p className="text-sm text-gray-600">Voluntary Turnover</p>
                      <p className="text-2xl font-bold text-gray-900">6.5%</p>
                      <p className="text-xs text-green-600 mt-1">-1.2% vs last year</p>
                    </div>
                    <div className="bg-gray-50 rounded-lg p-4">
                      <p className="text-sm text-gray-600">Involuntary Turnover</p>
                      <p className="text-2xl font-bold text-gray-900">1.5%</p>
                      <p className="text-xs text-gray-500 mt-1">Stable</p>
                    </div>
                    <div className="bg-gray-50 rounded-lg p-4">
                      <p className="text-sm text-gray-600">Avg Tenure</p>
                      <p className="text-2xl font-bold text-gray-900">3.8 yrs</p>
                      <p className="text-xs text-green-600 mt-1">+0.3 vs last year</p>
                    </div>
                  </div>

                  {/* Retention by Tenure */}
                  <div>
                    <h3 className="text-lg font-semibold text-gray-900 mb-4">Retention by Tenure</h3>
                    <div className="space-y-3">
                      {[
                        { range: '0-1 years', rate: 85, count: 850 },
                        { range: '1-2 years', rate: 90, count: 720 },
                        { range: '2-5 years', rate: 94, count: 1050 },
                        { range: '5-10 years', rate: 96, count: 620 },
                        { range: '10+ years', rate: 98, count: 210 },
                      ].map((item) => (
                        <div key={item.range} className="flex items-center">
                          <span className="w-32 text-sm text-gray-700">{item.range}</span>
                          <div className="flex-1 mx-4">
                            <div className="bg-gray-200 rounded-full h-4">
                              <div
                                className="bg-green-500 h-4 rounded-full"
                                style={{ width: `${item.rate}%` }}
                              ></div>
                            </div>
                          </div>
                          <span className="w-16 text-sm font-medium text-gray-900">{item.rate}%</span>
                          <span className="w-20 text-xs text-gray-500 text-right">{item.count} employees</span>
                        </div>
                      ))}
                    </div>
                  </div>

                  {/* Flight Risk Analysis */}
                  <div>
                    <h3 className="text-lg font-semibold text-gray-900 mb-4">Flight Risk Analysis</h3>
                    <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
                      <div className="flex items-center mb-3">
                        <svg className="h-5 w-5 text-yellow-600 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
                        </svg>
                        <span className="font-medium text-yellow-800">High Flight Risk Employees</span>
                        <span className="ml-auto px-2 py-1 bg-yellow-200 text-yellow-800 rounded-full text-sm font-medium">48</span>
                      </div>
                      <p className="text-sm text-yellow-700">
                        Based on engagement scores, tenure, and market conditions. Recommend retention actions for top performers.
                      </p>
                    </div>
                  </div>
                </div>
              )}

              {activeTab === 'diversity' && (
                <div className="space-y-6">
                  {/* Diversity Summary */}
                  <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                    <div className="bg-gray-50 rounded-lg p-4">
                      <p className="text-sm text-gray-600">Gender Diversity</p>
                      <p className="text-2xl font-bold text-gray-900">42%</p>
                      <p className="text-xs text-gray-500 mt-1">Female representation</p>
                    </div>
                    <div className="bg-gray-50 rounded-lg p-4">
                      <p className="text-sm text-gray-600">Underrepresented Groups</p>
                      <p className="text-2xl font-bold text-gray-900">35%</p>
                      <p className="text-xs text-green-600 mt-1">+3% vs last year</p>
                    </div>
                    <div className="bg-gray-50 rounded-lg p-4">
                      <p className="text-sm text-gray-600">Leadership Diversity</p>
                      <p className="text-2xl font-bold text-gray-900">38%</p>
                      <p className="text-xs text-green-600 mt-1">Executive level</p>
                    </div>
                    <div className="bg-gray-50 rounded-lg p-4">
                      <p className="text-sm text-gray-600">Global Representation</p>
                      <p className="text-2xl font-bold text-gray-900">24</p>
                      <p className="text-xs text-gray-500 mt-1">Countries</p>
                    </div>
                  </div>

                  {/* Gender by Level */}
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="bg-gray-50 rounded-lg p-6">
                      <h4 className="font-medium text-gray-900 mb-4">Gender Distribution by Level</h4>
                      <div className="space-y-3">
                        {[
                          { level: 'Executive', male: 12, female: 8, other: 0 },
                          { level: 'Director', male: 45, female: 32, other: 2 },
                          { level: 'Manager', male: 180, female: 145, other: 8 },
                          { level: 'Individual Contributor', male: 1450, female: 1120, other: 45 },
                        ].map((item) => {
                          const total = item.male + item.female + item.other;
                          return (
                            <div key={item.level}>
                              <div className="flex justify-between text-sm mb-1">
                                <span className="text-gray-700">{item.level}</span>
                                <span className="text-gray-500">{total} total</span>
                              </div>
                              <div className="flex h-4 rounded-full overflow-hidden">
                                <div className="bg-blue-500" style={{ width: `${(item.male / total) * 100}%` }}></div>
                                <div className="bg-pink-500" style={{ width: `${(item.female / total) * 100}%` }}></div>
                                <div className="bg-purple-500" style={{ width: `${(item.other / total) * 100}%` }}></div>
                              </div>
                              <div className="flex justify-between text-xs text-gray-500 mt-1">
                                <span>M: {item.male}</span>
                                <span>F: {item.female}</span>
                                <span>O: {item.other}</span>
                              </div>
                            </div>
                          );
                        })}
                      </div>
                    </div>

                    <div className="bg-gray-50 rounded-lg p-6">
                      <h4 className="font-medium text-gray-900 mb-4">Geographic Distribution</h4>
                      <div className="space-y-3">
                        {[
                          { country: 'United States', flag: '🇺🇸', percentage: 35, diversity: 42 },
                          { country: 'United Kingdom', flag: '🇬🇧', percentage: 18, diversity: 38 },
                          { country: 'Germany', flag: '🇩🇪', percentage: 14, diversity: 35 },
                          { country: 'France', flag: '🇫🇷', percentage: 12, diversity: 40 },
                          { country: 'Other Europe', flag: '🇪🇺', percentage: 12, diversity: 45 },
                          { country: 'Asia Pacific', flag: '🌏', percentage: 9, diversity: 58 },
                        ].map((item) => (
                          <div key={item.country} className="flex items-center">
                            <span className="text-xl mr-2">{item.flag}</span>
                            <span className="flex-1 text-sm text-gray-700">{item.country}</span>
                            <div className="w-24 bg-gray-200 rounded-full h-2 mr-2">
                              <div className="bg-blue-600 h-2 rounded-full" style={{ width: `${item.percentage}%` }}></div>
                            </div>
                            <span className="w-8 text-sm text-gray-900">{item.percentage}%</span>
                            <span className="w-16 text-xs text-gray-500 text-right">{item.diversity}% D&I</span>
                          </div>
                        ))}
                      </div>
                    </div>
                  </div>
                </div>
              )}

              {activeTab === 'insights' && (
                <div className="space-y-6">
                  <h3 className="text-lg font-semibold text-gray-900 mb-4">AI-Powered Insights</h3>

                  {insights?.recommendations ? (
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                      {insights.recommendations.map((insight: any, index: number) => (
                        <div key={index} className="bg-blue-50 border border-blue-200 rounded-lg p-4">
                          <div className="flex items-start">
                            <svg className="h-5 w-5 text-blue-600 mr-2 mt-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
                            </svg>
                            <div>
                              <h4 className="font-medium text-blue-900">{insight.title}</h4>
                              <p className="text-sm text-blue-700 mt-1">{insight.description}</p>
                              <button className="mt-2 text-xs font-medium text-blue-600 hover:text-blue-800">
                                View details →
                              </button>
                            </div>
                          </div>
                        </div>
                      ))}
                    </div>
                  ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                      {[
                        {
                          title: 'Recruitment Efficiency',
                          description: 'Time-to-fill in Germany is 40% above average. Consider local recruitment partnerships.',
                          priority: 'high',
                        },
                        {
                          title: 'Retention Risk Alert',
                          description: 'Engineering team shows 15% higher turnover in the 1-2 year tenure range.',
                          priority: 'high',
                        },
                        {
                          title: 'Compensation Benchmark',
                          description: 'Salaries for Product Managers in France are 8% below market. Adjust for retention.',
                          priority: 'medium',
                        },
                        {
                          title: 'Training Impact',
                          description: 'Teams with completed leadership training show 22% better performance scores.',
                          priority: 'low',
                        },
                      ].map((insight, index) => (
                        <div key={index} className="bg-gray-50 rounded-lg p-4">
                          <div className="flex items-start justify-between">
                            <div className="flex-1">
                              <div className="flex items-center gap-2">
                                <span className={`px-2 py-1 text-xs font-medium rounded-full ${
                                  insight.priority === 'high' ? 'bg-red-100 text-red-800' :
                                  insight.priority === 'medium' ? 'bg-yellow-100 text-yellow-800' :
                                  'bg-blue-100 text-blue-800'
                                }`}>
                                  {insight.priority}
                                </span>
                              </div>
                              <h4 className="font-medium text-gray-900 mt-2">{insight.title}</h4>
                              <p className="text-sm text-gray-600 mt-1">{insight.description}</p>
                            </div>
                          </div>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              )}
            </div>
          </div>
        </div>
      </DashboardLayout>
    </ProtectedRoute>
  );
};

export default AnalyticsPage;

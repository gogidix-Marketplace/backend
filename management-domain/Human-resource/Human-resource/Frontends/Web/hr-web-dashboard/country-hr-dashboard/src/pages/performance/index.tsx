'use client';

import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { ProtectedRoute } from '../../components/common/ProtectedRoute';
import { DashboardLayout } from '../../components/layout/DashboardLayout';
import { hrApi, PerformanceSummary } from '../../api/hrApi';

const PerformancePage: React.FC = () => {
  const [selectedPeriod, setSelectedPeriod] = useState<'q1' | 'q2' | 'q3' | 'q4' | 'ytd'>('ytd');
  const [activeTab, setActiveTab] = useState<'overview' | 'reviews' | 'training' | 'development'>('overview');

  const { data: performance, isLoading } = useQuery({
    queryKey: ['global-performance', selectedPeriod],
    queryFn: () => hrApi.getGlobalPerformance(),
  });

  const getRatingColor = (rating: number) => {
    if (rating >= 4) return 'text-green-600';
    if (rating >= 3) return 'text-yellow-600';
    return 'text-red-600';
  };

  const getCompletionColor = (rate: number) => {
    if (rate >= 80) return 'bg-green-500';
    if (rate >= 60) return 'bg-yellow-500';
    return 'bg-red-500';
  };

  return (
    <ProtectedRoute>
      <DashboardLayout>
        <div className="space-y-6">
          {/* Header */}
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">Performance & Development</h1>
              <p className="text-gray-600">Track performance reviews and employee development</p>
            </div>
            <select
              value={selectedPeriod}
              onChange={(e) => setSelectedPeriod(e.target.value as any)}
              className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
            >
              <option value="ytd">Year to Date</option>
              <option value="q1">Q1</option>
              <option value="q2">Q2</option>
              <option value="q3">Q3</option>
              <option value="q4">Q4</option>
            </select>
          </div>

          {isLoading ? (
            <div className="flex items-center justify-center h-64">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
            </div>
          ) : (
            <>
              {/* Summary Metrics */}
              <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                <div className="bg-white rounded-lg shadow p-6">
                  <p className="text-sm text-gray-600">Average Rating</p>
                  <p className={`text-2xl font-bold ${getRatingColor(performance?.averageRating || 0)}`}>
                    {performance?.averageRating?.toFixed(1) || 0}/5.0
                  </p>
                </div>
                <div className="bg-white rounded-lg shadow p-6">
                  <p className="text-sm text-gray-600">Review Completion</p>
                  <div className="flex items-center gap-2">
                    <p className="text-2xl font-bold text-gray-900">
                      {performance?.reviewCompletionRate || 0}%
                    </p>
                    <div className="flex-1 bg-gray-200 rounded-full h-2">
                      <div
                        className={`h-2 rounded-full ${getCompletionColor(performance?.reviewCompletionRate || 0)}`}
                        style={{ width: `${performance?.reviewCompletionRate || 0}%` }}
                      ></div>
                    </div>
                  </div>
                </div>
                <div className="bg-white rounded-lg shadow p-6">
                  <p className="text-sm text-gray-600">Training Completion</p>
                  <div className="flex items-center gap-2">
                    <p className="text-2xl font-bold text-gray-900">
                      {performance?.trainingCompletionRate || 0}%
                    </p>
                    <div className="flex-1 bg-gray-200 rounded-full h-2">
                      <div
                        className={`h-2 rounded-full ${getCompletionColor(performance?.trainingCompletionRate || 0)}`}
                        style={{ width: `${performance?.trainingCompletionRate || 0}%` }}
                      ></div>
                    </div>
                  </div>
                </div>
                <div className="bg-white rounded-lg shadow p-6">
                  <p className="text-sm text-gray-600">Active Countries</p>
                  <p className="text-2xl font-bold text-gray-900">
                    {performance?.activeCountries || 0}
                  </p>
                </div>
              </div>

              {/* Tabs */}
              <div className="bg-white rounded-lg shadow">
                <div className="border-b border-gray-200">
                  <nav className="flex -mb-px">
                    {[
                      { id: 'overview', label: 'Overview' },
                      { id: 'reviews', label: 'Performance Reviews' },
                      { id: 'training', label: 'Training' },
                      { id: 'development', label: 'Leadership Development' },
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
                  {activeTab === 'overview' && (
                    <div className="space-y-6">
                      {/* Rating Distribution */}
                      <div>
                        <h3 className="text-lg font-semibold text-gray-900 mb-4">Rating Distribution</h3>
                        <div className="space-y-3">
                          {[
                            { rating: '5 - Exceeds Expectations', count: 45, percentage: 15 },
                            { rating: '4 - Exceeds Expectations', count: 112, percentage: 37 },
                            { rating: '3 - Meets Expectations', count: 98, percentage: 33 },
                            { rating: '2 - Below Expectations', count: 35, percentage: 12 },
                            { rating: '1 - Unsatisfactory', count: 10, percentage: 3 },
                          ].map((item) => (
                            <div key={item.rating} className="flex items-center">
                              <span className="w-48 text-sm text-gray-700">{item.rating}</span>
                              <div className="flex-1 mx-4">
                                <div className="bg-gray-200 rounded-full h-4">
                                  <div
                                    className="bg-blue-600 h-4 rounded-full"
                                    style={{ width: `${item.percentage}%` }}
                                  ></div>
                                </div>
                              </div>
                              <span className="w-12 text-sm font-medium text-gray-900">{item.count}</span>
                            </div>
                          ))}
                        </div>
                      </div>

                      {/* Performance by Country */}
                      <div>
                        <h3 className="text-lg font-semibold text-gray-900 mb-4">Performance by Country</h3>
                        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                          {[
                            { country: 'United States', flag: '🇺🇸', avgRating: 4.2, completion: 92 },
                            { country: 'Germany', flag: '🇩🇪', avgRating: 3.9, completion: 88 },
                            { country: 'France', flag: '🇫🇷', avgRating: 3.7, completion: 85 },
                            { country: 'United Kingdom', flag: '🇬🇧', avgRating: 4.0, completion: 90 },
                            { country: 'Spain', flag: '🇪🇸', avgRating: 3.8, completion: 82 },
                            { country: 'Italy', flag: '🇮🇹', avgRating: 3.6, completion: 78 },
                          ].map((item) => (
                            <div key={item.country} className="bg-gray-50 rounded-lg p-4">
                              <div className="flex items-center gap-2 mb-3">
                                <span className="text-xl">{item.flag}</span>
                                <span className="font-medium text-gray-900">{item.country}</span>
                              </div>
                              <div className="space-y-2">
                                <div className="flex justify-between text-sm">
                                  <span className="text-gray-600">Avg Rating</span>
                                  <span className={`font-medium ${getRatingColor(item.avgRating)}`}>
                                    {item.avgRating}/5.0
                                  </span>
                                </div>
                                <div className="flex justify-between text-sm">
                                  <span className="text-gray-600">Completion</span>
                                  <span className="font-medium">{item.completion}%</span>
                                </div>
                              </div>
                            </div>
                          ))}
                        </div>
                      </div>
                    </div>
                  )}

                  {activeTab === 'reviews' && (
                    <div>
                      <h3 className="text-lg font-semibold text-gray-900 mb-4">Performance Reviews</h3>
                      <div className="bg-gray-50 rounded-lg p-6 text-center">
                        <svg className="h-16 w-16 text-gray-400 mx-auto mb-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                        </svg>
                        <p className="text-gray-600">Review management features coming soon.</p>
                      </div>
                    </div>
                  )}

                  {activeTab === 'training' && (
                    <div>
                      <h3 className="text-lg font-semibold text-gray-900 mb-4">Training Programs</h3>
                      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        {[
                          { name: 'Leadership Fundamentals', enrolled: 156, completed: 98, progress: 63 },
                          { name: 'Compliance Training 2024', enrolled: 450, completed: 412, progress: 92 },
                          { name: 'Diversity & Inclusion', enrolled: 280, completed: 198, progress: 71 },
                          { name: 'Performance Management', enrolled: 85, completed: 42, progress: 49 },
                        ].map((program) => (
                          <div key={program.name} className="bg-gray-50 rounded-lg p-4">
                            <h4 className="font-medium text-gray-900 mb-2">{program.name}</h4>
                            <div className="flex items-center justify-between text-sm text-gray-600 mb-2">
                              <span>{program.completed} of {program.enrolled} completed</span>
                              <span>{program.progress}%</span>
                            </div>
                            <div className="w-full bg-gray-200 rounded-full h-2">
                              <div className="bg-blue-600 h-2 rounded-full" style={{ width: `${program.progress}%` }}></div>
                            </div>
                          </div>
                        ))}
                      </div>
                    </div>
                  )}

                  {activeTab === 'development' && (
                    <div>
                      <h3 className="text-lg font-semibold text-gray-900 mb-4">Leadership Development</h3>
                      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                        {[
                          { title: 'High Potential Program', participants: 24, status: 'Active' },
                          { title: 'Executive Coaching', participants: 12, status: 'Active' },
                          { title: 'Succession Planning', participants: 45, status: 'In Progress' },
                          { title: 'Mentorship Program', participants: 89, status: 'Active' },
                          { title: 'Cross-Functional Rotation', participants: 18, status: 'Active' },
                          { title: 'Leadership Workshops', participants: 156, status: 'Scheduled' },
                        ].map((program) => (
                          <div key={program.title} className="bg-gray-50 rounded-lg p-4">
                            <h4 className="font-medium text-gray-900">{program.title}</h4>
                            <p className="text-sm text-gray-600 mt-1">{program.participants} participants</p>
                            <span className="inline-block mt-2 px-2 py-1 text-xs font-medium bg-blue-100 text-blue-800 rounded-full">
                              {program.status}
                            </span>
                          </div>
                        ))}
                      </div>
                    </div>
                  )}
                </div>
              </div>
            </>
          )}
        </div>
      </DashboardLayout>
    </ProtectedRoute>
  );
};

export default PerformancePage;

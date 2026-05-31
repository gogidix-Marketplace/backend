'use client';

import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { DashboardLayout } from '../../components/layout/GlobalDashboardLayout';
import { globalHrApi } from '../../api/globalHrApi';

const PerformancePage: React.FC = () => {
  const [selectedPeriod, setSelectedPeriod] = useState<'q1' | 'q2' | 'q3' | 'q4' | 'ytd'>('ytd');

  const { data: performance } = useQuery({
    queryKey: ['global-performance', selectedPeriod],
    queryFn: () => globalHrApi.getGlobalPerformance(selectedPeriod),
  });

  return (
    <DashboardLayout>
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Global Performance & Development</h1>
            <p className="text-gray-600">Worldwide performance tracking and development metrics</p>
          </div>
          <select
            value={selectedPeriod}
            onChange={(e) => setSelectedPeriod(e.target.value as any)}
            className="px-4 py-2 border border-gray-300 rounded-lg"
          >
            <option value="ytd">Year to Date</option>
            <option value="q1">Q1</option>
            <option value="q2">Q2</option>
            <option value="q3">Q3</option>
            <option value="q4">Q4</option>
          </select>
        </div>

        {/* Summary Metrics */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div className="bg-white rounded-lg shadow p-6">
            <p className="text-sm text-gray-600">Review Completion</p>
            <div className="flex items-center gap-2 mt-2">
              <p className="text-2xl font-bold text-gray-900">{performance?.reviewCompletionRate || 0}%</p>
              <div className="flex-1 bg-gray-200 rounded-full h-2">
                <div
                  className="bg-blue-600 h-2 rounded-full"
                  style={{ width: `${performance?.reviewCompletionRate || 0}%` }}
                />
              </div>
            </div>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <p className="text-sm text-gray-600">Average Rating</p>
            <p className={`text-2xl font-bold mt-2 ${performance?.averageRating >= 4 ? 'text-green-600' : performance?.averageRating >= 3 ? 'text-yellow-600' : 'text-red-600'}`}>
              {performance?.averageRating?.toFixed(1) || 0}/5.0
            </p>
            <p className="text-xs text-gray-500 mt-1">Global average</p>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <p className="text-sm text-gray-600">Participation</p>
            <div className="flex items-center gap-2 mt-2">
              <p className="text-2xl font-bold text-gray-900">{performance?.participationRate || 0}%</p>
              <div className="flex-1 bg-gray-200 rounded-full h-2">
                <div
                  className="bg-green-600 h-2 rounded-full"
                  style={{ width: `${performance?.participationRate || 0}%` }}
                />
              </div>
            </div>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <p className="text-sm text-gray-600">Training Completion</p>
            <p className="text-2xl font-bold text-green-600 mt-2">{performance?.trainingCompletion?.globalCompletion || 0}%</p>
            <p className="text-xs text-gray-500 mt-1">{performance?.trainingCompletion?.totalCourses || 0} courses</p>
          </div>
        </div>

        {/* Performance by Country */}
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Performance by Country</h2>
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Country</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Completion</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Avg Rating</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Participants</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Trend</th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {performance?.performanceByCountry.map((country) => (
                  <tr key={country.countryCode} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                      {country.countryName}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex items-center">
                        <div className="w-24 bg-gray-200 rounded-full h-2 mr-2">
                          <div
                            className={`h-2 rounded-full ${
                              country.completionRate >= 80 ? 'bg-green-500' :
                              country.completionRate >= 60 ? 'bg-yellow-500' :
                              'bg-red-500'
                            }`}
                            style={{ width: `${country.completionRate}%` }}
                          />
                        </div>
                        <span className="text-sm text-gray-900">{country.completionRate}%</span>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`text-sm font-semibold ${
                        country.averageRating >= 4 ? 'text-green-600' :
                        country.averageRating >= 3 ? 'text-yellow-600' :
                        'text-red-600'
                      }`}>
                        {country.averageRating.toFixed(1)}/5.0
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {country.participants}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className="text-green-600 text-sm">↑ 2%</span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>

        {/* Training Overview */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <div className="bg-white rounded-lg shadow p-6">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">Training by Country</h2>
            <div className="space-y-3">
              {performance?.trainingCompletion.byCountry.map((country) => (
                <div key={country.countryCode} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                  <div className="flex items-center gap-3">
                    <span className="text-xl">🌍</span>
                    <span className="font-medium text-gray-900">{country.countryName}</span>
                  </div>
                  <div className="flex items-center gap-4">
                    <div className="w-32 bg-gray-200 rounded-full h-2">
                      <div
                        className="bg-blue-600 h-2 rounded-full"
                        style={{ width: `${country.completion}%` }}
                      />
                    </div>
                    <span className="text-sm font-semibold text-gray-900">{country.completion}%</span>
                    <span className="text-xs text-gray-500">{country.courses} courses</span>
                  </div>
                </div>
              ))}
            </div>
          </div>

          <div className="bg-white rounded-lg shadow p-6">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">Performance Distribution</h2>
            <div className="space-y-3">
              {[
                { rating: '5 - Exceptional', count: 180, percentage: 22 },
                { rating: '4 - Exceeds Expectations', count: 350, percentage: 43 },
                { rating: '3 - Meets Expectations', count: 220, percentage: 27 },
                { rating: '2 - Below Expectations', count: 60, percentage: 7 },
                { rating: '1 - Unsatisfactory', count: 16, percentage: 1 },
              ].map((item) => (
                <div key={item.rating} className="flex items-center">
                  <span className="w-40 text-sm text-gray-600">{item.rating}</span>
                  <div className="flex-1 mx-4">
                    <div className="bg-gray-200 rounded-full h-3">
                      <div
                        className={`h-3 rounded-full ${
                          item.rating.startsWith('5') || item.rating.startsWith('4') ? 'bg-green-500' :
                          item.rating.startsWith('3') ? 'bg-yellow-500' :
                          'bg-red-500'
                        }`}
                        style={{ width: `${item.percentage}%` }}
                      />
                    </div>
                  </div>
                  <span className="w-12 text-sm font-medium text-gray-900">{item.count}</span>
                  <span className="w-12 text-sm text-gray-500">{item.percentage}%</span>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default PerformancePage;

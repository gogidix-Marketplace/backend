'use client';

import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { useRouter } from 'next/router';
import { hrApi } from '../../api/hrApi';
import { ProtectedRoute } from '../../components/common/ProtectedRoute';
import { DashboardLayout } from '../../components/layout/DashboardLayout';
import { HealthScoreCard } from '../../components/dashboard/HealthScoreCard';
import { MetricCard } from '../../components/dashboard/MetricCard';

const CountryDetailPage: React.FC = () => {
  const router = useRouter();
  const { countryCode } = router.query;

  const { data: countryDetail, isLoading } = useQuery({
    queryKey: ['country-detail', countryCode],
    queryFn: () => hrApi.getCountryDetail(countryCode as string),
    enabled: !!countryCode,
  });

  if (isLoading) {
    return (
      <DashboardLayout>
        <div className="flex items-center justify-center h-64">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>
      </DashboardLayout>
    );
  }

  if (!countryDetail) {
    return (
      <DashboardLayout>
        <div className="text-center py-12">
          <p className="text-gray-500">Country not found.</p>
          <button
            onClick={() => router.push('/countries')}
            className="mt-4 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
          >
            Back to Countries
          </button>
        </div>
      </DashboardLayout>
    );
  }

  const metrics = countryDetail?.metrics;
  const financialMetrics = countryDetail?.financialMetrics;
  const compliance = countryDetail?.compliance;

  return (
    <ProtectedRoute>
      <DashboardLayout>
        <div className="space-y-6">
          {/* Header */}
          <div className="flex items-center justify-between">
            <div className="flex items-center">
              <button
                onClick={() => router.push('/countries')}
                className="mr-4 p-2 hover:bg-gray-100 rounded-lg"
              >
                <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
                </svg>
              </button>
              <div>
                <h1 className="text-2xl font-bold text-gray-900">{countryDetail.countryName}</h1>
                <p className="text-gray-600">{countryDetail.countryCode}</p>
              </div>
            </div>
          </div>

          {/* Health Score */}
          <HealthScoreCard score={metrics?.healthScore || 0} />

          {/* Employee Metrics */}
          <div className="bg-white rounded-lg shadow p-6">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">Employee Metrics</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
              <div>
                <p className="text-sm text-gray-600">Total Employees</p>
                <p className="text-2xl font-bold text-gray-900">{metrics?.totalEmployees || 0}</p>
              </div>
              <div>
                <p className="text-sm text-gray-600">Active Employees</p>
                <p className="text-2xl font-bold text-gray-900">{metrics?.activeEmployees || 0}</p>
              </div>
              <div>
                <p className="text-sm text-gray-600">New Hires (This Month)</p>
                <p className="text-2xl font-bold text-green-600">+{metrics?.newHires || 0}</p>
              </div>
              <div>
                <p className="text-sm text-gray-600">Turnover Rate</p>
                <p className="text-2xl font-bold text-gray-900">{metrics?.turnoverRate || 0}%</p>
              </div>
            </div>
          </div>

          {/* Financial Metrics */}
          <div className="bg-white rounded-lg shadow p-6">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">Financial Metrics</h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div>
                <p className="text-sm text-gray-600">Total Payroll</p>
                <p className="text-2xl font-bold text-gray-900">
                  {financialMetrics?.currency} {financialMetrics?.totalPayroll?.toLocaleString() || 0}
                </p>
              </div>
              <div>
                <p className="text-sm text-gray-600">Total Benefits</p>
                <p className="text-2xl font-bold text-gray-900">
                  {financialMetrics?.currency} {financialMetrics?.totalBenefits?.toLocaleString() || 0}
                </p>
              </div>
              <div>
                <p className="text-sm text-gray-600">Average Tenure</p>
                <p className="text-2xl font-bold text-gray-900">{metrics?.averageTenure || 0} years</p>
              </div>
            </div>
          </div>

          {/* Compliance Status */}
          <div className="bg-white rounded-lg shadow p-6">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">Compliance Status</h2>
            <div className="space-y-3">
              <div className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                <span className="text-sm font-medium text-gray-700">Overall Status</span>
                <span className={`px-3 py-1 text-sm font-medium rounded-full ${
                  compliance?.overallStatus === 'compliant' ? 'bg-green-100 text-green-800' :
                  compliance?.overallStatus === 'warning' ? 'bg-yellow-100 text-yellow-800' :
                  'bg-red-100 text-red-800'
                }`}>
                  {compliance?.overallStatus || 'Unknown'}
                </span>
              </div>
              {compliance?.byCategory && Object.entries(compliance.byCategory).map(([category, status]) => (
                <div key={category} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                  <span className="text-sm font-medium text-gray-700">
                    {category.replace(/([A-Z])/g, ' $1').replace(/^./, (str) => str.toUpperCase())}
                  </span>
                  <span className={`px-3 py-1 text-sm font-medium rounded-full ${
                    status === 'compliant' ? 'bg-green-100 text-green-800' :
                    status === 'warning' ? 'bg-yellow-100 text-yellow-800' :
                    'bg-red-100 text-red-800'
                  }`}>
                    {status}
                  </span>
                </div>
              ))}
            </div>
          </div>

          {/* Quick Actions */}
          <div className="bg-white rounded-lg shadow p-6">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">Quick Actions</h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <button className="px-4 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition">
                View Employees
              </button>
              <button className="px-4 py-3 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition">
                View Payroll Details
              </button>
              <button className="px-4 py-3 bg-gray-100 text-gray-700 rounded-lg hover:bg-gray-200 transition">
                View Compliance Reports
              </button>
            </div>
          </div>
        </div>
      </DashboardLayout>
    </ProtectedRoute>
  );
};

export default CountryDetailPage;

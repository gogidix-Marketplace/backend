'use client';

import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { ProtectedRoute } from '../../components/common/ProtectedRoute';
import { DashboardLayout } from '../../components/layout/DashboardLayout';
import { hrApi, PayrollSummary, CountryPayroll } from '../../api/hrApi';

const PayrollPage: React.FC = () => {
  const [selectedPeriod, setSelectedPeriod] = useState<'current' | 'previous'>('current');
  const [selectedCountry, setSelectedCountry] = useState<string>('all');

  const { data: payroll, isLoading } = useQuery({
    queryKey: ['global-payroll', selectedPeriod],
    queryFn: () => hrApi.getGlobalPayroll(),
  });

  const filteredCountries = payroll?.byCountry.filter((c) => {
    if (selectedCountry === 'all') return true;
    return c.countryCode === selectedCountry;
  }) || [];

  const getStatusColor = (status: string) => {
    switch (status.toLowerCase()) {
      case 'processed':
        return 'bg-green-100 text-green-800';
      case 'pending':
        return 'bg-yellow-100 text-yellow-800';
      case 'failed':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  };

  const getCountryFlag = (code: string) => {
    const flags: Record<string, string> = {
      US: '🇺🇸', GB: '🇬🇧', DE: '🇩🇪', FR: '🇫🇷', ES: '🇪🇸', IT: '🇮🇹',
      NL: '🇳🇱', PL: '🇵🇱', SE: '🇸🇪', NO: '🇳🇴', DK: '🇩🇰', FI: '🇫🇮',
    };
    return flags[code] || '🌍';
  };

  const formatCurrency = (amount: number, currency: string = 'USD') => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: currency,
    }).format(amount);
  };

  return (
    <ProtectedRoute>
      <DashboardLayout>
        <div className="space-y-6">
          {/* Header */}
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">Global Payroll</h1>
              <p className="text-gray-600">View and manage payroll across all countries</p>
            </div>
            <div className="flex items-center gap-4">
              <select
                value={selectedPeriod}
                onChange={(e) => setSelectedPeriod(e.target.value as any)}
                className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
              >
                <option value="current">Current Period</option>
                <option value="previous">Previous Period</option>
              </select>
              <button className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
                Export Report
              </button>
            </div>
          </div>

          {isLoading ? (
            <div className="flex items-center justify-center h-64">
              <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
            </div>
          ) : (
            <>
              {/* Summary Cards */}
              <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                <div className="bg-white rounded-lg shadow p-6">
                  <p className="text-sm text-gray-600">Total Payroll</p>
                  <p className="text-2xl font-bold text-gray-900">
                    {formatCurrency(payroll?.totalPayroll || 0)}
                  </p>
                </div>
                <div className="bg-white rounded-lg shadow p-6">
                  <p className="text-sm text-gray-600">Countries Covered</p>
                  <p className="text-2xl font-bold text-gray-900">{payroll?.totalCountries || 0}</p>
                </div>
                <div className="bg-white rounded-lg shadow p-6">
                  <p className="text-sm text-gray-600">Processed</p>
                  <p className="text-2xl font-bold text-green-600">
                    {filteredCountries.filter(c => c.status === 'processed').length}
                  </p>
                </div>
                <div className="bg-white rounded-lg shadow p-6">
                  <p className="text-sm text-gray-600">Pending</p>
                  <p className="text-2xl font-bold text-yellow-600">
                    {filteredCountries.filter(c => c.status === 'pending').length}
                  </p>
                </div>
              </div>

              {/* Payroll by Country */}
              <div className="bg-white rounded-lg shadow">
                <div className="p-6 border-b border-gray-200">
                  <div className="flex items-center justify-between">
                    <h2 className="text-lg font-semibold text-gray-900">Payroll by Country</h2>
                    <select
                      value={selectedCountry}
                      onChange={(e) => setSelectedCountry(e.target.value)}
                      className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                    >
                      <option value="all">All Countries</option>
                      {payroll?.byCountry.map((c) => (
                        <option key={c.countryCode} value={c.countryCode}>
                          {c.countryName}
                        </option>
                      ))}
                    </select>
                  </div>
                </div>

                <div className="overflow-x-auto">
                  <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                      <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                          Country
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                          Amount
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                          % of Total
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                          Status
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                          Actions
                        </th>
                      </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                      {filteredCountries.map((country) => {
                        const percentage = payroll?.totalPayroll
                          ? (country.amount / payroll.totalPayroll) * 100
                          : 0;
                        return (
                          <tr key={country.countryCode} className="hover:bg-gray-50">
                            <td className="px-6 py-4 whitespace-nowrap">
                              <div className="flex items-center">
                                <span className="text-xl mr-2">{getCountryFlag(country.countryCode)}</span>
                                <span className="text-sm font-medium text-gray-900">
                                  {country.countryName}
                                </span>
                                <span className="ml-2 text-sm text-gray-500">
                                  ({country.countryCode})
                                </span>
                              </div>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                              {formatCurrency(country.amount)}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <div className="flex items-center">
                                <div className="w-24 bg-gray-200 rounded-full h-2 mr-2">
                                  <div
                                    className="bg-blue-600 h-2 rounded-full"
                                    style={{ width: `${percentage}%` }}
                                  ></div>
                                </div>
                                <span className="text-sm text-gray-600">{percentage.toFixed(1)}%</span>
                              </div>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                              <span className={`px-2 py-1 text-xs font-medium rounded-full ${getStatusColor(country.status)}`}>
                                {country.status}
                              </span>
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap text-sm">
                              <button className="text-blue-600 hover:text-blue-900 font-medium">
                                View Details
                              </button>
                            </td>
                          </tr>
                        );
                      })}
                    </tbody>
                  </table>
                </div>
              </div>

              {/* Salary Benchmarking */}
              <div className="bg-white rounded-lg shadow p-6">
                <h2 className="text-lg font-semibold text-gray-900 mb-4">Salary Benchmarking</h2>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                  {[
                    { role: 'Software Engineer', market: '$95,000', ourAvg: '$98,000', diff: '+3%' },
                    { role: 'HR Manager', market: '$75,000', ourAvg: '$76,500', diff: '+2%' },
                    { role: 'Finance Analyst', market: '$70,000', ourAvg: '$68,000', diff: '-3%' },
                  ].map((item) => (
                    <div key={item.role} className="bg-gray-50 rounded-lg p-4">
                      <h3 className="font-medium text-gray-900">{item.role}</h3>
                      <div className="mt-3 space-y-2">
                        <div className="flex justify-between text-sm">
                          <span className="text-gray-600">Market Average</span>
                          <span className="font-medium">{item.market}</span>
                        </div>
                        <div className="flex justify-between text-sm">
                          <span className="text-gray-600">Our Average</span>
                          <span className="font-medium">{item.ourAvg}</span>
                        </div>
                        <div className="flex justify-between text-sm">
                          <span className="text-gray-600">Difference</span>
                          <span className={`font-medium ${
                            item.diff.startsWith('+') ? 'text-green-600' : 'text-red-600'
                          }`}>
                            {item.diff}
                          </span>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </>
          )}
        </div>
      </DashboardLayout>
    </ProtectedRoute>
  );
};

export default PayrollPage;

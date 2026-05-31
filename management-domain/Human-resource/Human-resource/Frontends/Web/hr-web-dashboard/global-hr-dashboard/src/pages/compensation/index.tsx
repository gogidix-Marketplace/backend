'use client';

import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { DashboardLayout } from '../../components/layout/GlobalDashboardLayout';
import { globalHrApi } from '../../api/globalHrApi';

const CompensationPage: React.FC = () => {
  const [selectedPeriod, setSelectedPeriod] = useState<'current' | 'previous'>('current');
  const [selectedCurrency, setSelectedCurrency] = useState<string>('USD');

  const { data: payroll } = useQuery({
    queryKey: ['global-payroll', selectedPeriod],
    queryFn: () => globalHrApi.getGlobalPayroll(selectedPeriod),
  });

  const { data: comparison } = useQuery({
    queryKey: ['payroll-comparison'],
    queryFn: () => globalHrApi.getPayrollComparison(),
  });

  return (
    <DashboardLayout>
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Global Compensation & Benefits</h1>
            <p className="text-gray-600">Worldwide payroll and compensation overview</p>
          </div>
          <div className="flex items-center gap-4">
            <select
              value={selectedPeriod}
              onChange={(e) => setSelectedPeriod(e.target.value as any)}
              className="px-4 py-2 border border-gray-300 rounded-lg"
            >
              <option value="current">Current Period</option>
              <option value="previous">Previous Period</option>
            </select>
            <button className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
              Export Report
            </button>
          </div>
        </div>

        {/* Summary Cards */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div className="bg-white rounded-lg shadow p-6">
            <p className="text-sm text-gray-600">Total Payroll (USD)</p>
            <p className="text-2xl font-bold text-gray-900">
              ${((payroll?.totalPayrollUSD || 0) / 1000000).toFixed(1)}M
            </p>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <p className="text-sm text-gray-600">Processed</p>
            <p className="text-2xl font-bold text-green-600">
              {comparison?.filter(c => c.status === 'processed').length || 0}
            </p>
            <p className="text-xs text-gray-500">countries</p>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <p className="text-sm text-gray-600">Pending</p>
            <p className="text-2xl font-bold text-yellow-600">
              {comparison?.filter(c => c.status === 'pending').length || 0}
            </p>
            <p className="text-xs text-gray-500">countries</p>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <p className="text-sm text-gray-600">Avg Cost/Employee</p>
            <p className="text-2xl font-bold text-gray-900">
              ${Math.round((payroll?.totalPayrollUSD || 0) / 1000).toLocaleString()}
            </p>
          </div>
        </div>

        {/* Currency Breakdown */}
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Payroll by Currency</h2>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            {payroll?.currencyBreakdown.map((currency) => (
              <div key={currency.currency} className="bg-gray-50 rounded-lg p-4">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-lg font-bold text-gray-900">{currency.currency}</span>
                  <span className="text-sm text-gray-500">{currency.percentage.toFixed(1)}%</span>
                </div>
                <p className="text-xl font-semibold text-gray-900">
                  {currency.currency === 'USD' ? '$' : currency.currency === 'EUR' ? '€' : '£'}
                  {(currency.amountUSD / 1000000).toFixed(2)}M
                </p>
                <p className="text-xs text-gray-500">USD equivalent</p>
              </div>
            ))}
          </div>
        </div>

        {/* Payroll by Country */}
        <div className="bg-white rounded-lg shadow overflow-hidden">
          <div className="p-6 border-b border-gray-200">
            <h2 className="text-lg font-semibold text-gray-900">Payroll by Country</h2>
          </div>
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Country</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Local Amount</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">USD Equivalent</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Variance</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {comparison?.map((country) => (
                <tr key={country.countryCode} className="hover:bg-gray-50">
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    {country.countryName}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    {country.currency} {country.amount.toLocaleString()}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    ${country.amountUSD.toLocaleString()}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`text-sm font-semibold ${country.variance >= -5 && country.variance <= 5 ? 'text-green-600' : 'text-yellow-600'}`}>
                      {country.variance >= 0 ? '+' : ''}{country.variance.toFixed(1)}%
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-2 py-1 text-xs font-medium rounded-full ${
                      country.status === 'processed' ? 'bg-green-100 text-green-800' :
                      country.status === 'processing' ? 'bg-yellow-100 text-yellow-800' :
                      'bg-gray-100 text-gray-800'
                    }`}>
                      {country.status}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Payroll Trend */}
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Monthly Payroll Trend</h2>
          <div className="h-64 flex items-center justify-center bg-gray-50 rounded-lg">
            <div className="text-center text-gray-500">
              <svg className="h-12 w-12 mx-auto mb-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M7 12l3-3 3 3 3 4-4M8 21l4-4 4 4M3 4h18M4 4h16v12a1 1 0 001 1H5a1 1 0 01-1-1V4z" />
              </svg>
              <p>Payroll trend chart visualization</p>
              <p className="text-xs mt-1">Last 12 months by currency</p>
            </div>
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default CompensationPage;

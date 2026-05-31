'use client';

import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { useRouter } from 'next/router';
import { hrApi, Country } from '../../api/hrApi';
import { ProtectedRoute } from '../../components/common/ProtectedRoute';
import { DashboardLayout } from '../../components/layout/DashboardLayout';

const CountriesPage: React.FC = () => {
  const router = useRouter();
  const [selectedRegion, setSelectedRegion] = useState<string>('all');
  const [sortBy, setSortBy] = useState<'name' | 'employees' | 'healthScore'>('name');
  const [searchQuery, setSearchQuery] = useState('');

  const { data: countries, isLoading } = useQuery({
    queryKey: ['countries'],
    queryFn: () => hrApi.getCountries(),
  });

  const regions = ['all', 'Europe', 'Americas', 'Asia Pacific', 'Middle East & Africa'];

  const filteredCountries = countries
    ?.filter((country) => {
      const matchesSearch =
        country.countryName.toLowerCase().includes(searchQuery.toLowerCase()) ||
        country.countryCode.toLowerCase().includes(searchQuery.toLowerCase());
      // In real app, would filter by region
      return matchesSearch;
    })
    .sort((a, b) => {
      switch (sortBy) {
        case 'employees':
          return b.employeeCount - a.employeeCount;
        case 'healthScore':
          return b.healthScore - a.healthScore;
        default:
          return a.countryName.localeCompare(b.countryName);
      }
    });

  const getHealthScoreColor = (score: number) => {
    if (score >= 80) return 'bg-green-100 text-green-800';
    if (score >= 60) return 'bg-yellow-100 text-yellow-800';
    return 'bg-red-100 text-red-800';
  };

  const getStatusColor = (status: string) => {
    switch (status.toLowerCase()) {
      case 'active':
        return 'bg-green-100 text-green-800';
      case 'warning':
        return 'bg-yellow-100 text-yellow-800';
      case 'critical':
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

  if (isLoading) {
    return (
      <DashboardLayout>
        <div className="flex items-center justify-center h-64">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>
      </DashboardLayout>
    );
  }

  return (
    <ProtectedRoute>
      <DashboardLayout>
        <div className="space-y-6">
          {/* Header */}
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">Countries Overview</h1>
              <p className="text-gray-600">Compare HR metrics across all countries</p>
            </div>
          </div>

          {/* Filters */}
          <div className="bg-white rounded-lg shadow p-4">
            <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
              {/* Search */}
              <div className="flex-1">
                <input
                  type="text"
                  placeholder="Search countries..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                />
              </div>

              {/* Filters */}
              <div className="flex items-center gap-4">
                <select
                  value={selectedRegion}
                  onChange={(e) => setSelectedRegion(e.target.value)}
                  className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                >
                  {regions.map((region) => (
                    <option key={region} value={region}>
                      {region === 'all' ? 'All Regions' : region}
                    </option>
                  ))}
                </select>

                <select
                  value={sortBy}
                  onChange={(e) => setSortBy(e.target.value as any)}
                  className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                >
                  <option value="name">Sort by Name</option>
                  <option value="employees">Sort by Employees</option>
                  <option value="healthScore">Sort by Health Score</option>
                </select>
              </div>
            </div>
          </div>

          {/* Summary Cards */}
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <div className="bg-white rounded-lg shadow p-4">
              <p className="text-sm text-gray-600">Total Countries</p>
              <p className="text-2xl font-bold text-gray-900">{countries?.length || 0}</p>
            </div>
            <div className="bg-white rounded-lg shadow p-4">
              <p className="text-sm text-gray-600">Total Employees</p>
              <p className="text-2xl font-bold text-gray-900">
                {countries?.reduce((sum, c) => sum + c.employeeCount, 0).toLocaleString() || 0}
              </p>
            </div>
            <div className="bg-white rounded-lg shadow p-4">
              <p className="text-sm text-gray-600">Avg Health Score</p>
              <p className="text-2xl font-bold text-gray-900">
                {countries
                  ? Math.round(countries.reduce((sum, c) => sum + c.healthScore, 0) / countries.length)
                  : 0}
                %
              </p>
            </div>
            <div className="bg-white rounded-lg shadow p-4">
              <p className="text-sm text-gray-600">Total Payroll</p>
              <p className="text-2xl font-bold text-gray-900">
                ${countries?.reduce((sum, c) => sum + c.payrollAmount, 0).toLocaleString() || 0}
              </p>
            </div>
          </div>

          {/* Countries Table */}
          <div className="bg-white rounded-lg shadow overflow-hidden">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Country
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Status
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Employees
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Health Score
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Payroll
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                    Actions
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {filteredCountries?.map((country) => (
                  <tr key={country.countryCode} className="hover:bg-gray-50 cursor-pointer">
                    <td
                      className="px-6 py-4 whitespace-nowrap"
                      onClick={() => router.push(`/countries/${country.countryCode}`)}
                    >
                      <div className="flex items-center">
                        <span className="text-2xl mr-3">{getCountryFlag(country.countryCode)}</span>
                        <div>
                          <div className="text-sm font-medium text-gray-900">{country.countryName}</div>
                          <div className="text-sm text-gray-500">{country.countryCode}</div>
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`px-2 py-1 text-xs font-medium rounded-full ${getStatusColor(country.status)}`}>
                        {country.status}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {country.employeeCount.toLocaleString()}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`px-2 py-1 text-xs font-medium rounded-full ${getHealthScoreColor(country.healthScore)}`}>
                        {country.healthScore}%
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      ${country.payrollAmount.toLocaleString()}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm">
                      <button
                        onClick={() => router.push(`/countries/${country.countryCode}`)}
                        className="text-blue-600 hover:text-blue-900 font-medium"
                      >
                        View Details
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>

            {filteredCountries?.length === 0 && (
              <div className="text-center py-12">
                <p className="text-gray-500">No countries found matching your filters.</p>
              </div>
            )}
          </div>
        </div>
      </DashboardLayout>
    </ProtectedRoute>
  );
};

export default CountriesPage;

'use client';

import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { useRouter } from 'next/router';
import { globalHrApi, CountryComparison } from '../../api/globalHrApi';
import { DashboardLayout } from '../../components/layout/GlobalDashboardLayout';

const CountriesPage: React.FC = () => {
  const router = useRouter();
  const [selectedRegion, setSelectedRegion] = useState<string>('all');
  const [sortBy, setSortBy] = useState<'name' | 'headcount' | 'healthScore' | 'growth'>('healthScore');
  const [searchQuery, setSearchQuery] = useState('');

  const { data: countries, isLoading } = useQuery({
    queryKey: ['countries-comparison', selectedRegion, sortBy],
    queryFn: () => globalHrApi.getCountriesComparison({
      regions: selectedRegion === 'all' ? undefined : [selectedRegion],
    }),
  });

  const filteredAndSortedCountries = countries
    ?.filter((country) => {
      const matchesSearch =
        country.countryName.toLowerCase().includes(searchQuery.toLowerCase()) ||
        country.countryCode.toLowerCase().includes(searchQuery.toLowerCase());
      return matchesSearch;
    })
    .sort((a, b) => {
      switch (sortBy) {
        case 'name':
          return a.countryName.localeCompare(b.countryName);
        case 'headcount':
          return b.headcount - a.headcount;
        case 'healthScore':
          return b.healthScore - a.healthScore;
        case 'growth':
          return b.growth - a.growth;
        default:
          return 0;
      }
    }) || [];

  const getHealthScoreColor = (score: number) => {
    if (score >= 90) return 'bg-green-100 text-green-800';
    if (score >= 75) return 'bg-blue-100 text-blue-800';
    if (score >= 60) return 'bg-yellow-100 text-yellow-800';
    return 'bg-red-100 text-red-800';
  };

  const getComplianceColor = (status: string) => {
    switch (status) {
      case 'compliant': return 'bg-green-100 text-green-800';
      case 'warning': return 'bg-yellow-100 text-yellow-800';
      case 'critical': return 'bg-red-100 text-red-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  };

  const flags: Record<string, string> = {
    NG: '🇳🇬', KE: '🇰🇪', ZA: '🇿🇦', GH: '🇬🇭', EG: '🇪🇬',
    GB: '🇬🇧', IE: '🇮🇪', DE: '🇩🇪', FR: '🇫🇷', NL: '🇳🇱',
    US: '🇺🇸', CA: '🇨🇦', BR: '🇧🇷', MX: '🇲🇽',
    JP: '🇯🇵', SG: '🇸🇬', IN: '🇮🇳', MY: '🇲🇾', TH: '🇹🇭',
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
    <DashboardLayout>
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Countries Comparison</h1>
            <p className="text-gray-600">Compare HR metrics across all operating countries</p>
          </div>
        </div>

        {/* Filters */}
        <div className="bg-white rounded-lg shadow p-4">
          <div className="flex flex-col md:flex-row gap-4">
            <input
              type="text"
              placeholder="Search countries..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
            />
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
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value as any)}
              className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
            >
              <option value="name">Sort by Name</option>
              <option value="headcount">Sort by Headcount</option>
              <option value="healthScore">Sort by Health Score</option>
              <option value="growth">Sort by Growth</option>
            </select>
          </div>
        </div>

        {/* Comparison Table */}
        <div className="bg-white rounded-lg shadow overflow-hidden">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Country</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Region</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Headcount</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Growth</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Health Score</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Payroll (USD)</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Performance</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Compliance</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Actions</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {filteredAndSortedCountries.map((country) => (
                <tr key={country.countryCode} className="hover:bg-gray-50 cursor-pointer">
                  <td className="px-6 py-4 whitespace-nowrap">
                    <div className="flex items-center">
                      <span className="text-2xl mr-3">{flags[country.countryCode] || '🌍'}</span>
                      <div>
                        <div className="text-sm font-medium text-gray-900">{country.countryName}</div>
                        <div className="text-sm text-gray-500">{country.countryCode}</div>
                      </div>
                    </div>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-600">{country.region}</td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{country.headcount.toLocaleString()}</td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`text-sm font-semibold ${country.growth >= 0 ? 'text-green-600' : 'text-red-600'}`}>
                      {country.growth >= 0 ? '+' : ''}{country.growth.toFixed(1)}%
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-2 py-1 text-xs font-medium rounded-full ${getHealthScoreColor(country.healthScore)}`}>
                      {country.healthScore}/100
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    ${(country.payroll / 1000000).toFixed(1)}M
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{country.performanceRating.toFixed(1)}/5.0</td>
                  <td className="px-6 py-4 whitespace-nowrap">
                    <span className={`px-2 py-1 text-xs font-medium rounded-full ${getComplianceColor(country.complianceStatus)}`}>
                      {country.complianceStatus}
                    </span>
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm">
                    <button className="text-blue-600 hover:text-blue-900 font-medium">View Details</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default CountriesPage;

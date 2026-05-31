'use client';

import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { ProtectedRoute } from '../../components/common/ProtectedRoute';
import { DashboardLayout } from '../../components/layout/DashboardLayout';
import { hrApi } from '../../api/hrApi';

interface Report {
  id: string;
  name: string;
  description: string;
  category: string;
  frequency: 'on-demand' | 'daily' | 'weekly' | 'monthly' | 'quarterly';
  lastRun?: string;
  nextRun?: string;
  createdBy: string;
  format: 'pdf' | 'excel' | 'csv';
}

const ReportsPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'list' | 'generate' | 'scheduled'>('list');
  const [selectedCategory, setSelectedCategory] = useState<string>('all');
  const [searchQuery, setSearchQuery] = useState('');

  const { data: reports, isLoading } = useQuery({
    queryKey: ['reports'],
    queryFn: () => hrApi.getReports(),
  });

  const categories = ['all', 'workforce', 'payroll', 'performance', 'recruitment', 'compliance'];

  const filteredReports = reports?.filter((report: Report) => {
    const matchesCategory = selectedCategory === 'all' || report.category === selectedCategory;
    const matchesSearch = report.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      report.description.toLowerCase().includes(searchQuery.toLowerCase());
    return matchesCategory && matchesSearch;
  }) || [];

  const getCategoryColor = (category: string) => {
    const colors: Record<string, string> = {
      workforce: 'bg-blue-100 text-blue-800',
      payroll: 'bg-green-100 text-green-800',
      performance: 'bg-purple-100 text-purple-800',
      recruitment: 'bg-orange-100 text-orange-800',
      compliance: 'bg-red-100 text-red-800',
    };
    return colors[category] || 'bg-gray-100 text-gray-800';
  };

  const getFormatIcon = (format: string) => {
    switch (format) {
      case 'pdf':
        return <svg className="h-5 w-5 text-red-600" fill="currentColor" viewBox="0 0 20 20">
          <path fillRule="evenodd" d="M4 4a2 2 0 012-2h4.586A2 2 0 0112 2.586L15.414 6A2 2 0 0116 7.414V16a2 2 0 01-2 2H6a2 2 0 01-2-2V4z" clipRule="evenodd" />
        </svg>;
      case 'excel':
        return <svg className="h-5 w-5 text-green-600" fill="currentColor" viewBox="0 0 20 20">
          <path fillRule="evenodd" d="M4 4a2 2 0 012-2h4.586A2 2 0 0112 2.586L15.414 6A2 2 0 0116 7.414V16a2 2 0 01-2 2H6a2 2 0 01-2-2V4z" clipRule="evenodd" />
        </svg>;
      case 'csv':
        return <svg className="h-5 w-5 text-blue-600" fill="currentColor" viewBox="0 0 20 20">
          <path fillRule="evenodd" d="M4 4a2 2 0 012-2h4.586A2 2 0 0112 2.586L15.414 6A2 2 0 0116 7.414V16a2 2 0 01-2 2H6a2 2 0 01-2-2V4z" clipRule="evenodd" />
        </svg>;
      default:
        return null;
    }
  };

  return (
    <ProtectedRoute>
      <DashboardLayout>
        <div className="space-y-6">
          {/* Header */}
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">Reports</h1>
              <p className="text-gray-600">Generate and schedule HR reports</p>
            </div>
            <button
              onClick={() => setActiveTab('generate')}
              className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
            >
              + Create Report
            </button>
          </div>

          {/* Tabs */}
          <div className="bg-white rounded-lg shadow">
            <div className="border-b border-gray-200">
              <nav className="flex -mb-px">
                {[
                  { id: 'list', label: 'Report Library' },
                  { id: 'generate', label: 'Generate Report' },
                  { id: 'scheduled', label: 'Scheduled Reports' },
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
              {activeTab === 'list' && (
                <div>
                  {/* Filters */}
                  <div className="flex items-center gap-4 mb-6">
                    <input
                      type="text"
                      placeholder="Search reports..."
                      value={searchQuery}
                      onChange={(e) => setSearchQuery(e.target.value)}
                      className="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                    />
                    <select
                      value={selectedCategory}
                      onChange={(e) => setSelectedCategory(e.target.value)}
                      className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                    >
                      {categories.map((cat) => (
                        <option key={cat} value={cat}>
                          {cat === 'all' ? 'All Categories' : cat.charAt(0).toUpperCase() + cat.slice(1)}
                        </option>
                      ))}
                    </select>
                  </div>

                  {/* Reports Grid */}
                  {isLoading ? (
                    <div className="flex items-center justify-center h-64">
                      <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
                    </div>
                  ) : (
                    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                      {filteredReports.map((report: Report) => (
                        <div key={report.id} className="bg-gray-50 rounded-lg p-4 hover:shadow-md transition">
                          <div className="flex items-start justify-between mb-3">
                            <div className="flex items-center">
                              {getFormatIcon(report.format)}
                              <span className={`ml-2 px-2 py-1 text-xs font-medium rounded-full ${getCategoryColor(report.category)}`}>
                                {report.category}
                              </span>
                            </div>
                          </div>
                          <h3 className="font-semibold text-gray-900">{report.name}</h3>
                          <p className="text-sm text-gray-600 mt-1">{report.description}</p>
                          <div className="mt-4 flex items-center justify-between">
                            <span className="text-xs text-gray-500">
                              {report.frequency === 'on-demand' ? 'On demand' : `${report.frequency}`}
                            </span>
                            <div className="flex gap-2">
                              <button className="px-3 py-1 text-xs font-medium text-blue-600 hover:bg-blue-50 rounded">
                                Run
                              </button>
                              <button className="px-3 py-1 text-xs font-medium text-gray-600 hover:bg-gray-100 rounded">
                                Schedule
                              </button>
                            </div>
                          </div>
                        </div>
                      ))}
                    </div>
                  )}

                  {filteredReports.length === 0 && !isLoading && (
                    <div className="text-center py-12">
                      <p className="text-gray-500">No reports found matching your criteria.</p>
                    </div>
                  )}
                </div>
              )}

              {activeTab === 'generate' && (
                <div className="max-w-2xl mx-auto">
                  <h3 className="text-lg font-semibold text-gray-900 mb-6">Generate New Report</h3>

                  <form className="space-y-6">
                    {/* Report Type */}
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        Report Type
                      </label>
                      <select className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500">
                        <option value="">Select report type...</option>
                        <option value="workforce-summary">Workforce Summary</option>
                        <option value="payroll-detail">Payroll Detail</option>
                        <option value="performance-review">Performance Review</option>
                        <option value="recruitment-pipeline">Recruitment Pipeline</option>
                        <option value="compliance-status">Compliance Status</option>
                        <option value="turnover-analysis">Turnover Analysis</option>
                      </select>
                    </div>

                    {/* Date Range */}
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        Date Range
                      </label>
                      <div className="grid grid-cols-2 gap-4">
                        <div>
                          <label className="block text-xs text-gray-500 mb-1">From</label>
                          <input
                            type="date"
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                          />
                        </div>
                        <div>
                          <label className="block text-xs text-gray-500 mb-1">To</label>
                          <input
                            type="date"
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                          />
                        </div>
                      </div>
                    </div>

                    {/* Filters */}
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        Filters
                      </label>
                      <div className="space-y-3">
                        <select className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500">
                          <option value="all">All Countries</option>
                          <option value="us">United States</option>
                          <option value="de">Germany</option>
                          <option value="fr">France</option>
                          <option value="gb">United Kingdom</option>
                        </select>
                        <select className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500">
                          <option value="all">All Departments</option>
                          <option value="engineering">Engineering</option>
                          <option value="sales">Sales</option>
                          <option value="marketing">Marketing</option>
                          <option value="hr">Human Resources</option>
                        </select>
                      </div>
                    </div>

                    {/* Output Format */}
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        Output Format
                      </label>
                      <div className="flex gap-4">
                        {['pdf', 'excel', 'csv'].map((format) => (
                          <label key={format} className="flex items-center">
                            <input
                              type="radio"
                              name="format"
                              value={format}
                              className="mr-2"
                            />
                            <span className="uppercase text-sm text-gray-700">{format}</span>
                          </label>
                        ))}
                      </div>
                    </div>

                    {/* Actions */}
                    <div className="flex justify-end gap-4">
                      <button
                        type="button"
                        onClick={() => setActiveTab('list')}
                        className="px-6 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50"
                      >
                        Cancel
                      </button>
                      <button
                        type="submit"
                        className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
                      >
                        Generate Report
                      </button>
                    </div>
                  </form>
                </div>
              )}

              {activeTab === 'scheduled' && (
                <div>
                  <h3 className="text-lg font-semibold text-gray-900 mb-6">Scheduled Reports</h3>

                  <div className="space-y-4">
                    {[
                      {
                        name: 'Weekly Payroll Summary',
                        frequency: 'Weekly',
                        nextRun: '2024-03-04',
                        recipients: 5,
                        format: 'pdf',
                      },
                      {
                        name: 'Monthly Workforce Report',
                        frequency: 'Monthly',
                        nextRun: '2024-03-31',
                        recipients: 12,
                        format: 'excel',
                      },
                      {
                        name: 'Quarterly Compliance Review',
                        frequency: 'Quarterly',
                        nextRun: '2024-03-31',
                        recipients: 8,
                        format: 'pdf',
                      },
                    ].map((report, index) => (
                      <div key={index} className="bg-gray-50 rounded-lg p-4 flex items-center justify-between">
                        <div className="flex items-center gap-4">
                          {getFormatIcon(report.format)}
                          <div>
                            <h4 className="font-medium text-gray-900">{report.name}</h4>
                            <p className="text-sm text-gray-600">
                              {report.frequency} • Next run: {report.nextRun} • {report.recipients} recipients
                            </p>
                          </div>
                        </div>
                        <div className="flex gap-2">
                          <button className="p-2 text-gray-400 hover:text-gray-600">
                            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                            </svg>
                          </button>
                          <button className="p-2 text-gray-400 hover:text-red-600">
                            <svg className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                            </svg>
                          </button>
                        </div>
                      </div>
                    ))}
                  </div>

                  <div className="mt-6 text-center">
                    <button className="text-blue-600 hover:text-blue-700 font-medium">
                      + Schedule New Report
                    </button>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      </DashboardLayout>
    </ProtectedRoute>
  );
};

export default ReportsPage;

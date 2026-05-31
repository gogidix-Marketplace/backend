'use client';

import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { DashboardLayout } from '../../components/layout/GlobalDashboardLayout';
import { globalHrApi } from '../../api/globalHrApi';

const ReportsPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState<'library' | 'scheduled' | 'history'>('library');

  const { data: reports } = useQuery({
    queryKey: ['reports'],
    queryFn: () => globalHrApi.getReports(),
  });

  const getCategoryColor = (category: string) => {
    const colors: Record<string, string> = {
      workforce: 'bg-blue-100 text-blue-800',
      recruitment: 'bg-green-100 text-green-800',
      payroll: 'bg-purple-100 text-purple-800',
      performance: 'bg-orange-100 text-orange-800',
      compliance: 'bg-red-100 text-red-800',
      executive: 'bg-gray-800 text-white',
    };
    return colors[category] || 'bg-gray-100 text-gray-800';
  };

  return (
    <DashboardLayout>
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Global Reports</h1>
            <p className="text-gray-600">Generate and schedule HQ-level reports</p>
          </div>
          <button className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700">
            + Create New Report
          </button>
        </div>

        {/* Report Stats */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div className="bg-white rounded-lg shadow p-4">
            <p className="text-sm text-gray-600">Total Reports</p>
            <p className="text-2xl font-bold text-gray-900">{reports?.length || 0}</p>
          </div>
          <div className="bg-white rounded-lg shadow p-4">
            <p className="text-sm text-gray-600">Scheduled</p>
            <p className="text-2xl font-bold text-blue-600">{reports?.filter(r => r.frequency !== 'on-demand').length || 0}</p>
          </div>
          <div className="bg-white rounded-lg shadow p-4">
            <p className="text-sm text-gray-600">Last Run Today</p>
            <p className="text-2xl font-bold text-green-600">8</p>
          </div>
          <div className="bg-white rounded-lg shadow p-4">
            <p className="text-sm text-gray-600">Pending</p>
            <p className="text-2xl font-bold text-yellow-600">2</p>
          </div>
        </div>

        {/* Tabs */}
        <div className="bg-white rounded-lg shadow">
          <div className="border-b border-gray-200">
            <nav className="flex -mb-px">
              {[
                { id: 'library', label: 'Report Library' },
                { id: 'scheduled', label: 'Scheduled Reports' },
                { id: 'history', label: 'Generation History' },
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
            {activeTab === 'library' && (
              <div>
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                  {reports?.map((report) => (
                    <div key={report.id} className="bg-gray-50 rounded-lg p-4 hover:shadow-md transition">
                      <div className="flex items-start justify-between mb-3">
                        <span className={`px-2 py-1 text-xs font-medium rounded-full ${getCategoryColor(report.category)}`}>
                          {report.category}
                        </span>
                        <span className="text-xs text-gray-500">{report.frequency}</span>
                      </div>
                      <h3 className="font-semibold text-gray-900">{report.name}</h3>
                      <p className="text-sm text-gray-600 mt-1">{report.description}</p>
                      <div className="mt-4 flex items-center justify-between">
                        <span className="text-xs text-gray-500">Format: {report.format}</span>
                        <div className="flex gap-2">
                          <button className="px-3 py-1 text-xs font-medium text-blue-600 hover:bg-blue-50 rounded">
                            Run Now
                          </button>
                          <button className="px-3 py-1 text-xs font-medium text-gray-600 hover:bg-gray-100 rounded">
                            Schedule
                          </button>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {activeTab === 'scheduled' && (
              <div>
                <div className="space-y-3">
                  {reports?.filter(r => r.frequency !== 'on-demand').map((report) => (
                    <div key={report.id} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                      <div className="flex-1">
                        <div className="flex items-center gap-3">
                          <span className={`px-2 py-1 text-xs font-medium rounded-full ${getCategoryColor(report.category)}`}>
                            {report.category}
                          </span>
                          <h3 className="font-medium text-gray-900">{report.name}</h3>
                        </div>
                        <p className="text-sm text-gray-600 mt-1">
                          Frequency: {report.frequency} • Format: {report.format}
                        </p>
                        {report.nextRun && (
                          <p className="text-xs text-gray-500 mt-1">Next run: {report.nextRun}</p>
                        )}
                      </div>
                      <div className="flex gap-2">
                        <button className="px-3 py-1 text-xs font-medium text-gray-600 hover:bg-gray-100 rounded">
                          Edit
                        </button>
                        <button className="px-3 py-1 text-xs font-medium text-red-600 hover:bg-red-50 rounded">
                          Disable
                        </button>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {activeTab === 'history' && (
              <div>
                <div className="text-center py-12 text-gray-500">
                  <svg className="h-12 w-12 mx-auto mb-2 text-gray-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2 2V5a2 2 0 012-2h5.586a1 1 0 001.414.414l5.414 5.414a1 1 0 001.414 0z" />
                  </svg>
                  <p>No report history available</p>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </DashboardLayout>
  );
};

export default ReportsPage;
